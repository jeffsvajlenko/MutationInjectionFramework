import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author jeff
 *TESTED
 */
public class Simian {

	//test
	public static void main(String args[]) {
		Simian d = new Simian();
		List<Clone> clones = d.runTool(args[0], args[1], args[2], args[3], args[4]);
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
	
	public List<Clone> runTool(String systemDirectory, String installdir, String language, String opts, String memory) {
		List<Clone> clonelist = new LinkedList<Clone>();
		
		// Run Simian
		String fileopt="";
		if(language.equals("java")) {
			//fileopt = systemDirectory + "/*.java " + systemDirectory + "/**/*.java";
			fileopt = "find " + systemDirectory + " -name *.java";
		} else if(language.equals("cs")) {
			//fileopt = systemDirectory + "/*.cs " + systemDirectory + "/**/*.cs";
			fileopt = "find " + systemDirectory + " -name *.cs";
		} else if(language.equals("c")) {
			//fileopt = systemDirectory + "/*.c " + systemDirectory + "/**/*.c " + systemDirectory + "/**/*.h " + systemDirectory + "/*.h";
			fileopt = "find " + systemDirectory + " -name *.c -o -name *.h";
		}
		
		String command = fileopt;
		String files = "";
		//System.out.println(command);
		//System.out.flush();
		Runtime runtime = Runtime.getRuntime();
		Process process;
		
		try {
			process = runtime.exec(command);
			new StreamGobbler(process.getErrorStream(), "ERROR").start();
			Scanner s = new Scanner(new BufferedInputStream(process.getInputStream()));
			while(s.hasNextLine()) {
				files = files + s.nextLine() + " ";
			}
			//System.out.println(files);
			process.waitFor();
			process.destroy();
			
			//System.out.println(files);
			
			String command2 = "java " + memory + " -jar " + installdir + "/simian-2.3.33.jar -language=" + language + " " + opts + " " + files;
			//System.out.println("\n\n\n" + command2);
			process = runtime.exec(command2);
			new StreamGobbler(process.getErrorStream(), "ERROR").start();
			s = new Scanner(new BufferedInputStream(process.getInputStream()));
			String line;
			
			Pattern clonestart = Pattern.compile("Found .*? duplicate lines in the following files:");
			Pattern fragment = Pattern.compile(" Between lines (.*?) and (.*?) in (.*)");
			Matcher matcher;
			List<Fragment> fragments;
			
			//Read Disclaimer
			s.nextLine();
			s.nextLine();
			s.nextLine();
			s.nextLine();
			
			
			line = s.nextLine();
			//System.out.println(line);
			while(clonestart.matcher(line).matches()) {
				line = s.nextLine();
				//System.out.println(line);
				fragments = new LinkedList<Fragment>();
				matcher = fragment.matcher(line);
				while(matcher.matches()) {
					Fragment f = new Fragment(matcher.group(3), Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)));
					fragments.add(f);
					line = s.nextLine();
					if(line == null) {
						//System.out.println("null");
					}
					//System.out.println(line);
					matcher = fragment.matcher(line);
				}
				int size = fragments.size();
				for(int i = 0; i < size; i++) {
					for(int j = i+1; j < size; j++) {
						Clone c = new Clone(fragments.get(i), fragments.get(j));
						clonelist.add(c);
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
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return null;
		}
		
		return clonelist;
	}
}
