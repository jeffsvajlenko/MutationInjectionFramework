// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package models;

public class VerifiedClone extends Clone {

	boolean isclone;
	
	boolean verifiersuccess;

	public VerifiedClone(Fragment fragment1, Fragment fragment2, boolean isclone, boolean verifiersuccess) {
		super(fragment1, fragment2);
		this.isclone = isclone;
		this.verifiersuccess = verifiersuccess;
	}
	
	public boolean isClone() {
		return this.isclone;
	}
	
	public boolean isVerifierSuccess() {
		return this.verifiersuccess;
	}

	public String toString() {
		return new String(this.getFragment1() + " " + this.getFragment2() + " " + isclone + " " + verifiersuccess);
	}
	
	public boolean equals(Object obj) {
		//If obj null, then false
		if(obj == null) {
			return false;
		}
		
		//If obj not the same type as this, then false
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		//Cast and check
		VerifiedClone clone = (VerifiedClone) obj;		
		if(	this.isclone == clone.isclone && 
				this.verifiersuccess == clone.verifiersuccess && 
				this.getFragment1().equals(clone.getFragment1()) && 
				this.getFragment2().equals(clone.getFragment2())) {
			return true;
		} else {
			return false;
		}
	}
	
}
