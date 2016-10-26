package coffeeServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import strips.Operator;
import strips.Predicate;
import strips.State;

public class StandardHeuristics implements Intelligence {
	
	@Override
	public List<Predicate> orderFinalState(State state) {
		
		List<Predicate> l = new ArrayList<Predicate>();
		Map<Predicate, Integer> distances = new HashMap<Predicate, Integer>();
		for (int i=0; i<state.getPredicates().size(); i++) {
			Predicate pred = state.getPredicates().get(i);
			if (pred.getName().equals("Robot-location")) {
				l.add(pred);
			} else{
				int distance = 0;
				distances.put(pred, distance );	
			}	
		}
		Map<Predicate, Integer> sortedDistances = sortByValue(distances);
		l.addAll(sortedDistances.keySet());
		return l;
	}

	@Override
	public List<Predicate> orderPreconditions(Operator op, State currentState) {
		List<Predicate> l = new ArrayList<Predicate>();
		if (op.getName().equals("Serve")){
			l.add(op.getPreconditions().get(0));
			l.add(op.getPreconditions().get(1));
			l.add(op.getPreconditions().get(2));
			
		} else if (op.getName().equals("Make")){
			
			
			l.add(op.getPreconditions().get(0));
			
			l.add(op.getPreconditions().get(2));
			l.add(op.getPreconditions().get(1));
		}
		// TODO Auto-generated method stub
		return null;
	}
	
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}

}
