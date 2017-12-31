package lber;

class Map {
	
	private final int MAX_X = 80;
	private final int MAX_Y = 80;
	
	private boolean[][] horizonRoad;
	private boolean[][] verticalRoad;
	
	/**
	 * EFFECTS: constructs a(n) Map object
	 */
	public Map(boolean[][] horizonRoad,boolean[][] verticalRoad) {
		this.horizonRoad = horizonRoad;
		this.verticalRoad = verticalRoad;
	}
	
	/**
	 * REQUIRES: direction and spot is not null
	 * MODIFIES: none
	 * EFFECTS: if spot is null, throws NullPointerException;
	 * if spot is in the map, and the road on the direction of the spot is passable return true;
	 * otherwise return false;
	 */
	public boolean isPassable(Spot spot,Direction direction){
		/*determine if the road on the direction of the spot is passable*/
		int x = spot.x;
		int y = spot.y;
		
		if((x<0 || x>=MAX_X) || (y<0 || y>=MAX_Y)){
			return false;
		}
		
		if((x == 0 && direction == Direction.West) ||
		   (y == 0 && direction == Direction.South) ||
		   (x == MAX_X-1 && direction == Direction.East) ||
		   (y == MAX_Y-1 && direction == Direction.North)){//border condition
			return false;
		}
		
		switch (direction) {
		case North:	
			return verticalRoad[x][y];
		case East:	
			return horizonRoad[x][y];
		case South:	
			return verticalRoad[x][y-1];
		case West:	
			return horizonRoad[x-1][y];
		default:
			return false;
		}		
	}
	
	/**
	 * REQUIRES: if isHorizon, 0<=x<MAX_X-1 && 0<=y<MAX_Y;
	 * else(vertical) 0<=x<MAX_X && 0<=y<MAX_Y-1;
	 * MODIFIES: horizonRoad or verticalRoad
	 * EFFECTS: if isHorizon, set horizonRoad[x][y] to open;
	 * eles(vertical) set verticalRoad[x][y] to open;
	 */
	public void modifyRoad(int x,int y, boolean isHorizon, boolean open){
		if(isHorizon){
			if((x<0 || x>=MAX_X-1) || (y<0 || y>=MAX_Y)){
				return;
			}
			else{
				horizonRoad[x][y] = open;
			}
		}
		else{
			if((x<0 || x>=MAX_X) || (y<0 || y>=MAX_Y-1)){
				return;
			}
			else{
				verticalRoad[x][y] = open;
			}
		}
	}
	
}
