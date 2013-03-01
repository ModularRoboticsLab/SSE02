package statemachine.year5.compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import statemachine.year4.codegen.MachineGenerator;

public class MachineCompiler {

	public static void compile(String inputFileName, String outputDirectory) throws FileNotFoundException {
		FileReader file = new FileReader(inputFileName);
		MachineParser parser = new MachineParser(new BufferedReader(file));
		MachineGenerator generator = new MachineGenerator(parser);
		String name = parser.getMachineName();
		String className = getClassName(name);
		String packageName = getPackageName(name);
		generator.compile(outputDirectory, packageName, className);
	}

	private static String getPackageName(String name) {
		int index = name.lastIndexOf('.');
		if(index==-1) throw new Error("Illegal statemachine name: "+name);
		return name.substring(0,index);
	}

	private static String getClassName(String name) {
		int index = name.lastIndexOf('.');
		if(index==-1 || index>=name.length()-2) throw new Error("Illegal statemachine name: "+name);
		return name.substring(index+1);
	}
	
}
