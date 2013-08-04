package util;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.Fragment;

import experiment.ExperimentSpecification;

public class TXLUtil {
	/**
	 * Tokenizes the input file and saves the token stream into the output file.  Output representation is newline delimited tokens of the specified language.
	 * 
	 * Input file need not be a full language file.  File is analyzed using the language's token grammar.
	 * 
	 * @param infile The input file to process.  Must exist, be a regular file, and be readable.
	 * @param outfile Where to store the token stream.  Must not be an existing directory (existing files are overwritten).  Must be writable.
	 * @param language The language of the file.
	 * @return True if successful, false if unsuccessful (TXL script failed for input).  False usually indicates incorrect language specified or syntax error in file.
	 * @throws IllegalArgumentException If an argument's requirements are violated.
	 * @throws FileNotFoundException If input file is not found.
	 * @throws IOException If an IO error occurs.
	 * @throws InterruptedException If the TXL process executed by this function is externally interrupted.
	 */
	public static boolean tokenize(Path infile, Path outfile, int language) throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
	//Check Paramters
		//Paramters valid (not null, language exists)
		Objects.requireNonNull(infile);
		Objects.requireNonNull(outfile);
		ExperimentSpecification.isLanguageSupported(language);
		
		//Check input fike (exists, is regular, is readable)
		if(!Files.exists(infile)) {
			throw new FileNotFoundException("File does not exist.");
		}
		if(!Files.isRegularFile(infile)) {
			throw new IllegalArgumentException("infile is not regular.");
		}
		if(!Files.isReadable(infile)) {
			throw new IOException("infile is not readable.");
		}
		
		//Check output file (is not a directory, is writable)
		if(Files.exists(outfile) && Files.isDirectory(outfile)) {
			throw new IllegalArgumentException("outfile is a directory and cannot be overriden.");
		}
		Files.deleteIfExists(outfile);
		Files.createFile(outfile);
		if(!Files.isWritable(outfile)) {
			throw new IOException("outfile is not writable.");
		}
		
		//txl
		int retval = TXLUtil.runTxl(SystemUtil.getTxlDirectory(language).resolve("tokenize.txl"), infile, outfile);
		
