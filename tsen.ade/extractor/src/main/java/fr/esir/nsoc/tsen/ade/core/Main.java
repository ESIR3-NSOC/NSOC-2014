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
		//ADE_Tree at = new ADE_Tree(db);
		//at.browseTree();
		
		
		// example
		HashSet<TreeObject> tos = db.getTreeObjectChildren(new TreeObject(new Project(22, ""), -1, "", "7748", "", ""));
		Iterator<TreeObject> i = tos.iterator();
		while (i.hasNext()) {
			TreeObject to = i.next();
			
				if (true) System.out.println(to.getType() + ": \"" + to.getName() + "\", id: \"" + to.getId() + "\", level:" + to.getLevel());

				
				
			}

		// exit
		System.exit(0);
	}



}
