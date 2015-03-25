package fr.esir.nsoc.tsen.ade.object;

public class Event {
	
	private String id;
	private String dtstart;
	private String dtend;
	private String summary;
	private String location;
	private String description;
    private String adeID;

	public Event() {
		super();
		this.id = "";
		this.dtstart = "";
		this.dtend = "";
		this.summary = "";
		this.location = "";
		this.description = "";
        this.adeID = "";
	}
	
	public Event(String uid, String dtstart, String dtend, String summary,
			String location, String description, String adeID) {
		super();
		this.id = uid;
		this.dtstart = dtstart;
		this.dtend = dtend;
		this.summary = summary;
		this.location = location;
		this.description = description;
        this.adeID = adeID;
    }

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public String getADEId() {
        return adeID;
    }

    public void setAdeID(String adeID) {
        this.adeID = adeID;
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

    @Override
    public boolean equals(Object o){
         if (o instanceof Event){
             return ((Event)o).getId().equalsIgnoreCase(getId());
         }  else {
             return false;
         }
    }
}
