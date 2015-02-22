package fr.esir.nsoc.tsen.ade.object;

import java.util.HashSet;

public class Correspondence {
	
	private Event event;
	private HashSet<TreeObject> treeObjects;

	public Correspondence() {
		super();
	}
	
	
	public Correspondence(Event event, HashSet<TreeObject> treeObjects) {
		super();
		this.event = event;
		this.treeObjects = treeObjects;
	}
	
	public Event getEvent() {
		return event;
	}
	public void setEvent(Event event) {
		this.event = event;
	}
	public HashSet<TreeObject> getTreeObjects() {
		return treeObjects;
	}
	public void setTreeObjects(HashSet<TreeObject> treeObjects) {
		this.treeObjects = treeObjects;
	}
	
	

}
