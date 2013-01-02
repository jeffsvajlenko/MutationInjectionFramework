package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import models.Clone;
import models.Fragment;

import org.junit.Test;

import experiment.UnitRecall;

public class UnitRecallTest {

	@Test
	public void testUnitRecall() {
		//Regular Case
			Clone c1 = new Clone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src12"), 10, 100));
		UnitRecall ur1 = new UnitRecall(1, 10, 0.1, c1);
			Clone c2 = new Clone(new Fragment(Paths.get("/src21"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200));
		UnitRecall ur2 = new UnitRecall(2, 20, 0.2, c2);
			Clone c3 = new Clone(new Fragment(Paths.get("/src31"), 3, 30), new Fragment(Paths.get("/src32"), 30, 300));
		UnitRecall ur3 = new UnitRecall(3, 30, 0.3, c3);
			Clone c4 = new Clone(new Fragment(Paths.get("/src41"), 4, 40), new Fragment(Paths.get("/src42"), 40, 400));
		UnitRecall ur4 = new UnitRecall(4, 40, 0.4, c4);
		//Special Cases
			Clone c5 = null;
		UnitRecall ur5 = new UnitRecall(5, 50, 0.0, c5);
			Clone c6 = new Clone(new Fragment(Paths.get("/src61"), 6, 60), new Fragment(Paths.get("/src62"), 60, 600));
		UnitRecall ur6 = new UnitRecall(6, 60, 0.0, c6);
			Clone c7 = new Clone(new Fragment(Paths.get("/src71"), 7, 70), new Fragment(Paths.get("/src62"), 70, 700));
		UnitRecall ur7 = new UnitRecall(7, 70, 1.0, c7);
		
		//getToolid
		assertEquals(1, ur1.getToolid());
		assertEquals(2, ur2.getToolid());
		assertEquals(3, ur3.getToolid());
		assertEquals(4, ur4.getToolid());
		assertEquals(5, ur5.getToolid());
		assertEquals(6, ur6.getToolid());
		assertEquals(7, ur7.getToolid());
		
		//getBaseid
		assertEquals(10, ur1.getBaseid());
		assertEquals(20, ur2.getBaseid());
		assertEquals(30, ur3.getBaseid());
		assertEquals(40, ur4.getBaseid());
		assertEquals(50, ur5.getBaseid());
		assertEquals(60, ur6.getBaseid());
		assertEquals(70, ur7.getBaseid());
		
		//getRecall
		assertEquals(0.1, ur1.getRecall(), 0.0001);
		assertEquals(0.2, ur2.getRecall(), 0.0001);
		assertEquals(0.3, ur3.getRecall(), 0.0001);
		assertEquals(0.4, ur4.getRecall(), 0.0001);
		assertEquals(0.0, ur5.getRecall(), 0.0001);
		assertEquals(0.0, ur6.getRecall(), 0.0001);
		assertEquals(1.0, ur7.getRecall(), 0.0001);
		
		//getClone
		assertEquals(c1, ur1.getClone());
		assertEquals(c2, ur2.getClone());
		assertEquals(c3, ur3.getClone());
		assertEquals(c4, ur4.getClone());
		assertEquals(c5, ur5.getClone());
		assertEquals(c6, ur6.getClone());
		assertEquals(c7, ur7.getClone());
		
		//Errors
		boolean caught;
		
		caught = false;
		try {
			new UnitRecall(0, 0, -0.1, c1);
		} catch(IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new UnitRecall(0, 0, 1.1, c1);
		} catch(IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new UnitRecall(0, 0, 0.5, null);
		} catch(NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new UnitRecall(0, 0, 1.0, null);
		} catch(NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new UnitRecall(0, 0, 0.75, null);
		} catch(NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new UnitRecall(0, 0, 0.0, null);
		} catch(NullPointerException e) {
			caught = true;
		}
		assertFalse(caught);
	}

	@Test
	public void testGetToolid() {
		//Regular Case
			Clone c1 = new Clone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src12"), 10, 100));
		UnitRecall ur1 = new UnitRecall(1, 10, 0.1, c1);
			Clone c2 = new Clone(new Fragment(Paths.get("/src21"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200));
		UnitRecall ur2 = new UnitRecall(2, 20, 0.2, c2);
			Clone c3 = new Clone(new Fragment(Paths.get("/src31"), 3, 30), new Fragment(Paths.get("/src32"), 30, 300));
		UnitRecall ur3 = new UnitRecall(3, 30, 0.3, c3);
			Clone c4 = new Clone(new Fragment(Paths.get("/src41"), 4, 40), new Fragment(Paths.get("/src42"), 40, 400));
		UnitRecall ur4 = new UnitRecall(4, 40, 0.4, c4);
		//Special Cases
			Clone c5 = null;
		UnitRecall ur5 = new UnitRecall(5, 50, 0.0, c5);
			Clone c6 = new Clone(new Fragment(Paths.get("/src61"), 6, 60), new Fragment(Paths.get("/src62"), 60, 600));
		UnitRecall ur6 = new UnitRecall(6, 60, 0.0, c6);
			Clone c7 = new Clone(new Fragment(Paths.get("/src71"), 7, 70), new Fragment(Paths.get("/src62"), 70, 700));
		UnitRecall ur7 = new UnitRecall(7, 70, 1.0, c7);
		
		assertEquals(1, ur1.getToolid());
		assertEquals(2, ur2.getToolid());
		assertEquals(3, ur3.getToolid());
		assertEquals(4, ur4.getToolid());
		assertEquals(5, ur5.getToolid());
		assertEquals(6, ur6.getToolid());
		assertEquals(7, ur7.getToolid());
	}

	@Test
	public void testGetBaseid() {
		//Regular Case
			Clone c1 = new Clone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src12"), 10, 100));
		UnitRecall ur1 = new UnitRecall(1, 10, 0.1, c1);
			Clone c2 = new Clone(new Fragment(Paths.get("/src21"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200));
		UnitRecall ur2 = new UnitRecall(2, 20, 0.2, c2);
			Clone c3 = new Clone(new Fragment(Paths.get("/src31"), 3, 30), new Fragment(Paths.get("/src32"), 30, 300));
		UnitRecall ur3 = new UnitRecall(3, 30, 0.3, c3);
			Clone c4 = new Clone(new Fragment(Paths.get("/src41"), 4, 40), new Fragment(Paths.get("/src42"), 40, 400));
		UnitRecall ur4 = new UnitRecall(4, 40, 0.4, c4);
		//Special Cases
			Clone c5 = null;
		UnitRecall ur5 = new UnitRecall(5, 50, 0.0, c5);
			Clone c6 = new Clone(new Fragment(Paths.get("/src61"), 6, 60), new Fragment(Paths.get("/src62"), 60, 600));
		UnitRecall ur6 = new UnitRecall(6, 60, 0.0, c6);
			Clone c7 = new Clone(new Fragment(Paths.get("/src71"), 7, 70), new Fragment(Paths.get("/src62"), 70, 700));
		UnitRecall ur7 = new UnitRecall(7, 70, 1.0, c7);
		
		assertEquals(10, ur1.getBaseid());
		assertEquals(20, ur2.getBaseid());
		assertEquals(30, ur3.getBaseid());
		assertEquals(40, ur4.getBaseid());
		assertEquals(50, ur5.getBaseid());
		assertEquals(60, ur6.getBaseid());
		assertEquals(70, ur7.getBaseid());
	}

	@Test
	public void testGetRecall() {
		//Regular Case
			Clone c1 = new Clone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src12"), 10, 100));
		UnitRecall ur1 = new UnitRecall(1, 10, 0.1, c1);
			Clone c2 = new Clone(new Fragment(Paths.get("/src21"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200));
		UnitRecall ur2 = new UnitRecall(2, 20, 0.2, c2);
			Clone c3 = new Clone(new Fragment(Paths.get("/src31"), 3, 30), new Fragment(Paths.get("/src32"), 30, 300));
		UnitRecall ur3 = new UnitRecall(3, 30, 0.3, c3);
			Clone c4 = new Clone(new Fragment(Paths.get("/src41"), 4, 40), new Fragment(Paths.get("/src42"), 40, 400));
		UnitRecall ur4 = new UnitRecall(4, 40, 0.4, c4);
		//Special Cases
			Clone c5 = null;
		UnitRecall ur5 = new UnitRecall(5, 50, 0.0, c5);
			Clone c6 = new Clone(new Fragment(Paths.get("/src61"), 6, 60), new Fragment(Paths.get("/src62"), 60, 600));
		UnitRecall ur6 = new UnitRecall(6, 60, 0.0, c6);
			Clone c7 = new Clone(new Fragment(Paths.get("/src71"), 7, 70), new Fragment(Paths.get("/src62"), 70, 700));
		UnitRecall ur7 = new UnitRecall(7, 70, 1.0, c7);
		
		assertEquals(0.1, ur1.getRecall(), 0.0001);
		assertEquals(0.2, ur2.getRecall(), 0.0001);
		assertEquals(0.3, ur3.getRecall(), 0.0001);
		assertEquals(0.4, ur4.getRecall(), 0.0001);
		assertEquals(0.0, ur5.getRecall(), 0.0001);
		assertEquals(0.0, ur6.getRecall(), 0.0001);
		assertEquals(1.0, ur7.getRecall(), 0.0001);
	}

	@Test
	public void testGetClone() {
		//Regular Case
			Clone c1 = new Clone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src12"), 10, 100));
		UnitRecall ur1 = new UnitRecall(1, 10, 0.1, c1);
			Clone c2 = new Clone(new Fragment(Paths.get("/src21"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200));
		UnitRecall ur2 = new UnitRecall(2, 20, 0.2, c2);
			Clone c3 = new Clone(new Fragment(Paths.get("/src31"), 3, 30), new Fragment(Paths.get("/src32"), 30, 300));
		UnitRecall ur3 = new UnitRecall(3, 30, 0.3, c3);
			Clone c4 = new Clone(new Fragment(Paths.get("/src41"), 4, 40), new Fragment(Paths.get("/src42"), 40, 400));
		UnitRecall ur4 = new UnitRecall(4, 40, 0.4, c4);
		//Special Cases
			Clone c5 = null;
		UnitRecall ur5 = new UnitRecall(5, 50, 0.0, c5);
			Clone c6 = new Clone(new Fragment(Paths.get("/src61"), 6, 60), new Fragment(Paths.get("/src62"), 60, 600));
		UnitRecall ur6 = new UnitRecall(6, 60, 0.0, c6);
			Clone c7 = new Clone(new Fragment(Paths.get("/src71"), 7, 70), new Fragment(Paths.get("/src62"), 70, 700));
		UnitRecall ur7 = new UnitRecall(7, 70, 1.0, c7);
		
		assertEquals(c1, ur1.getClone());
		assertEquals(c2, ur2.getClone());
		assertEquals(c3, ur3.getClone());
		assertEquals(c4, ur4.getClone());
		assertEquals(c5, ur5.getClone());
		assertEquals(c6, ur6.getClone());
		assertEquals(c7, ur7.getClone());
	}

	@Test
	public void testEqualsObject() {
			Clone c1 = new Clone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src12"), 10, 100));
		UnitRecall ur1 = new UnitRecall(1, 10, 0.1, c1);
			Clone c2 = new Clone(new Fragment(Paths.get("/src21"), 2, 20), new Fragment(Paths.get("/src22"), 20, 200));
		UnitRecall ur2 = new UnitRecall(2, 20, 0.2, c2);
			Clone c3 = new Clone(new Fragment(Paths.get("/src31"), 3, 30), new Fragment(Paths.get("/src32"), 30, 300));
		UnitRecall ur3 = new UnitRecall(3, 30, 0.3, c3);
			Clone c4 = new Clone(new Fragment(Paths.get("/src41"), 4, 40), new Fragment(Paths.get("/src42"), 40, 400));
		UnitRecall ur4 = new UnitRecall(4, 40, 0.4, c4);
		//Special Cases
			Clone c5 = null;
		UnitRecall ur5 = new UnitRecall(5, 50, 0.0, c5);
			Clone c6 = new Clone(new Fragment(Paths.get("/src61"), 6, 60), new Fragment(Paths.get("/src62"), 60, 600));
		UnitRecall ur6 = new UnitRecall(6, 60, 0.0, c6);
			Clone c7 = new Clone(new Fragment(Paths.get("/src71"), 7, 70), new Fragment(Paths.get("/src62"), 70, 700));
		UnitRecall ur7 = new UnitRecall(7, 70, 1.0, c7);
			Clone c1c = new Clone(new Fragment(Paths.get("/src11"), 1, 10), new Fragment(Paths.get("/src12"), 10, 100));
		UnitRecall ur1c = new UnitRecall(1, 10, 0.1, c1c);
			Clone c5c = null;
		UnitRecall ur5c = new UnitRecall(5, 50, 0.0, c5c);
		
		
		assertTrue(ur1.equals(ur1));
		assertTrue(ur1.equals(ur1c));
		assertTrue(ur1c.equals(ur1));
		assertFalse(ur1.equals(null));
		assertFalse(ur1.equals(ur2));
		assertFalse(ur2.equals(ur1));
		assertFalse(ur1.equals(ur3));
		assertFalse(ur3.equals(ur1));
		assertFalse(ur1.equals(ur4));
		assertFalse(ur4.equals(ur1));
		assertFalse(ur1.equals(ur5));
		assertFalse(ur5.equals(ur1));
		assertFalse(ur1.equals(ur6));
		assertFalse(ur6.equals(ur1));
		assertFalse(ur1.equals(ur7));
		assertFalse(ur7.equals(ur1));
		
		
		assertTrue(ur5.equals(ur5));
		assertTrue(ur5c.equals(ur5c));
		assertTrue(ur5.equals(ur5c));
		assertTrue(ur5c.equals(ur5));
		assertFalse(ur5.equals(ur1));
		assertFalse(ur5.equals(ur2));
		assertFalse(ur5.equals(ur3));
		assertFalse(ur5.equals(ur4));
		assertFalse(ur5.equals(ur6));
		assertFalse(ur5.equals(ur7));
		assertFalse(ur1.equals(ur5));
		assertFalse(ur2.equals(ur5));
		assertFalse(ur3.equals(ur5));
		assertFalse(ur4.equals(ur5));
		assertFalse(ur6.equals(ur5));
		assertFalse(ur7.equals(ur5));
	}
}
