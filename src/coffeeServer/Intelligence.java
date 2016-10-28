package coffeeServer;

import java.util.List;

import strips.Operator;
import strips.Predicate;
import strips.State;

public interface Intelligence {

	
	public List<Predicate> orderFinalState(State state);
	
	public List<Predicate> orderPreconditions(Operator op, State currentState);
}
