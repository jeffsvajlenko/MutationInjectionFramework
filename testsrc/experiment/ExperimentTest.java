package experiment;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import main.ArtisticStyleFailedException;
import main.FileSanetizationFailedException;
import models.AbstractMutator;
import models.Clone;
import models.CloneDetectionReport;
import models.Fragment;
import models.InvalidToolRunnerException;
import models.MutantBase;
import models.Tool;
import models.VerifiedClone;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import sun.security.x509.EDIPartyName;
import util.FileUtil;
import util.FragmentUtil;
import util.SystemUtil;
import util.TXLException;
import util.TXLUtil;
import validator.LineValidator;
import validator.TokenValidator;
import validator.ValidatorResult;

public class ExperimentTest {

	@Test
	public void testCreateAutomaticExperiment() {
		fail("unimplemented.");
	}
	
	@Test
	public void testCreateManualExperiment() throws IOException, IllegalArgumentException, SQLException, InterruptedException, ArtisticStyleFailedException, FileSanetizationFailedException, IllegalStateException, IllegalManualImportSpecification {
		Path jrepository = Paths.get("data/systems/java/");
		Path jsystem = Paths.get("data/systems/java/");
		Path data = Paths.get("testdata/ExperimentTest/CreateManualExperiment_data");
		if(Files.exists(data)) {
			FileUtils.deleteDirectory(data.toFile());
		}
		
		ExperimentSpecification es = new ExperimentSpecification(data, jsystem, jrepository, ExperimentSpecification.JAVA_LANGUAGE);
		
		//Generation Properties
		es.setAllowedFragmentDifference(0.30);
		int maxSizeLines = 200; es.setFragmentMaxSizeLines(maxSizeLines);
		int maxSizeTokens = 2000; es.setFragmentMaxSizeTokens(maxSizeTokens);
		int minSizeLines = 15; es.setFragmentMinSizeLines(minSizeLines);
		int minSizeTokens = 100; es.setFragmentMinSizeTokens(minSizeTokens);
		es.setFragmentType(ExperimentSpecification.BLOCK_FRAGMENT_TYPE);
		es.setInjectNumber(1);
		es.setMaxFragments(1);
		es.setMutationAttempts(25);
		es.setMutationContainment(0.15);
		es.setOperatorAttempts(10);
		es.setPrecisionRequiredSimilarity(0.50);
		es.setRecallRequiredSimilarity(0.50);
		es.setSubsumeMatcherTolerance(0.15);
		
		//Create Experiment
		Experiment e = Experiment.createManualExperiment(es, Paths.get("testdata/ExperimentTest/CreateManualExperiment/import_spec"), System.out);
		ExperimentData ed = e.getExperimentData();
		
		//Check copy into repository
		assertTrue(FileUtils.contentEquals(ed.getRepositoryPath().resolve("f1").toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/f1").toFile()));
		assertTrue(FileUtils.contentEquals(ed.getRepositoryPath().resolve("mf1").toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/mf1").toFile()));
		
		assertTrue(FileUtils.contentEquals(ed.getRepositoryPath().resolve("f2").toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/f2").toFile()));
		assertTrue(FileUtils.contentEquals(ed.getRepositoryPath().resolve("mf2").toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/mf2").toFile()));
		
		assertTrue(FileUtils.contentEquals(ed.getRepositoryPath().resolve("f3").toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/f3").toFile()));
		assertTrue(FileUtils.contentEquals(ed.getRepositoryPath().resolve("mf3").toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/mf3").toFile()));
		
		assertTrue(FileUtils.contentEquals(ed.getRepositoryPath().resolve("f4").toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/f4").toFile()));
		assertTrue(FileUtils.contentEquals(ed.getRepositoryPath().resolve("mf4").toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/mf4").toFile()));
		
		//Check import fragment/mutantfragment/mutators
		FragmentDB f;
		MutantFragment mf;
		f = ed.getFragment(1);
		mf = ed.getMutantFragment(1);
		assertTrue(FileUtils.contentEquals(f.getFragmentFile().toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/f1").toFile()));
		assertTrue(FileUtils.contentEquals(mf.getFragmentFile().toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/mf1").toFile()));
		
		f = ed.getFragment(2);
		mf = ed.getMutantFragment(2);
		assertTrue(FileUtils.contentEquals(f.getFragmentFile().toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/f2").toFile()));
		assertTrue(FileUtils.contentEquals(mf.getFragmentFile().toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/mf2").toFile()));
		
		f = ed.getFragment(3);
		mf = ed.getMutantFragment(3);
		assertTrue(FileUtils.contentEquals(f.getFragmentFile().toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/f3").toFile()));
		assertTrue(FileUtils.contentEquals(mf.getFragmentFile().toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/mf3").toFile()));
		
		f = ed.getFragment(4);
		mf = ed.getMutantFragment(4);
		assertTrue(FileUtils.contentEquals(f.getFragmentFile().toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/f4").toFile()));
		assertTrue(FileUtils.contentEquals(mf.getFragmentFile().toFile(), Paths.get("testdata/ExperimentTest/CreateManualExperiment/mf4").toFile()));
		
		//Cleanup
		e.getExperimentData().close();
		FileUtils.deleteDirectory(Paths.get("testdata/ExperimentTest/CreateManualExperiment_data").toFile());
	}
	
	@Test
	public void testEvaluateRecall() throws IllegalArgumentException, NullPointerException, FileNotFoundException, InterruptedException, IOException, InvalidToolRunnerException, InputMismatchException {
		
		Fragment f1 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/contrib/TextAreaFigure.java"), 714, 753); //1020
		Fragment f2 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/contrib/PolygonFigure.java"), 86, 125); //577
		Fragment f3 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/figures/TextFigure.java"), 208, 239); //497
		Fragment f4 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/figures/TextFigure.java"), 100, 150); //497
		
		Clone c12 = new Clone(f1,f2);//top (file location)
		Clone c13 = new Clone(f1,f3);//middle
		Clone c23 = new Clone(f2,f3);//bottom
		
		ToolDB nicad = new ToolDB(5, "NiCad", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));
		ToolDB nicad2 = new ToolDB(8, "NiCad", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));

		
		MutantBaseDB mb_top = new MutantBaseDB(
				10, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f1,
				f2);
		MutantBaseDB mb_middle = new MutantBaseDB(
				20, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f1,
				f3);
		MutantBaseDB mb_bottom = new MutantBaseDB(
				30, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f2,
				f3);
		MutantBaseDB mb_fake = new MutantBaseDB(
				30, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f1,
				f4);
		
		
		double subsumeTolerance = 0.15;
		double requiredSimilarity = 0.70;
		TokenValidator tokval = new TokenValidator(1.0, 0.0, ExperimentSpecification.JAVA_LANGUAGE);
		LineValidator linval = new LineValidator(1.0, 0.0, ExperimentSpecification.JAVA_LANGUAGE);
		double toksim;
		double linsim;
		UnitRecall ur;
		
		CloneDetectionReport cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/report"));
		
	//Test: Location in report file (vary location, first, middle, last), all perfect match + test output values
		MutantBaseDB mb = mb_top;
		//Top clone
		ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getRecall(), .001);
		assertTrue( (ur.getClone().getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClone().getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClone()).getSimilarity();
		linsim = linval.validate(ur.getClone()).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone().getFragment2().equals(f2));
		
		//Middle Clone
		mb = mb_middle;
		ur = Experiment.evaluateRecall(cdr, nicad2, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad2.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getRecall(), .001);
		assertTrue( (ur.getClone().getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClone().getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClone()).getSimilarity();
		linsim = linval.validate(ur.getClone()).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		//Bottom Clone
		mb = mb_bottom;
		ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getRecall(), .001);
		assertTrue( (ur.getClone().getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClone().getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClone()).getSimilarity();
		linsim = linval.validate(ur.getClone()).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		assertTrue(ur.getClone().getFragment1().equals(f2));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		//not there
		mb = mb_fake;
		ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(0.0, ur.getRecall(), .001);
		assertTrue(ur.getClone() == null);
		
		
	//Test Subsume Matcher (ignore similarity)
		mb = new MutantBaseDB(
				10, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f1,
				f3);
		
		//Set #1: f1 match varies, f3 perfect match
		CloneDetectionReport cdr111 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/111"));
		ur = Experiment.evaluateRecall(cdr111, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		713, 746)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr112 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/112"));
		ur = Experiment.evaluateRecall(cdr112, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				713, 747)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr113 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/113"));
		ur = Experiment.evaluateRecall(cdr113, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				713, 750)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr114 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/114"));
		ur = Experiment.evaluateRecall(cdr114, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				713, 753)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr115 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/115"));
		ur = Experiment.evaluateRecall(cdr115, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				713, 754)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr121 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/121"));
		ur = Experiment.evaluateRecall(cdr121, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		714, 746)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr122 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/122"));
		ur = Experiment.evaluateRecall(cdr122, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				714, 747)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr123 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/123"));
		ur = Experiment.evaluateRecall(cdr123, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				714, 750)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr124 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/124"));
		ur = Experiment.evaluateRecall(cdr124, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				714, 753)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr125 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/125"));
		ur = Experiment.evaluateRecall(cdr125, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				714, 754)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr131 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/131"));
		ur = Experiment.evaluateRecall(cdr131, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		717, 746)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr132 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/132"));
		ur = Experiment.evaluateRecall(cdr132, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				717, 747)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr133 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/133"));
		ur = Experiment.evaluateRecall(cdr133, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				717, 750)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr134 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/134"));
		ur = Experiment.evaluateRecall(cdr134, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				717, 753)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr135 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/135"));
		ur = Experiment.evaluateRecall(cdr135, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				717, 754)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr141 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/141"));
		ur = Experiment.evaluateRecall(cdr141, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		720, 746)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr142 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/142"));
		ur = Experiment.evaluateRecall(cdr142, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				720, 747)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr143 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/143"));
		ur = Experiment.evaluateRecall(cdr143, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				720, 750)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr144 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/144"));
		ur = Experiment.evaluateRecall(cdr144, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				720, 753)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr145 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/145"));
		ur = Experiment.evaluateRecall(cdr145, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
				720, 754)));
		assertTrue(ur.getClone().getFragment2().equals(f3));
		
		CloneDetectionReport cdr151 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/151"));
		ur = Experiment.evaluateRecall(cdr151, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 746)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr152 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/152"));
		ur = Experiment.evaluateRecall(cdr152, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 747)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr153 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/153"));
		ur = Experiment.evaluateRecall(cdr153, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 750)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr154 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/154"));
		ur = Experiment.evaluateRecall(cdr154, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 753)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr155 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/155"));
		ur = Experiment.evaluateRecall(cdr155, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 754)));
		//assertTrue(ur.getClone().getFragment2().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr1sa = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sa"));
		ur = Experiment.evaluateRecall(cdr1sa, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr1sb = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sb"));
		ur = Experiment.evaluateRecall(cdr1sb, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr1sc = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sc"));
		ur = Experiment.evaluateRecall(cdr1sc, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr1sd = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sd"));
		ur = Experiment.evaluateRecall(cdr1sd, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone()== null);

		
		//alterative, swap fragments, stillvay the same fragment
		CloneDetectionReport cdr111a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/111a"));
		ur = Experiment.evaluateRecall(cdr111a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		713, 746)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr112a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/112a"));
		ur = Experiment.evaluateRecall(cdr112a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				713, 747)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr113a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/113a"));
		ur = Experiment.evaluateRecall(cdr113a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				713, 750)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr114a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/114a"));
		ur = Experiment.evaluateRecall(cdr114a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				713, 753)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr115a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/115a"));
		ur = Experiment.evaluateRecall(cdr115a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				713, 754)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr121a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/121a"));
		ur = Experiment.evaluateRecall(cdr121a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		714, 746)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr122a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/122a"));
		ur = Experiment.evaluateRecall(cdr122a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				714, 747)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr123a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/123a"));
		ur = Experiment.evaluateRecall(cdr123a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				714, 750)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr124a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/124a"));
		ur = Experiment.evaluateRecall(cdr124a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				714, 753)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr125a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/125a"));
		ur = Experiment.evaluateRecall(cdr125a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				714, 754)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr131a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/131a"));
		ur = Experiment.evaluateRecall(cdr131a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		717, 746)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr132a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/132a"));
		ur = Experiment.evaluateRecall(cdr132a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				717, 747)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr133a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/133a"));
		ur = Experiment.evaluateRecall(cdr133a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				717, 750)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr134a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/134a"));
		ur = Experiment.evaluateRecall(cdr134a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				717, 753)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr135a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/135a"));
		ur = Experiment.evaluateRecall(cdr135a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				717, 754)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr141a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/141a"));
		ur = Experiment.evaluateRecall(cdr141a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		720, 746)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr142a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/142a"));
		ur = Experiment.evaluateRecall(cdr142a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				720, 747)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr143a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/143a"));
		ur = Experiment.evaluateRecall(cdr143a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				720, 750)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr144a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/144a"));
		ur = Experiment.evaluateRecall(cdr144a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				720, 753)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr145a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/145a"));
		ur = Experiment.evaluateRecall(cdr145a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
				720, 754)));
		assertTrue(ur.getClone().getFragment1().equals(f3));
		
		CloneDetectionReport cdr151a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/151a"));
		ur = Experiment.evaluateRecall(cdr151a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 746)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr152a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/152a"));
		ur = Experiment.evaluateRecall(cdr152a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 747)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr153a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/153a"));
		ur = Experiment.evaluateRecall(cdr153a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 750)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr154a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/154a"));
		ur = Experiment.evaluateRecall(cdr154a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 753)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr155a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/155a"));
		ur = Experiment.evaluateRecall(cdr155a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 754)));
		//assertTrue(ur.getClone().getFragment1().equals(f3));
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr1saa = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1saa"));
		ur = Experiment.evaluateRecall(cdr1saa, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr1sba = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sba"));
		ur = Experiment.evaluateRecall(cdr1sba, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr1sca = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sca"));
		ur = Experiment.evaluateRecall(cdr1sca, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone()== null);
		
		CloneDetectionReport cdr1sda = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sda"));
		ur = Experiment.evaluateRecall(cdr1sda, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone()== null);
		
	//Vary fragment 2

		//Set #1: f1 match varies, f3 perfect match
		CloneDetectionReport cdr211 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/211"));
		ur = Experiment.evaluateRecall(cdr211, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		207, 234)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));

		CloneDetectionReport cdr212 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/212"));
		ur = Experiment.evaluateRecall(cdr212, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				207, 235)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr213 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/213"));
		ur = Experiment.evaluateRecall(cdr213, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				207, 236)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr214 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/214"));
		ur = Experiment.evaluateRecall(cdr214, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				207, 239)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr215 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/215"));
		ur = Experiment.evaluateRecall(cdr215, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				207, 240)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr221 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/221"));
		ur = Experiment.evaluateRecall(cdr221, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		208, 234)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr222 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/222"));
		ur = Experiment.evaluateRecall(cdr222, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				208, 235)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr223 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/223"));
		ur = Experiment.evaluateRecall(cdr223, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				208, 236)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr224 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/224"));
		ur = Experiment.evaluateRecall(cdr224, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				208, 239)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr225 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/225"));
		ur = Experiment.evaluateRecall(cdr225, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				208, 240)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr231 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/231"));
		ur = Experiment.evaluateRecall(cdr231, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		210, 234)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr232 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/232"));
		ur = Experiment.evaluateRecall(cdr232, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				210, 235)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr233 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/233"));
		ur = Experiment.evaluateRecall(cdr233, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				210, 236)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr234 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/234"));
		ur = Experiment.evaluateRecall(cdr234, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				210, 239)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr235 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/235"));
		ur = Experiment.evaluateRecall(cdr235, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				210, 240)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr241 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/241"));
		ur = Experiment.evaluateRecall(cdr241, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		212, 234)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr242 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/242"));
		ur = Experiment.evaluateRecall(cdr242, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				212, 235)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr243 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/243"));
		ur = Experiment.evaluateRecall(cdr243, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				212, 236)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr244 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/244"));
		ur = Experiment.evaluateRecall(cdr244, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				212, 239)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr245 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/245"));
		ur = Experiment.evaluateRecall(cdr245, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
				212, 240)));
		assertTrue(ur.getClone().getFragment1().equals(f1));
		
		CloneDetectionReport cdr251 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/251"));
		ur = Experiment.evaluateRecall(cdr251, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 234)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr252 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/252"));
		ur = Experiment.evaluateRecall(cdr252, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 235)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr253 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/253"));
		ur = Experiment.evaluateRecall(cdr253, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 236)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr254 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/254"));
		ur = Experiment.evaluateRecall(cdr254, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 239)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr255 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/255"));
		ur = Experiment.evaluateRecall(cdr255, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 240)));
		//assertTrue(ur.getClone().getFragment1().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr2sa = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sa"));
		ur = Experiment.evaluateRecall(cdr2sa, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr2sb = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sb"));
		ur = Experiment.evaluateRecall(cdr2sb, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr2sc = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sc"));
		ur = Experiment.evaluateRecall(cdr2sc, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr2sd = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sd"));
		ur = Experiment.evaluateRecall(cdr2sd, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		
		//alterative, swap fragments, stillvay the same fragment
		CloneDetectionReport cdr211a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/211a"));
		ur = Experiment.evaluateRecall(cdr211a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		207, 234)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));

		CloneDetectionReport cdr212a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/212a"));
		ur = Experiment.evaluateRecall(cdr212a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				207, 235)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr213a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/213a"));
		ur = Experiment.evaluateRecall(cdr213a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				207, 236)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr214a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/214a"));
		ur = Experiment.evaluateRecall(cdr214a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				207, 239)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr215a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/215a"));
		ur = Experiment.evaluateRecall(cdr215a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				207, 240)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr221a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/221a"));
		ur = Experiment.evaluateRecall(cdr221a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		208, 234)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr222a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/222a"));
		ur = Experiment.evaluateRecall(cdr222a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				208, 235)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr223a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/223a"));
		ur = Experiment.evaluateRecall(cdr223a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				208, 236)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr224a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/224a"));
		ur = Experiment.evaluateRecall(cdr224a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				208, 239)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr225a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/225a"));
		ur = Experiment.evaluateRecall(cdr225a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				208, 240)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr231a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/231a"));
		ur = Experiment.evaluateRecall(cdr231a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		210, 234)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr232a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/232a"));
		ur = Experiment.evaluateRecall(cdr232a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				210, 235)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr233a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/233a"));
		ur = Experiment.evaluateRecall(cdr233a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				210, 236)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr234a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/234a"));
		ur = Experiment.evaluateRecall(cdr234a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				210, 239)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr235a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/235a"));
		ur = Experiment.evaluateRecall(cdr235a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				210, 240)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr241a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/241a"));
		ur = Experiment.evaluateRecall(cdr241a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		212, 234)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr242a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/242a"));
		ur = Experiment.evaluateRecall(cdr242a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				212, 235)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr243a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/243a"));
		ur = Experiment.evaluateRecall(cdr243a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				212, 236)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr244a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/244a"));
		ur = Experiment.evaluateRecall(cdr244a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				212, 239)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr245a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/245a"));
		ur = Experiment.evaluateRecall(cdr245a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone().getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClone().getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
				212, 240)));
		assertTrue(ur.getClone().getFragment2().equals(f1));
		
		CloneDetectionReport cdr251a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/251a"));
		ur = Experiment.evaluateRecall(cdr251a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 234)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr252a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/252a"));
		ur = Experiment.evaluateRecall(cdr252a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 235)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr253a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/253a"));
		ur = Experiment.evaluateRecall(cdr253a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 236)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr254a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/254a"));
		ur = Experiment.evaluateRecall(cdr254a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 239)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr255a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/255a"));
		ur = Experiment.evaluateRecall(cdr255a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		//assertTrue(ur.getClone().getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 240)));
		//assertTrue(ur.getClone().getFragment2().equals(f1));
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr2saa = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2saa"));
		ur = Experiment.evaluateRecall(cdr2saa, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr2sba = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sba"));
		ur = Experiment.evaluateRecall(cdr2sba, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr2sca = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sca"));
		ur = Experiment.evaluateRecall(cdr2sca, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		
		CloneDetectionReport cdr2sda = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sda"));
		ur = Experiment.evaluateRecall(cdr2sda, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getRecall(), 0.001);
		assertTrue(ur.getClone() == null);
		
	// Test Similarity
		mb = mb_middle;
		requiredSimilarity = 0.58;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim1"));
		ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getRecall(), .001);
		assertTrue( (ur.getClone().getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClone().getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClone()).getSimilarity();
		linsim = linval.validate(ur.getClone()).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		//System.out.println(toksim + " " + linsim + " " + ur.getClone());
		
		mb = mb_middle;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim2"));
		ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getRecall(), .001);
		assertTrue( (ur.getClone().getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClone().getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClone()).getSimilarity();
		linsim = linval.validate(ur.getClone()).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		//System.out.println(toksim + " " + linsim + " " + ur.getClone());
		
		mb = mb_middle;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim3"));
		ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getRecall(), .001);
		assertTrue( (ur.getClone().getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClone().getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClone()).getSimilarity();
		linsim = linval.validate(ur.getClone()).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		//System.out.println(toksim + " " + linsim + " " + ur.getClone());
		
		mb = mb_middle;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim4"));
		ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getRecall(), .001);
		assertTrue( (ur.getClone().getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClone().getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClone().getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClone()).getSimilarity();
		linsim = linval.validate(ur.getClone()).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		//System.out.println(toksim + " " + linsim + " " + ur.getClone());
		
		mb = mb_middle;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim5"));
		ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(0.0, ur.getRecall(), .001);
		assertTrue(ur.getClone() == null);

//Automatic Check (check full range of values)
		Clone craftClone;
		int length1 = f1.getEndLine() - f1.getStartLine() + 1;
		int length2 = f3.getEndLine() - f3.getStartLine() + 1;
		int flength1 = FileUtil.countLines(f1.getSrcFile());
		int flength2 = FileUtil.countLines(f3.getSrcFile());
		double tolerance = 0.15;
		int tol1 = (int) (length1*tolerance);
		int tol2 = (int) (length2*tolerance);
		System.out.println(tol1 + " " + tol2);
		int language = ExperimentSpecification.JAVA_LANGUAGE;
		subsumeTolerance = 0.15;
		mb = new MutantBaseDB(
				14, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f1,
				f3);
		
//Fragment1
	//Startline Cases
		//System.out.println("F1_StartLine");
		for(int i = 1; i <= f1.getEndLine(); i++) {
			craftClone = new Clone(new Fragment(f1.getSrcFile(), i, f1.getEndLine()), f3);
			
			Files.deleteIfExists(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			Path cdr_path = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			PrintWriter out = new PrintWriter(cdr_path.toFile());
			out.print(craftClone.getFragment1().getSrcFile() + ",");
			out.print(craftClone.getFragment1().getStartLine() + ",");
			out.print(craftClone.getFragment1().getEndLine() + ",");
			out.print(craftClone.getFragment2().getSrcFile() + ",");
			out.print(craftClone.getFragment2().getStartLine() + ",");
			out.print(craftClone.getFragment2().getEndLine() + "\n");
			out.flush();
			out.close();
			
			cdr = new CloneDetectionReport(cdr_path);
			ur = Experiment.evaluateRecall(cdr, nicad, mb, 0.0, subsumeTolerance, language, System.out);
			
			//OKAY
			if(i <= f1.getStartLine()+tol1) {
				//System.out.println(i);
				assertTrue(ur.getClone() != null);
				assertTrue(ur.getClone().equals(craftClone));
				assertTrue(ur.getToolid() == nicad.getId());
				assertTrue(ur.getBaseid() == mb.getId());
				assertTrue(ur.getRecall() == 1.0);
			
			//NOT OKAY
			} else {
				//System.out.println(i + "-");
				assertTrue(ur.getClone() == null);
				assertTrue(ur.getRecall() == 0.0);
			}
		}
		
	//Endline Cases
		//System.out.println("F1_EndLine");
		for(int i = f1.getStartLine(); i <= flength1; i++) {
			craftClone = new Clone(new Fragment(f1.getSrcFile(), f1.getStartLine(), i), f3);
			
			Files.deleteIfExists(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			Path cdr_path = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			PrintWriter out = new PrintWriter(cdr_path.toFile());
			out.print(craftClone.getFragment1().getSrcFile() + ",");
			out.print(craftClone.getFragment1().getStartLine() + ",");
			out.print(craftClone.getFragment1().getEndLine() + ",");
			out.print(craftClone.getFragment2().getSrcFile() + ",");
			out.print(craftClone.getFragment2().getStartLine() + ",");
			out.print(craftClone.getFragment2().getEndLine() + "\n");
			out.flush();
			out.close();
			
			cdr = new CloneDetectionReport(cdr_path);
			ur = Experiment.evaluateRecall(cdr, nicad, mb, 0.0, subsumeTolerance, language, System.out);
			
			//NOT OKAY
			if(i >= f1.getEndLine()-tol1) {
				//System.out.println(i);
				assertTrue(ur.getClone() != null);
				assertTrue(ur.getClone().equals(craftClone));
				assertTrue(ur.getToolid() == nicad.getId());
				assertTrue(ur.getBaseid() == mb.getId());
				assertTrue(ur.getRecall() == 1.0);
			//OKAY
			} else {
				//System.out.println(i + "-");
				assertTrue(ur.getClone() == null);
				assertTrue(ur.getRecall() == 0.0);
			}
		}
	
		
//Fragment2
	//Startline Cases
		//System.out.println("F2_Startline");
		for(int i = 1; i <= f3.getEndLine(); i++) {
			craftClone = new Clone(f1, new Fragment(f3.getSrcFile(), i, f3.getEndLine()));
			
			Files.deleteIfExists(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			Path cdr_path = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			PrintWriter out = new PrintWriter(cdr_path.toFile());
			out.print(craftClone.getFragment1().getSrcFile() + ",");
			out.print(craftClone.getFragment1().getStartLine() + ",");
			out.print(craftClone.getFragment1().getEndLine() + ",");
			out.print(craftClone.getFragment2().getSrcFile() + ",");
			out.print(craftClone.getFragment2().getStartLine() + ",");
			out.print(craftClone.getFragment2().getEndLine() + "\n");
			out.flush();
			out.close();
			
			cdr = new CloneDetectionReport(cdr_path);
			ur = Experiment.evaluateRecall(cdr, nicad, mb, 0.0, subsumeTolerance, language, System.out);
			
			//OKAY
			if(i <= f3.getStartLine()+tol2) {
				//System.out.println(i);
				assertTrue(ur.getClone() != null);
				assertTrue(ur.getClone().equals(craftClone));
				assertTrue(ur.getToolid() == nicad.getId());
				assertTrue(ur.getBaseid() == mb.getId());
				assertTrue(ur.getRecall() == 1.0);
			
			//NOT OKAY
			} else {
				//System.out.println(i + "-");
				assertTrue(ur.getClone() == null);
				assertTrue(ur.getRecall() == 0.0);
			}
		}
		
		//System.out.println("F2_Endline");
		
	//Endline Cases
		for(int i = f3.getStartLine(); i <= flength2; i++) {
			craftClone = new Clone(f1, new Fragment(f3.getSrcFile(), f3.getStartLine(), i));
			
			Files.deleteIfExists(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			Path cdr_path = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			PrintWriter out = new PrintWriter(cdr_path.toFile());
			out.print(craftClone.getFragment1().getSrcFile() + ",");
			out.print(craftClone.getFragment1().getStartLine() + ",");
			out.print(craftClone.getFragment1().getEndLine() + ",");
			out.print(craftClone.getFragment2().getSrcFile() + ",");
			out.print(craftClone.getFragment2().getStartLine() + ",");
			out.print(craftClone.getFragment2().getEndLine() + "\n");
			out.flush();
			out.close();
			
			cdr = new CloneDetectionReport(cdr_path);
			ur = Experiment.evaluateRecall(cdr, nicad, mb, 0.0, subsumeTolerance, language, System.out);
			
			//NOT OKAY
			if(i >= f3.getEndLine()-tol2) {
				//System.out.println(i);
				assertTrue(ur.getClone() != null);
				assertTrue(ur.getClone().equals(craftClone));
				assertTrue(ur.getToolid() == nicad.getId());
				assertTrue(ur.getBaseid() == mb.getId());
				assertTrue(ur.getRecall() == 1.0);
			//OKAY
			} else {
				//System.out.println(i + "-");
				assertTrue(ur.getClone() == null);
				assertTrue(ur.getRecall() == 0.0);
			}
		}
		
//some error conditions
		boolean caught = false;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/nosuchfile"));
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/errorReport"));
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		} catch (InputMismatchException e) {
			caught = true;
		}
		assertTrue(caught);

		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/report"));

		caught = false;
		try {
			ur = Experiment.evaluateRecall(null, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluateRecall(cdr, null, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, null, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, null);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, mb, -0.1, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		caught = false;
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, mb, 1.01, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		
		caught = false;
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, -0.1, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		caught = false;
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, 1.1, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluateRecall(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, -221321, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}
	
	@Test
	public void testEvaluatePrecision() throws InputMismatchException, FileNotFoundException, IllegalArgumentException, NullPointerException, SQLException, IOException, InterruptedException {

		Fragment f1 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/contrib/TextAreaFigure.java").toAbsolutePath().normalize(), 714, 753); //1020
		Fragment f2 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/contrib/PolygonFigure.java").toAbsolutePath().normalize(), 86, 125); //577
		Fragment f3 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/figures/TextFigure.java").toAbsolutePath().normalize(), 208, 239); //497
		Fragment f4 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/figures/TextFigure.java").toAbsolutePath().normalize(), 100, 150); //497
		Fragment f5 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/samples/pert/PertApplication.java").toAbsolutePath().normalize(),33, 52);
		Fragment f6 = new Fragment(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/src/CH/ifa/draw/samples/pert/PertApplication.java").toAbsolutePath().normalize(),33, 80);

		
		Clone c12 = new Clone(f1,f2);//top (file location)
		Clone c13 = new Clone(f1,f3);//middle
		Clone c23 = new Clone(f2,f3);//bottom
		
		ToolDB nicad = new ToolDB(5, "NiCad", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad").toAbsolutePath().normalize(), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner").toAbsolutePath().normalize());
		
		MutantBaseDB mb_middle = new MutantBaseDB(
				20, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f1,
				f3);
		
		double subsumeTolerance = 0.15;
		double requiredSimilarity = 0.70;
		TokenValidator tokval = new TokenValidator(1.0, 0.0, ExperimentSpecification.JAVA_LANGUAGE);
		LineValidator linval = new LineValidator(1.0, 0.0, ExperimentSpecification.JAVA_LANGUAGE);
		double toksim;
		double linsim;
		UnitPrecision ur;
		
		CloneDetectionReport cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/report2"));
		
	//Test: Location in report file (vary location, first, middle, last), all perfect match + test output values
		MutantBaseDB mb;
		
		//Middle Clone
		mb = mb_middle;
		ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(3.0/5.0, ur.getPrecision(), .001);
		
		//Clone1 (f1,f2)
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1) && ur.getClones().get(0).getFragment2().equals(f2));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f2, subsumeTolerance));
		assertTrue(ur.getClones().get(0).isClone());
		assertTrue(ur.getClones().get(0).isVerifierSuccess());
		toksim = tokval.validate(ur.getClones().get(0)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(0)).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);

		//Clone2 (f1,f3)
		assertTrue(ur.getClones().get(1).getFragment1().equals(f1) && ur.getClones().get(1).getFragment2().equals(f3));
		assertTrue(ur.getClones().get(1).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(1).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(1).isClone());
		assertTrue(ur.getClones().get(1).isVerifierSuccess());
		toksim = tokval.validate(ur.getClones().get(1)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(1)).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);

		//Clone4 (f1,f5) -- insufficient similarity
		assertTrue(ur.getClones().get(2).getFragment1().equals(f1) && ur.getClones().get(2).getFragment2().equals(f5));
		//assertTrue(ur.getClones().get(2).getFragment1().subsumes(f1, subsumeTolerance));
		//assertTrue(ur.getClones().get(2).getFragment2().subsumes(f4, subsumeTolerance));
		assertFalse(ur.getClones().get(2).isClone());
		assertTrue(ur.getClones().get(2).isVerifierSuccess());
		toksim = tokval.validate(ur.getClones().get(2)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(2)).getSimilarity();
		assertFalse(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		
		//Clone5 (f1,f6) -- invalid fragment 2
		assertTrue(ur.getClones().get(3).getFragment1().equals(f1) && ur.getClones().get(3).getFragment2().equals(f6));
		assertTrue(ur.getClones().get(3).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(3).getFragment2().subsumes(f6, subsumeTolerance));
		assertFalse(ur.getClones().get(3).isClone());
		assertFalse(ur.getClones().get(3).isVerifierSuccess());
		
		//Clone3 (f2,f3)
		assertTrue(ur.getClones().get(4).getFragment1().equals(f2) && ur.getClones().get(4).getFragment2().equals(f3));
		assertTrue(ur.getClones().get(4).getFragment1().subsumes(f2, subsumeTolerance));
		assertTrue(ur.getClones().get(4).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(4).isClone());
		assertTrue(ur.getClones().get(4).isVerifierSuccess());
		toksim = tokval.validate(ur.getClones().get(4)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(4)).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		
	//Test Subsume Matcher (ignore similarity)
		mb = new MutantBaseDB(
				10, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f1,
				f5);
		
		//Set #1: f1 match varies, f3 perfect match
		CloneDetectionReport cdr111 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/111"));
		ur = Experiment.evaluatePrecision(cdr111, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		713, 746)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr112 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/112"));
		ur = Experiment.evaluatePrecision(cdr112, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				713, 747)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr113 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/113"));
		ur = Experiment.evaluatePrecision(cdr113, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				713, 750)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr114 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/114"));
		ur = Experiment.evaluatePrecision(cdr114, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				713, 753)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr115 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/115"));
		ur = Experiment.evaluatePrecision(cdr115, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				713, 754)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr121 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/121"));
		ur = Experiment.evaluatePrecision(cdr121, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		714, 746)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr122 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/122"));
		ur = Experiment.evaluatePrecision(cdr122, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				714, 747)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr123 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/123"));
		ur = Experiment.evaluatePrecision(cdr123, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				714, 750)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr124 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/124"));
		ur = Experiment.evaluatePrecision(cdr124, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				714, 753)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr125 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/125"));
		ur = Experiment.evaluatePrecision(cdr125, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				714, 754)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr131 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/131"));
		ur = Experiment.evaluatePrecision(cdr131, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		717, 746)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr132 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/132"));
		ur = Experiment.evaluatePrecision(cdr132, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				717, 747)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr133 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/133"));
		ur = Experiment.evaluatePrecision(cdr133, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				717, 750)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr134 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/134"));
		ur = Experiment.evaluatePrecision(cdr134, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				717, 753)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr135 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/135"));
		ur = Experiment.evaluatePrecision(cdr135, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				717, 754)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr141 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/141"));
		ur = Experiment.evaluatePrecision(cdr141, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		720, 746)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr142 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/142"));
		ur = Experiment.evaluatePrecision(cdr142, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				720, 747)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr143 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/143"));
		ur = Experiment.evaluatePrecision(cdr143, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				720, 750)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr144 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/144"));
		ur = Experiment.evaluatePrecision(cdr144, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				720, 753)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr145 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/145"));
		ur = Experiment.evaluatePrecision(cdr145, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
				720, 754)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr151 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/151"));
		ur = Experiment.evaluatePrecision(cdr151, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 746)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr152 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/152"));
		ur = Experiment.evaluatePrecision(cdr152, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 747)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr153 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/153"));
		ur = Experiment.evaluatePrecision(cdr153, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 750)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr154 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/154"));
		ur = Experiment.evaluatePrecision(cdr154, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 753)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr155 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/155"));
		ur = Experiment.evaluatePrecision(cdr155, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f1.getSrcFile(),
		//		721, 754)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr1sa = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sa"));
		ur = Experiment.evaluatePrecision(cdr1sa, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr1sb = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sb"));
		ur = Experiment.evaluatePrecision(cdr1sb, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr1sc = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sc"));
		ur = Experiment.evaluatePrecision(cdr1sc, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr1sd = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sd"));
		ur = Experiment.evaluatePrecision(cdr1sd, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 1);

		
		//alterative, swap fragments, stillvay the same fragment
		CloneDetectionReport cdr111a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/111a"));
		ur = Experiment.evaluatePrecision(cdr111a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		713, 746)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr112a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/112a"));
		ur = Experiment.evaluatePrecision(cdr112a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				713, 747)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr113a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/113a"));
		ur = Experiment.evaluatePrecision(cdr113a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				713, 750)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr114a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/114a"));
		ur = Experiment.evaluatePrecision(cdr114a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				713, 753)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr115a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/115a"));
		ur = Experiment.evaluatePrecision(cdr115a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				713, 754)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr121a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/121a"));
		ur = Experiment.evaluatePrecision(cdr121a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		714, 746)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr122a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/122a"));
		ur = Experiment.evaluatePrecision(cdr122a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				714, 747)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr123a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/123a"));
		ur = Experiment.evaluatePrecision(cdr123a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				714, 750)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr124a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/124a"));
		ur = Experiment.evaluatePrecision(cdr124a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				714, 753)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr125a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/125a"));
		ur = Experiment.evaluatePrecision(cdr125a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				714, 754)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr131a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/131a"));
		ur = Experiment.evaluatePrecision(cdr131a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		717, 746)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr132a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/132a"));
		ur = Experiment.evaluatePrecision(cdr132a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				717, 747)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr133a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/133a"));
		ur = Experiment.evaluatePrecision(cdr133a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				717, 750)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr134a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/134a"));
		ur = Experiment.evaluatePrecision(cdr134a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				717, 753)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr135a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/135a"));
		ur = Experiment.evaluatePrecision(cdr135a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				717, 754)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr141a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/141a"));
		ur = Experiment.evaluatePrecision(cdr141a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		720, 746)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr142a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/142a"));
		ur = Experiment.evaluatePrecision(cdr142a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				720, 747)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr143a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/143a"));
		ur = Experiment.evaluatePrecision(cdr143a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				720, 750)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr144a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/144a"));
		ur = Experiment.evaluatePrecision(cdr144a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				720, 753)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr145a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/145a"));
		ur = Experiment.evaluatePrecision(cdr145a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
				720, 754)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr151a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/151a"));
		ur = Experiment.evaluatePrecision(cdr151a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 746)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr152a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/152a"));
		ur = Experiment.evaluatePrecision(cdr152a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 747)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr153a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/153a"));
		ur = Experiment.evaluatePrecision(cdr153a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 750)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr154a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/154a"));
		ur = Experiment.evaluatePrecision(cdr154a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 753)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr155a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/155a"));
		ur = Experiment.evaluatePrecision(cdr155a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f1.getSrcFile(),
		//		721, 754)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f3));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr1saa = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1saa"));
		ur = Experiment.evaluatePrecision(cdr1saa, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr1sba = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sba"));
		ur = Experiment.evaluatePrecision(cdr1sba, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr1sca = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sca"));
		ur = Experiment.evaluatePrecision(cdr1sca, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr1sda = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/1sda"));
		ur = Experiment.evaluatePrecision(cdr1sda, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 1);
		
	//Vary fragment 2

		mb = new MutantBaseDB(
				10, 
				Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
				1,
				f5,
				f3);
		
		//Set #1: f1 match varies, f3 perfect match
		CloneDetectionReport cdr211 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/211"));
		ur = Experiment.evaluatePrecision(cdr211, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//System.out.println(ur.getClones());
		assertTrue(ur.getClones().size() == 0);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		207, 234)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));

		CloneDetectionReport cdr212 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/212"));
		ur = Experiment.evaluatePrecision(cdr212, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				207, 235)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr213 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/213"));
		ur = Experiment.evaluatePrecision(cdr213, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				207, 236)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr214 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/214"));
		ur = Experiment.evaluatePrecision(cdr214, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				207, 239)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr215 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/215"));
		ur = Experiment.evaluatePrecision(cdr215, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				207, 240)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr221 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/221"));
		ur = Experiment.evaluatePrecision(cdr221, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		208, 234)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr222 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/222"));
		ur = Experiment.evaluatePrecision(cdr222, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				208, 235)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr223 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/223"));
		ur = Experiment.evaluatePrecision(cdr223, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				208, 236)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr224 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/224"));
		ur = Experiment.evaluatePrecision(cdr224, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				208, 239)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr225 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/225"));
		ur = Experiment.evaluatePrecision(cdr225, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				208, 240)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr231 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/231"));
		ur = Experiment.evaluatePrecision(cdr231, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		210, 234)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr232 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/232"));
		ur = Experiment.evaluatePrecision(cdr232, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				210, 235)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr233 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/233"));
		ur = Experiment.evaluatePrecision(cdr233, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				210, 236)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr234 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/234"));
		ur = Experiment.evaluatePrecision(cdr234, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				210, 239)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr235 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/235"));
		ur = Experiment.evaluatePrecision(cdr235, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				210, 240)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr241 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/241"));
		ur = Experiment.evaluatePrecision(cdr241, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		212, 234)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr242 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/242"));
		ur = Experiment.evaluatePrecision(cdr242, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				212, 235)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr243 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/243"));
		ur = Experiment.evaluatePrecision(cdr243, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				212, 236)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr244 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/244"));
		ur = Experiment.evaluatePrecision(cdr244, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				212, 239)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr245 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/245"));
		ur = Experiment.evaluatePrecision(cdr245, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
				212, 240)));
		assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr251 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/251"));
		ur = Experiment.evaluatePrecision(cdr251, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 234)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr252 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/252"));
		ur = Experiment.evaluatePrecision(cdr252, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 235)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr253 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/253"));
		ur = Experiment.evaluatePrecision(cdr253, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 236)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr254 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/254"));
		ur = Experiment.evaluatePrecision(cdr254, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 239)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr255 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/255"));
		ur = Experiment.evaluatePrecision(cdr255, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment2().equals(new Fragment(f3.getSrcFile(),
		//		213, 240)));
		//assertTrue(ur.getClones().get(0).getFragment1().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr2sa = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sa"));
		ur = Experiment.evaluatePrecision(cdr2sa, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr2sb = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sb"));
		ur = Experiment.evaluatePrecision(cdr2sb, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr2sc = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sc"));
		ur = Experiment.evaluatePrecision(cdr2sc, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr2sd = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sd"));
		ur = Experiment.evaluatePrecision(cdr2sd, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 1);
		
		//alterative, swap fragments, stillvay the same fragment
		CloneDetectionReport cdr211a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/211a"));
		ur = Experiment.evaluatePrecision(cdr211a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		207, 234)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));

		CloneDetectionReport cdr212a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/212a"));
		ur = Experiment.evaluatePrecision(cdr212a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				207, 235)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr213a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/213a"));
		ur = Experiment.evaluatePrecision(cdr213a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				207, 236)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr214a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/214a"));
		ur = Experiment.evaluatePrecision(cdr214a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				207, 239)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr215a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/215a"));
		ur = Experiment.evaluatePrecision(cdr215a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				207, 240)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr221a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/221a"));
		ur = Experiment.evaluatePrecision(cdr221a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		208, 234)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr222a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/222a"));
		ur = Experiment.evaluatePrecision(cdr222a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				208, 235)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr223a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/223a"));
		ur = Experiment.evaluatePrecision(cdr223a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				208, 236)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr224a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/224a"));
		ur = Experiment.evaluatePrecision(cdr224a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				208, 239)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr225a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/225a"));
		ur = Experiment.evaluatePrecision(cdr225a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				208, 240)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr231a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/231a"));
		ur = Experiment.evaluatePrecision(cdr231a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		210, 234)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr232a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/232a"));
		ur = Experiment.evaluatePrecision(cdr232a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				210, 235)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr233a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/233a"));
		ur = Experiment.evaluatePrecision(cdr233a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				210, 236)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr234a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/234a"));
		ur = Experiment.evaluatePrecision(cdr234a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				210, 239)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr235a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/235a"));
		ur = Experiment.evaluatePrecision(cdr235a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				210, 240)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr241a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/241a"));
		ur = Experiment.evaluatePrecision(cdr241a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		212, 234)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr242a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/242a"));
		ur = Experiment.evaluatePrecision(cdr242a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				212, 235)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr243a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/243a"));
		ur = Experiment.evaluatePrecision(cdr243a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				212, 236)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr244a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/244a"));
		ur = Experiment.evaluatePrecision(cdr244a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				212, 239)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr245a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/245a"));
		ur = Experiment.evaluatePrecision(cdr245a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().get(0).getFragment1().subsumes(f3, subsumeTolerance));
		assertTrue(ur.getClones().get(0).getFragment2().subsumes(f1, 0.0));
		assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
				212, 240)));
		assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 1);
		
		CloneDetectionReport cdr251a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/251a"));
		ur = Experiment.evaluatePrecision(cdr251a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 234)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr252a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/252a"));
		ur = Experiment.evaluatePrecision(cdr252a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 235)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr253a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/253a"));
		ur = Experiment.evaluatePrecision(cdr253a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 236)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr254a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/254a"));
		ur = Experiment.evaluatePrecision(cdr254a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 239)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr255a = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/255a"));
		ur = Experiment.evaluatePrecision(cdr255a, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		//assertTrue(ur.getClones().get(0).getFragment1().equals(new Fragment(f3.getSrcFile(),
		//		213, 240)));
		//assertTrue(ur.getClones().get(0).getFragment2().equals(f1));
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr2saa = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2saa"));
		ur = Experiment.evaluatePrecision(cdr2saa, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr2sba = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sba"));
		ur = Experiment.evaluatePrecision(cdr2sba, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr2sca = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sca"));
		ur = Experiment.evaluatePrecision(cdr2sca, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(1.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 0);
		
		CloneDetectionReport cdr2sda = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/2sda"));
		ur = Experiment.evaluatePrecision(cdr2sda, nicad, mb, 0, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(0.0, ur.getPrecision(), 0.001);
		assertTrue(ur.getClones().size() == 1);
		
	// Test Similarity
		mb = mb_middle;
		requiredSimilarity = 0.58;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim1"));
		ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getPrecision(), .001);
		assertTrue( (ur.getClones().get(0).getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClones().get(0).getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClones().get(0).getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClones().get(0).getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClones().get(0)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(0)).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		//System.out.println(toksim + " " + linsim + " " + ur.getClones().get(0));
		
		mb = mb_middle;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim2"));
		ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getPrecision(), .001);
		assertTrue( (ur.getClones().get(0).getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClones().get(0).getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClones().get(0).getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClones().get(0).getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClones().get(0)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(0)).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		//System.out.println(toksim + " " + linsim + " " + ur.getClones().get(0));
		
		mb = mb_middle;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim3"));
		ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getPrecision(), .001);
		assertTrue( (ur.getClones().get(0).getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClones().get(0).getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClones().get(0).getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClones().get(0).getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClones().get(0)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(0)).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		//System.out.println(toksim + " " + linsim + " " + ur.getClones().get(0));
		
		mb = mb_middle;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim4"));
		ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(1.0, ur.getPrecision(), .001);
		assertTrue( (ur.getClones().get(0).getFragment1().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClones().get(0).getFragment2().subsumes(mb.getMutantFragment(), subsumeTolerance)) || (ur.getClones().get(0).getFragment2().subsumes(mb.getOriginalFragment(), subsumeTolerance) && ur.getClones().get(0).getFragment1().subsumes(mb.getMutantFragment(),subsumeTolerance)));
		toksim = tokval.validate(ur.getClones().get(0)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(0)).getSimilarity();
		assertTrue(Math.max(toksim, linsim) >= requiredSimilarity && Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);		
		mb = mb_middle;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/sim5"));
		ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		assertEquals(nicad.getId(), ur.getToolid()) ;
		assertEquals(mb.getId(), ur.getBaseid());
		assertEquals(0.0, ur.getPrecision(), .001);
		assertTrue(ur.getClones().size() == 1);
		toksim = tokval.validate(ur.getClones().get(0)).getSimilarity();
		linsim = linval.validate(ur.getClones().get(0)).getSimilarity();
		assertTrue(Math.max(toksim, linsim) <= 1.0 && Math.max(toksim, linsim) >= 0.0);
		assertFalse(Math.max(toksim, linsim) >= requiredSimilarity);

//Automatic Check (check fairly full range of values, i.e. all those that at least intersect the fragment)
		Clone craftClone;
		VerifiedClone vcraftClone;
		int length1 = f1.getEndLine() - f1.getStartLine() + 1;
		int length2 = f3.getEndLine() - f3.getStartLine() + 1;
		int flength1 = FileUtil.countLines(f1.getSrcFile());
		int flength2 = FileUtil.countLines(f3.getSrcFile());
		double tolerance = 0.15;
		int tol1 = (int) (length1*tolerance);
		int tol2 = (int) (length2*tolerance);
		//System.out.println(tol1 + " " + tol2);
		int language = ExperimentSpecification.JAVA_LANGUAGE;
		subsumeTolerance = 0.15;
		mb = new MutantBaseDB(
						14, 
						Paths.get("testdata/ExperimentTest/EvaluateRecallTest/JHotDraw54b1/"),
						1,
						f1,
						f3);
				
//Fragment1
	//Startline Cases
		//System.out.println("F1_StartLine");
		for(int i = 1; i <= f1.getEndLine(); i++) {
			craftClone = new Clone(new Fragment(f1.getSrcFile(), i, f1.getEndLine()), f2);
			vcraftClone = new VerifiedClone(craftClone.getFragment1(), craftClone.getFragment2(), true, true);
			
			Files.deleteIfExists(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			Path cdr_path = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			PrintWriter out = new PrintWriter(cdr_path.toFile());
			out.print(craftClone.getFragment1().getSrcFile() + ",");
			out.print(craftClone.getFragment1().getStartLine() + ",");
			out.print(craftClone.getFragment1().getEndLine() + ",");
			out.print(craftClone.getFragment2().getSrcFile() + ",");
			out.print(craftClone.getFragment2().getStartLine() + ",");
			out.print(craftClone.getFragment2().getEndLine() + "\n");
			out.flush();
			out.close();
			
			cdr = new CloneDetectionReport(cdr_path);
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, 0.0, subsumeTolerance, language, System.out);
			
			//System.out.println(i);
			
			//OKAY
			if(i <= f1.getStartLine()+tol1) {
				//System.out.println(i);
				assertTrue(ur.getClones().size() == 1);
				assertTrue(ur.getClones().get(0).equals(vcraftClone));
				assertTrue(ur.getToolid() == nicad.getId());
				assertTrue(ur.getBaseid() == mb.getId());
				assertTrue(ur.getPrecision() == 1.0);
			
			//NOT OKAY
			} else {
				//System.out.println(i + "-");
				assertTrue(ur.getClones().size() == 0);
				assertTrue(ur.getPrecision() == 1.0);
			}
		}
		
	//Endline Cases
		//System.out.println("F1_EndLine");
		for(int i = f1.getStartLine(); i <= flength1; i++) {
			craftClone = new Clone(new Fragment(f1.getSrcFile(), f1.getStartLine(), i), f2);
			vcraftClone = new VerifiedClone(craftClone.getFragment1(), craftClone.getFragment2(), true, true);
			
			Files.deleteIfExists(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			Path cdr_path = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			PrintWriter out = new PrintWriter(cdr_path.toFile());
			out.print(craftClone.getFragment1().getSrcFile() + ",");
			out.print(craftClone.getFragment1().getStartLine() + ",");
			out.print(craftClone.getFragment1().getEndLine() + ",");
			out.print(craftClone.getFragment2().getSrcFile() + ",");
			out.print(craftClone.getFragment2().getStartLine() + ",");
			out.print(craftClone.getFragment2().getEndLine() + "\n");
			out.flush();
			out.close();
			
			cdr = new CloneDetectionReport(cdr_path);
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, 0.0, subsumeTolerance, language, System.out);
			
			//System.out.println(i);
			
			//NOT OKAY
			if(i >= f1.getEndLine()-tol1) {
				//System.out.println(i);
				assertTrue(ur.getClones().size() == 1);
				assertTrue(ur.getClones().get(0).equals(vcraftClone));
				assertTrue(ur.getToolid() == nicad.getId());
				assertTrue(ur.getBaseid() == mb.getId());
				assertTrue(ur.getPrecision() == 1.0);
			//OKAY
			} else {
				//System.out.println(i + "-");
				assertTrue(ur.getClones().size() == 0);
				assertTrue(ur.getPrecision() == 1.0);
			}
		}
	
		
//Fragment2
	//Startline Cases
		//System.out.println("F2_Startline");
		for(int i = 1; i <= f3.getEndLine(); i++) {
			craftClone = new Clone(f2, new Fragment(f3.getSrcFile(), i, f3.getEndLine()));
			vcraftClone = new VerifiedClone(craftClone.getFragment1(), craftClone.getFragment2(), true, true);
			
			Files.deleteIfExists(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			Path cdr_path = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			PrintWriter out = new PrintWriter(cdr_path.toFile());
			out.print(craftClone.getFragment1().getSrcFile() + ",");
			out.print(craftClone.getFragment1().getStartLine() + ",");
			out.print(craftClone.getFragment1().getEndLine() + ",");
			out.print(craftClone.getFragment2().getSrcFile() + ",");
			out.print(craftClone.getFragment2().getStartLine() + ",");
			out.print(craftClone.getFragment2().getEndLine() + "\n");
			out.flush();
			out.close();
			
			cdr = new CloneDetectionReport(cdr_path);
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, 0.0, subsumeTolerance, language, System.out);
			
			//System.out.println(i);
			
			//OKAY
			if(i <= f3.getStartLine()+tol2) {
				//S
				assertTrue(ur.getClones().size() == 1);
				assertTrue(ur.getClones().get(0).equals(vcraftClone));
				assertTrue(ur.getToolid() == nicad.getId());
				assertTrue(ur.getBaseid() == mb.getId());
				assertTrue(ur.getPrecision() == 1.0);
			
			//NOT OKAY
			} else {
				//System.out.println(i + "-");
				assertTrue(ur.getClones().size() == 0);
				assertTrue(ur.getPrecision() == 1.0);
			}
		}
		
		//System.out.println("F2_Endline");
		
	//Endline Cases
		for(int i = f3.getStartLine(); i <= flength2; i++) {
			craftClone = new Clone(f2, new Fragment(f3.getSrcFile(), f3.getStartLine(), i));
			vcraftClone = new VerifiedClone(craftClone.getFragment1(), craftClone.getFragment2(), true, true);
			
			Files.deleteIfExists(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			Path cdr_path = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/auto_check"));
			PrintWriter out = new PrintWriter(cdr_path.toFile());
			out.print(craftClone.getFragment1().getSrcFile() + ",");
			out.print(craftClone.getFragment1().getStartLine() + ",");
			out.print(craftClone.getFragment1().getEndLine() + ",");
			out.print(craftClone.getFragment2().getSrcFile() + ",");
			out.print(craftClone.getFragment2().getStartLine() + ",");
			out.print(craftClone.getFragment2().getEndLine() + "\n");
			out.flush();
			out.close();
			
			cdr = new CloneDetectionReport(cdr_path);
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, 0.0, subsumeTolerance, language, System.out);
			
			//System.out.println(i);
			
			//NOT OKAY
			if(i >= f3.getEndLine()-tol2) {
				//
				assertTrue(ur.getClones().size() == 1);
				assertTrue(ur.getClones().get(0).equals(vcraftClone));
				assertTrue(ur.getToolid() == nicad.getId());
				assertTrue(ur.getBaseid() == mb.getId());
				assertTrue(ur.getPrecision() == 1.0);
			//OKAY
			} else {
				//System.out.println(i + "-");
				assertTrue(ur.getClones().size() == 0);
				assertTrue(ur.getPrecision() == 1.0);
			}
		}
		
//Error Conditions		
		boolean caught = false;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/nosuchfile"));
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/errorReport"));
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		} catch (InputMismatchException e) {
			caught = true;
		}
		assertTrue(caught);

		cdr = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/EvaluateRecallTest/report"));

		caught = false;
		try {
			ur = Experiment.evaluatePrecision(null, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluatePrecision(cdr, null, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, null, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, null);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, -0.1, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		caught = false;
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, 1.01, subsumeTolerance, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		
		caught = false;
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, -0.1, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		caught = false;
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, 1.1, ExperimentSpecification.JAVA_LANGUAGE, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			ur = Experiment.evaluatePrecision(cdr, nicad, mb, requiredSimilarity, subsumeTolerance, -221321, System.out);

		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}
	
	@Test
	public void testDetection() throws IllegalStateException, SQLException, IOException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException, FileSanetizationFailedException, NullPointerException, InvalidToolRunnerException {

//Temporary Manual Interface
//CREATION
		//Setup Experiment Specification
		Path jrepository = Paths.get("data/systems/java/");
		Path jsystem = Paths.get("data/systems/java/");
		Path data = Paths.get("testdata/ExperimentTest/DetectionTest/SampleExperimentRun/");
		if(Files.exists(data)) {
			FileUtils.deleteDirectory(data.toFile());
		}
		ExperimentSpecification es = new ExperimentSpecification(data, jsystem, jrepository, ExperimentSpecification.JAVA_LANGUAGE);
		es.setAllowedFragmentDifference(0.30);
		int maxSizeLines = 200; es.setFragmentMaxSizeLines(maxSizeLines);
		int maxSizeTokens = 3000; es.setFragmentMaxSizeTokens(maxSizeTokens);
		int minSizeLines = 15; es.setFragmentMinSizeLines(minSizeLines);
		int minSizeTokens = 50; es.setFragmentMinSizeTokens(minSizeTokens);
		es.setFragmentType(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE);
		//es.setGenerationType(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE);
		es.setInjectNumber(1);
		es.setMaxFragments(3);
		es.setMutationAttempts(25);
		es.setMutationContainment(0.15);
		es.setOperatorAttempts(10);
		es.setPrecisionRequiredSimilarity(0.50);
		es.setRecallRequiredSimilarity(0.50);
		es.setSubsumeMatcherTolerance(0.15);
		
		//Create Experiment
		Experiment e = Experiment.createAutomaticExperiment(es, System.out);
				
//SETUP PHASE
		//Operators
		OperatorDB o;
		List<OperatorDB> operators = new LinkedList<OperatorDB>();
		o=e.addOperator("id", "Change in comments: comment added between two tokens..", 1, Paths.get("operators/mCC_BT")); operators.add(o);
		o=e.addOperator("id", "Change in comments: comment added at end of a line.", 1, Paths.get("operators/mCC_EOL")); operators.add(o);
		o=e.addOperator("id", "Change in formatting: a newline is added between two tokens..", 1, Paths.get("operators/mCF_A")); operators.add(o);
		//o=e.addOperator("id", "Change in formatting: a newline is removed (without changing meangin).", 1, Paths.get("operators/mCF_R")); operators.add(o);
		//o=e.addOperator("id", "Change in whitespace: a space or tab is added between two tokens.", 1, Paths.get("operators/mCW_A")); operators.add(o);
		//o=e.addOperator("id", "Change in whitespace: a space or tab is removed (without changing meaning).", 1, Paths.get("operators/mCW_R")); operators.add(o);
		//o=e.addOperator("id", "Renaming of identifier: systamtic renaming of all instances of a chosen identifier.", 2, Paths.get("operators/mSRI")); operators.add(o);
		//o=e.addOperator("id", "Renaming of identifier: arbitrary renaming of a single identifier instance.", 2, Paths.get("operators/mARI")); operators.add(o);
		//o=e.addOperator("id", "Change in literal value: a number value is replaced.", 2, Paths.get("operators/mRL_N")); operators.add(o);
		//o=e.addOperator("id", "Change in literal value: a string value is replaced.", 2, Paths.get("operators/mRL_S")); operators.add(o); //slow
		//o=e.addOperator("id", "Deletion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mDL")); operators.add(o);
		//o=e.addOperator("id", "Insertion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mIL")); operators.add(o);
		//o=e.addOperator("id", "Modification of a whole line ((without invalidating syntax).", 3, Paths.get("operators/mML")); operators.add(o);
		//o=e.addOperator("id", "Small deletion within a line (removal of a parameter).", 3, Paths.get("operators/mSDL")); operators.add(o);
		//o=e.addOperator("id", "Small insertion within a line (addition of a parameter).", 3, Paths.get("operators/mSIL")); operators.add(o);
		
		
		//Mutators
		List<MutatorDB> mutators = new LinkedList<MutatorDB>();
		for(OperatorDB operator : operators) {
			LinkedList<Integer> oplist = new LinkedList<Integer>();
			oplist.add(operator.getId());
			MutatorDB m = e.addMutator(operator.getDescription(), oplist);
			mutators.add(m);
		}

//GENERATION PHASE
		e.generate();
		
//EVALUATION PHASE
		ToolDB nicad1 = e.addTool("NiCad1", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));
		ToolDB nicad2 = e.addTool("NiCad2", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));
		ToolDB nicad3 = e.addTool("NiCad3", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));

		e.evaluateTools(true);

//Redo Detection (two ways agree, then tested? prob not make same mistake twice)
		if(Files.exists(Paths.get("testdata/ExperimentTest/DetectionTest/check/"))) {
			FileUtils.deleteDirectory(Paths.get("testdata/ExperimentTest/DetectionTest/check/").toFile());
		}
		Files.createDirectory(Paths.get("testdata/ExperimentTest/DetectionTest/check/"));
		ExperimentData ed = e.getExperimentData();
		for(ToolDB tool : ed.getTools()) {
			for(MutantBaseDB mb : ed.getMutantBases()) {
				ed.constructBase(mb.getId());
				CloneDetectionReport cdr = tool.runTool(mb.getDirectory(), ed.getLanguage(), ed.getFragmentType(), ed.getFragmentMinimumSizeLines(), ed.getFragmentMinimumSizeTokens(), ed.getFragmentMaximumSizeLines(), ed.getFragmentMaximumSizeTokens(), ed.getAllowedFragmentDifference());
				Files.copy(cdr.getReport(), Paths.get("testdata/ExperimentTest/DetectionTest/check/" + tool.getId() + "-" + mb.getId()));
			}
		}
		
//CHECK
		for(ToolDB tool : e.getExperimentData().getTools()) {
			for(MutantBaseDB mb: e.getExperimentData().getMutantBases()) {
				List<Clone> check = new LinkedList<Clone>();
				List<Clone> clones = new LinkedList<Clone>();
				Clone clone;
				
				CloneDetectionReport cdr1 = ed.getCloneDetectionReport(tool.getId(), mb.getId());
				cdr1.open();
				clone = cdr1.next();
				while(clone != null) {
					clones.add(clone);
					clone = cdr1.next();
				}
				cdr1.close();
				
				CloneDetectionReport cdr2 = new CloneDetectionReport(Paths.get("testdata/ExperimentTest/DetectionTest/check/" + tool.getId() + "-" + mb.getId()));
				cdr2.open();
				clone = cdr2.next();
				while(clone != null) {
					check.add(clone);
					clone = cdr2.next();
				}
				cdr2.close();
				
				for(Clone c :clones) {
					assertTrue(check.contains(c));
				}
				for(Clone c : check) {
					assertTrue(clones.contains(c));
				}
				
				assertTrue(FileUtils.contentEquals(cdr1.getReport().toFile(), cdr2.getReport().toFile()));
			}
		}
	}
	
	@Test
	public void testEvaluateTools_UnitRecall() throws IllegalArgumentException, SQLException, IOException, InterruptedException, ArtisticStyleFailedException, FileSanetizationFailedException {
		//Experiment e = Experiment.loadExperiment(Paths.get("testdata/ExperimentTest/EvaluateTools_UnitRecall/SampleExperiment/"), System.out);
//Temporary Manual Interface
//CREATION
		//Setup Experiment Specification
		Path jrepository = Paths.get("data/systems/java/");
		Path jsystem = Paths.get("data/systems/java/");
		Path data = Paths.get("testdata/ExperimentTest/EvaluateTools_UnitRecall/SampleExperiment/");
		if(Files.exists(data)) {
			FileUtils.deleteDirectory(data.toFile());
		}
		ExperimentSpecification es = new ExperimentSpecification(data, jsystem, jrepository, ExperimentSpecification.JAVA_LANGUAGE);
		es.setAllowedFragmentDifference(0.30);
		int maxSizeLines = 200; es.setFragmentMaxSizeLines(maxSizeLines);
		int maxSizeTokens = 3000; es.setFragmentMaxSizeTokens(maxSizeTokens);
		int minSizeLines = 15; es.setFragmentMinSizeLines(minSizeLines);
		int minSizeTokens = 50; es.setFragmentMinSizeTokens(minSizeTokens);
		es.setFragmentType(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE);
		//es.setGenerationType(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE);
		es.setInjectNumber(1);
		es.setMaxFragments(3);
		es.setMutationAttempts(25);
		es.setMutationContainment(0.15);
		es.setOperatorAttempts(10);
		es.setPrecisionRequiredSimilarity(0.50);
		es.setRecallRequiredSimilarity(0.50);
		es.setSubsumeMatcherTolerance(0.15);
		
		//Create Experiment
		Experiment e = Experiment.createAutomaticExperiment(es, System.out);
				
//SETUP PHASE
		//Operators
		OperatorDB o;
		List<OperatorDB> operators = new LinkedList<OperatorDB>();
		o=e.addOperator("id", "Change in comments: comment added between two tokens..", 1, Paths.get("operators/mCC_BT")); operators.add(o);
		o=e.addOperator("id", "Change in comments: comment added at end of a line.", 1, Paths.get("operators/mCC_EOL")); operators.add(o);
		o=e.addOperator("id", "Change in formatting: a newline is added between two tokens..", 1, Paths.get("operators/mCF_A")); operators.add(o);
		//o=e.addOperator("id", "Change in formatting: a newline is removed (without changing meangin).", 1, Paths.get("operators/mCF_R")); operators.add(o);
		//o=e.addOperator("id", "Change in whitespace: a space or tab is added between two tokens.", 1, Paths.get("operators/mCW_A")); operators.add(o);
		//o=e.addOperator("id", "Change in whitespace: a space or tab is removed (without changing meaning).", 1, Paths.get("operators/mCW_R")); operators.add(o);
		//o=e.addOperator("id", "Renaming of identifier: systamtic renaming of all instances of a chosen identifier.", 2, Paths.get("operators/mSRI")); operators.add(o);
		//o=e.addOperator("id", "Renaming of identifier: arbitrary renaming of a single identifier instance.", 2, Paths.get("operators/mARI")); operators.add(o);
		//o=e.addOperator("id", "Change in literal value: a number value is replaced.", 2, Paths.get("operators/mRL_N")); operators.add(o);
		//o=e.addOperator("id", "Change in literal value: a string value is replaced.", 2, Paths.get("operators/mRL_S")); operators.add(o); //slow
		//o=e.addOperator("id", "Deletion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mDL")); operators.add(o);
		//o=e.addOperator("id", "Insertion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mIL")); operators.add(o);
		//o=e.addOperator("id", "Modification of a whole line ((without invalidating syntax).", 3, Paths.get("operators/mML")); operators.add(o);
		//o=e.addOperator("id", "Small deletion within a line (removal of a parameter).", 3, Paths.get("operators/mSDL")); operators.add(o);
		//o=e.addOperator("id", "Small insertion within a line (addition of a parameter).", 3, Paths.get("operators/mSIL")); operators.add(o);
		
		
		//Mutators
		List<MutatorDB> mutators = new LinkedList<MutatorDB>();
		for(OperatorDB operator : operators) {
			LinkedList<Integer> oplist = new LinkedList<Integer>();
			oplist.add(operator.getId());
			MutatorDB m = e.addMutator(operator.getDescription(), oplist);
			mutators.add(m);
		}

//GENERATION PHASE
		e.generate();
		
//EVALUATION PHASE
		ToolDB nicad1 = e.addTool("NiCad1", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));
		ToolDB nicad2 = e.addTool("NiCad2", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));
		ToolDB nicad3 = e.addTool("NiCad3", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));

		e.evaluateTools(false);
		
// ------- TEST
		List<ToolDB> tools = e.getExperimentData().getTools();
		
// Create New Reports
		
		List<List<Double>> recalls = new LinkedList<List<Double>>();
		
		Random rdm = new Random();
		double chance = 0.50;
		
		if(Files.exists(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/"))) {
			FileUtils.deleteDirectory(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/").toFile());
		}
		Files.createDirectories(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/"));
		
		int i = -1;
		for(ToolDB tool : tools) {
			i++;
			recalls.add(new LinkedList<Double>());
			for(MutantBaseDB mbdb : e.getExperimentData().getMutantBases()) {
				CloneDetectionReport cdr = e.getExperimentData().getCloneDetectionReport(tool.getId(), mbdb.getId());
				UnitRecall ur = e.getExperimentData().getUnitRecall(tool.getId(), mbdb.getId());
				
				
				
				Path cdrnew = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/" + tool.getId() + "-" + mbdb.getId()));
				
				boolean doFind; //if in new report, if tool should have found it
				if(rdm.nextDouble() < chance) {
					doFind = true;
					recalls.get(i).add(1.0);
					System.out.println(tool.getId() + " " + mbdb.getId() + " 1");
				} else {
					doFind = false;
					recalls.get(i).add(0.0);
					System.out.println(tool.getId() + " " + mbdb.getId() + " 0");
				}
				cdr.open();
				Clone clone;
				PrintWriter cdrnew_out = new PrintWriter(cdrnew.toFile());
				

				while((clone = cdr.next()) != null) {
					if(ur.getClone().equals(clone)) {
						continue;
					} else {
						cdrnew_out.print(clone.getFragment1().getSrcFile() + ",");
						cdrnew_out.print(clone.getFragment1().getStartLine() + ",");
						cdrnew_out.print(clone.getFragment1().getEndLine() + ",");
						cdrnew_out.print(clone.getFragment2().getSrcFile() + ",");
						cdrnew_out.print(clone.getFragment2().getStartLine() + ",");
						cdrnew_out.print(clone.getFragment2().getEndLine() + "\n");
					}
				}
				
				//if decided to find it, add it to the report (at end? this is okay we tested search previously)
				if(doFind) {
					cdrnew_out.print(mbdb.getOriginalFragment().getSrcFile().toAbsolutePath().normalize() + ",");
					cdrnew_out.print(mbdb.getOriginalFragment().getStartLine() + ",");
					cdrnew_out.print(mbdb.getOriginalFragment().getEndLine() + ",");
					
					cdrnew_out.print(mbdb.getMutantFragment().getSrcFile().toAbsolutePath().normalize() + ",");
					cdrnew_out.print(mbdb.getMutantFragment().getStartLine() + ",");
					cdrnew_out.print(mbdb.getMutantFragment().getEndLine() + "\n");
				}
				cdrnew_out.flush();
				cdrnew_out.close();
				cdr.close();
			}
			chance += 0.20;
		}
		
//Replace Reports
		FileUtils.deleteDirectory(e.getExperimentData().getReportsPath().toFile());
		FileUtils.copyDirectory(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/").toFile(), e.getExperimentData().getReportsPath().toFile());
		
//Redetect
		e.evaluateTools(true);
		
//Check
		i = -1;
		int j = -1;
		for(ToolDB t : tools) {
			i++;
			j = -1;
			for(MutantBaseDB mb : e.getExperimentData().getMutantBases()) {
				j++;
				assertTrue(recalls.get(i).get(j).equals(e.getExperimentData().getUnitRecall(t.getId(), mb.getId()).getRecall()));
			}
		}
	}
	
	@Test
	public void testEvaluateTools_UnitPrecision() throws SQLException, IOException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException, FileSanetizationFailedException {
		Experiment e = Experiment.loadExperiment(Paths.get("testdata/ExperimentTest/EvaluateTools_UnitRecall/SampleExperiment/"), System.out);
/*
//Temporary Manual Interface
//CREATION
		//Setup Experiment Specification
		Path jrepository = Paths.get("data/systems/java/");
		Path jsystem = Paths.get("data/systems/java/");
		Path data = Paths.get("testdata/ExperimentTest/EvaluateTools_UnitRecall/SampleExperiment/");
		if(Files.exists(data)) {
			FileUtils.deleteDirectory(data.toFile());
		}
		ExperimentSpecification es = new ExperimentSpecification(data, jsystem, jrepository, ExperimentSpecification.JAVA_LANGUAGE);
		es.setAllowedFragmentDifference(0.30);
		int maxSizeLines = 200; es.setFragmentMaxSizeLines(maxSizeLines);
		int maxSizeTokens = 3000; es.setFragmentMaxSizeTokens(maxSizeTokens);
		int minSizeLines = 15; es.setFragmentMinSizeLines(minSizeLines);
		int minSizeTokens = 50; es.setFragmentMinSizeTokens(minSizeTokens);
		es.setFragmentType(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE);
		es.setGenerationType(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE);
		es.setInjectNumber(1);
		es.setMaxFragments(3);
		es.setMutationAttempts(25);
		es.setMutationContainment(0.15);
		es.setOperatorAttempts(10);
		es.setPrecisionRequiredSimilarity(0.50);
		es.setRecallRequiredSimilarity(0.50);
		es.setSubsumeMatcherTolerance(0.15);
		
		//Create Experiment
		Experiment e = Experiment.createExperiment(es, System.out);
				
//SETUP PHASE
		//Operators
		OperatorDB o;
		List<OperatorDB> operators = new LinkedList<OperatorDB>();
		o=e.addOperator("id", "Change in comments: comment added between two tokens..", 1, Paths.get("operators/mCC_BT")); operators.add(o);
		o=e.addOperator("id", "Change in comments: comment added at end of a line.", 1, Paths.get("operators/mCC_EOL")); operators.add(o);
		o=e.addOperator("id", "Change in formatting: a newline is added between two tokens..", 1, Paths.get("operators/mCF_A")); operators.add(o);
		//o=e.addOperator("id", "Change in formatting: a newline is removed (without changing meangin).", 1, Paths.get("operators/mCF_R")); operators.add(o);
		//o=e.addOperator("id", "Change in whitespace: a space or tab is added between two tokens.", 1, Paths.get("operators/mCW_A")); operators.add(o);
		//o=e.addOperator("id", "Change in whitespace: a space or tab is removed (without changing meaning).", 1, Paths.get("operators/mCW_R")); operators.add(o);
		//o=e.addOperator("id", "Renaming of identifier: systamtic renaming of all instances of a chosen identifier.", 2, Paths.get("operators/mSRI")); operators.add(o);
		//o=e.addOperator("id", "Renaming of identifier: arbitrary renaming of a single identifier instance.", 2, Paths.get("operators/mARI")); operators.add(o);
		//o=e.addOperator("id", "Change in literal value: a number value is replaced.", 2, Paths.get("operators/mRL_N")); operators.add(o);
		//o=e.addOperator("id", "Change in literal value: a string value is replaced.", 2, Paths.get("operators/mRL_S")); operators.add(o); //slow
		//o=e.addOperator("id", "Deletion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mDL")); operators.add(o);
		//o=e.addOperator("id", "Insertion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mIL")); operators.add(o);
		//o=e.addOperator("id", "Modification of a whole line ((without invalidating syntax).", 3, Paths.get("operators/mML")); operators.add(o);
		//o=e.addOperator("id", "Small deletion within a line (removal of a parameter).", 3, Paths.get("operators/mSDL")); operators.add(o);
		//o=e.addOperator("id", "Small insertion within a line (addition of a parameter).", 3, Paths.get("operators/mSIL")); operators.add(o);
		
		
		//Mutators
		List<MutatorDB> mutators = new LinkedList<MutatorDB>();
		for(OperatorDB operator : operators) {
			LinkedList<Integer> oplist = new LinkedList<Integer>();
			oplist.add(operator.getId());
			MutatorDB m = e.addMutator(operator.getDescription(), oplist);
			mutators.add(m);
		}

//GENERATION PHASE
		e.generate();
		
//EVALUATION PHASE
		ToolDB nicad1 = e.addTool("NiCad1", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));
		ToolDB nicad2 = e.addTool("NiCad2", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));
		ToolDB nicad3 = e.addTool("NiCad3", "NiCad Clone Detector", Paths.get("testdata/ExperimentTest/NiCad"), Paths.get("testdata/ExperimentTest/NiCadRunner/NiCadRunner"));

		e.evaluateTools(false);
*/
// ------- TEST
		List<ToolDB> tools = e.getExperimentData().getTools();
		
// Create New Reports

List<List<Double>> precisions = new LinkedList<List<Double>>();
		
		Random rdm = new Random();
		
		if(Files.exists(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/"))) {
			FileUtils.deleteDirectory(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/").toFile());
		}
		Files.createDirectories(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/"));
		
		int i = -1;
		for(ToolDB tool : tools) {
			i++;
			precisions.add(new LinkedList<Double>());
			
			for(MutantBaseDB mbdb : e.getExperimentData().getMutantBases()) {
				CloneDetectionReport cdr = e.getExperimentData().getCloneDetectionReport(tool.getId(), mbdb.getId());
				Clone theclone = new Clone(mbdb.getOriginalFragment(), mbdb.getMutantFragment());
				
				//Where to put this report
				Path cdrnew = Files.createFile(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/" + tool.getId() + "-" + mbdb.getId()));
				
				//Build list of decoy clones (those which wont be involved in calculation
				cdr.open();
				List<Clone> decoys = new LinkedList<Clone>();
				Clone clone;
				double tolerance = e.getExperimentData().getSubsumeMatcherTolerance();
				while((clone = cdr.next()) != null) {
					if(clone.getFragment1().subsumes(theclone.getFragment1(), tolerance) ||
							clone.getFragment2().subsumes(theclone.getFragment1(), tolerance) ||
							clone.getFragment1().subsumes(theclone.getFragment2(), tolerance) ||
							clone.getFragment2().subsumes(theclone.getFragment2(), tolerance)) {
						continue;
					} else {
						decoys.add(clone);
					}
				}
				cdr.close();
				
				//Build new report (also track)
				List<Clone> additions = new LinkedList<Clone>();
				Clone addclone;
				int numyes = 0;
				int numno = 0;
				
				Fragment fragment1 = theclone.getFragment1();
				Fragment fragment2 = theclone.getFragment2();
				int length1 = theclone.getFragment1().getEndLine() - theclone.getFragment1().getStartLine() + 1;
				int length2 = theclone.getFragment2().getEndLine() - theclone.getFragment2().getStartLine() + 1;
				int tol1 = (int) (length1 * tolerance);
				int tol2 = (int) (length2 * tolerance);
				
				numyes = rdm.nextInt(10)+1;
				numno = rdm.nextInt(10)+1;
				
				for(int k = 0; k < numyes; k++) {
					additions.add(theclone);
				}
				for(int k = 0; k < numno; k++) {
					additions.add(new Clone(theclone.getFragment1(), new Fragment(fragment1.getSrcFile(), 1, fragment1.getStartLine())));
				}
				
				assertTrue(additions.size() == (numyes+numno));
				
				precisions.get(i).add(((double)numyes)/((double)numyes+numno));
				
				System.out.println(tool.getId() + " " + mbdb.getId() + " " + ((double)numyes)/((double)numyes+numno));
				
				PrintWriter out = new PrintWriter(cdrnew.toFile());
				for(Clone c : additions) {
					out.print(c.getFragment1().getSrcFile() + ",");
					out.print(c.getFragment1().getStartLine() + ",");
					out.print(c.getFragment1().getEndLine() + ",");
					out.print(c.getFragment2().getSrcFile() + ",");
					out.print(c.getFragment2().getStartLine() + ",");
					out.print(c.getFragment2().getEndLine() + "\n");
				}
				out.flush();
				out.close();
			}
		}
		
//Replace Reports
		FileUtils.deleteDirectory(e.getExperimentData().getReportsPath().toFile());
		FileUtils.copyDirectory(Paths.get("testdata/ExperimentTest/EvaluateTools_RecallTest/replace/").toFile(), e.getExperimentData().getReportsPath().toFile());
				
//Redetect
		e.evaluateTools(true);
		
//Check
		i = -1;
		int j = -1;
		for(ToolDB t : tools) {
			i++;
			j = -1;
			for(MutantBaseDB mb : e.getExperimentData().getMutantBases()) {
				j++;
				assertTrue(precisions.get(i).get(j).equals(e.getExperimentData().getUnitPrecision(t.getId(), mb.getId()).getPrecision()));
			}
		}
	}
}
