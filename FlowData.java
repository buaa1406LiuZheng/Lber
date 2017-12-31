package lber;

import java.util.concurrent.atomic.AtomicInteger;

class FlowData {
	
	private final int MAX_X = 80;
	private final int MAX_Y = 80;
	
	private AtomicInteger horizonFlowData[][];
	private AtomicInteger verticalFlowData[][];
	
	/**
	 * EFFECTS: constructs a(n) FlowData object, with all flows are 0
	 */
	public FlowData() {
		horizonFlowData = new AtomicInteger[MAX_X-1][MAX_Y];
		verticalFlowData = new AtomicInteger[MAX_X][MAX_Y-1];
		for(int x=0;x<MAX_X-1;x++){
			for(int y=0;y<MAX_Y;y++){
				horizonFlowData[x][y] = new AtomicInteger(0);
			}
		}
		for(int x=0;x<MAX_X;x++){
			for(int y=0;y<MAX_Y-1;y++){
				verticalFlowData[x][y] = new AtomicInteger(0);
			}
		}
	}
	
	/**
	 * REQUIRES: direction and spot is not null
	 * MODIFIES: none
	 * EFFECTS: if spot is null, throws NullPointerException;
	 * if the road specific by the spot and direction is in the map, return the road's flow count;
	 * otherwise return -1;
	 */
	public int getFlowData(Spot spot,Direction direction){
		int x = spot.x;
		int y = spot.y;
		
		if((x<0 || x>=MAX_X) || (y<0 || y>=MAX_Y)){
			return -1;
		}
		
		if((x == 0 && direction == Direction.West) ||
		   (y == 0 && direction == Direction.South) ||
		   (x == MAX_X-1 && direction == Direction.East) ||
		   (y == MAX_Y-1 && direction == Direction.North)){//border condition
			return -1;
		}
		
		switch (direction) {
		case North:	
			return verticalFlowData[x][y].get();
		case East:	
			return horizonFlowData[x][y].get();
		case South:	
			return verticalFlowData[x][y-1].get();
		case West:	
			return horizonFlowData[x-1][y].get();
		default:
			return -1;
		}
	}
	
	/**
	 * REQUIRES: from and to are not null, and both are in the map
	 * MODIFIES: horizonFlowData or verticalFlowData
	 * EFFECTS: if from or to is null, throws NullPointerException;
	 * if two spots are not adjacent, nothing change;
	 * else the road between two spots plus one flow;
	 */
	public void addFlow(Spot from, Spot to){
		Direction direction;
		try {
			direction = from.getDirection(to);
		} catch (NoPathException e) {
			return;
		}
		
		int x = from.x;
		int y = from.y;
		switch (direction) {
		case North:	
			verticalFlowData[x][y].incrementAndGet();
			break;
		case East:	
			horizonFlowData[x][y].incrementAndGet();
			break;
		case South:	
			verticalFlowData[x][y-1].incrementAndGet();
			break;
		case West:	
			horizonFlowData[x-1][y].incrementAndGet();
			break;
		default:
			return;
		}
	}
	
	/**
	 * REQUIRES: from and to are not null, and both are in the map
	 * MODIFIES: horizonFlowData or verticalFlowData
	 * EFFECTS: if from or to is null, throws NullPointerException;
	 * if two spots are not adjacent, nothing change;
	 * else the road between two spots decrease one flow;
	 */
	public void deFlow(Spot from, Spot to){
		Direction direction;
		try {
			direction = from.getDirection(to);
		} catch (NoPathException e) {
			return;
		}
		
		int x = from.x;
		int y = from.y;
		switch (direction) {
		case North:	
			verticalFlowData[x][y].decrementAndGet();
			break;
		case East:	
			horizonFlowData[x][y].decrementAndGet();
			break;
		case South:	
			verticalFlowData[x][y-1].decrementAndGet();
			break;
		case West:	
			horizonFlowData[x-1][y].decrementAndGet();
			break;
		default:
			return;
		}
	}
	
	/**
	 * REQUIRES: horizonFlowData and verticalFlowData's elements are not null
	 * MODIFIES: horizonFlowData and verticalFlowData
	 * EFFECTS: all horizonFlowData and verticalFlowData set to 0;
	 */
	public void refresh(){
		
		for(int x=0;x<MAX_X-1;x++){
			for(int y=0;y<MAX_Y;y++){
				horizonFlowData[x][y].set(0);
			}
		}
		for(int x=0;x<MAX_X;x++){
			for(int y=0;y<MAX_Y-1;y++){
				verticalFlowData[x][y].set(0);
			}
		}
	}
	
}
