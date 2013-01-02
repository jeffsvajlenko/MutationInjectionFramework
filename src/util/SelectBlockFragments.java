package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import experiment.ExperimentSpecification;

import models.Fragment;
import util.StreamGobbler;

/**
 * 
 * Selects function fragments from the system.
 *
 */
public class SelectBlockFragments {
	
	public static List<Fragment> getFragments(File srcloc, int languageSpec) {
		Objects.requireNonNull(srcloc);
		if(!ExperimentSpecification.isLanguageSupported(languageSpec)) {
			throw new IllegalArgumentException("Unsupported language.");
		}
		
		String language = ExperimentSpecification.languageToString(languageSpec);	
		
		//Delete leftover extraction data
		new File(srcloc + "/_blocks.xml").delete();
		
		// Extract the blocks from the code base.  If error occurs, report possible reasons and return null.
		int retval;
		try {
			//Execute statement and gobble output
			String command =  SystemUtil.getScriptsLocation().toString() + "/Extract blocks " + language + " " + srcloc.getAbsolutePath() + "/";
			Runtime runtime = Runtime.getRuntime();
			Process process = runtime.exec(command);
			new StreamGobbler(process.getErrorStream()).start();
			new StreamGobbler(process.getInputStream()).start();
			
			// Wait for process to finish and collect return value
			retval = process.waitFor();
			
			//Cleanup
			process.getErrorStream().close();
			process.getInputStream().close();
			process.getOutputStream().close();
			process.destroy();
			process = null;
		} catch (IOException e) { 
			e.printStackTrace();
			System.out.println("Error(SelectFragments): Failed to extract functions from the code base.  Possible Reasons: NiCad is not installed.  /scripts/Extract is not setup correctly.  System folder is unreadable or code base is missing.");
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.out.println("Error(SelectFragments): Failed due to extraction process being interrupted.");
			return null;
		}
		if(retval != 0) {
			System.out.println("Error(SelectFragments): Extract script failed.  Possible Reasons: Extract script not setup correctly.  Code base abscent from .../data/system/.");
			return null;
		}
		
		// Extract function information from file into a list structure
		List<Fragment> blocks = new ArrayList<Fragment>();
		File blocksFile = new File(srcloc.getAbsolutePath() + "/_blocks.xml");
		Scanner blockScanner;
		try {
			blockScanner = new Scanner(new FileInputStream(blocksFile));
			while(blockScanner.hasNextLine()) { // find the source files from _functions.xml add them to the functions list
				String line = blockScanner.nextLine();
				if(line.matches("<source.*")) {
					String[] parts = line.split(" ");
					String filename = parts[1].substring(6, parts[1].length()-1); // extract filename
					if(filename.endsWith(".ifdefed")) {
						filename = filename.substring(0, filename.length()-8);
					}
					int startline = Integer.parseInt(parts[2].substring(11,parts[2].length()-1)); // extract start line
					int endline = Integer.parseInt(parts[3].substring(9,parts[3].length()-2)); // extract end line
					Fragment f = new Fragment(Paths.get(filename).toAbsolutePath().normalize(), startline, endline);
					blocks.add(f);
				}
			}
			blockScanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.out.println("Error(SelectFragments): Failed to open .../data/system/_blocks.xml.  Possible Reason: _functions.xml was not created by Extract script.  Could the code base be missing?");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("");
			return null;
		}

		
		// Cleanup
		new File(srcloc.getAbsolutePath() + "/_blocks.xml").delete(); // no longer need this file, so trash it
		
		//Cleanup ifdef
		String command =  SystemUtil.getScriptsLocation().toString() + "/RemoveIfDefed " + srcloc.getAbsolutePath() + "/";
		Runtime runtime = Runtime.getRuntime();
		Process process;
		try {
			process = runtime.exec(command);
			new StreamGobbler(process.getErrorStream()).start();
			new StreamGobbler(process.getInputStream()).start();
			process.waitFor();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
		
		// Return
		return blocks;
	}
}
