package experiment;

import java.nio.file.Path;
import java.util.Objects;

public class ExperimentSpecification {
	public final static int FUNCTION_FRAGMENT_TYPE = 0;
	public final static int BLOCK_FRAGMENT_TYPE = 1;
	public final static int AUTOMATIC_GENERATION_TYPE = 0;
	public final static int MANUAL_GENERATION_TYPE = 1;
	public final static int JAVA_LANGUAGE = 0;
	public final static int C_LANGUAGE = 1;
	public final static int CS_LANGUAGE = 2;
	
	private Path data;
	private Path system;
	private Path repository;
	private int language;
	
	//private int generationType;
	private int fragmentType;
	private int fragmentMinSizeLines;
	private int fragmentMaxSizeLines;
	private int fragmentMinSizeTokens;
	private int fragmentMaxSizeTokens;
	//private int mutationNumber;
	private int injectNumber;
	private double subsumeMatcherTolerance;
	private double allowedFragmentDifference;
	private double mutationContainment;
	private int mutationAttempts;
	private int operatorAttempts;
	private int maxFragments;
	private double precisionRequiredSimilarity;
	private double recallRequiredSimilarity;
	
	/**
	 * Creates an ExperimentSpecifications object with the specified attributes.  Other properties are set to default values.
	 * @param data Where to store the experiment data.
	 * @param system The system for the experiment.
	 * @param repository The repository for the experiment.
	 * @param language The language of the system.
	 * @throws IllegalArgumentException
	 */
	public ExperimentSpecification(Path data, Path system, Path repository, int language) {
		Objects.requireNonNull(data);
		Objects.requireNonNull(system);
		Objects.requireNonNull(repository);
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language is invalid.");
		}
		
		this.data = data;
		this.system = system;
		this.repository = repository;
		this.language = language;
		
		//Some Defaults?
//		generationType = ExperimentSpecification.AUTOMATIC_GENERATION_TYPE;
		fragmentType = ExperimentSpecification.FUNCTION_FRAGMENT_TYPE;
		fragmentMinSizeLines = 15;
		fragmentMinSizeTokens = 50;
		fragmentMaxSizeLines = 100;
		fragmentMaxSizeTokens = 500;
		//mutationNumber = 1;
		injectNumber = 1;
		subsumeMatcherTolerance = 0.15;
		allowedFragmentDifference = 0.30;
		mutationContainment = 0.15;
		mutationAttempts = 100;
		operatorAttempts = 10;
		maxFragments = 50;
		precisionRequiredSimilarity = 0.70;
		recallRequiredSimilarity = 0.70;
	}

//------------------------------------------------------------
	
	/**
	 * @return Where the data will be stored.
	 */
	public Path getDataPath() {
		return data;
	}

	/**
	 * @param dataPath Where to store the experiment data.
	 */
	public void setDataPath(Path dataPath) {
		Objects.requireNonNull(dataPath);
		this.data = dataPath;
	}

	//------------------------------------------------------------
	
	/**
	 * @return The system.
	 */
	public Path getSystem() {
		return system;
	}

	/**
	 * @param system The system to use.
	 */
	public void setSystem(Path system) {
		Objects.requireNonNull(system);
		this.system = system;
	}

	//------------------------------------------------------------
	
	/**
	 * @return The repository.
	 */
	public Path getRepository() {
		return repository;
	}

	/**
	 * @param repository The repository to use.
	 */
	public void setRepository(Path repository) {
		Objects.requireNonNull(repository);
		this.repository = repository;
	}
	
//------------------------------------------------------------
	
	/**
	 * Returns if the language is supported.  Language is specified by constant defined in this class.
	 * @param language Constant representing the language, as defined in this class.
	 * @return if the language is supported.
	 */
	public static boolean isLanguageSupported(int language) {
		if(language != ExperimentSpecification.JAVA_LANGUAGE && language != ExperimentSpecification.C_LANGUAGE && language != ExperimentSpecification.CS_LANGUAGE) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Returns the specified language constant as a string.  Mapping is as follows:
	 * 
	 * JAVA_LANGUAGE -> "java"
	 * C_LANGUAGE -> "c"
	 * CS_LANGUAGE -> "cs"
	 * 
	 * @param language The language constant.
	 * @return the specified language constant as a string.
	 */
	public static String languageToString(int language) {
		if(!isLanguageSupported(language)) {
			throw new IllegalArgumentException("Invalid language.");
		} else {
			if(language == ExperimentSpecification.JAVA_LANGUAGE) {
				return "java";
			} else if (language == ExperimentSpecification.C_LANGUAGE) {
				return "c";
			} else if (language == ExperimentSpecification.CS_LANGUAGE) {
				return "cs";
			} else {
				throw new IllegalArgumentException("Invalid language.");
			}
		}
	}
	
	/**
	 * @return The system language.
	 */
	public int getLanguage() {
		return language;
	}

	/**
	 * @param language The language of the system.  Language must be JAVA_LANGUAGE or C_LANGUAGE, or CS_LANGUAGE.
	 * @throws IllegalArgumentException If language is invalid.
	 */
	public void setLanguage(int language) {
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language is invalid.");
		}
		this.language = language;
	}
	
