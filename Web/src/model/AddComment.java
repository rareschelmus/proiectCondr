package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import common.DBConnection;

/**
 * Servlet implementation class AddComment
 */
@WebServlet("/AddComment")
public class AddComment extends HttpServlet {
	private static final String STATEMENT_INSERT = "insert into USER_COMMENT_ITEM_ (ID,USER_ID,ITEM_ID,COMM,RATING, GOOD_TAGS, BAD_TAGS, DATA) values (?,?,?,?,?,?,?,?)";
	private static final String STATEMENT_SELECT = "select * from USER_COMMENT_ITEM_ c join USER_ u on u.id = c.user_id";

	

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddComment() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
		PrintWriter out = response.getWriter();
		
		response.setContentType("application/json");
        response.setHeader("Cache-control", "no-cache, no-store");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "-1");

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
		
		String comment = request.getParameter("comment");
		String rating = request.getParameter("rating");
		String productId = request.getParameter("product");
		String goodTags = request.getParameter("goodTags");
		String badTags = request.getParameter("badTags");

		
		java.util.Date utilDate = new Date();

		
		java.sql.Date date = new java.sql.Date((System.currentTimeMillis()));

		System.out.println("taguri "+goodTags+badTags);
		System.out.println("comment "+comment);
		System.out.println("rating "+rating);
		System.out.println("product "+productId);
		
		String userId = (String) request.getSession().getAttribute("user_id");
		String userProfilePicture =(String) request.getSession().getAttribute("urlImage");
	
		System.out.println("image frm user  "+userProfilePicture);
		
		Connection connection =  DBConnection.getConnection();
		
		
		
		StringBuilder sb = new StringBuilder();
		sb.append("");
		sb.append( ((userId.hashCode()<0)? (userId.hashCode()*(-1)) : (userId.hashCode()))  + (( comment.hashCode()<0) ?comment.hashCode()*(-1) : comment.hashCode()) );
		String id = sb.toString();

		
		try {
			PreparedStatement pstmt = connection.prepareStatement(STATEMENT_INSERT);
			pstmt.setString(1, id);
			pstmt.setString(2, userId);
			//System.out.println(new BigInteger(userName.trim().hashCode()).toString());
			pstmt.setString(3, productId);
			if (comment.equals(""))
			{
				pstmt.setString(4, "");
			}else
			pstmt.setString(4, comment);
			
			
			if (rating.equals(""))
			{
				pstmt.setString(5, " ");
			}
			else pstmt.setString(5, rating);
			
		
			pstmt.setString(6, goodTags);
			pstmt.setString(7, badTags);

			pstmt.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
			
			if ( !(rating.equals("") && comment.equals(" ") ))
			{
				pstmt.executeUpdate();
	            pstmt.close();
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Gson jSon = new Gson();
		
		JsonObject myObj = new JsonObject();
		JsonElement element = null;
		
		try {
		    element = jSon.toJsonTree(getComments(request),  new TypeToken<List<Comment>>() {}.getType());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myObj.addProperty("success", true);
		myObj.add("comments", element);
		out.println(myObj.toString());
		System.out.println(myObj.toString());
		out.close();
	}
	
	private List<Comment> getComments(HttpServletRequest request) throws SQLException
	{
		Connection connection = DBConnection.getConnection();
	    PreparedStatement st = null;
	    
	    List<Comment> comments = new ArrayList<Comment>();
	     
	    try {
		st = (PreparedStatement) connection.prepareStatement(STATEMENT_SELECT);
		 
	    } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    ResultSet resultSet = st.executeQuery();
	     
			while (resultSet.next())
			{
				String commentId = resultSet.getString(1);
				String userID = resultSet.getString(2);
				String comment = resultSet.getString(4);
				String rating = resultSet.getString(5);
				String goodTags = resultSet.getString(6);
				String badTags = resultSet.getString(7);

				java.sql.Timestamp date = resultSet.getTimestamp(8);
				
				String userImage = resultSet.getString(12);
				String userName = resultSet.getString(13);
				
				System.out.println(comment+" "+userImage+" "+userName);
				
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
				
				
				System.out.println(comment+"     meeeeh");
				
			}
			
		Collections.sort(comments);
		
		return comments;
	}

}
