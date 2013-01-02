package experiment;

import java.nio.file.Path;

import models.Tool;

/**
 * Represents a clone detection tool that is being tracked by the database.
 */
public class ToolDB extends Tool {

	private int id;
	
	/**
	 * Creates a tool.
	 * @param id The tool's id (given by the database).
	 * @param name The name of the tool.
	 * @param description A description of the tool.
	 * @param directory The install location of the tool.
	 * @param toolRunner The location of the tool's runner executable.
	 */
	public ToolDB(int id, String name, String description, Path directory, Path toolRunner) {
		super(name, description, directory, toolRunner);
		this.id = id;
	}
	
	/**
	 * Returns the id of the tool (given by the database).
	 * @return the id of the tool.
	 */
	public int getId() {
		return this.id;
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		
		if(!this.getClass().equals(obj.getClass())) {
			return false;
		}
		
		ToolDB t = (ToolDB) obj;
		if(	this.id == t.id &&
				this.getDescription().equals(t.getDescription()) &&
				this.getDirectory().equals(t.getDirectory()) &&
				this.getName().equals(t.getName()) &&
				this.getToolRunner().equals(t.getToolRunner())) {
			return true;
		} else {
			return false;
		}
	}
}
