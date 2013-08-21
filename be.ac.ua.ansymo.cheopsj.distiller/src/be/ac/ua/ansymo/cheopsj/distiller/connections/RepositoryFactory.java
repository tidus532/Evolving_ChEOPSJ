package be.ac.ua.ansymo.cheopsj.distiller.connections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import be.ac.ua.ansymo.cheopsj.distiller.connections.connector.RepositoryConnector;
import be.ac.ua.ansymo.cheopsj.distiller.connections.connector.impl.GITConnector;
import be.ac.ua.ansymo.cheopsj.distiller.connections.connector.impl.SVNConnector;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.RepositoryLogHandler;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl.GITLogEntryHandler;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl.SVNLogEntryHandler;

public class RepositoryFactory {
	//public static String config_file_path = "C:\\Users\\Daan\\workspace\\cheopsOfficial\\Evolving_ChEOPSJ\\Reengineering\\config.properties";
	static private RepositoryFactory singleton;
	private Properties prop;
	private static String type_in_use;

	
	
	private RepositoryFactory(String config_path) {	
		fetchPropertiesFile(config_path);
		type_in_use = prop.getProperty("type_in_use");
	}

	private static RepositoryFactory getFactory(String config_path){
		if(singleton == null){
			singleton = new RepositoryFactory(config_path);
		}
		return singleton;
	}
	
	public static RepositoryLogHandler getLogEntryHandler(String config_path){
		RepositoryFactory factory = RepositoryFactory.getFactory(config_path);
		RepositoryLogHandler LEH = factory.instantiateHandler();
		return LEH;
	}
	
	public static RepositoryConnector getConnector(String config_path){
		RepositoryFactory factory = RepositoryFactory.getFactory(config_path);
		RepositoryConnector RC = factory.instantiateConnector();
		return RC;
	}
	
	private RepositoryLogHandler instantiateHandler(){
		if(type_in_use.equalsIgnoreCase("svn")){
			return SVNLogEntryHandler.getLogEntryHandler();
		} else if(type_in_use.equalsIgnoreCase("git")){
			return GITLogEntryHandler.getLogEntryHandler();
		}
		return null;
	}
	
	private RepositoryConnector instantiateConnector(){
		if(type_in_use.equalsIgnoreCase("svn")){
			return SVNConnector.getConnector(prop);
		}else if(type_in_use.equalsIgnoreCase("git")){
			return GITConnector.getConnector(prop);
		}
		return null;
	}
	
	private  void fetchPropertiesFile(String config_path){
		prop = new Properties(); 
		try {
			prop.load(new FileInputStream(config_path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
