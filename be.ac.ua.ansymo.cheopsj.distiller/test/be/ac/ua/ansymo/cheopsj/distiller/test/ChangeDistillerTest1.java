package be.ac.ua.ansymo.cheopsj.distiller.test;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;
import be.ac.ua.ansymo.cheopsj.model.ModelManager;
import be.ac.ua.ansymo.cheopsj.model.changes.IChange;

//import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.ChangeExtractor;

public class ChangeDistillerTest1 extends ChangeDistillerBaseTest{
	private final static String CHANGE_ADD = "Addition";
	private final static String CHANGE_MOD = "Modification";
	private final static String CHANGE_DEL = "Removal";
	
	@Test
	public void test3newClasses(){
		this.setResourceDir("/test1");
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C
		this.doCommit();
		this.runDistiller();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Check the model for all changes.
		boolean seen_a = false;
		boolean seen_b = false;
		boolean seen_c = false;
		
		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		HashMap<?,?> map = (HashMap<?, ?>) manager.famixClassesMap;
		System.out.println("MAP SIZE:"+map.size());
		boolean test = manager.famixClassExists("A");
		if(changes.size() == 0 ){
			System.out.println("LIST IS EMPTY");
			assert(false);
		}
		
		for(IChange c : changes){
			String type = c.getChangeType();
			if(type.equals(CHANGE_ADD)){
				String name = c.getName();
				if(c.getName().equals("A") && !seen_a){
					seen_a = true;
				} else if (c.getName().equals("B") && !seen_b){
					seen_b = true;
				} else if(c.getName().equals("C") && !seen_c ){
					seen_c = true;
				} else {
					assert(false); //Detects more changes than there should be.
				}
			} else {
				assert(false); //Only additions can be allowed.
			}
		}
		
		assertEquals(true, seen_a && seen_b && seen_c); //Make sure it detected all additions.
	}
	
	@Test
	public void test3newClassesAndDelete2OfThem(){
		this.setResourceDir("/test2");
		// rev 1: class A, class B, class C
		// rev 2: Class A
		this.doCommit();
		this.runDistiller();
		
	}
	
	@Test
	public void testnewMethod(){
		this.setResourceDir("/test3");
		// rev 1: Class A empty
		// rev 2: Class A with method X
		this.doCommit();
		this.runDistiller();
		/*assertEquals(1, ChangeExtractor.newFile);
		assertEquals(0, ChangeExtractor.deletedFile);
		assertEquals(1, ChangeExtractor.modifiedFile);*/
	}
	
}
