package be.ac.ua.ansymo.cheopsj.distiller.connections.connector.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
import org.tmatesoft.svn.core.ISVNLogEntryHandler;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNLogClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import be.ac.ua.ansymo.cheopsj.distiller.connections.connector.RepositoryConnector;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.RepositoryLogHandler;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl.SVNLogEntryHandler;

public class GITConnector implements RepositoryConnector {

	private String fGITUserName = "";
	private String fGITUserPassword = "";
	private String fGITUrl = "";
	
	static private GITConnector singleton;

	/**
	 * Constructor gets username and password to the git repository
	 */
	private GITConnector(Properties prop) {
		fGITUserName = prop.getProperty("GIT_username");
		fGITUserPassword = prop.getProperty("GIT_password");
		fGITUrl = prop.getProperty("GIT_url");
	}

	public void initialize(){
		//TODO: implementation of initialize
	}

	public void updateToRevision(File file, long revisionNumber, IProgressMonitor monitor){		
		//TODO: implementation of updateToRevision
	}

	public long getCurrentRevision(File file){
		//TODO: implementation of getCurrentRevision
		return 0;
	}
	
	public void getCommitMessage(File file, long revisionNumber, RepositoryLogHandler entryHandler){
		//TODO: implementation of getCommitMessage
	}
	
	public String getFileContents(String filePath, long revision){
		//TODO: implementation of getFileContents
		return "";
	}
	
	public static RepositoryConnector getConnector(Properties prop){
		if(singleton == null){
			singleton = new GITConnector(prop);
		}
		return singleton;
	}

}
