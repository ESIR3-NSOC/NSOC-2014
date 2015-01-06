package com.esir.nsoc.ade.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.esir.nsoc.ade.database.SQLiteDB;
import com.esir.nsoc.ade.http.HTTP_Requester;
import com.esir.nsoc.ade.http.HTTP_Response;
import com.esir.nsoc.ade.parser.Cal;

public class ICSExtractor {

	private final static String ADE_SERVER_URL = "https://plannings.univ-rennes1.fr/";
	private static boolean ok=false;
	private final static boolean DEBUG = true;
	
	public ICSExtractor() {
		
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
		HTTP_Requester httpReq = new HTTP_Requester(ADE_SERVER_URL);
		HTTP_Response httpResp = httpReq.sendGet("ade/custom/modules/plannings/direct_cal.jsp?resources="+adeID+"&calType=ical&firstDate="+addOneDay(firstDate)+"&lastDate="+addOneDay(firstDate)+"&login=cal&password=visu&projectId="+projectID);
		if (DEBUG) System.out.println(httpResp.isOk() ? httpResp.getCode() : "err");
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
