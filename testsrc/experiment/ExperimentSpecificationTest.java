package experiment;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

public class ExperimentSpecificationTest {

	@Test
	public void testExperimentSpecification() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		assertEquals(Paths.get("testdata/ExperimentDataTest/StoreDataHere/").toAbsolutePath().normalize(), es.getDataPath().toAbsolutePath().normalize());
		assertEquals(Paths.get("testdata/ExperimentDataTest/system/").toAbsolutePath().normalize(), es.getSystem().toAbsolutePath().normalize());
		assertEquals(Paths.get("testdata/ExperimentDataTest/repository/").toAbsolutePath().normalize(), es.getRepository().toAbsolutePath().normalize());
		assertEquals(ExperimentSpecification.JAVA_LANGUAGE, es.getLanguage());

		//assertEquals(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE, es.getGenerationType());
		assertEquals(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE, es.getFragmentType());
		assertEquals(15, es.getFragmentMinSizeLines());
		assertEquals(50, es.getFragmentMinSizeTokens());
		assertEquals(100, es.getFragmentMaxSizeLines());
		assertEquals(500, es.getFragmentMaxSizeTokens());
		//assertEquals(1, es.getMutationNumber());
		assertEquals(1, es.getInjectNumber());
		assertEquals(0.15, es.getSubsumeMatcherTolerance(), 0.00001);
		assertEquals(0.30, es.getAllowedFragmentDifference(), 0.00001);
		assertEquals(0.15, es.getMutationContainment(), 0.00001);
		assertEquals(100, es.getMutationAttempts());
		assertEquals(10, es.getOperatorAttempts());
		assertEquals(100, es.getMaxFragments());
		assertEquals(0.70, es.getPrecisionRequiredSimilarity(), 0.00001);
		assertEquals(0.70, es.getRecallRequiredSimilarity(), 0.00001);
	}

	@Test
	public void testDataPath() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setDataPath(Paths.get("/data"));
		assertEquals(Paths.get("/data"), es.getDataPath());
		es.setDataPath(Paths.get("/data2"));
		assertEquals(Paths.get("/data2"), es.getDataPath());
		
		boolean error;
		error = false;
		try {
			es.setDataPath(null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testSystem() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setSystem(Paths.get("/system"));
		assertEquals(Paths.get("/system"), es.getSystem());
		es.setSystem(Paths.get("/system2"));
		assertEquals(Paths.get("/system2"), es.getSystem());
		
		boolean error;
		error = false;
		try {
			es.setSystem(null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testRepository() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setRepository(Paths.get("/rep"));
		assertEquals(Paths.get("/rep"), es.getRepository());
		es.setRepository(Paths.get("/rep2"));
		assertEquals(Paths.get("/rep2"), es.getRepository());
		
		boolean error;
		error = false;
		try {
			es.setRepository(null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testLanguage() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		assertEquals(ExperimentSpecification.JAVA_LANGUAGE, es.getLanguage());
		es.setLanguage(ExperimentSpecification.C_LANGUAGE);
		assertEquals(ExperimentSpecification.C_LANGUAGE, es.getLanguage());
		es.setLanguage(ExperimentSpecification.CS_LANGUAGE);
		assertEquals(ExperimentSpecification.CS_LANGUAGE, es.getLanguage());
		
		boolean error;
		error = false;
		try {
			es.setLanguage(99);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

/*
	@Test
	public void testGenerationType() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setGenerationType(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE);
		assertEquals(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE, es.getGenerationType());
		es.setGenerationType(ExperimentSpecification.MANUAL_GENERATION_TYPE);
		assertEquals(ExperimentSpecification.MANUAL_GENERATION_TYPE, es.getGenerationType());
		
		boolean error;
		error = false;
		try {
			es.setGenerationType(99);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}
*/
	
	@Test
	public void testFragmentType() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setFragmentType(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE);
		assertEquals(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE, es.getFragmentType());
		es.setFragmentType(ExperimentSpecification.BLOCK_FRAGMENT_TYPE);
		assertEquals(ExperimentSpecification.BLOCK_FRAGMENT_TYPE, es.getFragmentType());
		
		boolean error;
		error = false;
		try {
			es.setFragmentType(99);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}


	@Test
	public void testFragmentMinSizeLines() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setFragmentMaxSizeLines(100);
		es.setFragmentMinSizeLines(50);
		assertEquals(50, es.getFragmentMinSizeLines());
		es.setFragmentMinSizeLines(75);
		assertEquals(75, es.getFragmentMinSizeLines());
		
		boolean error;
		
		error = false;
		try {
			es.setFragmentMinSizeLines(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMinSizeLines(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMinSizeLines(101);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMinSizeLines(110);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testFragmentMaxSizeLines() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setFragmentMinSizeLines(10);
		es.setFragmentMaxSizeLines(50);
		assertEquals(50, es.getFragmentMaxSizeLines());
		es.setFragmentMaxSizeLines(75);
		assertEquals(75, es.getFragmentMaxSizeLines());
		
		boolean error;
		
		error = false;
		try {
			es.setFragmentMaxSizeLines(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMaxSizeLines(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMaxSizeLines(9);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMaxSizeLines(10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertFalse(error);
		
		error = false;
		try {
			es.setFragmentMaxSizeLines(8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}
	
	@Test
	public void testFragmentMinSizeTokens() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setFragmentMaxSizeTokens(100);
		es.setFragmentMinSizeTokens(10);
		assertEquals(10, es.getFragmentMinSizeTokens());
		es.setFragmentMinSizeTokens(55);
		assertEquals(55, es.getFragmentMinSizeTokens());
		es.setFragmentMinSizeTokens(1);
		assertEquals(1, es.getFragmentMinSizeTokens());
		
		boolean error;
		
		error = false;
		try {
			es.setFragmentMinSizeTokens(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMinSizeTokens(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMinSizeTokens(101);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMinSizeTokens(110);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMinSizeTokens(100);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertFalse(error);
	}

	@Test
	public void testFragmentMaxSizeTokens() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setFragmentMinSizeTokens(10);
		
		es.setFragmentMaxSizeTokens(100);
		assertEquals(100, es.getFragmentMaxSizeTokens());
		es.setFragmentMaxSizeTokens(235);
		assertEquals(235, es.getFragmentMaxSizeTokens());
		
		boolean error;
		
		error = false;
		try {
			es.setFragmentMaxSizeTokens(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMaxSizeTokens(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMaxSizeTokens(9);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMaxSizeTokens(8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setFragmentMaxSizeTokens(10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertFalse(error);
	}

//	@Test
//	public void testMutationNumber() {
//		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
//		es.setMutationNumber(10);
//		assertEquals(10, es.getMutationNumber());
//		es.setMutationNumber(5);
//		assertEquals(5, es.getMutationNumber());
//		es.setMutationNumber(1);
//		assertEquals(1, es.getMutationNumber());
//		
//		boolean error;
//		
//		error = false;
//		try {
//			es.setMutationNumber(0);
//		} catch (IllegalArgumentException e) {
//			error = true;
//		}
//		assertTrue(error);
//		
//		error = false;
//		try {
//			es.setMutationNumber(-10);
//		} catch (IllegalArgumentException e) {
//			error = true;
//		}
//		assertTrue(error);
//	}

	@Test
	public void testInjectNumber() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		
		es.setInjectNumber(1);
		assertEquals(1, es.getInjectNumber());
		es.setInjectNumber(5);
		assertEquals(5, es.getInjectNumber());
		
		boolean error;
		
		error = false;
		try {
			es.setInjectNumber(0);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setInjectNumber(-10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testSubsumeMatcherTolerance() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setSubsumeMatcherTolerance(0.0);
		assertEquals(0.0, es.getSubsumeMatcherTolerance(), 0.00001);
		es.setSubsumeMatcherTolerance(0.50);
		assertEquals(0.50, es.getSubsumeMatcherTolerance(), 0.00001);
		es.setSubsumeMatcherTolerance(0.75);
		assertEquals(0.75, es.getSubsumeMatcherTolerance(), 0.00001);
		es.setSubsumeMatcherTolerance(1.0);
		assertEquals(1.0, es.getSubsumeMatcherTolerance(), 0.00001);
		
		boolean error;
		error = false;
		try {
			es.setSubsumeMatcherTolerance(-0.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			es.setSubsumeMatcherTolerance(1.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}
	
	@Test
	public void testAllowedFragmentDifference() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setAllowedFragmentDifference(0.0);
		assertEquals(0.0, es.getAllowedFragmentDifference(), 0.00001);
		es.setAllowedFragmentDifference(0.50);
		assertEquals(0.50, es.getAllowedFragmentDifference(), 0.00001);
		es.setAllowedFragmentDifference(0.75);
		assertEquals(0.75, es.getAllowedFragmentDifference(), 0.00001);
		es.setAllowedFragmentDifference(1.0);
		assertEquals(1.0, es.getAllowedFragmentDifference(), 0.00001);
		
		boolean error;
		error = false;
		try {
			es.setAllowedFragmentDifference(-0.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			es.setAllowedFragmentDifference(1.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testMutationContainment() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setMutationContainment(0.0);
		assertEquals(0.0, es.getMutationContainment(), 0.00001);
		es.setMutationContainment(0.25);
		assertEquals(0.25, es.getMutationContainment(), 0.00001);
		es.setMutationContainment(0.50);
		assertEquals(0.50, es.getMutationContainment(), 0.00001);
		
		boolean error;
		error = false;
		try {
			es.setMutationContainment(-0.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			es.setMutationContainment(0.51);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testMutationAttempts() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setMutationAttempts(1);
		assertEquals(1, es.getMutationAttempts());
		es.setMutationAttempts(100);
		assertEquals(100, es.getMutationAttempts());
		es.setMutationAttempts(50);
		assertEquals(50, es.getMutationAttempts());
		
		boolean error;
		
		error = false;
		try {
			es.setMutationAttempts(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setMutationAttempts(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testOperatorAttempts() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setOperatorAttempts(1);
		assertEquals(1, es.getOperatorAttempts());
		es.setOperatorAttempts(100);
		assertEquals(100, es.getOperatorAttempts());
		
		boolean error;
		
		error = false;
		try {
			es.setOperatorAttempts(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setOperatorAttempts(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testMaxFragments() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setMaxFragments(1);
		assertEquals(1, es.getMaxFragments());
		es.setMaxFragments(10);
		assertEquals(10, es.getMaxFragments());
		
		boolean error;
		
		error = false;
		try {
			es.setMaxFragments(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setMaxFragments(-1);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			es.setMaxFragments(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testPrecisionRequiredSimilarity() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setPrecisionRequiredSimilarity(0.0);
		assertEquals(0.0, es.getPrecisionRequiredSimilarity(), 0.00001);
		es.setPrecisionRequiredSimilarity(0.50);
		assertEquals(0.50, es.getPrecisionRequiredSimilarity(), 0.00001);
		es.setPrecisionRequiredSimilarity(0.75);
		assertEquals(0.75, es.getPrecisionRequiredSimilarity(), 0.00001);
		es.setPrecisionRequiredSimilarity(1.0);
		assertEquals(1.0, es.getPrecisionRequiredSimilarity(), 0.00001);
		
		boolean error;
		error = false;
		try {
			es.setPrecisionRequiredSimilarity(-0.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			es.setPrecisionRequiredSimilarity(1.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testRecallRequiredSimilarity() {
		ExperimentSpecification es = new ExperimentSpecification(Paths.get("testdata/ExperimentDataTest/StoreDataHere/"), Paths.get("testdata/ExperimentDataTest/system/"), Paths.get("testdata/ExperimentDataTest/repository/"), ExperimentSpecification.JAVA_LANGUAGE);
		es.setRecallRequiredSimilarity(0.0);
		assertEquals(0.0, es.getRecallRequiredSimilarity(), 0.00001);
		es.setRecallRequiredSimilarity(0.50);
		assertEquals(0.50, es.getRecallRequiredSimilarity(), 0.00001);
		es.setRecallRequiredSimilarity(0.75);
		assertEquals(0.75, es.getRecallRequiredSimilarity(), 0.00001);
		es.setRecallRequiredSimilarity(1.0);
		assertEquals(1.0, es.getRecallRequiredSimilarity(), 0.00001);
		
		boolean error;
		error = false;
		try {
			es.setRecallRequiredSimilarity(-0.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);

		error = false;
		try {
			es.setRecallRequiredSimilarity(1.10);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}
	
	@Test
	public void testLanguageSupported() {
		assertTrue(ExperimentSpecification.isLanguageSupported(ExperimentSpecification.JAVA_LANGUAGE));
		assertTrue(ExperimentSpecification.isLanguageSupported(ExperimentSpecification.C_LANGUAGE));
		assertTrue(ExperimentSpecification.isLanguageSupported(ExperimentSpecification.CS_LANGUAGE));
		assertFalse(ExperimentSpecification.isLanguageSupported(100));
		assertFalse(ExperimentSpecification.isLanguageSupported(-100));
	}
}
