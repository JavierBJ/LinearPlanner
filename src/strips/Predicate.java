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
public class Predicate implements Stackable {
	
	private String name;
	private List<Parameter> params;

	/**
	 * Creates a predicate from its name and parameters.
	 */
	public Predicate(String name, Parameter... params) {
		this.name = name;
		this.params = Arrays.asList(params);
	}
	
	/**
	 * Creates a predicate from its name and a list of parameters.
	 */
	public Predicate(String name, List<Parameter> params) {
		this.name = name;
		this.params = params;
	}
	
	/* Getters and setters */
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public List<Parameter> getParams() {
		return params;
	}

	public void setParams(List<Parameter> params) {
		this.params = params;
	}
	
	public String toString() {
		String s = name + "(";
		for (int i=0; i<params.size(); i++) {
			s += params.get(i).toString();
			s += (i < params.size()-1) ? ", " : ")";
		}
		return s;
	}
	
}
