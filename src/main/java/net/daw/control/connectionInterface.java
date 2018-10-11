package net.daw.control;

import java.sql.Connection;

public interface connectionInterface {

	public Connection newConnection();
	
	public void disposeConnection();
	
}
