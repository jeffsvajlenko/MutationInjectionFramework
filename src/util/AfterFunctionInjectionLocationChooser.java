package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import experiment.ExperimentSpecification;

import models.Fragment;
import models.InjectionLocation;

/**
 * 
 * Class for choosing a injection location for a function fragment.  Locations are chosen
 * as the line after a function existing in the specified system.  On creation the class
 * populates a list of possible locations.  Locations are selected on request at random
 * without repeats.  The selection set can be reset.  This chooser makes the assumption
 * that the source files in the system have been normalized such that code functions
 * opening and closing braces are on their own lines, which ensures that a function can
 * always be injected right after a closing brace and be a syntactically correct function.
 * 
 * Injection location is after functions rather than before in order to avoid separating
 * function signatures from their function.
 *
 */
public class AfterFunctionInjectionLocationChooser implements InjectionLocationChooser {

	/**
	 * Reference list of functions in the system.
	 */
	private List<Fragment> functions_ref;
	
	/**
	 * Set of fragments to choose from to get injection location.
	 */
	private List<Fragment> functions;
	
	/**
	 * Random for choosing an injection location.
	 */
	private Random rdm;
	
	/**
	 * Language of the system.
	 */
	private int language;
	
	/**
	 * Creates an after function injection location chooser.  Locations are chosen as after functions of the
	 * specified language from the specified system.
	 * @param systemDirectory The directory containing the system to get injection locations of.
	 * @param language The language of the system.
	 * @throws FileNotFoundException If the specified system directory does not exist.
	 * @throws NullPointerException If systemDirectory is null.
	 * @throws IllegalArgumentException If language is not supported. 
	 */
	public AfterFunctionInjectionLocationChooser(Path systemDirectory, int language) throws FileNotFoundException {
		//Check Input
		Objects.requireNonNull(systemDirectory);
		if(!Files.isDirectory(systemDirectory)) {
			throw new FileNotFoundException("Specified directory does not exist.");
		}
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language is not supported.");
		}
		
		//Get reference set of function fragments in the system
		this.functions_ref = SelectFunctionFragments.getFragments(systemDirectory.toFile(), language);
		
		//Initialize set to chose from
		this.functions = new LinkedList<Fragment>(this.functions_ref);
		
		//Initialize random number genreator
		rdm = new Random();
		
		//Remember langauge of system
		this.language = language;
	}
	
	/**
	 * Chooses an injection location.  Locations are chosen at random, and repeats do not occur.  Injection
	 * locations only chosen after functions which are at least two lines long.  Assumes Source file has
	 * been normalized (for example by astyle) such that function opening and closing braces are on their own
	 * lines.
	 * @return An injection location, or null if one could not be chosen (no remaining injection locations).
	 * @throws IOException If an IO error occurs while reading injection locations.
	 * @throws FileNotFoundException If a file containing an injection location is not found.
	 * @throws InterruptedException If the TXL process required is interrupted.
	 */
	@Override
	public InjectionLocation generateInjectionLocation() throws FileNotFoundException, IOException, InterruptedException {
		Fragment selectedFragment = null;
		Fragment candidateFragment = null;
		int rdmi;
		
		//search for an injection location
		while(functions.size() != 0) {
			rdmi = rdm.nextInt(functions.size());
			candidateFragment = functions.remove(rdmi);
			
			//Check if valid place to inject at (perfectly frames a function, at least two lines long)
			 if(TXLUtil.isFunction(candidateFragment, this.language) && (candidateFragment.getStartLine() != candidateFragment.getEndLine())) { //yes, use it
				selectedFragment = candidateFragment;
				break;
			} else { //no, discard it to avoid recheck in future
				functions.remove(rdmi);
				continue;
			}
		}
		
		//Return injection location
		if(selectedFragment == null) {
			return null;
		} else {
			return new InjectionLocation(selectedFragment.getSrcFile().toAbsolutePath().normalize(), selectedFragment.getEndLine()+1); //+1 so its after
		}
	}

	@Override
	public void reset() {
		this.functions = new LinkedList<Fragment>(this.functions_ref);
	}
}
