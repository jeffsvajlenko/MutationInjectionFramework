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
import experiment.ExperimentSpecification;
import experiment.OperatorDB;

//TODO: Make operator menu.  Make mutator menu.  Make tool menu.  Make setup 

public class Interface {
	
	public static void main(String args[]) throws IOException, IllegalStateException, SQLException, InterruptedException, ArtisticStyleFailedException, IllegalArgumentException, FileSanetizationFailedException, NullPointerException, InvalidToolRunnerException {
		if(start_interface(new BufferedReader(new InputStreamReader(System.in)), System.out)) {
			System.exit(0);
		} else {
			System.exit(-1);
		}
	}
	
	private static boolean start_interface(BufferedReader in, PrintStream out) throws IllegalArgumentException, IOException, SQLException {
		out.println("------------------------------------------------------------------------------------------");
		out.println("--------------------------------MutationInjectionFramework--------------------------------");
		out.println("------------------------------------------------------------------------------------------");
		
		//Setup (Create|Load) Experiment
		Experiment experiment = Interface.setup_experiment_menu(new BufferedReader(new InputStreamReader(System.in)), System.out);
		
		//Generate
		if(!experiment.isGenerated()) {
			while(true) {
				String inline;
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("-GenerateSetup----------------------------------------------------------------------------");
				out.println("How do you wish to create an experiment?");
				out.println("    [1]: Load default Operators/Mutators. []");
				out.println("    [2]: Custom Operators/Mutators setup. [Not Available / Coming Soon!]");
				out.println("    [3]: Quit.  [You can generate this experiment by loading it later]");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				experiment.generateAutomatic();
				inline = in.readLine();
				if (inline.equals("1")) {
					//Operators
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
					
					//Mutators
					for(OperatorDB operator : operators) {
						LinkedList<Integer> oplist = new LinkedList<Integer>();
						oplist.add(operator.getId());
						experiment.addMutator(operator.getDescription(), oplist);
					}
				} else if (inline.equals("2")) {
					out.println("\t\tError: Not yet implemented.  I'm sorry!  I'll get on this soon I promise!");
					System.out.println("Press enter to continue...");
					in.readLine();
				} else if (inline.equals("3")) {
					return true;
				} else {
					out.println("\t\tERROR: Invalid option.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				experiment.generateAutomatic();
				break;
			}
		}
		
		//Evaluate
		if(experiment.isGenerated()) {
			
		}
		
		return true;
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
	private static Experiment setup_experiment_menu(BufferedReader in, PrintStream out) throws IOException, IllegalArgumentException, SQLException {
		String input_line;
		int int_input;
		Experiment experiment = null;
		
		while(true) {
			//Print Menu
			out.println("------------------------------------------------------------------------------------------");
			out.println("------------------------------------------------------------------------------------------");
			out.println("-ExperimentSetup--------------------------------------------------------------------------");
			out.println("How do you wish to create an experiment?");
			out.println("    [1]: Create a new experiment.");
			out.println("    [2]: Load a previous experiment.");
			out.println("    [3]: Quit.");
			out.println("------------------------------------------------------------------------------------------");
			out.print(":::: ");
			
			//Get User Option, Check Valid Integer
			input_line = in.readLine();
			try {
				int_input = Integer.parseInt(input_line);
			} catch (Exception e) {
				System.out.println("\t\tERROR: Selection invalid.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
			
			//If Menu Option Invalid, Re-Request
			if(int_input == 1) {
				experiment = create_new_experiment_menu(in, out);
				if(experiment == null) {
					continue;
				} else {
					break;
				}
			} else if (int_input == 2) {
				experiment = load_experiment_menu(in, out);
				if(experiment == null) {
					continue;
				} else {
					break;
				}
			} else if (int_input == 3) {
				experiment = null;
				break;
			} else {
				System.out.println("\t\tERROR: Selection invalid.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
		}
		return experiment;
	}
	
	/**
	 * Interface for creating a new experiment, not including generation.
	 * @param in Input reader for interface.
	 * @param out Output stream for interface.
	 * @return An experiment with the setup and generation phase complete.
	 * @throws IOException
	 */
	private static Experiment create_new_experiment_menu(BufferedReader in, PrintStream out) throws IOException {
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
		
		out.println("------------------------------------------------------------------------------------------");
		out.println("-CreateExperiment-------------------------------------------------------------------------");
		
		while(true) {
			out.println("------------------------------------------------------------------------------------------");
			out.println("------------------------------------------------------------------------------------------");
			out.println("Please specify experiment language.");
			out.println("    [1] - Java");
			out.println("    [2] - C");
			out.println("    [3] - CS");
			out.println("------------------------------------------------------------------------------------------");
			out.print(":::: ");
			inline = in.readLine();
			if(inline.equals("")) {
				return null;
			} else if (inline.equals("1")) {
				language = ExperimentSpecification.JAVA_LANGUAGE;
			} else if (inline.equals("2")) {
				language = ExperimentSpecification.C_LANGUAGE;
			} else if (inline.equals("3")) {
				language = ExperimentSpecification.CS_LANGUAGE;
			} else {
				System.out.println("\t\tERROR: Invalid selection.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
			break;
		}
		
		while(true) {
			out.println("------------------------------------------------------------------------------------------");
			out.println("------------------------------------------------------------------------------------------");
			out.println("Please specify where to store this experiment (full path), directory must not already");
			out.println("    exist.");
			out.println("------------------------------------------------------------------------------------------");
			out.print(":::: ");
			inline = in.readLine();
			if(inline.equals("")) {
				return null;
			}
			try {
				datadir = Paths.get(inline).toAbsolutePath().normalize();
			} catch (InvalidPathException e) {
				out.println("\t\tERROR: Path specified is invalid.  Please specify another.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
			if(Files.exists(datadir)) {
				out.println("\t\tERROR: Directory specified is already a file or directory.  Please specify another location.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
			try {
				Files.createDirectories(datadir);
			} catch (Exception e) {
				out.println("\t\tERROR: Unable to create the experiment directory.  Please specify another location.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
			Files.deleteIfExists(datadir);
			break;
		}
		
		while(true) {
			out.println("------------------------------------------------------------------------------------------");
			out.println("------------------------------------------------------------------------------------------");
			out.println("Please specify the system directory. The directory containing the subject system you wish");
			out.println("    to hide the clones in.  The system must have at least 2 source files (although ideally");
			out.println("    a lot more).");
			out.println("------------------------------------------------------------------------------------------");
			out.print(":::: ");
			inline = in.readLine();
			if(inline.equals("")) {
				return null;
			}
			try {
				systemdir = Paths.get(inline).toAbsolutePath().normalize();
			} catch (InvalidPathException e) {
				out.println("\t\tERROR: Path specified is invalid.  Please specify another.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
			if(!Files.isDirectory(systemdir)) {
				out.println("\t\tERROR: Path does not specify an existing directory.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
			break;
		}
		
		out.println("------------------------------------------------------------------------------------------");
		out.println("You will now be asked for generation settings. An empty response will cancel this process.");
		out.println("------------------------------------------------------------------------------------------");
		out.println("");
		
		//Check Generation Type
		while(true) {
			out.println("------------------------------------------------------------------------------------------");
			out.println("------------------------------------------------------------------------------------------");
			out.println("Please specify generation type:");
			out.println("    [1] - Automatic.  Clones will be artifically generated using injection/mutation.");
			out.println("    [2] - Manual.  Clones will be imported form properly formatted csv file.");
			out.println("------------------------------------------------------------------------------------------");
			out.print(":::: ");
			inline = in.readLine();
			if(inline.equals("1")) {
				generationType = ExperimentSpecification.AUTOMATIC_GENERATION_TYPE;
				break;
			} else if(inline.equals("")) {
				generationType = ExperimentSpecification.MANUAL_GENERATION_TYPE;
				break;
			} else {
				out.println("\t\tERROR: Invalid option.");
				System.out.println("Press enter to continue...");
				in.readLine();
				continue;
			}
		}
		
		// Generation Type is Automatic
		if(generationType == ExperimentSpecification.AUTOMATIC_GENERATION_TYPE) {
			//Repository
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify the repository directory. The directory containing the soruce from which to");
				out.println("    randomly choose fragments for clone generation.");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				try {
					repositorydir = Paths.get(inline).toAbsolutePath().normalize();
				} catch (InvalidPathException e) {
					out.println("\t\tERROR: Path specified is invalid.  Please specify another.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				if(!Files.isDirectory(repositorydir)) {
					out.println("\t\tERROR: Path does not specify an existing directory.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				break;
			}
			
			//Fragment Type
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify fragment type:");
				out.println("    [1] - Function.  All clones will be at the function granularity.  The fragments of the");
				out.println("                     generated clones are injected after existing functions in the system. ");
				out.println("    [2] - Block.  All clones will be at the block granularity.  The fragments of the");
				out.println("                  generated clones are injected at the start or end of existing code");
				out.println("                  blocks in the sytem.");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				inline = in.readLine();
				if(inline.equals("1")) {
					fragmentType = ExperimentSpecification.FUNCTION_FRAGMENT_TYPE;
					break;
				} else if(inline.equals("2")) {
					fragmentType = ExperimentSpecification.BLOCK_FRAGMENT_TYPE;
					break;
				} else if(inline.equals("")) {
					return null;
				} else {
					out.println("\t\tERROR: Invalid option.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
			}

			//Max Fragments
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify maximum fragments to select.  The number of generated clones will be the");
				out.println("    number of mutators multiplied by the number of selected fragments.  Note that only");
				out.println("    fragments which can be mutated successfully by all mutators, and satsify the");
				out.println("    properties will be chosen.");
				out.println("Valid range: [" + 0 + "," + Integer.MAX_VALUE + "].");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [0," + Integer.MAX_VALUE + "].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				maxFragments=inint;
				break;
			}
			
			//Fragment Min Size Lines
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify minimum fragment size measured in lines.  Valid range: [1," + Integer.MAX_VALUE + "].");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [1," + Integer.MAX_VALUE + "].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				minSizeLines = inint;
				break;
			}
			
			//Fragment Max Size Lines
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify maximum fragment size measured in lines.");
				out.println("Valid range: [" + minSizeLines + "," + Integer.MAX_VALUE + "].");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				if(inint <= minSizeLines) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + minSizeLines + "," + Integer.MAX_VALUE + "].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				maxSizeLines=inint;
				break;
			}
			
			//Fragment Min Size Tokens
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify minimum fragment size measured in tokens.");
				out.println("Valid range: [1," + Integer.MAX_VALUE + "].");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [1," + Integer.MAX_VALUE + "].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				minSizeTokens = inint;
				break;
			}
			
			//Fragment Max Size Tokens
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify maximum fragment size measured in tokens.");
				out.println("Valid range: [" + minSizeTokens + "," + Integer.MAX_VALUE + "].");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= minSizeTokens) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + minSizeTokens + "," + Integer.MAX_VALUE + "].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				maxSizeTokens=inint;
				break;
			}
			
			//Injection Number
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify the injection number.  This is the number of times each clone should be");
				out.println("    injected.  Each injection produces a mutant base.");
				out.println("Valid range: [1, " + Integer.MAX_VALUE + "].");
				out.println("Suggested value: 1.  Rather increase max fragments for more varied clones.");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + minSizeTokens + "," + Integer.MAX_VALUE + "].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				injectionNumber=inint;
				break;
			}
			
			//Allowed Fragment Difference
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify the allowed fragment difference.  This is the maximum % difference allowed");
				out.println("    between two fragments in a generated clone.  % difference is calculated both by line");
				out.println("	 and by token, and a generated clone must satisfy for both of these types.");
				out.println("Valid range: [0.0,1.0].");
				out.println("Suggested value: 0.30.");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					indouble = Double.parseDouble(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid double format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				if(indouble > 1.0 || indouble < 0.0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [0.0,1.0].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				allowedFragmentDifference=indouble;
				break;
			}
			
			//Mutation Containment
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify the allowed mutation containment.  This is how far from the edges of a");
				out.println("    fragment a mutation must occur.  This expressed as % of fragment size.  For example,");
				out.println("    if a fragment is 10 lines and this is 0.20, then the first and last 2 lines will not");
				out.println("    be modified.  The purpose of this feature is to ensure that mutations occur within");
				out.println("    the generated clone.  Otherwise tools may trim the mutation from their reported clone");
				out.println("    which defeates the purpose!");
				out.println("Valid range: [0.0,1.0].");
				out.println("Suggested value: 0.15.");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					indouble = Double.parseDouble(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid double format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				if(indouble > 1.0 || indouble < 0.0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [0.0,1.0].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				mutationContainment=indouble;
				break;
			}
			
			//MutatorAttempts
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify the mutator attempts.  This is the number of times to attempt running a");
				out.println("    mutator.  This is needed as a mutator may not produce a clone which satsifies the");
				out.println("    the previous generation properties.");
				out.println("Valid range: [1, " + Integer.MAX_VALUE + "].");
				out.println("Suggested value: 25.");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + 1 + "," + Integer.MAX_VALUE + "].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				mutatorAttempts=inint;
				break;
			}
			
			//OperatorAttempts
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify the operator attempts.  This is the number of times to attempt running a");
				out.println("    operator.  This is needed as the operators are not perfect and may introduce syntax");
				out.println("    errors.  Errors should be caught, but >1 attempts will allow the operator to be tried");
				out.println("    again to see if a valid application of the operator on the fragment is possible.");
				out.println("Valid range: [1, " + Integer.MAX_VALUE + "].");
				out.println("Suggested value: 10.");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				
				//Get and check value.
				inline = in.readLine();
				if(inline.equals("")) {
					return null;
				}
				
				try {
					inint = Integer.parseInt(inline);
				} catch (NumberFormatException e) {
					out.println("\t\tERROR: Invalid integer format.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				if(inint <= 0) {
					out.println("\t\tERROR: Invalid value.  Valid range: [" + 1 + "," + Integer.MAX_VALUE + "].");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
				
				operatorAttempts=inint;
				break;
			}
			
			//Summarize Information
			out.println("------------------------------------------------------------------------------------------");
			out.println("------------------------------------------------------------------------------------------");
			out.println("-Summary----------------------------------------------------------------------------------");
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
			out.println("------------------------------------------------------------------------------------------");
			while(true) {
				out.println("Is this acceptable?");
				out.println("    [y]: Yes.  Creates experiment.");
				out.println("    [n]: No.  Cancels creation.");
				out.println("------------------------------------------------------------------------------------------");
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
					es.setGenerationType(generationType);
					es.setInjectNumber(injectionNumber);
					es.setMaxFragments(maxFragments);
					es.setMutationAttempts(mutatorAttempts);
					es.setMutationContainment(mutationContainment);
					es.setOperatorAttempts(operatorAttempts);
					
					try {
						Experiment e = Experiment.createExperiment(es, out);
						return e;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  IllegalArgumentException.  Must be a bug?");
						System.out.println("Press enter to continue...");
						in.readLine();
						return null;
					} catch (SQLException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  Error occured when accessing database.");
						System.out.println("Press enter to continue...");
						in.readLine();
						return null;
					} catch (InterruptedException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  A required external process was interrupted.");
						System.out.println("Press enter to continue...");
						in.readLine();
						return null;
					} catch (ArtisticStyleFailedException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  Normalization (using astyle) of input file (system|repository) failed.");
						System.out.println("Press enter to continue...");
						in.readLine();
						return null;
					} catch (FileSanetizationFailedException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment.  Sanetization if input file (system|repository) failed.");
						System.out.println("Press enter to continue...");
						in.readLine();
						return null;
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("\t\tERROR: Failed to create experiment. IOException occured during creation.");
						System.out.println("Press enter to continue...");
						in.readLine();
						return null;
					}
				} else if (inline.equals("n")) {
					return null;
				} else {
					out.println("\t\tERROR: Invalid option.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
			}
		} if(generationType == ExperimentSpecification.AUTOMATIC_GENERATION_TYPE) {
			
			//Fragment Type
			while(true) {
				out.println("------------------------------------------------------------------------------------------");
				out.println("------------------------------------------------------------------------------------------");
				out.println("Please specify fragment type:");
				out.println("    [1] - Function.  All clones will be at the function granularity.  The fragments of the");
				out.println("                     generated clones are injected after existing functions in the system. ");
				out.println("    [2] - Block.  All clones will be at the block granularity.  The fragments of the");
				out.println("                  generated clones are injected at the start or end of existing code");
				out.println("                  blocks in the sytem.");
				out.println("------------------------------------------------------------------------------------------");
				out.print(":::: ");
				inline = in.readLine();
				if(inline.equals("1")) {
					fragmentType = ExperimentSpecification.FUNCTION_FRAGMENT_TYPE;
					break;
				} else if(inline.equals("2")) {
					fragmentType = ExperimentSpecification.BLOCK_FRAGMENT_TYPE;
					break;
				} else if(inline.equals("")) {
					return null;
				} else {
					out.println("\t\tERROR: Invalid option.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				}
			}
			//TODO implement
			System.out.println("\t\tERROR:Create experiment failed.  Manual not yet implemented.");
			System.out.println("Press enter to continue...");
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
	private static Experiment load_experiment_menu(BufferedReader in, PrintStream out) throws IOException, IllegalArgumentException, SQLException {
		while(true) {
			out.println("------------------------------------------------------------------------------------------");
			out.println("-Load Existing Experiment-----------------------------------------------------------------");
			out.println("------------------------------------------------------------------------------------------");
			out.println("------------------------------------------------------------------------------------------");
			out.println("Where is the experiment located?  Specify provide full path.  Provide empty path if you");
			out.println("    wish to cancel.");
			out.println("------------------------------------------------------------------------------------------");
			out.print(":::: ");
			
			String str_path = in.readLine();
			
			if(str_path.equals("")) {
				System.out.println("\t\tNo path specified.  Canceling...");
				System.out.println("Press enter to continue...");
				in.readLine();
				return null;
			} else {
				Path path = Paths.get(str_path);
				if(!Files.isDirectory(path)) {
					System.out.println("\t\tERROR: Specified directory does not exist.  Press enter to continue.");
					System.out.println("Press enter to continue...");
					in.readLine();
					continue;
				} else {
					try {
						boolean b;
						while(true) {
							out.println("------------------------------------------------------------------------------------------");
							out.println("------------------------------------------------------------------------------------------");
							out.println("Should the experiment be verified?  This may take a significant amount of time.");
							out.println("    Verification was done automatically after generation.  If you are sure that the data");
							out.println("    has not been modified since then it is safe to skip.  Provide empty line to cancel.");
							out.println("    [y]: Yes, verify data.");
							out.println("    [n]: No, skip verification.");
							out.println("------------------------------------------------------------------------------------------");
							out.print(":::: ");
							String inline = in.readLine();
							if (inline.equals("")) {
								return null;
						    }else if(inline.equals("y")) {
								b = true;
							} else if (inline.equals("n")) {
								b = false;
							} else {
								continue;
							}
							break;
						}
						Experiment exp;
						try {
							exp = Experiment.loadExperiment(path, out, b);
						} catch (SQLException e) {
							e.printStackTrace();
							System.out.println("\t\tERROR: Failed to load experiment.  Error with database.");
							System.out.println("Press enter to continue...");
							in.readLine();
							return null;
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
							System.out.println("\t\tERROR: Failed to load experiment, IllegalArgumentException thrown.  Bug?");
							System.out.println("Press enter to continue...");
							in.readLine();
							return null;
						}
						return exp;
					} catch (IllegalArgumentException e) {
						System.out.println("\t\tERROR: Specified experiment is invalid.  Returned error: " + e.getMessage() + ".  Press enter to continue.");
						System.out.println("Press enter to continue...");
						in.readLine();
						continue;
					}
				}
			}
		}
	}
}
