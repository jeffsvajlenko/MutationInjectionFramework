package experiment;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import main.ArtisticStyleFailedException;
import main.FileSanetizationFailedException;
import models.Clone;
import models.Fragment;
import models.VerifiedClone;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import util.FileUtil;
import util.FragmentUtil;
import util.SystemUtil;

public class ExperimentDataTest {

	@Test
	public void testCreateNew() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
		//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
		
		//Create
		ExperimentData ed = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
		//check stage
		assertEquals(ExperimentData.GENERATION_SETUP_STAGE, ed.getCurrentStage());
		
		//Check layout correct
		Files.isDirectory(exprdata.resolve("system"));
		Files.isDirectory(exprdata.resolve("repository"));
		Files.isDirectory(exprdata.resolve("fragments"));
		Files.isDirectory(exprdata.resolve("mutantfragments"));
		Files.isDirectory(exprdata.resolve("reports"));
		Files.isDirectory(exprdata.resolve("mutantbase"));
		Files.isDirectory(exprdata.resolve("temp"));
		Files.isRegularFile(exprdata.resolve("database"));
		
		//Check System copied properly
		List<Path> osystem_files = FileUtil.fileInventory(system);
		List<Path> dsystem_files = FileUtil.fileInventory(exprdata.resolve("system"));
		List<Path> osystem_dirs = FileUtil.directoryInventory(system);
		List<Path> dsystem_dirs = FileUtil.directoryInventory(exprdata.resolve("system"));
		for(Path p : osystem_files) {
			p = exprdata.resolve("system").resolve(system.relativize(p));
			assertTrue(dsystem_files.contains(p));
		}
		for(Path p : dsystem_files) {
			p = system.resolve(exprdata.resolve("system").relativize(p));
			assertTrue(osystem_files.contains(p));
		}
		for(Path p : osystem_dirs) {
			p = exprdata.resolve("system").resolve(system.relativize(p));
			assertTrue(dsystem_dirs.contains(p));
		}
		for(Path p : dsystem_dirs) {
			p = system.resolve(exprdata.resolve("system").relativize(p));
			assertTrue(osystem_dirs.contains(p));
		}
		
		//Check System copied properly
		List<Path> orep_files = FileUtil.fileInventory(repository);
		List<Path> drep_files = FileUtil.fileInventory(exprdata.resolve("repository"));
		List<Path> orep_dirs = FileUtil.directoryInventory(repository);
		List<Path> drep_dirs = FileUtil.directoryInventory(exprdata.resolve("repository"));
		for(Path p : orep_files) {
			p = exprdata.resolve("repository").resolve(repository.relativize(p));
			assertTrue(drep_files.contains(p));
		}
		for(Path p : drep_files) {
			p = repository.resolve(exprdata.resolve("repository").relativize(p));
			assertTrue(orep_files.contains(p));
		}
		for(Path p : orep_dirs) {
			p = exprdata.resolve("repository").resolve(repository.relativize(p));
			assertTrue(drep_dirs.contains(p));
		}
		for(Path p : drep_dirs) {
			p = repository.resolve(exprdata.resolve("repository").relativize(p));
			assertTrue(orep_dirs.contains(p));
		}
		
