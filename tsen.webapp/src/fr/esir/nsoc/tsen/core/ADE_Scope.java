package fr.esir.nsoc.tsen.core;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class ADE_Scope {

	private DataBase dataBase;
	private HashSet<TreeObject> scope;
	private LocalDate startPoint;
	private LocalDate endPoint;
	private Project project;

	public ADE_Scope(DataBase dataBase) {
		super();
		this.dataBase = dataBase;
		this.scope = new HashSet<TreeObject>();
	}

	public ADE_Scope(ADE_Scope scope) {
		super();
		this.dataBase = scope.getDataBase();
		this.scope = scope.getScope();
	}

	public boolean addChildrenToScope(TreeObject treeObject) {
		if (treeObject.getType().equals("leaf")) {
			scope.add(treeObject);
			System.out.println("ADD: " + treeObject.getType() + ": \"" + treeObject.getName()
					+ "\", id: \"" + treeObject.getId() + "\", level:"
					+ treeObject.getLevel());
			return true;
		} else if (treeObject.getType().equals("branch")) {
			HashSet<TreeObject> tos = dataBase
					.getTreeObjectChildren(treeObject);
			Iterator<TreeObject> i = tos.iterator();
			while (i.hasNext()) {
				TreeObject to = i.next();
				new ADE_Scope(this).addChildrenToScope(to);
				System.out.println("---: " + to.getType() + ": \"" + to.getName()
						+ "\", id: \"" + to.getId() + "\", level:"
						+ to.getLevel());
				//scope.add(i.next());
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

	public DataBase getDataBase() {
		return dataBase;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}





}
