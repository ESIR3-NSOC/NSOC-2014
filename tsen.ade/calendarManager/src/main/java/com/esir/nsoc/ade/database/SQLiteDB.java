package com.esir.nsoc.ade.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

	public void CreateExtractTable() {
		if (ExistTable("extract"))
			DropTable("extract");
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE extract "
					+ "(UID INT UNIQUE PRIMARY KEY     NOT NULL,"
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

	public void FillExtract(Set<ADE_Event> set, int projectid) {
		if (ExistTable("extract")){
			
		} else{
			CreateExtractTable();
		}
		
		// Get an iterator
		Iterator<ADE_Event> i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			ADE_Event adeEvent = (ADE_Event)i.next();
			PreparedStatement stmt;

			try {

				String sql = "INSERT INTO 'extract' (UID, PROJECTID, DTSTART, DTEND, SUMMARY, LOCATION, DESCRIPTION) " + "VALUES ('"
						+ adeEvent.getUid() + "', '" + projectid + "', '" + adeEvent.getDtstart() + "', '"
						+ adeEvent.getDtend() + "', ?, ?, ?);";
				stmt = _connection.prepareStatement(sql);
				//Evite d'utiliser les caractères spéciaux ex : "'"
				stmt.setString(1, adeEvent.getSummary());
				stmt.setString(2, adeEvent.getLocation());
				stmt.setString(3, adeEvent.getDescription());			

				stmt.executeUpdate();

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getConnection() {
		return _connection;
	}

	public boolean isConnected() {
		return _connected;
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
