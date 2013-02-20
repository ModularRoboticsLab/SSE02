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

import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import quickqui.model.Parameter;
import quickqui.model.QComponent;
import quickqui.model.QElement;
import quickqui.model.QFrame;
import quickqui.model.QPanel;

/**
 * QuickGUI allows concise description of a simple GUI (one frame, buttons, labels, organized into panels)
 * to be instantiated as a GUI that can be used from the application.
 * @author ups
 */
public class QuickGUI {

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
        JFrame frame = new JFrame(model.getName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Content must be a panel
        QPanel panel = (QPanel)model.getContents().get(0);
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
     * @return the component with matching name, if any, signalling an error otherwise
     */
    public JComponent getComponent(String name) {
        JComponent component = componentMap.get(name);
        if(component==null) throw new Error("Component not found: "+name);
        return component;
    }
}
