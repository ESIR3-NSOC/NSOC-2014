package fr.esir.nsoc.tsen.ade.browser;

import java.util.HashSet;
import java.util.Iterator;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.http.parser.TreeParser;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class TreeBrowser {
	
	private final static String ADE_TREE_PATH = "ade/standard/gui/tree.jsp";
	
	private final static boolean DEBUG = true;
	
	
	private TreeObject treeObject;
	private HTTP_Requester httpReq;
	private DataBase db;
	

	public TreeBrowser(TreeObject treeObject, HTTP_Requester httpReq, DataBase db) {
		super();
		this.treeObject = treeObject;
		this.httpReq = httpReq;
		this.db = db;
	}
	
	
	public TreeBrowser(TreeObject treeObject, TreeBrowser treeBrowser) {
		super();
		this.treeObject = treeObject;
		this.httpReq = treeBrowser.getHttpReq();
		this.db = treeBrowser.getDb();
	}


	public boolean browse() {
		HTTP_Response httpResp = null;
		
		if (treeObject.getLevel() == -1) // starting the tree
		{
			httpResp = httpReq.sendGet(ADE_TREE_PATH, null);
			TreeParser tp = new TreeParser(httpResp.getContent(), treeObject.getProject());
			HashSet<TreeObject> treeObjects = tp.Parse();
			
			Iterator<TreeObject> i = treeObjects.iterator();
			while (i.hasNext()) {
				TreeObject to = i.next();
				to.setRootId(to.getId());
				if (to.getLevel() == (treeObject.getLevel() + 1))
				{
					// display if debug
					if (DEBUG) System.out.println(to.getType() + ": \"" + to.getName() + "\", id: \"" + to.getId() + "\", level:" + to.getLevel());
					// store
					to.store(db);
					// browse
					new TreeBrowser(to, this).browse();
					
				}
			}
		} else if (treeObject.getLevel() == 0) {
			// open
			HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
			parameters.add(new HTTP_Parameter("category", treeObject.getId()));
			parameters.add(new HTTP_Parameter("expand", "false"));
			parameters.add(new HTTP_Parameter("forceLoad", "false"));
			parameters.add(new HTTP_Parameter("reload", "0"));
			
			httpResp = httpReq.sendGet(ADE_TREE_PATH, parameters);
			
			TreeParser tp = new TreeParser(httpResp.getContent(), treeObject.getProject());
			HashSet<TreeObject> treeObjects = tp.Parse();
			Iterator<TreeObject> i = treeObjects.iterator();
			while (i.hasNext()) {
				TreeObject to = i.next();
				to.setRootId(treeObject.getRootId());
				if (to.getLevel() == (treeObject.getLevel() + 1))
				{
					// set parent id
					to.setParentId(treeObject.getId());
					// display if debug
					if (DEBUG) System.out.println(to.getType() + ": \"" + to.getName() + "\", id: \"" + to.getId() + "\", parent id: \"" + to.getParentId() + "\", level:" + to.getLevel());
					// store
					to.store(db);
					// browse
					if (!to.getType().equals("leaf")) new TreeBrowser(to, this).browse();
				}
			}
			// close
			httpResp = httpReq.sendGet(ADE_TREE_PATH, parameters);
		} else {
			// open
			HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
			parameters.add(new HTTP_Parameter("branchId", treeObject.getId()));
			parameters.add(new HTTP_Parameter("expand", "false"));
			parameters.add(new HTTP_Parameter("forceLoad", "false"));
			parameters.add(new HTTP_Parameter("reload", "0"));
			
			httpResp = httpReq.sendGet(ADE_TREE_PATH, parameters);
			
			TreeParser tp = new TreeParser(httpResp.getContent(), treeObject.getProject());
			HashSet<TreeObject> treeObjects = tp.Parse();
			Iterator<TreeObject> i = treeObjects.iterator();
			while (i.hasNext()) {
				TreeObject to = i.next();
				to.setRootId(treeObject.getRootId());
				if (to.getLevel() == (treeObject.getLevel() + 1))
				{
					// set parent id
					to.setParentId(treeObject.getId());
					// display if debug
					if (DEBUG) System.out.println(to.getType() + ": \"" + to.getName() + "\", id: \"" + to.getId() + "\", parent id: \"" + to.getParentId() + "\", level:" + to.getLevel());
					// store
					to.store(db);
					// browse
					if (!to.getType().equals("leaf")) new TreeBrowser(to, this).browse();
				}
			}
			// close
			httpResp = httpReq.sendGet(ADE_TREE_PATH, parameters);
		}

		return true; // to finish
	}
	
	
	


	public TreeObject getTreeObject() {
		return treeObject;
	}


	public HTTP_Requester getHttpReq() {
		return httpReq;
	}


	public DataBase getDb() {
		return db;
	}
	
	
	
	
	
	
	
	

}
