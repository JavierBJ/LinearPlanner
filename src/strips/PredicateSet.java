package strips;

import java.util.List;

/**
 * 
 * A PredicateSet defines set of predicates of the problem being solved
 * by the linear planner. It consists in 1 or more predicates.
 * 
 * Predicate sets can be pushed into the stack of goals used by the
 * planning algorithm.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class PredicateSet implements Stackable {
	
	protected List<Predicate> predicates;

	/**
	 * Creates a predicate set from a list of predicates.
	 */
	public PredicateSet(List<Predicate> predicates) {
		this.predicates = predicates;
	}
	
	/**
	 * Checks whether one predicate is part of the set.
	 */
	public boolean contains(Predicate pred) {
		boolean found = false;
		for (int i=0; !found && i<predicates.size(); i++) {
			/* It must have the same name as one of the set */
			if (pred.getName().equals(predicates.get(i).getName())) {
				found = true;
				for (int j=0; found && j<predicates.get(i).getParams().size(); j++) {
					/* All their parameters must be the same */
					found &= predicates.get(i).getParams().get(j).equals(pred.getParams().get(j));
				}
			}
		}
		return found;
	}
	
	/* Getters and setters */
	
	public List<Predicate> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<Predicate> predicates) {
		this.predicates = predicates;
	}
	
	public String toString() {
		String s = "[";
		
		for (int i=0; i<predicates.size(); i++) {
			s += predicates.get(i).toString();
			s += (i < predicates.size()-1) ? "; " : "]";
		}
		
		return s;
	}
	
}
