// Automatically generated code, do not edit
package statemachine.year4.cookinghood;
import java.util.Map;
import statemachine.year4.codegen.GeneratedMachine;
public class CookingHood4Impl extends GeneratedMachine {
  @Override protected void internalProcessEvent(int event) {
    switch(state) {
    case 0:
      switch(event) {
      case 0:
        {
          power=1;
          state = 1;
        }
      break;
      default: ; // ignore
      }
    break;
    case 1:
      switch(event) {
      case 0:
        if(power==6) {
          state = 2;
        }
      else
        {
          power+=1;
        }
      break;
      case 1:
        if(power==1) {
          state = 0;
        }
      else
        {
          power+=-1;
        }
      break;
      default: ; // ignore
      }
    break;
    case 2:
      switch(event) {
      case 1:
        {
          power=6;
          state = 1;
        }
      break;
      default: ; // ignore
      }
    break;
    default: throw new Error("Internal error: unsupported state code");
    }
  }
  private int power;
  public int get_power() { return power; }
  @Override protected void internalInitialize(Map<String, Integer> event_code2int, Map<Integer, String> state_int2code) {
    state_int2code.put(1,"POWER_ON");
    state_int2code.put(0,"POWER_OFF");
    state_int2code.put(2,"MAX_POWER");
    event_code2int.put("PLUS",0);
    event_code2int.put("MINUS",1);
  }
}
