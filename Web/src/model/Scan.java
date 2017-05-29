package model;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;









@WebServlet("/Scan")
public class Scan extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public Scan() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
;
//		InputStream in = request.getInputStream();
//		String requestStr = request.getParameter("x"); 
//		byte[] bytes = requestStr.getBytes(someSingleByteEncoding);

//		byte[] bytes = new byte[10000];
//		
//		
//		int i=0;
//		Scanner s = new Scanner(in);
//		
//		while (s.hasNext())
//		 bytes[i++]=s.nextByte();
		
//
//		PrintWriter p = new PrintWriter(new File("/home/nemo/Desktop/num.txt"));
//
//	 for (int j=0;j<i;++j)

//		p.print(new BigInteger(""+bytes[j]).toString(16).toUpperCase());
//		
    String x = request.getParameter("image");
    //x = URLDecoder.decode(x, "UTF-8");
    
    System.out.println(x.charAt(x.length()-1));
    System.out.println(x.length());
    
       FileWriter fw = new FileWriter(new File("/home/nemo/Desktop/num.txt"));
       fw.write(x);
       fw.flush();
       fw.close();
       x=x.split(",")[1];

      if (x!=null) {

    	  byte[] imagedata = DatatypeConverter.parseBase64Binary(x);
    	  BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imagedata));
    	  ImageIO.write(bufferedImage, "png", new File("/home/nemo/Desktop/img.png"));
      }
	}

}
