package model;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import common.DBConnection;

@WebServlet("/UserProfile")
public class UserProfile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATEMENT_SELECT = "SELECT * FROM USER_COMMENT_ITEM_ U join ITEM_ I on U.ITEM_ID = I.ID where U.USER_ID = ?";
    public UserProfile() {
        super();
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
 	   doPost(request,response);	
 	}

 	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       VelocityEngine ve = common.VelocityEngineObject.getVelocityEngine();
 		
       ve.setProperty("input.encoding", "UTF-8");
       ve.setProperty("output.encoding", "UTF-8");
        	   
       StringWriter writer = new StringWriter();
 	   Template template = null;
 	   VelocityContext context = new VelocityContext();
 	   
 	   String userId = (String) request.getSession().getAttribute("user_id");
 	   System.out.println("vasea loh");
 	   System.out.println("user ul logat e "+userId);
 	   Connection connection = DBConnection.getConnection(); 
 	   PreparedStatement st = null;
 	   
 	   ArrayList list = new ArrayList();

 	   try {
		st = (PreparedStatement) connection.prepareStatement(STATEMENT_SELECT);
 	   	st.setString(1, userId);
 	   } catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
 	   }
 	   
	   try {
		ResultSet resultSet = st.executeQuery();
		while (resultSet.next())
		{
			String rating = resultSet.getString(5);
			String name =  resultSet.getString(10);
			String description = resultSet.getString(15);
			System.out.println(rating+" "+description);
			
			Map map = new HashMap();
			map.put("rating", rating);
			map.put("name", name);
			map.put("description", description);
			list.add(map);
		}
		
		
	} catch (SQLException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}

 	 
	    context.put("activity", list);
 		
 		
 		
 		context.put("encrypt_user_profile",DigestUtils.sha256Hex("4"));
 		context.put("name", (String) request.getSession().getAttribute("name"));
 		System.out.println((String) request.getSession().getAttribute("name"));
 		
 		context.put("urlImage",(String) request.getSession().getAttribute("urlImage"));
 		context.put("encrypt_js", DigestUtils.sha256Hex("5"));
 		template = ve.getTemplate("user_profile.html");        
 		
 		template.merge( context, writer );
 		response.setCharacterEncoding("UTF-8");
        response.getWriter().println(writer.toString());
 	}

}
