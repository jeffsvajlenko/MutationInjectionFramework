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
import java.util.LinkedList;
import java.util.List;

import models.InvalidToolRunnerException;
import experiment.Experiment;
import experiment.ExperimentData;
import experiment.ExperimentSpecification;
import experiment.MutatorDB;
import experiment.OperatorDB;

//TODO: Make operator menu.  Make mutator menu.  Make tool menu.  Make setup 

public class Interface {
	
	private static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private static PrintStream out = System.out;
	private static Experiment experiment = null;
	
	public static void main(String args[]) throws IOException, IllegalStateException, SQLException, InterruptedException, ArtisticStyleFailedException, IllegalArgumentException, FileSanetizationFailedException, NullPointerException, InvalidToolRunnerException {
		printBanner();
		
		initialize_experiment();
		if(experiment == null) {
			return;
		}
		
		loaded_menu();
	}
	
	private static void printBanner() {
		out.println("--------------------------------------------------------------------------------");
		out.println("--------------------------Mutation Injection Framework--------------------------");
		out.println("--------------------------------------------------------------------------------");
	}
	
	private static void loaded_menu() throws SQLException, IOException {
		while(true) {
			
			if(experiment.getGenerationType() == ExperimentSpecification.AUTOMATIC_GENERATION_TYPE) {
				// Evaluation Setup Stage
				if(experiment.currentPhase() == Experiment.GENERATION_SETUP_STAGE) {
					out.println("");
					out.println("");
					out.println("");
					out.println("");
					out.println("--------------------------------------------------------------------------------");
					out.println("    MAIN MENU: Generation Setup Stage");
					out.println("--------------------------------------------------------------------------------");
					out.println("  Experiment: " + experiment.getLocation());
					out.println("--------------------------------------------------------------------------------");
					out.println("    [1]: Setup Operators/Mutators");
					out.println("    [2]: Begin Generation");
					out.println("    [3]: Review Settings");
					out.println("    [4]: Exit");
					out.println("--------------------------------------------------------------------------------");
					out.println(":::: ");
					String input = in.readLine();
					
					//Setup Operators/Mutators
					if(input.equals("1")) {
						operator_mutator_menu();
					//Begin Generation
					} else if (input.equals("2")) {
						if(experiment.numMutators() < 1) {
							out.println("\t\tERROR: No mutators specified.  Can not generate.");
							out.println("\t\tPress enter to continue...");
							in.readLine();
						} else {
							boolean bretval = experiment.generate();
							if(bretval) {
								out.println("\t\tGeneration successfully completed.");
								out.println("\t\tPress enter to continue...");
								in.readLine();
							} else {
								out.println("\t\tGeneration failed.");
								out.println("\t\tPress enter to continue...");
								in.readLine();
							}
						}
					} else if (input.equals("3")) {
						out.println("");
						out.println("-Generation Properties----------------------------------------------------------");
						out.println("    GenerationType: Automatic");
						out.println("    FragmentType: " + ExperimentSpecification.fragmentTypeToString(experiment.getFragmentType()));
						out.println("    MaxFragments: " + experiment.getMaxFragments());
						out.println("    MinSizeLines: " + experiment.getFragmentMinimumSizeLines());
						out.println("    MaxSizeLines: " + experiment.getFragmentMaximumSizeLines());
						out.println("    MinSizeTokens: " + experiment.getFragmentMinimumSizeTokens());
						out.println("    MaxSizeTokens: " + experiment.getFragmentMaximumSizeTokens());
						out.println("    InjectionNumber: " + experiment.getInjectionNumber());
						out.println("    AllowedFragmentDifference: " + experiment.getAllowedFragmentDifference());
						out.println("    MutationContainment: " + experiment.getMutationContainment());
						out.println("    MutatorAttempts: " + experiment.getMutationAttempts());
						out.println("    OperatorAttempts: " + experiment.getOperatorAttempts());
						out.println("--------------------------------------------------------------------------------");
						out.println("Press enter to continue...");
						in.readLine();
					} else if (input.equals("4")) {
						return;
					} else {
						System.out.println("\t\tERROR: Invalid option.");
						System.out.println("\t\tPress enter to continue...");
						in.readLine();
						continue;
					}
				// Evaluation Stage
				} else if (experiment.getGenerationType() == Experiment.GENERATION_STAGE) {
					//TODO
					return;
				} else if (experiment.getGenerationType() == Experiment.EVALUATION_SETUP_STAGE) {
					//TODO
					return;
				} else if (experiment.getGenerationType() == Experiment.EVALUATION_STAGE) {
					//TODO
					return;
				} else if (experiment.getGenerationType() == Experiment.RESULTS_STAGE) {
					//TODO
					return;
				} else if (experiment.getGenerationType() == Experiment.ERROR_STAGE) {
					//TODO
					return;
				}
			}
		}
	}
	
