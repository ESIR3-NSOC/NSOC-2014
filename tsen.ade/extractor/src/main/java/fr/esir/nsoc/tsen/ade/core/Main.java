package fr.esir.nsoc.tsen.ade.core;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.database.MySQLDB;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class Main {

	private final static boolean DEBUG = true;

	public static void main(String[] args) {

		// connect to local DB
		DataBase db = new MySQLDB("tsen_ade");
		if (DEBUG)
			System.out.println(db.isConnected() ? "ok" : "nok");

		// brows ADE Tree
		ADE_Tree at = new ADE_Tree(db);
		
		//at.browseTree();
		at.setProject(22);

		// get project
		Project project = at.getProject();
		if (DEBUG)
			System.out.println(at.getProject().getName());

		// define scope
		ADE_Scope scope = new ADE_Scope(db);
		scope.addChildrenToScope(new TreeObject(project, -1, "", "7748", "",
				"branch")); // esir 3 domo
		scope.addChildrenToScope(new TreeObject(project, -1, "", "7828", "",
				"branch")); // esir 3 mat
		scope.addChildrenToScope(new TreeObject(project, -1, "", "6082", "",
				"branch")); // esir b41 td
		scope.addChildrenToScope(new TreeObject(project, -1, "", "6092", "",
				"branch")); // esir b41 td langue
		scope.addChildrenToScope(new TreeObject(project, -1, "", "2302", "",
				"leaf")); // eric beaty

		
		
		if (DEBUG) {
			HashSet<TreeObject> tos = scope.getScope();
			Iterator<TreeObject> i = tos.iterator();
			while (i.hasNext()) {
				TreeObject to = i.next();
				System.out.println(to.getType() + ": \"" + to.getName()
						+ "\", id: \"" + to.getId() + "\", level:"
						+ to.getLevel());
			}
		}

		scope.setStartPoint(parseDate("19/01/2015"));
		scope.setEndPoint(parseDate("25/01/2015"));

		// retrieve planning
		ADE_Planning planning = new ADE_Planning(db, scope);
		planning.retrieve(30);

		// exit
		//System.out.println("terminated !");
		//System.exit(0);
	}

	public static LocalDate parseDate(String input) {
		LocalDate date = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter
					.ofPattern("dd/MM/yyyy");
			date = LocalDate.parse(input, formatter);
		} catch (DateTimeParseException exc) {
			exc.printStackTrace();
		}
		return date;
	}

	public static String formatDate(LocalDate input) {
		String string = null;
		try {
			DateTimeFormatter format = DateTimeFormatter
					.ofPattern("yyyy-MM-dd");
			string = input.format(format);
		} catch (DateTimeException exc) {
			exc.printStackTrace();
		}
		return string;
	}

}
