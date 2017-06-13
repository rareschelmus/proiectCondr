package controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.internal.OraclePreparedStatement;
import oracle.jdbc.internal.OracleResultSet;
import common.DBConnection;


@WebServlet("/ImageLoader")
public class ImageLoader extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public ImageLoader() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		byte[] content =null;
		OracleConnection c = DBConnection.getConnection();
		
		
		String id = request.getParameter("id");
		String ccc = request.getParameter("c");
		if (ccc==null) ccc="1";
		if (!ccc.equals("1") && !ccc.equals("2") && !ccc.equals("3"))
			 ccc="1";
		System.out.println(id);
		try {
			
			OraclePreparedStatement st = (OraclePreparedStatement) c.prepareStatement("select image"+ccc+" from item_ where id=?");
			st.setString(1, id);
		    OracleResultSet r = (OracleResultSet) st.executeQuery();
		    if (r.next()) {
		    	content=r.getBytes(1);
		    }
		    
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		System.out.println(content.length);
		
		
		
		response.setContentType("image/jpg");
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
        response.getOutputStream().flush();
        response.getOutputStream().close();
	}
	
	public byte[] extractBytes (String ImageName) throws IOException {
		 
		File fi = new File(ImageName);
		byte[] fileContent = Files.readAllBytes(fi.toPath());

		 return fileContent;
		}

}