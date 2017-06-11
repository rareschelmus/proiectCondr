package model;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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

import oracle.jdbc.driver.OracleConnection;

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
	 private static final String STATEMENT = "select * from USER_COMMENT_ITEM_ c join USER_ u on u.id = c.user_id where ITEM_ID=?";
	 HashSet<String> hGoodSet = new HashSet<String>();
	 HashSet<String> hBadSet = new HashSet<String>();

	 HashMap<String, Integer> goodTagsMap = new HashMap<String, Integer>();
	 HashMap<String, Integer> badTagsMap = new HashMap<String, Integer>();

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
	 	 common.Product p = getProduct(id_product);
	 	 if (p!=null) {
	 		 context.put("exist", 1); 
	 		 context.put("product", p);
	 	 }
	 		      
	 	 goodTagsMap.clear();
	 	 badTagsMap.clear();


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
		 st.setString(1, id_product);
		 
	     } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	     
	    String canAdd = "true";
	    List<Comment> comments = new ArrayList<Comment>();

	    try {
			ResultSet resultSet = st.executeQuery();
			while (resultSet.next()){

				String commentId = resultSet.getString(1);
				String userID = resultSet.getString(2);
				String comment = resultSet.getString(4);
				String rating = resultSet.getString(5);
				String goodTags = resultSet.getString(6);
				String badTags = resultSet.getString(7);

				java.sql.Date date = resultSet.getDate(8);

				String userImage = resultSet.getString(12);
				String userName = resultSet.getString(13);
				
				Comment commentObj = new Comment();
				
				commentObj.setComment(comment);
				commentObj.setRating(rating);
				commentObj.setGoodTags(goodTags);
				commentObj.setBadTags(badTags);
				commentObj.setUserId(userID);
				commentObj.setUserImage(userImage);
				commentObj.setCommentId(commentId);
				commentObj.setUserName(userName);
				commentObj.setDate(date);
				
				if (userID.equals(request.getSession().getAttribute("user_id")))
				{
					commentObj.setCanEdit("true");
				}
				else 
				{
					commentObj.setCanEdit("false");
				}
				

				comments.add(commentObj);
			//	System.out.println(tags+" "+rating+" "+commentId+" "+userID+" "+comment+" "+userImage+" "+userName);
				splitGoodTags(goodTags);
				splitBadTags(badTags);

				createGoodTagProduct(goodTags); 
				createBadTagProduct(badTags); 

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
		Collections.sort(comments);
	    
	    for (Comment commentObj : comments )
	    {
	    	Map map = new HashMap();
			map.put("id", commentObj.getCommentID());
			map.put("user_id", commentObj.getUserId());
			map.put("comment", commentObj.getComment());
			map.put("userName", commentObj.getUserName());
			map.put("userImage", commentObj.getUserImage());
			map.put("rating", commentObj.getRating());
			map.put("goodTags", commentObj.getGoodTags());
			map.put("badTags", commentObj.getBadTags());

			
			if (commentObj.getRating().equals("5")){
				averageRating += 5;
				++nrRating;
			}
			
			if (commentObj.getRating().equals("4")){
				averageRating += 4;
				++nrRating;
			}
			
			if (commentObj.getRating().equals("3")){
				averageRating += 5;
				++nrRating;
			}
			
			if (commentObj.getRating().equals("2")){
				averageRating += 2;
				++nrRating;
			}
			
			if (commentObj.getRating().equals("1")){
				averageRating += 1;
				++nrRating;
			}
			
			String valueOfLike="";
			
			
			String canEdit = "false";
			if (commentObj.getUserId()!=null){
			if (commentObj.getUserId().equals(request.getSession().getAttribute("user_id")))
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
	    }
	       
	    context.put("comments", list);
	    context.put("canAdd", canAdd);
	    
	    context.put("goodTagsComment",createGoodTagsComment(hGoodSet));
	    context.put("badTagsComment",createGoodTagsComment(hBadSet));

	    context.put("goodTagsProduct", getGoodTagsProduct());
	    context.put("badTagsProduct", getBadTagsProduct());

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
	 
	 private void splitGoodTags(String tags)
	 {
		 if (tags!=null){
			 String[] splitTags = tags.split(",");
			 for (int i=0; i<splitTags.length; ++i)
			 {
				 hGoodSet.add(splitTags[i]);
			 }
		 }
	 }
	 
	 private void splitBadTags(String tags)
	 {
		 if (tags!=null){
			 String[] splitTags = tags.split(",");
			 for (int i=0; i<splitTags.length; ++i)
			 {
				 hBadSet.add(splitTags[i]);
			 }
		 }
	 }
	 
	 private String createGoodTagsComment(HashSet hSet)
	 {
		 Iterator iterator = hSet.iterator();
		 String tags="[";
		 while (iterator.hasNext())
		 {
			 tags+="'"+iterator.next()+"'"+",";
		 }
		 if (tags.length()>2){
		 String tagsReturn = tags.substring(0, tags.length()-2);
		 tagsReturn+="']";
		 System.out.println("tags"+tagsReturn);
		 return tagsReturn;
		 }
		 else return "[]";
	 }
	 
	 private String createBadTagsComment(HashSet hSet)
	 {
		 Iterator iterator = hSet.iterator();
		 String tags="[";
		 while (iterator.hasNext())
		 {
			 tags+="'"+iterator.next()+"'"+",";
		 }
		 if (tags.length()>2){
		 String tagsReturn = tags.substring(0, tags.length()-2);
		 tagsReturn+="']";
		 System.out.println("tags"+tagsReturn);
		 return tagsReturn;
		 }
		 else return "[]";
	 }
	 

	 
	 private void createGoodTagProduct(String  tags)
	 {
		 if (tags!=null){
			 String[] splitTags = tags.split(",");
			 System.out.println("array "+splitTags);
			 for (int i=0; i<splitTags.length; ++i)
			 {
				 String text = splitTags[i];
				 System.out.println(text+"trrr");
				 if (!goodTagsMap.containsKey(text))
				 {
					 System.out.println(text);
					 Integer value = new Integer(1);
					 goodTagsMap.put(text, value);
				 }
				 else
				 {
					 Integer value = goodTagsMap.get(text);
					 Integer newValue = new Integer(value.intValue() + 1);
					 goodTagsMap.put(text, newValue);
				 }
			 }
		 }
	 }
	 
	 private void createBadTagProduct(String  tags)
	 {
		 if (tags!=null){
			 String[] splitTags = tags.split(",");
			 System.out.println("array "+splitTags);
			 for (int i=0; i<splitTags.length; ++i)
			 {
				 String text = splitTags[i];
				 System.out.println(text+"trrr");
				 if (!badTagsMap.containsKey(text))
				 {
					 System.out.println(text);
					 Integer value = new Integer(1);
					 badTagsMap.put(text, value);
				 }
				 else
				 {
					 Integer value = badTagsMap.get(text);
					 Integer newValue = new Integer(value.intValue() + 1);
					 badTagsMap.put(text, newValue);
				 }
			 }
		 }
	 }
	
	 
	 private String getGoodTagsProduct()
	 {
		 TreeMap<String, Integer> sortedMap = sortMapByValue(goodTagsMap);
		 String tags="";
		 for(Map.Entry<String,Integer> entry : sortedMap.entrySet()) 
		 {
			  String key = entry.getKey();
			  tags+=key+",";
		 	}
		 
		 System.out.println("vassile "+tags);
		 if (tags.length()>2){
		 String tagsReturn = tags.substring(0, tags.length()-2);
		 System.out.println("dsda"+tagsReturn);
		 tagsReturn = tagsReturn.replaceAll(" ","");
		 return tagsReturn;
		 }
		 else return "";
	 }
	 
	 private String getBadTagsProduct()
	 {
		 TreeMap<String, Integer> sortedMap = sortMapByValue(badTagsMap);
		 String tags="";
		 for(Map.Entry<String,Integer> entry : sortedMap.entrySet()) 
		 {
			  String key = entry.getKey();
			  tags+=key+",";
		 	}
		 System.out.println("vassile "+tags);
		 if (tags.length()>2){
		 String tagsReturn = tags.substring(0, tags.length()-2);
		 System.out.println("dsda"+tagsReturn);
		 tagsReturn = tagsReturn.replaceAll(" ","");
		 return tagsReturn;
		 }
		 else return "";
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
	 
	 private common.Product getProduct(String id) {
		 OracleConnection con = common.DBConnection.getConnection();
		 PreparedStatement st;
		 common.Product p=null;
		try {
			st = con.prepareStatement("select name,description from item_ where id=?");
			st.setString(1, id);
			ResultSet r = st.executeQuery();
			if (!r.next()) return null;
		     p = new common.Product(id,r.getString(1), r.getString(2));

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return p;
	 }
}
