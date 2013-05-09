package experiment;


import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import main.ArtisticStyleFailedException;
import main.FileSanetizationFailedException;
import main.NormalizeSystem;
import models.Clone;
import models.CloneDetectionReport;
import models.Fragment;
import models.MutantBase;
import models.VerifiedClone;

import org.apache.commons.io.FileUtils;

import util.FileUtil;
import util.FragmentUtil;
import util.SystemUtil;

/**
 * Encapsulates the experiment's data.
 * 
 * Handles the initialization of an experiment's data, its modification.  Also tracks the stage of the experiment, and enforces data changes
 * to the stages where it is valid.
 */
public class ExperimentData {
	
// -- Fields
	
	/** Database Connection */
	private Connection connection;
	
	/** Experiment Folder */
	private Path folder;
	
	/** Subject System Folder*/
	private Path subjectsystem;
	
	/** Source Repository Folder */
	private Path repository;

	/** Selected Fragments Folder */
	private Path fragments;
	
	/** Mutant Fragments Folder */
	private Path mutantfragments;
	
	/** Clone Detection Reports Folder */
	private Path reports;
	
	/** Constructed Mutant System Folder*/
	private Path mutantsystem;
	
	/** Temporary Data Folder */
	private Path temp;
	
	/** Embeded Database File */
	private Path database;
	
// -- Constants	
	
	/**
	 * Error Stage.  Experiment has been corrupted (as judged by external logic).  Data is locked.
	 */
	static public final int ERROR_STAGE = -1;
	
	/**
	 * Generation Setup Stage.  Operators, mutators, and generation properties may be modified.  Experiments are initialized to this stage.
	 */
	static public final int GENERATION_SETUP_STAGE = 0;
	
	/**
	 * Generation Stage.  Fragments, mutant fragments, and mutant systems may be modified.
	 */
	static public final int GENERATION_STAGE = 1;
	
	/**
	 * Evaluation Setup Stage.  Subject tools and evaluation phase properties may be modified.  Reports, unit performances (recall/precision) may be deleted.
	 */
	static public final int EVALUATION_SETUP_STAGE = 2; 
	
	/**
	 * Evaluation Stage.  Clone detection reports, unit recall, and unit precisions may be modified.
	 */
	static public final int EVALUATION_STAGE = 3;
	
	/**
	 * Results Stage.  Summary recall and precision can be queried.
	 */
	static public final int RESULTS_STAGE = 4;
	
// --- Constructors
	
	/**
	 * Creates experiment data for a new experiment.
	 * @param datafolder Where to store the experiment.  Should not already exist.
	 * @param system The directory containing the desired subject system.
	 * @param repository The directory containing the desired source repository.
	 * @param language The language of the experiment.  
	 * @param log
	 * @throws SQLException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws InterruptedException
	 * @throws ArtisticStyleFailedException
	 * @throws FileSanetizationFailedException
	 */
	protected ExperimentData(Path datafolder, Path system, Path repository, int language, PrintStream log) throws SQLException, IOException, IllegalArgumentException, InterruptedException, ArtisticStyleFailedException, FileSanetizationFailedException {
		//Check Arguments
		Objects.requireNonNull(datafolder);
		Objects.requireNonNull(system);
		Objects.requireNonNull(repository);
		if(Files.exists(datafolder)) {
			throw new IllegalArgumentException("datafolder already exists.");
		}
		if(!Files.isDirectory(system)) {
			throw new IllegalArgumentException("system is not a directory.");
		}
		if(!Files.isDirectory(repository)) {
			throw new IllegalArgumentException("repository is not a directory.");
		}
		if(ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language is not supported.");
		}
		
		//Set Up Data Directories
		datafolder = datafolder.toAbsolutePath().normalize();
		this.folder = datafolder;
		this.subjectsystem = datafolder.resolve("system");
		this.repository = datafolder.resolve("repository");
		this.fragments = datafolder.resolve("fragments");
		this.mutantfragments = datafolder.resolve("mutantfragments");
		this.reports = datafolder.resolve("reports");
		this.mutantsystem = datafolder.resolve("mutantbase");
		this.temp = datafolder.resolve("temp");
		this.database = datafolder.resolve("database");
		Files.createDirectories(this.folder);
		Files.createDirectories(this.subjectsystem);
		Files.createDirectories(this.repository);
		Files.createDirectories(this.fragments);
		Files.createDirectories(this.mutantfragments);
		Files.createDirectories(this.reports);
		Files.createDirectories(this.mutantsystem);
		Files.createDirectories(this.temp);
		
		//Import
		FileUtils.copyDirectory(repository.toFile(), this.repository.toFile());
		FileUtils.copyDirectory(system.toFile(), this.subjectsystem.toFile());
		
		//Normalize Imports
		NormalizeSystem.normalizeSystem(this.repository, language, log);
		NormalizeSystem.normalizeSystem(this.subjectsystem, language, log);
		
		// Create Database  Connection
		connection = DriverManager.getConnection("jdbc:h2:" + database.toAbsolutePath().normalize().toString(), "sa", "");

		//Initialize Database
		this.initialize();
		
		//initialize stage
		this.setStage(ExperimentData.GENERATION_SETUP_STAGE);
	}
	
	//Load Existing Experiment
	protected ExperimentData(Path datafolder) throws SQLException, IllegalArgumentException {
		//Check Arguments
		Objects.requireNonNull(datafolder);
		if(!Files.isDirectory(datafolder)) {
			throw new IllegalArgumentException("folder does not exist.");
		}
		
		//Setup Directories
		datafolder = datafolder.toAbsolutePath().normalize();
		this.folder = datafolder;
		this.subjectsystem = datafolder.resolve("system");
		this.repository = datafolder.resolve("repository");
		this.fragments = datafolder.resolve("fragments");
		this.mutantfragments = datafolder.resolve("mutantfragments");
		this.reports = datafolder.resolve("reports");
		this.mutantsystem = datafolder.resolve("mutantbase");
		this.temp = datafolder.resolve("temp");
		this.database = datafolder.resolve("database");
		
		//Check Directories
		if(!Files.isDirectory(this.folder)) {
			throw new IllegalArgumentException("experiment directory is invalid -- missing root folder.");
		}
		if(!Files.isDirectory(this.subjectsystem)) {
			throw new IllegalArgumentException("experiment directory is invalid -- system is missing.");
		}
		if(!Files.isDirectory(this.repository)) {
			throw new IllegalArgumentException("experiment directory is invalid -- repository is missing.");
		}
		if(!Files.isDirectory(this.fragments)) {
			throw new IllegalArgumentException("experiment directory is invalid -- fragments is missing.");
		}
		if(!Files.isDirectory(this.mutantfragments)) {
			throw new IllegalArgumentException("experiment directory is invalid -- mutantfragments is missing.");
		}
		if(!Files.isDirectory(this.reports)) {
			throw new IllegalArgumentException("experiment directory is invalid -- reports is missing.");
		}
		if(!Files.isDirectory(this.mutantsystem)) {
			throw new IllegalArgumentException("experiment directory is invalid -- mutantbase is missing.");
		}
		if(!Files.isDirectory(this.temp)) {
			throw new IllegalArgumentException("experiment directory is invalid -- temp is missing.");
		}
		if(!Files.isRegularFile(Paths.get(this.database.toString() + ".h2.db"))) {
			throw new IllegalArgumentException("experiment directory is invalid -- database is missing.");
		}
		
		
		// Create Database  Connection
		connection = DriverManager.getConnection("jdbc:h2:" + database.toAbsolutePath().normalize().toString(), "sa", "");
		
		//Check Data
			//fragments
		for(FragmentDB fragment : this.getFragments()) {
			if(!Files.exists(fragment.getFragmentFile())) {
				throw new IllegalArgumentException("Data is incomplete, a fragment's fragment file is missing from fragment directory: " + fragment.getFragmentFile() + ".");
			}
			if(!Files.exists(fragment.getSrcFile())) {
				throw new IllegalArgumentException("Data is incomplete, a fragment's source file is missing from repository: " + fragment.getSrcFile() + ".");
			}
		}
			//mutantfragments
		for(MutantFragment mutantfragment : this.getMutantFragments()) {
			if(!Files.exists(mutantfragment.getFragmentFile())) {
				throw new IllegalArgumentException("Data is incomplete, a mutant fragment's fragment file is missing from mutant fragment directory: " + mutantfragment.getFragmentFile() + ".");
			}
		}
			//mutantbase
		for(MutantBaseDB mutantbase : this.getMutantBases()) {
			if(!Files.exists(this.subjectsystem.resolve(this.mutantsystem.relativize(mutantbase.getOriginalFragment().getSrcFile())))) {
				throw new IllegalArgumentException("Data is incomplete, a file in the system which is to be injected into is missing: " + mutantbase.getOriginalFragment().getSrcFile()+ ".");
			}
			if(!Files.exists(this.subjectsystem.resolve(this.mutantsystem.relativize(mutantbase.getMutantFragment().getSrcFile())))) {
				throw new IllegalArgumentException("Data is incomplete, a file in the system which is to be injected into is missing: " + mutantbase.getMutantFragment().getSrcFile() + ".");
			}
		}
			//reports
		for(CloneDetectionReportDB cdr : this.getCloneDetectionReports()) {
			if(!Files.exists(cdr.getReport())) {
				throw new IllegalArgumentException("Data is incomplete, a clone detection report file is missing from reports directory: " + cdr.getReport() + ".");
			}
		}
	}
		
	
// -- Experiment Stage	
	
	/**
	 * Returns the current stage.
	 * @return the current stage.
	 * @throws SQLException 
	 */
	public int getCurrentStage() throws SQLException {
		return this.getStage();
	}
	
	/**
	 * Invalidates experiment by setting stage to ERROR_STAGE.
	 * @return The current stage.
	 * @throws SQLException 
	 */
	public int invalidateExperiment() throws SQLException {
		this.setStage(ExperimentData.ERROR_STAGE);
		return this.getStage();
	}
	
	/**
	 * Progresses to the next stage.  The following lists the transitions as well as the requirements.
	 * 	GENERATION SETUP -> GENERATION : No requirements.  All settings have defaults.
	 *  GENERATION -> EVALUATION SETUP : At least one mutant base has been created (something to evaluate).
	 *  EVALUATION SETUP -> EVALUATION : At least one tool specified (something to evaluate).
	 *  EVALUATION -> RESULTS          : For each mutant base and tool, a unit recall and unit precision has been created.
	 * @return the (now) current stage.
	 * @throws SQLException 
	 */
	public int nextStage() throws SQLException, IllegalStateException {
		// GENERATION SETUP -> GENERATION
		if(this.getStage() == ExperimentData.GENERATION_SETUP_STAGE) {
			this.setStage(ExperimentData.GENERATION_STAGE);
			
		// GENERATION -> EVALUATION SETUP
		} else if (this.getStage() == ExperimentData.GENERATION_STAGE) {
			if(this.numMutantBases() > 0) {
				this.setStage(ExperimentData.EVALUATION_SETUP_STAGE);
			} else {
				throw new IllegalStateException("Nothing was generated.  Cannot proceed to evaluation setup stage.");
			}
			
		//EVALUATION SETUP -> EVALUATION
		} else if (this.getStage() == ExperimentData.EVALUATION_SETUP_STAGE) {
			if(this.numTools() > 0) {
				this.setStage(ExperimentData.EVALUATION_STAGE);
			} else {
				throw new IllegalStateException("No tools added.  Can not proceed to evaluation stage.");
			}
			
		// EVALUATION -> RESULTS
		} else if (this.getStage() == ExperimentData.EVALUATION_STAGE) {
			for(MutantBaseDB mbdb : this.getMutantBases()) {
				for(ToolDB tdb : this.getTools()) {
					if(!this.existsUnitRecall(tdb.getId(), mbdb.getId()) || !this.existsUnitPrecision(tdb.getId(), mbdb.getId())) {
						throw new IllegalStateException("Evaluation is not complete.  Can not proceed to results stage.");
					}
				}
			}
			this.setStage(ExperimentData.RESULTS_STAGE);
		
		//RESULTS -> ??, throw exception because calling this at results stage means calling logic is wrong
		} else {
			throw new IllegalStateException("There is no stage after Results.");
		}
		return this.getStage();
	}
	
	/**
	 * Reverts to the previous stage.  The following are the possible times to call this, and its effects.
	 *  EVALUATION -> EVALUATION SETUP : All unit recalls and unit precisions are deleted.  Intended to allow evaluation settings to be varied.  Clone detection reports are maintained.
	 * 	RESULTS -> EVALUATION : No side effects.  Intended to remove results lock and add/evaluate further tools.
	 * Reverting stages not allowed for generation.
	 * @return The current stage after call.
	 * @throws IllegalStateException Called when invalid to revert stage.
	 * @throws SQLException 
	 */
	public int returnToEvaluationSetup() throws SQLException, IllegalStateException {
		//EVALUATION -> EVALUATION SETUP
		if(this.getStage() == ExperimentData.EVALUATION_STAGE) {
			this.setStage(ExperimentData.EVALUATION_SETUP_STAGE);
			
		//RESULTS_STAGE -> EVALUATION_STAGE
		} else if (this.getStage() == ExperimentData.RESULTS_STAGE) {
			this.setStage(ExperimentData.EVALUATION_SETUP_STAGE);
			
		//Illegal Transition
		} else {
			throw new IllegalStateException("Illegal stage move.");
		}
		return this.getStage();
	}
	
	
// -- Experiment Data paths

	public Path getPath() {
		return this.folder.toAbsolutePath().normalize();
	}

	public Path getSystemPath() {
		return this.subjectsystem.toAbsolutePath().normalize();
	}

	public Path getRepositoryPath() {
		return this.repository.toAbsolutePath().normalize();
	}

	public Path getFragmentsPath() {
		return this.fragments.toAbsolutePath().normalize();
	}

	public Path getMutantFragmentsPath() {
		return this.mutantfragments.toAbsolutePath().normalize();
	}

	public Path getReportsPath() {
		return this.reports.toAbsolutePath().normalize();
	}

	public Path getMutantBasePath() {
		return this.mutantsystem.toAbsolutePath().normalize();
	}

	public Path getTemporaryPath() {
		return this.temp.toAbsolutePath().normalize();
	}
	

// -- Database
	
