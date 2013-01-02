package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import models.CloneDetectionReport;

import org.junit.Test;

import experiment.CloneDetectionReportDB;

public class CloneDetectionReportDBTest {

	@Test
	public void testEqualsObject() {
		CloneDetectionReportDB cdr1 = new CloneDetectionReportDB(1, 10, Paths.get("/1"));
		CloneDetectionReportDB cdr2 = new CloneDetectionReportDB(2, 20, Paths.get("/2"));
		CloneDetectionReportDB cdr3 = new CloneDetectionReportDB(3, 30, Paths.get("/3"));
		CloneDetectionReportDB cdr4 = new CloneDetectionReportDB(4, 40, Paths.get("/4"));
		CloneDetectionReportDB cdr5 = new CloneDetectionReportDB(5, 50, Paths.get("/5"));
		CloneDetectionReportDB cdr6 = new CloneDetectionReportDB(1, 10, Paths.get("/1"));
		CloneDetectionReport cdr = new CloneDetectionReport(Paths.get("/1"));
		
		assertTrue(cdr1.equals(cdr1));
		assertTrue(cdr1.equals(cdr6));
		assertTrue(cdr6.equals(cdr1));
		assertFalse(cdr1.equals(null));
		assertFalse(cdr1.equals(cdr2));
		assertFalse(cdr1.equals(cdr3));
		assertFalse(cdr1.equals(cdr4));
		assertFalse(cdr1.equals(cdr5));
		assertFalse(cdr1.equals(cdr));
	}
	
	@Test
	public void testCloneDetectionReportDB() {
		CloneDetectionReportDB cdr1 = new CloneDetectionReportDB(1, 10, Paths.get("/1"));
		CloneDetectionReportDB cdr2 = new CloneDetectionReportDB(2, 20, Paths.get("/2"));
		CloneDetectionReportDB cdr3 = new CloneDetectionReportDB(3, 30, Paths.get("/3"));
		CloneDetectionReportDB cdr4 = new CloneDetectionReportDB(4, 40, Paths.get("/4"));
		CloneDetectionReportDB cdr5 = new CloneDetectionReportDB(5, 50, Paths.get("/5"));
		
		assertEquals(1, cdr1.getToolId());
		assertEquals(2, cdr2.getToolId());
		assertEquals(3, cdr3.getToolId());
		assertEquals(4, cdr4.getToolId());
		assertEquals(5, cdr5.getToolId());
		
		assertEquals(10, cdr1.getBaseId());
		assertEquals(20, cdr2.getBaseId());
		assertEquals(30, cdr3.getBaseId());
		assertEquals(40, cdr4.getBaseId());
		assertEquals(50, cdr5.getBaseId());
		
		assertEquals(Paths.get("/1"), cdr1.getReport());
		assertEquals(Paths.get("/2"), cdr2.getReport());
		assertEquals(Paths.get("/3"), cdr3.getReport());
		assertEquals(Paths.get("/4"), cdr4.getReport());
		assertEquals(Paths.get("/5"), cdr5.getReport());
		
		boolean error;
		
		error = false;
		try {
			new CloneDetectionReport(null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testGetToolId() {
		CloneDetectionReportDB cdr1 = new CloneDetectionReportDB(1, 10, Paths.get("/1"));
		CloneDetectionReportDB cdr2 = new CloneDetectionReportDB(2, 20, Paths.get("/2"));
		CloneDetectionReportDB cdr3 = new CloneDetectionReportDB(3, 30, Paths.get("/3"));
		CloneDetectionReportDB cdr4 = new CloneDetectionReportDB(4, 40, Paths.get("/4"));
		CloneDetectionReportDB cdr5 = new CloneDetectionReportDB(5, 50, Paths.get("/5"));
		
		assertEquals(1, cdr1.getToolId());
		assertEquals(2, cdr2.getToolId());
		assertEquals(3, cdr3.getToolId());
		assertEquals(4, cdr4.getToolId());
		assertEquals(5, cdr5.getToolId());
	}

	@Test
	public void testGetBaseId() {
		CloneDetectionReportDB cdr1 = new CloneDetectionReportDB(1, 10, Paths.get("/1"));
		CloneDetectionReportDB cdr2 = new CloneDetectionReportDB(2, 20, Paths.get("/2"));
		CloneDetectionReportDB cdr3 = new CloneDetectionReportDB(3, 30, Paths.get("/3"));
		CloneDetectionReportDB cdr4 = new CloneDetectionReportDB(4, 40, Paths.get("/4"));
		CloneDetectionReportDB cdr5 = new CloneDetectionReportDB(5, 50, Paths.get("/5"));
		
		assertEquals(10, cdr1.getBaseId());
		assertEquals(20, cdr2.getBaseId());
		assertEquals(30, cdr3.getBaseId());
		assertEquals(40, cdr4.getBaseId());
		assertEquals(50, cdr5.getBaseId());
		
	}

}
