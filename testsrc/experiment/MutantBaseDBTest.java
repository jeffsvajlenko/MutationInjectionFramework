package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import models.Fragment;
import models.MutantBase;

import org.junit.Test;

import experiment.MutantBaseDB;

public class MutantBaseDBTest {

	@Test
	public void testEqualsObject() {
		MutantBaseDB mb1 = new MutantBaseDB(1, Paths.get("/directory1"), 1, new Fragment(Paths.get("/directory1/osrcfile1"), 10, 100), new Fragment(Paths.get("/directory1/msrcfile1"), 15, 150));
		MutantBaseDB mb2 = new MutantBaseDB(2, Paths.get("/directory2"), 2, new Fragment(Paths.get("/directory2/osrcfile2"), 20, 200), new Fragment(Paths.get("/directory2/msrcfile2"), 25, 250));
		MutantBaseDB mb3 = new MutantBaseDB(3, Paths.get("/directory3"), 3, new Fragment(Paths.get("/directory3/osrcfile3"), 30, 300), new Fragment(Paths.get("/directory3/msrcfile3"), 35, 350));
		MutantBaseDB mb4 = new MutantBaseDB(4, Paths.get("/directory4"), 4, new Fragment(Paths.get("/directory4/osrcfile4"), 40, 400), new Fragment(Paths.get("/directory4/msrcfile4"), 45, 450));
		MutantBaseDB mb5 = new MutantBaseDB(5, Paths.get("/directory5"), 5, new Fragment(Paths.get("/directory5/osrcfile5"), 50, 500), new Fragment(Paths.get("/directory5/msrcfile5"), 55, 550));
		MutantBaseDB mb1c = new MutantBaseDB(1, Paths.get("/directory1"), 1, new Fragment(Paths.get("/directory1/osrcfile1"), 10, 100), new Fragment(Paths.get("/directory1/msrcfile1"), 15, 150));
		MutantBase mbdb = new MutantBase(Paths.get("/directory1"), 1, new Fragment(Paths.get("/directory1/osrcfile1"), 10, 100), new Fragment(Paths.get("/directory1/msrcfile1"), 15, 150));
		
		assertTrue(mb1.equals(mb1));
		assertTrue(mb1.equals(mb1c));
		assertTrue(mb1c.equals(mb1));
		assertFalse(mb1.equals(null));
		assertFalse(mb1.equals(mb2));
		assertFalse(mb1.equals(mb3));
		assertFalse(mb1.equals(mb4));
		assertFalse(mb1.equals(mb5));
		assertFalse(mb1.equals(mbdb));
	}

