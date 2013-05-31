package be.ac.ua.ansymo.cheopsj.distiller;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.lang.reflect.*;

import org.eclipse.core.runtime.IProgressMonitor;

import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.DistillChanges;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.*;



public class ChangeDistillerTest {
	public final static String REPO_PATH="/tmp/svn-test";
	
	protected void deleteDir(File file){
		if(file.isDirectory()){
			if(file.list().length == 0){
				file.delete();
			} else {
				String files[] = file.list();
				for(String temp : files){
					File del = new File(file, temp);
					deleteDir(del);
				}
			}
			
			if(file.list().length == 0){
				file.delete();
			}
		} else {
			file.delete();
		}
		
	}
	@Test
	public void simpleTest(){
		//Creating temporary repository.
		File repo_dir = new File(REPO_PATH);
		try {
			SVNURL tgtURL = SVNRepositoryFactory.createLocalRepository( repo_dir, true , false );
			
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
			project.set(distiller, new EclipseProjectStub(REPO_PATH));
			
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
		
		//delete repository.
		//deleteDir(repo_dir);
	}
}
