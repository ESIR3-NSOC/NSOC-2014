package com.esir.nsoc.ade.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

	public void CreateEventTable(String firstdate) {
		if (ExistTable("event_"+getNameTable(firstdate)))
			DropTable("event_"+getNameTable(firstdate));
		Statement stmt = null;
				
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE event_" + getNameTable(firstdate)
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
	
	public boolean FillEvent(Set<ADE_Event> set, int projectid, String firstdate) {
		if (!ExistTable("event_"+getNameTable(firstdate))){
			CreateEventTable(firstdate);
		}
		
		// Get an iterator
		Iterator<ADE_Event> i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			ADE_Event adeEvent = (ADE_Event)i.next();
			
			PreparedStatement stmtUpdate;

			try {

				Statement stmtQuery = _connection.createStatement();
	            ResultSet rs = stmtQuery.executeQuery("SELECT UID FROM 'event_"+getNameTable(firstdate)+"' WHERE UID = '" +adeEvent.getUid()+"'");

	            if(!rs.next()){
	            	String sql = "INSERT INTO 'event_"+getNameTable(firstdate)+"' (UID, PROJECTID, DTSTART, DTEND, SUMMARY, LOCATION, DESCRIPTION) " + "VALUES ('"
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
		//close();
		return true;
	}

	public void CreateUidTable(String firstdate) {
		if (ExistTable("uid_"+getNameTable(firstdate)))
			DropTable("uid_"+getNameTable(firstdate));
		Statement stmt = null;
				
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE uid_" + getNameTable(firstdate)
					+ " (UID INT				 NOT NULL,"
					+ " ADE_ID           INTEGER    NOT NULL,"
					+ " PROJECTID           INTEGER    NOT NULL,"
					+ " PRIMARY KEY (UID, ADE_ID))";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean FillUid(Set<ADE_Event> set, int adeid, int projectid, String firstdate) {
		if (!ExistTable("uid_"+getNameTable(firstdate))){
			CreateUidTable(firstdate);
		}
		
		// Get an iterator
		Iterator<ADE_Event> i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			ADE_Event adeEvent = (ADE_Event)i.next();
			
			PreparedStatement stmtUpdate;

			try {

				Statement stmtQuery = _connection.createStatement();
	            ResultSet rs = stmtQuery.executeQuery("SELECT UID, ADE_ID FROM 'uid_"+getNameTable(firstdate)+"' WHERE UID = '" +adeEvent.getUid()+"' AND ADE_ID = '" +adeid+"'");

	            if(!rs.next()){
	            	String sql = "INSERT INTO 'uid_"+getNameTable(firstdate)+"' (UID, ADE_ID, PROJECTID) " + "VALUES ('"
							+ adeEvent.getUid() + "', '" + adeid + "', '" + projectid + "');";
					stmtUpdate = _connection.prepareStatement(sql);

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

	public Connection getConnection() {
		return _connection;
	}

	public boolean isConnected() {
		return _connected;
	}
	
	public String getNameTable(String firstdate){
		//Get day of year
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(firstdate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		
		return Integer.toString(calendar.get(Calendar.DAY_OF_YEAR))+"_"+Integer.toString(calendar.get(Calendar.YEAR));
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
