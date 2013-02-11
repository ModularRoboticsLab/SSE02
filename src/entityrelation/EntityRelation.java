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

import java.util.HashMap;
import java.util.Map;

import entityrelation.model.EntityField;
import entityrelation.model.EntityType;

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
     * Initialize entity-relation system by building semantic model
     */
    public EntityRelation() {
        this.build();
        getEntities().put(current.getName(),current);
    }

    /**
     * Builder: Add an entity type to the semantic model
     * @param name the name of the type of the entity
     * @return the builder (for further construction)
     */
    public EntityRelation entity(String name) {
        if(current!=null) getEntities().put(current.getName(), current);
        current = new EntityType(name);
        return this;
    }
    /**
     * Builder: Make the current entity type a sub-type of another (existing) entity type
     * @param name the name of the entity type to sub-type
     * @return the builder (for further construction)
     */
    public EntityRelation sub(String name) {
        current.set_super(getEntities().get(name));
        current.getFields().addAll(current.get_super().getFields());
        return this;
    }
    /**
     * Builder: Add an attribute to an entity
     * @param type the Java type that the attribute should have
     * @param name the name of the attribute
     * @return the builder (for further construction)
     */
    public EntityRelation attribute(Class<?> type, String name) {
        current.getFields().add(new EntityField(name,null,type,false));
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
        current.getFields().add(new EntityField(name,inverse,Entity.class,false));
        getEntities().get(target).getFields().add(new EntityField(inverse,name,Entity.class,true));
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
        current.getFields().add(new EntityField(name,inverse,Entity.class,true));
        getEntities().get(target).getFields().add(new EntityField(inverse,name,Entity.class,false));
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
        current.getFields().add(new EntityField(name,inverse,Entity.class,true));
        getEntities().get(target).getFields().add(new EntityField(inverse,name,Entity.class,true));
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
        for(EntityType t: getEntities().values()) result.append(t.toString()+'\n');
        return result.toString();
    }

	/**
	 * @return the entities
	 */
	public Map<String,EntityType> getEntities() {
		return entities;
	}

}
