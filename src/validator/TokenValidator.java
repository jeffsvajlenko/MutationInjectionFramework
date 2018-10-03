// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package validator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import experiment.ExperimentSpecification;

import models.Clone;
import models.Fragment;
import util.FileUtil;
import util.FragmentUtil;
import util.StreamGobbler;
import util.SystemUtil;
import util.TXLUtil;

public class TokenValidator {
	private double acceptThreshold;
	private double rejectThreshold;
	private int language;
	
	/**
	 * Creates a validator for validating potential clones.
	 * @param acceptThreshold % similarity above which a clone is considered a true positive.  If < 0 or > 1 it is rounded to 0 or 1 respectively.
	 * @param rejectThreshold % similarity below which a clone is considered a false positive.  If < 0 or > 1 it is rounded to 0 or 1 respectively.
	 * @param language The language of the clones to be validated.
	 * @throws IllegalArgumentException If input values are invalid.
	 */
	public TokenValidator(double acceptThreshold, double rejectThreshold, int language) throws IllegalArgumentException {
		// Check Input
		if(acceptThreshold < 0 || acceptThreshold > 1) { throw new IllegalArgumentException("Accept Threshold must be in range: [0.0,1.0].");}
		if(rejectThreshold < 0 || rejectThreshold > 1) { throw new IllegalArgumentException("Reject Threshold must be in range: [0.0,1.0].");}
		if(rejectThreshold > acceptThreshold) {throw new IllegalArgumentException("Reject threshold must be less than accept threshold.");}
		if(!ExperimentSpecification.isLanguageSupported(language)){throw new IllegalArgumentException("Language is not supported.");}
		
		//Set Values
		this.acceptThreshold = acceptThreshold;	
		this.rejectThreshold = rejectThreshold;
		this.language = language;
	}
	
	/**
	 * Validates the clone.
	 * @param clone The clone to validate.
	 * @return The validation result.
	 * @throws IOException If an IO error occurs while reading the clone's source files.
	 * @throws IllegalArgumentException
	 * @throws NullPointerException If clone is null.
	 * @throws FileNotFoundException If a fragment file can not be found.
	 * @throws InterruptedException 
	 */
	public ValidatorResult validate(Clone clone) throws IOException, FileNotFoundException, IllegalArgumentException, NullPointerException, InterruptedException {
	//Check
		//Check Objects
		Objects.requireNonNull(clone);
		
		//Check Fragment Source Files
		if(!Files.exists(clone.getFragment1().getSrcFile())) {
			throw new FileNotFoundException("Clone fragment 1 source file does not exist.");
		}
		if(!Files.isRegularFile(clone.getFragment1().getSrcFile())) {
			throw new IllegalArgumentException("Clone fragment 1 source file is not a regular file.");
		}
		if(!Files.isReadable(clone.getFragment1().getSrcFile())) {
			throw new IOException("Clone fragment 1 source file is not readable.");
		}
		
		if(!Files.exists(clone.getFragment2().getSrcFile())) {
			throw new FileNotFoundException("Clone fragment 2 source file does not exist.");
		}
		if(!Files.isRegularFile(clone.getFragment2().getSrcFile())) {
			throw new IllegalArgumentException("Clone fragment 2 source file is not a regular file.");
		}
		if(!Files.isReadable(clone.getFragment2().getSrcFile())) {
			throw new IOException("Clone fragment 2 source file is not readable.");
		}
		
		//Check Fragments Valid
		int numlinesf1 = FileUtil.countLines(clone.getFragment1().getSrcFile());
		int numlinesf2 = FileUtil.countLines(clone.getFragment2().getSrcFile());
		if(numlinesf1 < clone.getFragment1().getEndLine()) {
			throw new IllegalArgumentException("Clone fragment 1 endline proceeds end of file.");
		}
		if(numlinesf2 < clone.getFragment2().getEndLine()) {
			throw new IllegalArgumentException("Clone fragment 2 endline proceeds end of file.");
		}
		
	//Validate
		// Data
		Fragment frag1 = clone.getFragment1();
		Fragment frag2 = clone.getFragment2();
		boolean status;
		
		//Create Temporary Files (in such a way as to prevent conflict)
		Path file_1_uncommented=null;
		Path file_2_uncommented=null;
		Path fragment_1=null;
		Path fragment_2=null;
		Path fragment_1_pretty=null;
		Path fragment_2_pretty=null;
		Path fragment_1_pretty_blind=null;
		Path fragment_2_pretty_blind=null;
		try {
			file_1_uncommented = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "file_1_uncommented", null);
			file_2_uncommented = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "file_2_uncommented", null);
			fragment_1 = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "fragment_1", null);
			fragment_2 = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "fragment_2", null);
			fragment_1_pretty = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "fragment_1_pretty", null);
			fragment_2_pretty = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "fragment_2_pretty", null);
			fragment_1_pretty_blind = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "fragment_1_pretty_blind", null);
			fragment_2_pretty_blind = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "fragment_2_pretty_blind", null);
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			//try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		
// Create uncommented versions of the files
		try {
			status = TXLUtil.removeComments(frag1.getSrcFile(), file_1_uncommented, language);
		} catch (InterruptedException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(!status) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to uncomment fragment 1 source file.", 0, 0, 0);
		}
		
		try {
			status = TXLUtil.removeComments(frag2.getSrcFile(), file_2_uncommented, language);
		} catch (InterruptedException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(!status) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to uncomment fragment 2 source file.", 0, 0, 0);
		}
		
		// Another check
		int numlinesucf1;
		try {
			numlinesucf1 = FileUtil.countLines(file_1_uncommented);
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		int numlinesucf2;
		try {
			numlinesucf2 = FileUtil.countLines(file_2_uncommented);
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(numlinesucf1 != numlinesf1) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to uncomment fragment 1 source file.", 0, 0, 0);
		}
		if(numlinesucf2 != numlinesf2) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to uncomment fragment 2 source file.", 0, 0, 0);
		}
		
		
