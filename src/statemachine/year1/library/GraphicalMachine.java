package statemachine.year1.library;

import java.awt.event.ActionListener;
import java.util.Observer;

import quickqui.QuickGUI;

public abstract class GraphicalMachine implements ActionListener, Observer {

    protected QuickGUI gui;
    protected Machine machine;

    public GraphicalMachine(QuickGUI.GUIModel model, Machine machine) {
        this.gui = new QuickGUI(model,this);
        this.machine = machine;
        this.machine.addObserver(this);
    }
    
}
