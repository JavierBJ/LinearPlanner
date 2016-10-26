package strips;

import java.util.List;

/**
 * 
 * A State defines a state of the problem being solved
 * by the linear planner. A state is, in fact, a set of
 * predicates that are true for that set.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class State extends PredicateSet {

	/**
	 * Creates a state from a list of predicates.
	 */
	public State(List<Predicate> predicates) {
		super(predicates);
	}
	
	/**
	 * Changes this state by applying one operator. That
	 * is, the predicates from the "delete" list of the operator
	 * are removed from the state, and the predicates from the
	 * "add" list are added to the state.
	 */
	public void applyOperator(Operator op) {
		op.apply(this);
	}
	
}
