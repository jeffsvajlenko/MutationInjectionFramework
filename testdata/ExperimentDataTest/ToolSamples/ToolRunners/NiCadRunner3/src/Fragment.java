

import java.io.File;

/**
 *   
 *  Represents a code fragment.
 * 
 * Can represent an independent code fragment or one in the database (based on value of the identifier).
 *
 */
public class Fragment {
	private String srcFile; /** The file containing the fragment */
	private int startLine; /** The start line of the fragment (inclusive) */
	private int endLine; /** The end line of the fragment (inclusive) */
	private int id; /** The id of the fragment.  -1 if not in database. */
	
	/**
	 * Creates a code fragment that is tied to the database by the id.
	 * @param srcFile The file containing the fragment.
	 * @param startLine The start line of the fragment.
	 * @param endLine The end line of the fragment (inclusive).
	 */
	public Fragment(int id, String srcFile, int startLine, int endLine) {
		this.id = id;
		this.srcFile = new File(srcFile).getAbsolutePath();
		this.startLine = startLine;
		this.endLine = endLine;
	}
	
	/**
	 * Creates a code fragment that is not tied to the database.
	 * @param srcFile The file containing the fragment.
	 * @param startLine The start line of the fragment.
	 * @param endLine The end line of the fragment (inclusive).
	 */
	public Fragment(String srcFile, int startLine, int endLine) {
		id = -1;
		this.srcFile = new File(srcFile).getAbsolutePath();
		this.startLine = startLine;
		this.endLine = endLine;
	}
	
	/**
	 * Returns the fragment's identifier.
	 * @return the fragment's identifier.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Returns the file containing the fragment.
	 * @return The file containing the fragment.
	 */
	public String getSrcFile() {
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
	 * Returns true if this fragment subsumes 'another'.
	 * This fragment subsumes 'another' if it is in the same file, if its start line is less than or equal to another's start line, and if
	 * its end line is greater than or equal to another's end line.
	 * By specifying a non-zero relaxer, the subsume can be relaxer's lines off from the above definition.
	 * @param another The fragment to test if subsumed by this fragment.
	 * @param relaxer The number of lines off from the subsume definition the fragments can be while still conisdered to be subsuming.
	 * @return True if this fragment subsumes 'another'.
	 */
	public boolean subsumes(Fragment another, int relaxer){
		if(this.getSrcFile().equals(another.getSrcFile()) && this.startLine - relaxer <= another.getStartLine() && this.getEndLine() + relaxer >= another.getEndLine()){
			return true;
		}
		return false;
	}
	
	public boolean equals(Object obj) {
		Fragment f = (Fragment) obj;
		if(this.srcFile.equals(f.srcFile) && this.endLine == f.endLine && this.startLine == f.startLine && this.id == f.id) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "id: " + id + " srcfile: " + srcFile + " startline: " + startLine + " endline: " + endLine;
	}
	
	//Test
	public static void main(String args[]) {
		Fragment f = new Fragment(5, "/test", 2, 100);
		assert(f.getId() == 5);
		assert(f.getSrcFile() == "/test");
		assert(f.getStartLine() == 2);
		assert(f.getEndLine() == 100);
		System.out.println("Fragment test complete.");
	}
}
