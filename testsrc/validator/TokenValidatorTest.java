package validator;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Paths;

import models.Clone;
import models.Fragment;

import org.junit.Test;

import experiment.ExperimentSpecification;

public class TokenValidatorTest {

	@Test
	public void test() throws IOException, IllegalArgumentException, NullPointerException, InterruptedException {
		Fragment f1, f2;
		Clone clone;
		TokenValidator tv;
		ValidatorResult vr;
		
		//Java
		tv = new TokenValidator(0.50, 0.50, ExperimentSpecification.JAVA_LANGUAGE);
		//Function
			//Type1
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/contrib/CTXCommandMenu.java"), 126, 137);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/util/CommandMenu.java"), 90, 101);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
				//Type2
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/contrib/MDIDesktopPane.java"), 544, 605);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/contrib/MDIDesktopPane.java"), 476, 537);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
				//Type3
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/contrib/CTXWindowMenu.java"), 160, 202);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/contrib/WindowMenu.java"), 79, 116);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.7907, 0.0001);
		assertEquals(vr.getFragment1Similarity(), .7907, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.9551, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.7907, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 0.9551, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.7907, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
			//Block
				//Type1
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/standard/ConnectionTool.java"), 356, 368);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/standard/ConnectionHandle.java"), 159, 171);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
				//Type2
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/util/UndoCommand.java"), 42, 63);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/util/RedoCommand.java"), 36, 56);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
				//Type3
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/contrib/CompositeFigureCreationTool.java"), 37, 59);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/JHotDraw54b1/src/CH/ifa/draw/contrib/NestedCreationTool.java"), 35, 54);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.6328, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 0.6328, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 1.0, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.6328, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 1.0, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.6328, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
		//C
		tv = new TokenValidator(0.50, 0.50, ExperimentSpecification.C_LANGUAGE);
			//Function
				//Type1
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/device/sysdep_FREEBSD.c"), 92, 115);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/device/sysdep_LINUX.c"), 114, 137);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		System.out.println(vr.getMessage());
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
				//Type2
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/process/sysdep_LINUX.c"), 244, 296);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/process/sysdep_SOLARIS.c"), 188, 240);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		//System.out.println(vr.getMessage());
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		//System.out.println(vr.getSimilarity() + " " + vr.getFragment1Similarity() + " " + vr.getFragment2Similarity());
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
			//Type3
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/socket.c"), 490, 527);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/socket.c"), 446, 480);
				
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.8333, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 0.8333, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 1.0, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
				
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.8333, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 1.0, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.8333, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
			//Block
				//Type1
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/http/cervlet.c"), 2264, 2273);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/http/cervlet.c"), 2247, 2256);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
				//Type2
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/gc.c"), 299, 315);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/gc.c"), 319, 335);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
			//Type3
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/util.c"), 708, 727);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/monit-4.2/http/cervlet.c"), 1615, 1634);
				
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.9368, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 0.9674, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.9368, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
				
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.9368, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 0.9368, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.9674, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
		//CS
		tv = new TokenValidator(0.50, 0.50, ExperimentSpecification.CS_LANGUAGE);
		//Function
			//Type1
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/cs/OpenWebSpiderCS/lists.cs"), 355, 368);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/cs/OpenWebSpiderCS/lists.cs"), 18, 31);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
			//Type2
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/cs/Greenshot-SRC-0.7.009/Forms/SettingsForm.cs"), 159, 170);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/cs/Greenshot-SRC-0.7.009/Forms/LanguageDialog.cs"), 33, 44);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
				//Type3
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/cs/OpenWebSpiderCS/lists.cs"), 215, 231);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/cs/OpenWebSpiderCS/lists.cs"), 355, 368);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.76190, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 0.76190, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 1.0, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.76190, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 1.0, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.76190, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
		//Block
			//Type1
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/cs/OpenWebSpiderCS/lists.cs"), 356, 368);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/cs/OpenWebSpiderCS/lists.cs"), 134, 146);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE1);
		
			//Type2
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/cs/Greenshot-SRC-0.7.009/Forms/LanguageDialog.cs"), 34, 44);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/cs/Greenshot-SRC-0.7.009/Forms/SettingsForm.cs"), 160, 170);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertTrue(vr.getSimilarity() == 1.0);
		assertTrue(vr.getFragment1Similarity() == 1.0);
		assertTrue(vr.getFragment2Similarity() == 1.0);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE2);

			//Type3
		f1 = new Fragment(Paths.get("testdata/ValidatorTest/cs/Greenshot-SRC-0.7.009/Drawing/DrawableContainerList.cs"), 262, 273);
		f2 = new Fragment(Paths.get("testdata/ValidatorTest/cs/Greenshot-SRC-0.7.009/Drawing/DrawableContainerList.cs"), 312, 323);
		
		clone = new Clone(f1,f2);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.86765, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 0.92188, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.86765, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
		
		clone = new Clone(f2,f1);
		vr = tv.validate(clone);
		assertTrue(vr.getValidationResult() != ValidatorResult.ERROR);
		assertEquals(vr.getSimilarity(), 0.86765, 0.0001);
		assertEquals(vr.getFragment1Similarity(), 0.86765, 0.0001);
		assertEquals(vr.getFragment2Similarity(), 0.92188, 0.0001);
		assertTrue(vr.getCloneType() == ValidatorResult.TYPE3);
	}

}
