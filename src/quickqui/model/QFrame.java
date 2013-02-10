package quickqui.model;

import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JComponent;

/**
 * GUI metamodel: a top-level frame
 */
public class QFrame extends QComponent {
    /**
     * The title of the frame
     */
    private String name;
    /**
     * Create frame model with the given title
     * @param name the title of the frame
     */
    public QFrame(String name) { this.name = name; }
    /**
     * Get the name (frame title)
     */
    public String getName() {
    	return name;
    }
    /**
     * Create Swing GUI, since frames cannot be nested it is an error to call this method on this object
     */
    @Override protected JComponent create(ActionListener handler, Map<String,JComponent> componentMap) {
        throw new Error("Nested frames not allowed");
    }
    /**
     * For debugging
     */
    @Override public String toString() {
        return "Frame("+contents.get(0)+")";
    }
}