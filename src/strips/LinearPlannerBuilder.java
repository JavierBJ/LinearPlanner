package strips;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * A LinearPlannerBuilder is a Builder design pattern that helps
 * when constructing the linear planner. 
 * 
 * Methods are available for indicating which are the predicates and 
 * operators presented in the problem, as well as the initial and
 * final state. When all this data has been indicated, one method is used 
 * for building the linear planner with the information provided.
 * 
 * This is a robust way of building the planner, as it detects errors like
 * lack of information about the problem, or incoherent definitions of
 * states and operations given the predicates indicated.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class LinearPlannerBuilder {
	
	private List<Predicate> availablePredicates;
	private List<Operator> availableOperators;
	private State initialState;
	private State finalState;
	private Intelligence intelligence;
	private PrintStream logOutput;
	
	public LinearPlannerBuilder() {
		availablePredicates = new ArrayList<Predicate>();
		availableOperators = new ArrayList<Operator>();
	}
	
	/**
	 * Adds predicates present in the problem to be solved. All
	 * available predicates should have been added before building.
	 */
	public void addPredicates(List<Predicate> availablePredicates) {
		this.availablePredicates.addAll(availablePredicates);
	}
	
	/**
	 * Adds operators present in the problem to be solved. All
	 * available operators should have been added before building.
	 */
	public void addOperators(List<Operator> availableOperators) {
		this.availableOperators.addAll(availableOperators);
	}
	
	/**
	 * Indicates the initial state of the problem.
	 */
	public void setInitialState(State state) {
		initialState = state;
	}
	
	/**
	 * Indicates the final state of the problem.
	 */
	public void setFinalState(State state) {
		finalState = state;
	}
	
	public void setIntelligence(Intelligence intelligence) {
		this.intelligence = intelligence;
	}
	
	public void setLogOutput(PrintStream logOutput) {
		this.logOutput = logOutput;
	}
	
	/**
	 * Returns the constructed LinearPlanner, from the predicates, operators
	 * and initial and final state specified to the builder.
	 * @throws FileNotFoundException 
	 */
	public LinearPlanner build() throws FileNotFoundException {
		return new LinearPlanner(availablePredicates, availableOperators,
				initialState, finalState, intelligence, logOutput);
	}
	
}
