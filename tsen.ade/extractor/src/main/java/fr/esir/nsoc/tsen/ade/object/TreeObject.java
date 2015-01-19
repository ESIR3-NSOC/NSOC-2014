package fr.esir.nsoc.tsen.ade.object;

public class TreeObject {
	
	private Project project;
	private int level;
	private String name;
	private String id;
	private boolean leaf;
	private boolean branch;
	private boolean category;
	
	
	public TreeObject(Project project, int level, String name, String id,
			boolean leaf, boolean branch, boolean category) {
		super();
		this.project = project;
		this.level = level;
		this.name = name;
		this.id = id;
		this.leaf = leaf;
		this.branch = branch;
		this.category = category;
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


	public boolean isLeaf() {
		return leaf;
	}


	public boolean isBranch() {
		return branch;
	}


	public boolean isCategory() {
		return category;
	}

	
	
	
}
