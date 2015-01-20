package fr.esir.nsoc.tsen.ade.core;

import java.util.HashSet;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class ADE_Scope {
	
	private final static boolean DEBUG = true;
	
	private DataBase dataBase;
	private HashSet<TreeObject> scope;

	public ADE_Scope(DataBase dataBase) {
		super();
		this.dataBase = dataBase;
		this.scope = new HashSet<TreeObject>();
	}
	
	public boolean addChildrenToScope(TreeObject to) {
		if (to.getType() == "leaf")
		{
			scope.add(to);
			return true;
		} else if (to.getType() == "branch") {
			scope.add(to);
			return true;
		}
		return false;
	}
	
	

}
