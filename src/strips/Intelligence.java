package strips;

import java.util.List;

public interface Intelligence {

	
	public List<Predicate> orderFinalState(State initialState, State finalState);
	
	public List<Predicate> orderPreconditions(Operator op);

}
