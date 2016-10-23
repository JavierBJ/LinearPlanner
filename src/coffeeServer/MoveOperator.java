package coffeeServer;

import java.util.List;

import strips.Operator;
import strips.Parameter;
import strips.Predicate;

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
}
