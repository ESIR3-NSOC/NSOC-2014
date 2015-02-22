package fr.esir.nsoc.tsen.config;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "config")
@XmlType(propOrder = {"database", "projectId", "icsThreadPoolSize", "scopeObjects"})
public class Config {
	 
	/*
	private String db_name;
	private String db_login;
	private String db_password;
	*/
	
	private Database database;
	
	private int projectId;
	
    private int icsThreadPoolSize;
    
    private List<ScopeObject> scopeObjects;
 
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
	
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
/*
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
*/
	
	
	

	public Database getDatabase() {
		return database;
	}

	public void setDatabase(Database database) {
		this.database = database;
	}



	public int getIcsThreadPoolSize() {
		return icsThreadPoolSize;
	}

	public void setIcsThreadPoolSize(int icsThreadPoolSize) {
		this.icsThreadPoolSize = icsThreadPoolSize;
	}	
	
	public List<ScopeObject> getScopeObjects() {
		return scopeObjects;
	}

	public void setScopeObjects(List<ScopeObject> scopeObjects) {
		this.scopeObjects = scopeObjects;
	}

}
