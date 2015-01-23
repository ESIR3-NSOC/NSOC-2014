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

import fr.esir.nsoc.tsen.ade.object.Event;
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
	public void createProjectTable() {
		if (existTable("project"))
			dropTable("project");
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
	public void fillProject(HashSet<Project> projects) {
		Iterator<Project> i = projects.iterator();
		while (i.hasNext()) {
			addProject(i.next());
		}
	}

	@Override
	public void createTreeObjectTable(int projectid) {
		if (existTable("tree_object_" + Integer.toString(projectid) + ""))
			dropTable("tree_object_" + Integer.toString(projectid) + "");
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE 'tree_object_" + Integer.toString(projectid) + "' "
					+ "(ID          TEXT   NOT NULL,"
					+ " NAME        TEXT   NOT NULL,"
					+ " LEVEL       INT    NOT NULL,"
					+ " PARENT_ID   TEXT   NOT NULL,"
					+ " TYPE        TEXT   NOT NULL)";
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
			String sql = "INSERT INTO 'tree_object_" + treeObject.getProject().getId() + "' (ID,NAME,LEVEL,PARENT_ID,TYPE) "
					+ "VALUES (?, ?, ?, ?, ?);";
			stmt = _connection.prepareStatement(sql);
			stmt.setString(1, treeObject.getId());
			stmt.setString(2, treeObject.getName());
			stmt.setLong(3, treeObject.getLevel());
			stmt.setString(4, treeObject.getParentId());
			stmt.setString(5, treeObject.getType());
			stmt.executeUpdate();
			stmt.close();
		} catch (SQLException e) {
			if (DEBUG)
				e.printStackTrace();
			return false;

		}
		return true;
	}

	public void createEventTable(int projectid) {
		if (existTable("event_" + Integer.toString(projectid)))
			dropTable("event_" + Integer.toString(projectid));
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

	public boolean fillEvent(Set<Event> set, int projectid) {
		if (!existTable("event_" + Integer.toString(projectid))) {
			createEventTable(projectid);
		}

		// Get an iterator
		Iterator<Event> i = set.iterator();
		// Display elements
		while (i.hasNext()) {
			Event adeEvent = (Event) i.next();

			PreparedStatement stmtUpdate;

			try {

				Statement stmtQuery = _connection.createStatement();
				ResultSet rs = stmtQuery.executeQuery("SELECT UID FROM 'event_"
						+ Integer.toString(projectid) + "' WHERE UID = '"
						+ adeEvent.getId() + "'");

				if (!rs.next()) {
					String sql = "INSERT INTO 'event_"
							+ Integer.toString(projectid)
							+ "' "
							+ "(UID, DTSTART, DTEND, SUMMARY, LOCATION, DESCRIPTION) "
							+ "VALUES (?, ?, ?, ?, ?, ?);";
					stmtUpdate = _connection.prepareStatement(sql);
					// Evite d'utiliser les caractères spéciaux ex : "'"
					stmtUpdate.setString(1, adeEvent.getId());
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

	public void createCorrespondenceTable(int projectid) {
		if (existTable("correspondence_" + Integer.toString(projectid)))
			dropTable("correspondence_" + Integer.toString(projectid));
		Statement stmt = null;

		try {
			stmt = _connection.createStatement();
			String sql = "CREATE TABLE correspondence_" + Integer.toString(projectid)
					+ " (EVENT_ID INT				 NOT NULL,"
					+ " ADE_ID           INTEGER    NOT NULL,"
					+ " PRIMARY KEY (EVENT_ID, ADE_ID))";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	
	
	/**
	 * Récupération des enfants d'une branche
	 */
	@Override
	public HashSet<TreeObject> getTreeObjectChildren(TreeObject treeObject) {
		
		HashSet<TreeObject> TreeObjectChildren = new HashSet<TreeObject>();
		
		if (existTable("tree_object_" + treeObject.getProject().getId())){
		
			Statement stmt = null;
			
			int level;
			String name;
			String id;
			String parentId;
			String type;
			
			try {
				stmt = _connection.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT `ID`,`NAME`,`LEVEL`,`PARENT_ID`,`TYPE` FROM 'tree_object_" + Integer.toString(treeObject.getProject().getId()) + "' WHERE PARENT_ID=" + treeObject.getId()+";");
				
				while(rs.next()){
					id=rs.getString(1);
					name=rs.getString(2);
					level=rs.getInt(3);
					parentId=rs.getString(4);
					type=rs.getString(5);
					Project project=treeObject.getProject();
	
					TreeObjectChildren.add(new TreeObject(project, level, name, id, parentId, type));
				}
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return TreeObjectChildren;
		}
		//Si pas de table existante, retourne un HashSet vide
		return TreeObjectChildren;
	}
	
	/**
	 * Récupération des entités associées à un cours (prof, élèves, salle)
	 */
	public HashSet<TreeObject> getTreeObjectSession(String uid, Project project) {
		
		HashSet<TreeObject> TreeObjectSession = new HashSet<TreeObject>();
		int level;
		String name;
		String id;
		String parentId;
		String type;


		
		if (existTable("correspondence_"+Integer.toString(project.getId()))){
				
			Statement stmt = null;
			
			try {
				stmt = _connection.createStatement();
				ResultSet rs = stmt
						.executeQuery("SELECT * FROM 'correspondence_"+Integer.toString(project.getId())+"' WHERE UID=" + uid +";");
				
				while(rs.next()){
					
					ResultSet rs2 = stmt
							.executeQuery("SELECT * FROM tree_object_" + Integer.toString(project.getId()) + " WHERE ID=" + rs.getString(1)+";");
					
					while(rs2.next()){
						id=rs.getString(1);
						name=rs.getString(2);
						level=rs.getInt(3);
						parentId=rs.getString(4);
						type=rs.getString(5);

						TreeObjectSession.add(new TreeObject(new Project(rs.getInt(6),""), level, name, parentId, type));
					}
				}
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return TreeObjectSession;
		}
		//Si pas de table existante, retourne un HashSet vide
		return TreeObjectSession;
	}

	private void dropTable(String name) {
		Statement stmt = null;
		try {
			stmt = _connection.createStatement();
			stmt.executeUpdate("DROP TABLE '" + name + "';");
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private boolean existTable(String name) {
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
	public synchronized boolean addEvent(Event event, Project project) {
		if (!existTable("event_" + project.getId())) {
			createEventTable(project.getId());
		}
		PreparedStatement stmt;
		try {
			Statement stmtQuery = _connection.createStatement();
			ResultSet rs = stmtQuery.executeQuery("SELECT UID FROM 'event_"
					+ project.getId() + "' WHERE UID = '"
					+ event.getId() + "'");

			if (!rs.next()) {
				String sql = "INSERT INTO 'event_" + Integer.toString(project.getId())
						+ "' "
						+ "(UID, DTSTART, DTEND, SUMMARY, LOCATION, DESCRIPTION) "
						+ "VALUES (?, ?, ?, ?, ?, ?);";
				stmt = _connection.prepareStatement(sql);
	
				stmt.setString(1, event.getId());
				stmt.setString(2, event.getDtstart());
				stmt.setString(3, event.getDtend());
				stmt.setString(4, event.getSummary());
				stmt.setString(5, event.getLocation());
				stmt.setString(6, event.getDescription());
	
				stmt.executeUpdate();
				stmt.close();
			}
			stmtQuery.close();
		} catch (SQLException e) {
			if (DEBUG)
				e.printStackTrace();
			return false;
		}

		return true;
	}


	public synchronized boolean addCorrespondence(Event event, TreeObject treeObject) {
		
		if (!existTable("correspondence_" + treeObject.getProject().getId())) {
			createCorrespondenceTable(treeObject.getProject().getId());
		}

		PreparedStatement stmtUpdate;

		try {

			Statement stmtQuery = _connection.createStatement();
			ResultSet rs = stmtQuery
					.executeQuery("SELECT EVENT_ID, ADE_ID FROM 'correspondence_"
							+ Integer.toString(treeObject.getProject().getId())
							+ "' WHERE EVENT_ID = '" + event.getId()
							+ "' AND ADE_ID = '" + treeObject.getId() + "'");

			if (!rs.next()) {
				String sql = "INSERT INTO 'correspondence_"
						+ Integer.toString(treeObject.getProject().getId()) + "' (EVENT_ID, ADE_ID) "
						+ "VALUES (" + "?, ?);";
				stmtUpdate = _connection.prepareStatement(sql);

				stmtUpdate.setString(1, event.getId());
				stmtUpdate.setString(2, treeObject.getId());

				stmtUpdate.executeUpdate();
				stmtUpdate.close();
			}
			stmtQuery.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public boolean fillCorrespondence(HashSet<Event> events, TreeObject treeObject) {
		Iterator<Event> i = events.iterator();
		while (i.hasNext()) {
			Event event = i.next();
			addCorrespondence(event, treeObject);
		}
		return true;
	}

}
