package be.ac.ua.ansymo.cheopsj.distiller.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.Bundle;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

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

//import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.ChangeExtractor;
import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.DistillChanges;
import be.ac.ua.ansymo.cheopsj.logger.JavaProjectHelper;
import be.ac.ua.ansymo.cheopsj.model.ModelManager;

public class ChangeDistillerBaseTest extends TestCase{
	protected String REPO_PATH = "C:\\Users\\Daan\\workspace\\temp\\svn-test-repo";
	protected String REPO_LOCAL_PATH = null;
	private String RES_DIR = null;
	private IJavaProject jproject = null;
	
	@Before
	public void setUp() {
		// Init SVN lib
		SVNRepositoryFactoryImpl.setup(); // for svn and svn+ssh protocols
		DAVRepositoryFactory.setup(); // for http(s) protocol
		FSRepositoryFactory.setup(); // for local access (file protocol).
		
		//create new project
		try {
			this.jproject  = JavaProjectHelper.createJavaProject("Test", "");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		REPO_LOCAL_PATH = this.jproject.getProject().getLocation().toString();
		System.out.println("LOCAL PATH:"+REPO_LOCAL_PATH);
		
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
		SVNUpdateClient updateClient = SVNClientManager.newInstance().getUpdateClient();
		File local_dir = new File(REPO_LOCAL_PATH);
		try {
			updateClient.doCheckout(SVNURL.fromFile(repo_dir), local_dir,
					SVNRevision.UNDEFINED, SVNRevision.HEAD, SVNDepth.INFINITY,
					false);
		} catch (SVNException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//ChangeExtractor ce = new ChangeExtractor();
	}

	@After
	public void tearDown() {
		//ChangeExtractor.resetCounters();
		File repo_dir = new File(REPO_PATH);
		File local_dir = new File(REPO_LOCAL_PATH);
		try {
			jproject.getProject().delete(true, true, null);
		} catch (CoreException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		// delete repository and local working copy.
		try {
			FileUtils.deleteDirectory(repo_dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Clear the model for the next test.
		ModelManager.getInstance().clearModel();
		System.out.println("=============");
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
			project.set(distiller, this.jproject.getProject());

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
	@SuppressWarnings("deprecation")
	public void doCommit(){
		SVNRepositoryFactoryImpl.setup(); 
		FSRepositoryFactory.setup(); 
		DAVRepositoryFactory.setup(); 
		
		//String file = this.getClass().getResource(RES_DIR).getFile();
		Bundle bundle = Platform.getBundle("be.ac.ua.ansymo.cheopsj.distiller");
		java.net.URL fileURL = bundle.getEntry("resources/"+RES_DIR);
		File res_loc = null;
		//String file = fileURL.toString();
		try {
			res_loc = new File(FileLocator.resolve(fileURL).toURI());
		} catch (URISyntaxException e1) {
		    e1.printStackTrace();
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		File local_dir = new File(REPO_LOCAL_PATH);
		
		SVNClientManager clientManager = SVNClientManager.newInstance();
		SVNCommitClient commitClient = clientManager.getCommitClient();
		SVNStatusClient statusClient = clientManager.getStatusClient(); 
		SVNWCClient wcClient = clientManager.getWCClient(); 
		SVNUpdateClient updateClient = clientManager.getUpdateClient();
		
		//Commit java project files.
		File file_add = new File(REPO_LOCAL_PATH+"/.classpath");
		try {
			wcClient.doAdd(file_add, false, false, false, false, false);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		file_add = new File(REPO_LOCAL_PATH+"/.project");
		try {
			wcClient.doAdd(file_add, false, false, false, false, false);
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int i = 1;
		File next_commit = new File(res_loc, ""+i);
		while(next_commit.exists() && next_commit.isDirectory()){
			//Delete files that are not present in the directory anymore.
			String[] commit_file_list = next_commit.list();
			String[] local_file_list = local_dir.list();
			ArrayList<String> commit_list = new ArrayList<String>(Arrays.asList(commit_file_list));
			ArrayList<String> local_list = new ArrayList<String>(Arrays.asList(local_file_list));
			local_list.removeAll(commit_list);
			
			for(String file_del : local_list){
				if(!(file_del.equals(".svn") || file_del.equals(".classpath") || file_del.equals(".project"))){
					//Delete file from local repo.
					File del = new File(REPO_LOCAL_PATH+"/"+file_del);
					System.out.println(del.toString());
					
					try {
						SVNURL[] svnpath = {SVNURL.fromFile(del)};
						wcClient.doDelete(del, false, true, false);
					} catch (SVNException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			//Adding new files.
			local_list = new ArrayList<String>(Arrays.asList(local_file_list));
			commit_list = new ArrayList<String>(Arrays.asList(commit_file_list));
			commit_list.removeAll(local_list);
			try {
				FileUtils.copyDirectory(next_commit, local_dir);
				SVNProperties properties = new SVNProperties();
				for(String commit : commit_list){
					if(!commit.equals(".svn")){
						file_add = new File(REPO_LOCAL_PATH+"/"+commit);
						try {
							wcClient.doAdd(file_add, false, false, false, false, false);
						} catch (SVNException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//Do commit and update.
			File[] temp = {local_dir};
			try {
				commitClient.doCommit(temp, true, "commit", null, null, false, true, SVNDepth.INFINITY);
				updateClient.doUpdate(local_dir, SVNRevision.HEAD, SVNDepth.INFINITY, false, false);
			} catch (SVNException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			i++;
			next_commit = new File(res_loc, ""+i);
		}
		//Commit changes
	}
}
