package lber;

public class RoadModifier {
	private static Map map;
	
	/**
	 * REQUIRES: none
	 * MODIFIES: map
	 * EFFECTS: set the map to the given map object
	 */
	public static void setMap(Map map){
		RoadModifier.map = map;
	}

	/**
	 * REQUIRES: if isHorizon, 0<=x<MAX_X-1 && 0<=y<MAX_Y;
	 * else(vertical) 0<=x<MAX_X && 0<=y<MAX_Y-1;
	 * MODIFIES: map
	 * EFFECTS: set the road specified by (x,y) and isHorizon to open/close;
	 */
	public static void modifyRoad(int x,int y, boolean isHorizon, boolean open){
		map.modifyRoad(x, y, isHorizon, open);
	}
}