	/**
	 * Initializes the database.
	 * @throws SQLException 
	 */
	public void initialize() throws SQLException {
		//Set Schema
		Statement stmt = connection.createStatement();
		String sql = "RUNSCRIPT FROM '" + SystemUtil.getInstallRoot() + "/database/database.sql'";
		stmt.execute(sql);
		
		//Merge in default properties if they don't exist yet
		sql = "SELECT * FROM properties";
		ResultSet rs = stmt.executeQuery(sql);
		if(!rs.next()) {
			sql = "MERGE INTO properties(id) VALUES (1)";
			stmt.execute(sql);
		}
	}
	
	/**
	 * For testing purposes.
	 */
	public void close() throws SQLException {
		connection.close();
	}
	
// --- FRAGMENT -------------------------------------------------------------------------------------------------------
	
	/**
	 * Creates a fragment in the experiment data and returns it.  Can only be called during the generation stage.
	 * @param fragment The fragment.  Must be from the repository.
	 * @return the fragment.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 * @throws IllegalArgumentException If source file does not exist, or is not a regular file, or is not readable, or if it is not from the repository.
	 * @throws IllegalStateException If called in any stage exception the generation stage.
	 */
	public FragmentDB createFragment(Fragment fragment) throws SQLException, FileNotFoundException, IOException, IllegalStateException {
		//Check Input
		if(this.getStage() != ExperimentData.GENERATION_STAGE) {
			throw new IllegalStateException("Only valid during generation phase");
		}
		Objects.requireNonNull(fragment);
		if(!Files.exists(fragment.getSrcFile())) {
			throw new IllegalArgumentException("Fragment's source file does not exist.");
		}
		if(!Files.isRegularFile(fragment.getSrcFile())) {
			throw new IllegalArgumentException("Fragment's source file is not a regular file.");
		}
		if(!Files.isReadable(fragment.getSrcFile())) {
			throw new IllegalArgumentException("Fragment's source file is not readable.");
		}
		if(!fragment.getSrcFile().toAbsolutePath().normalize().startsWith(this.repository.toAbsolutePath().normalize())) {
			throw new IllegalArgumentException("Fragment is not from repository.");
		}
		
		//Configure connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get id to use
		Statement stmt = connection.createStatement();
		String sql = "SELECT nextval('fragments_fragment_id')";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int id = rs.getInt(1);
		
		//Prepare Insert
		String sourcefile = this.repository.toAbsolutePath().normalize().relativize(fragment.getSrcFile().toAbsolutePath().normalize()).toString();
		String fragmentfile = this.fragments.toAbsolutePath().normalize().relativize(this.fragments.resolve("" + id).toAbsolutePath().normalize()).toString();
		sql = "INSERT INTO fragments (fragment_id, fragmentfile, srcfile, startline, endline) VALUES (" + id + ",'" + fragmentfile + "','" + sourcefile + "', " + fragment.getStartLine() + ", " + fragment.getEndLine() +")";
		
		//Execute Insert
		stmt = connection.createStatement();
		int count = stmt.executeUpdate(sql);
		
		assert(count == 1) : "createFragment has a bug.";
		
		//Prepare Return
		FragmentDB retval = getFragment(id);
		
		//Make copy of fragment
		Files.deleteIfExists(this.fragments.resolve("" + id));
		FragmentUtil.extractFragment(fragment, this.fragments.resolve("" + id));
		
		return retval;
	}
	
	/**
	 * Tests if a fragment with the given id exists in the database.
	 * @param id The id of the fragment to check if exists.
	 * @return boolean True if the fragment with the given id exists in the database.
	 * @throws SQLException If a database connection error occurs, or a critical database schema error occurs.
	 */
	public boolean existsFragment(int id) throws SQLException {
		//Prepare SQL
		String sql = "SELECT * FROM fragments WHERE fragment_id = " + id;
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count rows found (should be 0 or 1)
		int num = 0;
		while(rs.next()) {
			num++;
		}
		
		assert(num == 0 || num == 1) : "existsFragment has a bug.  Found multiple entries.";
		
		//Found it, return true
		if (num == 1) {
			return true;
			
		//Found nothing, return false
		} else  {
			return false;
		}
	}
	
	/**
	 * Returns the fragment with the given id, if it exists.
	 * @param id The id of the fragment to retrieve.
	 * @return The fragment, or null if it does not exist.
	 * @throws SQLException If a database connection error occurs, or a critical scheme error occurs.
	 */
	public FragmentDB getFragment(int id) throws SQLException {
		//Prepare SQL
		String sql = "SELECT fragment_id, fragmentfile, srcfile, startline, endline FROM fragments WHERE fragment_id = " + id;
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Found it, return fragment
		if(rs.next()) {
			assert(!rs.next()) : "Bug in getFragment.  Found multiple results.";
			
			id = rs.getInt("fragment_id");
			String srcfile = rs.getString("srcfile");
			srcfile = this.repository.toAbsolutePath().normalize().toString() + "/" + srcfile;
			String fragfile = rs.getString("fragmentfile");
			fragfile = this.fragments.toAbsolutePath().normalize().toString() + "/" + fragfile;
			int sline = rs.getInt("startline");
			int eline = rs.getInt("endline");
			
			return new FragmentDB(id, Paths.get(fragfile), Paths.get(srcfile), sline, eline);
		
		//Did not find it, return null
		} else {	
			return null;
		}
	}
	
	/**
	 * Returns a list of the fragments in the database at time of call, ordered by ascending fragment id.
	 * @return a list of the fragments in the database at time of call, ordered by ascending fragment id.
	 * @throws SQLException If a database connection error occurs.
	 */
	public List<FragmentDB> getFragments() throws SQLException {
		//Prepare SQL
		String sql = "SELECT * FROM fragments ORDER BY fragment_id ASC";
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Populate the list
		List<FragmentDB> retval = new ArrayList<FragmentDB>();
		while(rs.next()) {
			int id = rs.getInt("fragment_id");
			int sline = rs.getInt("startline");
			int eline = rs.getInt("endline");
			String srcfile = rs.getString("srcfile");
			srcfile = this.repository.toAbsolutePath().normalize().toString() + "/" + srcfile;
			String fragfile = rs.getString("fragmentfile");
			fragfile = this.fragments.toAbsolutePath().normalize().toString() + "/" + fragfile;
			retval.add(new FragmentDB(id, Paths.get(fragfile), Paths.get(srcfile), sline, eline));
		}
		
		//Return the list
		return retval;
	}
	
	/**
	 * Returns the number of fragments in the database.
	 * @return the number of fragments in the database.
	 * @throws SQLException If a database connection error occurs.
	 */
	public int numFragments() throws SQLException {
		//Prepare SQL
		String sql = "SELECT * FROM fragments";
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count and return
		int num = 0;
		while(rs.next()) {
			num++;
		}
		return num;
	}
	
	/**
	 * Returns a list of the ids of the fragments in the database.
	 * @return a list of the ids of the fragments in the database.
	 * @throws SQLException If a database connection error occurs.
	 */
	public List<Integer> getFragmentIds() throws SQLException {
		//Prepare SQL
		String sql = "SELECT * FROM fragments";

		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Populate and return
		LinkedList<Integer> retval = new LinkedList<Integer>();
		while(rs.next()) {
			retval.add(rs.getInt("fragment_id"));
		}
		return retval;
	}

	//TODO: Add deleteFragment and deleteFragments (delayed b/c of testing time)
	
//	/**
//	 * Removes the fragment with the specified id from the database.  Also removes all other database elements which depends on this fragment.
//	 * @param id The id of the fragment to remove.
//	 * @return true if the fragment was removed, false otherwise (the specified fragment does not exist).
//	 * @throws SQLException If a database connection error occurs.
//	 * @throws IOException  If error occurs in deleting fragment file.
//	 */
//	public boolean deleteFragment(int id) throws SQLException, IOException {
//		//Prepare Connection
//		if(!connection.getAutoCommit()) {
//			connection.rollback();
//			connection.setAutoCommit(true);
//		}
//		
//		//Prepare SQL
//		String sql = "DELETE FROM fragments WHERE fragment_id = " + id;
//		
//		//Execute Statement
//		Statement stmt = connection.createStatement();
//		int count = stmt.executeUpdate(sql);
//		
//		assert(count == 0 || count == 1) : "Bug in deleteFragment(id).  It deleted multiple rows.";
//		
//		//Delete fragment file
//		if(count == 1) {
//			Files.deleteIfExists(this.fragments.resolve("" + id));
//		}
//		
//		//Return result
//		if(count == 1) {
//			return true;
//		} else {
//			return false;
//		}
//		
//	}
//	
//	/**
//	 * Removes all fragments form the database, also removing any elements which depend on them.
//	 * @return The number of fragments removed.
//	 * @throws SQLException If a database connection error occurs.
//	 */
//	public int deleteFragments() throws SQLException {
//		//Prepare SQL
//		String sql = "DELETE FROM fragments";
//		
//		//Prepare Connection
//		if(!connection.getAutoCommit()) {
//			connection.rollback();
//			connection.setAutoCommit(true);
//		}
//		
//		//Execute Statement
//		Statement stmt = connection.createStatement();
//		int count = stmt.executeUpdate(sql);
//		
//		//Return Result
//		return count;
//	}

// --- MUTANT FRAGMENT ------------------------------------------------------------------------------------------------

