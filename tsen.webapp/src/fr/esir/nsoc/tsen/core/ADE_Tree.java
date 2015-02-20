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
	private Project project;
	
	
	public ADE_Tree(DataBase dataBase) {
		super();
		this.dataBase = dataBase;
		this.project = null;
	}

	public String browseTree(String jSessionId, Project project) {

		// setup
		boolean _continue = true;

		// get a valid ADE JSESSION ID
		if (!((jSessionId.length() == 32) && (isHexNumber(jSessionId)))){
			return "Bad cookie";
		}

		// test ADE JSESSION ID
		HTTP_Requester httpReq = new HTTP_Requester(ADE_SERVER_URL, jSessionId);
		HTTP_Response httpResp = httpReq.sendGet(ADE_PROJECT_PATH, null);
		if (DEBUG)
			System.out.println(httpResp.getCode());
		_continue = httpResp.getCode() == 200;

		// list, select & store ADE projects
		if (_continue) {
			ProjectParser pp = new ProjectParser(httpResp.getContent());
			HashSet<Project> projectList = pp.Parse();

			// Get an iterator
			Iterator<Project> i = projectList.iterator();
			// Display elements
			while (i.hasNext() && DEBUG) {
				Project p = i.next();
				System.out.println(p.getId() + ": " + p.getName());
			}
			
			dataBase.createProjectTable();
			dataBase.fillProject(projectList);
			

			boolean loop = true;
	

				i = projectList.iterator();
				while (i.hasNext()) {
					Project p = i.next();
					if (p.getId() == project.getId())
					{
						project = p;
						loop = false;
					}
				}
				if(loop){
					return "Unknown project id : " + project.getId();
				}
		}
		
		// select an ADE project

		if (_continue) {

			try {
				HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
				parameters.add(new HTTP_Parameter("projectId", Integer
						.toString(project.getId())));
				httpResp = httpReq.sendPost(ADE_INTERFACE_PATH, parameters);
			} catch (Exception e) {
				e.printStackTrace();
				_continue = false;
			}
		}
		
		
		// start browsing ADE tree
		
		if (_continue)
		{
			dataBase.createTreeObjectTable(project.getId());
			TreeBrowser tb = new TreeBrowser(new TreeObject(project, -1, "root", "", "root"), httpReq, dataBase);
			_continue = tb.browse();
		}
	
		return "OK";
	}
	
	
	
	public Project getProject() {
		return project;
	}

	public void setProject(int projectId) {
		this.project = dataBase.getProject(projectId);
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
