import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Deckard {

	/**
	 * 
	 * @param args sysDir toolDir language granularity cloneType cloneGrouping srcTrasnform srcPath outPath
	 * @throws IOException 
	 */
	public static void main(String args[]) {
		Deckard d = new Deckard();
		//System.out.println(args[0]);
		//System.out.println(args[1]);
		//System.out.println(args[2]);
		//System.out.println(args[3]);
		List<Clone> clones = d.runTool(args[0], args[1], args[2]);
		if(clones == null) {
			System.exit(1);
		}
		File out = new File(args[3]);
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
			//System.out.println("==");
			System.exit(1);
		}
		System.out.println(out.getAbsolutePath());
	}
	
	public List<Clone> runTool(String systemDirectory, String toolDirectory, String language) {
		
		List<Clone> clonelist = new LinkedList<Clone>();
		LinkedList<CloneClass> cloneClasses = new LinkedList<CloneClass>();
		
		// Copy config file
		Path configsrc;
		if(language.equals("java")) {
			configsrc = Paths.get(new File("./java/config").getAbsolutePath());
		} else/* if (language.equals("c"))*/ {
			configsrc = Paths.get(new File("./c/config").getAbsolutePath());
		}
		Path configtarget = Paths.get(new File(systemDirectory).getAbsolutePath() + "/config");
		try {
			Files.copy(configsrc, configtarget, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			return null;
		}
		
		// Run tool
		String command = new File(toolDirectory).getAbsolutePath() + "/scripts/clonedetect/deckard.sh";
		Runtime runtime = Runtime.getRuntime();
		Process process;
		int pretval;
		try {
			//System.out.println(command);
			process = runtime.exec(command, null, new File(systemDirectory));
			new StreamGobbler(process.getInputStream(), "");
			new StreamGobbler(process.getErrorStream(), "");
			pretval = process.waitFor();
		} catch (IOException e) {
			//System.out.println("==");
			return null;
		} catch (InterruptedException e) {
			return null;
		}
		if(pretval != 0) {
			return null;
		}
		
		// Collect Results
		File resultfile = null;
		File resultdir = new File(systemDirectory + "/clusters/");
		for(File f : resultdir.listFiles()) {
			if(f.getName().startsWith("post_cluster")) {
				resultfile = f;
				break;
			}
		}
		if(resultfile == null) {
			return null;
		}
		BufferedReader in = null;
		try {
			in = new BufferedReader(new FileReader(resultfile));
		} catch (FileNotFoundException e) {
			return null;
		}
		try {
			String line = in.readLine();
			CloneClass cclass = new CloneClass(0);
			Pattern pattern = Pattern.compile(".*\tFILE (.*) LINE:([0-9]*):([0-9]*) .*");
			Matcher matcher;
			String sysdir = new File(systemDirectory).getAbsolutePath();
			while(line != null) {
				if(line.equals("")) {
					cloneClasses.add(cclass);
					cclass = new CloneClass(0);
					line = in.readLine();
				} else {
					matcher = pattern.matcher(line);
					if(matcher.matches()) {
						String srcfile = matcher.group(1).replaceFirst("./", sysdir + "/");
						int startline = Integer.parseInt(matcher.group(2));
						int endline = startline + Integer.parseInt(matcher.group(3)) - 1;
						cclass.addClone(new Fragment(srcfile, startline, endline));
						line = in.readLine();
					} else {
						line = in.readLine();
					}
				}
			}
			
		} catch (IOException e) {
			return null;
		}
		for(CloneClass cc : cloneClasses) {
			List<Fragment> list = cc.getClones();
			for(int i = 0; i < list.size(); i++) {
				for(int j = i+1; j < list.size(); j++) {
					Clone c = new Clone(list.get(i), list.get(j));
					clonelist.add(c);
				}
			}
		}
		
		//cleanup
		try {
			Files.delete(configtarget);
		} catch (IOException e) {
		}
		
		Deckard.recursiveDelete(new File("times"));
		Deckard.recursiveDelete(new File("vectors"));
		Deckard.recursiveDelete(new File("clusters"));
		
		return clonelist;
	}
	
	public static boolean recursiveDelete(File dir) {
		if(dir.isDirectory()) {
			String[] children = dir.list();
			for(String s : children) {
				boolean success = recursiveDelete(new File(dir,s));
				if(!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

}