	/**
	 * Creates a mutant fragment.  Can only be called during the generation stage.
	 * @param fragment_id
	 * @param mutator_id
	 * @param mutantfile
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 * @throws IllegalStateException Can only be called in the generation stage.
	 */
	public MutantFragment createMutantFragment(int fragment_id, int mutator_id, Path mutantfile) throws SQLException, IOException, IllegalStateException {
		//Check Arguments
		if(this.getStage() != ExperimentData.GENERATION_STAGE) {
			throw new IllegalStateException("Only valid during generation phase");
		}
		Objects.requireNonNull(mutantfile);
		if(!existsFragment(fragment_id)) {
			throw new IllegalArgumentException("fragmentid must refer to a fragment that exists in the database.");
		}	
		if(!existsMutator(mutator_id)) {
			throw new IllegalArgumentException("mutator_id must refer to a mutator that exists in the database.");
		}
		if(!Files.exists(mutantfile)) {
			throw new IllegalArgumentException("mutantfile does not exist.");
		}
		if(!Files.isRegularFile(mutantfile)) {
			throw new IllegalArgumentException("mutantfile is not a regular file.");
		}
		if(!Files.isReadable(mutantfile)) {
			throw new IllegalArgumentException("mutantfile is not readable.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Copy Mutant File
		Statement stmt = connection.createStatement();
		String sql = "SELECT nextval('mutant_fragments_mutant_id')";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int id = rs.getInt(1);
		rs.close();
		
		//Copy Mutant File
		Path mutantfilepath = Files.copy(mutantfile, this.mutantfragments.resolve("" + id), StandardCopyOption.REPLACE_EXISTING);
		mutantfilepath = this.mutantfragments.toAbsolutePath().normalize().relativize(mutantfilepath.toAbsolutePath().normalize());
		
		//Prepare SQL
		sql = "INSERT INTO mutant_fragments (mutant_id, fragment_id, mutator_id, srcfile) VALUES ("  + id + ", " + fragment_id + ", '" + mutator_id + "', '" + mutantfilepath.toString() + "')";

		//Execute Statement
		stmt = connection.createStatement();		
		int count = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		assert(count == 1) : "createMutantFragment failed.";
		
		//Release Resources
		rs.close();
		stmt.close();
		
		//Return
		return getMutantFragment(id);
	}
	
	/**
	 * Returns the mutant fragment with the given id, if it exists.
	 * @param id THe id of the fragment to retrieve.
	 * @return THe fragment, or null if it does not exist.
	 * @throws SQLException
	 */
	public MutantFragment getMutantFragment(int id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_fragments WHERE mutant_id = " + id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Found it, return mutant fragment
		MutantFragment retval;
		if(rs.next()) {
			assert(!rs.next()) : "getMutantFragment failed.";
			
			id = rs.getInt("mutant_id");
			int fragment_id = rs.getInt("fragment_id");
			String srcfile = rs.getString("srcfile");
			srcfile = this.mutantfragments.toAbsolutePath().normalize().toString() + "/" + srcfile;
			int mutator_id = rs.getInt("mutator_id");
			
			retval = new MutantFragment(id, fragment_id, Paths.get(srcfile), mutator_id);
			
		//Did not find it, return null
		} else {
			retval = null;
		}
		
		//Close Resources
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	/**
	 * Tests if the mutant fragment with the given id exists.
	 * @param id the id of the mutaunt to look for.
	 * @return if the mutant framgent with the given id exists.
	 * @throws SQLException
	 */
	public boolean existsMutantFragment(int id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_fragments WHERE mutant_id = " + id;
		
		//Execute statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count rows found (should be 0 or 1)
		int num = 0;
		while(rs.next()) {
			num++;
		}
		assert(num == 1 || num == 0) : "existsMutantFragment failed.";		
		
		//Found it, return is true
		boolean retval;
		if (num == 1) {
			retval = true;
			
		//Found nothing, return is false
		} else {
			retval = false;
		}
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	/**
	 * Returns a list of the mutant fragments in the database at time of call.
	 * @return a list of the mutant fragments in the database at time of call.
	 * @throws SQLException
	 */
	public List<MutantFragment> getMutantFragments() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}		
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_fragments ORDER BY mutant_id ASC";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Populate the list
		List<MutantFragment> retval = new ArrayList<MutantFragment>();
		while(rs.next()) {
			int mutant_id = rs.getInt("mutant_id");
			int fragment_id = rs.getInt("fragment_id");
			String srcfile = rs.getString("srcfile");
			srcfile = this.mutantfragments.toAbsolutePath().normalize().toString() + "/" + srcfile;
			int mutator_id = rs.getInt("mutator_id");
			retval.add(new MutantFragment(mutant_id, fragment_id, Paths.get(srcfile), mutator_id));
		}
		
		//cleanup
		rs.close();
		stmt.close();
		
		//Return the list
		return retval;
	}
	
	/**
	 * Returns a list of the mutant fragments made from the specified fragment in the database at time of call.
	 * @return a list of the mutant fragments made from the specified fragment in the database at time of call.
	 * @throws SQLException Thrown if connection error occurs.
	 */
	public List<MutantFragment> getMutantFragments(int fragment_id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}		
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_fragments WHERE fragment_id = " + fragment_id + " ORDER BY mutant_id ASC";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Populate the list
		List<MutantFragment> retval = new ArrayList<MutantFragment>();
		while(rs.next()) {
			int mutant_id = rs.getInt("mutant_id");
			int rfragment_id = rs.getInt("fragment_id");
			String srcfile = rs.getString("srcfile");
			srcfile = this.mutantfragments.toAbsolutePath().normalize().toString() + "/" + srcfile;
			int mutator_id = rs.getInt("mutator_id");
			retval.add(new MutantFragment(mutant_id, rfragment_id, Paths.get(srcfile), mutator_id));
		}
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return the list
		return retval;
	}
	
	/**
	 * Returns the number of mutant fragments in the database.
	 * @return the number of mutant fragments in the database.
	 * @throws SQLException
	 */
	public int numMutantFragments() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}		
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_fragments";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count and return
		int num = 0;
		while(rs.next()) {
			num++;
		}
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return
		return num;
	}

	/**
	 * Returns a list of the ids opathf the mutant fragments in the database.
	 * @return a list of the ids of the mutant fragments in the database.
	 * @throws SQLException If a database connection error occurs.
	 */
	public List<Integer> getMutantFragmentIds() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_fragments";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Populate and return
		LinkedList<Integer> retval = new LinkedList<Integer>();
		while(rs.next()) {
			retval.add(rs.getInt("mutant_id"));
		}
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	/**
	 * Returns a list of the ids of the mutant fragments in the database which were made from the specified fragment.
	 * @param fragment_id The fragment id.
	 * @return a list of the ids of the mutant fragments in the database which were made from the specified fragment.
	 * @throws SQLException
	 */
	public List<Integer> getMutantFragmentIds(int fragment_id) throws SQLException {
		//Check arguments
		if(!existsFragment(fragment_id)) {
			throw new IllegalArgumentException("Specified fragment does not exist.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_fragments WHERE fragment_id = " + fragment_id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Populate and return
		LinkedList<Integer> retval = new LinkedList<Integer>();
		while(rs.next()) {
			int fid = rs.getInt("fragment_id");
			assert(fid == fragment_id) : "getMutantFragmentIds failed.";
			retval.add(rs.getInt("mutant_id"));
		}
		
		//Close Resources
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	//TODO: DeleteMutantFragment/DeleteMutantFragments
	
//	/**
//	 * Removes the mutant fragment with the given id from the database.  Will cascade to delete any mutant bases, detection
//	 * results, and unit recall/precision that rely on this mutant.
//	 * @param id the id of the mutant to remove.
//	 * @return true if it was removed, false otherwise (mutant fragment does not exist).
//	 * @throws SQLException
//	 */
//	public void deleteMutantFragment(int id) throws SQLException {
//		//Check Conditions
//		if(existsMutantFragment(id)) {
//			throw new IllegalArgumentException("Mutant fragment with id " + id + " does not exist.");
//		}
//		
//		//Prepare SQL
//		String sql = "DELETE FROM mutant_fragments WHERE mutant_id = " + id;
//		
//		//Execute Statement
//		Statement stmt = connection.createStatement();
//		int retval = stmt.executeUpdate(sql);
//		
//		//Check for errors
//		if (retval != 1) {
//			System.out.println("Bug in deleteMutantFragment.  Update count was " + retval + ".");
//			throw new SQLException("Bug in deleteMutantFragment.  Update count was " + retval + ".");
//		}
//	}
//	
//	/**
//	 * Deletes all mutant fragments form the database.  Any data relying on it will also be deleted (bases, detection results, performance metrics)
//	 * @return the number of mutant fragments deleted.
//	 * @throws SQLException If a database connection error occurs.
//	 */
//	public int deleteMutantFragments() throws SQLException {
//		String sql = "DELETE FROM mutant_fragments";
//		Statement stmt = connection.createStatement();
//		return stmt.executeUpdate(sql);
//	}
	
// ---	MUTANT BASE -------------------------------------------------------------------------------------------------------
	
	/**
	 * Creates a mutant base.  Can only be called during the generation stage.
	 * @param original_inject_srcfile
	 * @param original_inject_line
	 * @param mutant_inject_srcfile
	 * @param mutant_inject_line
	 * @param mutant_fragment_id
	 * @return
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalStateException Can only be called during the generation stage.
	 */
	public MutantBaseDB createMutantBase(Path original_inject_srcfile, int original_inject_line, Path mutant_inject_srcfile, int mutant_inject_line, int mutant_fragment_id) throws SQLException, FileNotFoundException, IOException, IllegalStateException {
		if(this.getStage() != ExperimentData.GENERATION_STAGE) {
			throw new IllegalStateException("Only valid during generation phase");
		}
	//Check Arguments
		//Check Object References
		Objects.requireNonNull(original_inject_srcfile);
		Objects.requireNonNull(mutant_inject_srcfile);
		
		//Check Original Fragment injection information
			//check srcfile from system
		if(!original_inject_srcfile.toAbsolutePath().normalize().startsWith(this.getSystemPath().toAbsolutePath().normalize())) {
			throw new IllegalArgumentException("Original inject sourcefile must be from system.");
		}
			//check srcfile exists
		if(!Files.exists(original_inject_srcfile)) {
			throw new IllegalArgumentException("Original inject sourcefile does not exist.");
		}
			//check srcfile is regular file
		if(!Files.isRegularFile(original_inject_srcfile)) {
			throw new IllegalArgumentException("Original inject sourcefile is not a regular file.");
		}
			//check injection location
		int onumlines = FileUtil.countLines(original_inject_srcfile);
		if(original_inject_line > onumlines+1) {
			throw new IllegalArgumentException("Original inject line is invalid.");
		}
		
		//Check Mutant Fragment injection information
			//check srcfile form system
		if(!mutant_inject_srcfile.toAbsolutePath().normalize().startsWith(this.getSystemPath().toAbsolutePath().normalize())) {
			throw new IllegalArgumentException("Mutant inject sourcefile must be in the system.");
		}
			//check srcfile exists
		if(!Files.exists(mutant_inject_srcfile)) {
			throw new IllegalArgumentException("Mutant inject sourcefile does not exist.");
		}
			//check srcfile is regular file
		if(!Files.isRegularFile(mutant_inject_srcfile)) {
			throw new IllegalArgumentException("Mutant inject sourcefile is not a regular file.");
		}
			//check injection location
		int mnumlines = FileUtil.countLines(mutant_inject_srcfile);
		if(mutant_inject_line > mnumlines+1) {
			throw new IllegalArgumentException("Mutant inject line is invalid.");
		}
		
		//Check Mutant Fragment
		if(!existsMutantFragment(mutant_fragment_id)) {
			throw new IllegalArgumentException("Specified mutant fragment does not exist.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		Path osourcepath = subjectsystem.toAbsolutePath().normalize().relativize(original_inject_srcfile.toAbsolutePath().normalize());
		Path msourcepath = subjectsystem.toAbsolutePath().normalize().relativize(mutant_inject_srcfile.toAbsolutePath().normalize());
		
		MutantFragment mf = getMutantFragment(mutant_fragment_id);
		FragmentDB f = getFragment(mf.getFragmentId());
		int fnumlines = FileUtil.countLines(f.getFragmentFile());
		int mfnumlines = FileUtil.countLines(mf.getFragmentFile());
		
		String osourcefile = osourcepath.toString();
		int ostartline = original_inject_line;
		int oendline = ostartline + fnumlines - 1;
		
		String msourcefile = msourcepath.toString();
		int mstartline = mutant_inject_line;
		int mendline = mstartline + mfnumlines - 1;
		
		String sdirectory = mutantsystem.toAbsolutePath().normalize().relativize(mutantsystem.toAbsolutePath().normalize()).toString();
		
		String sql = "INSERT INTO mutant_bases (directory, mutant_id, isrcfile, istartline, iendline, osrcfile, ostartline, oendline) VALUES ("
				+ "'" + sdirectory + "',"
				+ mutant_fragment_id + ",'" 
				+ msourcefile + "'," 
				+ mstartline + "," 
				+ mendline + ","
				+ "'" + osourcefile + "',"
				+ ostartline + ","
				+ oendline +
			")";
		
		//Execute Statement
		connection.setAutoCommit(false);
		Statement stmt = connection.createStatement();
		int count = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		
		//Success, return created mutant base
		if(count == 1) {
			ResultSet rs = stmt.getGeneratedKeys();
			rs.next();
			int id = rs.getInt(1);
			connection.commit();
			connection.setAutoCommit(true);
			return getMutantBase(id);
		} else {
			connection.rollback();
			connection.setAutoCommit(true);
			return null;
		}
		
	}
	
	/**
	 * Retrieves a mutant base from the database.
	 * @param id The id of the mutant base to retrieve.
	 * @return the mutant base with the specified id in the database, or null if it does not exist.
	 * @throws SQLException
	 */
	public MutantBaseDB getMutantBase(int id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_bases WHERE base_id = " + id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Return
		if(rs.next()) {
			int base_id = rs.getInt("base_id");
			String directory = rs.getString("directory");
			Path directorypath = mutantsystem.toAbsolutePath().normalize();
			
			assert(Paths.get(directory).toAbsolutePath().normalize().equals(directorypath));
			
			int mutant_id = rs.getInt("mutant_id");
			String isrcfile = rs.getString("isrcfile");
			Path isrcpath = mutantsystem.toAbsolutePath().normalize().resolve(isrcfile).toAbsolutePath().normalize();
			int istartline = rs.getInt("istartline");
			int iendline = rs.getInt("iendline");
			String osrcfile = rs.getString("osrcfile");
			Path osrcpath = mutantsystem.toAbsolutePath().normalize().resolve(osrcfile).toAbsolutePath().normalize();
			int ostartline = rs.getInt("ostartline");
			int oendline = rs.getInt("oendline");
			return new MutantBaseDB(base_id, directorypath, mutant_id, new Fragment(osrcpath, ostartline, oendline), new Fragment(isrcpath, istartline, iendline));
		} else {
			return null;
		}
	}
	
	/**
	 * Returns a list of the mutant bases in the database at the time of call.
	 * @return a list of the mutant bases in the database at the time of call.
	 * @throws SQLException
	 */
	public List<MutantBaseDB> getMutantBases() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_bases ORDER BY base_id ASC";
		
		//Execute Statement
		List<MutantBaseDB> retval = new ArrayList<MutantBaseDB>();
		Statement stmt = connection.createStatement();
		
		//Collect Results
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			int base_id = rs.getInt("base_id");
			String directory = rs.getString("directory");
			Path directorypath = mutantsystem.toAbsolutePath().normalize();
			
			assert(Paths.get(directory).toAbsolutePath().normalize().equals(directorypath));
			
			int mutant_id = rs.getInt("mutant_id");
			String isrcfile = rs.getString("isrcfile");
			Path isrcpath = mutantsystem.toAbsolutePath().normalize().resolve(isrcfile).toAbsolutePath().normalize();
			int istartline = rs.getInt("istartline");
			int iendline = rs.getInt("iendline");
			String osrcfile = rs.getString("osrcfile");
			Path osrcpath = mutantsystem.toAbsolutePath().normalize().resolve(osrcfile).toAbsolutePath().normalize();
			int ostartline = rs.getInt("ostartline");
			int oendline = rs.getInt("oendline");
			retval.add(new MutantBaseDB(base_id, directorypath, mutant_id, new Fragment(osrcpath, ostartline, oendline), new Fragment(isrcpath, istartline, iendline)));
		}
		
		//Return
		return retval;
	}
	
	/**
	 * Tests if a mutant base with the given id exists in the database.
	 * @param id the id of the mutant base.
	 * @return if the mutant base exists.
	 * @throws SQLException
	 */
	public boolean existsMutantBase(int id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_bases WHERE base_id = " + id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Return
		if(rs.next()) {
			assert(!rs.next());
			return true;
		} else {
			return false;
		}
	}
	
	//TODO: DeleteMutantBase/DeleteMutantBases
	
//	/**
//	 * Removes the mutant base with the specified id from the database.  This deletion will cascade, deleting any
//	 * detection results and perforamcne metrics that tie back to this mutant base.
//	 * @param id the mutant base id.
//	 * @return true if the mutant base was removed, false otherwise (the specified mutant base does not exist).
//	 * @throws SQLException
//	 */
//	public boolean deleteMutantBase(int id) throws SQLException {
//		String sql = "DELETE FROM mutant_bases WHERE base_id = " + id;
//		Statement stmt = connection.createStatement();
//		int retval = stmt.executeUpdate(sql);
//		if(retval == 1) {
//			return true;
//		} else if (retval == 0) {
//			return false;
//		} else {
//			System.out.println("Bug in deleteMutantBase.  It just deleted more than one entry!");
//			throw new SQLException();
//		}
//	}
//	
//	/**
//	 * Removes all mutant bases from the database.  THis will cascade and remove all detection results and detection metrics based on these bases.
//	 * @return the number of mutant bases removed.
//	 * @throws SQLException
//	 */
//	public int deleteMutantBases() throws SQLException {
//		//deleteUnitRecalls();
//		//deleteUnitPrecisions();
//		//deleteReports();
//		String sql = "DELETE FROM mutant_bases";
//		Statement stmt = connection.createStatement();
//		int retval = stmt.executeUpdate(sql);
//		sql = "ALTER SEQUENCE mutant_bases_base_id RESTART WITH 1";
//		stmt.execute(sql);
//		return retval;
//	}
	
	/**
	 * Returns the number of mutant bases in the database.
	 * @return the number of mutant bases in the database.
	 * @throws SQLException
	 */
	public int numMutantBases() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_bases";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count
		int i = 0;
		while(rs.next()) {
			i++;
		}
		
		//Return
		return i;
	}
	
	/**
	 * Returns a list of the ids of the bases in the database.
	 * @return a list of the ids of the bases in the database.
	 * @throws SQLException
	 */
	public List<Integer> getMutantBaseIds() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutant_bases";
		
		//Execute Statement
		LinkedList<Integer> retval = new LinkedList<Integer>();
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Collect Ids
		while(rs.next()) {
			retval.add(rs.getInt("base_id"));
		}
		
		//Return
		return retval;
	}

// TOOLS	
	/**
	 * Adds a tool to the database with the specified values.  Can only be called during the evaluation setup stage.
	 * @param name The name of the tool.
	 * @param description A description of the tool.  Must have installation directory as a parent.
	 * @param directory The location of the tool.
	 * @return The tool.
	 * @throws SQLException If connection is lost.
	 * @throws IllegalArgumentException If not a directory.
	 * @throws IllegalStateException Can only be called during the evaluation setup stage.
	 */
	public ToolDB createTool(String name, String description, Path directory, Path toolRunner) throws IllegalArgumentException, IllegalStateException, SQLException {
		if(this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalArgumentException("Tools can only be added during the evaluation setup stage.");
		}
		
		//Check inputs
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(directory);
		Objects.requireNonNull(toolRunner);
		if(!Files.exists(directory)) {
			throw new IllegalArgumentException("Directory does not exist.");
		}
		if(!Files.exists(toolRunner)) {
			throw new IllegalArgumentException("ToolRunner does not exist.");
		}
		if(!Files.isDirectory(directory)) {
			throw new IllegalArgumentException("Directory does not denote a directory.");
		}
		if(!Files.isRegularFile(toolRunner)) {
			throw new IllegalArgumentException("ToolRunner does not denote a file.");
		}
		if(!Files.isReadable(toolRunner)) {
			throw new IllegalArgumentException("ToolRunner is not readable.");
		}
		if(!Files.isExecutable(toolRunner)) {
			throw new IllegalArgumentException("ToolRunner is not executable.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}		

		//Prepare SQL
		String sql = "INSERT INTO tools (name, directory, description, toolrunner) VALUES ('" + name + "', '" + directory.toAbsolutePath().normalize().toString() + "', '" + description + "','" + toolRunner.toAbsolutePath().normalize().toString() + "')";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		
		//Return tool
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int tool_id = rs.getInt(1);
		return getTool(tool_id);
	}
	
	/**
	 * Retrieves the tool with the specified id from the database.
	 * @param id the id of the tool to retrieve.
	 * @return the tool with the given id, or null if it does not exist in the database.
	 * @throws SQLException
	 */
	public ToolDB getTool(int id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}		
		
		//Prepare SQL
		String sql = "SELECT * FROM tools WHERE tool_id = " + id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Process ResultSet 
		if(rs.next()) {
			//Found, return tool
			int tool_id = rs.getInt("tool_id");
			String name = rs.getString("name");
			String directory = rs.getString("directory");
			String description = rs.getString("description");
			String toolrunner = rs.getString("toolrunner");
			return new ToolDB(tool_id, name, description, Paths.get(directory), Paths.get(toolrunner));
		} else {
			//Did not find, return null
			return null;
		}
	}

	/**
	 * Returns a collection of the tools in the database.
	 * @return a collection of the tools in the database.
	 * @throws SQLException
	 */
	public List<ToolDB> getTools() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM tools ORDER BY tool_id ASC";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Collect Tools
		List<ToolDB> tools = new ArrayList<ToolDB>();
		while(rs.next()) {
			int tool_id = rs.getInt("tool_id");
			String name = rs.getString("name");
			String directory = rs.getString("directory");
			String description = rs.getString("description");
			String toolrunner = rs.getString("toolrunner");
			tools.add(new ToolDB(tool_id, name, description, Paths.get(directory), Paths.get(toolrunner)));
		}
		
		//Return
		return tools;
	}
	
	/**
	 * Tests if a tool with the given id exists in the database.
	 * @param id the id of the tool.
	 * @return boolean if the tool exists.
	 * @throws SQLException
	 */
	public boolean existsTool(int id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM tools WHERE tool_id = " + id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Return if found
		if(rs.next()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes the tool with the specified id from the database.  This deletion will cascade, deleting any
	 * detection results or performance metrics associated with the tool.  Can only be called duing the
	 * evaluation setup stage.
	 * @param id the tool id.
	 * @return true if the tool is removed, or false if it fails (tool id does not exist).
	 * @throws SQLException
	 * @throws IOException 
	 * @throws IllegalStateException T
	 */
	public boolean deleteTool(int id) throws SQLException, IOException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalArgumentException("Tools can only be deleted in the evaluation setup stage.");
		}
		if(!existsTool(id)) {
			return false;
		} else {
			//Remove Dependent Unit Recalls
			List<UnitRecall> urs = getUnitRecallsByTool(id);
			for(UnitRecall ur : urs) {
				this.deleteUnitRecall(ur.getToolid(), ur.getBaseid());
			}
			
			//Remove Dependent Clone Detection Reports
			List<CloneDetectionReportDB> cdrs = getCloneDetectionReportsByToolId(id);
			for(CloneDetectionReportDB cdr : cdrs) {
				this.deleteCloneDetectionReport(cdr.getToolId(), cdr.getBaseId());
			}
			
			//Remove Dependent Unit Precisions
			List<UnitPrecision> ups = getUnitPrecisionForTool(id);
			for(UnitPrecision up : ups) {
				this.deleteUnitPrecision(up.getToolid(), up.baseid);
			}
			
			//Remove Tool
			String sql = "DELETE FROM tools WHERE tool_id = " + id;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			
			return true;
		}
	}
	
	/**
	 * Removes all tools from the database.  This deletion will cascade, deleting any
	 * detection results or performance metrics associated with the tool.  Can only
	 * be called during the evaluation setup stage.
	 * @return the number of tools removed.
	 * @throws SQLException
	 * @throws IOException 
	 */
	public int deleteTools() throws SQLException, IOException {
		if(this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalArgumentException("Tools can only be deleted in the evaluation setup stage.");
		}
		int retval = 0;
		List<ToolDB> tools = getTools();
		for(ToolDB tool : tools) {
			deleteTool(tool.getId());
			retval++;
		}
		return retval;
	}
	
	/**
	 * Returns the number of tools in the database.
	 * @return the number of tools in the database.
	 * @throws SQLException
	 */
	public int numTools() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM tools";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count tools
		int i = 0;
		while(rs.next()) {
			i++;
		}
		
		//Return
		return i;
	}
	
	/**
	 * Returns a list of the ids of the tools in the database.
	 * @return a list of the ids of the tools in the database.
	 * @throws SQLException
	 */
	public List<Integer> getToolIds() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM tools";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Collect Tools
		LinkedList<Integer> retval = new LinkedList<Integer>();
		while(rs.next()) {
			retval.add(rs.getInt("tool_id"));
		}
		
		//Return
		return retval;
	}
	
