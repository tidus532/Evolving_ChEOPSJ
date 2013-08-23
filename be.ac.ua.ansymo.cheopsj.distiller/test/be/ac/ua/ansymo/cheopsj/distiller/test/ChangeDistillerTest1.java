package be.ac.ua.ansymo.cheopsj.distiller.test;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;
import be.ac.ua.ansymo.cheopsj.model.ModelManager;
import be.ac.ua.ansymo.cheopsj.model.changes.IChange;

//import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.ChangeExtractor;

/**
 * The ChangeDistillerTest1 class is our class that we used for regression testing.
 * It tests the distiller functionality, by taking a look at the model's content after
 * iterating over the revisions.
 */

public class ChangeDistillerTest1 extends RepoBaseTest {
	private final static String CHANGE_ADD = "Addition";
	private final static String CHANGE_MOD = "Modification";
	private final static String CHANGE_DEL = "Removal";
	private final static String FAMIX_CLASS = "Class";
	private final static String FAMIX_METHOD = "Method";
	private final static String FAMIX_ATTRIBUTE = "Attribute";
	private final static String FAMIX_PACKAGE = "Package";
	
	

	public void getChangesFromResourcesAndCheck(String path, int amount_of_changes, String[][] requiredChanges){
		this.setResourceDir(path);
		this.doCommit();
		this.runDistiller();

		// Check the model for all changes.
		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		
		if (changes.size() != amount_of_changes) {
			fail("Incorrect amount of changes detected");
		}
		
		check_changes(changes, requiredChanges);
	}

	public void check_changes(List<IChange> changes,String[][] requiredChanges){
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
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"B",FAMIX_CLASS}, {CHANGE_ADD,"C",FAMIX_CLASS}};
		getChangesFromResourcesAndCheck("/test1", 3, requiredChanges);
		
	}

	@Test
	public void test3newClassesAndDelete2OfThem() {
		
		// rev 1: class A, class B, class C
		// rev 2: Class A
		
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"B",FAMIX_CLASS}, {CHANGE_ADD,"C",FAMIX_CLASS}
		,{CHANGE_DEL,"B",FAMIX_CLASS}, {CHANGE_DEL,"C",FAMIX_CLASS}};
		getChangesFromResourcesAndCheck("/test2", 5, requiredChanges);
	}

	@Test
	public void testnewMethod() {
		
		// rev1: Class A empty
		// rev 2: Class A
		// with method Hello
		
		
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"A.Hello",FAMIX_METHOD}};
		getChangesFromResourcesAndCheck("/test3", 2, requiredChanges);
	}

	@Test
	public void testMethodRemoval() {
		
		// rev1: Class A with method Hello
		// rev 2: Class A without method X
		
		
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"Hello",FAMIX_METHOD}, {CHANGE_DEL,"A.Hello",FAMIX_METHOD}};
		getChangesFromResourcesAndCheck("/test4", 3, requiredChanges);

	}

	@Test
	public void testAttributeAddidion() {
		
		// rev1: empty Class A 
		// rev 2: Class A with attribute test and value = 10
		
		
		String[][] requiredChanges = { {CHANGE_ADD,"A",FAMIX_CLASS}, {CHANGE_ADD,"A.test : int",FAMIX_ATTRIBUTE}};
		getChangesFromResourcesAndCheck("/test5", 2, requiredChanges);

	}

	@Test
	public void testAttributeRemovalFromPackage() {
		
		// rev1: package test Class A with attribute test and value 10
		// rev 2: package test class A without attribute
		
		
		String[][] requiredChanges = { {CHANGE_ADD,"test",FAMIX_PACKAGE}, {CHANGE_ADD,"test.A",FAMIX_CLASS},
				{CHANGE_ADD,"test.A.test",FAMIX_ATTRIBUTE}, {CHANGE_DEL,"test.A.test : int",FAMIX_ATTRIBUTE}};
		getChangesFromResourcesAndCheck("/test6", 4, requiredChanges);

	}
	

	@Test
	public void testnewreqchanges() {
		// rev 1: 
		// rev 2: Class A, Class B, Class C ,class D, class ,method Hello and class AA

		String[][] requiredChanges = { {CHANGE_ADD,"AA",FAMIX_CLASS},{CHANGE_ADD,"D",FAMIX_CLASS},{CHANGE_ADD,"Hello",FAMIX_METHOD},{CHANGE_ADD,"C",FAMIX_CLASS}, {CHANGE_ADD,"B",FAMIX_CLASS}, {CHANGE_ADD,"A",FAMIX_CLASS}};
		getChangesFromResourcesAndCheck("/test7", 6, requiredChanges);

	}

}
