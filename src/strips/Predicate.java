package strips;

import java.util.List;

public class Predicate implements Stackable {
	
	private String name;
	private List<Parameter> params;

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
