package com.example.esir.nsoc2014.tsen.lob.interfaces;

import com.example.esir.nsoc2014.tsen.lob.database.DatabaseRegression;

public class RunTimeTest {
	public static void main(String[] argz) throws Exception {
		DatabaseRegression db = new DatabaseRegression();
		db.predict();
		if (db.getListData() != null)
			System.out.println(db.getListData().get(0).toString());
		else
			System.out.println("The list is empty");
	}
}
