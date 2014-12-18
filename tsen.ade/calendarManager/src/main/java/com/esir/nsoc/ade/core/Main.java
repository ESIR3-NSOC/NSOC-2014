package com.esir.nsoc.ade.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
	
	public static void main(String[] args) {
		Boolean ok=false;
		ICSExtractor extract = new ICSExtractor();
		for(int i=6420 ; i<6428 ; i++){
			ok=extract.extractICS(i, "2014-12-14", "2014-12-20", 22);
			System.out.println(ok ? "Extraction done !" : "Extraction failed !");
		}
	}
}


/*



*/