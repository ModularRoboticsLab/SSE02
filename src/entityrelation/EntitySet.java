package entityrelation;

import java.util.HashSet;
import java.util.Set;

/**
 * Placeholder class for results returned from getMulti
 * @author ups
 */
public class EntitySet {
    /**
	 * The entity relation system in which this entity set exists
	 */
	private final EntityRelation entityRelationSystem;
	/**
     *  Contents of the result
     */
    private Set<Entity> contents = new HashSet<Entity>();
    /**
     * Create EntitySet
     * @param set the set to hold
     * @param entityRelation TODO
     */
    public EntitySet(EntityRelation entityRelation, Set<Entity> set) {
        entityRelationSystem = entityRelation;
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
        return new EntitySet(entityRelationSystem, result);
    }
    /**
     * For debugging
     */
    public String toString() { return contents.toString(); }
}