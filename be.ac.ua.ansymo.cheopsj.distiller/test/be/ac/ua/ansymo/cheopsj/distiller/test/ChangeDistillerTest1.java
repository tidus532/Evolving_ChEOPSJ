package be.ac.ua.ansymo.cheopsj.distiller.test;

import org.junit.Test;

//import be.ac.ua.ansymo.cheopsj.distiller.popup.actions.ChangeExtractor;

public class ChangeDistillerTest1 extends ChangeDistillerBaseTest{

	@Test
	public void test3newClasses(){
		this.setResourceDir("/test1");
		// rev 1: class A
		// rev 2: Class A, Class B
		// rev 3: Class A, class B, Class C
		this.doCommit();
		this.runDistiller();
		assertEquals(3, ChangeExtractor.newFile);
		assertEquals(0, ChangeExtractor.deletedFile);
		assertEquals(0, ChangeExtractor.modifiedFile);
	}
	
	@Test
	public void test3newClassesAndDelete2OfThem(){
		this.setResourceDir("/test2");
		// rev 1: class A, class B, class C
		// rev 2: Class A
		this.doCommit();
		this.runDistiller();
		/*assertEquals(3, ChangeExtractor.newFile);
		assertEquals(2, ChangeExtractor.deletedFile);
		assertEquals(0, ChangeExtractor.modifiedFile);*/
	}
	/*
	@Test
	public void testnewMethod(){
		this.setResourceDir("/test3");
		// rev 1: Class A empty
		// rev 2: Class A with method X
		this.doCommit();
		this.runDistiller();
		assertEquals(1, ChangeExtractor.newFile);
		assertEquals(0, ChangeExtractor.deletedFile);
		assertEquals(1, ChangeExtractor.modifiedFile);
	}*/
	
}
