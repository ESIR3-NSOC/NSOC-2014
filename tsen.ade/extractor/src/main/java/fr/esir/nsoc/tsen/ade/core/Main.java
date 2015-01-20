package fr.esir.nsoc.tsen.ade.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;





import fr.esir.nsoc.tsen.ade.browser.TreeBrowser;
import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.database.SQLiteDB;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.http.parser.ProjectParser;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class Main {



	private final static boolean DEBUG = true;

	public static void main(String[] args) {
		
		
		// connect to local DB
		DataBase db = new SQLiteDB("test1.db");
		if (DEBUG) System.out.println(db.isConnected() ? "ok" : "nok");
		
		// brows ADE Tree
		ADE_Tree at = new ADE_Tree(db);
		at.browseTree();
		
		
		// example
		// db.getTreeObjectChildren(new TreeObject(project, -1, "", "7748", "", ""));
		 
		 
		 
		// exit
		System.exit(0);
	}



}
