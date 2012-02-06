package statemachine.year3.microwaveoven;

import statemachine.year3.dsl.FluentMachine;

public class MicrowaveMachine extends FluentMachine {

    @Override
    public void build() {
        state("INACTIVE").
          transition("START").to("COOKING").
        state("COOKING").
          transition("TIMER").to("INACTIVE").
          transition("STOP"). to("INACTIVE").
          transition("OPEN"). to("DOOR_OPEN").
        state("DOOR_OPEN").
          transition("CLOSE").to("COOKING").
          transition("STOP"). to("INACTIVE");
    }

}
