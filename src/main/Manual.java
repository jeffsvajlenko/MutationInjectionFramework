package main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import models.Clone;
import models.CloneDetectionReport;
import models.InvalidToolRunnerException;

import org.apache.commons.io.FileUtils;

import experiment.Experiment;
import experiment.ExperimentData;
import experiment.ExperimentSpecification;
import experiment.MutatorDB;
import experiment.OperatorDB;
import experiment.ToolDB;

public class Manual {
	public static void main(String args[]) throws IOException, IllegalStateException, SQLException, InterruptedException, ArtisticStyleFailedException, IllegalArgumentException, FileSanetizationFailedException, NullPointerException, InvalidToolRunnerException {
/*
		//Temporary Manual Interface
//CREATION
		//Setup Experiment Specification
		Path jrepository = Paths.get("data/systems/java/");
		Path jsystem = Paths.get("data/systems/java/");
		Path data = Paths.get("experiments/Test/");
		if(Files.exists(data)) {
			FileUtils.deleteDirectory(data.toFile());
		}
		
		ExperimentSpecification es = new ExperimentSpecification(data, jsystem, jrepository, ExperimentSpecification.JAVA_LANGUAGE);
		
		//Generation Properties
		es.setAllowedFragmentDifference(0.30);
		int maxSizeLines = 200; es.setFragmentMaxSizeLines(maxSizeLines);
		int maxSizeTokens = 2000; es.setFragmentMaxSizeTokens(maxSizeTokens);
		int minSizeLines = 15; es.setFragmentMinSizeLines(minSizeLines);
		int minSizeTokens = 100; es.setFragmentMinSizeTokens(minSizeTokens);
		es.setFragmentType(ExperimentSpecification.FUNCTION_FRAGMENT_TYPE);
		es.setInjectNumber(1);
		es.setMaxFragments(1);
		es.setMutationAttempts(25);
		es.setMutationContainment(0.15);
		es.setOperatorAttempts(10);

		es.setPrecisionRequiredSimilarity(0.50);
		es.setRecallRequiredSimilarity(0.50);
		es.setSubsumeMatcherTolerance(0.15);
		
		//Create Experiment
		System.out.println("----Setting Up Experiment----");
		Experiment e = Experiment.createAutomaticExperiment(es, System.out);
		ExperimentData ed = e.getExperimentData();
				
//SETUP PHASE
		//Operators
		OperatorDB o;
		List<OperatorDB> operators = new LinkedList<OperatorDB>();
		o=e.addOperator("id", "Change in comments: comment added between two tokens..", 1, Paths.get("operators/mCC_BT")); operators.add(o);
		o=e.addOperator("id", "Change in comments: comment added at end of a line.", 1, Paths.get("operators/mCC_EOL")); operators.add(o);
		o=e.addOperator("id", "Change in formatting: a newline is added between two tokens..", 1, Paths.get("operators/mCF_A")); operators.add(o);
		o=e.addOperator("id", "Change in formatting: a newline is removed (without changing meangin).", 1, Paths.get("operators/mCF_R")); operators.add(o);
		o=e.addOperator("id", "Change in whitespace: a space or tab is added between two tokens.", 1, Paths.get("operators/mCW_A")); operators.add(o);
		o=e.addOperator("id", "Change in whitespace: a space or tab is removed (without changing meaning).", 1, Paths.get("operators/mCW_R")); operators.add(o);
		o=e.addOperator("id", "Renaming of identifier: systamtic renaming of all instances of a chosen identifier.", 2, Paths.get("operators/mSRI")); operators.add(o);
		o=e.addOperator("id", "Renaming of identifier: arbitrary renaming of a single identifier instance.", 2, Paths.get("operators/mARI")); operators.add(o);
		o=e.addOperator("id", "Change in literal value: a number value is replaced.", 2, Paths.get("operators/mRL_N")); operators.add(o);
		o=e.addOperator("id", "Change in literal value: a string value is replaced.", 2, Paths.get("operators/mRL_S")); operators.add(o); //slow
		o=e.addOperator("id", "Deletion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mDL")); operators.add(o);
		o=e.addOperator("id", "Insertion of a line of code (without invalidating syntax).", 3, Paths.get("operators/mIL")); operators.add(o);
		o=e.addOperator("id", "Modification of a whole line ((without invalidating syntax).", 3, Paths.get("operators/mML")); operators.add(o);
		o=e.addOperator("id", "Small deletion within a line (removal of a parameter).", 3, Paths.get("operators/mSDL")); operators.add(o);
		o=e.addOperator("id", "Small insertion within a line (addition of a parameter).", 3, Paths.get("operators/mSIL")); operators.add(o);
		
		
		//Mutators
		for(OperatorDB operator : operators) {
			LinkedList<Integer> oplist = new LinkedList<Integer>();
			oplist.add(operator.getId());
			e.addMutator(operator.getDescription(), oplist);
		}
*/
		Experiment e = Experiment.loadExperiment(Paths.get("experiments/Test/"), System.out, false);
		ExperimentData ed = e.getExperimentData();
		System.out.println("----Settings Log----");
		System.out.println("Generation Settings:");
		System.out.println("\tLanguage: " + ExperimentSpecification.languageToString(ed.getLanguage()));
		System.out.println("\tGeneration Type: " + ed.getGenerationType());
		System.out.println("\tOperator Attempts: " + ed.getOperatorAttempts());
		System.out.println("\tMutation Attempts: " + ed.getMutationAttempts());
		System.out.println("");
		System.out.println("\tMaximum Fragments: " + ed.getMaxFragments());
		System.out.println("\tFragment Type: " + ExperimentSpecification.fragmentTypeToString(ed.getFragmentType()));
		System.out.println("\tInjection Number: " + ed.getInjectionNumber());
		System.out.println("\tMutation Containment: " + ed.getMutationContainment());
		System.out.println("\tAllowed Difference: " + ed.getAllowedFragmentDifference());
		System.out.println("\tFragment Size Min Lines: " + ed.getFragmentMinimumSizeLines());
		System.out.println("\tFragment Size Max Lines: " + ed.getFragmentMaximumSizeLines());
		System.out.println("\tFragment Size Min Tokens: " + ed.getFragmentMinimumSizeTokens());
		System.out.println("\tFragment Size Max Tokens: " + ed.getFragmentMaximumSizeTokens());
		System.out.println("");
		System.out.println("Operators:");
		List<OperatorDB> ops = e.getOperators();
		for(OperatorDB operator : ops) {
			System.out.println("\t" + operator.toString());
		}
		System.out.println("Mutators:");
		List<MutatorDB> mutators = e.getMutators();
		for(MutatorDB mut : mutators) {
			System.out.println("\t" + mut.toString());
		}
		System.out.println("Evaluation Settings:");
		System.out.println("\tSubsume Tolerance: " + ed.getSubsumeMatcherTolerance());
		System.out.println("\tRecall Required Similarity: " + ed.getRecallRequiredSimilarity());
		System.out.println("\tPrecision Required Similarity: " + ed.getPrecisionRequiredSimilarity());
		System.out.println("Tools:");
		List<ToolDB> tools = e.getTools();
		for(ToolDB tool : tools) {
			System.out.println("\t" + tool.toString());
		}

		
//Generation Phase
		//e.generateAutomatic();
		
//EVALUATION PHASE
		ed.previousStage();//ed.previousStage();
		ed.deleteTools();
		e.addTool("NiCad", "NiCad-3.4 near miss clone detector.", Paths.get("tools/NiCad-3.4/"), Paths.get("tools/NiCad-3.4/NiCadRunner/NiCadRunner"));
		//e.addTool("SimCad", "SimCad-2.1 simhash based clone detector.", Paths.get("tools/SimCad-2.1/"), Paths.get("tools/SimCad-2.1/SimCadRunner/SimCadRunner"));
		//e.addTool("iclones", "iclones-0.1 incremental clone detector.", Paths.get("tools/iclones-0.1/"), Paths.get("tools/iclones-0.1/IClonesRunner/IClonesRunner"));
		//e.addTool("CCFinderX", "CCFinderX 10.2.7.4.", Paths.get("tools/CCFinderx-10.2.7.4/bin/"), Paths.get("tools/CCFinderx-10.2.7.4/CCFinderRunner/CCFinderRunner"));
		//e.addTool("Deckard", "Deckard clone detector.", Paths.get("tools/Deckard-1.2.3/"), Paths.get("tools/Deckard-1.2.3/DeckardRunner/DeckardRunner"));
		//e.addTool("Simian", "Simian-2.3.33", Paths.get("tools/Simian-2.3.33/bin/"), Paths.get("tools/Simian-2.3.33/SimianRunner/SimianRunner/"));
		
		e.evaluateTools(false);
		e.outputResults();
	}
}
