package be.ac.ua.ansymo.cheopsj.distiller.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.runtime.IProgressMonitor;
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

/**
 * The SVNTest class is a Unit test for the SVNConnector and the RepositoryFactory
 */
public class SVNTest extends RepoBaseTest {
	
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
			if(output.length() != 24 ){
				fail("Incorrect size of filecontent");
			}
		}catch(Exception e){
			fail("An error occured while getting the filecontent");
		}
	}
	
	@Test
	public void testUpdateToRevision() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/svntest");
		this.doCommit();
		try{
			RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH);
			connector.initialize();
			//EclipseProgressMonitor monitor = new EclipseProgressMonitorStub() ;
			SVNConnector tempConnector = (SVNConnector) connector;
			IProgressMonitor mon = new EclipseProgressMonitorStub();
			File file = new File(REPO_LOCAL_PATH + "\\A.java");
			tempConnector.updateToRevision(file,2,mon);
			long version = tempConnector.getCurrentRevision(file);
			if(version != 2 ){
				fail("Incorrect version after updating to revision");
			}
		}catch(Exception e){
			fail("An error occured while updating/getting");
		}
	}
	
	
	@Test
	public void testDefaultLogin() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/svntest");
		this.doCommit();
		RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH);

		try {
			Class c = connector.getClass();
			Field user = c.getDeclaredField("fSVNUserName");
			user.setAccessible(true);
			user.set(connector, null);
			Field password = c.getDeclaredField("fSVNPassword");
			password.setAccessible(true);
			password.set(connector, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connector.initialize();
	}
	
	@Test
	public void testgetCurrentBadRevision() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/svntest");
		this.doCommit();
		RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH);
		connector.initialize();
		File file = new File(REPO_LOCAL_PATH + "\\X.java");
		long targetRev = connector.getCurrentRevision(file);
		if( targetRev != 0 ){
			fail("Didn't notice that file X.java doesn't exist!");
		}
	}
	
	@Test
	public void testGetBadHeadRevisionNumber() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/svntest");
		this.doCommit();
		RepositoryConnector connector = RepositoryFactory.getConnector(CONFIG_PATH);
		connector.initialize();
		SVNConnector tempConnector = (SVNConnector) connector;
		File file = new File(REPO_LOCAL_PATH + "\\X.java");
		long targetRev = tempConnector.getHeadRevisionNumber(file);
		System.out.println("ADDED: " + targetRev);
		if( targetRev != 0 ){
			fail("Didn't notice that file X.java doesn't exist!");
		}
	}



}
