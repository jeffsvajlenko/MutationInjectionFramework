package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import util.FileUtil;
import util.StreamGobbler;
import util.SystemUtil;
import util.TXLUtil;

import experiment.ExperimentSpecification;

public class NormalizeSystem {
	
	/**
	 * Normalizes a system.  Runs each file through artistic style, dos2unix, and sed (ensure ends with newline).
	 * @param system Path of the system to normalize.
	 * @param language Language of the system.
	 * @throws IllegalArgumentException Thrown if language is not supported, or if system is not a directory.
	 * @throws InterruptedException Thrown if a required process (TXL or ArtisticStyle) is interrupted externally.
	 * @throws IOException If an IO error occurs.
	 * @throws ArtisticStyleFailedException If artistic style fails to run.
	 * @throws FileSanetizationFailedException  If file sanetization fails to run.  Will for sure occur if dos2unix or sed are not installed or in path.
	 */
	public static void normalizeSystem(Path system, int language, PrintStream log) throws IllegalArgumentException, InterruptedException, IOException, ArtisticStyleFailedException, FileSanetizationFailedException {
	Objects.requireNonNull(system);
	Objects.requireNonNull(log);
	if(!ExperimentSpecification.isLanguageSupported(language)) {
		throw new IllegalArgumentException("Language not supported.");
	}
	if(!Files.exists(system)) {
		throw new FileNotFoundException("Specified directory does not exist.");
	}
	if(!Files.isDirectory(system)) {
		throw new IllegalArgumentException("Specified system directory is not a directory.");
	}
	
	log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem) - Normalizing System: " + system + ", of language: " + ExperimentSpecification.languageToString(language));
	
	
//Run A-Style
		//Prepare Command
		String command = SystemUtil.getArtisticStyleExecutable().toAbsolutePath().normalize().toString() + " --recursive --style=allman --indent=spaces --suffix=none ";
		if(language == ExperimentSpecification.JAVA_LANGUAGE) {
			command += system.toAbsolutePath().normalize() + "/*.java";
		} else if (language == ExperimentSpecification.C_LANGUAGE) {
			command += system.toAbsolutePath().normalize() + "/*.c " + system.toAbsolutePath().normalize() + "/*.h";
		} else if (language == ExperimentSpecification.CS_LANGUAGE) {
			command +=  system.toAbsolutePath().normalize() + "/*.cs";
		} else {
			throw new IllegalArgumentException("Language not supported.");
		}
		
		//Run Command
		log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\tRunning Artistic Style...");
		int retval;
		Process process=null;
		try {
			process = Runtime.getRuntime().exec(command, null, null);
			new StreamGobbler(process.getInputStream()).run();
			new StreamGobbler(process.getErrorStream()).run();
			retval = process.waitFor();
			try {process.getInputStream().close();} catch (Exception ee) {}
			try {process.getOutputStream().close();} catch (Exception ee) {}
			try {process.getErrorStream().close();} catch (Exception ee) {}
		} catch (IOException e) {
			throw e;
		} catch (InterruptedException e) {
			if(process != null) {
				try {process.getInputStream().close();} catch (Exception ee) {}
				try {process.getOutputStream().close();} catch (Exception ee) {}
				try {process.getErrorStream().close();} catch (Exception ee) {}
			}
			throw e;
		}
		
		//Check Success
		if(retval != 0) {
			System.out.println(command);
			throw new ArtisticStyleFailedException("ArtisticStyle returned error status.");
		}
	
		List<Path> files = FileUtil.fileInventory(system);
	
//Sanetize (unix newlines, EOF ends with newline), done to prevent errors with TXL scripts
		log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\tSanetizing Files (normalize newlines to unix, ensure EOF is newline).");
		for(Path path : files) {
			log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\t\tSanetizing: " + path);
			String[] commandArray = new String[2];
			commandArray[0] = SystemUtil.getScriptsLocation().toAbsolutePath().normalize().resolve("SanetizeFile").toString();
			commandArray[1] = path.toAbsolutePath().normalize().toString();
			try {
				process = Runtime.getRuntime().exec(commandArray);
				new StreamGobbler(process.getInputStream()).run();
				new StreamGobbler(process.getErrorStream()).run();
				retval = process.waitFor();
				try {process.getInputStream().close();} catch (Exception ee) {}
				try {process.getOutputStream().close();} catch (Exception ee) {}
				try {process.getErrorStream().close();} catch (Exception ee) {}
			} catch (IOException e) {
				throw e;
			} catch (InterruptedException e) {
				if(process != null) {
					try {process.getInputStream().close();} catch (Exception ee) {}
					try {process.getOutputStream().close();} catch (Exception ee) {}
					try {process.getErrorStream().close();} catch (Exception ee) {}
				}
				throw e;
			}
			if(retval != 0) {
				throw new FileSanetizationFailedException("File Sanetaizaiton script failed for " + path + " .  Is dos2unix and sed installed? (Return value: " + retval + ")");
			}
		}
		
//Remove file that can not be parsed by txl grammar
		log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\tRemoving files unparseable by TXL grammar. (Require TXL abilities in framework)");
		Path tmpfile = Files.createTempFile(SystemUtil.getTemporaryDirectory(), "NormalizeStyle", "tmp");
		for(Path path : files) {
			log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\t\tChecking: " + path);
			if(!TXLUtil.prettyPrintSourceFile(path, tmpfile, language)) {
				try {
					log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\t\tRemoved: " + path + ".");
					Files.delete(path);
				} catch (IOException e) {
					Files.deleteIfExists(tmpfile);
					throw e;
				}
			}
		}
		Files.deleteIfExists(tmpfile);

//Remove file that can no be pretty-print
		log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\tRemoving files that can not be pretty printed by arbitrary source fragment pretty printer. (Avoid automatic validation problems)");
		files = FileUtil.fileInventory(system); // refresh list
		for(Path path : files) {
			log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\t\tChecking: " + path);
			if(FileUtil.countLines(path) == 0) {
				Files.delete(path);
			} else {
				if(!TXLUtil.prettyprint(path, tmpfile, 1, FileUtil.countLines(path), language)) {
					try {
						log.println("[" + Calendar.getInstance().getTime() + "] (NormalizeSystem)\t\tRemoved: " + path + ".");
						Files.delete(path);
					} catch (IOException e) {
						Files.deleteIfExists(tmpfile);
						throw e;
					}
				}
			}
		}
		Files.deleteIfExists(tmpfile);
	}
	
	public static void main(String args[]) throws IllegalArgumentException, InterruptedException, IOException, ArtisticStyleFailedException, FileSanetizationFailedException {
		NormalizeSystem.normalizeSystem(Paths.get("/media/Storage/monoosc"), ExperimentSpecification.CS_LANGUAGE, System.out);
	}
}
