package quickqui.model;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

/**
 * GUI metamodel: abstract class for all model GUI elements
 */
public abstract class QComponent {
    /**
     * The nested contents of the component, empty if none
     */
    protected List<QComponent> contents = new ArrayList<QComponent>();
    /**
     * Get the nested contents of the component, empty if none
     */
    public List<QComponent> getContents() { return contents; }
    /**
     * Add a new component to the nested contents
     * @param component the component to add
     */
    public void add(QComponent component) { contents.add(component); }
    /**
     * Instantiate Swing GUI elements from the model
     * @param handler the handler object to use for all events
     * @param componentMap the map in which to store all named components
     * @return the newly instantiated Swing GUI element
     */
    protected abstract JComponent create(ActionListener handler, Map<String,JComponent> componentMap);
}