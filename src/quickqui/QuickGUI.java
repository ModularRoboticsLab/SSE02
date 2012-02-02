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

package quickqui;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * QuickGUI allows concise description of a simple GUI (one frame, buttons, labels, organized into panels)
 * to be instantiated as a GUI that can be used from the application.
 * @author ups
 */
public class QuickGUI {

    /**
     * A parameter provided to describe a GUI element
     */
    public static class Parameter {
        /**
         * Description of the kind of a parameter
         */
        public enum Kind { TEXT, NAME; }
        /**
         * The kind of the parameter
         */
        private Kind kind;
        /**
         * The value of the parameter 
         */
        private String value;
        /**
         * Create a new parameter
         * @param kind the kind of the parameter
         * @param value the value of the parameter
         */
        public Parameter(Kind kind, String value) {
            this.kind = kind;
            this.value = value;
        }
        /**
         * Get the kind of the parameter
         * @return the parameter kind
         */
        public Kind getKind() { return kind; }
        /**
         * Get the value of the parameter
         * @return the parameter value
         */
        public String getValue() { return value; }
        /**
         * From a list of parameters, get the value of the first parameter of the given kind, null otherwise
         * @param ps the list of parameters to search
         * @param k the kind of parameter wanted
         * @return the value of the parameter matching the given kind, null otherwise
         */
        public static String get(List<Parameter> ps, Kind k) {
            for(Parameter p: ps) if(p.getKind()==k) return p.getValue();
            return null;
        }
    }
    
