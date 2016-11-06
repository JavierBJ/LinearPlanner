package coffeeServer;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import strips.*;

/**
 * 
 * The Coffee Server problem is a specific planning problem that can be
 * solved with the linear planner created. It consists in a grid in which
 * a robot is located, as well as coffee machines and coffee petitions. The
 * robot has to serve all of them and end up in a specific position.
 * 
 * It contains a special predicate: the Steps(x), which indicates the number
 * of path found by the robot.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class CoffeeServer {
	
	public static void main(String[] args) {
		/* Reads the problem specified by command or throws error */
		if (args.length < 1) {
			System.out.println("Error: Please indicate a problem to solve.");
			System.exit(1);
		}
		String problem = args[0];
		
		/* Creates a builder for the planner */
		LinearPlannerBuilder builder = new LinearPlannerBuilder();
		List<Predicate> availablePredicates = new ArrayList<>();
		List<Operator> availableOperators = new ArrayList<>();
		
		/* Defines the generic parameters used by the problem */
		Parameter paramO = new Parameter("o", "_o");
		Parameter paramO1 = new Parameter("o", "_o1");
		Parameter paramO2 = new Parameter("o", "_o2");
		Parameter paramN = new Parameter("n", "_n");
		Parameter paramX = new Parameter("x", "_x");
		
		/* Defines the predicates available */
		Predicate robotLocation = new Predicate("Robot-location", paramO);
		Predicate robotLocationO1 = new Predicate("Robot-location", paramO1);
		Predicate robotLocationO2 = new Predicate("Robot-location", paramO2);
		Predicate robotFree = new Predicate("Robot-free");
		Predicate robotLoaded = new Predicate("Robot-loaded", paramN);
		Predicate petition = new Predicate("Petition", paramO, paramN);
		Predicate served = new Predicate("Served", paramO);
		Predicate machine = new Predicate("Machine", paramO, paramN);
		Predicate steps = new Predicate("Steps", paramX);
		availablePredicates.addAll(Arrays.asList(robotLocation, robotFree,
				robotLoaded, petition, served, machine, steps));
		
		/* Defines the operators available */
		Operator make = new Operator("Make", Arrays.asList(robotLocation, robotFree, machine),
				Arrays.asList(robotLoaded), Arrays.asList(robotFree), paramO, paramN);
		Operator move = new MoveOperator(Arrays.asList(robotLocationO1, steps), 
				Arrays.asList(robotLocationO2, steps), Arrays.asList(robotLocationO1, steps), paramO1, paramO2);
		Operator serve = new Operator("Serve", Arrays.asList(robotLocation, robotLoaded, petition),
				Arrays.asList(served, robotFree), Arrays.asList(petition, robotLoaded), paramO, paramN);
		availableOperators.addAll(Arrays.asList(make, move, serve));
		
		/* Defines the heuristics used */
		Intelligence intelligence = new StandardHeuristics();
		
		try {
			/* Reads the initial and final state of the problem */
			ProblemReader reader = new ProblemReader(problem);
			reader.readStates();
			State initialState = reader.getInitialState();
			State goalState = reader.getGoalState();
			
			/* Adds to the builder all the information of the problem */
			builder.addPredicates(availablePredicates);
			builder.addOperators(availableOperators);
			builder.setInitialState(initialState);
			builder.setFinalState(goalState);
			builder.setIntelligence(intelligence);
			builder.setLogOutput(new PrintStream("log_" + problem));
			
			/* Creates the planner and executes it */
			LinearPlanner planner = builder.build();
			planner.executePlan();
			planner.logPlan();
			planner.logSteps();
		} catch (IOException e1) {
			System.out.println("Error: Could not read/write.");
		} catch (PlannerBuilderException e2) {
			System.out.println("Error: LinearPlanner was not properly created.");
		}
	}
	
}
