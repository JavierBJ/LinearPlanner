package strips;

import java.util.ArrayList;
import java.util.List;

public class LinearPlannerBuilder {
	
	private List<Predicate> availablePredicates;
	private List<Operator> availableOperators;
	private State initialState;
	private State finalState;
	
	public LinearPlannerBuilder() {
		availablePredicates = new ArrayList<Predicate>();
		availableOperators = new ArrayList<Operator>();
	}
	
	public void addPredicates(List<Predicate> availablePredicates) {
		this.availablePredicates.addAll(availablePredicates);
	}
	
	public void addOperators(List<Operator> availableOperators) {
		this.availableOperators.addAll(availableOperators);
	}
	
	public void setInitialState(State state) {
		initialState = state;
	}
	
	public void setFinalState(State state) {
		finalState = state;
	}
	
	public LinearPlanner build() {
		return new LinearPlanner(availablePredicates, availableOperators,
				initialState, finalState);
	}
	
}
