package strips;

import java.util.List;

/**
 * 
 * The intelligence is the part of the planner that contains the heuristics
 * created to apply domain knowledge to the solution. This planner allows
 * domain knowledge to be introduced when ordering the predicates of the final
 * state in the stack; and when stacking preconditions of an operator.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public interface Intelligence {

	
	public List<Predicate> orderFinalState(State initialState, State finalState);
	
	public List<Predicate> orderPreconditions(Operator op);

}
