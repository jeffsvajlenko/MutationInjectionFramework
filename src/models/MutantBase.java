package models;

import java.nio.file.Path;
import java.util.Objects;


/**
 * Represents a mutant code base.
 */
public class MutantBase {
	private int mutant_id;
	private Path directory;
	private Fragment mfragment;
	private Fragment ofragment;
	
	/**
	 * Creates a Mutant Base.
	 * @param id The id of the mutant base in the database.
	 * @param directory
	 * @param injection_type
	 * @param mutant_id
	 * @param original_fragment
	 * @param mutant_fragment
	 */
	public MutantBase(Path directory, int mutant_id, Fragment original_fragment, Fragment mutant_fragment) {
	//Check Arguemtns	
		Objects.requireNonNull(directory);
		Objects.requireNonNull(original_fragment);
		Objects.requireNonNull(mutant_fragment);
		if(!original_fragment.getSrcFile().toAbsolutePath().normalize().startsWith(directory.toAbsolutePath().normalize())) {
			throw new IllegalArgumentException("Original fragment is not rooted in the directory.");
		}
		if(!mutant_fragment.getSrcFile().toAbsolutePath().normalize().startsWith(directory.toAbsolutePath().normalize())) {
			throw new IllegalArgumentException("Mutant fragment is not rooted in the directory.");
		}
	
	//Initialize Object
		this.directory = directory;
		this.mutant_id = mutant_id;
		this.ofragment = original_fragment;
		this.mfragment = mutant_fragment;
	}
	
	public Path getDirectory() {
		return directory;
	}
	
	/**
	 * Returns the id of the injected mutant.
	 * @return the id of the injected mutant.
	 */
	public int getMutantId() {
		return mutant_id;
	}
	
	/**
	 * Returns the original fragment.
	 * @return the original fragment.
	 */
	public Fragment getOriginalFragment() {
		return this.ofragment;
	}
	
	/**
	 * Returns the mutant fragment.
	 * @return the mutant fragment.
	 */
	public Fragment getMutantFragment() {
		return this.mfragment;
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		MutantBase other = (MutantBase) obj;
		if( this.directory.equals(other.directory) &&
				this.mutant_id == other.mutant_id &&
				this.ofragment.equals(other.ofragment) &&
				this.mfragment.equals(other.mfragment)) {
			return true;
		} else {
			return false;
		}
	}
}
