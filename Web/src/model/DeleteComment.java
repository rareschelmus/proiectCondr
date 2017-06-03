package model;

import java.io.IOException;
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
 * Servlet implementation class DeleteComment
 */
@WebServlet("/DeleteComment")
public class DeleteComment extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATEMENT = "delete from USER_COMMENT_ITEM_ where id=?";

	
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
		// TODO Auto-generated method stub
		String comment = request.getParameter("delete");
		System.out.println(comment);
		
		Connection connection =  DBConnection.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(STATEMENT);
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
			
	}

}
