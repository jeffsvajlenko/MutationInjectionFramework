package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;


import models.Fragment;

/**
 * 
 * Collection of utility functions for code fragments.
 *
 */
public class FragmentUtil {
	
	/**
	 * Extracts a fragment.
	 * @param f Fragment to extract.
	 * @param fout Path to file to write fragment to.  File must not already exist.
	 * @throws IOException If end of file is reached before endline during extraction, or if a general IO error occurs.
	 * @throws IllegalArgumentException If the fragment is invalid (file does not exist/unreadable, or if file could not hold the fragment, i.e. endline proceeds EOF).
	 * @throws FileNotFoundException File specified by fragment does not exist.
	 */
	public static void extractFragment(Fragment f, Path fout) throws FileNotFoundException, IOException {
		//Check input
		Objects.requireNonNull(f);
		Objects.requireNonNull(fout);
		if(!Files.exists(f.getSrcFile())) {
			throw new FileNotFoundException("File containing fragment does not exist.");
		}
		if(!Files.isReadable(f.getSrcFile())) {
			throw new IllegalArgumentException("File containing fragment is not readable.");
		}
		if(f.getEndLine() > FileUtil.countLines(f.getSrcFile())) {
			throw new IllegalArgumentException("File containing fragment is shorter than the endline of the fragment.");
		}
		
		int startline = f.getStartLine();
		int endline = f.getEndLine();
			
		//Prep out file
		Files.deleteIfExists(fout);
		Files.createFile(fout);
		
		//Extract fragment
		BufferedReader in = new BufferedReader(new FileReader(f.getSrcFile().toFile()));
		BufferedWriter out = new BufferedWriter(new FileWriter(fout.toFile()));
		String line = in.readLine();
		int linenum = 0;
			
		while(line != null && linenum < endline) {
			linenum++;
			if(linenum >= startline && linenum <= endline) {
				out.write(line + "\n");
			}
			line = in.readLine();
		}
		in.close();
		out.flush();
		out.close();
		
		//Check success
		if(linenum != endline) {
			throw new IOException("EOF was reached before endline.");
		}
	}
	
	/**
	 * Injects a fragment into a file at the specified location.  If an exception is thrown, the original file may not be left in its original state!
	 * @param file The file to inject into.
	 * @param location The sourceline to inject at in file.
	 * @param f The fragment to inject.
	 * @throws NoSuchFileException If file or the file referenced by fragment f does not exist.
	 * @throws IllegalArgumentException If file or the file referenced by fragment f is not readable, if file is a symbolic link, or if location is invalid (with respect to file) or if fragment is invalid (with respect to its source file).
	 */
	public static void injectFragment(Path file, int location, Fragment f) throws NoSuchFileException, IOException {
		//Check pointers
		Objects.requireNonNull(file);
		Objects.requireNonNull(f);
		
		//Check file
		if(!Files.exists(file)) {
			throw new NoSuchFileException("File does not exist.");
		}
		if(!Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
			throw new IllegalArgumentException("File is not a regular file.");
		}
		if(!Files.isReadable(file)) {
			throw new IllegalArgumentException("File is not readable.");
		}
		
		//Check fragment file
		if(!Files.exists(f.getSrcFile())) {
			throw new NoSuchFileException("Fragment source file does not exist.");
		}
		if(!Files.isRegularFile(f.getSrcFile())) {
			throw new IllegalArgumentException("Fragment source file is not a regular file.");
		}
		if(!Files.isReadable(f.getSrcFile())) {
			throw new IllegalArgumentException("Fragment source file is not readable is not readable.");
		}
		
		//Check Location
		int numlinesfile = FileUtil.countLines(file);
		if(location < 1 || location > numlinesfile+1) {
			throw new IllegalArgumentException("Location is invalid.");
		}
		
		//Check Fragment Valid
		int numlinesfragment = FileUtil.countLines(f.getSrcFile());
		if(f.getEndLine() > numlinesfragment) {
			throw new IllegalArgumentException("Fragment is invalid.");
		}
		
		//Extact fragment to inject
		Path fragmentfile = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "FragmentUtil-injectFragment-fragment", null);
		FragmentUtil.extractFragment(f, fragmentfile);
		
		//Inject fragment
		injectFragment(file,location,fragmentfile);
		
		//cleanup
		Files.delete(fragmentfile);
	}
	
	/**
	 * Injects file fragment into file file at the specified location.  If an exception is thrown, the original file may not be left in its original state!
	 * @param file The file to inject into.
	 * @param location The sourceline in file to inject at.
	 * @param fragment File containing code fragment to inject.
	 * @throws IOException If an IOException occurs.
	 * @throws FileNotFoundException If file or fragment does not refer to a file.
	 */
	public static void injectFragment(Path file, int location, Path fragment) throws FileNotFoundException, IOException {
		Objects.requireNonNull(file);
		Objects.requireNonNull(fragment);
		
		//Check file
		if(!Files.exists(file)) {
			throw new NoSuchFileException("File does not exist.");
		}
		if(!Files.isRegularFile(file, LinkOption.NOFOLLOW_LINKS)) {
			throw new IllegalArgumentException("File is not a regular file.");
		}
		if(!Files.isReadable(file)) {
			throw new IllegalArgumentException("File is not readable.");
		}
		
		//Check fragment file
		if(!Files.exists(fragment)) {
			throw new NoSuchFileException("Fragment file does not exist.");
		}
		if(!Files.isRegularFile(fragment)) {
			throw new IllegalArgumentException("Fragment file is not a regular file.");
		}
		if(!Files.isReadable(fragment)) {
			throw new IllegalArgumentException("Fragment file is not readable is not readable.");
		}
		
		//Check Location
		int numlinesfile = FileUtil.countLines(file);
		if(location < 1 || location > numlinesfile+1) {
			throw new IllegalArgumentException("Location is invalid.");
		}
		
		//Create temp file
		Path tmp = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "FragmentUtil-injectFragment-fragment", null);
		
		//Write to the tmp file the original file with the fragment injected
		PrintWriter tmpwriter = new PrintWriter(new FileWriter(tmp.toFile()));
		BufferedReader fileIn = new BufferedReader(new FileReader(file.toFile()));
		BufferedReader fragIn = new BufferedReader(new FileReader(fragment.toFile()));
		try {
			int linenum = 1;
			while(linenum < location) {
				tmpwriter.println(fileIn.readLine());
				linenum++;
			}
			String line;
			while((line = fragIn.readLine())!= null) {
				tmpwriter.println(line);
			}
			while((line = fileIn.readLine()) != null) {
				tmpwriter.println(line);
			}
		} catch (IOException e) {
			tmpwriter.flush(); tmpwriter.close();
			try {fileIn.close();} catch (IOException ee) {};
			try {fragIn.close();} catch (IOException ee) {};
			try {Files.delete(tmp);} catch (IOException ee) {};
			throw e;
		}
		tmpwriter.close();
		fileIn.close();
		fragIn.close();
		
		//Move into original file
		PrintWriter writer = new PrintWriter(new FileWriter(file.toFile()));
		BufferedReader reader = new BufferedReader(new FileReader(tmp.toFile()));
		String line;
		try {
			while((line = reader.readLine())!= null) {
				writer.println(line);
			}
		}
		catch (IOException e) {
			try {reader.close();} catch (IOException ee) {};
			writer.flush(); writer.close();
			try {Files.delete(tmp);} catch (IOException ee) {};
			throw e;
		}
		writer.flush(); writer.close();
		reader.close();
		
		//Cleanup
		Files.deleteIfExists(tmp);
	}
}
