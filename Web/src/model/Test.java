package model;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * Servlet implementation class Test
 */
@WebServlet("/Test")
public class Test extends HttpServlet {
	private static final long serialVersionUID = 1L;
 
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
		VelocityContext context = new VelocityContext();
		
//		 System.out.println(request.getParameter("token"));
//		 
//		 
//		 HttpSession x = request.getSession();
//		 x.setAttribute("user",request.getParameter("name"));
		
//		Vector<String> pl = new Vector<String>();
//		
//		pl.add("test1");
//		pl.add("test2");
//		context.put("allProducts", pl);
		
		
		
		template = ve.getTemplate("test.vm");        
		
		template.merge( context, writer );
        response.getWriter().println(writer.toString());
	}

}