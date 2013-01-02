package util;


import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import models.Fragment;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import util.FragmentUtil;

public class FragmentUtilTest {

	@Test
	public void testExtractFragment() throws IOException {
	//Cleanup
		Files.deleteIfExists(Paths.get("testdata/FragmentUtilTest/ExtractTest"));	
		
	//Perform extract
		FragmentUtil.extractFragment(new Fragment(Paths.get("testdata/FragmentUtilTest/InventoriedSystem.java"), 45, 90), Paths.get("testdata/FragmentUtilTest/ExtractTest"));
	
	//Check
		List<String> manual = new LinkedList<String>();
		List<String> auto = new LinkedList<String>();
		
		//Fill Auto
		Scanner s = new Scanner(Paths.get("testdata/FragmentUtilTest/ExtractTest").toFile());
		while(s.hasNextLine()) {
			auto.add(s.nextLine());
		}
		s.close();
		
		//Fill Manual
		s = new Scanner(Paths.get("testdata/FragmentUtilTest/function").toFile());
		while(s.hasNextLine()) {
			manual.add(s.nextLine());
		}
		s.close();
	
		
		assertTrue(auto.size() == manual.size());
		for(int i = 0; i < auto.size(); i++) {
			assertEquals(manual.get(i), auto.get(i));
		}
		
	//Cleanup
		Files.deleteIfExists(Paths.get("testdata/FragmentUtilTest/ExtractTest"));
		
	//Check exceptions
		boolean thrown;
		
		thrown = false;
		try {
			FragmentUtil.extractFragment(null, Paths.get("testdata/FragmentUtilTest/ExtractTest"));
		} catch (NullPointerException e) {
			thrown = true;
		}
		assertTrue("Failed to throw nullpointerexception for null fragment.", thrown);
		
		thrown = false;
		try {
			FragmentUtil.extractFragment(new Fragment(Paths.get("testdata/FragmentUtilTest/InventoriedSystem.java"), 39, 84), null);
		} catch (NullPointerException e) {
			thrown = true;
		}
		assertTrue("Failed to throw nullpointerexception for null output directory.", thrown);
		
		thrown = false;
		try {
			FragmentUtil.extractFragment(new Fragment(Paths.get("testdata/FragmentUtilTest/InventoriedSystem343434.java"), 39, 84), Paths.get("testdata/FragmentUtilTest/ExtractTest"));
		} catch (FileNotFoundException e) {
			thrown = true;
		}
		assertTrue("Failed to throw file not found exception for non-existant file in fragment.", thrown);
		
		thrown = false;
		try {
			FragmentUtil.extractFragment(new Fragment(Paths.get("testdata/FragmentUtilTest/InventoriedSystem.java"), 39, 1000), Paths.get("testdata/FragmentUtilTest/ExtractTest"));
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue("Failed to throw illegal argument exception for invalid fragment.", thrown);
		
		Files.setPosixFilePermissions(Paths.get("testdata/FragmentUtilTest/cantread"), PosixFilePermissions.fromString("r--r-----"));
		thrown = false;
		try {
			FragmentUtil.extractFragment(new Fragment(Paths.get("testdata/FragmentUtilTest/cantread"), 39, 84), Paths.get("testdata/FragmentUtilTest/ExtractTest"));
		} catch (IllegalArgumentException e) {
			thrown = true;
		}
		assertTrue("Failed to throw illegal argument exception for an input file which is not readable.", thrown);
		Files.setPosixFilePermissions(Paths.get("testdata/FragmentUtilTest/cantread"), PosixFilePermissions.fromString("rw-rw-r--"));
		
	}

	@Test
	public void testInjectFragment() throws IOException {
		//Whitebox, injectFragment(path,line,fragment) also tests injectFragment(path,line,path) as it calls it
		
		Fragment f = new Fragment(Paths.get("testdata/FragmentUtilTest/InventoriedSystem.java"), 45, 90);
		Path file = Paths.get("testdata/FragmentUtilTest/InventoriedSystem.java");
		Path realfile = Paths.get("testdata/FragmentUtilTest/InventoriedSystem_copy.java");
		
		Files.deleteIfExists(realfile);
		Files.copy(file, realfile);
		FragmentUtil.injectFragment(realfile, 350, f);
		assertTrue(filesEqual(realfile, Paths.get("testdata/FragmentUtilTest/InventoriedSystem_injectEnd.java")));
		
		Files.deleteIfExists(realfile);
		Files.copy(file, realfile);
		FragmentUtil.injectFragment(realfile, 102, f);
		assertTrue(filesEqual(realfile, Paths.get("testdata/FragmentUtilTest/InventoriedSystem_injectMiddle.java")));
		
		Files.deleteIfExists(realfile);
		Files.copy(file, realfile);
		FragmentUtil.injectFragment(realfile, 1, f);
		assertTrue(filesEqual(realfile, Paths.get("testdata/FragmentUtilTest/InventoriedSystem_injectStart.java")));
		
		Files.deleteIfExists(realfile);
		Files.copy(file, realfile);
		FragmentUtil.injectFragment(realfile, 351, f);
		assertTrue(filesEqual(realfile, Paths.get("testdata/FragmentUtilTest/InventoriedSystem_injectAfterEnd.java")));
	}
	
	private boolean filesEqual(Path f1, Path f2) throws IOException {
		return FileUtils.contentEquals(f1.toFile(), f2.toFile());
	}
}