		if(retval == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Pretty prints the input file and stores the result in the output file.  Input file must be a complete file (syntatically) of the target language.
	 * @param infile The input file to process.  Must exist, be a regular file, and be readable.
	 * @param outfile Where to store the token stream.  Must not be an existing directory (existing files are overwritten).  Must be writable.
	 * @param language The language of the file.
	 * @return True if successful, false if unsuccessful (TXL script failed for input).  False usually indicates incorrect language specified or syntax error in file.  There may be rare cases where TXL can not handle a valid file.
	 * @throws IllegalArgumentException If an argument's requirements are violated.
	 * @throws FileNotFoundException If input file is not found.
	 * @throws IOException If an IO error occurs.
	 * @throws InterruptedException If the TXL process executed by this function is externally interrupted.
	 */
	public static boolean prettyPrintSourceFile(Path infile, Path outfile, int language) throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
	//Check Paramters
		//Paramters valid (not null, language exists)
		Objects.requireNonNull(infile);
		Objects.requireNonNull(outfile);
		ExperimentSpecification.isLanguageSupported(language);
		
		//Check input fike (exists, is regular, is readable)
		if(!Files.exists(infile)) {
			throw new FileNotFoundException("File does not exist.");
		}
		if(!Files.isRegularFile(infile)) {
			throw new IllegalArgumentException("infile is not regular.");
		}
		if(!Files.isReadable(infile)) {
			throw new IOException("infile is not readable.");
		}
		
		//Check output file (is not a directory, is writable)
		if(Files.exists(outfile) && Files.isDirectory(outfile)) {
			throw new IllegalArgumentException("outfile is a directory and cannot be overriden.");
		}
		Files.deleteIfExists(outfile);
		Files.createFile(outfile);
		if(!Files.isWritable(outfile)) {
			throw new IOException("outfile is not writable.");
		}
		
		//txl
		int retval = TXLUtil.runTxl(SystemUtil.getTxlDirectory(language).resolve("PrettyPrint.txl"), infile, outfile);
		
		if(retval == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Pretty prints the input file and stores the result in the output file.  input file must be a complete fragment of the Function or Block granularity.
	 * @param infile The input file to process.  Must exist, be a regular file, and be readable.
	 * @param outfile Where to store the token stream.  Must not be an existing directory (existing files are overwritten).  Must be writable.
	 * @param language The language of the file.
	 * @return True if successful, false if unsuccessful (TXL script failed for input).  False usually indicates incorrect language specified or syntax error in file.  There may be rare cases where TXL can not handle a valid file.
	 * @throws IllegalArgumentException If an argument's requirements are violated.
	 * @throws FileNotFoundException If input file is not found.
	 * @throws IOException If an IO error occurs.
	 * @throws InterruptedException If the TXL process executed by this function is externally interrupted.
	 */
	public static boolean prettyPrintSourceFragment(Path infile, Path outfile, int language) throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
	//Check Paramters
		//Paramters valid (not null, language exists)
		Objects.requireNonNull(infile);
		Objects.requireNonNull(outfile);
		ExperimentSpecification.isLanguageSupported(language);
		
		//Check input file (exists, is regular, is readable)
		if(!Files.exists(infile)) {
			throw new FileNotFoundException("File does not exist.");
		}
		if(!Files.isRegularFile(infile)) {
			throw new IllegalArgumentException("infile is not regular.");
		}
		if(!Files.isReadable(infile)) {
			throw new IOException("infile is not readable.");
		}
		
		//Check output file (is not a directory, is writable)
		if(Files.exists(outfile) && Files.isDirectory(outfile)) {
			throw new IllegalArgumentException("outfile is a directory and cannot be overriden.");
		}
		Files.deleteIfExists(outfile);
		Files.createFile(outfile);
		if(!Files.isWritable(outfile)) {
			throw new IOException("outfile is not writable.");
		}
		
		//txl
		int retval = TXLUtil.runTxl(SystemUtil.getTxlDirectory(language).resolve("PrettyPrintFragment.txl"), infile, outfile);
		
		if(retval == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Blind renames the input file and stores the result in the output file.  Input file must have correct tokens for the language, but need not be a full source file.
	 * @param infile The input file to process.  Must exist, be a regular file, and be readable.
	 * @param outfile Where to store the token stream.  Must not be an existing directory (existing files are overwritten).  Must be writable.
	 * @param language The language of the file.
	 * @return True if successful, false if unsuccessful (TXL script failed for input).  False usually indicates incorrect language specified or syntax error in file.  There may be rare cases where TXL can not handle a valid file.
	 * @throws IllegalArgumentException If an argument's requirements are violated.
	 * @throws FileNotFoundException If input file is not found.
	 * @throws IOException If an IO error occurs.
	 * @throws InterruptedException If the TXL process executed by this function is externally interrupted.
	 */
	public static boolean blindRename(Path infile, Path outfile, int language) throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
	//Check Paramters
		//Paramters valid (not null, language exists)
		Objects.requireNonNull(infile);
		Objects.requireNonNull(outfile);
		ExperimentSpecification.isLanguageSupported(language);
		
		//Check input fike (exists, is regular, is readable)
		if(!Files.exists(infile)) {
			throw new FileNotFoundException("File does not exist.");
		}
		if(!Files.isRegularFile(infile)) {
			throw new IllegalArgumentException("infile is not regular.");
		}
		if(!Files.isReadable(infile)) {
			throw new IOException("infile is not readable.");
		}
		
		//Check output file (is not a directory, is writable)
		if(Files.exists(outfile) && Files.isDirectory(outfile)) {
			throw new IllegalArgumentException("outfile is a directory and cannot be overriden.");
		}
		Files.deleteIfExists(outfile);
		Files.createFile(outfile);
		if(!Files.isWritable(outfile)) {
			throw new IOException("outfile is not writable.");
		}
		
		//txl
		int retval = TXLUtil.runTxl(SystemUtil.getTxlDirectory(language).resolve("BlindRenameFragment.txl"), infile, outfile);
		
		if(retval == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes comments from the input file and stores the result in the output file.  Input file may be a full source file, or a source fragment.
	 * Removes comments without altering line boundaries.
	 * @param infile The input file to process.  Must exist, be a regular file, and be readable.
	 * @param outfile Where to store the token stream.  Must not be an existing directory (existing files are overwritten).  Must be writable.
	 * @param language The language of the file.
	 * @return True if successful, false if unsuccessful (TXL script failed for input).  False usually indicates incorrect language specified or syntax error in file.  There may be rare cases where TXL can not handle a valid file.
	 * @throws IllegalArgumentException If an argument's requirements are violated.
	 * @throws FileNotFoundException If input file is not found.
	 * @throws IOException If an IO error occurs.
	 * @throws InterruptedException If the TXL process executed by this function is externally interrupted.
	 */
	public static boolean removeComments(Path infile, Path outfile, int language) throws IllegalArgumentException, FileNotFoundException, IOException, InterruptedException {
	//Check Paramters
		//Paramters valid (not null, language exists)
		Objects.requireNonNull(infile);
		Objects.requireNonNull(outfile);
		ExperimentSpecification.isLanguageSupported(language);
		
		//Check input fike (exists, is regular, is readable)
		if(!Files.exists(infile)) {
			throw new FileNotFoundException("File does not exist.");
		}
		if(!Files.isRegularFile(infile)) {
			throw new IllegalArgumentException("infile is not regular.");
		}
		if(!Files.isReadable(infile)) {
			throw new IOException("infile is not readable.");
		}
		
		//Check output file (is not a directory, is writable)
		if(Files.exists(outfile) && Files.isDirectory(outfile)) {
			throw new IllegalArgumentException("outfile is a directory and cannot be overriden.");
		}
		Files.deleteIfExists(outfile);
		Files.createFile(outfile);
		if(!Files.isWritable(outfile)) {
			throw new IOException("outfile is not writable.");
		}
		
		//txl
		int retval = TXLUtil.runTxl(SystemUtil.getTxlDirectory(language).resolve("removecomments.txl"), infile, outfile);
		
		if(retval == 0) {
			return true;
		} else {
			return false;
		}
	}

	
	
	/**
	 * Runs the given TXL script for the given input file and outputs the result to the output file.
	 * The function returns once the script is complete.
	 * @param txlScript The TXL script to run.
	 * @param inputFile The input file.
	 * @param outputFile The output file.
	 * @return The return value of the TXL execution (0 is success, > 0 is failure).  Or -1 if TXL execution failed.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	public static int runTxl(Path txlScript, Path inputFile, Path outputFile) throws InterruptedException, IOException {
		//Check input
		Objects.requireNonNull(txlScript);
		Objects.requireNonNull(inputFile);
		Objects.requireNonNull(outputFile);
		if(!Files.exists(txlScript)) {
			throw new FileNotFoundException("Specified txl script does not exist.");
		}
		if(!Files.isReadable(txlScript)) {
			throw new IOException("Specified txl script is not readable.");
		}
		if(!Files.isRegularFile(txlScript)) {
			throw new IllegalArgumentException("txlScript must be a regular file.");
		}
		if(!Files.exists(inputFile)) {
			throw new FileNotFoundException("Input file does not exist.");
		}
		if(!Files.isReadable(inputFile)) {
			throw new IOException("Input file is not readable or does not exist.");
		}
		if(!Files.exists(outputFile)) {
			try {
				Files.createFile(outputFile);
			} catch (IOException e) {
				throw new IllegalArgumentException("Output file can not be written to.");
			}
		}
		if(!Files.isWritable(outputFile)) {
			throw new IOException("Output file can not be written to.");
		}
		
		//Execute TXL
		String[] command = new String[2];
		command[0] = SystemUtil.getScriptsLocation() + "/unlimitedstack";
		command[1] = SystemUtil.getTxlExecutable() + " -s 400 -o " + outputFile.toAbsolutePath().normalize().toString() + " " + inputFile.toAbsolutePath().normalize().toString() + " " + txlScript.toAbsolutePath().normalize().toString();
		Process process = null;
		int retval;
		try {
			process = Runtime.getRuntime().exec(command);
			new StreamGobbler(process.getInputStream()).start();
			new StreamGobbler(process.getErrorStream()).start();
			retval = process.waitFor();
		} catch (IOException e) {
			//if(process != null) {
			//	try {process.getErrorStream().close();} catch (IOException e1) {}
			//	try {process.getInputStream().close();} catch (IOException e1) {}
			//	try {process.getOutputStream().close();} catch (IOException e1) {}
			//	process.destroy();
			//	process = null;
			//}
			throw e;
		} catch (InterruptedException e) {
			if(process != null) {
				try {process.getErrorStream().close();} catch (IOException e1) {}
				try {process.getInputStream().close();} catch (IOException e1) {}
				try {process.getOutputStream().close();} catch (IOException e1) {}
				process.destroy();
				process = null;
			}
			throw e;
		} finally {
			if(process != null) {
				try {process.getErrorStream().close();} catch (IOException e) {}
				try {process.getInputStream().close();} catch (IOException e) {}
				try {process.getOutputStream().close();} catch (IOException e) {}
				process.destroy();
				process = null;
			}
		}
		
		return retval;
	}

	/**
	 * Returns if the specified fragment is of the specified fragment type.
	 * @param fragment The file containing the fragment.
	 * @param fragmentType
	 * @param fragmentType The fragment type.
	 * @return if the specified fragment is of the specified fragment type;
	 * @throws FileNotFoundException If the fragment file does not exist.
	 * @throws IOException If an error occurs reading the file containing the fragment.
	 * @throws InterruptedException If dependent TXL process is interrupted.
	 * @throws IllegalArgumentException
	 */
	public static boolean isFragmentType(Path fragment, int fragmentType) throws FileNotFoundException, IOException, InterruptedException, IllegalArgumentException {
		Objects.requireNonNull(fragment);
		if(ExperimentSpecification.isFragmentTypeValid(fragmentType)) {
			throw new IllegalArgumentException("Fragment type is not valid.");
		}
		if(fragmentType == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
			return TXLUtil.isBlock(fragment, fragmentType);
		} else if (fragmentType == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
			return TXLUtil.isFunction(fragment, fragmentType);
		} else {
			throw new IllegalArgumentException("Fragment type is not valid.");
		}
	}
	
	/**
	 * Returns if the specified fragment is of the specified fragment type.
	 * @param fragment The fragment to check fragment type of.
	 * @param fragmentType The fragment type.
	 * @return if the specified fragment is of the specified fragment type;
	 * @throws FileNotFoundException If the fragment file does not exist.
	 * @throws IOException If an error occurs reading the file containing the fragment.
	 * @throws InterruptedException If dependent TXL process is interrupted.
	 * @throws IllegalArgumentException
	 */
	public static boolean isFragmentType(Fragment fragment, int fragmentType) throws FileNotFoundException, IOException, InterruptedException, IllegalArgumentException {
		Objects.requireNonNull(fragment);
		if(ExperimentSpecification.isFragmentTypeValid(fragmentType)) {
			throw new IllegalArgumentException("Fragment type is not valid.");
		}
		if(fragmentType == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
			return TXLUtil.isBlock(fragment, fragmentType);
		} else if (fragmentType == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
			return TXLUtil.isFunction(fragment, fragmentType);
		} else {
			throw new IllegalArgumentException("Fragment type is not valid.");
		}
	}
	
	/**
	 * Returns if the fragment represents a function.  Will return true if the code slice represented by the start/end lines of the fragment
	 * is a function of the specified language.  Will return false if it is not a function.  Note that if the fragment includes a function,
	 * but also includes other syntax external to the function (not including comments) it will return false.
	 * @param fragment The fragment to check.  Fragment must be valid (source file exists, is a regular file, and is readable; start/end-lines must be valid with respect to source file.)
	 * @param language The language of the fragment (must be of a language supported by this class).
	 * @return True if the fragment captures a function (in its entirity and without other external features except comments), false if it does not.
	 * @throws FileNotFoundException If the source file can not be found.
	 * @throws IOException If an IO error occurs.
	 * @throws InterruptedException 
	 * @throws IllegalArgumentException If one of the argument preconditions is broken: fragment is invalid (source file does not exist, is not a regular file, or is not readable, or start/endlines invalid) or if language is not supported.
	 * @throws NullPointerException if fragment of language are specified as null.
	 */
	public static boolean isFunction(Fragment fragment, int language) throws FileNotFoundException, IOException, InterruptedException {
		//Check Input
		Objects.requireNonNull(fragment);
		Objects.requireNonNull(language);
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language not supported.");
		}
		if(!Files.exists(fragment.getSrcFile())) {
			throw new FileNotFoundException("Fragment source file does not exist.");
		}
		if(!Files.isReadable(fragment.getSrcFile())) {
			throw new IllegalArgumentException("File specified by fragment is not readable.");
		}
		if(!Files.isRegularFile(fragment.getSrcFile())) {
			throw new IllegalArgumentException("File specified by fragment is not a regular file.");
		}
		int numlines = FileUtil.countLines(fragment.getSrcFile());
		if(fragment.getEndLine() > numlines) {
			throw new IllegalArgumentException("Fragment is invalid, endline proceeds end of file.");
		}
		
		//Prep fragment
		Path tmpfile = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "FragmentUtil-isFunction", null);
		FragmentUtil.extractFragment(fragment, tmpfile);
		
		//check is function
		boolean retval;
		try {
			retval = TXLUtil.isFunction(tmpfile, language);
		} catch (InterruptedException e) {
			Files.deleteIfExists(tmpfile);
			throw e;
		}
		
		//Cleanup
		Files.delete(tmpfile);
		
		//return answer
		return retval;
	}

	/**
	 * Returns if the fragment represents a block.  Will return true if the code slice represented by the start/end lines of the fragment
	 * is a block of the specified language.  Will return false if it is not a block.  Note that if the fragment includes a block,
	 * but also includes other syntax external to the block (not including comments) it will return false.
	 * 
	 * Reliability: May return false for a block input if grammar is unable to parse the function due to defect in grammar.
	 * 
	 * @param fragment The fragment to check.  Fragment must be valid (source file exists, is a regular file, and is readable; start/end-lines must be valid with respect to source file.)
	 * @param language The language of the fragment (must be of a language supported by this class).
	 * @return True if the fragment captures a block (in its entirety and without other external features except comments), false if it does not.
	 * @throws FileNotFoundException If the source file can not be found.
	 * @throws IOException If an IO error occurs.
	 * @throws InterruptedException 
	 * @throws IllegalArgumentException If one of the argument preconditions is broken: fragment is invalid (source file does not exist, is not a regular file, or is not readable, or start/endlines invalid) or if language is not supported.
	 * @throws NullPointerException if fragment of language are specified as null.
	 */
	public static boolean isBlock(Fragment fragment, int language) throws FileNotFoundException, IOException, InterruptedException {
		//Check Input
		Objects.requireNonNull(fragment);
		Objects.requireNonNull(language);
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language not supported.");
		}
		if(!Files.exists(fragment.getSrcFile())) {
			throw new FileNotFoundException("Fragment source file does not exist.");
		}
		if(!Files.isReadable(fragment.getSrcFile())) {
			throw new IOException("File specified by fragment is not readable.");
		}
		if(!Files.isRegularFile(fragment.getSrcFile())) {
			throw new IllegalArgumentException("File specified by fragment is not a regular file.");
		}
		int numlines = FileUtil.countLines(fragment.getSrcFile());
		if(fragment.getEndLine() > numlines) {
			throw new IllegalArgumentException("Fragment is invalid, endline proceeds end of file.");
		}
		
		//Prep fragment
		Path tmpfile = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "FragmentUtil-isBlock", null);
		FragmentUtil.extractFragment(fragment, tmpfile);
		
		//check is function
		boolean retval;
		try {
			retval = TXLUtil.isBlock(tmpfile, language);
		} catch (InterruptedException e) {
			Files.deleteIfExists(tmpfile);
			throw e;
		}
		
		//Cleanup
		Files.delete(tmpfile);
		
		//return answer
		return retval;
	}

