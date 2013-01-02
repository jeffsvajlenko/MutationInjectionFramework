package experiment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import util.AfterFunctionInjectionLocationChooser;
import util.BlockInjectionLocationChooser;
import util.FileUtil;
import util.FragmentUtil;
import util.InjectionLocationChooser;
import util.SelectBlockFragments;
import util.SelectFunctionFragments;
import util.TXLException;
import util.TXLUtil;
import validator.LineValidator;
import validator.TokenValidator;
import validator.ValidatorResult;

import main.ArtisticStyleFailedException;
import main.FileSanetizationFailedException;
import models.AbstractMutator;
import models.Clone;
import models.CloneDetectionReport;
import models.Fragment;
import models.InjectionLocation;
import models.InvalidToolRunnerException;
import models.MutantBase;
import models.VerifiedClone;

public class Experiment {

//-- Fields -----------------------------------------------------------------------------------------------------------	
	
	private ExperimentData ed;
	private PrintStream log;
	
	/**
	 * Setup phase.  Where experimental settings are configured before generation.
	 */
	public static final int SETUP_PHASE = ExperimentData.GENERATION_SETUP_STAGE;
	
	/**
	 * Generation Phase.  When clones and mutant bases are generated.
	 */
	public static final int GENERATION_PHASE = ExperimentData.GENERATION_STAGE;
	
	/**
	 * Evaluation Phase.  When the experiment data is evaluted using the clone detection tools.
	 */
	public static final int EVALUATION_PHASE = ExperimentData.EVALUATION_STAGE;
	
//-- Accessors ---------------------------------------------------------------------------------------------------------
	
	/**
	 * @return The current phase of the experiment.
	 * @throws SQLException 
	 */
	public int currentPhase() throws SQLException {
		return ed.getCurrentStage();
	}
	
	/**
	 * FOR TESTING ONLY
	 */
	public ExperimentData getExperimentData() {
		return this.ed;
	}
	
	public boolean isGenerated() throws SQLException {
		if(ed.getCurrentStage() == ExperimentData.GENERATION_SETUP_STAGE) {
			return false;
		} else {
			return true;
		}
	}
	
//-- Private Constructor ----------------------------------------------------------------------------------------------
	
	/**
	 * Constructor (private).
	 * @param ed The experiment data.
	 * @param log The logging printstream.
	 * @param generationComplete Is generation complete at this time.
	 */
	private Experiment(ExperimentData ed, PrintStream log) {
		this.ed = ed;
		this.log = log;
	}
	
//-- Public Creation --------------------------------------------------------------------------------------------------
	
	/**

	 * Creates a new experiment.
	 * @param spec The experiment specification.
	 * @param log A print stream to write logging messages to.
	 * @return The experiment object.
	 * @throws FileSanetizationFailedException 
	 * @throws IllegalArgumentException 
	 * @throws SQLException
	 * @throws IOException
	 * @throws ArtisticStyleFailedException 
	 * @throws InterruptedException
	 * @throws  
	 */
	public static Experiment createExperiment(ExperimentSpecification spec, PrintStream log) throws SQLException, IOException, InterruptedException, ArtisticStyleFailedException, IllegalArgumentException, FileSanetizationFailedException {
		//Create Data
		ExperimentData ed = new ExperimentData(spec.getDataPath(), spec.getSystem(), spec.getRepository(), spec.getLanguage(), log);
		
		//Set Properties
		ed.setAllowedFragmentDifference(spec.getAllowedFragmentDifference());
		ed.setFragmentMinimumSizeLines(spec.getFragmentMinSizeLines());
		ed.setFragmentMaximumSizeLines(spec.getFragmentMaxSizeLines());
		ed.setFragmentMinimumSizeTokens(spec.getFragmentMinSizeTokens());
		ed.setFragmentMaximumSizeTokens(spec.getFragmentMaxSizeTokens());
		ed.setFragmentType(spec.getFragmentType());
		ed.setGenerationType(spec.getGenerationType());
		ed.setInjectionNumber(spec.getInjectNumber());
		ed.setLanguage(spec.getLanguage());
		ed.setMaxFragments(spec.getMaxFragments());
		ed.setMutationAttempts(spec.getMutationAttempts());
		ed.setMutationContainment(spec.getMutationContainment());
		ed.setOperatorAttempts(spec.getOperatorAttempts());
		ed.setPrecisionRequiredSimilarity(spec.getPrecisionRequiredSimilarity());
		ed.setRecallRequiredSimilarity(spec.getRecallRequiredSimilarity());
		ed.setSubsumeMatcherTolerance(spec.getSubsumeMatcherTolerance());
		
		//Create Experiment object
		Experiment e = new Experiment(ed, log);
		
		//Return for analysis
		return e;
	}

	/**
	 * Loads a previous experiment by its output directory.
	 * @param experiment_directory The experiment output directory.
	 * @param log A print stream to write logging messages to.
	 * @return The experiment object.
	 * @throws IllegalArgumentException If directory does not exist, or if experiment directory appears to be invalid.
	 * @throws SQLException
	 */
	public static Experiment loadExperiment(Path experiment_directory, PrintStream log, boolean verify) throws IllegalArgumentException, SQLException {
		Objects.requireNonNull(experiment_directory);
		if(!Files.exists(experiment_directory)) {
			throw new IllegalArgumentException("Experiment directory does not exist.");
		}
		
		ExperimentData ed = new ExperimentData(experiment_directory);
		Experiment e = new Experiment(ed, log);
		if(verify)
			e.verifyAutomaticGeneration();
		return e;
	}

//-- Properties ----------------------------------------------------------------------------------------------------------
	