// Extract fragment
		
		try {
			FragmentUtil.extractFragment(new Fragment(file_1_uncommented, frag1.getStartLine(), frag1.getEndLine()), fragment_1);
		} catch (Exception e) { //intercept exceptions to delete temp files
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		try {
			FragmentUtil.extractFragment(new Fragment(file_2_uncommented, frag2.getStartLine(), frag2.getEndLine()), fragment_2);
		} catch (Exception e) { //intercept exceptions to delete temp files
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}

		
// Type 1
		// Type1 Normalise (tokenize)
		try {
			status = TXLUtil.tokenize(fragment_1, fragment_1_pretty, language);
		} catch (InterruptedException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(!status) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to type1 normalize (tokenize) for fragment 1.", 0, 0, 0);
		}
		try {
			status = TXLUtil.tokenize(fragment_2, fragment_2_pretty, language);
		} catch (InterruptedException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(!status) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to type1 normalize (tokenize) for fragment 2.", 0, 0, 0);
		}
		
		// Type1 test
		double type1sim1 = TokenValidator.getSimilarity(fragment_1_pretty, fragment_2_pretty);
		double type1sim2 = TokenValidator.getSimilarity(fragment_2_pretty, fragment_1_pretty);
		//System.out.println(type1sim1 + " " + type1sim2);
		
		if(type1sim1 < -1 || type1sim2 < -1) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to get type1 similarity.", 0, 0, 0);
		}
		if(Math.abs(Math.min(type1sim1, type1sim2)-1) < 0.0001) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE1, ValidatorResult.TRUE_POSITIVE, "", 1.0, 1.0, 1.0);
		}
		
// Type 2
		// Type2 Normalise
		try {
			status = TXLUtil.blindRename(fragment_1_pretty, fragment_1_pretty_blind, language);
		} catch (InterruptedException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(!status) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to type2 normalize for fragment 1.", 0, 0, 0);
		}
		
		try {
			status = TXLUtil.blindRename(fragment_2_pretty, fragment_2_pretty_blind, language);
		} catch (InterruptedException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(!status) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to type2 normalize for fragment 2.", 0, 0.0, 0.0);
		}
		
		//  Type2 test
		double prettyprintBlind_sim1;
		double prettyprintBlind_sim2;
		try {
			prettyprintBlind_sim1 = TokenValidator.getSimilarity(fragment_1_pretty_blind, fragment_2_pretty_blind);
			prettyprintBlind_sim2 = TokenValidator.getSimilarity(fragment_2_pretty_blind, fragment_1_pretty_blind);
		} catch (IOException e) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(prettyprintBlind_sim1 < -1 || prettyprintBlind_sim2 < -1) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to get type2 similarity.", 0, 0, 0);
		}
		if(Math.abs(Math.min(prettyprintBlind_sim1, prettyprintBlind_sim2) - 1) < 0.0001) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE2, ValidatorResult.TRUE_POSITIVE, "", 1.0, 1.0, 1.0);
		}
		
// Type 3
		
		double similarity = Math.min(prettyprintBlind_sim1, prettyprintBlind_sim2);
		if(similarity >= this.acceptThreshold) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE3, ValidatorResult.TRUE_POSITIVE, "", similarity, prettyprintBlind_sim1, prettyprintBlind_sim2);
		}
		
// Reject
		if(similarity <= this.rejectThreshold) {
			try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
			try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
			try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
			try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.FALSE_POSITIVE, "", similarity, prettyprintBlind_sim1, prettyprintBlind_sim2);
		}
		
