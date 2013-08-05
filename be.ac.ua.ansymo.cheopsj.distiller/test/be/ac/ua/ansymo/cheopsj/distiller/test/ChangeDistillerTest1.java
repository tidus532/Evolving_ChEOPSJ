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
		boolean seen_a = false;
		boolean seen_b = false;
		boolean seen_c = false;

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		HashMap<?, ?> map = (HashMap<?, ?>) manager.famixClassesMap;

		// Check that there are the required number of changes.
		if (changes.size() != 3) {
			fail("Incorrect amount of changes detected");
		}

		// Check that each change is present.
		for (IChange c : changes) {
			String type = c.getChangeType();
			if (type.equals(CHANGE_ADD)) {
				String name = c.getName();
				if (c.getName().equals("A")
						&& c.getFamixType().equals(FAMIX_CLASS) && !seen_a) {
					seen_a = true;
				} else if (c.getName().equals("B") && !seen_b
						&& c.getFamixType().equals(FAMIX_CLASS)) {
					seen_b = true;
				} else if (c.getName().equals("C") && !seen_c
						&& c.getFamixType().equals(FAMIX_CLASS)) {
					seen_c = true;
				} else {
					fail("Found an incorrect addition."); // Detects more
															// changes than
															// there should be.
				}
			} else {
				fail("Found an incorrect change type"); // Only additions can be
														// allowed.
			}
		}
		assertTrue(seen_a && seen_b && seen_c); // Make sure it detected
												// all additions.
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
		HashMap<?, ?> map = (HashMap<?, ?>) manager.famixClassesMap;

		// Check that there are the required number of changes.
		if (changes.size() != 5) {
			fail("Incorrect amount of changes detected (" + changes.size()
					+ ")");
		}
		// Check that each change is present.
		boolean seen_a = false;
		boolean seen_b = false;
		boolean seen_c = false;
		boolean b_removed = false;
		boolean c_removed = false;

		for (IChange c : changes) {
			String type = c.getChangeType();
			if (type.equals(CHANGE_ADD)) {
				String name = c.getName();
				if (c.getName().equals("A") && !seen_a
						&& c.getFamixType().equals(FAMIX_CLASS)) {
					seen_a = true;
				} else if (c.getName().equals("B") && !seen_b
						&& c.getFamixType().equals(FAMIX_CLASS)) {
					seen_b = true;
				} else if (c.getName().equals("C") && !seen_c
						&& c.getFamixType().equals(FAMIX_CLASS)) {
					seen_c = true;
				} else {
					fail("Found an incorrect addition."); // Detects more
															// changes than
															// there should be.
				}
			}

			else if (type.equals(CHANGE_DEL)) {
				if (c.getName().equals("B") && !b_removed
						&& c.getFamixType().equals(FAMIX_CLASS)) {
					b_removed = true;
				}

				else if (c.getName().equals("C") && !c_removed
						&& c.getFamixType().equals(FAMIX_CLASS)) {
					c_removed = true;
				}

				else {
					fail("Found an incorrect deletion.");
				}

			} else {
				fail("Found an incorrect change type"); // Only additions can be
														// allowed.
			}

		}
		assertTrue(seen_a && seen_b && seen_c && b_removed && c_removed); // Make
																			// sure
																			// it
																			// detected
		// all additions.

	}

	@Test
	public void testnewMethod() {
		this.setResourceDir("/test3");
		// rev1: Class A empty
		// rev 2: Class A
		// with method X
		this.doCommit();
		this.runDistiller();

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		HashMap<?, ?> map = (HashMap<?, ?>) manager.famixClassesMap;

		// Check that there are the required number of changes.
		if (changes.size() != 2) {
			fail("Incorrect amount of changes detected (" + changes.size()
					+ ")");
		}

		boolean seen_a = false;
		boolean seen_method = false;

		for (IChange c : changes) {
			String type = c.getChangeType();
			if (type.equals(CHANGE_ADD)) {
				String name = c.getName();
				if (c.getName().equals("A") && !seen_a
						&& c.getFamixType().equals(FAMIX_CLASS)) {
					seen_a = true;
				} else if (c.getName().equals("A.Hello") && !seen_method
						&& c.getFamixType().equals(FAMIX_METHOD)) {
					seen_method = true;
				} else {
					fail("Found an incorrect addition."); // Detects more
															// changes than
															// there should be.
				}
			} else {
				fail("Found an incorrect change type.");
			}
		}

		assertTrue(seen_a && seen_method);
	}

	@Test
	public void testMethodRemoval() {
		this.setResourceDir("/test4");
		this.doCommit();
		this.runDistiller();

		ModelManager manager = ModelManager.getInstance();
		List<IChange> changes = manager.getChanges();
		HashMap<?, ?> map = (HashMap<?, ?>) manager.famixClassesMap;

		// Check that there are the required number of changes.
		if (changes.size() != 3) {
			fail("Incorrect amount of changes detected (" + changes.size()
					+ ")");
		}

		int changeCount = 0;
		boolean seen_del = false;
		// Check for removal of method.
		for (IChange c : changes) {
			String type = c.getChangeType();
			if (type.equals(CHANGE_DEL)) {
				if(c.getName().equals("A.Hello") && !seen_del
						&& c.getFamixType().equals(FAMIX_METHOD)){
					seen_del = true;
				} else {
					fail("Found incorrect deletion");
				}
			} else if (type.equals(CHANGE_ADD)) {
				changeCount++;
			} else {
				fail("Found an incorrect change type.");
			}
		}

		assertEquals(2, changeCount);
		assertTrue(seen_del);
	}

}
