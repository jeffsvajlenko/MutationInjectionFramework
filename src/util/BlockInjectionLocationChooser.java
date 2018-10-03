// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
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
 * Class for choosing a injection location for a block fragment.  Locations are chosen
 * as the first or last line of a code block. On creation the class populates a list of 
 * possible locations.  Locations are selected on request at random without repeats.
 * The selection set can be reset.  This chooser makes the assumption that the source
 * files in the system have been normalized such that code block opening and closing
 * braces are on their own lines, which ensures the first and last line of a code block
 * are always valid injection locations for an additional code block.
 *
 */
public class BlockInjectionLocationChooser implements InjectionLocationChooser {

	private List<Fragment> blocks_ref;//List of all blocks in subject system
	private List<Fragment> blocks; //List of all the blocks in the subject system with selected removed
	private Random rdm_b;	//Random number generator for picking a block
	private Random rdm_sf; //Random number generator for inject start/end of block
	private int language; //Language of the system
	
	/**
	 * Creates a block injection location chooser.  Locations are chosen as after functions of the
	 * specified language from the specified system.
	 * @param systemDirectory The directory containing the system to get injection locations of.
	 * @param language The language of the system.
	 * @throws FileNotFoundException If the specified system directory does not exist.
	 * @throws NullPointerException If systemDirectory is null.
	 * @throws IllegalArgumentException If language is not supported. 
	 */
	public BlockInjectionLocationChooser(Path systemDirectory, int language) throws FileNotFoundException {
		Objects.requireNonNull(systemDirectory);
		if(!Files.isDirectory(systemDirectory)) {
			throw new FileNotFoundException("Specified directory does not exist.");
		}
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language is not supported.");
		}
		this.blocks_ref = SelectBlockFragments.getFragments(systemDirectory.toFile(), language);
		this.blocks = new LinkedList<Fragment>(this.blocks_ref);
		rdm_b = new Random();
		rdm_sf = new Random();
		this.language = language;
	}
	
	/**
	 * Chooses an injection location.  Locations are chosen at random, and repeats do not occur.  Injection locations
	 * are only chosen as the first or last line of a code block that is at least two lines long.  Assumes source
	 * files have been normalized such that the code block opening and closing braces are on their own lines.
	 * @return An injection location, or null if one could not be chosen (no remaining injection locations).
	 * @throws IOException If an IO error occurs while reading injection locations.
	 * @throws FileNotFoundException If a file containing an injection location is not found.
	 * @throws InterruptedException If the TXL process required is interrupted.
	 */
	@Override
	public InjectionLocation generateInjectionLocation() throws FileNotFoundException, IOException, InterruptedException {
		//Variables
		Fragment selectedFragment = null;
		Fragment candidateFragment = null;
		int rdmi;
		
		//search for an injection location
		while(blocks.size() != 0) {
			//Pick location
			rdmi = rdm_b.nextInt(blocks.size());
			candidateFragment = blocks.remove(rdmi);
			
			//Check validity (does it perfectly frame a block, is it at least two lines long)
			if(TXLUtil.isBlock(candidateFragment, language) && (candidateFragment.getStartLine() != candidateFragment.getEndLine())) { //yes, use it
				selectedFragment = candidateFragment;
				break;
			} else { //no, dicard to avoid recheck in future
				blocks.remove(rdmi);
				continue;
			}
		}
		
		if(selectedFragment != null) {
			if(rdm_sf.nextInt(2) == 0) {
				return new InjectionLocation(selectedFragment.getSrcFile(), selectedFragment.getEndLine());
			} else {
				return new InjectionLocation(selectedFragment.getSrcFile(), selectedFragment.getStartLine()+1);
			}
		} else {
			return null;
		}
	}

	@Override
	public void reset() {
		this.blocks = new LinkedList<Fragment>(this.blocks_ref);
	}
}