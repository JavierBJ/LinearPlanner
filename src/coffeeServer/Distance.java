package coffeeServer;

public class Distance {
	
	/**
	 * Calculates the Manhattan Distance between two positions specified as the
	 * parameters of Move.
	 */
	public static int manhattanDistance(String o1, String o2) {
		int x1 = toCoordsX(o1);
		int x2 = toCoordsX(o2);
		int y1 = toCoordsY(o1);
		int y2 = toCoordsY(o2);
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}

	/**
	 * Translates an Oi parameter to its X coordinate.
	 */
	private static int toCoordsX(String o) {
		int num = Integer.parseInt(o.substring(1));
		return (num - 1) % 6;
	}
	
	/**
	 * Translates an Oi parameter to its Y coordinate.
	 */
	private static int toCoordsY(String o) {
		int num = Integer.parseInt(o.substring(1));
		return (num - 1) / 6;
	}
	
}
