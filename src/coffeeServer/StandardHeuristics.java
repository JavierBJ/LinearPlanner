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
			} else {
				int distance = Distance.manhattanDistance(l.get(0).getParams().get(0).getValue(), pred.getParams().get(0).getValue());
				distances.put(pred, distance);	
			}	
		}
		Map<Predicate, Integer> sortedDistances = sortByValue(distances);
		l.addAll(sortedDistances.keySet());
		return l;
	}

	@Override
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
	
	
	public List<Predicate> getMachine(Operator op, State currentstate) {
		String location = "";
		for (int i=0; i<currentstate.getPredicates().size(); i++) {
			if (currentstate.getPredicates().get(i).getName().equals("Robot-location")){
				location = currentstate.getPredicates().get(i).getParams().get(0).getValue();
			}
		}
		
		List<Predicate> l = new ArrayList<Predicate>();
		Map<Predicate, Integer> distances = new HashMap<Predicate, Integer>();
		for (int i=0; i<currentstate.getPredicates().size(); i++) {
			Predicate pred = currentstate.getPredicates().get(i);
			int distance = Distance.manhattanDistance(l.get(0).getParams().get(0).getValue(), pred.getParams().get(0).getValue());
			distances.put(pred, distance );	
		}	
		Map<Predicate, Integer> sortedDistances = sortByValue(distances);
		l.addAll(sortedDistances.keySet());
		
		
		return l;
	}
	
	public List<Predicate> orderFinalState2(State initialState, State state) {
		
		/*inicializacion*/
		
		List<Predicate> l = new ArrayList<Predicate>();
		Map<Predicate, Integer> distancesToLast = new HashMap<Predicate, Integer>();
		Map<Predicate, Integer> distancesToFirst =new HashMap<Predicate, Integer>();
		Predicate initialPosition = initialState.getPredicates().get(0);
		
		/*nos quedamos con lo que no sea posicion, y lo diferenciamos entre distancia a la posicion final y de inicio*/
		
		for (int j=0; j<initialState.getPredicates().size(); j++) {
			if (initialState.getPredicates().get(j).getName().equals("Robot-location")){
			initialPosition = initialState.getPredicates().get(j);
			}
		}
		for (int i=0; i<state.getPredicates().size(); i++) {
			Predicate pred = state.getPredicates().get(i);
			if (pred.getName().equals("Robot-location")) {
				l.add(pred);
			} else {
				int distanceToFirst = Distance.manhattanDistance(l.get(0).getParams().get(0).getValue(), pred.getParams().get(0).getValue());
				int distanceToLast = Distance.manhattanDistance(l.get(0).getParams().get(0).getValue(), initialPosition.getParams().get(0).getValue());
				distancesToLast.put(pred, distanceToLast);	
				distancesToFirst.put(pred, distanceToFirst);
			}	
		}
		/*ordenamos los diccionarios para saber los mas cercanos al final y al principio*/
		Map<Predicate, Integer> sortedDistancesToLast = sortByValue(distancesToLast);
		Map<Predicate, Integer> sortedDistancesToFirst = sortByValue(distancesToFirst);
		
		/*creamos listas para manejar listas en vez de diccionarios */
		List<Predicate> forFirst = new ArrayList(sortedDistancesToFirst.keySet());
		List<Predicate> forLast =new ArrayList(sortedDistancesToLast.keySet());

		
		/* creamos aux para meter los predicados mas cercanos a la posicion final */
		List<Predicate> aux = new ArrayList<Predicate>();
		
		/* recorremos los predicados tomando primero uno mas cercano al principio, y luego uno mas cercano al final*/
		while(!forFirst.isEmpty() || !forLast.isEmpty()){
			Predicate first = forFirst.get(0);
			l.add(first);
			forFirst.remove(first);
			forLast.remove(first);
			if(!forLast.isEmpty()){
				Predicate last = forLast.get(0);
				aux.add(last);
				forFirst.remove(first);
				forLast.remove(first);		
			}
			
		}
		
		/* Añadimos los predicados de aux en orden invrso, para que el ultimo este msa cerca del final*/
		for(int i=0; i<aux.size(); i++){
			l.add(aux.get(aux.size()-i));
		}
		
		return l;
	}
	
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
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
