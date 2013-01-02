package experiment;

import java.nio.file.Path;
import java.util.Objects;

import models.Fragment;

public class FragmentDB extends Fragment {

	private int id;
	private Path fragmentFile;
	
	/**
	 * Creates a fragment associated with the database.
	 * @param id The fragment's id.
	 * @param srcFile The source file containing the fragment.
	 * @param startLine The startline of the fragment.
	 * @param endLine The endline of the fragment (inclusive).
	 */
	public FragmentDB(int id, Path fragmentFile, Path srcFile, int startLine, int endLine) {
		super(srcFile, startLine, endLine);
		Objects.requireNonNull(fragmentFile);
		this.fragmentFile = fragmentFile;
		this.id = id;
	}

	/**
	 * Returns the id of this fragment.
	 * @return the id of this fragment.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns the path to the file containing the fragment.
	 * @return the path to the file containing the fragment.
	 */
	public Path getFragmentFile() {
		return this.fragmentFile;
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		FragmentDB f = (FragmentDB) obj;
		if(this.id == f.id && this.fragmentFile.equals(f.fragmentFile) && this.getSrcFile().toAbsolutePath().normalize().equals(f.getSrcFile().toAbsolutePath().normalize()) && this.getEndLine() == f.getEndLine() && this.getStartLine() == f.getStartLine()) {
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "Fragment(" + id + ") : " + this.fragmentFile + " " + this.getSrcFile() + " " + this.getStartLine() + "-" + this.getEndLine();
	}
	
}
