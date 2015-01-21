package fr.esir.nsoc.tsen.ade.core;

import java.util.HashSet;
import java.util.Iterator;

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

	public boolean addChildrenToScope(TreeObject treeObject) {
		if (treeObject.getType() == "leaf") {
			scope.add(treeObject);
			return true;
		} else if (treeObject.getType() == "branch") {
			HashSet<TreeObject> tos = dataBase
					.getTreeObjectChildren(treeObject);
			Iterator<TreeObject> i = tos.iterator();
			while (i.hasNext()) {
				scope.add(i.next());
			}
			return true;
		}
		return false;
	}

}
