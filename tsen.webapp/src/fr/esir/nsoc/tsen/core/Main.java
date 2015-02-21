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
	private final static String DB_Name = "db.uion.fr:3306/tsen_ade";

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
	}

	@Override
	public void contextInitialized(ServletContextEvent ServletContext) {
		ServletContext context = ServletContext.getServletContext();
		Logger logger = Logger.getLogger(this.getClass().getName());
		
		
		// connect to DB
		DataBase db = new MySQLDB(DB_Name);
		logger.info("Database \"" + DB_Name + "\" " + (db.isConnected() ? "connected" : "not connected"));
		
		
		Universe universe = new Universe(new ADE_Scope(db));		
		context.setAttribute("universe", universe);
	}

}
