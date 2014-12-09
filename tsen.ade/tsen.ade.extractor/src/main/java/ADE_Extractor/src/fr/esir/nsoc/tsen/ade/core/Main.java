package ADE_Extractor.src.fr.esir.nsoc.tsen.ade.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import ADE_Extractor.src.fr.esir.nsoc.tsen.ade.database.SQLiteDB;
import ADE_Extractor.src.fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import ADE_Extractor.src.fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import ADE_Extractor.src.fr.esir.nsoc.tsen.ade.http.parser.CategoryParser;
import ADE_Extractor.src.fr.esir.nsoc.tsen.ade.http.parser.ProjectParser;



public class Main {

	private final static String ADE_SERVER_URL = "http://plannings.univ-rennes1.fr/";
	private final static String ADE_SERVER_URL_S = "https://plannings.univ-rennes1.fr/";
	private final static String ADE_PROJECT_PATH = "ade/standard/projects.jsp";
	private final static String ADE_INTERFACE_PATH = "ade/standard/gui/interface.jsp";
	private final static String ADE_TREE_PATH = "ade/standard/gui/tree.jsp";

	private final static String ADE_STUD_CATEGORY = "trainee";
	private final static String ADE_ROOM_CATEGORY = "room";
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
		HTTP_Requester httpsReq = new HTTP_Requester(ADE_SERVER_URL_S, jSessionId);
		HTTP_Response httpResp = httpReq.sendGet(ADE_PROJECT_PATH);
		if (DEBUG) System.out.println(httpResp.isOk() ? httpResp.getCode() : "err");
		status = ((httpResp.isOk() ? httpResp.getCode() : 0) == 200);
		
		// list & store ADE projects
		if (status)
		{
			ProjectParser pp = new ProjectParser(httpResp.getContent());
			HashMap<Integer, String> projectList = pp.Parse();
			
			// Get a set of the entries
			Set set = projectList.entrySet();
			// Get an iterator
			Iterator i = set.iterator();
			// Display elements
			while(i.hasNext() && DEBUG) {
				Map.Entry me = (Map.Entry)i.next();
				System.out.print(me.getKey() + ": ");
				System.out.println(me.getValue());
			}
			db.FillProject(set);
		}
		
		// select an ADE project
		if (status)
		{
			do {
				System.out.print("Enter a project ID: ");
				projectID = stdin.nextInt();
			} while (false); // TODO check the project id
			try {
				httpResp = httpsReq.sendPost(ADE_INTERFACE_PATH, "projectId=" + projectID);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				status = false;
			}
		}
		
		// list & store category
		if (status)
		{
			httpResp = httpReq.sendGet(ADE_TREE_PATH);
			
			CategoryParser cp = new CategoryParser(httpResp.getContent());
			HashMap<String, String> projectList = cp.Parse();
			
			// Get a set of the entries
			Set set = projectList.entrySet();
			// Get an iterator
			Iterator i = set.iterator();
			// Display elements
			while(i.hasNext() && DEBUG) {
				Map.Entry me = (Map.Entry)i.next();
				System.out.print(me.getKey() + "> ");
				System.out.println(me.getValue());
			}
			db.FillCategory(set, projectID);
			status = false;
		}
		
		
		// exit
		stdin.close();
		System.exit(0);
	}
	
	private static boolean isHexNumber(String input) {
		try {
			while (input.length() > 8)
			{
				Long.parseLong(input.substring(0, 7), 16);
				input = input.substring(8, input.length());
			}
			Long.parseLong(input, 16);
			return true;
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return false;
		}
	}

}
