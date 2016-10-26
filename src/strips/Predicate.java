package strips;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * A Predicate defines a predicate in the problem being solved
 * by the linear planner. It is identified by a name, and it
 * may have 0 or more parameters, which may be instantiated or not.
 * 
 * Predicates can be pushed into the stack of goals used by the
 * planning algorithm.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class Predicate extends SingleStackable {

	/**
	 * Creates a predicate from its name and parameters.
	 */
	public Predicate(String name, Parameter... params) {
		super.setName(name);
		super.setParams(Arrays.asList(params));
	}
	
	/**
	 * Creates a predicate from its name and a list of parameters.
	 */
	public Predicate(String name, List<Parameter> params) {
		super.setName(name);
		super.setParams(params);
	}
	
	public String toString() {
		String s = getName() + "(";
		for (int i=0; i<getParams().size(); i++) {
			s += getParams().get(i).toString();
			s += (i < getParams().size()-1) ? ", " : ")";
		}
		return s;
	}
	
}
