package strips;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A LinearPlanner receives a problem defined by a set of predicates,
 * operations and states, and executes the planning algorithm to find
 * a way to get from the initial state to the goal state.
 * 
 * The linear planner uses a stack of goals to select the sub-problems
 * to solve at each step. It also logs the state of the problem at every
 * step.
 * 
 * As the LinearPlanner is a complex object, it comes with a builder in
 * the LinearPlannerBuilder class. It helps constructing the linear planner
 * in an easy, appropriate way, preventing the user from some errors.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class LinearPlanner {
	
	/* Attributes needed for the log */
	private int step;
	private PrintStream logOutput;
	
	/* Attributes for the linear planner */
	private List<Predicate> availablePredicates;
	private List<Operator> availableOperators;
	private List<Operator> plan;
	private State currentState;
	private State finalState;
	private PlannerStack stack;
	
	/**
	 * Creates a linear planner that solves a problem defined by its predicates, its operators,
	 * and its initial and final state. It also specifies an path for the log to be output.
	 */
	public LinearPlanner(List<Predicate> availablePredicates, List<Operator> availableOperators,
			State currentState, State finalState, PrintStream logOutput) throws FileNotFoundException {
		step = 0;
		this.availablePredicates = availablePredicates;
		this.availableOperators = availableOperators;
		plan = new ArrayList<Operator>();
		this.currentState = currentState;
		this.finalState = finalState;
		stack = new PlannerStack();

		this.logOutput = logOutput;
	}
	
	/**
	 * Executes the STRIPS linear planning algorithm and returns the plan
	 * to be followed for getting to the final state from the initial state.
	 */
	public List<Operator> executePlan() {
		/* The stack is initialized with the predicates of the goal state */
		stack.push(finalState);
		for (Predicate p : finalState.getPredicates()) {
			stack.push(p);
		}
		
		/* 
		 * Every step of the algorithm, the state of the problem and of the
		 * stack is written, and the head element of the stack is unstacked. 
		 */
		while(!stack.isEmpty()) {
			step++;
			log();
			Stackable elem = stack.pop();
			
			if (elem instanceof Operator) {
				Operator op = (Operator) elem;
				
				/* Applies the unstacked operator and adds it to the plan */
				currentState.applyOperator(op);
				plan.add(op);
			} else if (elem instanceof PredicateSet) {
				PredicateSet set = (PredicateSet) elem;
				
				/* 
				 * Checks whether all the predicates of the set are true in
				 * the current state.
				 */
				for (Predicate p : set.getPredicates()) {
					if (!currentState.contains(p)) {
						/* Stacks predicates that are not true yet */
						stack.push(p);
					}
				}
			} else if (elem instanceof Predicate) {
				Predicate pred = (Predicate) elem;
				
				/* Instantiates the lacking parameters */
				for (Parameter p : pred.getParams()) {
					if (!stack.isInstantiated(p)) {
						instantiate(pred);
					}
				}
				
				/* 
				 * Once totally instantiated, checks if the parameter is true
				 * in the current state.
				 */
				if (!truePredicate(pred)) {
					/* Stacks the operator and preconditions needed */
					Operator selectedOp = selectOperator(pred);
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
	
	/**
	 * Returns true if pred is part of the current state, false otherwise.
	 */
	private boolean truePredicate(Predicate pred) {
		return currentState.contains(pred);
	}
	
	/**
	 * Gets an operator that adds the specified predicate when executed.
	 */
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
		/* Instantiates the operator with parameters of the predicate */
		return availableOperators.get(i-1).instantiate(pred);
	}
	
	/**
	 * Instantiates a predicate by looking at the current state. Those
	 * instances are translated to all the appearances of a parameter 
	 * in the stack.
	 */
	private void instantiate(Predicate pred) {
		for (int i=0; i<currentState.getPredicates().size(); i++) {
			Predicate currentPred = currentState.getPredicates().get(i);
			
			/* Looks for predicates with the same name in the current state */
			if (currentPred.getName().equals(pred.getName())) {
				for (int j=0; j<currentPred.getParams().size(); j++) {
					Parameter real = pred.getParams().get(j);
					Parameter expected = currentPred.getParams().get(j);
					
					/* 
					 * If a parameter is not instantiated (starts with _) and I
					 * found an instance in the state, translate it.
					 */
					if (real.getValue().startsWith("_") && 
							real.getName().equals(expected.getName())) {
						stack.translateParameters(real.getValue(), expected.getValue());
						pred.getParams().get(j).setValue(expected.getValue());
					}
				}
			}
		}
	}
	
	/**
	 * Writes logging information in the specified output. It shows the
	 * step of the problem, the current state and a representation 
	 * of the stack in that step.
	 */
	public void log() {
		logOutput.println("Step: " + step);
		logOutput.println("Current state: " + currentState.toString());
		logOutput.println("Stack:");
		logOutput.println(stack.toString());
		logOutput.println("-----");
	}
	
	
	/* Getters and setters */
	
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
