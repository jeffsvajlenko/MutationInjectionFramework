package util;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.List;

import models.Fragment;
import models.InjectionLocation;

import org.junit.Test;

import experiment.ExperimentSpecification;

public class AfterFunctionInjectionLocationChooserTest {

	@Test
	public void testJava() throws SQLException, FileNotFoundException, IOException, InterruptedException {
		Path system = Paths.get("testdata/AfterFunctionInjectionLocationChooserTest/java/");
		AfterFunctionInjectionLocationChooser ilc = new AfterFunctionInjectionLocationChooser(system, ExperimentSpecification.JAVA_LANGUAGE);
		List<Fragment> fragments = SelectFunctionFragments.getFragments(system.toFile(), ExperimentSpecification.JAVA_LANGUAGE);
		for(int i = 0; i < 1000; i++) {
			InjectionLocation il = ilc.generateInjectionLocation();
			assertTrue(il != null);
			boolean found = false;
			for(Fragment f : fragments) {
				if(il.getSourceFile().toAbsolutePath().normalize().equals(f.getSrcFile().toAbsolutePath().normalize())) {
					if(il.getLineNumber() == (f.getEndLine()+1)) {
						found = true;
						break;
					}
				}
			}
			assertTrue(found);
		}
	}
	
	@Test
	public void testC() throws SQLException, FileNotFoundException, IOException, InterruptedException {
		Path system = Paths.get("testdata/AfterFunctionInjectionLocationChooserTest/c/");
		AfterFunctionInjectionLocationChooser ilc = new AfterFunctionInjectionLocationChooser(system, ExperimentSpecification.C_LANGUAGE);
		List<Fragment> fragments = SelectFunctionFragments.getFragments(system.toFile(), ExperimentSpecification.C_LANGUAGE);
		for(int i = 0; i < 1000; i++) {
			InjectionLocation il = ilc.generateInjectionLocation();
			assertTrue(il != null);
			boolean found = false;
			for(Fragment f : fragments) {
				if(il.getSourceFile().toAbsolutePath().normalize().equals(f.getSrcFile().toAbsolutePath().normalize())) {
					if(il.getLineNumber() == (f.getEndLine()+1)) {
						found = true;
						break;
					}
				}
			}
			assertTrue(found);
		}
	}
	
	@Test
	public void testCS() throws SQLException, FileNotFoundException, IOException, InterruptedException {
		Path system = Paths.get("testdata/AfterFunctionInjectionLocationChooserTest/cs/");
		AfterFunctionInjectionLocationChooser ilc = new AfterFunctionInjectionLocationChooser(system, ExperimentSpecification.CS_LANGUAGE);
		List<Fragment> fragments = SelectFunctionFragments.getFragments(system.toFile(), ExperimentSpecification.CS_LANGUAGE);
		for(int i = 0; i < 1000; i++) {
			InjectionLocation il = ilc.generateInjectionLocation();
			assertTrue(il != null);
			boolean found = false;
			for(Fragment f : fragments) {
				if(il.getSourceFile().toAbsolutePath().normalize().equals(f.getSrcFile().toAbsolutePath().normalize())) {
					if(il.getLineNumber() == (f.getEndLine()+1)) {
						found = true;
						break;
					}
				}
			}
			assertTrue(found);
		}
	}


}
