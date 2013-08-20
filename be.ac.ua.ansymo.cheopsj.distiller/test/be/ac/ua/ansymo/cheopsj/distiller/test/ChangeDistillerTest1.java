package be.ac.ua.ansymo.cheopsj.distiller.test;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import be.ac.ua.ansymo.cheopsj.model.ModelManager;
import be.ac.ua.ansymo.cheopsj.model.changes.IChange;

//import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.ChangeExtractor;

public class ChangeDistillerTest1 extends ChangeDistillerBaseTest {
	private final static String CHANGE_ADD = "Addition";
	private final static String CHANGE_MOD = "Modification";
	private final static String CHANGE_DEL = "Removal";
	private final static String FAMIX_CLASS = "Class";
	private final static String FAMIX_METHOD = "Method";
	private final static String FAMIX_ATTRIBUTE = "Attribute";
	private final static String FAMIX_PACKAGE = "Package";
	
	
	/*public void perform(List<IChange> changes,String[][] requiredChanges){
		int i = 0;
		for (IChange c : changes) {
			String type = c.getChangeType();
			System.out.println("TYPE: " + type);
			if (type.equals(requiredChanges[i][0])){
				String name = c.getName();
				System.out.println("NAME: " + name);
				if ( ! (c.getName().equals(requiredChanges[i][1]) && c.getFamixType().equals(requiredChanges[i][2])) ){
					fail("Found an incorrect element");
				}
			}else{
				fail("Found an incorrect change type");
			}
			i++;
		}
	}*/

	public void perform(List<IChange> changes,String[][] requiredChanges){
		int size = changes.size();
		boolean seenArray[] = new boolean[size];
		for (IChange c : changes) {
			String type = c.getChangeType();
			System.out.println(type);
			System.out.println(c.getName());
			System.out.println(c.getFamixType());
			for(int j=0; j < requiredChanges.length; j++){
				if (type.equals(requiredChanges[j][0]) && (c.getName().equals(requiredChanges[j][1]) && c.getFamixType().equals(requiredChanges[j][2])) ){
					if(seenArray[j] == true){
						fail("Already seen that element!");
					}else{
						seenArray[j] = true;
					}
				}
			}
		}
		for(int j=0; j < seenArray.length; j++){
			if(seenArray[j] == false){
				fail("Not all changes were seen!");
			}
			
		}
		
	}
		
	@Test
	public void test3newClasses() {
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C

		// Setup repo.
		this.setResourceDir("/test1");
		this.doCommit();
		this.runDistiller();

		// Check the model for all changes.

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		

		// Check that there are the required number of changes.
		if (changes.size() != 3) {
			fail("Incorrect amount of changes detected");
		}
		
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"B",FAMIX_CLASS}, {CHANGE_ADD,"C",FAMIX_CLASS}};
		perform(changes, requiredChanges);
		
	}

	@Test
	public void test3newClassesAndDelete2OfThem() {
		this.setResourceDir("/test2");
		// rev 1: class A, class B, class C
		// rev 2: Class A
		this.doCommit();
		this.runDistiller();

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		

		// Check that there are the required number of changes.
		if (changes.size() != 5) {
			fail("Incorrect amount of changes detected (" + changes.size()
					+ ")");
		}
		// Check that each change is present.
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"B",FAMIX_CLASS}, {CHANGE_ADD,"C",FAMIX_CLASS}
		,{CHANGE_DEL,"B",FAMIX_CLASS}, {CHANGE_DEL,"C",FAMIX_CLASS}};
		perform(changes, requiredChanges);
	}

	@Test
	public void testnewMethod() {
		this.setResourceDir("/test3");
		// rev1: Class A empty
		// rev 2: Class A
		// with method Hello
		this.doCommit();
		this.runDistiller();

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		
		// Check that there are the required number of changes.
		if (changes.size() != 2) {
			fail("Incorrect amount of changes detected (" + changes.size()
					+ ")");
		}
		
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"A.Hello",FAMIX_METHOD}};
		perform(changes, requiredChanges);
	}

	@Test
	public void testMethodRemoval() {
		this.setResourceDir("/test4");
		// rev1: Class A with method Hello
		// rev 2: Class A without method X
		this.doCommit();
		this.runDistiller();

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		

		// Check that there are the required number of changes.
		if (changes.size() != 3) {
			fail("Incorrect amount of changes detected (" + changes.size()
					+ ")");
		}
		
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"Hello",FAMIX_METHOD}, {CHANGE_DEL,"A.Hello",FAMIX_METHOD}};
		perform(changes, requiredChanges);

	}

	@Test
	public void testAttributeAddidion() {
		this.setResourceDir("/test5");
		// rev1: empty Class A 
		// rev 2: Class A with attribute test and value = 10
		this.doCommit();
		this.runDistiller();

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		
		// Check that there are the required number of changes.
		if (changes.size() != 2) {
			fail("Incorrect amount of changes detected (" + changes.size()
					+ ")");
		}
		
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"A.test : int",FAMIX_ATTRIBUTE}};
		perform(changes, requiredChanges);

	}

	@Test
	public void testAttributeRemovalFromPackage() {
		this.setResourceDir("/test6");
		// rev1: package test Class A with attribute test and value 10
		// rev 2: package test class A without attribute
		this.doCommit();
		this.runDistiller();

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();


		// Check that there are the required number of changes.
		if (changes.size() != 4) {
			fail("Incorrect amount of changes detected (" + changes.size()
					+ ")");
		}
		
		String[][] requiredChanges = { {CHANGE_ADD,"test",FAMIX_PACKAGE}, {CHANGE_ADD,"test.A",FAMIX_CLASS},
				{CHANGE_ADD,"test.A.test",FAMIX_ATTRIBUTE}, {CHANGE_DEL,"test.A.test : int",FAMIX_ATTRIBUTE}};
		perform(changes, requiredChanges);

	}
	
	/*
	@Test
	public void testMixed() {

	}*/
	
	@Test
	public void testnewreqchanges() {
		// rev 1: 
		// rev 2: Class A, Class B, Class C ,class D, class ,method Hello and class AA

		// Setup repo.
		this.setResourceDir("/test7");
		this.doCommit();
		this.runDistiller();

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		

		// Check that there are the required number of changes.
		if (changes.size() != 6) {
			fail("Incorrect amount of changes detected");
		}
		
		String[][] requiredChanges = { {CHANGE_ADD,"AA",FAMIX_CLASS},{CHANGE_ADD,"D",FAMIX_CLASS},{CHANGE_ADD,"Hello",FAMIX_METHOD},{CHANGE_ADD,"C",FAMIX_CLASS}, {CHANGE_ADD,"B",FAMIX_CLASS}, {CHANGE_ADD,"A",FAMIX_CLASS}};
		perform(changes, requiredChanges);

	}

}
