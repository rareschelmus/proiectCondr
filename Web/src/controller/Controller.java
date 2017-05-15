package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public Controller() {
        super();
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String url=request.getRequestURL().toString();
		String result="";
		
		RequestDispatcher requestDispatcher=null;
		
		int i=url.length()-1;
		
		while (i>=0 && url.charAt(i)!='/') {
			result=url.charAt(i)+result;
			i--;
		}

		
		
	    if (result.equals("Test"))
		 requestDispatcher = request.getRequestDispatcher("/Test"); else 
	    if (result.equals("Login"))
		 requestDispatcher = request.getRequestDispatcher("/Login"); else 
	     requestDispatcher = request.getRequestDispatcher("/MainPageModel");
	    
		 	 
		requestDispatcher.forward(request, response);
        
	}

}