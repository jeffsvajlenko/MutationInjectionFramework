
import java.util.*;

/**
 * 
 * Represents a clone class.  A set of similar fragments.
 *
 */
public class CloneClass {
	private int id = -1; /** The identifier of this clone class */
	private ArrayList<Fragment> fragments = new ArrayList<Fragment>(); /** The code fragments in this class*/
	
	/**
	 * Creates a clone class with the specified id.
	 * @param id
	 */
	public CloneClass(int id){
		this.id = id;
	}
	
	/**
	 * Addas a code fragment to the clone class.
	 * @param clone
	 */
	public void addClone(Fragment clone){
		fragments.add(clone);
	}
	
	/**
	 * @return The fragments in this class.
	 */
	public ArrayList<Fragment> getClones(){
		return fragments;
	}
	
	/**
	 * @return The identifier of this clone class.
	 */
	public int getId(){
		return this.id;
	}
	
	/**
	 * @return The number of fragments in the clone class.
	 */
	public int getNumFragments(){
		return fragments.size();
	}

}