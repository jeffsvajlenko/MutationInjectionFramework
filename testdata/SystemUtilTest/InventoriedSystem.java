package models;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import util.FileUtil;
import util.SelectFunctionFragments;


public class InventoriedSystem {
	
	//Location of system
	private Path location;
	
	//System Inventory
	private List<Path> files;
	private List<Path> directories;
	private List<Path> leafDirectories;
	private List<FunctionFragment> functionFragments;
	
	//For random selection without repeats
	private List<Path> selectFiles;
	private List<Path> selectDirectories;
	private List<Path> selectLeafDirectories;
	private List<FunctionFragment> selectFunctionFragments;
	
	//Random Number Generator
	private Random random;
	
	//Language
	private String language;
	
	/**
	 * Creates an inventoried system from the specified directory.
	 * @param systemdir The directory containing the system.
	 * @throws IOException
	 */
	public InventoriedSystem(Path systemdir, String language) throws IOException {
		// Check input
		Objects.requireNonNull(systemdir);
		Objects.requireNonNull(language);
		if(!Files.exists(systemdir)) {
			throw new IllegalArgumentException("System does not exist.");
		}
		if(!Files.isDirectory(systemdir)) {
			throw new IllegalArgumentException("System is not a directory.");
		}
		
		//System Location
		this.location = systemdir.toAbsolutePath().normalize();
		
		//Language
		this.language = language.toLowerCase();
		
		//Files
		files = FileUtil.fileInventory(systemdir);
		selectFiles = new ArrayList<Path>(files);
		
		//Directories
		directories = FileUtil.directoryInventory(systemdir);
		selectDirectories = new ArrayList<Path>(directories);
		
		//LeafDirectories
		leafDirectories = new ArrayList<Path>();
		for(Path p : directories) {
			if(FileUtil.isLeafDirectory(p)) {
				leafDirectories.add(p);
			}
		}
		selectLeafDirectories = new ArrayList<Path>(leafDirectories);
		
		//Function Fragments
		this.functionFragments = SelectFunctionFragments.getFragments(this.location.toFile(), this.language);
		this.selectFunctionFragments = new LinkedList<FunctionFragment>(this.functionFragments);
		
		//Random Number Generator
		random = new Random();
	}

//--- Query Features
	
	/**
	 * Returns the number of files in the system.
	 * @return the number of files in the system.
	 */
	public int numFiles() {
		return this.files.size();
	}
	
	/**
	 * Returns the number of directories in the system.
	 * @return the number of directories in the system.
	 */
	public int numDirectories() {
		return this.directories.size();
	}
	
	/**
	 * Returns the number of leaf directories in the system.
	 * @return the number of leaf directories in the system.
	 */
	public int numLeafDirectories() {
		return this.leafDirectories.size();
	}
	
	/**
	 * Returns an unmodifiable list of all files in the system.
	 * @return an unmodifiable list of all files in the system.
	 */
	public List<Path> getFiles() {
		return Collections.unmodifiableList(this.files);
	}
	
	/**
	 * Returns an unmodifiable list of all directories in the system.
	 * @return an unmodifiable list of all directories in the system.
	 */
	public List<Path> getDirectories() {
		return Collections.unmodifiableList(this.directories);
	}
	
	/**
	 * Returns an unmodifiable list of all the leaf directories in the system.
	 * @return an unmodifiable list of all the leaf directories in the system.
	 */
	public List<Path> getLeafDirectories() {
		return Collections.unmodifiableList(this.leafDirectories);
	}
	
	/**
	 * Returns an unmodifiable list of all the function fragments in the system.
	 * @return an unmodifiable list of all the function fragments in the system.
	 */
	public List<FunctionFragment> getFunctionFragments() {
		return Collections.unmodifiableList(this.functionFragments);
	}
	
	/**
	 * Returns the location of the system (absolute and normalized).
	 * @return the location of the system.
	 */
	public Path getLocation() {
		return this.location;
	}
	
	/**
	 * Returns the system language.
	 * @return the system language.
	 */
	public String getLanguage() {
		return this.language;
	}
	
//--- Select Random Features, repeats	
	
	/**
	 * Returns a random file (as a Path object) from the system.  Repeats may occur in subsequent calls.
	 * @return a random file (as a path object) from the system, or null if there is no files to choose from.
	 */
	public Path getRandomFile() {
		if(files.size() == 0) {
			return null;
		} else {
			int index = random.nextInt(files.size());
			return files.get(index);
		}
	}
	
