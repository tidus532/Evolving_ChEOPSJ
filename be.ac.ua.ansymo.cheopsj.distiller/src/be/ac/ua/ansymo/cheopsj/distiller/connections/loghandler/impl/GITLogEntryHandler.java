package be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl;

import java.util.Date;
import java.util.Map;

import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.RepositoryLogHandler;

public class GITLogEntryHandler implements RepositoryLogHandler {
	
	private String message = "";
	private Map<?, ?> changedPaths;
	private Date date;
	private String user = "";
	static private GITLogEntryHandler singleton;
	
	private GITLogEntryHandler() {	
	}

	public String getMessage(){
		// TODO: implementation of getMessage
		return message;
	}
	
	public Map<?, ?> getChangedPaths(){
		// TODO: implementation of getChangedPaths
		return changedPaths;
	}
	
	public Date getDate(){
		// TODO: implementation of getDate
		return date;
	}

	public String getUser() {
		// TODO: implementation of getUser
		return user;
	}
	
	public static RepositoryLogHandler getLogEntryHandler(){
		if(singleton == null){
			singleton = new GITLogEntryHandler();
		}
		return singleton;
	}
	
}
