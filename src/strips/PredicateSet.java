package strips;

import java.util.List;

public class PredicateSet implements Stackable {
	
	protected List<Predicate> predicates;

	public PredicateSet(List<Predicate> predicates) {
		this.predicates = predicates;
	}
	
	public List<Predicate> getPredicates() {
		return predicates;
	}

	public void setPredicates(List<Predicate> predicates) {
		this.predicates = predicates;
	}
	
	public void applyOperator(Operator op) {
		predicates.removeAll(op.getDeletes());
		predicates.addAll(op.getAdds());
	}
	
	public boolean contains(Predicate pred) {
		return predicates.contains(pred);
	}
	
}
