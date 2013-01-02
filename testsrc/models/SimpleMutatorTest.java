package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import experiment.ExperimentSpecification;

import util.FileUtil;
import util.SystemUtil;
import util.TXLUtil;
import validator.TokenValidator;
import validator.ValidatorResult;

public class SimpleMutatorTest {

	@Test
	public void testMutator() {
		Operator mCC_BT  = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		Operator mCC_EOL = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator mCF_A   = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		Operator mCF_R   = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		//Operator mCW_A   = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		//Operator mCW_R   = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		Operator mDL     = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		//Operator mIL     = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		//Operator mML     = new Operator("id", "description", 3, Paths.get("operators/mML"));
		Operator mRL_N   = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		//Operator mRL_S   = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		//Operator mSDL    = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		//Operator mSIL    = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		Operator mSRI    = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		Operator mARI    = new Operator("id", "description", 2, Paths.get("operators/mARI"));

		List<Operator> oplist = new LinkedList<Operator>();
		
		oplist.add(mCC_BT);
		SimpleMutator m11 = new SimpleMutator("m11", oplist);
		oplist.add(mCC_EOL);
		SimpleMutator m21 = new SimpleMutator("m21", oplist);
		oplist.add(mCF_R);
		SimpleMutator m31 = new SimpleMutator("m31", oplist);
		
		oplist.clear();
		oplist.add(mSRI);
		SimpleMutator m12 = new SimpleMutator("m12", oplist);
		oplist.add(mRL_N);
		SimpleMutator m22 = new SimpleMutator("m22", oplist);
		oplist.add(mCF_A);
		SimpleMutator m32 = new SimpleMutator("m32", oplist);
		
		oplist.clear();
		oplist.add(mDL);
		SimpleMutator m13 = new SimpleMutator("m13", oplist);
		oplist.add(mARI);
		SimpleMutator m23 = new SimpleMutator("m23", oplist);
		oplist.add(mCC_BT);
		SimpleMutator m33 = new SimpleMutator("m33", oplist);
		
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
			new SimpleMutator(null, oplist);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			new SimpleMutator("desc", null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		oplist.clear();
		try {
			new SimpleMutator("desc", oplist);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testGetDescription() {
		Operator mCC_BT  = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		Operator mCC_EOL = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator mCF_A   = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		Operator mCF_R   = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		//Operator mCW_A   = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		//Operator mCW_R   = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		Operator mDL     = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		//Operator mIL     = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		//Operator mML     = new Operator("id", "description", 3, Paths.get("operators/mML"));
		Operator mRL_N   = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		//Operator mRL_S   = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		//Operator mSDL    = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		//Operator mSIL    = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		Operator mSRI    = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		Operator mARI    = new Operator("id", "description", 2, Paths.get("operators/mARI"));

		List<Operator> oplist = new LinkedList<Operator>();
		
		oplist.add(mCC_BT);
		SimpleMutator m11 = new SimpleMutator("m11", oplist);
		oplist.add(mCC_EOL);
		SimpleMutator m21 = new SimpleMutator("m21", oplist);
		oplist.add(mCF_R);
		SimpleMutator m31 = new SimpleMutator("m31", oplist);
		
		oplist.clear();
		oplist.add(mSRI);
		SimpleMutator m12 = new SimpleMutator("m12", oplist);
		oplist.add(mRL_N);
		SimpleMutator m22 = new SimpleMutator("m22", oplist);
		oplist.add(mCF_A);
		SimpleMutator m32 = new SimpleMutator("m32", oplist);
		
		oplist.clear();
		oplist.add(mDL);
		SimpleMutator m13 = new SimpleMutator("m13", oplist);
		oplist.add(mARI);
		SimpleMutator m23 = new SimpleMutator("m23", oplist);
		oplist.add(mCC_BT);
		SimpleMutator m33 = new SimpleMutator("m33", oplist);
		

		assertEquals("m11", m11.getDescription());
		assertEquals("m21", m21.getDescription());
		assertEquals("m31", m31.getDescription());
		assertEquals("m12", m12.getDescription());
		assertEquals("m22", m22.getDescription());
		assertEquals("m32", m32.getDescription());
		assertEquals("m13", m13.getDescription());
		assertEquals("m23", m23.getDescription());
		assertEquals("m33", m33.getDescription());
	}

	@Test
	public void testGetTargetCloneType() {
		Operator mCC_BT  = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		Operator mCC_EOL = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator mCF_A   = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		Operator mCF_R   = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		//Operator mCW_A   = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		//Operator mCW_R   = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		Operator mDL     = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		//Operator mIL     = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		//Operator mML     = new Operator("id", "description", 3, Paths.get("operators/mML"));
		Operator mRL_N   = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		//Operator mRL_S   = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		//Operator mSDL    = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		//Operator mSIL    = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		Operator mSRI    = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		Operator mARI    = new Operator("id", "description", 2, Paths.get("operators/mARI"));

		List<Operator> oplist = new LinkedList<Operator>();
		
		oplist.add(mCC_BT);
		SimpleMutator m11 = new SimpleMutator("m11", oplist);
		oplist.add(mCC_EOL);
		SimpleMutator m21 = new SimpleMutator("m21", oplist);
		oplist.add(mCF_R);
		SimpleMutator m31 = new SimpleMutator("m31", oplist);
		
		oplist.clear();
		oplist.add(mSRI);
		SimpleMutator m12 = new SimpleMutator("m12", oplist);
		oplist.add(mRL_N);
		SimpleMutator m22 = new SimpleMutator("m22", oplist);
		oplist.add(mCF_A);
		SimpleMutator m32 = new SimpleMutator("m32", oplist);
		
		oplist.clear();
		oplist.add(mDL);
		SimpleMutator m13 = new SimpleMutator("m13", oplist);
		oplist.add(mARI);
		SimpleMutator m23 = new SimpleMutator("m23", oplist);
		oplist.add(mCC_BT);
		SimpleMutator m33 = new SimpleMutator("m33", oplist);
		
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
	}

	@Test
	public void testGetOperators() {
		Operator mCC_BT  = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		Operator mCC_EOL = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator mCF_A   = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		Operator mCF_R   = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		//Operator mCW_A   = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		//Operator mCW_R   = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		Operator mDL     = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		//Operator mIL     = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		//Operator mML     = new Operator("id", "description", 3, Paths.get("operators/mML"));
		Operator mRL_N   = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		//Operator mRL_S   = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		//Operator mSDL    = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		//Operator mSIL    = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		Operator mSRI    = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		Operator mARI    = new Operator("id", "description", 2, Paths.get("operators/mARI"));

		List<Operator> oplist = new LinkedList<Operator>();
		
		oplist.add(mCC_BT);
		SimpleMutator m11 = new SimpleMutator("m11", oplist);
		oplist.add(mCC_EOL);
		SimpleMutator m21 = new SimpleMutator("m21", oplist);
		oplist.add(mCF_R);
		SimpleMutator m31 = new SimpleMutator("m31", oplist);
		
		oplist.clear();
		oplist.add(mSRI);
		SimpleMutator m12 = new SimpleMutator("m12", oplist);
		oplist.add(mRL_N);
		SimpleMutator m22 = new SimpleMutator("m22", oplist);
		oplist.add(mCF_A);
		SimpleMutator m32 = new SimpleMutator("m32", oplist);
		
		oplist.clear();
		oplist.add(mDL);
		SimpleMutator m13 = new SimpleMutator("m13", oplist);
		oplist.add(mARI);
		SimpleMutator m23 = new SimpleMutator("m23", oplist);
		oplist.add(mCC_BT);
		SimpleMutator m33 = new SimpleMutator("m33", oplist);
		
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
	}

	@Test
	public void testPerformMutation() throws SQLException, IOException, InterruptedException {
		Operator mNC     = new Operator("mNC", "description", 1, Paths.get("operators/mNC"));
		Operator mCC_BT  = new Operator("mCC_BT", "description", 1, Paths.get("operators/mCC_BT"));
		Operator mCC_EOL = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator mCF_A   = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		Operator mCF_R   = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		Operator mCW_A   = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		Operator mCW_R   = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		Operator mDL     = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		Operator mIL     = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		Operator mML     = new Operator("id", "description", 3, Paths.get("operators/mML"));
		Operator mRL_N   = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		Operator mRL_S   = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		Operator mSDL    = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		Operator mSIL    = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		Operator mSRI    = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		Operator mARI    = new Operator("id", "description", 2, Paths.get("operators/mARI"));
		
		List<Operator> oplist = new LinkedList<Operator>();
		oplist.add(mNC);
		oplist.add(mCC_BT);
		oplist.add(mCC_EOL);
		oplist.add(mCF_A);
		oplist.add(mCF_R);
		oplist.add(mCW_A);
		oplist.add(mCW_R);
		oplist.add(mDL);
		oplist.add(mIL);
		oplist.add(mML);
		oplist.add(mRL_N);
		oplist.add(mRL_S);
		oplist.add(mSDL);
		oplist.add(mSIL);
		oplist.add(mSRI);
		oplist.add(mARI);
		
		
		// test mutator
		int ret;
		int numlines;
		List<Integer> lines;
		int bottom;
		int top;
		double diff = 0.30;
		double containment = 0.20;
		Path fragment = SystemUtil.getInstallRoot().resolve("testdata/MutatorTest/fragment");
		Path fragment_pretty = SystemUtil.getInstallRoot().resolve("testdata/MutatorTest/fragment_pretty");
		Path fragment_pretty_blind = SystemUtil.getInstallRoot().resolve("testdata/MutatorTest/fragment_pretty_blind");
		Path mutant = SystemUtil.getInstallRoot().resolve("testdata/MutatorTest/mutant");
		Path mutant_pretty = SystemUtil.getInstallRoot().resolve("testdata/MutatorTest/mutant_pretty");
		Path mutant_pretty_blind = SystemUtil.getInstallRoot().resolve("testdata/MutatorTest/mutant_pretty_blind");
		
		TXLUtil.prettyPrintSourceFragment(fragment, fragment_pretty, ExperimentSpecification.JAVA_LANGUAGE);
		TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("BlindRenameFragment.txl"), fragment_pretty, fragment_pretty_blind);
		
		//single operator
		for(Operator op : oplist) {
			List<Operator> ops = new LinkedList<Operator>();
			ops.add(op);
			SimpleMutator m = new SimpleMutator("", ops);
			ret = m.performMutation(fragment, mutant, 100, 100, diff, containment, ExperimentSpecification.JAVA_LANGUAGE);
			
			//mutation
			assertTrue(op.getName() + "", ret == 0);
			
			//containment
			numlines = FileUtil.countLines(fragment);
			lines = SimpleMutator.getLinesMutatedFromOriginal(fragment.toString(), mutant.toString());
			bottom = (int) (Math.ceil(numlines*containment));
			top = (int) (Math.floor(numlines*(1-containment)));
			for(int i : lines) {
				assertTrue(i > bottom && i <= top);
			}
			
			//similarity
			TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrintFragment.txl"), mutant, mutant_pretty);
			TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("BlindRenameFragment.txl"), mutant_pretty, mutant_pretty_blind);
			double sim = FileUtil.getSimilarity(fragment_pretty_blind, mutant_pretty_blind);
			assertTrue((1-sim) < diff);
			
			TokenValidator validator = new TokenValidator(0,0,ExperimentSpecification.JAVA_LANGUAGE);
			ValidatorResult vr = validator.validate(new Clone(new Fragment(fragment, 1, FileUtil.countLines(fragment)), new Fragment(mutant, 1, FileUtil.countLines(mutant))));
			assertTrue((1-vr.getSimilarity()) < diff);
		}
		
		//Multiple Operator
		List<Operator> ops = new LinkedList<Operator>();
		ops.add(mNC);
		ops.add(mCC_BT);
		ops.add(mRL_S);
		ops.add(mDL);
		ops.add(mCC_EOL);
		SimpleMutator m = new SimpleMutator("", ops);
		ret = m.performMutation(fragment, mutant, 100, 100, diff, containment, ExperimentSpecification.JAVA_LANGUAGE);
		
		//mutation
		assert(ret == 0);
		
		//containment
		numlines = FileUtil.countLines(fragment);
		lines = SimpleMutator.getLinesMutatedFromOriginal(fragment.toString(), mutant.toString());
		bottom = (int) (Math.ceil(numlines*containment));
		top = (int) (Math.floor(numlines*(1-containment)));
		for(int i : lines) {
			assertTrue(i > bottom && i <= top);
		}
		
		//similarity
		TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrintFragment.txl"), mutant, mutant_pretty);
		TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("BlindRenameFragment.txl"), mutant_pretty, mutant_pretty_blind);
		double sim = FileUtil.getSimilarity(fragment_pretty_blind, mutant_pretty_blind);
		assertTrue((1-sim) < diff);
		
		TokenValidator validator = new TokenValidator(0,0,ExperimentSpecification.JAVA_LANGUAGE);
		ValidatorResult vr = validator.validate(new Clone(new Fragment(fragment, 1, FileUtil.countLines(fragment)), new Fragment(mutant, 1, FileUtil.countLines(mutant))));
		assertTrue((1-vr.getSimilarity()) < diff);
	}

