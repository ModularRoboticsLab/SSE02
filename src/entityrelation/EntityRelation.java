/*
Copyright (c) 2012, Ulrik Pagh Schultz, University of Southern Denmark
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the University of Southern Denmark.
*/

package entityrelation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Entity-Relation example: specialization of entities, they can change class, and have binary n-n relations
 * 
 * The semantic model classes EntityType and EntityField are all inner classes, meaning they can
 * refer to state (the entire model) stored in the enclosing EntityRelation class
 *  
 * @author ups
 */
public abstract class EntityRelation {

    /**
     *  Empty set of entities (convenience syntax)
     */
    public final Entity[] EMPTY = new Entity[0];
    /**
     *  Use single entity as argument when specifying multi-relation value (convenience syntax)
     * @param entity the entity to make into a array for creating a multi-relation
     * @return an array containing the entity
     */
    public static Object multi(Entity entity) { return new Entity[] { entity }; }
    
    /**
     * Type of an entity: has name, optional supertype, and a list of typed fields.
     * Fields of supertype (if any) are copied down, so the set of fields is complete for a given type.
     * Currently no support for storing the entity type
     * @author ups
     */
    public class EntityType {
        /**
         * Name of the entity
         */
        private String name;
        /**
         * The super type of the entity, if any, otherwise null
         */
        private EntityType _super;
        /**
         *  The fields associated  with this type
         */
        private List<EntityField> fields = new ArrayList<EntityField>();
        /**
         * Construct type with no associations
         * @param name the name of the type
         */
        public EntityType(String name) { this.name = name; }
        /**
         * For debugging
         */
        public String toString() {
            StringBuffer result = new StringBuffer("<"+name);
            if(_super!=null) result.append("("+_super.name+")");
            for(EntityField f: fields) result.append(f.toString());
            result.append(">");
            return result.toString();
        }
    }
    
    /**
     * Description of a field stored in an entity type, that is, the type of the field.
     * @author ups
     */
    public class EntityField {
        /**
         *  Name of the field, and name of the corresponding inverse if there is one (null otherwise)
         */
        private String name, inverseMaybe;
        /**
         *  The type of the field, Entity if it is an entity (single or multiple)
         */
        private Class<?> type;
        /**
         *  Indication of a single or multi relation
         */
        private boolean isMultiple;
        /**
         * Construct
         * @param name name of the field
         * @param inverseMaybe name of the inverse field in the target, if any, null otherwise
         * @param type the type of Java object referenced by the field
         * @param isMultiple true if this is a "*" multiplicity relation
         */
        public EntityField(String name, String inverseMaybe, Class<?> type, boolean isMultiple) {
            this.name = name; this.inverseMaybe = inverseMaybe; this.type = type; this.isMultiple = isMultiple;
        }
        /**
         * String representation
         */
        public String toString() {
            return "["+name+":"+type.getName()+"@"+(isMultiple?"*":"1")+"]";
        }
    }
    
