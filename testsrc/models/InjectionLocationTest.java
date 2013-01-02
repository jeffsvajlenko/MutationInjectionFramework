package models;

import static org.junit.Assert.*;

import java.nio.file.Paths;

import org.junit.Test;

public class InjectionLocationTest {

	@Test
	public void testInjectionLocation() {
		InjectionLocation il = new InjectionLocation(Paths.get("/file"), 10);
		assert(il != null);
		assert(il.getLineNumber() == 10);
		assert(il.getSourceFile().toAbsolutePath().normalize().equals("/file"));
		
		boolean error;
		
		error = false;
		try {
			new InjectionLocation(null, 10);
		} catch (NullPointerException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			new InjectionLocation(Paths.get("/file2"), 0);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
		
		error = false;
		try {
			new InjectionLocation(Paths.get("/file2"), -10);
		} catch (IllegalArgumentException e) {
			error = true;
		}
		assertTrue(error);
	}

	@Test
	public void testGetLineNumber() {
		InjectionLocation il = new InjectionLocation(Paths.get("/file"), 10);
		assert(il.getLineNumber() == 10);
		
		il = new InjectionLocation(Paths.get("/file2"), 20);
		assert(il.getLineNumber() == 20);
	}

	@Test
	public void testGetSourceFile() {
		InjectionLocation il = new InjectionLocation(Paths.get("/file"), 10);
		assert(il.getSourceFile().toAbsolutePath().normalize().equals("/file"));
		
		il = new InjectionLocation(Paths.get("/file2"), 20);
		assert(il.getSourceFile().toAbsolutePath().normalize().equals("/file2"));
	}

	@Test
	public void testEqualsObject() {
		InjectionLocation il1 = new InjectionLocation(Paths.get("/file"), 10);
		InjectionLocation il2 = new InjectionLocation(Paths.get("/file"), 10);
		InjectionLocation il3 = new InjectionLocation(Paths.get("/file2"), 10);
		InjectionLocation il4 = new InjectionLocation(Paths.get("/file"), 12);
		assert(il1.equals(il2));
		assert(!il1.equals(il3));
		assert(!il1.equals(il4));
		assert(!il1.equals(null));
	}

}
