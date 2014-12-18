package com.esir.nsoc.ade.core;

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

	public boolean extractICS(int ADE_ID, String FIRST_DATE, String LAST_DATE, int PROJECT_ID){

		Cal cal = new Cal(getICS(ADE_ID, FIRST_DATE, LAST_DATE, PROJECT_ID));
		
		//connect to local DB
		SQLiteDB db = new SQLiteDB("test1.db");
		System.out.println(db.isConnected() ? "ok" : "nok");

		// Get a set of the entries
		Set set = cal.getSet();

		ok=db.FillEvent(set, PROJECT_ID);
		
		return ok;
	}
	
	private String getICS(int adeID, String firstDate, String lastDate, int projectID){
		HTTP_Requester httpReq = new HTTP_Requester(ADE_SERVER_URL);
		HTTP_Response httpResp = httpReq.sendGet("ade/custom/modules/plannings/direct_cal.jsp?resources="+adeID+"&calType=ical&firstDate="+firstDate+"&lastDate="+lastDate+"&login=cal&password=visu&projectId="+projectID);
		if (DEBUG) System.out.println(httpResp.isOk() ? httpResp.getCode() : "err");
		return httpResp.getContent();
	}

	
	
	
}
