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

import fr.esir.nsoc.tsen.ade.object.TreeObject;


/**
 * Servlet Filter implementation class AdminAccessControl
 */

@WebFilter(
		urlPatterns = { "/settings.html" }, 
		servletNames = { 
				"Config", 
				"DbConfig"
		})
public class AdminAccessControl implements Filter {

	public static final String LOGIN_ACCESS  		= "/login";
	public static final String INSUFFICIENT_RIGHT  	= "/error/403.html";
    public static final String ATT_SESSION_USER 	= "user";
    
    
    
    
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
         * l'utilisateur n'est pas connect�.
         */
        if ( session.getAttribute( ATT_SESSION_USER ) == null ) {
        	request.getRequestDispatcher( LOGIN_ACCESS ).forward( request, response );
        } else {
        	TreeObject to = (TreeObject)session.getAttribute( ATT_SESSION_USER );
        	if (to.getId().equals("6579"))
        	{
                chain.doFilter( req, res );
        	}
        	else
        	{
            	request.getRequestDispatcher( INSUFFICIENT_RIGHT ).forward( request, response );
        	}
        }
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

}
