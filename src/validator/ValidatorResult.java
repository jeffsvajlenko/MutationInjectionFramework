// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package validator;

public class ValidatorResult {
// Constants	
	
	// Clone Types
	/** Constant for type 0 (unknown type) clone. */
	public final static int TYPE0 = 0;
	
	/** Constant for type 1 clone. */
	public final static int TYPE1 = 1;
	
	/** Constant for type 2 clone. */
	public final static int TYPE2 = 2;
	
	/** Constant for type 3 clone. */
	public final static int TYPE3 = 3;
	
	/** Constant for type 4 clone. */
	public final static int TYPE4 = 4;
	
	// Validation Status
	/** Constant for result of test finding the clone to be a true positive. */
	public final static int TRUE_POSITIVE = 10;
	
	/** Constant for result of test being unable to determine clone status. */
	public final static int UNKNOWN = 11;
	
	/** Constant for result of test finding the clone to be a false positive. */
	public final static int FALSE_POSITIVE = 12;
	
	/** Constant for result of test failing to validate due to error. */
	public final static int ERROR = 13;
	
// Fields
	/** The acceptance threshold used during validation. */
	private double acceptThreshold;
	
	/** The rejection threshold used during validation. */
	private double rejectThreshold;
	
	/** 1 if a true positive, 0 if unknown, -1 if a false positive. */
	private int isClone;
	
	/** The clone's type if validation accepts the clone.  Otherwise, -1. */
	private int cloneType;
	
	/** A message associated with the validation. */
	private String msg;
	
	/** The similarity. */
	private double similarity;
	
	/** The unique percentage of items for fragment 1 */
	private double upi1;
	
	/** The unique percentage of items for fragment 2 */
	private double upi2;
	
	/**
	 * Creates a validator result object.
	 * @param acceptThreshold The acceptance threshold used during the validation.  If a value < 0 or > 1 is specified, it is rounded to 0 or 1, respectively.
	 * @param rejectThreshold The rejection threshold used during the validation.  If a value < 0 or > 1 is specified, it is rounded to 0 or 1, respectively.
	 * @param isClone The result of validation.
	 * @param cloneType The type of clone.
	 * @param msg A message to associate with the result.
	 * @throws IllegalArgumentException If illegal input parameters are specified.
	 */
	public ValidatorResult(double acceptThreshold, double rejectThreshold, int cloneType, int isClone, String msg, double similarity, double f1similarity, double f2similarity) {
		
		// Check input
		if(msg == null) {
			throw new IllegalArgumentException("Message is null.");
		}
		if(!(isClone == ValidatorResult.TRUE_POSITIVE || isClone == ValidatorResult.FALSE_POSITIVE || isClone == ValidatorResult.UNKNOWN || isClone == ValidatorResult.ERROR)) {
			throw new IllegalArgumentException("isClone value is invalid.");
		}
		if(!(cloneType == ValidatorResult.TYPE0 || cloneType == ValidatorResult.TYPE1 || cloneType == ValidatorResult.TYPE2 || cloneType == ValidatorResult.TYPE3 || cloneType == ValidatorResult.TYPE4)) {
			throw new IllegalArgumentException("cloneType value is invalid.");
		}
		if(rejectThreshold > acceptThreshold) {
			throw new IllegalArgumentException("RejectThreshold can not be greater than the acceptance threshold.");
		}
		if(similarity < 0.0 || similarity > 1.0) {
			throw new IllegalArgumentException("Similarity must be between 0.0 and 1.0 (inclusive).");
		}
		if(f1similarity < 0.0 || f1similarity > 1.0) {
			throw new IllegalArgumentException("Unique percentage of items for fragment1 must be between 0.0 and 1.0 (inclusive).");
		}
		if(f2similarity < 0.0 || f2similarity > 1.0) {
			throw new IllegalArgumentException("Unique percentage of items for fragment2 must be between 0.0 and 1.0 (inclusive).");
		}
		if(rejectThreshold < 0) {
			rejectThreshold = 0;
		}
		if(acceptThreshold < 0) {
			acceptThreshold = 0;
		}
		if(rejectThreshold > 1) {
			rejectThreshold = 1;
		}
		if(acceptThreshold > 1) {
			acceptThreshold = 1;
		}
		
		this.acceptThreshold = acceptThreshold;
		this.rejectThreshold = rejectThreshold;
		this.isClone = isClone;
		this.cloneType = cloneType;
		this.msg = msg;
		this.similarity = similarity;
		this.upi1 = f1similarity;
		this.upi2 = f2similarity;
	}
	
	/**
	 * The similarity value of the fragments.
	 * @return the similarity value of the fragments.
	 */
	public double getSimilarity() {
		return this.similarity;
	}
	
	/**
	 * Returns the percentage of the lines in fragment 1 that are unique.
	 * @return the percentage of the lines in fragment 1 that are unique.
	 */
	public double getFragment1Similarity() {
		return this.upi1;
	}
	
	/**
	 * Returns the percentage of the lines in fragment 2 that are unique.
	 * @return the percentage of the lines in fragment 2 that are unique.
	 */
	public double getFragment2Similarity() {
		return this.upi2;
	}
	
	/**
	 * The acceptance threshold used during validation.
	 * @return the acceptance threshold used during validation.
	 */
	public double getAcceptThreshold() {
		return this.acceptThreshold;
	}
	
	/**
	 * The rejection threshold used during validation.
	 * @return the rejection threshold used during validation.
	 */
	public double getRejectThreshold() {
		return this.rejectThreshold;
	}
	
	/**
	 * The result of validation.  Result may be false positive, true positive, unknown, or error.  Values as integers are defined by the constants in the ValidatorResult class.
	 * @return the result of validation.
	 */
	public int getValidationResult() {
		return this.isClone;
	}
	
	/**
	 * If true positive, the type of clone this is.  Result may be type 0, 1, 2 or 3.  Values as integers are defined by the constants in the ValidatorResult class.  Value is undefined (and meaningless) if validator result is false positive or uknown.
	 * @return If true positive, the type of clone this is.
	 */
	public int getCloneType() {
		return this.cloneType;
	}
	
	public String getMessage() {
		return this.msg;
	}
}
