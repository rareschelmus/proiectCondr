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
	 private static final String STATEMENT_SELECT_COMMENT = "select * from USER_COMMENT_ITEM_ c join USER_ u on u.id = c.user_id where ITEM_ID=?";
	 private static final String STATEMENT_SELECT_REL_PRODUCTS  = "select * from ITEM_ where ID <> ?";
	 private static final String STATEMENT_UPDATE_TAGS = "update ITEM_ set GOOD_TAGS = ?, BAD_TAGS = ? where ID = ?";
	 
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


	
	 	 context.put("encrypt_bootstrap_social", DigestUtils.sha256Hex("3"));
	 	 context.put("id_product", id_product);
	 	 context.put("urlImage",(String) request.getSession().getAttribute("urlImage"));
	 	 context.put("name", (String) request.getSession().getAttribute("name"));
	 	 context.put("encrypt_js", DigestUtils.sha256Hex("5"));
	 	 String amazon = common.TestEAN.getResult(id_product);
	 	 if (amazon!=null) {
	 		 context.put("amazon", "https://www.amazon.com/dp/"+amazon);
	 	 }
	 	 
	 	 template = ve.getTemplate("product_container.html");
	 	 int averageRating = 0;
	 	 int nrRating = 0;
	    // response.getWriter().println(writer.toString()); 
	     
	     //Starting getting user comments 
	     
	     Connection connection = DBConnection.getConnection();
	     ArrayList list = new ArrayList();
	     PreparedStatement st = null;
	     
	     try {
		 st = (PreparedStatement) connection.prepareStatement(STATEMENT_SELECT_COMMENT);
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

				java.sql.Timestamp date = resultSet.getTimestamp(8);

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
			String isGoodTag = "true";
			String isBadTag = "true";
	    	Map map = new HashMap();
	    	System.out.println(commentObj.getDate().getTime()+"+++++++++++++++ ");
			map.put("id", commentObj.getCommentID());
			map.put("user_id", commentObj.getUserId());
			map.put("comment", commentObj.getComment());
			map.put("userName", commentObj.getUserName());
			map.put("userImage", commentObj.getUserImage());
			map.put("rating", commentObj.getRating());
			System.out.println("tagurile sunt "+commentObj.getGoodTags()+" "+commentObj.getBadTags());
			map.put("goodTags", commentObj.getGoodTags());
			map.put("badTags", commentObj.getBadTags());
			
			if (commentObj.getGoodTags().equals(" "))
			{
				isGoodTag = "false";
			}
			
			if (commentObj.getBadTags().equals(" ") )
			{
				isBadTag = "false";
			}
			
			map.put("isGoodTags", isGoodTag);
			map.put("isBadTags", isBadTag);

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

	    
	    String loggedUserID = (String) request.getSession().getAttribute("user_id");
		System.out.println("userul logat"+request.getSession().getAttribute("user_id") );
		if (loggedUserID==null)
		{
			canAdd = "false";
		}
	       
	    context.put("comments", list);
	    context.put("canAdd", canAdd);
	    
	    context.put("goodTagsComment",createGoodTagsComment(hGoodSet));
	    context.put("badTagsComment",createGoodTagsComment(hBadSet));
	    
	    String goodTagsProduct = getGoodTagsProduct();
	    String badTagsProduct = getBadTagsProduct();
	    
	    try {
			st = (PreparedStatement) connection.prepareStatement(STATEMENT_UPDATE_TAGS);
			st.setString(1, goodTagsProduct);
			st.setString(2, badTagsProduct);
			st.setString(3, id_product);
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   
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
	 
	 private ArrayList<GenericProduct> getRelatedProducts(String goodTags, String badTags, String currProductId) throws SQLException
	 {
		 hBadSet.clear();
		 hGoodSet.clear();
		 
		 ArrayList<GenericProduct> list = new ArrayList<GenericProduct>();
		 
		 HashSet<String> l_hGoodSet = new HashSet<String>();
		 HashSet<String> l_hBadSet = new HashSet<String>();
		 
		 splitBadTags(badTags);
		 splitGoodTags(goodTags);

		 Connection connection = DBConnection.getConnection(); 
	  	 PreparedStatement stItem = (PreparedStatement) connection.prepareStatement(STATEMENT_SELECT_REL_PRODUCTS);
	  	 
	  	 stItem.setString(1, currProductId);
	  	 
	  	 ResultSet resultSet = stItem.executeQuery(); 
	  	 
	  	 while (resultSet.next())
	  	 {
	  		 String l_goodTags = resultSet.getString(8);
	  		 String l_badTags = resultSet.getString(9);
	  		 
	  		if (l_goodTags!=null){
				 String[] splitTags = l_goodTags.split(",");
				 for (int i=0; i<splitTags.length; ++i)
				 {
					 l_hGoodSet.add(splitTags[i]);
				 }
			 }
	  		
	  		if (l_badTags!=null){
				 String[] splitTags = l_badTags.split(",");
				 for (int i=0; i<splitTags.length; ++i)
				 {
					 l_hBadSet.add(splitTags[i]);
				 }
			 }
	  		int countHits = 0;
	  		
	  		Iterator iterator = hBadSet.iterator();
	  		while (iterator.hasNext())
	  		{
	  			if (l_hBadSet.contains(iterator.next()))
	  			{
	  				countHits++;
	  			}
	  			if (l_hGoodSet.contains(iterator.next()))
	  			{
	  				countHits++;
	  			}
	  		}
	  		
	  	    iterator = hGoodSet.iterator();
	  		while (iterator.hasNext())
	  		{
	  			if (l_hBadSet.contains(iterator.next()))
	  			{
	  				countHits++;
	  			}
	  			if (l_hGoodSet.contains(iterator.next()))
	  			{
	  				countHits++;
	  			}
	  		}
	  		
	  		GenericProduct genericProduct = new GenericProduct();
	  		
	  		String id = resultSet.getString(1);
	  		String name = resultSet.getString(2);
	  		
	  		genericProduct.setId(id);
	  		genericProduct.setName(name);

	  		list.add(genericProduct);
	  		
	  	 }
	  	 
		 Collections.sort(list);
		 
		 return null;
	 }
	 
	 
}
