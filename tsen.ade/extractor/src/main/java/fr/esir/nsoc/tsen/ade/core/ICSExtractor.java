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
import fr.esir.nsoc.tsen.ade.object.Project;

public class ICSExtractor implements Callable<Boolean> {

	private final static String ADE_SERVER_URL = "https://plannings.univ-rennes1.fr/";
	private static boolean ok=false;
	private final static boolean DEBUG = true;
	
	private Project project;
	private String _ADE_ID;
	private Date startDate;
	private Date endDate;
	
	
	
	public ICSExtractor(Project project, String _ADE_ID, Date stratDate,
			Date endDate) {
		super();
		this.project = project;
		this._ADE_ID = _ADE_ID;
		this.startDate = stratDate;
		this.endDate = endDate;
	}
	
	

	@Override
	public Boolean call() throws Exception {
		//Récupération de l'ICS
		Cal cal = new Cal(getICS());
				
		//Create and/or connect to local DB
		SQLiteDB db = new SQLiteDB("test1.db");
		System.out.println(db.isConnected() ? "Connection DB ok" : "Connection DB nok");

		// Get a set of the entries
		Set set = cal.getSet();
		//Test si l'ICS à bien été interprété
		if(set!=null){

//			ok=db.FillEvent(set, PROJECT_ID);
//			db.FillUid(set, ADE_ID, PROJECT_ID);

			ok=db.FillEvent(set, project.getId());
			db.FillUid(set, _ADE_ID, project.getId());

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
		Set set = cal.getSet();
		//Test si l'ICS à bien été interprété
		if(set!=null){
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
		
		parameters.add(new HTTP_Parameter("resources", _ADE_ID));
		parameters.add(new HTTP_Parameter("calType", "ical"));
		parameters.add(new HTTP_Parameter("firstDate", addOneDay(startDate)));
		parameters.add(new HTTP_Parameter("lastDate", addOneDay(endDate)));
		parameters.add(new HTTP_Parameter("login", "cal"));
		parameters.add(new HTTP_Parameter("password", "visu"));
		parameters.add(new HTTP_Parameter("projectId", Integer.toString(project.getId())));
		HTTP_Response httpResp = httpReq.sendGet("ade/custom/modules/plannings/direct_cal.jsp", parameters);
		
		if (DEBUG) System.out.println(httpResp.getCode()==200 ? httpResp.getCode() : "err");
		return httpResp.getContent();
	}
	
	private String addOneDay(Date date){
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();    
		c.setTime(date);
		c.add(Calendar.DATE, 1);
		return formatter.format(c.getTime()).toString();
	}
}
