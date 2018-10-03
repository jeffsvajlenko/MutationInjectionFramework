// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package experiment;

/**
 * 
 * Exception thrown when an error is found in the manual clone specification
 * file during manual clone generation.
 *
 */
public class IllegalManualImportSpecification extends Exception {

	public IllegalManualImportSpecification() {
		super();
	}

	public IllegalManualImportSpecification(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public IllegalManualImportSpecification(String message, Throwable cause) {
		super(message, cause);
	}

	public IllegalManualImportSpecification(String message) {
		super(message);
	}

	public IllegalManualImportSpecification(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -3093742556914544964L;

}
