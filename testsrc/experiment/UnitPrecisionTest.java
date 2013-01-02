package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import models.Fragment;
import models.VerifiedClone;

import org.junit.Test;

import experiment.UnitPrecision;

public class UnitPrecisionTest {

	@Test
	public void testUnitPrecision() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/srcfile11"), 1, 10), new Fragment(Paths.get("/srcfile21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/srcfile12"), 2, 20), new Fragment(Paths.get("/srcfile22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/srcfile13"), 3, 30), new Fragment(Paths.get("/srcfile23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/srcfile14"), 4, 40), new Fragment(Paths.get("/srcfile24"), 40, 400), false, false);
		VerifiedClone vc5 = new VerifiedClone(new Fragment(Paths.get("/srcfile15"), 5, 50), new Fragment(Paths.get("/srcfile25"), 50, 500), true, true);
		VerifiedClone vc6 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 6, 60), new Fragment(Paths.get("/srcfile26"), 60, 600), false, true);
		VerifiedClone vc7 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 7, 70), new Fragment(Paths.get("/srcfile26"), 70, 700), false, true);
		
		List<VerifiedClone> list = new LinkedList<VerifiedClone>();
		
		list.clear();
		list.add(vc1);
		UnitPrecision up1 = new UnitPrecision(1, 10, 0.1, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		UnitPrecision up2 = new UnitPrecision(2, 20, 0.2, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		UnitPrecision up3 = new UnitPrecision(3, 30, 0.3, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		UnitPrecision up4 = new UnitPrecision(4, 40, 0.4, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		UnitPrecision up5 = new UnitPrecision(5, 50, 0.5, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		UnitPrecision up6 = new UnitPrecision(6, 60, 0.6, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		list.add(vc7);
		UnitPrecision up7 = new UnitPrecision(7, 70, 0.0, list);
		
		UnitPrecision up8 = new UnitPrecision(8, 80, 1.0, list); // 0.0 but non-empty list
		
		list.clear();
		UnitPrecision up9 = new UnitPrecision(9, 90, 1.0, list); // 0.0 and empty list
		
		//getToolid
		assertEquals(1, up1.getToolid());
		assertEquals(2, up2.getToolid());
		assertEquals(3, up3.getToolid());
		assertEquals(4, up4.getToolid());
		assertEquals(5, up5.getToolid());
		assertEquals(6, up6.getToolid());
		assertEquals(7, up7.getToolid());
		assertEquals(8, up8.getToolid());
		assertEquals(9, up9.getToolid());
		
		//getBaseid
		assertEquals(10, up1.getBaseid());
		assertEquals(20, up2.getBaseid());
		assertEquals(30, up3.getBaseid());
		assertEquals(40, up4.getBaseid());
		assertEquals(50, up5.getBaseid());
		assertEquals(60, up6.getBaseid());
		assertEquals(70, up7.getBaseid());
		assertEquals(80, up8.getBaseid());
		assertEquals(90, up9.getBaseid());
		
		
		//getPrecision
		assertEquals(0.1, up1.getPrecision(), 0.0001);
		assertEquals(0.2, up2.getPrecision(), 0.0001);
		assertEquals(0.3, up3.getPrecision(), 0.0001);
		assertEquals(0.4, up4.getPrecision(), 0.0001);
		assertEquals(0.5, up5.getPrecision(), 0.0001);
		assertEquals(0.6, up6.getPrecision(), 0.0001);
		assertEquals(0.0, up7.getPrecision(), 0.0001);
		assertEquals(1.0, up8.getPrecision(), 0.0001);
		assertEquals(1.0, up9.getPrecision(), 0.0001);
		
		//getClones
		list = up1.getClones();
		assertTrue(up1.getClones().size() == 1);
		assertTrue(up1.getClones().get(0).equals(vc1));
		
		list = up2.getClones();
		assertTrue(up2.getClones().size() == 2);
		assertTrue(up2.getClones().get(0).equals(vc1));
		assertTrue(up2.getClones().get(1).equals(vc2));
		
		list = up3.getClones();
		assertTrue(up3.getClones().size() == 3);
		assertTrue(up3.getClones().get(0).equals(vc1));
		assertTrue(up3.getClones().get(1).equals(vc2));
		assertTrue(up3.getClones().get(2).equals(vc3));
		
		list = up4.getClones();
		assertTrue(up4.getClones().size() == 4);
		assertTrue(up4.getClones().get(0).equals(vc1));
		assertTrue(up4.getClones().get(1).equals(vc2));
		assertTrue(up4.getClones().get(2).equals(vc3));
		assertTrue(up4.getClones().get(3).equals(vc4));
		
		list = up5.getClones();
		assertTrue(up5.getClones().size() == 5);
		assertTrue(up5.getClones().get(0).equals(vc1));
		assertTrue(up5.getClones().get(1).equals(vc2));
		assertTrue(up5.getClones().get(2).equals(vc3));
		assertTrue(up5.getClones().get(3).equals(vc4));
		assertTrue(up5.getClones().get(4).equals(vc5));
		
		list = up6.getClones();
		assertTrue(up6.getClones().size() == 6);
		assertTrue(up6.getClones().get(0).equals(vc1));
		assertTrue(up6.getClones().get(1).equals(vc2));
		assertTrue(up6.getClones().get(2).equals(vc3));
		assertTrue(up6.getClones().get(3).equals(vc4));
		assertTrue(up6.getClones().get(4).equals(vc5));
		assertTrue(up6.getClones().get(5).equals(vc6));
		
		list = up7.getClones();
		assertTrue(up7.getClones().size() == 7);
		assertTrue(up7.getClones().get(0).equals(vc1));
		assertTrue(up7.getClones().get(1).equals(vc2));
		assertTrue(up7.getClones().get(2).equals(vc3));
		assertTrue(up7.getClones().get(3).equals(vc4));
		assertTrue(up7.getClones().get(4).equals(vc5));
		assertTrue(up7.getClones().get(5).equals(vc6));
		assertTrue(up7.getClones().get(6).equals(vc7));
		
		list = up8.getClones();
		assertTrue(up8.getClones().size() == 7);
		assertTrue(up8.getClones().get(0).equals(vc1));
		assertTrue(up8.getClones().get(1).equals(vc2));
		assertTrue(up8.getClones().get(2).equals(vc3));
		assertTrue(up8.getClones().get(3).equals(vc4));
		assertTrue(up8.getClones().get(4).equals(vc5));
		assertTrue(up8.getClones().get(5).equals(vc6));
		assertTrue(up8.getClones().get(6).equals(vc7));
		
		list = up9.getClones();
		assertTrue(up9.getClones().size() == 0);
		
		//Errors
		list = new LinkedList<VerifiedClone>();
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		list.add(vc7);
		boolean caught;
		
		caught = false;
		try {
			new UnitPrecision(5, 50, 0.5, null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new UnitPrecision(5, 50, -0.01, list);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			new UnitPrecision(5, 50, 1.01, list);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			list.clear();
			new UnitPrecision(5, 50, 0.10, list);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			list.clear();
			new UnitPrecision(5, 50, 0.50, list);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			list.clear();
			new UnitPrecision(5, 50, 0.5, list);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			list.clear();
			new UnitPrecision(5, 50, 1.0, list);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertFalse(caught);
	}

	@Test
	public void testGetToolid() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/srcfile11"), 1, 10), new Fragment(Paths.get("/srcfile21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/srcfile12"), 2, 20), new Fragment(Paths.get("/srcfile22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/srcfile13"), 3, 30), new Fragment(Paths.get("/srcfile23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/srcfile14"), 4, 40), new Fragment(Paths.get("/srcfile24"), 40, 400), false, false);
		VerifiedClone vc5 = new VerifiedClone(new Fragment(Paths.get("/srcfile15"), 5, 50), new Fragment(Paths.get("/srcfile25"), 50, 500), true, true);
		VerifiedClone vc6 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 6, 60), new Fragment(Paths.get("/srcfile26"), 60, 600), false, true);
		VerifiedClone vc7 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 7, 70), new Fragment(Paths.get("/srcfile26"), 70, 700), false, true);
		
		List<VerifiedClone> list = new LinkedList<VerifiedClone>();
		
		list.clear();
		list.add(vc1);
		UnitPrecision up1 = new UnitPrecision(1, 10, 0.1, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		UnitPrecision up2 = new UnitPrecision(2, 20, 0.2, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		UnitPrecision up3 = new UnitPrecision(3, 30, 0.3, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		UnitPrecision up4 = new UnitPrecision(4, 40, 0.4, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		UnitPrecision up5 = new UnitPrecision(5, 50, 0.5, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		UnitPrecision up6 = new UnitPrecision(6, 60, 0.6, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		list.add(vc7);
		UnitPrecision up7 = new UnitPrecision(7, 70, 0.0, list);
		
		UnitPrecision up8 = new UnitPrecision(8, 80, 1.0, list); // 0.0 but non-empty list
		
		list.clear();
		UnitPrecision up9 = new UnitPrecision(9, 90, 1.0, list); // 0.0 and empty list
		
		assertEquals(1, up1.getToolid());
		assertEquals(2, up2.getToolid());
		assertEquals(3, up3.getToolid());
		assertEquals(4, up4.getToolid());
		assertEquals(5, up5.getToolid());
		assertEquals(6, up6.getToolid());
		assertEquals(7, up7.getToolid());
		assertEquals(8, up8.getToolid());
		assertEquals(9, up9.getToolid());
	}

	@Test
	public void testGetBaseid() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/srcfile11"), 1, 10), new Fragment(Paths.get("/srcfile21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/srcfile12"), 2, 20), new Fragment(Paths.get("/srcfile22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/srcfile13"), 3, 30), new Fragment(Paths.get("/srcfile23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/srcfile14"), 4, 40), new Fragment(Paths.get("/srcfile24"), 40, 400), false, false);
		VerifiedClone vc5 = new VerifiedClone(new Fragment(Paths.get("/srcfile15"), 5, 50), new Fragment(Paths.get("/srcfile25"), 50, 500), true, true);
		VerifiedClone vc6 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 6, 60), new Fragment(Paths.get("/srcfile26"), 60, 600), false, true);
		VerifiedClone vc7 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 7, 70), new Fragment(Paths.get("/srcfile26"), 70, 700), false, true);
		
		List<VerifiedClone> list = new LinkedList<VerifiedClone>();
		
		list.clear();
		list.add(vc1);
		UnitPrecision up1 = new UnitPrecision(1, 10, 0.1, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		UnitPrecision up2 = new UnitPrecision(2, 20, 0.2, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		UnitPrecision up3 = new UnitPrecision(3, 30, 0.3, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		UnitPrecision up4 = new UnitPrecision(4, 40, 0.4, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		UnitPrecision up5 = new UnitPrecision(5, 50, 0.5, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		UnitPrecision up6 = new UnitPrecision(6, 60, 0.6, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		list.add(vc7);
		UnitPrecision up7 = new UnitPrecision(7, 70, 0.0, list);
		
		UnitPrecision up8 = new UnitPrecision(8, 80, 1.0, list); // 0.0 but non-empty list
		
		list.clear();
		UnitPrecision up9 = new UnitPrecision(9, 90, 1.0, list); // 0.0 and empty list
		
		assertEquals(10, up1.getBaseid());
		assertEquals(20, up2.getBaseid());
		assertEquals(30, up3.getBaseid());
		assertEquals(40, up4.getBaseid());
		assertEquals(50, up5.getBaseid());
		assertEquals(60, up6.getBaseid());
		assertEquals(70, up7.getBaseid());
		assertEquals(80, up8.getBaseid());
		assertEquals(90, up9.getBaseid());
	}

	@Test
	public void testGetPrecision() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/srcfile11"), 1, 10), new Fragment(Paths.get("/srcfile21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/srcfile12"), 2, 20), new Fragment(Paths.get("/srcfile22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/srcfile13"), 3, 30), new Fragment(Paths.get("/srcfile23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/srcfile14"), 4, 40), new Fragment(Paths.get("/srcfile24"), 40, 400), false, false);
		VerifiedClone vc5 = new VerifiedClone(new Fragment(Paths.get("/srcfile15"), 5, 50), new Fragment(Paths.get("/srcfile25"), 50, 500), true, true);
		VerifiedClone vc6 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 6, 60), new Fragment(Paths.get("/srcfile26"), 60, 600), false, true);
		VerifiedClone vc7 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 7, 70), new Fragment(Paths.get("/srcfile26"), 70, 700), false, true);
		
		List<VerifiedClone> list = new LinkedList<VerifiedClone>();
		
		list.clear();
		list.add(vc1);
		UnitPrecision up1 = new UnitPrecision(1, 10, 0.1, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		UnitPrecision up2 = new UnitPrecision(2, 20, 0.2, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		UnitPrecision up3 = new UnitPrecision(3, 30, 0.3, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		UnitPrecision up4 = new UnitPrecision(4, 40, 0.4, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		UnitPrecision up5 = new UnitPrecision(5, 50, 0.5, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		UnitPrecision up6 = new UnitPrecision(6, 60, 0.6, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		list.add(vc7);
		UnitPrecision up7 = new UnitPrecision(7, 70, 0.0, list);
		
		UnitPrecision up8 = new UnitPrecision(8, 80, 1.0, list); // 0.0 but non-empty list
		
		list.clear();
		UnitPrecision up9 = new UnitPrecision(9, 90, 1.0, list); // 0.0 and empty list
		
		assertEquals(0.1, up1.getPrecision(), 0.0001);
		assertEquals(0.2, up2.getPrecision(), 0.0001);
		assertEquals(0.3, up3.getPrecision(), 0.0001);
		assertEquals(0.4, up4.getPrecision(), 0.0001);
		assertEquals(0.5, up5.getPrecision(), 0.0001);
		assertEquals(0.6, up6.getPrecision(), 0.0001);
		assertEquals(0.0, up7.getPrecision(), 0.0001);
		assertEquals(1.0, up8.getPrecision(), 0.0001);
		assertEquals(1.0, up9.getPrecision(), 0.0001);
	}

	@Test
	public void testGetClones() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/srcfile11"), 1, 10), new Fragment(Paths.get("/srcfile21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/srcfile12"), 2, 20), new Fragment(Paths.get("/srcfile22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/srcfile13"), 3, 30), new Fragment(Paths.get("/srcfile23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/srcfile14"), 4, 40), new Fragment(Paths.get("/srcfile24"), 40, 400), false, false);
		VerifiedClone vc5 = new VerifiedClone(new Fragment(Paths.get("/srcfile15"), 5, 50), new Fragment(Paths.get("/srcfile25"), 50, 500), true, true);
		VerifiedClone vc6 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 6, 60), new Fragment(Paths.get("/srcfile26"), 60, 600), false, true);
		VerifiedClone vc7 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 7, 70), new Fragment(Paths.get("/srcfile26"), 70, 700), false, true);
		
		List<VerifiedClone> list = new LinkedList<VerifiedClone>();
		
		list.clear();
		list.add(vc1);
		UnitPrecision up1 = new UnitPrecision(1, 10, 0.1, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		UnitPrecision up2 = new UnitPrecision(2, 20, 0.2, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		UnitPrecision up3 = new UnitPrecision(3, 30, 0.3, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		UnitPrecision up4 = new UnitPrecision(4, 40, 0.4, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		UnitPrecision up5 = new UnitPrecision(5, 50, 0.5, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		UnitPrecision up6 = new UnitPrecision(6, 60, 0.6, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		list.add(vc7);
		UnitPrecision up7 = new UnitPrecision(7, 70, 0.0, list);
		
		UnitPrecision up8 = new UnitPrecision(8, 80, 1.0, list); // 0.0 but non-empty list
		
		list.clear();
		UnitPrecision up9 = new UnitPrecision(9, 90, 1.0, list); // 0.0 and empty list
		
		list = up1.getClones();
		assertTrue(up1.getClones().size() == 1);
		assertTrue(up1.getClones().get(0).equals(vc1));
		
		list = up2.getClones();
		assertTrue(up2.getClones().size() == 2);
		assertTrue(up2.getClones().get(0).equals(vc1));
		assertTrue(up2.getClones().get(1).equals(vc2));
		
		list = up3.getClones();
		assertTrue(up3.getClones().size() == 3);
		assertTrue(up3.getClones().get(0).equals(vc1));
		assertTrue(up3.getClones().get(1).equals(vc2));
		assertTrue(up3.getClones().get(2).equals(vc3));
		
		list = up4.getClones();
		assertTrue(up4.getClones().size() == 4);
		assertTrue(up4.getClones().get(0).equals(vc1));
		assertTrue(up4.getClones().get(1).equals(vc2));
		assertTrue(up4.getClones().get(2).equals(vc3));
		assertTrue(up4.getClones().get(3).equals(vc4));
		
		list = up5.getClones();
		assertTrue(up5.getClones().size() == 5);
		assertTrue(up5.getClones().get(0).equals(vc1));
		assertTrue(up5.getClones().get(1).equals(vc2));
		assertTrue(up5.getClones().get(2).equals(vc3));
		assertTrue(up5.getClones().get(3).equals(vc4));
		assertTrue(up5.getClones().get(4).equals(vc5));
		
		list = up6.getClones();
		assertTrue(up6.getClones().size() == 6);
		assertTrue(up6.getClones().get(0).equals(vc1));
		assertTrue(up6.getClones().get(1).equals(vc2));
		assertTrue(up6.getClones().get(2).equals(vc3));
		assertTrue(up6.getClones().get(3).equals(vc4));
		assertTrue(up6.getClones().get(4).equals(vc5));
		assertTrue(up6.getClones().get(5).equals(vc6));
		
		list = up7.getClones();
		assertTrue(up7.getClones().size() == 7);
		assertTrue(up7.getClones().get(0).equals(vc1));
		assertTrue(up7.getClones().get(1).equals(vc2));
		assertTrue(up7.getClones().get(2).equals(vc3));
		assertTrue(up7.getClones().get(3).equals(vc4));
		assertTrue(up7.getClones().get(4).equals(vc5));
		assertTrue(up7.getClones().get(5).equals(vc6));
		assertTrue(up7.getClones().get(6).equals(vc7));
		
		list = up8.getClones();
		assertTrue(up8.getClones().size() == 7);
		assertTrue(up8.getClones().get(0).equals(vc1));
		assertTrue(up8.getClones().get(1).equals(vc2));
		assertTrue(up8.getClones().get(2).equals(vc3));
		assertTrue(up8.getClones().get(3).equals(vc4));
		assertTrue(up8.getClones().get(4).equals(vc5));
		assertTrue(up8.getClones().get(5).equals(vc6));
		assertTrue(up8.getClones().get(6).equals(vc7));
		
		list = up9.getClones();
		assertTrue(up9.getClones().size() == 0);
	}

	@Test
	public void testEqualsObject() {
		VerifiedClone vc1 = new VerifiedClone(new Fragment(Paths.get("/srcfile11"), 1, 10), new Fragment(Paths.get("/srcfile21"), 10, 100), true, true);
		VerifiedClone vc2 = new VerifiedClone(new Fragment(Paths.get("/srcfile12"), 2, 20), new Fragment(Paths.get("/srcfile22"), 20, 200), false, true);
		VerifiedClone vc3 = new VerifiedClone(new Fragment(Paths.get("/srcfile13"), 3, 30), new Fragment(Paths.get("/srcfile23"), 30, 300), true, false);
		VerifiedClone vc4 = new VerifiedClone(new Fragment(Paths.get("/srcfile14"), 4, 40), new Fragment(Paths.get("/srcfile24"), 40, 400), false, false);
		VerifiedClone vc5 = new VerifiedClone(new Fragment(Paths.get("/srcfile15"), 5, 50), new Fragment(Paths.get("/srcfile25"), 50, 500), true, true);
		VerifiedClone vc6 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 6, 60), new Fragment(Paths.get("/srcfile26"), 60, 600), false, true);
		VerifiedClone vc7 = new VerifiedClone(new Fragment(Paths.get("/srcfile16"), 7, 70), new Fragment(Paths.get("/srcfile26"), 70, 700), false, true);
		
		List<VerifiedClone> list = new LinkedList<VerifiedClone>();
		
		list.clear();
		list.add(vc1);
		UnitPrecision up1 = new UnitPrecision(1, 10, 0.1, list);
		UnitPrecision up1c = new UnitPrecision(1, 10, 0.1, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		UnitPrecision up2 = new UnitPrecision(2, 20, 0.2, list);
		UnitPrecision up2c = new UnitPrecision(2, 20, 0.2, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		UnitPrecision up3 = new UnitPrecision(3, 30, 0.3, list);
		UnitPrecision up3c = new UnitPrecision(3, 30, 0.3, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		UnitPrecision up4 = new UnitPrecision(4, 40, 0.4, list);
		UnitPrecision up4c = new UnitPrecision(4, 40, 0.4, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		UnitPrecision up5 = new UnitPrecision(5, 50, 0.5, list);
		UnitPrecision up5c = new UnitPrecision(5, 50, 0.5, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		UnitPrecision up6 = new UnitPrecision(6, 60, 0.6, list);
		UnitPrecision up6c = new UnitPrecision(6, 60, 0.6, list);
		
		list.clear();
		list.add(vc1);
		list.add(vc2);
		list.add(vc3);
		list.add(vc4);
		list.add(vc5);
		list.add(vc6);
		list.add(vc7);
		UnitPrecision up7 = new UnitPrecision(7, 70, 0.0, list);
		UnitPrecision up7c = new UnitPrecision(7, 70, 0.0, list);
		
		UnitPrecision up8 = new UnitPrecision(8, 80, 1.0, list); // 0.0 but non-empty list
		UnitPrecision up8c = new UnitPrecision(8, 80, 1.0, list); // 0.0 but non-empty list
		
		list.clear();
		UnitPrecision up9 = new UnitPrecision(9, 90, 1.0, list); // 0.0 and empty list
		UnitPrecision up9c = new UnitPrecision(9, 90, 1.0, list); // 0.0 and empty list
		
		//
		assertTrue(up1.equals(up1));
		assertTrue(up1.equals(up1c));
		assertTrue(up1c.equals(up1));
		
		assertFalse(up1.equals(up2));
		assertFalse(up1.equals(up3));
		assertFalse(up1.equals(up4));
		assertFalse(up1.equals(up5));
		assertFalse(up1.equals(up6));
		assertFalse(up1.equals(up7));
		assertFalse(up1.equals(up8));
		assertFalse(up1.equals(up9));
		
		//
		assertTrue(up2.equals(up2));
		assertTrue(up2.equals(up2c));
		assertTrue(up2c.equals(up2));
		
		assertFalse(up2.equals(up1));
		assertFalse(up2.equals(up3));
		assertFalse(up2.equals(up4));
		assertFalse(up2.equals(up5));
		assertFalse(up2.equals(up6));
		assertFalse(up2.equals(up7));
		assertFalse(up2.equals(up8));
		assertFalse(up2.equals(up9));
		
		//
		assertTrue(up3.equals(up3));
		assertTrue(up3.equals(up3c));
		assertTrue(up3c.equals(up3));
		
		assertFalse(up3.equals(up1));
		assertFalse(up3.equals(up2));
		assertFalse(up3.equals(up4));
		assertFalse(up3.equals(up5));
		assertFalse(up3.equals(up6));
		assertFalse(up3.equals(up7));
		assertFalse(up3.equals(up8));
		assertFalse(up3.equals(up9));
		
		//
		assertTrue(up4.equals(up4));
		assertTrue(up4.equals(up4c));
		assertTrue(up4c.equals(up4));
		
		assertFalse(up4.equals(up1));
		assertFalse(up4.equals(up2));
		assertFalse(up4.equals(up3));
		assertFalse(up4.equals(up5));
		assertFalse(up4.equals(up6));
		assertFalse(up4.equals(up7));
		assertFalse(up4.equals(up8));
		assertFalse(up4.equals(up9));
		
		//
		assertTrue(up5.equals(up5));
		assertTrue(up5.equals(up5c));
		assertTrue(up5c.equals(up5));
		
		assertFalse(up5.equals(up1));
		assertFalse(up5.equals(up2));
		assertFalse(up5.equals(up3));
		assertFalse(up5.equals(up4));
		assertFalse(up5.equals(up6));
		assertFalse(up5.equals(up7));
		assertFalse(up5.equals(up8));
		assertFalse(up5.equals(up9));
		
		//
		assertTrue(up6.equals(up6));
		assertTrue(up6.equals(up6c));
		assertTrue(up6c.equals(up6));
		
		assertFalse(up6.equals(up1));
		assertFalse(up6.equals(up2));
		assertFalse(up6.equals(up3));
		assertFalse(up6.equals(up4));
		assertFalse(up6.equals(up5));
		assertFalse(up6.equals(up7));
		assertFalse(up6.equals(up8));
		assertFalse(up6.equals(up9));
		
		//
		assertTrue(up7.equals(up7));
		assertTrue(up7.equals(up7c));
		assertTrue(up7c.equals(up7));
		
		assertFalse(up7.equals(up1));
		assertFalse(up7.equals(up2));
		assertFalse(up7.equals(up3));
		assertFalse(up7.equals(up4));
		assertFalse(up7.equals(up5));
		assertFalse(up7.equals(up6));
		assertFalse(up7.equals(up8));
		assertFalse(up7.equals(up9));
		
		//
		assertTrue(up8.equals(up8));
		assertTrue(up8.equals(up8c));
		assertTrue(up8c.equals(up8));
		
		assertFalse(up8.equals(up1));
		assertFalse(up8.equals(up2));
		assertFalse(up8.equals(up3));
		assertFalse(up8.equals(up4));
		assertFalse(up8.equals(up5));
		assertFalse(up8.equals(up6));
		assertFalse(up8.equals(up7));
		assertFalse(up8.equals(up9));
		
		//
		assertTrue(up9.equals(up9));
		assertTrue(up9.equals(up9c));
		assertTrue(up9c.equals(up9));
		
		assertFalse(up9.equals(up1));
		assertFalse(up9.equals(up2));
		assertFalse(up9.equals(up3));
		assertFalse(up9.equals(up4));
		assertFalse(up9.equals(up5));
		assertFalse(up9.equals(up6));
		assertFalse(up9.equals(up7));
		assertFalse(up9.equals(up8));
		
	}

}