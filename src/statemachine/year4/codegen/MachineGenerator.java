package statemachine.year4.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import statemachine.year2.framework.Transition;
import statemachine.year3.dsl.FluentMachine;
import statemachine.year3.dsl.FluentMachine.TransitionFactory;
import statemachine.year3.dsl.IntegerState;
import statemachine.year3.dsl.FluentMachine.Condition;
import statemachine.year3.dsl.FluentMachine.Effect;

/**
 * Manages compilation of a FluentMachine model to a class file
 * @author ups
 *
 */
public class MachineGenerator {

	/**
	 * The model to compile
	 */
	private FluentMachine machine;

	/**
	 * Create compiler for the given FluentMachine model
	 * @param machine the model to compile
	 */
	public MachineGenerator(FluentMachine machine) {
		machine.setTransitionFactory(new HolderFactory());
		this.machine = machine;
	}
	
	/**
	 * Perform the compilation into the given directory with the given naming
	 * @param directory name of directory to store code in, excluding package-name-derived directory
	 * @param packageName package (and subdirectory) to store code in
	 * @param className class name and also file name to store code in
	 */
	public void compile(String directory, String packageName, String className) {
		String definition = new MachineJavaCodeGenerator(machine).generate(packageName, className);
		String fileName = directory+File.separatorChar+packageName.replace('.', File.separatorChar)+File.separatorChar+className+".java";
		try {
			FileWriter writer = new FileWriter(fileName);
			writer.write(definition);
			writer.close();
		} catch (IOException e) {
			throw new Error("I/O error: "+e);
		}
		System.out.println("Wrote file "+fileName);
	}
	
	/**
	 * Transition factory that stores the transition in a TransitionHolder instance
	 */
	private class HolderFactory extends TransitionFactory {
		@Override
		protected Transition createTransitionHook(String target, 
				Effect effect, IntegerState effectVar, int effectArg, 
				Condition cond, IntegerState condVariableMaybe, int condValue) {
			return new TransitionHolder(target,
	                effect,effectVar,effectArg,
	                cond, condVariableMaybe,condValue);
		}		
	}
	
}
