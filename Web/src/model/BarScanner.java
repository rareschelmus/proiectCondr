package model;

import java.io.IOException;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

@WebServlet("/BarScanner")
public class BarScanner extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public BarScanner() {
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
		
		context.put("encrypt_search_page",DigestUtils.sha256Hex("6"));
		context.put("encrypt_bootstrap_social", DigestUtils.sha256Hex("3"));
		context.put("urlImage",(String) request.getSession().getAttribute("urlImage"));
		context.put("name", (String) request.getSession().getAttribute("name"));
		context.put("encrypt_js", DigestUtils.sha256Hex("5"));
       
		template = ve.getTemplate("bar_scanner.html");        
		
		template.merge( context, writer );
        response.getWriter().println(writer.toString());
        
	}

}
