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

import com.google.gson.Gson;

import fr.esir.nsoc.tsen.ade.object.Project;
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
		String info = "";
		if(function.equals("browse") && cookie!=null){
			ADE_Tree at = new ADE_Tree(universe.getScope().getDataBase());
			info = at.browseTree(cookie, universe.getScope().getProject());
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
