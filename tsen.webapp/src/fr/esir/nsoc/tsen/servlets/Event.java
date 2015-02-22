package fr.esir.nsoc.tsen.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import fr.esir.nsoc.tsen.core.Universe;

/**
 * Servlet implementation class Event
 */
@WebServlet({ "/Event", "/event" })
public class Event extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ServletConfig config;
	private ServletContext context;
	private Logger logger;
	private Universe universe;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Event() {
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
		String eventid = request.getParameter("eventid");
		String datetime = request.getParameter("datetime");
		String s = "parameter not recieved";
		PrintWriter pw = response.getWriter();
		Gson gson = new Gson();
		if (eventid!=null) s = gson.toJson(universe.getDataBase().getEventByUID(eventid, universe.getScope().getProject()));
		if(datetime!=null) {
			if(datetime.equals("now")){
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date();
				datetime = dateFormat.format(date);
			}
			s=gson.toJson(universe.getDataBase().getEventByDate(datetime, universe.getScope().getProject()));
		}
		pw.write(s);
		pw.close();
	}

}
