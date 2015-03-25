package fr.esir.nsoc.tsen.ade.object;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Course {

    private Date date;
	private String ID;
	private Time startPoint;
	private Time endPoint;
	private List<String> peopleId;
    private String roomID;
    private String roomName;
    private String studentGroupName;
    public int duree;

    public Course (String iD, Time startPoint, Time endPoint, int duree, Date date)   {
        this.ID = iD;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.date = date;

        this.peopleId = new ArrayList<String>();
        this.roomID = "undefined";
        this.roomName = "undefined";
        this.studentGroupName = "undefined";
        this.duree = duree;
    }

	public Course(String iD, Time startPoint, Time endPoint, Date date,
			ArrayList<String> peopleId) {
		super();
		ID = iD;
        this.date = date;
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

	public Time getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Time startPoint) {
		this.startPoint = startPoint;
	}

	public Time getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Time endPoint) {
		this.endPoint = endPoint;
	}

	public List<String> getPeopleId() {
		return peopleId;
	}

	public void setPeopleId(ArrayList<String> peopleId) {
		this.peopleId = peopleId;
	}

    public void addPeopleId(String pid){
        this.peopleId.add(pid);
    }

    public String getRoomID() {
        return roomID;
    }

    public void setRoomID(String roomID) {
        this.roomID = roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getStudentGroupName() {
        return studentGroupName;
    }

    public void setStudentGroupName(String studentGroupName) {
        this.studentGroupName = studentGroupName;
    }

    public int getDuree() {
        return duree;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public Date getDate (){
        return date;
    }
}