	/**
	 * Returns a random directory (as a Path object) from the system.  Repeats may occur in subsequent calls.
	 * @return a random directory (as a path object) from the system, or null if there is no directories to choose from.
	 */
	public Path getRandomDirectory() {
		if(directories.size() == 0) {
			return null;
		} else {
			int index = random.nextInt(directories.size());
			return directories.get(index);
		}	
	}
	
	/**
	 * Returns a random leaf directory (as a Path object) from the system.  Repeats may occur in subsequent calls.
	 * @return a random leaf directory (as a path object) from the system, or null if there is no directories to choose from.
	 */
	public Path getRandomLeafDirectory() {
		if(leafDirectories.size() == 0) {
			return null;
		} else {
			int index = random.nextInt(leafDirectories.size());
			return leafDirectories.get(index);
		}
	}
	
	/**
	 * Returns a random function fragment from the system.  Repeats may occur in subsequent calls.
	 * @return a random function fragment from the system.  Repeats may occur in subsequent calls.
	 */
	public Fragment getRandomFunctionFragment() {
		if(functionFragments.size() == 0) {
			return null;
		} else {
			int index = random.nextInt(functionFragments.size());
			return functionFragments.get(index);
		}
	}
	
//---- Select random features, no repeats.	
	
	/**
	 * Returns a random file (as a Path object) from the system.  Repeats do not occur with subsequent calls unless it is reset (see resetRandomFileRepeat).
	 * @return a random file (as a Path object) from the system, or null if no files left to chose from (due to no repeats).
	 */
	public Path getRandomFileNoRepeats() {
		if(selectFiles.size() == 0) {
			return null;
		}	else {
			int index = random.nextInt(selectFiles.size());
			Path p = selectFiles.remove(index);
			return p;
		}
	}
	
	/**
	 * Resets getRandomFileNoRepeats so that any file in the system may be chosen (again without repeats).
	 */
	public void resetRandomFileRepeat() {
		this.selectFiles = new ArrayList<Path>(this.files);
	}
	
	/**
	 * Returns a random directory (as a Path object) from the system.  Repeats do not occur with subsequent calls unless it is reset (see resetRandomDirectoryRepeat).
	 * @return a random directory (as a Path object) from the system, or null if no directory left to chose from (due to no repeats).
	 */
	public Path getRandomDirectoryNoRepeats() {
		if(selectDirectories.size() == 0) {
			return null;
		} else {
			int index = random.nextInt(selectDirectories.size());
			Path p = selectDirectories.remove(index);
			return p;
		}
	}
	
	/**
	 * Resets getRandomDirectory so that any directory in the system may be chosen (again without repeats).
	 */
	public void resetRandomDirectoryRepeat() {
		this.selectDirectories = new ArrayList<Path>(this.directories);
	}
	
	/**
	 * Returns a random leaf directory (as a Path object) from the system.  Repeats do not occur with subsequent calls unless it is reset (see resetRandomLeafDirectoryRepeat).
	 * @return a random leaf directory (as a Path object) from the system, or null if no leaf directory left to chose from (due to no repeats).
	 */
	public Path getRandomLeafDirectoryNoRepeats() {
		if(selectLeafDirectories.size() == 0) {
			return null;
		} else {
			int index = random.nextInt(selectLeafDirectories.size());
			Path p = selectLeafDirectories.remove(index);
			return p;
		}
	}
	
	/**
	 * Resets getRandomLeafDirectory so that any leaf directory in the system may be chosen (again without repeats).
	 */
	public void resetRandomLeafDirectoryRepeat() {
		this.selectLeafDirectories = new ArrayList<Path>(this.leafDirectories);
	}

	/**
	 * Returns a random function fragment from the system.  Repeats do not occur with subsequent calls unless it is reset (see resetRandomFunctionFragmentRepeat).
	 * @return a random function fragment from the system, or null of no function fragments left to chose from (due to no repeats).
	 */
	public FunctionFragment getRandomFunctionFragmentNoRepeats() {
		if(selectFunctionFragments.size() == 0) {
			return null;
		} else {
			int index = random.nextInt(selectFunctionFragments.size());
			FunctionFragment f = selectFunctionFragments.remove(index);
			return f;
		}
	}
	
	/**
	 * Resets getRandomFunctionFragmentNoRepeats so that any function fragment in the system may be chosen (again without repeats).
	 */
	public void resetRandomFunctionFragmentRepeat() {
		this.selectFunctionFragments = new ArrayList<FunctionFragment>(this.functionFragments);
	}
}
