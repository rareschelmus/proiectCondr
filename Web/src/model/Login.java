package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.driver.OracleConnection;
import oracle.jdbc.internal.OraclePreparedStatement;
import oracle.jdbc.internal.OracleResultSet;

import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Version;
import com.restfb.exception.FacebookException;
import com.restfb.exception.FacebookGraphException;
import com.restfb.exception.FacebookJsonMappingException;
import com.restfb.exception.FacebookNetworkException;
import com.restfb.exception.FacebookOAuthException;
import com.restfb.exception.FacebookResponseStatusException;
import com.restfb.types.User;

import common.DBConnection;



@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Login() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  
		String token = (String) request.getParameter("token");
		String method = (String) request.getParameter("mt");
		String name = (String) request.getParameter("name");
		PrintWriter p = new PrintWriter(response.getOutputStream());
		
		String urlImage = null;
		String user_id = null;
		if (method.equals("f")) {
			try {
			FacebookClient facebookClient = new DefaultFacebookClient(token, "28bb62c0578cef84b395e0269349d152", Version.LATEST);        
	        User user = facebookClient.fetchObject("me", User.class);

	        name= user.getName();
	        user_id= user.getId();
	        System.out.println(user_id);
	        
	        
	        urlImage = "https://graph.facebook.com/"+user_id+"/picture?type=large";
			} catch (FacebookOAuthException e) {
				p.append("--");
	    		p.flush();
	    		p.close();
            	return ;
			}
		} else 
	    if (method.equals("g")) {
             urlImage = (String) request.getParameter("url");
             
	    	try {

	    	    URL url = new URL("https://www.googleapis.com/oauth2/v2/tokeninfo?id_token="+token);

	    	    HttpURLConnection connection = (HttpURLConnection) url
	    	            .openConnection();
	    	    connection.setDoInput(true);
	    	    
                connection.connect();
	    	    
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
	    	         // clientul este validat 
                	Scanner a = new Scanner (connection.getInputStream());
                	String ab = "";
                	while (a.hasNext()) ab+=a.next();
                	user_id= ab.split(":")[3].split("\"")[1];

                } else {
                	p.append("--");
    	    		p.flush();
    	    		p.close();
                	return ;
                }
	    	    	
	    	    	
	    	    
	    	} catch (MalformedURLException e) {
	    		p.append("--");
	    		p.flush();
	    		p.close();
	    		return ;
	    	} catch (IOException e) {
	    		
	    		p.append("--");
	    		p.flush();
	    		p.close();
	    		return ;
	    	}
	    }
		System.out.println(name+" logat");
//		System.out.println(user_id);
//		System.out.println(urlImage);
		 
		request.getSession().setAttribute("name", name);
		request.getSession().setAttribute("user_id", user_id);
		request.getSession().setAttribute("urlImage", urlImage);
		Cookie [] cookies = request.getCookies();
		if (cookies!=null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {
					Cookie x = (Cookie) cookie.clone();
					cookie.setPath("/");
					cookie.setValue("");
					cookie.setMaxAge(0);
					x.setMaxAge(24*3600);
					request.getSession().setMaxInactiveInterval(24*3600);
					x.setPath("/Web");
					response.addCookie(x);
					response.addCookie(cookie);
				}
			}
		// utilizator logat verific daca utilizatorul este pentru prima data logat si inserez in bd
		OracleConnection c = DBConnection.getConnection();
		try {
			OraclePreparedStatement st = (OraclePreparedStatement) c.prepareStatement("select count(id) from user_ where id=?");
			st.setString(1, user_id);
			OracleResultSet r = (OracleResultSet) st.executeQuery();
			if (r.next() && r.getInt(1)==0) {
				//utilizatorul este pentru prima data logat
				st = (OraclePreparedStatement) c.prepareStatement("insert into user_(id,type,user_image,user_name) values(?,?,?,?)");
				st.setString(1, user_id);
				st.setString(2, method);
				st.setString(3, urlImage);
				st.setString(4, name);
				st.executeUpdate();
			} else {
				st = (OraclePreparedStatement) c.prepareStatement("update user_ set data_delete=null, user_image = ?, user_name=?  where id=?");
				st.setString(3, user_id);
				st.setString(1, urlImage);
				st.setString(2, name);
				st.executeUpdate();
				st = (OraclePreparedStatement) c.prepareStatement("insert into util (id) values(?)");
				st.setString(1, (String)request.getSession().getAttribute("user_id"));
				st.executeUpdate();
				st.close();
				
			} 
		} catch (SQLException e) {
    		p.append("--");
    		p.flush();
    		p.close();
    		return ;
		}
		p.append("++");
		p.flush();
		p.close();
	}

}
