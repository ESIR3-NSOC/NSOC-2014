package com.esir.nsoc.ade.core;

public class Main {
	
	public static void main(String[] args) {
		Boolean ok=false;
		ICSExtractor extract = new ICSExtractor();
		/*for(int i=6414 ; i<6428 ; i++){
			ok=extract.extractICS(i, "2014-12-14", "2014-12-20", 22);
			System.out.println(ok ? "Extraction done !" : "Extraction failed !");
		}*/
		
		ok=extract.extractICS(359, "2014-10-01", "2015-05-28", 22);
		System.out.println(ok ? "Extraction done !" : "Extraction failed !");
		
	}
}


/*



*/