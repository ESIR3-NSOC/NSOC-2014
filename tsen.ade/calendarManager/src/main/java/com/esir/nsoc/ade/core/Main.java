package com.esir.nsoc.ade.core;

import java.util.Set;

import com.esir.nsoc.ade.database.SQLiteDB;
import com.esir.nsoc.ade.http.HTTP_Requester;
import com.esir.nsoc.ade.http.HTTP_Response;
import com.esir.nsoc.ade.parser.Cal;

public class Main {
	
	private final static String ADE_SERVER_URL = "http://plannings.univ-rennes1.fr/";
	//private final static String ADE_CALENDAR_PATH = "ade/custom/modules/plannings/direct_cal.jsp?resources=6467&calType=ical&firstDate=2014-12-16&lastDate=2014-12-20&login=cal&password=visu&projectId=22";
	private static int ADE_ID=7816;
	private static String FIRST_DATE="2014-12-16";
	private static String LAST_DATE="2014-12-20";
	private static int PROJECT_ID=22;
	
	private final static boolean DEBUG = true;
	
	//main plusieurs ID remplir la meme table
	
	public static void main(String[] args) {
		
		boolean status = true; // OK
		
		Cal cal = new Cal(getICS(ADE_ID, FIRST_DATE, LAST_DATE, PROJECT_ID));
		
		//connect to local DB
		SQLiteDB db = new SQLiteDB("test1.db");
		System.out.println(db.isConnected() ? "ok" : "nok");

		// Get a set of the entries
		Set set = cal.getSet();

		db.FillExtract(set, PROJECT_ID);

	}
	
	private static String getICS(int adeID, String firstDate, String lastDate, int projectID){
		HTTP_Requester httpReq = new HTTP_Requester(ADE_SERVER_URL);
		HTTP_Response httpResp = httpReq.sendGet("ade/custom/modules/plannings/direct_cal.jsp?resources="+ADE_ID+"&calType=ical&firstDate="+FIRST_DATE+"&lastDate="+LAST_DATE+"&login=cal&password=visu&projectId="+PROJECT_ID);
		if (DEBUG) System.out.println(httpResp.isOk() ? httpResp.getCode() : "err");
		return httpResp.getContent();
	}

}
