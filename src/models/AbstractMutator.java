package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import experiment.ExperimentSpecification;

import util.FileUtil;
import util.SystemUtil;
import util.TXLUtil;

public class AbstractMutator<T extends Operator> implements Mutator<T> {

	List<T> operators;
	String description;
	int targetCloneType;
	
	public AbstractMutator(String description, List<T> operators) {
		Objects.requireNonNull(operators);
		Objects.requireNonNull(description);
		if(operators.size() < 1) {
			throw new IllegalArgumentException("Mutator created with no operators.");
		}
		
		this.description = description;
		this.targetCloneType = 0;
		this.operators = new ArrayList<T>();
		for(T op : operators) {
			this.operators.add(op);
			if(op.getTargetCloneType() > this.targetCloneType) {
				this.targetCloneType = op.getTargetCloneType();
			}
		}
	}
	
	
	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public int getTargetCloneType() {
		return this.targetCloneType;
	}

	@Override
	public List<T> getOperators() {
		return this.operators;
	}

	@Override
	public int performMutation(Path infile, Path outfile, int numAttemptsMutator, int numAttemptsOperator, double allowedDiff, double containment, int language) throws SQLException, IOException, InterruptedException {
		//Check input
		Objects.requireNonNull(infile);
		Objects.requireNonNull(outfile);
		if(!Files.exists(infile)) {
			throw new IllegalArgumentException("infile does not exist.");
		}
		if(!Files.isRegularFile(infile)) {
			throw new IllegalArgumentException("infile does not denote a regular file.");
		}
		if(!Files.isReadable(infile)) {
			throw new IllegalArgumentException("infile is not readable.");
		}
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language is not supported.");
		}
		
	//Perform operator	
		// Prepare paths
		Path tmp = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "Mutator", null);
		Path tmp2 = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "Mutator", null);
		Path mutator_check_f1p = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "mutator_check_f1p", null);
		Path mutator_check_f1pb = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "mutator_check_f1pb", null);
		Path mutator_check_f1pbn = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "mutator_check_f1pbn", null);
		Path mutator_check_f1pbnt = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "mutator_check_f1pbnt", null);
		Path mutator_check_f2p = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "mutator_check_f2p", null);
		Path mutator_check_f2pb = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "mutator_check_f2pb", null);
		Path mutator_check_f2pbn = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "mutator_check_f2pbn", null);
		Path mutator_check_f2pbnt = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "mutator_check_f2pbnt", null);
		
		int j = numAttemptsMutator;
