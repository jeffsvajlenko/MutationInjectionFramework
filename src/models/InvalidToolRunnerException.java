package models;

public class InvalidToolRunnerException extends Exception {
	private static final long serialVersionUID = 3179938479187850815L;

	public InvalidToolRunnerException() {
	}

	public InvalidToolRunnerException(String message) {
		super(message);
	}

	public InvalidToolRunnerException(Throwable cause) {
		super(cause);
	}

	public InvalidToolRunnerException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidToolRunnerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
