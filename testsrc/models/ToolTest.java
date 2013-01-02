package models;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.InputMismatchException;

import org.junit.Test;

import experiment.ExperimentSpecification;

public class ToolTest {

	@Test
	public void testTool() {
	//Check Creation, field sets properly
		Tool t1 = new Tool("name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		Tool t2 = new Tool("name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		Tool t3 = new Tool("name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		Tool t4 = new Tool("name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		Tool t5 = new Tool("name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		Tool t6 = new Tool("name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		Tool t7 = new Tool("name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
	
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
		
	// check exceptions
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
	public void testGetName() {
		Tool t1 = new Tool("name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		Tool t2 = new Tool("name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		Tool t3 = new Tool("name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		Tool t4 = new Tool("name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		Tool t5 = new Tool("name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		Tool t6 = new Tool("name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		Tool t7 = new Tool("name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
		
		assertEquals("name1", t1.getName());
		assertEquals("name2", t2.getName());
		assertEquals("name3", t3.getName());
		assertEquals("name4", t4.getName());
		assertEquals("name5", t5.getName());
		assertEquals("name6", t6.getName());
		assertEquals("name7", t7.getName());
	}

	@Test
	public void testGetDescription() {
		Tool t1 = new Tool("name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		Tool t2 = new Tool("name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		Tool t3 = new Tool("name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		Tool t4 = new Tool("name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		Tool t5 = new Tool("name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		Tool t6 = new Tool("name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		Tool t7 = new Tool("name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
		
		assertEquals("desc1", t1.getDescription());
		assertEquals("desc2", t2.getDescription());
		assertEquals("desc3", t3.getDescription());
		assertEquals("desc4", t4.getDescription());
		assertEquals("desc5", t5.getDescription());
		assertEquals("desc6", t6.getDescription());
		assertEquals("desc7", t7.getDescription());
	}

	@Test
	public void testGetDirectory() {
		Tool t1 = new Tool("name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		Tool t2 = new Tool("name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		Tool t3 = new Tool("name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		Tool t4 = new Tool("name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		Tool t5 = new Tool("name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		Tool t6 = new Tool("name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		Tool t7 = new Tool("name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
		
		assertEquals(Paths.get("/directory1"), t1.getDirectory());
		assertEquals(Paths.get("/directory2"), t2.getDirectory());
		assertEquals(Paths.get("/directory3"), t3.getDirectory());
		assertEquals(Paths.get("/directory4"), t4.getDirectory());
		assertEquals(Paths.get("/directory5"), t5.getDirectory());
		assertEquals(Paths.get("/directory6"), t6.getDirectory());
		assertEquals(Paths.get("/directory7"), t7.getDirectory());
	}

	@Test
	public void testGetToolRunner() {
		Tool t1 = new Tool("name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		Tool t2 = new Tool("name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		Tool t3 = new Tool("name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		Tool t4 = new Tool("name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		Tool t5 = new Tool("name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		Tool t6 = new Tool("name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		Tool t7 = new Tool("name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
		
		assertEquals(Paths.get("/toolrunner1"), t1.getToolRunner());
		assertEquals(Paths.get("/toolrunner2"), t2.getToolRunner());
		assertEquals(Paths.get("/toolrunner3"), t3.getToolRunner());
		assertEquals(Paths.get("/toolrunner4"), t4.getToolRunner());
		assertEquals(Paths.get("/toolrunner5"), t5.getToolRunner());
		assertEquals(Paths.get("/toolrunner6"), t6.getToolRunner());
		assertEquals(Paths.get("/toolrunner7"), t7.getToolRunner());
	}

	@Test
	public void testRunTool() throws InputMismatchException, IOException, IllegalArgumentException, NullPointerException, InterruptedException, InvalidToolRunnerException {
		Tool tool = new Tool("NiCad", "NiCad Clone Detector", Paths.get("testdata/ToolTest/NiCad"), Paths.get("testdata/ToolTest/NiCadRunner/NiCadRunner"));
		CloneDetectionReport report = tool.runTool(Paths.get("testdata/ToolTest/JHotDraw/"), ExperimentSpecification.JAVA_LANGUAGE, ExperimentSpecification.FUNCTION_FRAGMENT_TYPE, 10, 10, 10000, 10000, 0.30);
		assertNotNull(report);
		report.open();
		Clone c;
		int num = 0;
		while((c = report.next()) != null) {
			num++;
		}
		report.close();
		assertTrue(num > 0);
	}

	@Test
	public void testEqualsObject() {
		Tool t1 = new Tool("name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		Tool t2 = new Tool("name2", "desc2", Paths.get("/directory2"), Paths.get("/toolrunner2"));
		Tool t3 = new Tool("name3", "desc3", Paths.get("/directory3"), Paths.get("/toolrunner3"));
		Tool t4 = new Tool("name4", "desc4", Paths.get("/directory4"), Paths.get("/toolrunner4"));
		Tool t5 = new Tool("name5", "desc5", Paths.get("/directory5"), Paths.get("/toolrunner5"));
		Tool t6 = new Tool("name6", "desc6", Paths.get("/directory6"), Paths.get("/toolrunner6"));
		Tool t7 = new Tool("name7", "desc7", Paths.get("/directory7"), Paths.get("/toolrunner7"));
		Tool t8 = new Tool("name1", "desc1", Paths.get("/directory1"), Paths.get("/toolrunner1"));
		
		assertTrue(t1.equals(t1));
		assertTrue(t1.equals(t8));
		assertTrue(t8.equals(t1));
		assertTrue(!t1.equals(t2));
		assertTrue(!t1.equals(t3));
		assertTrue(!t1.equals(t4));
		assertTrue(!t1.equals(t5));
		assertTrue(!t1.equals(t6));
		assertTrue(!t1.equals(t7));
		assertTrue(!t1.equals(null));
	}

}
