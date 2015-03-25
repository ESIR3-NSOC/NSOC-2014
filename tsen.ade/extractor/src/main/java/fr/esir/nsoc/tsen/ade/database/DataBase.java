package fr.esir.nsoc.tsen.ade.database;

import java.sql.ResultSet;
import java.util.HashSet;
import java.util.Set;

import fr.esir.nsoc.tsen.ade.object.Event;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public interface DataBase {
	
	public boolean isConnected();

	public void createProjectTable();
	public boolean addProject(Project project);
	public void fillProject(HashSet<Project> projects);
	public Project getProject(int projectId);
	
	public void createTreeObjectTable(int projectid);
	public boolean addTreeObject(TreeObject treeObject);
	public HashSet<TreeObject> getTreeObjectChildren(TreeObject treeObject);
	
	public void createEventTable(int project_ID);
	public boolean addEvent(Event event, Project project);
	public boolean fillEvent(Set<Event> set, int projectid);
	/*
	public void createUidTable(int project_ID);
	public boolean fillUid(Set<Event> set, String adeid, int projectid);
	
	*/
	
	public boolean addCorrespondence(Event event, TreeObject treeObject);
	public boolean fillCorrespondence(HashSet<Event> events, TreeObject treeObject);
	public HashSet<TreeObject> getTreeObjectSession(String UID, Project projet);

    public ResultSet executeQuery(String query);

}