	//-- Generation Settings (can query not set, set at experiment creation)
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getLanguage() throws SQLException {
		return ed.getLanguage();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getMaxFragments() throws SQLException {
		return ed.getMaxFragments();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getInjectionNumber() throws SQLException {
		return ed.getInjectionNumber();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getGenerationType() throws SQLException {
		return ed.getGenerationType();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getFragmentType() throws SQLException {
		return ed.getFragmentType();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getFragmentMaximumSizeLines() throws SQLException {
		return ed.getFragmentMaximumSizeLines();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getFragmentMinimumSizeLines() throws SQLException {
		return ed.getFragmentMinimumSizeLines();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getFragmentMaximumSizeTokens() throws SQLException {
		return ed.getFragmentMaximumSizeTokens();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getFragmentMinimumSizeTokens() throws SQLException {
		return ed.getFragmentMinimumSizeTokens();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public double getAllowedFragmentDifference() throws SQLException {
		return ed.getAllowedFragmentDifference();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public double getMutationContainment() throws SQLException {
		return ed.getMutationContainment();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getMutationAttempts() throws SQLException {
		return ed.getMutationAttempts();
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public int getOperatorAttempts() throws SQLException {
		return ed.getOperatorAttempts();
	}

	//-- Evaluation Settings (can query and set)
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public double getSubsumeMatcherTolerance() throws SQLException {
		return ed.getSubsumeMatcherTolerance();
	}
	
	/**
	 * 
	 * @param tolerance
	 * @throws SQLException
	 */
	public void setSubsumeMatcherTolerance(double tolerance) throws SQLException {
		ed.setSubsumeMatcherTolerance(tolerance);
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public double getRecallRequiredSimilarity() throws SQLException {
		return ed.getRecallRequiredSimilarity();
	}
	
	/**
	 * 
	 * @param similarity
	 * @throws SQLException
	 */
	public void setRecallRequiredSimilarity(double similarity) throws SQLException {
		ed.setRecallRequiredSimilarity(similarity);
	}
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public double getPrecisionRequiredSimilarity() throws SQLException {
		return ed.getPrecisionRequiredSimilarity();
	}
	
	/**
	 * 
	 * @param similarity
	 * @throws SQLException
	 */
	public void setPrecisionRequiredSimilarity(double similarity) throws SQLException {
		ed.setPrecisionRequiredSimilarity(similarity);
	}
	
//-- Operators -----------------------------------------------------------------------------------------------------------

	/**
	 * Adds an operator to the experiment.  Can only be done in setup phase.
	 * @param name The name of the operator.
	 * @param description A description the operator.
	 * @param clonetype The type of clone it should produce.
	 * @param executable The operator's executable.
	 * @return The operator.
	 * @throws SQLException
	 * @throws IllegalStateException If called after generation is complete.
	 */
	public OperatorDB addOperator(String name, String description, int clonetype, Path executable) throws SQLException, IllegalStateException {
		if(ed.getCurrentStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Can not add operator after setup phase.");
		} else {
			return ed.createOperator(name, description, clonetype, executable);
		}
	}
	
	/**
	 * Retrieves the operator with the given id.  Can only be done in setup phase.
	 * @param id the id of the operator.
	 * @return the operator.
	 * @throws SQLException 
	 */
	public OperatorDB getOperator(int id) throws SQLException {
		return ed.getOperator(id);
	}
	
	/**
	 * Returns a list of the operators currently registered with the experiment.  Can only be done in setup phase.
	 * @return a list of the operators currently registered with the experiment.
	 * @throws SQLException
	 */
	public List<OperatorDB> getOperators() throws SQLException {
		return ed.getOperators();
	}
	
	/**
	 * Removes an operator from the experiment.  Can only be done in setup phase.
	 * @param id The id of the operator to remove.
	 * @return If removed.
	 * @throws SQLException
	 * @throws IllegalStateException
	 */
	public boolean removeOperator(int id) throws SQLException, IllegalStateException {
		if(ed.getCurrentStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Can not remove operator after generation phase is complete.");
		} else {
			return ed.deleteOperator(id);
		}
	}
	
//-- Mutators ------------------------------------------------------------------------------------------------------------	
	
	/**
	 * Adds a mutator to the experiment.  Can only be done in setup phase.
	 * @param description Description of the mutator.
	 * @param oplist The list of operators specified by their database id.
	 * @return The mutator.
	 * @throws IllegalStateException If called after generation is complete.
	 * @throws SQLException
	 */
	public MutatorDB addMutator(String description, List<Integer> oplist) throws SQLException, IllegalStateException, IllegalArgumentException {
		if(ed.getCurrentStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Can not add mutator after setup phase is complete.");
		} else {
			return ed.createMutator(description, oplist);
		}
	}
	
	/**
	 * Returns the mutator for the experiment with the given id.  Can only be done in setup phase.
	 * @param id the id of the mutator to retrieve.
	 * @return the mutator for the experiment with the given id.
	 * @throws SQLException 
	 */
	public MutatorDB getMutator(int id) throws SQLException {
		return ed.getMutator(id);
	}
	
	/**
	 * Returns the mutators currently registered with the experiment.  Can only be done in setup phase.
	 * @return the mutators currently registered with the experiment.
	 * @throws SQLException
	 */
	public List<MutatorDB> getMutators() throws SQLException {
		return ed.getMutators();
	}
	
	/**
	 * Removes a mutator from the experiment.  Can only be done in setup phase.
	 * @param id The mutator to remove.
	 * @return If a mutator was removed (false if the id does not corresponding to an existing mutator).
	 * @throws IllegalStateException
	 * @throws SQLException
	 */
	public boolean removeMutator(int id) throws IllegalStateException, SQLException, IllegalArgumentException {
		if(ed.getCurrentStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Can not remove mutator after setup phase is complete.");
		} else {
			return ed.deleteMutator(id);
		}
	}
	
	/**
	 * Removes all mutators from the experiment.  Can only be done in setup phase.
	 * @return The number of mutators removed.
	 * @throws IllegalStateException
	 * @throws SQLException
	 */
	public int removeMutators() throws IllegalStateException, SQLException, IllegalArgumentException {
		if(ed.getCurrentStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new IllegalStateException("Can not remove mutator after setup phase is complete.");
		} else {
			return ed.deleteMutators();
		}
	}

//-- Tools -------------------------------------------------------------------------------------------------------------
	
	public ToolDB addTool(String name, String description, Path install_directory, Path tool_runner) throws SQLException, IllegalArgumentException {
	//Check Arguments
		Objects.requireNonNull(name);
		Objects.requireNonNull(description);
		Objects.requireNonNull(install_directory);
		Objects.requireNonNull(tool_runner);
		
		if(!Files.exists(install_directory)) {
			throw new IllegalArgumentException("Tool install directory does not exist.");
		}
		if(!Files.isDirectory(install_directory)) {
			throw new IllegalArgumentException("Tool install directory is not a directory");
		}
		
		if(!Files.exists(tool_runner)) {
			throw new IllegalArgumentException("Tool runner does not exist.");
		}
		if(!Files.isReadable(tool_runner)) {
			throw new IllegalArgumentException("Tool runner is not a regular file.");
		}
		if(!Files.isExecutable(tool_runner)) {
			throw new IllegalArgumentException("Tool runner is not executable.");
		}
	
	//Add Tool
		return ed.createTool(name, description, install_directory, tool_runner);
	}
	
	/**
	 * Removes a tool from the database (as well as its Clone Detection Reports, Unit Recalls, and Unit Precision data).
	 * @param id The id of the tool to remove.
	 * @return True if the tool was removed, false if there is not a tool with the given id.
	 * @throws SQLException Error communicating with database.
	 * @throws IOException IO Error while deleting tool's data from output directory.
	 */
	public boolean removeTool(int id) throws SQLException, IOException {
		return ed.deleteTool(id);
	}
	
	/**
	 * Returns a list of the tools in the experiment.
	 * @return a list of the tools in the experiment.
	 * @throws SQLException
	 */
	public List<ToolDB> getTools() throws SQLException {
		return ed.getTools();
	}
		
	
//-- Generation ----------------------------------------------------------------------------------------------------------	
	
	//-- Public Interface ------------------------------------------------------------------------------------------------
	/**
	 * 
	 * @return
	 * @throws SQLException
	 * @throws AlreadyGeneratedException
	 */
	public boolean generateAutomatic() throws SQLException, AlreadyGeneratedException {
		if(ed.getCurrentStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new AlreadyGeneratedException();
		} else {
			boolean bretval;
			
			//Assert Generation Type
			ed.setGenerationType(ExperimentSpecification.AUTOMATIC_GENERATION_TYPE);
			
			//Progress to generation stage
			ed.nextStage();
			assert(ed.getCurrentStage() == ExperimentData.GENERATION_STAGE);
			
			//Generate Clones
			bretval = generateClones();
			if(bretval == false) {
				log.println("[" + Calendar.getInstance().getTime() + "] (generateAutomatic): " + "Error: Artifical clone generation failed.  See previous error messages.");
				return false;
			}
			
			//Create Mutant Bases
			bretval = this.createMutantBases();
			if(bretval == false) {
				log.println("[" + Calendar.getInstance().getTime() + "] (generateAutomatic): " + "Error: Mutant base creation failed.  See previous error messages.");
				return false;
			}
			
			//Verify Generation
			bretval = this.verifyAutomaticGeneration();
			if(bretval == false) {
				log.println("[" + Calendar.getInstance().getTime() + "] (generateAutomatic): " + "Error: Generation verification failed.  See previous error messages.");
				return false;
			}
			
			//Progress to evaluation setup stage
			ed.nextStage();
			assert(ed.getCurrentStage() == ExperimentData.EVALUATION_SETUP_STAGE);
			
			//Full process succeeded
			return true;
		}
	}
	
	
	public boolean generateManual(Path manual_spec) throws SQLException, AlreadyGeneratedException, FileNotFoundException {
		log.println("[" + Calendar.getInstance().getTime() + "] (generateManual): " + "Start: Generating manual clones.");
		Objects.requireNonNull(manual_spec);
		if(!Files.exists(manual_spec)) {
			throw new FileNotFoundException("Specified manual clone file does not exist.");
		}
		if(!Files.isRegularFile(manual_spec)) {
			throw new IllegalArgumentException("Specified manual clone file is not a regular file.");
		}
		if(!Files.isReadable(manual_spec)) {
			throw new IllegalArgumentException("Specified manual clone file is not readable.");
		}
		if(ed.getCurrentStage() != ExperimentData.GENERATION_SETUP_STAGE) {
			throw new AlreadyGeneratedException();
		} else {
			boolean bretval;
			
			//Assert Generation Type
			ed.setGenerationType(ExperimentSpecification.MANUAL_GENERATION_TYPE);
			
			//Progress to generation stage
			ed.nextStage();
			assert(ed.getCurrentStage() == ExperimentData.GENERATION_STAGE);
			
			//Generate Clones
			//bretval = importClones(manual_spec);
			//if(bretval == false) {
			//	log.println("[" + Calendar.getInstance().getTime() + "] (generateManual): " + "Error: Manual clone import failed.  See previous error messages.");
			//	return false;
			//}
			
			//Create Mutant Bases
			bretval = this.createMutantBases();
			if(bretval == false) {
				log.println("[" + Calendar.getInstance().getTime() + "] (generateManual): " + "Error: Mutant base creation failed.  See previous error messages.");
				return false;
			}
			
			//Verify Generation
			bretval = this.verifyManualGeneration();
			if(bretval == false) {
				log.println("[" + Calendar.getInstance().getTime() + "] (generateManual): " + "Error: Generation verification failed.  See previous error messages.");
				return false;
			}
			
			//Full process succeeded
			log.println("[" + Calendar.getInstance().getTime() + "] (generateManual): " + "End: Generating manual clones.");
			return true;
		}
	}
	
	private boolean importClones(Path manual_spec) throws FileNotFoundException, IllegalArgumentException, IllegalManualImportSpecification, SQLException {
		log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Start: Importing manual clones.");
		Objects.requireNonNull(manual_spec);
		if(!Files.exists(manual_spec)) {
			throw new FileNotFoundException("Specified manual clone file does not exist.");
		}
		if(!Files.isRegularFile(manual_spec)) {
			throw new IllegalArgumentException("Specified manual clone file is not a regular file.");
		}
		if(!Files.isReadable(manual_spec)) {
			throw new IllegalArgumentException("Specified manual clone file is not readable.");
		}
		
	//Get Clone Specification From File
		List<Path> fragments = new ArrayList<Path>();
		List<Path> mfragments = new ArrayList<Path>();
		Scanner fs = new Scanner(manual_spec.toFile());
		int linenum = 0;
		int clonenum = 0;
		while(fs.hasNextLine()) {
			linenum++;
			
			//Read paths
			String spath1;
			String spath2;
			spath1 = fs.nextLine();
			if(fs.hasNextLine()) { 
				spath2 = fs.nextLine();
			} else {
				fs.close();
				throw new IllegalManualImportSpecification("Error on line " + linenum + ".");
			}
			
			//Store and validate path
			clonenum++;
			Path fragment, mfragment;
			try {
				fragment = Paths.get(spath1).toAbsolutePath().normalize();
			} catch (InvalidPathException e) {
				fs.close();
				throw new IllegalManualImportSpecification("Fragment 1 from clone " + clonenum + " is not a valid path.");
			}
			try {
				mfragment = Paths.get(spath2).toAbsolutePath().normalize();
			} catch (InvalidPathException e) {
				fs.close();
				throw new IllegalManualImportSpecification("Fragment 2 from clone " + clonenum + " is not a valid path.");
			}
			fragments.add(fragment);
			mfragments.add(mfragment);
			
			//clear delimeter line
			if(fs.hasNextLine())
				fs.nextLine();
				
		}
		fs.close();
		
	//Check Clones
		for(int i = 0; i < fragments.size(); i ++) {
			Path f = fragments.get(i);
			Path mf = mfragments.get(i);
			
			//Validate Fragment
			if(!Files.exists(f)) {
				log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: Fragment 1 of clone " + i + " does not exist.");
				return false;
			}
			if(!Files.isRegularFile(f)) {
				log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: Fragment 1 of clone " + i + " is not a regular.");
				return false;
			}
			try {
				if(!TXLUtil.isFragmentType(f, ed.getFragmentType())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: Fragment 1 of clone " + i + " is not correct fragment type.");
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: IO exception while checking fragment 1 of clone " + i + ".");
				return false;
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: A dependent process was interrupted while checking fragment 1 of clone " + i + ".");
				return false;
			}
			
			//Validate Mutant Fragment
			if(!Files.exists(mf)) {
				throw new IllegalManualImportSpecification("Fragment 2 of clone " + i + " does not exist.");
			}
			if(!Files.isRegularFile(mf)) {
				throw new IllegalManualImportSpecification("Fragment 2 of clone " + i + " is not a regular file.");
			}
			try {
				if(!TXLUtil.isFragmentType(mf, ed.getFragmentType())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: Fragment 2 of clone " + i + " is not correct fragment type.");
					return false;
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: IO exception while checking fragment 2 of clone " + i + ".");
				return false;
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: A dependent process was interrupted while checking fragment 2 of clone " + i + ".");
				return false;
			}
		}
		
	//Import into experiment and database
		ed.deleteOperators();
		ed.deleteMutators();
		OperatorDB operatorDB = ed.createOperator("ManualOperator", "Pretend Operator for manaul clones.", 1, Paths.get("operators/mNC"));
		List<Integer> ops = new LinkedList<Integer>();
		ops.add(operatorDB.getId());
		
		//Adding Clones to Database (fragment 1 as fragment, fragment 2 as mutant fragment, and a new mutator for each imported clone)
		for(int i = 0; i < fragments.size(); i ++) {
			log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Importing manual clone " + (i+1) + " into the experiment.");

			Path f = fragments.get(i);
			Path mf = mfragments.get(i);
			
			//Mutator for this clone
			MutatorDB mutatorDB = ed.createMutator("Clone#1", ops);
			
			//Fragment (fragment1)
			FragmentDB fragment;
			try {
				fragment = ed.createFragment(new Fragment(f, 1, FileUtil.countLines(f)));
			} catch (IOException e) {
				e.printStackTrace();
				log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: IO exception when adding fragment 1 of clone " + (i+1) + " into the experiment.");
				return false;
			}
			
			//Mutant Fragment (fragment2)
			MutantFragment mfragment;
			try {
				mfragment = ed.createMutantFragment(fragment.getId(), mutatorDB.getId(), mf);
			} catch (IOException e) {
				e.printStackTrace();
				log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "Error: IO exception when adding fragment 2 of clone " + (i+1) + " into the experiment.");
				return false;
			}
		}
		
	//Return success
		log.println("[" + Calendar.getInstance().getTime() + "] (importClones): " + "End: Importing manual clones.");
		return true;
	}
	
//-- Automatic Generate Clones Logic --------------------------------------------------------------------------------------------
	
	private boolean generateClones() throws SQLException {
		log.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Start: Generating artifical clones.");
	
	// Get needed information from database	
		int fragmentType = ed.getFragmentType();
		int language = ed.getLanguage();
		int maxFragments = ed.getMaxFragments();
		int minSizeLines = ed.getFragmentMinimumSizeLines();
		int maxSizeLines = ed.getFragmentMaximumSizeLines();
		int minSizeTokens = ed.getFragmentMinimumSizeTokens();
		int maxSizeTokens = ed.getFragmentMaximumSizeTokens();
		double containment = ed.getMutationContainment();
		double allowedDifference = ed.getAllowedFragmentDifference();
		int numAttemptsMutator = ed.getMutationAttempts();
		int numAttemptsOperator = ed.getOperatorAttempts();
		List<MutatorDB> mutators = ed.getMutators(); // Mutators to use
		if(mutators.size() == 0) {
			System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: No mutators specified in database, artifical clone generation can not continue.");
			return false;
		}
		
	// Extract Fragments From Repository
		List<Fragment> fragments;
		System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Extracting fragment candidates from repository... ");
		if(fragmentType == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
			fragments = SelectFunctionFragments.getFragments(ed.getRepositoryPath().toFile(), language);
		} else if(fragmentType == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
			fragments = SelectBlockFragments.getFragments(ed.getRepositoryPath().toFile(), language);
		} else {
			log.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Fragment type invalid.  Could not generate artifical clones.  Either logic is not up to date with advertised fragment types, or fragment type was not properly validated previously.");
			return false;
		}
		
		if(fragments == null) {
			System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Failed to extract fragments from repository.  Is NiCad dependency set up (compiled)?  Did Extract script process get interrupted/killed?");
			return false;
		}
		System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "\t" + fragments.size() + " fragments successfully extracted.");
		
	// Select fragments, mutate them with all the defined mutators, if succeeds add to database, otherwise proceed.	
		System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Selecting and mutating fragments.");
		
		int numGeneratedClones = 0;
		Random rdm = new Random(); //Random number generator for choosing fragments
		Fragment selectedFragment; //Selected fragment
		Path tmpFragmentFile; //Temp file to store fragment text
		
generateloop:
		while(numGeneratedClones != maxFragments && fragments.size() != 0) {
			//Select A Fragment Randomly
			selectedFragment = fragments.remove(rdm.nextInt(fragments.size()));
			
			//Export fragment to a temporary file
			try {
				tmpFragmentFile = Files.createTempFile(ed.getTemporaryPath(), "generate_tempFragmentFile", null);
			} catch (IOException e) {
				System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Failed to create temporary file in data directory.  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
				return false;
			}
			try {
				FragmentUtil.extractFragment(selectedFragment, tmpFragmentFile);
			} catch (FileNotFoundException e) {
				System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Failed to export selected fragment to temporary file (File not found?!).  Have you touched the output directory?  Is it R/Wable? Else a bug...");
				try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
				return false;
			} catch (IOException e) {
				System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Failed to export selected fragment to temporary file (IOException).  Have you touched the output directory?  Is it R/Wable? Else a bug...");
				try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
				return false;
			}
			
			//check if fragment is appropriate, if not continue next loop iteration
			int numLinesFragment;
			int numTokensFragment;
			try {
				//Check fragment is of type specified, if yes continue, if no delete and continue next loop iteration
				if(fragmentType == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
					try {
						if(!TXLUtil.isFunction(tmpFragmentFile, language)) {
							try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
							continue generateloop;
						}
					} catch (InterruptedException e) {
						try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
						e.printStackTrace();
						System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: While checking extracted fragment's type, TXL process was interrupted unexpectantly.");
						return false;
					}
				} else if (fragmentType == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
					try {
						if(!TXLUtil.isBlock(tmpFragmentFile, language)) {
							try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
							continue generateloop;
						}
					} catch (InterruptedException e) {
						try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
						e.printStackTrace();
						System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: While checking extracted fragment's type, TXL process was interrupted unexpectantly.");
						return false;
					}
				} else {
					log.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Fragment type invalid.  Could not generate artifical clones.  Either logic is not up to date with advertised fragment types, or fragment type was not properly validated previously.");
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					return false;
				}
				
				//Check Length (lines) requirement
				numLinesFragment = FileUtil.countLines(tmpFragmentFile);
				if(numLinesFragment < minSizeLines || numLinesFragment > maxSizeLines) {
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					continue generateloop;
				}
				
				//Check Length (tokens) requirement
				try {
					numTokensFragment = FileUtil.countTokens(tmpFragmentFile, language);
				} catch (InterruptedException e) {
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					e.printStackTrace();
					System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: While checking length of fragment, a required process was unexpectantly interrupted.");
					return false;
				}
				if(numTokensFragment < minSizeTokens || numTokensFragment > maxSizeTokens) {
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					continue generateloop;
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: IO error occured while accessing temp fragment file (IOException).  Have you touched the output directory?  Is it R/Wable? Else a bug...");
				try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
				return false;
			} catch (TXLException e) { //Could not parse fragment, don't use it
				//e.printStackTrace();
				//System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: TXL error occured while accessing temp fragment file (IOException).");
				try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
				continue generateloop;
			}
			
			
			//Perform Mutations, if problem found in mutation, restart generation loop for new fragment
			List<Path> mutantfiles = new LinkedList<Path>(); //Store mutants crated, in same order as mutator list
			for(MutatorDB m : mutators) {
				//Prepare an exclusive file to mutate to
				Path mutant;
				try {
					mutant = Files.createTempFile(ed.getTemporaryPath(), "MutatorOutput_" + m.getId() + "-", null);
				} catch (IOException e) {
					System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Failed to create mutant file (IOException).  Have you touched the output directory?  Is it R/Wable? Else a bug...");
					for(Path p : mutantfiles) {
						try {Files.deleteIfExists(p);} catch(IOException ee) {}
					}
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					return false;
				}
				mutantfiles.add(mutant);
				
				//Perform Mutation
				int mutationRetval;
				try {
					mutationRetval = m.performMutation(tmpFragmentFile, mutant, numAttemptsMutator, numAttemptsOperator, allowedDifference, containment, language);
				} catch (IOException e) {
					System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: IO error during mutation.  Have you touched the output directory?  Is it R/Wable? Else a bug...");
					for(Path p : mutantfiles) {
						try {Files.deleteIfExists(p);} catch(IOException ee) {}
					}
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					return false;
				} catch (InterruptedException e) {
					System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Mutator process was interrupted.  Did you force kill it?");
					for(Path p : mutantfiles) {
						try {Files.deleteIfExists(p);} catch(IOException ee) {}
					}
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					return false;
				}
				
				//Check if mutation was successful (if not clean up and reiterate)
				if(mutationRetval != 0) {
					for(Path p : mutantfiles) {
						try {Files.deleteIfExists(p);} catch(IOException ee) {}
					}
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					continue generateloop;
				}
				
				//Check if mutant is appropraite
				int numLinesMutant;
				int numTokensMutant;
				try {
					//Check Mutant is of type specified
					if(fragmentType == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
						try {
							if(!TXLUtil.isFunction(mutant, language)) {
								for(Path p : mutantfiles) {
									try {Files.deleteIfExists(p);} catch(IOException ee) {}
								}
								try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
								continue generateloop;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: While checking mutant fragment type, a required process was unexpectantly interrupted.");
							for(Path p : mutantfiles) {
								try {Files.deleteIfExists(p);} catch(IOException ee) {}
							}
							try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
							return false;
						}
					} else if (fragmentType == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
						try {
							if(!TXLUtil.isBlock(mutant, language)) {
								for(Path p : mutantfiles) {
									try {Files.deleteIfExists(p);} catch(IOException ee) {}
								}
								try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
								continue generateloop;
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: While checking length of fragment, a required process was unexpectantly interrupted.");
							for(Path p : mutantfiles) {
								try {Files.deleteIfExists(p);} catch(IOException ee) {}
							}
							try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
							return false;
						}
					} else {
						log.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Fragment type invalid.  Could not generate artifical clones.  Either logic is not up to date with advertised fragment types, or fragment type was not properly validated previously.");
						for(Path p : mutantfiles) {
							try {Files.deleteIfExists(p);} catch(IOException ee) {}
						}
						try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
						return false;
					}
					
					//Check Length (lines)
					numLinesMutant = FileUtil.countLines(mutant);
					if(numLinesMutant < minSizeLines || numLinesMutant > maxSizeLines) {
						for(Path p : mutantfiles) {
							try {Files.deleteIfExists(p);} catch(IOException ee) {}
						}
						try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
						continue generateloop;
					}
					
					//Check Length (tokens)
					try {
						numTokensMutant = FileUtil.countTokens(mutant, language);
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: While checking mutant token length, a required process was unexpectantly interrupted.");
						for(Path p : mutantfiles) {
							try {Files.deleteIfExists(p);} catch(IOException ee) {}
						}
						try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
						return false;
					}
					if(numTokensMutant < minSizeTokens || numTokensMutant > maxSizeTokens) {
						for(Path p : mutantfiles) {
							try {Files.deleteIfExists(p);} catch(IOException ee) {}
						}
						try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
						continue generateloop;
					}
				} catch (IOException e) {
					System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: IO error occured while accessing mutant fragment file (IOException).  Have you touched the output directory?  Is it R/Wable? Else a bug...");
					for(Path p : mutantfiles) {
						try {Files.deleteIfExists(p);} catch(IOException ee) {}
					}
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					return false;
				} catch (TXLException e) {
					for(Path p : mutantfiles) {
						try {Files.deleteIfExists(p);} catch(IOException ee) {}
					}
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					continue generateloop;
				}
			}
			
			//Mutations all succeeded, save artificial clones
				//Add Fragment
			FragmentDB fragmentDB;
			try {
				fragmentDB = ed.createFragment(selectedFragment);
			} catch (FileNotFoundException e) {
				System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: Fragment source file dissapeared.  Have you touched the output directory?  Is it R/Wable? Else a bug...");
				for(Path p : mutantfiles) {
					try {Files.deleteIfExists(p);} catch(IOException ee) {}
				}
				try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
				return false;
			} catch (IOException e) {
				System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: IO error reading fragment source file or writing fragment to output directory.  Have you touched the output directory?  Is it R/Wable? Else a bug...");
				for(Path p : mutantfiles) {
					try {Files.deleteIfExists(p);} catch(IOException ee) {}
				}
				try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
				return false;
			}
			
				//Add mutants
			int fragmentId = fragmentDB.getId();
			for(int index = 0; index < mutators.size(); index++) {
				MutatorDB m = mutators.get(index);
				try {
					ed.createMutantFragment(fragmentId, m.getId(), mutantfiles.get(index));
				} catch (IOException e) {
					System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: IO error importing mutant fragment to output directory.  Have you touched the output directory?  Is it R/Wable? Else a bug...");
					for(Path p : mutantfiles) {
						try {Files.deleteIfExists(p);} catch(IOException ee) {}
					}
					try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
					return false;
				}
			}
			
			//cleanup
			for(Path p : mutantfiles) {
				try {Files.deleteIfExists(p);} catch(IOException ee) {}
			}
			try {Files.deleteIfExists(tmpFragmentFile);} catch(IOException ee) {}
			
			//Success loop, iterate
			numGeneratedClones++;
		}
		// Reached here means success!
		log.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Artifical clone generation complete.  " + numGeneratedClones*mutators.size()  + " artifical clones generated from " + numGeneratedClones + " fragments.");
		log.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "End: Generate Artifical Clones.");
		return true;
	}

//-- Create Mutant Base For Automatic Clone Generation (function/block) ----------------------------------------------- 
	
	private  boolean createMutantBases() throws SQLException {
		log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Start: Creating mutant bases.");

		int numinjections = ed.getInjectionNumber();
		List<FragmentDB> fragments = ed.getFragments();
		int numfragments = fragments.size();
		int fragmentType = ed.getFragmentType();
		Path systempath = ed.getSystemPath();
		int language = ed.getLanguage();
		
		InjectionLocationChooser injectChooser;
		if(fragmentType == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
			try {
				injectChooser = new AfterFunctionInjectionLocationChooser(systempath, language);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: While creating an AfterFunctionInjectionLocationChooser, the system could not be found (system directory does not exist).");
				return false;
			}
		} else if (fragmentType == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
			try {
				injectChooser = new BlockInjectionLocationChooser(systempath, language);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: While creating an AfterFunctionInjectionLocationChooser, the system could not be found (system directory does not exist).");
				return false;
			}
		} else {
			log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: Fragment type invalid.  Could not create mutant bases.  Either logic is not up to date with advertised fragment types, or fragment type was not properly validated previously.");
			return false;
		}
		
		//For each fragment
		for(int fragmentN = 0; fragmentN < numfragments; fragmentN++) {
			//Reset no repeat on injection location chooser (dont allow repeats within inject#s)
			injectChooser.reset();
			
			//For each injections (in case multiple injections)
			for(int injectN = 0; injectN < numinjections; injectN++) {
				//keep trying to make a mutant base until succeed
createbase_attempt:
				while(true) {
					//Get Needed Data
					FragmentDB fragment = fragments.get(fragmentN);
					int fragmentId = fragment.getId();
					List<MutantFragment> mutants = ed.getMutantFragments(fragmentId);//mutant fragments for this fragment
					
					LinkedList<MutantBase> mbases = new LinkedList<MutantBase>();//temporary store before push to database.
					
					log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Creating mutant bases for injection " + (injectN+1) + " of " + (numinjections) + " for fragment " + fragmentId);
					
					//Select Injection Locations
						//fragment
					InjectionLocation fragmentInjectLocation;
					try {
						fragmentInjectLocation = injectChooser.generateInjectionLocation();
					} catch (FileNotFoundException e) {
						e.printStackTrace();
						log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: While choosing the fragment injection location, a file that sould be in the system could not be found.  Have you touched the output directory?");
						return false;
					} catch (IOException e) {
						e.printStackTrace();
						log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: While choosing the fragment injection location, an IO error occured while reading a file.  Have you touched the output directory?");
						return false;
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: While choosing the fragment injection location, a required process was unexpectantly interrupted.");
						return false;
					}
					if(fragmentInjectLocation == null) {
						log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: Could not get an injection location.  Does system contain at least two source files of the selected language?  Each with at least one fragment of the fragment type?");
						return false;
					}
						//mutant (can't be in same source file!)
					InjectionLocation mutantInjectLocation;
					do {
						try {
							mutantInjectLocation = injectChooser.generateInjectionLocation();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: While choosing the mutant injection location, a file that sould be in the system could not be found.  Have you touched the output directory?");
							return false;
						} catch (IOException e) {
							e.printStackTrace();
							log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: While choosing the mutant injection location, an IO error occured while reading a file.  Have you touched the output directory?");
							return false;
						} catch (InterruptedException e) {
							e.printStackTrace();
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: While choosing the fragment injection location, a required process was unexpectantly interrupted.");
							return false;
						}
						if(mutantInjectLocation == null) {
							log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "Error: Could not get an injection location.  Does system contain at least two source files of the selected language?  Each with at least one fragment of the fragment type?");
							return false;
						} 
					} while(mutantInjectLocation.getSourceFile().toAbsolutePath().normalize().equals(fragmentInjectLocation.getSourceFile().toAbsolutePath().normalize()));
					
					//Make mutant bases (before add to database)
					for(MutantFragment mf : mutants) {
						Path osrcfile = fragmentInjectLocation.getSourceFile().toAbsolutePath().normalize();
						int ostartline = fragmentInjectLocation.getLineNumber();
						int oendline;
						try {
							oendline = ostartline + FileUtil.countLines(fragment.getFragmentFile()) - 1;
						} catch (IOException e) {
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: IO error reading fragment file (counting lines).  Have you touched the output directory?  Is it R/Wable? Else a bug...");
							e.printStackTrace();
							return false;
						}
						
						Path msrcfile = mutantInjectLocation.getSourceFile().toAbsolutePath().normalize();
						int mstartline = mutantInjectLocation.getLineNumber();
						int mendline;
						try {
							mendline = mstartline + FileUtil.countLines(mf.getFragmentFile()) - 1;
						} catch (IOException e) {
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Error: IO error reading mutant file (counting lines).  Have you touched the output directory?  Is it R/Wable? Else a bug...");
							e.printStackTrace();
							return false;
						}
						
						MutantBase mb = new MutantBase(systempath, mf.getId(),
								new Fragment(osrcfile, ostartline, oendline), 
								new Fragment(msrcfile, mstartline, mendline));
						mbases.add(mb);
					}
					
					//Check Bases
					for(MutantBase mb : mbases) {
						MutantBase nmb;
						try {
							nmb = ed.constructBase(mb);
						} catch (FileNotFoundException e) {
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to construct mutant base (for checking), file that should be there was not found.  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
							e.printStackTrace();
							return false;
						} catch (IOException e) {
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to construct mutant base (for checking), an IO exception occured while copying/editing the system's files.  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
							e.printStackTrace();
							return false;
						}
						
						
						Path tmp1=null;
						Path tmp2=null;
						try {
							tmp1 = Files.createTempFile(ed.getTemporaryPath(), "experiment_createMutantBase_testInjection", null);
							tmp2 = Files.createTempFile(ed.getTemporaryPath(), "experiment_createMutantBase_testInjection", null);
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to create temporary files for checking mutant base, an IO exception occured.  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
							if(tmp1 != null) try {Files.deleteIfExists(tmp1);} catch (Exception ee){}
							//if(tmp2 != null) try {Files.deleteIfExists(tmp2);} catch (Exception ee){}
							return false;
						}
						
						//edited file containing original fragment is parseable?
						try {
							if(!TXLUtil.prettyPrintSourceFile(nmb.getOriginalFragment().getSrcFile(), tmp1, language)) {
								try {
									Files.deleteIfExists(tmp1);
									Files.deleteIfExists(tmp2);
								} catch (IOException e) {
									System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to clean up after mutant base check (IOException deleting temp files).  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
									e.printStackTrace();
									return false;
								}
								continue createbase_attempt;
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
							if(tmp1 != null) try {Files.deleteIfExists(tmp1);} catch (Exception ee){}
							if(tmp2 != null) try {Files.deleteIfExists(tmp2);} catch (Exception ee){}
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "While checking file with injected fragment has valid syntax, a required process was unexpectedly interrupted.");
							return false;
						} catch (IOException e1) {
							e1.printStackTrace();
							if(tmp1 != null) try {Files.deleteIfExists(tmp1);} catch (Exception ee){}
							if(tmp2 != null) try {Files.deleteIfExists(tmp2);} catch (Exception ee){}
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "While checking file with injected fragment has valid syntax, an IO error occured with the TXL process.");
							return false;
						}
						
						//edited file containing mutant fragment is parseable?
						try {
							if(!TXLUtil.prettyPrintSourceFile(nmb.getMutantFragment().getSrcFile(), tmp2, language)) {
								try {
									Files.deleteIfExists(tmp1);
									Files.deleteIfExists(tmp2);
								} catch (IOException e) {
									System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to clean up after mutant base check (IOException deleting temp files).  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
									e.printStackTrace();
									return false;
								}
								continue createbase_attempt;
							}
						} catch (InterruptedException e1) {
							e1.printStackTrace();
							if(tmp1 != null) try {Files.deleteIfExists(tmp1);} catch (Exception ee){}
							if(tmp2 != null) try {Files.deleteIfExists(tmp2);} catch (Exception ee){}
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "While checking file with injected fragment has valid syntax, a required process was unexpectedly interrupted.");
							return false;
						} catch (IOException e1) {
							e1.printStackTrace();
							if(tmp1 != null) try {Files.deleteIfExists(tmp1);} catch (Exception ee){}
							if(tmp2 != null) try {Files.deleteIfExists(tmp2);} catch (Exception ee){}
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "While checking file with injected fragment has valid syntax, an IO error occured with the TXL process.");
							return false;
						}
						
						//Cleanup
						try {
							Files.deleteIfExists(tmp1);
							Files.deleteIfExists(tmp2);
						} catch (IOException e) {
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to clean up after mutant base check (IOException deleting temp files).  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
							e.printStackTrace();
							return false;
						}
					}
					
					//add to database
					for(MutantBase mb : mbases) {
						try {
							ed.createMutantBase(mb.getOriginalFragment().getSrcFile(), mb.getOriginalFragment().getStartLine(), 
									mb.getMutantFragment().getSrcFile(), mb.getMutantFragment().getStartLine(),
									mb.getMutantId());
						} catch (FileNotFoundException e) {
							e.printStackTrace();
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to add mutant base to database (FileNotFoundException).  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
							return false;
						} catch (IOException e) {
							e.printStackTrace();
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to add mutant base to database (IOException).  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
							return false;
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							System.out.println("[" + Calendar.getInstance().getTime() + "] (generateClones): " + "Failed to add mutant base to database (IllegalArgumentException): files where framgnets are inserted are either not in system directory (bug) or no longer exist (bug or you modified the output directory).  Have you touched the output directory?  Is it R/Wable?  Else a bug...");
							return false;
						}
					}
					
					break createbase_attempt;
				}
			}
		}
		
		log.println("[" + Calendar.getInstance().getTime() + "] (createMutantBases): " + "End: Creating mutant bases.");
		return true;
	}
	
	public boolean verifyManualGeneration() throws SQLException {
		try {
			log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Start: Verifying generated data.");
				
			ExperimentData ed = this.getExperimentData();
			List<MutatorDB> mutators = ed.getMutators();
		
		//Fragments
			//TODO
			
		//Mutant Fragments
			//TODO
			
		//Mutant Bases
			log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tVerifying mutantbases...");
			List<MutantBaseDB> mutantbases = ed.getMutantBases();
			
			//Check Mutant Bases (individually)
			for(MutantBaseDB mutantbase : mutantbases) {
				log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\t\tChecking mutant base " + mutantbase.getId() + ".");
			//Requied Data
				MutantFragment mutantfragment = ed.getMutantFragment(mutantbase.getMutantId());
				FragmentDB fragment = ed.getFragment(mutantfragment.getFragmentId());
				
			//Construct Base
				ed.constructBase(mutantbase.getId());
				
			//Tmp Files
				Path extractedFragment = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				Path extractedMutant = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				Path checkfile = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				
			//Check Injected Files Valid (txl pretty print)		
				if(!TXLUtil.prettyPrintSourceFile(mutantbase.getOriginalFragment().getSrcFile(), checkfile, ed.getLanguage())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") File which original fragment was injected into is invalid (improper syntax).");
					return false;
				}
				if(!TXLUtil.prettyPrintSourceFile(mutantbase.getMutantFragment().getSrcFile(), checkfile, ed.getLanguage())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") File which mutant fragment was injected into is invalid (improper syntax).");
					return false;
				}
				
			//Check Injections (match stored versions)
				FragmentUtil.extractFragment(mutantbase.getOriginalFragment(), extractedFragment);
				FragmentUtil.extractFragment(mutantbase.getMutantFragment(), extractedMutant);
				if(!(FileUtils.contentEquals(extractedFragment.toFile(), fragment.getFragmentFile().toFile()))) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Original fragment was no injected properly (tried to extract it and it does not match original fragment file in output directory).");
					return false;
				}
				if(!(FileUtils.contentEquals(extractedMutant.toFile(), mutantfragment.getFragmentFile().toFile()))) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Mutant fragment was no injected properly (tried to extract it and it does not match mutant fragment file in output directory).");
					return false;
				}
				
				
			//Check Mutant Base Contents (match original, except for modified)
				Path psystem = ed.getSystemPath().toAbsolutePath().normalize();
				Path pmutantbase = ed.getMutantBasePath().toAbsolutePath().normalize();
				Path fragmentifile = mutantbase.getOriginalFragment().getSrcFile().toAbsolutePath().normalize();
				Path mutantifile = mutantbase.getMutantFragment().getSrcFile().toAbsolutePath().normalize();
				List<Path> ofiles = FileUtil.fileInventory(ed.getSystemPath());
				List<Path> odirs = FileUtil.directoryInventory(ed.getSystemPath());
				List<Path> mbfiles = FileUtil.fileInventory(ed.getMutantBasePath());
				List<Path> mbdirs = FileUtil.directoryInventory(ed.getMutantBasePath());
				
				//Check Directories
					//Mutant base has all the directoreis from original
				for(Path path : odirs) {
					Path normalized = pmutantbase.resolve(psystem.relativize(path)).toAbsolutePath().normalize();
					if(!mbdirs.contains(normalized)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Is missing a directory from the original system: " + path + ".");
						return false;
					}
				}
					//Mutant base does not have directories not in original
				for(Path path : mbdirs) {
					Path normalized = psystem.resolve(pmutantbase.relativize(path)).toAbsolutePath().normalize();
					if(!odirs.contains(normalized)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Has a directory not from the original system: " + path + ".");
						return false;
					}
				}
				
				//Check Files
					//Mutant Base has all files it should, and contents are equal (except for altered)
				for(Path path : ofiles) {
					Path normalized = pmutantbase.resolve(psystem.relativize(path)).toAbsolutePath().normalize();
					if(!mbfiles.contains(normalized)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Is missing a file from the original system: " + path + ".");
						return false;
					}
					if(!normalized.equals(fragmentifile) && !normalized.equals(mutantifile)) {
						if(!FileUtils.contentEquals(path.toFile(), normalized.toFile())) {
							log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Has a file that was modified that shouldn't have been (wasn't one of the files injected into): " + normalized + ".");
							return false;
						}
					}
				}
					//Mutant base should not have extra files
				for(Path path : mbfiles) {
					Path normalized = psystem.resolve(pmutantbase.relativize(path)).toAbsolutePath().normalize();
					if(!ofiles.contains(normalized)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Has a file not from the original system: " + path + ".");
						return false;
					}
				}
			}
			
			//Check Mutant Bases injection locations
				//build hash map containing the mutant bases per fragment
			HashMap<Integer,List<MutantBaseDB>> mutantBasesPerFragment = new HashMap<Integer,List<MutantBaseDB>>();
			for(MutantBaseDB mutantbase : mutantbases) {
				MutantFragment mutant = ed.getMutantFragment(mutantbase.getMutantId());
				FragmentDB fragment = ed.getFragment(mutant.getFragmentId());
				if(!mutantBasesPerFragment.containsKey(fragment.getId())) {
					List<MutantBaseDB> list = new LinkedList<MutantBaseDB>();
					list.add(mutantbase);
					mutantBasesPerFragment.put(fragment.getId(), list);
				} else {
					mutantBasesPerFragment.get(fragment.getId()).add(mutantbase);
				}
			}
			
			//for each fragment, all injections should be the same (per injection)
			if(ed.getInjectionNumber() == 0) { //for 1 injection (faster)
				for(Integer fragmentid : ed.getFragmentIds()) {
					List<MutantBaseDB> mutantBasesForFragment = mutantBasesPerFragment.get(fragmentid);
					MutantBaseDB mb = mutantBasesForFragment.get(0);
					InjectionLocation finject = new InjectionLocation(mb.getOriginalFragment().getSrcFile(), mb.getOriginalFragment().getStartLine());
					InjectionLocation minject = new InjectionLocation(mb.getMutantFragment().getSrcFile(), mb.getMutantFragment().getStartLine());
					for(MutantBaseDB mbc: mutantBasesForFragment) {
						InjectionLocation finjectc = new InjectionLocation(mbc.getOriginalFragment().getSrcFile(), mbc.getOriginalFragment().getStartLine());
						InjectionLocation minjectc = new InjectionLocation(mbc.getMutantFragment().getSrcFile(), mbc.getMutantFragment().getStartLine());
						if(!finjectc.equals(finject)) {
							log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Fragment " + fragmentid + " is not injected in the same place for all of its generated clones.");
							return false;
						}
						if(!minjectc.equals(minject)) {
							log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Fragment " + fragmentid + " mutants are not all injected in the same place.");
							return false;
						}
						
					}
				}
			} else { //for > 1 injections
				//Sort mutant bases per fragments into per mutator
				for(Integer fragmentid : ed.getFragmentIds()) {
					List<MutantBaseDB> mutantBasesForFragment = mutantBasesPerFragment.get(fragmentid);
					
					//Sort into hashmap per mutator id
					HashMap<Integer,LinkedList<MutantBaseDB>> mutantBasesPerMutator = new HashMap<Integer, LinkedList<MutantBaseDB>>();
					for(MutantBaseDB mb : mutantBasesForFragment) {
						int mid = ed.getMutantFragment(mb.getMutantId()).getMutatorId();
						if(!mutantBasesPerMutator.containsKey(mid)) {
							mutantBasesPerMutator.put(mid, new LinkedList<MutantBaseDB>());
						}
						mutantBasesPerMutator.get(mid).add(mb);
					}
					
					//Get list of injection locations from the first mutator
					List<InjectionLocation> oilocations = new LinkedList<InjectionLocation>();
					List<InjectionLocation> milocations = new LinkedList<InjectionLocation>();
					int key = mutantBasesPerMutator.keySet().iterator().next();
					List<MutantBaseDB> mbs = mutantBasesPerMutator.get(key);
					for(MutantBaseDB mb : mbs) {
						oilocations.add(new InjectionLocation(mb.getOriginalFragment().getSrcFile(), mb.getOriginalFragment().getStartLine()));
						milocations.add(new InjectionLocation(mb.getMutantFragment().getSrcFile(), mb.getMutantFragment().getStartLine()));
					}
					
					//Check injection locations not repeated
					List<InjectionLocation> oilocations_checkrepeat = new LinkedList<InjectionLocation>(oilocations);
					List<InjectionLocation> milocations_checkrepeat = new LinkedList<InjectionLocation>(milocations);
					InjectionLocation repeat;
					
					while(oilocations_checkrepeat.size() != 0) {
						repeat = oilocations_checkrepeat.remove(0);
						if(oilocations_checkrepeat.contains(repeat)) {
							log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Fragment " + fragmentid + " has an injection location repeated (original fragment).");
							return false;
						}
					}
					
					while(milocations_checkrepeat.size() != 0) {
						repeat = milocations_checkrepeat.remove(0);
						if(milocations_checkrepeat.contains(repeat)) {
							log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Fragment " + fragmentid + " has an injection location repeated (mutant fragment).");
							return false;
						}
					}
					
					//Check for each mutator
					for(int i : ed.getMutatorIds()) {
						List<MutantBaseDB> mbdbs = mutantBasesPerMutator.get(i);
						
						//Get injection locations for this mutator
						List<InjectionLocation> coilocations = new LinkedList<InjectionLocation>();
						List<InjectionLocation> cmilocations = new LinkedList<InjectionLocation>();
						for(MutantBaseDB mb : mbdbs) {
							coilocations.add(new InjectionLocation(mb.getOriginalFragment().getSrcFile(), mb.getOriginalFragment().getStartLine()));
							cmilocations.add(new InjectionLocation(mb.getMutantFragment().getSrcFile(), mb.getMutantFragment().getStartLine()));
						}
						
						//Check the injection locations for this mutator and for the reference are the same sets
						for(InjectionLocation il : coilocations) {
							if(!oilocations.contains(il)) {
								log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Fragment " + fragmentid + " multiple injections is incorrect.  Original injection locations not correct.");
								return false;
							}
						}
						
						for(InjectionLocation il : oilocations) {
							if(!coilocations.contains(il)) {
								log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Fragment " + fragmentid + " multiple injections is incorrect.  Original injection locations not correct.");
								return false;
							}
						}
						
						for(InjectionLocation il : cmilocations) {
							if(!milocations.contains(il)) {
								log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Fragment " + fragmentid + " multiple injections is incorrect.  Mutant injection locations not correct.");
								return false;
							}
						}
						
						for(InjectionLocation il : milocations) {
							if(!cmilocations.contains(il)) {
								log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): " + "Fragment " + fragmentid + " multiple injections is incorrect.  Mutant injection locations not correct.");
								return false;
							}
						}
						
					}
				}
			}
			
			return true;
		}  catch (Exception e) {
			e.printStackTrace();
			log.println("[" + Calendar.getInstance().getTime() + "] (verifyManualGeneration): Generation validation failed, some component threw an exception (see exception log previous).");
			return false;
		}
	}
	
	public boolean verifyAutomaticGeneration() throws SQLException {
		try {
			log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Start: Verifying generated data.");
			
			ExperimentData ed = this.getExperimentData();
			List<MutatorDB> mutators = ed.getMutators();
		
		// CHECK Fragments ---------------------------------------------------------------------------------------
			log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tVerifying fragments.");
			//Check num fragments
			if(!(ed.getMaxFragments() >= ed.numFragments())) {
				log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Number of fragments exceeds max fragments parameter.");
				return false;
			}
			
			//Check the fragments
			List<FragmentDB> fragments = ed.getFragments();
			for(FragmentDB fragment : fragments) {
				log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\t\tChecking fragment: " + fragment.getId() + ".");
				//Check Fragment Exists
				if(!Files.exists(fragment.getFragmentFile())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " file does not exist in output directory.");
					return false;
				}
				if(!Files.isRegularFile(fragment.getFragmentFile())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " file in output directory is not a regular file.");
					return false;
				}
				
				//Get The Fragment
				Path extractFragment = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				FragmentUtil.extractFragment(fragment, extractFragment);
				
				//Check fragment file in output directory is correct
				if(!FileUtils.contentEquals(extractFragment.toFile(), fragment.getFragmentFile().toFile())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " was not extracted properly.  Its file (in output directory) contents do not match the original fragment.");
					Files.deleteIfExists(extractFragment);
					return false;
				}
				
				//Fragment Size (Lines)
				int fragment_numlines = FileUtil.countLines(extractFragment);
				if(!(fragment_numlines >= ed.getFragmentMinimumSizeLines())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid length.  Line length shorter than minimum fragment line length.");
					Files.deleteIfExists(extractFragment);
					return false;
				}
				if(!(fragment_numlines <= ed.getFragmentMaximumSizeLines())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid length.  Line length longer than maximum fragment line length.");
					Files.deleteIfExists(extractFragment);
					return false;
				}
				
				//Fragment Size (Tokens)
				int fragment_numtokens = FileUtil.countTokens(extractFragment, ed.getLanguage());
				if(!(fragment_numtokens >= ed.getFragmentMinimumSizeTokens())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid length.  Token length shorter than minimum fragment token length.");
					Files.deleteIfExists(extractFragment);
					return false;
				}
				if(!(fragment_numtokens <= ed.getFragmentMaximumSizeTokens())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid length.  Token length longer than maximum fragment token length.");
					Files.deleteIfExists(extractFragment);
					return false;
				}
				
				//Check Type
				if(ed.getFragmentType() == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
					if(!TXLUtil.isBlock(fragment, ed.getLanguage())) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid fragment type.  Is not a proper block fragment. (Checking original fragment file).");
						Files.deleteIfExists(extractFragment);
						return false;
					}
					if(!TXLUtil.isBlock(fragment.getFragmentFile(), ed.getLanguage())) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid fragment type.  Is not a proper block fragment. (Checking stored fragment file in output directory).");
						Files.deleteIfExists(extractFragment);
						return false;
					}
					if(!TXLUtil.isBlock(extractFragment, ed.getLanguage())) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid fragment type.  Is not a proper block fragment. (Checking extracted original fragment file)");
						Files.deleteIfExists(extractFragment);
						return false;
					}
				} else if (ed.getFragmentType() == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
					if(!TXLUtil.isFunction(fragment, ed.getLanguage())) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid fragment type.  Is not a proper function fragment. (Checking original fragment file).");
						Files.deleteIfExists(extractFragment);
						return false;
					}
					if(!TXLUtil.isFunction(fragment.getFragmentFile(), ed.getLanguage())) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid fragment type.  Is not a proper function fragment. (Original extracted fragment file in output directory).");
						Files.deleteIfExists(extractFragment);
						return false;
					}
					if(!TXLUtil.isFunction(extractFragment, ed.getLanguage())) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Fragment " + fragment.getId() + " has invalid fragment type.  Is not a proper function fragment. (Extracted original fragment).");
						Files.deleteIfExists(extractFragment);
						return false;
					}
				} else {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "Error: Experiment has invalid fragment type.  Not function or block?");
					Files.deleteIfExists(extractFragment);
					return false;
				}
				
				//Cleanup
				Files.deleteIfExists(extractFragment);
			}
			
		// CHECK Mutants ---------------------------------------------------------------------------------------
			log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tVerifying mutant fragments...");
			
			//Check number of mutants (total number, number per mutator)
			if(!(ed.getMaxFragments()*mutators.size() == ed.numMutantFragments())) {
				log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tThe number of mutant fragments should be the (#fragments)*(#mutators) but it is not.");
				return false;
			}
			for(FragmentDB fragment : fragments) {
				if(!(ed.numMutators() == ed.getMutantFragments(fragment.getId()).size())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tThe number of mutant fragments created for fragment " + fragment.getId() + " should be equal to the number of mutators, but it is not.");
					return false;
				}
			}
			
			//Check Mutants
			List<MutantFragment> mutants = ed.getMutantFragments();
			for(MutantFragment mutant : mutants) {
				log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\t\tChecking mutant fragment: " + mutant.getId() + ".");
				
				//Get Require Data
				MutatorDB mutator = ed.getMutator(mutant.getMutatorId());
				FragmentDB fragment = ed.getFragment(mutant.getFragmentId());
				
				//Check mutant exits
				if(!Files.exists(mutant.getFragmentFile())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " fragment file in output directory does not exist.");
					return false;
				}
				if(!Files.isReadable(mutant.getFragmentFile())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " fragment file in output directory is not a regular file.");
					return false;
				}
				
				//Fragment Size (lines)
				int mutant_numlines = FileUtil.countLines(mutant.getFragmentFile());
				if(!(mutant_numlines >= ed.getFragmentMinimumSizeLines())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " has invalid length.  Line length is less than the minimum fragment line length.");
					return false;
				}
				if(!(mutant_numlines <= ed.getFragmentMaximumSizeLines())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " has invalid length.  Line length is greater than the maximum fragment line length.");
					return false;
				}
				
				//Fragment Size (Tokens)
				int mutant_numtokens = FileUtil.countTokens(mutant.getFragmentFile(), ed.getLanguage());
				if(!(mutant_numtokens >= ed.getFragmentMinimumSizeTokens())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " has invalid length.  Token length is less than the minimum fragment token length.");
					return false;
				}
				if(!(mutant_numtokens <= ed.getFragmentMaximumSizeTokens())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " has invalid length.  Token length is less than the maximum fragment token length.");
					return false;
				}
				
				//Check Valid Type (also checks fragment syntax valid)
				if(ed.getFragmentType() == ExperimentSpecification.BLOCK_FRAGMENT_TYPE) {
					if(!TXLUtil.isBlock(mutant.getFragmentFile(), ed.getLanguage())) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " is not of the proper fragment (expected block and was not).");
						return false;
					}
				} else if(ed.getFragmentType() == ExperimentSpecification.FUNCTION_FRAGMENT_TYPE) {
					if(!TXLUtil.isFunction(mutant.getFragmentFile(), ed.getLanguage())) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " is not of the proper fragment (expected function and was not).");
						return false;
					}
				} else {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " fragment type not supported?  Expected block or function...");
					return false;
				}
				
				//Check Clone Type
				Path fragmentpath = fragment.getFragmentFile();
				Path fragmentpath_pretty = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				Path fragmentpath_blind = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				Path fragmentpath_pretty_blind = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				
				
				Path mutantpath = mutant.getFragmentFile();
				Path mutantpath_pretty = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_mutant");
				Path mutantpath_blind = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_mutant");
				Path mutantpath_pretty_blind = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_mutant");
				
					//normalize fragment and mutant
				TXLUtil.prettyPrintSourceFragment(fragmentpath, fragmentpath_pretty, ed.getLanguage());
				FileUtil.removeEmptyLines(fragmentpath_pretty, fragmentpath_pretty);
				TXLUtil.blindRename(fragmentpath, fragmentpath_blind, ed.getLanguage());
				TXLUtil.blindRename(fragmentpath_pretty, fragmentpath_pretty_blind, ed.getLanguage());
				
				TXLUtil.prettyPrintSourceFragment(mutantpath, mutantpath_pretty, ed.getLanguage());
				FileUtil.removeEmptyLines(mutantpath_pretty, mutantpath_pretty);
				TXLUtil.blindRename(mutantpath, mutantpath_blind, ed.getLanguage());
				TXLUtil.blindRename(mutantpath_pretty, mutantpath_pretty_blind, ed.getLanguage());

				
				if(mutator.getTargetCloneType() == 1) {
					if(!(Math.abs(FileUtil.getSimilarity(fragmentpath_pretty, mutantpath_pretty) - 1.0) < 0.00001)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " is of incorrect type (expected type 1).");
						Files.deleteIfExists(fragmentpath_pretty);
						Files.deleteIfExists(fragmentpath_pretty_blind);
						Files.deleteIfExists(mutantpath_pretty);
						Files.deleteIfExists(mutantpath_pretty_blind);
						Files.deleteIfExists(fragmentpath_blind);
						Files.deleteIfExists(mutantpath_blind);

						return false;
					}
				} else if (mutator.getTargetCloneType() == 2) {
					if(Math.abs(FileUtil.getSimilarity(fragmentpath_pretty, mutantpath_pretty) - 1.0) < 0.00001) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " is of incorrect type (expected type 2, but was type 1).");
						Files.deleteIfExists(fragmentpath_pretty);
						Files.deleteIfExists(fragmentpath_pretty_blind);
						Files.deleteIfExists(mutantpath_pretty);
						Files.deleteIfExists(mutantpath_pretty_blind);
						Files.deleteIfExists(fragmentpath_blind);
						Files.deleteIfExists(mutantpath_blind);

						return false;
					}
					if(!(Math.abs(FileUtil.getSimilarity(fragmentpath_blind, mutantpath_blind) - 1.0) < 0.00001)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " is of incorrect type (expected type 2, but was type 3).");
						Files.deleteIfExists(fragmentpath_pretty);
						Files.deleteIfExists(fragmentpath_pretty_blind);
						Files.deleteIfExists(mutantpath_pretty);
						Files.deleteIfExists(mutantpath_pretty_blind);
						Files.deleteIfExists(fragmentpath_blind);
						Files.deleteIfExists(mutantpath_blind);

						return false;
					}
				} else if (mutator.getTargetCloneType() == 3) {
					if(Math.abs(FileUtil.getSimilarity(fragmentpath_pretty, mutantpath_pretty) - 1.0) < 0.00001) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " is of incorrect type (expected type 3, but was type 1).");
						Files.deleteIfExists(fragmentpath_pretty);
						Files.deleteIfExists(fragmentpath_pretty_blind);
						Files.deleteIfExists(mutantpath_pretty);
						Files.deleteIfExists(mutantpath_pretty_blind);
						Files.deleteIfExists(fragmentpath_blind);
						Files.deleteIfExists(mutantpath_blind);

						return false;
					}
					if(Math.abs(FileUtil.getSimilarity(fragmentpath_pretty_blind, mutantpath_pretty_blind) - 1.0) < 0.00001) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " is of incorrect type (expected type 3, but was type 2).");
						Files.deleteIfExists(fragmentpath_pretty);
						Files.deleteIfExists(fragmentpath_pretty_blind);
						Files.deleteIfExists(mutantpath_pretty);
						Files.deleteIfExists(mutantpath_pretty_blind);
						Files.deleteIfExists(fragmentpath_blind);
						Files.deleteIfExists(mutantpath_blind);

						return false;
					}
				} else {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " clone type not supported: " + mutator.getTargetCloneType() + " mutator: " + mutator.getId());
					Files.deleteIfExists(fragmentpath_pretty);
					Files.deleteIfExists(fragmentpath_pretty_blind);
					Files.deleteIfExists(mutantpath_pretty);
					Files.deleteIfExists(mutantpath_pretty_blind);
					Files.deleteIfExists(fragmentpath_blind);
					Files.deleteIfExists(mutantpath_blind);

					return false;
				}
				
				//Check Similarity
					//by token
				Path fragmentpath_pretty_blind_tokenize = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				Path mutantpath_pretty_blind_tokenize = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				TXLUtil.tokenize(fragmentpath_pretty_blind, fragmentpath_pretty_blind_tokenize, ed.getLanguage());
				TXLUtil.tokenize(mutantpath_pretty_blind, mutantpath_pretty_blind_tokenize, ed.getLanguage());
				double tokensimilarity = FileUtil.getSimilarity(fragmentpath_pretty_blind_tokenize, mutantpath_pretty_blind_tokenize);
					//by line
				double linesimilarity = FileUtil.getSimilarity(fragmentpath_pretty_blind, mutantpath_pretty_blind);
					//similarity
				double similarity = Math.max(tokensimilarity, linesimilarity);
				if(similarity < 1 - ed.getAllowedFragmentDifference()) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " is not similar enough to its fragment " + fragment.getId() + " as per the allowed maximum difference.");
					Files.deleteIfExists(fragmentpath_pretty_blind_tokenize);
					Files.deleteIfExists(mutantpath_pretty_blind_tokenize);
					Files.deleteIfExists(fragmentpath_pretty);
					Files.deleteIfExists(fragmentpath_pretty_blind);
					Files.deleteIfExists(mutantpath_pretty);
					Files.deleteIfExists(mutantpath_pretty_blind);
					Files.deleteIfExists(fragmentpath_blind);
					Files.deleteIfExists(mutantpath_blind);

					return false;
				}
				
				//Check Containment
				int fnumlines = FileUtil.countLines(fragment.getFragmentFile());
				List<Integer> changedlines = AbstractMutator.getLinesMutatedFromOriginal(fragment.getFragmentFile().toAbsolutePath().toString(), mutant.getFragmentFile().toAbsolutePath().toString());
				int startpoint = (int) (Math.ceil(fnumlines*ed.getMutationContainment()));
				int endpoint = (int) (Math.floor(fnumlines*(1-ed.getMutationContainment())));
				for(int line : changedlines) {
					//System.out.println(fragment.getId() + " " + mutant.getId() + " " + startpoint + " " + endpoint + " " + " " + changedlines);
					if(!(line > startpoint && line <= endpoint)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant fragment " + mutant.getId() + " from fragment " + fragment.getId() + " has invalid mutation containment.");
						Files.deleteIfExists(fragmentpath_pretty_blind_tokenize);
						Files.deleteIfExists(mutantpath_pretty_blind_tokenize);
						Files.deleteIfExists(fragmentpath_pretty);
						Files.deleteIfExists(fragmentpath_pretty_blind);
						Files.deleteIfExists(mutantpath_pretty);
						Files.deleteIfExists(mutantpath_pretty_blind);
						Files.deleteIfExists(fragmentpath_blind);
						Files.deleteIfExists(mutantpath_blind);

						return false;
					}
				}
				
				//Cleanup
				Files.deleteIfExists(fragmentpath_pretty_blind_tokenize);
				Files.deleteIfExists(mutantpath_pretty_blind_tokenize);
				Files.deleteIfExists(fragmentpath_pretty);
				Files.deleteIfExists(fragmentpath_pretty_blind);
				Files.deleteIfExists(mutantpath_pretty);
				Files.deleteIfExists(mutantpath_pretty_blind);
				Files.deleteIfExists(fragmentpath_blind);
				Files.deleteIfExists(mutantpath_blind);

			}
			
		//Mutant Bases
			log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tVerifying mutantbases...");
			List<MutantBaseDB> mutantbases = ed.getMutantBases();
			
			//Check Mutant Bases (individually)
			for(MutantBaseDB mutantbase : mutantbases) {
				log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\t\tChecking mutant base " + mutantbase.getId() + ".");
			//Requied Data
				MutantFragment mutantfragment = ed.getMutantFragment(mutantbase.getMutantId());
				FragmentDB fragment = ed.getFragment(mutantfragment.getFragmentId());
				
			//Construct Base
				ed.constructBase(mutantbase.getId());
				
			//Tmp Files
				Path extractedFragment = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				Path extractedMutant = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				Path checkfile = Files.createTempFile(ed.getTemporaryPath(), "ExperimentTest_", "_fragment");
				
			//Check Injected Files Valid (txl pretty print)		
				if(!TXLUtil.prettyPrintSourceFile(mutantbase.getOriginalFragment().getSrcFile(), checkfile, ed.getLanguage())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") File which original fragment was injected into is invalid (improper syntax).");
					return false;
				}
				if(!TXLUtil.prettyPrintSourceFile(mutantbase.getMutantFragment().getSrcFile(), checkfile, ed.getLanguage())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") File which mutant fragment was injected into is invalid (improper syntax).");
					return false;
				}
				
			//Check Injections (match stored versions)
				FragmentUtil.extractFragment(mutantbase.getOriginalFragment(), extractedFragment);
				FragmentUtil.extractFragment(mutantbase.getMutantFragment(), extractedMutant);
				if(!(FileUtils.contentEquals(extractedFragment.toFile(), fragment.getFragmentFile().toFile()))) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Original fragment was no injected properly (tried to extract it and it does not match original fragment file in output directory).");
					return false;
				}
				if(!(FileUtils.contentEquals(extractedMutant.toFile(), mutantfragment.getFragmentFile().toFile()))) {
					log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Mutant fragment was no injected properly (tried to extract it and it does not match mutant fragment file in output directory).");
					return false;
				}
				
				
			//Check Mutant Base Contents (match original, except for modified)
				Path psystem = ed.getSystemPath().toAbsolutePath().normalize();
				Path pmutantbase = ed.getMutantBasePath().toAbsolutePath().normalize();
				Path fragmentifile = mutantbase.getOriginalFragment().getSrcFile().toAbsolutePath().normalize();
				Path mutantifile = mutantbase.getMutantFragment().getSrcFile().toAbsolutePath().normalize();
				List<Path> ofiles = FileUtil.fileInventory(ed.getSystemPath());
				List<Path> odirs = FileUtil.directoryInventory(ed.getSystemPath());
				List<Path> mbfiles = FileUtil.fileInventory(ed.getMutantBasePath());
				List<Path> mbdirs = FileUtil.directoryInventory(ed.getMutantBasePath());
				
				//Check Directories
					//Mutant base has all the directoreis from original
				for(Path path : odirs) {
					Path normalized = pmutantbase.resolve(psystem.relativize(path)).toAbsolutePath().normalize();
					if(!mbdirs.contains(normalized)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Is missing a directory from the original system: " + path + ".");
						return false;
					}
				}
					//Mutant base does not have directories not in original
				for(Path path : mbdirs) {
					Path normalized = psystem.resolve(pmutantbase.relativize(path)).toAbsolutePath().normalize();
					if(!odirs.contains(normalized)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Has a directory not from the original system: " + path + ".");
						return false;
					}
				}
				
				//Check Files
					//Mutant Base has all files it should, and contents are equal (except for altered)
				for(Path path : ofiles) {
					Path normalized = pmutantbase.resolve(psystem.relativize(path)).toAbsolutePath().normalize();
					if(!mbfiles.contains(normalized)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Is missing a file from the original system: " + path + ".");
						return false;
					}
					if(!normalized.equals(fragmentifile) && !normalized.equals(mutantifile)) {
						if(!FileUtils.contentEquals(path.toFile(), normalized.toFile())) {
							log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Has a file that was modified that shouldn't have been (wasn't one of the files injected into): " + normalized + ".");
							return false;
						}
					}
				}
					//Mutant base should not have extra files
				for(Path path : mbfiles) {
					Path normalized = psystem.resolve(pmutantbase.relativize(path)).toAbsolutePath().normalize();
					if(!ofiles.contains(normalized)) {
						log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): " + "\tMutant Base: " + mutantbase.getId() + " (fragment: " + fragment.getId() + ",mutant: " + mutantfragment.getId() + ") Has a file not from the original system: " + path + ".");
						return false;
					}
				}
			}
			
			//Check Mutant Bases injection locations
				//build hash map containing the mutant bases per fragment
			HashMap<Integer,List<MutantBaseDB>> mutantBasesPerFragment = new HashMap<Integer,List<MutantBaseDB>>();
			for(MutantBaseDB mutantbase : mutantbases) {
				MutantFragment mutant = ed.getMutantFragment(mutantbase.getMutantId());
				FragmentDB fragment = ed.getFragment(mutant.getFragmentId());
				if(!mutantBasesPerFragment.containsKey(fragment.getId())) {
					List<MutantBaseDB> list = new LinkedList<MutantBaseDB>();
					list.add(mutantbase);
					mutantBasesPerFragment.put(fragment.getId(), list);
				} else {
					mutantBasesPerFragment.get(fragment.getId()).add(mutantbase);
				}
			}
			
			//for each fragment, all injections should be the same (per injection)
			if(ed.getInjectionNumber() == 0) { //for 1 injection (faster)
				for(Integer fragmentid : ed.getFragmentIds()) {
					List<MutantBaseDB> mutantBasesForFragment = mutantBasesPerFragment.get(fragmentid);
					MutantBaseDB mb = mutantBasesForFragment.get(0);
					InjectionLocation finject = new InjectionLocation(mb.getOriginalFragment().getSrcFile(), mb.getOriginalFragment().getStartLine());
					InjectionLocation minject = new InjectionLocation(mb.getMutantFragment().getSrcFile(), mb.getMutantFragment().getStartLine());
					for(MutantBaseDB mbc: mutantBasesForFragment) {
						InjectionLocation finjectc = new InjectionLocation(mbc.getOriginalFragment().getSrcFile(), mbc.getOriginalFragment().getStartLine());
						InjectionLocation minjectc = new InjectionLocation(mbc.getMutantFragment().getSrcFile(), mbc.getMutantFragment().getStartLine());
						if(!finjectc.equals(finject)) {
							log.println("[" + Calendar.getInstance().getTime() + "] Fragment " + fragmentid + " is not injected in the same place for all of its generated clones.");
							return false;
						}
						if(!minjectc.equals(minject)) {
							log.println("[" + Calendar.getInstance().getTime() + "] Fragment " + fragmentid + " mutants are not all injected in the same place.");
							return false;
						}
						
					}
				}
			} else { //for > 1 injections
				//Sort mutant bases per fragments into per mutator
				for(Integer fragmentid : ed.getFragmentIds()) {
					List<MutantBaseDB> mutantBasesForFragment = mutantBasesPerFragment.get(fragmentid);
					
					//Sort into hashmap per mutator id
					HashMap<Integer,LinkedList<MutantBaseDB>> mutantBasesPerMutator = new HashMap<Integer, LinkedList<MutantBaseDB>>();
					for(MutantBaseDB mb : mutantBasesForFragment) {
						int mid = ed.getMutantFragment(mb.getMutantId()).getMutatorId();
						if(!mutantBasesPerMutator.containsKey(mid)) {
							mutantBasesPerMutator.put(mid, new LinkedList<MutantBaseDB>());
						}
						mutantBasesPerMutator.get(mid).add(mb);
					}
					
					//Get list of injection locations from the first mutator
					List<InjectionLocation> oilocations = new LinkedList<InjectionLocation>();
					List<InjectionLocation> milocations = new LinkedList<InjectionLocation>();
					int key = mutantBasesPerMutator.keySet().iterator().next();
					List<MutantBaseDB> mbs = mutantBasesPerMutator.get(key);
					for(MutantBaseDB mb : mbs) {
						oilocations.add(new InjectionLocation(mb.getOriginalFragment().getSrcFile(), mb.getOriginalFragment().getStartLine()));
						milocations.add(new InjectionLocation(mb.getMutantFragment().getSrcFile(), mb.getMutantFragment().getStartLine()));
					}
					
					//Check injection locations not repeated
					List<InjectionLocation> oilocations_checkrepeat = new LinkedList<InjectionLocation>(oilocations);
					List<InjectionLocation> milocations_checkrepeat = new LinkedList<InjectionLocation>(milocations);
					InjectionLocation repeat;
					
					while(oilocations_checkrepeat.size() != 0) {
						repeat = oilocations_checkrepeat.remove(0);
						if(oilocations_checkrepeat.contains(repeat)) {
							log.println("[" + Calendar.getInstance().getTime() + "] Fragment " + fragmentid + " has an injection location repeated (original fragment).");
							return false;
						}
					}
					
					while(milocations_checkrepeat.size() != 0) {
						repeat = milocations_checkrepeat.remove(0);
						if(milocations_checkrepeat.contains(repeat)) {
							log.println("[" + Calendar.getInstance().getTime() + "] Fragment " + fragmentid + " has an injection location repeated (mutant fragment).");
							return false;
						}
					}
					
					//Check for each mutator
					for(int i : ed.getMutatorIds()) {
						List<MutantBaseDB> mbdbs = mutantBasesPerMutator.get(i);
						
						//Get injection locations for this mutator
						List<InjectionLocation> coilocations = new LinkedList<InjectionLocation>();
						List<InjectionLocation> cmilocations = new LinkedList<InjectionLocation>();
						for(MutantBaseDB mb : mbdbs) {
							coilocations.add(new InjectionLocation(mb.getOriginalFragment().getSrcFile(), mb.getOriginalFragment().getStartLine()));
							cmilocations.add(new InjectionLocation(mb.getMutantFragment().getSrcFile(), mb.getMutantFragment().getStartLine()));
						}
						
						//Check the injection locations for this mutator and for the reference are the same sets
						for(InjectionLocation il : coilocations) {
							if(!oilocations.contains(il)) {
								log.println("[" + Calendar.getInstance().getTime() + "] Fragment " + fragmentid + " multiple injections is incorrect.  Original injection locations not correct.");
								return false;
							}
						}
						
						for(InjectionLocation il : oilocations) {
							if(!coilocations.contains(il)) {
								log.println("[" + Calendar.getInstance().getTime() + "] Fragment " + fragmentid + " multiple injections is incorrect.  Original injection locations not correct.");
								return false;
							}
						}
						
						for(InjectionLocation il : cmilocations) {
							if(!milocations.contains(il)) {
								log.println("[" + Calendar.getInstance().getTime() + "] Fragment " + fragmentid + " multiple injections is incorrect.  Mutant injection locations not correct.");
								return false;
							}
						}
						
						for(InjectionLocation il : milocations) {
							if(!cmilocations.contains(il)) {
								log.println("[" + Calendar.getInstance().getTime() + "] Fragment " + fragmentid + " multiple injections is incorrect.  Mutant injection locations not correct.");
								return false;
							}
						}
						
					}
				}
			}
			
			return true;
		}  catch (Exception e) {
			e.printStackTrace();
			log.println("[" + Calendar.getInstance().getTime() + "] (verifyAutomaticGeneration): Generation validation failed, some component threw an exception (see exception log previous).");
			return false;
		}
	}
	
