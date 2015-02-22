package fr.esir.nsoc.tsen.core;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import fr.esir.nsoc.tsen.ade.database.DataBase;
import fr.esir.nsoc.tsen.config.Config;
import fr.esir.nsoc.tsen.config.Database;
import fr.esir.nsoc.tsen.config.ScopeObject;

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
	
	public void refreshConfig() {
		loadConfig();
		scope.setProject(dataBase.getProject(config.getProjectId()));
	}
	
	private void createDefaultConfig() {
		config = new Config();
		
		config.setDatabase(new Database());
		
		config.getDatabase().setDb_name("db.uion.fr:3306/tsen_ade");
		config.getDatabase().setDb_login("xxxx");
		config.getDatabase().setDb_password("xxxx");
		
		config.setProjectId(22);
		config.setIcsThreadPoolSize(30);
		
		config.setScopeObjects(new ArrayList<ScopeObject>());
		config.getScopeObjects().add(new ScopeObject("5864", "esir1"));
		config.getScopeObjects().add(new ScopeObject("5598", "esir2"));
		config.getScopeObjects().add(new ScopeObject("5856", "esir3"));
		
		config.getScopeObjects().add(new ScopeObject("5436", "esir prof A-K"));
		config.getScopeObjects().add(new ScopeObject("5446", "esir prof L-W"));
		config.getScopeObjects().add(new ScopeObject("6940", "esir Personnel IATOSS"));
		config.getScopeObjects().add(new ScopeObject("988", "esir Vacataires et extérieurs"));
		config.getScopeObjects().add(new ScopeObject("1149", "scelva prof"));
		config.getScopeObjects().add(new ScopeObject("4217", "istic prof"));

		config.getScopeObjects().add(new ScopeObject("346", "Esir"));	
		config.getScopeObjects().add(new ScopeObject("3635", "Bat.5"));	
		config.getScopeObjects().add(new ScopeObject("3636", "Bat.6"));	
		config.getScopeObjects().add(new ScopeObject("3865", "Istic Anglais"));	
		config.getScopeObjects().add(new ScopeObject("3972", "Istic CM/TD"));	
		config.getScopeObjects().add(new ScopeObject("3806", "Istic Projet"));	
		config.getScopeObjects().add(new ScopeObject("3820", "Istic Projets avec code"));	
		config.getScopeObjects().add(new ScopeObject("3809", "Istic TEEO"));	
		config.getScopeObjects().add(new ScopeObject("3935", "Istic TP"));	
		config.getScopeObjects().add(new ScopeObject("3839", "Istic TP priorité aux L1-L2 (LINUX)"));	
		config.getScopeObjects().add(new ScopeObject("3805", "Istic TP spéciaux"));	
		config.getScopeObjects().add(new ScopeObject("1695", "Bat.28 Amphis"));
		

	}
	
	

}
