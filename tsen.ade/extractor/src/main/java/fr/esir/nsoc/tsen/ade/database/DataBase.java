package fr.esir.nsoc.tsen.ade.database;

import java.util.HashSet;
import java.util.Set;

import com.esir.nsoc.ade.parser.ADE_Event;

import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public interface DataBase {
	
	public boolean isConnected();

	public void CreateProjectTable();
	public boolean addProject(Project project);
	public void FillProject(HashSet<Project> projects);
	
	public void CreateTreeObjectTable();
	public boolean addTreeObject(TreeObject treeObject);
	public HashSet<TreeObject> getTreeObjectChildren(TreeObject treeObject);
	
	public void CreateEventTable(int project_ID);
	public boolean FillEvent(Set<ADE_Event> set, int projectid, String firstdate);
	
	public void CreateUidTable(String firstdate);
	public boolean FillUid(Set<ADE_Event> set, int adeid, int projectid, String firstdate);
	
	public String getNameTable(String firstdate);
}
