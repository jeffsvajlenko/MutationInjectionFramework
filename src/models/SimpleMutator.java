// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package models;

import java.util.List;

/**
 * Represents a mutator.
 */
public class SimpleMutator extends AbstractMutator<Operator> {
	
	/**
	 * Creates a simple mutator.
	 * @param description Description of the mutator.
	 * @param operators List of operators this mutator uses, in order.
	 */
	public SimpleMutator(String description, List<Operator> operators) {
		super(description, operators);
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		SimpleMutator m = (SimpleMutator) obj;
		if(	this.description.equals(m.description) &&
			this.targetCloneType == m.targetCloneType &&
			oplistequals(this.operators,m.operators)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean oplistequals(List<Operator> one, List<Operator> two) {
		if(one.size() != two.size()) {
			return false;
		} else {
			int i = 0;
			while(i < one.size()) {
				if(!one.get(i).equals(two.get(i))) {
					return false;
				}
				i++;
			}
			return true;
		}
	}
}
