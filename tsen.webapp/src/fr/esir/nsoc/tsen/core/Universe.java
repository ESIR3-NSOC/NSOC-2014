package fr.esir.nsoc.tsen.core;

import java.io.File;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import fr.esir.nsoc.tsen.ade.database.DataBase;

public class Universe {

	private static final String CONFIG_PATH = "tsen";
	private static final String CONFIG_FILE = "config.xml";
	
	private Logger logger;
	private ADE_Scope scope;
	private Config config;
	private DataBase dataBase;



	public Universe() {
		super();
		logger = Logger.getLogger(this.getClass().getName());
		this.scope = null;
		this.config = null;
		this.dataBase = null;
	}


	public ADE_Scope getScope() {
		return scope;
	}


	public void setScope(ADE_Scope scope) {
		this.scope = scope;
	}

	public Config getConfig() {
		return config;
	}

	public void setConfig(Config config) {
		this.config = config;
	}
	
	
	
	public DataBase getDataBase() {
		return dataBase;
	}

	public void setDataBase(DataBase dataBase) {
		this.dataBase = dataBase;
	}

	public void saveConfig() {
		try {
            JAXBContext context = JAXBContext.newInstance(Config.class);
            Marshaller m = context.createMarshaller();
            //for pretty-print XML in JAXB
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
 
            // Write to File
            File path = new File(CONFIG_PATH);
            if (!(path.exists() && path.isDirectory()))
            {
            	path.mkdirs();
            }
            m.marshal(config, new File(CONFIG_PATH + "/" + CONFIG_FILE));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
	}
	
	public void loadConfig() {
		File file = new File(CONFIG_PATH + "/" + CONFIG_FILE);
		if (file.exists() && file.isFile()) {
			try {
	            JAXBContext context = JAXBContext.newInstance(Config.class);
	            Unmarshaller un = context.createUnmarshaller();
	            config = (Config) un.unmarshal(file);
	        } catch (JAXBException e) {
	            logger.severe(e.toString());
				createDefaultConfig();
				saveConfig();
	        }
		} else {
			createDefaultConfig();
			saveConfig();
		}
	}
	
	private void createDefaultConfig() {
		config = new Config();
		
		config.setDb_name("db.uion.fr:3306/tsen_ade");
		config.setDb_login("xxxx");
		config.setDb_password("xxxx");
		
		config.setProjectId(22);
		//config.setId(1);
		config.setAge(25);
		config.setName("Pankaj");
		config.setGender("Male");
		config.setRole("Developer");
		config.setPassword("sensitive");
	}
	
	

}
