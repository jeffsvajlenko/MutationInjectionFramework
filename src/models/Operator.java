package models;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Objects;

import experiment.ExperimentSpecification;

import util.FileUtil;
import util.StreamGobbler;
import util.SystemUtil;
import util.TXLUtil;

/**
 * 
 * Represents a mutation operator.
 * 
 */
public class Operator {
	private String name; /** The operator's unique identifier */
	private String description; /** The description of the operator */
	private int targetCloneType; /** The type of clone this operator produces */
	private Path mutator; /** Script used to perform this mutation operator */
	
	/**
	 * Creates an operator.
	 * @param name The operator's name.
	 * @param description The description of the operator.
	 * @param targetCloneType The type of clone this operator produces.  Must be in set: {1,2,3}.
	 * @param mutator Script which performs the mutator.
	 * @throws IllegalArgumentException If targetCloneType is not one of {1,2,3}.
	 * @throws NullPointerException If any of the object parameters are null.
	 */
	public Operator(String name, String description, int targetCloneType, Path mutator) {
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(mutator);
		
		if(targetCloneType != 1 && targetCloneType != 2 && targetCloneType != 3) {
			throw new IllegalArgumentException("Target clone type is invalid.");
		}
		if(!Files.isReadable(mutator)) {
			throw new IllegalArgumentException("Mutator is not readable.");
		}
		
		this.name = name;
		this.description = description;
		this.targetCloneType = targetCloneType;
		this.mutator = mutator;
	}
	
	/**
	 * Returns the path to the operator.
	 * @return the path to the operator.
	 */
	public Path getMutator() {
		return this.mutator;
	}
	
	/**
	 * Returns this operator's unique identifier.
	 * @return the operator's unique identifier.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Returns the description of this operator.
	 * @return the description of this operator.
	 */
	public String getDescription() {
		return description;
	} 
	
	/**
	 * Returns the clone type this operator produces.
	 * @return the clone type this operator produces.
	 */
	public int getTargetCloneType() {
		return targetCloneType;
	}

	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		Operator o = (Operator) obj;
		if(this.description.equals(o.description) &&
				this.name == o.name &&
				this.mutator.equals(o.mutator) &&
				this.targetCloneType == o.targetCloneType) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Performs this mutation operator on infile to create outfile.
	 * @param infile The input file.
	 * @param outfile The output file.
	 * @param allowedDiff The allowed % difference between the original and mutated fragment (in case of type 3 clone).
	 * @return 0 if mutation succeeded, or -1 if it failed.  Failure could occur if the operator can not be applied to the input file or if the mutation causes the fragments to diverge more than the allowed difference.
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws InterruptedException 
	 */
	public int performOperator(Path infile, Path outfile, int numAttempts, int language) throws FileNotFoundException, IOException, InterruptedException {
		boolean txl_retval;
		double sim_retval;		

		Path tmpfile1 = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "Operator-PerformOperator", null);
		Path tmpfile2 = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "Operator-PerformOperator", null);
		
		while(numAttempts > 0) {//keep trying it don't get it right until attempts is used up
			numAttempts--;
			
			// Perform the mutation
			String[] command = new String[6];
			command[0] = mutator.toAbsolutePath().normalize().toString();
			command[1] = SystemUtil.getTxlExecutable().toString();
			command[2] = ExperimentSpecification.languageToString(language);
			command[3] = SystemUtil.getOperatorsPath().toString();
			command[4] = infile.toAbsolutePath().normalize().toString();
			command[5] = outfile.toAbsolutePath().normalize().toString();
			
			Process p = Runtime.getRuntime().exec(command);
			new StreamGobbler(p.getErrorStream()).start();
			new StreamGobbler(p.getInputStream()).start();
			
			int pretval = p.waitFor();
			// If mutation failed, give up (not possible to do the mutation even if retry)
			if(pretval != 0) {
				break;
			}
			
			// Check if mutation produced valid output
			if(getTargetCloneType() == 1) { //type1 check
				// Create pretty-printed versions of the fragment
				txl_retval = TXLUtil.prettyPrintSourceFragment(infile, tmpfile1, language);
				if(!txl_retval) { // parsing failed, original file is defective, fail
					break;
				}
				
				// Create pretty-printed version of the mutant
				txl_retval = TXLUtil.prettyPrintSourceFragment(outfile, tmpfile2, language);
				if(!txl_retval) { // parsing failed, something went wrong during mutation, try again
					continue;
				}
				
				// Is this a proper type1 clone? (is 100% similar?)
				sim_retval = FileUtil.getSimilarity(tmpfile1, tmpfile2);
				if(Math.abs(sim_retval-1) < 0.00000001) {
					Files.deleteIfExists(tmpfile1);
					Files.deleteIfExists(tmpfile2);
					return 0;
				} else {
					continue;
				}
			} else if (getTargetCloneType() == 2) { //type2 check
				// Create pretty-printed, blind-renamed versions of the fragment and mutant
				txl_retval = TXLUtil.blindRename(infile, tmpfile1, language);
				if(!txl_retval) { // parsing failed on original fragment, fail now
					break;
				}
				txl_retval = TXLUtil.blindRename(outfile, tmpfile2, language);
				if(!txl_retval) { // parsing failed, something went wrong during mutation, try again
					continue;
				}
				
				// Is this a proper type2 clone?
				sim_retval = FileUtil.getSimilarity(tmpfile1,tmpfile2);
				if(Math.abs(sim_retval-1) < 0.00000001) {
					Files.deleteIfExists(tmpfile1);
					Files.deleteIfExists(tmpfile2);
					return 0;
				} else {
					continue;
				}
			} else if (getTargetCloneType() == 3) { //type3 check
				// Create pretty-printed versions of the fragment and mutant, not to use for checking but just to ensure they are parseable (Correct syntax)
				txl_retval = TXLUtil.prettyPrintSourceFragment(infile, tmpfile1, language);
				if(!txl_retval) { // parsing failed, original 
					continue;
				}
				txl_retval = TXLUtil.prettyPrintSourceFragment(outfile, tmpfile2, language);
				if(!txl_retval) { // parsing failed, something went wrong, try again
					continue;
				}
				
				// Is this an acceptable type3 clone (using original files)
				sim_retval = FileUtil.getSimilarity(tmpfile1, tmpfile2);
				if(sim_retval < 1.0) {
					Files.deleteIfExists(tmpfile1);
					Files.deleteIfExists(tmpfile2);
					return 0;
				} else {
					continue;
				}
			} else { // ???, operator has unknown clone type
				break;
			}
		}
		// reach here if failed in the given number of attempts
		//Files.deleteIfExists(tmpfile1);
		//Files.deleteIfExists(tmpfile2);
		Files.deleteIfExists(tmpfile1);
		Files.deleteIfExists(tmpfile2);
		return -1;
	}
}
