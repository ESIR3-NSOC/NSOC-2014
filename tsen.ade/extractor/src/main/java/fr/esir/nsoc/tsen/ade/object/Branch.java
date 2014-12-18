package fr.esir.nsoc.tsen.ade.object;

public class Branch {
	
	private int id;
	private String name;
	private int level;
	private int projectID;
	private String category;
	private boolean branch;
	
	
	public Branch(int id, String name, int level, int projectID, String category, boolean branch) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
		this.projectID = projectID;
		this.category = category;
		this.branch = branch;
	}


	public int getId() {
		return id;
	}


	public String getName() {
		return name;
	}


	public int getLevel() {
		return level;
	}


	public int getProjectID() {
		return projectID;
	}


	public String getCategory() {
		return category;
	}


	public boolean isBranch() {
		return branch;
	}
	
	
	
	
	

}
