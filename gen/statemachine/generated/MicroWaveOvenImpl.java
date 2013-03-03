// Automatically generated code, do not edit
package statemachine.generated;
import java.util.Map;
import statemachine.year4.codegen.GeneratedMachine;
public class MicroWaveOvenImpl extends GeneratedMachine {
  @Override protected void internalProcessEvent(int event) {
    switch(state) {
    case 0: // INACTIVE
      switch(event) {
      case 0: // START
        {
          state = 1; // COOKING
        }
      break;
      default: ; // ignore
      }
    break;
    case 1: // COOKING
      switch(event) {
      case 1: // OPEN
        {
          state = 2; // DOOR_OPEN
        }
      break;
      case 2: // STOP
        {
          state = 0; // INACTIVE
        }
      break;
      case 3: // TIMER
        {
          state = 0; // INACTIVE
        }
      break;
      default: ; // ignore
      }
    break;
    case 2: // DOOR_OPEN
      switch(event) {
      case 2: // STOP
        {
          state = 0; // INACTIVE
        }
      break;
      case 4: // CLOSE
        {
          state = 1; // COOKING
        }
      break;
      default: ; // ignore
      }
    break;
    default: throw new Error("Internal error: unsupported state code");
    }
  }
  @Override protected void internalInitialize(Map<String, Integer> event_code2int, Map<Integer, String> state_int2code) {
    state_int2code.put(1,"COOKING");
    state_int2code.put(2,"DOOR_OPEN");
    state_int2code.put(0,"INACTIVE");
    event_code2int.put("OPEN",1);
    event_code2int.put("START",0);
    event_code2int.put("STOP",2);
    event_code2int.put("CLOSE",4);
    event_code2int.put("TIMER",3);
  }
}
