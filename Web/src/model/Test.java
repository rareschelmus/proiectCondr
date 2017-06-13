//package model;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import common.Utils;
//
//
///**
// * Servlet implementation class Test
// */
//@WebServlet("/Test")
//public class Test extends HttpServlet {
//	private static final long serialVersionUID = 1L;
// 
//    public Test() {
//        super();
//        
//    }
//
//	
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		doPost(request,response);
//	}
//
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        String redirectUrl = Utils.service.getAuthorizationUrl(null);
//        response.sendRedirect(redirectUrl);
//	    
//	}
//
//}
package model;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public static final String OAUTH_KEY = "291893044602593"; 
	 public static final String OAUTH_SECRET = "28bb62c0578cef84b395e0269349d152"; 
 
    public Test() {
        super();
        
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		VelocityEngine ve = common.VelocityEngineObject.getVelocityEngine();
		StringWriter writer = new StringWriter();
		Template template = null;
		template = ve.getTemplate("test.vm");
		
		
		VelocityContext context = new VelocityContext();
		
		
		context.put("obj", common.TestEAN.getResult("4006067081521"));
		template.merge( context, writer );
		response.setCharacterEncoding("UTF-8");
		
        response.getWriter().println(writer.toString());
	}

}