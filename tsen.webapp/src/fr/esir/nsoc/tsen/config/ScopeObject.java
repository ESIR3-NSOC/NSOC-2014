package fr.esir.nsoc.tsen.config;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "ScopeObject")
@XmlType(propOrder = {"id", "name"})
public class ScopeObject {
	
	private String id;
	private String name;
	
	public ScopeObject(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	
	
	public ScopeObject() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
