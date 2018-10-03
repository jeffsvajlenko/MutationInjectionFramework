// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package experiment;

public class AlreadyGeneratedException extends RuntimeException {

	public AlreadyGeneratedException() {
		super();
	}

	public AlreadyGeneratedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AlreadyGeneratedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AlreadyGeneratedException(String message) {
		super(message);
	}

	public AlreadyGeneratedException(Throwable cause) {
		super(cause);
	}

	private static final long serialVersionUID = -347808477941937935L;

}
