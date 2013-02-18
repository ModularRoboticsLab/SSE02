package statemachine.year4.codegen;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MachineCompiler {

	public void compile(String directory, String packageName, String className, String definition) {
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
	
}
