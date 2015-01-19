package fr.esir.nsoc.tsen.ade.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.object.Category;
import fr.esir.nsoc.tsen.ade.object.Project;

public class SQLiteDB implements DataBase {

	private final static boolean DEBUG = true;

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

	public void CreateBranchTable() {
		if (ExistTable("branch"))
			DropTable("branch");
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE branch "
					+ "(ID             INT     NOT NULL,"
					+ " PROJECT_ID     INT     NOT NULL,"
					+ " NAME           TEXT    NOT NULL,"
					+ " LEVEL          INT     NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void CreateCategoryTable() {
		if (ExistTable("category"))
			DropTable("category");
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE category "
					+ "(ID          TEXT   NOT NULL,"
					+ " NAME        TEXT   NOT NULL,"
					+ " PROJECT_ID  INT    NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void CreateBrancheTable() {
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

	public void FillProject(HashSet<Project> projects) {
		Iterator<Project> i = projects.iterator();
		while (i.hasNext()) {
			addProject(i.next());
		}
	}

	public void FillCategory(HashSet<Category> Categories) {
		Iterator<Category> i = Categories.iterator();
		while (i.hasNext()) {
			addCategory(i.next());
		}
	}

	public void close() {
		try {
			_connection.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		_connected = false;
	}

	public boolean addProject(Project project) {
		CreateProjectTable();
		PreparedStatement stmt;
		try {
			String sql = "INSERT INTO 'project' (ID,NAME) "
					+ "VALUES (?, ?);";
			stmt = _connection.prepareStatement(sql);
			stmt.setLong(1, project.getId());
			stmt.setString(2, project.getName());	
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			if (DEBUG)
				e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean addCategory(Category category) {
		CreateCategoryTable();
		PreparedStatement stmt;
		try {
			String sql = "INSERT INTO 'category' (ID,NAME,PROJECT_ID) "
					+ "VALUES (?, ?, ?);";
			stmt = _connection.prepareStatement(sql);
			stmt.setString(1, category.getId());
			stmt.setString(2, category.getName());
			stmt.setLong(3, category.getProject().getId());		
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			if (DEBUG)
				e.printStackTrace();
			return false;
		}
		return true;
	}
	

	
	public Connection getConnection() {
		return _connection;
	}

	public boolean isConnected() {
		return _connected;
	}

}
