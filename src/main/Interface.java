package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import experiment.Experiment;
import experiment.ExperimentSpecification;
import experiment.IllegalManualImportSpecification;
import experiment.MutatorDB;
import experiment.OperatorDB;
import experiment.ToolDB;
import util.SystemUtil;

public class Interface {
	
//---- Data ---------------------------------------------------------------------------------------
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private static PrintStream out = System.out;
	private static Experiment experiment = null;
	
// ---- Program Cycle -----------------------------------------------------------------------------
	public static void main(String args[]) throws IOException, SQLException {
		out.println("");
		out.println("********************************************************************************");
		out.println("***********************   Mutation Injection Framework   ***********************");
		out.println("********************************************************************************");
		out.println("");
		while(true) {
			//Load or Create Experiment
			root_menu();
			
			//Main Menu of Loaded Experiment
			main_menu();
			
			out.println("");
			out.println("********************************************************************************");
			out.println("");
		}
	}
	
//-------------------------------------------------------------------------------------------------
//---- ROOT MENU ----------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------

	/*
	 * Root Menu.  For creating and loading experiments, and exiting the framework.  Returns once an
	 * experiment is loaded ('experiment' field).  Close is done by System.exit.
	 */
	public static void root_menu() throws IOException {
		while(true) {
			out.println("================================================================================");
			out.println("    Root Menu");
			out.println("================================================================================");
			out.println("[1] Create New Experiment.");
			out.println("[2] Load Existing Experiment.");
			out.println("");
			out.println("[h] Help.");
			out.println("[x] Exit Framework.");
			out.println("--------------------------------------------------------------------------------");
			out.  print(":::: ");
			String input = in.readLine();
			switch(input) {
			case "1": // [1]: Create Experiment.
				root_menu_create_experiment();
				if(experiment!=null) {
					return;
				}
				break;
			case "2": // [2]: Load Existing Experiment.
				root_menu_load_experiment();
				if(experiment!=null) {
					return;
				}
				break;
			case "h": // [h]: Help.
				root_menu_help();
				break;
			case "x": // [x]: Exit Framework.
				root_menu_exit_framework();
				break;
			default: // Invalid Selection
				out.println("Invalid Selection.  Press enter to return to main menu...");
				in.readLine();
				break;
			}
			out.println("");
			out.println("********************************************************************************");
			out.println("");
		}
	}
	
