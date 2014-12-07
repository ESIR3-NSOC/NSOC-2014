package fr.esir.nsoc.tsen.ade.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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

	public void CreateProjectTable() {
		if (ExistTable("project"))
			DropTable("project");
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE project "
					+ "(ID INT PRIMARY KEY     NOT NULL,"
					+ " NAME           TEXT    NOT NULL)";
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

	public void FillProject(Set set) {
		CreateProjectTable();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			Statement stmt;
			try {
				stmt = _connection.createStatement();
				String sql = "INSERT INTO 'project' (ID,NAME) " + "VALUES ("
						+ me.getKey() + ", '" + me.getValue() + "');";
				stmt.executeUpdate(sql);
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
