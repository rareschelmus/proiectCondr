package common;

import java.util.Properties;

import org.apache.velocity.app.VelocityEngine;

public class VelocityEngineObject {
	
      public static VelocityEngine getVelocityEngine() {
    	  Properties p = new Properties(); 
  		  p.setProperty( "resource.loader", "class" );
  		  p.setProperty( "class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader" );
  		
  		  VelocityEngine ve = new VelocityEngine();
  		  ve.init(p);
  		  
  		  return ve;
      }
}