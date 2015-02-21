package fr.esir.nsoc.tsen.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String function = request.getParameter("function");
		String cookie = request.getParameter("cookie");
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