		ed.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
	}
	
	@Test
	public void testGetPathFunctions() throws FileSanetizationFailedException, SQLException, IOException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
		//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
				
		//Create
		ExperimentData eb = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
		//Test
		assertEquals(eb.getPath(), exprdata);
		assertEquals(eb.getSystemPath(), exprdata.resolve("system"));
		assertEquals(eb.getRepositoryPath(), exprdata.resolve("repository"));
		assertEquals(eb.getFragmentsPath(), exprdata.resolve("fragments"));
		assertEquals(eb.getMutantFragmentsPath(), exprdata.resolve("mutantfragments"));
		assertEquals(eb.getReportsPath(), exprdata.resolve("reports"));
		assertEquals(eb.getMutantBasePath(), exprdata.resolve("mutantbase"));
		assertEquals(eb.getTemporaryPath(), exprdata.resolve("temp"));
		
		eb.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
	}
	
	@Test
	public void testFragments() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
		//Prep
		Path root = Paths.get("");
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
				
		//Create
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
		//Check can't create fragments in setup stage
		Fragment frag0 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514);
		boolean caughtstate=false;
		try {
			db.createFragment(frag0);
		} catch (IllegalStateException e) {
			caughtstate=true;
		}
		assertTrue(caughtstate);
		
		//advance state
		db.nextStage();
		
		//Empty Check
		assertTrue(db.numFragments() == 0);
		assertTrue(db.getFragments().size() == 0);
		assertTrue(db.getFragmentIds().size() == 0);
		assertTrue(db.existsFragment(1) == false);
		assertTrue(db.getFragment(1) == null);
		//assertTrue(db.deleteFragment(1) == false);
		//assertTrue(db.deleteFragments() == 0);
		
	//Check createFragments, and exists,get,gets,num,getids as it changes	
		//Add some fragments
		Fragment frag1 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514);
		FragmentDB f1 = db.createFragment(frag1);
		
		//Check return
			int f1id = f1.getId();
			assertEquals(f1id, f1.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java").toAbsolutePath().normalize(), f1.getSrcFile());
			assertEquals(493, f1.getStartLine());
			assertEquals(514, f1.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f1").toFile(), f1.getFragmentFile().toFile()));
			
			//exists
			assertTrue(db.existsFragment(f1id));
			
			//get
			assertEquals(f1, db.getFragment(f1id));
			
			//gets
			assertTrue(db.getFragments().size() == 1);
			assertEquals(f1, db.getFragments().get(0));
			
			//num
			assertTrue(db.numFragments() == 1);
			
			//getids
			assertTrue(db.getFragmentIds().size() == 1);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			
			//fragment file
			Path f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			Path tmpfile = root.resolve("testdata/ExperimentDataTest/tmp");
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
		
		Fragment frag2 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java"), 58, 73);
		FragmentDB f2 = db.createFragment(frag2);
			
			//Check return
			int f2id = f2.getId();
			assertEquals(f2id, f2.getId());
			assertEquals(root.resolve("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java").toAbsolutePath().normalize(), f2.getSrcFile());
			assertEquals(58, f2.getStartLine());
			assertEquals(73, f2.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f2").toFile(), f2.getFragmentFile().toFile()));
			
			//exists
			assertTrue(db.existsFragment(f1id));
			assertTrue(db.existsFragment(f2id));
			
			//get
			assertEquals(f1, db.getFragment(f1id));
			assertEquals(f2, db.getFragment(f2id));
			
			//gets
			assertTrue(db.getFragments().size() == 2);
			assertEquals(f1, db.getFragments().get(0));
			assertEquals(f2, db.getFragments().get(1));
			
			//num
			assertTrue(db.numFragments() == 2);
			
			//getids
			assertTrue(db.getFragmentIds().size() == 2);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			assertEquals(new Integer(f2id), db.getFragmentIds().get(1));
			
			//fragment file
			f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
			
			Path f2file = exprdata.resolve("fragments/" + f2id);
			assertTrue(Files.exists(f2file));
			assertTrue(Files.isRegularFile(f2file));
			FragmentUtil.extractFragment(frag2, tmpfile);
			assertTrue(FileUtils.contentEquals(f2file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
		
		Fragment frag3 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 330, 366);
		FragmentDB f3 = db.createFragment(frag3);
			
			//check return
			int f3id = f3.getId();
			assertEquals(f3id, f3.getId());
			assertEquals(root.resolve("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f3.getSrcFile());
			assertEquals(330, f3.getStartLine());
			assertEquals(366, f3.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f3").toFile(), f3.getFragmentFile().toFile()));
			
			//exists
			assertTrue(db.existsFragment(f1id));
			assertTrue(db.existsFragment(f2id));
			assertTrue(db.existsFragment(f3id));
			
			//get
			assertEquals(f1, db.getFragment(f1id));
			assertEquals(f2, db.getFragment(f2id));
			assertEquals(f3, db.getFragment(f3id));
			
			//gets
			assertTrue(db.getFragments().size() == 3);
			assertEquals(f1, db.getFragments().get(0));
			assertEquals(f2, db.getFragments().get(1));
			assertEquals(f3, db.getFragments().get(2));
			
			//num
			assertTrue(db.numFragments() == 3);
			
			//getids
			assertTrue(db.getFragmentIds().size() == 3);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			assertEquals(new Integer(f2id), db.getFragmentIds().get(1));
			assertEquals(new Integer(f3id), db.getFragmentIds().get(2));
			
			//fragment file
			f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
			
			f2file = exprdata.resolve("fragments/" + f2id);
			assertTrue(Files.exists(f2file));
			assertTrue(Files.isRegularFile(f2file));
			FragmentUtil.extractFragment(frag2, tmpfile);
			assertTrue(FileUtils.contentEquals(f2file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
			
			Path f3file = exprdata.resolve("fragments/" + f3id);
			assertTrue(Files.exists(f3file));
			assertTrue(Files.isReadable(f3file));
			FragmentUtil.extractFragment(frag3, tmpfile);
			assertTrue(FileUtils.contentEquals(f3file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
		
		Fragment frag4 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 449, 486);
		FragmentDB f4 = db.createFragment(frag4);
			//check return
			int f4id = f4.getId();
			assertEquals(f4id, f4.getId());
			assertEquals(root.resolve("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f4.getSrcFile());
			assertEquals(449, f4.getStartLine());
			assertEquals(486, f4.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f4").toFile(), f4.getFragmentFile().toFile()));
			
			//exists
			assertTrue(db.existsFragment(f1id));
			assertTrue(db.existsFragment(f2id));
			assertTrue(db.existsFragment(f3id));
			assertTrue(db.existsFragment(f4id));
			
			//get
			assertEquals(f1, db.getFragment(f1id));
			assertEquals(f2, db.getFragment(f2id));
			assertEquals(f3, db.getFragment(f3id));
			assertEquals(f4, db.getFragment(f4id));
			
			//gets
			assertTrue(db.getFragments().size() == 4);
			assertEquals(f1, db.getFragments().get(0));
			assertEquals(f2, db.getFragments().get(1));
			assertEquals(f3, db.getFragments().get(2));
			assertEquals(f4, db.getFragments().get(3));
			
			//num
			assertTrue(db.numFragments() == 4);
			
			//getids
			assertTrue(db.getFragmentIds().size() == 4);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			assertEquals(new Integer(f2id), db.getFragmentIds().get(1));
			assertEquals(new Integer(f3id), db.getFragmentIds().get(2));
			assertEquals(new Integer(f4id), db.getFragmentIds().get(3));
			
			//fragment file
			f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
			
			f2file = exprdata.resolve("fragments/" + f2id);
			assertTrue(Files.exists(f2file));
			assertTrue(Files.isRegularFile(f2file));
			FragmentUtil.extractFragment(frag2, tmpfile);
			assertTrue(FileUtils.contentEquals(f2file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
			
			f3file = exprdata.resolve("fragments/" + f3id);
			assertTrue(Files.exists(f3file));
			assertTrue(Files.isReadable(f3file));
			FragmentUtil.extractFragment(frag3, tmpfile);
			assertTrue(FileUtils.contentEquals(f3file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
			
			Path f4file = exprdata.resolve("fragments/" + f4id);
			assertTrue(Files.exists(f4file));
			assertTrue(Files.isReadable(f4file));
			FragmentUtil.extractFragment(frag4, tmpfile);
			assertTrue(FileUtils.contentEquals(f4file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
			
		//errors
		boolean error;
		
		error = false;
		try {
			db.createFragment(null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createFragment(new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry123.java"), 449, 486));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createFragment(new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/"), 449, 486));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createFragment(new Fragment(Paths.get("testdata/OperatorTest/cfunctionfragment"), 449, 486));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//advance state
		db.nextStage();
		
		//can't add fragment now
		error = false;
		try {
			db.createFragment(frag0);
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
	}
	
	@Test
	public void testOperators() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
		//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
						
		//Create
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
		//Empty
		assertTrue(db.numOperators() == 0);
		assertTrue(db.getOperator(1) == null);
		assertTrue(db.getOperators().size() == 0);
		assertTrue(db.existsOperator(1) == false);
		assertTrue(db.getOperatorIds().size() == 0);
		
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
			int op1id = op1.getId();
			assertEquals("Name1", op1.getName());
			assertEquals("Description1", op1.getDescription());
			assertEquals(1, op1.getTargetCloneType());
			assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_EOL").toAbsolutePath().normalize(), op1.getMutator());
			
			//exists
			assertTrue(db.existsOperator(op1id));
			
			//num
			assertTrue(db.numOperators() == 1);
			
			//get
			assertEquals(op1, db.getOperator(op1id));
			
			//gets
			assertTrue(db.getOperators().size() == 1);
			assertEquals(op1, db.getOperators().get(0));
			
			//getids
			assertTrue(db.getOperatorIds().size() == 1);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			
		OperatorDB op2 = db.createOperator("Name2", "Description2", 2, SystemUtil.getOperatorsPath().resolve("mRL_S"));
			int op2id = op2.getId();
			assertEquals("Name2", op2.getName());
			assertEquals("Description2", op2.getDescription());
			assertEquals(2, op2.getTargetCloneType());
			assertEquals(SystemUtil.getOperatorsPath().resolve("mRL_S").toAbsolutePath().normalize(), op2.getMutator());
			
			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			
			//num
			assertTrue(db.numOperators() == 2);
			
			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			
			//gets
			assertTrue(db.getOperators().size() == 2);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			
			//getids
			assertTrue(db.getOperatorIds().size() == 2);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			
		OperatorDB op3 = db.createOperator("Name3", "Description3", 3, SystemUtil.getOperatorsPath().resolve("mSIL"));
			int op3id = op3.getId();
			assertEquals("Name3", op3.getName());
			assertEquals("Description3", op3.getDescription());
			assertEquals(3, op3.getTargetCloneType());
			assertEquals(SystemUtil.getOperatorsPath().resolve("mSIL").toAbsolutePath().normalize(), op3.getMutator());
			
			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			
			//num
			assertTrue(db.numOperators() == 3);
			
			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			
			//gets
			assertTrue(db.getOperators().size() == 3);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			assertEquals(op3, db.getOperators().get(2));
			
			//getids
			assertTrue(db.getOperatorIds().size() == 3);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(2));
		
		OperatorDB op4 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
			int op4id = op4.getId();
			assertEquals("Name4", op4.getName());
			assertEquals("Description4", op4.getDescription());
			assertEquals(1, op4.getTargetCloneType());
			assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_BT").toAbsolutePath().normalize(), op4.getMutator());
			
			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(db.existsOperator(op4id));
			
			//num
			assertTrue(db.numOperators() == 4);
			
			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(op4, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 4);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			assertEquals(op3, db.getOperators().get(2));
			assertEquals(op4, db.getOperators().get(3));
			
			//getids
			assertTrue(db.getOperatorIds().size() == 4);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(2));
			assertEquals(new Integer(op4id), db.getOperatorIds().get(3));
		
		db.deleteOperator(op2id);
			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(!db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(db.existsOperator(op4id));
			
			//num
			assertTrue(db.numOperators() == 3);
			
			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(null, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(op4, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 3);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op3, db.getOperators().get(1));
			assertEquals(op4, db.getOperators().get(2));
			
			//getids
			assertTrue(db.getOperatorIds().size() == 3);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op4id), db.getOperatorIds().get(2));
			
		db.deleteOperator(op4id);
			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(!db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(!db.existsOperator(op4id));
				
			//num
			assertTrue(db.numOperators() == 2);
			
			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(null, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(null, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 2);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op3, db.getOperators().get(1));
			
			//getids
			assertTrue(db.getOperatorIds().size() == 2);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(1));
			
		db.deleteOperators();
			//exists
			assertTrue(!db.existsOperator(op1id));
			assertTrue(!db.existsOperator(op2id));
			assertTrue(!db.existsOperator(op3id));
			assertTrue(!db.existsOperator(op4id));
				
			//num
			assertTrue(db.numOperators() == 0);
			
			//get
			assertEquals(null, db.getOperator(op1id));
			assertEquals(null, db.getOperator(op2id));
			assertEquals(null, db.getOperator(op3id));
			assertEquals(null, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 0);
			
			//getids
			assertTrue(db.getOperatorIds().size() == 0);
			
		OperatorDB op5 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		int op5id = op5.getId();
		OperatorDB op6 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		int op6id = op6.getId();
		OperatorDB op7 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		int op7id = op7.getId();
			
		//Errors
		boolean error;
		
		error = false;
		try {
			db.createOperator(null, "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createOperator("Name4", null, 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createOperator("Name4", "Description4", 1, null);
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT213"));
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath());
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//generation stage should not be valid
		db.nextStage();
		error = false;
		try {
			db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		} catch(IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.deleteOperator(op5id);
		} catch(IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.deleteOperators();
		} catch(IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		//evaluation stage should not be valid
		db.nextStage();
		error = false;
		try {
			db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		} catch(IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.deleteOperator(op5id);
		} catch(IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.deleteOperators();
		} catch(IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
	}
	
	@Test
	public void testMutators() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
	//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
								
		//Create
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
		//Some Operators
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
		OperatorDB op2 = db.createOperator("Name2", "Description2", 2, SystemUtil.getOperatorsPath().resolve("mRL_S"));
		OperatorDB op3 = db.createOperator("Name3", "Description3", 3, SystemUtil.getOperatorsPath().resolve("mSIL"));
		OperatorDB op4 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		
			int op1id = op1.getId();
			assertEquals("Name1", op1.getName());
			assertEquals("Description1", op1.getDescription());
			assertEquals(1, op1.getTargetCloneType());
			assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_EOL").toAbsolutePath().normalize(), op1.getMutator());
			int op2id = op2.getId();
			assertEquals("Name2", op2.getName());
			assertEquals("Description2", op2.getDescription());
			assertEquals(2, op2.getTargetCloneType());
			assertEquals(SystemUtil.getOperatorsPath().resolve("mRL_S").toAbsolutePath().normalize(), op2.getMutator());
			int op3id = op3.getId();
			assertEquals("Name3", op3.getName());
			assertEquals("Description3", op3.getDescription());
			assertEquals(3, op3.getTargetCloneType());
			assertEquals(SystemUtil.getOperatorsPath().resolve("mSIL").toAbsolutePath().normalize(), op3.getMutator());
			int op4id = op4.getId();
			assertEquals("Name4", op4.getName());
			assertEquals("Description4", op4.getDescription());
			assertEquals(1, op4.getTargetCloneType());
			assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_BT").toAbsolutePath().normalize(), op4.getMutator());
			
			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(db.existsOperator(op4id));
			
			//num
			assertTrue(db.numOperators() == 4);
			
			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(op4, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 4);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			assertEquals(op3, db.getOperators().get(2));
			assertEquals(op4, db.getOperators().get(3));
			
			//getids
			assertTrue(db.getOperatorIds().size() == 4);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(2));
			assertEquals(new Integer(op4id), db.getOperatorIds().get(3));
			
	//Test Mutators
		List<Integer> oplist = new LinkedList<Integer>();
		
		//empty
		assertTrue(db.getMutators().size() == 0);
		assertTrue(db.numMutators() == 0);
		assertTrue(!db.existsMutator(0));
		assertTrue(db.getMutator(1) == null);
		assertTrue(db.getMutatorIds().size() == 0);
		
		oplist.add(op1id);
		MutatorDB m1 = db.createMutator("m1", oplist);
			int m1id = m1.getId();
			assertEquals("m1", m1.getDescription());
			assertTrue(db.existsMutator(m1id));
			assertEquals(1, m1.getTargetCloneType());
			assertTrue(m1.getOperators().size() == 1);
			assertTrue(m1.getOperators().get(0).equals(op1));
			
			//get
			assertEquals(m1, db.getMutator(m1id));
			
			//exists
			assertTrue(db.existsMutator(m1id));
			
			//num
			assertTrue(db.numMutators() == 1);
			
			//getsid
			assertTrue(db.getMutatorIds().size() == 1);
			assertTrue(db.getMutatorIds().get(0).equals(m1id));
			
			//gets
			assertTrue(db.getMutators().size()==1);
			assertTrue(db.getMutators().get(0).equals(m1));
			
		oplist.add(op2id);
		MutatorDB m2 = db.createMutator("m2", oplist);
			int m2id = m2.getId();
			assertEquals("m2", m2.getDescription());
			assertTrue(db.existsMutator(m2id));
			assertEquals(2, m2.getTargetCloneType());
			assertTrue(m2.getOperators().size() == 2);
			assertTrue(m2.getOperators().get(0).equals(op1));
			assertTrue(m2.getOperators().get(1).equals(op2));
				
			//get
			assertEquals(m1, db.getMutator(m1id));
			assertEquals(m2, db.getMutator(m2id));
				
			//exists
			assertTrue(db.existsMutator(m1id));
			assertTrue(db.existsMutator(m2id));
				
			//num
			assertTrue(db.numMutators() == 2);
				
			//getsid
			assertTrue(db.getMutatorIds().size() == 2);
			assertTrue(db.getMutatorIds().get(0).equals(m1id));
			assertTrue(db.getMutatorIds().get(1).equals(m2id));
				
			//gets
			assertTrue(db.getMutators().size()==2);
			assertTrue(db.getMutators().get(0).equals(m1));
			assertTrue(db.getMutators().get(1).equals(m2));
		
		oplist.add(op3id);
		MutatorDB m3 = db.createMutator("m3", oplist);
			int m3id = m3.getId();
			assertEquals("m3", m3.getDescription());
			assertTrue(db.existsMutator(m3id));
			assertEquals(3, m3.getTargetCloneType());
			assertTrue(m3.getOperators().size() == 3);
			assertTrue(m3.getOperators().get(0).equals(op1));
			assertTrue(m3.getOperators().get(1).equals(op2));
			assertTrue(m3.getOperators().get(2).equals(op3));
					
			//get
			assertEquals(m1, db.getMutator(m1id));
			assertEquals(m2, db.getMutator(m2id));
			assertEquals(m3, db.getMutator(m3id));
					
			//exists
			assertTrue(db.existsMutator(m1id));
			assertTrue(db.existsMutator(m2id));
			assertTrue(db.existsMutator(m3id));
					
			//num
			assertTrue(db.numMutators() == 3);
					
			//getsid
			assertTrue(db.getMutatorIds().size() == 3);
			assertTrue(db.getMutatorIds().get(0).equals(m1id));
			assertTrue(db.getMutatorIds().get(1).equals(m2id));
			assertTrue(db.getMutatorIds().get(2).equals(m3id));
				
			//gets
			assertTrue(db.getMutators().size()==3);
			assertTrue(db.getMutators().get(0).equals(m1));
			assertTrue(db.getMutators().get(1).equals(m2));
			assertTrue(db.getMutators().get(2).equals(m3));
			
		oplist.add(op4id);
		MutatorDB m4 = db.createMutator("m4", oplist);
			int m4id = m4.getId();
			assertEquals("m4", m4.getDescription());
			assertTrue(db.existsMutator(m4id));
			assertEquals(3, m4.getTargetCloneType());
			assertTrue(m4.getOperators().size() == 4);
			assertTrue(m4.getOperators().get(0).equals(op1));
			assertTrue(m4.getOperators().get(1).equals(op2));
			assertTrue(m4.getOperators().get(2).equals(op3));
			assertTrue(m4.getOperators().get(3).equals(op4));
					
			//get
			assertEquals(m1, db.getMutator(m1id));
			assertEquals(m2, db.getMutator(m2id));
			assertEquals(m3, db.getMutator(m3id));
			assertEquals(m4, db.getMutator(m4id));
					
			//exists
			assertTrue(db.existsMutator(m1id));
			assertTrue(db.existsMutator(m2id));
			assertTrue(db.existsMutator(m3id));
			assertTrue(db.existsMutator(m4id));
					
			//num
			assertTrue(db.numMutators() == 4);
					
			//getsid
			assertTrue(db.getMutatorIds().size() == 4);
			assertTrue(db.getMutatorIds().get(0).equals(m1id));
			assertTrue(db.getMutatorIds().get(1).equals(m2id));
			assertTrue(db.getMutatorIds().get(2).equals(m3id));
			assertTrue(db.getMutatorIds().get(3).equals(m4id));
					
			//gets
			assertTrue(db.getMutators().size()==4);
			assertTrue(db.getMutators().get(0).equals(m1));
			assertTrue(db.getMutators().get(1).equals(m2));
			assertTrue(db.getMutators().get(2).equals(m3));
			assertTrue(db.getMutators().get(3).equals(m4));
		
		db.deleteMutator(m2id);
			//get
			assertEquals(m1, db.getMutator(m1id));
			assertEquals(null, db.getMutator(m2id));
			assertEquals(m3, db.getMutator(m3id));
			assertEquals(m4, db.getMutator(m4id));
					
			//exists
			assertTrue(db.existsMutator(m1id));
			assertTrue(!db.existsMutator(m2id));
			assertTrue(db.existsMutator(m3id));
			assertTrue(db.existsMutator(m4id));
					
			//num
			assertTrue(db.numMutators() == 3);
					
			//getsid
			assertTrue(db.getMutatorIds().size() == 3);
			assertTrue(db.getMutatorIds().get(0).equals(m1id));
			assertTrue(db.getMutatorIds().get(1).equals(m3id));
			assertTrue(db.getMutatorIds().get(2).equals(m4id));
					
			//gets
			assertTrue(db.getMutators().size()==3);
			assertTrue(db.getMutators().get(0).equals(m1));
			assertTrue(db.getMutators().get(1).equals(m3));
			assertTrue(db.getMutators().get(2).equals(m4));
		
		db.deleteMutator(m4id);
			//get
			assertEquals(m1, db.getMutator(m1id));
			assertEquals(null, db.getMutator(m2id));
			assertEquals(m3, db.getMutator(m3id));
			assertEquals(null, db.getMutator(m4id));
					
			//exists
			assertTrue(db.existsMutator(m1id));
			assertTrue(!db.existsMutator(m2id));
			assertTrue(db.existsMutator(m3id));
			assertTrue(!db.existsMutator(m4id));
					
			//num
			assertTrue(db.numMutators() == 2);
					
			//getsid
			assertTrue(db.getMutatorIds().size() == 2);
			assertTrue(db.getMutatorIds().get(0).equals(m1id));
			assertTrue(db.getMutatorIds().get(1).equals(m3id));
					
			//gets
			assertTrue(db.getMutators().size()==2);
			assertTrue(db.getMutators().get(0).equals(m1));
			assertTrue(db.getMutators().get(1).equals(m3));
			
		db.deleteMutators();
			//get
			assertEquals(null, db.getMutator(m1id));
			assertEquals(null, db.getMutator(m2id));
			assertEquals(null, db.getMutator(m3id));
			assertEquals(null, db.getMutator(m4id));
					
			//exists
			assertTrue(!db.existsMutator(m1id));
			assertTrue(!db.existsMutator(m2id));
			assertTrue(!db.existsMutator(m3id));
			assertTrue(!db.existsMutator(m4id));
					
			//num
			assertTrue(db.numMutators() == 0);
					
			//getsid
			assertTrue(db.getMutatorIds().size() == 0);
					
			//gets
			assertTrue(db.getMutators().size()==0);
		
		//just some stuff
		MutatorDB m5 = db.createMutator("m4", oplist);
		int m5id = m5.getId();
		MutatorDB m6 = db.createMutator("m4", oplist);
		int m6id = m6.getId();
		MutatorDB m7 = db.createMutator("m4", oplist);
		int m7id = m7.getId();
		
		//error conditions
		boolean error;
		
		error = false;
		try {
			db.createMutator(null, oplist);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutator("m1", null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutator(null, null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		List<Integer> oplisterror = new LinkedList<Integer>();
		error = false;
		try {
			db.createMutator("", oplisterror);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//generate stage
		db.nextStage();
		error = false;
		try {
			db.createMutator("m4", oplist);
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.nextStage();
		error = false;
		try {
			db.deleteMutator(m7id);
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.nextStage();
		error = false;
		try {
			db.deleteMutators();
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		//evaluate stage
		db.nextStage();
		db.nextStage();
		error = false;
		try {
			db.createMutator("m4", oplist);
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.nextStage();
		error = false;
		try {
			db.deleteMutator(m7id);
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.nextStage();
		error = false;
		try {
			db.deleteMutators();
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
	}
	
	@Test
	public void testMutantFragments() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
//Prep
	//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
								
	//Create Experiment Data
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
	//Operators
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
		OperatorDB op2 = db.createOperator("Name2", "Description2", 2, SystemUtil.getOperatorsPath().resolve("mRL_S"));
		OperatorDB op3 = db.createOperator("Name3", "Description3", 3, SystemUtil.getOperatorsPath().resolve("mSIL"));
		OperatorDB op4 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));

		//Check Them
		int op1id = op1.getId();
		assertEquals("Name1", op1.getName());
		assertEquals("Description1", op1.getDescription());
		assertEquals(1, op1.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_EOL").toAbsolutePath().normalize(), op1.getMutator());
		int op2id = op2.getId();
		assertEquals("Name2", op2.getName());
		assertEquals("Description2", op2.getDescription());
		assertEquals(2, op2.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mRL_S").toAbsolutePath().normalize(), op2.getMutator());
		int op3id = op3.getId();
		assertEquals("Name3", op3.getName());
		assertEquals("Description3", op3.getDescription());
		assertEquals(3, op3.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mSIL").toAbsolutePath().normalize(), op3.getMutator());
		int op4id = op4.getId();
		assertEquals("Name4", op4.getName());
		assertEquals("Description4", op4.getDescription());
		assertEquals(1, op4.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_BT").toAbsolutePath().normalize(), op4.getMutator());

			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(db.existsOperator(op4id));

			//num
			assertTrue(db.numOperators() == 4);

			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(op4, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 4);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			assertEquals(op3, db.getOperators().get(2));
			assertEquals(op4, db.getOperators().get(3));
					
			//getids
			assertTrue(db.getOperatorIds().size() == 4);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(2));
			assertEquals(new Integer(op4id), db.getOperatorIds().get(3));
			
	//Mutators
		List<Integer> oplist = new LinkedList<Integer>();

		oplist.add(op1id);
		MutatorDB m1 = db.createMutator("m1", oplist);
		oplist.add(op2id);
		MutatorDB m2 = db.createMutator("m2", oplist);
		oplist.add(op3id);
		MutatorDB m3 = db.createMutator("m3", oplist);
		oplist.add(op4id);
		MutatorDB m4 = db.createMutator("m4", oplist);

		//Check
			int m1id = m1.getId();
			assertEquals("m1", m1.getDescription());
			assertTrue(db.existsMutator(m1id));
			assertEquals(1, m1.getTargetCloneType());
			assertTrue(m1.getOperators().size() == 1);
			assertTrue(m1.getOperators().get(0).equals(op1));
			int m2id = m2.getId();
			assertEquals("m2", m2.getDescription());
			assertTrue(db.existsMutator(m2id));
			assertEquals(2, m2.getTargetCloneType());
			assertTrue(m2.getOperators().size() == 2);
			assertTrue(m2.getOperators().get(0).equals(op1));
			assertTrue(m2.getOperators().get(1).equals(op2));
			int m3id = m3.getId();
			assertEquals("m3", m3.getDescription());
			assertTrue(db.existsMutator(m3id));
			assertEquals(3, m3.getTargetCloneType());
			assertTrue(m3.getOperators().size() == 3);
			assertTrue(m3.getOperators().get(0).equals(op1));
			assertTrue(m3.getOperators().get(1).equals(op2));
			assertTrue(m3.getOperators().get(2).equals(op3));
			int m4id = m4.getId();
			assertEquals("m4", m4.getDescription());
			assertTrue(db.existsMutator(m4id));
			assertEquals(3, m4.getTargetCloneType());
			assertTrue(m4.getOperators().size() == 4);
			assertTrue(m4.getOperators().get(0).equals(op1));
			assertTrue(m4.getOperators().get(1).equals(op2));
			assertTrue(m4.getOperators().get(2).equals(op3));
			assertTrue(m4.getOperators().get(3).equals(op4));

				//get
				assertEquals(m1, db.getMutator(m1id));
				assertEquals(m2, db.getMutator(m2id));
				assertEquals(m3, db.getMutator(m3id));
				assertEquals(m4, db.getMutator(m4id));
								
				//exists
				assertTrue(db.existsMutator(m1id));
				assertTrue(db.existsMutator(m2id));
				assertTrue(db.existsMutator(m3id));
				assertTrue(db.existsMutator(m4id));
								
				//num
				assertTrue(db.numMutators() == 4);
					
				//getsid
				assertTrue(db.getMutatorIds().size() == 4);
				assertTrue(db.getMutatorIds().get(0).equals(m1id));
				assertTrue(db.getMutatorIds().get(1).equals(m2id));
				assertTrue(db.getMutatorIds().get(2).equals(m3id));
				assertTrue(db.getMutatorIds().get(3).equals(m4id));
						
				//gets
				assertTrue(db.getMutators().size()==4);
				assertTrue(db.getMutators().get(0).equals(m1));
				assertTrue(db.getMutators().get(1).equals(m2));
				assertTrue(db.getMutators().get(2).equals(m3));
				assertTrue(db.getMutators().get(3).equals(m4));
				
	//mf should not work in setup phase
		boolean stageerror = false;
		try {
			db.createMutantFragment(1, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one"));
		} catch (IllegalStateException e) {
			stageerror = true;
		}
		assertTrue(stageerror);
				
	//Proceed to experiment phase
		db.nextStage();
		
	//Fragments
		Fragment frag1 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514);
		FragmentDB f1 = db.createFragment(frag1);
		Fragment frag2 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java"), 58, 73);
		FragmentDB f2 = db.createFragment(frag2);
		Fragment frag3 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 330, 366);
		FragmentDB f3 = db.createFragment(frag3);
		Fragment frag4 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 449, 486);
		FragmentDB f4 = db.createFragment(frag4);

			int f1id = f1.getId();
			assertEquals(f1id, f1.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java").toAbsolutePath().normalize(), f1.getSrcFile());
			assertEquals(493, f1.getStartLine());
			assertEquals(514, f1.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f1").toFile(), f1.getFragmentFile().toFile()));

			int f2id = f2.getId();
			assertEquals(f2id, f2.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java").toAbsolutePath().normalize(), f2.getSrcFile());
			assertEquals(58, f2.getStartLine());
			assertEquals(73, f2.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f2").toFile(), f2.getFragmentFile().toFile()));

			int f3id = f3.getId();
			assertEquals(f3id, f3.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f3.getSrcFile());
			assertEquals(330, f3.getStartLine());
			assertEquals(366, f3.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f3").toFile(), f3.getFragmentFile().toFile()));

			int f4id = f4.getId();
			assertEquals(f4id, f4.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f4.getSrcFile());
			assertEquals(449, f4.getStartLine());
			assertEquals(486, f4.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f4").toFile(), f4.getFragmentFile().toFile()));

			//exists
			assertTrue(db.existsFragment(f1id));
			assertTrue(db.existsFragment(f2id));
			assertTrue(db.existsFragment(f3id));
			assertTrue(db.existsFragment(f4id));
							
			//get
			assertEquals(f1, db.getFragment(f1id));
			assertEquals(f2, db.getFragment(f2id));
			assertEquals(f3, db.getFragment(f3id));
			assertEquals(f4, db.getFragment(f4id));
							
			//gets
			assertTrue(db.getFragments().size() == 4);
			assertEquals(f1, db.getFragments().get(0));
			assertEquals(f2, db.getFragments().get(1));
			assertEquals(f3, db.getFragments().get(2));
			assertEquals(f4, db.getFragments().get(3));
							
			//num
			assertTrue(db.numFragments() == 4);
					
			//getids
			assertTrue(db.getFragmentIds().size() == 4);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			assertEquals(new Integer(f2id), db.getFragmentIds().get(1));
			assertEquals(new Integer(f3id), db.getFragmentIds().get(2));
			assertEquals(new Integer(f4id), db.getFragmentIds().get(3));
							
			//fragment file
			Path tmpfile = SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/tmp");
			
			Path f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
						
			Path f2file = exprdata.resolve("fragments/" + f2id);
			assertTrue(Files.exists(f2file));
			assertTrue(Files.isRegularFile(f2file));
			FragmentUtil.extractFragment(frag2, tmpfile);
			assertTrue(FileUtils.contentEquals(f2file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f3file = exprdata.resolve("fragments/" + f3id);
			assertTrue(Files.exists(f3file));
			assertTrue(Files.isReadable(f3file));
			FragmentUtil.extractFragment(frag3, tmpfile);
			assertTrue(FileUtils.contentEquals(f3file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f4file = exprdata.resolve("fragments/" + f4id);
			assertTrue(Files.exists(f4file));
			assertTrue(Files.isReadable(f4file));
			FragmentUtil.extractFragment(frag4, tmpfile);
			assertTrue(FileUtils.contentEquals(f4file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
	
//Mutator
		//empty
		assertTrue(db.getMutantFragment(1) == null);
		assertTrue(db.getMutantFragments().size() == 0);
		assertTrue(db.getMutantFragmentIds().size() == 0);
		assertTrue(db.getMutantFragmentIds(1).size() == 0);
		assertTrue(db.existsMutantFragment(1) == false);
		assertTrue(db.numMutantFragments() == 0);
		
		int mf1id = 1;
		int mf2id = 2;
		int mf3id = 3;
		int mf4id = 4;
		int mf5id = 5;
		
		MutantFragment mf1 = db.createMutantFragment(f1id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one"));
			mf1id = mf1.getId();
			assertEquals(m1id, mf1.getMutatorId());
			assertEquals(f1id, mf1.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf1id).toAbsolutePath().normalize(),mf1.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one").toFile(), mf1.getFragmentFile().toFile()));
		
			//get
			assertEquals(mf1, db.getMutantFragment(mf1id));
			assertEquals(null, db.getMutantFragment(mf2id));
			assertEquals(null, db.getMutantFragment(mf3id));
			assertEquals(null, db.getMutantFragment(mf4id));
			assertEquals(null, db.getMutantFragment(mf5id));
			
			//exists
			assertEquals(true, db.existsMutantFragment(mf1id));
			assertEquals(false, db.existsMutantFragment(mf2id));
			assertEquals(false, db.existsMutantFragment(mf3id));
			assertEquals(false, db.existsMutantFragment(mf4id));
			assertEquals(false, db.existsMutantFragment(mf5id));
			
			//gets
			assertTrue(db.getMutantFragments().size() == 1);
			assertTrue(db.getMutantFragments().contains(mf1));
			
			//gets(fid)
			assertTrue(db.getMutantFragments(f1id).size() == 1);
			assertTrue(db.getMutantFragments(f1id).contains(mf1));
			
			assertTrue(db.getMutantFragments(f2id).size() == 0);
			
			//num
			assertTrue(db.numMutantFragments() == 1);
			
			//getid
			assertTrue(db.getMutantFragmentIds().size() == 1);
			assertTrue(db.getMutantFragmentIds().contains(mf1id));
			
			//getid(fid)
			assertTrue(db.getMutantFragmentIds(f1id).size() == 1);
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
			
			assertTrue(db.getMutantFragmentIds(f2id).size() == 0);
			
		MutantFragment mf2 = db.createMutantFragment(f1id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two"));
			mf2id = mf2.getId();
			assertEquals(m2id, mf2.getMutatorId());
			assertEquals(f1id, mf2.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf2id).toAbsolutePath().normalize(),mf2.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two").toFile(), mf2.getFragmentFile().toFile()));
		
			//get
			assertEquals(mf1, db.getMutantFragment(mf1id));
			assertEquals(mf2, db.getMutantFragment(mf2id));
			assertEquals(null, db.getMutantFragment(mf3id));
			assertEquals(null, db.getMutantFragment(mf4id));
			assertEquals(null, db.getMutantFragment(mf5id));
			
			//exists
			assertEquals(true, db.existsMutantFragment(mf1id));
			assertEquals(true, db.existsMutantFragment(mf2id));
			assertEquals(false, db.existsMutantFragment(mf3id));
			assertEquals(false, db.existsMutantFragment(mf4id));
			assertEquals(false, db.existsMutantFragment(mf5id));
			
			//gets
			assertTrue(db.getMutantFragments().size() == 2);
			assertTrue(db.getMutantFragments().contains(mf1));
			assertTrue(db.getMutantFragments().contains(mf2));
			
			//gets(fid)
			assertTrue(db.getMutantFragments(f1id).size() == 2);
			assertTrue(db.getMutantFragments(f1id).contains(mf1));
			assertTrue(db.getMutantFragments(f1id).contains(mf2));
			
			assertTrue(db.getMutantFragments(f2id).size() == 0);
			
			//num
			assertTrue(db.numMutantFragments() == 2);
			
			//getid
			assertTrue(db.getMutantFragmentIds().size() == 2);
			assertTrue(db.getMutantFragmentIds().contains(mf1id));
			assertTrue(db.getMutantFragmentIds().contains(mf2id));
			
			//getid(fid)
			assertTrue(db.getMutantFragmentIds(f1id).size() == 2);
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf2id));
			
			assertTrue(db.getMutantFragmentIds(f2id).size() == 0);
			
		MutantFragment mf3 = db.createMutantFragment(f1id, m3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three"));
			mf3id = mf3.getId();
			assertEquals(m3id, mf3.getMutatorId());
			assertEquals(f1id, mf3.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf3id).toAbsolutePath().normalize(),mf3.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three").toFile(), mf3.getFragmentFile().toFile()));
		
			//get
			assertEquals(mf1, db.getMutantFragment(mf1id));
			assertEquals(mf2, db.getMutantFragment(mf2id));
			assertEquals(mf3, db.getMutantFragment(mf3id));
			assertEquals(null, db.getMutantFragment(mf4id));
			assertEquals(null, db.getMutantFragment(mf5id));
			
			//exists
			assertEquals(true, db.existsMutantFragment(mf1id));
			assertEquals(true, db.existsMutantFragment(mf2id));
			assertEquals(true, db.existsMutantFragment(mf3id));
			assertEquals(false, db.existsMutantFragment(mf4id));
			assertEquals(false, db.existsMutantFragment(mf5id));
			
			//gets
			assertTrue(db.getMutantFragments().size() == 3);
			assertTrue(db.getMutantFragments().contains(mf1));
			assertTrue(db.getMutantFragments().contains(mf2));
			assertTrue(db.getMutantFragments().contains(mf3));
			
			//gets(fid)
			assertTrue(db.getMutantFragments(f1id).size() == 3);
			assertTrue(db.getMutantFragments(f1id).contains(mf1));
			assertTrue(db.getMutantFragments(f1id).contains(mf2));
			assertTrue(db.getMutantFragments(f1id).contains(mf3));
			
			assertTrue(db.getMutantFragments(f2id).size() == 0);
			
			//num
			assertTrue(db.numMutantFragments() == 3);
			
			//getid
			assertTrue(db.getMutantFragmentIds().size() == 3);
			assertTrue(db.getMutantFragmentIds().contains(mf1id));
			assertTrue(db.getMutantFragmentIds().contains(mf2id));
			assertTrue(db.getMutantFragmentIds().contains(mf3id));
			
			//getid(fid)
			assertTrue(db.getMutantFragmentIds(f1id).size() == 3);
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf2id));
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf3id));
			
			assertTrue(db.getMutantFragmentIds(f2id).size() == 0);
			
		MutantFragment mf4 = db.createMutantFragment(f2id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four"));
			mf4id = mf4.getId();
			assertEquals(m1id, mf4.getMutatorId());
			assertEquals(f2id, mf4.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf4id).toAbsolutePath().normalize(),mf4.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four").toFile(), mf4.getFragmentFile().toFile()));
		
			//get
			assertEquals(mf1, db.getMutantFragment(mf1id));
			assertEquals(mf2, db.getMutantFragment(mf2id));
			assertEquals(mf3, db.getMutantFragment(mf3id));
			assertEquals(mf4, db.getMutantFragment(mf4id));
			assertEquals(null, db.getMutantFragment(mf5id));
			
			//exists
			assertEquals(true, db.existsMutantFragment(mf1id));
			assertEquals(true, db.existsMutantFragment(mf2id));
			assertEquals(true, db.existsMutantFragment(mf3id));
			assertEquals(true, db.existsMutantFragment(mf4id));
			assertEquals(false, db.existsMutantFragment(mf5id));
			
			//gets
			assertTrue(db.getMutantFragments().size() == 4);
			assertTrue(db.getMutantFragments().contains(mf1));
			assertTrue(db.getMutantFragments().contains(mf2));
			assertTrue(db.getMutantFragments().contains(mf3));
			assertTrue(db.getMutantFragments().contains(mf4));
			
			//gets(fid)
			assertTrue(db.getMutantFragments(f1id).size() == 3);
			assertTrue(db.getMutantFragments(f1id).contains(mf1));
			assertTrue(db.getMutantFragments(f1id).contains(mf2));
			assertTrue(db.getMutantFragments(f1id).contains(mf3));
			
			assertTrue(db.getMutantFragments(f2id).size() == 1);
			assertTrue(db.getMutantFragments(f2id).contains(mf4));
			
			//num
			assertTrue(db.numMutantFragments() == 4);
			
			//getid
			assertTrue(db.getMutantFragmentIds().size() == 4);
			assertTrue(db.getMutantFragmentIds().contains(mf1id));
			assertTrue(db.getMutantFragmentIds().contains(mf2id));
			assertTrue(db.getMutantFragmentIds().contains(mf3id));
			assertTrue(db.getMutantFragmentIds().contains(mf4id));
			
			//getid(fid)
			assertTrue(db.getMutantFragmentIds(f1id).size() == 3);
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf2id));
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf3id));
			
			assertTrue(db.getMutantFragmentIds(f2id).size() == 1);
			assertTrue(db.getMutantFragmentIds(f2id).contains(mf4id));
			
		MutantFragment mf5 = db.createMutantFragment(f2id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five"));
			mf5id = mf5.getId();
			assertEquals(m2id, mf5.getMutatorId());
			assertEquals(f2id, mf5.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf5id).toAbsolutePath().normalize(),mf5.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five").toFile(), mf5.getFragmentFile().toFile()));
		
			//get
			assertEquals(mf1, db.getMutantFragment(mf1id));
			assertEquals(mf2, db.getMutantFragment(mf2id));
			assertEquals(mf3, db.getMutantFragment(mf3id));
			assertEquals(mf4, db.getMutantFragment(mf4id));
			assertEquals(mf5, db.getMutantFragment(mf5id));
			
			//exists
			assertEquals(true, db.existsMutantFragment(mf1id));
			assertEquals(true, db.existsMutantFragment(mf2id));
			assertEquals(true, db.existsMutantFragment(mf3id));
			assertEquals(true, db.existsMutantFragment(mf4id));
			assertEquals(true, db.existsMutantFragment(mf5id));
			
			//gets
			assertTrue(db.getMutantFragments().size() == 5);
			assertTrue(db.getMutantFragments().contains(mf1));
			assertTrue(db.getMutantFragments().contains(mf2));
			assertTrue(db.getMutantFragments().contains(mf3));
			assertTrue(db.getMutantFragments().contains(mf4));
			assertTrue(db.getMutantFragments().contains(mf5));
			
			//gets(fid)
			assertTrue(db.getMutantFragments(f1id).size() == 3);
			assertTrue(db.getMutantFragments(f1id).contains(mf1));
			assertTrue(db.getMutantFragments(f1id).contains(mf2));
			assertTrue(db.getMutantFragments(f1id).contains(mf3));
			
			assertTrue(db.getMutantFragments(f2id).size() == 2);
			assertTrue(db.getMutantFragments(f2id).contains(mf4));
			assertTrue(db.getMutantFragments(f2id).contains(mf5));
			
			//num
			assertTrue(db.numMutantFragments() == 5);
			
			//getid
			assertTrue(db.getMutantFragmentIds().size() == 5);
			assertTrue(db.getMutantFragmentIds().contains(mf1id));
			assertTrue(db.getMutantFragmentIds().contains(mf2id));
			assertTrue(db.getMutantFragmentIds().contains(mf3id));
			assertTrue(db.getMutantFragmentIds().contains(mf4id));
			assertTrue(db.getMutantFragmentIds().contains(mf5id));
			
			//getid(fid)
			assertTrue(db.getMutantFragmentIds(f1id).size() == 3);
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf2id));
			assertTrue(db.getMutantFragmentIds(f1id).contains(mf3id));
			
			assertTrue(db.getMutantFragmentIds(f2id).size() == 2);
			assertTrue(db.getMutantFragmentIds(f2id).contains(mf4id));
			assertTrue(db.getMutantFragmentIds(f2id).contains(mf5id));
			
		//Error Conditions
		boolean error;
		
		error = false;
		try {
			db.createMutantFragment(100, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantFragment(f2id, 100, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantFragment(f2id, m2id, null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantFragment(f2id, 100, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/six"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantFragment(f2id, 100, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//evaluation phase should error
		db.nextStage();
		error = false;
		try {
			db.createMutantFragment(f1id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one"));
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
	}
	
	@Test
	public void testTools() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
//Prep
	//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
	
		//Create Experiment Data
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
//Tools
		//Empty
		assertEquals(null, db.getTool(1));
		assertEquals(0, db.getTools().size());
		assertFalse(db.existsTool(1));
		assertEquals(0, db.numTools());
		assertEquals(0, db.getToolIds().size());
		
		int t1id = 1;
		int t2id = 2;
		int t3id = 3;
		int t4id = 4;
		ToolDB t1 = null;
		ToolDB t2 = null;
		ToolDB t3 = null;
		ToolDB t4 = null;
		
		t1 = db.createTool("NiCad", "NiCadDescription", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"));
			t1id = t1.getId();
			assertEquals("NiCad", t1.getName());
			assertEquals("NiCadDescription", t1.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), t1.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"), t1.getToolRunner());
		
			//get
			assertEquals(t1, db.getTool(t1id));
			assertEquals(null, db.getTool(t2id));
			assertEquals(null, db.getTool(t3id));
			assertEquals(null, db.getTool(t4id));
			
			//gets
			assertEquals(1, db.getTools().size());
			assertEquals(true, db.getTools().contains(t1));
			assertEquals(false, db.getTools().contains(t2id));
			assertEquals(false, db.getTools().contains(t3id));
			assertEquals(false, db.getTools().contains(t4id));
			
			//exists
			assertEquals(true, db.existsTool(t1id));
			assertEquals(false, db.existsTool(t2id));
			assertEquals(false, db.existsTool(t3id));
			assertEquals(false, db.existsTool(t4id));
			
			//num
			assertEquals(1, db.numTools());
			
			//getids
			assertEquals(1, db.getToolIds().size());
			assertEquals(true, db.getToolIds().contains(t1id));
			assertEquals(false, db.getToolIds().contains(t2id));
			assertEquals(false, db.getToolIds().contains(t3id));
			assertEquals(false, db.getToolIds().contains(t4id));
			
		t2 = db.createTool("NiCad2", "NiCadDescription2", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad2/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner2/NiCadRunner"));
			t2id = t2.getId();
			assertEquals("NiCad2", t2.getName());
			assertEquals("NiCadDescription2", t2.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad2/"), t2.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner2/NiCadRunner"), t2.getToolRunner());
		
			//get
			assertEquals(t1, db.getTool(t1id));
			assertEquals(t2, db.getTool(t2id));
			assertEquals(null, db.getTool(t3id));
			assertEquals(null, db.getTool(t4id));
			
			//gets
			assertEquals(2, db.getTools().size());
			assertEquals(true, db.getTools().contains(t1));
			assertEquals(true, db.getTools().contains(t2));
			assertEquals(false, db.getTools().contains(t3));
			assertEquals(false, db.getTools().contains(t4));
			
			//exists
			assertEquals(true, db.existsTool(t1id));
			assertEquals(true, db.existsTool(t2id));
			assertEquals(false, db.existsTool(t3id));
			assertEquals(false, db.existsTool(t4id));
			
			//num
			assertEquals(2, db.numTools());
			
			//getids
			assertEquals(2, db.getToolIds().size());
			assertEquals(true, db.getToolIds().contains(t1id));
			assertEquals(true, db.getToolIds().contains(t2id));
			assertEquals(false, db.getToolIds().contains(t3id));
			assertEquals(false, db.getToolIds().contains(t4id));
			
		t3 = db.createTool("NiCad3", "NiCadDescription3", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad3/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner3/NiCadRunner"));
			t3id = t3.getId();
			assertEquals("NiCad3", t3.getName());
			assertEquals("NiCadDescription3", t3.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad3/"), t3.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner3/NiCadRunner"), t3.getToolRunner());
		
			//get
			assertEquals(t1, db.getTool(t1id));
			assertEquals(t2, db.getTool(t2id));
			assertEquals(t3, db.getTool(t3id));
			assertEquals(null, db.getTool(t4id));
			
			//gets
			assertEquals(3, db.getTools().size());
			assertEquals(true, db.getTools().contains(t1));
			assertEquals(true, db.getTools().contains(t2));
			assertEquals(true, db.getTools().contains(t3));
			assertEquals(false, db.getTools().contains(t4));
			
			//exists
			assertEquals(true, db.existsTool(t1id));
			assertEquals(true, db.existsTool(t2id));
			assertEquals(true, db.existsTool(t3id));
			assertEquals(false, db.existsTool(t4id));
			
			//num
			assertEquals(3, db.numTools());
			
			//getids
			assertEquals(3, db.getToolIds().size());
			assertEquals(true, db.getToolIds().contains(t1id));
			assertEquals(true, db.getToolIds().contains(t2id));
			assertEquals(true, db.getToolIds().contains(t3id));
			assertEquals(false, db.getToolIds().contains(t4id));
			
		t4 = db.createTool("NiCad4", "NiCadDescription4", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad4/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner4/NiCadRunner"));
			t4id = t4.getId();
			assertEquals("NiCad4", t4.getName());
			assertEquals("NiCadDescription4", t4.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad4/"), t4.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner4/NiCadRunner"), t4.getToolRunner());
		
			//get
			assertEquals(t1, db.getTool(t1id));
			assertEquals(t2, db.getTool(t2id));
			assertEquals(t3, db.getTool(t3id));
			assertEquals(t4, db.getTool(t4id));
			
			//gets
			assertEquals(4, db.getTools().size());
			assertEquals(true, db.getTools().contains(t1));
			assertEquals(true, db.getTools().contains(t2));
			assertEquals(true, db.getTools().contains(t3));
			assertEquals(true, db.getTools().contains(t4));
			
			//exists
			assertEquals(true, db.existsTool(t1id));
			assertEquals(true, db.existsTool(t2id));
			assertEquals(true, db.existsTool(t3id));
			assertEquals(true, db.existsTool(t4id));
			
			//num
			assertEquals(4, db.numTools());
			
			//getids
			assertEquals(4, db.getToolIds().size());
			assertEquals(true, db.getToolIds().contains(t1id));
			assertEquals(true, db.getToolIds().contains(t2id));
			assertEquals(true, db.getToolIds().contains(t3id));
			assertEquals(true, db.getToolIds().contains(t4id));
		
		//Check Error Conditions
		boolean error = false;	
		
		error = false;
		try {
			db.createTool(null, "description", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad5/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/NiCadRunner"));
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createTool("name", null, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad5/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/NiCadRunner"));
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createTool("name", "description", null, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/NiCadRunner"));
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createTool("name", "description", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad5/"), null);
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createTool("Name", "description", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad6/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/NiCadRunner"));
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createTool("Name", "description", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad5/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner6/NiCadRunner"));
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createTool("Name", "description", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad5/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/"));
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createTool("Name", "description", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/NiCadRunner"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/NiCadRunner"));
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createTool("Name", "description", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner5/src/Clone.java"));
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		db.deleteTools();
		
	//Test delete cascade	
		t1 = db.createTool("NiCad", "NiCadDescription", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"));
		t1id = t1.getId();
		
		//needed data
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
		OperatorDB op2 = db.createOperator("Name2", "Description2", 2, SystemUtil.getOperatorsPath().resolve("mARI"));
		OperatorDB op3 = db.createOperator("Name3", "Description3", 3, SystemUtil.getOperatorsPath().resolve("mIL"));
		OperatorDB op4 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));
		int op1id = op1.getId();
		int op2id = op2.getId();
		int op3id = op3.getId();
		int op4id = op4.getId();
		
		List<Integer> oplist = new LinkedList<Integer>();
		oplist.add(op1id);
		MutatorDB m1 = db.createMutator("m1", oplist);
		oplist.add(op2id);
		MutatorDB m2 = db.createMutator("m2", oplist);
		oplist.add(op3id);
		MutatorDB m3 = db.createMutator("m3", oplist);
		oplist.add(op4id);
		MutatorDB m4 = db.createMutator("m4", oplist);
		int m1id = m1.getId();
		int m2id = m2.getId();
		int m3id = m3.getId();
		int m4id = m4.getId();
				
		db.nextStage();
		
		Fragment frag1 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514);
		FragmentDB f1 = db.createFragment(frag1);
		Fragment frag2 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java"), 58, 73);
		FragmentDB f2 = db.createFragment(frag2);
		Fragment frag3 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 330, 366);
		FragmentDB f3 = db.createFragment(frag3);
		Fragment frag4 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 449, 486);
		FragmentDB f4 = db.createFragment(frag4);
		int f1id = f1.getId();
		int f2id = f2.getId();
		int f3id = f3.getId();
		int f4id = f4.getId();
		
		MutantFragment mf1 = db.createMutantFragment(f1id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one"));
		int mf1id = mf1.getId();
		MutantFragment mf2 = db.createMutantFragment(f1id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two"));
		int mf2id = mf2.getId();
		MutantFragment mf3 = db.createMutantFragment(f1id, m3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three"));
		int mf3id = mf3.getId();
		MutantFragment mf4 = db.createMutantFragment(f2id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four"));
		int mf4id = mf4.getId();
		MutantFragment mf5 = db.createMutantFragment(f2id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five"));
		int mf5id = mf5.getId();
		
		

		MutantBaseDB mb1 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
		MutantBaseDB mb2 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 80, mf2id);
		MutantBaseDB mb3 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, mf3id);
		MutantBaseDB mb4 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, mf3id);
		
		int mb1id = mb1.getId();
		int mb2id = mb2.getId();
		int mb3id = mb3.getId();
		int mb4id = mb4.getId();
		
		db.nextStage();
		
		//Cascaded Data
		CloneDetectionReportDB cdr1 = db.createCloneDetectionReport(t1id, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report1.txt"));
		CloneDetectionReportDB cdr2 = db.createCloneDetectionReport(t1id, mb2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report2.txt"));
		CloneDetectionReportDB cdr3 = db.createCloneDetectionReport(t1id, mb3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report3.txt"));
		CloneDetectionReportDB cdr4 = db.createCloneDetectionReport(t1id, mb4id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report4.txt"));

		UnitRecall ur1 = db.createUnitRecall(t1id, mb1id, 1.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		UnitRecall ur2 = db.createUnitRecall(t1id, mb2id, 1.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		UnitRecall ur3 = db.createUnitRecall(t1id, mb3id, 0.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		UnitRecall ur4 = db.createUnitRecall(t1id, mb4id, 0.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));

		List<VerifiedClone> clonelist = new LinkedList<VerifiedClone>();
		clonelist.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
		UnitPrecision up1 = db.createUnitPrecision(t1id, mb1id, 0.50, clonelist);
		UnitPrecision up2 = db.createUnitPrecision(t1id, mb2id, 0.80, clonelist);
		UnitPrecision up3 = db.createUnitPrecision(t1id, mb3id, 0.30, clonelist);
		UnitPrecision up4 = db.createUnitPrecision(t1id, mb4id, 0.00, clonelist);
		
		//Test
		assertEquals(4, db.getUnitRecallsByTool(t1id).size());
		assertEquals(4, db.getUnitPrecisionForTool(t1id).size());
		assertEquals(4, db.getCloneDetectionReportsByToolId(t1id).size());
		db.deleteTool(t1id);
		assertEquals(0, db.getUnitRecallsByTool(t1id).size());
		assertEquals(0, db.getUnitPrecisionForTool(t1id).size());
		assertEquals(0, db.getCloneDetectionReportsByToolId(t1id).size());
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);	
	}
	
	@Test
	public void testMutantBase() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
//Prep
	//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
								
	//Create Experiment Data
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
	//Operators
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
		OperatorDB op2 = db.createOperator("Name2", "Description2", 2, SystemUtil.getOperatorsPath().resolve("mARI"));
		OperatorDB op3 = db.createOperator("Name3", "Description3", 3, SystemUtil.getOperatorsPath().resolve("mIL"));
		OperatorDB op4 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));

		//Check Them
		int op1id = op1.getId();
		assertEquals("Name1", op1.getName());
		assertEquals("Description1", op1.getDescription());
		assertEquals(1, op1.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_EOL").toAbsolutePath().normalize(), op1.getMutator());
		int op2id = op2.getId();
		assertEquals("Name2", op2.getName());
		assertEquals("Description2", op2.getDescription());
		assertEquals(2, op2.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mARI").toAbsolutePath().normalize(), op2.getMutator());
		int op3id = op3.getId();
		assertEquals("Name3", op3.getName());
		assertEquals("Description3", op3.getDescription());
		assertEquals(3, op3.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mIL").toAbsolutePath().normalize(), op3.getMutator());
		int op4id = op4.getId();
		assertEquals("Name4", op4.getName());
		assertEquals("Description4", op4.getDescription());
		assertEquals(1, op4.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_BT").toAbsolutePath().normalize(), op4.getMutator());

			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(db.existsOperator(op4id));

			//num
			assertTrue(db.numOperators() == 4);

			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(op4, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 4);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			assertEquals(op3, db.getOperators().get(2));
			assertEquals(op4, db.getOperators().get(3));
					
			//getids
			assertTrue(db.getOperatorIds().size() == 4);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(2));
			assertEquals(new Integer(op4id), db.getOperatorIds().get(3));
		
	//Mutators
		List<Integer> oplist = new LinkedList<Integer>();

		oplist.add(op1id);
		MutatorDB m1 = db.createMutator("m1", oplist);
		oplist.add(op2id);
		MutatorDB m2 = db.createMutator("m2", oplist);
		oplist.add(op3id);
		MutatorDB m3 = db.createMutator("m3", oplist);
		oplist.add(op4id);
		MutatorDB m4 = db.createMutator("m4", oplist);

		//Check
			int m1id = m1.getId();
			assertEquals("m1", m1.getDescription());
			assertTrue(db.existsMutator(m1id));
			assertEquals(1, m1.getTargetCloneType());
			assertTrue(m1.getOperators().size() == 1);
			assertTrue(m1.getOperators().get(0).equals(op1));
			int m2id = m2.getId();
			assertEquals("m2", m2.getDescription());
			assertTrue(db.existsMutator(m2id));
			assertEquals(2, m2.getTargetCloneType());
			assertTrue(m2.getOperators().size() == 2);
			assertTrue(m2.getOperators().get(0).equals(op1));
			assertTrue(m2.getOperators().get(1).equals(op2));
			int m3id = m3.getId();
			assertEquals("m3", m3.getDescription());
			assertTrue(db.existsMutator(m3id));
			assertEquals(3, m3.getTargetCloneType());
			assertTrue(m3.getOperators().size() == 3);
			assertTrue(m3.getOperators().get(0).equals(op1));
			assertTrue(m3.getOperators().get(1).equals(op2));
			assertTrue(m3.getOperators().get(2).equals(op3));
			int m4id = m4.getId();
			assertEquals("m4", m4.getDescription());
			assertTrue(db.existsMutator(m4id));
			assertEquals(3, m4.getTargetCloneType());
			assertTrue(m4.getOperators().size() == 4);
			assertTrue(m4.getOperators().get(0).equals(op1));
			assertTrue(m4.getOperators().get(1).equals(op2));
			assertTrue(m4.getOperators().get(2).equals(op3));
			assertTrue(m4.getOperators().get(3).equals(op4));

				//get
				assertEquals(m1, db.getMutator(m1id));
				assertEquals(m2, db.getMutator(m2id));
				assertEquals(m3, db.getMutator(m3id));
				assertEquals(m4, db.getMutator(m4id));
								
				//exists
				assertTrue(db.existsMutator(m1id));
				assertTrue(db.existsMutator(m2id));
				assertTrue(db.existsMutator(m3id));
				assertTrue(db.existsMutator(m4id));
								
				//num
				assertTrue(db.numMutators() == 4);
					
				//getsid
				assertTrue(db.getMutatorIds().size() == 4);
				assertTrue(db.getMutatorIds().get(0).equals(m1id));
				assertTrue(db.getMutatorIds().get(1).equals(m2id));
				assertTrue(db.getMutatorIds().get(2).equals(m3id));
				assertTrue(db.getMutatorIds().get(3).equals(m4id));
						
				//gets
				assertTrue(db.getMutators().size()==4);
				assertTrue(db.getMutators().get(0).equals(m1));
				assertTrue(db.getMutators().get(1).equals(m2));
				assertTrue(db.getMutators().get(2).equals(m3));
				assertTrue(db.getMutators().get(3).equals(m4));

	//cant add mutant base in setup phase
		boolean stageError=false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, 1);
		} catch (IllegalStateException e) {
			stageError = true;
		}
		assertTrue(stageError);
				
	//proceed phase
		db.nextStage();
				
	//Fragments
		Fragment frag1 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514);
		FragmentDB f1 = db.createFragment(frag1);
		Fragment frag2 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java"), 58, 73);
		FragmentDB f2 = db.createFragment(frag2);
		Fragment frag3 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 330, 366);
		FragmentDB f3 = db.createFragment(frag3);
		Fragment frag4 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 449, 486);
		FragmentDB f4 = db.createFragment(frag4);

			int f1id = f1.getId();
			assertEquals(f1id, f1.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java").toAbsolutePath().normalize(), f1.getSrcFile());
			assertEquals(493, f1.getStartLine());
			assertEquals(514, f1.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f1").toFile(), f1.getFragmentFile().toFile()));

			int f2id = f2.getId();
			assertEquals(f2id, f2.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java").toAbsolutePath().normalize(), f2.getSrcFile());
			assertEquals(58, f2.getStartLine());
			assertEquals(73, f2.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f2").toFile(), f2.getFragmentFile().toFile()));

			int f3id = f3.getId();
			assertEquals(f3id, f3.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f3.getSrcFile());
			assertEquals(330, f3.getStartLine());
			assertEquals(366, f3.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f3").toFile(), f3.getFragmentFile().toFile()));

			int f4id = f4.getId();
			assertEquals(f4id, f4.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f4.getSrcFile());
			assertEquals(449, f4.getStartLine());
			assertEquals(486, f4.getEndLine());
			assertTrue(FileUtils.contentEquals(Paths.get("testdata/ExperimentDataTest/Fragments/f4").toFile(), f4.getFragmentFile().toFile()));

			//exists
			assertTrue(db.existsFragment(f1id));
			assertTrue(db.existsFragment(f2id));
			assertTrue(db.existsFragment(f3id));
			assertTrue(db.existsFragment(f4id));
							
			//get
			assertEquals(f1, db.getFragment(f1id));
			assertEquals(f2, db.getFragment(f2id));
			assertEquals(f3, db.getFragment(f3id));
			assertEquals(f4, db.getFragment(f4id));
							
			//gets
			assertTrue(db.getFragments().size() == 4);
			assertEquals(f1, db.getFragments().get(0));
			assertEquals(f2, db.getFragments().get(1));
			assertEquals(f3, db.getFragments().get(2));
			assertEquals(f4, db.getFragments().get(3));
							
			//num
			assertTrue(db.numFragments() == 4);
					
			//getids
			assertTrue(db.getFragmentIds().size() == 4);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			assertEquals(new Integer(f2id), db.getFragmentIds().get(1));
			assertEquals(new Integer(f3id), db.getFragmentIds().get(2));
			assertEquals(new Integer(f4id), db.getFragmentIds().get(3));
							
			//fragment file
			Path tmpfile = SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/tmp");
			
			Path f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
						
			Path f2file = exprdata.resolve("fragments/" + f2id);
			assertTrue(Files.exists(f2file));
			assertTrue(Files.isRegularFile(f2file));
			FragmentUtil.extractFragment(frag2, tmpfile);
			assertTrue(FileUtils.contentEquals(f2file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f3file = exprdata.resolve("fragments/" + f3id);
			assertTrue(Files.exists(f3file));
			assertTrue(Files.isReadable(f3file));
			FragmentUtil.extractFragment(frag3, tmpfile);
			assertTrue(FileUtils.contentEquals(f3file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f4file = exprdata.resolve("fragments/" + f4id);
			assertTrue(Files.exists(f4file));
			assertTrue(Files.isReadable(f4file));
			FragmentUtil.extractFragment(frag4, tmpfile);
			assertTrue(FileUtils.contentEquals(f4file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
	
	//MutantFragments
	MutantFragment mf1 = db.createMutantFragment(f1id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one"));
			int mf1id = mf1.getId();
			assertEquals(m1id, mf1.getMutatorId());
			assertEquals(f1id, mf1.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf1id).toAbsolutePath().normalize(),mf1.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one").toFile(), mf1.getFragmentFile().toFile()));
		MutantFragment mf2 = db.createMutantFragment(f1id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two"));
			int mf2id = mf2.getId();
			assertEquals(m2id, mf2.getMutatorId());
			assertEquals(f1id, mf2.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf2id).toAbsolutePath().normalize(),mf2.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two").toFile(), mf2.getFragmentFile().toFile()));		
		MutantFragment mf3 = db.createMutantFragment(f1id, m3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three"));
			int mf3id = mf3.getId();
			assertEquals(m3id, mf3.getMutatorId());
			assertEquals(f1id, mf3.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf3id).toAbsolutePath().normalize(),mf3.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three").toFile(), mf3.getFragmentFile().toFile()));
		MutantFragment mf4 = db.createMutantFragment(f2id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four"));
			int mf4id = mf4.getId();
			assertEquals(m1id, mf4.getMutatorId());
			assertEquals(f2id, mf4.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf4id).toAbsolutePath().normalize(),mf4.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four").toFile(), mf4.getFragmentFile().toFile()));
		MutantFragment mf5 = db.createMutantFragment(f2id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five"));
			int mf5id = mf5.getId();
			assertEquals(m2id, mf5.getMutatorId());
			assertEquals(f2id, mf5.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf5id).toAbsolutePath().normalize(),mf5.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five").toFile(), mf5.getFragmentFile().toFile()));
				
		//get
		assertEquals(mf1, db.getMutantFragment(mf1id));
		assertEquals(mf2, db.getMutantFragment(mf2id));
		assertEquals(mf3, db.getMutantFragment(mf3id));
		assertEquals(mf4, db.getMutantFragment(mf4id));
		assertEquals(mf5, db.getMutantFragment(mf5id));
			
		//exists
		assertEquals(true, db.existsMutantFragment(mf1id));
		assertEquals(true, db.existsMutantFragment(mf2id));
		assertEquals(true, db.existsMutantFragment(mf3id));
		assertEquals(true, db.existsMutantFragment(mf4id));
		assertEquals(true, db.existsMutantFragment(mf5id));
					
		//gets
		assertTrue(db.getMutantFragments().size() == 5);
		assertTrue(db.getMutantFragments().contains(mf1));
		assertTrue(db.getMutantFragments().contains(mf2));
		assertTrue(db.getMutantFragments().contains(mf3));
		assertTrue(db.getMutantFragments().contains(mf4));
		assertTrue(db.getMutantFragments().contains(mf5));
					
		//gets(fid)
		assertTrue(db.getMutantFragments(f1id).size() == 3);
		assertTrue(db.getMutantFragments(f1id).contains(mf1));
		assertTrue(db.getMutantFragments(f1id).contains(mf2));
		assertTrue(db.getMutantFragments(f1id).contains(mf3));
					
		assertTrue(db.getMutantFragments(f2id).size() == 2);
		assertTrue(db.getMutantFragments(f2id).contains(mf4));
		assertTrue(db.getMutantFragments(f2id).contains(mf5));
					
		//num
		assertTrue(db.numMutantFragments() == 5);
			
		//getid
		assertTrue(db.getMutantFragmentIds().size() == 5);
		assertTrue(db.getMutantFragmentIds().contains(mf1id));
		assertTrue(db.getMutantFragmentIds().contains(mf2id));
		assertTrue(db.getMutantFragmentIds().contains(mf3id));
		assertTrue(db.getMutantFragmentIds().contains(mf4id));
		assertTrue(db.getMutantFragmentIds().contains(mf5id));
					
		//getid(fid)
		assertTrue(db.getMutantFragmentIds(f1id).size() == 3);
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf2id));
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf3id));
					
		assertTrue(db.getMutantFragmentIds(f2id).size() == 2);
		assertTrue(db.getMutantFragmentIds(f2id).contains(mf4id));
		assertTrue(db.getMutantFragmentIds(f2id).contains(mf5id));
		
			
//Test MutantBase
		//empty
		assertEquals(null, db.getMutantBase(1));
		assertEquals(0, db.getMutantBases().size());
		assertEquals(false, db.existsMutantBase(1));
		assertEquals(0, db.numMutantBases());
		assertEquals(0, db.getMutantBaseIds().size());
	
		MutantBaseDB mb1 = null;
		MutantBaseDB mb2 = null;
		MutantBaseDB mb3 = null;
		MutantBaseDB mb4 = null;
		
		int mb1id = 1;
		int mb2id = 2;
		int mb3id = 3;
		int mb4id = 4;
		
		mb1 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
			mb1id = mb1.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb1.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize(), 55, 76), mb1.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/util/InetAddressUtils.java").toAbsolutePath().normalize(), 53, 74), mb1.getMutantFragment());
			assertEquals(mb1id, mb1.getMutantId());
		
			//get
			assertEquals(mb1, db.getMutantBase(mb1id));
			assertEquals(null, db.getMutantBase(mb2id));
			assertEquals(null, db.getMutantBase(mb3id));
			assertEquals(null, db.getMutantBase(mb4id));
			
			//gets
			assertEquals(1, db.getMutantBases().size());
			assertEquals(true, db.getMutantBases().contains(mb1));
			assertEquals(false, db.getMutantBases().contains(mb2));
			assertEquals(false, db.getMutantBases().contains(mb3));
			assertEquals(false, db.getMutantBases().contains(mb4));
			
			//exists
			assertEquals(true, db.existsMutantBase(mb1id));
			assertEquals(false, db.existsMutantBase(mb2id));
			assertEquals(false, db.existsMutantBase(mb3id));
			assertEquals(false, db.existsMutantBase(mb4id));
			
			//num
			assertEquals(1,db.numMutantBases());
			
			//getids
			assertEquals(1, db.getMutantBaseIds().size());
			assertEquals(true, db.getMutantBaseIds().contains(mb1id));
			assertEquals(false, db.getMutantBaseIds().contains(mb2id));
			assertEquals(false, db.getMutantBaseIds().contains(mb3id));
			assertEquals(false, db.getMutantBaseIds().contains(mb4id));

		mb2 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 80, mf2id);
			mb2id = mb2.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb2.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/util/InetAddressUtils.java").toAbsolutePath().normalize(), 53, 74), mb2.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize(), 80, 101), mb2.getMutantFragment());
			assertEquals(mb2id, mb2.getMutantId());
		
			//get
			assertEquals(mb1, db.getMutantBase(mb1id));
			assertEquals(mb2, db.getMutantBase(mb2id));
			assertEquals(null, db.getMutantBase(mb3id));
			assertEquals(null, db.getMutantBase(mb4id));
			
			//gets
			assertEquals(2, db.getMutantBases().size());
			assertEquals(true, db.getMutantBases().contains(mb1));
			assertEquals(true, db.getMutantBases().contains(mb2));
			assertEquals(false, db.getMutantBases().contains(mb3));
			assertEquals(false, db.getMutantBases().contains(mb4));
			
			//exists
			assertEquals(true, db.existsMutantBase(mb1id));
			assertEquals(true, db.existsMutantBase(mb2id));
			assertEquals(false, db.existsMutantBase(mb3id));
			assertEquals(false, db.existsMutantBase(mb4id));
			
			//num
			assertEquals(2,db.numMutantBases());
			
			//getids
			assertEquals(2, db.getMutantBaseIds().size());
			assertEquals(true, db.getMutantBaseIds().contains(mb1id));
			assertEquals(true, db.getMutantBaseIds().contains(mb2id));
			assertEquals(false, db.getMutantBaseIds().contains(mb3id));
			assertEquals(false, db.getMutantBaseIds().contains(mb4id));
			
		mb3 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, mf3id);
			mb3id = mb3.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb3.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java").toAbsolutePath().normalize(), 58, 79), mb3.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java").toAbsolutePath().normalize(), 73, 95), mb3.getMutantFragment());
			assertEquals(mb3id, mb3.getMutantId());
		
			//get
			assertEquals(mb1, db.getMutantBase(mb1id));
			assertEquals(mb2, db.getMutantBase(mb2id));
			assertEquals(mb3, db.getMutantBase(mb3id));
			assertEquals(null, db.getMutantBase(mb4id));
			
			//gets
			assertEquals(3, db.getMutantBases().size());
			assertEquals(true, db.getMutantBases().contains(mb1));
			assertEquals(true, db.getMutantBases().contains(mb2));
			assertEquals(true, db.getMutantBases().contains(mb3));
			assertEquals(false, db.getMutantBases().contains(mb4));
			
			//exists
			assertEquals(true, db.existsMutantBase(mb1id));
			assertEquals(true, db.existsMutantBase(mb2id));
			assertEquals(true, db.existsMutantBase(mb3id));
			assertEquals(false, db.existsMutantBase(mb4id));
			
			//num
			assertEquals(3,db.numMutantBases());
			
			//getids
			assertEquals(3, db.getMutantBaseIds().size());
			assertEquals(true, db.getMutantBaseIds().contains(mb1id));
			assertEquals(true, db.getMutantBaseIds().contains(mb2id));
			assertEquals(true, db.getMutantBaseIds().contains(mb3id));
			assertEquals(false, db.getMutantBaseIds().contains(mb4id));
			
		mb4 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, mf4id);
			mb4id = mb4.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb4.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java").toAbsolutePath().normalize(), 73, 88), mb4.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java").toAbsolutePath().normalize(), 58, 73), mb4.getMutantFragment());
			assertEquals(mb4id, mb4.getMutantId());
		
			//get
			assertEquals(mb1, db.getMutantBase(mb1id));
			assertEquals(mb2, db.getMutantBase(mb2id));
			assertEquals(mb3, db.getMutantBase(mb3id));
			assertEquals(mb4, db.getMutantBase(mb4id));
			
			//gets
			assertEquals(4, db.getMutantBases().size());
			assertEquals(true, db.getMutantBases().contains(mb1));
			assertEquals(true, db.getMutantBases().contains(mb2));
			assertEquals(true, db.getMutantBases().contains(mb3));
			assertEquals(true, db.getMutantBases().contains(mb4));
			
			//exists
			assertEquals(true, db.existsMutantBase(mb1id));
			assertEquals(true, db.existsMutantBase(mb2id));
			assertEquals(true, db.existsMutantBase(mb3id));
			assertEquals(true, db.existsMutantBase(mb4id));
			
			//num
			assertEquals(4,db.numMutantBases());
			
			//getids
			assertEquals(4, db.getMutantBaseIds().size());
			assertEquals(true, db.getMutantBaseIds().contains(mb1id));
			assertEquals(true, db.getMutantBaseIds().contains(mb2id));
			assertEquals(true, db.getMutantBaseIds().contains(mb3id));
			assertEquals(true, db.getMutantBaseIds().contains(mb4id));
			
		//Error Conditions
		boolean error;
		
		error = false;
		try {
			db.createMutantBase(null, 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, null, 53, mf1id);
		} catch(NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, 99);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 9999, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 999, mf1id);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main234234.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/12312321.java"), 53, mf1id);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/"), 53, mf1id);
		} catch(IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//cant add in evaluation phase
		db.nextStage();
		error = false;
		try {
			db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, mf4id);
		} catch (IllegalStateException e) {
			error = true;
		}
		assertTrue(error);
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);	
	}
	
	@Test
	public void testCloneDetectionReport() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
//Prep
	//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
								
	//Create Experiment Data
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
	//Operators
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
		OperatorDB op2 = db.createOperator("Name2", "Description2", 2, SystemUtil.getOperatorsPath().resolve("mRL_S"));
		OperatorDB op3 = db.createOperator("Name3", "Description3", 3, SystemUtil.getOperatorsPath().resolve("mSIL"));
		OperatorDB op4 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));

		//Check Them
		int op1id = op1.getId();
		assertEquals("Name1", op1.getName());
		assertEquals("Description1", op1.getDescription());
		assertEquals(1, op1.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_EOL").toAbsolutePath().normalize(), op1.getMutator());
		int op2id = op2.getId();
		assertEquals("Name2", op2.getName());
		assertEquals("Description2", op2.getDescription());
		assertEquals(2, op2.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mRL_S").toAbsolutePath().normalize(), op2.getMutator());
		int op3id = op3.getId();
		assertEquals("Name3", op3.getName());
		assertEquals("Description3", op3.getDescription());
		assertEquals(3, op3.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mSIL").toAbsolutePath().normalize(), op3.getMutator());
		int op4id = op4.getId();
		assertEquals("Name4", op4.getName());
		assertEquals("Description4", op4.getDescription());
		assertEquals(1, op4.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_BT").toAbsolutePath().normalize(), op4.getMutator());

			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(db.existsOperator(op4id));

			//num
			assertTrue(db.numOperators() == 4);

			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(op4, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 4);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			assertEquals(op3, db.getOperators().get(2));
			assertEquals(op4, db.getOperators().get(3));
					
			//getids
			assertTrue(db.getOperatorIds().size() == 4);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(2));
			assertEquals(new Integer(op4id), db.getOperatorIds().get(3));
		
	//Mutators
		List<Integer> oplist = new LinkedList<Integer>();

		oplist.add(op1id);
		MutatorDB m1 = db.createMutator("m1", oplist);
		oplist.add(op2id);
		MutatorDB m2 = db.createMutator("m2", oplist);
		oplist.add(op3id);
		MutatorDB m3 = db.createMutator("m3", oplist);
		oplist.add(op4id);
		MutatorDB m4 = db.createMutator("m4", oplist);

		//Check
			int m1id = m1.getId();
			assertEquals("m1", m1.getDescription());
			assertTrue(db.existsMutator(m1id));
			assertEquals(1, m1.getTargetCloneType());
			assertTrue(m1.getOperators().size() == 1);
			assertTrue(m1.getOperators().get(0).equals(op1));
			int m2id = m2.getId();
			assertEquals("m2", m2.getDescription());
			assertTrue(db.existsMutator(m2id));
			assertEquals(2, m2.getTargetCloneType());
			assertTrue(m2.getOperators().size() == 2);
			assertTrue(m2.getOperators().get(0).equals(op1));
			assertTrue(m2.getOperators().get(1).equals(op2));
			int m3id = m3.getId();
			assertEquals("m3", m3.getDescription());
			assertTrue(db.existsMutator(m3id));
			assertEquals(3, m3.getTargetCloneType());
			assertTrue(m3.getOperators().size() == 3);
			assertTrue(m3.getOperators().get(0).equals(op1));
			assertTrue(m3.getOperators().get(1).equals(op2));
			assertTrue(m3.getOperators().get(2).equals(op3));
			int m4id = m4.getId();
			assertEquals("m4", m4.getDescription());
			assertTrue(db.existsMutator(m4id));
			assertEquals(3, m4.getTargetCloneType());
			assertTrue(m4.getOperators().size() == 4);
			assertTrue(m4.getOperators().get(0).equals(op1));
			assertTrue(m4.getOperators().get(1).equals(op2));
			assertTrue(m4.getOperators().get(2).equals(op3));
			assertTrue(m4.getOperators().get(3).equals(op4));

				//get
				assertEquals(m1, db.getMutator(m1id));
				assertEquals(m2, db.getMutator(m2id));
				assertEquals(m3, db.getMutator(m3id));
				assertEquals(m4, db.getMutator(m4id));
								
				//exists
				assertTrue(db.existsMutator(m1id));
				assertTrue(db.existsMutator(m2id));
				assertTrue(db.existsMutator(m3id));
				assertTrue(db.existsMutator(m4id));
								
				//num
				assertTrue(db.numMutators() == 4);
					
				//getsid
				assertTrue(db.getMutatorIds().size() == 4);
				assertTrue(db.getMutatorIds().get(0).equals(m1id));
				assertTrue(db.getMutatorIds().get(1).equals(m2id));
				assertTrue(db.getMutatorIds().get(2).equals(m3id));
				assertTrue(db.getMutatorIds().get(3).equals(m4id));
						
				//gets
				assertTrue(db.getMutators().size()==4);
				assertTrue(db.getMutators().get(0).equals(m1));
				assertTrue(db.getMutators().get(1).equals(m2));
				assertTrue(db.getMutators().get(2).equals(m3));
				assertTrue(db.getMutators().get(3).equals(m4));

	//Error: adding CDR in setup
		boolean stageError = false;
		try {
			db.createCloneDetectionReport(1, 2, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report2.txt"));
		} catch (IllegalStateException e) {
			stageError=true;
		}
		assertTrue(stageError);
				
	//next stage
		db.nextStage();
		
	//Error: adding CDR in generation
		stageError = false;
		try {
			db.createCloneDetectionReport(1, 2, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report2.txt"));
		} catch (IllegalStateException e) {
			stageError=true;
		}
		assertTrue(stageError);
				
	//Fragments
		Fragment frag1 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514);
		FragmentDB f1 = db.createFragment(frag1);
		Fragment frag2 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java"), 58, 73);
		FragmentDB f2 = db.createFragment(frag2);
		Fragment frag3 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 330, 366);
		FragmentDB f3 = db.createFragment(frag3);
		Fragment frag4 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 449, 486);
		FragmentDB f4 = db.createFragment(frag4);

			int f1id = f1.getId();
			assertEquals(f1id, f1.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java").toAbsolutePath().normalize(), f1.getSrcFile());
			assertEquals(493, f1.getStartLine());
			assertEquals(514, f1.getEndLine());

			int f2id = f2.getId();
			assertEquals(f2id, f2.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java").toAbsolutePath().normalize(), f2.getSrcFile());
			assertEquals(58, f2.getStartLine());
			assertEquals(73, f2.getEndLine());

			int f3id = f3.getId();
			assertEquals(f3id, f3.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f3.getSrcFile());
			assertEquals(330, f3.getStartLine());
			assertEquals(366, f3.getEndLine());

			int f4id = f4.getId();
			assertEquals(f4id, f4.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f4.getSrcFile());
			assertEquals(449, f4.getStartLine());
			assertEquals(486, f4.getEndLine());

			//exists
			assertTrue(db.existsFragment(f1id));
			assertTrue(db.existsFragment(f2id));
			assertTrue(db.existsFragment(f3id));
			assertTrue(db.existsFragment(f4id));
							
			//get
			assertEquals(f1, db.getFragment(f1id));
			assertEquals(f2, db.getFragment(f2id));
			assertEquals(f3, db.getFragment(f3id));
			assertEquals(f4, db.getFragment(f4id));
							
			//gets
			assertTrue(db.getFragments().size() == 4);
			assertEquals(f1, db.getFragments().get(0));
			assertEquals(f2, db.getFragments().get(1));
			assertEquals(f3, db.getFragments().get(2));
			assertEquals(f4, db.getFragments().get(3));
							
			//num
			assertTrue(db.numFragments() == 4);
					
			//getids
			assertTrue(db.getFragmentIds().size() == 4);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			assertEquals(new Integer(f2id), db.getFragmentIds().get(1));
			assertEquals(new Integer(f3id), db.getFragmentIds().get(2));
			assertEquals(new Integer(f4id), db.getFragmentIds().get(3));
							
			//fragment file
			Path tmpfile = SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/tmp");
			
			Path f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
						
			Path f2file = exprdata.resolve("fragments/" + f2id);
			assertTrue(Files.exists(f2file));
			assertTrue(Files.isRegularFile(f2file));
			FragmentUtil.extractFragment(frag2, tmpfile);
			assertTrue(FileUtils.contentEquals(f2file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f3file = exprdata.resolve("fragments/" + f3id);
			assertTrue(Files.exists(f3file));
			assertTrue(Files.isReadable(f3file));
			FragmentUtil.extractFragment(frag3, tmpfile);
			assertTrue(FileUtils.contentEquals(f3file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f4file = exprdata.resolve("fragments/" + f4id);
			assertTrue(Files.exists(f4file));
			assertTrue(Files.isReadable(f4file));
			FragmentUtil.extractFragment(frag4, tmpfile);
			assertTrue(FileUtils.contentEquals(f4file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
	
	//MutantFragments
	MutantFragment mf1 = db.createMutantFragment(f1id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one"));
			int mf1id = mf1.getId();
			assertEquals(m1id, mf1.getMutatorId());
			assertEquals(f1id, mf1.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf1id).toAbsolutePath().normalize(),mf1.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one").toFile(), mf1.getFragmentFile().toFile()));
		MutantFragment mf2 = db.createMutantFragment(f1id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two"));
			int mf2id = mf2.getId();
			assertEquals(m2id, mf2.getMutatorId());
			assertEquals(f1id, mf2.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf2id).toAbsolutePath().normalize(),mf2.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two").toFile(), mf2.getFragmentFile().toFile()));		
		MutantFragment mf3 = db.createMutantFragment(f1id, m3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three"));
			int mf3id = mf3.getId();
			assertEquals(m3id, mf3.getMutatorId());
			assertEquals(f1id, mf3.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf3id).toAbsolutePath().normalize(),mf3.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three").toFile(), mf3.getFragmentFile().toFile()));
		MutantFragment mf4 = db.createMutantFragment(f2id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four"));
			int mf4id = mf4.getId();
			assertEquals(m1id, mf4.getMutatorId());
			assertEquals(f2id, mf4.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf4id).toAbsolutePath().normalize(),mf4.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four").toFile(), mf4.getFragmentFile().toFile()));
		MutantFragment mf5 = db.createMutantFragment(f2id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five"));
			int mf5id = mf5.getId();
			assertEquals(m2id, mf5.getMutatorId());
			assertEquals(f2id, mf5.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf5id).toAbsolutePath().normalize(),mf5.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five").toFile(), mf5.getFragmentFile().toFile()));
				
		//get
		assertEquals(mf1, db.getMutantFragment(mf1id));
		assertEquals(mf2, db.getMutantFragment(mf2id));
		assertEquals(mf3, db.getMutantFragment(mf3id));
		assertEquals(mf4, db.getMutantFragment(mf4id));
		assertEquals(mf5, db.getMutantFragment(mf5id));
			
		//exists
		assertEquals(true, db.existsMutantFragment(mf1id));
		assertEquals(true, db.existsMutantFragment(mf2id));
		assertEquals(true, db.existsMutantFragment(mf3id));
		assertEquals(true, db.existsMutantFragment(mf4id));
		assertEquals(true, db.existsMutantFragment(mf5id));
					
		//gets
		assertTrue(db.getMutantFragments().size() == 5);
		assertTrue(db.getMutantFragments().contains(mf1));
		assertTrue(db.getMutantFragments().contains(mf2));
		assertTrue(db.getMutantFragments().contains(mf3));
		assertTrue(db.getMutantFragments().contains(mf4));
		assertTrue(db.getMutantFragments().contains(mf5));
					
		//gets(fid)
		assertTrue(db.getMutantFragments(f1id).size() == 3);
		assertTrue(db.getMutantFragments(f1id).contains(mf1));
		assertTrue(db.getMutantFragments(f1id).contains(mf2));
		assertTrue(db.getMutantFragments(f1id).contains(mf3));
					
		assertTrue(db.getMutantFragments(f2id).size() == 2);
		assertTrue(db.getMutantFragments(f2id).contains(mf4));
		assertTrue(db.getMutantFragments(f2id).contains(mf5));
					
		//num
		assertTrue(db.numMutantFragments() == 5);
			
		//getid
		assertTrue(db.getMutantFragmentIds().size() == 5);
		assertTrue(db.getMutantFragmentIds().contains(mf1id));
		assertTrue(db.getMutantFragmentIds().contains(mf2id));
		assertTrue(db.getMutantFragmentIds().contains(mf3id));
		assertTrue(db.getMutantFragmentIds().contains(mf4id));
		assertTrue(db.getMutantFragmentIds().contains(mf5id));
					
		//getid(fid)
		assertTrue(db.getMutantFragmentIds(f1id).size() == 3);
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf2id));
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf3id));
					
		assertTrue(db.getMutantFragmentIds(f2id).size() == 2);
		assertTrue(db.getMutantFragmentIds(f2id).contains(mf4id));
		assertTrue(db.getMutantFragmentIds(f2id).contains(mf5id));
		
			
		//MutantBases
		//empty
		assertEquals(null, db.getMutantBase(1));
		assertEquals(0, db.getMutantBases().size());
		assertEquals(false, db.existsMutantBase(1));
		assertEquals(0, db.numMutantBases());
		assertEquals(0, db.getMutantBaseIds().size());
	
		MutantBaseDB mb1 = null;
		MutantBaseDB mb2 = null;
		MutantBaseDB mb3 = null;
		MutantBaseDB mb4 = null;
		
		int mb1id = 1;
		int mb2id = 2;
		int mb3id = 3;
		int mb4id = 4;

		mb1 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
		mb2 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 80, mf2id);
		mb3 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, mf3id);
		mb4 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, mf4id);
		mb1id = mb1.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb1.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize(), 55, 76), mb1.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/util/InetAddressUtils.java").toAbsolutePath().normalize(), 53, 74), mb1.getMutantFragment());
			assertEquals(mb1id, mb1.getMutantId());
			mb2id = mb2.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb2.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/util/InetAddressUtils.java").toAbsolutePath().normalize(), 53, 74), mb2.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize(), 80, 101), mb2.getMutantFragment());
			assertEquals(mb2id, mb2.getMutantId());
			mb3id = mb3.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb3.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java").toAbsolutePath().normalize(), 58, 79), mb3.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java").toAbsolutePath().normalize(), 73, 95), mb3.getMutantFragment());
			assertEquals(mb3id, mb3.getMutantId());
			mb4id = mb4.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb4.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java").toAbsolutePath().normalize(), 73, 88), mb4.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java").toAbsolutePath().normalize(), 58, 73), mb4.getMutantFragment());
			assertEquals(mb4id, mb4.getMutantId());
		
			//get
			assertEquals(mb1, db.getMutantBase(mb1id));
			assertEquals(mb2, db.getMutantBase(mb2id));
			assertEquals(mb3, db.getMutantBase(mb3id));
			assertEquals(mb4, db.getMutantBase(mb4id));
			
			//gets
			assertEquals(4, db.getMutantBases().size());
			assertEquals(true, db.getMutantBases().contains(mb1));
			assertEquals(true, db.getMutantBases().contains(mb2));
			assertEquals(true, db.getMutantBases().contains(mb3));
			assertEquals(true, db.getMutantBases().contains(mb4));
			
			//exists
			assertEquals(true, db.existsMutantBase(mb1id));
			assertEquals(true, db.existsMutantBase(mb2id));
			assertEquals(true, db.existsMutantBase(mb3id));
			assertEquals(true, db.existsMutantBase(mb4id));
			
			//num
			assertEquals(4,db.numMutantBases());
			
			//getids
			assertEquals(4, db.getMutantBaseIds().size());
			assertEquals(true, db.getMutantBaseIds().contains(mb1id));
			assertEquals(true, db.getMutantBaseIds().contains(mb2id));
			assertEquals(true, db.getMutantBaseIds().contains(mb3id));
			assertEquals(true, db.getMutantBaseIds().contains(mb4id));

		//next stage
		db.nextStage();
			
		//Tools
		//Empty
		assertEquals(null, db.getTool(1));
		assertEquals(0, db.getTools().size());
		assertFalse(db.existsTool(1));
		assertEquals(0, db.numTools());
		assertEquals(0, db.getToolIds().size());
		
		int t1id = 1;
		int t2id = 2;
		int t3id = 3;
		int t4id = 4;
		ToolDB t1 = null;
		ToolDB t2 = null;
		ToolDB t3 = null;
		ToolDB t4 = null;
		
		t1 = db.createTool("NiCad", "NiCadDescription", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"));
			t1id = t1.getId();
			assertEquals("NiCad", t1.getName());
			assertEquals("NiCadDescription", t1.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), t1.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"), t1.getToolRunner());
		t2 = db.createTool("NiCad2", "NiCadDescription2", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad2/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner2/NiCadRunner"));
			t2id = t2.getId();
			assertEquals("NiCad2", t2.getName());
			assertEquals("NiCadDescription2", t2.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad2/"), t2.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner2/NiCadRunner"), t2.getToolRunner());
		t3 = db.createTool("NiCad3", "NiCadDescription3", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad3/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner3/NiCadRunner"));
			t3id = t3.getId();
			assertEquals("NiCad3", t3.getName());
			assertEquals("NiCadDescription3", t3.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad3/"), t3.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner3/NiCadRunner"), t3.getToolRunner());
		t4 = db.createTool("NiCad4", "NiCadDescription4", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad4/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner4/NiCadRunner"));
			t4id = t4.getId();
			assertEquals("NiCad4", t4.getName());
			assertEquals("NiCadDescription4", t4.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad4/"), t4.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner4/NiCadRunner"), t4.getToolRunner());
		
			//get
			assertEquals(t1, db.getTool(t1id));
			assertEquals(t2, db.getTool(t2id));
			assertEquals(t3, db.getTool(t3id));
			assertEquals(t4, db.getTool(t4id));
			
			//gets
			assertEquals(4, db.getTools().size());
			assertEquals(true, db.getTools().contains(t1));
			assertEquals(true, db.getTools().contains(t2));
			assertEquals(true, db.getTools().contains(t3));
			assertEquals(true, db.getTools().contains(t4));
			
			//exists
			assertEquals(true, db.existsTool(t1id));
			assertEquals(true, db.existsTool(t2id));
			assertEquals(true, db.existsTool(t3id));
			assertEquals(true, db.existsTool(t4id));
			
			//num
			assertEquals(4, db.numTools());
			
			//getids
			assertEquals(4, db.getToolIds().size());
			assertEquals(true, db.getToolIds().contains(t1id));
			assertEquals(true, db.getToolIds().contains(t2id));
			assertEquals(true, db.getToolIds().contains(t3id));
			assertEquals(true, db.getToolIds().contains(t4id));

	//Test Clone Detection Reports
			
		CloneDetectionReportDB cdr1 = null;
		CloneDetectionReportDB cdr2 = null;
		CloneDetectionReportDB cdr3 = null;
		CloneDetectionReportDB cdr4 = null;
		CloneDetectionReportDB cdr5 = null;
		CloneDetectionReportDB cdr6 = null;
		CloneDetectionReportDB cdr7 = null;
		CloneDetectionReportDB cdr8 = null;
		
		//empty
		assertEquals(null, db.getCloneDetectionReport(t1id, mb1id));
		assertEquals(null, db.getCloneDetectionReport(t1id, mb2id));
		assertEquals(null, db.getCloneDetectionReport(t1id, mb3id));
		assertEquals(null, db.getCloneDetectionReport(t2id, mb1id));
		assertEquals(null, db.getCloneDetectionReport(t2id, mb2id));
		assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
		assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
		assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
		
		
		assertEquals(false, db.existsCloneDetectionReport(t1id, mb1id));
		assertEquals(false, db.existsCloneDetectionReport(t1id, mb2id));
		assertEquals(false, db.existsCloneDetectionReport(t1id, mb3id));
		assertEquals(false, db.existsCloneDetectionReport(t2id, mb1id));
		assertEquals(false, db.existsCloneDetectionReport(t2id, mb2id));
		assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
		assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
		assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
		
		assertEquals(0, db.numCloneDetectionReports());
		assertEquals(0, db.numCloneDetectionReports(1));
		
		assertEquals(0, db.getCloneDetectionReports().size());
		assertEquals(0, db.getCloneDetectionReportsByToolId(1).size());
		assertEquals(0, db.getCloneDetectionReportsByBaseId(1).size());
		
		assertEquals(false, db.deleteCloneDetectionReport(1, 1));
		assertEquals(0, db.deleteCloneDetectionReports());
		assertEquals(0, db.deleteCloneDetectionReportsByToolId(1));
		assertEquals(0, db.deleteCloneDetectionReportsByBaseId(1));
		
		
		cdr1 = db.createCloneDetectionReport(t1id, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report1.txt"));
			assertEquals(t1id, cdr1.getToolId());	
			assertEquals(mb1id, cdr1.getBaseId());
			assertEquals(db.getReportsPath().resolve(t1id + "-" + mb1id).toAbsolutePath().normalize(), cdr1.getReport().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(cdr1.getReport().toFile(), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report1.txt").toFile()));
			
			//num
			assertEquals(1, db.numCloneDetectionReports());
			assertEquals(1, db.numCloneDetectionReports(t1id));
			assertEquals(0, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));
			
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(1, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			
			//gets_tools
			assertEquals(1, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t2id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(mb2id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(mb3id).size());
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
			
		cdr2 = db.createCloneDetectionReport(t1id, mb2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report2.txt"));
			assertEquals(t1id, cdr2.getToolId());	
			assertEquals(mb2id, cdr2.getBaseId());
			assertEquals(db.getReportsPath().resolve(t1id + "-" + mb2id).toAbsolutePath().normalize(), cdr2.getReport().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(cdr2.getReport().toFile(), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report2.txt").toFile()));
			
			//num
			assertEquals(2, db.numCloneDetectionReports());
			assertEquals(2, db.numCloneDetectionReports(t1id));
			assertEquals(0, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));
			
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(2, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			
			//gets_tools
			assertEquals(2, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t2id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(mb3id).size());
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
		
		cdr3 = db.createCloneDetectionReport(t1id, mb3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report3.txt"));
			assertEquals(t1id, cdr3.getToolId());	
			assertEquals(mb3id, cdr3.getBaseId());
			assertEquals(db.getReportsPath().resolve(t1id + "-" + mb3id).toAbsolutePath().normalize(), cdr3.getReport().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(cdr3.getReport().toFile(), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report3.txt").toFile()));
			
			//num
			assertEquals(3, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(0, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));
			
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(3, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t2id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
			
		cdr4 = db.createCloneDetectionReport(t2id, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report4.txt"));
			assertEquals(t2id, cdr4.getToolId());	
			assertEquals(mb1id, cdr4.getBaseId());
			assertEquals(db.getReportsPath().resolve(t2id + "-" + mb1id).toAbsolutePath().normalize(), cdr4.getReport().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(cdr4.getReport().toFile(), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report4.txt").toFile()));
			
			//num
			assertEquals(4, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(1, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));
			
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(cdr4, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(4, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			assertTrue(db.getCloneDetectionReports().contains(cdr4));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(1, db.getCloneDetectionReportsByToolId(t2id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr4));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr4));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
			
		cdr5 = db.createCloneDetectionReport(t2id, mb2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report5.txt"));
			assertEquals(t2id, cdr5.getToolId());
			assertEquals(mb2id, cdr5.getBaseId());
			assertEquals(db.getReportsPath().resolve(t2id + "-" + mb2id).toAbsolutePath().normalize(), cdr5.getReport().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(cdr5.getReport().toFile(), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report5.txt").toFile()));
			
			//num
			assertEquals(5, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(2, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));
			
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(cdr4, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(cdr5, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(5, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			assertTrue(db.getCloneDetectionReports().contains(cdr4));
			assertTrue(db.getCloneDetectionReports().contains(cdr5));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(2, db.getCloneDetectionReportsByToolId(t2id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr5));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr4));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr5));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
	
		cdr6 = db.createCloneDetectionReport(t2id, mb3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report6.txt"));
			assertEquals(t2id, cdr6.getToolId());
			assertEquals(mb3id, cdr6.getBaseId());
			assertEquals(db.getReportsPath().resolve(t2id + "-" + mb3id).toAbsolutePath().normalize(), cdr6.getReport().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(cdr6.getReport().toFile(), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report6.txt").toFile()));
			
			//num
			assertEquals(6, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(3, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));
			
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(cdr4, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(cdr5, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(cdr6, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(6, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			assertTrue(db.getCloneDetectionReports().contains(cdr4));
			assertTrue(db.getCloneDetectionReports().contains(cdr5));
			assertTrue(db.getCloneDetectionReports().contains(cdr6));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(3, db.getCloneDetectionReportsByToolId(t2id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr5));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr6));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr4));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr5));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr6));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
			
		cdr7 = db.createCloneDetectionReport(t3id, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report7.txt"));
			assertEquals(t3id, cdr7.getToolId());
			assertEquals(mb1id, cdr7.getBaseId());
			assertEquals(db.getReportsPath().resolve(t3id + "-" + mb1id).toAbsolutePath().normalize(), cdr7.getReport().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(cdr7.getReport().toFile(), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report7.txt").toFile()));
			
			//num
			assertEquals(7, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(3, db.numCloneDetectionReports(t2id));
			assertEquals(1, db.numCloneDetectionReports(t3id));
			
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(cdr4, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(cdr5, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(cdr6, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(cdr7, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(7, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			assertTrue(db.getCloneDetectionReports().contains(cdr4));
			assertTrue(db.getCloneDetectionReports().contains(cdr5));
			assertTrue(db.getCloneDetectionReports().contains(cdr6));
			assertTrue(db.getCloneDetectionReports().contains(cdr7));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(3, db.getCloneDetectionReportsByToolId(t2id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr5));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr6));
			
			assertEquals(1, db.getCloneDetectionReportsByToolId(t3id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t3id).contains(cdr7));
			
			//gets_bases
			assertEquals(3, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr7));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr5));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr6));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
	
		cdr8 = db.createCloneDetectionReport(t3id, mb2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report8.txt"));
			assertEquals(t3id, cdr8.getToolId());
			assertEquals(mb2id, cdr8.getBaseId());
			assertEquals(db.getReportsPath().resolve(t3id + "-" + mb2id).toAbsolutePath().normalize(), cdr8.getReport().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(cdr8.getReport().toFile(), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report8.txt").toFile()));
			
			//num
			assertEquals(8, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(3, db.numCloneDetectionReports(t2id));
			assertEquals(2, db.numCloneDetectionReports(t3id));
			
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(cdr4, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(cdr5, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(cdr6, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(cdr7, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(cdr8, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(8, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			assertTrue(db.getCloneDetectionReports().contains(cdr4));
			assertTrue(db.getCloneDetectionReports().contains(cdr5));
			assertTrue(db.getCloneDetectionReports().contains(cdr6));
			assertTrue(db.getCloneDetectionReports().contains(cdr7));
			assertTrue(db.getCloneDetectionReports().contains(cdr8));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(3, db.getCloneDetectionReportsByToolId(t2id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr5));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr6));
			
			assertEquals(2, db.getCloneDetectionReportsByToolId(t3id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t3id).contains(cdr7));
			assertTrue(db.getCloneDetectionReportsByToolId(t3id).contains(cdr8));
			
			//gets_bases
			assertEquals(3, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr7));
			
			assertEquals(3, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr5));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr8));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr6));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t3id, mb2id));
		
		db.deleteCloneDetectionReport(t3id, mb2id);
			
			//num
			assertEquals(7, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(3, db.numCloneDetectionReports(t2id));
			assertEquals(1, db.numCloneDetectionReports(t3id));
		
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(cdr4, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(cdr5, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(cdr6, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(cdr7, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(7, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			assertTrue(db.getCloneDetectionReports().contains(cdr4));
			assertTrue(db.getCloneDetectionReports().contains(cdr5));
			assertTrue(db.getCloneDetectionReports().contains(cdr6));
			assertTrue(db.getCloneDetectionReports().contains(cdr7));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(3, db.getCloneDetectionReportsByToolId(t2id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr5));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr6));
			
			assertEquals(1, db.getCloneDetectionReportsByToolId(t3id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t3id).contains(cdr7));
			
			//gets_bases
			assertEquals(3, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr7));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr5));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr6));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));

		db.deleteCloneDetectionReport(t3id, mb1id);
			
			//num
			assertEquals(6, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(3, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));	
		
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(cdr4, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(cdr5, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(cdr6, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(6, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			assertTrue(db.getCloneDetectionReports().contains(cdr4));
			assertTrue(db.getCloneDetectionReports().contains(cdr5));
			assertTrue(db.getCloneDetectionReports().contains(cdr6));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(3, db.getCloneDetectionReportsByToolId(t2id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr4));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr5));
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr6));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr4));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr5));
			
			assertEquals(2, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr6));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			
		db.deleteCloneDetectionReportsByToolId(t2id);
		
			//num
			assertEquals(3, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(0, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));			
		
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(3, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t2id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			
		cdr5 = db.createCloneDetectionReport(t2id, mb2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report5.txt"));
		cdr8 = db.createCloneDetectionReport(t3id, mb2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report8.txt"));

			//num
			assertEquals(5, db.numCloneDetectionReports());
			assertEquals(3, db.numCloneDetectionReports(t1id));
			assertEquals(1, db.numCloneDetectionReports(t2id));
			assertEquals(1, db.numCloneDetectionReports(t3id));	
		
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(cdr2, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(cdr5, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(cdr8, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(5, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr2));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			assertTrue(db.getCloneDetectionReports().contains(cdr5));
			assertTrue(db.getCloneDetectionReports().contains(cdr8));
			
			//gets_tools
			assertEquals(3, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(1, db.getCloneDetectionReportsByToolId(t2id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t2id).contains(cdr5));
			
			assertEquals(1, db.getCloneDetectionReportsByToolId(t3id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t3id).contains(cdr8));
			
			//gets_bases
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			
			assertEquals(3, db.getCloneDetectionReportsByBaseId(mb2id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr2));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr5));
			assertTrue(db.getCloneDetectionReportsByBaseId(mb2id).contains(cdr8));
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(true, db.existsCloneDetectionReport(t3id, mb2id));
			
		db.deleteCloneDetectionReportsByBaseId(mb2id);
		
			//num
			assertEquals(2, db.numCloneDetectionReports());
			assertEquals(2, db.numCloneDetectionReports(t1id));
			assertEquals(0, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));	
		
			//get
			assertEquals(cdr1, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(cdr3, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(2, db.getCloneDetectionReports().size());
			assertTrue(db.getCloneDetectionReports().contains(cdr1));
			assertTrue(db.getCloneDetectionReports().contains(cdr3));
			
			//gets_tools
			assertEquals(2, db.getCloneDetectionReportsByToolId(t1id).size());
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr1));
			assertTrue(db.getCloneDetectionReportsByToolId(t1id).contains(cdr3));
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t2id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb1id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb1id).contains(cdr1));
			
			assertEquals(0, db.getCloneDetectionReportsByBaseId(mb2id).size());
			
			assertEquals(1, db.getCloneDetectionReportsByBaseId(mb3id).size());
			assertTrue(db.getCloneDetectionReportsByBaseId(mb3id).contains(cdr3));
			
			//exists
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(true, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
		
			
		db.deleteCloneDetectionReports();
		
			//num
			assertEquals(0, db.numCloneDetectionReports());
			assertEquals(0, db.numCloneDetectionReports(t1id));
			assertEquals(0, db.numCloneDetectionReports(t2id));
			assertEquals(0, db.numCloneDetectionReports(t3id));			
			
			//get
			assertEquals(null, db.getCloneDetectionReport(t1id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t1id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t1id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb2id));
			assertEquals(null, db.getCloneDetectionReport(t2id, mb3id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb1id));
			assertEquals(null, db.getCloneDetectionReport(t3id, mb2id));
			
			//gets
			assertEquals(0, db.getCloneDetectionReports().size());
			
			//gets_tools
			assertEquals(0, db.getCloneDetectionReportsByToolId(t1id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t2id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByToolId(t3id).size());
			
			//gets_bases
			assertEquals(0, db.getCloneDetectionReportsByBaseId(mb1id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByBaseId(mb2id).size());
			
			assertEquals(0, db.getCloneDetectionReportsByBaseId(mb3id).size());
			
			//exists
			assertEquals(false, db.existsCloneDetectionReport(t1id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t1id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t1id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb2id));
			assertEquals(false, db.existsCloneDetectionReport(t2id, mb3id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb1id));
			assertEquals(false, db.existsCloneDetectionReport(t3id, mb2id));
		
		boolean error;
		
		error = false;
		try {
			db.createCloneDetectionReport(100, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report5.txt"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createCloneDetectionReport(t1id, 100, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report5.txt"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createCloneDetectionReport(100, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report23232.txt"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createCloneDetectionReport(100, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createCloneDetectionReport(t1id, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report5.txt"));
			db.createCloneDetectionReport(t1id, mb1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/CloneDetectionReportSamples/report5.txt"));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createCloneDetectionReport(t1id, mb1id, null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);	
	}
	
	@Test
	public void testConstructMutantBase() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
	//Prep
		//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
									
		//Create Experiment Data
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
			
		//Operator
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
		
		//Mutator
		List<Integer> oplist = new LinkedList<Integer>();
		oplist.add(op1.getId());
		MutatorDB m1 = db.createMutator("Description", oplist);
		
		db.nextStage();
		
		//Fragment
		FragmentDB f1 = db.createFragment(new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514));		
		
		//Mutant Fragment
		MutantFragment mf1 = db.createMutantFragment(f1.getId(), m1.getId(), Paths.get("testdata/ExperimentDataTest/constructMutantBase/mf").toAbsolutePath().normalize());
		
		//MutantBases
		MutantBaseDB mb1 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 117,
				                             Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/core/Scanner.java"), 69, mf1.getId());
		
		
		//Create
		db.constructBase(mb1.getId());
		
		//Prep Check
		FragmentUtil.extractFragment(mb1.getOriginalFragment(), Paths.get("testdata/ExperimentDataTest/constructMutantBase/original_extracted").toAbsolutePath().normalize());
		FragmentUtil.extractFragment(mb1.getMutantFragment(), Paths.get("testdata/ExperimentDataTest/constructMutantBase/mutant_extracted").toAbsolutePath().normalize());
		
		Path eofrag = Paths.get("testdata/ExperimentDataTest/constructMutantBase/original_extracted").toAbsolutePath().normalize();
		Path omfrag = Paths.get("testdata/ExperimentDataTest/constructMutantBase/mutant_extracted").toAbsolutePath().normalize();
		
		Path ofile = Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize();
		Path mfile = Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/core/Scanner.java").toAbsolutePath().normalize();
		
		Path newofile =  Paths.get("testdata/ExperimentDataTest/exprdata/mutantbase/angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize();
		Path newmfile = Paths.get("testdata/ExperimentDataTest/exprdata/mutantbase/angryipscanner/net/azib/ipscan/core/Scanner.java").toAbsolutePath().normalize();
		
		List<Path> original_files = FileUtil.fileInventory(db.getSystemPath());
		original_files.remove(ofile);
		original_files.remove(mfile);
		
		List<Path> new_files = FileUtil.fileInventory(db.getMutantBasePath());
		new_files.remove(newofile);
		new_files.remove(newmfile);
		
		List<Path> original_dirs = FileUtil.directoryInventory(db.getSystemPath());
		List<Path> new_dirs = FileUtil.directoryInventory(db.getMutantBasePath());
		
		//Check
			//injected matches original
		assertTrue(FileUtils.contentEquals(eofrag.toFile(), f1.getFragmentFile().toAbsolutePath().normalize().toFile()));
		assertTrue(FileUtils.contentEquals(omfrag.toFile(), mf1.getFragmentFile().toAbsolutePath().normalize().toFile()));
		
			//mutated files match manual crafted
		assertTrue(FileUtils.contentEquals(newofile.toFile(), Paths.get("testdata/ExperimentDataTest/constructMutantBase/Main.java").toAbsolutePath().normalize().toFile()));
		assertTrue(FileUtils.contentEquals(newmfile.toFile(), Paths.get("testdata/ExperimentDataTest/constructMutantBase/Scanner.java").toAbsolutePath().normalize().toFile()));
		
			//Rest of the files/directories are okay
		for(Path p : original_dirs) {
			p = db.getMutantBasePath().resolve(db.getSystemPath().toAbsolutePath().normalize().relativize(p.toAbsolutePath().normalize()));
			assertTrue(new_dirs.contains(p));
		}
		for(Path p : new_dirs) {
			p = db.getSystemPath().resolve(db.getMutantBasePath().toAbsolutePath().normalize().relativize(p.toAbsolutePath().normalize()));
			assertTrue(original_dirs.contains(p));
		}
		
		for(Path p : original_files) {
			p = db.getMutantBasePath().resolve(db.getSystemPath().toAbsolutePath().normalize().relativize(p.toAbsolutePath().normalize()));
			assertTrue(new_files.contains(p));
		}
		for(Path p : new_files) {
			p = db.getSystemPath().resolve(db.getMutantBasePath().toAbsolutePath().normalize().relativize(p.toAbsolutePath().normalize()));
			assertTrue(original_files.contains(p));
		}
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);	
	}
	
	@Test
	public void testUnitRecall() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
//Prep
	//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
								
	//Create Experiment Data
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
	//Operators
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
		OperatorDB op2 = db.createOperator("Name2", "Description2", 2, SystemUtil.getOperatorsPath().resolve("mRL_S"));
		OperatorDB op3 = db.createOperator("Name3", "Description3", 3, SystemUtil.getOperatorsPath().resolve("mSIL"));
		OperatorDB op4 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));

		//Check Them
		int op1id = op1.getId();
		assertEquals("Name1", op1.getName());
		assertEquals("Description1", op1.getDescription());
		assertEquals(1, op1.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_EOL").toAbsolutePath().normalize(), op1.getMutator());
		int op2id = op2.getId();
		assertEquals("Name2", op2.getName());
		assertEquals("Description2", op2.getDescription());
		assertEquals(2, op2.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mRL_S").toAbsolutePath().normalize(), op2.getMutator());
		int op3id = op3.getId();
		assertEquals("Name3", op3.getName());
		assertEquals("Description3", op3.getDescription());
		assertEquals(3, op3.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mSIL").toAbsolutePath().normalize(), op3.getMutator());
		int op4id = op4.getId();
		assertEquals("Name4", op4.getName());
		assertEquals("Description4", op4.getDescription());
		assertEquals(1, op4.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_BT").toAbsolutePath().normalize(), op4.getMutator());

			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(db.existsOperator(op4id));

			//num
			assertTrue(db.numOperators() == 4);

			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(op4, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 4);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			assertEquals(op3, db.getOperators().get(2));
			assertEquals(op4, db.getOperators().get(3));
					
			//getids
			assertTrue(db.getOperatorIds().size() == 4);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(2));
			assertEquals(new Integer(op4id), db.getOperatorIds().get(3));
		
	//Mutators
		List<Integer> oplist = new LinkedList<Integer>();

		oplist.add(op1id);
		MutatorDB m1 = db.createMutator("m1", oplist);
		oplist.add(op2id);
		MutatorDB m2 = db.createMutator("m2", oplist);
		oplist.add(op3id);
		MutatorDB m3 = db.createMutator("m3", oplist);
		oplist.add(op4id);
		MutatorDB m4 = db.createMutator("m4", oplist);

		//Check
			int m1id = m1.getId();
			assertEquals("m1", m1.getDescription());
			assertTrue(db.existsMutator(m1id));
			assertEquals(1, m1.getTargetCloneType());
			assertTrue(m1.getOperators().size() == 1);
			assertTrue(m1.getOperators().get(0).equals(op1));
			int m2id = m2.getId();
			assertEquals("m2", m2.getDescription());
			assertTrue(db.existsMutator(m2id));
			assertEquals(2, m2.getTargetCloneType());
			assertTrue(m2.getOperators().size() == 2);
			assertTrue(m2.getOperators().get(0).equals(op1));
			assertTrue(m2.getOperators().get(1).equals(op2));
			int m3id = m3.getId();
			assertEquals("m3", m3.getDescription());
			assertTrue(db.existsMutator(m3id));
			assertEquals(3, m3.getTargetCloneType());
			assertTrue(m3.getOperators().size() == 3);
			assertTrue(m3.getOperators().get(0).equals(op1));
			assertTrue(m3.getOperators().get(1).equals(op2));
			assertTrue(m3.getOperators().get(2).equals(op3));
			int m4id = m4.getId();
			assertEquals("m4", m4.getDescription());
			assertTrue(db.existsMutator(m4id));
			assertEquals(3, m4.getTargetCloneType());
			assertTrue(m4.getOperators().size() == 4);
			assertTrue(m4.getOperators().get(0).equals(op1));
			assertTrue(m4.getOperators().get(1).equals(op2));
			assertTrue(m4.getOperators().get(2).equals(op3));
			assertTrue(m4.getOperators().get(3).equals(op4));

				//get
				assertEquals(m1, db.getMutator(m1id));
				assertEquals(m2, db.getMutator(m2id));
				assertEquals(m3, db.getMutator(m3id));
				assertEquals(m4, db.getMutator(m4id));
								
				//exists
				assertTrue(db.existsMutator(m1id));
				assertTrue(db.existsMutator(m2id));
				assertTrue(db.existsMutator(m3id));
				assertTrue(db.existsMutator(m4id));
								
				//num
				assertTrue(db.numMutators() == 4);
					
				//getsid
				assertTrue(db.getMutatorIds().size() == 4);
				assertTrue(db.getMutatorIds().get(0).equals(m1id));
				assertTrue(db.getMutatorIds().get(1).equals(m2id));
				assertTrue(db.getMutatorIds().get(2).equals(m3id));
				assertTrue(db.getMutatorIds().get(3).equals(m4id));
						
				//gets
				assertTrue(db.getMutators().size()==4);
				assertTrue(db.getMutators().get(0).equals(m1));
				assertTrue(db.getMutators().get(1).equals(m2));
				assertTrue(db.getMutators().get(2).equals(m3));
				assertTrue(db.getMutators().get(3).equals(m4));
				
		boolean stageError = false;
		try {
			db.createUnitRecall(1, 2, 0.5, new Clone(
						new Fragment(db.getMutantBasePath().resolve("file1"), 10, 20), 
						new Fragment(db.getMutantBasePath().resolve("file2"), 30, 40)));
		} catch (IllegalStateException e) {
			stageError = true;
		}
		assertTrue(stageError);
				
	// switch to generation
		db.nextStage();
		stageError = false;
		try {
			db.createUnitRecall(1, 2, 0.5, new Clone(
						new Fragment(db.getMutantBasePath().resolve("file1"), 10, 20), 
						new Fragment(db.getMutantBasePath().resolve("file2"), 30, 40)));
		} catch (IllegalStateException e) {
			stageError = true;
		}
		assertTrue(stageError);
				
	//Fragments
		Fragment frag1 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514);
		FragmentDB f1 = db.createFragment(frag1);
		Fragment frag2 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java"), 58, 73);
		FragmentDB f2 = db.createFragment(frag2);
		Fragment frag3 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 330, 366);
		FragmentDB f3 = db.createFragment(frag3);
		Fragment frag4 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 449, 486);
		FragmentDB f4 = db.createFragment(frag4);

			int f1id = f1.getId();
			assertEquals(f1id, f1.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java").toAbsolutePath().normalize(), f1.getSrcFile());
			assertEquals(493, f1.getStartLine());
			assertEquals(514, f1.getEndLine());

			int f2id = f2.getId();
			assertEquals(f2id, f2.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java").toAbsolutePath().normalize(), f2.getSrcFile());
			assertEquals(58, f2.getStartLine());
			assertEquals(73, f2.getEndLine());

			int f3id = f3.getId();
			assertEquals(f3id, f3.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f3.getSrcFile());
			assertEquals(330, f3.getStartLine());
			assertEquals(366, f3.getEndLine());

			int f4id = f4.getId();
			assertEquals(f4id, f4.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f4.getSrcFile());
			assertEquals(449, f4.getStartLine());
			assertEquals(486, f4.getEndLine());

			//exists
			assertTrue(db.existsFragment(f1id));
			assertTrue(db.existsFragment(f2id));
			assertTrue(db.existsFragment(f3id));
			assertTrue(db.existsFragment(f4id));
							
			//get
			assertEquals(f1, db.getFragment(f1id));
			assertEquals(f2, db.getFragment(f2id));
			assertEquals(f3, db.getFragment(f3id));
			assertEquals(f4, db.getFragment(f4id));
							
			//gets
			assertTrue(db.getFragments().size() == 4);
			assertEquals(f1, db.getFragments().get(0));
			assertEquals(f2, db.getFragments().get(1));
			assertEquals(f3, db.getFragments().get(2));
			assertEquals(f4, db.getFragments().get(3));
							
			//num
			assertTrue(db.numFragments() == 4);
					
			//getids
			assertTrue(db.getFragmentIds().size() == 4);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			assertEquals(new Integer(f2id), db.getFragmentIds().get(1));
			assertEquals(new Integer(f3id), db.getFragmentIds().get(2));
			assertEquals(new Integer(f4id), db.getFragmentIds().get(3));
							
			//fragment file
			Path tmpfile = SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/tmp");
			
			Path f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
						
			Path f2file = exprdata.resolve("fragments/" + f2id);
			assertTrue(Files.exists(f2file));
			assertTrue(Files.isRegularFile(f2file));
			FragmentUtil.extractFragment(frag2, tmpfile);
			assertTrue(FileUtils.contentEquals(f2file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f3file = exprdata.resolve("fragments/" + f3id);
			assertTrue(Files.exists(f3file));
			assertTrue(Files.isReadable(f3file));
			FragmentUtil.extractFragment(frag3, tmpfile);
			assertTrue(FileUtils.contentEquals(f3file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f4file = exprdata.resolve("fragments/" + f4id);
			assertTrue(Files.exists(f4file));
			assertTrue(Files.isReadable(f4file));
			FragmentUtil.extractFragment(frag4, tmpfile);
			assertTrue(FileUtils.contentEquals(f4file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
	
	//MutantFragments
	MutantFragment mf1 = db.createMutantFragment(f1id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one"));
			int mf1id = mf1.getId();
			assertEquals(m1id, mf1.getMutatorId());
			assertEquals(f1id, mf1.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf1id).toAbsolutePath().normalize(),mf1.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one").toFile(), mf1.getFragmentFile().toFile()));
		MutantFragment mf2 = db.createMutantFragment(f1id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two"));
			int mf2id = mf2.getId();
			assertEquals(m2id, mf2.getMutatorId());
			assertEquals(f1id, mf2.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf2id).toAbsolutePath().normalize(),mf2.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two").toFile(), mf2.getFragmentFile().toFile()));		
		MutantFragment mf3 = db.createMutantFragment(f1id, m3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three"));
			int mf3id = mf3.getId();
			assertEquals(m3id, mf3.getMutatorId());
			assertEquals(f1id, mf3.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf3id).toAbsolutePath().normalize(),mf3.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three").toFile(), mf3.getFragmentFile().toFile()));
		MutantFragment mf4 = db.createMutantFragment(f2id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four"));
			int mf4id = mf4.getId();
			assertEquals(m1id, mf4.getMutatorId());
			assertEquals(f2id, mf4.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf4id).toAbsolutePath().normalize(),mf4.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four").toFile(), mf4.getFragmentFile().toFile()));
		MutantFragment mf5 = db.createMutantFragment(f2id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five"));
			int mf5id = mf5.getId();
			assertEquals(m2id, mf5.getMutatorId());
			assertEquals(f2id, mf5.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf5id).toAbsolutePath().normalize(),mf5.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five").toFile(), mf5.getFragmentFile().toFile()));
				
		//get
		assertEquals(mf1, db.getMutantFragment(mf1id));
		assertEquals(mf2, db.getMutantFragment(mf2id));
		assertEquals(mf3, db.getMutantFragment(mf3id));
		assertEquals(mf4, db.getMutantFragment(mf4id));
		assertEquals(mf5, db.getMutantFragment(mf5id));
			
		//exists
		assertEquals(true, db.existsMutantFragment(mf1id));
		assertEquals(true, db.existsMutantFragment(mf2id));
		assertEquals(true, db.existsMutantFragment(mf3id));
		assertEquals(true, db.existsMutantFragment(mf4id));
		assertEquals(true, db.existsMutantFragment(mf5id));
					
		//gets
		assertTrue(db.getMutantFragments().size() == 5);
		assertTrue(db.getMutantFragments().contains(mf1));
		assertTrue(db.getMutantFragments().contains(mf2));
		assertTrue(db.getMutantFragments().contains(mf3));
		assertTrue(db.getMutantFragments().contains(mf4));
		assertTrue(db.getMutantFragments().contains(mf5));
					
		//gets(fid)
		assertTrue(db.getMutantFragments(f1id).size() == 3);
		assertTrue(db.getMutantFragments(f1id).contains(mf1));
		assertTrue(db.getMutantFragments(f1id).contains(mf2));
		assertTrue(db.getMutantFragments(f1id).contains(mf3));
					
		assertTrue(db.getMutantFragments(f2id).size() == 2);
		assertTrue(db.getMutantFragments(f2id).contains(mf4));
		assertTrue(db.getMutantFragments(f2id).contains(mf5));
					
		//num
		assertTrue(db.numMutantFragments() == 5);
			
		//getid
		assertTrue(db.getMutantFragmentIds().size() == 5);
		assertTrue(db.getMutantFragmentIds().contains(mf1id));
		assertTrue(db.getMutantFragmentIds().contains(mf2id));
		assertTrue(db.getMutantFragmentIds().contains(mf3id));
		assertTrue(db.getMutantFragmentIds().contains(mf4id));
		assertTrue(db.getMutantFragmentIds().contains(mf5id));
					
		//getid(fid)
		assertTrue(db.getMutantFragmentIds(f1id).size() == 3);
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf2id));
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf3id));
					
		assertTrue(db.getMutantFragmentIds(f2id).size() == 2);
		assertTrue(db.getMutantFragmentIds(f2id).contains(mf4id));
		assertTrue(db.getMutantFragmentIds(f2id).contains(mf5id));
		
			
		//MutantBases
		//empty
		assertEquals(null, db.getMutantBase(1));
		assertEquals(0, db.getMutantBases().size());
		assertEquals(false, db.existsMutantBase(1));
		assertEquals(0, db.numMutantBases());
		assertEquals(0, db.getMutantBaseIds().size());
	
		MutantBaseDB mb1 = null;
		MutantBaseDB mb2 = null;
		MutantBaseDB mb3 = null;
		MutantBaseDB mb4 = null;
		
		int mb1id = 1;
		int mb2id = 2;
		int mb3id = 3;
		int mb4id = 4;

		mb1 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
		mb2 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 80, mf2id);
		mb3 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, mf3id);
		mb4 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, mf3id);
			mb1id = mb1.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb1.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize(), 55, 76), mb1.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/util/InetAddressUtils.java").toAbsolutePath().normalize(), 53, 74), mb1.getMutantFragment());
			assertEquals(m1id, mb1.getMutantId());
			mb2id = mb2.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb2.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/util/InetAddressUtils.java").toAbsolutePath().normalize(), 53, 74), mb2.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize(), 80, 101), mb2.getMutantFragment());
			assertEquals(m2id, mb2.getMutantId());
			mb3id = mb3.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb3.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java").toAbsolutePath().normalize(), 58, 79), mb3.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java").toAbsolutePath().normalize(), 73, 95), mb3.getMutantFragment());
			assertEquals(m3id, mb3.getMutantId());
			mb4id = mb4.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb4.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java").toAbsolutePath().normalize(), 73, 94), mb4.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java").toAbsolutePath().normalize(), 58, 80), mb4.getMutantFragment());
			assertEquals(m3id, mb4.getMutantId());
		
			//get
			assertEquals(mb1, db.getMutantBase(mb1id));
			assertEquals(mb2, db.getMutantBase(mb2id));
			assertEquals(mb3, db.getMutantBase(mb3id));
			assertEquals(mb4, db.getMutantBase(mb4id));
			
			//gets
			assertEquals(4, db.getMutantBases().size());
			assertEquals(true, db.getMutantBases().contains(mb1));
			assertEquals(true, db.getMutantBases().contains(mb2));
			assertEquals(true, db.getMutantBases().contains(mb3));
			assertEquals(true, db.getMutantBases().contains(mb4));
			
			//exists
			assertEquals(true, db.existsMutantBase(mb1id));
			assertEquals(true, db.existsMutantBase(mb2id));
			assertEquals(true, db.existsMutantBase(mb3id));
			assertEquals(true, db.existsMutantBase(mb4id));
			
			//num
			assertEquals(4,db.numMutantBases());
			
			//getids
			assertEquals(4, db.getMutantBaseIds().size());
			assertEquals(true, db.getMutantBaseIds().contains(mb1id));
			assertEquals(true, db.getMutantBaseIds().contains(mb2id));
			assertEquals(true, db.getMutantBaseIds().contains(mb3id));
			assertEquals(true, db.getMutantBaseIds().contains(mb4id));

		//go to evaluatin phase
		db.nextStage();
			
		//Tools
		//Empty
		assertEquals(null, db.getTool(1));
		assertEquals(0, db.getTools().size());
		assertFalse(db.existsTool(1));
		assertEquals(0, db.numTools());
		assertEquals(0, db.getToolIds().size());
		
		int t1id = 1;
		int t2id = 2;
		int t3id = 3;
		int t4id = 4;
		ToolDB t1 = null;
		ToolDB t2 = null;
		ToolDB t3 = null;
		ToolDB t4 = null;
		
		t1 = db.createTool("NiCad", "NiCadDescription", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"));
			t1id = t1.getId();
			assertEquals("NiCad", t1.getName());
			assertEquals("NiCadDescription", t1.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), t1.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"), t1.getToolRunner());
		t2 = db.createTool("NiCad2", "NiCadDescription2", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad2/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner2/NiCadRunner"));
			t2id = t2.getId();
			assertEquals("NiCad2", t2.getName());
			assertEquals("NiCadDescription2", t2.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad2/"), t2.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner2/NiCadRunner"), t2.getToolRunner());
		t3 = db.createTool("NiCad3", "NiCadDescription3", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad3/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner3/NiCadRunner"));
			t3id = t3.getId();
			assertEquals("NiCad3", t3.getName());
			assertEquals("NiCadDescription3", t3.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad3/"), t3.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner3/NiCadRunner"), t3.getToolRunner());
		t4 = db.createTool("NiCad4", "NiCadDescription4", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad4/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner4/NiCadRunner"));
			t4id = t4.getId();
			assertEquals("NiCad4", t4.getName());
			assertEquals("NiCadDescription4", t4.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad4/"), t4.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner4/NiCadRunner"), t4.getToolRunner());
		
			//get
			assertEquals(t1, db.getTool(t1id));
			assertEquals(t2, db.getTool(t2id));
			assertEquals(t3, db.getTool(t3id));
			assertEquals(t4, db.getTool(t4id));
			
			//gets
			assertEquals(4, db.getTools().size());
			assertEquals(true, db.getTools().contains(t1));
			assertEquals(true, db.getTools().contains(t2));
			assertEquals(true, db.getTools().contains(t3));
			assertEquals(true, db.getTools().contains(t4));
			
			//exists
			assertEquals(true, db.existsTool(t1id));
			assertEquals(true, db.existsTool(t2id));
			assertEquals(true, db.existsTool(t3id));
			assertEquals(true, db.existsTool(t4id));
			
			//num
			assertEquals(4, db.numTools());
			
			//getids
			assertEquals(4, db.getToolIds().size());
			assertEquals(true, db.getToolIds().contains(t1id));
			assertEquals(true, db.getToolIds().contains(t2id));
			assertEquals(true, db.getToolIds().contains(t3id));
			assertEquals(true, db.getToolIds().contains(t4id));
		
	//Test UnitRecall
		//empty
		
		//get
		assertEquals(null, db.getUnitRecall(t1id, mb1id));
		
		//exists
		assertEquals(false, db.existsUnitRecall(t1id, mb1id));
		
		//gets
		assertEquals(0, db.getUnitRecalls().size());
		
		//gets(tool)
		assertEquals(0, db.getUnitRecallsByTool(t1id).size());
		
		//gets(base)
		assertEquals(0, db.getUnitRecallsByBase(mb1id).size());
		
		//get(tool/mutator)
		assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
		
		UnitRecall ur1 = db.createUnitRecall(t1id, mb1id, 0.5, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
			assertEquals(t1id, ur1.getToolid());
			assertEquals(mb1id, ur1.getBaseid());
			assertEquals(new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()), ur1.getClone());
			assertEquals(0.5, ur1.getRecall(), 0.000001);
			
			//get
			assertEquals(ur1, db.getUnitRecall(t1id, mb1id));
			
			//exists
			assertEquals(true, db.existsUnitRecall(t1id, mb1id));
			
			//gets
			assertEquals(1, db.getUnitRecalls().size());
			assertTrue(db.getUnitRecalls().contains(ur1));
			
			//gets(tool)
			assertEquals(1, db.getUnitRecallsByTool(t1id).size());
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur1));
			
			//gets(base)
			assertEquals(1, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur1));
			
			//get(tool/mutator)
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
		UnitRecall ur2 = db.createUnitRecall(t2id, mb1id, 1.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
			assertEquals(t2id, ur2.getToolid());
			assertEquals(mb1id, ur2.getBaseid());
			assertEquals(new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()), ur2.getClone());
			assertEquals(1.0, ur2.getRecall(), 0.000001);
			
			//get
			assertEquals(ur1, db.getUnitRecall(t1id, mb1id));
			assertEquals(ur2, db.getUnitRecall(t2id, mb1id));
			
			//exists
			assertEquals(true, db.existsUnitRecall(t1id, mb1id));
			assertEquals(true, db.existsUnitRecall(t2id, mb1id));
			
			//gets
			assertEquals(2, db.getUnitRecalls().size());
			assertTrue(db.getUnitRecalls().contains(ur1));
			assertTrue(db.getUnitRecalls().contains(ur2));
			
			//gets(tool)
			assertEquals(1, db.getUnitRecallsByTool(t1id).size());
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByTool(t2id).size());
			assertTrue(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			//gets(base)
			assertEquals(2, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur2));
			
			//get(tool/mutator)
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
		UnitRecall ur3 = db.createUnitRecall(t3id, mb1id, 0.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
			assertEquals(t2id, ur2.getToolid());
			assertEquals(mb1id, ur2.getBaseid());
			assertEquals(new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()), ur2.getClone());
			assertEquals(0.0, ur3.getRecall(), 0.000001);
			
			//get
			assertEquals(ur1, db.getUnitRecall(t1id, mb1id));
			assertEquals(ur2, db.getUnitRecall(t2id, mb1id));
			assertEquals(ur3, db.getUnitRecall(t3id, mb1id));
			
			//exists
			assertEquals(true, db.existsUnitRecall(t1id, mb1id));
			assertEquals(true, db.existsUnitRecall(t2id, mb1id));
			assertEquals(true, db.existsUnitRecall(t3id, mb1id));
			
			//gets
			assertEquals(3, db.getUnitRecalls().size());
			assertTrue(db.getUnitRecalls().contains(ur1));
			assertTrue(db.getUnitRecalls().contains(ur2));
			assertTrue(db.getUnitRecalls().contains(ur3));
			
			//gets(tool)
			assertEquals(1, db.getUnitRecallsByTool(t1id).size());
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByTool(t2id).size());
			assertTrue(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByTool(t3id).size());
			assertTrue(db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(3, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			//get(tool/mutator)
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
		UnitRecall ur4 = db.createUnitRecall(t1id, mb2id, 0.0, null);
			assertEquals(t1id, ur4.getToolid());
			assertEquals(mb2id, ur4.getBaseid());
			assertEquals(null, ur4.getClone());
			assertEquals(0.0, ur4.getRecall(), 0.000001);
			
			//get
			assertEquals(ur1, db.getUnitRecall(t1id, mb1id));
			assertEquals(ur2, db.getUnitRecall(t2id, mb1id));
			assertEquals(ur3, db.getUnitRecall(t3id, mb1id));
			assertEquals(ur4, db.getUnitRecall(t1id, mb2id));
			
			//exists
			assertEquals(true, db.existsUnitRecall(t1id, mb1id));
			assertEquals(true, db.existsUnitRecall(t2id, mb1id));
			assertEquals(true, db.existsUnitRecall(t3id, mb1id));
			assertEquals(true, db.existsUnitRecall(t1id, mb2id));
			
			//gets
			assertEquals(4, db.getUnitRecalls().size());
			assertTrue(db.getUnitRecalls().contains(ur1));
			assertTrue(db.getUnitRecalls().contains(ur2));
			assertTrue(db.getUnitRecalls().contains(ur3));
			assertTrue(db.getUnitRecalls().contains(ur4));
			
			//gets(tool)
			assertEquals(2, db.getUnitRecallsByTool(t1id).size());
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur1));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur4));
			
			assertEquals(1, db.getUnitRecallsByTool(t2id).size());
			assertTrue(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByTool(t3id).size());
			assertTrue(db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(3, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByBase(mb2id).size());
			assertTrue(db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			//get(tool/mutator)
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
		UnitRecall ur5 = db.createUnitRecall(t1id, mb3id, 1.0, new Clone(mb3.getOriginalFragment(), mb3.getMutantFragment()));
			assertEquals(t1id, ur5.getToolid());
			assertEquals(mb3id, ur5.getBaseid());
			assertEquals(new Clone(mb3.getOriginalFragment(), mb3.getMutantFragment()), ur5.getClone());
			assertEquals(1.0, ur5.getRecall(), 0.000001);
			
			//get
			assertEquals(ur1, db.getUnitRecall(t1id, mb1id));
			assertEquals(ur2, db.getUnitRecall(t2id, mb1id));
			assertEquals(ur3, db.getUnitRecall(t3id, mb1id));
			assertEquals(ur4, db.getUnitRecall(t1id, mb2id));
			assertEquals(ur5, db.getUnitRecall(t1id, mb3id));
			
			//exists
			assertEquals(true, db.existsUnitRecall(t1id, mb1id));
			assertEquals(true, db.existsUnitRecall(t2id, mb1id));
			assertEquals(true, db.existsUnitRecall(t3id, mb1id));
			assertEquals(true, db.existsUnitRecall(t1id, mb2id));
			assertEquals(true, db.existsUnitRecall(t1id, mb3id));
			
			//gets
			assertEquals(5, db.getUnitRecalls().size());
			assertTrue(db.getUnitRecalls().contains(ur1));
			assertTrue(db.getUnitRecalls().contains(ur2));
			assertTrue(db.getUnitRecalls().contains(ur3));
			assertTrue(db.getUnitRecalls().contains(ur4));
			assertTrue(db.getUnitRecalls().contains(ur5));
			
			//gets(tool)
			assertEquals(3, db.getUnitRecallsByTool(t1id).size());
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur1));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur4));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur5));
			
			assertEquals(1, db.getUnitRecallsByTool(t2id).size());
			assertTrue(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByTool(t3id).size());
			assertTrue(db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(3, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByBase(mb2id).size());
			assertTrue(db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			assertEquals(1, db.getUnitRecallsByBase(mb3id).size());
			assertTrue(db.getUnitRecallsByBase(mb3id).contains(ur5));
			
			//get(tool/mutator)
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m3id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur5));
			
		UnitRecall ur6 = db.createUnitRecall(t1id, mb4id, 0.0, null);
			assertEquals(t1id, ur6.getToolid());
			assertEquals(mb4id, ur6.getBaseid());
			assertEquals(null, ur6.getClone());
			assertEquals(0.0, ur6.getRecall(), 0.000001);
			
			//get
			assertEquals(ur1, db.getUnitRecall(t1id, mb1id));
			assertEquals(ur2, db.getUnitRecall(t2id, mb1id));
			assertEquals(ur3, db.getUnitRecall(t3id, mb1id));
			assertEquals(ur4, db.getUnitRecall(t1id, mb2id));
			assertEquals(ur5, db.getUnitRecall(t1id, mb3id));
			assertEquals(ur6, db.getUnitRecall(t1id, mb4id));
			
			//exists
			assertEquals(true, db.existsUnitRecall(t1id, mb1id));
			assertEquals(true, db.existsUnitRecall(t2id, mb1id));
			assertEquals(true, db.existsUnitRecall(t3id, mb1id));
			assertEquals(true, db.existsUnitRecall(t1id, mb2id));
			assertEquals(true, db.existsUnitRecall(t1id, mb3id));
			assertEquals(true, db.existsUnitRecall(t1id, mb4id));
			
			//gets
			assertEquals(6, db.getUnitRecalls().size());
			assertTrue(db.getUnitRecalls().contains(ur1));
			assertTrue(db.getUnitRecalls().contains(ur2));
			assertTrue(db.getUnitRecalls().contains(ur3));
			assertTrue(db.getUnitRecalls().contains(ur4));
			assertTrue(db.getUnitRecalls().contains(ur5));
			assertTrue(db.getUnitRecalls().contains(ur6));
			
			//gets(tool)
			assertEquals(4, db.getUnitRecallsByTool(t1id).size());
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur1));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur4));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur5));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur6));
			
			assertEquals(1, db.getUnitRecallsByTool(t2id).size());
			assertTrue(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByTool(t3id).size());
			assertTrue(db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(3, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByBase(mb2id).size());
			assertTrue(db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			assertEquals(1, db.getUnitRecallsByBase(mb3id).size());
			assertTrue(db.getUnitRecallsByBase(mb3id).contains(ur5));
			
			assertEquals(1, db.getUnitRecallsByBase(mb4id).size());
			assertTrue(db.getUnitRecallsByBase(mb4id).contains(ur6));
			
			//get(tool/mutator)
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
			assertEquals(2, db.getUnitRecallsByToolAndMutator(t1id, m3id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur5));
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur6));
		
		db.deleteUnitRecall(t3id, m1id);
			//get
			assertEquals(ur1, db.getUnitRecall(t1id, mb1id));
			assertEquals(ur2, db.getUnitRecall(t2id, mb1id));
			assertEquals(null, db.getUnitRecall(t3id, mb1id));
			assertEquals(ur4, db.getUnitRecall(t1id, mb2id));
			assertEquals(ur5, db.getUnitRecall(t1id, mb3id));
			assertEquals(ur6, db.getUnitRecall(t1id, mb4id));
			
			//exists
			assertEquals(true, db.existsUnitRecall(t1id, mb1id));
			assertEquals(true, db.existsUnitRecall(t2id, mb1id));
			assertEquals(false, db.existsUnitRecall(t3id, mb1id));
			assertEquals(true, db.existsUnitRecall(t1id, mb2id));
			assertEquals(true, db.existsUnitRecall(t1id, mb3id));
			assertEquals(true, db.existsUnitRecall(t1id, mb4id));
			
			//gets
			assertEquals(5, db.getUnitRecalls().size());
			assertTrue(db.getUnitRecalls().contains(ur1));
			assertTrue(db.getUnitRecalls().contains(ur2));
			assertFalse(db.getUnitRecalls().contains(ur3));
			assertTrue(db.getUnitRecalls().contains(ur4));
			assertTrue(db.getUnitRecalls().contains(ur5));
			assertTrue(db.getUnitRecalls().contains(ur6));
			
			//gets(tool)
			assertEquals(4, db.getUnitRecallsByTool(t1id).size());
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur1));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur4));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur5));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur6));
			
			assertEquals(1, db.getUnitRecallsByTool(t2id).size());
			assertTrue(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByTool(t3id).size());
			assertFalse(db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(2, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertFalse(db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByBase(mb2id).size());
			assertTrue(db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			assertEquals(1, db.getUnitRecallsByBase(mb3id).size());
			assertTrue(db.getUnitRecallsByBase(mb3id).contains(ur5));
			
			assertEquals(1, db.getUnitRecallsByBase(mb4id).size());
			assertTrue(db.getUnitRecallsByBase(mb4id).contains(ur6));
			
			//get(tool/mutator)
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
			assertEquals(2, db.getUnitRecallsByToolAndMutator(t1id, m3id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur5));
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur6));
			
		db.deleteUnitRecallsByTool(t1id);
			//get
			assertEquals(null, db.getUnitRecall(t1id, mb1id));
			assertEquals(ur2, db.getUnitRecall(t2id, mb1id));
			assertEquals(null, db.getUnitRecall(t3id, mb1id));
			assertEquals(null, db.getUnitRecall(t1id, mb2id));
			assertEquals(null, db.getUnitRecall(t1id, mb3id));
			assertEquals(null, db.getUnitRecall(t1id, mb4id));
			
			//exists
			assertEquals(false, db.existsUnitRecall(t1id, mb1id));
			assertEquals(true, db.existsUnitRecall(t2id, mb1id));
			assertEquals(false, db.existsUnitRecall(t3id, mb1id));
			assertEquals(false, db.existsUnitRecall(t1id, mb2id));
			assertEquals(false, db.existsUnitRecall(t1id, mb3id));
			assertEquals(false, db.existsUnitRecall(t1id, mb4id));
			
			//gets
			assertEquals(1, db.getUnitRecalls().size());
			assertFalse(db.getUnitRecalls().contains(ur1));
			assertTrue(db.getUnitRecalls().contains(ur2));
			assertFalse(db.getUnitRecalls().contains(ur3));
			assertFalse(db.getUnitRecalls().contains(ur4));
			assertFalse(db.getUnitRecalls().contains(ur5));
			assertFalse(db.getUnitRecalls().contains(ur6));
			
			//gets(tool)
			assertEquals(0, db.getUnitRecallsByTool(t1id).size());
			assertFalse(db.getUnitRecallsByTool(t1id).contains(ur1));
			assertFalse(db.getUnitRecallsByTool(t1id).contains(ur4));
			assertFalse(db.getUnitRecallsByTool(t1id).contains(ur5));
			assertFalse(db.getUnitRecallsByTool(t1id).contains(ur6));
			
			assertEquals(1, db.getUnitRecallsByTool(t2id).size());
			assertTrue(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByTool(t3id).size());
			assertFalse(db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(1, db.getUnitRecallsByBase(mb1id).size());
			assertFalse(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertFalse(db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(0, db.getUnitRecallsByBase(mb2id).size());
			assertFalse(db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			assertEquals(0, db.getUnitRecallsByBase(mb3id).size());
			assertFalse(db.getUnitRecallsByBase(mb3id).contains(ur5));
			
			assertEquals(0, db.getUnitRecallsByBase(mb4id).size());
			assertFalse(db.getUnitRecallsByBase(mb4id).contains(ur6));
			
			//get(tool/mutator)
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m3id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur5));
			assertFalse(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur6));

		db.deleteUnitRecall(t2id, mb1id);
			//get
			assertEquals(null, db.getUnitRecall(t1id, mb1id));
			assertEquals(null, db.getUnitRecall(t2id, mb1id));
			assertEquals(null, db.getUnitRecall(t3id, mb1id));
			assertEquals(null, db.getUnitRecall(t1id, mb2id));
			assertEquals(null, db.getUnitRecall(t1id, mb3id));
			assertEquals(null, db.getUnitRecall(t1id, mb4id));
			
			//exists
			assertEquals(false, db.existsUnitRecall(t1id, mb1id));
			assertEquals(false, db.existsUnitRecall(t2id, mb1id));
			assertEquals(false, db.existsUnitRecall(t3id, mb1id));
			assertEquals(false, db.existsUnitRecall(t1id, mb2id));
			assertEquals(false, db.existsUnitRecall(t1id, mb3id));
			assertEquals(false, db.existsUnitRecall(t1id, mb4id));
			
			//gets
			assertEquals(0, db.getUnitRecalls().size());
			assertFalse(db.getUnitRecalls().contains(ur1));
			assertFalse(db.getUnitRecalls().contains(ur2));
			assertFalse(db.getUnitRecalls().contains(ur3));
			assertFalse(db.getUnitRecalls().contains(ur4));
			assertFalse(db.getUnitRecalls().contains(ur5));
			assertFalse(db.getUnitRecalls().contains(ur6));
			
			//gets(tool)
			assertEquals(0, db.getUnitRecallsByTool(t1id).size());
			assertFalse(db.getUnitRecallsByTool(t1id).contains(ur1));
			assertFalse(db.getUnitRecallsByTool(t1id).contains(ur4));
			assertFalse(db.getUnitRecallsByTool(t1id).contains(ur5));
			assertFalse(db.getUnitRecallsByTool(t1id).contains(ur6));
			
			assertEquals(0, db.getUnitRecallsByTool(t2id).size());
			assertFalse(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByTool(t3id).size());
			assertFalse(db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(0, db.getUnitRecallsByBase(mb1id).size());
			assertFalse(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertFalse(db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertFalse(db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(0, db.getUnitRecallsByBase(mb2id).size());
			assertFalse(db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			assertEquals(0, db.getUnitRecallsByBase(mb3id).size());
			assertFalse(db.getUnitRecallsByBase(mb3id).contains(ur5));
			
			assertEquals(0, db.getUnitRecallsByBase(mb4id).size());
			assertFalse(db.getUnitRecallsByBase(mb4id).contains(ur6));
			
			//get(tool/mutator)
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m3id).size());
			assertFalse(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur5));
			assertFalse(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur6));
	
		ur1 = db.createUnitRecall(t1id, mb1id, 1.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		ur2 = db.createUnitRecall(t2id, mb1id, 1.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		ur3 = db.createUnitRecall(t3id, mb1id, 0.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		ur4 = db.createUnitRecall(t1id, mb2id, 0.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		ur5 = db.createUnitRecall(t1id, mb3id, 1.0, new Clone(mb3.getOriginalFragment(), mb3.getMutantFragment()));
		ur6 = db.createUnitRecall(t1id, mb4id, 0.0, new Clone(mb4.getOriginalFragment(), mb4.getMutantFragment()));
			
			//get
			assertEquals(ur1, db.getUnitRecall(t1id, mb1id));
			assertEquals(ur2, db.getUnitRecall(t2id, mb1id));
			assertEquals(ur3, db.getUnitRecall(t3id, mb1id));
			assertEquals(ur4, db.getUnitRecall(t1id, mb2id));
			assertEquals(ur5, db.getUnitRecall(t1id, mb3id));
			assertEquals(ur6, db.getUnitRecall(t1id, mb4id));
			
			//exists
			assertEquals(true, db.existsUnitRecall(t1id, mb1id));
			assertEquals(true, db.existsUnitRecall(t2id, mb1id));
			assertEquals(true, db.existsUnitRecall(t3id, mb1id));
			assertEquals(true, db.existsUnitRecall(t1id, mb2id));
			assertEquals(true, db.existsUnitRecall(t1id, mb3id));
			assertEquals(true, db.existsUnitRecall(t1id, mb4id));
			
			//gets
			assertEquals(6, db.getUnitRecalls().size());
			assertTrue(db.getUnitRecalls().contains(ur1));
			assertTrue(db.getUnitRecalls().contains(ur2));
			assertTrue(db.getUnitRecalls().contains(ur3));
			assertTrue(db.getUnitRecalls().contains(ur4));
			assertTrue(db.getUnitRecalls().contains(ur5));
			assertTrue(db.getUnitRecalls().contains(ur6));
			
			//gets(tool)
			assertEquals(4, db.getUnitRecallsByTool(t1id).size());
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur1));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur4));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur5));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur6));
			
			assertEquals(1, db.getUnitRecallsByTool(t2id).size());
			assertTrue(db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByTool(t3id).size());
			assertTrue(db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(3, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertTrue(db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByBase(mb2id).size());
			assertTrue(db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			assertEquals(1, db.getUnitRecallsByBase(mb3id).size());
			assertTrue(db.getUnitRecallsByBase(mb3id).contains(ur5));
			
			assertEquals(1, db.getUnitRecallsByBase(mb4id).size());
			assertTrue(db.getUnitRecallsByBase(mb4id).contains(ur6));
			
			//get(tool/mutator)
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
			assertEquals(2, db.getUnitRecallsByToolAndMutator(t1id, m3id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur5));
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur6));
			
		int numdel = db.deleteUnitRecallsByBase(mb1id);
		assertEquals(3, numdel);
	
			//get
			assertEquals(null, db.getUnitRecall(t1id, mb1id));
			assertEquals(null, db.getUnitRecall(t2id, mb1id));
			assertEquals(null, db.getUnitRecall(t3id, mb1id));
			assertEquals(ur4, db.getUnitRecall(t1id, mb2id));
			assertEquals(ur5, db.getUnitRecall(t1id, mb3id));
			assertEquals(ur6, db.getUnitRecall(t1id, mb4id));
			
			//exists
			assertEquals(false, db.existsUnitRecall(t1id, mb1id));
			assertEquals(false, db.existsUnitRecall(t2id, mb1id));
			assertEquals(false, db.existsUnitRecall(t3id, mb1id));
			assertEquals(true, db.existsUnitRecall(t1id, mb2id));
			assertEquals(true, db.existsUnitRecall(t1id, mb3id));
			assertEquals(true, db.existsUnitRecall(t1id, mb4id));
			
			//gets
			assertEquals(3, db.getUnitRecalls().size());
			assertTrue(!db.getUnitRecalls().contains(ur1));
			assertTrue(!db.getUnitRecalls().contains(ur2));
			assertTrue(!db.getUnitRecalls().contains(ur3));
			assertTrue(db.getUnitRecalls().contains(ur4));
			assertTrue(db.getUnitRecalls().contains(ur5));
			assertTrue(db.getUnitRecalls().contains(ur6));
			
			//gets(tool)
			assertEquals(3, db.getUnitRecallsByTool(t1id).size());
			assertTrue(!db.getUnitRecallsByTool(t1id).contains(ur1));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur4));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur5));
			assertTrue(db.getUnitRecallsByTool(t1id).contains(ur6));
			
			assertEquals(0, db.getUnitRecallsByTool(t2id).size());
			assertTrue(!db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByTool(t3id).size());
			assertTrue(!db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(0, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(!db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(!db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertTrue(!db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByBase(mb2id).size());
			assertTrue(db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			assertEquals(1, db.getUnitRecallsByBase(mb3id).size());
			assertTrue(db.getUnitRecallsByBase(mb3id).contains(ur5));
			
			assertEquals(1, db.getUnitRecallsByBase(mb4id).size());
			assertTrue(db.getUnitRecallsByBase(mb4id).contains(ur6));
			
			//get(tool/mutator)
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(!db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(!db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertTrue(!db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(1, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
			assertEquals(2, db.getUnitRecallsByToolAndMutator(t1id, m3id).size());
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur5));
			assertTrue(db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur6));
			
		numdel = db.deleteUnitRecalls();
		assertEquals(3, numdel);
			//get
			assertEquals(null, db.getUnitRecall(t1id, mb1id));
			assertEquals(null, db.getUnitRecall(t2id, mb1id));
			assertEquals(null, db.getUnitRecall(t3id, mb1id));
			assertEquals(null, db.getUnitRecall(t1id, mb2id));
			assertEquals(null, db.getUnitRecall(t1id, mb3id));
			assertEquals(null, db.getUnitRecall(t1id, mb4id));
			
			//exists
			assertEquals(false, db.existsUnitRecall(t1id, mb1id));
			assertEquals(false, db.existsUnitRecall(t2id, mb1id));
			assertEquals(false, db.existsUnitRecall(t3id, mb1id));
			assertEquals(false, db.existsUnitRecall(t1id, mb2id));
			assertEquals(false, db.existsUnitRecall(t1id, mb3id));
			assertEquals(false, db.existsUnitRecall(t1id, mb4id));
			
			//gets
			assertEquals(0, db.getUnitRecalls().size());
			assertTrue(!db.getUnitRecalls().contains(ur1));
			assertTrue(!db.getUnitRecalls().contains(ur2));
			assertTrue(!db.getUnitRecalls().contains(ur3));
			assertTrue(!db.getUnitRecalls().contains(ur4));
			assertTrue(!db.getUnitRecalls().contains(ur5));
			assertTrue(!db.getUnitRecalls().contains(ur6));
			
			//gets(tool)
			assertEquals(0, db.getUnitRecallsByTool(t1id).size());
			assertTrue(!db.getUnitRecallsByTool(t1id).contains(ur1));
			assertTrue(!db.getUnitRecallsByTool(t1id).contains(ur4));
			assertTrue(!db.getUnitRecallsByTool(t1id).contains(ur5));
			assertTrue(!db.getUnitRecallsByTool(t1id).contains(ur6));
			
			assertEquals(0, db.getUnitRecallsByTool(t2id).size());
			assertTrue(!db.getUnitRecallsByTool(t2id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByTool(t3id).size());
			assertTrue(!db.getUnitRecallsByTool(t3id).contains(ur3));
			
			//gets(base)
			assertEquals(0, db.getUnitRecallsByBase(mb1id).size());
			assertTrue(!db.getUnitRecallsByBase(mb1id).contains(ur1));
			assertTrue(!db.getUnitRecallsByBase(mb1id).contains(ur2));
			assertTrue(!db.getUnitRecallsByBase(mb1id).contains(ur3));
			
			assertEquals(0, db.getUnitRecallsByBase(mb2id).size());
			assertTrue(!db.getUnitRecallsByBase(mb2id).contains(ur4));
			
			assertEquals(0, db.getUnitRecallsByBase(mb3id).size());
			assertTrue(!db.getUnitRecallsByBase(mb3id).contains(ur5));
			
			assertEquals(0, db.getUnitRecallsByBase(mb4id).size());
			assertTrue(!db.getUnitRecallsByBase(mb4id).contains(ur6));
			
			//get(tool/mutator)
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m1id).size());
			assertTrue(!db.getUnitRecallsByToolAndMutator(t1id, m1id).contains(ur1));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t2id, m1id).size());
			assertTrue(!db.getUnitRecallsByToolAndMutator(t2id, m1id).contains(ur2));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t3id, m1id).size());
			assertTrue(!db.getUnitRecallsByToolAndMutator(t3id, m1id).contains(ur3));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m2id).size());
			assertTrue(!db.getUnitRecallsByToolAndMutator(t1id, m2id).contains(ur4));
			
			assertEquals(0, db.getUnitRecallsByToolAndMutator(t1id, m3id).size());
			assertTrue(!db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur5));
			assertTrue(!db.getUnitRecallsByToolAndMutator(t1id, m3id).contains(ur6));
			
	//test error conditions
		boolean error;
		
		//create errors
		error = false;
		try {
			db.createUnitRecall(t1id, mb1id, 1.0, null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		db.deleteUnitRecalls();
		ur1 = db.createUnitRecall(t1id, mb1id, 1.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		error = false;
		try {
			ur1 = db.createUnitRecall(t1id, mb1id, 1.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		db.deleteUnitRecalls();
		
		error = false;
		try {
			db.createUnitRecall(9999, mb1id, 0.6, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitRecall(t1id, 9999, 0.6, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitRecall(t1id, mb1id, 2.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitRecall(t1id, mb1id, -1.0, new Clone(mb1.getOriginalFragment(), mb1.getMutantFragment()));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitRecall(t1id, mb1id, 0.6, new Clone(new Fragment(Paths.get("/dir/file"), 1, 10), new Fragment(db.getMutantBasePath().resolve("file2"), 2, 20)));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitRecall(t1id, mb1id, 0.6, new Clone(new Fragment((db.getMutantBasePath().resolve("file")), 1, 10), new Fragment(Paths.get("/dir/file"), 2, 20)));
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
	}
	
	@Test
	public void testUnitPrecision() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
//Prep
	//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
								
	//Create Experiment Data
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
	//Operators
		OperatorDB op1 = db.createOperator("Name1", "Description1", 1, SystemUtil.getOperatorsPath().resolve("mCC_EOL"));
		OperatorDB op2 = db.createOperator("Name2", "Description2", 2, SystemUtil.getOperatorsPath().resolve("mRL_S"));
		OperatorDB op3 = db.createOperator("Name3", "Description3", 3, SystemUtil.getOperatorsPath().resolve("mSIL"));
		OperatorDB op4 = db.createOperator("Name4", "Description4", 1, SystemUtil.getOperatorsPath().resolve("mCC_BT"));

		//Check Them
		int op1id = op1.getId();
		assertEquals("Name1", op1.getName());
		assertEquals("Description1", op1.getDescription());
		assertEquals(1, op1.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_EOL").toAbsolutePath().normalize(), op1.getMutator());
		int op2id = op2.getId();
		assertEquals("Name2", op2.getName());
		assertEquals("Description2", op2.getDescription());
		assertEquals(2, op2.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mRL_S").toAbsolutePath().normalize(), op2.getMutator());
		int op3id = op3.getId();
		assertEquals("Name3", op3.getName());
		assertEquals("Description3", op3.getDescription());
		assertEquals(3, op3.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mSIL").toAbsolutePath().normalize(), op3.getMutator());
		int op4id = op4.getId();
		assertEquals("Name4", op4.getName());
		assertEquals("Description4", op4.getDescription());
		assertEquals(1, op4.getTargetCloneType());
		assertEquals(SystemUtil.getOperatorsPath().resolve("mCC_BT").toAbsolutePath().normalize(), op4.getMutator());

			//exists
			assertTrue(db.existsOperator(op1id));
			assertTrue(db.existsOperator(op2id));
			assertTrue(db.existsOperator(op3id));
			assertTrue(db.existsOperator(op4id));

			//num
			assertTrue(db.numOperators() == 4);

			//get
			assertEquals(op1, db.getOperator(op1id));
			assertEquals(op2, db.getOperator(op2id));
			assertEquals(op3, db.getOperator(op3id));
			assertEquals(op4, db.getOperator(op4id));
			
			//gets
			assertTrue(db.getOperators().size() == 4);
			assertEquals(op1, db.getOperators().get(0));
			assertEquals(op2, db.getOperators().get(1));
			assertEquals(op3, db.getOperators().get(2));
			assertEquals(op4, db.getOperators().get(3));
					
			//getids
			assertTrue(db.getOperatorIds().size() == 4);
			assertEquals(new Integer(op1id), db.getOperatorIds().get(0));
			assertEquals(new Integer(op2id), db.getOperatorIds().get(1));
			assertEquals(new Integer(op3id), db.getOperatorIds().get(2));
			assertEquals(new Integer(op4id), db.getOperatorIds().get(3));
		
	//Mutators
		List<Integer> oplist = new LinkedList<Integer>();

		oplist.add(op1id);
		MutatorDB m1 = db.createMutator("m1", oplist);
		oplist.add(op2id);
		MutatorDB m2 = db.createMutator("m2", oplist);
		oplist.add(op3id);
		MutatorDB m3 = db.createMutator("m3", oplist);
		oplist.add(op4id);
		MutatorDB m4 = db.createMutator("m4", oplist);

		//Check
			int m1id = m1.getId();
			assertEquals("m1", m1.getDescription());
			assertTrue(db.existsMutator(m1id));
			assertEquals(1, m1.getTargetCloneType());
			assertTrue(m1.getOperators().size() == 1);
			assertTrue(m1.getOperators().get(0).equals(op1));
			int m2id = m2.getId();
			assertEquals("m2", m2.getDescription());
			assertTrue(db.existsMutator(m2id));
			assertEquals(2, m2.getTargetCloneType());
			assertTrue(m2.getOperators().size() == 2);
			assertTrue(m2.getOperators().get(0).equals(op1));
			assertTrue(m2.getOperators().get(1).equals(op2));
			int m3id = m3.getId();
			assertEquals("m3", m3.getDescription());
			assertTrue(db.existsMutator(m3id));
			assertEquals(3, m3.getTargetCloneType());
			assertTrue(m3.getOperators().size() == 3);
			assertTrue(m3.getOperators().get(0).equals(op1));
			assertTrue(m3.getOperators().get(1).equals(op2));
			assertTrue(m3.getOperators().get(2).equals(op3));
			int m4id = m4.getId();
			assertEquals("m4", m4.getDescription());
			assertTrue(db.existsMutator(m4id));
			assertEquals(3, m4.getTargetCloneType());
			assertTrue(m4.getOperators().size() == 4);
			assertTrue(m4.getOperators().get(0).equals(op1));
			assertTrue(m4.getOperators().get(1).equals(op2));
			assertTrue(m4.getOperators().get(2).equals(op3));
			assertTrue(m4.getOperators().get(3).equals(op4));

				//get
				assertEquals(m1, db.getMutator(m1id));
				assertEquals(m2, db.getMutator(m2id));
				assertEquals(m3, db.getMutator(m3id));
				assertEquals(m4, db.getMutator(m4id));
								
				//exists
				assertTrue(db.existsMutator(m1id));
				assertTrue(db.existsMutator(m2id));
				assertTrue(db.existsMutator(m3id));
				assertTrue(db.existsMutator(m4id));
								
				//num
				assertTrue(db.numMutators() == 4);
					
				//getsid
				assertTrue(db.getMutatorIds().size() == 4);
				assertTrue(db.getMutatorIds().get(0).equals(m1id));
				assertTrue(db.getMutatorIds().get(1).equals(m2id));
				assertTrue(db.getMutatorIds().get(2).equals(m3id));
				assertTrue(db.getMutatorIds().get(3).equals(m4id));
						
				//gets
				assertTrue(db.getMutators().size()==4);
				assertTrue(db.getMutators().get(0).equals(m1));
				assertTrue(db.getMutators().get(1).equals(m2));
				assertTrue(db.getMutators().get(2).equals(m3));
				assertTrue(db.getMutators().get(3).equals(m4));

	
		boolean stageError = false;
		try {
			db.createUnitPrecision(1, 1, 1.0, null);
		} catch (IllegalStateException e) {
			stageError = true;
		}
		assertTrue(stageError);
		
	//generation phase
		db.nextStage();
		stageError = false;
		try {
			db.createUnitPrecision(1, 1, 1.0, null);
		} catch (IllegalStateException e) {
			stageError = true;
		}
		assertTrue(stageError);
				
	//Fragments
		Fragment frag1 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java"), 493, 514);
		FragmentDB f1 = db.createFragment(frag1);
		Fragment frag2 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java"), 58, 73);
		FragmentDB f2 = db.createFragment(frag2);
		Fragment frag3 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 330, 366);
		FragmentDB f3 = db.createFragment(frag3);
		Fragment frag4 = new Fragment(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java"), 449, 486);
		FragmentDB f4 = db.createFragment(frag4);

			int f1id = f1.getId();
			assertEquals(f1id, f1.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/xml/sax/helpers/AttributesImpl.java").toAbsolutePath().normalize(), f1.getSrcFile());
			assertEquals(493, f1.getStartLine());
			assertEquals(514, f1.getEndLine());

			int f2id = f2.getId();
			assertEquals(f2id, f2.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/omg/DynamicAny/DynStructHelper.java").toAbsolutePath().normalize(), f2.getSrcFile());
			assertEquals(58, f2.getStartLine());
			assertEquals(73, f2.getEndLine());

			int f3id = f3.getId();
			assertEquals(f3id, f3.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f3.getSrcFile());
			assertEquals(330, f3.getStartLine());
			assertEquals(366, f3.getEndLine());

			int f4id = f4.getId();
			assertEquals(f4id, f4.getId());
			assertEquals(Paths.get("testdata/ExperimentDataTest/exprdata/repository/org/w3c/dom/bootstrap/DOMImplementationRegistry.java").toAbsolutePath().normalize(), f4.getSrcFile());
			assertEquals(449, f4.getStartLine());
			assertEquals(486, f4.getEndLine());

			//exists
			assertTrue(db.existsFragment(f1id));
			assertTrue(db.existsFragment(f2id));
			assertTrue(db.existsFragment(f3id));
			assertTrue(db.existsFragment(f4id));
							
			//get
			assertEquals(f1, db.getFragment(f1id));
			assertEquals(f2, db.getFragment(f2id));
			assertEquals(f3, db.getFragment(f3id));
			assertEquals(f4, db.getFragment(f4id));
							
			//gets
			assertTrue(db.getFragments().size() == 4);
			assertEquals(f1, db.getFragments().get(0));
			assertEquals(f2, db.getFragments().get(1));
			assertEquals(f3, db.getFragments().get(2));
			assertEquals(f4, db.getFragments().get(3));
							
			//num
			assertTrue(db.numFragments() == 4);
					
			//getids
			assertTrue(db.getFragmentIds().size() == 4);
			assertEquals(new Integer(f1id), db.getFragmentIds().get(0));
			assertEquals(new Integer(f2id), db.getFragmentIds().get(1));
			assertEquals(new Integer(f3id), db.getFragmentIds().get(2));
			assertEquals(new Integer(f4id), db.getFragmentIds().get(3));
							
			//fragment file
			Path tmpfile = SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/tmp");
			
			Path f1file = exprdata.resolve("fragments/" + f1id);
			assertTrue(Files.exists(f1file));
			assertTrue(Files.isRegularFile(f1file));
			FragmentUtil.extractFragment(frag1, tmpfile);
			assertTrue(FileUtils.contentEquals(f1file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
						
			Path f2file = exprdata.resolve("fragments/" + f2id);
			assertTrue(Files.exists(f2file));
			assertTrue(Files.isRegularFile(f2file));
			FragmentUtil.extractFragment(frag2, tmpfile);
			assertTrue(FileUtils.contentEquals(f2file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f3file = exprdata.resolve("fragments/" + f3id);
			assertTrue(Files.exists(f3file));
			assertTrue(Files.isReadable(f3file));
			FragmentUtil.extractFragment(frag3, tmpfile);
			assertTrue(FileUtils.contentEquals(f3file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
					
			Path f4file = exprdata.resolve("fragments/" + f4id);
			assertTrue(Files.exists(f4file));
			assertTrue(Files.isReadable(f4file));
			FragmentUtil.extractFragment(frag4, tmpfile);
			assertTrue(FileUtils.contentEquals(f4file.toFile(), tmpfile.toFile()));
			Files.deleteIfExists(tmpfile);
	
	//MutantFragments
	MutantFragment mf1 = db.createMutantFragment(f1id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one"));
			int mf1id = mf1.getId();
			assertEquals(m1id, mf1.getMutatorId());
			assertEquals(f1id, mf1.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf1id).toAbsolutePath().normalize(),mf1.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/one").toFile(), mf1.getFragmentFile().toFile()));
		MutantFragment mf2 = db.createMutantFragment(f1id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two"));
			int mf2id = mf2.getId();
			assertEquals(m2id, mf2.getMutatorId());
			assertEquals(f1id, mf2.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf2id).toAbsolutePath().normalize(),mf2.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/two").toFile(), mf2.getFragmentFile().toFile()));		
		MutantFragment mf3 = db.createMutantFragment(f1id, m3id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three"));
			int mf3id = mf3.getId();
			assertEquals(m3id, mf3.getMutatorId());
			assertEquals(f1id, mf3.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf3id).toAbsolutePath().normalize(),mf3.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/three").toFile(), mf3.getFragmentFile().toFile()));
		MutantFragment mf4 = db.createMutantFragment(f2id, m1id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four"));
			int mf4id = mf4.getId();
			assertEquals(m1id, mf4.getMutatorId());
			assertEquals(f2id, mf4.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf4id).toAbsolutePath().normalize(),mf4.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/four").toFile(), mf4.getFragmentFile().toFile()));
		MutantFragment mf5 = db.createMutantFragment(f2id, m2id, SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five"));
			int mf5id = mf5.getId();
			assertEquals(m2id, mf5.getMutatorId());
			assertEquals(f2id, mf5.getFragmentId());
			assertEquals(db.getMutantFragmentsPath().resolve("" + mf5id).toAbsolutePath().normalize(),mf5.getFragmentFile().toAbsolutePath().normalize());
			assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/MutantFragmentSamples/five").toFile(), mf5.getFragmentFile().toFile()));
				
		//get
		assertEquals(mf1, db.getMutantFragment(mf1id));
		assertEquals(mf2, db.getMutantFragment(mf2id));
		assertEquals(mf3, db.getMutantFragment(mf3id));
		assertEquals(mf4, db.getMutantFragment(mf4id));
		assertEquals(mf5, db.getMutantFragment(mf5id));
			
		//exists
		assertEquals(true, db.existsMutantFragment(mf1id));
		assertEquals(true, db.existsMutantFragment(mf2id));
		assertEquals(true, db.existsMutantFragment(mf3id));
		assertEquals(true, db.existsMutantFragment(mf4id));
		assertEquals(true, db.existsMutantFragment(mf5id));
					
		//gets
		assertTrue(db.getMutantFragments().size() == 5);
		assertTrue(db.getMutantFragments().contains(mf1));
		assertTrue(db.getMutantFragments().contains(mf2));
		assertTrue(db.getMutantFragments().contains(mf3));
		assertTrue(db.getMutantFragments().contains(mf4));
		assertTrue(db.getMutantFragments().contains(mf5));
					
		//gets(fid)
		assertTrue(db.getMutantFragments(f1id).size() == 3);
		assertTrue(db.getMutantFragments(f1id).contains(mf1));
		assertTrue(db.getMutantFragments(f1id).contains(mf2));
		assertTrue(db.getMutantFragments(f1id).contains(mf3));
					
		assertTrue(db.getMutantFragments(f2id).size() == 2);
		assertTrue(db.getMutantFragments(f2id).contains(mf4));
		assertTrue(db.getMutantFragments(f2id).contains(mf5));
					
		//num
		assertTrue(db.numMutantFragments() == 5);
			
		//getid
		assertTrue(db.getMutantFragmentIds().size() == 5);
		assertTrue(db.getMutantFragmentIds().contains(mf1id));
		assertTrue(db.getMutantFragmentIds().contains(mf2id));
		assertTrue(db.getMutantFragmentIds().contains(mf3id));
		assertTrue(db.getMutantFragmentIds().contains(mf4id));
		assertTrue(db.getMutantFragmentIds().contains(mf5id));
					
		//getid(fid)
		assertTrue(db.getMutantFragmentIds(f1id).size() == 3);
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf1id));
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf2id));
		assertTrue(db.getMutantFragmentIds(f1id).contains(mf3id));
					
		assertTrue(db.getMutantFragmentIds(f2id).size() == 2);
		assertTrue(db.getMutantFragmentIds(f2id).contains(mf4id));
		assertTrue(db.getMutantFragmentIds(f2id).contains(mf5id));
		
			
		//MutantBases
		//empty
		assertEquals(null, db.getMutantBase(1));
		assertEquals(0, db.getMutantBases().size());
		assertEquals(false, db.existsMutantBase(1));
		assertEquals(0, db.numMutantBases());
		assertEquals(0, db.getMutantBaseIds().size());
	
		MutantBaseDB mb1 = null;
		MutantBaseDB mb2 = null;
		MutantBaseDB mb3 = null;
		MutantBaseDB mb4 = null;
		
		int mb1id = 1;
		int mb2id = 2;
		int mb3id = 3;
		int mb4id = 4;

		mb1 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 55, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, mf1id);
		mb2 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/util/InetAddressUtils.java"), 53, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/Main.java"), 80, mf2id);
		mb3 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, mf3id);
		mb4 = db.createMutantBase(Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java"), 73, Paths.get("testdata/ExperimentDataTest/exprdata/system/angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java"), 58, mf3id);
			mb1id = mb1.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb1.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize(), 55, 76), mb1.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/util/InetAddressUtils.java").toAbsolutePath().normalize(), 53, 74), mb1.getMutantFragment());
			assertEquals(m1id, mb1.getMutantId());
			mb2id = mb2.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb2.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/util/InetAddressUtils.java").toAbsolutePath().normalize(), 53, 74), mb2.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/Main.java").toAbsolutePath().normalize(), 80, 101), mb2.getMutantFragment());
			assertEquals(m2id, mb2.getMutantId());
			mb3id = mb3.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb3.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java").toAbsolutePath().normalize(), 58, 79), mb3.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java").toAbsolutePath().normalize(), 73, 95), mb3.getMutantFragment());
			assertEquals(m3id, mb3.getMutantId());
			mb4id = mb4.getId();
			assertEquals(db.getMutantBasePath().toAbsolutePath().normalize(), mb4.getDirectory());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RandomFeeder.java").toAbsolutePath().normalize(), 73, 94), mb4.getOriginalFragment());
			assertEquals(new Fragment(db.getMutantBasePath().resolve("angryipscanner/net/azib/ipscan/feeders/RescanFeeder.java").toAbsolutePath().normalize(), 58, 80), mb4.getMutantFragment());
			assertEquals(m3id, mb4.getMutantId());
		
			//get
			assertEquals(mb1, db.getMutantBase(mb1id));
			assertEquals(mb2, db.getMutantBase(mb2id));
			assertEquals(mb3, db.getMutantBase(mb3id));
			assertEquals(mb4, db.getMutantBase(mb4id));
			
			//gets
			assertEquals(4, db.getMutantBases().size());
			assertEquals(true, db.getMutantBases().contains(mb1));
			assertEquals(true, db.getMutantBases().contains(mb2));
			assertEquals(true, db.getMutantBases().contains(mb3));
			assertEquals(true, db.getMutantBases().contains(mb4));
			
			//exists
			assertEquals(true, db.existsMutantBase(mb1id));
			assertEquals(true, db.existsMutantBase(mb2id));
			assertEquals(true, db.existsMutantBase(mb3id));
			assertEquals(true, db.existsMutantBase(mb4id));
			
			//num
			assertEquals(4,db.numMutantBases());
			
			//getids
			assertEquals(4, db.getMutantBaseIds().size());
			assertEquals(true, db.getMutantBaseIds().contains(mb1id));
			assertEquals(true, db.getMutantBaseIds().contains(mb2id));
			assertEquals(true, db.getMutantBaseIds().contains(mb3id));
			assertEquals(true, db.getMutantBaseIds().contains(mb4id));

		//next
		db.nextStage();
			
		//Tools
		//Empty
		assertEquals(null, db.getTool(1));
		assertEquals(0, db.getTools().size());
		assertFalse(db.existsTool(1));
		assertEquals(0, db.numTools());
		assertEquals(0, db.getToolIds().size());
		
		int t1id = 1;
		int t2id = 2;
		int t3id = 3;
		int t4id = 4;
		ToolDB t1 = null;
		ToolDB t2 = null;
		ToolDB t3 = null;
		ToolDB t4 = null;
		
		t1 = db.createTool("NiCad", "NiCadDescription", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"));
			t1id = t1.getId();
			assertEquals("NiCad", t1.getName());
			assertEquals("NiCadDescription", t1.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad/"), t1.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner/NiCadRunner"), t1.getToolRunner());
		t2 = db.createTool("NiCad2", "NiCadDescription2", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad2/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner2/NiCadRunner"));
			t2id = t2.getId();
			assertEquals("NiCad2", t2.getName());
			assertEquals("NiCadDescription2", t2.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad2/"), t2.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner2/NiCadRunner"), t2.getToolRunner());
		t3 = db.createTool("NiCad3", "NiCadDescription3", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad3/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner3/NiCadRunner"));
			t3id = t3.getId();
			assertEquals("NiCad3", t3.getName());
			assertEquals("NiCadDescription3", t3.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad3/"), t3.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner3/NiCadRunner"), t3.getToolRunner());
		t4 = db.createTool("NiCad4", "NiCadDescription4", SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad4/"), SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner4/NiCadRunner"));
			t4id = t4.getId();
			assertEquals("NiCad4", t4.getName());
			assertEquals("NiCadDescription4", t4.getDescription());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/Tools/NiCad4/"), t4.getDirectory());
			assertEquals(SystemUtil.getInstallRoot().resolve("testdata/ExperimentDataTest/ToolSamples/ToolRunners/NiCadRunner4/NiCadRunner"), t4.getToolRunner());
		
			//get
			assertEquals(t1, db.getTool(t1id));
			assertEquals(t2, db.getTool(t2id));
			assertEquals(t3, db.getTool(t3id));
			assertEquals(t4, db.getTool(t4id));
			
			//gets
			assertEquals(4, db.getTools().size());
			assertEquals(true, db.getTools().contains(t1));
			assertEquals(true, db.getTools().contains(t2));
			assertEquals(true, db.getTools().contains(t3));
			assertEquals(true, db.getTools().contains(t4));
			
			//exists
			assertEquals(true, db.existsTool(t1id));
			assertEquals(true, db.existsTool(t2id));
			assertEquals(true, db.existsTool(t3id));
			assertEquals(true, db.existsTool(t4id));
			
			//num
			assertEquals(4, db.numTools());
			
			//getids
			assertEquals(4, db.getToolIds().size());
			assertEquals(true, db.getToolIds().contains(t1id));
			assertEquals(true, db.getToolIds().contains(t2id));
			assertEquals(true, db.getToolIds().contains(t3id));
			assertEquals(true, db.getToolIds().contains(t4id));
		
	//Test UnitPrecision
		UnitPrecision up1;
		UnitPrecision up2;
		UnitPrecision up3;
		UnitPrecision up4;
		UnitPrecision up5;
		UnitPrecision up6;
		UnitPrecision up7;
		UnitPrecision up8;
		
		//empty
			//get
			assertEquals(null, db.getUnitPrecision(t1id, mb1id));
			assertEquals(null, db.getUnitPrecision(t1id, mb2id));
			assertEquals(null, db.getUnitPrecision(t1id, mb3id));
			assertEquals(null, db.getUnitPrecision(t1id, mb4id));
			assertEquals(null, db.getUnitPrecision(t2id, mb1id));
			assertEquals(null, db.getUnitPrecision(t2id, mb2id));
			assertEquals(null, db.getUnitPrecision(t2id, mb3id));
			assertEquals(null, db.getUnitPrecision(t2id, mb4id));
		
			//exists
			assertEquals(false, db.existsUnitPrecision(t1id, mb1id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb4id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb1id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
			
			//gets
			assertEquals(0, db.getUnitPrecisions().size());
			
			//getsForTool
			assertEquals(0, db.getUnitPrecisionForTool(t1id).size());
			
			assertEquals(0, db.getUnitPrecisionForTool(t2id).size());
			
			//getForToolAndMutator
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
			
		List<VerifiedClone> clonelist1 = new LinkedList<VerifiedClone>();
		
		up1 = db.createUnitPrecision(t1id, mb1id, 1.00, clonelist1);
			assertEquals(t1id, up1.getToolid());
			assertEquals(mb1id, up1.getBaseid());
			assertEquals(1.00, up1.getPrecision(), 0.00001);
			assertEquals(clonelist1, up1.getClones());
			
			//get
			assertEquals(up1, db.getUnitPrecision(t1id, mb1id));
			assertEquals(null, db.getUnitPrecision(t1id, mb2id));
			assertEquals(null, db.getUnitPrecision(t1id, mb3id));
			assertEquals(null, db.getUnitPrecision(t1id, mb4id));
			assertEquals(null, db.getUnitPrecision(t2id, mb1id));
			assertEquals(null, db.getUnitPrecision(t2id, mb2id));
			assertEquals(null, db.getUnitPrecision(t2id, mb3id));
			assertEquals(null, db.getUnitPrecision(t2id, mb4id));
		
			//exists
			assertEquals(true, db.existsUnitPrecision(t1id, mb1id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb4id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb1id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
			
			//gets
			assertEquals(1, db.getUnitPrecisions().size());
			assertEquals(true, db.getUnitPrecisions().contains(up1));
			
			//getsForTool
			assertEquals(1, db.getUnitPrecisionForTool(t1id).size());
			assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up1));
			
			assertEquals(0, db.getUnitPrecisionForTool(t2id).size());
			
			//getForToolAndMutator
			assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
				assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());


		List<VerifiedClone> clonelist2 = new LinkedList<VerifiedClone>();
		clonelist2.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
		clonelist2.add(new VerifiedClone(mb2.getOriginalFragment(), mb2.getMutantFragment(), true, false));
		
		up2 = db.createUnitPrecision(t1id, mb2id, 1.0, clonelist2);
			assertEquals(t1id, up2.getToolid());
			assertEquals(mb2id, up2.getBaseid());
			assertEquals(1.00, up2.getPrecision(), 0.00001);
			assertEquals(clonelist2, up2.getClones());
			
			//get
			assertEquals(up1, db.getUnitPrecision(t1id, mb1id));
			assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
			assertEquals(null, db.getUnitPrecision(t1id, mb3id));
			assertEquals(null, db.getUnitPrecision(t1id, mb4id));
			assertEquals(null, db.getUnitPrecision(t2id, mb1id));
			assertEquals(null, db.getUnitPrecision(t2id, mb2id));
			assertEquals(null, db.getUnitPrecision(t2id, mb3id));
			assertEquals(null, db.getUnitPrecision(t2id, mb4id));
		
			//exists
			assertEquals(true, db.existsUnitPrecision(t1id, mb1id));
			assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb4id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb1id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
			
			//gets
			assertEquals(2, db.getUnitPrecisions().size());
			assertEquals(true, db.getUnitPrecisions().contains(up1));
			assertEquals(true, db.getUnitPrecisions().contains(up2));
			
			//getsForTool
			assertEquals(2, db.getUnitPrecisionForTool(t1id).size());
			assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up1));
			assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
			
			assertEquals(0, db.getUnitPrecisionForTool(t2id).size());
			
			//getForToolAndMutator
			assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
				assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
			assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
				assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
		
		List<VerifiedClone> clonelist3 = new LinkedList<VerifiedClone>();
		clonelist3.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
		clonelist3.add(new VerifiedClone(mb2.getOriginalFragment(), mb2.getMutantFragment(), true, false));
		clonelist3.add(new VerifiedClone(mb3.getOriginalFragment(), mb3.getMutantFragment(), false, true));

		up3 = db.createUnitPrecision(t1id, mb3id, 0.30, clonelist3);
			assertEquals(t1id, up3.getToolid());
			assertEquals(mb3id, up3.getBaseid());
			assertEquals(0.30, up3.getPrecision(), 0.00001);
			assertEquals(clonelist3, up3.getClones());
			
			//get
			assertEquals(up1, db.getUnitPrecision(t1id, mb1id));
			assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
			assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
			assertEquals(null, db.getUnitPrecision(t1id, mb4id));
			assertEquals(null, db.getUnitPrecision(t2id, mb1id));
			assertEquals(null, db.getUnitPrecision(t2id, mb2id));
			assertEquals(null, db.getUnitPrecision(t2id, mb3id));
			assertEquals(null, db.getUnitPrecision(t2id, mb4id));
		
			//exists
			assertEquals(true, db.existsUnitPrecision(t1id, mb1id));
			assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
			assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb4id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb1id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
			
			//gets
			assertEquals(3, db.getUnitPrecisions().size());
			assertEquals(true, db.getUnitPrecisions().contains(up1));
			assertEquals(true, db.getUnitPrecisions().contains(up2));
			assertEquals(true, db.getUnitPrecisions().contains(up3));
			
			//getsForTool
			assertEquals(3, db.getUnitPrecisionForTool(t1id).size());
			assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up1));
			assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
			assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
			
			assertEquals(0, db.getUnitPrecisionForTool(t2id).size());
			
			//getForToolAndMutator
			assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
				assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
			assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
				assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
			assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
				assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
			
			List<VerifiedClone> clonelist4 = new LinkedList<VerifiedClone>();
			clonelist4.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
			clonelist4.add(new VerifiedClone(mb2.getOriginalFragment(), mb2.getMutantFragment(), true, false));
			clonelist4.add(new VerifiedClone(mb3.getOriginalFragment(), mb3.getMutantFragment(), false, true));
			clonelist4.add(new VerifiedClone(mb4.getOriginalFragment(), mb4.getMutantFragment(), true, true));

			up4 = db.createUnitPrecision(t1id, mb4id, 0.00, clonelist4);
				assertEquals(t1id, up4.getToolid());
				assertEquals(mb4id, up4.getBaseid());
				assertEquals(0.00, up4.getPrecision(), 0.00001);
				assertEquals(clonelist4, up4.getClones());
				
				//get
				assertEquals(up1, db.getUnitPrecision(t1id, mb1id));
				assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
				assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
				assertEquals(up4, db.getUnitPrecision(t1id, mb4id));
				assertEquals(null, db.getUnitPrecision(t2id, mb1id));
				assertEquals(null, db.getUnitPrecision(t2id, mb2id));
				assertEquals(null, db.getUnitPrecision(t2id, mb3id));
				assertEquals(null, db.getUnitPrecision(t2id, mb4id));
			
				//exists
				assertEquals(true, db.existsUnitPrecision(t1id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb4id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb1id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
				
				//gets
				assertEquals(4, db.getUnitPrecisions().size());
				assertEquals(true, db.getUnitPrecisions().contains(up1));
				assertEquals(true, db.getUnitPrecisions().contains(up2));
				assertEquals(true, db.getUnitPrecisions().contains(up3));
				assertEquals(true, db.getUnitPrecisions().contains(up4));
				
				//getsForTool
				assertEquals(4, db.getUnitPrecisionForTool(t1id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up1));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up4));
				
				assertEquals(0, db.getUnitPrecisionForTool(t2id).size());
				
				//getForToolAndMutator
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
				
			List<VerifiedClone> clonelist5 = new LinkedList<VerifiedClone>();
			clonelist5.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
			//clonelist4.add(new VerifiedClone(mb2.getOriginalFragment(), mb2.getMutantFragment(), true, false));
			//clonelist4.add(new VerifiedClone(mb3.getOriginalFragment(), mb3.getMutantFragment(), false, true));
			//clonelist4.add(new VerifiedClone(mb4.getOriginalFragment(), mb4.getMutantFragment(), true, true));

			up5 = db.createUnitPrecision(t2id, mb1id, 1.00, clonelist5);
				assertEquals(t2id, up5.getToolid());
				assertEquals(mb1id, up5.getBaseid());
				assertEquals(1.00, up5.getPrecision(), 0.00001);
				assertEquals(clonelist5, up5.getClones());
				
				//get
				assertEquals(up1, db.getUnitPrecision(t1id, mb1id));
				assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
				assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
				assertEquals(up4, db.getUnitPrecision(t1id, mb4id));
				assertEquals(up5, db.getUnitPrecision(t2id, mb1id));
				assertEquals(null, db.getUnitPrecision(t2id, mb2id));
				assertEquals(null, db.getUnitPrecision(t2id, mb3id));
				assertEquals(null, db.getUnitPrecision(t2id, mb4id));
			
				//exists
				assertEquals(true, db.existsUnitPrecision(t1id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb4id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb1id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
				
				//gets
				assertEquals(5, db.getUnitPrecisions().size());
				assertEquals(true, db.getUnitPrecisions().contains(up1));
				assertEquals(true, db.getUnitPrecisions().contains(up2));
				assertEquals(true, db.getUnitPrecisions().contains(up3));
				assertEquals(true, db.getUnitPrecisions().contains(up4));
				assertEquals(true, db.getUnitPrecisions().contains(up5));
				
				//getsForTool
				assertEquals(4, db.getUnitPrecisionForTool(t1id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up1));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up4));
				
				assertEquals(1, db.getUnitPrecisionForTool(t2id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up5));
				
				//getForToolAndMutator
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m1id).contains(up5));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
				
			List<VerifiedClone> clonelist6 = new LinkedList<VerifiedClone>();
			//clonelist5.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
			clonelist6.add(new VerifiedClone(mb2.getOriginalFragment(), mb2.getMutantFragment(), true, false));
			clonelist6.add(new VerifiedClone(mb3.getOriginalFragment(), mb3.getMutantFragment(), false, true));
			//clonelist4.add(new VerifiedClone(mb4.getOriginalFragment(), mb4.getMutantFragment(), true, true));

			up6 = db.createUnitPrecision(t2id, mb2id, 0.50, clonelist6);
				assertEquals(t2id, up6.getToolid());
				assertEquals(mb2id, up6.getBaseid());
				assertEquals(0.50, up6.getPrecision(), 0.00001);
				assertEquals(clonelist6, up6.getClones());
				
				//get
				assertEquals(up1, db.getUnitPrecision(t1id, mb1id));
				assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
				assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
				assertEquals(up4, db.getUnitPrecision(t1id, mb4id));
				assertEquals(up5, db.getUnitPrecision(t2id, mb1id));
				assertEquals(up6, db.getUnitPrecision(t2id, mb2id));
				assertEquals(null, db.getUnitPrecision(t2id, mb3id));
				assertEquals(null, db.getUnitPrecision(t2id, mb4id));
			
				//exists
				assertEquals(true, db.existsUnitPrecision(t1id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb4id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb2id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
				
				//gets
				assertEquals(6, db.getUnitPrecisions().size());
				assertEquals(true, db.getUnitPrecisions().contains(up1));
				assertEquals(true, db.getUnitPrecisions().contains(up2));
				assertEquals(true, db.getUnitPrecisions().contains(up3));
				assertEquals(true, db.getUnitPrecisions().contains(up4));
				assertEquals(true, db.getUnitPrecisions().contains(up5));
				assertEquals(true, db.getUnitPrecisions().contains(up6));
				
				//getsForTool
				assertEquals(4, db.getUnitPrecisionForTool(t1id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up1));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up4));
				
				assertEquals(2, db.getUnitPrecisionForTool(t2id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up5));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up6));
				
				//getForToolAndMutator
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m1id).contains(up5));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m2id).contains(up6));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
			
			List<VerifiedClone> clonelist7 = new LinkedList<VerifiedClone>();
			//clonelist5.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
			clonelist7.add(new VerifiedClone(mb2.getOriginalFragment(), mb2.getMutantFragment(), true, false));
			clonelist7.add(new VerifiedClone(mb3.getOriginalFragment(), mb3.getMutantFragment(), false, true));
			clonelist7.add(new VerifiedClone(mb4.getOriginalFragment(), mb4.getMutantFragment(), true, true));

			up7 = db.createUnitPrecision(t2id, mb3id, 0.75, clonelist7);
				assertEquals(t2id, up7.getToolid());
				assertEquals(mb3id, up7.getBaseid());
				assertEquals(0.75, up7.getPrecision(), 0.00001);
				assertEquals(clonelist7, up7.getClones());
				
				//get
				assertEquals(up1, db.getUnitPrecision(t1id, mb1id));
				assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
				assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
				assertEquals(up4, db.getUnitPrecision(t1id, mb4id));
				assertEquals(up5, db.getUnitPrecision(t2id, mb1id));
				assertEquals(up6, db.getUnitPrecision(t2id, mb2id));
				assertEquals(up7, db.getUnitPrecision(t2id, mb3id));
				assertEquals(null, db.getUnitPrecision(t2id, mb4id));
			
				//exists
				assertEquals(true, db.existsUnitPrecision(t1id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb4id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb3id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
				
				//gets
				assertEquals(7, db.getUnitPrecisions().size());
				assertEquals(true, db.getUnitPrecisions().contains(up1));
				assertEquals(true, db.getUnitPrecisions().contains(up2));
				assertEquals(true, db.getUnitPrecisions().contains(up3));
				assertEquals(true, db.getUnitPrecisions().contains(up4));
				assertEquals(true, db.getUnitPrecisions().contains(up5));
				assertEquals(true, db.getUnitPrecisions().contains(up6));
				assertEquals(true, db.getUnitPrecisions().contains(up7));
				
				//getsForTool
				assertEquals(4, db.getUnitPrecisionForTool(t1id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up1));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up4));
				
				assertEquals(3, db.getUnitPrecisionForTool(t2id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up5));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up6));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up7));
				
				//getForToolAndMutator
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m1id).contains(up5));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m2id).contains(up6));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up7));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
				
			List<VerifiedClone> clonelist8 = new LinkedList<VerifiedClone>();
			clonelist8.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
			clonelist8.add(new VerifiedClone(mb2.getOriginalFragment(), mb2.getMutantFragment(), true, false));
			clonelist8.add(new VerifiedClone(mb3.getOriginalFragment(), mb3.getMutantFragment(), false, true));
			clonelist8.add(new VerifiedClone(mb4.getOriginalFragment(), mb4.getMutantFragment(), true, true));

			up8 = db.createUnitPrecision(t2id, mb4id, 0.0, clonelist8);
				assertEquals(t2id, up8.getToolid());
				assertEquals(mb4id, up8.getBaseid());
				assertEquals(0.0, up8.getPrecision(), 0.00001);
				assertEquals(clonelist8, up8.getClones());
				
				//get
				assertEquals(up1, db.getUnitPrecision(t1id, mb1id));
				assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
				assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
				assertEquals(up4, db.getUnitPrecision(t1id, mb4id));
				assertEquals(up5, db.getUnitPrecision(t2id, mb1id));
				assertEquals(up6, db.getUnitPrecision(t2id, mb2id));
				assertEquals(up7, db.getUnitPrecision(t2id, mb3id));
				assertEquals(up8, db.getUnitPrecision(t2id, mb4id));
			
				//exists
				assertEquals(true, db.existsUnitPrecision(t1id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb4id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb4id));
				
				//gets
				assertEquals(8, db.getUnitPrecisions().size());
				assertEquals(true, db.getUnitPrecisions().contains(up1));
				assertEquals(true, db.getUnitPrecisions().contains(up2));
				assertEquals(true, db.getUnitPrecisions().contains(up3));
				assertEquals(true, db.getUnitPrecisions().contains(up4));
				assertEquals(true, db.getUnitPrecisions().contains(up5));
				assertEquals(true, db.getUnitPrecisions().contains(up6));
				assertEquals(true, db.getUnitPrecisions().contains(up7));
				assertEquals(true, db.getUnitPrecisions().contains(up8));
				
				//getsForTool
				assertEquals(4, db.getUnitPrecisionForTool(t1id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up1));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up4));
				
				assertEquals(4, db.getUnitPrecisionForTool(t2id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up5));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up6));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up7));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up8));
				
				//getForToolAndMutator
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m1id).contains(up5));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m2id).contains(up6));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up7));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up8));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
				
			db.deleteUnitPrecision(t1id, mb1id);
				//get
				assertEquals(null, db.getUnitPrecision(t1id, mb1id));
				assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
				assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
				assertEquals(up4, db.getUnitPrecision(t1id, mb4id));
				assertEquals(up5, db.getUnitPrecision(t2id, mb1id));
				assertEquals(up6, db.getUnitPrecision(t2id, mb2id));
				assertEquals(up7, db.getUnitPrecision(t2id, mb3id));
				assertEquals(up8, db.getUnitPrecision(t2id, mb4id));
			
				//exists
				assertEquals(false, db.existsUnitPrecision(t1id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb4id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb4id));
				
				//gets
				assertEquals(7, db.getUnitPrecisions().size());
				assertEquals(false, db.getUnitPrecisions().contains(up1));
				assertEquals(true, db.getUnitPrecisions().contains(up2));
				assertEquals(true, db.getUnitPrecisions().contains(up3));
				assertEquals(true, db.getUnitPrecisions().contains(up4));
				assertEquals(true, db.getUnitPrecisions().contains(up5));
				assertEquals(true, db.getUnitPrecisions().contains(up6));
				assertEquals(true, db.getUnitPrecisions().contains(up7));
				assertEquals(true, db.getUnitPrecisions().contains(up8));
				
				//getsForTool
				assertEquals(3, db.getUnitPrecisionForTool(t1id).size());
				assertEquals(false, db.getUnitPrecisionForTool(t1id).contains(up1));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up4));
				
				assertEquals(4, db.getUnitPrecisionForTool(t2id).size());
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up5));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up6));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up7));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up8));
				
				//getForToolAndMutator
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m1id).contains(up5));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m2id).contains(up6));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up7));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up8));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
		
			int numdelete = db.deleteUnitPrecisions(t2id);
				assertEquals(4, numdelete);
				//get
				assertEquals(null, db.getUnitPrecision(t1id, mb1id));
				assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
				assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
				assertEquals(up4, db.getUnitPrecision(t1id, mb4id));
				assertEquals(null, db.getUnitPrecision(t2id, mb1id));
				assertEquals(null, db.getUnitPrecision(t2id, mb2id));
				assertEquals(null, db.getUnitPrecision(t2id, mb3id));
				assertEquals(null, db.getUnitPrecision(t2id, mb4id));
			
				//exists
				assertEquals(false, db.existsUnitPrecision(t1id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb4id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb1id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
				
				//gets
				assertEquals(3, db.getUnitPrecisions().size());
				assertEquals(false, db.getUnitPrecisions().contains(up1));
				assertEquals(true, db.getUnitPrecisions().contains(up2));
				assertEquals(true, db.getUnitPrecisions().contains(up3));
				assertEquals(true, db.getUnitPrecisions().contains(up4));
				assertEquals(false, db.getUnitPrecisions().contains(up5));
				assertEquals(false, db.getUnitPrecisions().contains(up6));
				assertEquals(false, db.getUnitPrecisions().contains(up7));
				assertEquals(false, db.getUnitPrecisions().contains(up8));
				
				//getsForTool
				assertEquals(3, db.getUnitPrecisionForTool(t1id).size());
				assertEquals(false, db.getUnitPrecisionForTool(t1id).contains(up1));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up4));
				
				assertEquals(0, db.getUnitPrecisionForTool(t2id).size());
				assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up5));
				assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up6));
				assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up7));
				assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up8));
				
				//getForToolAndMutator
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m1id).contains(up5));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m2id).contains(up6));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up7));
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up8));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
					
		
			up7 = db.createUnitPrecision(t2id, mb3id, 0.75, clonelist7);
			up8 = db.createUnitPrecision(t2id, mb4id, 0.9, clonelist8);
				//get
				assertEquals(null, db.getUnitPrecision(t1id, mb1id));
				assertEquals(up2, db.getUnitPrecision(t1id, mb2id));
				assertEquals(up3, db.getUnitPrecision(t1id, mb3id));
				assertEquals(up4, db.getUnitPrecision(t1id, mb4id));
				assertEquals(null, db.getUnitPrecision(t2id, mb1id));
				assertEquals(null, db.getUnitPrecision(t2id, mb2id));
				assertEquals(up7, db.getUnitPrecision(t2id, mb3id));
				assertEquals(up8, db.getUnitPrecision(t2id, mb4id));
			
				//exists
				assertEquals(false, db.existsUnitPrecision(t1id, mb1id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t1id, mb4id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb1id));
				assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb3id));
				assertEquals(true, db.existsUnitPrecision(t2id, mb4id));
				
				//gets
				assertEquals(5, db.getUnitPrecisions().size());
				assertEquals(false, db.getUnitPrecisions().contains(up1));
				assertEquals(true, db.getUnitPrecisions().contains(up2));
				assertEquals(true, db.getUnitPrecisions().contains(up3));
				assertEquals(true, db.getUnitPrecisions().contains(up4));
				assertEquals(false, db.getUnitPrecisions().contains(up5));
				assertEquals(false, db.getUnitPrecisions().contains(up6));
				assertEquals(true, db.getUnitPrecisions().contains(up7));
				assertEquals(true, db.getUnitPrecisions().contains(up8));
				
				//getsForTool
				assertEquals(3, db.getUnitPrecisionForTool(t1id).size());
				assertEquals(false, db.getUnitPrecisionForTool(t1id).contains(up1));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up2));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up3));
				assertEquals(true, db.getUnitPrecisionForTool(t1id).contains(up4));
				
				assertEquals(2, db.getUnitPrecisionForTool(t2id).size());
				assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up5));
				assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up6));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up7));
				assertEquals(true, db.getUnitPrecisionForTool(t2id).contains(up8));
				
				//getForToolAndMutator
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
				assertEquals(1, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m1id).contains(up5));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
					assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m2id).contains(up6));
				assertEquals(2, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up7));
					assertEquals(true, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up8));
				assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
	
		numdelete = db.deleteUnitPrecisions();
			assertEquals(5, numdelete);
			//get
			assertEquals(null, db.getUnitPrecision(t1id, mb1id));
			assertEquals(null, db.getUnitPrecision(t1id, mb2id));
			assertEquals(null, db.getUnitPrecision(t1id, mb3id));
			assertEquals(null, db.getUnitPrecision(t1id, mb4id));
			assertEquals(null, db.getUnitPrecision(t2id, mb1id));
			assertEquals(null, db.getUnitPrecision(t2id, mb2id));
			assertEquals(null, db.getUnitPrecision(t2id, mb3id));
			assertEquals(null, db.getUnitPrecision(t2id, mb4id));
		
			//exists
			assertEquals(false, db.existsUnitPrecision(t1id, mb1id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t1id, mb4id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb1id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb2id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb3id));
			assertEquals(false, db.existsUnitPrecision(t2id, mb4id));
			
			//gets
			assertEquals(0, db.getUnitPrecisions().size());
			assertEquals(false, db.getUnitPrecisions().contains(up1));
			assertEquals(false, db.getUnitPrecisions().contains(up2));
			assertEquals(false, db.getUnitPrecisions().contains(up3));
			assertEquals(false, db.getUnitPrecisions().contains(up4));
			assertEquals(false, db.getUnitPrecisions().contains(up5));
			assertEquals(false, db.getUnitPrecisions().contains(up6));
			assertEquals(false, db.getUnitPrecisions().contains(up7));
			assertEquals(false, db.getUnitPrecisions().contains(up8));
			
			//getsForTool
			assertEquals(0, db.getUnitPrecisionForTool(t1id).size());
			assertEquals(false, db.getUnitPrecisionForTool(t1id).contains(up1));
			assertEquals(false, db.getUnitPrecisionForTool(t1id).contains(up2));
			assertEquals(false, db.getUnitPrecisionForTool(t1id).contains(up3));
			assertEquals(false, db.getUnitPrecisionForTool(t1id).contains(up4));
			
			assertEquals(0, db.getUnitPrecisionForTool(t2id).size());
			assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up5));
			assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up6));
			assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up7));
			assertEquals(false, db.getUnitPrecisionForTool(t2id).contains(up8));
			
			//getForToolAndMutator
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m1id).size());
				assertEquals(false, db.getUnitPrecisionForToolAndMutator(t1id, m1id).contains(up1));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m2id).size());
				assertEquals(false, db.getUnitPrecisionForToolAndMutator(t1id, m2id).contains(up2));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m3id).size());
				assertEquals(false, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up3));
				assertEquals(false, db.getUnitPrecisionForToolAndMutator(t1id, m3id).contains(up4));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t1id, m4id).size());
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m1id).size());
				assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m1id).contains(up5));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m2id).size());
				assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m2id).contains(up6));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m3id).size());
				assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up7));
				assertEquals(false, db.getUnitPrecisionForToolAndMutator(t2id, m3id).contains(up8));
			assertEquals(0, db.getUnitPrecisionForToolAndMutator(t2id, m4id).size());
		
		//Error Conditions
		boolean error;
		
		error = false;
		try {
			db.createUnitPrecision(t1id, mb1id, 0.50, null);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitPrecision(t1id, mb1id, -0.50, clonelist8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitPrecision(t1id, mb1id, 1.50, clonelist8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitPrecision(t1id, 999, 0.50, clonelist8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.createUnitPrecision(999, mb1id, 0.50, clonelist8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		clonelist8.clear();
		try {
			db.createUnitPrecision(t1id, mb1id, 0.50, clonelist8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		clonelist8.add(new VerifiedClone(new Fragment(Paths.get("/file1"), 1, 2), mb1.getOriginalFragment(), true, true));
		try {
			db.createUnitPrecision(t1id, mb1id, 0.50, clonelist8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		clonelist8.add(new VerifiedClone(mb1.getOriginalFragment(), new Fragment(Paths.get("/file1"), 1, 2), true, true));
		try {
			db.createUnitPrecision(t1id, mb1id, 0.50, clonelist8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		db.deleteUnitPrecisions();
		
		clonelist8.clear();
		clonelist8.add(new VerifiedClone(mb1.getOriginalFragment(), mb1.getMutantFragment(), true, true));
		clonelist8.add(new VerifiedClone(mb2.getOriginalFragment(), mb2.getMutantFragment(), true, false));
		clonelist8.add(new VerifiedClone(mb3.getOriginalFragment(), mb3.getMutantFragment(), false, true));
		clonelist8.add(new VerifiedClone(mb4.getOriginalFragment(), mb4.getMutantFragment(), true, true));
		up8 = db.createUnitPrecision(t2id, mb4id, 0.9, clonelist8);
		clonelist8.add(new VerifiedClone(mb1.getOriginalFragment(), new Fragment(Paths.get("/file1"), 1, 2), true, true));
		try {
			up8 = db.createUnitPrecision(t2id, mb4id, 0.9, clonelist8);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		db.deleteUnitPrecisions();
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);	
	}
	
	@Test
	public void testProperties() throws FileSanetizationFailedException, SQLException, IOException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
		//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
		boolean error;
		
		//Create
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
		//Language
		db.setLanguage(ExperimentSpecification.JAVA_LANGUAGE);
		assertEquals(ExperimentSpecification.JAVA_LANGUAGE, db.getLanguage());
		
		error = false;
		try {
			db.setLanguage(99);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//GenerationType
		db.setGenerationType(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE);
		assertEquals(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE, db.getGenerationType());
		db.setGenerationType(ExperimentSpecification.MANUAL_GENERATION_TYPE);
		assertEquals(ExperimentSpecification.MANUAL_GENERATION_TYPE, db.getGenerationType());
		
		error = false;
		try {
			db.setGenerationType(5);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//FragmentType
		db.setFragmentType(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE);
		assertEquals(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE, db.getFragmentType());
		db.setFragmentType(ExperimentSpecification.BLOCK_FRAGMENT_TYPE);
		assertEquals(ExperimentSpecification.BLOCK_FRAGMENT_TYPE, db.getFragmentType());
		
		error = false;
		try {
			db.setFragmentType(3);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//MinSizeFragment(Line)
		db.setFragmentMinimumSizeLines(1);
		db.setFragmentMaximumSizeLines(Integer.MAX_VALUE);
		
		db.setFragmentMaximumSizeLines(100);
		
		db.setFragmentMinimumSizeLines(100);
		assertEquals(100, db.getFragmentMinimumSizeLines());
		db.setFragmentMinimumSizeLines(23);
		assertEquals(23, db.getFragmentMinimumSizeLines());
		
		error = false;
		try {
			db.setFragmentMinimumSizeLines(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMinimumSizeLines(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMinimumSizeLines(101);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMinimumSizeLines(150);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//MinSizeFragment(Token)
		db.setFragmentMinimumSizeTokens(1);
		db.setFragmentMaximumSizeTokens(Integer.MAX_VALUE);
		
		db.setFragmentMaximumSizeTokens(100);
		
		db.setFragmentMinimumSizeTokens(100);
		assertEquals(100, db.getFragmentMinimumSizeTokens());
		db.setFragmentMinimumSizeTokens(34);
		assertEquals(34, db.getFragmentMinimumSizeTokens());
		
		error = false;
		try {
			db.setFragmentMinimumSizeTokens(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMinimumSizeTokens(-23);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMinimumSizeTokens(101);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMinimumSizeTokens(150);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//MaxSizeFragment(Line)
		db.setFragmentMinimumSizeLines(1);
		db.setFragmentMaximumSizeLines(Integer.MAX_VALUE);
		
		db.setFragmentMinimumSizeLines(10);
		
		db.setFragmentMaximumSizeLines(10);
		assertEquals(10, db.getFragmentMaximumSizeLines());
		db.setFragmentMaximumSizeLines(26);
		assertEquals(26, db.getFragmentMaximumSizeLines());
		
		error = false;
		try {
			db.setFragmentMaximumSizeLines(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMaximumSizeLines(-324);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMaximumSizeLines(9);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMaximumSizeLines(5);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//MaxSizeFragment(Token)
		db.setFragmentMinimumSizeTokens(1);
		db.setFragmentMaximumSizeTokens(Integer.MAX_VALUE);
		
		db.setFragmentMinimumSizeTokens(10);
		
		db.setFragmentMaximumSizeTokens(10);
		assertEquals(10, db.getFragmentMaximumSizeTokens());
		db.setFragmentMaximumSizeTokens(42);
		assertEquals(42, db.getFragmentMaximumSizeTokens());
		
		error = false;
		try {
			db.setFragmentMaximumSizeTokens(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMaximumSizeTokens(-3214);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMaximumSizeTokens(9);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setFragmentMaximumSizeTokens(5);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
//		//MutationNumber
//		db.setMutationNumber(5);
//		assertEquals(5, db.getMutationNumber());
//		
//		error = false;
//		try {
//			db.setMutationNumber(0);
//		} catch (IllegalArgumentException e) {
//			error = true;
//		}
//		assertTrue(error);
//		
//		error = false;
//		try {
//			db.setMutationNumber(-10);
//		} catch (IllegalArgumentException e) {
//			error = true;
//		}
//		assertTrue(error);
		
		//InjectionNumber
		db.setInjectionNumber(6);
		assertEquals(6, db.getInjectionNumber());
		
		error = false;
		try {
			db.setInjectionNumber(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setInjectionNumber(-6);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//SubsumeMatcherTolerance
		db.setSubsumeMatcherTolerance(0.56);
		assertEquals(0.56, db.getSubsumeMatcherTolerance(), 0.00001);
		
		error = false;
		try {
			db.setSubsumeMatcherTolerance(-0.02);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setSubsumeMatcherTolerance(1.02);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//Mutation Containment
		db.setMutationContainment(0.73);
		assertEquals(0.73, db.getMutationContainment(), 0.00001);
		
		error = false;
		try {
			db.setMutationContainment(-0.20);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setMutationContainment(1.23);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//mutator attempts
		db.setMutationAttempts(137);
		assertEquals(137, db.getMutationAttempts());
		
		error = false;
		try {
			db.setMutationAttempts(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setMutationAttempts(-5);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//operator attempts
		db.setOperatorAttempts(235);
		assertEquals(235, db.getOperatorAttempts());
		
		error = false;
		try {
			db.setOperatorAttempts(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setOperatorAttempts(-10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		
		//max fragmetns
		db.setMaxFragments(14);
		assertEquals(14, db.getMaxFragments());
		
		error = false;
		try {
			db.setMaxFragments(0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setMaxFragments(-14);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//precision required similarity
		db.setPrecisionRequiredSimilarity(0.34);
		assertEquals(0.34, db.getPrecisionRequiredSimilarity(), 0.00001);
		
		error = false;
		try {
			db.setPrecisionRequiredSimilarity(-0.34);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setPrecisionRequiredSimilarity(1.34);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		//recall required similarity
		db.setRecallRequiredSimilarity(0.85);
		assertEquals(0.85, db.getRecallRequiredSimilarity(), 0.00001);
		
		error = false;
		try {
			db.setRecallRequiredSimilarity(-0.85);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			db.setRecallRequiredSimilarity(1.85);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);	
	}
	
	@Test
	public void testStages() throws FileSanetizationFailedException, IOException, SQLException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException {
		//Prep
		Path install = Paths.get("").toAbsolutePath().normalize();
		Path exprdata = install.resolve("testdata/ExperimentDataTest/exprdata");
		Path system = install.resolve("testdata/ExperimentDataTest/system/");
		Path repository = install.resolve("testdata/ExperimentDataTest/repository/");
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
										
		//Create Experiment Data
		ExperimentData db = new ExperimentData(exprdata, system, repository, ExperimentSpecification.JAVA_LANGUAGE, System.out);
		
		assertEquals(ExperimentData.GENERATION_SETUP_STAGE, db.getCurrentStage());
		assertEquals(ExperimentData.GENERATION_STAGE, db.nextStage());
		assertEquals(ExperimentData.GENERATION_STAGE, db.getCurrentStage());
		assertEquals(ExperimentData.EVALUATION_STAGE, db.nextStage());
		assertEquals(ExperimentData.EVALUATION_STAGE, db.getCurrentStage());
		
		
		//cleanup
		db.close();
		FileUtils.deleteQuietly(exprdata.toFile());
		Files.deleteIfExists(exprdata);
		
		//TODO thoroughly check when things can be run
	}
}