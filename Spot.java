package lber;

enum Direction{North,South,East,West};

class Spot {
	
	public final int x;
	public final int y;
	
	/**
	 * EFFECTS: constructs a(n) Spot object
	 */
	public Spot(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * REQUIRES: none
	 * MODIFIES: none
	 * EFFECTS: if direction is valid, return a new Spot object to the direction of this spot;
	 * otherwise return null;
	 */
	public Spot getAdjecentSpot(Direction direction){
		/*get the spot on the direction of spot*/
		switch (direction) {
		case North:	
			return new Spot(x, y+1);
		case East:	
			return new Spot(x+1, y);
		case South:	
			return new Spot(x, y-1);
		case West:	
			return new Spot(x-1, y);
		default:
			return null;
		}
	}
	
	/**
	 * REQUIRES: destination is not null
	 * MODIFIES: none
	 * EFFECTS: if destination is null, throws NullPointerException;
	 * if destination is not adjacent to this spot, throws NoPathException;
	 * else return the direction from this spot to destination
	 */
	public Direction getDirection(Spot destination) throws NoPathException{
		int fx = x;
		int fy = y;
		int tx = destination.x;
		int ty = destination.y;
		
		if(tx == fx && ty == fy+1){
			return Direction.North;
		}
		if(tx == fx+1 && ty == fy){
			return Direction.East;
		}
		if(tx == fx && ty == fy-1){
			return Direction.South;
		}
		if(tx == fx-1 && ty == fy){
			return Direction.West;
		}
		else{
			throw new NoPathException("no direction found");
		}
	}
	
	/**
	 *  Override equals
	 * REQUIRES: obj is instance of Spot
	 * MODIFIES: none 
	 * EFFECTS: if obj is not instance of Spot, return false;
	 * else if this.x == obj.x and this.y == obj.y return true;
	 * otherwise return false;
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Spot){
			Spot spot = (Spot)obj;
			if(spot.x == x && spot.y == y){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Override toString
	 * REQUIRES: none
	 * MODIFIES: none
	 * EFFECTS:  return formatted spot's information in String
	 */
	@Override
	public String toString() {
		return "("+x+","+y+")";
	}

}
