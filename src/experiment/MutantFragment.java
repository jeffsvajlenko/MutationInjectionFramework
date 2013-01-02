package experiment;

import java.nio.file.Path;
import java.util.Objects;

/**
 * Represents a mutant fragment.
 * 
 * Can represent an independent mutant fragment or one in the database (based on value of the identifier).
 */
public class MutantFragment {
	int id;
	int fragment_id;
	Path fragmentfile;
	int mutator_id;
	
	/**
	 * Creates a mutant fragment that is tied to the database by the id.
	 * @param id The id for this mutant.  Forms a unique identifier with the fragment_id.
	 * @param fragment_id The id of the fragment this mutant was made from.
	 * @param srcfile The file containing this fragment (a file with only the mutant's code).
	 * @param mutator_id id of the mutator that made this fragment.
	 */
	public MutantFragment(int id, int fragment_id, Path srcfile, int mutator_id) {
	// Check Arguments
		Objects.requireNonNull(srcfile);
		
	//Initialize 
		this.id = id;
		this.fragment_id = fragment_id;
		this.fragmentfile = srcfile;
		this.mutator_id = mutator_id;
	}
	
	/**
	 * Returns the mutant's id.
	 * @return the mutant's id.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns the id of the fragment this mutant was made from.
	 * @return the id of the fragment this mutant was made from.
	 */
	public int getFragmentId() {
		return this.fragment_id;
	}
	
	/**
	 * Returns the file containing this mutant fragment.
	 * @return the file containing this mutant fragment.
	 */
	public Path getFragmentFile() {
		return this.fragmentfile;
	}
	
	/**
	 * Returns the id of the mutator that made this mutant.
	 * @return the id of the mutator that made this mutant.
	 */
	public int getMutatorId() {
		return this.mutator_id;
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!obj.getClass().equals(this.getClass())) {
			return false;
		}
		
		MutantFragment m = (MutantFragment) obj;
		if(this.fragment_id == m.fragment_id &&
				this.id == m.id &&
				this.mutator_id == m.mutator_id &&
				this.fragmentfile.equals(m.fragmentfile)) {
			return true;
		} else {
			return false;
		}
	}
}