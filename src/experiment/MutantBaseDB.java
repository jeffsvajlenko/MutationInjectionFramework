// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package experiment;

import java.nio.file.Path;

import models.Fragment;
import models.MutantBase;

public class MutantBaseDB extends MutantBase {

	private int id;
	
	public MutantBaseDB(int id, Path directory, int mutant_id, Fragment original_fragment, Fragment mutant_fragment) {
		super(directory, mutant_id, original_fragment, mutant_fragment);
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}

	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		MutantBaseDB other = (MutantBaseDB) obj;
		if( this.id == other.id &&
				this.getDirectory().equals(other.getDirectory()) &&
				this.getMutantId() == other.getMutantId() &&
				this.getOriginalFragment().equals(other.getOriginalFragment()) &&
				this.getMutantFragment().equals(other.getMutantFragment())) {
			return true;
		} else {
			return false;
		}
	}
	
}
