package fr.esir.nsoc.tsen.ade.browser;

import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.http.parser.BranchParser;
import fr.esir.nsoc.tsen.ade.object.Branch;
import fr.esir.nsoc.tsen.ade.object.Category;
import fr.esir.nsoc.tsen.ade.object.Leaf;

public class CategoryBrowser {

	private final static String ADE_TREE_PATH = "ade/standard/gui/tree.jsp";
	
	private final static boolean DEBUG = true;

	private Category category;
	private int projectID;
	private HTTP_Requester httpReq;
	private DataBase db;
	
	
	public CategoryBrowser(Category category, int projectID, HTTP_Requester httpReq, DataBase db) {
		super();
		this.category = category;
		this.projectID = projectID;
		this.httpReq = httpReq;
		this.db = db;
	}
	
	public boolean browse(){
		HTTP_Response httpResp = null;
		try {
			HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
			parameters.add(new HTTP_Parameter("category", category.getId()));
			httpResp = httpReq.sendGet(ADE_TREE_PATH, parameters);
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			return false;
		}
		
		if (httpResp != null)
		{
			BranchParser bp = new BranchParser(httpResp.getContent(), category.getProjectID() , category.getName());			

			Iterator<Branch> i = bp.Parse().iterator();
			while(i.hasNext()) {
				Branch b = i.next();
				if (b.isBranch())
				{
					
				}
				else
				{
					Leaf l = new Leaf(b);
				}
				

			}
		}
		else
		{
			return false;
		}
		
		return true;
	}
	
	
}
