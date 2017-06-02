package model;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import common.VelocityEngineObject;

@WebServlet("/Product")
public class Product extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATEMENT = "select * from USER_COMMENT_ITEM_ c join USER_ u on u.id = c.user_id ";
	public Product()
	{
		super();
	}
	
	 protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	 	   doPost(request,response);	
	 }
	 
	 protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 VelocityEngine ve = common.VelocityEngineObject.getVelocityEngine();
	 		
		 StringWriter writer = new StringWriter();
	 	 Template template = null;
	 	 VelocityContext context = new VelocityContext();	
	 	 
	 	 String id_product = request.getParameter("id_product");
	 	 if (id_product==null) id_product="0";

	     String sha256hex = DigestUtils.sha256Hex("2");
	 	 context.put("encrypt_main_page",DigestUtils.sha256Hex("2"));
	 	 context.put("encrypt_bootstrap_social", DigestUtils.sha256Hex("3"));
	 	 context.put("id_product", id_product);
	 	 
	 	 template = ve.getTemplate("product_container.html");
	    // response.getWriter().println(writer.toString()); 
	     
	     //Starting getting user comments 
	     
	     Connection connection = DBConnection.getConnection();
	     ArrayList list = new ArrayList();
	     PreparedStatement st = null;
	     
	     try {
		 st = (PreparedStatement) connection.prepareStatement(STATEMENT);
		 
	     } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	    try {
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()){
				UserCommentData commentData;
				String userID = resultSet.getString(2);
				String comment = resultSet.getString(4);
				String rating = resultSet.getString(5);;
				String userImage = resultSet.getString(9);
				String userName = resultSet.getString(10);
				System.out.println(userID+" "+comment+" "+userImage+" "+userName);
			
				Map map = new HashMap();
				map.put("user_id", userID);
				map.put("comment", comment);
				map.put("userName", userName);
				map.put("userImage", userImage);
				map.put("rating", rating);
				list.add(map);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    for (int i=0; i<list.size(); ++i)
	    {
	  //  	System.out.println(list.get(i).getComment()+" "+list.get(i).getUserName());
	    }
	    
	    context.put("comments", list);
	    template.merge( context, writer );
	    response.getWriter().println(writer.toString()); 
	 }
}