//------------------------------------------------------------

	/**
	 * Checks if generationType is valid, with respect to constants defined in this class.
	 * @param generationType The type to check.
	 * @return if generationType is valid.
	 */
	public static boolean isGenerationTypeValid(int generationType) {
		if (generationType != ExperimentSpecification.AUTOMATIC_GENERATION_TYPE && generationType != ExperimentSpecification.MANUAL_GENERATION_TYPE) {
			return false;
		} else {
			return true;
		}
	}
	
//	/**
//	 * @return The generation type.
//	 */
//	public int getGenerationType() {
//		return generationType;
//	}
// -- Set my experiment
//	/**
//	 * @param generationType The generation type to use.  Must be AUTOMATIC_GENERATION_TYPE or MANUAL_GENERATION_TYPE of this class.
//	 * @throws IllegalArgumentValue if the value is invalid.
//	 */
//	public void setGenerationType(int generationType) throws IllegalArgumentException {
//		if (!ExperimentSpecification.isGenerationTypeValid(generationType)) {
//			throw new IllegalArgumentException("Specified generation type is invalid.");
//		}
//		this.generationType = generationType;
//	}

//------------------------------------------------------------
	
	/**
	 * Checking if fragmentType is valid, with respect to constants defined in this class.
	 * @param fragmentType The type to check.
	 * @return if fragmentType is valid.
	 */
	public static boolean isFragmentTypeValid(int fragmentType) {
		if(fragmentType != ExperimentSpecification.FUNCTION_FRAGMENT_TYPE && fragmentType != ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * @return the fragmentType The fragment type.
	 */
	public int getFragmentType() {
		return fragmentType;
	}

	/**
	 * @param fragmentType The fragment type to use.  Must be FUNCTION_FRAGMENT_TYPE or BLOCK_FRAGMENT_TYPE.
	 * @throws IllegalArgumentException If fragment type is invalid.
	 */
	public void setFragmentType(int fragmentType) throws IllegalArgumentException {
		if (!ExperimentSpecification.isFragmentTypeValid(fragmentType)) {
			throw new IllegalArgumentException("Fragment type is invalid.");
		}
		this.fragmentType = fragmentType;
	}

	/**
	 * Returns a string representation of the fragment type.
	 * FUNCTION_FRAGMENT_TYPE = function
	 * BLOCK_FRAGMENT_TYPE = block
	 * @param fragmentType The fragment type (FUNCTION_FRAGMENT_TYPE or BLOCK_FRAGMENT_TYPE)
	 * @throws IllegalArgumentException if the fragment type specified does not exist.
	 * @return a string representation of the fragment type.
	 */
	public static String fragmentTypeToString(int fragmentType) {
		if(fragmentType == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
			return "function";
		} else if(fragmentType == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
			return "block";
		} else {
			throw new IllegalArgumentException("Fragment type is invalid.");
		}
	}
	
//------------------------------------------------------------
	
	public static boolean isFragmentMinSizeLinesValid(int size, int fragmentMaxSizeLines) {
		if(size <= 0) {
			return false;
		} else {
			if(size > fragmentMaxSizeLines) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * @return The minimum fragment size in lines.
	 */
	public int getFragmentMinSizeLines() {
		return fragmentMinSizeLines;
	}

	/**
	 * @param fragmentMinSizeLines The minimum fragment size in lines to use.  Must be greater than 0.  Must be less than or equal to the currently set framgnet maximum size in lines.
	 */
	public void setFragmentMinSizeLines(int fragmentMinSizeLines) throws IllegalArgumentException {
		if(fragmentMinSizeLines <= 0) {
			throw new IllegalArgumentException("Fragment minimum size in lines must be greater than 0.");
		}
		if(fragmentMinSizeLines > this.fragmentMaxSizeLines) {
			throw new IllegalArgumentException("Fragment minimum size in lines must be less than or equal to the fragment maximum size in lines.");
		}
		this.fragmentMinSizeLines = fragmentMinSizeLines;
	}

//------------------------------------------------------------	
	
	/**
	 * @return the fragmentMaxSizeLines The maximum fragment size in lines.
	 */
	public int getFragmentMaxSizeLines() {
		return fragmentMaxSizeLines;
	}

	/**
	 * @param fragmentMaxSizeLines The maximum fragment size in lines to use.  Must be greater than 0.  Must be greater than or equal to the currently set fragment minimum size in lines.
	 */
	public void setFragmentMaxSizeLines(int fragmentMaxSizeLines) throws IllegalArgumentException {
		if(fragmentMaxSizeLines <= 0) {
			throw new IllegalArgumentException("Fragment maximum size in lines must be greater than 0.");
		}
		if(fragmentMaxSizeLines < this.fragmentMinSizeLines) {
			throw new IllegalArgumentException("Fragment maximum size in lines must be greater than or equal to the fragment minimum size in lines.");
		}
		this.fragmentMaxSizeLines = fragmentMaxSizeLines;
	}

	/**
	 * @return The minimum fragment size in tokens.
	 */
	public int getFragmentMinSizeTokens() {
		return fragmentMinSizeTokens;
	}

	/**
	 * @param fragmentMinSizeTokens The minimum fragment size in tokens.  Must be greater than 0,  Must be less than or equal to maximum fragment size in tokens.
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setFragmentMinSizeTokens(int fragmentMinSizeTokens) throws IllegalArgumentException {
		if(fragmentMinSizeTokens <= 0) {
			throw new IllegalArgumentException("Fragment minimum size in tokens must be greater than 0.");
		}
		if(fragmentMinSizeTokens > this.fragmentMaxSizeTokens) {
			throw new IllegalArgumentException("Fragment minimum size in tokens must be less than or equal to the fragment maximum size in tokens.");
		}
		this.fragmentMinSizeTokens = fragmentMinSizeTokens;
	}

	/**
	 * @return The maximum fragment size in tokens.
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public int getFragmentMaxSizeTokens() {
		return fragmentMaxSizeTokens;
	}

	/**
	 * @param fragmentMaxSizeTokens The maximum fragment size in tokens.  Must be greater than 0.  Must be greater than or equal minimum fragment size in tokens.
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setFragmentMaxSizeTokens(int fragmentMaxSizeTokens) throws IllegalArgumentException {
		if(fragmentMaxSizeTokens <= 0) {
			throw new IllegalArgumentException("Fragment maximum size in tokens must be greater than 0.");
		}
		if(fragmentMaxSizeTokens < this.fragmentMinSizeTokens) {
			throw new IllegalArgumentException("Fragment maximum size in tokens must be greater than or equal to the fragment minimum size in tokens.");
		}
		this.fragmentMaxSizeTokens = fragmentMaxSizeTokens;
	}

	/**
	 * @return The number of times to mutate each fragment (per mutator).
	 */
//	public int getMutationNumber() {
//		return mutationNumber;
//	}

	/**
	 * @param mutationNumber The number of times to mutate each fragment (per mutator).  Mutation number must be greater than 0.
	 * @throws IllegalArgumentException if value is invalid.
	 */
//	public void setMutationNumber(int mutationNumber) throws IllegalArgumentException {
//		if(mutationNumber <= 0) {
//			throw new IllegalArgumentException("Mutation number must be greater than 0.");
//		}
//		this.mutationNumber = mutationNumber;
//	}

	/**
	 * @return The number of times each generated clone is injected.
	 */
	public int getInjectNumber() {
		return injectNumber;
	}

	/**
	 * @param injectNumber The number of times to inject each generated clone.  Inject number must be greater than 0.
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setInjectNumber(int injectNumber) {
		if(injectNumber <= 0) {
			throw new IllegalArgumentException("Inject number must be greater than 0.");
		}
		this.injectNumber = injectNumber;
	}

	/**
	 * @return The subsume matcher tolerance.
	 */
	public double getSubsumeMatcherTolerance() {
		return subsumeMatcherTolerance;
	}

	/**
	 * @param subsumeMatcherTolerance The subsume matcher tolerance value to use, must be in range [0, 1].
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setSubsumeMatcherTolerance(double subsumeMatcherTolerance) {
		if(subsumeMatcherTolerance < 0.0 || subsumeMatcherTolerance > 1.0) {
			throw new IllegalArgumentException("Subsume matcher tolerance must be in range [0,1].");
		}
		this.subsumeMatcherTolerance = subsumeMatcherTolerance;
	}

	/**
	 * @return How different fragments and mutant fragments are allowed to be (a % difference in range [0, 1]).
	 */
	public double getAllowedFragmentDifference() {
		return allowedFragmentDifference;
	}

	/**
	 * @param allowedFragmentDifference How different fragment and mutant fragment are allowed to be.  Must be in range [0, 1].
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setAllowedFragmentDifference(double allowedFragmentDifference) {
		if(allowedFragmentDifference < 0.0 || allowedFragmentDifference > 1.0) {
			throw new IllegalArgumentException("Allowed fragment difference is invalid.");
		}
		this.allowedFragmentDifference = allowedFragmentDifference;
	}

	/**
	 * @return How the mutations are contained.  The % of the start/end of the fragment which won't be altered.
	 */
	public double getMutationContainment() {
		return mutationContainment;
	}

	/**
	 * @param mutationContainment How the mutations are contained.  The % of the start/end of the fragment which won't be altered.  Must be in range [0, 0.5].
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setMutationContainment(double mutationContainment) {
		if(mutationContainment < 0.0 || mutationContainment > 0.5) {
			throw new IllegalArgumentException("Mutation containment must be in range [0, 0.5].");
		}
		this.mutationContainment = mutationContainment;
	}

	/**
	 * @return The number of times to attempt a mutator before giving up.
	 */
	public int getMutationAttempts() {
		return mutationAttempts;
	}

	/**
	 * @param mutationAttempts The number of times to attempt a mutator before giving up.  Must be greater than 0.
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setMutationAttempts(int mutationAttempts) {
		if(mutationAttempts <= 0) {
			throw new IllegalArgumentException("Mutation attmpts must be greater than 0.");
		}
		this.mutationAttempts = mutationAttempts;
	}

	/**
	 * @return The number of times to attempt an operator before giving up.
	 */
	public int getOperatorAttempts() {
		return operatorAttempts;
	}

	/**
	 * @param operatorAttempts The number of times to attempt an operator before giving up.  Must be greater than 0.
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setOperatorAttempts(int operatorAttempts) {
		if(operatorAttempts <= 0) {
			throw new IllegalArgumentException("Operator attemtps must be greater than 0.");
		}
		this.operatorAttempts = operatorAttempts;
	}

	/**
	 * @return The maximum number of fragments to pick.
	 */
	public int getMaxFragments() {
		return maxFragments;
	}

	/**
	 * @param maxFragments The maximum number of fragments to pick, must be greater than 0.
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setMaxFragments(int maxFragments) {
		if(maxFragments <= 0) {
			throw new IllegalArgumentException("Maximum fragments must be greater than 0.");
		}
		this.maxFragments = maxFragments;
	}

	/**
	 * @return The required similarity for precision analysis.
	 */
	public double getPrecisionRequiredSimilarity() {
		return precisionRequiredSimilarity;
	}

	/**
	 * @param precisionRequiredSimilarity The required similarity for precision analysis.  Must be in range [0, 1].
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setPrecisionRequiredSimilarity(double precisionRequiredSimilarity) {
		if(precisionRequiredSimilarity < 0.0 || precisionRequiredSimilarity > 1.0) {
			throw new IllegalArgumentException("Precision required simialrity must be in range [0, 1].");
		}
		this.precisionRequiredSimilarity = precisionRequiredSimilarity;
	}

	/**
	 * @return The required similarity for recall analysis.
	 */
	public double getRecallRequiredSimilarity() {
		return recallRequiredSimilarity;
	}

	/**
	 * @param recallRequiredSimilarity The required similairty for precision analysis.  Must be in range [0, 1].
	 * @throws IllegalArgumentException if value is invalid.
	 */
	public void setRecallRequiredSimilarity(double recallRequiredSimilarity) {
		if(recallRequiredSimilarity < 0.0 || recallRequiredSimilarity > 1.0) {
			throw new IllegalArgumentException("Recall required similarity must be in range [0, 1].");
		}
		this.recallRequiredSimilarity = recallRequiredSimilarity;
	}
}
