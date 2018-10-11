package net.daw.control;

import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class C3p0 implements connectionInterface {
	
	private Connection oConnection;
	private ComboPooledDataSource cpds = new ComboPooledDataSource();

	public Connection newConnection() {
		
		
		try {
        	Class.forName("com.mysql.jdbc.Driver");
        } catch (Exception e) {
        	System.out.println(e);
        }
        
        try {
 
        
        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver            
        cpds.setJdbcUrl("jdbc:mysql://" + connectionConfig.url + ":" + connectionConfig.port + "/" + connectionConfig.dataBase);
        cpds.setUser(connectionConfig.user);                                  
        cpds.setPassword(connectionConfig.pass); 

        oConnection = cpds.getConnection();
        
        
        
        
        } catch (Exception e) {
        	System.out.println(e);
        }
        
        return oConnection;
	}

	public void disposeConnection() {
		 try {
	            oConnection.close();
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            System.out.print(e);
	        }
		
	}

	
	
}
