package fr.esir.nsoc.tsen.ade.object;

public class Branch {

	private int id;
	private String name;
	private int level;
	private Category category;
	private boolean leaf;

	public Branch(int id, String name, int level, Category category, boolean leaf) {
		super();
		this.id = id;
		this.name = name;
		this.level = level;
		this.category = category;
		this.leaf = leaf;
	}

	public Branch(int id, String name, Branch branch, boolean leaf) {
		super();
		this.id = id;
		this.name = name;
		this.level = branch.getLevel()+1;
		this.category = branch.getCategory();
		this.leaf = leaf;
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

	public Category getCategory() {
		return category;
	}

	public boolean isLeaf() {
		return leaf;
	}

}
