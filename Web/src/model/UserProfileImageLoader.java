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
 * Servlet implementation class UserProfileImageLoader
 */
@WebServlet("/UserProfileImageLoader")
public class UserProfileImageLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATEMENT = "select user_id from USER_COMMENT_ITEM_ where ID = ?";
   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserProfileImageLoader() {
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
		Connection connection = DBConnection.getConnection();
		String commentID = request.getParameter("comment_id");
		try {
			PreparedStatement st = (PreparedStatement) connection.prepareStatement(STATEMENT);
		//	st.setString(1, id);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

}