// Indeterminate
		try {if (file_1_uncommented != null) Files.deleteIfExists(file_1_uncommented);} catch (Exception exception) {};
		try {if (file_2_uncommented != null) Files.deleteIfExists(file_2_uncommented);} catch (Exception exception) {};
		try {if (fragment_1 != null) Files.deleteIfExists(fragment_1);} catch (Exception exception) {};
		try {if (fragment_2 != null) Files.deleteIfExists(fragment_2);} catch (Exception exception) {};
		try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
		try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
		try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
		try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
		return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.UNKNOWN, "",  similarity, prettyprintBlind_sim1, prettyprintBlind_sim2);
	}

	public static double getSimilarity(Path file1, Path file2) throws FileNotFoundException, IOException {
		//Check input
		Objects.requireNonNull(file1);
		Objects.requireNonNull(file2);
		if(!Files.exists(file1)) {
			throw new FileNotFoundException("File1 does not exist.");
		}
		if(!Files.exists(file2)) {
			throw new FileNotFoundException("File2 does not exist.");
		}
		if(!Files.isRegularFile(file1)) {
			throw new IllegalArgumentException("File 1 is not a regular file.");
		}
		if(!Files.isRegularFile(file2)) {
			throw new IllegalArgumentException("File 2 is not a regular file.");
		}
		if(!Files.isReadable(file1)) {
			throw new IllegalArgumentException("File 1 is not readable.");
		}
		if(!Files.isReadable(file2)) {
			throw new IllegalArgumentException("File 2 is not readable.");
		}
		
		// Get line counts
		int fileSize1 = FileUtil.countLines(file1);
		//int fileSize2 = FileUtil.countLines(file2);
		
		//Check similarity
		double leftUnique=0.0;
		//double rightUnique=0.0;
		Process p = Runtime.getRuntime().exec("diff -Bb " + file1.toAbsolutePath().normalize().toString() + " " + file2.toAbsolutePath().normalize().toString());
		int leftDistinct=0; 
		//int rightDistinct=0;
		
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		new StreamGobbler(p.getErrorStream()).start();
		
		String line = null;
		
		while ((line = in.readLine()) != null) {
			if (line.substring(0,1).contains("<")) {
				leftDistinct++;
			}
			if(line.substring(0,1).contains(">")) {
				//rightDistinct++;
			}
		}          
		leftUnique = ((double) ((double)leftDistinct / (double)(fileSize1)));
		//rightUnique = ((double) ((double)rightDistinct / (double)(fileSize2)));

		//Return similarity
		return 1.0d - leftUnique;
	}
	
//	public static void main(String args[]) throws InputMismatchException, IOException {
//		TokenValidator v = new TokenValidator(0.70, 0.30, "c");
//		CloneDetectionReport cdr = new CloneDetectionReport(new File("/home/jeff/Development/workspace/MyMutationInjectionFramework/testdata/ValidatorTest/c/clones.txt"));
//		CloneReportReader crr = new CloneReportReader(cdr);
//		Clone c;
//		ValidatorResult vr;
//		int i = 0;
//		//long l = new Date().getTime();
//		while((c = crr.next()) != null) {
//			i++;
//			if(i < 11780) {
//				continue;
//			}
//			vr = v.validate(c);
//			if(vr.getValidationResult() == ValidatorResult.ERROR) {
//				System.out.println(i + ":" + vr.getMessage());
//				System.out.println("\t" + c.getFragment1().getSrcFile());
//				System.out.println("\t\t" + c.getFragment1().getStartLine() + " " + c.getFragment1().getEndLine());
//				System.out.println("\t" + c.getFragment2().getSrcFile());
//				System.out.println("\t\t" + c.getFragment2().getStartLine() + " " + c.getFragment2().getEndLine());
//				return;
//			}
//			else {
//					System.out.println(i + " " + vr.getSimilarity());
//			}
//		}
//		System.out.println("Done");
//
///*		
//		TokenValidator v = new TokenValidator(0.90, 0.88, "java");
//		ValidatorResult rs = 
//				v.validate(new Clone(new Fragment
//						(new File("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/fragmentsrcs/java/com/sun/org/apache/xalan/internal/xsltc/compiler/util/ErrorMessages_ko.java"),
//								98, 1049),
//				 new Fragment(new File("/home/jeff/Development/workspace/MyMutationInjectionFramework/data/fragmentsrcs/java/com/sun/org/apache/xalan/internal/xsltc/compiler/util/ErrorMessages_ca.java"),
//						 98, 1049)));
//		System.out.println("Result: " + rs.getValidationResult());
//		System.out.println("\tTruePositive: " + ValidatorResult.TRUE_POSITIVE);
//		System.out.println("\tFasePositive: " + ValidatorResult.FALSE_POSITIVE);
//		System.out.println("\tUknown: " + ValidatorResult.UNKNOWN);
//		System.out.println("\tError: " + ValidatorResult.ERROR);
//		System.out.println("CloneType: " + rs.getCloneType());
//		System.out.println("Similarity: " + rs.getSimilarity());
//		System.out.println("F1Sim: " + rs.getFragment1Similarity());
//		System.out.println("F2Sim: " + rs.getFragment2Similarity());
//		if(rs.getValidationResult() == 13) {
//			System.out.println(rs.getMessage());
//		}
//*/
//	}
	
}
