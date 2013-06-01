package be.ac.ua.ansymo.cheopsj.distiller;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;

import org.eclipse.core.runtime.IProgressMonitor;

import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.DistillChanges;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.BasicAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;
import org.tmatesoft.svn.core.wc.admin.SVNAdminClient;
import org.apache.commons.io.FileUtils;


public class ChangeDistillerTest {
	public final static String REPO_PATH="/tmp/svn-test-repo";
	public final static String REPO_LOCAL_PATH="/tmp/svn-test-local";
	
	@Test
	public void simpleTest(){
		//Init SVN lib
		SVNRepositoryFactoryImpl.setup(); // for svn and svn+ssh protocols 
		DAVRepositoryFactory.setup(); // for http(s) protocol 
		FSRepositoryFactory.setup(); // for local access (file protocol). 
		
		//Creating temporary repository.
		File repo_dir = new File(REPO_PATH);
		SVNURL tgtURL = null;
		try {
			tgtURL = SVNRepositoryFactory.createLocalRepository( repo_dir, true , true );
			
		} catch (SVNException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//create a local working copy.
		SVNClientManager clientManager = SVNClientManager.newInstance();;
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		File local_dir = new File(REPO_LOCAL_PATH);
		//local_dir.deleteOnExit();
		try {
			updateClient.doCheckout(SVNURL.fromFile(repo_dir), local_dir, SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY, false);
		} catch (SVNException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//Modifying distillchanges
		DistillChanges distiller = new DistillChanges();
		
		//Bypass all encapsulation, accessing private methods and fields directly.
		Class c = distiller.getClass();
		try {
			//Replace private selectedProject with a stub.
			Field project = c.getDeclaredField("selectedProject");
			project.setAccessible(true);
			project.set(distiller, new EclipseProjectStub(REPO_LOCAL_PATH));
			
			//Launch private method iterateRevisions.
			Method iterate = c.getDeclaredMethod("iterateRevisions", IProgressMonitor.class);
			iterate.setAccessible(true);
			iterate.invoke(distiller, new EclipseProgressMonitorStub());
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//delete repository and local working copy.
		try {
			FileUtils.deleteDirectory(local_dir);
			FileUtils.deleteDirectory(repo_dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//local_dir.delete();
	}
}
