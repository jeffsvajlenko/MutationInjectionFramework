// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package experiment;

import java.nio.file.Path;

import models.CloneDetectionReport;

public class CloneDetectionReportDB extends CloneDetectionReport {

	int toolid;
	int baseid;
	
	/**
	 * Constructs a clone detection report tied to the database.
	 * @param toolid The tool which created the report.
	 * @param baseid The base analyzed.
	 * @param report Path to the report.
	 */
	public CloneDetectionReportDB(int toolid, int baseid, Path report) {
		super(report);
		this.toolid = toolid;
		this.baseid = baseid;
	}
	
	public CloneDetectionReportDB(int toolid, int baseid, Path report, Path root) {
		super(report, root);
		this.toolid = toolid;
		this.baseid = baseid;
	}
	
	/**
	 * Returns the id of the tool which produced this report.
	 * @return the id of the tool which produced this report.
	 */
	public int getToolId() {
		return this.toolid;
	}
	
	/**
	 * Returns the id of the base which was analysed.
	 * @return the id of the base which was analysed.
	 */
	public int getBaseId() {
		return this.baseid;
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		CloneDetectionReportDB other = (CloneDetectionReportDB) obj;
		if(this.toolid == other.toolid &&
				this.baseid == other.baseid &&
				this.getReport().equals(other.getReport())) {
			return true;
		} else {
			return false;
		}
	}

}
