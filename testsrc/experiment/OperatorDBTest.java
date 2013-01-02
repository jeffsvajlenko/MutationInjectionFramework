package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import experiment.OperatorDB;

public class OperatorDBTest {

	@Test
	public void testOperatorDB() {
		OperatorDB o;
		o = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals(1, o.getId());
		assertEquals("id", o.getName());
		assertEquals("description", o.getDescription());
		assertEquals(1, o.getTargetCloneType());
		
		OperatorDB o2;
		o2 = new OperatorDB(2, "id2", "description2", 3, Paths.get("operators/mIL"));
		assertEquals(2, o2.getId());
		assertEquals("id2", o2.getName());
		assertEquals("description2", o2.getDescription());
		assertEquals(3, o2.getTargetCloneType());
		
		boolean throwexception;
		
	// NullPointerExceptions
		throwexception = false;
		try {
			new OperatorDB(1, null, "description", 1, Paths.get("operators/mCC_EOL"));
		} catch(NullPointerException e) {
			throwexception = true;
		}
		assertTrue(throwexception);
		
		throwexception = false;
		try {
			new OperatorDB(1, "id", null, 1, Paths.get("operators/mCC_EOL"));
		} catch(NullPointerException e) {
			throwexception = true;
		}
		assertTrue(throwexception);
		
		throwexception = false;
		try {
			new OperatorDB(1, "id", "description", 1, null);
		} catch(NullPointerException e) {
			throwexception = true;
		}
		assertTrue(throwexception);
	
	// IllegalArgumentException
		throwexception = false;
		try {
			new OperatorDB(1, "id", "description", 1, Paths.get("operators/wqeqwe"));
		} catch (IllegalArgumentException e) {
			throwexception = true;
		}
	}

	@Test
	public void testGetId() {
		OperatorDB o = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB oe = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB od1 = new OperatorDB(2, "id_", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB od2 = new OperatorDB(3, "id", "description_", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB od3 = new OperatorDB(4, "id", "description", 2, Paths.get("operators/mCC_EOL"));
		OperatorDB od4 = new OperatorDB(5, "id", "description", 1, Paths.get("operators/mCC_BT"));
		
		assertEquals(1,o.getId());
		assertEquals(1,oe.getId());
		assertEquals(2,od1.getId());
		assertEquals(3,od2.getId());
		assertEquals(4,od3.getId());
		assertEquals(5,od4.getId());
	}

	@Test
	public void testEqualsObject() {
		OperatorDB o = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB oe = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB od1 = new OperatorDB(2, "id_", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB od2 = new OperatorDB(3, "id", "description_", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB od3 = new OperatorDB(4, "id", "description", 2, Paths.get("operators/mCC_EOL"));
		OperatorDB od4 = new OperatorDB(5, "id", "description", 1, Paths.get("operators/mCC_BT"));
		
		assertTrue(o.equals(oe));
		assertTrue(o.equals(o));
		assertTrue(!o.equals(od1));
		assertTrue(!o.equals(od2));
		assertTrue(!o.equals(od3));
		assertTrue(!o.equals(od4));
		assertTrue(!o.equals(null));
	}
	
}
