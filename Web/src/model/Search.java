package model;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.OracleConnection;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import common.DBConnection;

@WebServlet("/Search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public Search() {
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
			
			String e = (String)request.getParameter("EAN");
			
			String p = (String)request.getParameter("Product");
			
			String b = (String)request.getParameter("Brand");
			
			String c = (String) request.getParameter("Category");
			
			OracleResultSet res = null;
			OracleConnection con = DBConnection.getConnection();
			
			OraclePreparedStatement st = null;
			try {
				if (e!=null) {
					st = (OraclePreparedStatement) con.prepareStatement("select id,name,description,image1,image2,image3 from item_ where id=?");
					st.setString(1, e);
				} else 
				if (p!=null) {
					st = (OraclePreparedStatement) con.prepareStatement("select id,name,description,image1,image2,image3 from item_ where lower(name) like '%'||lower(?)||'%'");
				    st.setString(1, p);
				} else 
				if (b!=null) {
					st = (OraclePreparedStatement) con.prepareStatement("select i.id,i.name,i.description,image1,image2,image3 from item_ i join brand_ b on i.brand_id=b.brand_id where lower(b.name) like '%'||lower(?)||'%'");
				    st.setString(1, b);
				} else 
				if (c!=null) {
					st = (OraclePreparedStatement) con.prepareStatement("select i.id,i.name,i.description,image1,image2,image3 from item_ i join category_item_ c on i.id=c.item_id join category_ cc on c.category_id=cc.category_id where lower(cc.name) like '%'||lower(?)||'%' ");
				    st.setString(1, c);
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			ArrayList<common.Product> arr = new ArrayList<common.Product>();
			
			if (st!=null) {
				try {
					res=(OracleResultSet) st.executeQuery();
					while (res.next()) {
						arr.add(new common.Product(res.getString(1), res.getString(2), res.getString(3),res.getString(4)));	
					}
					if (arr.size()>0) {
						context.put("exists_product", arr.size());
						
						if (arr.size()>5) {
							int sz = arr.size()/5;
							if (arr.size()%5>0) sz++;
							context.put("total",sz);
							String page = request.getParameter("page");
							if (page==null) page="1";
							int pg = Integer.parseInt(page);
							
							ArrayList<common.Product> array = new ArrayList<common.Product>();
							for (int i=5*(pg-1);i<5*pg && arr.size()>i;++i)
								array.add(arr.get(i));
							context.put("arr", array);
							context.put("startpage", pg);
							
						} else {
						       context.put("total", 0);
						       context.put("arr", arr);
						       context.put("startpage", 1);
						       
							}
					} else {
						if (e!=null) {
					    	   System.out.println(e);
					    	   String asin = common.TestEAN.getResult(e);
					    	   
					    	   if (asin!=null) {
					    		   context.put("asin", asin);
					    		   System.out.println(asin);
					    	   }
					       }
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				
			} 

			context.put("encrypt_search_page",DigestUtils.sha256Hex("6"));
			context.put("encrypt_starabiliti_master",DigestUtils.sha256Hex("8"));
			context.put("encrypt_bootstrap_social", DigestUtils.sha256Hex("3"));
			
			context.put("urlImage",(String) request.getSession().getAttribute("urlImage"));
			context.put("name", (String) request.getSession().getAttribute("name"));
			context.put("encrypt_js", DigestUtils.sha256Hex("5"));
			context.put("sageata_stanga",'←');
			context.put("sageata_dreapta",'→');
			template = ve.getTemplate("search_page.html");        
			
			template.merge( context, writer );
			response.setCharacterEncoding("UTF-8");
	        response.getWriter().println(writer.toString());
	}
}