    /**
     * GUI metamodel: abstract class for all model GUI elements
     */
    protected static abstract class QComponent {
        /**
         * The nested contents of the component, empty if none
         */
        protected List<QComponent> contents = new ArrayList<QComponent>();
        /**
         * Add a new component to the nested contents
         * @param component the component to add
         */
        protected void add(QComponent component) { contents.add(component); }
        /**
         * Instantiate Swing GUI elements from the model
         * @param handler the handler object to use for all events
         * @param componentMap the map in which to store all named components
         * @return the newly instantiated Swing GUI element
         */
        protected abstract JComponent create(ActionListener handler, Map<String,JComponent> componentMap);
    }
    /**
     * GUI metamodel: a top-level frame
     */
    protected static class QFrame extends QComponent {
        /**
         * The title of the frame
         */
        private String name;
        /**
         * Create frame model with the given title
         * @param name the title of the frame
         */
        QFrame(String name) { this.name = name; }
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
    /**
     * GUI metamodel: a nested panel organizing a number of nested components
     */
    protected static class QPanel extends QComponent {
        /**
         * The layout of the panel
         */
        private GUIModel.Layout layout;
        /**
         * Create a panel model with the given layout
         * @param layout the layout of the panel
         */
        QPanel(GUIModel.Layout layout) { this.layout = layout; }
        /**
         * Create JPanel containing the nested components
         */
        public JPanel create(ActionListener handler, Map<String,JComponent> componentMap) {
            JPanel panel = new JPanel();
            panel.setLayout(getLayout(panel));
            for(QComponent c: contents) panel.add(c.create(handler,componentMap));
            return panel;
        }
        /**
         * Create layout manager
         * @param panel the panel to assign the given layout manager
         * @return the new layout manager
         */
        private LayoutManager getLayout(JPanel panel) {
            if(layout==GUIModel.Layout.FLOW)
                return new FlowLayout();
            else if(layout==GUIModel.Layout.HORIZONTAL)
                return new BoxLayout(panel,BoxLayout.X_AXIS);
            else if(layout==GUIModel.Layout.VERTICAL)
                return new BoxLayout(panel,BoxLayout.Y_AXIS);
            else
                throw new Error("Unknown layout: "+layout);
        }
        /**
         * For debugging
         */
        @Override public String toString() {
            StringBuffer result = new StringBuffer("JPanel[");
            for(QComponent c: contents) result.append(c.toString()+",");
            result.append("]");
            return result.toString();
        }
    } 
    /**
     * GUI metamodel: a simple, named element such as a button or label
     */
    protected static class QElement extends QComponent {
        /**
         * The kinds of simple elements supported
         */
        enum Kind { BUTTON, LABEL }
        /**
         * The element kind
         */
        private Kind kind;
        /**
         * The properties of this element
         */
        private List<Parameter> properties = new ArrayList<Parameter>();
        /**
         * Create a new element of the given kind with the given properties
         * @param kind the element kind
         * @param properties the element properties
         */
        QElement(Kind kind, Parameter[] properties) {
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
    
    /**
     * Builder class: provides convenience methods for constructing a GUI metamodel
     * @author ups
     */
    public static abstract class GUIModel {
        /**
         * The kinds of layout supported by the builder
         */
        public enum Layout { FLOW, VERTICAL, HORIZONTAL; }
        /**
         * The top-level frame
         */
        private QFrame top;
        /**
         * Create a frame with the given name and layout, containing the given nested model components,
         * and store it inside the builder
         * @param name title of the frame
         * @param layout layout used in the implicitly immediately nested panel
         * @param components the GUI components nested inside
         * @return the frame model
         */
        public QFrame frame(String name, Layout layout, QComponent ... components) {
            QPanel p = panel(layout,components);
            QFrame frame = new QFrame(name);
            frame.add(p);
            top = frame;
            return frame; 
        }
        /**
         * Crate a panel with the given layout and the given nested components
         * @param layout the layout used in the panel
         * @param components the model GUI components nested inside
         * @return the panel model
         */
        public QPanel panel(Layout layout, QComponent ... components) {
            QPanel panel = new QPanel(layout);
            for(QComponent c: components) panel.add(c);
            return panel; 
        }
        /**
         * Create a button with the given properties
         * @param spec the properties specifying the button
         * @return the button model
         */
        public QElement button(Parameter ... spec) { 
            return new QElement(QElement.Kind.BUTTON,spec);
        }
        /**
         * Create a label with the given properties
         * @param spec the properties specifying the label
         * @return the label model
         */
        public QElement label(Parameter ... spec) { 
            return new QElement(QElement.Kind.LABEL,spec);
        }
        /**
         * Create a text property, typically visible in the GUI
         * @param text the property value
         * @return the property model
         */
        public Parameter text(String text) { return new Parameter(Parameter.Kind.TEXT,text); }
        /**
         * Create a name property, used internally to identify GUI elements
         * @param name the property name
         * @return the property model
         */
        public Parameter name(String name) { return new Parameter(Parameter.Kind.NAME,name); }
        /**
         * Get the top-level frame
         * @return the top-level frame model
         */
        public QFrame getTop() { build(); return (QFrame)top; }
        /**
         * Build a gui model, must have one and only one top-level frame
         */
        public abstract void build();
    }

    /**
     * Map from names to corresponding Swing components
     */
    private Map<String,JComponent> componentMap = new HashMap<String,JComponent>();
    
    /**
     * Instantiate a Swing GUI based on the given model
     * @param model the model to instantiate from
     * @param handler the handler to use when handling GUI events
     */
    public QuickGUI(final GUIModel model, final ActionListener handler) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                buildGUI(model.getTop(),handler);
            }
        }); 
    }

    /**
     * Does the actual task of building the GUI for the given model and handler 
     * @param model to build the GUI from
     * @param handler for handling events
     */
    private void buildGUI(QFrame model, ActionListener handler) {
        // Create and set up the window.
        JFrame frame = new JFrame(model.name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Content must be a panel
        QPanel panel = (QPanel)model.contents.get(0);
        JPanel gui = panel.create(handler,componentMap);
        //Create and set up the content pane.
        frame.setContentPane(gui);
        gui.setOpaque(true);
         //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    /**
     * Get the component with a given name
     * @param name the name of the component to find
     * @return the component with matching name, if any, null otherwise
     */
    public JComponent getComponent(String name) {
        return componentMap.get(name);
    }
}
