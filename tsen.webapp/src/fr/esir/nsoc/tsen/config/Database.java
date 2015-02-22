package fr.esir.nsoc.tsen.config;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "database")
@XmlType(propOrder = {"db_name", "db_login", "db_password"})
public class Database {
	
	private String db_name;
	private String db_login;
	private String db_password;
	
	
	public String getDb_name() {
		return db_name;
	}
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	public String getDb_login() {
		return db_login;
	}
	public void setDb_login(String db_login) {
		this.db_login = db_login;
	}
	public String getDb_password() {
		return db_password;
	}
	public void setDb_password(String db_password) {
		this.db_password = db_password;
	}

}
