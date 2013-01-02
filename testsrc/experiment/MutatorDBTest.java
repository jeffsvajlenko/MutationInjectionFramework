package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import experiment.MutatorDB;
import experiment.OperatorDB;

public class MutatorDBTest {

	@Test
	public void testMutatorDB() {
		OperatorDB mCC_BT  = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_BT"));
		OperatorDB mCC_EOL = new OperatorDB(2, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB mCF_A   = new OperatorDB(3, "id", "description", 1, Paths.get("operators/mCF_A"));
		OperatorDB mCF_R   = new OperatorDB(4, "id", "description", 1, Paths.get("operators/mCF_R"));
		//OperatorDB mCW_A   = new OperatorDB(5, "id", "description", 1, Paths.get("operators/mCW_A"));
		//OperatorDB mCW_R   = new OperatorDB(6, "id", "description", 1, Paths.get("operators/mCW_R"));
		OperatorDB mDL     = new OperatorDB(7, "id", "description", 3, Paths.get("operators/mDL"));
		//OperatorDB mIL     = new OperatorDB(8, "id", "description", 3, Paths.get("operators/mIL"));
		//OperatorDB mML     = new OperatorDB(9, "id", "description", 3, Paths.get("operators/mML"));
		OperatorDB mRL_N   = new OperatorDB(10, "id", "description", 2, Paths.get("operators/mRL_N"));
		//OperatorDB mRL_S   = new OperatorDB(11, "id", "description", 2, Paths.get("operators/mRL_S"));
		//OperatorDB mSDL    = new OperatorDB(12, "id", "description", 3, Paths.get("operators/mSDL"));
		//OperatorDB mSIL    = new OperatorDB(13, "id", "description", 3, Paths.get("operators/mSIL"));
		OperatorDB mSRI    = new OperatorDB(14, "id", "description", 2, Paths.get("operators/mSRI"));
		OperatorDB mARI    = new OperatorDB(15, "id", "description", 2, Paths.get("operators/mARI"));

		List<OperatorDB> oplist = new LinkedList<OperatorDB>();
		
		oplist.add(mCC_BT);
		MutatorDB m11 = new MutatorDB(1, "m11", oplist);
		oplist.add(mCC_EOL);
		MutatorDB m21 = new MutatorDB(2, "m21", oplist);
		oplist.add(mCF_R);
		MutatorDB m31 = new MutatorDB(3, "m31", oplist);
		
		oplist.clear();
		oplist.add(mSRI);
		MutatorDB m12 = new MutatorDB(4, "m12", oplist);
		oplist.add(mRL_N);
		MutatorDB m22 = new MutatorDB(5, "m22", oplist);
		oplist.add(mCF_A);
		MutatorDB m32 = new MutatorDB(6, "m32", oplist);
		
		oplist.clear();
		oplist.add(mDL);
		MutatorDB m13 = new MutatorDB(7, "m13", oplist);
		oplist.add(mARI);
		MutatorDB m23 = new MutatorDB(8, "m23", oplist);
		oplist.add(mCC_BT);
		MutatorDB m33 = new MutatorDB(9, "m33", oplist);
		
		//check id
		assertEquals(1, m11.getId());
		assertEquals(2, m21.getId());
		assertEquals(3, m31.getId());
		assertEquals(4, m12.getId());
		assertEquals(5, m22.getId());
		assertEquals(6, m32.getId());
		assertEquals(7, m13.getId());
		assertEquals(8, m23.getId());
		assertEquals(9, m33.getId());
		
		//check name
		assertEquals("m11", m11.getDescription());
		assertEquals("m21", m21.getDescription());
		assertEquals("m31", m31.getDescription());
		assertEquals("m12", m12.getDescription());
		assertEquals("m22", m22.getDescription());
		assertEquals("m32", m32.getDescription());
		assertEquals("m13", m13.getDescription());
		assertEquals("m23", m23.getDescription());
		assertEquals("m33", m33.getDescription());
		
		//check type
		assertEquals(1, m11.getTargetCloneType());
		assertEquals(1, m21.getTargetCloneType());
		assertEquals(1, m31.getTargetCloneType());
		assertEquals(2, m12.getTargetCloneType());
		assertEquals(2, m22.getTargetCloneType());
		assertEquals(2, m32.getTargetCloneType());
		assertEquals(3, m13.getTargetCloneType());
		assertEquals(3, m23.getTargetCloneType());
		assertEquals(3, m33.getTargetCloneType());
		
		//check oplist
		assertTrue(m11.getOperators().size() == 1);
		assertTrue(m11.getOperators().get(0).equals(mCC_BT));
		assertTrue(m21.getOperators().size() == 2);
		assertTrue(m21.getOperators().get(0).equals(mCC_BT));
		assertTrue(m21.getOperators().get(1).equals(mCC_EOL));
		assertTrue(m31.getOperators().size() == 3);
		assertTrue(m31.getOperators().get(0).equals(mCC_BT));
		assertTrue(m31.getOperators().get(1).equals(mCC_EOL));
		assertTrue(m31.getOperators().get(2).equals(mCF_R));
		
		
		assertTrue(m12.getOperators().size() == 1);
		assertTrue(m12.getOperators().get(0).equals(mSRI));
		assertTrue(m22.getOperators().size() == 2);
		assertTrue(m22.getOperators().get(0).equals(mSRI));
		assertTrue(m22.getOperators().get(1).equals(mRL_N));
		assertTrue(m32.getOperators().size() == 3);
		assertTrue(m32.getOperators().get(0).equals(mSRI));
		assertTrue(m32.getOperators().get(1).equals(mRL_N));
		assertTrue(m32.getOperators().get(2).equals(mCF_A));
		
		
		assertTrue(m13.getOperators().size() == 1);
		assertTrue(m13.getOperators().get(0).equals(mDL));
		
		assertTrue(m23.getOperators().size() == 2);
		assertTrue(m23.getOperators().get(0).equals(mDL));
		assertTrue(m23.getOperators().get(1).equals(mARI));
		
		assertTrue(m33.getOperators().size() == 3);
		assertTrue(m33.getOperators().get(0).equals(mDL));
		assertTrue(m33.getOperators().get(1).equals(mARI));
		assertTrue(m33.getOperators().get(2).equals(mCC_BT));
	
	//Errors
		boolean error;
		
		error = false;
		try {
			new MutatorDB(1, null, oplist);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			new MutatorDB(1, "desc", null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		oplist.clear();
		try {
			new MutatorDB(1, "desc", oplist);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
	}

	@Test
	public void testIncludesOperator() {
		OperatorDB mCC_BT  = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_BT"));
		OperatorDB mCC_EOL = new OperatorDB(2, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB mCF_A   = new OperatorDB(3, "id", "description", 1, Paths.get("operators/mCF_A"));
		OperatorDB mCF_R   = new OperatorDB(4, "id", "description", 1, Paths.get("operators/mCF_R"));
		OperatorDB mCW_A   = new OperatorDB(5, "id", "description", 1, Paths.get("operators/mCW_A"));
		OperatorDB mCW_R   = new OperatorDB(6, "id", "description", 1, Paths.get("operators/mCW_R"));
		OperatorDB mDL     = new OperatorDB(7, "id", "description", 3, Paths.get("operators/mDL"));
		OperatorDB mIL     = new OperatorDB(8, "id", "description", 3, Paths.get("operators/mIL"));
		OperatorDB mML     = new OperatorDB(9, "id", "description", 3, Paths.get("operators/mML"));
		OperatorDB mRL_N   = new OperatorDB(10, "id", "description", 2, Paths.get("operators/mRL_N"));
		OperatorDB mRL_S   = new OperatorDB(11, "id", "description", 2, Paths.get("operators/mRL_S"));
		OperatorDB mSDL    = new OperatorDB(12, "id", "description", 3, Paths.get("operators/mSDL"));
		OperatorDB mSIL    = new OperatorDB(13, "id", "description", 3, Paths.get("operators/mSIL"));
		OperatorDB mSRI    = new OperatorDB(14, "id", "description", 2, Paths.get("operators/mSRI"));
		OperatorDB mARI    = new OperatorDB(15, "id", "description", 2, Paths.get("operators/mARI"));
		
		List<OperatorDB> oplist = new LinkedList<OperatorDB>();
		
		oplist.add(mCC_BT);
		MutatorDB m11 = new MutatorDB(1, "m11", oplist);
		oplist.add(mCC_EOL);
		MutatorDB m21 = new MutatorDB(2, "m21", oplist);
		oplist.add(mCF_R);
		MutatorDB m31 = new MutatorDB(3, "m31", oplist);
		oplist.clear();
		
		
		assertTrue(m11.includesOperator(mCC_BT.getId()));
		assertTrue(!m11.includesOperator(mCC_EOL.getId()));
		assertTrue(!m11.includesOperator(mCF_A.getId()));
		assertTrue(!m11.includesOperator(mCF_R.getId()));
		assertTrue(!m11.includesOperator(mCW_A.getId()));
		assertTrue(!m11.includesOperator(mCW_R.getId()));
		assertTrue(!m11.includesOperator(mDL.getId()));
		assertTrue(!m11.includesOperator(mIL.getId()));
		assertTrue(!m11.includesOperator(mML.getId()));
		assertTrue(!m11.includesOperator(mRL_N.getId()));
		assertTrue(!m11.includesOperator(mRL_S.getId()));
		assertTrue(!m11.includesOperator(mSDL.getId()));
		assertTrue(!m11.includesOperator(mSIL.getId()));
		assertTrue(!m11.includesOperator(mSRI.getId()));
		assertTrue(!m11.includesOperator(mARI.getId()));
		
		assertTrue(m21.includesOperator(mCC_BT.getId()));
		assertTrue(m21.includesOperator(mCC_EOL.getId()));
		assertTrue(!m21.includesOperator(mCF_A.getId()));
		assertTrue(!m21.includesOperator(mCF_R.getId()));
		assertTrue(!m21.includesOperator(mCW_A.getId()));
		assertTrue(!m21.includesOperator(mCW_R.getId()));
		assertTrue(!m21.includesOperator(mDL.getId()));
		assertTrue(!m21.includesOperator(mIL.getId()));
		assertTrue(!m21.includesOperator(mML.getId()));
		assertTrue(!m21.includesOperator(mRL_N.getId()));
		assertTrue(!m21.includesOperator(mRL_S.getId()));
		assertTrue(!m21.includesOperator(mSDL.getId()));
		assertTrue(!m21.includesOperator(mSIL.getId()));
		assertTrue(!m21.includesOperator(mSRI.getId()));
		assertTrue(!m21.includesOperator(mARI.getId()));
		
		assertTrue(m31.includesOperator(mCC_BT.getId()));
		assertTrue(m31.includesOperator(mCC_EOL.getId()));
		assertTrue(!m31.includesOperator(mCF_A.getId()));
		assertTrue(m31.includesOperator(mCF_R.getId()));
		assertTrue(!m31.includesOperator(mCW_A.getId()));
		assertTrue(!m31.includesOperator(mCW_R.getId()));
		assertTrue(!m31.includesOperator(mDL.getId()));
		assertTrue(!m31.includesOperator(mIL.getId()));
		assertTrue(!m31.includesOperator(mML.getId()));
		assertTrue(!m31.includesOperator(mRL_N.getId()));
		assertTrue(!m31.includesOperator(mRL_S.getId()));
		assertTrue(!m31.includesOperator(mSDL.getId()));
		assertTrue(!m31.includesOperator(mSIL.getId()));
		assertTrue(!m31.includesOperator(mSRI.getId()));
		assertTrue(!m31.includesOperator(mARI.getId()));
	}
	
	@Test
	public void testGetId() {
		OperatorDB mCC_BT  = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_BT"));
		OperatorDB mCC_EOL = new OperatorDB(2, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB mCF_A   = new OperatorDB(3, "id", "description", 1, Paths.get("operators/mCF_A"));
		OperatorDB mCF_R   = new OperatorDB(4, "id", "description", 1, Paths.get("operators/mCF_R"));
		//OperatorDB mCW_A   = new OperatorDB(5, "id", "description", 1, Paths.get("operators/mCW_A"));
		//OperatorDB mCW_R   = new OperatorDB(6, "id", "description", 1, Paths.get("operators/mCW_R"));
		OperatorDB mDL     = new OperatorDB(7, "id", "description", 3, Paths.get("operators/mDL"));
		//OperatorDB mIL     = new OperatorDB(8, "id", "description", 3, Paths.get("operators/mIL"));
		//OperatorDB mML     = new OperatorDB(9, "id", "description", 3, Paths.get("operators/mML"));
		OperatorDB mRL_N   = new OperatorDB(10, "id", "description", 2, Paths.get("operators/mRL_N"));
		//OperatorDB mRL_S   = new OperatorDB(11, "id", "description", 2, Paths.get("operators/mRL_S"));
		//OperatorDB mSDL    = new OperatorDB(12, "id", "description", 3, Paths.get("operators/mSDL"));
		//OperatorDB mSIL    = new OperatorDB(13, "id", "description", 3, Paths.get("operators/mSIL"));
		OperatorDB mSRI    = new OperatorDB(14, "id", "description", 2, Paths.get("operators/mSRI"));
		OperatorDB mARI    = new OperatorDB(15, "id", "description", 2, Paths.get("operators/mARI"));

		List<OperatorDB> oplist = new LinkedList<OperatorDB>();
		
		oplist.add(mCC_BT);
		MutatorDB m11 = new MutatorDB(1, "m11", oplist);
		oplist.add(mCC_EOL);
		MutatorDB m21 = new MutatorDB(2, "m21", oplist);
		oplist.add(mCF_R);
		MutatorDB m31 = new MutatorDB(3, "m31", oplist);
		
		oplist.clear();
		oplist.add(mSRI);
		MutatorDB m12 = new MutatorDB(4, "m12", oplist);
		oplist.add(mRL_N);
		MutatorDB m22 = new MutatorDB(5, "m22", oplist);
		oplist.add(mCF_A);
		MutatorDB m32 = new MutatorDB(6, "m32", oplist);
		
		oplist.clear();
		oplist.add(mDL);
		MutatorDB m13 = new MutatorDB(7, "m13", oplist);
		oplist.add(mARI);
		MutatorDB m23 = new MutatorDB(8, "m23", oplist);
		oplist.add(mCC_BT);
		MutatorDB m33 = new MutatorDB(9, "m33", oplist);
		
		//check id
		assertEquals(1, m11.getId());
		assertEquals(2, m21.getId());
		assertEquals(3, m31.getId());
		assertEquals(4, m12.getId());
		assertEquals(5, m22.getId());
		assertEquals(6, m32.getId());
		assertEquals(7, m13.getId());
		assertEquals(8, m23.getId());
		assertEquals(9, m33.getId());
	}
	
	@Test
	public void testEqualsObject() {
		OperatorDB mCC_BT  = new OperatorDB(1, "id", "description", 1, Paths.get("operators/mCC_BT"));
		OperatorDB mCC_EOL = new OperatorDB(2, "id", "description", 1, Paths.get("operators/mCC_EOL"));
		OperatorDB mCF_A   = new OperatorDB(3, "id", "description", 1, Paths.get("operators/mCF_A"));
		OperatorDB mCF_R   = new OperatorDB(4, "id", "description", 1, Paths.get("operators/mCF_R"));
		//OperatorDB mCW_A   = new OperatorDB(5, "id", "description", 1, Paths.get("operators/mCW_A"));
		//OperatorDB mCW_R   = new OperatorDB(6, "id", "description", 1, Paths.get("operators/mCW_R"));
		OperatorDB mDL     = new OperatorDB(7, "id", "description", 3, Paths.get("operators/mDL"));
		//OperatorDB mIL     = new OperatorDB(8, "id", "description", 3, Paths.get("operators/mIL"));
		//OperatorDB mML     = new OperatorDB(9, "id", "description", 3, Paths.get("operators/mML"));
		OperatorDB mRL_N   = new OperatorDB(10, "id", "description", 2, Paths.get("operators/mRL_N"));
		//OperatorDB mRL_S   = new OperatorDB(11, "id", "description", 2, Paths.get("operators/mRL_S"));
		//OperatorDB mSDL    = new OperatorDB(12, "id", "description", 3, Paths.get("operators/mSDL"));
		//OperatorDB mSIL    = new OperatorDB(13, "id", "description", 3, Paths.get("operators/mSIL"));
		OperatorDB mSRI    = new OperatorDB(14, "id", "description", 2, Paths.get("operators/mSRI"));
		OperatorDB mARI    = new OperatorDB(15, "id", "description", 2, Paths.get("operators/mARI"));

		List<OperatorDB> oplist = new LinkedList<OperatorDB>();
		
		oplist.add(mCC_BT);
		MutatorDB m11 = new MutatorDB(1, "m11", oplist);
		MutatorDB m11c = new MutatorDB(1, "m11", oplist);
		oplist.add(mCC_EOL);
		MutatorDB m21 = new MutatorDB(2, "m21", oplist);
		oplist.add(mCF_R);
		MutatorDB m31 = new MutatorDB(3, "m31", oplist);
		
		oplist.clear();
		oplist.add(mSRI);
		MutatorDB m12 = new MutatorDB(4, "m12", oplist);
		oplist.add(mRL_N);
		MutatorDB m22 = new MutatorDB(5, "m22", oplist);
		oplist.add(mCF_A);
		MutatorDB m32 = new MutatorDB(6, "m32", oplist);
		
		oplist.clear();
		oplist.add(mDL);
		MutatorDB m13 = new MutatorDB(7, "m13", oplist);
		oplist.add(mARI);
		MutatorDB m23 = new MutatorDB(8, "m23", oplist);
		oplist.add(mCC_BT);
		MutatorDB m33 = new MutatorDB(9, "m33", oplist);
		
		assertTrue(m11.equals(m11));
		assertTrue(m11.equals(m11c));
		assertFalse(m11.equals(null));
		assertFalse(m11.equals(m21));
		assertFalse(m11.equals(m31));
		assertFalse(m11.equals(m12));
		assertFalse(m11.equals(m22));
		assertFalse(m11.equals(m32));
		assertFalse(m11.equals(m13));
		assertFalse(m11.equals(m23));
		assertFalse(m11.equals(m33));
	}

}
