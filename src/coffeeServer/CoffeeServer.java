package coffeeServer;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import strips.*;

public class CoffeeServer {
	
	public static void main(String[] args) {
		LinearPlannerBuilder builder = new LinearPlannerBuilder();
		List<Predicate> availablePredicates = new ArrayList<>();
		List<Operator> availableOperators = new ArrayList<>();
		
		Parameter paramO = new Parameter("o", "_o");
		Parameter paramO1 = new Parameter("o", "_o1");
		Parameter paramO2 = new Parameter("o", "_o2");
		Parameter paramN = new Parameter("n", "_n");
		//Parameter paramX = new Parameter("x", "_x");
		
		Predicate robotLocation = new Predicate("Robot-location", paramO);
		Predicate robotLocationO1 = new Predicate("Robot-location", paramO1);
		Predicate robotLocationO2 = new Predicate("Robot-location", paramO2);
		Predicate robotFree = new Predicate("Robot-free");
		Predicate robotLoaded = new Predicate("Robot-loaded", paramN);
		Predicate petition = new Predicate("Petition", paramO, paramN);
		Predicate served = new Predicate("Served", paramO);
		Predicate machine = new Predicate("Machine", paramO, paramN);
		//Predicate steps = new Predicate("Steps", paramX);
		availablePredicates.addAll(Arrays.asList(robotLocation, robotFree,
				robotLoaded, petition, served, machine/*, steps*/));
		
		Operator make = new Operator("Make", Arrays.asList(robotLocation, robotFree, machine),
				Arrays.asList(robotLoaded), Arrays.asList(robotFree), paramO, paramN);
		Operator move = new MoveOperator(Arrays.asList(robotLocationO1/*, steps*/), 
				Arrays.asList(robotLocationO2/*, steps*/), Arrays.asList(robotLocationO1/*, steps*/), paramO1, paramO2);
		Operator serve = new Operator("Serve", Arrays.asList(robotLocation, robotLoaded, petition),
				Arrays.asList(served, robotFree), Arrays.asList(petition, robotLoaded), paramO, paramN);
		availableOperators.addAll(Arrays.asList(make, move, serve));
		
		List<Predicate> initialPredicates = new ArrayList<Predicate>();
		initialPredicates.add(new Predicate("Robot-free"));
		initialPredicates.add(new Predicate("Robot-location", new Parameter("o", "o1")));
		initialPredicates.add(new Predicate("Machine", new Parameter("o", "o4"), new Parameter("n", "3")));
		initialPredicates.add(new Predicate("Petition", new Parameter("o", "o11"), new Parameter("n", "3")));
		
		List<Predicate> finalPredicates = new ArrayList<Predicate>();
		finalPredicates.add(new Predicate("Robot-location", new Parameter("o", "o7")));
		finalPredicates.add(new Predicate("Served", new Parameter("o", "o11")));
		
		State initialState = new State(initialPredicates);
		State goalState = new State(finalPredicates);
		
		builder.addPredicates(availablePredicates);
		builder.addOperators(availableOperators);
		builder.setInitialState(initialState);
		builder.setFinalState(goalState);
		builder.setLogOutput(System.out);

		try {
			LinearPlanner planner = builder.build();
			List<Operator> plan = planner.executePlan();
			for (Operator op : plan) {
				System.out.println(op.getName());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
