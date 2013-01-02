package models;

import java.util.Objects;

/**
 * Represents a clone pair, containing two code fragments.
 */
public class Clone {

	/**
	 * Constant specifying the clone's type is unknown or unspecified.
	 */
	final static int TYPE_UNSPECIFIED = 0;
	
	/**
	 * Constant specifying the clone is type 1.
	 */
	final static int TYPE_1 = 1;
	
	/**
	 * Constant specifying the clone is type 2.
	 */
	final static int TYPE_2 = 2;
	
	/**
	 * Constant specifying the clone is type 3.
	 */
	final static int TYPE_3 = 3;
	
	/**
	 * Constant specifying the clone is type 4.
	 */
	final static int TYPE_4 = 4;
	
	/** Fragment 1 */
	private Fragment fragment1;
	
	/** Fragment 2 */
	private Fragment fragment2;
	
	/** The clone's type */
	private int type;
	
	/**
	 * Creates a new clone.
	 * @param fragment1 The first fragment.
	 * @param fragment2 The second fragment.
	 * @param type The clone's type.  Set by one of the TYPE_* constants.
	 * @throws IllegalArgumentException If one of the specified fragments is null, or if the type is invalid.
	 */
	public Clone(Fragment fragment1, Fragment fragment2, int type) {
	//Check Parameters
		//Check parameters not null
		Objects.requireNonNull(fragment1);
		Objects.requireNonNull(fragment2);
		if(type != TYPE_UNSPECIFIED && type != TYPE_1 && type != TYPE_2 && type != TYPE_3 && type != TYPE_4) {
			throw new IllegalArgumentException("Clone's type is invalid.");
		}
		
	//Initialize Object
		this.fragment1 = fragment1;
		this.fragment2 = fragment2;
		this.type = type;
	}
	
	/**
	 * Creates a new clone.
	 * @param fragment1 The first fragment.
	 * @param fragment2 The second fragment.
	 * @param type The clone's type.  Set by one of the TYPE_* constants.
	 * @throws IllegalArgumentException If one of the specified fragments is null, or if the type is invalid.
	 */
	public Clone(Fragment fragment1, Fragment fragment2) {
		this(fragment1, fragment2, Clone.TYPE_UNSPECIFIED);
	}
		
	/**
	 * Returns the first code fragment.
	 * @return the first code fragment.
	 */
	public Fragment getFragment1() {
		return this.fragment1;
	}
	
	/**
	 * Returns the second code fragment.
	 * @return the second code fragment.
	 */
	public Fragment getFragment2() {
		return this.fragment2;
	}
	
	/**
	 * Returns the clone's type.
	 * @return the clone's type.
	 */
	public int getType() {
		return this.type;
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
		Clone clone = (Clone) obj;		
		if(this.fragment1.equals(clone.fragment1) && this.fragment2.equals(clone.fragment2)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		String string;
		string = "Fragment 1:" + this.fragment1 + " Fragment 2:" + this.fragment2 + " Type: " + this.type;
		return string;
	}
}