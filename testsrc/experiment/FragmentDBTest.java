package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import models.Fragment;

import org.junit.Test;

import experiment.FragmentDB;

public class FragmentDBTest {

	@Test
	public void testEqualsObject() {
		FragmentDB f1 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f2 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f3 = new FragmentDB(2, Paths.get("/ofolder/file1"), Paths.get("/folder/file2"), 3, 30);
		FragmentDB f4 = new FragmentDB(3, Paths.get("/ofolder/file2"), Paths.get("/folder/file3"), 4, 40);
		FragmentDB f5 = new FragmentDB(4, Paths.get("/ofolder/file3"), Paths.get("/folder/file4"), 5, 50);
		Fragment f6 = new Fragment(Paths.get("/folder/file1"), 2, 10);
		
		assertTrue(f1.equals(f1));
		assertTrue(f1.equals(f2));
		assertTrue(f2.equals(f1));
		assertTrue(!f1.equals(f3));
		assertTrue(!f1.equals(f4));
		assertTrue(!f1.equals(f5));
		assertTrue(!f1.equals(f6));
		assertTrue(!f1.equals(null));
	}

	@Test
	public void testToString() {
		FragmentDB f1 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f2 = new FragmentDB(1, Paths.get("/ofolder/file2"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f3 = new FragmentDB(2, Paths.get("/ofolder/file3"), Paths.get("/folder/file2"), 3, 30);
		FragmentDB f4 = new FragmentDB(3, Paths.get("/ofolder/file4"), Paths.get("/folder/file3"), 4, 40);
		FragmentDB f5 = new FragmentDB(4, Paths.get("/ofolder/file5"), Paths.get("/folder/file4"), 5, 50);
		FragmentDB f6 = new FragmentDB(5, Paths.get("/ofolder/file6"), Paths.get("/folder/file5"), 6, 60);
		
		assertEquals("Fragment(1) : /ofolder/file1 /folder/file1 2-10", f1.toString());
		assertEquals("Fragment(1) : /ofolder/file2 /folder/file1 2-10", f2.toString());
		assertEquals("Fragment(2) : /ofolder/file3 /folder/file2 3-30", f3.toString());
		assertEquals("Fragment(3) : /ofolder/file4 /folder/file3 4-40", f4.toString());
		assertEquals("Fragment(4) : /ofolder/file5 /folder/file4 5-50", f5.toString());
		assertEquals("Fragment(5) : /ofolder/file6 /folder/file5 6-60", f6.toString());
		
		
	}

	@Test
	public void testGetFragmentFile() {
		FragmentDB f1 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f2 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f3 = new FragmentDB(2, Paths.get("/ofolder/file2"), Paths.get("/folder/file2"), 3, 30);
		FragmentDB f4 = new FragmentDB(3, Paths.get("/ofolder/file3"), Paths.get("/folder/file3"), 4, 40);
		FragmentDB f5 = new FragmentDB(4, Paths.get("/ofolder/file4"), Paths.get("/folder/file4"), 5, 50);
		FragmentDB f6 = new FragmentDB(5, Paths.get("/ofolder/file5"), Paths.get("/folder/file5"), 6, 60);
		
		assertEquals(Paths.get("/ofolder/file1").toAbsolutePath().normalize(), f1.getFragmentFile());
		assertEquals(Paths.get("/ofolder/file1").toAbsolutePath().normalize(), f2.getFragmentFile());
		assertEquals(Paths.get("/ofolder/file2").toAbsolutePath().normalize(), f3.getFragmentFile());
		assertEquals(Paths.get("/ofolder/file3").toAbsolutePath().normalize(), f4.getFragmentFile());
		assertEquals(Paths.get("/ofolder/file4").toAbsolutePath().normalize(), f5.getFragmentFile());
		assertEquals(Paths.get("/ofolder/file5").toAbsolutePath().normalize(), f6.getFragmentFile());
	}
	
	@Test public void testGetId() {
		FragmentDB f1 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f2 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f3 = new FragmentDB(2, Paths.get("/ofolder/file2"), Paths.get("/folder/file2"), 3, 30);
		FragmentDB f4 = new FragmentDB(3, Paths.get("/ofolder/file3"), Paths.get("/folder/file3"), 4, 40);
		FragmentDB f5 = new FragmentDB(4, Paths.get("/ofolder/file4"), Paths.get("/folder/file4"), 5, 50);
		FragmentDB f6 = new FragmentDB(5, Paths.get("/ofolder/file5"), Paths.get("/folder/file5"), 6, 60);
		
		assertEquals(1, f1.getId());
		assertEquals(1, f2.getId());
		assertEquals(2, f3.getId());
		assertEquals(4, f5.getId());
		assertEquals(5, f6.getId());
		assertEquals(5, f6.getId());
	}
	
	@Test
	public void testFragmentDB() {
		FragmentDB f1 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f2 = new FragmentDB(1, Paths.get("/ofolder/file1"), Paths.get("/folder/file1"), 2, 10);
		FragmentDB f3 = new FragmentDB(2, Paths.get("/ofolder/file2"), Paths.get("/folder/file2"), 3, 30);
		FragmentDB f4 = new FragmentDB(3, Paths.get("/ofolder/file3"), Paths.get("/folder/file3"), 4, 40);
		FragmentDB f5 = new FragmentDB(4, Paths.get("/ofolder/file4"), Paths.get("/folder/file4"), 5, 50);
		FragmentDB f6 = new FragmentDB(5, Paths.get("/ofolder/file5"), Paths.get("/folder/file5"), 6, 60);
		
		assertEquals(1, f1.getId());
		assertEquals(Paths.get("/ofolder/file1").toAbsolutePath().normalize(), f1.getFragmentFile());
		assertEquals(Paths.get("/folder/file1").toAbsolutePath().normalize(), f1.getSrcFile());
		assertEquals(2, f1.getStartLine());
		assertEquals(10, f1.getEndLine());
		
		assertEquals(1, f2.getId());
		assertEquals(Paths.get("/ofolder/file1").toAbsolutePath().normalize(), f2.getFragmentFile());
		assertEquals(Paths.get("/folder/file1").toAbsolutePath().normalize(), f2.getSrcFile());
		assertEquals(2, f2.getStartLine());
		assertEquals(10, f2.getEndLine());
		
		assertEquals(2, f3.getId());
		assertEquals(Paths.get("/ofolder/file2").toAbsolutePath().normalize(), f3.getFragmentFile());
		assertEquals(Paths.get("/folder/file2").toAbsolutePath().normalize(), f3.getSrcFile());
		assertEquals(3, f3.getStartLine());
		assertEquals(30, f3.getEndLine());
		
		assertEquals(3, f4.getId());
		assertEquals(Paths.get("/ofolder/file3").toAbsolutePath().normalize(), f4.getFragmentFile());
		assertEquals(Paths.get("/folder/file3").toAbsolutePath().normalize(), f4.getSrcFile());
		assertEquals(4, f4.getStartLine());
		assertEquals(40, f4.getEndLine());
		
		assertEquals(4, f5.getId());
		assertEquals(Paths.get("/ofolder/file4").toAbsolutePath().normalize(), f5.getFragmentFile());
		assertEquals(Paths.get("/folder/file4").toAbsolutePath().normalize(), f5.getSrcFile());
		assertEquals(5, f5.getStartLine());
		assertEquals(50, f5.getEndLine());
		
		assertEquals(5, f6.getId());
		assertEquals(Paths.get("/ofolder/file5").toAbsolutePath().normalize(), f6.getFragmentFile());
		assertEquals(Paths.get("/folder/file5").toAbsolutePath().normalize(), f6.getSrcFile());
		assertEquals(6, f6.getStartLine());
		assertEquals(60, f6.getEndLine());
		
		boolean caught;
		
		caught = false;
		try {
			new FragmentDB(1, Paths.get("/file"), null, 1, 10);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new FragmentDB(1, null, Paths.get("/file"), 1, 10);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new FragmentDB(1, Paths.get("/file"), Paths.get("folder/file1"), 100, 10);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}

}
