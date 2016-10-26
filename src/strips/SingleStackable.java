package strips;

import java.util.List;

/**
 * 
 * A SingleStackable is any Stackable element that consists in
 * just one element and not a set of them. That is, predicates
 * and operators are SingleStackable, but sets of predicates
 * (including states) are not.
 * 
 * This helps treating the list of parameters of both predicates
 * and operators in the same way, and thus improving the
 * readability of the code.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public abstract class SingleStackable implements Stackable {
	
	private String name;
	private List<Parameter> params;
	
	
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
	
}
