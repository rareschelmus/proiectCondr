package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Controller
 */

public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller() {
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
	    if (result.contains("css1"))  {
	     requestDispatcher = request.getRequestDispatcher("/Resources"); 
	     request.setAttribute("ADR",result.substring(0,result.length()-1));
	    } else 
	     requestDispatcher = request.getRequestDispatcher("/MainPageModel");
	    
		 	 
		requestDispatcher.forward(request, response);
        
	}

}