package fr.esir.nsoc.tsen.core;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "config")
@XmlType(propOrder = {"db_name", "db_login", "db_password", "projectId", "name", "age", "role", "gender"})
public class Config {
	
	private int id;
	 
	
	private String db_name;
	private String db_login;
	private String db_password;
	
	private int projectId;
    private int age;
    private String name;
    private String role;
    private String gender;
 
    private String password;

//    @XmlAttribute
//	public int getId() {
//		return id;
//	}
//
//	public void setId(int id) {
//		this.id = id;
//	}

    
    
    
	public int getProjectId() {
		return projectId;
	}

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

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@XmlTransient
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	 

    
}
