package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;



public class TestEAN {
   public static String getResult(String EAN) {
	   if (EAN==null) 
		   return null;
	    
	    String  str = null;
	   
	   try {
	
		   
		   URL url = new URL("https://www.upccodesearch.com/api/v1/ean/"+EAN);
		   URLConnection con = url.openConnection();
		   BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
           String inputLine;
           StringBuilder b = new StringBuilder();
            while ((inputLine = in.readLine()) != null) 
               b.append(inputLine);
           in.close();
           inputLine = b.toString();
           if (inputLine.contains("\"status\":4")) {
        	   return null;
           }
           
           
           str = inputLine.split("\"asins\":")[1].split("\"")[1].split("\"")[0];
           
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   return str;
   }
}
