package coffeeServer;

import java.util.List;

import strips.Operator;
import strips.Predicate;
import strips.State;

public interface Intelligence2 {

	
	public List<Predicate> orderFinalState2(State initialState, State state);
	
	public List<Predicate> orderPreconditions(Operator op);
}
