package fr.esir.nsoc.tsen.config;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "AdminUser")
@XmlType(propOrder = {"id", "password"})
public class AdminUser {
	
	private String id;
	private String password;
	
	
	
	public AdminUser(String id, String password) {
		super();
		this.id = id;
		this.password = password;
	}



	public AdminUser() {
		super();
		// TODO Auto-generated constructor stub
	}



	public String getId() {
		return id;
	}



	public void setId(String id) {
		this.id = id;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}




	
}
