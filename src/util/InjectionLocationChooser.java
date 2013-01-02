package util;

import java.io.FileNotFoundException;
import java.io.IOException;

import models.InjectionLocation;

/**
 * 
 * Interfaces for classes implementing an injection location algorithm.
 * The algorithm should take at creation a system directory and language.
 * It should construct a set of possible code fragment injection locations,
 * and return one at random and without repeats when 
 * generateInjectionLocation is called.  It should also provide a reset
 * function to reset the possible injection location set (restore previously
 * selected).
 *
 */
public interface InjectionLocationChooser {
	/**
	 * Chooses an injection location.  Locations are chosen at random, and repeats do not occur.
	 * @return An injection location, or null if one could not be chosen (no remaining injection locations).
	 * @throws IOException If an IO error occurs while reading injection locations.
	 * @throws FileNotFoundException If a file containing an injection location is not found.
	 * @throws InterruptedException If the TXL process required is interrupted.
	 */
	public InjectionLocation generateInjectionLocation() throws FileNotFoundException, IOException, InterruptedException;
	
	/**
	 * Resets the set of potential injection locations (adds previously chosen locations back in).
	 */
	public void reset();
}
