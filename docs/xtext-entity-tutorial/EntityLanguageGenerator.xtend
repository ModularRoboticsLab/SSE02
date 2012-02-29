/*
 * Entity language code generator
 */
package dk.sdu.mmmi.sse02.tutorial2.generator

import org.eclipse.emf.ecore.resource.Resource
import org.eclipse.xtext.generator.IGenerator
import org.eclipse.xtext.generator.IFileSystemAccess

import static extension org.eclipse.xtext.xbase.lib.IteratorExtensions.*
import org.eclipse.xtext.naming.IQualifiedNameProvider
import com.google.inject.Inject

import dk.sdu.mmmi.sse02.tutorial2.entityLanguage.Entity
import dk.sdu.mmmi.sse02.tutorial2.entityLanguage.Feature
import dk.sdu.mmmi.sse02.tutorial2.entityLanguage.PackageDeclaration

class EntityLanguageGenerator implements IGenerator {
	
	@Inject extension IQualifiedNameProvider nameProvider 
	
	override void doGenerate(Resource resource, IFileSystemAccess fsa) {
		for(e: resource.getAllContents().toIterable().filter(typeof(Entity))) {
			fsa.generateFile(e.getFullyQualifiedName.toString.replace(".", "/") + ".java",e.compile)
		}
	}
	
	// Helper function: checks if the container of the entity has a type of the given name
	// Package names are not consistent between interfaces and eObjects, so just use the simple name for comparison
	def containerIs(Entity e, Class<?> type) {
		e.eContainer.eClass().name.equals(type.simpleName)
	}
	
	def compile(Entity e) '''
	// Auto-generated code
	«IF containerIs(e,typeof(PackageDeclaration))»
	package «e.eContainer.getFullyQualifiedName»;
	«ELSE»
	// Default package
	«ENDIF»
	import java.util.List;
	import java.util.ArrayList;
	
	public class «e.name» «IF e.supertype!=null» extends «e.supertype.getFullyQualifiedName» «ENDIF» {
		«FOR f:e.features»
			«f.compile»
		«ENDFOR»
	}
    '''
    
    def compile(Feature f) '''
    «IF !f.many»
    private «f.type.getFullyQualifiedName» «f.name»;
    public void set«f.name.toFirstUpper»(«f.type.getFullyQualifiedName» value) { «f.name» = value; }
    public «f.type.getFullyQualifiedName» get«f.name.toFirstUpper»() { return «f.name»; }
    «ELSE»
    private List<«f.type.getFullyQualifiedName»> «f.name» = new ArrayList<«f.type.getFullyQualifiedName»>();
    public void add«f.name.toFirstUpper»(«f.type.getFullyQualifiedName» value) { «f.name».add(value); }
    public List<«f.type.getFullyQualifiedName»> get«f.name.toFirstUpper»() { return «f.name»; }
    «ENDIF»
    '''
    
}