	private static void operator_mutator_menu() throws IOException, SQLException {
main:	while(true) {
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("--------------------------------------------------------------------------------");
			out.println("    Operator/Mutator Menu");
			out.println("--------------------------------------------------------------------------------");
			out.println("    [1]: List Operators");
			out.println("    [2]: Add Operator");
			out.println("    [3]: Remove Operator");
			out.println("    [4]: List Mutators");
			out.println("    [5]: Add Mutator");
			out.println("    [6]: Remove Mutator");
			out.println("    [7]: Load Default Operators and Mutators");
			out.println("    [8]: Exit");
			out.println("--------------------------------------------------------------------------------");
			out.println(":::: ");
			String input = in.readLine();
			
			//List Operators
			if (input.equals("1")) {
				out.println("");
				out.println("-Operators----------------------------------------------------------------------");
				for (OperatorDB operator : experiment.getOperators()) {
					out.println(operator.getId());
					out.println("           Name: " + operator.getName());
					out.println("    Description: " + operator.getDescription());
					out.println("     Clone Type: " + operator.getTargetCloneType());
					out.println("     Executable: " + operator.getMutator());
				}
				out.println("--------------------------------------------------------------------------------");
				out.println("Press enter to continue...");
				in.readLine();
				continue main;
				
			//Add Operator
			} else if (input.equals("2")) {
				String name, description, clonetype, sexecutable;
				Path executable;
				int iclonetype;
				
				//Get Operator Name
				out.println("");
				out.println("Enter a name for the operator (or blank to cancel).");
				out.println("--------------------------------------------------------------------------------");
				out.println(":::: ");
				name = in.readLine();
				if(name.equals("")) {
					out.println("\t\tCanceling operator addition.");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				}
				
				//Get Operator Description
				out.println("");
				out.println("Enter a description of the operator (or blank to cancel).");
				out.println("--------------------------------------------------------------------------------");
				out.println(":::: ");
				description = in.readLine();
				if(description.equals("")) {
					out.println("\t\tCanceling operator addition.");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				}
				
				//Get clone type
				while(true) {
					out.println("");
					out.println("Enter expected clone type of operator (1, 2 or 3; or blank to cancel).");
					out.println("--------------------------------------------------------------------------------");
					out.println(":::: ");
					clonetype = in.readLine();
					if(clonetype.equals("")) {
						out.println("\t\tCanceling operator addition.");
						out.println("\t\tPress enter to continue.");
						in.readLine();
						continue main;
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
						out.println("\t\tInvalid clone type");
						out.println("\t\tPress enter to continue.");
						in.readLine();
						continue;
					}
				}
				
				//Get executable
				while(true) {
					out.println("");
					out.println("Enter full path to operator executable (or blank to cancel).");
					out.println("--------------------------------------------------------------------------------");
					out.println(":::: ");
					sexecutable = in.readLine();
					
					if(sexecutable.equals("")) {
						out.println("\t\tCanceling operator addition.");
						out.println("\t\tPress enter to continue.");
						in.readLine();
						continue main;
					}
					
					try {
						executable = Paths.get(sexecutable).toAbsolutePath().normalize();
					} catch (InvalidPathException e) {
						out.println("\t\tERROR: Not a legal path.");
						out.println("\t\tPress enter to continue...");
						in.readLine();
						continue;
					}
					
					if(!Files.isRegularFile(executable) || !Files.isExecutable(executable)) {
						out.println("\t\tERROR: Not an executable regular file.");
						out.println("\t\tPress enter to continue...");
						in.readLine();
						continue;
					}
					break;
				}
				
				OperatorDB op = experiment.addOperator(name, description, iclonetype, executable);
				out.println("\t\tOperator successfully added.  It's id is: " + op.getId());
				out.println("\t\tPress enter to continue...");
				in.readLine();
				continue main;
				
			//Remove Operator
			} else if (input.equals("3")) {
				out.println("");
				out.println("Enter the id (integer) of the operator to remove.");
				out.println("--------------------------------------------------------------------------------");
				out.println(":::: ");
				input = in.readLine();
				int id;
				try {
					id = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid id (not an integer).");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				}
				boolean bretval = experiment.removeOperator(id);
				if(bretval) {
					out.println("\t\tOperator " + id + " successfully removed.");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				} else {
					out.println("\t\tOperator " + id + " does not exist.");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				}
				
			//List Mutators
			} else if (input.equals("4")) {
				out.println("");
				out.println("-Mutators-----------------------------------------------------------------------");
				for (MutatorDB mutator : experiment.getMutators()) {
					out.println(mutator.getId());
					out.println("    Description: " + mutator.getDescription());
					out.println("     Clone Type: " + mutator.getTargetCloneType());
					out.print  ("      Operators: ");
						for(OperatorDB operator : mutator.getOperators()) {
							out.print(operator.getId() + " ");
						}
						out.println("");
				}
				out.println("--------------------------------------------------------------------------------");
				out.println("Press enter to continue...");
				in.readLine();
				continue main;
				
			//Add Mutator
			} else if (input.equals("5")) {
				if(experiment.numOperators() > 0) {
					String description;
					int id;
					List<Integer> operators = new LinkedList<Integer>();
					
					out.println("");
					out.println("Enter description of mutator (leave blank to cancel).");
					out.println("--------------------------------------------------------------------------------");
					out.println(":::: ");
					description = in.readLine();
					if(description.equals("")) {
						out.println("\t\tCanceling mutator addition.");
						out.println("\t\tPress enter to continue.");
						in.readLine();
						continue main;
					}
					
					while(true) {
						out.println("");
						out.println("Enter id of operator #1 (leave blank to cancel).");
						out.println("--------------------------------------------------------------------------------");
						out.println(":::: ");
						input = in.readLine();
						if(input.equals("")) {
							out.println("\t\tCanceling mutator addition.");
							out.println("\t\tPress enter to continue.");
							in.readLine();
							continue main;
						}
						try {
							id = Integer.parseInt(input);
						} catch (NumberFormatException e) {
							out.println("\t\tERROR: Not a valid id (integer).");
							out.println("\t\tPress enter to continue.");
							in.readLine();
							continue;
						}
						if(experiment.existsOperator(id)) {
							operators.add(id);
							break;
						} else {
							out.println("\t\tERROR: Operator with id " + id + " does not exist.");
							continue;
						}
					}
					
					while(true) {
						out.println("");
						out.println("Enter id of operator#" + (operators.size() + 1) + ".");
						out.println("Enter d to finish operators, or blank to cancel.");
						out.println("--------------------------------------------------------------------------------");
						out.println(":::: ");
						input = in.readLine();
						
						//Cancel
						if(input.equals("")) {
							out.println("\t\tCanceling mutator addition.");
							out.println("\t\tPress enter to continue.");
							in.readLine();
							continue main;
						}
						
						//End
						if(input.equals("d")) {
							break;
						}
						
						//Parse
						try {
							id = Integer.parseInt(input);
						} catch (NumberFormatException e) {
							out.println("\t\tERROR: Not a valid id (integer).");
							out.println("\t\tPress enter to continue.");
							in.readLine();
							continue;
						}
						
						//If exists add, else retry
						if(experiment.existsOperator(id)) {
							operators.add(id);
							continue;
						} else {
							out.println("\t\tERROR: Operator with id " + id + " does not exist.");
							out.println("\t\tPress enter to continue.");
							in.readLine();
							continue;
						}
					}
					
					//Add operator
					experiment.addMutator(description, operators);
				} else {
					System.out.println("\t\tERROR: No operators defined.  Can not create a mutator.");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				}
				
			// Remove Mutator
			} else if (input.equals("6")) {
				out.println("");
				out.println("Enter the id (integer) of the mutator to remove.");
				out.println("--------------------------------------------------------------------------------");
				out.println(":::: ");
				input = in.readLine();
				int id;
				try {
					id = Integer.parseInt(input);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid id (not an integer).");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				}
				boolean bretval = experiment.removeMutator(id);
				if(bretval) {
					out.println("\t\tMutator " + id + " successfully removed.");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				} else {
					out.println("\t\tMutator " + id + " does not exist.");
					out.println("\t\tPress enter to continue.");
					in.readLine();
					continue main;
				}
			} else if (input.equals("7")) {
				OperatorDB o;
				List<OperatorDB> operators = new LinkedList<OperatorDB>();
				o=experiment.addOperator("id", "Change in comments: comment added between two tokens..", 1, Paths.get("operators/mCC_BT")); operators.add(o);
				o=experiment.addOperator("id", "Change in comments: comment added at end of a line.", 1, Paths.get("operators/mCC_EOL")); operators.add(o);
				o=experiment.addOperator("id", "Change in formatting: a newline is added between two tokens..", 1, Paths.get("operators/mCF_A")); operators.add(o);
				o=experiment.addOperator("id", "Change in formatting: a newline is removed (without changing meangin).", 1, Paths.get("operators/mCF_R")); operators.add(o);
				o=experiment.addOperator("id", "Change in whitespace: a space or tab is added between two tokens.", 1, Paths.get("operators/mCW_A")); operators.add(o);
				o=experiment.addOperator("id", "Change in whitespace: a space or tab is removed (without changing meaning).", 1, Paths.get("operators/mCW_R")); operators.add(o);
				o=experiment.addOperator("id", "Renaming of identifier: systamtic renaming of all instances of a chosen identifier.", 2, Paths.get("operators/mSRI")); operators.add(o);
				o=experiment.addOperator("id", "Renaming of identifier: arbitrary renaming of a single identifier instance.", 2, Paths.get("operators/mARI")); operators.add(o);
				o=experiment.addOperator("id", "Change in literal value: a number value is replaced.", 2, Paths.get("operators/mRL_N")); operators.add(o);
				o=experiment.addOperator("id", "Change in literal value: a string value is replaced.", 2, Paths.get("operators/mRL_S")); operators.add(o); //slow
				o=experiment.addOperator("id", "Deletion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mDL")); operators.add(o);
				o=experiment.addOperator("id", "Insertion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mIL")); operators.add(o);
				o=experiment.addOperator("id", "Modification of a whole line ((without invalidating syntax).", 3, Paths.get("operators/mML")); operators.add(o);
				o=experiment.addOperator("id", "Small deletion within a line (removal of a parameter).", 3, Paths.get("operators/mSDL")); operators.add(o);
				o=experiment.addOperator("id", "Small insertion within a line (addition of a parameter).", 3, Paths.get("operators/mSIL")); operators.add(o);
				for(OperatorDB operator : operators) {
					LinkedList<Integer> oplist = new LinkedList<Integer>();
					oplist.add(operator.getId());
					experiment.addMutator(operator.getDescription(), oplist);
				}
				out.println("\t\tDefault's added successfully.");
				out.println("\t\tPress enter to continue");
				in.readLine();
				continue;
			} else if (input.equals("8")) {
				return;
			}
		}
	}
	