// --- Clone Reports
	/**
	 * Adds a clone detection report to the database.  Can only be called during the evaluation stage.
	 * @param toolid The tool which produced this report.
	 * @param baseid The mutant base this report is of.
	 * @param report The location of the report.  Must have the installation directory as a parent.
	 * @return 1 if successful, 0 if failed.  Failure will occur if the toolid or baseid are not known in the database.
	 * @throws SQLException If connection is lost.
	 * @throws IOException If fail to copy report to output directory.
	 * @throws FileNotFoundException If report can not be found.
	 * @throws IllegalArgumentException If report does not have the installation directory as a parent or if the report is not a file or does not exist.
	 * @throws IllegalStateException Can only be called during the evaluation stage.
	 */
	public CloneDetectionReportDB createCloneDetectionReport(int toolid, int baseid, Path report) throws SQLException, InputMismatchException, IOException, FileNotFoundException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		//Check Arguments
		Objects.requireNonNull(report);
		if(!this.existsTool(toolid)) {
			throw new IllegalArgumentException("Tool with id " + toolid + " does not exist in the database.");
		}
		if(!this.existsMutantBase(baseid)) {
			throw new IllegalArgumentException("Mutant base with id " + baseid + " does not exist in the database..");
		}
		if(!Files.exists(report)) {
			throw new FileNotFoundException("Report does not exist.");
		}
		if(!Files.isRegularFile(report)) {
			throw new IllegalArgumentException("Report is not a regular file.");
		}
		if(!Files.isReadable(report)) {
			throw new IllegalArgumentException("Report is not readable.");
		}
		if(this.existsCloneDetectionReport(toolid, baseid)) {
			throw new IllegalArgumentException("Clone detection report already exists.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Check/Normalize Report
		CloneDetectionReport cdr = new CloneDetectionReport(report);
		Path cdr_norm = Files.createTempFile(getTemporaryPath(), "EvaluateTools", "NormCheckReport");
		Clone c;
		Path mb_path = getMutantBasePath().toAbsolutePath().normalize();
		cdr.open();
		PrintWriter pr = new PrintWriter(new FileWriter(cdr_norm.toFile()));
		while((c = cdr.next()) != null) {
			Path p1o = c.getFragment1().getSrcFile().toAbsolutePath().normalize();
			Path p2o = c.getFragment2().getSrcFile().toAbsolutePath().normalize();
			
			//If clone src paths are not from mutant base, skip
			if(!p1o.startsWith(mb_path)) {
				continue;
			}
			if(!p2o.startsWith(mb_path)) {
				continue;
			}
			
			Path p1 = mb_path.relativize(p1o).normalize();
			int sl1 = c.getFragment1().getStartLine();
			int el1 = c.getFragment1().getEndLine();
			Path p2 = mb_path.relativize(p2o).normalize();
			int sl2 = c.getFragment2().getStartLine();
			int el2 = c.getFragment2().getEndLine();
			
			pr.println(p1.toString() + "," + sl1 + "," + el1 + "," + p2.toString() + "," + sl2 + "," + el2);
		}
		pr.flush();
		cdr.close();
		pr.close();
		
		//Copy File
		Files.copy(cdr_norm, reports.resolve(toolid + "-" + baseid), StandardCopyOption.REPLACE_EXISTING);
		Files.delete(cdr_norm);
		
		//Prepare SQL
		String sql = "INSERT INTO clone_detection_reports (tool_id, base_id, report) VALUES ("
				+ toolid + "," 
				+ baseid + ","
				+ "'" + toolid + "-" + baseid + "'" + ")";
		
		//Execute Statement
		int retval = connection.createStatement().executeUpdate(sql);
		assert(retval == 1);
		
		//Return
		return getCloneDetectionReport(toolid, baseid);
	}
	
	/**
	 * Retrieves a clone detection report from the database.
	 * @param toolid The tool id of the detection report.
	 * @param baseid The base id of the detection report.
	 * @return The detection report, or null if a detection report with the specified tool/base id does not exist in the database.
	 * @throws SQLException If connection fails.
	 */
	public CloneDetectionReportDB getCloneDetectionReport(int toolid, int baseid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM clone_detection_reports WHERE tool_id = " + toolid + " AND base_id = " + baseid;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Return
		if(rs.next()) {
			assert(!rs.next());
			int tool_id = rs.getInt("tool_id");
			int base_id = rs.getInt("base_id");
			Path report = reports.resolve(toolid + "-" + baseid).toAbsolutePath().normalize();
			return new CloneDetectionReportDB(tool_id, base_id, report, getMutantBasePath().toAbsolutePath().normalize());
		} else {
			return null;
		}
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<CloneDetectionReportDB> getCloneDetectionReports() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM clone_detection_reports";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Create List
		List<CloneDetectionReportDB> retval = new ArrayList<CloneDetectionReportDB>();
		while(rs.next()) {
			int tool_id = rs.getInt("tool_id");
			int base_id = rs.getInt("base_id");
			Path report = reports.resolve(tool_id + "-" + base_id).toAbsolutePath().normalize();
			CloneDetectionReportDB cdr = new CloneDetectionReportDB(tool_id, base_id, report, getMutantBasePath().toAbsolutePath().normalize());
			retval.add(cdr);
		}
		
		//Return
		return retval;
	}
	
	/**
	 * 
	 * @param toolid
	 * @return
	 * @throws SQLException
	 */
	public List<CloneDetectionReportDB> getCloneDetectionReportsByToolId(int toolid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM clone_detection_reports WHERE tool_id = " + toolid;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Create List
		List<CloneDetectionReportDB> retval = new ArrayList<CloneDetectionReportDB>();
		while(rs.next()) {
			int tool_id = rs.getInt("tool_id");
			assert(toolid == tool_id);
			int base_id = rs.getInt("base_id");
			Path report = reports.resolve(tool_id + "-" + base_id).toAbsolutePath().normalize();
			CloneDetectionReportDB cdr = new CloneDetectionReportDB(tool_id, base_id, report, getMutantBasePath().toAbsolutePath().normalize());
			retval.add(cdr);
		}
		
		//Return
		return retval;
	}
	
	/**
	 * 
	 * @param baseid
	 * @return
	 * @throws SQLException
	 */
	public List<CloneDetectionReportDB> getCloneDetectionReportsByBaseId(int baseid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM clone_detection_reports WHERE base_id = " + baseid;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Create List
		List<CloneDetectionReportDB> retval = new ArrayList<CloneDetectionReportDB>();
		while(rs.next()) {
			int tool_id = rs.getInt("tool_id");
			int base_id = rs.getInt("base_id");
			assert(baseid == base_id);
			Path report = reports.resolve(tool_id + "-" + base_id).toAbsolutePath().normalize();
			CloneDetectionReportDB cdr = new CloneDetectionReportDB(tool_id, base_id, report, getMutantBasePath().toAbsolutePath().normalize());
			retval.add(cdr);
		}
		
		//Return
		return retval;
	}
	
	/**
	 * Checks if a clone detection report exists in the database.
	 * @param toolid the tool id of the detection report.
	 * @param baseid THe base id of the detection report.
	 * @return If the clone detection report exists in the database.
	 * @throws SQLException
	 */
	public boolean existsCloneDetectionReport(int toolid, int baseid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM clone_detection_reports WHERE tool_id = " + toolid + " AND base_id = " + baseid;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Return if found
		if(rs.next()) {
			assert(!rs.next());
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes a clone detection report from the database. Can only be called during the evaluation stage.
	 * @param toolid The tool id of the detection report.
	 * @param baseid The base id of the detection report.
	 * @return 1 if successful, 0 if failed.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws IllegalStateException Can only be called during the evaluation stage.
	 */
	public boolean deleteCloneDetectionReport(int toolid, int baseid) throws SQLException, IOException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE && this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Handle Files
		Files.deleteIfExists(reports.resolve(toolid + "-" + baseid));
		
		//Prepare SQL
		String sql = "DELETE FROM clone_detection_reports WHERE tool_id = " + toolid + " AND base_id = " + baseid;
		
		//Execute SQL
		Statement stmt = connection.createStatement();
		int retval = stmt.executeUpdate(sql);
		assert(retval == 0 || retval == 1);
		
		//Return
		if(retval == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Removes all clone detection reports from the database which are from the specified tool.
	 * Can only be called during the evaluation stage.
	 * @param toolid The id of the tool's whose detection reports are to be deleted.
	 * @return The number of detection reports removed.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws IllegalStateException Can only be called during the evaluation stage.
	 */
	public int deleteCloneDetectionReportsByToolId(int toolid) throws SQLException, IOException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE && this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage and evaluation setup stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Delete Files
		List<CloneDetectionReportDB> reports = getCloneDetectionReportsByToolId(toolid);
		for(CloneDetectionReportDB cdr : reports) {
			Files.deleteIfExists(cdr.getReport());
		}
		
		//Prepare SQL
		String sql = "DELETE FROM clone_Detection_reports WHERE tool_id = " + toolid;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		int retval = stmt.executeUpdate(sql);
		
		//Return number deleted
		return retval;
	}
	
	/**
	 * Removes all clone detection reports from the database which are from the specified tool.
	 * Can only be called during the evaluation stage.
	 * @param toolid The id of the tool's whose detection reports are to be deleted.
	 * @return The number of detection reports removed.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws IllegalStateException Can only be called during the evaluation stage.
	 */
	public int deleteCloneDetectionReportsByBaseId(int baseid) throws SQLException, IOException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Delete Files
		List<CloneDetectionReportDB> reports = getCloneDetectionReportsByBaseId(baseid);
		for(CloneDetectionReportDB cdr : reports) {
			Files.deleteIfExists(cdr.getReport());
		}
		
		//Prepare SQL
		String sql = "DELETE FROM clone_Detection_reports WHERE base_id = " + baseid;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		int retval = stmt.executeUpdate(sql);
		
		//Return number deleted
		return retval;
	}
	
	/**
	 * Removes all clone detection reports from the database.
	 * Can only be called during the evaluation stage.
	 * @return The number of detection reports removed.
	 * @throws SQLException
	 * @throws IOException 
	 * @throws IllegalStateException
	 */
	public int deleteCloneDetectionReports() throws SQLException, IOException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Delete Files
		List<CloneDetectionReportDB> reports = getCloneDetectionReports();
		for(CloneDetectionReportDB cdr : reports) {
			Files.deleteIfExists(cdr.getReport());
		}
		
		//Prepare SQL
		String sql = "DELETE FROM clone_detection_reports";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		int retval = stmt.executeUpdate(sql);
		
		//Return
		return retval;
	}
	
	/**
	 * Returns the number of reports in the database created by the specified tool.
	 * @param toolid The id of the tool.
	 * @return the number of reports in the database produced by the tool.
	 * @throws SQLException
	 */
	public int numCloneDetectionReports(int toolid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM clone_detection_reports WHERE tool_id = " + toolid;
		
		//Execute
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count
		int retval = 0;
		while(rs.next()) {
			retval++;
		}
		
		//Return
		return retval;
	}
	
	/**
	 * REturns the number of reports in the database.
	 * @return the number of reports.
	 * @throws SQLException
	 */
	public int numCloneDetectionReports() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM clone_detection_reports";	
		
		//Execute
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count
		int retval = 0;
		while(rs.next()) {
			retval++;
		}
		//Return
		return retval;
	}
	
