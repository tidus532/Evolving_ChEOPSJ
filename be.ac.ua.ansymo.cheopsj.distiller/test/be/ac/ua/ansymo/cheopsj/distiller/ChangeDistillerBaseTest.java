package be.ac.ua.ansymo.cheopsj.distiller;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.print.DocFlavor.URL;

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

public class ChangeDistillerBaseTest extends TestCase{
	protected static String REPO_PATH = "/tmp/svn-test-repo";
	protected static String REPO_LOCAL_PATH = "/tmp/svn-test-local";
	private static String RES_DIR = null;

	public void setUp() {
		// Init SVN lib
		SVNRepositoryFactoryImpl.setup(); // for svn and svn+ssh protocols
		DAVRepositoryFactory.setup(); // for http(s) protocol
		FSRepositoryFactory.setup(); // for local access (file protocol).

		// Creating temporary repository.
		File repo_dir = new File(REPO_PATH);
		SVNURL tgtURL = null;
		try {
			tgtURL = SVNRepositoryFactory.createLocalRepository(repo_dir, true,
					true);

		} catch (SVNException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// create a local working copy.
		SVNClientManager clientManager = SVNClientManager.newInstance();
		;
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		File local_dir = new File(REPO_LOCAL_PATH);
		// local_dir.deleteOnExit();
		try {
			updateClient.doCheckout(SVNURL.fromFile(repo_dir), local_dir,
					SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY,
					false);
		} catch (SVNException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	public void tearDown() {
		File repo_dir = new File(REPO_PATH);
		File local_dir = new File(REPO_LOCAL_PATH);
		// delete repository and local working copy.
		try {
			FileUtils.deleteDirectory(local_dir);
			FileUtils.deleteDirectory(repo_dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void runDistiller() {

		// Modifying distillchanges
		DistillChanges distiller = new DistillChanges();

		// Bypass all encapsulation, accessing private methods and fields
		// directly.
		Class c = distiller.getClass();
		try {
			// Replace private selectedProject with a stub.
			Field project = c.getDeclaredField("selectedProject");
			project.setAccessible(true);
			project.set(distiller, new EclipseProjectStub(REPO_LOCAL_PATH));

			// Launch private method iterateRevisions.
			Method iterate = c.getDeclaredMethod("iterateRevisions",
					IProgressMonitor.class);
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
	}
	
	/*
	 * Set the directory to use for commits in the tests resource directory.
	 */
	public void setResourceDir(String path){
		RES_DIR = path;
	}
	
	/*
	 * Do the commits contained in the subdirectories of the resource directory.
	 * All files in directory named 1 will be copied to the local working directory and committed.
	 * Then all files in directory named 2 will be copied to the local working directory and committed.
	 * And so on. If a file is not present in the next commit, it will be deleted from the local working copy.
	 * 
	 * NOTE: subdirectories are not supported, only files.
	 */
	public void doCommit(){
		String file = this.getClass().getResource(RES_DIR).getFile();
		File local_dir = new File(REPO_LOCAL_PATH);
		
		//copy files
		int i = 1;
		File next_commit = new File(file+"/"+1);
		while(next_commit.exists() && next_commit.isDirectory()){
			String[] commit_file_list = next_commit.list();
			String[] local_file_list = local_dir.list();
			ArrayList<String> commit_list = new ArrayList<String>(Arrays.asList(commit_file_list));
			ArrayList<String> local_list = new ArrayList<String>(Arrays.asList(local_file_list));
			local_list.removeAll(commit_list);
			for(String file_del : local_list){
				File del = new File(REPO_LOCAL_PATH+"/"+file_del);
				del.delete();
			}
			try {
				FileUtils.copyDirectory(next_commit, local_dir);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
			next_commit = new File(file+"/"+i);
		}
		//Commit changes
	}
}
