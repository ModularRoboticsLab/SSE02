package entityrelation.model;

import java.util.ArrayList;
import java.util.List;


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
     * @param entityRelation TODO
     */
    public EntityType(String name) { 
    	this.name = name; 
    }
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
    /**
     * Get name of the entity
     * @return name of the enity
     */
	public String getName() {
		return name;
	}
	/**
	 * Get the super type of the entity, if any, otherwise null
	 * @return the super type of the entity
	 */
	public EntityType get_super() {
		return _super;
	}
	/**
	 * Set the super type of the entity (null for none)
	 * @param _super the new super type of the entity
	 */
	public void set_super(EntityType _super) {
		this._super = _super;
	}
	/**
	 * Get the fields associated with this type
	 * @return the fields
	 */
	public List<EntityField> getFields() {
		return fields;
	}
}