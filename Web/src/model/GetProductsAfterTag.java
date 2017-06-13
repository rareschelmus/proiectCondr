package model;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import common.DBConnection;

/**
 * Servlet implementation class GetProductsAfterTag
 */
@WebServlet("/GetProductsAfterTag")
public class GetProductsAfterTag extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String STATEMENT_SELECT_PRODUCTS = "select * from ITEM_";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProductsAfterTag() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
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
	 	 
	 	 context.put("encrypt_bootstrap_social", DigestUtils.sha256Hex("3"));
	 	 context.put("urlImage",(String) request.getSession().getAttribute("urlImage"));
	 	 context.put("name", (String) request.getSession().getAttribute("name"));
	 	 context.put("encrypt_js", DigestUtils.sha256Hex("5"));
	 	 template = ve.getTemplate("tag_page.html");
	 	 String tag = request.getParameter("tag");
		 System.out.println(tag);

	 	 context.put("tag_name", tag);
		
	 	 
	     Connection connection = DBConnection.getConnection();
	     PreparedStatement st = null;
	     
	     ArrayList<GenericProduct> genericProducts = new ArrayList<GenericProduct>();
	     
		 try {
		    st = (PreparedStatement) connection.prepareStatement(STATEMENT_SELECT_PRODUCTS);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		 
		try {
			ResultSet resultSet = st.executeQuery();
			boolean isFound = false;
			while (resultSet.next())
			{
				String id = resultSet.getString(1);
				String name = resultSet.getString(2);
				String goodTags = resultSet.getString(8);
				String badTags = resultSet.getString(9);
				
				String[] goodTagsList = goodTags.split(",");
				String[] badTagsList = badTags.split(",");
				
				for (int i=0; i<goodTagsList.length; ++i)
				{
					if (goodTagsList[i].equals(tag))
					{
						isFound = true;
						break;
					}
				}
				
				for (int i=0; i<badTagsList.length; ++i)
				{
					if (badTagsList[i].equals(tag))
					{
						isFound = true;
						break;
					}
				}
				
				GenericProduct genericProduct = new GenericProduct();
				
				genericProduct.setId(id);
				genericProduct.setName(name);
				
				if (isFound)
				{
					genericProducts.add(genericProduct);
					break;
				}
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    ArrayList list = new ArrayList();
		
		for (int i=0; i<genericProducts.size(); ++i)
		{
			Map map = new HashMap();
			map.put("name", genericProducts.get(i).getName());
			map.put("id",  genericProducts.get(i).getId());
			map.put("productImage", "/Web/ImageLoader?id="+genericProducts.get(i).getId());
			map.put("link", "/Web/Controller/Product?id_product="+genericProducts.get(i).getId());
			list.add(map);
		}

	    context.put("products", list);

		template.merge(context, writer);
	    response.getWriter().println(writer.toString()); 
	}

}
