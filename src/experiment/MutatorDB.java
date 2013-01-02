package experiment;

import java.util.List;

import models.AbstractMutator;

public class MutatorDB extends AbstractMutator<OperatorDB> {

	private int id;
	
	/**
	 * Created a mutator from the database.
	 * @param id The identifier of the mutator.
	 * @param description Description of the mutator.
	 * @param operators List of operators applied by the mutator (in order).
	 */
	public MutatorDB(int id, String description, List<OperatorDB> operators) {
		super(description, operators);
		this.id = id;
	}

	/**
	 * Returns the id of the mutator in the database.
	 * @return the id of the mutator in the database.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * Returns if the mutator includes an operator from the database with the specified id.
	 * @param operator_id The id of the operator from the database to see if this mutator includes.
	 * @return if the mutator includes an operator from the database with the specified id.
	 */
	public boolean includesOperator(int operator_id) {
		List<OperatorDB> operators = this.getOperators();
		for(OperatorDB opdb : operators) {
			if(opdb.getId() == operator_id) {
				return true;
			}
		}
		return false;
	}
	
	public String toString() {
		String str = "Mutator(id: " + this.getId() + ", Description: " + this.getDescription() + ", TargetCloneType: " + this.getTargetCloneType() + ", Operators: ";
		for(OperatorDB opdb : this.getOperators()) {
			str += opdb.getId() + ",";
		}
		str += ")";
		return str;
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		MutatorDB m = (MutatorDB) obj;
		if(	this.id == m.id &&
				this.getDescription().equals(m.getDescription()) &&
				this.getTargetCloneType() == m.getTargetCloneType() &&
				oplistequals(this.getOperators(),m.getOperators())) {
			return true;
		} else {
			return false;
		}
	}
	private boolean oplistequals(List<OperatorDB> one, List<OperatorDB> two) {
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
