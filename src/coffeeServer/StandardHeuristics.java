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

public class StandardHeuristics implements Intelligence {
	
	@Override
	public List<Predicate> orderFinalState(State initialState, State finalState) {
		
		List<Predicate> l = new ArrayList<Predicate>();
		Map<Predicate, Integer> distances = new HashMap<Predicate, Integer>();
		for (int i=0; i<finalState.getPredicates().size(); i++) {
			Predicate pred = finalState.getPredicates().get(i);
			if (pred.getName().equals("Robot-location")) {
				l.add(pred);
			} else {
				int distance = Distance.manhattanDistance(l.get(0).getParams().get(0).getValue(), pred.getParams().get(0).getValue());
				distances.put(pred, distance);	
			}	
		}
		Map<Predicate, Integer> sortedDistances = sortByValue(distances);
		l.addAll(sortedDistances.keySet());
		return l;
	}
	
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
