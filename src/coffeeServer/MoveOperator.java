package coffeeServer;

import java.util.List;

import strips.Operator;
import strips.Parameter;
import strips.Predicate;
import strips.State;

/**
 * 
 * A MoveOperator defines an Operator Move(o1, o2) for the coffee-server
 * problem. It means that the server is moved from position o1 to position o2.
 * It can only be instantiated from a Robot-location(o) predicate,
 * and will be instantiated in the following way:
 * 
 * Robot-location(o), Move(_o1, _o2) -> Move(_o1, o)
 * 
 * That is, the first parameter is still uninstantiated and will be revealed
 * as the problem solver progresses. The same rule is applied for the predicates
 * in the adds, deletes and preconditions.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class MoveOperator extends Operator {
	
	/**
	 * Creates a MoveOperator with some params and a list of preconditions,
	 * adds and deletes.
	 */
	public MoveOperator(List<Predicate> preconditions, 
			List<Predicate> adds, List<Predicate> deletes, Parameter... params) {
		super("Move", preconditions, adds, deletes, params);
	}
	
	/**
	 * Creates a copy of an existing MoveOperator.
	 */
	public MoveOperator(Operator op) {
		super(op);
	}
	
	/**
	 * Instantiates a Move(o1, o2) with a predicate Robot-location(o).
	 */
	public MoveOperator instantiate(Predicate pred) {
		MoveOperator copiedOp = new MoveOperator(this);
		/* Updates parameters of operator with the ones of predicate */
		copiedOp.setParams(updateList(pred.getParams(), copiedOp.getParams()));
		
		/* Updates parameters for the predicates in preconditions, adds and deletes */
		for (Predicate prec : copiedOp.getPreconditions()) {
			prec.setParams(updateList(pred.getParams(), prec.getParams()));
		}
		for (Predicate add : copiedOp.getAdds()) {
			add.setParams(updateList(pred.getParams(), add.getParams()));
		}
		for (Predicate del : copiedOp.getDeletes()) {
			del.setParams(updateList(pred.getParams(), del.getParams()));
		}
				
		return copiedOp;
	}
	
	/**
	 * Updates a list of parameters from an operator using the parameters of a
	 * predicate. It takes into account that only the second parameter of the
	 * Move operator should be instantiated.
	 */
	private List<Parameter> updateList(List<Parameter> copy, List<Parameter> paste) {
		for (int i=0; i<paste.size(); i++) {
			for (int j=0; j<copy.size(); j++) {
				if (paste.get(i).getValue().endsWith("2") && 
						paste.get(i).getName().equals(copy.get(j).getName())) {
					paste.get(i).setValue(copy.get(j).getValue());
				}
			}
		}
		return paste;
	}
	
	/**
	 * Applies the Move operator to the state, that is, it adds and deletes the
	 * predicates specified in the adds and deletes tables. Also, the steps(x)
	 * predicate is properly treated, so as to be added with the value of steps(x)
	 * plus the distance from o1 to o2, given Move(o1,o2).
	 */
	public void apply(State state) {
		/* Searches the Serve(x) predicate, retrieves x and updates it with the new steps */
		int x = 0;
		for (int i=0; i<state.getPredicates().size(); i++) {
			if (state.getPredicates().get(i).getName().equals("Steps")) {
				x = Integer.parseInt(state.getPredicates().get(i).getParams().get(0).getValue());
			}
		}
		x += manhattanDistance();
		
		/* Updates adds and deletes from the state */
		state.getPredicates().removeAll(this.getDeletes());
		state.getPredicates().addAll(this.getAdds());
		
		/* Instantiates Serve(x) as Serve(x + distance(o1,o2) */
		for (int i=0; i<state.getPredicates().size(); i++) {
			if (state.getPredicates().get(i).getName().equals("Steps")) {
				state.getPredicates().get(i).getParams().get(0).setValue(new Integer(x).toString());
			}
		}
	}

	/**
	 * Calculates the Manhattan Distance between two positions specified as the
	 * parameters of Move.
	 */
	private int manhattanDistance() {
		String o1 = this.getParams().get(0).getValue();
		String o2 = this.getParams().get(1).getValue();
		int x1 = toCoordsX(o1);
		int x2 = toCoordsX(o2);
		int y1 = toCoordsY(o1);
		int y2 = toCoordsY(o2);
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}

	/**
	 * Translates an Oi parameter to its X coordinate.
	 */
	private int toCoordsX(String o) {
		int num = Integer.parseInt(o.substring(1));
		return (num - 1) % 6;
	}
	
	/**
	 * Translates an Oi parameter to its Y coordinate.
	 */
	private int toCoordsY(String o) {
		int num = Integer.parseInt(o.substring(1));
		return (num - 1) / 6;
	}
}
