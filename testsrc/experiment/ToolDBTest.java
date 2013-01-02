package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import models.Tool;

import org.junit.Test;

import experiment.ToolDB;

public class ToolDBTest {

	@Test
	public void testToolDB() {
		ToolDB t1 = new ToolDB(1, "name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		ToolDB t2 = new ToolDB(2, "name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		ToolDB t3 = new ToolDB(3, "name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		ToolDB t4 = new ToolDB(4, "name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		ToolDB t5 = new ToolDB(5, "name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		ToolDB t6 = new ToolDB(6, "name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		ToolDB t7 = new ToolDB(7, "name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
		
		assertEquals(1, t1.getId());
		assertEquals(2, t2.getId());
		assertEquals(3, t3.getId());
		assertEquals(4, t4.getId());
		assertEquals(5, t5.getId());
		assertEquals(6, t6.getId());
		assertEquals(7, t7.getId());
		
		assertEquals("name1", t1.getName());
		assertEquals("name2", t2.getName());
		assertEquals("name3", t3.getName());
		assertEquals("name4", t4.getName());
		assertEquals("name5", t5.getName());
		assertEquals("name6", t6.getName());
		assertEquals("name7", t7.getName());
		
		assertEquals("desc1", t1.getDescription());
		assertEquals("desc2", t2.getDescription());
		assertEquals("desc3", t3.getDescription());
		assertEquals("desc4", t4.getDescription());
		assertEquals("desc5", t5.getDescription());
		assertEquals("desc6", t6.getDescription());
		assertEquals("desc7", t7.getDescription());
		
		assertEquals(Paths.get("/directory1"), t1.getDirectory());
		assertEquals(Paths.get("/directory2"), t2.getDirectory());
		assertEquals(Paths.get("/directory3"), t3.getDirectory());
		assertEquals(Paths.get("/directory4"), t4.getDirectory());
		assertEquals(Paths.get("/directory5"), t5.getDirectory());
		assertEquals(Paths.get("/directory6"), t6.getDirectory());
		assertEquals(Paths.get("/directory7"), t7.getDirectory());
		
		assertEquals(Paths.get("/toolrunner1"), t1.getToolRunner());
		assertEquals(Paths.get("/toolrunner2"), t2.getToolRunner());
		assertEquals(Paths.get("/toolrunner3"), t3.getToolRunner());
		assertEquals(Paths.get("/toolrunner4"), t4.getToolRunner());
		assertEquals(Paths.get("/toolrunner5"), t5.getToolRunner());
		assertEquals(Paths.get("/toolrunner6"), t6.getToolRunner());
		assertEquals(Paths.get("/toolrunner7"), t7.getToolRunner());
		
	//Check exceptions
		boolean caught;
		caught = false;
		try {
			new Tool(null, "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool("name", null, Paths.get("/directory1"), Paths.get("/toolrunner1"));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool("name", "desc1", null, Paths.get("/toolrunner1"));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool("name", "desc1", Paths.get("/directory1"), null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool(null, null, Paths.get("/directory1"), Paths.get("/toolrunner1"));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool(null, "desc1", null, Paths.get("/toolrunner1"));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool(null, "desc1", Paths.get("/directory1"), null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool("name", null, null, Paths.get("/toolrunner1"));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool("name", null, Paths.get("/directory1"), null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool("name", "desc1", null, null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool(null, null, null, Paths.get("/toolrunner1"));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool(null, null, Paths.get("/directory1"), null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool(null, "desc1", null, null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Tool(null, null, null, null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
	}

	@Test
	public void testGetId() {
		ToolDB t1 = new ToolDB(1, "name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		ToolDB t2 = new ToolDB(2, "name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		ToolDB t3 = new ToolDB(3, "name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		ToolDB t4 = new ToolDB(4, "name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		ToolDB t5 = new ToolDB(5, "name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		ToolDB t6 = new ToolDB(6, "name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		ToolDB t7 = new ToolDB(7, "name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
		
		assertEquals(1, t1.getId());
		assertEquals(2, t2.getId());
		assertEquals(3, t3.getId());
		assertEquals(4, t4.getId());
		assertEquals(5, t5.getId());
		assertEquals(6, t6.getId());
		assertEquals(7, t7.getId());
	}

	@Test
	public void testEqualsObject() {
		ToolDB t1 = new ToolDB(1, "name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		ToolDB t2 = new ToolDB(2, "name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		ToolDB t3 = new ToolDB(3, "name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		ToolDB t4 = new ToolDB(4, "name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		ToolDB t5 = new ToolDB(5, "name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		ToolDB t6 = new ToolDB(6, "name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		ToolDB t7 = new ToolDB(7, "name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
		ToolDB t8 = new ToolDB(1, "name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		
		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t8));
		assertTrue(t8.equals(t1));
		assertFalse(t1.equals(null));
		assertFalse(t1.equals(t2));
		assertFalse(t1.equals(t3));
		assertFalse(t1.equals(t4));
		assertFalse(t1.equals(t5));
		assertFalse(t1.equals(t6));
		assertFalse(t1.equals(t7));
	}
}
