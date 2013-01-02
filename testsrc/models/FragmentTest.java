package models;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

public class FragmentTest {

	@Test
	public void testFragment() {
		Fragment f1 = new Fragment(Paths.get("folder/file1"), 2, 10);
		assertEquals(Paths.get("folder/file1").toAbsolutePath().normalize(), f1.getSrcFile());
		assertEquals(2, f1.getStartLine());
		assertEquals(10, f1.getEndLine());
		
		Fragment f2 = new Fragment(Paths.get("folder/file1"), 2, 10);
		assertEquals(Paths.get("folder/file1").toAbsolutePath().normalize(), f2.getSrcFile());
		assertEquals(2, f2.getStartLine());
		assertEquals(10, f2.getEndLine());
		
		Fragment f3 = new Fragment(Paths.get("folder/file2"), 3, 30);
		assertEquals(Paths.get("folder/file2").toAbsolutePath().normalize(), f3.getSrcFile());
		assertEquals(3, f3.getStartLine());
		assertEquals(30, f3.getEndLine());
		
		Fragment f4 = new Fragment(Paths.get("folder/file3"), 4, 40);
		assertEquals(Paths.get("folder/file3").toAbsolutePath().normalize(), f4.getSrcFile());
		assertEquals(4, f4.getStartLine());
		assertEquals(40, f4.getEndLine());
		
		Fragment f5 = new Fragment(Paths.get("folder/file4"), 5, 50);
		assertEquals(Paths.get("folder/file4").toAbsolutePath().normalize(), f5.getSrcFile());
		assertEquals(5, f5.getStartLine());
		assertEquals(50, f5.getEndLine());
		
		Fragment f6 = new Fragment(Paths.get("folder/file5"), 6, 60);
		assertEquals(Paths.get("folder/file5").toAbsolutePath().normalize(), f6.getSrcFile());
		assertEquals(6, f6.getStartLine());
		assertEquals(60, f6.getEndLine());
		
		boolean caught;
		
		caught = false;
		try {
			new Fragment(null, 1, 10);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new Fragment(Paths.get("folder/file1"), 100, 10);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}

	@Test
	public void testGetSrcFile() {
		Fragment f1 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f2 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f3 = new Fragment(Paths.get("folder/file2"), 3, 30);
		Fragment f4 = new Fragment(Paths.get("folder/file3"), 4, 40);
		Fragment f5 = new Fragment(Paths.get("folder/file4"), 5, 50);
		Fragment f6 = new Fragment(Paths.get("folder/file5"), 6, 60);
		
		assertEquals(Paths.get("").resolve("folder/file1").toAbsolutePath().normalize(), f1.getSrcFile());
		assertEquals(Paths.get("").resolve("folder/file1").toAbsolutePath().normalize(), f2.getSrcFile());
		assertEquals(Paths.get("").resolve("folder/file2").toAbsolutePath().normalize(), f3.getSrcFile());
		assertEquals(Paths.get("").resolve("folder/file3").toAbsolutePath().normalize(), f4.getSrcFile());
		assertEquals(Paths.get("").resolve("folder/file4").toAbsolutePath().normalize(), f5.getSrcFile());
		assertEquals(Paths.get("").resolve("folder/file5").toAbsolutePath().normalize(), f6.getSrcFile());
	}

	@Test
	public void testGetStartLine() {
		Fragment f1 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f2 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f3 = new Fragment(Paths.get("folder/file2"), 3, 30);
		Fragment f4 = new Fragment(Paths.get("folder/file3"), 4, 40);
		Fragment f5 = new Fragment(Paths.get("folder/file4"), 5, 50);
		Fragment f6 = new Fragment(Paths.get("folder/file5"), 6, 60);
		
		assertEquals(2, f1.getStartLine());
		assertEquals(2, f2.getStartLine());
		assertEquals(3, f3.getStartLine());
		assertEquals(4, f4.getStartLine());
		assertEquals(5, f5.getStartLine());
		assertEquals(6, f6.getStartLine());
	}

	@Test
	public void testGetEndLine() {
		Fragment f1 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f2 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f3 = new Fragment(Paths.get("folder/file2"), 3, 30);
		Fragment f4 = new Fragment(Paths.get("folder/file3"), 4, 40);
		Fragment f5 = new Fragment(Paths.get("folder/file4"), 5, 50);
		Fragment f6 = new Fragment(Paths.get("folder/file5"), 6, 60);
		
		assertEquals(10, f1.getEndLine());
		assertEquals(10, f2.getEndLine());
		assertEquals(30, f3.getEndLine());
		assertEquals(40, f4.getEndLine());
		assertEquals(50, f5.getEndLine());
		assertEquals(60, f6.getEndLine());
	}

	@Test
	public void testGetNumberOfLines() {
		Fragment f1 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f2 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f3 = new Fragment(Paths.get("folder/file2"), 3, 30);
		Fragment f4 = new Fragment(Paths.get("folder/file3"), 4, 40);
		Fragment f5 = new Fragment(Paths.get("folder/file4"), 5, 50);
		Fragment f6 = new Fragment(Paths.get("folder/file5"), 6, 60);
		
		assertEquals(9, f1.getNumberOfLines());
		assertEquals(9, f2.getNumberOfLines());
		assertEquals(28, f3.getNumberOfLines());
		assertEquals(37, f4.getNumberOfLines());
		assertEquals(46, f5.getNumberOfLines());
		assertEquals(55, f6.getNumberOfLines());
	}

	@Test
	public void testSubsumes() {
		Fragment f = new Fragment(Paths.get("folder/file1"), 5, 15);
		
		Fragment f11 = new Fragment(Paths.get("folder/file1"), 4, 12);
		Fragment f12 = new Fragment(Paths.get("folder/file1"), 4, 13);
		Fragment f13 = new Fragment(Paths.get("folder/file1"), 4, 14);
		Fragment f14 = new Fragment(Paths.get("folder/file1"), 4, 15);
		Fragment f15 = new Fragment(Paths.get("folder/file1"), 4, 16);
		Fragment f21 = new Fragment(Paths.get("folder/file1"), 5, 12);
		Fragment f22 = new Fragment(Paths.get("folder/file1"), 5, 13);
		Fragment f23 = new Fragment(Paths.get("folder/file1"), 5, 14);
		Fragment f24 = new Fragment(Paths.get("folder/file1"), 5, 15);
		Fragment f25 = new Fragment(Paths.get("folder/file1"), 5, 16);
		Fragment f31 = new Fragment(Paths.get("folder/file1"), 6, 12);
		Fragment f32 = new Fragment(Paths.get("folder/file1"), 6, 13);
		Fragment f33 = new Fragment(Paths.get("folder/file1"), 6, 14);
		Fragment f34 = new Fragment(Paths.get("folder/file1"), 6, 15);
		Fragment f35 = new Fragment(Paths.get("folder/file1"), 6, 16);
		Fragment f41 = new Fragment(Paths.get("folder/file1"), 7, 12);
		Fragment f42 = new Fragment(Paths.get("folder/file1"), 7, 13);
		Fragment f43 = new Fragment(Paths.get("folder/file1"), 7, 14);
		Fragment f44 = new Fragment(Paths.get("folder/file1"), 7, 15);
		Fragment f45 = new Fragment(Paths.get("folder/file1"), 7, 16);
		Fragment f51 = new Fragment(Paths.get("folder/file1"), 8, 12);
		Fragment f52 = new Fragment(Paths.get("folder/file1"), 8, 13);
		Fragment f53 = new Fragment(Paths.get("folder/file1"), 8, 14);
		Fragment f54 = new Fragment(Paths.get("folder/file1"), 8, 15);
		Fragment f55 = new Fragment(Paths.get("folder/file1"), 8, 16);
		Fragment fsa = new Fragment(Paths.get("folder/file1"), 3, 7);
		Fragment fsb = new Fragment(Paths.get("folder/file1"), 17, 20);
		Fragment fsc = new Fragment(Paths.get("folder/file2"), 5, 15);

		double tolerance = .20;
		
		assertFalse(f11.subsumes(f, tolerance));
		assertTrue(f12.subsumes(f, tolerance));
		assertTrue(f13.subsumes(f, tolerance));
		assertTrue(f14.subsumes(f, tolerance));
		assertTrue(f15.subsumes(f, tolerance));
		assertFalse(f21.subsumes(f, tolerance));
		assertTrue(f22.subsumes(f, tolerance));
		assertTrue(f23.subsumes(f, tolerance));
		assertTrue(f24.subsumes(f, tolerance));
		assertTrue(f25.subsumes(f, tolerance));
		assertFalse(f31.subsumes(f, tolerance));
		assertTrue(f32.subsumes(f, tolerance));
		assertTrue(f33.subsumes(f, tolerance));
		assertTrue(f34.subsumes(f, tolerance));
		assertTrue(f35.subsumes(f, tolerance));
		assertFalse(f41.subsumes(f, tolerance));
		assertTrue(f42.subsumes(f, tolerance));
		assertTrue(f43.subsumes(f, tolerance));
		assertTrue(f44.subsumes(f, tolerance));
		assertTrue(f45.subsumes(f, tolerance));
		assertFalse(f51.subsumes(f, tolerance));
		assertFalse(f52.subsumes(f, tolerance));
		assertFalse(f53.subsumes(f, tolerance));
		assertFalse(f54.subsumes(f, tolerance));
		assertFalse(f55.subsumes(f, tolerance));
		assertFalse(fsa.subsumes(f, tolerance));
		assertFalse(fsb.subsumes(f, tolerance));
		assertFalse(fsc.subsumes(f, tolerance));
		
	}

	@Test
	public void testEqualsObject() {
		Fragment f1 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f2 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f3 = new Fragment(Paths.get("folder/file2"), 3, 30);
		Fragment f4 = new Fragment(Paths.get("folder/file3"), 4, 40);
		Fragment f5 = new Fragment(Paths.get("folder/file4"), 5, 50);
		Fragment f6 = new Fragment(Paths.get("folder/file5"), 6, 60);
		
		assertTrue(f1.equals(f1));
		assertTrue(f1.equals(f2));
		assertTrue(!f1.equals(f3));
		assertTrue(!f1.equals(f4));
		assertTrue(!f1.equals(f5));
		assertTrue(!f1.equals(f6));
	}

	@Test
	public void testToString() {
		Fragment f1 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f2 = new Fragment(Paths.get("folder/file1"), 2, 10);
		Fragment f3 = new Fragment(Paths.get("folder/file2"), 3, 30);
		Fragment f4 = new Fragment(Paths.get("folder/file3"), 4, 40);
		Fragment f5 = new Fragment(Paths.get("folder/file4"), 5, 50);
		Fragment f6 = new Fragment(Paths.get("folder/file5"), 6, 60);
		
		assertEquals(Paths.get("folder/file1").toAbsolutePath().normalize().toString() + " 2-10", f1.toString());
		assertEquals(Paths.get("folder/file1").toAbsolutePath().normalize().toString() + " 2-10", f2.toString());
		assertEquals(Paths.get("folder/file2").toAbsolutePath().normalize().toString() + " 3-30", f3.toString());
		assertEquals(Paths.get("folder/file3").toAbsolutePath().normalize().toString() + " 4-40", f4.toString());
		assertEquals(Paths.get("folder/file4").toAbsolutePath().normalize().toString() + " 5-50", f5.toString());
		assertEquals(Paths.get("folder/file5").toAbsolutePath().normalize().toString() + " 6-60", f6.toString());
	}
}
