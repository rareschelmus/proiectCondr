package model;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OraclePreparedStatement;

import common.DBConnection;

@WebServlet("/DeleteAcount")
public class DeleteAcount extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
    public DeleteAcount() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("delete");
		
		try {
			
			OracleConnection c = DBConnection.getConnection();
			
			  OraclePreparedStatement st;
			  String p =(String) request.getSession().getAttribute("user_id");
			st = (OraclePreparedStatement) c.prepareStatement("update user_ set data_delete = ? where id=?");
			st.setDate(1, new Date(new java.util.Date().getTime()));
			st.setString(2,p );
			System.out.println(st.executeUpdate()+" "+p);
			st = (OraclePreparedStatement) c.prepareStatement("delete from util where id=?");
			st.setString(1, (String)request.getSession().getAttribute("user_id"));
			st.executeUpdate();
			st.close();
			
			request.getSession().invalidate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		  
	}

}
