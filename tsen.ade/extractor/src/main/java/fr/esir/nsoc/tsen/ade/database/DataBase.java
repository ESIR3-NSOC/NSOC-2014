package fr.esir.nsoc.tsen.ade.database;

import java.util.HashSet;

import fr.esir.nsoc.tsen.ade.object.Category;
import fr.esir.nsoc.tsen.ade.object.Project;

public interface DataBase {
	
	public void FillProject(HashSet<Project> projects);
	public void FillCategory(HashSet<Category> Categories);
	public boolean addCategory(Category category);
	public boolean addProject(Project project);

}
