package fr.esir.nsoc.tsen.ade.parser;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.Callable;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.ade.database.MySQLDB;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.object.Event;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class ICSExtractor implements Callable<Boolean> {

	private final static String ADE_SERVER_URL = "https://plannings.univ-rennes1.fr/";
	private final static boolean DEBUG = true;
	
	private TreeObject treeObject;
	private LocalDate startPoint;
	private LocalDate endPoint;
	private DataBase dataBase;
	



	public ICSExtractor(TreeObject treeObject, LocalDate startPoint,
			LocalDate endPoint, DataBase dataBase) {
		super();
		this.treeObject = treeObject;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.dataBase = dataBase;
	}

	@Override
	public Boolean call() throws Exception {
		boolean statut = false;
		//Récupération de l'ICS
		Cal cal = new Cal(getICS());
				


		// Get a set of the entries
		HashSet<Event> _ADE_Events = cal.parse();
		//Test si l'ICS à bien été interprété
		if(_ADE_Events!=null){
			Iterator<Event> i = _ADE_Events.iterator();
			while (i.hasNext()) {
				Event event = i.next();
				dataBase.addCorrespondence(event, treeObject);
				dataBase.addEvent(event, treeObject.getProject());
			}
			statut=true;
		}

		return statut;
	}
	
	
	private String getICS(){
		HTTP_Requester httpReq = new HTTP_Requester(ADE_SERVER_URL, "");
		HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();

		parameters.add(new HTTP_Parameter("resources", treeObject.getId()));
		parameters.add(new HTTP_Parameter("calType", "ical"));
		parameters.add(new HTTP_Parameter("firstDate", formatDate(startPoint)));
		parameters.add(new HTTP_Parameter("lastDate", formatDate(endPoint)));
		parameters.add(new HTTP_Parameter("login", "cal"));
		parameters.add(new HTTP_Parameter("password", "visu"));
		parameters.add(new HTTP_Parameter("projectId", Integer.toString(treeObject.getProject().getId())));
		HTTP_Response httpResp = httpReq.sendGet("ade/custom/modules/plannings/direct_cal.jsp", parameters);
		
		if (DEBUG) System.out.println(httpResp.getCode()==200 ? httpResp.getCode() : "err");
		return httpResp.getContent();
	}

	
	public static LocalDate parseDate (String input) {
		LocalDate date = null;
		try {
		    DateTimeFormatter formatter =
		                      DateTimeFormatter.ofPattern("dd/MM/yyyy");
		    date = LocalDate.parse(input, formatter);
		}
		catch (DateTimeParseException exc) {
			exc.printStackTrace();
		}
		return date;
	}

	public static String formatDate (LocalDate input) {
		String string = null;
		try {
		    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		    string = input.format(format);
		}
		catch (DateTimeException exc) {
		    exc.printStackTrace();
		}
		return string;
	}
}
