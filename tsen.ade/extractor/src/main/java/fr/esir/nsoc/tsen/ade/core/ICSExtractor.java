package fr.esir.nsoc.tsen.ade.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;







import com.esir.nsoc.ade.parser.Cal;

import fr.esir.nsoc.tsen.ade.database.SQLiteDB;
import fr.esir.nsoc.tsen.ade.http.HTTP_Parameter;
import fr.esir.nsoc.tsen.ade.http.HTTP_Requester;
import fr.esir.nsoc.tsen.ade.http.HTTP_Response;
import fr.esir.nsoc.tsen.ade.object.ADEDay;

public class ICSExtractor implements Callable<Boolean> {

	private final static String ADE_SERVER_URL = "https://plannings.univ-rennes1.fr/";
	private static boolean ok=false;
	private final static boolean DEBUG = true;
	private int ADE_ID;
	private String FIRST_DATE;
	private int PROJECT_ID;
	
	public ICSExtractor(ADEDay obj) {
		this.ADE_ID=obj.getADE_ID();
		this.FIRST_DATE=obj.getDate();
		this.PROJECT_ID=obj.getProjectID();
	}

	@Override
	public Boolean call() throws Exception {
		//Récupération de l'ICS
		Cal cal = new Cal(getICS(ADE_ID, FIRST_DATE, PROJECT_ID));
				
		//Create and/or connect to local DB
		SQLiteDB db = new SQLiteDB("test1.db");
		System.out.println(db.isConnected() ? "Connection DB ok" : "Connection DB nok");

		// Get a set of the entries
		Set set = cal.getSet();
		//Test si l'ICS à bien été interprété
		if(set!=null){
			ok=db.FillEvent(set, PROJECT_ID);
			db.FillUid(set, ADE_ID, PROJECT_ID);
		}
		else ok=false;

		return ok;
	}
	
	public boolean extractICS(int ADE_ID, String FIRST_DATE, int PROJECT_ID){
		
		//Récupération de l'ICS
		Cal cal = new Cal(getICS(ADE_ID, FIRST_DATE, PROJECT_ID));
		
		//Create and/or connect to local DB
		SQLiteDB db = new SQLiteDB("test1.db");
		System.out.println(db.isConnected() ? "Connection DB ok" : "Connection DB nok");

		// Get a set of the entries
		Set set = cal.getSet();
		//Test si l'ICS à bien été interprété
		if(set!=null){
			ok=db.FillEvent(set, PROJECT_ID, FIRST_DATE);
			db.FillUid(set, ADE_ID, PROJECT_ID, FIRST_DATE);
		}
		else ok=false;

		return ok;
	}
	
	
	private String getICS(int adeID, String firstDate, int projectID){
		HTTP_Requester httpReq = new HTTP_Requester(ADE_SERVER_URL, "");
		HashSet<HTTP_Parameter> parameters = new HashSet<HTTP_Parameter>();
		
		//HTTP_Response httpResp = httpReq.sendGet("ade/custom/modules/plannings/direct_cal.jsp?resources="+adeID+"&calType=ical&firstDate="+addOneDay(firstDate)+"&lastDate="+addOneDay(firstDate)+"&login=cal&password=visu&projectId="+projectID);
		
		parameters.add(new HTTP_Parameter("resources", Integer.toString(adeID)));
		parameters.add(new HTTP_Parameter("calType", "ical"));
		parameters.add(new HTTP_Parameter("firstDate", addOneDay(firstDate)));
		parameters.add(new HTTP_Parameter("lastDate", addOneDay(firstDate)));
		parameters.add(new HTTP_Parameter("login", "cal"));
		parameters.add(new HTTP_Parameter("password", "visu"));
		parameters.add(new HTTP_Parameter("projectId", Integer.toString(projectID)));
		HTTP_Response httpResp = httpReq.sendGet("ade/custom/modules/plannings/direct_cal.jsp", parameters);
		
		if (DEBUG) System.out.println(httpResp.getCode()==200 ? httpResp.getCode() : "err");
		return httpResp.getContent();
	}
	
	private String addOneDay(String firstDate){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(firstDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar c = Calendar.getInstance();    
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return formatter.format(c.getTime()).toString();
	}
}