	/**
	 * Determines if a file contains a single block of the specified language.  True means 
	 * the file contains a block, false means it does not or that parsing failed (Grammar
	 * may not be perfect, do not rely on false meaning not a block!  Grammar should be good
	 * enough to be reliable for true cases).
	 * @param block Path to a file containing the syntax to test if a block.
	 * @param language The language of the syntax.
	 * @return true if the file specified by the path block is a block, or false if it is not.
	 * @throws IOException If an IOException occurs during analysis.
	 * @throws FileNotFoundException If function does not point to an existing file.
	 * @throws InterruptedException 
	 */
	public static boolean isBlock(Path block, int language) throws FileNotFoundException, IOException, InterruptedException {
		//Check Input
		Objects.requireNonNull(block);
		Objects.requireNonNull(language);
		if(!Files.exists(block)) {
			throw new FileNotFoundException("Function does not exist.");
		}
		if(!Files.isRegularFile(block)) {
			throw new IllegalArgumentException("block is not a regular file.");
		}
		if(!Files.isReadable(block)) {
			throw new IOException("Function is not readable.");
		}
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language not supported.");
		}
		
		//Prep
		Path txlscript = SystemUtil.getTxlDirectory(language).resolve("isblock.txl");
		Path tmpout = null;
		tmpout = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "FragmentUtil-isBlock", null);
		
		//Run isFunction txl program
		int retval;
		try {
			retval = runTxl(txlscript.toAbsolutePath().normalize(), block.toAbsolutePath().normalize(), tmpout.toAbsolutePath().normalize());
		} catch (InterruptedException e) {
			Files.deleteIfExists(tmpout);
			throw e;
		}
		
		//Cleanup
		Files.delete(tmpout);
		
		//Check
		if(retval == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Determines if a file contains a single function of the specified language.  True means 
	 * the file contains a function, false means it does not or that parsing failed (Grammar
	 * may not be perfect, do not rely on false meaning not a function!  Grammar should be good
	 * enough to be reliable for true cases).
	 * @param function Path to a file containing the syntax to test if a function.
	 * @param language The language of the syntax.
	 * @return true if the file specified by the path function is a function, or false if it is not.
	 * @throws IOException If an IOException occurs during analysis.
	 * @throws FileNotFoundException If function does not point to an existing file.
	 * @throws InterruptedException 
	 */
	public static boolean isFunction(Path function, int language) throws FileNotFoundException, IOException, InterruptedException {
		//Check Input
		Objects.requireNonNull(function);
		Objects.requireNonNull(language);
		if(!Files.exists(function)) {
			throw new FileNotFoundException("Function does not exist.");
		}
		if(!Files.isReadable(function)) {
			throw new IllegalArgumentException("Function is not readable.");
		}
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language not supported.");
		}
		
		//Prep
		Path txlscript = SystemUtil.getTxlDirectory(language).resolve("isfunction.txl");
		Path tmpout = null;
		tmpout = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "FragmentUtil-isFunction", null);
		
		//Run isFunction txl program
		int retval;
		try {
			retval = runTxl(txlscript.toAbsolutePath().normalize(), function.toAbsolutePath().normalize(), tmpout.toAbsolutePath().normalize());
		} catch (InterruptedException e) {
			e.printStackTrace();
			Files.deleteIfExists(tmpout);
			throw e;
		}
		
		//Cleanup
		Files.deleteIfExists(tmpout);
		
		//Check
		if(retval == 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Pretty prints the given source fragment and saves it in the output file.  Use other pretty print functions if desired pretty printing job
	 * is for a whole source file or for a function/block fragment.  This method works for any fragmetn but is much slower.
	 * @param infile Input source file.
	 * @param outfile File to write the pretty printed fragment to.
	 * @param startline Start line of fragment in source file.
	 * @param endline End line of fragment in source file.
	 * @param language The language of the file.
	 * @return True if successful, otherwise false.
	 * @throws FileNotFoundException Input file not found.
	 * @throws IOException IO error.
	 * @throws InterruptedException A required TXL process was externally interrupted.
	 */
	public static boolean prettyprint(Path infile, Path outfile, int startline, int endline, int language) throws FileNotFoundException, IOException, InterruptedException {
		//Check Arguments
			//Objects
			Objects.requireNonNull(infile);
			Objects.requireNonNull(outfile);
		
			//Language
			ExperimentSpecification.isLanguageSupported(language);
			
			//Input File
			if(!Files.exists(infile)) { //should exist
				throw new FileNotFoundException("Input file does not exist.");
			}
			if(!Files.isRegularFile(infile)) {
				throw new IllegalArgumentException("infile is not a regular file.");
			}
			if(!Files.isReadable(infile)) { //should be readable
				throw new IOException("Input file can not be read.");
			}
			
			//Output File
			Files.deleteIfExists(outfile);
			Files.createFile(outfile);
			if(!Files.exists(outfile)) { //create output file
				throw new IOException("Output file could not be created.");
			}
			if(!Files.isWritable(outfile)) { //should be able to write
				throw new IOException("Output file is not writable.");
			}
			
			//Startline/Endline are appropraite
			if(endline < startline) {
				throw new IllegalArgumentException("Endline preceeds startline.");
			}
			int numlines = FileUtil.countLines(infile);
			if(endline > numlines) {
				throw new IllegalArgumentException("Endline exceeds length of file.");
			}
			
			Path prettyfile=null;
			Path prettyfile_spaced=null;
			Path uncommented=null;
			Path fragment=null;
			Path fragment_tokstream=null;
			Path fragment_beforeRemoveEmptyLines = null;
			try {
				prettyfile = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_prettyfile", null);
				prettyfile_spaced = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_prettyfile_spaced", null);
				uncommented = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_uncommented", null);
				fragment = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_fragment", null);
				fragment_tokstream = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_fragment_tokstream", null);
				fragment_beforeRemoveEmptyLines = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "LineValidator_fragment_beforeRemoveEmptyLine", null);
			} catch (IOException e) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				//try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e;
			}
			
	// Perform pretty-printing
		//PrettyPrint file containing the fragment
			try {
				if(0 != runTxl(SystemUtil.getTxlDirectory(language).resolve("PrettyPrint.txl"), infile, prettyfile)) {
					try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
					try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
					try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
					try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
					try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
					try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
					//System.out.println("here1");
					return false;
				}
			} catch (InterruptedException e1) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e1;
			}
			
		//Add spaces between each token of the pretty-printed file
			try {
				if(0 != runTxl(SystemUtil.getTxlDirectory(language).resolve("spacer.txl"), prettyfile, prettyfile_spaced)) {
					try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
					try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
					try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
					try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
					try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
					try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
					return false;
				}
			} catch (InterruptedException e1) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e1;
			}
			
		//Load space adjusted pretty-printed file into a string, fix whitespace so all tokens are 1 space apart, no tabs, newlines persist
			String file = new String();
			try {
				Scanner scanner = new Scanner(prettyfile_spaced);
				while(scanner.hasNextLine()) {
					String line = scanner.nextLine();
						file = file + line + "\n";
				}
				scanner.close();
			} catch (IOException e) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e;
			}
			
	//Create fragment search pattern
			//Remove comments from original file
			try {
				if(0 != runTxl(SystemUtil.getTxlDirectory(language).resolve("removecomments.txl"), infile, uncommented)) {
					try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
					try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
					try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
					try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
					try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
					try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
					return false;
				}
			} catch (InterruptedException e1) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e1;
			}
			
			//check same length (not perfect check, but catch some error cases)
			int numlinesu;
			try {
				numlinesu = FileUtil.countLines(uncommented);
			} catch (IOException e) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e;
			}
			if(numlinesu != numlines) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				return false;
			}
			
			//Extract fragment form comment removed file
			try {
				FragmentUtil.extractFragment(new Fragment(uncommented, startline, endline), fragment);
			} catch (IOException e) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e;
			}
			
			//Transform fragment into a space delimited token stream (embeded in first line of a file)
			
			try {
				if(0!=runTxl(SystemUtil.getTxlDirectory(language).resolve("tokenize.txl"), fragment, fragment_tokstream)) {
					try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
					try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
					try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
					try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
					try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
					try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
					return false;
				}
			} catch (InterruptedException e1) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e1;
			}
			
			//Extract token stream from file and turn it into a regex search pattern
			String fragmentpattern = "";
			try {
				Scanner scanner = new Scanner(fragment_tokstream);
				while(scanner.hasNextLine()) {
					String tok = scanner.nextLine();
					//System.out.println(tok);
					tok = Pattern.quote(tok); //escape for regex
					if(scanner.hasNext()) {
						fragmentpattern = fragmentpattern + tok + "\\s+";
					} else {
						fragmentpattern = fragmentpattern + tok;
					}
				}
				scanner.close();
			} catch (IOException e) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e;
			}
			
			//System.out.println(fragmentpattern);
			
		//Search space adjusted pretty-printed version for the fragment
			Pattern p = Pattern.compile(fragmentpattern);
			Matcher m = p.matcher(file);
			if(!m.find()) {
				//System.out.println(fragmentpattern);
				//System.out.println(file);
				//System.out.println("here6");
				return false;
			}
			String pfragment = m.group();
			
		//Write the fragment to output file
			PrintWriter pw = new PrintWriter(new FileWriter(fragment_beforeRemoveEmptyLines.toFile()));
			pw.print(pfragment);
			pw.print("\n");
			pw.flush();
			pw.close();
		
		//Remove empty lines from fragment
			try {
				FileUtil.removeEmptyLines(fragment_beforeRemoveEmptyLines, outfile);
			} catch(IOException e) {
				try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
				try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
				try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
				try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
				try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
				try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
				throw e;
			}
			
		//Cleanup
			try {if (prettyfile != null) Files.deleteIfExists(prettyfile);} catch (Exception exception) {};
			try {if (prettyfile_spaced != null) Files.deleteIfExists(prettyfile_spaced);} catch (Exception exception) {};
			try {if (uncommented != null) Files.deleteIfExists(uncommented);} catch (Exception exception) {};
			try {if (fragment != null) Files.deleteIfExists(fragment);} catch (Exception exception) {};
			try {if (fragment_tokstream != null) Files.deleteIfExists(fragment_tokstream);} catch (Exception exception) {};
			try {if (fragment_beforeRemoveEmptyLines != null) Files.deleteIfExists(fragment_beforeRemoveEmptyLines);} catch (Exception exception) {};
			
			return true;
		}
}
