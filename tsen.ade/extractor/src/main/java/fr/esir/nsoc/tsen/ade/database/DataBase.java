package fr.esir.nsoc.tsen.ade.database;

import java.util.HashSet;
import java.util.Set;

import fr.esir.nsoc.tsen.ade.object.Event;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public interface DataBase {
	
	public boolean isConnected();

	public void CreateProjectTable();
	public boolean addProject(Project project);
	public void FillProject(HashSet<Project> projects);
	public Project getProject(int projectId);
	
	public void CreateTreeObjectTable();
	public boolean addTreeObject(TreeObject treeObject);
	public HashSet<TreeObject> getTreeObjectChildren(TreeObject treeObject);
	
	public void CreateEventTable(int project_ID);
	public boolean addADE_Event(Event _ADE_Event, Project project);
	public boolean FillEvent(Set<Event> set, int projectid);
	
	public void CreateUidTable(int project_ID);
	public boolean addUid(Event _ADE_Event, TreeObject treeObject, Project project);
	public boolean FillUid(Set<Event> set, String adeid, int projectid);

}
