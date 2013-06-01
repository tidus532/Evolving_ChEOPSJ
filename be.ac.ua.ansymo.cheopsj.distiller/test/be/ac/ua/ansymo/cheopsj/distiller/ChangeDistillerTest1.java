package be.ac.ua.ansymo.cheopsj.distiller;

import org.junit.Test;

public class ChangeDistillerTest1 extends ChangeDistillerBaseTest{

	@Test
	public void test(){
		this.setResourceDir("/test1");
		this.doCommit();
		this.runDistiller();
	}
}
