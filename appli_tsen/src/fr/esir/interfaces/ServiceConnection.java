package fr.esir.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * oep
 */
public interface ServiceConnection {
	public ResultSet getDataFromDB() throws SQLException;

	public void close();
	
	public boolean connect();
}
