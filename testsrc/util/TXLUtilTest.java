package util;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.List;
import java.util.Random;

import main.ArtisticStyleFailedException;
import main.FileSanetizationFailedException;
import models.Fragment;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import experiment.ExperimentSpecification;

public class TXLUtilTest {

	@Test
	public void testTokenize() throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
		TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/jfragment"), Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/TokenizeTest/jtokenized").toFile(), Paths.get("testdata/TXLUtilTest/TokenizeTest/test").toFile()));
		TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/cfragment"), Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), ExperimentSpecification.C_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/TokenizeTest/ctokenized").toFile(), Paths.get("testdata/TXLUtilTest/TokenizeTest/test").toFile()));
		TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/csfragment"), Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), ExperimentSpecification.CS_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/TokenizeTest/cstokenized").toFile(), Paths.get("testdata/TXLUtilTest/TokenizeTest/test").toFile()));
		TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/empty"), Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), ExperimentSpecification.CS_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/TokenizeTest/empty").toFile(), Paths.get("testdata/TXLUtilTest/TokenizeTest/empty").toFile()));
		Files.delete(Paths.get("testdata/TXLUtilTest/TokenizeTest/test"));
		
		//error condiitons
		boolean caught;
		
		//null pointers
		caught = false;
		try {
			TXLUtil.tokenize(null, Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/jfragment"), null, ExperimentSpecification.JAVA_LANGUAGE);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//Invalid Language
		caught = false;
		try {
			TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/jfragment"), Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), 23532324);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//Infile does not exist
		caught = false;
		try {
			TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/jfragment_nosuchfile"), Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//Infile is a directory
		caught = false;
		try {
			TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/"), Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//Infile is not readable
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/TokenizeTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		caught = false;
		try {
			TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/cantread"), Paths.get("testdata/TXLUtilTest/TokenizeTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IOException e) {
			caught = true;
		}
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/TokenizeTest/cantread"), PosixFilePermissions.fromString("rw-rw-r--"));
		assertTrue(caught);
		
		//Outfile is a directory, can't overwrite that
		caught = false;
		try {
			TXLUtil.tokenize(Paths.get("testdata/TXLUtilTest/TokenizeTest/jfragment"), Paths.get("testdata/TXLUtilTest/TokenizeTest/"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}

	@Test
	public void testPrettyPrintSourceFile() throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
		TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/java.java"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/java_check.java").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test").toFile()));
		
		TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/cs.cs"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test"), ExperimentSpecification.CS_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/cs_check.cs").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test").toFile()));
		
		TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/c.c"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test"), ExperimentSpecification.C_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/c_check.c").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test").toFile()));
		
		//Exception Cases
		boolean caught;
		
		//null
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFile(null, Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test"), ExperimentSpecification.JAVA_LANGUAGE);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/java.java"), null, ExperimentSpecification.JAVA_LANGUAGE);

		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//language
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/java.java"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test"), 2345);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//infile does not exist
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/java.java_nosuchfile"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//infile is a directory
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//outfile is a directory
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/java.java"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//infile is not readable
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFile(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/cantread"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IOException e) {
			caught = true;
		}
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFileTest/cantread"), PosixFilePermissions.fromString("rw-rw----"));
		assertTrue(caught);
		
		
	}

	@Test
	public void testPrettyPrintSourceFragment() throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
		//Java
		TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/javafunction"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/javafunction_check").toFile()));
		TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/javablock"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/javablock_check").toFile()));
		
		//c
		TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/cfunction"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.C_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/cfunction_check").toFile()));
		TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/cblock"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.C_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/cblock_check").toFile()));
		
		//cs
		TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/csfunction"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.CS_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/csfunction_check").toFile()));
		TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/csblock"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.CS_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test").toFile(), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/csblock_check").toFile()));
		
		//Exceptions
		boolean caught;

		//NullPointer
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFragment(null, Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/javafunction"), null, ExperimentSpecification.JAVA_LANGUAGE);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//illegal language
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/javafunction"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), 234234);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//input file not found
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/javafunction_nosuchfule"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//input file not readable
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/cantread"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IOException e) {
			caught = true;
		}
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/cantread"), PosixFilePermissions.fromString("rw-rw----"));
		assertTrue(caught);
		
		//input file is a directory
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//output file is a directory
		caught = false;
		try {
			TXLUtil.prettyPrintSourceFragment(Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/javafunction"), Paths.get("testdata/TXLUtilTest/PrettyPrintSourceFragmentTest/"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}

	@Test
	public void testBlindRename() throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
		//real examples
		TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/jfragment"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/BlindRenameTest/jfragment_blind").toFile(), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test").toFile()));
		TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/cfragment"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.C_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/BlindRenameTest/cfragment_blind").toFile(), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test").toFile()));
		TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/csfragment"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.CS_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/BlindRenameTest/csfragment_blind").toFile(), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test").toFile()));
		TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/empty"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.CS_LANGUAGE);
		
		//check
		TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/jfragment_literal"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/BlindRenameTest/jfragment_literal_blind").toFile(), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test").toFile()));
		TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/cfragment_literal"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.C_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/BlindRenameTest/cfragment_literal_blind").toFile(), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test").toFile()));
		TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/csfragment_literal"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.CS_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/BlindRenameTest/csfragment_literal_blind").toFile(), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test").toFile()));
		
		//empty
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/BlindRenameTest/empty").toFile(), Paths.get("testdata/TXLUtilTest/BlindRenameTest/empty").toFile()));
		Files.delete(Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"));
		
		//test error condiitons
		boolean caught;
		
		//null pointers
		caught = false;
		try {
			TXLUtil.blindRename(null, Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/jfragment"), null, ExperimentSpecification.JAVA_LANGUAGE);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//infile does not exist
		caught = false;
		try {
			TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/jfragment_notexist"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);

		
		//infile is not a regular file
		caught = false;
		try {
			TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);

		
		//infile is not readable
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/BlindRenameTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		caught = false;
		try {
			TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/cantread"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IOException e) {
			caught = true;
		}
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/BlindRenameTest/cantread"), PosixFilePermissions.fromString("rw-rw----"));
		assertTrue(caught);
		
		//outfile is a directory
		caught = false;
		try {
			TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/jfragment"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);

		
		//language does not exist
		caught = false;
		try {
			TXLUtil.blindRename(Paths.get("testdata/TXLUtilTest/BlindRenameTest/jfragment"), Paths.get("testdata/TXLUtilTest/BlindRenameTest/test"), 35435);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}

	@Test
	public void testRemoveComments() throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
		TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/java.java"), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/java_nocomments.java").toFile(), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test").toFile()));
		
		TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/c.c"), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test"), ExperimentSpecification.C_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/c_nocomments.c").toFile(), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test").toFile()));
		
		TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/cs.cs"), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test"), ExperimentSpecification.CS_LANGUAGE);
		assertTrue(FileUtils.contentEquals(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/cs_nocomments.cs").toFile(), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test").toFile()));
		
		//test error condiitons
		boolean caught;
		
		//null pointers
		caught = false;
		try {
			TXLUtil.removeComments(null, Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/java.java"), null, ExperimentSpecification.JAVA_LANGUAGE);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//infile does not exist
		caught = false;
		try {
			TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/notexist"), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);

		
		//infile is not a regular file
		caught = false;
		try {
			TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/"), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);

		
		//infile is not readable
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		caught = false;
		try {
			TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/cantread"), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IOException e) {
			caught = true;
		}
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/cantread"), PosixFilePermissions.fromString("rw-rw----"));
		assertTrue(caught);
		
		//outfile is a directory
		caught = false;
		try {
			TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/java.java"), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);

		
		//language does not exist
		caught = false;
		try {
			TXLUtil.removeComments(Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/java.java"), Paths.get("testdata/TXLUtilTest/RemoveCommentsTest/test"), 35435);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}

	@Test
	public void testRunTxl() throws InterruptedException, IOException {
		Files.deleteIfExists(Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_out"));
		int retval = TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrint.txl"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_out"));
		assertTrue(retval==0);
		assertTrue(Files.exists(Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_out")));
		
		//error cases
		boolean caught;
		
		//null pointers
		caught = false;
		try {
			TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrint.txl"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java"), null);
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrint.txl"), null, Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_out"));
		} catch (NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		caught = false;
		try {
			TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrint.txl"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java"), null);
		} catch(NullPointerException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//Script does not exist
		caught = false;
		try {
			 TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrint.txl_jknotrealfile"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_out"));
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//Infile does not exist
		caught = false;
		try {
			 TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrint.txl"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_jknotarealfile"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_out"));
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//Script is not readable
		caught = false;
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/RunTxlTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		try {
			TXLUtil.runTxl(Paths.get("testdata/TXLUtilTest/RunTxlTest/cantread"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_out"));
		} catch (IOException e) {
			caught = true;
		}
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/RunTxlTest/cantread"), PosixFilePermissions.fromString("rw-rw----"));
		assertTrue(caught);
		
		//Infile is not readable
		caught = false;
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/RunTxlTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		try {
			TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("PrettyPrint.txl"), Paths.get("testdata/TXLUtilTest/RunTxlTest/cantread"), Paths.get("testdata/TXLUtilTest/RunTxlTest/InventoriedSystem.java_out"));
		} catch (IOException e) {
			caught = true;
		}
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/RunTxlTest/cantread"), PosixFilePermissions.fromString("rw-rw----"));
		assertTrue(caught);
		
	}

	@Test
	public void testIsFunctionFragmentInt() throws FileNotFoundException, IOException, InterruptedException {			
		assertTrue(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 226, 240), ExperimentSpecification.JAVA_LANGUAGE));//a function
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 230, 240), ExperimentSpecification.JAVA_LANGUAGE)); //ablock
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 231, 239), ExperimentSpecification.JAVA_LANGUAGE));
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 234, 238), ExperimentSpecification.JAVA_LANGUAGE));
		
		assertTrue(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/c.c"), 170, 205), ExperimentSpecification.C_LANGUAGE));//a function
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/c.c"), 171, 205), ExperimentSpecification.C_LANGUAGE)); //ablock
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/c.c"), 172, 204), ExperimentSpecification.C_LANGUAGE));
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/c.c"), 157, 213), ExperimentSpecification.C_LANGUAGE));
		
		assertTrue(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cs.cs"), 26, 60), ExperimentSpecification.CS_LANGUAGE));//a function
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cs.cs"), 27, 60), ExperimentSpecification.CS_LANGUAGE)); //ablock
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cs.cs"), 28, 59), ExperimentSpecification.CS_LANGUAGE));
		assertFalse(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cs.cs"), 19, 60), ExperimentSpecification.CS_LANGUAGE));
		
		//Mass Check
			//java
		List<Fragment> functions = SelectFunctionFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsFunctionTest/java/").toFile(), ExperimentSpecification.JAVA_LANGUAGE);
		List<Fragment> blocks = SelectBlockFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsFunctionTest/java/").toFile(), ExperimentSpecification.JAVA_LANGUAGE);
		for(Fragment fragment : functions) {
			assertTrue(fragment + "", TXLUtil.isFunction(fragment, ExperimentSpecification.JAVA_LANGUAGE));
		}
		for(Fragment fragment : blocks) {
			if(fragment.getStartLine() != fragment.getEndLine()) { //pesky one liners that astyle won't break
				assertFalse(fragment + "", TXLUtil.isFunction(fragment, ExperimentSpecification.JAVA_LANGUAGE));
			}
		}
			//c
		//functions = SelectFunctionFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsFunctionTest/c/").toFile(), ExperimentSpecification.C_LANGUAGE);
		//blocks = SelectBlockFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsFunctionTest/c/").toFile(), ExperimentSpecification.C_LANGUAGE);
		//for(Fragment fragment : functions) {
		//	assertTrue(TXLUtil.isFunction(fragment, ExperimentSpecification.C_LANGUAGE));
		//}
		//for(Fragment fragment : blocks) {
		//	assertFalse(TXLUtil.isFunction(fragment, ExperimentSpecification.C_LANGUAGE));
		//}
			//cs
		//functions = SelectFunctionFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cs/").toFile(), ExperimentSpecification.CS_LANGUAGE);
		//blocks = SelectBlockFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cs/").toFile(), ExperimentSpecification.CS_LANGUAGE);
		//for(Fragment fragment : functions) {
		//	assertTrue(TXLUtil.isFunction(fragment, ExperimentSpecification.CS_LANGUAGE));
		//}
		//for(Fragment fragment : blocks) {
		//	assertFalse(TXLUtil.isFunction(fragment, ExperimentSpecification.CS_LANGUAGE));
		//}
		
		//Check exception cases
		boolean thrown;
		
			//language does not exist
		thrown = false;
		try {
			TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/InventoriedSystem.java"), 45, 90), 123123213);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
			//fragment file is a directory
		thrown = false;
		try {
			TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/"), 45, 90), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
			//File does not exist
		thrown = false;
		try {
			TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/InventoriedSystem.java_notexist"), 45, 90), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			thrown = true;
		}
		assertTrue(thrown);
		
			//unreadable file
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		thrown=false;
		try {
			TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cantread"), 50, 60), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			thrown=true;
		}
		assertTrue("Failed to throw illegal argument exception for unreadable function.", thrown);
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cantread"), PosixFilePermissions.fromString("rw-rw-r--"));
		
			//invalid fragment
		thrown = false;
		try {
			assertTrue(TXLUtil.isFunction(new Fragment(Paths.get("testdata/TXLUtilTest/IsFunctionTest/InventoriedSystem.java"), 45, 9000000), ExperimentSpecification.JAVA_LANGUAGE));
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue(thrown);
	}
	


	@Test
	public void testIsFunctionPathInt() throws FileNotFoundException, IOException, InterruptedException {
		assertTrue(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/function"), ExperimentSpecification.JAVA_LANGUAGE));
		assertFalse(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/notfunction"), ExperimentSpecification.JAVA_LANGUAGE));
		assertTrue(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cfunction"), ExperimentSpecification.C_LANGUAGE));
		assertFalse(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cnotfunction1"), ExperimentSpecification.C_LANGUAGE));
		assertFalse(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cnotfunction2"), ExperimentSpecification.C_LANGUAGE));
		assertTrue(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/csfunction1"), ExperimentSpecification.CS_LANGUAGE));
		assertTrue(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/csfunction2"), ExperimentSpecification.CS_LANGUAGE));
		assertFalse(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/csnotfunction1"), ExperimentSpecification.CS_LANGUAGE));
		assertFalse(TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/csnotfunction2"), ExperimentSpecification.CS_LANGUAGE));
		
		//Check exception cases
		boolean thrown;
		thrown=false;
		try {
			TXLUtil.isFunction(Paths.get(null), 768);
		} catch (NullPointerException e) {
			thrown=true;
		}
		assertTrue("Failed to throw null pointer exception for path.", thrown);
		
		thrown=false;
		try {
			TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/function324234324"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			thrown=true;
		}
		assertTrue("Failed to throw illegal argument exception for non-existant function.", thrown);
		
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		thrown=false;
		try {
			TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cantread"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			thrown=true;
		}
		assertTrue("Failed to throw illegal argument exception for unreadable function.", thrown);
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/IsFunctionTest/cantread"), PosixFilePermissions.fromString("rw-rw-r--"));
		
		thrown=false;
		try {
			TXLUtil.isFunction(Paths.get("testdata/TXLUtilTest/IsFunctionTest/function"), 4664);
		} catch (IllegalArgumentException e) {
			thrown=true;
		}
		assertTrue("Failed to throw illegal argument exception for unsupported language.", thrown);
		
	}

	@Test
	public void testIsBlockPathInt() throws FileNotFoundException, IOException, InterruptedException {
		assertTrue(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/javablockfile"), ExperimentSpecification.JAVA_LANGUAGE));
		assertFalse(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/javanotblockfile"), ExperimentSpecification.JAVA_LANGUAGE));
		assertFalse(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/javanotblockfile2"), ExperimentSpecification.JAVA_LANGUAGE));
		
		assertTrue(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/csblockfile"), ExperimentSpecification.C_LANGUAGE));
		assertFalse(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/csnotblockfile"), ExperimentSpecification.C_LANGUAGE));
		assertFalse(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/csnotblockfile2"), ExperimentSpecification.C_LANGUAGE));
		
		assertTrue(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/csblockfile"), ExperimentSpecification.CS_LANGUAGE));
		assertFalse(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/csnotblockfile"), ExperimentSpecification.CS_LANGUAGE));
		assertFalse(TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/csnotblockfile2"), ExperimentSpecification.CS_LANGUAGE));
		
		//check error
		boolean caught;
		
		//invalid language
		caught = false;
		try {
			TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/javablockfile"), 2345234);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//File not found
		caught = false;
		try {
			TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/javablockfile_nosuchfile"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//infile is a directory
		caught = false;
		try {
			TXLUtil.isBlock(Paths.get("testdata/TXLUtilTest/IsBlockTest/"), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		
	}
	
	@Test
	public void testIsBlockFragmentInt() throws FileNotFoundException, IOException, InterruptedException {
		assertTrue(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 230, 240), ExperimentSpecification.JAVA_LANGUAGE));
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 226, 240), ExperimentSpecification.JAVA_LANGUAGE));//a function
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 231, 239), ExperimentSpecification.JAVA_LANGUAGE));
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 219, 242), ExperimentSpecification.JAVA_LANGUAGE));
		
		assertTrue(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/c.c"), 171, 205), ExperimentSpecification.C_LANGUAGE));
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/c.c"), 170, 205), ExperimentSpecification.C_LANGUAGE));//a function
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/c.c"), 172, 204), ExperimentSpecification.C_LANGUAGE));
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/c.c"), 157, 211), ExperimentSpecification.C_LANGUAGE));
		
		assertTrue(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/cs.cs"), 27, 60), ExperimentSpecification.CS_LANGUAGE));
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/cs.cs"), 26, 60), ExperimentSpecification.CS_LANGUAGE));//a function
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/cs.cs"), 28, 59), ExperimentSpecification.CS_LANGUAGE));
		assertFalse(TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/cs.cs"), 19, 60), ExperimentSpecification.CS_LANGUAGE));
		
		//JAVA
		List<Fragment> blocks = SelectBlockFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsBlockTest/java/").toFile(), ExperimentSpecification.JAVA_LANGUAGE);
		for(Fragment fragment : blocks) {
			if(fragment.getStartLine() != fragment.getEndLine()) { //dont check if it is a {}, b/c astyle will miss fixing that!
				assertTrue(fragment + "", TXLUtil.isBlock(fragment, ExperimentSpecification.JAVA_LANGUAGE));
			}
		}
		List<Fragment> functions = SelectFunctionFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsBlockTest/java/").toFile(), ExperimentSpecification.JAVA_LANGUAGE);
		for(Fragment fragment : functions) {
			if(fragment.getStartLine() != fragment.getEndLine()) {
				assertFalse(TXLUtil.isBlock(fragment, ExperimentSpecification.JAVA_LANGUAGE));
			}
		}
		//CS
		blocks = SelectBlockFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsBlockTest/cs/").toFile(), ExperimentSpecification.CS_LANGUAGE);
		for(Fragment fragment : blocks) {
			if(fragment.getStartLine() != fragment.getEndLine()) { //dont check if it is a {}, b/c astyle will miss fixing that!
				assertTrue(fragment + "", TXLUtil.isBlock(fragment, ExperimentSpecification.CS_LANGUAGE));
			}
		}
		functions = SelectFunctionFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsBlockTest/cs/").toFile(), ExperimentSpecification.CS_LANGUAGE);
		for(Fragment fragment : functions) {
			if(fragment.getStartLine() != fragment.getEndLine()) {
				assertFalse(TXLUtil.isBlock(fragment, ExperimentSpecification.CS_LANGUAGE));
			}
		}
		
		//C
		/*
		blocks = SelectBlockFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsBlockTest/c/").toFile(), ExperimentSpecification.C_LANGUAGE);
		for(Fragment fragment : blocks) {
			if(fragment.getStartLine() != fragment.getEndLine()) { //dont check if it is a {}, b/c astyle will miss fixing that!
				assertTrue(fragment + "", TXLUtil.isBlock(fragment, ExperimentSpecification.C_LANGUAGE));
			}
		}
		functions = SelectFunctionFragments.getFragments(Paths.get("testdata/TXLUtilTest/IsBlockTest/c/").toFile(), ExperimentSpecification.C_LANGUAGE);
		for(Fragment fragment : functions) {
			if(fragment.getStartLine() != fragment.getEndLine()) {
				assertFalse(TXLUtil.isBlock(fragment, ExperimentSpecification.C_LANGUAGE));
			}
		}
		*/
		
		//Error Conditions
		boolean caught;
		
		//illegal language
		caught = false;
		try {
			TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 230, 240), 2342);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//fragment source file does not exist
		caught = false;
		try {
			TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java_nosuchfile"), 230, 240), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (FileNotFoundException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//fragment source file is a directory
		caught = false;
		try {
			TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/"), 230, 240), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
		
		//fragment source file is not readable
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/IsBlockTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
		caught = false;
		try {
			TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/cantread"), 230, 240), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IOException e) {
			caught = true;
		}
		Files.setPosixFilePermissions(Paths.get("testdata/TXLUtilTest/IsBlockTest/cantread"), PosixFilePermissions.fromString("rw-rw----"));
		assertTrue(caught);
		
		//fragment is invalid
		caught = false;
		try {
			TXLUtil.isBlock(new Fragment(Paths.get("testdata/TXLUtilTest/IsBlockTest/java.java"), 230, 8000), ExperimentSpecification.JAVA_LANGUAGE);
		} catch (IllegalArgumentException e) {
			caught = true;
		}
		assertTrue(caught);
	}
	
	@Test
	public void testPrettyprint() throws IllegalArgumentException, InterruptedException, IOException, ArtisticStyleFailedException, FileSanetizationFailedException {
		int numcheck = Integer.MAX_VALUE;
		Random rdm = new Random();
		
		Path fragment = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "testPrettyPrint", "fragment");
		Path fragment_pretty = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "testPrettyPrint", "prettyfragment");
		Path fragment_pretty_spaced = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "testPrettyPrint", "prettyfragmentspaced");
		Path fragment_pretty_spaced_emptylines = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "testPrettyPrint", "prettyfragmentspacedemptylines");
		Path prettyprinted = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "testPrettyPrint", "prettyprint");
		/*
		//JAVA
		System.out.println("java");
		List<Fragment> functions = SelectFunctionFragments.getFragments(Paths.get("testdata/TXLUtilTest/PrettyPrintTest/java/").toFile(), ExperimentSpecification.JAVA_LANGUAGE);
		for(int i = 0; i < numcheck && functions.size() > 0; i++) {
			if(i % 100 == 0)
				System.out.println(i);
			//Pick a function
			Fragment function = functions.remove(rdm.nextInt(functions.size()));
			
			//Extract and pretty print function (check)
			FragmentUtil.extractFragment(function, fragment);
			
			if(!TXLUtil.prettyPrintSourceFragment(fragment, fragment_pretty, ExperimentSpecification.JAVA_LANGUAGE)) {
				System.out.println("Detected fail for (prettyPrintFragment) " + function);
				continue;
			}
			
			//Space
			if(0 != TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).resolve("spacer.txl"), fragment_pretty, fragment_pretty_spaced)) {
				System.out.println("Detected fail for (spacer) " + function);
				continue;
			}
			
			//Remove Empty Lines
			FileUtil.removeEmptyLines(fragment_pretty_spaced, fragment_pretty_spaced_emptylines);
			
			//Pretty Print
			if(!TXLUtil.prettyprint(function.getSrcFile(), prettyprinted, function.getStartLine(), function.getEndLine(), ExperimentSpecification.JAVA_LANGUAGE)) {
				System.out.println("Detected fail for (prettyprint) " + function);
				continue;
			}
			
			//Check
			assertEquals(1, FileUtil.getSimilarity(fragment_pretty_spaced_emptylines, prettyprinted), 0.001);
		}
		*/
		
		//C
		//assertTrue(TXLUtil.prettyprint(Paths.get("/Users/jeff/Dropbox/MutationInjectionFramework/testdata/TXLUtilTest/PrettyPrintTest/c/monit-4.2/net.c"), 
		//		prettyprinted, 662, 794, ExperimentSpecification.C_LANGUAGE));
		
		//NormalizeSystem.normalizeSystem(Paths.get("testdata/TXLUtilTest/PrettyPrintTest/c/"), ExperimentSpecification.C_LANGUAGE);
		
		System.out.println("c"); //"  testdata/TXLUtilTest/PrettyPrintTest/c/
		List<Fragment> functions = SelectFunctionFragments.getFragments(Paths.get("data/repositories/c/").toFile(), ExperimentSpecification.C_LANGUAGE);
		for(int i = 0; i < numcheck && functions.size() > 0; i++) {
			if(i % 100 == 0)
				System.out.println(i);
			//Pick a function
			Fragment function = functions.remove(rdm.nextInt(functions.size()));
			
			//Extract and pretty print function (check)
			FragmentUtil.extractFragment(function, fragment);
			
			if(!TXLUtil.prettyPrintSourceFragment(fragment, fragment_pretty, ExperimentSpecification.C_LANGUAGE)) {
				System.out.println("Detected fail for (prettyPrintFragment) " + function);
				continue;
			}
			
			//Space
			if(0 != TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.C_LANGUAGE).resolve("spacer.txl"), fragment_pretty, fragment_pretty_spaced)) {
				System.out.println("Detected fail for (spacer) " + function);
				continue;
			}
			
			//Remove Empty Lines
			FileUtil.removeEmptyLines(fragment_pretty_spaced, fragment_pretty_spaced_emptylines);
			
			//Pretty Print
			if(!TXLUtil.prettyprint(function.getSrcFile(), prettyprinted, function.getStartLine(), function.getEndLine(), ExperimentSpecification.C_LANGUAGE)) {
				System.out.println("Detected fail for (prettyprint) " + function);
				continue;
			}
			
			//Check
			assertEquals(1, FileUtil.getSimilarity(fragment_pretty_spaced_emptylines, prettyprinted), 0.001);
		}
		/*
		//CS
		System.out.println("cs ");
		List<Fragment> functions = SelectFunctionFragments.getFragments(Paths.get("testdata/TXLUtilTest/PrettyPrintTest/cs/").toFile(), ExperimentSpecification.CS_LANGUAGE);
		for(int i = 0; i < numcheck && functions.size() > 0; i++) {
			if(i % 10 == 0)
				System.out.println(i);
			//Pick a function
			Fragment function = functions.remove(rdm.nextInt(functions.size()));
			
			//Extract and pretty print function (check)
			FragmentUtil.extractFragment(function, fragment);
			
			if(!TXLUtil.prettyPrintSourceFragment(fragment, fragment_pretty, ExperimentSpecification.CS_LANGUAGE)) {
				System.out.println("Detected fail for (prettyPrintFragment) " + function);
				continue;
			}
			
			//Space
			if(0 != TXLUtil.runTxl(SystemUtil.getTxlDirectory(ExperimentSpecification.CS_LANGUAGE).resolve("spacer.txl"), fragment_pretty, fragment_pretty_spaced)) {
				System.out.println("Detected fail for (spacer) " + function);
				continue;
			}
			
			//Remove Empty Lines
			FileUtil.removeEmptyLines(fragment_pretty_spaced, fragment_pretty_spaced_emptylines);
			
			//Pretty Print
			if(!TXLUtil.prettyprint(function.getSrcFile(), prettyprinted, function.getStartLine(), function.getEndLine(), ExperimentSpecification.CS_LANGUAGE)) {
				System.out.println("Detected fail for (prettyprint) " + function);
				continue;
			}
			
			//Check
			assertEquals(1, FileUtil.getSimilarity(fragment_pretty_spaced_emptylines, prettyprinted), 0.001);
		}
		*/
		
		
		//cleanup
		Files.deleteIfExists(fragment);
		Files.deleteIfExists(fragment_pretty);
		Files.deleteIfExists(prettyprinted);
		Files.deleteIfExists(fragment_pretty_spaced_emptylines);
		Files.deleteIfExists(fragment_pretty_spaced);
		
	}

}
