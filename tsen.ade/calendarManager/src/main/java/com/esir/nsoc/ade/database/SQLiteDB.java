package com.esir.nsoc.ade.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;

import com.esir.nsoc.ade.parser.ADE_Event;

public class SQLiteDB {

	private Connection _connection = null;
	private boolean _connected = false;
	public SQLiteDB(String name) {
		try {
			Class.forName("org.sqlite.JDBC");
			_connection = DriverManager.getConnection("jdbc:sqlite:" + name);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			_connected = false;
		}
		_connected = true;
	}

	public void CreateEventTable() {
		if (ExistTable("event_"+getDayOfYear()))
			DropTable("event_"+getDayOfYear());
		Statement stmt = null;
				
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE event_" + getDayOfYear()
					+ " (UID INT UNIQUE PRIMARY KEY     NOT NULL,"
					+ " PROJECTID           INTEGER    NOT NULL,"
					+ " DTSTART           DATE    NOT NULL,"
					+ " DTEND           DATE    NOT NULL,"
					+ " SUMMARY           TEXT    NOT NULL,"
					+ " LOCATION           TEXT    NOT NULL,"
					+ " DESCRIPTION          TEXT    NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void DropTable(String name) {
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			stmt.executeUpdate("DROP TABLE '" + name + "';");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean ExistTable(String name) {
		Statement stmt = null;
		boolean exist = false;
		try {
			stmt = _connection.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='"
							+ name + "';");
			exist = rs.next() && name.equals(rs.getString("name"));			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return exist;
	}

	public boolean FillEvent(Set<ADE_Event> set, int projectid) {
		if (!ExistTable("event_"+getDayOfYear())){
			CreateEventTable();
		}
		
		// Get an iterator
		Iterator<ADE_Event> i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			ADE_Event adeEvent = (ADE_Event)i.next();
			
			PreparedStatement stmtUpdate;

			try {

				Statement stmtQuery = _connection.createStatement();
	            ResultSet rs = stmtQuery.executeQuery("SELECT UID FROM 'event_"+getDayOfYear()+"' WHERE UID = '" +adeEvent.getUid()+"'");

	            if(!rs.next()){
	            	String sql = "INSERT INTO 'event_"+getDayOfYear()+"' (UID, PROJECTID, DTSTART, DTEND, SUMMARY, LOCATION, DESCRIPTION) " + "VALUES ('"
							+ adeEvent.getUid() + "', '" + projectid + "', '" + adeEvent.getDtstart() + "', '"
							+ adeEvent.getDtend() + "', ?, ?, ?);";
					stmtUpdate = _connection.prepareStatement(sql);
					//Evite d'utiliser les caractères spéciaux ex : "'"
					stmtUpdate.setString(1, adeEvent.getSummary());
					stmtUpdate.setString(2, adeEvent.getLocation());
					stmtUpdate.setString(3, adeEvent.getDescription());			

					stmtUpdate.executeUpdate();
					
	            }
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
			
		}
		close();
		return true;
	}

	public Connection getConnection() {
		return _connection;
	}

	public boolean isConnected() {
		return _connected;
	}
	
	public int getDayOfYear(){
		//Get day of year
		Calendar calendar = Calendar.getInstance();
		int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);  
		return dayOfYear;
	}

	public void close() {
		try {
			_connection.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		_connected = false;
	}
}
