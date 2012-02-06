/*
Copyright (c) 2012, Ulrik Pagh Schultz, University of Southern Denmark
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

The views and conclusions contained in the software and documentation are those
of the authors and should not be interpreted as representing official policies, 
either expressed or implied, of the University of Southern Denmark.
*/

package statemachine.year3.dsl;

import java.util.ArrayList;
import java.util.List;

import statemachine.year2.framework.Machine;
import statemachine.year2.framework.State;
import statemachine.year2.framework.Transition;

/**
 * Abstract class providing a fluent interface for defining a state machine using
 * the generic statemachine framework (year2).  Acts as a builder.
 * @author ups
 */
public abstract class FluentMachine extends Machine {

    // Enums defining the types of effects and conditions that can be used
    private enum Effect { SET, CHANGE }
    private enum Condition { EQUAL, GREATER }
    
    // The complete list of all states (first is assumed to be initial)
    private List<State> allStates = new ArrayList<State>();
    // The current state being built
    private State currentState;
    // The current event that transitions are being defined for
    private String pendingEvent;
    // The target of the current transition
    private String targetTransition;
    // The effect of the current transition, if any
    private Effect effectMaybe;
    // The constant argument to the effect on the current transition, if any
    private int effectArgument;
    // The mutable state acted upon by the effet on the current transition, if any
    private IntegerState effectVariable;
    
    /**
     * Build a machine using the fluent interface.  First call build (overridden in subclass),
     * then ensure that all transitions associated with the current state have been defined,
     * and last add the state as the last in the list of states
     */
    public FluentMachine() {
        build();
        flushTransition(null,null,0);
        allStates.add(currentState);
    }
    
    @Override
    protected List<State> getAllStates() {
        return allStates;
    }

    /**
     * Override in subclasses, must define state machine using fluent interface (and initialize state variables)
     */
    protected abstract void build();
    
    /**
     * Start a new state, of the given name
     */
    public FluentMachine state(String name) {
        if(currentState!=null) {
            flushTransition(null,null,0);
            allStates.add(currentState);
        }
        currentState = new State(this,name);
        return this;
    }
    
    /**
     * Define a new transition, in the context of the current state
     */
    public FluentMachine transition(String event) {
        if(targetTransition!=null || effectMaybe!=null) flushTransition(null,null,0);
        pendingEvent = event;
        return this;
    }
    
    /**
     * Name the target state of the current transition
     */
    public FluentMachine to(String state) {
        targetTransition = state;
        return this;
        
    }

    /**
     * Include a set state effect in the current transition
     * @param variable the variable to set
     * @param value the value to set it to
     */
    public FluentMachine setState(IntegerState variable, int value) {
        effectMaybe = Effect.SET;
        effectVariable = variable;
        effectArgument = value;
        return this;
    }

    /**
     * Include a change state effect in the current transition
     * @param variable the variable to change
     * @param value the value to add to the variable
     */
    public FluentMachine changeState(IntegerState variable, int value) {
        effectMaybe = Effect.CHANGE;
        effectVariable = variable;
        effectArgument = value;
        return this;
    }
    
    /**
     * Add an equals condition to the current transition
     * @param variable the variable to have a condition on
     * @param value the value to compare to
     */
    public FluentMachine whenStateEquals(IntegerState variable, int value) {
        flushTransition(Condition.EQUAL,variable,value);
        return this;
    }
    
    /**
     * Add a comparison condition to the current transition
     * @param variable the variable to have a condition on
     * @param value the value to compare to
     */
    public FluentMachine whenStateGreaterThan(IntegerState variable, int value) {
        flushTransition(Condition.GREATER,variable,value);
        return this;
    }
    
    /**
     * Indicate that a state is a simple alternative (syntactic sugar)
     */
    public FluentMachine otherwise() {
        flushTransition(null,null,0);
        return this;
    }

    /**
     * Flush the current transition, in preparation for the start of a new transition or state
     * @param cond the condition type, if any, on the transition being finalized
     * @param condVariableMaybe the variable to test on, if any
     * @param value the value to compare to, if any
     */
    private void flushTransition(Condition cond, IntegerState condVariableMaybe, int value) {
        if(pendingEvent==null) return; // Nothing to flush
        if(targetTransition==null && effectMaybe==null) return; // empty transition
        // Define transition and add to current state
        Transition transition = new GenericTransition(targetTransition,
                effectMaybe,effectVariable,effectArgument,
                cond, condVariableMaybe,value);
        currentState.addTransition(pendingEvent, transition);
        // Clear all context variables
        effectMaybe = null;
        effectVariable = null;
        targetTransition = null;
    }

    /**
     * Generic transition that performs its function depending on its
     * description of effects and conditions (passed as parameters, at
     * most one of each)
     * @author ups
     */
    private static class GenericTransition extends Transition {

        private Effect effectMaybe;
        private IntegerState condVariableMaybe;
        private IntegerState effectVariable;
        private int effectArgument;
        private int condValue;
        private Condition conditionMaybe;

        public GenericTransition(String target, 
                Effect effectMaybe, IntegerState effectVariable, int effectArgument, 
                Condition cond, IntegerState condVariableMaybe, int value) {
            super(target);
            this.effectMaybe = effectMaybe; this.effectVariable = effectVariable; this.effectArgument = effectArgument; 
            this.conditionMaybe = cond; this.condVariableMaybe = condVariableMaybe; this.condValue = value;
            if(effectMaybe!=null && effectVariable==null) throw new Error("Inconistent effect description");
        }
        
        @Override public boolean isApplicable() {
            if(conditionMaybe==null) return true; // no condition
            if(conditionMaybe==Condition.EQUAL) {
                return condVariableMaybe.value()==condValue;
            } else if(conditionMaybe==Condition.GREATER) {
                return condVariableMaybe.value()>condValue;
            } else 
                throw new Error("Illegal condition kind");
        }
        
        @Override public void effect() {
            if(effectMaybe==null) return; // no effect
            if(effectMaybe==Effect.SET)
                effectVariable.set(effectArgument);
            else if(effectMaybe==Effect.CHANGE)
                effectVariable.set(effectVariable.value()+effectArgument);
            else
                throw new Error("Uknown effect");
        }
        
        public String toString() {
            return "T("+super.getTarget()+"): "+effectMaybe + "@" + effectVariable + "," + effectArgument;
        }
    }
}