//-- Evaluation Phase -------------------------------------------------------------------------------------------------
	
	public boolean evaluateTools(boolean reuseReports) throws SQLException, IOException {
		if(ed.getCurrentStage() != ExperimentData.EVALUATION_SETUP_STAGE) {
			throw new IllegalStateException("Evaluation must be done in evaluation setup phase.");
		}
		ed.nextStage();
		assert(ed.getCurrentStage() == ExperimentData.EVALUATION_STAGE);

		log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "Start: Evaluate Tools.");
		
		if(!reuseReports) {
			log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "Deleting previous evaluation (CloneReports/UnitRecall/UnitPrecision).");
			ed.deleteCloneDetectionReports();
		} else {
			log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "Deleting previous evaluation (UnitRecall/UnitPrecision).");
		}
		ed.deleteUnitRecalls();
		ed.deleteUnitPrecisions();
		
		//Needed experiment properties
		int language = ed.getLanguage();
		int fragmentType = ed.getFragmentType();
		int minlines = ed.getFragmentMinimumSizeLines();
		int mintokens = ed.getFragmentMinimumSizeTokens();
		int maxlines = ed.getFragmentMaximumSizeLines();
		int maxtokens = ed.getFragmentMaximumSizeTokens();
		double alloweddifference = ed.getAllowedFragmentDifference();
		double recallRequiredSimilarity = ed.getRecallRequiredSimilarity();
		double precisionRequiredSimilarity = ed.getPrecisionRequiredSimilarity();
		double subsumeTolerance = ed.getSubsumeMatcherTolerance();
		
		//Number/count
		int num_mutantbases = ed.numMutantBases();
		int count_mutantbases = 0;
		int num_tools = ed.numTools();
		int count_tools = 0;
		
		for(MutantBaseDB mutantbase : ed.getMutantBases()) {
			count_mutantbases++;
			count_tools = 0;
			log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "Evaluating tools for mutant base " + mutantbase.getMutantId() + " [" + count_mutantbases + "/" + num_mutantbases + "]");
			
			for(ToolDB tool : ed.getTools()) {
				count_tools++;
				log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "\tEvaluating " + tool.getName() + "(" + tool.getId() + ") [" + count_tools + "/" + num_tools + "]");
				
				
				//Build Mutant Base
				log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "\t\tConstructing mutant base (fresh copy)...");
				try {
					ed.constructBase(mutantbase.getId());
				} catch (FileNotFoundException e) {
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When constructing the mutant base, could not find the files to inject into.  Either you have tampered with the output directory, or bug.");
					e.printStackTrace();
					return false;
				} catch (IllegalArgumentException e) {
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Could not construct mutant base because it does not exist (bug!).");
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When constructing the mutant base, an IO exception(error) occured.  Either you have tampered with the output directory, or bug.");
					e.printStackTrace();
					return false;
				}
				
				//Run tool only if have not in past
				if(!ed.existsCloneDetectionReport(tool.getId(), mutantbase.getId())) {
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "\t\tRunning tool...");
					//Evaluate mutant base with tool
					CloneDetectionReport cdr;
					try {
						cdr = tool.runTool(mutantbase.getDirectory(),
										language, 
										fragmentType,
										minlines, mintokens, maxlines, maxtokens, alloweddifference);
					} catch (FileNotFoundException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: The clone detection report path the tool runner reported does not point to a file that exists.");
						e.printStackTrace();
						return false;
					} catch (InterruptedException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: The tool runner was interrupted unexpectedly.  Did you terminate the tool's process?  Did the tool crash?");
						e.printStackTrace();
						return false;
					} catch (IOException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: An IO error occured when reading the tool runner's output stream.");
						e.printStackTrace();
						return false;
					} catch (IllegalArgumentException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: An illegal argument was passed to the tool's runTool function (bug!?).");
						e.printStackTrace();
						return false;
					} catch (NullPointerException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: An null pointer argument was passed to the tool's runTool function (bug!?).");
						e.printStackTrace();
						return false;
					} catch (InvalidToolRunnerException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Tool runner is not valid.  It returned success but did not specify a path to a clone report.");
						e.printStackTrace();
						return false;
					}
					if(cdr == null) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Tool runner ended with error status.");
						return false;
					}
					
					//Check Report
					try {
						cdr.open();
						while(cdr.next() != null) {
							
						}
						cdr.close();
					} catch (FileNotFoundException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Clone detection report generated by tool does not exist.");
						e.printStackTrace();
						try{cdr.close();} catch (Exception ee) {}
						return false;
					} catch (InputMismatchException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Clone detection report generated by tool has invalid format.");
						e.printStackTrace();
						try{cdr.close();} catch (Exception ee) {}
						return false;
					} catch (IOException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: IO error while reading tool's clone detection report.");
						e.printStackTrace();
						try{cdr.close();} catch (Exception ee) {}
						return false;
					}
					
					//Add to database
					try {
						ed.createCloneDetectionReport(tool.getId(), mutantbase.getId(), cdr.getReport());
					} catch (FileNotFoundException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When adding tool's detection report to database, the report could not be found.  Did you tamper with the file?");
						e.printStackTrace();
						return false;
					} catch (IOException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When adding tool's detection report to database, an IO error occured while copying the report to the output directory.  Did you tamper with the output directory?");
						e.printStackTrace();
						return false;
					} catch (IllegalArgumentException e) {
						log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When adding tool's detection report to database, an IllegalArgumentException was thrown.  Did you tamper with the output directory (could cause report to be illegal argument)?  Else a bug.");
						e.printStackTrace();
						return false;
					}
				} else {
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "\t\tFound clone detection report from previous evaluation, re-using tool run.");
				}
				
				//Get clone detection report
				CloneDetectionReport cdr = ed.getCloneDetectionReport(tool.getId(), mutantbase.getId());
				
			//Evaluate Unit Recall
				//Evaluate
				log.print("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "\t\tCalculating unit recall... ");
				UnitRecall ur;
				try {
					ur = Experiment.evaluateRecall(cdr, tool, mutantbase, recallRequiredSimilarity, subsumeTolerance, language, log);
				} catch (InputMismatchException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Clone detection report had illegal format.");
					e.printStackTrace();
					return false;
				} catch (FileNotFoundException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Clone detection report file was missing.  Did you mess with the output directory?");
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: IO error while reading the clone detection report.");
					e.printStackTrace();
					return false;
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: While evaluating unit recall, a required process was interrupted.  Probably a process required by the clone verifiers.");
					return false;
				}
				
				//Add To Database
				ed.deleteUnitRecall(tool.getId(), mutantbase.getId());
				try {
					ed.createUnitRecall(tool.getId(), mutantbase.getId(), ur.getRecall(), ur.getClone());
				} catch (IllegalArgumentException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When unit recall was added to database, illegal argument was specified (probably a bug!!).");
					e.printStackTrace();
					return false;
				} catch (NullPointerException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When unit recall was added to database, clone was null when recall was not 0.0 (probably a bug!!).");
					e.printStackTrace();
					return false;
				}
				log.println(ur.getRecall());
				
			//Evaluate Unit Precision
				//Evaluate
				log.print("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "\t\tCalculating unit precision... ");
				UnitPrecision up;
				try {
					up = Experiment.evaluatePrecision(cdr, tool, mutantbase, precisionRequiredSimilarity, subsumeTolerance, language, log);
				} catch (InputMismatchException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Clone detection report had illegal format.");
					e.printStackTrace();
					return false;
				} catch (FileNotFoundException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: Clone detection report file was missing.  Did you mess with the output directory?");
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: IO error while reading the clone detection report.");
					e.printStackTrace();
					return false;
				} catch (InterruptedException e) {
					e.printStackTrace();
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: While evaluating unit recall, a required process was interrupted.  Probably a process required by the clone verifiers.");
					return false;
				}
				
				//Add To Database
				ed.deleteUnitPrecision(tool.getId(), mutantbase.getId());
				try {
					ed.createUnitPrecision(tool.getId(), mutantbase.getId(), up.getPrecision(), up.getClones());
				} catch (IllegalArgumentException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When unit precision was added to database, illegal argument was specified (probably a bug!!).");
					e.printStackTrace();
					return false;
				} catch (NullPointerException e) {
					log.println();
					log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): Error: When unit precision was added to database, clone was null when recall was not 0.0 (probably a bug!!).");
					e.printStackTrace();
					return false;
				}
				log.println(up.getPrecision() + " (from " + up.getClones().size() + " clones capturing one of the injected fragments)");
				
			}
		}
		
		log.println("[" + Calendar.getInstance().getTime() + "] (evaluateTools): " + "End: Evaluate Tools.");
		
		assert(ed.getCurrentStage() == ExperimentData.EVALUATION_STAGE);
		ed.nextStage();
		assert(ed.getCurrentStage() == ExperimentData.RESULTS_STAGE);
		
		return true;
	}
	
	//Fragment fails to match if:
	//		It's fragments do not subsume (within tolerance) the fragments of the clone
	//		It's fragment are invalid (exceed file boundaries)
	//		Its similarity is less than that required (both by token and line validator)
	// For Experiment and ExperimentTest only
	protected static UnitRecall evaluateRecall(CloneDetectionReport cdr, ToolDB tool, MutantBaseDB mutantbase, double requiredSimilarity, double subsumeTolerance, int language, PrintStream log) throws FileNotFoundException, IOException, InputMismatchException, IllegalArgumentException, NullPointerException, InterruptedException {
		Objects.requireNonNull(cdr);
		Objects.requireNonNull(tool);
		Objects.requireNonNull(mutantbase);
		Objects.requireNonNull(log);
		
		if(!Files.exists(cdr.getReport())) throw new FileNotFoundException("Clone detection report file does not exist.");
		if(subsumeTolerance < 0 || subsumeTolerance > 1.0) throw new IllegalArgumentException("Subsume tolerance must be in range [0.0, 1.0]");
		if(requiredSimilarity < 0 || requiredSimilarity > 1.0) throw new IllegalArgumentException("Required similarity msut be in range [0.0, 1.0]");
		if(!ExperimentSpecification.isLanguageSupported(language)) throw new IllegalArgumentException("Unsupported language.");
		
		cdr = new CloneDetectionReport(cdr.getReport());//ensure uniquely used reader
		Clone aclone = new Clone(mutantbase.getOriginalFragment(), mutantbase.getMutantFragment());
		Clone clone;
		
		try {
			cdr.open();
			//Check each clone found to see if it matches
			while((clone = cdr.next()) != null) {
				//If matches (subsumes) within tolerance
				if(	(clone.getFragment1().subsumes(aclone.getFragment1(), subsumeTolerance)) && (clone.getFragment2().subsumes(aclone.getFragment2(), subsumeTolerance)) || 
						(clone.getFragment2().subsumes(aclone.getFragment1(), subsumeTolerance)) && (clone.getFragment1().subsumes(aclone.getFragment2(), subsumeTolerance))) {
					
					//Check fragments are valid
					if(clone.getFragment1().getEndLine() > FileUtil.countLines(clone.getFragment1().getSrcFile())) {
						continue;
					}
					if(clone.getFragment2().getEndLine() > FileUtil.countLines(clone.getFragment2().getSrcFile())) {
						continue;
					}
					
					//Check Required Similarity
					if(requiredSimilarity == 0.0) { // If don't require similarity check, then skip the validators for speed
						break;
					} else { //require validation
							//Token Validator
						TokenValidator tokenValidator = new TokenValidator(requiredSimilarity, 0.0, language);
						ValidatorResult tokenValidatorResult = tokenValidator.validate(clone);
						if(tokenValidatorResult.getValidationResult() == ValidatorResult.ERROR) {
							log.println("EvaluateRecall: DEBUG: Token Validator failed to validate clone: " + clone + ".  Analysis will continue by accepting this clone unconditionally.  However, this could harm tool's performance analysis.  Consider manually trimming this fragment and its bases from experiment or fixing this manually.");
							break;
						}
						if(tokenValidatorResult.getSimilarity() >= requiredSimilarity) {
							break;
						}
						
							//Line Validator
						LineValidator lineValidator = new LineValidator(requiredSimilarity, 0.0, language);
						ValidatorResult lineValidatorResult = lineValidator.validate(clone);
						if(lineValidatorResult.getValidationResult() == ValidatorResult.ERROR) {
							log.println("EvaluateRecall: DEBUG: Line Validator failed to validate clone: " + clone + ".  Analysis will continue by accepting this clone unconditionally.  However, this could harm tool's performance analysis.  Consider manually trimming this fragment and its bases from experiment or fixing this manually.");
							break;
						}
						if(lineValidatorResult.getSimilarity() >= requiredSimilarity) {
							break;
						}
						
					}
				}
			}
			cdr.close();
		} catch(FileNotFoundException e) {
			try {cdr.close();} catch (Exception ee){}
			throw e;
		} catch (InputMismatchException | IOException e) {
			try {cdr.close();} catch (Exception ee){}
			throw e;
		}
		
		if(clone != null) {
			return new UnitRecall(tool.getId(), mutantbase.getId(), 1.0, clone);
		} else {
			return new UnitRecall(tool.getId(), mutantbase.getId(), 0.0, null);
		}
	}

	/**
	 * For Experiment/ExperimentTest use only
	 */
	protected static UnitPrecision evaluatePrecision(CloneDetectionReport cdr, ToolDB tool, MutantBaseDB mutantbase, double requiredSimilarity, double subsumeTolerance, int language, PrintStream log) throws SQLException, FileNotFoundException, IOException, InputMismatchException, IllegalArgumentException, NullPointerException, InterruptedException {
		Objects.requireNonNull(cdr);
		Objects.requireNonNull(tool);
		Objects.requireNonNull(mutantbase);
		Objects.requireNonNull(log);
		
		if(!Files.exists(cdr.getReport())) throw new FileNotFoundException("Clone detection report file does not exist.");
		if(subsumeTolerance < 0 || subsumeTolerance > 1.0) throw new IllegalArgumentException("Subsume tolerance must be in range [0.0, 1.0]");
		if(requiredSimilarity < 0 || requiredSimilarity > 1.0) throw new IllegalArgumentException("Required similarity msut be in range [0.0, 1.0]");
		if(!ExperimentSpecification.isLanguageSupported(language)) throw new IllegalArgumentException("Unsupported language.");
		
		cdr = new CloneDetectionReport(cdr.getReport());//ensure uniquely used reader
		Clone aclone = new Clone(mutantbase.getOriginalFragment(), mutantbase.getMutantFragment());
		Clone clone;
		List<VerifiedClone> clones = new LinkedList<VerifiedClone>();
		double running_unit_precision = 0.0;
		int num_clones_running = 0;
		
		try {
			cdr.open();
cdrloop:	while((clone = cdr.next()) != null) {
				if(clone.getFragment1().subsumes(aclone.getFragment1(), subsumeTolerance) ||
				   clone.getFragment1().subsumes(aclone.getFragment2(), subsumeTolerance) ||
				   clone.getFragment2().subsumes(aclone.getFragment1(), subsumeTolerance) || 
				   clone.getFragment2().subsumes(aclone.getFragment2(), subsumeTolerance)
						) {
					//Update Num
					num_clones_running++;
					//Check Fragmetns are Valid (endline larger than end of file line)
					if(clone.getFragment1().getEndLine() > FileUtil.countLines(clone.getFragment1().getSrcFile())) {
						clones.add(new VerifiedClone(clone.getFragment1(), clone.getFragment2(), false, false));
						continue cdrloop;
					}
					if(clone.getFragment2().getEndLine() > FileUtil.countLines(clone.getFragment2().getSrcFile())) {
						clones.add(new VerifiedClone(clone.getFragment1(), clone.getFragment2(), false, false));
						continue cdrloop;
					}
					
					// Validate Clone
					//Token
					TokenValidator tokenValidator = new TokenValidator(requiredSimilarity, 0.0, language);
					ValidatorResult tokenValidatorResult = tokenValidator.validate(clone);
						//if failed, accept unconditionally and proceed
					if(tokenValidatorResult.getValidationResult() == ValidatorResult.ERROR) {
						log.println("EvaluateRecall: DEBUG: Token Validator failed to validate clone: " + clone + ".  Analysis will continue by accepting this clone unconditionally.  However, this could harm tool's performance analysis.  Consider manually trimming this fragment and its bases from experiment or fixing this manually.");
						clones.add(new VerifiedClone(clone.getFragment1(), clone.getFragment2(), false, false));
						running_unit_precision += 1.0;
						continue cdrloop;
					}
						//if success and similarity is sufficient, accept and continue (without line validation)
					if(tokenValidatorResult.getSimilarity() >= requiredSimilarity) {
						clones.add(new VerifiedClone(clone.getFragment1(), clone.getFragment2(), true, true));
						running_unit_precision += 1.0;
						continue cdrloop;
					}
					
					//Line
					LineValidator lineValidator = new LineValidator(requiredSimilarity, 0.0, language);
					ValidatorResult lineValidatorResult = lineValidator.validate(clone);
						//if failed, accept unconditionally and proceed
					if(lineValidatorResult.getValidationResult() == ValidatorResult.ERROR) {
						log.println("EvaluateRecall: DEBUG: Line Validator failed to validate clone: " + clone + ".  Analysis will continue by accepting this clone unconditionally.  However, this could harm tool's performance analysis.  Consider manually trimming this fragment and its bases from experiment or fixing this manually.");
						clones.add(new VerifiedClone(clone.getFragment1(), clone.getFragment2(), false, false));
						running_unit_precision += 1.0;
						continue cdrloop;
					}
						//if success and similarity is sufficient, accept and continue (without line validation)
					if(lineValidatorResult.getSimilarity() >= requiredSimilarity) {
						clones.add(new VerifiedClone(clone.getFragment1(), clone.getFragment2(), true, true));
						running_unit_precision += 1.0;
						continue cdrloop;
						//else, neither validator accepted it, so not a clone case (add but dont increment precision)
					} else {
						clones.add(new VerifiedClone(clone.getFragment1(), clone.getFragment2(), false, true));
						continue cdrloop;
					}
				}
			}
			cdr.close();
		} catch (FileNotFoundException e) {
			// cdr file not found
			try {cdr.close();} catch (Exception ee){}
			throw e;
		} catch (InputMismatchException e) {
			// error in report
			try {cdr.close();} catch (Exception ee){}
			throw e;
		} catch (IOException e) {
			// error reading report
			try {cdr.close();} catch (Exception ee){}
			throw e;
		}
		
		if(num_clones_running == 0) {
			return new UnitPrecision(tool.getId(), mutantbase.getId(), 1.0, clones);
		} else {
			return new UnitPrecision(tool.getId(), mutantbase.getId(), running_unit_precision/num_clones_running, clones);
		}
	}
	
