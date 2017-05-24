package common;

import java.sql.DriverManager;
import java.sql.SQLException;

import oracle.jdbc.driver.OracleConnection;

public class DBConnection {
  public static OracleConnection getConnection() {
	  OracleConnection con=null;
	  try {
		Class.forName("oracle.jdbc.driver.OracleDriver");
         con=(OracleConnection)DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","condr","CONDR");  
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	  return con;
  }
}
