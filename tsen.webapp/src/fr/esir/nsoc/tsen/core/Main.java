package fr.esir.nsoc.tsen.core;

import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.database.MySQLDB;


@WebListener
public class Main implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent ServletContext) {
		ServletContext context = ServletContext.getServletContext();
		Logger logger = Logger.getLogger(this.getClass().getName());
		
		Universe universe = new Universe();
		universe.loadConfig();
		
		// connect to DB
		DataBase db = new MySQLDB(universe.getConfig().getDb_name(), universe.getConfig().getDb_login(), universe.getConfig().getDb_password());
		logger.info("Database \"" + universe.getConfig().getDb_name() + "\" " + (db.isConnected() ? "connected" : "not connected"));

		universe.setScope(new ADE_Scope(db));
		
		context.setAttribute("universe", universe);
	}

}
