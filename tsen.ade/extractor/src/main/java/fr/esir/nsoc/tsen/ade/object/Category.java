package fr.esir.nsoc.tsen.ade.object;

import fr.esir.nsoc.tsen.ade.database.DataBase;

public class Category {

	private String id;
	private String name;
	private int projectID;

	public Category(String id, String name, int projectID) {
		super();
		this.id = id;
		this.name = name;
		this.projectID = projectID;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getProjectID() {
		return projectID;
	}
	
	public boolean store(DataBase db){
		return db.addCategory(this);
	}

}
