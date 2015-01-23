package fr.esir.nsoc.tsen.ade.core;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class ADE_Scope {

	private DataBase dataBase;
	private HashSet<TreeObject> scope;
	private LocalDate startPoint;
	private LocalDate endPoint;

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

	public LocalDate getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(LocalDate startPoint) {
		this.startPoint = startPoint;
	}

	public LocalDate getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(LocalDate endPoint) {
		this.endPoint = endPoint;
	}

	public HashSet<TreeObject> getScope() {
		return scope;
	}



}
