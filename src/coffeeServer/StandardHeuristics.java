package coffeeServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import strips.Intelligence;
import strips.Operator;
import strips.Predicate;
import strips.State;

/**
 * 
 * This is the implementation of the heuristics for the problem. It contains
 * all the domain knowledge that may help improving the performance of the
 * linear planner. In particular, it implements two heuristics:
 * 
 * The first one decides how to order the predicates of the final state in
 * the stack, in order to be more efficient while serving the petitions.
 * 
 * The second one prevents loops by deciding a proper order of the preconditions
 * of any operator in the stack.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class StandardHeuristics implements Intelligence {
	
	/**
	 * Orders the Served predicates of the final state, so the petitions served first
	 * are the ones that are farther from the final robot location, and the ones served
	 * last are nearest to it.
	 */
	@Override
	public List<Predicate> orderFinalState(State initialState, State finalState) {
		List<Predicate> l = new ArrayList<Predicate>();
		Map<Predicate, Integer> distances = new HashMap<Predicate, Integer>();
		for (int i=0; i<finalState.getPredicates().size(); i++) {
			Predicate pred = finalState.getPredicates().get(i);
			if (pred.getName().equals("Robot-location")) {
				/* Retrieves the final robot location */
				l.add(pred);
			} else {
				/* Calculates the distance between the location and the petition */
				int distance = Distance.manhattanDistance(
						l.get(0).getParams().get(0).getValue(), 
						pred.getParams().get(0).getValue());
				distances.put(pred, distance);	
			}	
		}
		
		/* Sorts the petitions by distance */
		Map<Predicate, Integer> sortedDistances = sortByValue(distances);
		l.addAll(sortedDistances.keySet());
		return l;
	}
	
	/**
	 * Orders the preconditions of an operator in order to prevent
	 * unwanted situations and be more efficient when treating a petition.
	 */
	public List<Predicate> orderPreconditions(Operator op) {
		List<Predicate> l = new ArrayList<Predicate>();
		if (op.getName().equals("Serve")){
			l.add(op.getPreconditions().get(0));
			l.add(op.getPreconditions().get(1));
			l.add(op.getPreconditions().get(2));
			
		} else if (op.getName().equals("Make")){
			l.add(op.getPreconditions().get(0));
			l.add(op.getPreconditions().get(2));
			l.add(op.getPreconditions().get(1));
		} else if (op.getName().equals("Move")) {
			l.addAll(op.getPreconditions());
		}
		return l;
	}
	
	/**
	 * Sorts a map by value in ascending order.
	 */
	private <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}

}
