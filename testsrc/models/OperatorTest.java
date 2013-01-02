package models;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Test;

import experiment.ExperimentSpecification;

public class OperatorTest {

	@Test
	public void testOperator() {
		Operator o;
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals("id", o.getName());
		assertEquals("description", o.getDescription());
		assertEquals(1, o.getTargetCloneType());
		
		boolean throwexception;
		
	// NullPointerExceptions
		throwexception = false;
		try {
			new Operator(null, "description", 1, Paths.get("operators/mCC_EOL"));
		} catch(NullPointerException e) {
			throwexception = true;
		}
		assertTrue(throwexception);
		
		throwexception = false;
		try {
			new Operator("id", null, 1, Paths.get("operators/mCC_EOL"));
		} catch(NullPointerException e) {
			throwexception = true;
		}
		assertTrue(throwexception);
		
		throwexception = false;
		try {
			new Operator("id", "description", 1, null);
		} catch(NullPointerException e) {
			throwexception = true;
		}
		assertTrue(throwexception);
	
	// IllegalArgumentException
		throwexception = false;
		try {
			new Operator("id", "description", 1, Paths.get("operators/wqeqwe"));
		} catch (IllegalArgumentException e) {
			throwexception = true;
		}
	}

	@Test
	public void testGetMutator() {
		Operator o;
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals(Paths.get("operators/mCC_EOL"), o.getMutator());
		
		o = new Operator("id1", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals(Paths.get("operators/mCC_EOL"), o.getMutator());
		
		o = new Operator("id1", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals(Paths.get("operators/mCC_EOL"), o.getMutator());
	}

	@Test
	public void testGetName() {
		Operator o;
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals("id", o.getName());
		
		o = new Operator("id1", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals("id1", o.getName());
		
		o = new Operator("id1", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals("id1", o.getName());
	}

	@Test
	public void testGetDescription() {
		Operator o;
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals("description", o.getDescription());
		
		o = new Operator("id1", "description2", 1, Paths.get("operators/mCC_EOL"));
		assertEquals("description2", o.getDescription());
		
		o = new Operator("id1", "description3", 1, Paths.get("operators/mCC_EOL"));
		assertEquals("description3", o.getDescription());
	}

	@Test
	public void testGetTargetCloneType() {
		Operator o;
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		assertEquals("description", o.getDescription());
		
		o = new Operator("id1", "description2", 2, Paths.get("operators/mCC_EOL"));
		assertEquals("description2", o.getDescription());
		
		o = new Operator("id1", "description3", 3, Paths.get("operators/mCC_EOL"));
		assertEquals("description3", o.getDescription());
	}

	@Test
	public void testEqualsObject() {
		Operator o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator oe = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator od1 = new Operator("id_", "description", 1, Paths.get("operators/mCC_EOL"));
		Operator od2 = new Operator("id", "description_", 1, Paths.get("operators/mCC_EOL"));
		Operator od3 = new Operator("id", "description", 2, Paths.get("operators/mCC_EOL"));
		Operator od4 = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		
		assertTrue(o.equals(oe));
		assertTrue(o.equals(o));
		assertTrue(!o.equals(od1));
		assertTrue(!o.equals(od2));
		assertTrue(!o.equals(od3));
		assertTrue(!o.equals(od4));
		assertTrue(!o.equals(null));
	}

	@Test
	public void testPerformOperator() throws FileNotFoundException, IOException, InterruptedException {
		Path jinfile = Paths.get("testdata/OperatorTest/javafunctionfragment");
		Path cinfile = Paths.get("testdata/OperatorTest/cfunctionfragment");
		Path csinfile = Paths.get("testdata/OperatorTest/csfunctionfragment");
		
		Path mCC_BT = Paths.get("testdata/OperatorTest/functionfragment_mCC_BT");
		Path mCC_EOL = Paths.get("testdata/OperatorTest/functionfragment_mCC_EOL");
		Path mCF_A = Paths.get("testdata/OperatorTest/functionfragment_mCF_A");
		Path mCF_R = Paths.get("testdata/OperatorTest/functionfragment_mCF_R");
		Path mCW_A = Paths.get("testdata/OperatorTest/functionfragment_mCW_A");
		Path mCW_R = Paths.get("testdata/OperatorTest/functionfragment_mCW_R");
		Path mDL = Paths.get("testdata/OperatorTest/functionfragment_mDL");
		Path mIL = Paths.get("testdata/OperatorTest/functionfragment_mIL");
		Path mML = Paths.get("testdata/OperatorTest/functionfragment_mML");
		Path mRL_N = Paths.get("testdata/OperatorTest/functionfragment_mRL_N");
		Path mRL_S = Paths.get("testdata/OperatorTest/functionfragment_mRL_S");
		Path mSDL = Paths.get("testdata/OperatorTest/functionfragment_mSDL");
		Path mSIL = Paths.get("testdata/OperatorTest/functionfragment_mSIL");
		Path mSRI = Paths.get("testdata/OperatorTest/functionfragment_mSRI");
		Path mARI = Paths.get("testdata/OperatorTest/functionfragment_mARI");
		
		Operator o;
		int retval;
		
	//java
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		retval = o.performOperator(jinfile, mCC_BT, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		retval = o.performOperator(jinfile, mCC_EOL, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		retval = o.performOperator(jinfile, mCF_A, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		retval = o.performOperator(jinfile, mCF_R, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		retval = o.performOperator(jinfile, mCW_A, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		retval = o.performOperator(jinfile, mCW_R, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		retval = o.performOperator(jinfile, mDL, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		retval = o.performOperator(jinfile, mIL, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mML"));
		retval = o.performOperator(jinfile, mML, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		retval = o.performOperator(jinfile, mRL_N, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		retval = o.performOperator(jinfile, mRL_S, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		retval = o.performOperator(jinfile, mSDL, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		retval = o.performOperator(jinfile, mSIL, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		retval = o.performOperator(jinfile, mSRI, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mARI"));
		retval = o.performOperator(jinfile, mARI, 10, ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(0 == retval);
		
	//c
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		retval = o.performOperator(cinfile, mCC_BT, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		retval = o.performOperator(cinfile, mCC_EOL, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		retval = o.performOperator(cinfile, mCF_A, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		retval = o.performOperator(cinfile, mCF_R, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		retval = o.performOperator(cinfile, mCW_A, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		retval = o.performOperator(cinfile, mCW_R, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		retval = o.performOperator(cinfile, mDL, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		retval = o.performOperator(cinfile, mIL, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mML"));
		retval = o.performOperator(cinfile, mML, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		retval = o.performOperator(cinfile, mRL_N, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		retval = o.performOperator(cinfile, mRL_S, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		retval = o.performOperator(cinfile, mSDL, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		retval = o.performOperator(cinfile, mSIL, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		retval = o.performOperator(cinfile, mSRI, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mARI"));
		retval = o.performOperator(cinfile, mARI, 10, ExperimentSpecification.C_LANGUAGE);
		assertTrue(0 == retval);
	
	// cs
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_BT"));
		retval = o.performOperator(csinfile, mCC_BT, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCC_EOL"));
		retval = o.performOperator(csinfile, mCC_EOL, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCF_A"));
		retval = o.performOperator(csinfile, mCF_A, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCF_R"));
		retval = o.performOperator(csinfile, mCF_R, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCW_A"));
		retval = o.performOperator(csinfile, mCW_A, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 1, Paths.get("operators/mCW_R"));
		retval = o.performOperator(csinfile, mCW_R, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mDL"));
		retval = o.performOperator(csinfile, mDL, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mIL"));
		retval = o.performOperator(csinfile, mIL, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mML"));
		retval = o.performOperator(csinfile, mML, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mRL_N"));
		retval = o.performOperator(csinfile, mRL_N, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mRL_S"));
		retval = o.performOperator(csinfile, mRL_S, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mSDL"));
		retval = o.performOperator(csinfile, mSDL, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 3, Paths.get("operators/mSIL"));
		retval = o.performOperator(csinfile, mSIL, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mSRI"));
		retval = o.performOperator(csinfile, mSRI, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);
		
		o = new Operator("id", "description", 2, Paths.get("operators/mARI"));
		retval = o.performOperator(csinfile, mARI, 10, ExperimentSpecification.CS_LANGUAGE);
		assertTrue(0 == retval);

	//cleanup
		Files.deleteIfExists(mCC_BT);
		Files.deleteIfExists(mCC_EOL);
		Files.deleteIfExists(mCF_A);
		Files.deleteIfExists(mCF_R);
		Files.deleteIfExists(mCW_A);
		Files.deleteIfExists(mCW_R);
		Files.deleteIfExists(mDL);
		Files.deleteIfExists(mIL);
		Files.deleteIfExists(mML);
		Files.deleteIfExists(mRL_N);
		Files.deleteIfExists(mRL_S);
		Files.deleteIfExists(mSDL);
		Files.deleteIfExists(mSIL);
		Files.deleteIfExists(mSRI);
		Files.deleteIfExists(mARI);
	}

}
