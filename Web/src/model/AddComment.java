package model;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.DBConnection;

/**
 * Servlet implementation class AddComment
 */
@WebServlet("/AddComment")
public class AddComment extends HttpServlet {
	private static final String STATEMENT = "insert into USER_COMMENT_ITEM_ (ID,USER_ID,ITEM_ID,COMM,RATING, TAGS) values (?,?,?,?,?,?)";

	

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
		String comment = request.getParameter("comment");
		String rating = request.getParameter("rating");
		String productId = request.getParameter("product");
		String tags = request.getParameter("tags");
		System.out.println("taguri "+tags);
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
			PreparedStatement pstmt = connection.prepareStatement(STATEMENT);
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
			
		
			pstmt.setString(6, tags);
			
			if ( !(rating.equals("") && comment.equals(" ") ))
			{
				pstmt.executeUpdate();
	            pstmt.close();
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	}

}
