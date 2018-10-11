package net.daw.control;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class Hikari implements connectionInterface {
	
	private Connection oConnection;
        private HikariDataSource oConnectionPool;

	HikariConfig config = new HikariConfig();
	
	public Connection newConnection() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");

		} catch (Exception ex) {
			System.out.println(ex);
		}
		
		config.setJdbcUrl("jdbc:mysql://localhost:3306/trolleyes");
		config.setUsername("root");
		config.setPassword("melapela15");

		config.addDataSourceProperty("cachePrepStmts", "true");
		config.addDataSourceProperty("prepStmtCacheSize", "250");
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

		config.setMaximumPoolSize(10);
		config.setMinimumIdle(5);
		config.setLeakDetectionThreshold(15000);
		config.setConnectionTestQuery("SELECT 1");
		config.setConnectionTimeout(2000);
		try {
			oConnectionPool = new HikariDataSource(config);
			oConnection = (Connection) oConnectionPool.getConnection();
			return oConnection;
		} catch (SQLException ex) {
			System.out.println(ex);
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
