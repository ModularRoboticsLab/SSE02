package statemachine.year1.cookinghood;

import java.awt.event.ActionEvent;
import java.util.Observable;
import javax.swing.JLabel;

import quickqui.QuickGUI;
import statemachine.year1.library.Event;
import statemachine.year1.library.GraphicalMachine;

public class CookingHood extends GraphicalMachine {

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

    /**
     * Create GUI and then activate robot server functionality
     */
    public static void main(String argv[]) {
        new CookingHood(new ControlGUI(),new CookingHoodMachine());
    }
    
    public CookingHood(ControlGUI controlGUI, CookingHoodMachine cookingHoodMachine) {
        super(controlGUI,cookingHoodMachine);
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
        ((JLabel)gui.getComponent("power")).setText(new Integer(((CookingHoodMachine)machine).getPower()).toString());
    }

}
