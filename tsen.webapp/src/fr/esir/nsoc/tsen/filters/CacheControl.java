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
 * Servlet Filter implementation class CacheControl
 */
@WebFilter("/*")
public class CacheControl implements Filter {

    /**
     * Default constructor. 
     */
    public CacheControl() {
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
        if ( path.startsWith( "/static" ) ) {
            chain.doFilter( req, res );
            return;
        }
		
		response.addHeader("Cache-Control", "private, max-age=0, no-cache, no-store, must-revalidate");
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Expires", "-1");
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
