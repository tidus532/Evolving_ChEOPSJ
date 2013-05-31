package be.ac.ua.ansymo.cheopsj.distiller;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.*;

import org.eclipse.core.runtime.IProgressMonitor;

import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.DistillChanges;



public class ChangeDistillerTest {

	@Test
	public void simpleTest(){
		DistillChanges distiller = new DistillChanges();
		
		//Bypass all encapsulation, accessing private methods and fields directly.
		Class c = distiller.getClass();
		try {
			//Replace private selectedProject with a stub.
			Field project = c.getDeclaredField("selectedProject");
			project.setAccessible(true);
			project.set(distiller, new EclipseProjectStub());
			
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
		
	}
}
