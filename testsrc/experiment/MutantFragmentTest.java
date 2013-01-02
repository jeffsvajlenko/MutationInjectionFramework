package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import experiment.MutantFragment;

public class MutantFragmentTest {

	@Test
	public void testMutantFragment() {
		MutantFragment mf1 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf2 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf3 = new MutantFragment(2, 5, Paths.get("/blah2"), 12);
		MutantFragment mf4 = new MutantFragment(3, 4, Paths.get("/blah3"), 13);
		MutantFragment mf5 = new MutantFragment(4, 3, Paths.get("/blah4"), 14);
		MutantFragment mf6 = new MutantFragment(5, 2, Paths.get("/blah5"), 15);
		MutantFragment mf7 = new MutantFragment(6, 1, Paths.get("/blah6"), 16);
		
		//Check ids
		assertEquals(1, mf1.getId());
		assertEquals(1, mf2.getId());
		assertEquals(2, mf3.getId());
		assertEquals(3, mf4.getId());
		assertEquals(4, mf5.getId());
		assertEquals(5, mf6.getId());
		assertEquals(6, mf7.getId());
		
		//Check fragment id
		assertEquals(6, mf1.getFragmentId());
		assertEquals(6, mf2.getFragmentId());
		assertEquals(5, mf3.getFragmentId());
		assertEquals(4, mf4.getFragmentId());
		assertEquals(3, mf5.getFragmentId());
		assertEquals(2, mf6.getFragmentId());
		assertEquals(1, mf7.getFragmentId());
		
		//Check srcfile
		assertEquals(Paths.get("/blah1"), mf1.getFragmentFile());
		assertEquals(Paths.get("/blah1"), mf2.getFragmentFile());
		assertEquals(Paths.get("/blah2"), mf3.getFragmentFile());
		assertEquals(Paths.get("/blah3"), mf4.getFragmentFile());
		assertEquals(Paths.get("/blah4"), mf5.getFragmentFile());
		assertEquals(Paths.get("/blah5"), mf6.getFragmentFile());
		assertEquals(Paths.get("/blah6"), mf7.getFragmentFile());
		
		//check mutator id
		assertEquals(11, mf1.getMutatorId());
		assertEquals(11, mf2.getMutatorId());
		assertEquals(12, mf3.getMutatorId());
		assertEquals(13, mf4.getMutatorId());
		assertEquals(14, mf5.getMutatorId());
		assertEquals(15, mf6.getMutatorId());
		assertEquals(16, mf7.getMutatorId());
	}

	@Test
	public void testGetId() {
		MutantFragment mf1 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf2 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf3 = new MutantFragment(2, 5, Paths.get("/blah2"), 12);
		MutantFragment mf4 = new MutantFragment(3, 4, Paths.get("/blah3"), 13);
		MutantFragment mf5 = new MutantFragment(4, 3, Paths.get("/blah4"), 14);
		MutantFragment mf6 = new MutantFragment(5, 2, Paths.get("/blah5"), 15);
		MutantFragment mf7 = new MutantFragment(6, 1, Paths.get("/blah6"), 16);
		
		assertEquals(1, mf1.getId());
		assertEquals(1, mf2.getId());
		assertEquals(2, mf3.getId());
		assertEquals(3, mf4.getId());
		assertEquals(4, mf5.getId());
		assertEquals(5, mf6.getId());
		assertEquals(6, mf7.getId());
	}

	@Test
	public void testGetFragmentId() {
		MutantFragment mf1 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf2 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf3 = new MutantFragment(2, 5, Paths.get("/blah2"), 12);
		MutantFragment mf4 = new MutantFragment(3, 4, Paths.get("/blah3"), 13);
		MutantFragment mf5 = new MutantFragment(4, 3, Paths.get("/blah4"), 14);
		MutantFragment mf6 = new MutantFragment(5, 2, Paths.get("/blah5"), 15);
		MutantFragment mf7 = new MutantFragment(6, 1, Paths.get("/blah6"), 16);
		
		assertEquals(6, mf1.getFragmentId());
		assertEquals(6, mf2.getFragmentId());
		assertEquals(5, mf3.getFragmentId());
		assertEquals(4, mf4.getFragmentId());
		assertEquals(3, mf5.getFragmentId());
		assertEquals(2, mf6.getFragmentId());
		assertEquals(1, mf7.getFragmentId());
	}

	@Test
	public void testGetFragmentFile() {
		MutantFragment mf1 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf2 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf3 = new MutantFragment(2, 5, Paths.get("/blah2"), 12);
		MutantFragment mf4 = new MutantFragment(3, 4, Paths.get("/blah3"), 13);
		MutantFragment mf5 = new MutantFragment(4, 3, Paths.get("/blah4"), 14);
		MutantFragment mf6 = new MutantFragment(5, 2, Paths.get("/blah5"), 15);
		MutantFragment mf7 = new MutantFragment(6, 1, Paths.get("/blah6"), 16);
		
		assertEquals(Paths.get("/blah1"), mf1.getFragmentFile());
		assertEquals(Paths.get("/blah1"), mf2.getFragmentFile());
		assertEquals(Paths.get("/blah2"), mf3.getFragmentFile());
		assertEquals(Paths.get("/blah3"), mf4.getFragmentFile());
		assertEquals(Paths.get("/blah4"), mf5.getFragmentFile());
		assertEquals(Paths.get("/blah5"), mf6.getFragmentFile());
		assertEquals(Paths.get("/blah6"), mf7.getFragmentFile());
	}

	@Test
	public void testGetMutatorId() {
		MutantFragment mf1 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf2 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf3 = new MutantFragment(2, 5, Paths.get("/blah2"), 12);
		MutantFragment mf4 = new MutantFragment(3, 4, Paths.get("/blah3"), 13);
		MutantFragment mf5 = new MutantFragment(4, 3, Paths.get("/blah4"), 14);
		MutantFragment mf6 = new MutantFragment(5, 2, Paths.get("/blah5"), 15);
		MutantFragment mf7 = new MutantFragment(6, 1, Paths.get("/blah6"), 16);
		
		assertEquals(11, mf1.getMutatorId());
		assertEquals(11, mf2.getMutatorId());
		assertEquals(12, mf3.getMutatorId());
		assertEquals(13, mf4.getMutatorId());
		assertEquals(14, mf5.getMutatorId());
		assertEquals(15, mf6.getMutatorId());
		assertEquals(16, mf7.getMutatorId());
	}

	@Test
	public void testEqualsObject() {
		MutantFragment mf1 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf2 = new MutantFragment(1, 6, Paths.get("/blah1"), 11);
		MutantFragment mf3 = new MutantFragment(2, 5, Paths.get("/blah2"), 12);
		MutantFragment mf4 = new MutantFragment(3, 4, Paths.get("/blah3"), 13);
		MutantFragment mf5 = new MutantFragment(4, 3, Paths.get("/blah4"), 14);
		MutantFragment mf6 = new MutantFragment(5, 2, Paths.get("/blah5"), 15);
		MutantFragment mf7 = new MutantFragment(6, 1, Paths.get("/blah6"), 16);
		
		assertTrue(mf1.equals(mf1));
		assertTrue(mf1.equals(mf2));
		assertTrue(mf2.equals(mf1));
		assertTrue(!mf1.equals(mf3));
		assertTrue(!mf1.equals(mf4));
		assertTrue(!mf1.equals(mf5));
		assertTrue(!mf1.equals(mf6));
		assertTrue(!mf1.equals(mf7));
		assertTrue(!mf1.equals(null));
	}

}
