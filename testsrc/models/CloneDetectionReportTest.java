package models;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.InputMismatchException;

import org.junit.Test;

import util.SystemUtil;

import experiment.CloneDetectionReportDB;

public class CloneDetectionReportTest {

	@Test
	public void testCloneDetectionReport() {
		CloneDetectionReport cdr1 = new CloneDetectionReport(Paths.get("/1"));
		CloneDetectionReport cdr2 = new CloneDetectionReport(Paths.get("/2"));
		CloneDetectionReport cdr3 = new CloneDetectionReport(Paths.get("/3"));
		CloneDetectionReport cdr4 = new CloneDetectionReport(Paths.get("/4"));
		CloneDetectionReport cdr5 = new CloneDetectionReport(Paths.get("/5"));
		
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
	public void testGetReport() {
		CloneDetectionReport cdr1 = new CloneDetectionReport(Paths.get("/1"));
		CloneDetectionReport cdr2 = new CloneDetectionReport(Paths.get("/2"));
		CloneDetectionReport cdr3 = new CloneDetectionReport(Paths.get("/3"));
		CloneDetectionReport cdr4 = new CloneDetectionReport(Paths.get("/4"));
		CloneDetectionReport cdr5 = new CloneDetectionReport(Paths.get("/5"));
		
		assertEquals(Paths.get("/1"), cdr1.getReport());
		assertEquals(Paths.get("/2"), cdr2.getReport());
		assertEquals(Paths.get("/3"), cdr3.getReport());
		assertEquals(Paths.get("/4"), cdr4.getReport());
		assertEquals(Paths.get("/5"), cdr5.getReport());
	}

	@Test
	public void testOpenCloseNext() throws InputMismatchException, IOException {
		CloneDetectionReport crr = new CloneDetectionReport(SystemUtil.getInstallRoot().resolve("testdata/CloneDetectionReportTest/report.txt"));
		Clone c;
		
		crr.open();
		c = crr.next();
		assertTrue(c!=null);
		assertTrue(c.getFragment1().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java")));
		assertTrue(c.getFragment1().getStartLine() == 101);
		assertTrue(c.getFragment1().getEndLine() == 112);
		assertTrue(c.getFragment2().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/core/PortIterator.java")));
		assertTrue(c.getFragment2().getStartLine() == 105);
		assertTrue(c.getFragment2().getEndLine() == 116);
		
		c = crr.next();
		assertTrue(c!=null);
		assertTrue(c.getFragment1().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java")));
		assertTrue(c.getFragment1().getStartLine() == 101);
		assertTrue(c.getFragment1().getEndLine() == 112);
		assertTrue(c.getFragment2().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/exporters/AbstractExporter.java")));
		assertTrue(c.getFragment2().getStartLine() == 50);
		assertTrue(c.getFragment2().getEndLine() == 61);
		
		c = crr.next();
		assertTrue(c!=null);
		assertTrue(c.getFragment1().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/fetchers/AbstractFetcher.java")));
		assertTrue(c.getFragment1().getStartLine() == 30);
		assertTrue(c.getFragment1().getEndLine() == 40);
		assertTrue(c.getFragment2().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/exporters/AbstractExporter.java")));
		assertTrue(c.getFragment2().getStartLine() == 50);
		assertTrue(c.getFragment2().getEndLine() == 61);
		
		c = crr.next();
		assertTrue(c==null);
		c = crr.next();
		assertTrue(c==null);
		c = crr.next();
		assertTrue(c==null);
		
		crr.close();
		crr.open();
		c = crr.next();
		assertTrue(c!=null);
		assertTrue(c.getFragment1().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java")));
		assertTrue(c.getFragment1().getStartLine() == 101);
		assertTrue(c.getFragment1().getEndLine() == 112);
		assertTrue(c.getFragment2().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/core/PortIterator.java")));
		assertTrue(c.getFragment2().getStartLine() == 105);
		assertTrue(c.getFragment2().getEndLine() == 116);
		
		c = crr.next();
		assertTrue(c!=null);
		assertTrue(c.getFragment1().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java")));
		assertTrue(c.getFragment1().getStartLine() == 101);
		assertTrue(c.getFragment1().getEndLine() == 112);
		assertTrue(c.getFragment2().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/exporters/AbstractExporter.java")));
		assertTrue(c.getFragment2().getStartLine() == 50);
		assertTrue(c.getFragment2().getEndLine() == 61);
		
		c = crr.next();
		assertTrue(c!=null);
		assertTrue(c.getFragment1().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/fetchers/AbstractFetcher.java")));
		assertTrue(c.getFragment1().getStartLine() == 30);
		assertTrue(c.getFragment1().getEndLine() == 40);
		assertTrue(c.getFragment2().getSrcFile().equals(Paths.get("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/bases/angryipscanner/net/azib/ipscan/exporters/AbstractExporter.java")));
		assertTrue(c.getFragment2().getStartLine() == 50);
		assertTrue(c.getFragment2().getEndLine() == 61);
		
		c = crr.next();
		assertTrue(c==null);
		c = crr.next();
		assertTrue(c==null);
		c = crr.next();
		assertTrue(c==null);
		
		crr.close();
		
		boolean tryfail;
		tryfail = false;
		try {
			crr.next();
		} catch(IOException e) {
			tryfail = true;
		}
		assertTrue(tryfail);
		
		tryfail = false;
		try {
			crr.next();
		} catch(IOException e) {
			tryfail = true;
		}
		assertTrue(tryfail);
		
		crr.open();
		
		//not file
		tryfail = false;
		try {
			crr = new CloneDetectionReport(Paths.get("/blah/blah/blah"));
			crr.open();
		} catch(FileNotFoundException e) {
			tryfail = true;
		}
		assertTrue(tryfail);
		
		//empty
		crr = new CloneDetectionReport(SystemUtil.getInstallRoot().resolve("testdata/CloneDetectionReportTest/clones_e.txt"));
		crr.open();
		assertTrue(crr.next()==null);
	}

	@Test
	public void testEqualsObject() {
		CloneDetectionReport cdr1 = new CloneDetectionReport(Paths.get("/1"));
		CloneDetectionReport cdr2 = new CloneDetectionReport(Paths.get("/2"));
		CloneDetectionReport cdr3 = new CloneDetectionReport(Paths.get("/3"));
		CloneDetectionReport cdr4 = new CloneDetectionReport(Paths.get("/4"));
		CloneDetectionReport cdr5 = new CloneDetectionReport(Paths.get("/5"));
		CloneDetectionReport cdr6 = new CloneDetectionReport(Paths.get("/1"));
		CloneDetectionReportDB cdrdb = new CloneDetectionReportDB(1, 1, Paths.get("/1"));
		
		assertTrue(cdr1.equals(cdr1));
		assertTrue(cdr1.equals(cdr6));
		assertFalse(cdr1.equals(null));
		assertFalse(cdr1.equals(cdr2));
		assertFalse(cdr1.equals(cdr3));
		assertFalse(cdr1.equals(cdr4));
		assertFalse(cdr1.equals(cdr5));
		assertFalse(cdr1.equals(cdrdb));
	}

}
