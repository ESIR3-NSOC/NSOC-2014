package fr.esir.nsoc.tsen.ade.core;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;

import fr.esir.nsoc.tsen.ade.browser.BranchBrowser;
import fr.esir.nsoc.tsen.ade.database.SQLiteDB;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.http.parser.CategoryParser;
import fr.esir.nsoc.tsen.ade.http.parser.ProjectParser;
import fr.esir.nsoc.tsen.ade.object.Branch;
import fr.esir.nsoc.tsen.ade.object.Category;
import fr.esir.nsoc.tsen.ade.object.Project;

public class Main {

	private final static String ADE_SERVER_URL = "https://plannings.univ-rennes1.fr/";
	private final static String ADE_PROJECT_PATH = "ade/standard/projects.jsp";
	private final static String ADE_INTERFACE_PATH = "ade/standard/gui/interface.jsp";
	private final static String ADE_TREE_PATH = "ade/standard/gui/tree.jsp";

	@SuppressWarnings("unused")
	private final static String ADE_STUD_CATEGORY = "trainee";
	@SuppressWarnings("unused")
	private final static String ADE_ROOM_CATEGORY = "room";
	@SuppressWarnings("unused")
	private final static String ADE_PROF_CATEGORY = "instructor";

	private final static boolean DEBUG = true;

	public static void main(String[] args) {
		
		// setup
		Scanner stdin = new Scanner(System.in);
		boolean status = true; // OK
		int projectID = 0;
		
		// connect to local DB
		SQLiteDB db = new SQLiteDB("test1.db");
		System.out.println(db.isConnected() ? "ok" : "nok");
		
		// get a valid ADE JSESSION ID
		String jSessionId = "";
		do {
			System.out.print("Enter a valid JSESSION ID: ");
			jSessionId = stdin.next();
			if (DEBUG) System.out.println(jSessionId.length());
			if (DEBUG) System.out.println(isHexNumber(jSessionId) ? "ok" : "nok");
		} while (!((jSessionId.length() == 32) && (isHexNumber(jSessionId))));
		
		// test ADE JSESSION ID
		HTTP_Requester httpReq = new HTTP_Requester(ADE_SERVER_URL, jSessionId);
		HTTP_Response httpResp = httpReq.sendGet(ADE_PROJECT_PATH, null);
		if (DEBUG) System.out.println(httpResp.getCode());
		status = httpResp.getCode() == 200;
		
		// list & store ADE projects
		if (status)
		{
			ProjectParser pp = new ProjectParser(httpResp.getContent());
			HashSet<Project> projectList = pp.Parse();
			
			// Get an iterator
			Iterator<Project> i = projectList.iterator();
			// Display elements
			while(i.hasNext() && DEBUG) {
				Project p = i.next();
				System.out.println(p.getId() + ": " + p.getName());
			}
			db.FillProject(projectList);
		}
		
		// select an ADE project
		if (status)
		{
			do {
				System.out.print("Enter a project ID: ");
				projectID = stdin.nextInt();
			} while (false); // TODO check the project id
			try {
				HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
				parameters.add(new HTTP_Parameter("projectId", Integer.toString(projectID)));
				httpResp = httpReq.sendPost(ADE_INTERFACE_PATH, parameters);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = false;
			}
		}
		
		
		// list & store category
		if (status)
		{
			httpResp = httpReq.sendGet(ADE_TREE_PATH, null);
			
			CategoryParser cp = new CategoryParser(httpResp.getContent(), projectID);

			Iterator<Category> i = cp.Parse().iterator();
			while(i.hasNext()) {
				Category cat = i.next();
				if (DEBUG) System.out.println(cat.getId() + "> " + cat.getName());
				cat.store(db);
				new BranchBrowser(new Branch(0, cat.getName(), 0, cat, false), httpReq, db).browse();
			}
			status = false;
		}
		
		
		// exit
		stdin.close();
		System.exit(0);
	}

	private static boolean isHexNumber(String input) {
		try {
			while (input.length() > 8) {
				Long.parseLong(input.substring(0, 7), 16);
				input = input.substring(8, input.length());
			}
			Long.parseLong(input, 16);
			return true;
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			return false;
		}
	}

}
