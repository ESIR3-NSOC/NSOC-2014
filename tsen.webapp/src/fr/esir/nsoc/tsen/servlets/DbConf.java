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

import fr.esir.nsoc.tsen.core.ADE_Planning;
import fr.esir.nsoc.tsen.core.ADE_Tree;
import fr.esir.nsoc.tsen.core.Universe;

/**
 * Servlet implementation class DbConf
 */
@WebServlet(name = "DbConfig", urlPatterns = { "/DBConf", "/dbconf" })
public class DbConf extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletConfig config;
	private ServletContext context;
	private Logger logger;
	private Universe universe;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DbConf() {
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
		PrintWriter pw = response.getWriter();
		Gson gson = new Gson();
		String s = gson.toJson(universe.getConfig().getDatabase());
		pw.write(s);
		pw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String db_name = request.getParameter("db_name");
		String db_login = request.getParameter("db_login");
		String db_password = request.getParameter("db_password");
		
		if (db_name!=null && db_login!=null && db_password!=null)
		{
			universe.getConfig().getDatabase().setDb_login(db_login);
			universe.getConfig().getDatabase().setDb_name(db_name);
			universe.getConfig().getDatabase().setDb_password(db_password);
			universe.saveConfig();
			universe.refreshConfig();
		}
		doGet(request, response);
	}

}
