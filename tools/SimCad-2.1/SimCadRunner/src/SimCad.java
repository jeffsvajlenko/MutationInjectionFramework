import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimCad {

	/**
	 * 
	 * @param args sysDir toolDir language granularity cloneType cloneGrouping srcTrasnform srcPath outPath
	 * @throws IOException 
	 */
	public static void main(String args[]) {
		SimCad d = new SimCad();
		List<Clone> clones = d.runTool(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]);
		if(clones == null) {
			System.exit(1);
		}
		File out = new File(args[8]);
		out.delete();
		try {
			out.createNewFile();
		} catch (IOException e) {
			System.exit(1);
		}
		PrintStream ps;
		try {
			ps = new PrintStream(out);
			for(Clone c : clones) {
				ps.println(c);
			}
		} catch (FileNotFoundException e) {
			System.exit(1);
		}
		System.out.println(out.getAbsolutePath());
	}
	
	public List<Clone> runTool(String systemDirectory, String toolDirectory, String language, String granularity, String cloneType, String cloneGrouping, String srcTransform, String outPath) {
		// Cleanup
		if(new File(toolDirectory + "/temp/").exists()) {
			for(File f : new File(toolDirectory + "/temp/").listFiles()) {
				f.delete();
			}
		}
		
		List<Clone> clonelist = new LinkedList<Clone>();
		
		// Run SimCad
		String command = "./simcad2 " + " -l " + language + " -g " + granularity + " -t " + cloneType + " -c " + cloneGrouping + " -x " + srcTransform + " -s " + systemDirectory + " -o " + outPath;
		//System.out.println(command);
		Runtime runtime = Runtime.getRuntime();
		Process process;
		int pretval;
		try {
			String envp[] = new String[1];
			envp[0] = "PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:/usr/X11/bin";
			process = runtime.exec(command, envp, new File(toolDirectory));
			new StreamGobbler(process.getInputStream(), "");
			new StreamGobbler(process.getErrorStream(), "");
			pretval = process.waitFor();
		} catch (IOException e) {
			//e.printStackTrace();
			//System.out.println("SimCad: Error, process interrupted. Probable Cause: NiCad not setup correctly, or tool incorrectly specified in database.");
			return null;
		} catch (InterruptedException e) {
			//e.printStackTrace();
			//System.out.println("SimCad: Error, process interrupted. Probable Cause: Unknown.");
			return null;
		}
		if(pretval != 0) {
			//System.out.println("SimCad Error.");
			return null;
		}
	
		
		// Extract Clones
		File results;
		if(granularity.equals("f")) {
			results = new File(toolDirectory + "/temp/simcad_function_clone-pairs_type-1-2-3_generous.xml");
		} else {
			results = new File(toolDirectory + "/temp/simcad_block_clone-pairs_type-1-2-3_generous.xml");
		}
		if(!results.exists()) {
			//System.out.println("SimCad: Could not find results file.");
			return null;
		}
		Scanner s;
		try {
			s = new Scanner(results);
		} catch (FileNotFoundException e) {
			//e.printStackTrace();
			//System.out.println("SimCad: Could not read results file.  permissions error?");
			return null;
		}
		
		Pattern pattern = Pattern.compile("<clone_fragment file=\"(.*)\" startline=\"(.*)\" endline=\"(.*)\" pcid=\"(.*)\"/>");
		Matcher matcher;
		Fragment f1, f2;
		File sysdir = new File(systemDirectory);
		while(s.hasNextLine()) {
			String line = s.nextLine();
			
			// found clone
			if(line.startsWith("<clone_pair groupid=")) {
				//fragment1
				line = s.nextLine();
				matcher = pattern.matcher(line);
				matcher.matches();
				//System.out.println(line);
				String srcfile1 = matcher.group(1);
				if(language.equals("c") || language.equals("cs")) {
					if(srcfile1.endsWith(".ifdefed")) {
						srcfile1 = srcfile1.substring(0, srcfile1.length()-8);
					}
				}
				
				f1 = new Fragment(sysdir.getAbsolutePath() + "/" + srcfile1, Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
				
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
				
				f2 = new Fragment(sysdir.getAbsolutePath() + "/" + srcfile2, Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
				
				clonelist.add(new Clone(f1,f2));
			}
			
		}
		
		// Cleanup
		for(File f : new File(toolDirectory + "/temp/").listFiles()) {
			f.delete();
		}
		
		return clonelist;
	}

}
