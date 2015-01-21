package fr.esir.nsoc.tsen.ade.core;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.concurrent.Callable;














import com.esir.nsoc.ade.parser.Cal;

import fr.esir.nsoc.tsen.ade.database.SQLiteDB;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.object.Event;
import fr.esir.nsoc.tsen.ade.object.Project;
import fr.esir.nsoc.tsen.ade.object.TreeObject;

public class ICSExtractor implements Callable<Boolean> {

	private final static String ADE_SERVER_URL = "https://plannings.univ-rennes1.fr/";
	private static boolean ok=false;
	private final static boolean DEBUG = true;
	
	private TreeObject treeObject;
	private LocalDate startPoint;
	private LocalDate endPoint;
	

	public ICSExtractor(TreeObject treeObject, LocalDate startPoint,
			LocalDate endPoint) {
		super();
		this.treeObject = treeObject;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	@Override
	public Boolean call() throws Exception {
		//Récupération de l'ICS
		Cal cal = new Cal(getICS());
				
		//Create and/or connect to local DB
		SQLiteDB db = new SQLiteDB("test1.db");
		System.out.println(db.isConnected() ? "Connection DB ok" : "Connection DB nok");

		// Get a set of the entries
		HashSet<Event> _ADE_Events = cal.parse();
		//Test si l'ICS à bien été interprété
		if(_ADE_Events!=null){

//			ok=db.FillEvent(set, PROJECT_ID);
//			db.FillUid(set, ADE_ID, PROJECT_ID);

			ok=db.FillEvent(_ADE_Events, treeObject.getProject().getId());
			db.FillUid(_ADE_Events, treeObject.getId(), treeObject.getProject().getId());

		}
		else ok=false;

		return ok;
	}
	
	public boolean extractICS(int ADE_ID, String FIRST_DATE, int PROJECT_ID){
		
		//Récupération de l'ICS
		Cal cal = new Cal(getICS());
		
		//Create and/or connect to local DB
		SQLiteDB db = new SQLiteDB("test1.db");
		System.out.println(db.isConnected() ? "Connection DB ok" : "Connection DB nok");

		// Get a set of the entries
		HashSet<Event> _ADE_Events = cal.parse();
		//Test si l'ICS à bien été interprété
		if(_ADE_Events!=null){
//			ok=db.FillEvent(set, PROJECT_ID, FIRST_DATE);
//			db.FillUid(set, ADE_ID, PROJECT_ID, FIRST_DATE);
		}
		else ok=false;

		return ok;
	}
	
	
	private String getICS(){
		HTTP_Requester httpReq = new HTTP_Requester(ADE_SERVER_URL, "");
		HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
		
		//HTTP_Response httpResp = httpReq.sendGet("ade/custom/modules/plannings/direct_cal.jsp?resources="+adeID+"&calType=ical&firstDate="+addOneDay(firstDate)+"&lastDate="+addOneDay(firstDate)+"&login=cal&password=visu&projectId="+projectID);
		
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
	/*
	private String addOneDay(LocalDate date){
		
		
		
		
		
		
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();    
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return formatter.format(c.getTime()).toString();
	}
	*/
	
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