	@Test
	public void testMutantBaseDB() {
		MutantBaseDB mb1 = new MutantBaseDB(1, Paths.get("/directory1"), 1, new Fragment(Paths.get("/directory1/osrcfile1"), 10, 100), new Fragment(Paths.get("/directory1/msrcfile1"), 15, 150));
		MutantBaseDB mb2 = new MutantBaseDB(2, Paths.get("/directory2"), 2, new Fragment(Paths.get("/directory2/osrcfile2"), 20, 200), new Fragment(Paths.get("/directory2/msrcfile2"), 25, 250));
		MutantBaseDB mb3 = new MutantBaseDB(3, Paths.get("/directory3"), 3, new Fragment(Paths.get("/directory3/osrcfile3"), 30, 300), new Fragment(Paths.get("/directory3/msrcfile3"), 35, 350));
		MutantBaseDB mb4 = new MutantBaseDB(4, Paths.get("/directory4"), 4, new Fragment(Paths.get("/directory4/osrcfile4"), 40, 400), new Fragment(Paths.get("/directory4/msrcfile4"), 45, 450));
		MutantBaseDB mb5 = new MutantBaseDB(5, Paths.get("/directory5"), 5, new Fragment(Paths.get("/directory5/osrcfile5"), 50, 500), new Fragment(Paths.get("/directory5/msrcfile5"), 55, 550));
		
		assertEquals(1, mb1.getId());
		assertEquals(2, mb2.getId());
		assertEquals(3, mb3.getId());
		assertEquals(4, mb4.getId());
		assertEquals(5, mb5.getId());		
		
		assertTrue(mb1.getDirectory().equals(Paths.get("/directory1")));
		assertTrue(mb2.getDirectory().equals(Paths.get("/directory2")));
		assertTrue(mb3.getDirectory().equals(Paths.get("/directory3")));
		assertTrue(mb4.getDirectory().equals(Paths.get("/directory4")));
		assertTrue(mb5.getDirectory().equals(Paths.get("/directory5")));
		
		assertTrue(mb1.getMutantId() == 1);
		assertTrue(mb2.getMutantId() == 2);
		assertTrue(mb3.getMutantId() == 3);
		assertTrue(mb4.getMutantId() == 4);
		assertTrue(mb5.getMutantId() == 5);
		
		assertTrue(mb1.getMutantFragment().getSrcFile().equals(Paths.get("/directory1/msrcfile1")));
		assertTrue(mb2.getMutantFragment().getSrcFile().equals(Paths.get("/directory2/msrcfile2")));
		assertTrue(mb3.getMutantFragment().getSrcFile().equals(Paths.get("/directory3/msrcfile3")));
		assertTrue(mb4.getMutantFragment().getSrcFile().equals(Paths.get("/directory4/msrcfile4")));
		assertTrue(mb5.getMutantFragment().getSrcFile().equals(Paths.get("/directory5/msrcfile5")));
		
		assertTrue(mb1.getMutantFragment().getStartLine() == 15);
		assertTrue(mb2.getMutantFragment().getStartLine() == 25);
		assertTrue(mb3.getMutantFragment().getStartLine() == 35);
		assertTrue(mb4.getMutantFragment().getStartLine() == 45);
		assertTrue(mb5.getMutantFragment().getStartLine() == 55);
		
		assertTrue(mb1.getMutantFragment().getEndLine() == 150);
		assertTrue(mb2.getMutantFragment().getEndLine() == 250);
		assertTrue(mb3.getMutantFragment().getEndLine() == 350);
		assertTrue(mb4.getMutantFragment().getEndLine() == 450);
		assertTrue(mb5.getMutantFragment().getEndLine() == 550);
		
		assertTrue(mb1.getOriginalFragment().getSrcFile().equals(Paths.get("/directory1/osrcfile1")));
		assertTrue(mb2.getOriginalFragment().getSrcFile().equals(Paths.get("/directory2/osrcfile2")));
		assertTrue(mb3.getOriginalFragment().getSrcFile().equals(Paths.get("/directory3/osrcfile3")));
		assertTrue(mb4.getOriginalFragment().getSrcFile().equals(Paths.get("/directory4/osrcfile4")));
		assertTrue(mb5.getOriginalFragment().getSrcFile().equals(Paths.get("/directory5/osrcfile5")));
		
		assertTrue(mb1.getOriginalFragment().getStartLine() == 10);
		assertTrue(mb2.getOriginalFragment().getStartLine() == 20);
		assertTrue(mb3.getOriginalFragment().getStartLine() == 30);
		assertTrue(mb4.getOriginalFragment().getStartLine() == 40);
		assertTrue(mb5.getOriginalFragment().getStartLine() == 50);
		
		assertTrue(mb1.getOriginalFragment().getEndLine() == 100);
		assertTrue(mb2.getOriginalFragment().getEndLine() == 200);
		assertTrue(mb3.getOriginalFragment().getEndLine() == 300);
		assertTrue(mb4.getOriginalFragment().getEndLine() == 400);
		assertTrue(mb5.getOriginalFragment().getEndLine() == 500);
		
		
		boolean error;
		
		error = false;
		try {
			new MutantBaseDB(1, null, 5, new Fragment(Paths.get("/srcfile5"), 50, 500), new Fragment(Paths.get("/directory5/osrcfile5"), 55, 550));
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			new MutantBaseDB(1, Paths.get("/directory5"), 5, null, new Fragment(Paths.get("/directory5/osrcfile5"), 55, 550));
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			new MutantBaseDB(1, Paths.get("/directory5"), 5, new Fragment(Paths.get("/srcfile5"), 50, 500), null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			new MutantBaseDB(1, Paths.get("/directory1"), 1, new Fragment(Paths.get("/directory2/osrcfile1"), 10, 100), new Fragment(Paths.get("/directory1/msrcfile1"), 15, 150));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			new MutantBaseDB(1, Paths.get("/directory1"), 1, new Fragment(Paths.get("/directory1/osrcfile1"), 10, 100), new Fragment(Paths.get("/directory2/msrcfile1"), 15, 150));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testGetId() {
		MutantBaseDB mb1 = new MutantBaseDB(1, Paths.get("/directory1"), 1, new Fragment(Paths.get("/directory1/osrcfile1"), 10, 100), new Fragment(Paths.get("/directory1/msrcfile1"), 15, 150));
		MutantBaseDB mb2 = new MutantBaseDB(2, Paths.get("/directory2"), 2, new Fragment(Paths.get("/directory2/osrcfile2"), 20, 200), new Fragment(Paths.get("/directory2/msrcfile2"), 25, 250));
		MutantBaseDB mb3 = new MutantBaseDB(3, Paths.get("/directory3"), 3, new Fragment(Paths.get("/directory3/osrcfile3"), 30, 300), new Fragment(Paths.get("/directory3/msrcfile3"), 35, 350));
		MutantBaseDB mb4 = new MutantBaseDB(4, Paths.get("/directory4"), 4, new Fragment(Paths.get("/directory4/directory4/osrcfile4"), 40, 400), new Fragment(Paths.get("/directory4/msrcfile4"), 45, 450));
		MutantBaseDB mb5 = new MutantBaseDB(5, Paths.get("/directory5"), 5, new Fragment(Paths.get("/directory5/osrcfile5"), 50, 500), new Fragment(Paths.get("/directory5/msrcfile5"), 55, 550));
		
		assertEquals(1, mb1.getId());
		assertEquals(2, mb2.getId());
		assertEquals(3, mb3.getId());
		assertEquals(4, mb4.getId());
		assertEquals(5, mb5.getId());
	}

}
