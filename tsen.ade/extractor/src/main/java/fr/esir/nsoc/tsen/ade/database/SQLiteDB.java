package fr.esir.nsoc.tsen.ade.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import fr.esir.nsoc.tsen.ade.object.ADE_Event;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

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

	@Override
	public boolean isConnected() {
		return _connected;
	}

	public Connection getConnection() {
		return _connection;
	}

	public void close() {
		try {
			_connection.close();
		} catch (SQLException e) {
			// e.printStackTrace();
		}
		_connected = false;
	}

	@Override
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

	@Override
	public boolean addProject(Project project) {
		PreparedStatement stmt;
		try {
			String sql = "INSERT INTO 'project' (ID,NAME) " + "VALUES (?, ?);";
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

	@Override
	public void FillProject(HashSet<Project> projects) {
		Iterator<Project> i = projects.iterator();
		while (i.hasNext()) {
			addProject(i.next());
		}
	}

	@Override
	public void CreateTreeObjectTable() {
		if (ExistTable("tree_object"))
			DropTable("tree_object");
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE tree_object "
					+ "(ID          TEXT   NOT NULL,"
					+ " NAME        TEXT   NOT NULL,"
					+ " LEVEL       INT    NOT NULL,"
					+ " PARENT_ID   TEXT   NOT NULL,"
					+ " TYPE        TEXT   NOT NULL,"
					+ " PROJECT_ID  INT    NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public boolean addTreeObject(TreeObject treeObject) {
		PreparedStatement stmt;
		try {
			String sql = "INSERT INTO 'tree_object' (ID,NAME,LEVEL,PARENT_ID,TYPE,PROJECT_ID) "
					+ "VALUES (?, ?, ?, ?, ?, ?);";
			stmt = _connection.prepareStatement(sql);
			stmt.setString(1, treeObject.getId());
			stmt.setString(2, treeObject.getName());
			stmt.setLong(3, treeObject.getLevel());
			stmt.setString(4, treeObject.getParentId());
			stmt.setString(5, treeObject.getType());
			stmt.setLong(6, treeObject.getProject().getId());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			if (DEBUG)
				e.printStackTrace();
			return false;

		}
		return true;
	}

	public void CreateEventTable(int projectid) {
		if (ExistTable("event_" + Integer.toString(projectid)))
			DropTable("event_" + Integer.toString(projectid));
		Statement stmt = null;

		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE event_" + Integer.toString(projectid)
					+ " (UID TEXT UNIQUE PRIMARY KEY     NOT NULL,"
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

	public boolean FillEvent(Set<ADE_Event> set, int projectid) {
		if (!ExistTable("event_" + Integer.toString(projectid))) {
			CreateEventTable(projectid);
		}

		// Get an iterator
		Iterator<ADE_Event> i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			ADE_Event adeEvent = (ADE_Event) i.next();

			PreparedStatement stmtUpdate;

			try {

				Statement stmtQuery = _connection.createStatement();
				ResultSet rs = stmtQuery.executeQuery("SELECT UID FROM 'event_"
						+ Integer.toString(projectid) + "' WHERE UID = '"
						+ adeEvent.getUid() + "'");

				if (!rs.next()) {
					String sql = "INSERT INTO 'event_"
							+ Integer.toString(projectid)
							+ "' "
							+ "(UID, DTSTART, DTEND, SUMMARY, LOCATION, DESCRIPTION) "
							+ "VALUES (?, ?, ?, ?, ?, ?);";
					stmtUpdate = _connection.prepareStatement(sql);
					// Evite d'utiliser les caractères spéciaux ex : "'"
					stmtUpdate.setString(1, adeEvent.getUid());
					stmtUpdate.setString(2, adeEvent.getDtstart());
					stmtUpdate.setString(3, adeEvent.getDtend());
					stmtUpdate.setString(4, adeEvent.getSummary());
					stmtUpdate.setString(5, adeEvent.getLocation());
					stmtUpdate.setString(6, adeEvent.getDescription());

					stmtUpdate.executeUpdate();
					stmtUpdate.close();
				}
				stmtQuery.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

		}
		return true;
	}

	public void CreateUidTable(int projectid) {
		if (ExistTable("uid_" + Integer.toString(projectid)))
			DropTable("uid_" + Integer.toString(projectid));
		Statement stmt = null;

		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE uid_" + Integer.toString(projectid)
					+ " (UID INT				 NOT NULL,"
					+ " ADE_ID           INTEGER    NOT NULL,"
					+ " PRIMARY KEY (UID, ADE_ID))";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean FillUid(Set<ADE_Event> set, String adeid, int projectid) {
		if (!ExistTable("uid_" + Integer.toString(projectid))) {
			CreateUidTable(projectid);
		}

		// Get an iterator
		Iterator<ADE_Event> i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			ADE_Event adeEvent = (ADE_Event) i.next();

			PreparedStatement stmtUpdate;

			try {

				Statement stmtQuery = _connection.createStatement();
				ResultSet rs = stmtQuery
						.executeQuery("SELECT UID, ADE_ID FROM 'uid_"
								+ Integer.toString(projectid)
								+ "' WHERE UID = '" + adeEvent.getUid()
								+ "' AND ADE_ID = '" + adeid + "'");

				if (!rs.next()) {
					String sql = "INSERT INTO 'uid_"
							+ Integer.toString(projectid) + "' (UID, ADE_ID) "
							+ "VALUES (" + "?, ?);";
					stmtUpdate = _connection.prepareStatement(sql);

					stmtUpdate.setString(1, adeEvent.getUid());
					stmtUpdate.setString(2, adeid);

					stmtUpdate.executeUpdate();
					stmtUpdate.close();
				}
				stmtQuery.close();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	/*
	 * public String getNameTable(String firstdate){ //Get day of year
	 * SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd"); Date
	 * date = null; try { date = formatter.parse(firstdate); } catch
	 * (ParseException e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); }
	 * 
	 * Calendar calendar = new GregorianCalendar(); calendar.setTime(date);
	 * 
	 * return
	 * Integer.toString(calendar.get(Calendar.DAY_OF_YEAR))+"_"+Integer.toString
	 * (calendar.get(Calendar.YEAR)); }
	 */

	@Override
	public HashSet<TreeObject> getTreeObjectChildren(TreeObject treeObject) {
		Statement stmt = null;

		int level;
		String name;
		String id;
		String parentId;
		String type;

		HashSet<TreeObject> TreeObjectChildren = new HashSet<TreeObject>();
		try {
			stmt = _connection.createStatement();
			ResultSet rs = stmt
					.executeQuery("SELECT * FROM tree_object WHERE PARENT_ID="
							+ treeObject.getId() + ";");

			while (rs.next()) {
				id = rs.getString(1);
				name = rs.getString(2);
				level = rs.getInt(3);
				parentId = rs.getString(4);
				type = rs.getString(5);
				Project project = new Project(rs.getInt(6), "");
				TreeObjectChildren.add(new TreeObject(project, level, name,
						parentId, type));
			}
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return TreeObjectChildren;
	}

	private void DropTable(String name) {
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			stmt.executeUpdate("DROP TABLE '" + name + "';");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean ExistTable(String name) {
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

	@Override
	public Project getProject(int projectId) {

		Statement stmt = null;
		Project project = null;
		try {

			stmt = _connection.createStatement();
			String sql = "SELECT ID, NAME FROM project WHERE ID = " + projectId
					+ ";";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				project = new Project(rs.getInt(1), rs.getString(2));
			} else {
				project = new Project(-1, "error");
			}

			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return project;
	}

	@Override
	public boolean addADE_Event(ADE_Event _ADE_Event, Project project) {
		PreparedStatement stmt;
		try {
			String sql = "INSERT INTO 'event_" + Integer.toString(project.getId())
					+ "' "
					+ "(UID, DTSTART, DTEND, SUMMARY, LOCATION, DESCRIPTION) "
					+ "VALUES (?, ?, ?, ?, ?, ?);";
			stmt = _connection.prepareStatement(sql);

			stmt.setString(1, _ADE_Event.getUid());
			stmt.setString(2, _ADE_Event.getDtstart());
			stmt.setString(3, _ADE_Event.getDtend());
			stmt.setString(4, _ADE_Event.getSummary());
			stmt.setString(5, _ADE_Event.getLocation());
			stmt.setString(6, _ADE_Event.getDescription());

			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			if (DEBUG)
				e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean addUid(ADE_Event _ADE_Event, TreeObject treeObject,
			Project project) {
// TODO
		return true;
	}

}
