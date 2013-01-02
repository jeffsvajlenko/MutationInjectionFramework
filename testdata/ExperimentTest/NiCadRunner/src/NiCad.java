import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NiCad {

	/**
	 * Runs NiCad
	 * @param args systemDir nicadInstallDir language granularity configFile
	 */
	public static void main(String args[]) {
		NiCad nicad = new NiCad();
		List<Clone> clones = nicad.runTool(args[0], args[1], args[2], args[3], args[4]);
		if(clones == null) {
			System.err.println("here1");
			System.exit(1);
		}
		File out = new File(args[5]);
		out.delete();
		try {
			out.createNewFile();
		} catch (IOException e) {
			System.err.println("here2");
			System.exit(1);
		}
		PrintStream ps;
		try {
			ps = new PrintStream(out);
			for(Clone c : clones) {
				ps.println(c);
			}
		} catch (FileNotFoundException e) {
			System.err.println("here3");
			System.exit(1);
		}
		System.out.println(out.getAbsolutePath());
	}

	/**
	 * Runs NiCad.
	 * @param systemDirectory The directory of the system to analyze.
	 * @param language The language of the system.
	 * @param granularity The granularity of the clone search.
	 * @param toolDirectory Directory containing NiCad.
	 * @return
	 */
	public List<Clone> runTool(String systemDirectory, String toolDirectory, String language, String granularity, String configFile) {
		List<Clone> clonelist = new LinkedList<Clone>();
		
		// Clear up any previous temporary files
		File tmp = new File(systemDirectory + "/_functions.xml");
		if(tmp.exists()) {
			tmp.delete();
		}
		
		// Run NiCad
		String command = "./nicad3 " + granularity + " " + language + " " + systemDirectory + "/ framework";
		String envp[] = new String[1];
		envp[0] = "PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:/usr/X11/bin";
		Runtime runtime = Runtime.getRuntime();
		Process process;
		int pretval;
		try {
			process = runtime.exec(command, envp, new File(toolDirectory));
			new StreamGobbler(process.getInputStream(), "");
			new StreamGobbler(process.getErrorStream(), "");
			pretval = process.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("NiCad: Error, process interrupted. Probable Cause: NiCad not setup correctly, or tool incorrectly specified in database.");
			return null;
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.err.println("NiCad: Error, process interrupted. Probable Cause: Unknown.");
			return null;
		}
		if(pretval != 0) {
			System.err.println("NiCad: Error, NiCad ended with error. Probable Cause: Unknown, check log in " + systemDirectory + ".");
			return null;
		}
		
		// Extract Clones
		File results;
		if(granularity.equals("functions")) {
			results = new File(systemDirectory + "/_functions-blind-abstract-clones/_functions-blind-abstract-clones-0.35.xml");
		} else {
			results = new File(systemDirectory + "/_blocks-blind-abstract-clones/_blocks-blind-abstract-clones-0.35.xml");
		}
		if(!results.exists()) {
			System.err.println("NiCad: Could not find results file.  If changed config, need to alter NiCad tool runner!");
		}
		Scanner s;
		try {
			s = new Scanner(results);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.err.println("NiCad: Could not read results file.  permissions error?");
			return null;
		}
		Pattern pattern = Pattern.compile("<source file=\"(.*)\" startline=\"(.*)\" endline=\"([0-9]*)\"? pcid=\".*\"></source>");
		Matcher matcher;
		Fragment f1, f2;
		while(s.hasNextLine()) {
			String line = s.nextLine();
			
			// found clone
			if(line.startsWith("<clone nlines")) {
				//fragment1
				line = s.nextLine();
				matcher = pattern.matcher(line);
				matcher.matches();
				
				String srcfile1=matcher.group(1);
				if(language.equals("c") || language.equals("cs")) {
					if(srcfile1.endsWith(".ifdefed")) {
						srcfile1 = srcfile1.substring(0, srcfile1.length()-8);
					}
				}
				
				f1 = new Fragment(srcfile1, Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
				
				//fragment2
				line = s.nextLine();
				matcher = pattern.matcher(line);
				matcher.matches();
				
				String srcfile2=matcher.group(1);
				if(language.equals("c") || language.equals("cs")) {
					if(srcfile2.endsWith(".ifdefed")) {
						srcfile2 = srcfile2.substring(0, srcfile2.length()-8);
					}
				}
				
				f2 = new Fragment(srcfile2, Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
				clonelist.add(new Clone(f1,f2));
			}
			
		}
		
		
		// Cleanup temporary files
		/*for(File f : new File(systemDirectory + "/").listFiles()) {
			if(f.toString().endsWith("_functions.xml")) {
				f.delete();
			} else if(f.toString().endsWith(".log")) {
				f.delete();
			} else if(f.toString().endsWith(".xml")) {
				f.delete();
			} else if(f.toString().endsWith("_functions-consistent-abstract-clones")) {
				HelperFunctions.recursiveDelete(f);
			} else if(f.toString().endsWith("_blocks-consistent-abstract-clones")) {
				HelperFunctions.recursiveDelete(f);
			}
		}*/
		
		return clonelist;
	}

}
