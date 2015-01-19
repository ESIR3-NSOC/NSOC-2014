package fr.esir.nsoc.tsen.ade.browser;

import java.util.HashSet;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.object.Branch;

public class TreeBrowser {
	
	private final static String ADE_TREE_PATH = "ade/standard/gui/tree.jsp";
	
	private final static boolean DEBUG = true;
	
	
	private Branch branch;
	private HTTP_Requester httpReq;
	private DataBase db;
	

	public TreeBrowser(Branch branch, HTTP_Requester httpReq, DataBase db) {
		super();
		this.branch =  branch;
		this.httpReq = httpReq;
		this.db = db;
	}
	
	
	public TreeBrowser(Branch branch, TreeBrowser treeBrowser) {
		super();
		this.branch =  branch;
		this.httpReq = treeBrowser.getHttpReq();
		this.db = treeBrowser.getDb();
	}


	public boolean browse() {
		HTTP_Response httpResp = null;
		try {
			HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
			
			
			
			//
			parameters.add(new HTTP_Parameter("category", branch.getCategory().getId()));
			
			
			
			
			httpResp = httpReq.sendGet(ADE_TREE_PATH, parameters);
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			return false;
		}
		
		if (httpResp != null)
		{	
		
		}
		return false; // to finish
	}
	
	
	
	
	public Branch getBranch() {
		return branch;
	}


	public HTTP_Requester getHttpReq() {
		return httpReq;
	}


	public DataBase getDb() {
		return db;
	}
	
	
	
	
	
	
	
	

}
