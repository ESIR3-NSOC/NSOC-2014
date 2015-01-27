package com.example.esir.nsoc2014.tsen.lob.objects;

import java.util.Date;

public class DatesInterval implements Comparable<DatesInterval> {
	private Date start;
	private Date end;
	private double consigne;

	public DatesInterval(Date start, Date end, double consigne) {
		this.start = start;
		this.end = end;
		this.consigne = consigne;
	}

	public double getConsigne() {
		return consigne;
	}

	public Date getStartDate() {
		return start;
	}

	public Date getStartEnd() {
		return end;
	}

	public int compareTo(DatesInterval date2) {
		if (consigne == date2.getConsigne())
			return 0;
		else if (consigne < date2.getConsigne())
			return -1;
		else
			return 1;
	}

}
