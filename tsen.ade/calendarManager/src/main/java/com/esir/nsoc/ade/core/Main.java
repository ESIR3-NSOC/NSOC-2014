package com.esir.nsoc.ade.core;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import net.fortuna.ical4j.model.DateTime;

public class Main {
	
	public static void main(String[] args) {
		Boolean ok=false;
		ICSExtractor extract = new ICSExtractor();
		/*for(int i=6420 ; i<6422 ; i++){
			ok=extract.extractICS(i, "2015-01-01", "2015-01-20", 22);
			System.out.println(ok ? "Extraction done !" : "Extraction failed !");
		}*/
		
		ok=extract.extractICS(6434, "2015-01-09", 22);
		System.out.println(ok ? "Extraction done !" : "Extraction failed !");

	}
}