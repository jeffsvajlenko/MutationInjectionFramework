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
import util.FileUtil;
import util.StreamGobbler;
import util.SystemUtil;
import util.TXLUtil;

public class LineValidator {
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
	public LineValidator(double acceptThreshold, double rejectThreshold, int language) {
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
	 * @throws IllegalArgumentException
	 * @throws IOException If an IO error occurs while reading a file.
	 * @throws InterruptedException
	 * @throws NullPointerException If clone is null.
	 * @throws FileNotFoundException If a fragment file can not be found.
	 */
	public ValidatorResult validate(Clone clone) throws IllegalArgumentException, IOException, InterruptedException, NullPointerException, FileNotFoundException {
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
		boolean status;
		
//Files
		Path fragment_1_pretty=null;
		Path fragment_2_pretty=null;
		Path fragment_1_pretty_blind=null;
		Path fragment_2_pretty_blind=null;
		try {
			fragment_1_pretty = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_fragment_1_pretty", null);
			fragment_2_pretty = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_fragment_2_pretty", null);
			fragment_1_pretty_blind = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_fragment_1_pretty_blind", null);
			fragment_2_pretty_blind = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_fragment_2_pretty_blind", null);
		} catch (IOException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			//try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		
// Type 1
		try {
			if(!TXLUtil.prettyprint(clone.getFragment1().getSrcFile(), fragment_1_pretty, clone.getFragment1().getStartLine(), clone.getFragment1().getEndLine(), language)) {
				try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
				try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
				try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
				try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
				return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to extract type 1 normalized (pretty print) for fragment 1.", 0, 0, 0);
			}
		} catch (IOException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (InterruptedException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		
		try {
			if(!TXLUtil.prettyprint(clone.getFragment2().getSrcFile(), fragment_2_pretty, clone.getFragment2().getStartLine(), clone.getFragment2().getEndLine(), language)) {
				try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
				try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
				try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
				try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
				return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to extract type 1 normalized (pretty print) for fragment 1.", 0, 0, 0);
			}
		} catch (IOException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (InterruptedException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}

		// Type1 test
		double type1sim1;
		double type1sim2;
		try {
			type1sim1 = LineValidator.getSimilarity(fragment_1_pretty, fragment_2_pretty);
			type1sim2 = LineValidator.getSimilarity(fragment_2_pretty, fragment_1_pretty);
		} catch (IOException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(Math.abs(Math.min(type1sim1, type1sim2)-1) < 0.0001) {
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
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (IOException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(!status) {
			Files.deleteIfExists(fragment_1_pretty);
			Files.deleteIfExists(fragment_2_pretty);
			Files.deleteIfExists(fragment_1_pretty_blind);
			Files.deleteIfExists(fragment_2_pretty_blind);
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to type2 normalize for fragment 1.", 0, 0, 0);
		}
		
		try {
			status = TXLUtil.blindRename(fragment_2_pretty, fragment_2_pretty_blind, language);
		} catch (InterruptedException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		} catch (IOException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(!status) {
			Files.deleteIfExists(fragment_1_pretty);
			Files.deleteIfExists(fragment_2_pretty);
			Files.deleteIfExists(fragment_1_pretty_blind);
			Files.deleteIfExists(fragment_2_pretty_blind);
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.ERROR, "Failed to type2 normalize for fragment 2.", 0, 0.0, 0.0);
		}
		
		//  Type2 test
		double prettyprintBlind_sim1;
		double prettyprintBlind_sim2;
		try {
			prettyprintBlind_sim1 = LineValidator.getSimilarity(fragment_1_pretty_blind, fragment_2_pretty_blind);
			prettyprintBlind_sim2 = LineValidator.getSimilarity(fragment_2_pretty_blind, fragment_1_pretty_blind);
		} catch (IOException e) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			throw e;
		}
		if(Math.abs(Math.min(prettyprintBlind_sim1, prettyprintBlind_sim2) - 1) < 0.0001) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE2, ValidatorResult.TRUE_POSITIVE, "", 1.0, 1.0, 1.0);
		}
		
//Type3
		double similarity = Math.min(prettyprintBlind_sim1, prettyprintBlind_sim2);
		if(similarity >= this.acceptThreshold) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE3, ValidatorResult.TRUE_POSITIVE, "", similarity, prettyprintBlind_sim1, prettyprintBlind_sim2);
		}
		
// Reject
		if(similarity <= this.rejectThreshold) {
			try {if (fragment_1_pretty != null) Files.deleteIfExists(fragment_1_pretty);} catch (Exception exception) {};
			try {if (fragment_2_pretty != null) Files.deleteIfExists(fragment_2_pretty);} catch (Exception exception) {};
			try {if (fragment_1_pretty_blind != null) Files.deleteIfExists(fragment_1_pretty_blind);} catch (Exception exception) {};
			try {if (fragment_2_pretty_blind != null) Files.deleteIfExists(fragment_2_pretty_blind);} catch (Exception exception) {};
			return new ValidatorResult(acceptThreshold, rejectThreshold, ValidatorResult.TYPE0, ValidatorResult.FALSE_POSITIVE, "", similarity, prettyprintBlind_sim1, prettyprintBlind_sim2);
		}
		
// Indeterminate
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
}