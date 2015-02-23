package fr.esir.nsoc.tsen.objects;


public class Vote {
	
	private String user;
	private String event;
	private String room;
	private String date;
	private String rate;
	
	
	
	
	
	public Vote(String user, String event, String room, String date, String rate) {
		super();
		this.user = user;
		this.event = event;
		this.room = room;
		this.date = date;
		this.rate = rate;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	
	
}