	/**
	 * Initialize Experiment.
	 * @param in InputReader, source of interface input.
	 * @param out PrintStream, stream to export interface output.
	 * @return An initialized experiment, or null if the process was canceled (quit).
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	private static void initialize_experiment() throws IOException, IllegalArgumentException, SQLException {
		String input_line;
		
		while(true) {
			//Print Menu
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("--------------------------------------------------------------------------------");
			out.println("-Initialize Experiment----------------------------------------------------------");
			out.println("--------------------------------------------------------------------------------");
			out.println("");
			out.println("Would you like to create a new experiment, or load an existing?");
			out.println("    [1]:  Create New Experiment");
			out.println("    [2]:  Load Existing Experiment");
			out.println("    [3]:  Quit");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			
			//Get User Option
			input_line = in.readLine();
			
			//If Menu Option Invalid, Re-Request
			if (input_line.equals("1")) {
				experiment = create_new_experiment_menu();
				if(experiment == null) {
					continue;
				} else {
					break;
				}
			} else if (input_line.equals("2")) {
				experiment = load_experiment();
				if(experiment == null) {
					continue;
				} else {
					break;
				}
			} else if (input_line.equals("3")) {
				experiment = null;
				break;
			} else {
				System.out.println("\t\tERROR: Selection invalid.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				continue;
			}
		}
	}
	
	/**
	 * Interface for creating a new experiment, not including generation.
	 * @param in Input reader for interface.
	 * @param out Output stream for interface.
	 * @return An experiment with the setup and generation phase complete.
	 * @throws IOException
	 */
	private static Experiment create_new_experiment_menu() throws IOException {
		String inline;
		int inint;
		double indouble;
		
		Path datadir;
		Path systemdir;
		Path repositorydir;
		
		int language;
		
		int generationType;
		int fragmentType;
		int minSizeLines;
		int maxSizeLines;
		int minSizeTokens;
		int maxSizeTokens;
		int injectionNumber;
		double allowedFragmentDifference;
		double mutationContainment;
		int mutatorAttempts;
		int operatorAttempts;
		int maxFragments;
		
		out.println("");
		out.println("");
		out.println("");
		out.println("");
		out.println("--------------------------------------------------------------------------------");
		out.println("-Create New Experiment----------------------------------------------------------");
		out.println("--------------------------------------------------------------------------------");
		out.println("Experiment properties will now be requested from you.  You may cancel this");
		out.println("process at any time by providing an empty response to any of the options.  This");
		out.println("will return you directly to the experiment initialization menu.");
		out.println("--------------------------------------------------------------------------------");
		
		while(true) {
			out.println("");
			out.println("Specify experiment language (language of subject system and repository).");
			out.println("    [1] - Java");
			out.println("    [2] - C");
			out.println("    [3] - CS");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			inline = in.readLine();
			if(inline.equals("")) {
				out.println("Canceling new experiment creation. Press enter to continue.");
				out.println("--------------------------------------------------------------------------------");
				in.readLine();
				return null;
			} else if (inline.equals("1")) {
				language = ExperimentSpecification.JAVA_LANGUAGE;
			} else if (inline.equals("2")) {
				language = ExperimentSpecification.C_LANGUAGE;
			} else if (inline.equals("3")) {
				language = ExperimentSpecification.CS_LANGUAGE;
			} else {
				System.out.println("\t\tERROR: Invalid selection.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				continue;
			}
			break;
		}
		
		while(true) {
			out.println("");
			out.println("Specify full path to a directory in which to store this experiment.  Directory");
			out.println("must not already exist (it will be created).");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			inline = in.readLine();
			if(inline.equals("")) {
				out.println("Canceling new experiment creation. Press enter to continue.");
				out.println("--------------------------------------------------------------------------------");
				in.readLine();
				return null;
			}
			try {
				datadir = Paths.get(inline).toAbsolutePath().normalize();
			} catch (InvalidPathException e) {
				out.println("\t\tERROR: Path specified is invalid.  Please specify another.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				continue;
			}
			if(Files.exists(datadir)) {
				out.println("\t\tERROR: Directory specified is already a file or directory.  Please specify another location.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				continue;
			}
			try {
				Files.createDirectories(datadir);
			} catch (Exception e) {
				out.println("\t\tERROR: Unable to create the experiment directory.  Please specify another location.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				continue;
			}
			Files.deleteIfExists(datadir);
			break;
		}
		
		while(true) {
			out.println("");
			out.println("Specify full path of directory of the desired subject system.  Subject system");
			out.println("must be of the language specified earlier.");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			inline = in.readLine();
			if(inline.equals("")) {
				out.println("Canceling new experiment creation. Press enter to continue.");
				out.println("--------------------------------------------------------------------------------");
				in.readLine();
				return null;
			}
			try {
				systemdir = Paths.get(inline).toAbsolutePath().normalize();
			} catch (InvalidPathException e) {
				out.println("\t\tERROR: Path specified is invalid.  Please specify another.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				continue;
			}
			if(!Files.isDirectory(systemdir)) {
				out.println("\t\tERROR: Path does not specify an existing directory.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				continue;
			}
			break;
		}
		
		//Check Generation Type
		while(true) {
			out.println("");
			out.println("Specify generation type.:");
			out.println("    [1] - Automatic.  Clones will be artifically generated by extracting");
			out.println("                      fragements from the repository and mutating them.");
			out.println("    [2] - Manual.  Manually (user) created clones will be imported.");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			inline = in.readLine();
			if(inline.equals("")) {
				out.println("Canceling new experiment creation. Press enter to continue.");
				out.println("--------------------------------------------------------------------------------");
				in.readLine();
				return null;
			} else if(inline.equals("1")) {
				generationType = ExperimentSpecification.AUTOMATIC_GENERATION_TYPE;
				break;
			} else if(inline.equals("2")) {
				generationType = ExperimentSpecification.MANUAL_GENERATION_TYPE;
				break;
			} else {
				out.println("\t\tERROR: Invalid option.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				continue;
			}
		}
		
		// Generation Type is Automatic
		if(generationType == ExperimentSpecification.AUTOMATIC_GENERATION_TYPE) {
			//Repository
			while(true) {
				out.println("");
				out.println("Specify full path of repository. This is the directory of source code that the");
				out.println("source fragments will be selected.  Should be of the language specified earlier.");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				try {
					repositorydir = Paths.get(inline).toAbsolutePath().normalize();
				} catch (InvalidPathException e) {
					out.println("\t\tERROR: Path specified is invalid.  Please specify another.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				if(!Files.isDirectory(repositorydir)) {
					out.println("\t\tERROR: Path does not specify an existing directory.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				break;
			}
			
			//Fragment Type
			while(true) {
				out.println("");
				out.println("Specify clone granularity.");
				out.println("    [1] - Function.  All clones will be at the function granularity.");
				out.println("    [2] - Block.  All clones will be at the block granularity.");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				} else if(inline.equals("1")) {
					fragmentType = ExperimentSpecification.FUNCTION_FRAGMENT_TYPE;
					break;
				} else if(inline.equals("2")) {
					fragmentType = ExperimentSpecification.BLOCK_FRAGMENT_TYPE;
					break;
				} else {
					out.println("\t\tERROR: Invalid option.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
			}

			//Max Fragments
			while(true) {
				out.println("");
				out.println("Specify the maximum number of fragments to select.  The maximum number of");
				out.println("generated clones will then be the number of mutators multiplied by the number of");
				out.println("selected fragments.  Only fragments which can be successfully mutated by all");
				out.println("defined mutators, and which satrisfy the other generation properties, will be");
				out.println("selected.");
				out.println("");
				out.println("Valid range: [" + 0 + "," + Integer.MAX_VALUE + "].");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [0," + Integer.MAX_VALUE + "].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				maxFragments=inint;
				break;
			}
			
			//Fragment Min Size Lines
			while(true) {
				out.println("");
				out.println("Specify minimum clone fragment size measured in lines.");
				out.println("Valid range: [1," + Integer.MAX_VALUE + "].");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [1," + Integer.MAX_VALUE + "].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				minSizeLines = inint;
				break;
			}
			
			//Fragment Max Size Lines
			while(true) {
				out.println("");
				out.println("Specify maximum clone fragment size measured in lines.");
				out.println("Valid range: [" + minSizeLines + "," + Integer.MAX_VALUE + "].");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				if(inint <= minSizeLines) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + minSizeLines + "," + Integer.MAX_VALUE + "].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				maxSizeLines=inint;
				break;
			}
			
			//Fragment Min Size Tokens
			while(true) {
				out.println("");
				out.println("Specify minimum clone fragment size measured in tokens.");
				out.println("Valid range: [1," + Integer.MAX_VALUE + "].");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [1," + Integer.MAX_VALUE + "].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				minSizeTokens = inint;
				break;
			}
			
			//Fragment Max Size Tokens
			while(true) {
				out.println("");
				out.println("Specify maximum clone fragment size measured in tokens.");
				out.println("Valid range: [" + minSizeTokens + "," + Integer.MAX_VALUE + "].");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= minSizeTokens) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + minSizeTokens + "," + Integer.MAX_VALUE + "].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				maxSizeTokens=inint;
				break;
			}
			
			//Injection Number
			while(true) {
				out.println("");
				out.println("Specify injection number.  This is the number of times each clone should be");
				out.println("injected into the subject system.  Each injection produces a unique mutant base.");
				out.println("The reason to inject multiple times is to reduce clone detector detection");
				out.println("performance with respect to a specific clone due to injection location.");
				out.println("Valid range: [1, " + Integer.MAX_VALUE + "].");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + minSizeTokens + "," + Integer.MAX_VALUE + "].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				injectionNumber=inint;
				break;
			}
			
			//Allowed Fragment Difference
			while(true) {
				out.println("");
				out.println("Specify allowed fragment difference.  This is the maximum % difference allowed");
				out.println("between two fragments of a generated.  % difference is measured after the two");
				out.println("fragments have been pretty-printed and normalized.  % difference is measured");
				out.println("both by token and by line using the diff algorithm to match similar items.  The");
				out.println("must satsify both measures to be accepted.  Use this guarenteed maximum");
				out.println("% difference to configure/tune evaluated ");
				out.println("Valid range: [0.0,1.0].");
				out.println("Suggested value: 0.30.");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					indouble = Double.parseDouble(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid double format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				if(indouble > 1.0 || indouble < 0.0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [0.0,1.0].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				allowedFragmentDifference=indouble;
				break;
			}
			
			//Mutation Containment
			while(true) {
				out.println("");
				out.println("As an example, if a selected fragment is 10 lines and mutation containment is");
				out.println("0.20, then a clone generated from it will not have a mutated fragment with");
				out.println("changesm ade to the first or last 2 lines.");
				out.println("");
				out.println("Valid range: [0.0,1.0].");
				out.println("Suggested value: 0.15.");
				out.println("Note: The evaluation property, subsume tolerance, should be no larger than this.");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					indouble = Double.parseDouble(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid double format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				if(indouble > 1.0 || indouble < 0.0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [0.0,1.0].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				mutationContainment=indouble;
				break;
			}
			
			//MutatorAttempts
			while(true) {
				out.println("");
				out.println("Specify the mutator attempts.  This is the number of times to attempt running a");
				out.println("mutator.  This is needed as a mutator may not produce a clone which satsifies");
				out.println("the the previous generation properties.  When the number of attempts is exceeded");
				out.println("the fragment being mutated is deemed not mutatable by the mutator.");
				out.println("");
				out.println("Valid range: [1, " + Integer.MAX_VALUE + "].");
				out.println("Suggested value: 25.");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + 1 + "," + Integer.MAX_VALUE + "].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				mutatorAttempts=inint;
				break;
			}
			
			//OperatorAttempts
			while(true) {
				out.println("");
				out.println("Specify the operator attempts.  This is the number of times to attempt running a");
				out.println("operator.  This is needed as the operators are not perfect and may introduce");
				out.println("syntax errors.  Errors should be caught, but >1 attempts will allow the operator");
				out.println("to be tried again to see if a valid application of the operator on the fragment");
				out.println("is possible.");
				out.println("");
				out.println("Valid range: [1, " + Integer.MAX_VALUE + "].");
				out.println("Suggested value: 10.");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					out.println("Canceling new experiment creation. Press enter to continue.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + 1 + "," + Integer.MAX_VALUE + "].");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				operatorAttempts=inint;
				break;
			}
			
			//Summarize Information
			out.println("");
			out.println("-Summary------------------------------------------------------------------------");
			out.println("  Generation Properties:");
			out.println("    GenerationType: Automatic");
			out.println("    FragmentType: " + ExperimentSpecification.fragmentTypeToString(fragmentType));
			out.println("    MaxFragments: " + maxFragments);
			out.println("    MinSizeLines: " + minSizeLines);
			out.println("    MaxSizeLines: " + maxSizeLines);
			out.println("    MinSizeTokens: " + minSizeTokens);
			out.println("    MaxSizeTokens: " + maxSizeTokens);
			out.println("    InjectionNumber: " + injectionNumber);
			out.println("    AllowedFragmentDifference: " + allowedFragmentDifference);
			out.println("    MutationContainment: " + mutationContainment);
			out.println("    MutatorAttempts: " + mutatorAttempts);
			out.println("    OperatorAttempts: " + operatorAttempts);
			out.println("    Subject System: " + systemdir);
			out.println("    Repository: " + repositorydir);
			out.println("--------------------------------------------------------------------------------");
			while(true) {
				out.println("Is this acceptable?");
				out.println("    [y]: Yes.  Create experiment.");
				out.println("    [n]: No.  Cancel creation and return to experiment initializaiton menu.");
				out.println("--------------------------------------------------------------------------------");
				out.print(":::: ");
				inline = in.readLine();
				if(inline.equals("y")) {
					ExperimentSpecification es = new ExperimentSpecification(datadir, systemdir, repositorydir, language);
					
					es.setFragmentMinSizeLines(1);
					es.setFragmentMaxSizeLines(Integer.MAX_VALUE);
					es.setFragmentMinSizeTokens(1);
					es.setFragmentMaxSizeTokens(Integer.MAX_VALUE);
					es.setFragmentMinSizeLines(minSizeLines);
					es.setFragmentMaxSizeLines(maxSizeLines);
					es.setFragmentMinSizeTokens(minSizeTokens);
					es.setFragmentMaxSizeTokens(maxSizeTokens);
					
					es.setFragmentType(fragmentType);
					es.setAllowedFragmentDifference(allowedFragmentDifference);
					es.setInjectNumber(injectionNumber);
					es.setMaxFragments(maxFragments);
					es.setMutationAttempts(mutatorAttempts);
					es.setMutationContainment(mutationContainment);
					es.setOperatorAttempts(operatorAttempts);
					
					
					out.println("The new experiment will now be initilized.  During this the subject system and");
					out.println("will be imported.  Both will be normalized and source files which are");
					out.println("incompatible with the framework removed.  Depending on the size of these two");
					out.println("features, this make take some time.  Press enter to start.");
					out.println("--------------------------------------------------------------------------------");
					in.readLine();
					
					try {
						Experiment e = Experiment.createAutomaticExperiment(es, out);
						out.println("Then new experiment has been successfully initialized.  Press enter to continue.");
						out.println("--------------------------------------------------------------------------------");
						in.readLine();
						return e;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  IllegalArgumentException.  Must be a bug?");
						System.out.println("\t\tPress enter to continue...");
						in.readLine();
						return null;
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  Error occured when accessing database.");
						System.out.println("\t\tPress enter to continue...");
						in.readLine();
						return null;
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  A required external process was interrupted.");
						System.out.println("\t\tPress enter to continue...");
						in.readLine();
						return null;
					} catch (ArtisticStyleFailedException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  Normalization (using astyle) of input file (system|repository) failed.");
						System.out.println("\t\tPress enter to continue...");
						in.readLine();
						return null;
					} catch (FileSanetizationFailedException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  Sanetization if input file (system|repository) failed.");
						System.out.println("\t\tPress enter to continue...");
						in.readLine();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment. IO error occured during creation.");
						System.out.println("\t\tPress enter to continue...");
						in.readLine();
						return null;
					}
				} else if (inline.equals("n")) {
					return null;
				} else {
					out.println("\t\tERROR: Invalid option.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
			}
		} if(generationType == ExperimentSpecification.MANUAL_GENERATION_TYPE) {
			//TODO implement
			//	Specify import
			//	Specify fragment type
			System.out.println("\t\tERROR:Create experiment failed.  Manual not yet implemented.");
			System.out.println("\t\tPress enter to continue...");
			in.readLine();
			return null;
		} else {
			return null;
		}
	}
	
	/**
	 * Load experiment interface.
	 * @param in Interface input reader.
	 * @param out Interface output stream.
	 * @return An experiment if loaded, or null.
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws SQLException
	 */
	private static Experiment load_experiment() throws IOException, IllegalArgumentException, SQLException {
		while(true) {
			out.println("");
			out.println("");
			out.println("");
			out.println("");
			out.println("--------------------------------------------------------------------------------");
			out.println("-Load Existing Experiment-------------------------------------------------------");
			out.println("--------------------------------------------------------------------------------");
			out.println("");
			out.println("Specify complete path to root of experiment.  Provide blank path to cancel.");
			out.println("--------------------------------------------------------------------------------");
			out.print(":::: ");
			String str_path = in.readLine();
			
			//Blank path, cancel load
			if(str_path.equals("")) {
				System.out.println("\t\tNo path specified.  Canceling.");
				System.out.println("\t\tPress enter to continue...");
				in.readLine();
				return null;
				
			//Path Provided
			} else {
				Path path;
				
				//Check path is valid
				try {
					path = Paths.get(str_path);
				} catch (InvalidPathException e) {
					System.out.println("\t\tERROR: Invalid path.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				//Check Directory Exists
				if(!Files.isDirectory(path)) {
					System.out.println("\t\tERROR: Specified directory does not exist.");
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					continue;
				}
				
				//Load Experiment
				Experiment exp;
				try {
					exp = Experiment.loadExperiment(path, out);
				} catch (SQLException e) {
					e.printStackTrace();
					System.out.println("\t\tERROR: Failed to load experiment.  Error with database.");
					System.out.println("\t\tException Message:" + e.getMessage());
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					return null;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
					out.println("--------------------------------------------------------------------------------");
					System.out.println("\t\tERROR: Failed to load experiment, IllegalArgumentException thrown.");
					System.out.println("\t\tException Message:" + e.getMessage());
					e.printStackTrace();
					System.out.println("\t\tPress enter to continue...");
					in.readLine();
					return null;
				}
				return exp;
			}
		}
	}
}
