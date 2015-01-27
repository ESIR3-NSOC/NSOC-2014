package com.example.esir.nsoc2014.tsen.lob.objects;

import java.util.Date;

public class DatesInterval implements Comparable<DatesInterval> {
	private Date start;
	private Date end;
	private double consigne;
	private int nbPerson;
	private WeatherForecast prev;

	public DatesInterval(Date start, Date end, double consigne, int nbPerson,WeatherForecast prev) {
		this.start = start;
		this.end = end;
		this.consigne = consigne;
		this.nbPerson = nbPerson;
		this.prev = prev;
	}
	
	public int getNbPerson(){
		return nbPerson;
	}
	
	public WeatherForecast getPrevision(){
		return prev;
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
