package model;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.deploy.ContextTransaction;
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
	 HashSet<String> hSet = new HashSet<String>();
	 HashMap<String, Integer> tagsMap = new HashMap<String, Integer>();
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
	 	 tagsMap.clear();
	     String sha256hex = DigestUtils.sha256Hex("2");
	 	 context.put("encrypt_main_page",DigestUtils.sha256Hex("2"));
	 	 context.put("encrypt_bootstrap_social", DigestUtils.sha256Hex("3"));
	 	 context.put("id_product", id_product);
	 	context.put("urlImage",(String) request.getSession().getAttribute("urlImage"));
		context.put("name", (String) request.getSession().getAttribute("name"));
		context.put("encrypt_js", DigestUtils.sha256Hex("5"));
	 	 
	 	 template = ve.getTemplate("product_container.html");
	 	 int averageRating = 0;
	 	 int nrRating = 0;
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
				splitTags(tags);
				Map map = new HashMap();
				map.put("id", commentId);
				map.put("user_id", userID);
				map.put("comment", comment);
				map.put("userName", userName);
				map.put("userImage", userImage);
				map.put("rating", rating);
				map.put("tags", tags);
				createTagProduct(tags);
				if (rating.equals("5")){
					averageRating += 5;
					++nrRating;
				}
				
				if (rating.equals("4")){
					averageRating += 4;
					++nrRating;
				}
				
				if (rating.equals("3")){
					averageRating += 5;
					++nrRating;
				}
				
				if (rating.equals("2")){
					averageRating += 2;
					++nrRating;
				}
				
				if (rating.equals("1")){
					averageRating += 1;
					++nrRating;
				}
				
				String valueOfLike="";
				
				
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
	    
	    context.put("tagsComment",createTagsComment(hSet));
	    context.put("tagsProduct", getTagsProduct());
	    if (nrRating>0)
	    {
	    	context.put("rating", averageRating/nrRating);
	    }
	    else 
	    {
	    	context.put("rating", 0);
	    }
	    String loggedUserID = (String) request.getSession().getAttribute("user_id");
	    System.out.println("user care ii logat"+loggedUserID);
	    context.put("logged_user", loggedUserID);
	    template.merge(context, writer);
	    response.getWriter().println(writer.toString()); 
	 }
	 
	 private void splitTags(String tags)
	 {
		 String[] splitTags = tags.split(",");
		 for (int i=0; i<splitTags.length; ++i)
		 {
			 hSet.add(splitTags[i]);
		 }
	 }
	 
	 private String createTagsComment(HashSet hSet)
	 {
		 Iterator iterator = hSet.iterator();
		 String tags="[";
		 while (iterator.hasNext())
		 {
			 tags+="'"+iterator.next()+"'"+",";
		 }
		 
		 String tagsReturn = tags.substring(0, tags.length()-2);
		 tagsReturn+="']";
		 System.out.println("tags"+tagsReturn);
		 return tagsReturn;
	 }
	 
	 private void createTagProduct(String  tags)
	 {
		 String[] splitTags = tags.split(",");
		 for (int i=0; i<splitTags.length; ++i)
		 {
			 String text = splitTags[i];
			 System.out.println(text+"trrr");
			 if (!tagsMap.containsKey(text))
			 {
				 System.out.println(text);
				 Integer value = new Integer(1);
				 tagsMap.put(text, value);
			 }
			 else
			 {
				 Integer value = tagsMap.get(text);
				 Integer newValue = new Integer(value.intValue() + 1);
				 tagsMap.put(text, newValue);
			 }
		 }
	 }
	 
	 private String getTagsProduct()
	 {
		 TreeMap<String, Integer> sortedMap = sortMapByValue(tagsMap);
		 String tags="";
		 for(Map.Entry<String,Integer> entry : sortedMap.entrySet()) 
		 {
			  String key = entry.getKey();
			  tags+=key+",";
		 	}
		 String tagsReturn = tags.substring(0, tags.length()-2);
		 System.out.println(tagsReturn);
		 tagsReturn = tagsReturn.replaceAll(" ","");
		 return tagsReturn;
	 }
	 
	 private  static TreeMap<String, Integer> sortMapByValue(HashMap<String, Integer> map){
			Comparator<String> comparator = new ValueComparator(map);
			TreeMap<String, Integer> result = new TreeMap<String, Integer>(comparator);
			result.putAll(map);
			return result;
		}
	 
	 private static class ValueComparator implements Comparator<String>{
		 
			HashMap<String, Integer> map = new HashMap<String, Integer>();
		 
			public ValueComparator(HashMap<String, Integer> map){
				this.map.putAll(map);
			}
		 
			public int compare(String s1, String s2) {
				if(map.get(s1) >= map.get(s2)){
					return -1;
				}else{
					return 1;
				}	
			}
		}
}
