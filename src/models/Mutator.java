// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package models;

import java.io.IOException;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public interface Mutator<T extends Operator> {
	/**
	 * Returns a description of the mutator.
	 * @return a description of the mutator.
	 */
	public String getDescription();
	
	/**
	 * Returns the type of clone this mutator produces.
	 * @return the type of clone this mutator produces.
	 */
	public int getTargetCloneType();
	
	/**
	 * Returns the operators this mutator uses in the order in which it applies them.
	 * @return the operators this mutator uses in the order in which it applies them.
	 */
	public List<T> getOperators();
	
	/**
	 * Performs the mutation
	 * @param infile
	 * @param outfile
	 * @param numAttemptsMutator
	 * @param numAttemptsOperator
	 * @param allowedDiff
	 * @param containment
	 * @param language
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public int performMutation(Path infile, Path outfile, int numAttemptsMutator, int numAttemptsOperator, double allowedDiff, double containment, int language) throws SQLException, IOException, InterruptedException;
}
