package entityrelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import entityrelation.model.EntityField;
import entityrelation.model.EntityType;

/**
 * An entity instance, described by an entity type, and storing the values of the entity
 * @author ups
 */
public class Entity {
    /**
	 * The entity relation system in which this entity exists
	 */
	private final EntityRelation entityRelationSystem;
	/**
     * Type of the entity (describes all of its contents)
     */
    private EntityType type;
    /**
     * Data stored in the entity (described by fields of type)
     */
    private Map<String,Object> data = new HashMap<String,Object>();
    /**
     * Construct an entity, initializing the contents using the list of arguments provided.
     * @param typeName the type of the entity 
     * @param contents one argument for each of the contents to be stored in the array
     * @param entityRelation TODO
     */
    public Entity(EntityRelation entityRelation, String typeName, Object ... contents) {
        entityRelationSystem = entityRelation;
		initialize(typeName,contents);
    }
    /**
     * Perform the actual initialization
     * <i>Note: currently, multi-relation argument must be empty during initialization!</i>
     * @param typeName the type of the entity
     * @param contents one argument for each of the contents to be stored in the array
     */
    private void initialize(String typeName, Object contents[]) {
        // Set type
        type = entityRelationSystem.getEntities().get(typeName);
        if(type==null) throw new Error("Unknown entity type: "+typeName);
        int index = 0;
        // Initialize fields, reading from contents and checking that the types match
        for(EntityField f: type.getFields()) {
            if(index==contents.length) throw new Error("Missing value for "+typeName+" initialization of "+f.getName());
            Object next = contents[index++];
            // Type check
            Class<?> checkType = next.getClass();
            if(f.isMultiple()) { 
                if(!next.getClass().isArray()) throw new Error("Expected multiple argument for "+typeName+"."+f.getName());
                checkType = next.getClass().getComponentType();
            }
            if(!f.getType().isAssignableFrom(checkType)) throw new Error("Illegal type argument, expected "+f.getType()+", got "+checkType);
            if(f.isMultiple()) // Must store set for multi-argument, single element otherwise
                next = new HashSet<Entity>(Arrays.asList((Entity[])next));
            // Store data in field
            data.put(f.getName(),next);
        }
    }
    /**
     * Change the type of the entity, fields with matching names are preserved, all
     * others must get values from the contents argument.
     * @param name the name of the type to change to
     * @param newcontents new contents, must be one for every field not present in the old type (by name)
     */
    public void become(String name, Object ...newcontents) {
        List<Object> newData = new ArrayList<Object>();
        EntityType newType = entityRelationSystem.getEntities().get(name);
        // Build list of data for a complete initialization (old data if available, otherwise new)
        int index = 0;
        for(EntityField f: newType.getFields()) {
            if(data.containsKey(f.getName())) newData.add(data.get(f.getName()));
            else {
                if(index==newcontents.length) throw new Error("become missing data for field "+f.getName());
                newData.add(newcontents[index++]);
            }
        }
        // Do standard initialization
        initialize(name,newData.toArray());
    }
    /**
     * Associated this entity to the entity provided through the specified relation name,
     * preserving binary relations
     * @param name the name of the link
     * @param what the entity to associate to
     */
    public void associate(String name, Entity what) {
        Object link = data.get(name);
        if(link instanceof Set<?>)
            ((Set<Entity>)link).add(what);
        else
            data.put(name, what);
        for(EntityField field: type.getFields()) {
            if(field.getName().equals(name)) {
                Object invLink = what.data.get(field.getInverseMaybe());
                if(invLink instanceof Set<?>)
                    ((Set<Entity>)invLink).add(this);
                else
                    data.put(field.getInverseMaybe(), this);
                return;
            }
        }
        throw new Error("Inconsistent relation: could not find "+name+" in "+type.getName());
    }
    /**
     * Get the value of a multi relation
     * @param name the name of the field
     * @return an EntitySet containing all of the elements referred to by the relation
     */
    public EntitySet getMulti(String name) {
        if(!(data.get(name) instanceof Set<?>)) throw new Error("Not multiple: "+type.getName()+"."+name);
        return new EntitySet(entityRelationSystem, (Set<Entity>)data.get(name));
    }
    /**
     * Debug output
     */
    public String toString() {
        StringBuffer result = new StringBuffer("{"+type.getName()+": ");
        for(Entry<String, Object> entry: data.entrySet()) {
            if(entry.getValue() instanceof Entity || entry.getValue() instanceof Set<?>)
                result.append("["+entry.getKey()+"] ");
            else
                result.append(entry.getValue()+" ");
        }
        result.append("}");
        return result.toString();
    }
}