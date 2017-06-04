package model;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.DBConnection;

/**
 * Servlet implementation class CommentRating
 */
@WebServlet("/CommentRating")
public class CommentRating extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATEMENT_INSERT = "insert into USER_COMMENT_LIKE (ID,USER_ID,COMM_ID,VALUE) values (?,?,?,?)";
	private static final String STATEMENT_UPDATE = "update USER_COMMENT_LIKE SET VALUE = ? WHERE USER_ID=? and COMM_ID=?  ";
	private static final String STATEMENT_CHECK  = "select * from USER_COMMENT_LIKE";
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CommentRating() {
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
		// TODO Auto-generated method stub
		String commentId = request.getParameter("id");
		String first = request.getParameter("first");
		String like = request.getParameter("like");
		String userId = request.getParameter("user");
		System.out.println(commentId+" "+first+" "+like+" "+userId);
		
		
		Connection connection =  DBConnection.getConnection();
		PreparedStatement pstmt1 = null;
		boolean isAdded=true;
		try {
			pstmt1 = connection.prepareStatement(STATEMENT_CHECK);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ResultSet resultSet = pstmt1.executeQuery();
			while(resultSet.next())
			{
				if (resultSet.getString(2).equals(request.getSession().getAttribute("user_id")) && resultSet.getString(3).equals(commentId))
				{
					isAdded = false;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("a fost adauga:"+isAdded);
		if (isAdded)
		{
			try {
				PreparedStatement pstmt = connection.prepareStatement(STATEMENT_INSERT);
				pstmt.setString(1, commentId+userId);
				pstmt.setString(2, userId);
				pstmt.setString(3, commentId);
				pstmt.setString(4, "like");
				pstmt.executeUpdate();
	            pstmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			try {
				PreparedStatement pstmt = connection.prepareStatement(STATEMENT_UPDATE);
				System.out.println("aiuccc");
				if (like.equals("true"))
				{
					pstmt.setString(1, "like");
				}
				else
				{
					pstmt.setString(1, "dislike");
				}
				System.out.println(like);
				pstmt.setString(2,userId);
				pstmt.setString(3, commentId);
				pstmt.executeUpdate();
	            pstmt.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
