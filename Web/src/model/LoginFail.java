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


@WebServlet("/LoginFail")
public class LoginFail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginFail() {
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
 		
 		
 		
        String sha256hex = DigestUtils.sha256Hex("2");
 		context.put("encrypt_main_page",DigestUtils.sha256Hex("2"));
 		context.put("encrypt_bootstrap_social", DigestUtils.sha256Hex("3"));
 		
 		template = ve.getTemplate("log_in_fail.html");        
 		
 		template.merge( context, writer );
         response.getWriter().println(writer.toString());
 	}

}
