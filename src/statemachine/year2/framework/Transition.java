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

package statemachine.year2.framework;

/**
 * Generic template class for transitions.  Simple transitions can use this class directly,
 * conditions can be added by overriding the isApplicable method, effects can be added by
 * overriding the effect method.
 * @author ups
 */
public class Transition {

	/**
	 * The target state of the transition
	 */
    private String targetState;

    /**
     * Create a transition that targets the given state
     * @param targetState the target state
     */
    public Transition(String targetState) {
        this.targetState = targetState;
    }
    
    /**
     * The action of the state: perform effect (as defined by hook method), returning the target state
     * @return the target state
     */
    public String action() {
        effect();
        return targetState;
    }

    /**
     * Provide name of target state that should be transitioned to
     * @return name of the target state
     */
    public String getTarget() {
        return targetState;
    }

    /**
     * Hook method: override to provide effect 
     */
    protected void effect() { ; }

    /**
     * Hook method: override to provide condition
     * @return false if this transition should not take place now, true otherwise
     */
    protected boolean isApplicable() {
        return true;
    }

}
