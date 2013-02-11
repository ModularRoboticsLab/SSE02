package entityrelation.model;

/**
 * Description of a field stored in an entity type, that is, the type of the field.
 * @author ups
 */
public class EntityField {
	/**
     *  Name of the field, and name of the corresponding inverse if there is one (null otherwise)
     */
    private String name;
	private String inverseMaybe;
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
     * @param entityRelation TODO
     */
    public EntityField(String name, String inverseMaybe, Class<?> type, boolean isMultiple) {
		this.name = name; this.inverseMaybe = inverseMaybe; this.type = type; this.isMultiple = isMultiple;
    }
    /**
     * String representation
     */
    public String toString() {
        return "["+getName()+":"+getType().getName()+"@"+(isMultiple()?"*":"1")+"]";
    }
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @return the type
	 */
	public Class<?> getType() {
		return type;
	}
	/**
	 * @return the isMultiple
	 */
	public boolean isMultiple() {
		return isMultiple;
	}
	/**
	 * @return the inverseMaybe
	 */
	public String getInverseMaybe() {
		return inverseMaybe;
	}
}