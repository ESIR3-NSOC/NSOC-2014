package fr.esir.nsoc.tsen.ade.object;

import fr.esir.nsoc.tsen.ade.database.DataBase;

public class TreeObject {

	private Project project;
	private int level;
	private String name;
	private String id;
	private String parentId;
	private String type; // leaf | branch | category

	public TreeObject(Project project, int level, String name, String id,
			String parentId, String type) {
		super();
		this.project = project;
		this.level = level;
		this.name = name;
		this.id = id;
		this.parentId = parentId;
		this.type = type;
	}

	public TreeObject(Project project, int level, String name, String id,
			String type) {
		super();
		this.project = project;
		this.level = level;
		this.name = name;
		this.id = id;
		this.parentId = "";
		this.type = type;
	}

	public boolean store(DataBase db) {
		return db.addTreeObject(this);
	}

	public Project getProject() {
		return project;
	}

	public int getLevel() {
		return level;
	}

	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getType() {
		return type;
	}

	public String toString() {
		return this.getType() + ": \"" + this.getName()
				+ "\", id: \"" + this.getId() + "\", parent id: \"" + this.getParentId() + "\", level:"
				+ this.getLevel();
	}
	
	

}