//-- RECALL -----------------------------------------------------------------------------------------------------------

	public UnitRecall createUnitRecall(int toolid, int baseid, double recall, Clone c) throws SQLException, NullPointerException, IllegalArgumentException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		
		//Check Arguments
		if(recall != 0.0) {
			Objects.requireNonNull(c);
		}
		if(recall > 1.0 || recall < 0.0) {
			throw new IllegalArgumentException("Recall must be a value in the range 0.0, 1.0].");
		}
		if(!this.existsTool(toolid)) {
			throw new IllegalArgumentException("Tool does not exist in database with id " + toolid + ".");
		}
		if(!this.existsMutantBase(baseid)) {
			throw new IllegalArgumentException("Mutant base does not exist in database with id " + baseid + ".");
		}
		if(this.existsUnitRecall(toolid, baseid)) {
			throw new IllegalArgumentException("UnitRecall already exists.");
		}
		if(c != null) {
			if(!c.getFragment1().getSrcFile().toAbsolutePath().normalize().startsWith(mutantsystem.toAbsolutePath().normalize())) {
				throw new IllegalArgumentException("Fragment1 of clone is not from mutant base.");
			}
			if(!c.getFragment2().getSrcFile().toAbsolutePath().normalize().startsWith(mutantsystem.toAbsolutePath().normalize())) {
				throw new IllegalArgumentException("Fragment2 of clone is not from mutant base.");
			}
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql;
		if(c != null) {
			Path srcfile1 = mutantsystem.toAbsolutePath().normalize().relativize(c.getFragment1().getSrcFile().toAbsolutePath().normalize());
			Path srcfile2 = mutantsystem.toAbsolutePath().normalize().relativize(c.getFragment2().getSrcFile().toAbsolutePath().normalize());
			sql = "INSERT INTO unit_recall (tool_id, base_id, recall, srcfile1, startline1, endline1, srcfile2, startline2, endline2) VALUES ("
					+ toolid + ","
					+ baseid + ","
					+ recall + ","
					+ "'" + srcfile1.toString() + "',"
					+ c.getFragment1().getStartLine() + ","
					+ c.getFragment1().getEndLine() + ","
					+ "'" + srcfile2.toString() + "',"
					+ c.getFragment2().getStartLine() + ","
					+ c.getFragment2().getEndLine()
				+ ")";
		} else {
			sql = "INSERT INTO unit_recall (tool_id, base_id, recall) VALUES ("
					+ toolid + ","
					+ baseid + ","
					+ recall
				+ ")";
		}
		
		//Execute SQL
		int retval = connection.createStatement().executeUpdate(sql);
		assert(retval == 1);
		
		//Return
		return getUnitRecall(toolid, baseid);
	}
		
	/**
	 * Retrieves the unit recall value for a specified detection tool and mutant base.
	 * @param toolid
	 * @param baseid
	 * @return the unit recall.  Negative if the specified unit recall does not exist.
	 * @throws SQLException
	 */
	public UnitRecall getUnitRecall(int toolid, int baseid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM unit_recall WHERE tool_id = " + toolid + " AND base_id = " + baseid;
		
		//Execute 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Return
		if(rs.next()) {
			assert(!rs.next());
			int tid = rs.getInt("tool_id");
			int bid = rs.getInt("base_id");
			double recall = rs.getDouble("recall");
			
			
			
			Clone clone;
			rs.getInt("startline1");
			if(rs.wasNull()) { //if startline1 was null, all clone details were null (database spec)
				clone = null;
				assert(recall == 0.0);

			} else { //else none are null
				int startline1 = rs.getInt("startline1");
				Path srcfile1 = mutantsystem.resolve(rs.getString("srcfile1"));
				int endline1 = rs.getInt("endline1");
				Path srcfile2 = mutantsystem.resolve(rs.getString("srcfile2"));
				int startline2 = rs.getInt("startline2");
				int endline2 = rs.getInt("endline2");
				clone = new Clone(new Fragment(srcfile1,startline1,endline1), new Fragment(srcfile2,startline2,endline2));
			}
			
			return new UnitRecall(tid, bid, recall, clone);
		} else {
			return null;
		}
	}
	
	public List<UnitRecall> getUnitRecalls() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM unit_recall";
		
		//Execute 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Create list
		List<UnitRecall> retval = getUnitRecallsFromResultSet_HelperFunction(rs);
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	/**
	 * Returns the UnitRecall objects in the database for the specified mutant base.
	 * @param baseid
	 * @return
	 * @throws SQLException
	 */
	public List<UnitRecall> getUnitRecallsByBase(int baseid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM unit_recall WHERE base_id = " + baseid;
		
		//Execute 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Create list
		List<UnitRecall> retval = getUnitRecallsFromResultSet_HelperFunction(rs);
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	/**
	 * Returns the UnitRecall objects in the database for the specified tool.
	 * @param toolid
	 * @return
	 * @throws SQLException
	 */
	public List<UnitRecall> getUnitRecallsByTool(int toolid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM unit_recall WHERE tool_id = " + toolid;
		
		//Execute 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Create list
		List<UnitRecall> retval = getUnitRecallsFromResultSet_HelperFunction(rs);
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	public List<UnitRecall> getUnitRecallsByToolAndMutator(int toolid, int mutatorid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "" +
				"SELECT ur.tool_id, ur.base_id, ur.recall, ur.srcfile1, ur.startline1, ur.endline1, ur.srcfile2, ur.startline2, ur.endline2 " +
				"FROM mutant_bases mb, mutant_fragments mf, mutators mo, unit_recall ur " +
				"WHERE mb.mutant_id = mf.mutant_id AND " +
				      "mf.mutator_id = mo.mutator_id AND " +
				      "ur.base_id = mb.base_id AND " +
				      "tool_id = " + toolid + " AND " +
				      "mo.mutator_id = '" + mutatorid + "'";
		
		//Execute 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Create list
		List<UnitRecall> retval = getUnitRecallsFromResultSet_HelperFunction(rs);
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	/**
	 * Helper function for ExperimentData.  Takes a result set of any query on the UnitRecall table that includes all fields and returns
	 * a list of UnitRecall objects.  Does not close the result set.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<UnitRecall> getUnitRecallsFromResultSet_HelperFunction(ResultSet rs) throws SQLException {
		List<UnitRecall> retval = new ArrayList<UnitRecall>();
		while(rs.next()) {
			int tid = rs.getInt("tool_id");
			int bid = rs.getInt("base_id");
			double recall = rs.getDouble("recall");

			Clone clone;
			rs.getInt("startline1");
			if(rs.wasNull()) { //if startline1 was null, all clone details were null (database spec)
				clone = null;
				assert(recall == 0.0);

			} else {
				int startline1 = rs.getInt("startline1");
				Path srcfile1 = mutantsystem.resolve(rs.getString("srcfile1"));
				int endline1 = rs.getInt("endline1");
				Path srcfile2 = mutantsystem.resolve(rs.getString("srcfile2"));
				int startline2 = rs.getInt("startline2");
				int endline2 = rs.getInt("endline2");
				clone = new Clone(new Fragment(srcfile1,startline1,endline1), new Fragment(srcfile2,startline2,endline2));
			}
			retval.add(new UnitRecall(tid, bid, recall, clone));
		}
		return retval;
	}
	
	/**
	 * Returns if a unit recall value exists for the specified tool and mutant base.
	 * @param toolid the tool.
	 * @param baseid the mutant base.
	 * @return if a unit recall value exists for the specified tool and mutant base.
	 * @throws SQLException
	 */
	public boolean existsUnitRecall(int toolid, int baseid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM unit_recall WHERE tool_id = " + toolid + " AND base_id = " + baseid;
		
		//Execute SQL
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Return
		if(rs.next()) {
			assert(!rs.next());
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Deletes a unit recall entry for specific tool and base.
	 * @param toolid the id of the tool.
	 * @param baseid the id of the base.
	 * @return 1 if removed, 0 if entry does not exist.
	 * @throws SQLException
	 * @throws IllegalStateException
	 */
	public boolean deleteUnitRecall(int toolid, int baseid) throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		
		//Check Arguments
		if(!this.existsTool(toolid)) {
			throw new IllegalArgumentException("Tool does not exist.");
		}
		if(!this.existsMutantBase(baseid)) {
			throw new IllegalArgumentException("MutantBase does not exist.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "DELETE FROM unit_recall WHERE tool_id = " + toolid + " AND base_id = " + baseid;
		
		//Execute
		Statement stmt = connection.createStatement();
		int num = stmt.executeUpdate(sql);
		assert(num == 0 || num == 1);
		
		//Return
		if(num == 1) {
			return true;
		} else {
			return false;
		}
	}
		
	/**
	 * Deletes all unit recalls from the database.
	 * @return the number of entries removed.
	 * @throws SQLException
	 * @throws IllegalStateException
	 */
	public int deleteUnitRecalls() throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE && this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage or evaluation setup stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "DELETE FROM unit_recall";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		int num = stmt.executeUpdate(sql);
		
		//Return
		return num;
	}
	
	public int deleteUnitRecallsByTool(int tool_id) throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE && this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage or evaluation setup stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "DELETE FROM unit_recall WHERE tool_id = " + tool_id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		int num = stmt.executeUpdate(sql);
		
		//Return
		return num;
	}
	
	public int deleteUnitRecallsByBase(int base_id) throws SQLException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "DELETE FROM unit_recall WHERE base_id = " + base_id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		int num = stmt.executeUpdate(sql);
		
		//Return
		return num;
	}
	
// PRECISION ---------------------------------------------------------------------------------------------------------------------------------------------------------
	
	/**
	 * Adds a unit precision value to the database for the specified tool and base.
	 * @param toolid the id of the tool.
	 * @param baseid the id of the base.
	 * @param precision the precision value.
	 * @return 1 if successful, 0 if fail.
	 * @throws SQLException
	 */
	public UnitPrecision createUnitPrecision(int toolid, int baseid, double precision, List<VerifiedClone> clones) throws SQLException, IllegalArgumentException, NullPointerException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		
		//Check Argumetns
		Objects.requireNonNull(clones);
		if(!existsTool(toolid)) {
			throw new IllegalArgumentException("Tool does not exist with id " + toolid + ".");
		}
		if(!existsMutantBase(baseid)) {
			throw new IllegalArgumentException("Mutant Base does not exist with id " + baseid + ".");
		}
		if(existsUnitPrecision(toolid, baseid)) {
			throw new IllegalArgumentException("UnitPrecision already exists.");
		}
		if(clones.size() < 1 && precision < 1.0) {
			throw new IllegalArgumentException("Clones list is empty when precision is not 1.0.");
		}
		for(VerifiedClone vc : clones) {
			if(!vc.getFragment1().getSrcFile().toAbsolutePath().normalize().startsWith(mutantsystem.toAbsolutePath().normalize())) {
				throw new IllegalArgumentException("One of the clones's first fragment is not form mutant base.");
			}
			if(!vc.getFragment2().getSrcFile().toAbsolutePath().normalize().startsWith(mutantsystem.toAbsolutePath().normalize())) {
				throw new IllegalArgumentException("One of the clones's first fragment is not form mutant base.");
			}
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Insert unit_precision header
		String sql = "INSERT INTO unit_precision (tool_id, base_id, precision) VALUES ("
				+ toolid + ","
				+ baseid + ","
				+ precision + ")";
		int retval = connection.createStatement().executeUpdate(sql);
		assert(retval == 1);
		
		//Insert Verified Clones
		for(VerifiedClone vc : clones) {
			Path srcfile1 = mutantsystem.toAbsolutePath().normalize().relativize(vc.getFragment1().getSrcFile().toAbsolutePath().normalize());
			Path srcfile2 = mutantsystem.toAbsolutePath().normalize().relativize(vc.getFragment2().getSrcFile().toAbsolutePath().normalize());
			sql = "INSERT INTO unit_precision_clones_considered (tool_id, base_id, isclone, verifiersuccess, srcfile1, startline1, endline1, srcfile2, startline2, endline2) VALUES ("
					+ toolid + ","
					+ baseid + ","
					+ vc.isClone() + ","
					+ vc.isVerifierSuccess() + ","
					+ "'" + srcfile1.toString() + "'" + ","
					+ vc.getFragment1().getStartLine() + ","
					+ vc.getFragment1().getEndLine() + ","
					+ "'" + srcfile2.toString() + "'" + ","
					+ vc.getFragment2().getStartLine() + ","
					+ vc.getFragment2().getEndLine() 
				+ ")";
			connection.createStatement().executeUpdate(sql);
		}
		
		//return
		return getUnitPrecision(toolid, baseid);
	}
	
	/**
	 * Retrieves the unit precision value for a specified detection tool and mutant base.
	 * @param toolid
	 * @param baseid
	 * @return the unit precision.  Negative if the specified unit recall does not exist.
	 * @throws SQLException
	 */
	public UnitPrecision getUnitPrecision(int toolid, int baseid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get Unit Precision
		String sql = "SELECT * FROM unit_precision WHERE tool_id = " + toolid + " AND base_id = " + baseid;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			int tool_id  = rs.getInt("tool_id");
			int base_id = rs.getInt("base_id");
			
			assert(tool_id == toolid);
			assert(base_id == baseid);
			
			double precision = rs.getDouble("precision");
			rs.close();
			stmt.close();
			
			
			//Get Verified Clones
			List<VerifiedClone> clones = new LinkedList<VerifiedClone>();
			sql = "SELECT * FROM unit_precision_clones_considered WHERE tool_id = " + toolid + " AND base_id = " + baseid;
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				int tool_idv = rs.getInt("tool_id");
				int base_idv = rs.getInt("base_id");
				
				assert(toolid == tool_idv);
				assert(base_idv == baseid);
				
				boolean isclone = rs.getBoolean("isclone");
				boolean verifiersuccess = rs.getBoolean("verifiersuccess");
				Path srcfile1 = mutantsystem.resolve(rs.getString("srcfile1")).toAbsolutePath().normalize();
				int startline1 = rs.getInt("startline1");
				int endline1 = rs.getInt("endline1");
				Path srcfile2 = mutantsystem.resolve(rs.getString("srcfile2")).toAbsolutePath().normalize();
				int startline2 = rs.getInt("startline2");
				int endline2 = rs.getInt("endline2");
				clones.add(new VerifiedClone(new Fragment(srcfile1, startline1, endline1), new Fragment(srcfile2, startline2, endline2), isclone, verifiersuccess));
			}
			rs.close();
			stmt.close();
			
			return new UnitPrecision(toolid, baseid, precision, clones);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns if a unit precision value exists for the specified tool and mutant base.
	 * @param toolid the tool.
	 * @param baseid the mutant base.
	 * @return if a unit recall value exists for the specified tool and mutant base.
	 * @throws SQLException
	 */
	public boolean existsUnitPrecision(int toolid, int baseid) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM unit_precision WHERE tool_id = " + toolid + " AND base_id = " + baseid;
		
		//Execute
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Return
		if(rs.next()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @param toolid
	 * @param baseid
	 * @return
	 * @throws SQLException
	 */
	public boolean deleteUnitPrecision(int toolid, int baseid) throws SQLException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		if(existsUnitPrecision(toolid, baseid)) {
			//Prepare SQL
			String sql1 = "DELETE FROM unit_precision_clones_considered WHERE tool_id = " + toolid + " AND base_id = " + baseid;
			String sql2 = "DELETE FROM unit_precision WHERE tool_id = " + toolid + " AND base_id = " + baseid;
			
			//Execute
			Statement stmt = connection.createStatement();
			int count;
			count = stmt.executeUpdate(sql1);
			count = stmt.executeUpdate(sql2);
			assert(count == 1);
			stmt.close();
			
			//Return
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Deletes all unit recalls from the database.
	 * @return the number of entries removed.
	 * @throws SQLException
	 */
	public int deleteUnitPrecisions(int tool_id) throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE && this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage and evaluation setup stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//prepare sql
		String sql1 = "DELETE FROM unit_precision_clones_considered WHERE tool_id = " + tool_id;
		String sql2 = "DELETE FROM unit_precision WHERE tool_id = " + tool_id;
		
		//execute
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql1);
		int num = stmt.executeUpdate(sql2);
		
		//return
		return num;
	}
	
	/**
	 * Deletes all unit recalls from the database.
	 * @return the number of entries removed.
	 * @throws SQLException
	 */
	public int deleteUnitPrecisions() throws SQLException {
		if(this.getStage() != ExperimentData.EVALUATION_STAGE && this.getStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during evaluation stage and evaluation setup stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//prepare sql
		String sql1 = "DELETE FROM unit_precision_clones_considered";
		String sql2 = "DELETE FROM unit_precision";
		
		//execute
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql1);
		int num = stmt.executeUpdate(sql2);
		
		//return
		return num;
	}
	
	public List<UnitPrecision> getUnitPrecisions() throws SQLException, IllegalArgumentException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get Unit Precision
		String sql = "SELECT * FROM unit_precision";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		List<UnitPrecision> retval = new LinkedList<UnitPrecision>();
		while(rs.next()) {
			int tool_id  = rs.getInt("tool_id");
			int base_id = rs.getInt("base_id");
					
			double precision = rs.getDouble("precision");
					
					
			//Get Verified Clones
			List<VerifiedClone> clones = new LinkedList<VerifiedClone>();
			sql = "SELECT * FROM unit_precision_clones_considered WHERE tool_id = " + tool_id + " AND base_id = " + base_id;
			Statement stmt2 = connection.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql);
			while(rs2.next()) {
				int tool_idv = rs2.getInt("tool_id");
				int base_idv = rs2.getInt("base_id");
				
				assert(tool_id == tool_idv);
				assert(base_id == base_idv);
				
				boolean isclone = rs2.getBoolean("isclone");
				boolean verifiersuccess = rs2.getBoolean("verifiersuccess");
				Path srcfile1 = mutantsystem.resolve(rs2.getString("srcfile1")).toAbsolutePath().normalize();
				int startline1 = rs2.getInt("startline1");
				int endline1 = rs2.getInt("endline1");
				Path srcfile2 = mutantsystem.resolve(rs2.getString("srcfile2")).toAbsolutePath().normalize();
				int startline2 = rs2.getInt("startline2");
				int endline2 = rs2.getInt("endline2");
				clones.add(new VerifiedClone(new Fragment(srcfile1, startline1, endline1), new Fragment(srcfile2, startline2, endline2), isclone, verifiersuccess));
			}
			rs2.close();
			stmt2.close();
					
			retval.add(new UnitPrecision(tool_id, base_id, precision, clones));
		}
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	/**
	 * 
	 * @param toolid
	 * @return
	 * @throws SQLException
	 * @throws IllegalArgumentException
	 */
	public List<UnitPrecision> getUnitPrecisionForTool(int toolid) throws SQLException, IllegalArgumentException, IllegalStateException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get Unit Precision
		String sql = "SELECT * FROM unit_precision WHERE tool_id = " + toolid;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		List<UnitPrecision> retval = new LinkedList<UnitPrecision>();
		while(rs.next()) {
			int tool_id  = rs.getInt("tool_id");
			int base_id = rs.getInt("base_id");
					
			assert(tool_id == toolid);
					
			double precision = rs.getDouble("precision");
					
					
			//Get Verified Clones
			List<VerifiedClone> clones = new LinkedList<VerifiedClone>();
			sql = "SELECT * FROM unit_precision_clones_considered WHERE tool_id = " + tool_id + " AND base_id = " + base_id;
			Statement stmt2 = connection.createStatement();
			ResultSet rs2 = stmt2.executeQuery(sql);
			while(rs2.next()) {
				int tool_idv = rs2.getInt("tool_id");
				int base_idv = rs2.getInt("base_id");
				
				assert(tool_id == tool_idv);
				assert(base_id == base_idv);
				
				boolean isclone = rs2.getBoolean("isclone");
				boolean verifiersuccess = rs2.getBoolean("verifiersuccess");
				Path srcfile1 = mutantsystem.resolve(rs2.getString("srcfile1")).toAbsolutePath().normalize();
				int startline1 = rs2.getInt("startline1");
				int endline1 = rs2.getInt("endline1");
				Path srcfile2 = mutantsystem.resolve(rs2.getString("srcfile2")).toAbsolutePath().normalize();
				int startline2 = rs2.getInt("startline2");
				int endline2 = rs2.getInt("endline2");
				clones.add(new VerifiedClone(new Fragment(srcfile1, startline1, endline1), new Fragment(srcfile2, startline2, endline2), isclone, verifiersuccess));
			}
			rs2.close();
			stmt2.close();
					
			retval.add(new UnitPrecision(tool_id, base_id, precision, clones));
		}
		rs.close();
		stmt.close();
		
		//Return
		return retval;
	}
	
	/**
	 * Retrieves the overall precision value for a specified tool for a specified mutator.  Found by averaging
	 * the unit precision for the tool in the database that were for bases with injected clones produced using the
	 * specified mutation operator.
	 * @param tool_id the tool id.
	 * @param operator the mutantion operator.
	 * @return the overall precision value for a specified tool for a specified mutation operator.
	 * @throws SQLException
	 */
	public List<UnitPrecision> getUnitPrecisionForToolAndMutator(int tool_id, int mutator_id) throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "" +
				"SELECT ur.base_id, ur.tool_id " +
				"FROM mutant_bases mb, mutant_fragments mf, mutators mo, unit_precision ur " +
				"WHERE mb.mutant_id = mf.mutant_id AND " +
				      "mf.mutator_id = mo.mutator_id AND " +
				      "ur.base_id = mb.base_id AND " +
				      "tool_id = " + tool_id + " AND " +
				      "mo.mutator_id = '" + mutator_id + "'";
		
		//Execute
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Base Ids
		List<Integer> baseids = new LinkedList<Integer>();
		while(rs.next()) {
			baseids.add(rs.getInt("base_id"));
		}
		rs.close();
		stmt.close();
		
		//Get Unit Precisions
		List<UnitPrecision> retval = new LinkedList<UnitPrecision>();
		for(int baseid : baseids) {
			retval.add(getUnitPrecision(tool_id, baseid));
		}
		
		//Return
		return retval;
	}
	
