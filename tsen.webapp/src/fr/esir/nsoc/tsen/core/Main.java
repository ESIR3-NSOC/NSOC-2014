package fr.esir.nsoc.tsen.core;

import java.util.ArrayList;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;



@WebListener
public class Main implements ServletContextListener {
	private static final boolean DEBUG = false;

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent ServletContext) {
		ServletContext context = ServletContext.getServletContext();
		Logger logger = Logger.getLogger(this.getClass().getName());
		
		Universe universe = new Universe();
		
		context.setAttribute("universe", universe);
	}

}
