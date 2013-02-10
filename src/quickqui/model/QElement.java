package quickqui.model;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;

/**
 * GUI metamodel: a simple, named element such as a button or label
 */
public class QElement extends QComponent {
    /**
     * The kinds of simple elements supported
     */
    public enum Kind { BUTTON, LABEL }
    /**
     * The element kind
     */
    private QElement.Kind kind;
    /**
     * The properties of this element
     */
    private List<Parameter> properties = new ArrayList<Parameter>();
    /**
     * Create a new element of the given kind with the given properties
     * @param kind the element kind
     * @param properties the element properties
     */
    public QElement(QElement.Kind kind, Parameter[] properties) {
        this.kind = kind; this.properties.addAll(Arrays.asList(properties));
    }
    /**
     * Create the corresponding Swing element, depending on the kind, store in map etc
     */
    @Override protected JComponent create(ActionListener handler, Map<String,JComponent> componentMap) {
        if(kind==Kind.BUTTON) return makeButton(handler,componentMap);
        else if(kind==Kind.LABEL) return makeLabel(componentMap);
        else throw new Error("Element kind not supported: "+kind);
    }
    /**
     * Helper method: create a JLabel
     */
    private JComponent makeLabel(Map<String,JComponent> componentMap) {
        String name = Parameter.get(properties,Parameter.Kind.NAME);
        JLabel label = new JLabel(Parameter.get(properties,Parameter.Kind.TEXT));
        if(name!=null) componentMap.put(name,label);
        return label;
    }
    /**
     * Helper method: create a JButton
     */
    private JComponent makeButton(ActionListener handler, Map<String,JComponent> componentMap) {
        String name = Parameter.get(properties,Parameter.Kind.NAME);
        JButton button = new JButton(Parameter.get(properties,Parameter.Kind.TEXT));
        button.setActionCommand(name);
        button.addActionListener(handler);
        if(name!=null) componentMap.put(name, button);
        return button;
    }
    /**
     * For debugging
     */
    @Override public String toString() {
        return (kind==Kind.BUTTON?"Button":(kind==Kind.LABEL?"Label":"?"))+"[...]";
    }
}