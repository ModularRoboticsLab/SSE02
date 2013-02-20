// Automatically generated code, do not edit
package statemachine.year4.cookinghood;
import java.util.Map;
import statemachine.year4.codegen.GeneratedMachine;
public class CookingHood4Impl extends GeneratedMachine {
  @Override protected void internalProcessEvent(int event) {
    switch(state) {
    case 0: // POWER_OFF
      switch(event) {
      case 0: // PLUS
        {
          power=1;
          state = 1; // POWER_ON
        }
      break;
      default: ; // ignore
      }
    break;
    case 1: // POWER_ON
      switch(event) {
      case 0: // PLUS
        if(power==6) {
          state = 2; // MAX_POWER
        }
      else
        {
          power+=1;
        }
      break;
      case 1: // MINUS
        if(power==1) {
          state = 0; // POWER_OFF
        }
      else
        {
          power+=-1;
        }
      break;
      default: ; // ignore
      }
    break;
    case 2: // MAX_POWER
      switch(event) {
      case 1: // MINUS
        {
          power=6;
          state = 1; // POWER_ON
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
    state_int2code.put(1,"POWER_ON");
    state_int2code.put(0,"POWER_OFF");
    state_int2code.put(2,"MAX_POWER");
    event_code2int.put("PLUS",0);
    event_code2int.put("MINUS",1);
  }
}
