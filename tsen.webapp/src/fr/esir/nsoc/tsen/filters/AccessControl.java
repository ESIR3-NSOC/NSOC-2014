package fr.esir.nsoc.tsen.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class AccessControl
 */
@WebFilter("/*")
public class AccessControl implements Filter {

	public static final String LOGIN_ACCESS  			= "/login";
	public static final String ATT_PAGE_PATH_REQUESTED	= "pathRequested";
    public static final String ATT_SESSION_USER 		= "user";
	
	
	
    /**
     * Default constructor. 
     */
    public AccessControl() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        HttpSession session = request.getSession();

        
        
        String path = request.getRequestURI().substring( request.getContextPath().length() );
        
        if ( path.startsWith( "/static/public" ) ) {
            chain.doFilter( req, res );
            return;
        }
        
        /**
         * Si l'objet utilisateur n'existe pas dans la session en cours, alors
         * l'utilisateur n'est pas connecté.
         */
        if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
        	request.setAttribute( ATT_PAGE_PATH_REQUESTED, request.getRequestURI() );
        	request.getRequestDispatcher( LOGIN_ACCESS ).forward( request, response );
        } else {
            chain.doFilter( req, res );
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
