package strips;

/**
 * 
 * A Parameter object is a parameter in the predicates and operators 
 * of the problem. It is defined by a name, that helps the algorithm
 * distinguishing it from other parameters; and a value, which is a
 * text string.
 * 
 * A parameter may be instantiated (that is, it has a specific value)
 * or not. In the latter case, its value starts with a _ symbol. That
 * means that a parameter may not have real values starting with _.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class Parameter {
	
	private String name;
	private String value;

	/**
	 * Creates a Parameter with a name and value.
	 */
	public Parameter(String name, String value) {
		this.setName(name);
		this.setValue(value);
	}

	/**
	 * Checks whether a parameter has already been
	 * instantiated or not.
	 */
	public boolean isInstantiated() {
		/* Uninstantiated parameters start with _ */
		return !value.startsWith("_");
	}
	
	
	/* Getters and setters */
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public boolean equals(Parameter p) {
		return name.equals(p.getName()) && value.equals(p.getValue());
	}
	
	public String toString() {
		return value;
	}
	
}
