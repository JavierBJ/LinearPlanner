package strips;

import java.util.List;

public class Operator implements Stackable {
	
	private String name;
	private List<Parameter> params;
	
	private List<Predicate> preconditions;
	private List<Predicate> adds;
	private List<Predicate> deletes;

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

	public List<Predicate> getPreconditions() {
		return preconditions;
	}

	public void setPreconditions(List<Predicate> preconditions) {
		this.preconditions = preconditions;
	}

	public List<Predicate> getAdds() {
		return adds;
	}

	public void setAdds(List<Predicate> adds) {
		this.adds = adds;
	}

	public List<Predicate> getDeletes() {
		return deletes;
	}

	public void setDeletes(List<Predicate> deletes) {
		this.deletes = deletes;
	}
	
}
