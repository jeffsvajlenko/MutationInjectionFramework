package util;


import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

import experiment.ExperimentSpecification;

public class SystemUtilTest {

	@Test
	public void testGetInstallRoot() {
		assertTrue(SystemUtil.getInstallRoot().toAbsolutePath().normalize().equals(Paths.get(".").toAbsolutePath().normalize()));
	}

	@Test
	public void testGetScriptsLocation() {
		assertTrue(SystemUtil.getScriptsLocation().toAbsolutePath().normalize().equals(SystemUtil.getInstallRoot().resolve("scripts").toAbsolutePath().normalize()));
	}

	@Test
	public void testGetTemporaryDirectory() {
		assertTrue(SystemUtil.getTemporaryDirectory().toAbsolutePath().normalize().equals(SystemUtil.getInstallRoot().resolve("tmp").toAbsolutePath().normalize()));
	}

	@Test
	public void testGetTxlDirectory() {
		assertTrue(SystemUtil.getTxlDirectory().toAbsolutePath().normalize().equals(SystemUtil.getInstallRoot().resolve("txl").toAbsolutePath().normalize()));
	}

	@Test
	public void testGetTxlDirectoryInt() {
		assertTrue(SystemUtil.getTxlDirectory(ExperimentSpecification.JAVA_LANGUAGE).toAbsolutePath().normalize().equals(SystemUtil.getInstallRoot().resolve("txl/java/").toAbsolutePath().normalize()));
		assertTrue(SystemUtil.getTxlDirectory(ExperimentSpecification.C_LANGUAGE).toAbsolutePath().normalize().equals(SystemUtil.getInstallRoot().resolve("txl/c/").toAbsolutePath().normalize()));
		assertTrue(SystemUtil.getTxlDirectory(ExperimentSpecification.CS_LANGUAGE).toAbsolutePath().normalize().equals(SystemUtil.getInstallRoot().resolve("txl/cs/").toAbsolutePath().normalize()));
	}
	
	@Test
	public void testGetOperatorsDirectory() {
		assertTrue(SystemUtil.getOperatorsPath().equals(SystemUtil.getInstallRoot().resolve("operators").toAbsolutePath().normalize()));
	}
	
	@Test
	public void testGetTxlExecutable() {
		System.out.println("TXL: " + SystemUtil.getTxlExecutable());
		assertEquals(Paths.get("/usr/local/bin/txl"), SystemUtil.getTxlExecutable());
	}
	
	@Test
	public void testGetArtisticStyleExecutable() {
		System.out.println("astyle: " + SystemUtil.getArtisticStyleExecutable());
	}
}
