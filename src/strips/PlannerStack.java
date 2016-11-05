package strips;

import java.util.Collections;
import java.util.Stack;

/**
 * 
 * The PlannerStack is the data structure used by the
 * planning algorithm to solve a problem. It consists in
 * a stack in which predicates, operators and sets of predicates
 * can be stacked.
 * 
 * The PlannerStack manages a dictionary of all parameters contained
 * in the stack, so all the elements stacked are aware of parameters
 * being instantiated.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class PlannerStack {
	
	/* The stack of goals used by the planner */
	private Stack<Stackable> stack;

	/**
	 * Creates a PlannerStack.
	 */
	public PlannerStack() {
		stack = new Stack<>();
	}
	
	/**
	 * Stacks an element and checks if there are new parameters
	 * that have not appeared yet to the stack, in order to
	 * register them.
	 */
	public void push(Stackable elem) {
		stack.push(elem);
	}
	
	/**
	 * Returns the element at the top of the stack and removes
	 * it from the stack.
	 */
	public Stackable pop() {
		return stack.pop();
	}
	
	/**
	 * Checks whether the stack is empty or not.
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/**
	 * Adds a new entry to the mappings that translate uninstantiated
	 * variables to its instantiated version.
	 */
	public void translateParameters(String value, String translation) {
		for (int i=0; i<stack.size(); i++) {
			Stackable elem = stack.get(i);
			if (elem instanceof SingleStackable) {
				translate(value, translation, (SingleStackable) elem);
			} else if (elem instanceof PredicateSet) {
				PredicateSet set = (PredicateSet) elem;
				for (Predicate pred : set.getPredicates()) {
					translate(value, translation, pred);
				}
			}
		}
	}
	
	/**
	 * Execute the parameters translation of a single stackable element, that is,
	 * a predicate or an operator.
	 */
	private void translate(String value, String translation, SingleStackable elem) {
		/* Translates parameters of the element itself */
		boolean changed = false;
		for (int j=0; !changed && j<elem.getParams().size(); j++) {
			String oldValue = elem.getParams().get(j).getValue();
			if (value.equals(oldValue)) {
				elem.getParams().get(j).setValue(translation);
			}
		}
		
		/* If it is an operator, also the tables need translation */
		if (elem instanceof Operator) {
			Operator op = (Operator) elem;
			
			/* Translates predicates from preconditions, adds and deletes tables */
			for (Predicate pred : op.getPreconditions()) {
				translate(value, translation, pred);
			}
			for (Predicate pred : op.getAdds()) {
				translate(value, translation, pred);
			}
			for (Predicate pred : op.getDeletes()) {
				translate(value, translation, pred);
			}
		}
	}
	
	
	/* Getters and setters */
	
	public Stack<Stackable> getStack() {
		return stack;
	}

	public void setStack(Stack<Stackable> stack) {
		this.stack = stack;
	}
	
	public String toString() {
		String s =  "";
		for (int i=stack.size()-1; i>=0; i--) {
			String text = stack.get(i).toString();
			int textLength = text.length();
			s += "|" + String.join("", Collections.nCopies(78, " ")) + "|\n";
			
			/* 
			 * Separates the full text of the element in parts of maximum of 78 characters,
			 * and writes every part in a different line.
			 */
			int pos = 0;
			while (pos < textLength) {
				String part = text.substring(pos, Math.min(pos+76, textLength));
				s += "| " + part;
				/* Adds as many spaces as necessary to fulfill 80 characters in the whole line */
				s += String.join("", Collections.nCopies(77-part.length(), " ")) + "|\n";
				pos += 76;
			}
			s += "|" + String.join("", Collections.nCopies(78, " ")) + "|\n";
			s += "|" + String.join("", Collections.nCopies(78, "-")) + "|\n";
		}
		return s;
		
	}
	
}
