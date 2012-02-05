package statemachine.year1.cookinghood;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import quickqui.QuickGUI;
import statemachine.year1.library.Event;

public class CookingHood implements ActionListener, Observer {

    private static String POWER_ON_COMMAND = "__ON__";
    
   public static class ControlGUI extends QuickGUI.GUIModel {
        
        @Override 
        public void build() {
            frame("Cooking hood",Layout.VERTICAL,
                panel(Layout.HORIZONTAL,
                  label(text("Current state: ")),
                  label(name("state"),text("?"))),
                panel(Layout.HORIZONTAL,
                  label(text("Current motor power: ")),
                  label(name("power"),text("?"))),
                panel(Layout.HORIZONTAL,
                  label(text("Controls: ")),
                  button(name("PLUS"),text("(+)")),
                  button(name("MINUS"),text("(-)"))
                ),
                button(name(POWER_ON_COMMAND),text("Power on machine"))
              )
            ;
        }
    }


   private QuickGUI gui;
   private CookingHoodMachine machine;

    /**
     * Create GUI and then activate robot server functionality
     */
    public static void main(String argv[]) {
        CookingHood self = new CookingHood();
        self.gui = new QuickGUI(new ControlGUI(),self);
        self.machine = new CookingHoodMachine();
        self.machine.addObserver(self);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals(POWER_ON_COMMAND))
            machine.initialize();
        else {
            Event event = new Event(e.getActionCommand());
            machine.processEvent(event);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        if(!(o==machine)) throw new Error("Inconsistent observer notification");
        ((JLabel)gui.getComponent("state")).setText(machine.getState().toString());
        ((JLabel)gui.getComponent("power")).setText(new Integer(machine.getPower()).toString());
    }

}
