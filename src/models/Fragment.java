package models;

import java.nio.file.Path;
import java.util.Objects;

/**
 * 
 * A code fragment, specified by source file and line numbers.
 *
 */
public class Fragment {
	/** The file containing the fragment */
	private Path srcFile;
	
	/** The start line of the fragment (inclusive) */
	private int startLine;
	
	/** The end line of the fragment (inclusive) */
	private int endLine;
	
	/**
	 * Creates a code fragment.
	 * @param srcFile The file containing the fragment.  Relative paths are converted to absolute.
	 * @param startLine The start line of the fragment.
	 * @param endLine The end line of the fragment (inclusive).
	 * @throws IllegalArgumentException If startline > endline.
	 */
	public Fragment(Path srcFile, int startLine, int endLine) {
	//Validate Input	
		Objects.requireNonNull(srcFile);
		if(endLine < startLine) {
			throw new IllegalArgumentException("Fragment created with invalid start/end line.");
		}

	//Initialize Object
		this.srcFile = srcFile.toAbsolutePath().normalize();
		this.startLine = startLine;
		this.endLine = endLine;
	}
	
	/**
	 * Returns the file containing the fragment.  May not be absolute.
	 * @return The file containing the fragment.
	 */
	public Path getSrcFile() {
		return srcFile;
	}
	
	/**
	 * Returns the start line of the fragment.
	 * @return The fragment's start line.
	 */
	public int getStartLine() {
		return startLine;
	}
	
	/**
	 * Returns the end line of the fragment.
	 * @return The fragment's end line.
	 */
	public int getEndLine() {
		return endLine;
	}
	
	/**
	 * Returns the length of the fragment in lines.
	 * @return the length of the fragment in lines.
	 */
	public int getNumberOfLines() {
		return this.endLine - this.startLine + 1;
	}
	
	/**
	 * Returns true if this fragment subsumes 'another'.
	 * This fragment subsumes 'another' if it is in the same file, if its start line is less than or equal to another's start line, and if
	 * its end line is greater than or equal to another's end line.
	 * By specifying a non-zero relaxer, the subsume can be relaxer's lines off from the above definition.
	 * @param another The fragment to test if subsumed by this fragment.
	 * @param relaxer The number of lines of 'another' that this fragmnet is allowed to miss while still being considered to have subsumed it.
	 * @return True if this fragment subsumes 'another'.
	 */
	private boolean subsumesHelper(Fragment another, int relaxer){
		if(this.getSrcFile().toAbsolutePath().normalize().equals(another.getSrcFile().toAbsolutePath().normalize()) 
				&& this.startLine - relaxer <= another.getStartLine() 
				&& this.getEndLine() + relaxer >= another.getEndLine()){
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if this fragment subsumes 'another'.
	 * 
	 * Subsumes defined as true if source files are the same, and if its line and:
	 * 		startLine-relaxer <= another.startLine and
	 * 		endline+relaxer >= another.getEndLine.
	 * 
	 * A relaxer of 0 is the an exact subsume.
	 * 
	 * @param another
	 * @param tolerance
	 * @return
	 */
	public boolean subsumes(Fragment another, double tolerance) {
		Objects.requireNonNull(another);
		if(tolerance < 0.0 || tolerance > 1.0) {
			throw new IllegalArgumentException("Tolernace must be a percetange ( 0.0 <= x <= 1.0 )");
		}
		
		int size = another.endLine - another.startLine + 1;
		int relaxer = (int)(size * tolerance);
		
		return subsumesHelper(another, relaxer);
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		Fragment f = (Fragment) obj;
		if(this.srcFile.toAbsolutePath().normalize().equals(f.srcFile.toAbsolutePath().normalize()) && this.endLine == f.endLine && this.startLine == f.startLine) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "" + this.srcFile + " " + this.startLine + "-" + this.endLine;
	}
}