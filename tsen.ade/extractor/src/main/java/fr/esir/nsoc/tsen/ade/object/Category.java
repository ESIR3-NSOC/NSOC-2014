package fr.esir.nsoc.tsen.ade.object;

import fr.esir.nsoc.tsen.ade.database.DataBase;

public class Category {

	private String id;
	private String name;
	private Project project;

	public Category(String id, String name, Project project) {
		super();
		this.id = id;
		this.name = name;
		this.project = project;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Project getProject() {
		return project;
	}

	public boolean store(DataBase db) {
		return db.addCategory(this);
	}

}
