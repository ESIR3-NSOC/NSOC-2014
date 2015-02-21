package fr.esir.nsoc.tsen.core;

import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.browser.TreeBrowser;
import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.http.parser.ProjectParser;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class ADE_Tree {
	

	private final static boolean DEBUG = true;
	
	private final static String ADE_SERVER_URL = "https://plannings.univ-rennes1.fr/";
	private final static String ADE_PROJECT_PATH = "ade/standard/projects.jsp";
	private final static String ADE_INTERFACE_PATH = "ade/standard/gui/interface.jsp";
	
	private DataBase dataBase;
	
	private HTTP_Requester httpReq;
	private HTTP_Response httpResp;
	private HashSet<Project> projectList;
	
	public ADE_Tree(DataBase dataBase) {
		super();
		this.dataBase = dataBase;
	}

	
	public int listProject(String jSessionId) {
		if (!checkCookie(jSessionId)) return 2; // bad cookie format
		if (DEBUG) System.out.println(jSessionId);
		
		// test ADE JSESSION ID
		httpReq = new HTTP_Requester(ADE_SERVER_URL, jSessionId);
		httpResp = httpReq.sendGet(ADE_PROJECT_PATH, null);
		
		if (httpResp.getCode() != 200) return 3; // invalid ade cookie	
		if (DEBUG) System.out.println(httpResp.getCode());

		// list, select & store ADE projects
		ProjectParser pp = new ProjectParser(httpResp.getContent());
		projectList = pp.Parse();

		// Display elements
		Iterator<Project> i = projectList.iterator();
		while (i.hasNext() && DEBUG) {
			Project p = i.next();
			System.out.println(p.getId() + ": " + p.getName());
		}
		
		dataBase.createProjectTable();
		dataBase.fillProject(projectList);
		
		return 0; // all good
	}		
		
		
		
		
		



		
		

	public int browseTree(String jSessionId, Project project) {

		int errCode = listProject(jSessionId);
		if (errCode!=0) return errCode;
		
		// select an ADE project
		boolean exist = false;
		Iterator<Project>i = projectList.iterator();
		while (i.hasNext()) {
			Project p = i.next();
			if (p.getId() == project.getId())
			{
				project = p;
				exist = true;
			}
		}
		if(!exist) return 4; // Unknown project id

		try {
			HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
			parameters.add(new HTTP_Parameter("projectId", Integer
					.toString(project.getId())));
			httpResp = httpReq.sendPost(ADE_INTERFACE_PATH, parameters);
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			return 1; // unknown error
		}
		
		
		// start browsing ADE tree
		dataBase.createTreeObjectTable(project.getId());
		TreeBrowser tb = new TreeBrowser(new TreeObject(project, -1, "root", "", "root"), httpReq, dataBase);
		return (tb.browse() ? 0 : 5); // all good | browsing tree error
	}
	
	private boolean checkCookie(String cookie){
		return ((cookie.length() == 32) && (isHexNumber(cookie)));
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
