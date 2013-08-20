package be.ac.ua.ansymo.cheopsj.distiller.connections;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.RepositoryLogHandler;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl.GITLogEntryHandler;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl.SVNLogEntryHandler;

public class RepositoryFactory {
	public static String config_file_path = "C:\\Users\\Detlev\\workspace\\temp\\svn-test-repo\\config.properties";
	static private RepositoryFactory singleton;
	private static String type_in_use;
	
	private RepositoryFactory() {		
	}

	private static RepositoryFactory getFactory(){
		if(singleton == null){
			singleton = new RepositoryFactory();
			fetchFromPropertiesFile();
		}
		return singleton;
	}
	
	public static RepositoryLogHandler getLogEntryHandler(){
		RepositoryFactory factory = RepositoryFactory.getFactory();
		RepositoryLogHandler LEH = factory.instantiateHandler();
		return LEH;
	}
	
	private RepositoryLogHandler instantiateHandler(){
		if(type_in_use.equalsIgnoreCase("svn")){
			return SVNLogEntryHandler.getLogEntryHandler();
		} else if(type_in_use.equalsIgnoreCase("git")){
			return GITLogEntryHandler.getLogEntryHandler();
		}
		return null;
	}
	
	private static void fetchFromPropertiesFile(){
		//TODO: this
		/*
		Properties prop = new Properties(); 
		try {
			prop.load(new FileInputStream(config_file_path));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		type_in_use = prop.getProperty("type_in_use");	
		*/
		type_in_use = "svn";
	}
}