attemptloop:
	while(j != 0) {
			j--;
			//Prep Files
			Files.deleteIfExists(outfile);
			Files.copy(infile, tmp, StandardCopyOption.REPLACE_EXISTING);
			
			//Perform Mutations
			for(Operator op : this.operators) {
				
				//Perform operator, if fails repeating won't help (failure at this stage means impossible)
				if(op.performOperator(tmp, tmp2, numAttemptsOperator, language) !=0) {
					Files.deleteIfExists(tmp);
					Files.deleteIfExists(tmp2);
					Files.deleteIfExists(mutator_check_f1p);
					Files.deleteIfExists(mutator_check_f1pb);
					Files.deleteIfExists(mutator_check_f1pbn);
					Files.deleteIfExists(mutator_check_f2p);
					Files.deleteIfExists(mutator_check_f2pb);
					Files.deleteIfExists(mutator_check_f2pbn);
					Files.deleteIfExists(mutator_check_f1pbnt);
					Files.deleteIfExists(mutator_check_f2pbnt);
					return -1;
				}
				
				//Copy out to tmp
				Files.copy(tmp2, tmp, StandardCopyOption.REPLACE_EXISTING);
			}
			
			//Check containment condition satisfied ((temporary until operators are made to do this themselves?))
			List<Integer> lines = getLinesMutatedFromOriginal(infile.toAbsolutePath().normalize().toString(), tmp2.toAbsolutePath().normalize().toString());
			if(lines == null) {
				Files.deleteIfExists(tmp);
				Files.deleteIfExists(tmp2);
				Files.deleteIfExists(mutator_check_f1p);
				Files.deleteIfExists(mutator_check_f1pb);
				Files.deleteIfExists(mutator_check_f1pbn);
				Files.deleteIfExists(mutator_check_f2p);
				Files.deleteIfExists(mutator_check_f2pb);
				Files.deleteIfExists(mutator_check_f2pbn);
				Files.deleteIfExists(mutator_check_f1pbnt);
				Files.deleteIfExists(mutator_check_f2pbnt);
				return -1;
			}
			int numlines = FileUtil.countLines(infile);
			int startpoint = (int) (Math.ceil(numlines*containment));
			int endpoint = (int) (Math.floor(numlines*(1-containment)));
			for(int i : lines) {
				if(i <= startpoint || i > endpoint) {
					continue attemptloop; // not properly contained, try again or give up
				}
			}

				//line-method
			boolean status;
				
				//f1
			status = TXLUtil.prettyPrintSourceFragment(infile, mutator_check_f1p, language);
			if(!status) {
				System.err.println("Debug(Mutator): Prettyprint failed?  Continuing with next attempt.");
				continue attemptloop;
			}
			
			status = TXLUtil.blindRename(mutator_check_f1p, mutator_check_f1pb, language);
			if(!status) {
				System.err.println("Debug(Mutator): BlindRename failed?  Continuing with next attempt.");
				continue attemptloop;
			}
			
			try {
				FileUtil.removeEmptyLines(mutator_check_f1pb, mutator_check_f1pbn);
			} catch (Exception e) {
				System.err.println("Debug(Mutator): RemoveEmptyLines failed?  Continuing with next attempt.");
				continue attemptloop;
			}
			
			status = TXLUtil.tokenize(mutator_check_f1pbn, mutator_check_f1pbnt, language);
			if(!status) {
				System.err.println("Debug(Mutator): Tokenization failed? Continuing with next attempt.");
				continue attemptloop;
			}
			
				//f2
			status = TXLUtil.prettyPrintSourceFragment(tmp2, mutator_check_f2p, language);
			if(!status) {
				System.err.println("Debug(Mutator): Prettyprint failed?  Continuing with next attempt.");
				continue attemptloop;
			}
			
			status = TXLUtil.blindRename(mutator_check_f2p, mutator_check_f2pb, language);
			if(!status) {
				System.err.println("Debug(Mutator): BlindRename failed?  Continuing with next attempt.");
				continue attemptloop;
			}
			
			try {
				FileUtil.removeEmptyLines(mutator_check_f2pb, mutator_check_f2pbn);
			} catch (Exception e) {
				System.err.println("Debug(Mutator): RemoveEmptyLines failed?  Continuing with next mutation attempt.");
				continue attemptloop;
			}
			
			status = TXLUtil.tokenize(mutator_check_f2pbn, mutator_check_f2pbnt, language);
			if(!status) {
				System.err.println("Debug(Mutator): Tokenization failed? Continue with next attempt.");
				continue attemptloop;
			}
			
			double tsimilarity = FileUtil.getSimilarity(mutator_check_f1pbnt, mutator_check_f2pbnt);
			double lsimilarity = FileUtil.getSimilarity(mutator_check_f1pbn, mutator_check_f2pbn);
			
				//check
			if(tsimilarity < (1 - allowedDiff) || lsimilarity < (1 - allowedDiff)) {
				//System.out.println("Similarity insufficient: " + tsimilarity + " " + lsimilarity);
				continue attemptloop;
			}
			//Worked, copy to out
			Files.copy(tmp2, outfile, StandardCopyOption.REPLACE_EXISTING);
			Files.deleteIfExists(tmp);
			Files.deleteIfExists(tmp2);
			Files.deleteIfExists(mutator_check_f1p);
			Files.deleteIfExists(mutator_check_f1pb);
			Files.deleteIfExists(mutator_check_f1pbn);
			Files.deleteIfExists(mutator_check_f2p);
			Files.deleteIfExists(mutator_check_f2pb);
			Files.deleteIfExists(mutator_check_f2pbn);
			Files.deleteIfExists(mutator_check_f1pbnt);
			Files.deleteIfExists(mutator_check_f2pbnt);
			return 0;
		}
		Files.deleteIfExists(tmp);
		Files.deleteIfExists(tmp2);
		Files.deleteIfExists(mutator_check_f1p);
		Files.deleteIfExists(mutator_check_f1pb);
		Files.deleteIfExists(mutator_check_f1pbn);
		Files.deleteIfExists(mutator_check_f2p);
		Files.deleteIfExists(mutator_check_f2pb);
		Files.deleteIfExists(mutator_check_f2pbn);
		Files.deleteIfExists(mutator_check_f1pbnt);
		Files.deleteIfExists(mutator_check_f2pbnt);
		return -1;
	}

	/**
	 * Help function.  Takes original and mutated fragment and returns all the lines from the original where something has been changed in the mutated.
	 * Used to determine of containment has been satisfied.
	 */
	public static List<Integer> getLinesMutatedFromOriginal(String original, String mutant) {
		List<Integer> retval = new LinkedList<Integer>();
		
		Runtime runtime = Runtime.getRuntime();
		String stmt = "diff " + original + " " + mutant;
		Process process = null;
		BufferedReader br = null;
		try {
			process = runtime.exec(stmt);
			br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = br.readLine();
			while(line != null) {
				if(!(line.startsWith("<") || line.startsWith(">") || line.startsWith("-"))) {
					//If addition, then the first number in the output is the line in the original fragment that
					//the mutation adds lines after, add 1 to this line and add it to the list
					if(line.contains("a")) {
						Scanner s = new Scanner(line);
						s.useDelimiter("a");
						String data = s.next();
						int i = Integer.parseInt(data);
						retval.add(i+1);
						s.close();
					} else if (line.contains("c")) {
						Scanner s = new Scanner(line);
						s.useDelimiter("c");
						String data = s.next();
						if(data.contains(",")) {
							s.close();
							s = new Scanner(data);
							s.useDelimiter(",");
							int i = s.nextInt();
							int j = s.nextInt();
							for(int k = i; k <= j; k++) {
								retval.add(k);
							}
						} else {
							int i = Integer.parseInt(data);
							retval.add(i);
						}
						s.close();
					} else if (line.contains("d")) {
						Scanner s = new Scanner(line);
						s.useDelimiter("d");
						String data = s.next();
						if(data.contains(",")) {
							s.close();
							s = new Scanner(data);
							s.useDelimiter(",");
							int i = s.nextInt();
							int j = s.nextInt();
							for(int k = i; k <= j; k++) {
								retval.add(k);
							}
						} else {
							int i = Integer.parseInt(data);
							retval.add(i);
						}
						s.close();
					}
				}
				line = br.readLine();
			}
			try {process.getErrorStream().close();} catch (IOException e) {}
			try {process.getInputStream().close();} catch (IOException e) {}
			try {process.getOutputStream().close();} catch (IOException e) {}
			process.destroy();
			process = null;
			try {br.close();} catch (IOException e1) {}
			br = null;
			return retval;
		} catch (IOException e) {
			if(process != null) {
				try {process.getErrorStream().close();} catch (IOException e1) {}
				try {process.getInputStream().close();} catch (IOException e1) {}
				try {process.getOutputStream().close();} catch (IOException e1) {}
				process.destroy();
				process = null;
			}
			if(br != null) {
				try {br.close();} catch (IOException e1) {}
				br = null;
			}
			return null;
		}
	}
	
}
