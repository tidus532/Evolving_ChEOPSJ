package be.ac.ua.ansymo.cheopsj.distiller.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.junit.Test;
import static org.junit.Assert.*;
import be.ac.ua.ansymo.cheopsj.distiller.connections.RepositoryFactory;
import be.ac.ua.ansymo.cheopsj.distiller.connections.connector.RepositoryConnector;
import be.ac.ua.ansymo.cheopsj.distiller.connections.connector.impl.SVNConnector;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.RepositoryLogHandler;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl.GITLogEntryHandler;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl.SVNLogEntryHandler;
import be.ac.ua.ansymo.cheopsj.model.ModelManager;
import be.ac.ua.ansymo.cheopsj.model.changes.IChange;

//import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.ChangeExtractor;

public class SVNTest extends ChangeDistillerBaseTest {
	
	@Test
	public void testGetCurrentRevision() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/svntest");
		this.doCommit();
		RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH);
		connector.initialize();
		File file = new File(REPO_LOCAL_PATH + "\\A.java");
		long targetRev = connector.getCurrentRevision(file);
		if( targetRev != 3 ){
			fail("Incorrect number for current revision");
		}
	}
		
		/*@Test
		public void testBadInit() {
			// rev 1: class A
			// rev 2: Class A, Class B
			// rev 3: Class A, class B, Class C

			// Setup repo.
			this.setResourceDir("/svntest");
			this.doCommit();
			
			Properties prop = new Properties();
	    	try {
	    		//set the properties value
	    		prop.setProperty("type_in_use", "SVN");
	    		
	    		prop.setProperty("SVN_username", "");
	    		prop.setProperty("SVN_password", "");
	    		prop.setProperty("SVN_url", "");

	    		//save properties to project root folder
	    		prop.store(new FileOutputStream(CONFIG_PATH + "bad"), null);
	 
	    	} catch (IOException ex) {
	    		ex.printStackTrace();
	        }
	    	try{
				RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH + "bad");
				connector.initialize();
				File file = new File(REPO_LOCAL_PATH + "\\A.java");
				long targetRev = connector.getCurrentRevision(file);
				fail("Malformed url wasn't noticed");
	    	}catch(Exception e){
	    		//malform url handled
	    	}
	}*/
	
	@Test
	public void testgetHeadRevisionNumber() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/svntest");
		this.doCommit();
		RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH);
		connector.initialize();
		SVNConnector tempConnector = (SVNConnector) connector;
		File file = new File(REPO_LOCAL_PATH + "\\A.java");
		long targetRev = tempConnector.getHeadRevisionNumber(file);
		if( targetRev != 3 ){
			fail("Incorrect number for current revision");
		}
	}

	@Test
	public void testGetCommitMessage() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/svntest");
		this.doCommit();
		try{
			RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH);
			connector.initialize();
			File file = new File(REPO_LOCAL_PATH + "\\A.java");
			RepositoryLogHandler handler = RepositoryFactory.getLogEntryHandler(CONFIG_PATH);
			connector.getCommitMessage(file,3,handler);
		}catch(Exception e){
			fail("An error occured while getting the commitmessage");
		}
	}
	
	@Test
	public void testGetFileContents() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/svntest");
		this.doCommit();
		try{
			RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH);
			connector.initialize();
			String output = connector.getFileContents("/A.java",3);
			System.out.println("ADDED: " + output.length());
			if(output.length() != 24 ){
				fail("Incorrect size of filecontent");
			}
		}catch(Exception e){
			fail("An error occured while getting the filecontent");
		}
	}
	
}
