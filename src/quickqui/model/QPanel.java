package quickqui.model;

import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import quickqui.QuickGUI;
import quickqui.QuickGUI.GUIModel;
import quickqui.QuickGUI.GUIModel.Layout;

/**
 * GUI metamodel: a nested panel organizing a number of nested components
 */
public class QPanel extends QComponent {
    /**
     * The layout of the panel
     */
    private GUIModel.Layout layout;
    /**
     * Create a panel model with the given layout
     * @param layout the layout of the panel
     */
    public QPanel(GUIModel.Layout layout) { this.layout = layout; }
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