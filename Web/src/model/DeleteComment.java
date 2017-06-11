package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
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
 * Servlet implementation class DeleteComment
 */
@WebServlet("/DeleteComment")
public class DeleteComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATEMENT_DELETE = "delete from USER_COMMENT_ITEM_ where id=?";
	private static final String STATEMENT_SELECT = "select * from USER_COMMENT_ITEM_ c join USER_ u on u.id = c.user_id";

	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteComment() {
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
		
		String comment = request.getParameter("delete");
		System.out.println(comment);
		
		Connection connection =  DBConnection.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(STATEMENT_DELETE);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			pstmt.setString(1, comment);
			pstmt.executeUpdate();
			pstmt.close();
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
				
				System.out.println("aici+++++++++");
				System.out.println(comment+"     meeeeh");
				
			}
		Collections.sort(comments);
		return comments;
	}

}