	@Test
	public void testGetLinesMutatedFromOriginal() {
		List<Integer> lines =SimpleMutator.getLinesMutatedFromOriginal("testdata/MutatorTest/getLinesMutatedFromOriginalTest/1", "testdata/MutatorTest/getLinesMutatedFromOriginalTest/2");
		//mixed changed, added, deleted
		assert(lines.contains(4));
		assert(lines.contains(12));
		assert(lines.contains(16));
		assert(lines.contains(10));
		assert(lines.contains(15));
		assert(lines.contains(20));
		assert(lines.contains(7));
		assert(lines.contains(13));
		assert(lines.contains(18));
		
			//check changed line
		lines =SimpleMutator.getLinesMutatedFromOriginal("testdata/MutatorTest/getLinesMutatedFromOriginalTest/1", "testdata/getLinesMutatedFromOriginalTest/3");
		assert(lines.contains(1));
		assert(lines.contains(10));
		assert(lines.contains(20));
		
			//check added lines
		lines = SimpleMutator.getLinesMutatedFromOriginal("testdata/MutatorTest/getLinesMutatedFromOriginalTest/1", "testdata/MutatorTest/getLinesMutatedFromOriginalTest/4");
		assert(lines.contains(1));
		assert(lines.contains(10));
		assert(lines.contains(20));
		assert(lines.contains(21));
		
			//check deleted lines
		lines = SimpleMutator.getLinesMutatedFromOriginal("testdata/MutatorTest/getLinesMutatedFromOriginalTest/1", "testdata/MutatorTest/getLinesMutatedFromOriginalTest/5");
		assert(lines.contains(1));
		assert(lines.contains(10));
		assert(lines.contains(20));
	}

