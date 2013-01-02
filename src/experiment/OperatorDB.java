package experiment;

import java.nio.file.Path;

import models.Operator;

public class OperatorDB extends Operator {

	private int id;
	
	public OperatorDB(int id, String name, String description, int targetCloneType, Path mutator) {
		super(name, description, targetCloneType, mutator);
		this.id = id;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String toString() {
		return "Operator(id: " + this.getId() + ", Name: " + this.getName() + ", Description: " + this.getDescription() + ", TargetCloneType: " +this.getTargetCloneType() + ", MutatorExec: " + this.getMutator() + ")";
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		OperatorDB o = (OperatorDB) obj;
		if(	this.id == o.id &&
				this.getDescription().equals(o.getDescription()) &&
				this.getName() == o.getName() &&
				this.getMutator().equals(o.getMutator()) &&
				this.getTargetCloneType() == o.getTargetCloneType()) {
			return true;
		} else {
			return false;
		}
	}
}
