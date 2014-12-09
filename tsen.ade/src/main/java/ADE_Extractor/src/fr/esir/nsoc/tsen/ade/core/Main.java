package ADE_Extractor.src.fr.esir.nsoc.tsen.ade.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import fr.esir.nsoc.tsen.ade.database.SQLiteDB;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.http.parser.ProjectParser;

public class Main {

	private final static String ADE_SERVER_URL = "http://plannings.univ-rennes1.fr/";
	private final static String ADE_PROJECT_PATH = "ade/standard/projects.jsp";
	private final static String ADE_INTERFACE_PATH = "ade/standard/gui/interface.jsp";
	private final static boolean DEBUG = true;
			
			
	public static void main(String[] args) {
		
		// setup
		Scanner stdin = new Scanner(System.in);
		boolean status = true; // OK
		
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
			while(i.hasNext()) {
				Map.Entry me = (Map.Entry)i.next();
				System.out.print(me.getKey() + ": ");
				System.out.println(me.getValue());
			}
			db.FillProject(set);
		}
		
		// select an ADE project
		if (status)
		{
			int projectId = 0;
			do {
				System.out.print("Enter a project ID: ");
				projectId = stdin.nextInt();
			} while (false); // TODO check the project id
			try {
				httpResp = httpReq.sendPost(ADE_INTERFACE_PATH, "projectId=" + projectId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
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
