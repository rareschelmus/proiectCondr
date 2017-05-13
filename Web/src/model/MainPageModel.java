package model;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * Servlet implementation class MainPageModel
 */
@WebServlet("/MainPageModel")
public class MainPageModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainPageModel() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   doPost(request,response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
VelocityEngine ve = common.VelocityEngineObject.getVelocityEngine();
		
		StringWriter writer = new StringWriter();
		Template template = null;
		VelocityContext context = new VelocityContext();
		
       System.out.println(request.getSession().getAttribute("user"));		
		
		template = ve.getTemplate("main_page.html");        
		
		template.merge( context, writer );
        response.getWriter().println(writer.toString());
	}

}
