package fr.esir.nsoc.tsen.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import fr.esir.nsoc.tsen.ade.object.TreeObject;
import fr.esir.nsoc.tsen.core.Universe;

/**
 * Servlet implementation class Vote
 */
@WebServlet({ "/Vote", "/vote" })
public class Vote extends HttpServlet {
	
	public static final String ATT_USER         		= "user";
	
	
	
	private static final long serialVersionUID = 1L;
	private ServletConfig config;
	private ServletContext context;
	private Logger logger;
	private Universe universe;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Vote() {
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
		String roomId = request.getParameter("roomId");
		String delay = request.getParameter("delay");
		HashSet<fr.esir.nsoc.tsen.objects.Vote> votes = null;
		if (roomId!= null && delay!= null)
		{
			votes = universe.getDataBase().getVoteByRoomByDelay(universe.getScope().getProject(), roomId, Integer.parseInt(delay));
		}
		PrintWriter pw = response.getWriter();
		Gson gson = new Gson();
		String s = gson.toJson(votes);
		pw.write(s);
		pw.close();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String rate = request.getParameter("rate");
		if (rate!= null)
		{
			HttpSession session = request.getSession();
			Object obj = session.getAttribute(ATT_USER);
			if (obj==null || (!(obj instanceof fr.esir.nsoc.tsen.ade.object.TreeObject))) 
			{
			   obj = null;
			}
			TreeObject userTo = (fr.esir.nsoc.tsen.ade.object.TreeObject)obj;
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = new Date();
			fr.esir.nsoc.tsen.ade.object.Event event = universe.getDataBase().getEventByUserIdByDate(dateFormat.format(new Date()), Integer.parseInt(userTo.getId()), universe.getScope().getProject());
			
			HashSet<TreeObject> rooms = universe.getDataBase().getTreeObjectByEventByRoot(event, "room", universe.getScope().getProject());
			
			for (TreeObject room : rooms)
			{
				universe.getDataBase().addVote(universe.getScope().getProject(), userTo, room, event, rate, dateFormat.format(new Date()));
			}
		}
	}

}
