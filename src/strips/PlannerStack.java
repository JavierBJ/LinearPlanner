package strips;

import java.util.HashMap;
import java.util.Stack;

public class PlannerStack {
	
	private Stack<Stackable> stack;
	
	private HashMap<Integer, Integer> mappings;

	public PlannerStack() {
		stack = new Stack<>();
		mappings = new HashMap<>();
	}
	
	public void push(Stackable elem) {
		stack.push(elem);
		addParameters(elem);
	}
	
	public Stackable pop() {
		return stack.pop();
	}
	
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	
	public Stack<Stackable> getStack() {
		return stack;
	}

	public void setStack(Stack<Stackable> stack) {
		this.stack = stack;
	}

	public HashMap<Integer, Integer> getMappings() {
		return mappings;
	}

	public void setMappings(HashMap<Integer, Integer> mappings) {
		this.mappings = mappings;
	}
	
	private void addParameters(Stackable elem) {
		if (elem instanceof Predicate) {
			Predicate pred = (Predicate) elem;
			addPredicateParams(pred);
		} else if (elem instanceof Operator) {
			Operator op = (Operator) elem;
			addOperatorParams(op);
		} else if (elem instanceof PredicateSet) {
			PredicateSet set = (PredicateSet) elem;
			for (Predicate pred: set.getPredicates()) {
				addPredicateParams(pred);
			}
		}
	}
	
	private void addPredicateParams(Predicate pred) {
		for (Parameter p : pred.getParams()) {
			addParam(p);
		}
	}
	
	private void addOperatorParams(Operator op) {
		for (Parameter p : op.getParams()) {
			addParam(p);
		}
	}
	
	private void addParam(Parameter p) {
		if (!mappings.containsKey(p.getKey())) {
			mappings.put(p.getKey(), p.getValue());
		}
	}
	
	public boolean isInstantiated(Parameter p) {
		return mappings.get(p.getKey()) != null;
	}
	
}
