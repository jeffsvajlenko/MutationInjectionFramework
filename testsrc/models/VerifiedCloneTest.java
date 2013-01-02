package models;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

public class VerifiedCloneTest {

	@Test
	public void testVerifiedClone() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/src12"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/src13"), 3, 30), new Fragment(Paths.get("/src23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/src14"), 4, 40), new Fragment(Paths.get("/src24"), 40, 400), false, false);
		
		assertEquals(new Fragment(Paths.get("/src11"), 1, 10), vc1.getFragment1());
		assertEquals(new Fragment(Paths.get("/src12"), 2, 20), vc2.getFragment1());
		assertEquals(new Fragment(Paths.get("/src13"), 3, 30), vc3.getFragment1());
		assertEquals(new Fragment(Paths.get("/src14"), 4, 40), vc4.getFragment1());
		
		assertEquals(new Fragment(Paths.get("/src21"), 10, 100), vc1.getFragment2());
		assertEquals(new Fragment(Paths.get("/src22"), 20, 200), vc2.getFragment2());
		assertEquals(new Fragment(Paths.get("/src23"), 30, 300), vc3.getFragment2());
		assertEquals(new Fragment(Paths.get("/src24"), 40, 400), vc4.getFragment2());
		
		assertEquals(true, vc1.isClone());
		assertEquals(false, vc2.isClone());
		assertEquals(true, vc3.isClone());
		assertEquals(false, vc4.isClone());
		
		assertEquals(true, vc1.isVerifierSuccess());
		assertEquals(true, vc2.isVerifierSuccess());
		assertEquals(false, vc3.isVerifierSuccess());
		assertEquals(false, vc4.isVerifierSuccess());
	}

	@Test
	public void testIsClone() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/src12"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/src13"), 3, 30), new Fragment(Paths.get("/src23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/src14"), 4, 40), new Fragment(Paths.get("/src24"), 40, 400), false, false);
		
		assertEquals(true, vc1.isClone());
		assertEquals(false, vc2.isClone());
		assertEquals(true, vc3.isClone());
		assertEquals(false, vc4.isClone());
	}

	@Test
	public void testIsVerifierSuccess() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/src12"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/src13"), 3, 30), new Fragment(Paths.get("/src23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/src14"), 4, 40), new Fragment(Paths.get("/src24"), 40, 400), false, false);
		
		assertEquals(true, vc1.isVerifierSuccess());
		assertEquals(true, vc2.isVerifierSuccess());
		assertEquals(false, vc3.isVerifierSuccess());
		assertEquals(false, vc4.isVerifierSuccess());
	}
	
	@Test
	public void testEquals() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/src12"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/src13"), 3, 30), new Fragment(Paths.get("/src23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/src14"), 4, 40), new Fragment(Paths.get("/src24"), 40, 400), false, false);
		VerifiedClone vc5 = new VerifiedClone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src21"), 10, 100), true, true);
		
		assertTrue(vc1.equals(vc1));
		assertTrue(vc1.equals(vc5));
		assertTrue(vc5.equals(vc1));
		assertFalse(vc1.equals(null));
		assertFalse(vc1.equals(vc2));
		assertFalse(vc1.equals(vc3));
		assertFalse(vc1.equals(vc4));
		
	}

}
