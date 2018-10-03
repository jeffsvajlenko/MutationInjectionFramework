// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import experiment.ExperimentSpecification;

import util.StreamGobbler;
import util.SystemUtil;

/**
 * 
 * Represents a clone detection tool.
 *
 */
public class Tool {
	String name;
	String description;
	Path directory;
	Path toolrunner;
	
	/**
	 * Creates a tool thats in the database.
	 * @param name the tools name.
	 * @param description the description of the tool.
	 * @param directory the directory containing the tool.
	 * @param toolRunner the path to the executable that runs the tool.
	 * @throws IllegalArgumentException If any of the parameters are null.
	 */
	public Tool(String name, String description, Path directory, Path toolRunner) {
	// Check Argument
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(directory);
		Objects.requireNonNull(toolRunner);
		
	// Initialize
		this.name = name;
		this.description = description;
		this.directory = directory;
		this.toolrunner = toolRunner;
	}
	
	/**
	 * Returns the name of this tool.
	 * @return the name of this tool.
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * Returns a description of this tool.
	 * @return a description of this tool.
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * Returns the directory containing this tool.
	 * @return the directory containing this tool.
	 */
	public Path getDirectory() {
		return this.directory;
	}
	
	/**
	 * Returns the tool runner for this tool.
	 * @return the tool runner for this tool.
	 */
	public Path getToolRunner() {
		return this.toolrunner;
	}
	
	/**
	 * Runs the clone detection tool (using its tool runner).  Tool runner is given all the following parameters, but it does not need to use them.
	 * @param systemDirectory The directory containing the source to analyze.  Must exist and be a directory.
	 * @param language The language of the source code.  Must be one specificed in ExperimentSpecification.
	 * @param fragmentType The fragment type of clones to look for.  Must be one specified in ExperimentSpecification.
	 * @param minlines  The minimum clone fragment size (lines).
	 * @param mintokens The minimum clone fragment size (tokens).
	 * @param maxlines  The maximum clone fragment size (lines).
	 * @param maxtokens The maximum clone fragment size (tokens).
	 * @param maxdifference The maximum difference between clones fragments.
	 * @return A clone detection report of the tool's results, or null if tool failed.
	 * @throws IllegalArgumentException If a parameter is invalid.
	 * @throws NullPointerException If one of the object parameters is null.
	 * @throws InterruptedException
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws InvalidToolRunnerException
	 */
	public CloneDetectionReport runTool(Path systemDirectory, int language, int fragmentType, int minlines, int mintokens, int maxlines, int maxtokens, double maxdifference) throws IllegalArgumentException, NullPointerException, InterruptedException, IOException, FileNotFoundException, InvalidToolRunnerException {
		Objects.requireNonNull(systemDirectory);
		Objects.requireNonNull(language);
		Objects.requireNonNull(fragmentType);
		if(!Files.exists(systemDirectory)) {
			throw new IllegalArgumentException("systemDirectory does not exist.");
		}
		if(!Files.isDirectory(systemDirectory)) {
			throw new IllegalArgumentException("systemDirectory is not a directory");
		}
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("language is not valid.");
		}
		if(!ExperimentSpecification.isFragmentTypeValid(fragmentType)) {
			throw new IllegalArgumentException("fragment type is not valid.");
		}
		if(minlines < 0) {
			throw new IllegalArgumentException("minlines must be >= 0.");
		}
		if(mintokens < 0) {
			throw new IllegalArgumentException("minlines must be >= 0.");
		}
		if(maxlines < 0 || maxlines < minlines) {
			throw new IllegalArgumentException("maxlines must be >= 0 and >= minlines.");
		}
		if(maxtokens < 0 || maxtokens < mintokens) {
			throw new IllegalArgumentException("maxtokens must be >=0 and >= mintokens.");
		}
		if(maxdifference < 0.0 || maxdifference > 1.0) {
			throw new IllegalArgumentException("maxdifference must be >= 0.0 and <= 1.0");
		}
		
		Runtime runtime = Runtime.getRuntime();
		String[] command = new String[12];
		command[0] = this.toolrunner.toAbsolutePath().normalize().toString();
		command[1] = systemDirectory.toAbsolutePath().normalize().toString(); 
		command[2] = ExperimentSpecification.languageToString(language); 
		command[3] = SystemUtil.getInstallRoot().toString();
		command[4] = this.getDirectory().toAbsolutePath().normalize().toString(); 
		command[5] = ExperimentSpecification.fragmentTypeToString(fragmentType); 
		command[6] = this.getToolRunner().getParent().toAbsolutePath().normalize().toString(); 
		command[7] = minlines + ""; 
		command[8] = mintokens + ""; 
		command[9] = maxlines + ""; 
		command[10] = maxtokens + ""; 
		command[11] = maxdifference + "";
		//System.out.println(command);
		Process process = null;
		BufferedReader br = null;
		String line = null;
		int retval;
		
		try {
			//Run the tool
			process = runtime.exec(command);
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			line = br.readLine();
			new StreamGobbler(process.getErrorStream()).run();
			retval = process.waitFor();
		} catch (IOException | InterruptedException e) {
			if(process != null) {
				try {process.getErrorStream().close();} catch(IOException ee) {};
				try {process.getInputStream().close();} catch(IOException ee) {};
				try {process.getOutputStream().close();} catch(IOException ee) {};
				process.destroy();
				process = null;
			}
			if(br != null) {
				try {br.close();} catch (IOException ee) {};
				br = null;
			}
			throw e;
		}
		
		//Cleanup
		if(process != null) {
			try {process.getErrorStream().close();} catch(IOException e) {};
			try {process.getInputStream().close();} catch(IOException e) {};
			try {process.getOutputStream().close();} catch(IOException e) {};
			process.destroy();
			process = null;
		}
		if(br != null) {
			try {br.close();} catch (IOException e) {};
			br = null;
		}
		
		//Check returned report path
		if(retval != 0) {
			System.out.println(retval);
			return null;
		} else {
			if(line == null) {
				throw new InvalidToolRunnerException("Tool runner returned success but did not provide a path to the clone report file.");
			} else {
				Path report = Paths.get(line).toAbsolutePath().normalize();
				if(!Files.exists(report)) {
					throw new FileNotFoundException("Clone report file reported by tool does not exist.");
				}
				return new CloneDetectionReport(report);
			}
		}
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		Tool t = (Tool) obj;
		if(this.description.equals(t.description) &&
				this.directory.equals(t.directory) &&
				this.name.equals(t.name) &&
				this.toolrunner.equals(t.toolrunner)) {
			return true;
		} else {
			return false;
		}
	}
}
