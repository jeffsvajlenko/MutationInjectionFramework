package models;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

public class CloneTest {

	@Test
	public void testClone() {
		Clone c1 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c2 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_1);
		Clone c3 = new Clone(new Fragment(Paths.get("/file4"),20,100), new Fragment(Paths.get("/file3"),20,200), Clone.TYPE_2);
		Clone c4 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,2000), Clone.TYPE_3);
		Clone c5 = new Clone(new Fragment(Paths.get("/file"),10,1000), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_4);
		Clone c6 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_UNSPECIFIED);
		
		Fragment f11 = new Fragment(Paths.get("/file"),10,100);
		Fragment f21 = new Fragment(Paths.get("/file"),10,100);
		Fragment f31 = new Fragment(Paths.get("/file4"),20,100);
		Fragment f41 = new Fragment(Paths.get("/file"),10,100);
		Fragment f51 = new Fragment(Paths.get("/file"),10,1000);
		Fragment f61 = new Fragment(Paths.get("/file"),10,100);
		
		Fragment f12 = new Fragment(Paths.get("/file2"),20,200);
		Fragment f22 = new Fragment(Paths.get("/file2"),20,200);
		Fragment f32 = new Fragment(Paths.get("/file3"),20,200);
		Fragment f42 = new Fragment(Paths.get("/file2"),20,2000);
		Fragment f52 = new Fragment(Paths.get("/file2"),20,200);
		Fragment f62 = new Fragment(Paths.get("/file2"),20,200);
		
		//Check created properly
		assertEquals(f11, c1.getFragment1());
		assertEquals(f21, c2.getFragment1());
		assertEquals(f31, c3.getFragment1());
		assertEquals(f41, c4.getFragment1());
		assertEquals(f51, c5.getFragment1());
		assertEquals(f61, c6.getFragment1());
		
		assertEquals(f12, c1.getFragment2());
		assertEquals(f22, c2.getFragment2());
		assertEquals(f32, c3.getFragment2());
		assertEquals(f42, c4.getFragment2());
		assertEquals(f52, c5.getFragment2());
		assertEquals(f62, c6.getFragment2());
		
		assertEquals(Clone.TYPE_UNSPECIFIED, c1.getType());
		assertEquals(Clone.TYPE_1, c2.getType());
		assertEquals(Clone.TYPE_2, c3.getType());
		assertEquals(Clone.TYPE_3, c4.getType());
		assertEquals(Clone.TYPE_4, c5.getType());
		assertEquals(Clone.TYPE_UNSPECIFIED, c6.getType());
		
		//check error conditions
		boolean caught = false;
		
		caught = false;
		try {
			new Clone(null, new Fragment(Paths.get("/file2"),20,200));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Clone(new Fragment(Paths.get("/file2"),20,200), null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Clone(null, null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
	}

	@Test
	public void testGetFragment1() {
		Clone c1 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c2 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c3 = new Clone(new Fragment(Paths.get("/file4"),20,100), new Fragment(Paths.get("/file3"),20,200));
		Clone c4 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,2000));
		Clone c5 = new Clone(new Fragment(Paths.get("/file"),10,1000), new Fragment(Paths.get("/file2"),20,200));
		Clone c6 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		
		Fragment f1 = new Fragment(Paths.get("/file"),10,100);
		Fragment f2 = new Fragment(Paths.get("/file"),10,100);
		Fragment f3 = new Fragment(Paths.get("/file4"),20,100);
		Fragment f4 = new Fragment(Paths.get("/file"),10,100);
		Fragment f5 = new Fragment(Paths.get("/file"),10,1000);
		Fragment f6 = new Fragment(Paths.get("/file"),10,100);
		
		assertEquals(c1.getFragment1(), f1);
		assertEquals(c2.getFragment1(), f2);
		assertEquals(c3.getFragment1(), f3);
		assertEquals(c4.getFragment1(), f4);
		assertEquals(c5.getFragment1(), f5);
		assertEquals(c6.getFragment1(), f6);
	}

	@Test
	public void testGetFragment2() {
		Clone c1 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c2 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c3 = new Clone(new Fragment(Paths.get("/file4"),20,100), new Fragment(Paths.get("/file3"),20,200));
		Clone c4 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,2000));
		Clone c5 = new Clone(new Fragment(Paths.get("/file"),10,1000), new Fragment(Paths.get("/file2"),20,200));
		Clone c6 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		
		Fragment f1 = new Fragment(Paths.get("/file2"),20,200);
		Fragment f2 = new Fragment(Paths.get("/file2"),20,200);
		Fragment f3 = new Fragment(Paths.get("/file3"),20,200);
		Fragment f4 = new Fragment(Paths.get("/file2"),20,2000);
		Fragment f5 = new Fragment(Paths.get("/file2"),20,200);
		Fragment f6 = new Fragment(Paths.get("/file2"),20,200);
		
		assertEquals(c1.getFragment2(), f1);
		assertEquals(c2.getFragment2(), f2);
		assertEquals(c3.getFragment2(), f3);
		assertEquals(c4.getFragment2(), f4);
		assertEquals(c5.getFragment2(), f5);
		assertEquals(c6.getFragment2(), f6);
	}

	@Test
	public void testEqualsObject() {
		Clone c1 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c2 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c3 = new Clone(new Fragment(Paths.get("/file4"),20,100), new Fragment(Paths.get("/file3"),20,200));
		Clone c4 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,2000));
		Clone c5 = new Clone(new Fragment(Paths.get("/file"),10,1000), new Fragment(Paths.get("/file2"),20,200));
		Clone c6 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		
		assertTrue(c1.equals(c1));
		assertTrue(c1.equals(c2));
		assertTrue(c1.equals(c6));
		assertTrue(c2.equals(c1));
		assertTrue(c6.equals(c1));
		assertTrue(c2.equals(c6));
		assertTrue(c6.equals(c2));
		
		assertFalse(c1.equals(c3));
		assertFalse(c1.equals(c4));
		assertFalse(c1.equals(c5));
		assertFalse(c1.equals(null));
		
		
	}

	@Test
	public void testGetType() {
		Clone c1 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c2 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_1);
		Clone c3 = new Clone(new Fragment(Paths.get("/file4"),20,100), new Fragment(Paths.get("/file3"),20,200), Clone.TYPE_2);
		Clone c4 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,2000), Clone.TYPE_3);
		Clone c5 = new Clone(new Fragment(Paths.get("/file"),10,1000), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_4);
		Clone c6 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_UNSPECIFIED);
		
		assertEquals(Clone.TYPE_UNSPECIFIED, c1.getType());
		assertEquals(Clone.TYPE_1, c2.getType());
		assertEquals(Clone.TYPE_2, c3.getType());
		assertEquals(Clone.TYPE_3, c4.getType());
		assertEquals(Clone.TYPE_4, c5.getType());
		assertEquals(Clone.TYPE_UNSPECIFIED, c6.getType());
	}
	
	@Test
	public void testToString() {
		Clone c1 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200));
		Clone c2 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_1);
		Clone c3 = new Clone(new Fragment(Paths.get("/file4"),20,100), new Fragment(Paths.get("/file3"),20,200), Clone.TYPE_2);
		Clone c4 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,2000), Clone.TYPE_3);
		Clone c5 = new Clone(new Fragment(Paths.get("/file"),10,1000), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_4);
		Clone c6 = new Clone(new Fragment(Paths.get("/file"),10,100), new Fragment(Paths.get("/file2"),20,200), Clone.TYPE_UNSPECIFIED);
		
		assertEquals("Clone's toString() function reported unexpected result.", 
				"Fragment 1:" + c1.getFragment1().toString() + " Fragment 2:" + c1.getFragment2() + " Type: 0",
				c1.toString());
		assertEquals("Clone's toString() function reported unexpected result.",
				"Fragment 1:" + c2.getFragment1().toString() + " Fragment 2:" + c2.getFragment2() + " Type: 1",
				c2.toString());
		assertEquals("Clone's toString() function reported unexpected result.",
				"Fragment 1:" + c3.getFragment1().toString() + " Fragment 2:" + c3.getFragment2() + " Type: 2",
				c3.toString());
		assertEquals("Clone's toString() function reported unexpected result.",
				"Fragment 1:" + c4.getFragment1().toString() + " Fragment 2:" + c4.getFragment2() + " Type: 3",
				c4.toString());
		assertEquals("Clone's toString() function reported unexpected result.",
				"Fragment 1:" + c5.getFragment1().toString() + " Fragment 2:" + c5.getFragment2() + " Type: 4",
				c5.toString());
		assertEquals("Clone's toString() function reported unexpected result.",
				"Fragment 1:" + c6.getFragment1().toString() + " Fragment 2:" + c6.getFragment2() + " Type: 0",
				c6.toString());
	}
}
