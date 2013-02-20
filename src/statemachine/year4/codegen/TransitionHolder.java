package statemachine.year4.codegen;

import statemachine.year2.framework.Transition;
import statemachine.year3.dsl.FluentMachine.Condition;
import statemachine.year3.dsl.FluentMachine.Effect;
import statemachine.year3.dsl.IntegerState;

/**
 * Data value object for transitions
 * @author ups
 *
 */
public class TransitionHolder extends Transition {
	private Effect effect;
	private IntegerState effectVar;
	private int effectArg;
	private Condition cond;
	private IntegerState condVariableMaybe;
	private int condValue;
	public TransitionHolder(String target, Effect effect,
			IntegerState effectVar, int effectArg, Condition cond,
			IntegerState condVariableMaybe, int condValue) {
		super(target);
		this.effect = effect;
		this.effectVar = effectVar;
		this.effectArg = effectArg;
		this.cond = cond;
		this.condVariableMaybe = condVariableMaybe;
		this.condValue = condValue;
	}
	public Effect getEffect() {
		return effect;
	}
	public IntegerState getEffectVar() {
		return effectVar;
	}
	public int getEffectArg() {
		return effectArg;
	}
	public Condition getCond() {
		return cond;
	}
	public IntegerState getCondVariableMaybe() {
		return condVariableMaybe;
	}
	public int getCondValue() {
		return condValue;
	}

}
