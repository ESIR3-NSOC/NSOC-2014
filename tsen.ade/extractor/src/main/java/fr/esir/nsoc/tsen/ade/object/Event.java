package fr.esir.nsoc.tsen.ade.object;

public class Event {
	
	private String uid;
	private String dtstart;
	private String dtend;
	private String summary;
	private String location;
	private String description;

	public Event() {
		super();
		this.uid = "";
		this.dtstart = "";
		this.dtend = "";
		this.summary = "";
		this.location = "";
		this.description = "";
	}
	
	public Event(String uid, String dtstart, String dtend, String summary,
			String location, String description) {
		super();
		this.uid = uid;
		this.dtstart = dtstart;
		this.dtend = dtend;
		this.summary = summary;
		this.location = location;
		this.description = description;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getDtstart() {
		return dtstart;
	}
	
	public void setDtstart(String dtstart) {
		this.dtstart = dtstart;
	}

	public String getDtend() {
		return dtend;
	}

	public void setDtend(String dtend) {
		this.dtend = dtend;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
