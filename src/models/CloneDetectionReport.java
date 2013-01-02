package models;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;

/**-
 * Represents a clone detection report for a clone base.
 */
public class CloneDetectionReport {
	
	Path report;
	private BufferedReader br;
	private boolean closed = true;
	
	/**
	 * Constructs a clone report object.
	 * @param toolid id (from the database) of the tool which created this report (>=0).
	 * @param baseid id (from the database) of the base which was analyzed (>=0).
	 * @param report location of the clone detection report.
	 */
	public CloneDetectionReport(Path report) {
		Objects.requireNonNull(report);
		this.report = report;
		this.closed = true;
	}
	
	/**
	 * Returns the location of the report.
	 * @return the location of the report.
	 */
	public Path getReport() {
		return this.report;
	}
	
	/**
	 * Opens the clone detection report for reading. Has no effect if already opened.
	 * @throws FileNotFoundException If the report file cold not be found.
	 */
	public void open() throws FileNotFoundException {
		if(this.closed == true) {
			this.br = new BufferedReader(new FileReader(report.toFile()));
			this.closed = false;
		}
	}
	
	/**
	 * Closes the clone detection report.  Has no effect is already closed.
	 * @throws IOException
	 */
	public void close() throws IOException {
		if(this.closed == false) {
			this.br.close();
			this.closed = true;
		}
	}
	
	/**
	 * Returns the next clone in the clone report.
	 * @return Then next clone, or null if the end of the report was reached.
	 * @throws IOException If an IO exception occurs while reading the file.
	 * @throws InputMismatchException Thrown if the next line is not valid.  Reading can still continue with next() which will proceed at the next entry.
	 */
	public Clone next() throws IOException, InputMismatchException {
		if(this.closed == false) {
			// Get next line from the report
			String line = br.readLine();
			Scanner s = null;
			// If no more lines, return null
			if(line == null) {
				return null;
			// Otherwise, parse the clone and return it
			} else {
				try {
					// Parse the data out of the line
					s = new Scanner(line);
					s.useDelimiter(",");
					String srcfile1 = s.next();
					int startline1 = s.nextInt();
					int endline1 = s.nextInt();	
					String srcfile2 = s.next();
					int startline2 = s.nextInt();
					int endline2 = s.nextInt();
					s.close();
					
					// Construct the clone and return it
					Fragment f1 = new Fragment(Paths.get(srcfile1), startline1, endline1);
					Fragment f2 = new Fragment(Paths.get(srcfile2), startline2, endline2);
					Clone c = new Clone(f1,f2);
					return c;	
				} catch (InputMismatchException e) { // If input mismatch, then line is malformed
					if(s!=null) {
						try {s.close();} catch(Exception ee) {}
					}
					throw new InputMismatchException();
				} catch (NoSuchElementException e) { // If no such element, then line is malformed
					if(s!=null) {
						try {s.close();} catch(Exception ee) {}
					}
					throw new InputMismatchException("Error in line: " + line);
				}
			}
		} else {
			throw new IOException("Can not use next() on a closed CloneDetectionReport.");
		}
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		CloneDetectionReport cdr = (CloneDetectionReport) obj;
		if(this.report.equals(cdr.report)) {
			return true;
		} else {
			return false;
		}
	}
}