    /**
     * An entity instance, described by an entity type, and storing the values of the entity
     * @author ups
     */
    public class Entity {
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
         */
        public Entity(String typeName, Object ... contents) {
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
            type = entities.get(typeName);
            if(type==null) throw new Error("Unknown entity type: "+typeName);
            int index = 0;
            // Initialize fields, reading from contents and checking that the types match
            for(EntityField f: type.fields) {
                if(index==contents.length) throw new Error("Missing value for "+typeName+" initialization of "+f.name);
                Object next = contents[index++];
                // Type check
                Class<?> checkType = next.getClass();
                if(f.isMultiple) { 
                    if(!next.getClass().isArray()) throw new Error("Expected multiple argument for "+typeName+"."+f.name);
                    checkType = next.getClass().getComponentType();
                }
                if(!f.type.isAssignableFrom(checkType)) throw new Error("Illegal type argument, expected "+f.type+", got "+checkType);
                if(f.isMultiple) // Must store set for multi-argument, single element otherwise
                    next = new HashSet<Entity>(Arrays.asList((Entity[])next));
                // Store data in field
                data.put(f.name,next);
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
            EntityType newType = entities.get(name);
            // Build list of data for a complete initialization (old data if available, otherwise new)
            int index = 0;
            for(EntityField f: newType.fields) {
                if(data.containsKey(f.name)) newData.add(data.get(f.name));
                else {
                    if(index==newcontents.length) throw new Error("become missing data for field "+f.name);
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
            for(EntityField field: type.fields) {
                if(field.name.equals(name)) {
                    Object invLink = what.data.get(field.inverseMaybe);
                    if(invLink instanceof Set<?>)
                        ((Set<Entity>)invLink).add(this);
                    else
                        data.put(field.inverseMaybe, this);
                    return;
                }
            }
            throw new Error("Inconsistent relation: could not find "+name+" in "+type.name);
        }
        /**
         * Get the value of a multi relation
         * @param name the name of the field
         * @return an EntitySet containing all of the elements referred to by the relation
         */
        public EntitySet getMulti(String name) {
            if(!(data.get(name) instanceof Set<?>)) throw new Error("Not multiple: "+type.name+"."+name);
            return new EntitySet((Set<Entity>)data.get(name));
        }
        /**
         * Debug output
         */
        public String toString() {
            StringBuffer result = new StringBuffer("{"+type.name+": ");
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
    
    /**
     * Placeholder class for results returned from getMulti
     * @author ups
     */
    public class EntitySet {
        /**
         *  Contents of the result
         */
        private Set<Entity> contents = new HashSet<Entity>();
        /**
         * Create EntitySet
         * @param set the set to hold
         */
        public EntitySet(Set<Entity> set) {
            contents.addAll(set);
        }
        /**
         * Recursive getMulti operation: perform on all elements and join
         * @param name the name of the relation to get from all of the elements
         * @return the union of all of the sets gotten from all of the elements
         */
        public EntitySet getMulti(String name) {
            Set<Entity> result = new HashSet<Entity>();
            for(Entity entity: contents) {
                result.addAll(entity.getMulti(name).contents);
            }
            return new EntitySet(result);
        }
        /**
         * For debugging
         */
        public String toString() { return contents.toString(); }
    }

    /**
     * Initialize entity-relation system by building semantic model
     */
    public EntityRelation() {
        this.build();
        entities.put(current.name,current);
    }

    /**
     * Builder: Add an entity type to the semantic model
     * @param name the name of the type of the entity
     * @return the builder (for further construction)
     */
    public EntityRelation entity(String name) {
        if(current!=null) entities.put(current.name, current);
        current = new EntityType(name);
        return this;
    }
    /**
     * Builder: Make the current entity type a sub-type of another (existing) entity type
     * @param name the name of the entity type to sub-type
     * @return the builder (for further construction)
     */
    public EntityRelation sub(String name) {
        current._super = entities.get(name);
        current.fields.addAll(current._super.fields);
        return this;
    }
    /**
     * Builder: Add an attribute to an entity
     * @param type the Java type that the attribute should have
     * @param name the name of the attribute
     * @return the builder (for further construction)
     */
    public EntityRelation attribute(Class<?> type, String name) {
        current.fields.add(new EntityField(name,null,type,false));
        return this;
    }
    /**
     * Builder: Add a 1-* relation to an entity (with inverse name specified)
     * @param name the name of the relation (from this object)
     * @param target the target to add the relation to (will be reflexive)
     * @param inverse the name of the inverse relation (from target to this object)
     * @return the builder (for further construction)
     */
    public EntityRelation relation_1_n(String name, String target, String inverse) {
        current.fields.add(new EntityField(name,inverse,Entity.class,false));
        entities.get(target).fields.add(new EntityField(inverse,name,Entity.class,true));
        return this;
    }
    /**
     * Builder: Add a *-1 relation to an entity (with inverse name specified)
     * @param name the name of the relation (from this object)
     * @param target the target to add the relation to (will be reflexive)
     * @param inverse the name of the inverse relation (from target to this object)
     * @return the builder (for further construction)
     */
    public EntityRelation relation_n_1(String name, String target, String inverse) {
        current.fields.add(new EntityField(name,inverse,Entity.class,true));
        entities.get(target).fields.add(new EntityField(inverse,name,Entity.class,false));
        return this;
    }
    /**
     * Builder: Add a *-* relation to an entity (with inverse name specified)
     * @param name the name of the relation (from this object)
     * @param target the target to add the relation to (will be reflexive)
     * @param inverse the name of the inverse relation (from target to this object)
     * @return the builder (for further construction)
     */
    public EntityRelation relation_n_n(String name, String target, String inverse) {
        current.fields.add(new EntityField(name,inverse,Entity.class,true));
        entities.get(target).fields.add(new EntityField(inverse,name,Entity.class,true));
        return this;
    }
    
    /**
     * Context variable for building semantic model
     */
    private EntityType current;
    /**
     * Semantic model
     */
    private Map<String,EntityType> entities = new HashMap<String,EntityType>();
    
    /**
     * Builder method, override in subclass to create semantic model using builder methods
     */
    protected abstract void build();
    
    /**
     * For debugging
     */
    public String toString() {
        StringBuffer result = new StringBuffer();
        for(EntityType t: entities.values()) result.append(t.toString()+'\n');
        return result.toString();
    }
}
