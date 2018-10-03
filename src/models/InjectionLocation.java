// Mutation and Injection Framework (https://github.com/jeffsvajlenko/MutationInjectionFramework)
package models;

import java.nio.file.Path;
import java.util.Objects;

/**-
 * Represents an injection location.
 */
public class InjectionLocation {
	
	/** Line to inject at */
	private int line;
	
	/** Source file to inject into */
	private Path srcfile;
	
	/**
	 * Creates an InjectionLocation with the specified source file and line number.
	 * @param sourceFile The file to inject into.
	 * @param lineNumber The line to inject at.
	 * @throws IllegalArgumentException if sourcfile is null or if linenumber < 0.
	 */
	public InjectionLocation(Path sourceFile, int lineNumber) {
		Objects.requireNonNull(sourceFile);
		if(lineNumber < 1) {
			throw new IllegalArgumentException("InjectionLocation created with invalid line number.");
		}
		this.srcfile = sourceFile;
		this.line = lineNumber;
	}
	
	/**
	 * Returns the injection line number.
	 * @return the injection line number.
	 */
	public int getLineNumber() {
		return this.line;
	}
	
	/**
	 * Returns the injection source file.
	 * @return the injection source file.
	 */
	public Path getSourceFile() {
		return this.srcfile;
	}
	
	public boolean equals(Object obj) {
		if(obj == null) {
			return false;
		}
		if(!obj.getClass().equals(this.getClass())) {
			return false;
		}
		
		InjectionLocation o = (InjectionLocation) obj;
		if(this.line == o.line && this.srcfile.toAbsolutePath().normalize().equals(o.srcfile.toAbsolutePath().normalize())) {
			return true;
		} else {
			return false;
		}
	}
}
