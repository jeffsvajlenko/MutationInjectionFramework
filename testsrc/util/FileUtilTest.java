package util;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import experiment.ExperimentSpecification;

public class FileUtilTest {

	@Test
	public void testFileInventory() throws IOException {
		Path path = Paths.get("testdata/FileUtilTest/testInventory");
		List<Path> filelist = FileUtil.fileInventory(path);
		Collections.sort(filelist);
		
		//are the expected files there?
		assertTrue(filelist.size() == 5);
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/dir1/file").toAbsolutePath().normalize()));
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/dir2/dira/file").toAbsolutePath().normalize()));
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/dir2/dirb/file").toAbsolutePath().normalize()));
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/dir2/file").toAbsolutePath().normalize()));
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/file").toAbsolutePath().normalize()));
		
		//are the expected files missing?
		assertFalse(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/badfile")));
		
		//check exceptions
			//its a file not a directory!
		boolean exception_thrown = false;
		try {
			FileUtil.fileInventory(Paths.get("testdata/FileUtilTest/testInventory/dir1/file"));
		} catch (IllegalArgumentException e) {
			exception_thrown = true;
		}
		assertTrue(exception_thrown);
		
			//directory does not exist
		exception_thrown = false;
		try {
			FileUtil.fileInventory(Paths.get("testdata/FileUtilTest/nosuchdirectory"));
		} catch (FileNotFoundException e) {
			exception_thrown = true;
		}
		assertTrue(exception_thrown);
		
	}

	@Test
	public void testDirectoryInventory() throws IOException {
		Path path = Paths.get("testdata/FileUtilTest/testInventory");
		List<Path> filelist = FileUtil.directoryInventory(path);
		Collections.sort(filelist);
		
		//are the expected files there?
		assertTrue(filelist.size() == 4);
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/dir1").toAbsolutePath().normalize()));
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/dir2").toAbsolutePath().normalize()));
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/dir2/dira").toAbsolutePath().normalize()));
		assertTrue(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/dir2/dirb").toAbsolutePath().normalize()));
		
		//are the expected files missing?
		assertFalse(filelist.contains(Paths.get("testdata/FileUtilTest/testInventory/badfolder")));
		
		//check exceptions
		boolean exception_thrown = false;
		try {
			FileUtil.directoryInventory(Paths.get("testdata/FileUtilTest/testInventory/dir1/file"));
		} catch (IllegalArgumentException e) {
			exception_thrown = true;
		}
		assertTrue(exception_thrown);
		
		exception_thrown = false;
		try {
			FileUtil.directoryInventory(Paths.get("testdata/FileUtilTest/nosuchdirectory"));
		} catch (FileNotFoundException e) {
			exception_thrown = true;
		}
		assertTrue(exception_thrown);
	}

	@Test
	public void testIsLeafDirectory() throws FileNotFoundException, IOException {
		assertTrue(FileUtil.isLeafDirectory(Paths.get("testdata/FileUtilTest/testInventory/dir1")));
		assertFalse(FileUtil.isLeafDirectory(Paths.get("testdata/FileUtilTest/testInventory/dir2")));
		assertTrue(FileUtil.isLeafDirectory(Paths.get("testdata/FileUtilTest/testInventory/dir2/dira")));
		assertTrue(FileUtil.isLeafDirectory(Paths.get("testdata/FileUtilTest/testInventory/dir2/dirb")));
		
		//check exceptions
		boolean exception_thrown = false;
		try {
			FileUtil.isLeafDirectory(Paths.get("testdata/FileUtilTest/testInventory/dir1/file"));
		} catch (IllegalArgumentException e) {
			exception_thrown = true;
		}
		assertTrue(exception_thrown);
		
		exception_thrown = false;
		try {
			FileUtil.isLeafDirectory(Paths.get("testdata/FileUtilTest/nosuchdirectory"));
		} catch (FileNotFoundException e) {
			exception_thrown = true;
		}
		assertTrue(exception_thrown);
	}
	
	@Test
	public void testCountLines() throws FileNotFoundException, IOException {
		assertEquals(33, FileUtil.countLines(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountLines/fragment1")));
		assertEquals(18, FileUtil.countLines(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountLines/fragment2")));
		assertEquals(18, FileUtil.countLines(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountLines/fragment3")));
		assertEquals(28, FileUtil.countLines(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountLines/fragment4")));
		assertEquals(27, FileUtil.countLines(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountLines/fragment5")));
		assertEquals(0, FileUtil.countLines(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountLines/fragment6")));
		
		//Check some success cases
				assertTrue(FileUtil.countLines(Paths.get("testdata/FragmentUtilTest/function")) == 46);
				assertTrue(FileUtil.countLines(Paths.get("testdata/FragmentUtilTest/InventoriedSystem.java")) == 350);
				assertTrue(FileUtil.countLines(Paths.get("testdata/FragmentUtilTest/notfunction")) == 39);
				
				//Check some exception cases
				boolean thrown;
				
				thrown = false;
				try {
					FileUtil.countLines(null);
				} catch(NullPointerException e) {
					thrown = true;
				}
				assertTrue("Failed to catch null file path.", thrown);
				
				thrown = false;
				try {
					FileUtil.countLines(Paths.get("testdata/FragmentUtilTest/nosuchfile"));
				} catch(FileNotFoundException e) {
					thrown = true;
				}
				assertTrue("Failed to catch file not found exception.", thrown);
				
				Files.setPosixFilePermissions(Paths.get("testdata/FragmentUtilTest/cantread"), PosixFilePermissions.fromString("-w--w----"));
				thrown = false;
				try {
					FileUtil.countLines(Paths.get("testdata/FragmentUtilTest/cantread"));
				} catch(IllegalArgumentException e) {
					thrown = true;
				}
				assertTrue("Failed to catch illegal argument exception - file not readable.", thrown);
				Files.setPosixFilePermissions(Paths.get("testdata/FragmentUtilTest/cantread"), PosixFilePermissions.fromString("rw-rw-r--"));
				
				thrown = false;
				try {
					FileUtil.countLines(Paths.get("testdata/FragmentUtilTest/"));
				} catch(IllegalArgumentException e) {
					thrown = true;
				}
				assertTrue("Failed to catch illegal argument exception - file is not a regulr file.", thrown);
	}
	
	@Test
	public void testCountTokens() throws IOException, TXLException, InterruptedException {
		assertEquals(132, FileUtil.countTokens(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountTokens/jfragment1"), ExperimentSpecification.JAVA_LANGUAGE));
		assertEquals(268, FileUtil.countTokens(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountTokens/cfragment1"), ExperimentSpecification.C_LANGUAGE));
		assertEquals(117, FileUtil.countTokens(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountTokens/csfragment1"), ExperimentSpecification.CS_LANGUAGE));
		
		assertEquals(0, FileUtil.countTokens(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountTokens/empty"), ExperimentSpecification.JAVA_LANGUAGE));
		assertEquals(0, FileUtil.countTokens(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountTokens/empty"), ExperimentSpecification.C_LANGUAGE));
		assertEquals(0, FileUtil.countTokens(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testCountTokens/empty"), ExperimentSpecification.CS_LANGUAGE));
	}
	
	
	@Test
	public void testGetSimilarity() throws FileNotFoundException, IOException {
		Path f2 = SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testGetSimilarity/fragment2");
		Path f3 = SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testGetSimilarity/fragment3");
		Path f4 = SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testGetSimilarity/fragment4");
		
		assertEquals(0.888888888888, FileUtil.getSimilarity(f2, f3), 0.000000001);
		assertEquals(0.5, FileUtil.getSimilarity(f2, f4), 0.000000001);
		assertEquals(0.57142857142, FileUtil.getSimilarity(f3, f4), 0.000000001);
	}

	@Test
	public void testRemoveEmptyLines() throws FileNotFoundException, IOException {
		FileUtil.removeEmptyLines(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/withEmptyLines"), SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/test"));
		assertTrue(org.apache.commons.io.FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/test").toFile(), SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/withoutEmptyLines").toFile()));
		Files.deleteIfExists(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/test"));
		Files.copy(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/withEmptyLines"), SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/test"));
		assertFalse(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/test").toFile(), SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/withoutEmptyLines").toFile()));
		FileUtil.removeEmptyLines(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/test"), SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/test"));
		assertTrue(FileUtils.contentEquals(SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/test").toFile(), SystemUtil.getInstallRoot().resolve("testdata/FileUtilTest/testRemoveEmptyLines/withoutEmptyLines").toFile()));
	}
	
}
