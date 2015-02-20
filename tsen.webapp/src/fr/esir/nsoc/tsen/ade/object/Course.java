package fr.esir.nsoc.tsen.ade.object;

import java.time.LocalDate;
import java.util.ArrayList;

public class Course {

	private String ID;
	private LocalDate startPoint;
	private LocalDate endPoint;
	private ArrayList<String> peopleId;

	public Course(String iD, LocalDate startPoint, LocalDate endPoint,
			ArrayList<String> peopleId) {
		super();
		ID = iD;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.peopleId = peopleId;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public LocalDate getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(LocalDate startPoint) {
		this.startPoint = startPoint;
	}

	public LocalDate getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(LocalDate endPoint) {
		this.endPoint = endPoint;
	}

	public ArrayList<String> getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(ArrayList<String> peopleId) {
		this.peopleId = peopleId;
	}

}
