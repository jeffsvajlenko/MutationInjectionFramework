import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class CCFinder {

	/**
	 * Runs NiCad
	 * @param args systemDir nicadInstallDir language granularity configFile
	 * @throws IOException 
	 */
	public static void main(String args[]) throws IOException {
		CCFinder ccfinder = new CCFinder();
		List<Clone> clones = ccfinder.runTool(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7], args[8], args[9]);
		if(clones == null) {
			System.exit(1);
		}
		File out = new File(args[10]);
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

	/**
	 * Runs NiCad.
	 * @param systemDirectory The directory of the system to analyze.
	 * @param language The language of the system.
	 * @param granularity The granularity of the clone search.
	 * @param toolDirectory Directory containing NiCad.
	 * @return
	 * @throws IOException 
	 */
	public List<Clone> runTool(String systemDirectory, String language, String finstalldir, String toolDirectory, String granularity, String trdir, String minlines, String mintokens, String numthreads, String tks) throws IOException {
		List<Clone> clonelist = new LinkedList<Clone>();
		
		// Clear up any previous temporary files
		//.......
		
	// Run CCFinder
		if(language.equals("c")) {
			language = "cpp";
		} else if(language.equals("cs")) {
			language = "csharp";
		} else if(language.equals("java")) {
			language = "java";
		}
		
		String command = "wine ccfx D " + language + " --threads=" + Integer.parseInt(numthreads) + " -t " + Integer.parseInt(tks) + " -dn " + new File(systemDirectory).getAbsolutePath();
		String envp[] = new String[1];
		envp[0] = "PATH=/usr/bin:/bin:/usr/sbin:/sbin:/usr/local/bin:/usr/X11/bin";
		Runtime runtime = Runtime.getRuntime();
		Process process;
		int pretval;
		
		//run ccfx D
		try {
			process = runtime.exec(command, envp, new File(toolDirectory));
			new StreamGobbler(process.getInputStream(), "");
			new StreamGobbler(process.getErrorStream(), "");
			pretval = process.waitFor();
		} catch (IOException e) {
			return null;
		} catch (InterruptedException e) {
			return null;
		}
		if(pretval != 0) {
			return null;
		}
		
		//format results propery
		command = "./format";
		try {
			process = runtime.exec(command, envp, new File(toolDirectory));
			new StreamGobbler(process.getInputStream(), "");
			new StreamGobbler(process.getErrorStream(), "");
			pretval = process.waitFor();
		} catch (IOException e) {
			return null;
		} catch (InterruptedException e) {
			return null;
		}
		if(pretval != 0) {
			return null;
		}
		
	//Convert to NiCad	
		Path system = Paths.get(systemDirectory).toAbsolutePath().normalize();
		Path tool = Paths.get(toolDirectory).toAbsolutePath().normalize();
		Path ccfxprepdir = system.resolve(".ccfxprepdir");
		Path result = tool.resolve("b.ccfxd");
		
		Scanner resultScanner = new Scanner(result.toFile());
		String line;
		String postfix = null;
		
		//option: -preprocessed_file_postfix .cpp.2_0_0_2.default.ccfxprep
		
		//Eat input until read the 'option: -preprocessed_file_postfix ' line
		while(true) {
			if(!resultScanner.hasNextLine()) {
				return null;
			} else {
				line = resultScanner.nextLine();
				if(line.startsWith("option: -preprocessed_file_postfix ")) {
					break;
				} else {
					continue;
				}
			}
		}
		
		postfix = line.replace("option: -preprocessed_file_postfix ", "");
		
		//Eat input until read the 'source_files {' line
		while(true) {
			if(!resultScanner.hasNextLine()) {
				return null;
			} else {
				line = resultScanner.nextLine();
				if(line.startsWith("source_files {")) {
					break;
				} else {
					continue;
				}
			}
		}
		
		//Initialize files
		HashMap<Integer,Path> files = new HashMap<Integer,Path>();
		while(true) {
			if(!resultScanner.hasNextLine()) {
				return null;
			} else {
				line = resultScanner.nextLine();
				if(line.startsWith("}")) {
					break;
				} else {
					String[] items = line.split("\t");
					files.put(Integer.parseInt(items[0]), Paths.get(items[1]).toAbsolutePath().normalize());
				}
			}
		}
		
		//Eat input until read the 'source_files {' line
		while(true) {
			if(!resultScanner.hasNextLine()) {
				return null;
			} else {
				line = resultScanner.nextLine();
				if(line.startsWith("clone_pairs {")) {
					break;
				} else {
					continue;
				}
			}
		}
		
		//Get Clones
		while(true) {
			if(!resultScanner.hasNextLine()) {
				return null;
			} else {
				line = resultScanner.nextLine();
				if(line.startsWith("}")) {
					break;
				} else {
					//id?	f1id.st-et	f2id.st-et
					//System.out.println("--");
					String[] items = line.split("\t");
					Fragment f1 = this.getFragment(items[1], files, system, ccfxprepdir, postfix);
					Fragment f2 = this.getFragment(items[2], files, system, ccfxprepdir, postfix);
					clonelist.add(new Clone(f1,f2));
				}
			}
		}
		
		return clonelist;
	}

	Fragment getFragment(String fragment, HashMap<Integer,Path> files, Path system, Path ccfxprepdir, String postfix) throws IOException {
		String[] teir1 = fragment.split("\\.");
		String[] teir2 = teir1[1].split("-");

		int fileid = Integer.parseInt(teir1[0]);
		int startTok = Integer.parseInt(teir2[0])+1;
		int endTok = Integer.parseInt(teir2[1])+1;
		
		Path srcfile = files.get(fileid);
		//System.out.println(srcfile + " " + HelperFunctions.countLines(srcfile.toString()));
		//System.out.println("stok: " + startTok);
		int startline = this.tokenToLine(startTok, srcfile, system, ccfxprepdir, postfix);
		//System.out.println("startline: " + startline);
		//System.out.println("etok: " + endTok);
		int endline = this.tokenToLine(endTok, srcfile, system, ccfxprepdir, postfix);
		//System.out.println("endline: " + endline);
		return new Fragment(srcfile.toAbsolutePath().normalize().toString(), startline, endline);
	}
	
	int tokenToLine(int token, Path srcfile, Path system, Path ccfxprepdir, String postfix) throws IOException {
		if(token == 0) {
			return 1;
		}
		Path tokfile = ccfxprepdir.resolve(system.relativize(srcfile.toAbsolutePath().normalize()));
		tokfile = tokfile.getParent().resolve(tokfile.getFileName().toString() + postfix);
		
		//Get token's line
		int linenum = 0;
		Scanner s = new Scanner(tokfile);
		String line = null;
		while(linenum != token) {
			if(!s.hasNextLine()) {
				s.close();
				return HelperFunctions.countLines(srcfile.toString());
			} else {
				linenum++;
				line = s.nextLine();
			}
		}
		s.close();
		
		if(line != null) {
			String[] items = line.split("\\.");
			int linen = Integer.parseInt(items[0], 16);
			int length = HelperFunctions.countLines(srcfile.toString());
			if(linen > length) {
				return length;
			} else {
				return linen;
			}
		} else {
			return 0;
		}
	}
}
