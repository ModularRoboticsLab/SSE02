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

public class MachineCompiler {

	private FluentMachine machine;

	public MachineCompiler(FluentMachine machine) {
		machine.setTransitionFactory(new HolderFactory());
		this.machine = machine;
	}
	
	public void compile(String directory, String packageName, String className) {
		String definition = new MachineCodegenerator(machine).generate(packageName, className);
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
