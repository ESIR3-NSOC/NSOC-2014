package com.example.esir.nsoc2014.tsen.lob.interfaces;

import java.util.List;

import com.example.esir.nsoc2014.tsen.lob.database.DatabaseRegression;
import com.example.esir.nsoc2014.tsen.lob.objects.DatesInterval;

public class RunTimeTest {
	public static void main(String[] argz) throws Exception {
		Prevision db = new DatabaseRegression();
		List<DatesInterval> list= db.predict();
		if (list != null)
			System.out.println(list.get(0).toString());
		else
			System.out.println("The list is empty");
	}
}
