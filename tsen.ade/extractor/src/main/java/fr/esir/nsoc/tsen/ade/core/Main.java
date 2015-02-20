package fr.esir.nsoc.tsen.ade.core;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.database.MySQLDB;
import fr.esir.nsoc.tsen.ade.database.SQLiteDB;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class Main {

	private final static boolean DEBUG = true;

	public static void main(String[] args) {


		// connect to local DB
		//DataBase db = new SQLiteDB("test1.db");
		DataBase db = new MySQLDB("tsen_ade");
		if (DEBUG)
			System.out.println(db.isConnected() ? "ok" : "nok");

		// brows ADE Tree
		ADE_Tree at = new ADE_Tree(db);
		
		at.browseTree();
		at.setProject(22);

		// get project
		Project project = at.getProject();
		if (DEBUG)
			System.out.println(at.getProject().getName());

		// define scope
		ADE_Scope scope = new ADE_Scope(db);

		//Etudiants
		scope.addChildrenToScope(new TreeObject(project, -1, "", "5864", "", "branch")); // esir1
		scope.addChildrenToScope(new TreeObject(project, -1, "", "5598", "", "branch")); // esir2
		scope.addChildrenToScope(new TreeObject(project, -1, "", "5856", "", "branch")); // esir3
		//Professeurs
		scope.addChildrenToScope(new TreeObject(project, -1, "", "5436", "", "branch")); // esir prof A-K
		scope.addChildrenToScope(new TreeObject(project, -1, "", "5446", "", "branch")); // esir prof L-W
		scope.addChildrenToScope(new TreeObject(project, -1, "", "6940", "", "branch")); // esir Personnel IATOSS
		scope.addChildrenToScope(new TreeObject(project, -1, "", "988", "", "branch")); // esir Vacataires et extérieurs
		scope.addChildrenToScope(new TreeObject(project, -1, "", "1149", "", "branch")); // scelva prof
		scope.addChildrenToScope(new TreeObject(project, -1, "", "4217", "", "branch")); // istic prof
		//Salles
		scope.addChildrenToScope(new TreeObject(project, -1, "",  "346", "", "branch")); // Esir	
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3635", "", "branch")); // Bat.5
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3636", "", "branch")); // Bat.6
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3865", "", "branch")); // Istic Anglais
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3972", "", "branch")); // Istic CM/TD
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3806", "", "branch")); // Istic Projet
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3820", "", "branch")); // Istic Projets avec code
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3809", "", "branch")); // Istic TEEO
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3935", "", "branch")); // Istic TP
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3839", "", "branch")); // Istic TP priorité aux L1-L2 (LINUX)
		scope.addChildrenToScope(new TreeObject(project, -1, "", "3805", "", "branch")); // Istic TP spéciaux
		scope.addChildrenToScope(new TreeObject(project, -1, "", "1695", "", "branch")); // Bat.28 Amphis

		
		/*
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

		scope.setStartPoint(parseDate("02/02/2015"));
		scope.setEndPoint(parseDate("14/02/2015"));

		// retrieve planning
		ADE_Planning planning = new ADE_Planning(db, scope);
		planning.retrieve(30);
*/
		// exit
		System.out.println("terminated !");
		System.exit(0);
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
