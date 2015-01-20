package fr.esir.nsoc.tsen.ade.database;

import java.util.HashSet;

import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public interface DataBase {
	
	public boolean isConnected();

	public void CreateProjectTable();
	public boolean addProject(Project project);
	public void FillProject(HashSet<Project> projects);
	
	public void CreateTreeObjectTable();
	public boolean addTreeObject(TreeObject treeObject);

}