//-- Output Results ---------------------------------------------------------------------------------------------------
	
	public boolean outputResults() throws SQLException, IllegalStateException {
		if(ed.getCurrentStage() != ExperimentData.RESULTS_STAGE) {
			throw new IllegalStateException("Can only output results ");
		}
		
		List<ToolDB> tools = ed.getTools();
		
		log.println("----- Analysis Results -----");
		
		//Iterate through each tool
		for(ToolDB tool : tools) {
			System.out.println(tool.getName() + "[" + tool.getId() + "]" + " - " + tool.getDescription() + ":");
			
		//Get Basic Data
			List<UnitRecall> unitRecalls = ed.getUnitRecallsByTool(tool.getId());
			List<UnitPrecision> unitPrecisions = ed.getUnitPrecisionForTool(tool.getId());
			List<CloneDetectionReportDB> reports = ed.getCloneDetectionReportsByToolId(tool.getId());
			int numBases = ed.numMutantBases();
			
		//If not fully evaluated, skip
			if(unitRecalls.size() != numBases || unitPrecisions.size() != numBases || reports.size() != numBases) {
				System.out.println("\t" + "Tool has not been fully evaluated.");
				continue;
			}
			
			//Collect mutators & operators
			List<MutatorDB> mutators = ed.getMutators();
			List<OperatorDB> operators = ed.getOperators();
			
			//Collect Mutators by Type
			List<MutatorDB> typeOneMutators = new LinkedList<MutatorDB>();
			List<MutatorDB> typeTwoMutators = new LinkedList<MutatorDB>();
			List<MutatorDB> typeThreeMutators = new LinkedList<MutatorDB>();
			for(MutatorDB mutator : mutators) {
				if(mutator.getTargetCloneType() == 1) {
					typeOneMutators.add(mutator);
				} else if(mutator.getTargetCloneType() == 2) {
					typeTwoMutators.add(mutator);
				} else if (mutator.getTargetCloneType() == 3) {
					typeThreeMutators.add(mutator);
				}
			}
			
		//Overall Results
			log.println("\tOverall:");
			
			double runningOverallRecall = 0;
			for(UnitRecall ur : unitRecalls) {
				runningOverallRecall += ur.getRecall();
			}
			double overallRecall = runningOverallRecall / unitRecalls.size();
			
			double runningOverallPrecision = 0;
			for(UnitPrecision up : unitPrecisions) {
				runningOverallPrecision += up.getPrecision();
			}
			double overallPrecision = runningOverallPrecision / unitPrecisions.size();
			
			log.println("\t\t   Recall: " + overallRecall);
			log.println("\t\tPrecision: " + overallPrecision);
			
		//Per Type
			log.println("\tPer Clone Type:");
			
			//Type 1
			if(typeOneMutators.size() > 0) {
				log.println("\t\tType1:");
				List<UnitRecall> typeOneUnitRecall = new LinkedList<UnitRecall>();
				List<UnitPrecision> typeOneUnitPrecision = new LinkedList<UnitPrecision>();
				for(MutatorDB mutator : typeOneMutators) {
					typeOneUnitRecall.addAll(ed.getUnitRecallsByToolAndMutator(tool.getId(), mutator.getId()));
					typeOneUnitPrecision.addAll(ed.getUnitPrecisionForToolAndMutator(tool.getId(), mutator.getId()));
				}
				
				double runningTypeOneRecall = 0;
				for(UnitRecall ur : typeOneUnitRecall) {
					runningTypeOneRecall += ur.getRecall();
				}
				double typeOneRecall = runningTypeOneRecall / typeOneUnitRecall.size();
				
				double runningTypeOnePrecision = 0;
				for(UnitPrecision up : typeOneUnitPrecision) {
					runningTypeOnePrecision += up.getPrecision();
				}
				double typeOnePrecision = runningTypeOnePrecision / typeOneUnitPrecision.size();
				
				log.println("\t\t\t   Recall: " + typeOneRecall);
				log.println("\t\t\tPrecision: " + typeOnePrecision);
			}

			//Type 2
			if(typeTwoMutators.size() > 0) {
				log.println("\t\tType2:");
				List<UnitRecall> typeTwoUnitRecall = new LinkedList<UnitRecall>();
				List<UnitPrecision> typeTwoUnitPrecision = new LinkedList<UnitPrecision>();
				for(MutatorDB mutator : typeTwoMutators) {
					typeTwoUnitRecall.addAll(ed.getUnitRecallsByToolAndMutator(tool.getId(), mutator.getId()));
					typeTwoUnitPrecision.addAll(ed.getUnitPrecisionForToolAndMutator(tool.getId(), mutator.getId()));
				}
				
				double runningTypeTwoRecall = 0;
				for(UnitRecall ur : typeTwoUnitRecall) {
					runningTypeTwoRecall += ur.getRecall();
				}
				double typeTwoRecall = runningTypeTwoRecall / typeTwoUnitRecall.size();
				
				double runningTypeTwoPrecision = 0;
				for(UnitPrecision up : typeTwoUnitPrecision) {
					runningTypeTwoPrecision += up.getPrecision();
				}
				double typeTwoPrecision = runningTypeTwoPrecision / typeTwoUnitPrecision.size();
				
				log.println("\t\t\t   Recall: " + typeTwoRecall);
				log.println("\t\t\tPrecision: " + typeTwoPrecision);
			}
			
			//Type 3
			if(typeThreeMutators.size() > 0) {
				log.println("\t\tType3:");
				List<UnitRecall> typeThreeUnitRecall = new LinkedList<UnitRecall>();
				List<UnitPrecision> typeThreeUnitPrecision = new LinkedList<UnitPrecision>();
				for(MutatorDB mutator : typeThreeMutators) {
					typeThreeUnitRecall.addAll(ed.getUnitRecallsByToolAndMutator(tool.getId(), mutator.getId()));
					typeThreeUnitPrecision.addAll(ed.getUnitPrecisionForToolAndMutator(tool.getId(), mutator.getId()));
				}
				
				double runningTypeThreeRecall = 0;
				for(UnitRecall ur : typeThreeUnitRecall) {
					runningTypeThreeRecall += ur.getRecall();
				}
				double typeThreeRecall = runningTypeThreeRecall / typeThreeUnitRecall.size();
				
				double runningTypeThreePrecision = 0;
				for(UnitPrecision up : typeThreeUnitPrecision) {
					runningTypeThreePrecision += up.getPrecision();
				}
				double typeThreePrecision = runningTypeThreePrecision / typeThreeUnitPrecision.size();
				
				log.println("\t\t\t   Recall: " + typeThreeRecall);
				log.println("\t\t\tPrecision: " + typeThreePrecision);
			}
			
		//Per Mutator
			log.println("\tPer Mutator:");
			for(MutatorDB mutator : mutators) {
				log.println("\t\t[" + mutator.getId() + "] Type: " + mutator.getTargetCloneType() + ", Description: " + mutator.getDescription());
				
				double runningMutatorRecall = 0;
				List<UnitRecall> mutatorRecalls = ed.getUnitRecallsByToolAndMutator(tool.getId(), mutator.getId());
				for(UnitRecall ur : mutatorRecalls) {
					runningMutatorRecall += ur.getRecall();
				}
				double mutatorRecall = runningMutatorRecall / mutatorRecalls.size();
				
				double runningMutatorPrecision = 0;
				List<UnitPrecision> mutatorPrecisions = ed.getUnitPrecisionForToolAndMutator(tool.getId(), mutator.getId());
				for(UnitPrecision up : mutatorPrecisions) {
					runningMutatorPrecision += up.getPrecision();
				}
				double mutatorPrecision = runningMutatorPrecision / mutatorPrecisions.size();
				
				log.println("\t\t\t   Recall: " + mutatorRecall);
				log.println("\t\t\tPrecision: " + mutatorPrecision);
			}
			
		//Per Operator
			log.println("\tPer Operator:");
			for(OperatorDB operator : operators) {
				log.println("\t\t[" + operator.getId() + "] Name: " + operator.getName() + " Description: " + operator.getDescription() + " Type: " + operator.getTargetCloneType() + "]" );
				
				//Collect Mutators including this operator
				List<MutatorDB> mutatorsIncluded = new LinkedList<MutatorDB>();
				for(MutatorDB mutator : mutators) {
					if(mutator.includesOperator(operator.getId())) {
						mutatorsIncluded.add(mutator);
					}
				}
				
				//Collect UnitRecalls for these mutators for this tool
				List<UnitRecall> recalls = new LinkedList<UnitRecall>();
				for(MutatorDB mutator : mutatorsIncluded) {
					recalls.addAll(ed.getUnitRecallsByToolAndMutator(tool.getId(), mutator.getId()));
				}
				
				//Collect UnitPrecisions for these mutators for this tool
				List<UnitPrecision> precisions = new LinkedList<UnitPrecision>();
				for(MutatorDB mutator : mutatorsIncluded) {
					precisions.addAll(ed.getUnitPrecisionForToolAndMutator(tool.getId(), mutator.getId()));
				}
				
				//recall
				double runningOperatorRecall = 0;
				for(UnitRecall ur : recalls) {
					runningOperatorRecall += ur.getRecall();
				}
				double operatorRecall = runningOperatorRecall / recalls.size();
				
				//Precision
				double runningOperatorPrecision = 0;
				for(UnitPrecision up : precisions) {
					runningOperatorPrecision += up.getPrecision();
				}
				double operatorPrecision = runningOperatorPrecision / precisions.size();
				
				log.println("\t\t\t   Recall: " + operatorRecall);
				log.println("\t\t\tPrecision: " + operatorPrecision);
			}
		
		}
		
		return true;
	}
}
