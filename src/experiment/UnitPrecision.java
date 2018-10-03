// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package experiment;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import models.VerifiedClone;

public class UnitPrecision {
	
	/**The clone detection tool.*/
	int toolid;
	
	/**The mutant base.*/
	int baseid;
	
	/**The unit precision.*/
	double precision;
	
	/**The clone examined in determining unit precision.*/
	List<VerifiedClone> clones;
	
	/**
	 * Creates a UnitPrecision with the given values.
	 * @param toolid The tool's identifier.
	 * @param baseid The mutant base's identifier.
	 * @param precision The 
	 * @param clones Can be empty if precision is 1.0.
	 */
	public UnitPrecision(int toolid, int baseid, double precision, List<VerifiedClone> clones) throws NullPointerException, IllegalArgumentException {
	//Check Parameters	
		Objects.requireNonNull(clones);
		if(precision < 0 || precision > 1) {
			throw new IllegalArgumentException("Precision value is invalid.");
		}
		if(clones.size() < 1 && precision < 1.0) {
			throw new IllegalArgumentException("Clone list is empty when precision is not 1.0.");
		}
	
	//Initialize	
		this.toolid = toolid;
		this.baseid = baseid;
		this.precision = precision;
		this.clones = new LinkedList<VerifiedClone>();
		for(VerifiedClone vc : clones) {
			this.clones.add(vc);
		}
	}

	/**
	 * @return the toolid
	 */
	public int getToolid() {
		return toolid;
	}

	/**
	 * @return the baseid
	 */
	public int getBaseid() {
		return baseid;
	}

	/**
	 * @return the precision
	 */
	public double getPrecision() {
		return precision;
	}

	/**
	 * @return the clones considered in the precision 
	 */
	public List<VerifiedClone> getClones() {
		return new LinkedList<VerifiedClone>(clones);
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		UnitPrecision other = (UnitPrecision) obj;
		if(this.baseid == other.baseid &&
					this.toolid == other.toolid &&
					Math.abs(this.precision - other.precision) < 0.000001 &&
					cloneListEquals(this.clones, other.clones)) {
			return true;
		} else {
			return false;
		}
	}
	private boolean cloneListEquals(List<VerifiedClone> list1, List<VerifiedClone> list2) {
		if(list1.size() != list2.size()) {
			return false;
		} else {
			for(int i = 0; i < list1.size(); i++) {
				if(!list1.get(i).equals(list2.get(i))) {
					return false;
				}
			}
			return true;
		}
	}
}