// Recall ------------------------------------------------------------------------------------------------------------------------------
	
	public double getRecall(int tool_id) throws SQLException, IllegalStateException, IllegalArgumentException {
		//Check state and existance of tool
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		if(!this.existsTool(tool_id)) {
			throw new IllegalArgumentException("No tool with id " + tool_id + ".");
		}
				
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Create and Execute Statement
		String sql = "SELECT AVG(recall) AS recall FROM unit_recall WHERE tool_id = " + tool_id;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Result
		rs.next();
		double recall = rs.getDouble("recall");
		
		//Clean-up
		rs.close();
		stmt.close();
		
		//return result
		return recall;
	}
	
	/**
	 * 
	 * @param tool_id
	 * @param type
	 * @return The recall for the type and tool id.  -1 if no clones of this type in the corpus.
	 * @throws SQLException
	 * @throws IllegalStateException
	 * @throws IllegalArgumentException
	 */
	public double getRecallForCloneType(int tool_id, int type) throws SQLException, IllegalStateException, IllegalArgumentException {
		//Check state and existance of tool
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		if(!this.existsTool(tool_id)) {
			throw new IllegalArgumentException("No tool with id " + tool_id + ".");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get mutator ids for target clone type
		List<MutatorDB> mutators = this.getMutators();
		List<MutatorDB> rmutators = new LinkedList<MutatorDB>();
		for(MutatorDB mutator : mutators) {
			if(mutator.getTargetCloneType() == type) {
				rmutators.add(mutator);
			}
		}
		mutators=null;
		
		//If none, send no mutators for this type flag
		if(rmutators.size() == 0) {
			return -1;
		}
		
		//Create and execute statement
		String sql = "SELECT AVG(ur.recall) AS recall " +
		             "FROM unit_recall ur, mutant_bases mb, mutant_fragments mf, mutators m " +
		             "WHERE mb.base_id = ur.base_id AND " +
		             "mf.mutant_id = mb.mutant_id AND " +
		             "m.mutator_id = mf.mutator_id AND " +
		             "ur.tool_id = " + tool_id + " AND ( ";
		Iterator<MutatorDB> iterator = rmutators.iterator();
		MutatorDB mutator;
		while(iterator.hasNext()) {
			mutator = iterator.next();
			sql += "m.mutator_id = " + mutator.getId() + " ";
			if(iterator.hasNext()) {
				sql += "OR ";
			} else {
				sql += ") ";
			}
		}
		
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Result
		rs.next();
		double recall = rs.getDouble("recall");
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return Result
		return recall;
	}
	
	public double getRecallForMutator(int tool_id, int mutator_id) throws SQLException, IllegalStateException, IllegalArgumentException {
		//Check state and existance of tool
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		if(!this.existsTool(tool_id)) {
			throw new IllegalArgumentException("No tool with id " + tool_id + ".");
		}
		if(!this.existsMutator(mutator_id)) {
			throw new IllegalArgumentException("No mutator with id " + mutator_id + ".");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Create and execute statement
		String sql = "SELECT AVG(ur.recall) AS recall " +
		             "FROM unit_recall ur, mutant_bases mb, mutant_fragments mf, mutators m " +
		             "WHERE mb.base_id = ur.base_id AND " +
		             "mf.mutant_id = mb.mutant_id AND " +
		             "m.mutator_id = mf.mutator_id AND " +
		             "ur.tool_id = " + tool_id + " AND " + 
		             "m.mutator_id = " + mutator_id;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Result
		rs.next();
		double recall = rs.getDouble("recall");
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return Result
		return recall;
	}
	
	public double getRecallForOperator(int tool_id, int operator_id) throws SQLException, IllegalStateException, IllegalArgumentException {
		//Check state and existance of tool
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		if(!this.existsTool(tool_id)) {
			throw new IllegalArgumentException("No tool with id " + tool_id + ".");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get mutator ids for target clone type
		List<MutatorDB> mutators = this.getMutators();
		List<MutatorDB> rmutators = new LinkedList<MutatorDB>();
		for(MutatorDB mutator : mutators) {
			if(mutator.includesOperator(operator_id)) {
				rmutators.add(mutator);
			}
		}
		mutators=null;
		
		//If none, send no mutators for this type flag
		if(rmutators.size() == 0) {
			return -1;
		}
		
		//Create and execute statement
		String sql = "SELECT AVG(ur.recall) AS recall " +
		             "FROM unit_recall ur, mutant_bases mb, mutant_fragments mf, mutators m " +
		             "WHERE mb.base_id = ur.base_id AND " +
		             "mf.mutant_id = mb.mutant_id AND " +
		             "m.mutator_id = mf.mutator_id AND " +
		             "ur.tool_id = " + tool_id + " AND ( ";
		Iterator<MutatorDB> iterator = rmutators.iterator();
		MutatorDB mutator;
		while(iterator.hasNext()) {
			mutator = iterator.next();
			sql += "m.mutator_id = " + mutator.getId() + " ";
			if(iterator.hasNext()) {
				sql += "OR ";
			} else {
				sql += ") ";
			}
		}
		
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Result
		rs.next();
		double recall = rs.getDouble("recall");
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return Result
		return recall;
	}
	
// Precision ---------------------------------------------------------------------------------------------------------------------------
	
	public double getPrecision(int tool_id) throws SQLException, IllegalStateException, IllegalArgumentException {
		//Check state and existance of tool
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		if(!this.existsTool(tool_id)) {
			throw new IllegalArgumentException("No tool with id " + tool_id + ".");
		}
				
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Create and Execute Statement
		String sql = "SELECT AVG(precision) AS precision FROM unit_precision WHERE tool_id = " + tool_id;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Result
		rs.next();
		double precision = rs.getDouble("precision");
		
		//Clean-up
		rs.close();
		stmt.close();
		
		//return result
		return precision;
	}
	
	public double getPrecisionForCloneType(int tool_id, int type) throws SQLException, IllegalStateException, IllegalArgumentException {
		//Check state and existance of tool
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		if(!this.existsTool(tool_id)) {
			throw new IllegalArgumentException("No tool with id " + tool_id + ".");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get mutator ids for target clone type
		List<MutatorDB> mutators = this.getMutators();
		List<MutatorDB> rmutators = new LinkedList<MutatorDB>();
		for(MutatorDB mutator : mutators) {
			if(mutator.getTargetCloneType() == type) {
				rmutators.add(mutator);
			}
		}
		mutators=null;
		
		if(rmutators.size() == 0) {
			return -1;
		}
		
		//Create and execute statement
		String sql = "SELECT AVG(up.precision) AS precision " +
		             "FROM unit_precision up, mutant_bases mb, mutant_fragments mf, mutators m " +
		             "WHERE mb.base_id = up.base_id AND " +
		             "mf.mutant_id = mb.mutant_id AND " +
		             "m.mutator_id = mf.mutator_id AND " +
		             "up.tool_id = " + tool_id + " AND ( "; 
 		Iterator<MutatorDB> iterator = rmutators.iterator();
		MutatorDB mutator;
		while(iterator.hasNext()) {
			mutator = iterator.next();
			sql += "m.mutator_id = " + mutator.getId() + " ";
			if(iterator.hasNext()) {
				sql += "OR ";
			} else {
				sql += ") ";
			}
		}
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Result
		rs.next();
		double precision = rs.getDouble("precision");
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return Result
		return precision;
	}
	
	public double getPrecisionForMutator(int tool_id, int mutator_id) throws SQLException, IllegalStateException, IllegalArgumentException {
		//Check state and existance of tool
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		if(!this.existsTool(tool_id)) {
			throw new IllegalArgumentException("No tool with id " + tool_id + ".");
		}
		if(!this.existsMutator(mutator_id)) {
			throw new IllegalArgumentException("No mutator with id " + mutator_id + ".");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Create and execute statement
		String sql = "SELECT AVG(up.precision) AS precision " +
		             "FROM unit_precision up, mutant_bases mb, mutant_fragments mf, mutators m " +
		             "WHERE mb.base_id = up.base_id AND " +
		             "mf.mutant_id = mb.mutant_id AND " +
		             "m.mutator_id = mf.mutator_id AND " +
		             "up.tool_id = " + tool_id + " AND " + 
		             "m.mutator_id = " + mutator_id;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Result
		rs.next();
		double recall = rs.getDouble("precision");
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return Result
		return recall;
	}
	
	public double getPrecisionForOperator(int tool_id, int operator_id) throws SQLException, IllegalStateException, IllegalArgumentException {
		//Check state and existance of tool
		if(this.getStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only be called during results stage.");
		}
		if(!this.existsTool(tool_id)) {
			throw new IllegalArgumentException("No tool with id " + tool_id + ".");
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get mutator ids for target clone type
		List<MutatorDB> mutators = this.getMutators();
		List<MutatorDB> rmutators = new LinkedList<MutatorDB>();
		for(MutatorDB mutator : mutators) {
			if(mutator.includesOperator(operator_id)) {
				rmutators.add(mutator);
			}
		}
		mutators=null;
		
		if(rmutators.size() == 0) {
			return -1;
		}
		
		//Create and execute statement
		String sql = "SELECT AVG(up.precision) AS precision " +
		             "FROM unit_precision up, mutant_bases mb, mutant_fragments mf, mutators m " +
		             "WHERE mb.base_id = up.base_id AND " +
		             "mf.mutant_id = mb.mutant_id AND " +
		             "m.mutator_id = mf.mutator_id AND " +
		             "up.tool_id = " + tool_id + " AND ( "; 
 		Iterator<MutatorDB> iterator = rmutators.iterator();
		MutatorDB mutator;
		while(iterator.hasNext()) {
			mutator = iterator.next();
			sql += "m.mutator_id = " + mutator.getId() + " ";
			if(iterator.hasNext()) {
				sql += "OR ";
			} else {
				sql += ") ";
			}
		}
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Get Result
		rs.next();
		double precision = rs.getDouble("precision");
		
		//Cleanup
		rs.close();
		stmt.close();
		
		//Return Result
		return precision;
	}
	
// OPERATORS
	
	public OperatorDB createOperator(String name, String description, int clonetype, Path executable) throws SQLException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		
		//Check Arguments
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(executable);
		executable = executable.toAbsolutePath().normalize();
		if(!Files.exists(executable)) {
			throw new IllegalArgumentException("executable does not exist.");
		}
		if(!Files.isRegularFile(executable)) {
			throw new IllegalArgumentException("executable is not a regular file.");
		}
		if(!Files.isExecutable(executable)) {
			throw new IllegalArgumentException("executable is not executable.");
		}
		if(!executable.startsWith(SystemUtil.getInstallRoot().toAbsolutePath().normalize())) {
			throw new IllegalArgumentException("executable must be in the operator folder.");
		}
		
		//Prepare SQL Statement
		executable = SystemUtil.getOperatorsPath().toAbsolutePath().normalize().relativize(executable.toAbsolutePath().normalize());
		String sql = "INSERT INTO mutation_operators (operator_name, description, target_clone_type, mutator) VALUES (" +
				"'" + name + "'," + 
				"'" + description + "'," +
				+ clonetype + "," + 
				"'" + executable.toString() + "'" + 
			")";
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		int retval = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		assert(retval == 1) : "createOperator failed.";
		
		//Prepare Return
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int operator_id = rs.getInt(1);
		OperatorDB op = getOperator(operator_id);
		
		//Cleanup
		stmt.close();
		
		return op;
	}
	
	/**
	 * Retrieves a MutationOperator from the database.
	 * @param id The id of the mutation operator to retrieve.
	 * @return The mutation operator, or null if one with the specified id does not exist in the database.
	 * @throws SQLException
	 */
	public OperatorDB getOperator(int id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}		
		
		//Prepare SQL Statement
		String sql = "SELECT * FROM mutation_operators WHERE operator_id = " + id;
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Found it, return it
		if(rs.next()) {
			assert(!rs.next()) : "getOperator failed.";
			id = rs.getInt("operator_id");
			String name = rs.getString("operator_name");
			String desc = rs.getString("description");
			int tct = rs.getInt("target_clone_type");
			String mutator = rs.getString("mutator");
			mutator = SystemUtil.getOperatorsPath().toAbsolutePath().normalize().toString() + "/" + mutator;
			return new OperatorDB(id, name, desc, tct, Paths.get(mutator));
			
		//Did not find it, return null
		} else {
			return null;
		}
	}
	
	/**
	 * Removes an operator.
	 * @param id The id of the operator to remove.
	 * @return True if removed, false if not removed (no operator with this id).
	 * @throws SQLException
	 */
	public boolean deleteOperator(int id) throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(!existsOperator(id)) {
			return false;
		} else {
			//Prepare Connection
			if(!connection.getAutoCommit()) {
				connection.rollback();
				connection.setAutoCommit(true);
			}
			
			//Prepare SQL
			String sql = "DELETE FROM mutation_operators WHERE operator_id = " + id;
			
			Statement stmt = connection.createStatement();
			int result = stmt.executeUpdate(sql);
			stmt.close();
			assert(result == 1);
			return true;
		}
	}
	
	/**
	 * Removes all operators from the database.
	 * @return The number removed.
	 * @throws SQLException
	 */
	public int deleteOperators() throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "DELETE FROM mutation_operators";
		
		Statement stmt = connection.createStatement();
		int num = stmt.executeUpdate(sql);
		stmt.close();
		return num;
	}
	
	/**
	 * Returns the operators.
	 * @return the operators.
	 * @throws SQLException
	 */
	public List<OperatorDB> getOperators() throws SQLException {
		//Prepare Statement
		String sql = "SELECT * FROM mutation_operators ORDER BY operator_id ASC";
	
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		List<OperatorDB> retval = new ArrayList<OperatorDB>();
		Statement stmt = connection.createStatement();
		
		//Collect Results and Return
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			int id = rs.getInt("operator_id");
			String name = rs.getString("operator_name");
			String desc = rs.getString("description");
			int tct = rs.getInt("target_clone_type");
			String mutator = rs.getString("mutator");
			mutator = SystemUtil.getOperatorsPath().toAbsolutePath().normalize().toString() + "/" + mutator;
			retval.add(new OperatorDB(id, name, desc, tct, Paths.get(mutator)));
		}
		return retval;
	}
	
	/**
	 * Retrieves the operator ids.
	 * @return the operator ids.
	 * @throws SQLException
	 */
	public List<Integer> getOperatorIds() throws SQLException {
		//Prepare SQL Statement
		String sql = "SELECT * FROM mutation_operators ORDER BY operator_id ASC";
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Collect Results and Return
		List<Integer> retval = new LinkedList<Integer>();
		while(rs.next()) {
			retval.add(rs.getInt("operator_id"));
		}
		return retval;
	}
	
	/**
	 * Checks if an operator exists.
	 * @param id Id of the operator.
	 * @return If the operator exists.
	 * @throws SQLException
	 */
	public boolean existsOperator(int id) throws SQLException {
		//Prepare SQL Statement
		String sql = "SELECT * FROM mutation_operators WHERE operator_id = " + id;
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count rows found (should be 0 or 1)
		int num = 0;
		while(rs.next()) {
			num++;
		}
				
		assert(num == 0 || num == 1) : "existsFragment has a bug.  Found multiple entries.";
		
		//Found it, return true
		if (num == 1) {
			return true;
			
		//Found nothing, return false
		} else  {
			return false;
		}
	}
	
	/**
	 * Returns the number of the operators in the database.
	 * @return the number of the operators in the database.
	 * @throws SQLException
	 */
	public int numOperators() throws SQLException {
		//Prepare SQL Statement
		String sql = "SELECT * FROM mutation_operators";
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Coount Number, and return
		int num = 0;
		while(rs.next()) {
			num++;
		}
		return num;
	}
	
// --- Mutators -------------------------------------------------------------------------------------------------------
	
	/**
	 * Adds the given mutator to the database.
	 * @param m the mutator
	 * @return The id of the mutator.
	 * @throws SQLException
	 */
	public MutatorDB createMutator(String description, List<Integer> operator_ids) throws SQLException, IllegalArgumentException, IllegalStateException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		//Check Arguments
		Objects.requireNonNull(description);
		Objects.requireNonNull(operator_ids);
		if(operator_ids.size() < 1) {
			throw new IllegalArgumentException("Operators must contain at least one operator.");
		}
		for(Integer id : operator_ids) {
			if(!existsOperator(id)) {
				throw new IllegalArgumentException("Operator with id " + id + " in operator_ids does not exist in the database.");
			}
		}
		
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Create Mutator
		String sql = "INSERT INTO mutators (description) VALUES ('" + description + "')";
		Statement stmt = connection.createStatement();
		int count = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
		assert(count == 1) : "createMutator failed.";
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		int mutator_id = rs.getInt(1);
		
		//Add operators to mutator
		int num = 1;
		for(Integer id : operator_ids) {
			sql = "INSERT INTO mutator_operators (mutator_id, operator_num, operator_id) VALUES (" + mutator_id + "," + num + ",'" + id + "')";
			count = stmt.executeUpdate(sql);
			assert(count == 1) : "createMutator failed.";
			num++;
		}
		
		//cleanup
		rs.close();
		stmt.close();
		
		//Return the mutator
		return getMutator(mutator_id);
	}
	
	public boolean deleteMutator(int id) throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(!existsMutator(id)) {
			return false;
		} else {
			//Prepare Connection
			if(!connection.getAutoCommit()) {
				connection.rollback();
				connection.setAutoCommit(true);
			}
			Statement stmt = connection.createStatement();
			
			String sql = "DELETE FROM mutators WHERE mutator_id = " + id;
			
			int num = stmt.executeUpdate(sql);
			assert(num == 1);
			stmt.close();
			return true;
		}
	}
	
	public int deleteMutators() throws SQLException, IllegalStateException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}

		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
			
		String sql = "DELETE FROM mutators";
		Statement stmt = connection.createStatement();
		int num = stmt.executeUpdate(sql);
		stmt.close();
		return num;
	}
	
	/**
	 * Returns a mutator from the database.
	 * @param id The id of the mutator to retrieve. 
	 * @return a mutator from the database.
	 * @throws SQLException
	 */
	public MutatorDB getMutator(int id) throws SQLException {
	//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
	//Get Mutator
		//Get header
		String sql = "SELECT * FROM mutators WHERE mutator_id = " + id;
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			assert(!rs.next()) : "getMutator failed, found multiple with same id.";
			String desc = rs.getString("description");
			rs.close();
			stmt.close();
		
		//Get operators of mutator
			List<OperatorDB> oplist = new LinkedList<OperatorDB>();
			sql = "SELECT * FROM mutator_operators WHERE mutator_id = " + id + " ORDER BY operator_num ASC";
			stmt = connection.createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()) {
				oplist.add(getOperator(rs.getInt("operator_id")));
			}
			rs.close();
			stmt.close();
			
			if(oplist.size() > 0) {
				return new MutatorDB(id, desc, oplist);
			} else {
				return null;
			}
		} else {
			stmt.close();
			return null;
		}
	}
	
	/**
	 * Does the specified mutator exist in the database.
	 * @param id The id of the mutator to query about.
	 * @return true if a mutator with the given id exists, or false if it does not.
	 * @throws SQLException
	 */
	public boolean existsMutator(int id) throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutators WHERE mutator_id = " + id;
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Determine result
		boolean retval;
		if(rs.next()) {
			assert(!rs.next()) : "existsMutator failed, found multiple rows.";
			retval = true;
		} else {
			retval = false;
		}
		
		//cleanup
		rs.close();
		stmt.close();
		
		return retval;
	}
	
	/**
	 * Returns the number of mutators in the database.
	 * @return the number of mutators in the database.
	 * @throws SQLException
	 */
	public int numMutators() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT * FROM mutators";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Count number and return
		int retval=0;
		while(rs.next()) {
			retval++;
		}
		
		//cleanup
		rs.close();
		stmt.close();
		
		return retval;
	}
	
	/**
	 * Returns a collection of the mutators specified in the database.
	 * @return a collection of the mutators specified in the database.
	 * @throws SQLException
	 */
	public List<MutatorDB> getMutators() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Get mutators
		ArrayList<MutatorDB> list = new ArrayList<MutatorDB>();
		String sql = "SELECT * FROM mutators";
		Statement stmt = connection.createStatement();
		Statement stmt2 = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		while(rs.next()) {
			int id = rs.getInt("mutator_id");
			String description = rs.getString("description");
			LinkedList<OperatorDB> oplist = new LinkedList<OperatorDB>();
			
			sql = "SELECT * FROM mutator_operators WHERE mutator_id = " + id + " ORDER BY operator_num ASC";
			ResultSet rs2 = stmt2.executeQuery(sql);
			while(rs2.next()) {
				oplist.add(getOperator(rs2.getInt("operator_id")));
			}
			rs2.close();
			
			if(oplist.size() > 0) {
				list.add(new MutatorDB(id, description, oplist));
			}
		}
		rs.close();
		stmt.close();
		stmt2.close();
		return list;
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public List<Integer> getMutatorIds() throws SQLException {
		//Prepare Connection
		if(!connection.getAutoCommit()) {
			connection.rollback();
			connection.setAutoCommit(true);
		}
		
		//Prepare SQL
		String sql = "SELECT mutator_id FROM mutators ORDER BY mutator_id ASC";
		
		//Execute Statement
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		
		//Populate List
		List<Integer> retval = new ArrayList<Integer>();
		while(rs.next()) {
			retval.add(rs.getInt("mutator_id"));
		}
		
		//CLeanup
		rs.close();
		stmt.close();
		
		return retval;
	}
	
