// Automatically generated code, do not edit
package statemachine.generated;
import java.util.Map;
import statemachine.year4.codegen.GeneratedMachine;
public class CookingHoodImpl extends GeneratedMachine {
  @Override protected void internalProcessEvent(int event) {
    switch(state) {
    case 0: // OFF
      switch(event) {
      case 0: // PLUS
        {
          power=1;
          state = 1; // ON
        }
      break;
      default: ; // ignore
      }
    break;
    case 1: // ON
      switch(event) {
      case 0: // PLUS
        if(power==6) {
          state = 2; // MAX
        }
      else
        {
          power+=1;
          state = 1; // ON
        }
      break;
      case 1: // MINUS
        if(power==1) {
          state = 0; // OFF
        }
      else
        {
          power+=-1;
          state = 1; // ON
        }
      break;
      default: ; // ignore
      }
    break;
    case 2: // MAX
      switch(event) {
      case 1: // MINUS
        {
          state = 1; // ON
        }
      break;
      default: ; // ignore
      }
    break;
    default: throw new Error("Internal error: unsupported state code");
    }
  }
  private int power;
  /** Get the value of the extended state power
    * @return value of power
  */
  public int get_power() { return power; }
  @Override protected void internalInitialize(Map<String, Integer> event_code2int, Map<Integer, String> state_int2code) {
    state_int2code.put(2,"MAX");
    state_int2code.put(1,"ON");
    state_int2code.put(0,"OFF");
    event_code2int.put("PLUS",0);
    event_code2int.put("MINUS",1);
  }
}
