package statemachine.year5.compiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import statemachine.year4.codegen.MachineGenerator;

/**
 * Class that provides a single entrypoint for a compilation process from statemachine source code
 * to generated code
 * @author ups
 *
 */
public class MachineCompiler {

	/**
	 * Compile statemachine source code to generated code
	 * @param inputFileName the input file to parse
	 * @param outputDirectory the base directory in which to store the generated code
	 * @throws FileNotFoundException if the input file was not found or if the target directory does not exist
	 */
	public static void compile(String inputFileName, String outputDirectory) throws FileNotFoundException {
		FileReader file = new FileReader(inputFileName);
		MachineParser parser = new MachineParser(new BufferedReader(file));
		MachineGenerator generator = new MachineGenerator(parser);
		String name = parser.getMachineName();
		String className = getClassName(name);
		String packageName = getPackageName(name);
		generator.compile(outputDirectory, packageName, className);
	}

	/**
	 * Get the package name from a dotted name sequence
	 * @param name full name
	 * @return the package name
	 */
	private static String getPackageName(String name) {
		int index = name.lastIndexOf('.');
		if(index==-1) throw new Error("Illegal statemachine name: "+name);
		return name.substring(0,index);
	}

	/**
	 * Get the class name from a dotted name sequence
	 * @param name full name
	 * @return the class name
	 */
	private static String getClassName(String name) {
		int index = name.lastIndexOf('.');
		if(index==-1 || index>=name.length()-2) throw new Error("Illegal statemachine name: "+name);
		return name.substring(index+1);
	}
	
}