// Properties
	public void setLanguage(int language) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(!ExperimentSpecification.isLanguageSupported(language)) {
			throw new IllegalArgumentException("Language is not supported.");
		} else {
			Statement stmt = connection.createStatement();
			String sql = "UPDATE properties SET language = " + language;
			int i = stmt.executeUpdate(sql);
			assert(i == 1);
			stmt.close();
		}
	}
	public int getLanguage() throws SQLException {
		int retval;
		String sql = "SELECT language FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		retval = rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//GenerationType
	public void setGenerationType(int generation_type) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(!ExperimentSpecification.isGenerationTypeValid(generation_type)) {
			throw new IllegalArgumentException();
		} else {
			Statement stmt = connection.createStatement();
			String sql = "UPDATE properties SET generation_type = " + generation_type;
			int i = stmt.executeUpdate(sql);
			assert(i == 1);
			stmt.close();
		}
	}	
	public int getGenerationType() throws SQLException {
		int retval;
		String sql = "SELECT generation_type FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		retval = rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//FragmentType
	public void setFragmentType(int type) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(!ExperimentSpecification.isFragmentTypeValid(type)) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET fragment_type = " + type;
			Statement stmt = connection.createStatement();
			int i = stmt.executeUpdate(sql);
			assert(i == 1);
			stmt.close();
		}
	}
	public int getFragmentType() throws SQLException {
		Statement stmt = connection.createStatement();
		String sql = "SELECT fragment_type FROM properties";
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval = rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//Fragment Minimum Size (lines)
	public void setFragmentMinimumSizeLines(int num) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(num <= 0) {
			throw new IllegalArgumentException();
		} else if (!(num <= this.getFragmentMaximumSizeLines())) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET fragment_min_size_lines = " + num;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public int getFragmentMinimumSizeLines() throws SQLException {
		String sql = "SELECT fragment_min_size_lines FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval = rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//Fragment Maximum Size
	public void setFragmentMaximumSizeLines(int num) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(num <= 0) {
			throw new IllegalArgumentException();
		} else if (!(num >= this.getFragmentMinimumSizeLines())) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET fragment_max_size_lines = " + num;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public int getFragmentMaximumSizeLines() throws SQLException {
		String sql = "SELECT fragment_max_size_lines FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval = rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//Fragment Minimum Size (tokens)
	public void setFragmentMinimumSizeTokens(int num) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(num <= 0) {
			throw new IllegalArgumentException();
		} else if (!(num < this.getFragmentMaximumSizeTokens())) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET fragment_min_size_tokens = " + num;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public int getFragmentMinimumSizeTokens() throws SQLException {
		String sql = "SELECT fragment_min_size_tokens FROM properties";
		ResultSet rs = connection.createStatement().executeQuery(sql);
		rs.next();
		return rs.getInt(1);
	}
	
	//Fragment Maximum Size
	public void setFragmentMaximumSizeTokens(int num) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(num <= 0) {
			throw new IllegalArgumentException();
		} else if(!(num > this.getFragmentMinimumSizeTokens())) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET fragment_max_size_tokens = " + num;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public int getFragmentMaximumSizeTokens() throws SQLException {
		String sql = "SELECT fragment_max_size_tokens FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval = rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//InjectionNumber
	public void setInjectionNumber(int num) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(num <= 0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET injection_number = " + num;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public int getInjectionNumber() throws SQLException {
		String sql = "SELECT injection_number FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval = rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//Subsume Matcher Tolerance
	public void setSubsumeMatcherTolerance(double tolerance) throws SQLException {
		if(tolerance < 0.0 || tolerance > 1.0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET subsume_matcher_tolerance = " + tolerance;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public double getSubsumeMatcherTolerance() throws SQLException {
		String sql = "SELECT subsume_matcher_tolerance FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		double retval = rs.getDouble(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//Allowed Fragment Difference
	public void setAllowedFragmentDifference(double difference) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(difference < 0.0 || difference > 1.0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET allowed_fragment_difference = " + difference;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public double getAllowedFragmentDifference() throws SQLException {
		String sql = "SELECT allowed_fragment_difference FROM properties";
		Statement stmt = connection.createStatement(); 
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		double retval = rs.getDouble(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//Mutation Containment
	public void setMutationContainment(double containment) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(containment < 0.0 || containment > 1.0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET mutation_containment = " + containment;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public double getMutationContainment() throws SQLException {
		String sql = "SELECT mutation_containment FROM properties";
		ResultSet rs = connection.createStatement().executeQuery(sql);
		rs.next();
		return rs.getDouble(1);
	}
	
	//MutationAttempts
	public void setMutationAttempts(int num) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(num <= 0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET mutation_attempts = " + num;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public int getMutationAttempts() throws SQLException {
		String sql = "SELECT mutation_attempts FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval =  rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//OperatorAttempts
	public void setOperatorAttempts(int num) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(num <= 0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET operator_attempts = " + num;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public int getOperatorAttempts() throws SQLException {
		String sql = "SELECT operator_attempts FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval =  rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//MaxFragments
	public void setMaxFragments(int num) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(this.getStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Only valid during setup stage.");
		}
		if(num <= 0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET max_fragments = " + num;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public int getMaxFragments() throws SQLException {
		String sql = "SELECT max_fragments FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval = rs.getInt(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//PrecisionRequiredSimilarity
	public void setPrecisionRequiredSimilarity(double similarity) throws SQLException {
		if(similarity < 0.0 || similarity > 1.0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET precision_required_similarity = " + similarity;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public double getPrecisionRequiredSimilarity() throws SQLException {
		String sql = "SELECT precision_required_similarity FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		double retval = rs.getDouble(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//RecallRequireSimilarity
	public void setRecallRequiredSimilarity(double similarity) throws SQLException {
		if(similarity < 0.0 || similarity > 1.0) {
			throw new IllegalArgumentException();
		} else {
			String sql = "UPDATE properties SET recall_required_similarity = " + similarity;
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}
	}
	public double getRecallRequiredSimilarity() throws SQLException {
		String sql = "SELECT recall_required_similarity FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		double retval = rs.getDouble(1);
		rs.close();
		stmt.close();
		return retval;
	}
	
	//Stage
	private void setStage(int stage) throws SQLException {
		if(stage == ExperimentData.GENERATION_SETUP_STAGE) {
			stage = 0;
		} else if (stage == ExperimentData.GENERATION_STAGE) {
			stage =1;
		} else if (stage == ExperimentData.EVALUATION_SETUP_STAGE) {
			stage = 2;
		} else if (stage == ExperimentData.EVALUATION_STAGE) {
			stage = 3;
		} else if (stage == ExperimentData.RESULTS_STAGE) {
			stage = 4;
		} else if (stage == ExperimentData.ERROR_STAGE){ //error
			stage = -1;
		} else {
			throw new IllegalArgumentException("Not a valid stage.");
		}
		
		String sql = "UPDATE properties SET experiment_stage = " + stage;
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sql);
		stmt.close();
	}
	
	private int getStage() throws SQLException {
		String sql = "SELECT experiment_stage FROM properties";
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		rs.next();
		int retval = rs.getInt(1);
		rs.close();
		stmt.close();
		
		if(retval == 0) {
			return ExperimentData.GENERATION_SETUP_STAGE;
		} else if (retval == 1) {
			return ExperimentData.GENERATION_STAGE;
		} else if (retval == 2) {
			return ExperimentData.EVALUATION_SETUP_STAGE;
		} else if (retval == 3) {
			return ExperimentData.EVALUATION_STAGE;
		} else if (retval == 4) {
			return ExperimentData.RESULTS_STAGE;
		} else {
			return ExperimentData.ERROR_STAGE;
		}
	}
	
	/**
	 * 
	 * @param mbid
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public void constructBase(int mbid) throws SQLException, FileNotFoundException, IOException, IllegalArgumentException {
		//Check Arguments
		if(!existsMutantBase(mbid)) {
			throw new IllegalArgumentException("Mutant base does not exist with id " + mbid + ".");
		}
		MutantBaseDB mb = getMutantBase(mbid);
		constructBaseHelper(mb);
	}
	
	public MutantBase constructBase(MutantBase mb) throws FileNotFoundException, IllegalArgumentException, SQLException, IOException {
		Objects.requireNonNull(mb);
		
		Path osrcfile = mb.getDirectory().toAbsolutePath().normalize().relativize(mb.getOriginalFragment().getSrcFile().toAbsolutePath().normalize());
		osrcfile = this.getMutantBasePath().resolve(osrcfile);
		
		Path msrcfile = mb.getDirectory().toAbsolutePath().normalize().relativize(mb.getMutantFragment().getSrcFile().toAbsolutePath().normalize());
		msrcfile = this.getMutantBasePath().resolve(msrcfile);
		
		Fragment original = new Fragment(osrcfile, 
				mb.getOriginalFragment().getStartLine(), 
				mb.getOriginalFragment().getEndLine());
		
		Fragment mutant = new Fragment(msrcfile,
				mb.getMutantFragment().getStartLine(),
				mb.getMutantFragment().getEndLine());
		
		MutantBase nmb = new MutantBase(this.getMutantBasePath(), mb.getMutantId(), original, mutant);
		constructBaseHelper(nmb);
		return nmb;
	}
	
	/**
	 * 
	 * @param mbid
	 * @throws SQLException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throw IllegalArgumentException
	 */
	private void constructBaseHelper(MutantBase mb) throws SQLException, FileNotFoundException, IOException {
		//Clear Mutant Base Directory
		FileUtils.deleteDirectory(this.mutantsystem.toAbsolutePath().normalize().toFile());
		Files.createDirectories(mutantsystem);
		
		//Needed variable/data
		MutantFragment mf = getMutantFragment(mb.getMutantId());
		FragmentDB f = getFragment(mf.getFragmentId());
		
		// make a copy of the original code base
		FileUtils.copyDirectory(this.getSystemPath().toAbsolutePath().normalize().toFile(), this.getMutantBasePath().toAbsolutePath().normalize().toFile());
		
		//Inject
			//Inject Fragment
		FragmentUtil.injectFragment(mb.getOriginalFragment().getSrcFile(), mb.getOriginalFragment().getStartLine(), f.getFragmentFile());
			//Inject Mutant Fragment
		FragmentUtil.injectFragment(mb.getMutantFragment().getSrcFile(), mb.getMutantFragment().getStartLine(), mf.getFragmentFile());
	}
}