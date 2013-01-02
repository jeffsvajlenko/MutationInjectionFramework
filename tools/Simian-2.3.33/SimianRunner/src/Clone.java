/**
 * 
 * Represents a clone pair.
 *
 */
public class Clone {

	Fragment fragment1; /** Fragment 1 */
	Fragment fragment2; /** Fragment 2 */
	
	/**
	 * Creates a new clone.
	 * @param fragment1 A fragment.
	 * @param fragment2 Another fragment.
	 */
	public Clone(Fragment fragment1, Fragment fragment2) {
		this.fragment1 = fragment1;
		this.fragment2 = fragment2;
	}
	
	/**
	 * Creates a new clone.
	 * @param srcfile1
	 * @param startline1
	 * @param endline1
	 * @param srcfile2
	 * @param startline2
	 * @param endline2
	 */
	public Clone(String srcfile1, int startline1, int endline1, String srcfile2, int startline2, int endline2) {
		this.fragment1 = new Fragment(srcfile1, startline1, endline1);
		this.fragment2 = new Fragment(srcfile2, startline2, endline2);
	}
	
	/**
	 * Returns the first code fragment.
	 * @return the first code fragment.
	 */
	public Fragment getFragment1() {
		return this.fragment1;
	}
	
	/**
	 * Returns the second code fragment.
	 * @return the second code fragment.
	 */
	public Fragment getFragment2() {
		return this.fragment2;
	}
	
	public boolean equals(Object obj) {
		Clone clone = (Clone) obj;
		if(this.fragment1.equals(clone.fragment1) && this.fragment2.equals(clone.fragment2)) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return fragment1.getSrcFile() + "," + fragment1.getStartLine() + "," + fragment1.getEndLine() + "," + fragment2.getSrcFile() + "," + fragment2.getStartLine() + "," + fragment2.getEndLine();
	}
	
	public static void main(String args[]) {
		Clone c1 = new Clone(new Fragment("/file",10,100), new Fragment("/file2",20,200));
		Clone c2 = new Clone(new Fragment("/file",10,100), new Fragment("/file2",20,200));
		Clone c3 = new Clone(new Fragment("/file4",20,100), new Fragment("/file3",20,200));
		assert(c1.getFragment1().getSrcFile().equals("/file"));
		assert(c1.getFragment1().getStartLine() == 10);
		assert(c1.getFragment1().getEndLine() == 100);
		assert(c1.getFragment2().getSrcFile().equals("/file2"));
		assert(c1.getFragment2().getStartLine() == 20);
		assert(c1.getFragment2().getEndLine() == 200);
		assert(c1.equals(c2));
		assert(!c1.equals(c3));
		System.out.println("Clone test successful.");
	}
}
