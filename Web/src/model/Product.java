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
	private static final String STATEMENT = "select * from USER_COMMENT_ITEM_ c join USER_ u on u.id = c.user_id";
	private static final String STATEMENT1 = "select * from USER_COMMENT_LIKE";
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
	    
	     
	     String canAdd = "true";
	    try {
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()){

				String commentId = resultSet.getString(1);
				String userID = resultSet.getString(2);
				String comment = resultSet.getString(4);
				String rating = resultSet.getString(5);
				String tags = resultSet.getString(6);
				String userImage = resultSet.getString(10);
				String userName = resultSet.getString(11);
			//	System.out.println(tags+" "+rating+" "+commentId+" "+userID+" "+comment+" "+userImage+" "+userName);
				Map map = new HashMap();
				map.put("id", commentId);
				map.put("user_id", userID);
				map.put("comment", comment);
				map.put("userName", userName);
				map.put("userImage", userImage);
				map.put("rating", rating);
				map.put("tags", tags);
				
				String valueOfLike="";
				
				 try {
						st = (PreparedStatement) connection.prepareStatement(STATEMENT1);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					ResultSet resultSet1 = null;
					try {
						resultSet1 = st.executeQuery();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						while (resultSet1.next()){
								String user_id1 = (String) request.getSession().getAttribute("user_id");
								String commentID = resultSet1.getString(3);
								System.out.println("current comm_id "+commentId+" aux comm_id"+commentID);
								if (commentID.equals(commentId) && !isEqual(user_id1, userID))
								{
								
									valueOfLike = resultSet1.getString(4);
									System.out.println("++++"+valueOfLike);
									System.out.println(commentID+" "+commentId+" "+user_id1+" "+userID);
									break;
								}
						
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				
				String canEdit = "false";
				if (userID!=null){
				if (userID.equals(request.getSession().getAttribute("user_id")))
				{
						canEdit = "true";
						canAdd = "false";
				}
				}
				map.put("edit",canEdit);
				System.out.println("lala"+valueOfLike);
				if (valueOfLike.equals("like"))
				{
					System.out.println("aici");
					map.put("like_value", "red");
				}
				else
					if (valueOfLike.equals("dislike"))
					{
						System.out.println("aic2");
						map.put("like_value", "blue");
					}
					else
					{
						System.out.println("aic3");
						map.put("like_value", "black");	
					}
				list.add(map);
				System.out.println("lal"+map.get("like_value"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	   	    
	    context.put("comments", list);
	    context.put("canAdd", canAdd);
	    String loggedUserID = (String) request.getSession().getAttribute("user_id");
	    System.out.println("user care ii logat"+loggedUserID);
	    context.put("logged_user", loggedUserID);
	    template.merge( context, writer );
	    response.getWriter().println(writer.toString()); 
	 }
	 
	 private boolean isEqual(String s1, String s2)
	 {
		 if (s1 == null || s2==null)
		 {
			 return  false;
		 }
		 
		 for (int i=0; i<Math.min(s1.length(),13); ++i)
		 {
			 if (s1.charAt(i)!=s2.charAt(i))
			 {
				 return false;
			 }
		 }
		 
		 return true;
	 }
}
