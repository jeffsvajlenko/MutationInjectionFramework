package experiment;

import java.util.Objects;

import models.Clone;

/**
 * 
 * Captures the unit recall performance of some tool for some mutant base.
 * 
 */
public class UnitRecall {

	/** The tool who this unit recall is for */
	int toolid;

	/** The mutant base this unit recall is for */
	int baseid;

	/** The recall value */
	double recall;

	/** The matching clone which provided this unit recall */
	Clone clone;

	/**
	 * 
	 * @param toolid
	 * @param baseid
	 * @param recall
	 * @param clone
	 * @throws NullPointerException If clone is null when recall is not 0.0.
	 * @throws IllegalPointerException If recall is < 0.0 or > 1.0.
	 */
	public UnitRecall(int toolid, int baseid, double recall, Clone clone) {
		// Check Parameters
		if(recall != 0.0) {
			Objects.requireNonNull(clone);
		}
		if (recall < 0.0 || recall > 1.0) {
			throw new IllegalArgumentException("Recall must be of range: 0.0 <= recall <= 1.0");
		}

		// Initialize
		this.toolid = toolid;
		this.baseid = baseid;
		this.recall = recall;
		this.clone = clone;
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
	 * @return the recall
	 */
	public double getRecall() {
		return recall;
	}

	/**
	 * @return the clone
	 */
	public Clone getClone() {
		return clone;
	}

	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		if (!this.getClass().equals(o.getClass())) {
			return false;
		}

		UnitRecall other = (UnitRecall) o;
		if (this.baseid == other.baseid && this.toolid == other.toolid && Math.abs(this.recall - other.recall) < 0.000001) {
			if(this.clone == null && other.clone == null) {
				return true;
			} else if (this.clone != null && other.clone != null) {
				if(this.clone.equals(other.clone)) {
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
