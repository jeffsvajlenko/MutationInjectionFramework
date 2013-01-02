package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import experiment.ExperimentSpecification;

/**
 *
 * Collection of utility functions for the MutationInjectionFramework.
 *
 */
public class SystemUtil {
	/**
	 * @return MutationInjectionFramework installation location.
	 */
	public static Path getInstallRoot() {
		return Paths.get(".").toAbsolutePath().normalize();
	}
	
	/**
	 * @return MutationInjectionFramework script location.
	 */
	public static Path getScriptsLocation() {
		return getInstallRoot().resolve("scripts").toAbsolutePath().normalize();
	}
	
	/**
	 * @return MutationInjectionFramework temporary file directory.
	 */
	public static Path getTemporaryDirectory() {
		return getInstallRoot().resolve("tmp").toAbsolutePath().normalize();
	}
	
	/**
	 * @return MutationInjectionFramework TXL script directory.
	 */
	public static Path getTxlDirectory() {
		return getInstallRoot().resolve("txl").toAbsolutePath().normalize();
	}
	
	/**
	 * 
	 * @param language
	 * @return MutationInjectionFramework TXL script directory for the specified directory.
	 */
	public static Path getTxlDirectory(int language) {
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language not supported.");
		}
		Path p = getTxlDirectory().resolve(ExperimentSpecification.languageToString(language));
		if(Files.isDirectory(p)) {
			return p.toAbsolutePath().normalize();
		}
		else {
			return null;
		}
	}
	
	private static Path txl = null;
	
	/**
	 * Returns a path to the TXL executable, or null if TXL is not installed.  Relies on 
	 * @return a path to the TXL executable, or null if TXL is not installed.
	 */
	public static Path getTxlExecutable() {
		if(txl == null) {
			String txlstring=null;
			try {
				Process process = Runtime.getRuntime().exec("which txl");
				Scanner scanner = new Scanner(process.getInputStream());
				if(scanner.hasNextLine())
					txlstring = scanner.nextLine();
				scanner.close();
				new StreamGobbler(process.getInputStream()).run();
				new StreamGobbler(process.getErrorStream()).run();
				process.waitFor();
				try{process.getInputStream().close();} catch(Exception ee) {};
				try{process.getOutputStream().close();} catch(Exception ee) {};
				try{process.getErrorStream().close();} catch(Exception ee) {};
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Method for getting location of TXL exectuable had an IO error occur.  Executable could therefore not be found.  Application must terminate.");
				System.exit(-1);
				return null;
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Method for getting location of TXL executable uses the command 'which'.  The which process was interrupted.  TXL executable could therefore not be found.  Application must terminate.");
				System.exit(-1);
				return null;
			}
			if(txlstring == null) {
				System.out.println("Could not find TXL.  Framework can not continue (could not find dependency). Install TXL and ensure the its executable location is in the PATH env variable when you run this program.");
				System.out.println(System.getenv());
				System.exit(-1);
				return null;
			} else {
				txl = Paths.get(txlstring);
			}
		}
		return txl;
	}
	
	private static Path astyle = null;
	public static Path getArtisticStyleExecutable() {
		if(astyle == null) {
			String txlstring=null;
			try {
				Process process = Runtime.getRuntime().exec("which astyle");
				Scanner scanner = new Scanner(process.getInputStream());
				if(scanner.hasNextLine()) {
					txlstring = scanner.nextLine();
				}
				scanner.close();
				new StreamGobbler(process.getInputStream()).run();
				new StreamGobbler(process.getErrorStream()).run();
				process.waitFor();
				try{process.getInputStream().close();} catch(Exception ee) {};
				try{process.getOutputStream().close();} catch(Exception ee) {};
				try{process.getErrorStream().close();} catch(Exception ee) {};
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Method for getting location of astyle exectuable had an IO error occur.  Executable could therefore not be found.  Application must terminate.");
				System.exit(-1);
				return null;
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.out.println("Method for getting location of astyle executable uses the command 'which'.  The which process was interrupted.  astyle executable could therefore not be found.  Application must terminate.");
				System.exit(-1);
				return null;
			}
			if(txlstring == null) {
				System.out.println("Could not find astyle.  Framework can not continue (could not find dependency). Install astyle and ensure the its executable location is in the PATH env variable when you run this program.");
				System.out.println(System.getenv());
				System.exit(-1);
				return null;
			} else {
				astyle = Paths.get(txlstring);
			}
		}
		return astyle;
	}
	
	/**
	 * Returns the root directory of the mutators.
	 * @return the root directory of the mutators.
	 */
	public static Path getOperatorsPath() {
		return getInstallRoot().resolve("operators").toAbsolutePath().normalize();
	}
}
