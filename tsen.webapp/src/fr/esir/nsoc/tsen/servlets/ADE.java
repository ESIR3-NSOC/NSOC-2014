package fr.esir.nsoc.tsen.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.esir.nsoc.tsen.ade.object.TreeObject;
import fr.esir.nsoc.tsen.config.ScopeObject;
import fr.esir.nsoc.tsen.core.ADE_Planning;
import fr.esir.nsoc.tsen.core.ADE_Scope;
import fr.esir.nsoc.tsen.core.ADE_Tree;
import fr.esir.nsoc.tsen.core.Universe;

/**
 * Servlet implementation class ADE
 */
@WebServlet({ "/ADE", "/ade" })
public class ADE extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletConfig config;
	private ServletContext context;
	private Logger logger;
	private Universe universe;
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ADE() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config=config;
		ServletConfig sc = config;
		context = sc.getServletContext();

		logger = Logger.getLogger(this.getClass().getName());
		Object obj = context.getAttribute("universe");
		if (obj==null || (!(obj instanceof fr.esir.nsoc.tsen.core.Universe))) 
		{
		   obj = new fr.esir.nsoc.tsen.core.Universe();
		   this.getServletContext().setAttribute("universe", obj);
		}
		universe = (fr.esir.nsoc.tsen.core.Universe)obj;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String function = request.getParameter("function");
		String cookie = request.getParameter("cookie");
		String startDate = request.getParameter("startDate");
		String stopDate = request.getParameter("stopDate");
		int code = -1;
		String info = "";
		if (function!=null) 
		{
			
			if(function.equals("browse") && cookie!=null){
				ADE_Tree at = new ADE_Tree(universe.getDataBase());
				code = at.browseTree(cookie, universe.getScope().getProject());
			} else if(function.equals("list_project") && cookie!=null){
				ADE_Tree at = new ADE_Tree(universe.getDataBase());
				code = at.listProject(cookie);
			} else if(function.equals("sync_planning") && startDate!=null && stopDate!=null) {
				universe.getScope().setStartPoint(parseDate(startDate));
				universe.getScope().setEndPoint(parseDate(stopDate));
				
				universe.getScope().dropScope();
				ADE_Scope scope= universe.getScope();
				for(ScopeObject obj : universe.getConfig().getScopeObjects())
				{
					scope.addChildrenToScope(new TreeObject(scope.getProject(), -1, "", obj.getId(), "", "", "branch"));
				}
				
				ADE_Planning planning = new ADE_Planning(universe.getDataBase(), universe.getScope());
				planning.retrieve(universe.getConfig().getIcsThreadPoolSize());
				code = 0;
			}
			
			switch (code)
			{
				case -1 : 
					info = "Unknown function";
					break;
				case 0 :
					info = "All good";
					break;
				case 1 :
					info = "Unknown error";
					break;
				case 2 :
					info = "Bad cookie format";
					break;
				case 3 :
					info = "Invalid ade cookie";
					break;
				case 4 :
					info = "Unknown project id";
					break;
				case 5 :
					info = "Browsing tree error";
					break;
				default :
					info = "Unknown error";
					break;
					
			}
		
		}
		else
		{
			info = "function parameter not recieved";
		}
		PrintWriter pw = response.getWriter();
		String s = "{\"info\" : \"" + info + "\"}";
		pw.write(s);
		pw.close();
	}
	
	private static LocalDate parseDate(String input) {
		LocalDate date = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter
					.ofPattern("dd-MM-yyyy");
			date = LocalDate.parse(input, formatter);
		} catch (DateTimeParseException exc) {
			exc.printStackTrace();
		}
		return date;
	}

	
}
