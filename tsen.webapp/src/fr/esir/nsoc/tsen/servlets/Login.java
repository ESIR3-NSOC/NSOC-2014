package fr.esir.nsoc.tsen.servlets;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.esir.nsoc.tsen.core.Universe;
import fr.esir.nsoc.tsen.forms.LoginForm;



/**
 * Servlet implementation class Login
 */
@WebServlet(name = "Login", urlPatterns = {"/Login", "/login"})
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public static final String VUE              		= "/WEB-INF/login.jsp";
    public static final String ATT_USER         		= "user";
    public static final String ATT_FORM         		= "form";
    public static final String ATT_SESSION_USER 		= "user";
	public static final String ATT_PAGE_PATH_REQUESTED	= "pathRequested";

	private ServletConfig config;
	private ServletContext context;
	private Logger logger;
	private Universe universe;
    
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
        this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LoginForm form = new LoginForm();
        fr.esir.nsoc.tsen.ade.object.TreeObject to = form.connect( universe, request );
        //User user = form.connect( universe, request );

        HttpSession session = request.getSession();


        request.setAttribute( ATT_FORM, form );
        request.setAttribute( ATT_USER, to );
        
        if ( form.getErrors().isEmpty() ) {
            session.setAttribute( ATT_SESSION_USER, to );
            String path = (String)request.getAttribute(ATT_PAGE_PATH_REQUESTED);
            if ( path.contains("/login"))
            {
                response.sendRedirect( (String) request.getContextPath() );
            }
            else
            	response.sendRedirect( path );
        } else {
            session.setAttribute( ATT_SESSION_USER, null );
            this.getServletContext().getRequestDispatcher( VUE ).forward( request, response );
        }

        
        
        
        
	}
}
