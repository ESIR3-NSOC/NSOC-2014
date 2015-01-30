package com.example.esir.nsoc2014.tsen.lob.interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ServiceConnection {
	public ResultSet getDataFromDB() throws SQLException;

	public void close();
	
	public boolean connect();
}
