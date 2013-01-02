import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 
 * @author jeff
 *TESTED
 */
public class IClones {

	//test
	public static void main(String args[]) {
		IClones d = new IClones();
		List<Clone> clones = d.runTool(args[0], args[1], args[2], args[3], args[4], args[6]);
		if(clones == null) {
			System.exit(1);
		}
		File out = new File(args[5]);
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
	
	public List<Clone> runTool(String systemDirectory, String installdir, String language, String minblock, String minclone, String toolRunnerDirectory) {
		List<Clone> clonelist = new LinkedList<Clone>();
		File sysdir = new File(systemDirectory);
		File results = new File(toolRunnerDirectory + "/out.txt");
		File toolRunnerDir = new File(toolRunnerDirectory).getAbsoluteFile();
		Process process;
		Runtime runtime = Runtime.getRuntime();
		
		try {
			// Cleanup old data is necissary
			if(results.exists()) {
				if(!results.delete()) {
					//System.out.println("iClones output file location is unwritable.  Failed for " + sysdir.getAbsolutePath());
					return null;
				}
			}
			
			if(language.equals("c")) {
				language = "c++";
			}
			
			// Run iClones
			String command = installdir + "/iclones.sh -informat single -language " + language + " -outformat text -output " + toolRunnerDir.toString() + "/out.txt -minblock " + minblock + " -minclone " + minclone + " -input " + sysdir.getAbsolutePath();
			//System.out.println(command);
			process = runtime.exec(command);
			new StreamGobbler(process.getErrorStream(), "ERROR").start();
			new StreamGobbler(process.getInputStream(), "INPUT").start();
			int retval = process.waitFor();
			
			// Check success
			if(retval != 0) {
				//System.out.println("iClones error while analzying " + sysdir.getAbsolutePath());
				return null;
			}
			if(!results.exists() || !results.canRead()) {
				//System.out.println("iClones could not find output for " + sysdir.getAbsolutePath());
				return null;
			}
			
			// Parse output
			BufferedReader br = new BufferedReader(new FileReader(results));
			String line;
			Pattern clonestart = Pattern.compile("\tCloneClass\t[0-9]*");
			Pattern fragment = Pattern.compile("\t\t[0-9]*\t(.*?)\t([0-9]*)\t([0-9]*)$");
			Matcher matcher;
			LinkedList<LinkedList<Fragment>> fragments = new LinkedList<LinkedList<Fragment>>();
			
			int list = -1;
			line = br.readLine();
			while(line != null) {
				matcher = clonestart.matcher(line);
				if(matcher.matches()) {
					list++;
					fragments.add(new LinkedList<Fragment>());
				} else {
					matcher = fragment.matcher(line);
					if(matcher.matches()) {
						fragments.get(list).add(new Fragment(sysdir.getAbsolutePath() + "/" + matcher.group(1), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3))));
					}
				}
				line = br.readLine();
			}
			
			// Construct output clones
			for(LinkedList<Fragment> l : fragments) {
				for(int i = 0; i < l.size(); i++) {
					for(int j = i+1; j < l.size(); j++) {
						clonelist.add(new Clone(l.get(i), l.get(j)));
					}
				}
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		} catch (NoSuchElementException e) {
			//e.printStackTrace();
			return null;
		} catch (IllegalStateException e) {
			//e.printStackTrace();
			return null;
		} catch (InterruptedException e) {
			//e.printStackTrace();
			return null;
		}
		
		return clonelist;
	}
}