	@Test
	public void testEqualsObject() {
		Operator mCC_BT  = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		Operator mCC_EOL = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator mCF_A   = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		Operator mCF_R   = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		//Operator mCW_A   = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		//Operator mCW_R   = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		Operator mDL     = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		//Operator mIL     = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		//Operator mML     = new Operator("id", "description", 3, Paths.get("operators/mML"));
		Operator mRL_N   = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		//Operator mRL_S   = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		//Operator mSDL    = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		//Operator mSIL    = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		Operator mSRI    = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		Operator mARI    = new Operator("id", "description", 2, Paths.get("operators/mARI"));

		List<Operator> oplist = new LinkedList<Operator>();
		
		oplist.add(mCC_BT);
		SimpleMutator m11 = new SimpleMutator("m11", oplist);
		SimpleMutator m11c = new SimpleMutator("m11", oplist);
		oplist.add(mCC_EOL);
		SimpleMutator m21 = new SimpleMutator("m21", oplist);
		oplist.add(mCF_R);
		SimpleMutator m31 = new SimpleMutator("m31", oplist);
		
		oplist.clear();
		oplist.add(mSRI);
		SimpleMutator m12 = new SimpleMutator("m12", oplist);
		oplist.add(mRL_N);
		SimpleMutator m22 = new SimpleMutator("m22", oplist);
		oplist.add(mCF_A);
		SimpleMutator m32 = new SimpleMutator("m32", oplist);
		
		oplist.clear();
		oplist.add(mDL);
		SimpleMutator m13 = new SimpleMutator("m13", oplist);
		oplist.add(mARI);
		SimpleMutator m23 = new SimpleMutator("m23", oplist);
		oplist.add(mCC_BT);
		SimpleMutator m33 = new SimpleMutator("m33", oplist);
		
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
