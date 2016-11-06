package coffeeServer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import strips.Parameter;
import strips.Predicate;
import strips.State;

/**
 * 
 * The ProblemReader is able to read a CoffeeServer problem.
 * It reads a text file with the following syntax:
 * 
 * InitialState=Predicate1(p1,p2);Predicate2(p1);...
 * 
 * FinalState=Predicate1(p1);Predicate2(p2);
 * Predicate3(p3);
 * 
 * And is able to return an initial and final state containing
 * the specified information.
 * 
 * @author Javier Beltran, Jorge Rodriguez
 *
 */
public class ProblemReader {
	
	private BufferedReader file;
	private State initialState, goalState;
	
	public ProblemReader(String path) throws IOException {
		file = new BufferedReader(new FileReader(path));
		initialState = null;
		goalState = null;
	}
	
	/**
	 * Reads the file and creates the initial and final states.
	 */
	public void readStates() throws IOException {
		/* Reads the initial state part */
		String initialText = "";
		String line = file.readLine();
		while (!line.equals("")) {
			initialText += line;
			line = file.readLine();
		}
		
		/* Reads the final state part */
		String goalText = "";
		while (line != null) {
			goalText += line;
			line = file.readLine();
		}
		
		/* Detects the list of predicates of both parts */
		String[] initStrings = initialText.split("=")[1].split(";");
		String[] goalStrings = goalText.split("=")[1].split(";");
		List<Predicate> initPredicates = new ArrayList<>();
		List<Predicate> goalPredicates = new ArrayList<>();
		
		/* Adds the predicates for both states */
		for (String predString : initStrings) {
			initPredicates.add(createPredicate(predString));
		}
		for (String predString : goalStrings) {
			goalPredicates.add(createPredicate(predString));
		}
		
		initialState = new State(initPredicates);
		goalState = new State(goalPredicates);
	}
	
	/**
	 * Reads a predicate in its textual form and translates
	 * it to a real predicate object.
	 */
	private Predicate createPredicate(String s) {
		/* Gets the name and parameters of the string */
		s = s.trim().replace(")","");
		String[] parts = s.split("\\(");
		String name = parts[0];
		if (parts.length > 1) {
			String[] paramParts = parts[1].split(",");
			List<Parameter> params = new ArrayList<>();
			
			/* Converts parameter text to parameter */
			for (String paramString : paramParts) {
				if (paramString.startsWith("o")) {
					params.add(new Parameter("o", paramString));
				} else if (name.equals("Steps")){
					params.add(new Parameter("x", paramString));
				} else {
					params.add(new Parameter("n", paramString));
				}
			}
			
			return new Predicate(name, params);
		} else return new Predicate(name);
		
	}
	
	
	/* Getters and setters */
	
	public State getInitialState() {
		return initialState;
	}
	
	public State getGoalState() {
		return goalState;
	}
	
}
