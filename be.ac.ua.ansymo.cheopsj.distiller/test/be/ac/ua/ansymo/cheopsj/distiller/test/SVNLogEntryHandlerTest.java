package be.ac.ua.ansymo.cheopsj.distiller.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

import junit.framework.TestCase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import javax.print.DocFlavor.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.swt.widgets.Shell;
import org.tmatesoft.svn.core.SVNLogEntry;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;
import org.tmatesoft.svn.core.wc.admin.SVNAdminClient;
import org.apache.commons.io.FileUtils;

import be.ac.ua.ansymo.cheopsj.distiller.connections.RepositoryFactory;
import be.ac.ua.ansymo.cheopsj.distiller.connections.connector.RepositoryConnector;
import be.ac.ua.ansymo.cheopsj.distiller.connections.connector.impl.SVNConnector;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.RepositoryLogHandler;
import be.ac.ua.ansymo.cheopsj.distiller.connections.loghandler.impl.SVNLogEntryHandler;
//import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.ChangeExtractor;
import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.DistillChanges;
import be.ac.ua.ansymo.cheopsj.logger.JavaProjectHelper;
import be.ac.ua.ansymo.cheopsj.model.ModelManager;

public class SVNLogEntryHandlerTest extends TestCase{

	private SVNLogEntryHandler handler;
	private String CONFIG_PATH = "C:\\Users\\Daan\\workspace\\temp\\config.properties";
	
	@Before
	public void setUp() {
		Properties prop = new Properties();
    	try {
    		//set the properties value
    		prop.setProperty("type_in_use", "SVN");
    		//save properties to project root folder
    		prop.store(new FileOutputStream(CONFIG_PATH), null);

    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
		 handler = (SVNLogEntryHandler) RepositoryFactory.getLogEntryHandler(CONFIG_PATH);
		 Date date = new Date(1369198800);
		 SVNLogEntry logEntry = new SVNLogEntry(null,2, "Daan", date, "dummy fix message");
		 try{
			 handler.handleLogEntry(logEntry);
		 }catch(Exception e){
			 fail("can't setup handler!");
		 }
	}

	@After
	public void tearDown() {
		
	}
	
	@Test
	public void testGetRevision() {
		if(handler.getRevision() != 2){
			fail("Wrong revision returned");
		}
	}
	
	@Test
	public void testGetMessage() {
		if(! handler.getMessage().equals("dummy fix message")){
			fail("Wrong message returned");
		}
	}
	
	@Test
	public void testGetChangedPaths() {
		if(handler.getChangedPaths() != null){
			fail("Wrong path returned");
		}
	}
	
	@Test
	public void testGetDate() {
		if(!handler.getDate().equals(new Date(1369198800))){
			fail("Wrong Date returned");
		}
	}
	
	@Test
	public void testGetUser() {
		if(! handler.getUser().equals("Daan")){
			fail("Wrong User returned");
		}
	}
	
	@Test
	public void testEntryIsBugFix() {
		if(! handler.entryIsBugFix()){
			fail("Didn't figure out that the entry is a bug fix!");
		}
	}
}
