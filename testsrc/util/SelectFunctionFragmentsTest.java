package util;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import models.Fragment;

import org.junit.Test;

import experiment.ExperimentSpecification;

import util.SelectFunctionFragments;

public class SelectFunctionFragmentsTest {

	@Test
	public void testGetFragments() throws IOException {
		//Java
		List<Fragment> ff = SelectFunctionFragments.getFragments(Paths.get("testdata/SelectFragmentsTest/JHotDraw54b1/").toFile(), ExperimentSpecification.JAVA_LANGUAGE);
		Path checkfunc = Paths.get("testdata/SelectFragmentsTest/jfunctions");
		BufferedReader br = new BufferedReader(new FileReader(checkfunc.toFile()));
		
		List<Fragment> checkff = new LinkedList<Fragment>();
		String line = br.readLine();
		while(line != null) {
			
			Scanner linescanner = new Scanner(line);
			String srcfile = linescanner.next();
			int startline = linescanner.nextInt();
			int endline = linescanner.nextInt();
			checkff.add(new Fragment(Paths.get(srcfile).toAbsolutePath().normalize(), startline, endline));
			linescanner.close();
			
			line = br.readLine();
		}
		br.close();
	
		assertTrue(ff.size() == 2886);
		assertTrue(checkff.size() == 2886);
		
		for(Fragment f : ff) {
			assertTrue(checkff.contains(f));
		}
		for(Fragment f : checkff) {
			assertTrue(ff.contains(f));
		}
		
		//C
		ff = SelectFunctionFragments.getFragments(Paths.get("testdata/SelectFragmentsTest/monit-4.2/").toFile(), ExperimentSpecification.C_LANGUAGE);
		checkfunc = Paths.get("testdata/SelectFragmentsTest/cfunctions");
		br = new BufferedReader(new FileReader(checkfunc.toFile()));
		
		checkff = new LinkedList<Fragment>();
		line = br.readLine();
		while(line != null) {
			
			Scanner linescanner = new Scanner(line);
			String srcfile = linescanner.next();
			int startline = linescanner.nextInt();
			int endline = linescanner.nextInt();
			checkff.add(new Fragment(Paths.get(srcfile).toAbsolutePath().normalize(), startline, endline));
			linescanner.close();
			
			line = br.readLine();
		}
		br.close();
	
		assertTrue(ff.size() == 437);
		assertTrue(checkff.size() == 437);
		
		for(Fragment f : ff) {
			assertTrue(checkff.contains(f));
		}
		for(Fragment f : checkff) {
			assertTrue(ff.contains(f));
		}
		
		//C#
		ff = SelectFunctionFragments.getFragments(Paths.get("testdata/SelectFragmentsTest/greenshot/").toFile(), ExperimentSpecification.CS_LANGUAGE);
		checkfunc = Paths.get("testdata/SelectFragmentsTest/csfunctions");
		br = new BufferedReader(new FileReader(checkfunc.toFile()));
		
		checkff = new LinkedList<Fragment>();
		line = br.readLine();
		while(line != null) {
			
			Scanner linescanner = new Scanner(line);
			String srcfile = linescanner.next();
			int startline = linescanner.nextInt();
			int endline = linescanner.nextInt();
			checkff.add(new Fragment(Paths.get(srcfile).toAbsolutePath().normalize(), startline, endline));
			linescanner.close();
			
			line = br.readLine();
		}
		br.close();
	
		assertTrue(ff.size() == 310);
		assertTrue(checkff.size() == 310);
		
		for(Fragment f : ff) {
			assertTrue(checkff.contains(f));
		}
		for(Fragment f : checkff) {
			assertTrue(ff.contains(f));
		}
	}
}
