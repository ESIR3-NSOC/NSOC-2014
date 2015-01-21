package com.esir.nsoc.ade.core;

public class ADEDay {
	private int projectID;
	private int ADE_ID;
	private String date;

	public ADEDay(int aDE_ID, String date, int projectID) {
		super();
		this.projectID = projectID;
		this.ADE_ID = aDE_ID;
		this.date = date;
	}

	public int getProjectID() {
		return projectID;
	}

	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}

	public int getADE_ID() {
		return ADE_ID;
	}

	public void setADE_ID(int aDE_ID) {
		ADE_ID = aDE_ID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

}
