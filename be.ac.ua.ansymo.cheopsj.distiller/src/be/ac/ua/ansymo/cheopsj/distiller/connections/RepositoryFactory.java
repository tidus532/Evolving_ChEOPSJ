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
	public static String config_file_path = "C:\\Users\\Daan\\workspace\\cheopsOfficial\\Evolving_ChEOPSJ\\Reengineering\\config.properties";
	static private RepositoryFactory singleton;
	private Properties prop;
	private static String type_in_use;
	//private static String user;
	//private static String password;
	
	
	private RepositoryFactory() {	
		fetchPropertiesFile();
		type_in_use = prop.getProperty("type_in_use");
		System.out.println("=============TYPE IN USE:");
		System.out.println(type_in_use);
		System.out.println("=============");
		//type_in_use = "svn";
	}

	private static RepositoryFactory getFactory(){
		if(singleton == null){
			singleton = new RepositoryFactory();
		}
		return singleton;
	}
	
	public static RepositoryLogHandler getLogEntryHandler(){
		RepositoryFactory factory = RepositoryFactory.getFactory();
		RepositoryLogHandler LEH = factory.instantiateHandler();
		return LEH;
	}
	
	public static RepositoryConnector getConnector(){
		RepositoryFactory factory = RepositoryFactory.getFactory();
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
	
	private  void fetchPropertiesFile(){
		prop = new Properties(); 
		try {
			prop.load(new FileInputStream(config_file_path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
