package statemachine.year1.cookinghood;

import javax.swing.JLabel;

import quickqui.QuickGUI;
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
        new CookingHood();
    }
    
    public CookingHood() {
        super(new ControlGUI(),new CookingHoodMachine(),POWER_ON_COMMAND);
    }

    @Override
    public void update() {
        ((JLabel)gui.getComponent("state")).setText(machine.getState().toString());
        ((JLabel)gui.getComponent("power")).setText(new Integer(((CookingHoodMachine)machine).getPower()).toString());
    }

}
