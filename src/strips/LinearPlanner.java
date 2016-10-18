package strips;

import java.util.ArrayList;
import java.util.List;

public class LinearPlanner {
	
	private List<Predicate> availablePredicates;
	private List<Operator> availableOperators;
	private List<Operator> plan;
	private State currentState;
	private State finalState;
	private PlannerStack stack;
	
	public LinearPlanner(List<Predicate> availablePredicates, List<Operator> availableOperators,
			State currentState, State finalState) {
		this.availablePredicates = availablePredicates;
		this.availableOperators = availableOperators;
		plan = new ArrayList<Operator>();
		this.currentState = currentState;
		this.finalState = finalState;
		stack = new PlannerStack();
	}
	
	public List<Operator> executePlan() {
		stack.push(finalState);
		for (Predicate p : finalState.getPredicates()) {
			stack.push(p);
		}
		
		while(!stack.isEmpty()) {
			Stackable elem = stack.pop();
			
			if (elem instanceof Operator) {
				Operator op = (Operator) elem;
				currentState.applyOperator(op);
				plan.add(op);
			} else if (elem instanceof PredicateSet) {
				// TODO: check if a State elem holds true for this condition
				PredicateSet set = (PredicateSet) elem;
				for (Predicate p : set.getPredicates()) {
					if (!currentState.contains(p)) {
						stack.push(p);
					}
				}
			} else if (elem instanceof Predicate) {
				Predicate pred = (Predicate) elem;
				
				/* Checks if every parameter is instantiated */
				boolean allInstantiated = true;
				for (Parameter p : pred.getParams()) {
					if (!stack.isInstantiated(p)) {
						allInstantiated &= instantiate(p);
					}
				}
				
				/* Checks if the predicate is totally instantiated */
				if (!allInstantiated) {
					Operator selectedOp = selectOperator(pred);
					// TODO: make sure if "instantiate operator" is needed
					stack.push(selectedOp);
					stack.push(new PredicateSet(selectedOp.getPreconditions()));
					for (Predicate onePred : selectedOp.getPreconditions()) {
						stack.push(onePred);
					}
				}
			}
		}
		
		return plan;
	}
	
	private Operator selectOperator(Predicate pred) {
		boolean found = false;
		int i;
		for (i=0; !found && i<availableOperators.size(); i++) {
			List<Predicate> preds = availableOperators.get(i).getAdds();
			for (int j=0; !found && j<preds.size(); j++) {
				if (preds.get(j).getName().equals(pred.getName())) {
					found = true;
				}
			}
		}
		
		// XXX: only works if problem is well defined. Add robust?
		return availableOperators.get(i-1);
	}
	
	private boolean instantiate(Parameter p) {
		// TODO: implement it
		return false;
	}
	
	
	public List<Predicate> getAvailablePredicates() {
		return availablePredicates;
	}

	public void setAvailablePredicates(List<Predicate> availablePredicates) {
		this.availablePredicates = availablePredicates;
	}

	public List<Operator> getAvailableOperators() {
		return availableOperators;
	}

	public void setAvailableOperators(List<Operator> availableOperators) {
		this.availableOperators = availableOperators;
	}
	
	public List<Operator> getPlan() {
		return plan;
	}
	
	public void setPlan(List<Operator> plan) {
		this.plan = plan;
	}
	
	public State getCurrentState() {
		return currentState;
	}
	
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
	
	public State getFinalState() {
		return finalState;
	}
	
	public void setFinalState(State finalState) {
		this.finalState = finalState;
	}
	
	public PlannerStack getStack() {
		return stack;
	}
	
	public void setStack(PlannerStack stack) {
		this.stack = stack;
	}
	
}