	/*
	 * Root Menu - Create Experiment.  'experiment' field is set to the new experiment, or null if canceled.
	 */
	public static void root_menu_create_experiment() throws IOException {
		String input;
		int language;
		Path exp_dir;
		Path rep_dir;
		Path sub_dir;
		Path import_file = null;
		int granularity = ExperimentSpecification.FUNCTION_FRAGMENT_TYPE;
		int generation_type;
		
		//Banner
		out.println("");
		out.println("================================================================================");;
		out.println("   >Create New Experiment");

		//GetLanguage
root_menu_create_experiment_get_experiment_type:
		while(true) {
			out.println("================================================================================");
			out.println("Specify type of experiment:");
			out.println("  [1] Automatic clone synthesis experiment (default).");
			out.println("  [2] Import manual function clones.");
			out.println("  [3] Import manual block clones.");
			out.println("  [c] Cancel");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			switch(input) {
			case "1":
				generation_type = ExperimentSpecification.AUTOMATIC_GENERATION_TYPE;
				break root_menu_create_experiment_get_experiment_type;
			case "2":
				generation_type = ExperimentSpecification.MANUAL_GENERATION_TYPE;
				granularity = ExperimentSpecification.FUNCTION_FRAGMENT_TYPE;
				break root_menu_create_experiment_get_experiment_type;
			case "3":
				generation_type = ExperimentSpecification.MANUAL_GENERATION_TYPE;
				granularity = ExperimentSpecification.BLOCK_FRAGMENT_TYPE;
				break root_menu_create_experiment_get_experiment_type;
			case "c":
				experiment = null;
				out.print("Experiment creation canceled.  Press enter to return to root menu.");
				in.readLine();
				return;
			default:
				out.print("Invalid Selection.  Press enter to retry...");
				in.readLine();
				continue;
			}
		}
		
		//GetLanguage
root_menu_create_experiment_get_language:
		while(true) {
			out.println("================================================================================");
			out.println("Specify experiment language:");
			out.println("  [1] Java");
			out.println("  [2] C");
			out.println("  [3] C#");
			out.println("  [c] Cancel");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			switch(input) {
			case "1":
				language = ExperimentSpecification.JAVA_LANGUAGE;
				break root_menu_create_experiment_get_language;
			case "2":
				language = ExperimentSpecification.C_LANGUAGE;
				break root_menu_create_experiment_get_language;
			case "3":
				language = ExperimentSpecification.CS_LANGUAGE;
				break root_menu_create_experiment_get_language;
			case "c":
				experiment = null;
				out.print("Experiment creation canceled.  Press enter to return to root menu.");
				in.readLine();
				return;
			default:
				out.print("Invalid Selection.  Press enter to retry...");
				in.readLine();
				continue;
			}
		}
		
		//GetExperimentDir
root_menu_create_experiment_get_experiment_dir:
		while(true) {
			out.println("");
			out.println("Specify a directory to store the experiment.  Provide full path to the");
			out.println("directory.  Directory must not already exist (it will be created).  Leave");
			out.println("blank to cancel new experiment creation.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			//Cancled
			if(input.equals("")) {
				experiment = null;
				out.print  ("Create experiment canceled.  Press enter to return to root menu...");
				in.readLine();
				return;
				
			//Dir
			} else {
				
				//Check Path Valid
				try {
					exp_dir = Paths.get(input);
				} catch (InvalidPathException e) {
					out.print  ("Invalid path.  Press enter to retry...");
					in.readLine();
					continue root_menu_create_experiment_get_experiment_dir;
				}
				
				//Check Not Exists
				if(Files.exists(exp_dir)) {
					out.print  ("Directory already exists.  Press enter to retry...");
					in.readLine();
					continue root_menu_create_experiment_get_experiment_dir;
				}
				
				break root_menu_create_experiment_get_experiment_dir;
			}
		}
	
		//GetSubjectSystem
root_menu_create_experiment_get_subject_system_dir:
		while(true) {
			out.println("");
			out.println("Specify directory containing subject system.  Provide full path to the");
			out.println("directory.  Subject system must be of the previously specified language.  Leave");
			out.println("blank to cancel new experiment creation.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			//Canceled
			if(input.equals("")) {
				experiment = null;
				out.println("");
				out.print  ("Create experiment canceled.  Press enter to return to root menu...");
				in.readLine();
				return;
			
			//Dir
			} else {
				try {
					sub_dir = Paths.get(input);
				} catch (InvalidPathException e) {
					out.println("");
					out.print  ("Invalid path.  Press enter to retry...");
					in.readLine();
					continue root_menu_create_experiment_get_subject_system_dir;
				}
				
				//Check Not Exists
				if(!Files.exists(sub_dir)) {
					out.println("");
					out.print  ("Directory does not exists.  Press enter to retry...");
					in.readLine();
					continue root_menu_create_experiment_get_subject_system_dir;
				}
				
				//Check Directory
				if(!Files.isDirectory(sub_dir)) {
					out.println("");
					out.print  ("Specified path does not denote a directory.  Press enter to retry...");
					in.readLine();
					continue root_menu_create_experiment_get_subject_system_dir;
				}
				
				break root_menu_create_experiment_get_subject_system_dir;
			}
		}
		
		if(generation_type == ExperimentSpecification.AUTOMATIC_GENERATION_TYPE) {
			//GetSourceRepository
	root_menu_create_experiment_get_source_repository_dir:
			while(true) {
				out.println("");
				out.println("Specify directory containing source repository.  Provide full path to the");
				out.println("directory.  Source repository must be of the previously specified language.");
				out.println("Leave blank to cancel new experiment creation.");
				out.println("--------------------------------------------------------------------------------");
				out.print  (":::: ");
				input = in.readLine();
				
				//Canceled
				if(input.equals("")) {
					experiment = null;
					out.println("");
					out.print  ("Create experiment canceled.  Press enter to return to root menu...");
					in.readLine();
					return;
				
				//ProcessDir
				} else {
					try {
						rep_dir = Paths.get(input);
					} catch (InvalidPathException e) {
						out.println("");
						out.print  ("Invalid path.  Press enter to retry...");
						in.readLine();
						continue root_menu_create_experiment_get_source_repository_dir;
					}
					
					//Check Not Exists
					if(!Files.exists(rep_dir)) {
						out.print  ("Directory does not exists.  Press enter to retry...");
						in.readLine();
						continue root_menu_create_experiment_get_source_repository_dir;
					}
					
					//Check Directory
					if(!Files.isDirectory(rep_dir)) {
						out.println("");
						out.print  ("Specified path does not denote a directory.  Press enter to retry...");
						in.readLine();
						continue root_menu_create_experiment_get_source_repository_dir;
					}
					
					break root_menu_create_experiment_get_source_repository_dir;
				}
			}
		} else {
			rep_dir = Files.createTempDirectory(SystemUtil.getTemporaryDirectory(), "fake_repository");
		}
		
		if(generation_type == ExperimentSpecification.MANUAL_GENERATION_TYPE) {
			root_menu_create_experiment_get_import_file:
				while(true) {
					out.println("");
					out.println("Specify import clone specification. Provide full path to the file. The specified");
					out.println("clones must be of the same language and granularity as specified previously.");
					out.println("Leave blank to cancel new experiment creation.");
					out.println("--------------------------------------------------------------------------------");
					out.print  (":::: ");
					input = in.readLine();
					
					//Canceled
					if(input.equals("")) {
						experiment = null;
						out.println("");
						out.print  ("Create experiment canceled.  Press enter to return to root menu...");
						in.readLine();
						return;
					
					//ProcessDFile
					} else {
						try {
							import_file = Paths.get(input);
						} catch (InvalidPathException e) {
							out.println("");
							out.print  ("Invalid path.  Press enter to retry...");
							in.readLine();
							continue root_menu_create_experiment_get_import_file;
						}
						
						//Check Not Exists
						if(!Files.exists(import_file)) {
							out.print  ("File does not exists.  Press enter to retry...");
							in.readLine();
							continue root_menu_create_experiment_get_import_file;
						}
						
						//Check Directory
						if(!Files.isRegularFile(import_file)) {
							out.println("");
							out.print  ("Specified path does not denote a file.  Press enter to retry...");
							in.readLine();
							continue root_menu_create_experiment_get_import_file;
						}
						
						break root_menu_create_experiment_get_import_file;
					}
				}
		}
		
		out.println("");
		out.println("The new experiment will now be initialized.  During which, the subject system");
		out.println("and source repository will be imported.  They will be normalized for");
		out.println("compatibility with the framework.  Depending on their size, this may take some");
		out.println("time.  Press enter to begin creation...");
		out.print("--------------------------------------------------------------------------------");
		in.readLine();
		
		
		ExperimentSpecification es = new ExperimentSpecification(exp_dir, sub_dir, rep_dir, language);
		
		try {
			if(generation_type == ExperimentSpecification.AUTOMATIC_GENERATION_TYPE) {
				experiment = Experiment.createAutomaticExperiment(es, System.out);
			} else {
				es.setFragmentMaxSizeLines(Integer.MAX_VALUE);
				es.setFragmentMaxSizeTokens(Integer.MAX_VALUE);
				es.setFragmentMinSizeLines(1);
				es.setFragmentMinSizeTokens(1);
				es.setFragmentType(granularity);
				es.setInjectNumber(1);
				es.setMaxFragments(1);
				es.setMutationAttempts(25);
				es.setMutationContainment(0);
				es.setOperatorAttempts(10);
				es.setPrecisionRequiredSimilarity(0.50);
				es.setRecallRequiredSimilarity(0.50);
				es.setSubsumeMatcherTolerance(0.15);
				es.setLanguage(language);
				experiment = Experiment.createManualExperiment(es, import_file, System.out);
				boolean success = experiment.generate();
				if(success) {
					out.println("--------------------------------------------------------------------------------");
					out.print  ("Generation phase completed successfully.  Press enter to return to main menu...");
					in.readLine();
				} else {
					out.println("--------------------------------------------------------------------------------");
					out.println("Generation phase errored.  Please see execution log for details.  Experiment is");
					out.print  ("now currrupt.  Press enter to return to main menu.");
					in.readLine();
				}
			}
			
		} catch (IllegalArgumentException | SQLException | InterruptedException | ArtisticStyleFailedException | FileSanetizationFailedException | IllegalStateException | IllegalManualImportSpecification e) {
			experiment = null;
			out.print("--------------------------------------------------------------------------------");
			out.println("");
			out.println("Experiment creation failed for reason: " + e.getMessage());
			e.printStackTrace(out);
			out.println("Press enter to return to root menu...");
			return;
		}
		
		out.print("--------------------------------------------------------------------------------");
		out.println("");
		out.print  ("The experiment has been successfully initialized.  Press enter to continue...");
		in.readLine();
	}
	
	/*
	 * Root Menu - Load Experiment. 'experiment' field is set to the loaded experiment, or null if canceled.
	 */
	public static void root_menu_load_experiment() throws IOException {
		while(true) {
			out.println("");
			out.println("================================================================================");
			out.println("    Load Existing Experiment");
			out.println("================================================================================");
			out.println("Specify complete path to experiment's directory.  Leave blank to cancel load.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			String input = in.readLine();
			
			// If Cancel
			if(input.equals("")) {
				experiment = null;
				out.println("");
				out.print  ("Load experiment cancled.  Press enter to return to main menu...");
				in.readLine();
				return;
			
			// If Load
			} else {
				Path expd;
				
				//Check Path Valid
				try {
					expd= Paths.get(input);
				} catch (InvalidPathException e) {
					out.println("");
					out.print  ("Specified path is invalid.  Press enter to retry...");
					in.readLine();
					continue;
				}
				
				//Check Path Exists
				if(!Files.exists(expd)) {
					out.println("");
					out.print  ("Specified path does not exist.  Press enter to retry...");
					in.readLine();
					continue;
				}
				
				//Check Path Denotes a Directory
				if(!Files.isDirectory(expd)) {
					out.println("");
					out.print  ("Specified path does not denote a directory.  Press enter to retry...");
					in.readLine();
					continue;
				}
				
				//Load Experiment
				try {
					experiment = Experiment.loadExperiment(expd, System.out);
				} catch (IllegalArgumentException | SQLException e) {
					experiment = null;
					out.println("");
					out.println("Error: " + e.getMessage());
					out.println("");
					out.println("Specified directory does not appear to contain an experiment, or the experiment");
					out.print  ("is currupted.  Press enter to retry...");
					in.readLine();
					continue;
				}
				out.println("");
				out.print  ("Experiment loaded successfully.  Press enter to continue to Main Menu...");
				in.readLine();
				return;
				
			}
		}
	}
	
	/*
	 * Root Menu - Help - Describes the options of the root menu.
	 */
	public static void root_menu_help() throws IOException {
		out.println("");
		out.println("[1] Create New Experiment.");
		out.println("    Creates a new evaluation experiment.  Upon selection, user is requested for");
		out.println("    a directory to store the experiment in, the directory containing the source");
		out.println("    repository, the directory containing the subject system, and the language");
		out.println("    of the experiment.  The experiment is then initialized, and the source");
		out.println("    repository and subject system imported.  This may take some time.");
		out.println("[2] Load Existing Experiment.");
		out.println("    Loads an existing experiment.  User specifies the directory containing the");
		out.println("    experiment.  The experiment resumes where it was left off previously.");
		out.println("[h] Help.");
		out.println("    Shows this menu description.");
		out.println("[x] Exit Framework.");
		out.println("    Closes the framework application..");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to main menu....");
		in.readLine();
	}
	
	/*
	 * Root Menu - Exit Framework - Closes the framework.
	 */
	public static void root_menu_exit_framework() {
		out.println("");
		out.println("");
		out.println("********************************************************************************");
		out.println("************  Thank You for Using the Mutation Injection Framework  ************");
		out.println("********************************************************************************");
		out.println("");
		System.exit(0);
	}

//-------------------------------------------------------------------------------------------------
//---- Main Menu ----------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
	public static void main_menu() throws IOException, SQLException {
		boolean open = true;
		while(open) {
			out.println("");
			out.println("********************************************************************************");
			out.println("");
			
			switch(experiment.getStage()) {
				case Experiment.GENERATION_SETUP_STAGE:
					open = main_menu_generation_setup_stage();
					break;
				case Experiment.GENERATION_STAGE:
					open = main_menu_generation_phase_stage();
					break;
				case Experiment.EVALUATION_SETUP_STAGE:
					open = main_menu_evaluation_setup_stage();
					break;
				case Experiment.EVALUATION_STAGE:
					open = main_menu_evaluation_phase_stage();
					break;
				case Experiment.RESULTS_STAGE:
					open = main_menu_results_stage();
					break;
				case Experiment.ERROR_STAGE:
					open = main_menu_error_stage();
					break;
				default:
					open = main_menu_invalid_stage();
					break;
			}
			if(open == false) {
				return;
			}
		}
		out.println("");
		out.print  ("Experiment closed.  Press enter to return to root menu...");
		in.readLine();
		
	}
	
//-------------------------------------------------------------------------------------------------
//---- Generation Phase Setup ---------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
	
	public static boolean main_menu_generation_setup_stage() throws IOException, SQLException {
		out.println("================================================================================");
		out.println("    Stage 1 of 5: Generation Phase Setup");
		out.println("================================================================================");
		out.println("Experiment: " + experiment.getDirectory());
		out.println("================================================================================");
		out.println("[c] Configure Mutators and Mutation Operators.");
		out.println("");
		out.println("[1] Review Options.");
		out.println("[2] Set Max Fragments.");
		out.println("[3] Set Clone Granularity.");
		out.println("[4] Set Clone Size.");
		out.println("[5] Set Minimum Clone Similarity.");
		out.println("[6] Set Mutation Containment.");
		out.println("[7] Set Injection Number.");
		out.println("[8] Set Mutation Operator Attempts.");
		out.println("[9] Set Mutator Attempts.");
		out.println("");
		out.println("[s]  Begin Generation Phase.");
		out.println("");
		out.println("[h]  Help.");
		out.println("[x]  Close Experiment.");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		
		String input = in.readLine();
		switch(input) {
			case "c":
				main_menu_generation_configure_mutators_and_mutation_operators();
				break;
			case "1":
				main_menu_generation_review_options();
				break;
			case "2":
				main_menu_generation_set_max_fragments();
				break;
			case "3":
				main_menu_generation_set_clone_granularity();
				break;
			case "4":
				main_menu_generation_set_clone_size();
				break;
			case "5":
				main_menu_generation_set_minimum_clone_similarity();
				break;
			case "6":
				main_menu_generation_set_mutation_containment();
				break;
			case "7":
				main_menu_generation_set_injection_number();
				break;
			case "8":
				main_menu_generation_set_mutation_operator_attempts();
				break;
			case "9":
				main_menu_generation_set_mutator_attempts();
				break;
			case "s":
				main_menu_generation_setup_stage_begin_generation_phase();
				break;
			case "h":
				main_menu_generation_setup_stage_help();
				break;
			case "x":
				return false;
			default:
				out.print("Invalid selection, press enter to return to main menu...");
				in.readLine();
				break;
		}
		return true;
	}
	
	private static void main_menu_generation_review_options() throws SQLException, IOException {
		out.println("");
		
		out.println("================================================================================");
		out.println("    >Review: Generation Options");
		out.println("================================================================================");
		out.println("           Fragment Language: " + ExperimentSpecification.languageToString(experiment.getLanguage()));
		out.println("               Fragment Type: " + ExperimentSpecification.fragmentTypeToString(experiment.getFragmentType()));
		out.println("               Max Fragments: " + experiment.getMaxFragments());
		out.println("        Minimum Size (Lines): " + experiment.getFragmentMinimumSizeLines());
		out.println("       Minimum Size (Tokens): " + experiment.getFragmentMinimumSizeTokens());
		out.println("        Maximum Size (Lines): " + experiment.getFragmentMaximumSizeLines());
		out.println("       Maximum Size (Tokens): " + experiment.getFragmentMaximumSizeTokens());
		out.println("            Injection Number: " + experiment.getInjectionNumber());
		out.println("    Minimum Clone Similarity: " + (1 - experiment.getAllowedFragmentDifference()));
		out.println("        Mutation Containment: " + experiment.getMutationContainment());
		out.println("");
		out.println("           Operator Attempts: " + experiment.getOperatorAttempts());
		out.println("            Mutator Attempts: " + experiment.getMutationAttempts());
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to main menu...");
		in.readLine();
	}
	
	private static void main_menu_generation_set_max_fragments() throws SQLException, IOException {
		String input;
		int max;
		
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Max Fragments");
		out.println("================================================================================");
		while(true) {
			out.println("Specify max fragments.  Leave blank to cancel.");
			out.println("");
			out.println("Description:");
			out.println("    The maximum number of code fragments to select from the source repository");
			out.println("    during the clone generation process.  This parameter defines the maximum");
			out.println("    number of clones in the generated corpus.  The maximum number of generated");
			out.println("    clonses is equal to this value multiplied by the number of mutators.  Less");
			out.println("    clones may be geneated if the repository runs out of eligible fragments");
			out.println("    (those mutable by all the mutators and which produced clone satisfy the");
			out.println("    other properties) before this value is reached.");
			out.println("");
			out.println("Valid Range: [1," + Integer.MAX_VALUE + "]");
			out.println("");
			out.println("Default Value: 100");
			out.println("");
			out.println("Current Value: " + experiment.getMaxFragments());
			out.println("");
			out.println("Advice:");
			out.println("    When picking a value, consider the number of mutators and the injection");
			out.println("    number as well.  This defines the maximum number of mutant systems, which");
			out.println("    is the major deterimination in the execution time of the evaluation phase.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.print  ("Set max fragments canceled.  Press enter to return to main menu...");
				in.readLine();
				return;
			}
			
			try {
				max = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				out.println("");
				out.println("Invalid integer.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(max < 1) {
				out.println("");
				out.println("Not in range.  press enter to retry...");
				in.readLine();
				continue;
			}
			
			experiment.setMaxFragments(max);
			out.println("");
			out.print  ("Maximum fragmnets successfully set.  Press enter to return to main menu...");
			in.readLine();
			return;
		}
	}
	
	private static void main_menu_generation_set_clone_granularity() throws IOException, IllegalStateException, IllegalArgumentException, SQLException {
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Clone Granularity");
		out.println("================================================================================");
		while(true) {
			out.println("Specify granularity of the clones to be generated.  Leave blank to cancel.");
			out.println("    [1] - Function.");
			out.println("    [2] - Block.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			String input = in.readLine();
			switch(input) {
				case "1":
					out.println("");
					out.print("Clone granularity successfully set.  Press enter to return to main menu...");
					in.readLine();
					experiment.setCloneGranularity(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE);
					return;
				case "2":
					out.println("");
					out.print("Clone granularity successfully set.  Press enter to return to main menu...");
					in.readLine();
					experiment.setCloneGranularity(ExperimentSpecification.BLOCK_FRAGMENT_TYPE);
					return;
				case "":
					out.println("");
					out.print("Set clone granularity canceled.  Press enter to return to main menu...");
					in.readLine();
					return;
				default:
					out.println("");
					out.print ("Invalid Selection.  Press enter to retry...");
					in.readLine();
					out.println("");
			}
		}
	}
	
	private static void main_menu_generation_set_clone_size() throws IOException, SQLException {
		String input;
		int min_tok;
		int max_tok;
		int min_line;
		int max_line;
		
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Clone Size");
		out.println("================================================================================");
		out.println("Following is a series of prompts for specifying the minimum and maximum fragment");
		out.println("size of the generated clones, by token and by line.  The purpose of this");
		out.println("constraint is to make it possible to configure subject tools which require a");
		out.println("clone size be specified as a runtime parameter.  ");
		
		//MinTokens
		while(true) {
			out.println("");
			out.println("Specify minimum clone fragment size in tokens.  Leave blank to cancel.");
			out.println("Valid Range: [1," + (Integer.MAX_VALUE-1) + "]");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			if(input.equals("")) { //If Cancel
				out.println("");
				out.print("Set clone size cancled, press enter to return to main menu...");
				in.readLine();
				return;
			} else { //If Value
				try { //Read Value
					min_tok = Integer.parseInt(input);
				} catch (NumberFormatException e) { //If Invalid
					out.println("");
					out.print("Not a valid integer.  Press enter to retry.");
					in.readLine();
					continue;
				}
				if(min_tok <=0 || min_tok == Integer.MAX_VALUE) { //if out of range
					out.println("");
					out.print("Invalid value.  Press enter to retry.");
					in.readLine();
					continue;
				}
			}
			break;
		}
		
		//MaxTokens
		while(true) {
			out.println("");
			out.println("Specify maximum clone fragment size in tokens.  Leave blank to cancel.");
			out.println("Valid Range: [" + (min_tok + 1) + "," + Integer.MAX_VALUE + "]");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			if(input.equals("")) { // if cancel
				out.println("");
				out.print("Set clone size canceled, press enter to return to main menu...");
				in.readLine();
				return;
			} else { //if value
				try { //read value
					max_tok = Integer.parseInt(input);
				} catch (NumberFormatException e) { //if invalid
					out.println("");
					out.print("Not a valid integer.  Press enter to retry.");
					in.readLine();
					continue;
				}
				if(max_tok <= min_tok) { //if out of range
					out.println("");
					out.print("Invalid value.  Press enter to retry.");
					in.readLine();
					continue;
				}
			}
			break;
		}
		
		//MinLines
		while(true) {
			out.println("");
			out.println("Specify minimum clone fragment size in lines.  Leave blank to cancel.");
			out.println("Valid Range: [1," + (Integer.MAX_VALUE-1) + "]");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			if(input.equals("")) { //If Cancel
				out.println("");
				out.print("Set clone size cancled, press enter to return to main menu...");
				in.readLine();
				return;
			} else { //If Value
				try { //Read Value
					min_line = Integer.parseInt(input);
				} catch (NumberFormatException e) { //If Invalid
					out.println("");
					out.print("Not a valid integer.  Press enter to retry.");
					in.readLine();
					continue;
				}
				if(min_line <=0 || min_line == Integer.MAX_VALUE) { //if out of range
					out.println("");
					out.print("Invalid value.  Press enter to retry.");
					in.readLine();
					continue;
				}
			}
			break;
		}
		
		//MaxLines
		while(true) {
			out.println("");
			out.println("Specify maximum clone fragment size in lines.  Leave blank to cancel.");
			out.println("Valid Range: [" + (min_line + 1) + "," + Integer.MAX_VALUE + "]");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			if(input.equals("")) { // if cancel
				out.println("");
				out.print("Set clone size canceled, press enter to return to main menu...");
				in.readLine();
				return;
			} else { //if value
				try { //read value
					max_line = Integer.parseInt(input);
				} catch (NumberFormatException e) { //if invalid
					out.println("");
					out.print("Not a valid integer.  Press enter to retry.");
					in.readLine();
					continue;
				}
				if(max_line <= min_line) { //if out of range
					out.println("");
					out.print("Invalid value.  Press enter to retry.");
					in.readLine();
					continue;
				}
			}
			break;
		}
		
		//Set to limits (to prevent invalid numbers in real set)
		experiment.setFragmentMinimumSizeTokens(1);
		experiment.setFragmentMaximumSizeTokens(Integer.MAX_VALUE);
		experiment.setFragmentMinimumSizeLines(1);
		experiment.setFragmentMaximumSizeLines(Integer.MAX_VALUE);
		
		//Set to configuration
		experiment.setFragmentMinimumSizeTokens(min_tok);
		experiment.setFragmentMaximumSizeTokens(max_tok);
		experiment.setFragmentMinimumSizeLines(min_line);
		experiment.setFragmentMaximumSizeLines(max_line);
		
		
		out.println("");
		out.print("New clone size set.  Press enter to return to main menu...");
		in.readLine();
	}
	
	private static void main_menu_generation_set_minimum_clone_similarity() throws IOException, SQLException {
		String input;
		double similarity;
		
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Minimum Clone Similarity");
		out.println("================================================================================");
		while(true) {
			out.println("Specify minimum clone similarity, or blank to cancel.");
			out.println("");
			out.println("Description:");
			out.println("    This is the minimum clone similarity of the generated clones.  Similarity is");
			out.println("    measured as the percentage of shared items between the code fragments of the");
			out.println("    clone, as determined by a diff-like algorithm.  The generated clones will");
			out.println("    satsify this similarity measure for both line and token items.");
			out.println("");
			out.println("Valid Range: [0.0, 1.0]");
			out.println("");
			out.println("Default Value: 0.70");
			out.println("");
			out.println("Current Value: " + (1-experiment.getAllowedFragmentDifference()));
			out.println("");
			out.println("Advice:");
			out.println("    The value chosen defines how similar generated type 3 clones will be after");
			out.println("    type 1 and type 2 normalizations.  The ideal minimum similarity for type 3");
			out.println("    clones is 0.70-0.80.  Higher or lower can be used to create a corpus with");
			out.println("    a stricter or looser type 3 definition.  This parameter should be kept");
			out.println("    greater than 0.50 to ensure that the generated clones contain more cloned");
			out.println("    code than non-cloned code.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) { // canceled
				out.println("");
				out.print  ("Set minimum clone similarity canceled, press enter to return to main menu.");
				in.readLine();
				return;
			} else { // value
				try { //get value
					similarity = Double.parseDouble(input);
				} catch (NumberFormatException e) { //check double
					out.println("");
					out.print  ("Not a valid double.  Press enter to retry.");
					in.readLine();
					out.println("");
					continue;
				}
				if(similarity < 0.0 || similarity > 1.0) { //check value
					out.println("");
					out.print  ("Invalid value.  Press enter to retry...");
					in.readLine();
					out.println("");
					continue;
				}
			}
			break;
		}
		
		//set
		experiment.setAllowedFragmentDifference(1 - similarity);
		out.println("");
		out.print("New minimum clone similarity set.  Press enter to return to main menu...");
		in.readLine();
	}
	
	private static void main_menu_generation_set_mutation_containment() throws SQLException, IOException {
		String input;
		double containment;
		
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Mutation Containment");
		out.println("================================================================================");
		while(true) {
			out.println("Specify the mutation containment, or blank to cancel.");
			out.println("");
			out.println("Description:");
			out.println("    When a selected fragment is mutated to form a generated clone pair between");
			out.println("    the selected and mutated code fragments, this defines how far from the edges");
			out.println("    of the selected fragment is the mutation(s) allowed to occur.  This ensures");
			out.println("    the the clone starts and ends with cloned code, as otherwise the mutation");
			out.println("    will be external to the clone, and ignored by the subject tools.  Mutation");
			out.println("    containment is expressed as a ratio of the selected fragment's size.  For");
			out.println("    a selected framgnet 10 lines long will not be mutated within its first and");
			out.println("    last 3 lines if mutation containment is 0.30.");
			out.println("");
			out.println("Valid Range: [0.0, 0.5]");
			out.println("");
			out.println("Default Value: 0.15");
			out.println("");
			out.println("Current Value: " + (1-experiment.getMutationContainment()));
			out.println("");
			out.println("Advice:");
			out.println("    Should leave about 2-3 lines unmodified at the start and end of the");
			out.println("    generated clone's code fragment.  This will depend on the minimum clone size");
			out.println("    set.  For example, for a minimum size of 15 lines, a 0.15 mutation");
			out.println("    containment would be appropriate.  Should not be set much higher than 0.20");
			out.println("    or mutation will become too constricted to the center of the clones.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) { // canceled
				out.println("");
				out.print  ("Set mutation containment canceled, press enter to return to main menu.");
				in.readLine();
				return;
			} else { // value
				try { //get value
					containment = Double.parseDouble(input);
				} catch (NumberFormatException e) { //check double
					out.println("");
					out.print  ("Not a valid double.  Press enter to retry.");
					in.readLine();
					out.println("");
					continue;
				}
				if(containment < 0.0 || containment > 0.5) { //check value
					out.println("");
					out.print  ("Invalid value.  Press enter to retry...");
					in.readLine();
					out.println("");
					continue;
				}
			}
			break;
		}
		
		//set
		experiment.setMutationContainment(containment);
		out.println("");
		out.print("New mutation containment set.  Press enter to return to main menu...");
		in.readLine();
	}
	
	private static void main_menu_generation_set_injection_number() throws IOException, IllegalStateException, IllegalArgumentException, SQLException {
		String input;
		int injection_number;
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Injection Number");
		out.println("================================================================================");
		while(true) {
			out.println("Specify the injection number, or blank to cancel.");
			out.println("");
			out.println("Description:");
			out.println("    This is the number of mutant systems to generate per generated clone, each");
			out.println("    using a unique injection locations.  Clones produced from the same selected");
			out.println("    code fragment use the same set of injection locations.  A subject tool's");
			out.println("    ability to detect a particular clone is affected both by the contnets of the");
			out.println("    clone and its location in the mutant system.  By using an injection number");
			out.println("    greater than one, the measure of recall for a particular generated clone is");
			out.println("    improved by varying its location.  Note, injection locations are chosen");
			out.println("    randomly and independently for each set of clones produced from a particular");
			out.println("    selected fragment.  A large injection number is not needed to provide strong");
			out.println("    variation in clone location across the corpus, its purpose is to add");
			out.println("    variation for individual clones of the corpus");
			out.println("");
			out.println("Valid Range: [1, " + Integer.MAX_VALUE + "]");
			out.println("");
			out.println("Default Value: 5");
			out.println("");
			out.println("Current Value: " + experiment.getInjectionNumber());
			out.println("");
			out.println("Advice:");
			out.println("    A value of 5 or 10 should provide sufficient variation of injection location");
			out.println("    for each generated clone.  A value of 1 can be used when variation is not");
			out.println("    desired.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.print  ("Set injection number canceled, press enter to return to main menu.");
				in.readLine();
				return;
			} else {
				try {
					injection_number = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("");
					out.print  ("Not a valid integer.  Press enter to retry.");
					in.readLine();
					out.println("");
					continue;
				}
				if(injection_number <=0) {
					out.println("");
					out.print  ("Invalid value.  Press enter to retry...");
					in.readLine();
					out.println("");
					continue;
				}
			}
			break;
		}
		
		//set
		experiment.setInjectionNumber(injection_number);
		out.println("");
		out.print("New mutation containment set.  Press enter to return to main menu...");
		in.readLine();
	}
	
	private static void main_menu_generation_set_mutation_operator_attempts() throws IllegalStateException, IllegalArgumentException, SQLException, IOException {
		String input;
		int operator_attempts;
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Mutation Operator Attempts");
		out.println("================================================================================");
		while(true) {
			out.println("Specify the number of mutation operator attempts, or blank to cancel.");
			out.println("");
			out.println("Description:");
			out.println("    During the mutation process, this is the number of times to attempt an");
			out.println("    individual mutation operator.  The opeator is repeated if the mutation");
			out.println("    causes the geneated clone to not satisfy the required corpus constraints.");
			out.println("");
			out.println("Valid Range: [1, " + Integer.MAX_VALUE + "]");
			out.println("");
			out.println("Default Value: 10");
			out.println("");
			out.println("Current Value: " + experiment.getOperatorAttempts());
			out.println("");
			out.println("Advice:");
			out.println("    A value of 10 is a good compromise between time spent on a retrying a");
			out.println("    particular operator, and the chance of giving up on it early.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.print  ("Set mutation operator attempts canceled, press enter to return to main menu.");
				in.readLine();
				return;
			} else {
				try {
					operator_attempts = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("");
					out.print  ("Not a valid integer.  Press enter to retry.");
					in.readLine();
					out.println("");
					continue;
				}
				if(operator_attempts <=0) {
					out.println("");
					out.print  ("Invalid value.  Press enter to retry...");
					in.readLine();
					out.println("");
					continue;
				}
			}
			break;
		}
		
		//set
		experiment.setOperatorAttempts(operator_attempts);
		out.println("");
		out.print("New mutation operator attempts set.  Press enter to return to main menu...");
		in.readLine();
	}
	
	private static void main_menu_generation_set_mutator_attempts() throws IllegalStateException, IllegalArgumentException, SQLException, IOException {
		String input;
		int mutator_attempts;
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Mutator Attempts");
		out.println("================================================================================");
		while(true) {
			out.println("Specify the number of mutator attempts, or blank to cancel.");
			out.println("");
			out.println("Description:");
			out.println("    During the mutation process, this is the number of times to attempt an");
			out.println("    individual mutation operator.  The opeator is repeated if the mutation");
			out.println("    causes the geneated clone to not satisfy the required corpus constraints.");
			out.println("");
			out.println("Valid Range: [1, " + Integer.MAX_VALUE + "]");
			out.println("");
			out.println("Default Value: 100");
			out.println("");
			out.println("Current Value: " + experiment.getMutationAttempts());
			out.println("");
			out.println("Advice: Default is fine.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.print  ("Set mutator attempts canceled, press enter to return to main menu.");
				in.readLine();
				return;
			} else {
				try {
					mutator_attempts = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("");
					out.print  ("Not a valid integer.  Press enter to retry.");
					in.readLine();
					out.println("");
					continue;
				}
				if(mutator_attempts <=0) {
					out.println("");
					out.print  ("Invalid value.  Press enter to retry...");
					in.readLine();
					out.println("");
					continue;
				}
			}
			break;
		}
		
		//set
		experiment.setMutationAttempts(mutator_attempts);
		out.println("");
		out.print("New mutator attempts set.  Press enter to return to main menu...");
		in.readLine();
	}

	private static void main_menu_generation_setup_stage_begin_generation_phase() throws SQLException, IOException {
		boolean success;
		if(!(experiment.numMutators() > 0)) {
			out.println("");
			out.println("No mutators have been defined.  Can not begin generation.  Please define some.");
			out.print  ("Press enter to return to main menu...");
			in.readLine();
		} else {
			out.println("");
			out.println("********************************************************************************");
			out.println("");
			out.println("================================================================================");
			out.println("    Stage 2 of 5: Generation Phase Execution");
			out.println("================================================================================");
			out.println("Experiment: " + experiment.getDirectory());
			out.println("================================================================================");
			out.println("Execution of the generation phase has begun.  Please see progress log:");
			out.println("--------------------------------------------------------------------------------");
			success = experiment.generate();
			if(success) {
				out.println("--------------------------------------------------------------------------------");
				out.print  ("Generation phase completed successfully.  Press enter to return to main menu...");
				in.readLine();
			} else {
				out.println("--------------------------------------------------------------------------------");
				out.println("Generation phase errored.  Please see execution log for details.  Experiment is");
				out.print  ("now currrupt.  Press enter to return to main menu.");
				in.readLine();
			}
		}
	}

	public static void main_menu_generation_setup_stage_help() throws IOException {
		out.println("");
		out.println("================================================================================");
		out.println("    >Help");
		out.println("================================================================================");
		out.println("[1] Configure Mutators and Mutation Operators.");
		out.println("    Allows the configuration of the mutators and mutation operators.  Done");
		out.println("    through the mutation sub menu.");
		out.println("[2] Review Options.");
		out.println("    Summarizes the generation phase settings currently set.");
		out.println("[3] Set Clone Granularity.");
		out.println("    Describes the clone granularity option and allows it to be set.");
		out.println("[4] Set Clone Size.");
		out.println("    Allows the clone size options to be set, including the minimum and ");
		out.println("    maximum size of the clone in tokens and lines.");
		out.println("[5] Set Minimum Clone Similarity.");
		out.println("    Describes the minimum clone similarity option and allows it to be set.");
		out.println("[6] Set Mutation Containment.");
		out.println("    Describes the mutation containment option and allows it to be set.");
		out.println("[7] Set Injection Number.");
		out.println("    Describes the injection number option and allows it to be set.");
		out.println("[8] Set Mutation Operator Attempts.");
		out.println("    Describes the mutation operator attempts option and allows it to be set.");
		out.println("[9] Set Mutator Attempts.");
		out.println("    Describes the mutator attempts option and allows it to be set.");
		out.println("[s] Begin Generation Phase.");
		out.println("    Begins the execution of the generation phase using the settings configured");
		out.println("    during this stage.  The framework is non-interactive during the generation");
		out.println("    phase.  Once the evaluation phase is completed, the user regains control");
		out.println("    for the evaluation phase setup stage.");
		out.println("[h] Help.");
		out.println("    Shows this dialogue.");
		out.println("[x] Close Experiment.");
		out.println("    Closes the experiment and returns to the root menu.");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to main menu....");
		in.readLine();
	}
	
//-------------------------------------------------------------------------------------------------
//---- Mutation Configuration Sub-Menu ------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
	
	private static void main_menu_generation_configure_mutators_and_mutation_operators() throws IOException, SQLException {
		String input;
		while(true) {
			out.println("");
			out.println("================================================================================");
			out.println("    >Configure Mutators and Mutation Operators");
			out.println("================================================================================");
			out.println("[1] List Mutation Operators.");
			out.println("[2] Add Mutation Operator.");
			out.println("[3] Remove Mutation Operator.");
			out.println("[4] Remove All Mutation Operators.");
			out.println("");
			out.println("[5] List Mutators.");
			out.println("[6] Add Mutator.");
			out.println("[7] Remove Mutator.");
			out.println("[8] Remove All Mutation Operators.");
			out.println("");
			out.println("[r] Restore Default Mutators and Mutation Operators.");
			out.println("");
			out.println("[h] Help.");
			out.println("[x] Return to Main Menu.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			switch(input) {
			case "1":
				main_menu_generation_configure_mutators_and_mutation_operators_list_operators();
				break;
			case "2":
				main_menu_generation_configure_mutators_and_mutation_operators_add_operator();
				break;
			case "3":
				main_menu_generation_configure_mutators_and_mutation_operators_remove_operator();
				break;
			case "4":
				main_menu_generation_configure_mutators_and_mutation_operators_remove_all_operators();
				break;
			case "5":
				main_menu_generation_configure_mutators_and_mutation_operators_list_mutators();
				break;
			case "6":
				main_menu_generation_configure_mutators_and_mutation_operators_add_mutator();
				break;
			case "7":
				main_menu_generation_configure_mutators_and_mutation_operators_remove_mutator();
				break;
			case "8":
				main_menu_generation_configure_mutators_and_mutation_operators_remove_all_mutators();
				break;
			case "r":
				main_menu_generation_configure_mutators_and_mutation_operators_restore_defaults();
				break;
			case "h":
				main_menu_generation_configure_mutators_and_mutation_operators_help();
				break;
			case "x":
				return;
			default:
				out.println("");
				out.print  ("Invalid selection.  Press enter to return to sub menu...");
				in.readLine();
				break;
			}
		}
	}
	
	private static void main_menu_generation_configure_mutators_and_mutation_operators_list_operators() throws SQLException, IOException {
		out.println("");
		out.println("================================================================================");
		out.println("    >>List Mutation Operators");
		out.println("================================================================================");
		for (OperatorDB operator : experiment.getOperators()) {
			out.println("[" + operator.getId() + "]");
			out.println("           Name: " + operator.getName());
			out.println("    Description: " + operator.getDescription());
			out.println("     Clone Type: " + operator.getTargetCloneType());
			out.println("     Executable: " + operator.getMutator());
		}
		out.println("--------------------------------------------------------------------------------");
		out.print ("Press enter to return to mutation sub menu...");
		in.readLine();
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_add_operator() throws IOException, IllegalStateException, SQLException {
		String name, description, clonetype, sexecutable;
		Path executable;
		int iclonetype;
		
		out.println("");
		out.println("================================================================================");
		out.println("    >>Add Mutation Operator");
		out.println("================================================================================");
		
		//Get Operator Name
		out.println("");
		out.println("Enter a name for the mutation operator (or blank to cancel).");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		name = in.readLine();
		if(name.equals("")) {
			out.println("");
			out.print  ("Add mutation operator canceled.  Press enter to return to sub menu...");
			in.readLine();
			return;
		}
		
		//Get Operator Description
		out.println("");
		out.println("Enter a description of the operator (or blank to cancel).");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		description = in.readLine();
		if(description.equals("")) {
			out.println("");
			out.print  ("Add mutation operator canceled.  Press enter to return to sub menu...");
			in.readLine();
			return;
		}
		
		//Get clone type
		while(true) {
			out.println("");
			out.println("Enter the clone type created by this operator (1, 2 or 3; or blank to cancel).");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			clonetype = in.readLine();
			if(clonetype.equals("")) {
				out.println("");
				out.print  ("Add mutation operator canceled.  Press enter to return to sub menu...");
				in.readLine();
				return;
			} else if (clonetype.equals("1")) {
				iclonetype = 1;
				break;
			} else if (clonetype.equals("2")) {
				iclonetype = 2;
				break;
			} else if (clonetype.equals("3")) {
				iclonetype = 3;
				break;
			} else {
				out.println("");
				out.print  ("Invalid clone type.  Press enter to retry...");
				in.readLine();
				continue;
			}
		}
		
		//Get executable
		while(true) {
			out.println("");
			out.println("Enter full path to operator executable (or blank to cancel).");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			sexecutable = in.readLine();
			
			if(sexecutable.equals("")) {
				out.println("");
				out.print  ("Add mutation operator canceled.  Press enter to return to sub menu...");
				in.readLine();
				return;
			}
			
			try {
				executable = Paths.get(sexecutable).toAbsolutePath().normalize();
			} catch (InvalidPathException e) {
				out.println("");
				out.print  ("Not a legal path.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(!Files.exists(executable)) {
				out.println("");
				out.print  ("Executable does not exist.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(!Files.isRegularFile(executable) || !Files.isExecutable(executable)) {
				out.println("");
				out.print  ("Not an executable regular file.  Press enter to retry...");
				in.readLine();
				continue;
			}
			break;
		}
		
		OperatorDB op = experiment.addOperator(name, description, iclonetype, executable);
		out.println("");
		out.println("Mutation operator successfully added.  It's id is: " + op.getId() + ".");
		out.print  ("Press enter to return to mutation sub menu...");
		in.readLine();
		return;	
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_remove_operator() throws IOException, IllegalStateException, SQLException {
		String input;
		int id;
		out.println("");
		out.println("================================================================================");
		out.println("    >>Remove Mutation Operator");
		out.println("================================================================================");
		while(true) {
			out.println("Enter the id of the mutation operator to remove, or blank to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			if(input.equals("")) {
				out.println("");
				out.println("Remove mutation operator canceled.");
				out.print  ("Press enter to return to mutation sub menu...");
				in.readLine();
				return;
			} else {
				try {
					id = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("");
					out.println("Invalid id, press enter to retry...");
					in.readLine();
					continue;
				}
				
				if(experiment.removeOperator(id)) {
					out.println("");
					out.println("Operator " + id + " successfully removed.");
					out.print  ("Press enter to return to mutation sub menu...");
					in.readLine();
					return;
				} else {
					out.println("");
					out.println("No mutation operator exists with that id.  Press enter to retry...");
					in.readLine();
					continue;
				}
			}
		}	
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_remove_all_operators() throws IOException, IllegalStateException, SQLException {
		out.println("");
		out.println("================================================================================");
		out.println("    >>Remove All Mutation Operators");
		out.println("================================================================================");
		out.println("This will remove all mutation operators registred with the experiment, and as a");
		out.println("consequence all of the mutators.  This action can not be reversed.  Are you sure");
		out.println("you wish to continue?  'y' for yes, any other input to cancel.");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		String input = in.readLine();
		if(input.equals("y")) {
			int num = experiment.removeOperators();
			out.println("");
			out.println(num + " mutation operatores have been removed.");
			out.print  ("Press enter to return to mutation sub menu...");
			in.readLine();
			return;
		} else {
			out.println("");
			out.println("Remove all mutation operators canceled.");
			out.print  ("Press enter to return to mutation sub menu...");
			in.readLine();
			return;
		}
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_list_mutators() throws SQLException, IOException {
		out.println("");
		out.println("================================================================================");
		out.println("    >>List Mutators");
		out.println("================================================================================");
		for (MutatorDB mutator : experiment.getMutators()) {
			out.println("[" + mutator.getId() + "]");
			out.println("    Description: " + mutator.getDescription());
			out.println("     Clone Type: " + mutator.getTargetCloneType());
			out.print  ("  Operator List: ");
				for(OperatorDB operator : mutator.getOperators()) {
					out.print(operator.getId() + " ");
				}
				out.println("");
		}
		out.println("--------------------------------------------------------------------------------");
		out.print ("Press enter to return to mutation sub menu...");
		in.readLine();
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_add_mutator() throws IllegalStateException, IllegalArgumentException, SQLException, IOException {
		String input;
		
		out.println("");
		out.println("================================================================================");
		out.println("    >>Add Mutator");
		out.println("================================================================================");
		if(experiment.numOperators() > 0) {
			String description;
			int id;
			List<Integer> operators = new LinkedList<Integer>();
			
			out.println("Enter a description of the mutator (leave blank to cancel).");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			description = in.readLine();
			if(description.equals("")) {
				out.println("");
				out.print  ("Add mutator canceled.  Press enter to return to sub menu...");
				in.readLine();
				return;
			}
			
			while(true) {
				out.println("");
				out.println("Enter id of operator #1 (leave blank to cancel).");
				out.println("--------------------------------------------------------------------------------");
				out.print  (":::: ");
				input = in.readLine();
				if(input.equals("")) {
					out.println("");
					out.print  ("Add mutator canceled.  Press enter to return to sub menu...");
					in.readLine();
					return;
				}
				try {
					id = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("");
					out.print ("Invalid mutation operator identifier.  Press enter to retry...");
					in.readLine();
					continue;
				}
				if(experiment.existsOperator(id)) {
					operators.add(id);
					break;
				} else {
					out.println("");
					out.print  ("Operator with id " + id + " does not exist.  Press enter to retry...");
					in.readLine();
					continue;
				}
			}
			
			while(true) {
				out.println("");
				out.println("Enter id of operator#" + (operators.size() + 1) + ".");
				out.println("Enter d to finish operators, or blank to cancel.");
				out.println("--------------------------------------------------------------------------------");
				out.print  (":::: ");
				input = in.readLine();
				
				//Cancel
				if(input.equals("")) {
					out.println("");
					out.print  ("Add mutator canceled.  Press enter to return to sub menu...");
					in.readLine();
					return;
				}
				
				//End
				if(input.equals("d")) {
					break;
				}
				
				//Parse
				try {
					id = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("");
					out.print ("Invalid mutation operator identifier.  Press enter to retry...");
					in.readLine();
					continue;
				}
				
				//If exists add, else retry
				if(experiment.existsOperator(id)) {
					operators.add(id);
					continue;
				} else {
					out.println("");
					out.print  ("Operator with id " + id + " does not exist.  Press enter to retry...");
					in.readLine();
					continue;
				}
			}
			
			//Add operator
			MutatorDB mutator = experiment.addMutator(description, operators);
			
			out.println("");
			out.println("Mutator successfully added.  It's id is: " + mutator.getId() + ".");
			out.print  ("Press enter to return to mutation sub menu...");
			in.readLine();
			return;
		} else {
			out.println("");
			out.println("No operators have been defined.  Can not create a mutator without operators.");
			out.print  ("Press enter to return to mutation sub menu...");
			in.readLine();
			return;
		}
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_remove_mutator() throws IOException, IllegalStateException, IllegalArgumentException, SQLException {
		String input;
		int id;
		out.println("");
		out.println("================================================================================");
		out.println("    >>Remove Mutator");
		out.println("================================================================================");
		while(true) {
			out.println("Enter the id of the mutator to remove, or blank to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			if(input.equals("")) {
				out.println("");
				out.println("Remove mutator canceled.");
				out.print  ("Press enter to return to mutation sub menu...");
				in.readLine();
				return;
			} else {
				try {
					id = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("");
					out.println("Invalid id, press enter to retry...");
					in.readLine();
					continue;
				}
				
				if(experiment.removeMutator(id)) {
					out.println("");
					out.println("Mutator " + id + " successfully removed.");
					out.print  ("Press enter to return to mutation sub menu...");
					in.readLine();
					return;
				} else {
					out.println("");
					out.println("No mutator exists with that id.  Press enter to retry...");
					in.readLine();
					continue;
				}
			}
		}	
		
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_remove_all_mutators() throws IOException, IllegalStateException, IllegalArgumentException, SQLException {
		out.println("");
		out.println("================================================================================");
		out.println("    >>Remove All Mutators");
		out.println("================================================================================");
		out.println("This will remove all mutators registerd with the experiment, and can not be");
		out.println("reversed.  Are you sure you wish to continue? 'y' for yes, or any other input to");
		out.println("cancel.");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		String input = in.readLine();
		if(input.equals("y")) {
			int num = experiment.removeMutators();
			out.println("");
			out.println(num + " mutators have been removed.");
			out.print  ("Press enter to return to mutation sub menu...");
			in.readLine();
		} else {
			out.println("");
			out.println("Remove all mutators canceled.");
			out.print  ("Press enter to return to mutation sub menu...");
			in.readLine();
		}
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_restore_defaults() throws IllegalStateException, SQLException, IOException {
		String input;
		out.println("");
		out.println("================================================================================");
		out.println("    >>Restore Defaults");
		out.println("================================================================================");
		out.println("This will remove all current mutation operators and mutators and replace them");
		out.println("with the default set.  This can not be reversed.  Are you sure you wish to");
		out.println("continue?  'y' to continue, or any other input to cancel.");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		input = in.readLine();
		
		if(input.equals("y")) {
			experiment.removeMutators();
			experiment.removeOperators();
			OperatorDB o;
			List<OperatorDB> operators = new LinkedList<OperatorDB>();
			o=experiment.addOperator("mCC_BT", "Change in comments: comment added between two tokens..", 1, Paths.get("operators/mCC_BT")); operators.add(o);
			o=experiment.addOperator("mCC_EOL", "Change in comments: comment added at end of a line.", 1, Paths.get("operators/mCC_EOL")); operators.add(o);
			o=experiment.addOperator("mNM_A", "Change in formatting: a newline is added between two tokens..", 1, Paths.get("operators/mCF_A")); operators.add(o);
			o=experiment.addOperator("mNL_R", "Change in formatting: a newline is removed (without changing meangin).", 1, Paths.get("operators/mCF_R")); operators.add(o);
			o=experiment.addOperator("mCF_A", "Change in whitespace: a space or tab is added between two tokens.", 1, Paths.get("operators/mCW_A")); operators.add(o);
			o=experiment.addOperator("mCF_R", "Change in whitespace: a space or tab is removed (without changing meaning).", 1, Paths.get("operators/mCW_R")); operators.add(o);
			o=experiment.addOperator("mSRI", "Renaming of identifier: systamtic renaming of all instances of a chosen identifier.", 2, Paths.get("operators/mSRI")); operators.add(o);
			o=experiment.addOperator("mARI", "Renaming of identifier: arbitrary renaming of a single identifier instance.", 2, Paths.get("operators/mARI")); operators.add(o);
			o=experiment.addOperator("mRL_N", "Change in literal value: a number value is replaced.", 2, Paths.get("operators/mRL_N")); operators.add(o);
			o=experiment.addOperator("mRL_S", "Change in literal value: a string value is replaced.", 2, Paths.get("operators/mRL_S")); operators.add(o);
			o=experiment.addOperator("mIL", "Insertion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mIL")); operators.add(o);
			o=experiment.addOperator("mDL", "Deletion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mDL")); operators.add(o);
			o=experiment.addOperator("mML", "Modification of a whole line ((without invalidating syntax).", 3, Paths.get("operators/mML")); operators.add(o);
			o=experiment.addOperator("mSIL", "Small insertion within a line (addition of a parameter).", 3, Paths.get("operators/mSIL")); operators.add(o);
			o=experiment.addOperator("mSDL", "Small deletion within a line (removal of a parameter).", 3, Paths.get("operators/mSDL")); operators.add(o);
			for(OperatorDB operator : operators) {
				LinkedList<Integer> oplist = new LinkedList<Integer>();
				oplist.add(operator.getId());
				experiment.addMutator(operator.getDescription(), oplist);
			}
			out.println("");
			out.print  ("Defaults restored.  Press enter to return to mutation sub menu...");
			in.readLine();
		} else {
			out.println("");
			out.print  ("Restore defaults canceled.  Press enter to return to mutation sub menu...");
			in.readLine();
		}
	}

	private static void main_menu_generation_configure_mutators_and_mutation_operators_help() throws IOException {
		out.println("");
		out.println("================================================================================");
		out.println("    >>Help");
		out.println("================================================================================");
		out.println("[1] List Mutation Operators.");
		out.println("    Summarizes the mutation operators currently registered with the experiment.");
		out.println("    Including their id, name, description, produced clone type, and executable");
		out.println("    location.");
		out.println("[2] Add Mutation Operator.");
		out.println("    Allows you to add your own custom mutation operator to the experiment.");
		out.println("[3] Remove Mutation Operator.");
		out.println("    Allows you to remove a mutation operator from the experiment.");
		out.println("[4] Remove All Mutation Operators.");
		out.println("    Removes all mutation operators registered with the experiment.");
		out.println("[5] List Mutators.");
		out.println("    Summarizes the mutators currently configured for the experiment.  Including");
		out.println("    their id, name, description, produced clone type, and operator list.");
		out.println("[6] Add Mutator.");
		out.println("    Allows you to add a mutator to the experiment.");
		out.println("[7] Remove Mutator.");
		out.println("    Allows you to remove an mutator from the experiment.");
		out.println("[8] Remove All Mutation Operators.");
		out.println("    Allows you to remove all mutators from the experiment.");
		out.println("[r] Restore Default Mutators and Mutation Operators.");
		out.println("    Restores the default set of mutation operators and mutators.");
		out.println("[h] Help.");
		out.println("    Shows this help dialogue.");
		out.println("[x] Return to Main Menu.");
		out.println("    Returns to the generation phase configuration main menu.");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to mutation sub menu...");
		in.readLine();
	}
	
//-------------------------------------------------------------------------------------------------
//---- Generation Phase ---------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
	
		
	public static boolean main_menu_generation_phase_stage() throws IOException {
		out.println("================================================================================");
		out.println("    Stage 2 of 5: Generation Phase Execution");
		out.println("================================================================================");
		out.println("Experiment: " + experiment.getDirectory());
		out.println("================================================================================");
		out.println("Loaded experiment is in generation phase execution stage.  This means that the");
		out.println("generation phase was previously interrupted.  This experiment is now currupt");
		out.println("and can not be resumed.  Please create a new experiment.");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to root menu...");
		in.readLine();
		return false;
	}
	
//-------------------------------------------------------------------------------------------------
//---- Evaluation Phase Setup ---------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
	
	public static boolean main_menu_evaluation_setup_stage() throws IOException, SQLException {
		String input;
		out.println("================================================================================");
		out.println("    Stage 3 of 5: Evaluation Phase Setup");
		out.println("================================================================================");
		out.println("Experiment: " + experiment.getDirectory());
		out.println("================================================================================");
		out.println("[1] List Subject Tools.");
		out.println("[2] Add Subject Tool.");
		out.println("[3] Remove Subject Tool.");
		out.println("[4] Remove All Subject Tools.");
		out.println("[5] Delete a subject tool's evaluation data.");
		out.println("");
		out.println("[6] Review Configurations.");
		out.println("[7] Set Subsume Tolerance.");
		out.println("[8] Set Unit Recall Required Similarity.");
		out.println("[9] Set Unit Precision Required Similarity.");
		out.println("");
		out.println("[s] Begin Evaluation Phase.");
		out.println("");
		out.println("[h] Help.");
		out.println("[x] Close Experiment.");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		input = in.readLine();
		switch(input) {
			case "1":
				main_menu_evaluation_setup_stage_list_subject_tools();
				break;
			case "2":
				main_menu_evaluation_setup_stage_add_subject_tool();
				break;
			case "3":
				main_menu_evaluation_setup_stage_remove_subject_tool();
				break;
			case "4":
				main_menu_evaluation_setup_stage_remove_all_subject_tools();
				break;
			case "5":
				main_menu_evaluation_setup_stage_delete_subject_tool_evaluation_data();
				break;
			case "6":
				main_menu_evaluation_setup_stage_review_configuration();
				break;
			case "7":
				main_menu_evaluation_setup_stage_set_subsume_tolerance();
				break;
			case "8":
				main_menu_evaluation_setup_stage_set_unit_recall_required_similarity();
				break;
			case "9":
				main_menu_evaluation_setup_stage_set_unit_precision_required_similarity();
				break;
			case "s":
				main_menu_evaluation_setup_stage_begin_evaluation_phase();
				break;
			case "x":
				return false;
			case "h":
				main_menu_evaluation_setup_stage_help();
				break;
			default:
				break;
		}
		return true;
	}
	
	private static void main_menu_evaluation_setup_stage_list_subject_tools() throws SQLException, IOException {
		out.println("");
		out.println("================================================================================");
		out.println("    >List Subject Tools");
		out.println("================================================================================");
		for(ToolDB t : experiment.getTools()) {
			out.println("[" + t.getId() + "]");
			out.println("              Name: " + t.getName());
			out.println("       Description: " + t.getDescription());
			out.println("       Install Dir: " + t.getDirectory());
			out.println("       Tool Runner: " + t.getToolRunner());
			if(experiment.isToolDetectionComplete(t.getId())) {
				out.println("  Detection Status: Complete");
			} else {
				out.println("  Detection Status: Incomplete");
			}
		}
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to root menu...");
		in.readLine();
	}

	private static void main_menu_evaluation_setup_stage_add_subject_tool() throws IOException, IllegalArgumentException, SQLException {	
		String name, description, sdir, srunner;
		Path dir, runner;
		
		out.println("");
		out.println("================================================================================");
		out.println("    >Add Subject Tool");
		out.println("================================================================================");
		
		//Name
		out.println("Enter a name for this tool (or blank to cancel).");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		name = in.readLine();
		
		if(name.equals("")) {
			out.println("");
			out.print  ("Tool addition canceled.  Press enter to return to main menu...");
			in.readLine();
			return;
		}
		
		//Description
		out.println("");
		out.println("Enter a description of this tool (or blank to cancel).");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		description = in.readLine();
		
		if(description.equals("")) {
			out.println("");
			out.print  ("Tool addition canceled.  Press enter to return to main menu...");
			in.readLine();
			return;
		}
		
		//Directory
		while(true) {
			out.println("");
			out.println("Specify the installation directory of this subject tool.  This is the tool");
			out.println("provided to the tool runner for locating the tool.  Please provide full path.");
			out.println("Leave blank to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			sdir = in.readLine();
			
			if(sdir.equals("")) {
				out.println("");
				out.print  ("Tool addition canceled.  Press enter to return to main menu...");
				in.readLine();
				return;
			}
			
			try {
				dir = Paths.get(sdir);
			} catch (InvalidPathException e) {
				out.println("");
				out.print  ("Invalid path.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(!Files.exists(dir)) {
				out.println("");
				out.print  ("Directory does not exist.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(!Files.isDirectory(dir)) {
				out.println("");
				out.print  ("Specified path is not a directory.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			break;
		}
		
		//Tool Runner
		while(true) {
			out.println("");
			out.println("Specify the tool runner executable for this subject tool.  Please provide full");
			out.println("path.  Leave blank to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			srunner = in.readLine();
			
			if(srunner.equals("")) {
				out.println("");
				out.print  ("Tool addition canceled.  Press enter to return to main menu...");
				in.readLine();
				return;
			}
			
			try {
				runner = Paths.get(srunner);
			} catch (InvalidPathException e) {
				out.println("");
				out.print  ("Invalid path.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(!Files.exists(runner)) {
				out.println("");
				out.print  ("Tool runner does not exist.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(!(Files.isRegularFile(runner) && Files.isExecutable(runner))) {
				out.println("");
				out.print  ("Specify path does not denote an executable file.  press enter to retry...");
				in.readLine();
				continue;
			}
			
			break;
		}
		
		//Add Tool
		ToolDB tool = experiment.addTool(name, description, dir, runner);
		out.println("");
		out.println("Subject tool added successfully.  Its id is " + tool.getId() + ".");
		out.print  ("Press enter to return to main menu...");
		in.readLine();
	}

	private static void main_menu_evaluation_setup_stage_remove_subject_tool() throws IOException, SQLException {
		String input;
		int id;
		out.println("");
		out.println("================================================================================");
		out.println("    >Remove Subject Tool");
		out.println("================================================================================");
		while(true) {
			out.println("Specify the id of the subject tool to remove.  The tool's evaluation data is");
			out.println("also removed, and can not be restored.  Leave blank to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.print  ("Remove subject tool canceled.  Press enter to return to main menu...");
				in.readLine();
				return;
			}
			
			try {
				id = Integer.parseInt(input);
			} catch (NumberFormatException e) {
				out.println("");
				out.println("Invalid identifier (integer).  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(experiment.removeTool(id)) {
				out.println("");
				out.print  ("Tool successfully removed.  Press enter to return to main menu...");
				in.readLine();
				return;
			} else {
				out.println("");
				out.println("A subject tool does not exist with this id.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
		}
	}

	private static void main_menu_evaluation_setup_stage_remove_all_subject_tools() throws IOException, SQLException {
		String input;
		int num;
		out.println("");
		out.println("================================================================================");
		out.println("    >Remove All Subject Tools");
		out.println("================================================================================");
		out.println("All subject tools, and their evaluation data, will be pernamently deleted.");
		out.println("Are you sure you wish to continue?  'y' for yes, or other to cancel.");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		input = in.readLine();
		
		if(input.equals("")) {
			out.println("");
			out.print  ("Remove all subject tools canceled.  Press enter to return to main menu...");
			in.readLine();
			return;
		}
		
		if(input.equals("y")) {
			num = experiment.removeTools();
			out.println("");
			out.print  (num + " tools have been removed.  Press enter to return to main menu.");
			in.readLine();
			return;
		} else {
			out.println("");
			out.print  ("Removal canceled.  Press enter to return to main menu.");
			in.readLine();
			return;
		}
	}

	private static void main_menu_evaluation_setup_stage_delete_subject_tool_evaluation_data() throws IOException, IllegalStateException, SQLException {
		String input;
		int id;
		out.println("");
		out.println("================================================================================");
		out.println("    >Delete Subject Tool Evaluation Data");
		out.println("================================================================================");
		while(true) {
			out.println("Specify the id of the subject tool to delete evaluation data for.  This includes");
			out.println("the deletion of its clone detection reports, unit recall and unit precision");
			out.println("data.  Provide blank reponse to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.print  ("Tool data deletion canceled.  Press enter to return to main menu.");
				in.readLine();
				return;
			} else {
				try {
					id = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("");
					out.println("Invalid identifier (integer).  Press enter to retry...");
					in.readLine();
					continue;
				}
				
				if(experiment.deleteToolEvaluationData(id)) {
					out.println("");
					out.print  ("Tool data successfully deleted.  Press enter to return to main menu...");
					in.readLine();
					return;
				} else {
					out.println("");
					out.println("A subject tool does not exist with this id.  Press enter to retry...");
					in.readLine();
					continue;
				}
			}			
		}
	}

	private static void main_menu_evaluation_setup_stage_review_configuration() throws SQLException, IOException {
		out.println("");
		out.println("================================================================================");
		out.println("    >Delete Subject Tool Evaluation Data");
		out.println("================================================================================");
		out.println("                   Subsume Tolerance: " + experiment.getSubsumeMatcherTolerance());
		out.println("     Unit Recall Required Similarity: " + experiment.getRecallRequiredSimilarity());
		out.println("  Unit Precision Required Similarity: " + experiment.getPrecisionRequiredSimilarity());
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to main menu.");
		in.readLine();
	}

	private static void main_menu_evaluation_setup_stage_set_subsume_tolerance() throws SQLException, IOException {
		String input;
		double tolerance;
		
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Subsume Tolerance");
		out.println("================================================================================");
		while(true) {
			out.println("Please specify a subsume tolerance.  Provide blank response to cancel.");
			out.println("");
			out.println("Note, changing this parameter will result in the deletion of any existing unit");
			out.println("recall and unit precision evaluations, as previous measures of these are");
			out.println("are invalidated by this change.");
			out.println("");
			out.println("The framework uses a subsume-based code fragment matching algorithm to determine");
			out.println("if a clone detected by a tool matches the injected clone (unit recall) or");
			out.println("contains one of the injected clone's fragments (unit precision). This matching");
			out.println("algorithm is parameterized by a tolerance, which provides flexibility to the");
			out.println("subsume criteria of the matcher.");
			out.println("");
			out.println("The tolerance considers a fragment a match (i.e., to subsume) of the target");
			out.println("fragment, even if it fails to contain the first or last few lines of the");
			out.println("target fragment.  The subsume tolerance is specified as a ratio of the size");
			out.println("of the code fragment.  For example, with a subsume tolerance of 0.20 and a");
			out.println("target fragment of 10 lines, the proposed code fragment is considered a");
			out.println("match by subsume even if it does not contain the first and last 2 lines of");
			out.println("the target.");
			out.println("");
			out.println("Valid range: [0.0,0.5]");
			out.println("");
			out.println("Default Value: 0.15");
			out.println("");
			out.println("Current Value: " + experiment.getSubsumeMatcherTolerance());
			out.println("");
			out.println("Advice:");
			out.println("    Best value is one that allows a couple lines to be missed on the start and");
			out.println("    end of the smallest possible target clone fragment.  Needs to be less than");
			out.println("    the mutation containment.");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.print  ("Set subsume tolerance canceled.  Press enter to return to main menu...");
				in.readLine();
				return;
			}
			
			try {
				tolerance = Double.parseDouble(input);
			} catch (NumberFormatException e) {
				out.println("");
				out.println("Not a valid double.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(tolerance < 0.0 || tolerance > 0.50) {
				out.println("");
				out.println("Value not in range.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			experiment.setSubsumeMatcherTolerance(tolerance);
			out.println("");
			out.print  ("Subsume tolerance set successfully.  Press enter to return to main menu...");
			in.readLine();
			return;
		}
	}

	private static void main_menu_evaluation_setup_stage_set_unit_recall_required_similarity() throws SQLException, IOException {
		String input;
		double ur_sim;
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Unit Recall Required Similarity");
		out.println("================================================================================");
		while(true) {
			out.println("Specify the unit recall required similarity.  Or leave blank to cancel.");
			out.println("");
			out.println("Note, changing this parameter will result in the deletion of any existing unit");
			out.println("recall evaluations, as previous measures of these are invalidated by this are");
			out.println("invalidated by this change.");
			out.println("");
			out.println("Adds the requirement that clones proposed by the subject tools must meet or ");
			out.println("exceed a minimum clone similarity to be accepted as the match of an injected");
			out.println("clone (in addition to satisfying the subsume-matcher).  This ensures that the");
			out.println("accepted clones are not false positives, and that they meet some minimum");
			out.println("detection 'quality'.");
			out.println("");
			out.println("Valid Range: [0.0, 1.0]");
			out.println("");
			out.println("Default Value: 0.50");
			out.println("");
			out.println("Current Value: " + experiment.getUnitRecallRequiredSimilarity());
			out.println("");
			out.println("Advice:");
			out.println("    A value from 0.50 to the minimum clone similarity of the refrence corpus is");
			out.println("    most appropraite.  A lower value is less likely to reject a matching true");
			out.println("    positive.  A higher value is more strict regarding the quality of a matching");
			out.println("    clone.  A value less than 0.50 is inappropraite, as clones should be at");
			out.println("    least half cloned code.  A value greater than the minimum clone similarity");
			out.println("    is inappropriate as it will prevent successful matches of generated clones");
			out.println("    from being accepted.  A value of 0.0 can be used to disable the similarity");
			out.println("    requirement.  This allows recall to be measured for the tools' ability to");
			out.println("    capture (subsume) the injected clones, regardless of the quality of the");
			out.println("    matching detected clone.");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.println("Set unit recall required similarity canceled.");
				out.print  ("Press enter to return to main menu...");
				in.readLine();
				return;
			}
			
			try {
				ur_sim = Double.parseDouble(input);
			} catch (NumberFormatException e) {
				out.println("");
				out.println("Not a valid double.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(ur_sim < 0 || ur_sim > 1.0) {
				out.println("");
				out.println("Value not in range.  Press enter to retry.");
				in.readLine();
				continue;
			}
			
			experiment.setRecallRequiredSimilarity(ur_sim);
			out.println("");
			out.println("Unit recall required similarity set successfully.");
			out.print  ("Press enter to return to main menu...");
			in.readLine();
			return;
		}
	}

	private static void main_menu_evaluation_setup_stage_set_unit_precision_required_similarity() throws SQLException, IOException {
		String input;
		double up_sim;
		out.println("");
		out.println("================================================================================");
		out.println("    >Set Unit Precision Required Similarity");
		out.println("================================================================================");
		while(true) {
			out.println("Specify the unit precision required similarity.  Or leave blank to cancel.");
			out.println("");
			out.println("Note, changing this parameter will result in the deletion of any existing unit");
			out.println("precision evaluations, as previous measures of these are invalidated by this");
			out.println("are invalidated by this change.");
			out.println("");
			out.println("Unit precision is calculated for a subject tool by validating the clones it");
			out.println("detected in a mutant system which contain one of the fragments of the");
			out.println("injected clone.  This parameter defines how similar these clones must be,");
			out.println("after nomalization, to be considered a true positive.  If their similarity");
			out.println("is less than this parameter, they are considered false positives.  Clone");
			out.println("similarity is measured both by token and by line.  Only one of these");
			out.println("measures must exceed this parameter to be accepted as a true positive.");
			out.println("");
			out.println("Valid Range: [0.0, 1.0]");
			out.println("");
			out.println("Default Value: 0.50");
			out.println("");
			out.println("Current Value: " + experiment.getPrecisionRequiredSimilarity());
			out.println("");
			out.println("Advice:");
			out.println("    A value of 0.50 to the minimum similarity of the clones of the corpus are");
			out.println("    appropraite values.  Choice depends on confidence in automatic validation");
			out.println("    and desired strictness of precision measurement.  A lower value has less");
			out.println("    risk of rejecting a true positive, while it has higher risk of accepting a");
			out.println("    false positive as a true positive.  This automatic precision measure is");
			out.println("    intended to compliment manual measurement of a tool's precision.");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.println("Set unit precision required similarity canceled.");
				out.print  ("Press enter to return to main menu...");
				in.readLine();
				return;
			}
			
			try {
				up_sim = Double.parseDouble(input);
			} catch (NumberFormatException e) {
				out.println("");
				out.println("Not a valid double.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(up_sim < 0.0 || up_sim > 1.0) {
				out.println("");
				out.println("Value not in range.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			experiment.setPrecisionRequiredSimilarity(up_sim);
			out.println("");
			out.println("Unit precision required similarity set successfully.");
			out.print  ("Press enter to return to main menu...");
			in.readLine();
			return;
		}
	}

	private static void main_menu_evaluation_setup_stage_begin_evaluation_phase() throws SQLException, IOException {
		boolean success;
		String input;
		if(!(experiment.numSubjectTools() > 0)) {
			out.println("");
			out.println("No subject tools have been defined.  Can not begine evaluation.");
			out.print  ("Press enter to return to main menu...");
			in.readLine();
			return;
		} else {
			out.println("");
			out.println("================================================================================");
			out.println("    >Begin Evaluation Phase");
			out.println("================================================================================");
			out.println("This begins the execution of the evaluation phase, during which the subject");
			out.println("tools are evaluated for the reference tool.  Depending on the number of subject");
			out.println("tools, the number of mutant systems, and the execution time of the subject ");
			out.println("tools, this may take a significant amount of time.  The framework is not");
			out.println("interactive during this process, and the experiment can not be closed until its ");
			out.println("completion.");
			out.println("It is *probably* safe to kill the framework process during the evaluation phase,");
			out.println("and resume it later.  The experiment will resume during the evaluation phase");
			out.println("setup stage.  You may need to manually delete the database lock file in the");
			out.println("experiment directory to load the experiment in this case.  The experiment data");
			out.println("should be uncurrupt, but this is not guarenteed, so it is best to avoid it.");
			out.println("");
			out.println("Are you sure you are ready to proceed?  'y' for yes, or other input to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			
			input = in.readLine();
			if(!input.equals("y")) {
				out.println("");
				out.print  ("Begin evaluation phase canceled.  Press enter to return to main menu.");
				in.readLine();
				return;
			}
			
			out.println("");
			out.println("********************************************************************************");
			out.println("");
			out.println("================================================================================");
			out.println("    Stage 4 of 5: Evaluation Phase Execution");
			out.println("================================================================================");
			out.println("Experiment: " + experiment.getDirectory());
			out.println("================================================================================");
			out.println("Execution of the evaluation phase has begun.  Please see progress log:");
			out.println("--------------------------------------------------------------------------------");
			success = experiment.evaluateTools();
			if(success) {
				out.println("--------------------------------------------------------------------------------");
				out.print  ("Evaluation phase completed successfully.  Press enter to return to main menu...");
				in.readLine();
				return;
			} else {
				out.println("--------------------------------------------------------------------------------");
				out.println("Evaluation phase errored.  Please see execution log for details.  Likely this");
				out.println("due to a tool runner not behaving correctly.  Press enter to return to main");
				out.print  ("menu, where you can close the experiment and fix the tool runner.");
				in.readLine();
				return;
			}	
		}	
	}

	private static void main_menu_evaluation_setup_stage_help() throws IOException {
		out.println("");
		out.println("================================================================================");
		out.println("    >Help");
		out.println("================================================================================");
		out.println("[1] List Subject Tools.");
		out.println("    Summarizes the subject tools currently registered to the experiment.");
		out.println("[2] Add Subject Tool.");
		out.println("    Allows you to add a subject tool to the experiment.");
		out.println("[3] Remove Subject Tool.");
		out.println("    Allows you to remove a subject tool from the experiment.");
		out.println("[4] Remove All Subject Tools.");
		out.println("    Allows you to remove all of the subject tools from the experiment.");
		out.println("[5] Delete a subject tool's evaluation data.");
		out.println("    Deletes the clone detection reports, unit recall and unit precision data for");
		out.println("    the specified tool.  This allows you to force the re-evaluation of a tool");
		out.println("    for the generated corpus.  For example, if you changed the tool's tool");
		out.println("    runner to configure the tool differently.");
		out.println("[6] Review Configurations.");
		out.println("    Summarizes the evaluation phase configuration options and their current");
		out.println("    value.");
		out.println("[7] Set Subsume Tolerance.");
		out.println("    Describes the subsume tolerance option, and allows you to set it for the");
		out.println("    experiment.");
		out.println("[8] Set Unit Recall Required Similarity.");
		out.println("    Describes the unit recall required similarity option, and allows you to set");
		out.println("    it for the experiment.");
		out.println("[9] Set Unit Precision Required Similarity.");
		out.println("    Describes the unit precision required similarity option, and allows you to");
		out.println("    set it for the experiment.");
		out.println("[s] Begin Evaluation Phase.");
		out.println("    Begins the execution of the evaluation phase using the subject tools and");
		out.println("    settings configured during this stage.");
		out.println("[h] Help.");
		out.println("    Shows this help dialogue.");
		out.println("[x] Close Experiment.");
		out.println("    Closes the experiment, and returns to the root menu.");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to main menu.");
		in.readLine();
	}
	
//-------------------------------------------------------------------------------------------------
//---- Evaluation Phase ---------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------

	public static boolean main_menu_evaluation_phase_stage() throws IOException, SQLException {
		out.println("================================================================================");
		out.println("    Stage 4 of 5: Evaluation Phase Execution");
		out.println("================================================================================");
		out.println("Experiment: " + experiment.getDirectory());
		out.println("================================================================================");
		out.println("Loaded experiment is in evaluation phase execution stage.  This means that the");
		out.println("evaluation phase was previously interrupted.  The experiment will now return to");
		out.println("the evaluation setup stage.  Evaluation done before interruption will be kept.");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to main menu...");
		in.readLine();
		experiment.previousStage();
		return true;
	}
	
//-------------------------------------------------------------------------------------------------
//---- Results ------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
	
	public static boolean main_menu_results_stage() throws IOException, SQLException {
		out.println("================================================================================");
		out.println("    Stage 5 of 5: Results");
		out.println("================================================================================");
		out.println("Experiment: " + experiment.getDirectory());
		out.println("================================================================================");
		out.println("[1] View Performance Results.");
		out.println("[2] Generate Full Evaluation Report.");
		out.println("[3] Return to Evaluation Configuration Stage.");
		out.println("");
		out.println("[h] Help.");
		out.println("[x] Close Experiment.");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		String input = in.readLine();
		switch(input) {
			case "1":
				main_menu_results_view_performance_results();
				break;
			case "2":
				main_menu_results_stage_generate_full_evaluation_report();
				break;
			case "3":
				main_menu_results_stage_return_to_evaluation_configuration_stage();
				break;
			case "h":
				main_menu_results_stage_help();
				break;
			case "x":
				return false;
			case "t":
				experiment.outputResults();
				break;
			default:
				out.println("");
				out.println("Invalid Selection.  Press enter to return to main menu...");
				in.readLine();
				break;
		}
		
		return true;
	}
	
	private static void main_menu_results_view_performance_results() throws SQLException, IOException {
		DecimalFormat df = new DecimalFormat("0.0000");
		out.println("");
		out.println("================================================================================");
		out.println("    >View Performance Results");
		out.println("================================================================================");
		for(ToolDB tool : experiment.getTools()) {
			double type1r = experiment.getRecallForCloneType(tool.getId(), 1);
			double type2r = experiment.getRecallForCloneType(tool.getId(), 2);
			double type3r = experiment.getRecallForCloneType(tool.getId(), 3);
			double type1p = experiment.getPrecisionForCloneType(tool.getId(), 1);
			double type2p = experiment.getPrecisionForCloneType(tool.getId(), 2);
			double type3p = experiment.getPrecisionForCloneType(tool.getId(), 3);
			
			out.println("[" + tool.getId() + "] - " + tool.getName());
			out.println("  Per Type:");
			if(type1r >= 0) { //Skip if none
			out.println("    1 - Recall: " + df.format(type1r) + ", Precision: " + df.format(type1p));
			}
			if(type2r >= 0) { //Skip if none
			out.println("    2 - Recall: " + df.format(type2r) + ", Precision: " + df.format(type2p));
			}
			if(type3r >= 0) { //Skip if none
			out.println("    3 - Recall: " + df.format(type3r) + ", Precision: " + df.format(type3p));
			}
			out.println("  Per Mutator:");
			for(MutatorDB mutator : experiment.getMutators()) {
				double recall = experiment.getRecallForMutator(tool.getId(), mutator.getId());
				double precision = experiment.getPrecisionForMutator(tool.getId(), mutator.getId());
				out.println("    " + mutator.getId() + " - Recall: " + df.format(recall) + ", Precision: " + df.format(precision));
			}
			out.println("  Per Mutation Operator:");
			for(OperatorDB operator : experiment.getOperators()) {
				double recall = experiment.getRecallForOperator(tool.getId(), operator.getId());
				double precision = experiment.getPrecisionForOperator(tool.getId(), operator.getId());
				out.println("    " + operator.getId() + " - Recall: " + df.format(recall) + ", Precision: " + df.format(precision));
			}
		}
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to main menu...");
		in.readLine();
	}

	private static void main_menu_results_stage_generate_full_evaluation_report() throws IOException, SQLException  {
		String input;
		Path file;
		out.println("");
		out.println("================================================================================");
		out.println("    >Generate Full Evaluation Report");
		out.println("================================================================================");
		while(true) {
			out.println("Specify path to a file to write the full evaluation report to.  File should not");
			out.println("already exist.  Leave blank to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print  (":::: ");
			input = in.readLine();
			
			if(input.equals("")) {
				out.println("");
				out.print  ("Generate full evaluation report canceled.  Press enter to return to main menu...");
				in.readLine();
				return;
			}
			
			try {
				file = Paths.get(input);
			} catch (InvalidPathException e) {
				out.println("");
				out.println("Invalid path.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			if(Files.exists(file)) {
				out.println("");
				out.print  ("File already exists.  Press enter to retry...");
				in.readLine();
				continue;
			}
			
			//Test Create File and Writable, delete after
			try {
				Files.createFile(file);
			} catch (IOException e) {
				out.println("");
				out.print  ("Not possible to create file.  Press enter to retry...");
				in.readLine();
				continue;
			}
			if(!Files.isWritable(file)) {
				out.println("");
				out.print  ("Specified file not writable.  Press enter to retry...");
				try {Files.deleteIfExists(file);} catch (IOException e) {}
				in.readLine();
				continue;
			}
			try {Files.deleteIfExists(file);} catch (IOException e) {}
			
			//Create Report
			try {
				experiment.generateReport(file);
			} catch (IOException e) {
				out.println("");
				out.print  ("Report generation failed (write error).  Press enter to retry...");
				try {Files.deleteIfExists(file);} catch (IOException ee) {}
				in.readLine();
				continue;
			}
			
			//End
			out.println("");
			out.print("Report generation complete.  Press enter to return to main menu...");
			in.readLine();
			return;
		}
	}

	public static void main_menu_results_stage_return_to_evaluation_configuration_stage() throws IOException, SQLException {
		out.println("");
		out.println("================================================================================");
		out.println("    >Return to Evaluation Configuration Stage");
		out.println("================================================================================");
		out.println("Upon returning to the evaluation configuration stage, the evaluation phase will");
		out.println("will need to be re-executed in order to return this the results stage.  However,");
		out.println("re-execution will re-use any previous evaluation data not invalidated by changes");
		out.println("to the evaluation phase configuration.  It is reccomended that you make a copy");
		out.println("of the experiment before proceeding so that previous results are not lost.");
		out.println("");
		out.println("Are you sure you wish to proceed? Input 'y' to proceed or other input to cancel.");
		out.println("--------------------------------------------------------------------------------");
		out.print  (":::: ");
		String input = in.readLine();
		switch(input) {
			case "y":
				experiment.previousStage();
				out.println("");
				out.println("The experiment has been returned to the evaluation configuration stage.");
				out.print  ("Press enter to return to main menu...");
				in.readLine();
				break;
			default:
				out.println("");
				out.println("Return to the evaluation setup phase has been canceled.");
				out.print  ("Press enter to return to main menu...");
				in.readLine();
				break;
		}
	}
	
	public static void main_menu_results_stage_help() throws IOException {
		out.println("");
		out.println("================================================================================");
		out.println("    >Help");
		out.println("================================================================================");
		out.println("[1] View Performance Results.");
		out.println("    Outputs the subject tool's evaluation performances to the screen.");
		out.println("[2] Generate Full Evaluation Report.");
		out.println("    Creates an evaluation report file at a location specified by you.");
		out.println("    Report summarizes the properties of the reference corpus, the subject tools");
		out.println("    and their evaluated performances.");
		out.println("[3] Return to Evaluation Configuration Stage.");
		out.println("    Returns the experiment to the evaluation configuration stage.  This allows");
		out.println("    the evaluation phase to be efficiently re-configured and re-executed.  This");
		out.println("    includes the addition, removal, or modification of the subject tools, and");
		out.println("    changes to the evaluation options.");
		out.println("[h] Help.");
		out.println("    Shows this help ");
		out.println("[x] Close Experiment.");
		out.println("    Closes the experiment.");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to main menu...");
		in.readLine();
	}
	
//-------------------------------------------------------------------------------------------------
//---- Error --------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
	
	//Menu displayed if experiment is in the error stage.
	public static boolean main_menu_error_stage() throws IOException, SQLException {
		out.println("================================================================================");
		out.println("    Error Stage");
		out.println("================================================================================");
		out.println("Experiment: " + experiment.getDirectory());
		out.println("================================================================================");
		out.println("Loaded experiment previously crashed with an error.  Please refer to the ");
		out.println("experiment's log for details.  This experiment can not be continued and will now");
		out.println("be closed.");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to root menu...");
		in.readLine();
		return false;
	}
	
//-------------------------------------------------------------------------------------------------
//---- Invalid ------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------------------
	
	//Menu displayed if experiment is in an invalid (unknown) stage.
	public static boolean main_menu_invalid_stage() throws IOException {
		out.println("================================================================================");
		out.println("    Stage ? of 5");
		out.println("================================================================================");
		out.println("Experiment: " + experiment.getDirectory());
		out.println("================================================================================");
		out.println("The experiment appears to be in an invalid stage.  Bug?  Sorry, but experiment");
		out.println("must now close.");
		out.println("--------------------------------------------------------------------------------");
		out.print  ("Press enter to return to root menu...");
		in.readLine();
		return false;
	}
}