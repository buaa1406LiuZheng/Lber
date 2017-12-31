package lber;

import java.util.LinkedList;

class Navigator {
	
	private final int MAX_X = 80;
	private final int MAX_Y = 80;
	
	private Map map;
	private FlowData flowData;
	
	/**
	 * EFFECTS: constructs a(n) Navigator object
	 */
	public Navigator(Map map,FlowData flowData) {
		this.map = map;
		this.flowData = flowData;
	}
	
	/**
	 * REQUIRES: from and to is not null
	 * MODIFIES: none
	 * EFFECTS: if no path found, throws NoPathException;
	 * else return a Direction sequence which specify a path between two spot,
	 * the path is found by searchPath;
	 */
	public Direction[] findPath(Spot from,Spot to) throws NoPathException{
		if(from.equals(to)){
			return new Direction[0];
		}
		Spot[] vertexed = derivePathVertexes(from, to);
		Direction[] edges = derivePathEdges(vertexed);
		return edges;
	}
	
	/**
	 * REQUIRES: from and to is not null
	 * MODIFIES: none
	 * EFFECTS: if NoPathException occurs, return Integer.MAX_VALUE;
	 * else if there is a path between two spots, return the length of the path;
	 */
	public int getDistance(Spot from,Spot to){
		if(from.equals(to)){
			return 0;
		}
		try{
			Spot[] vertexes = derivePathVertexes(from, to);
			return (vertexes.length-1);
		}catch(NoPathException e){
			return Integer.MAX_VALUE;
		}
	}
	
	/**
	 * REQUIRES: position is not null
	 * MODIFIES: none
	 * EFFECTS: find the passable roads that have the minimum flow,
	 * and return the direction to the position of those road; 
	 */
	public Direction[] findPassableWays(Spot position){	

		LinkedList<Direction> passableRoadList = new LinkedList<>();

		for(Direction direction:Direction.values()){
			if(map.isPassable(position,direction)){
				passableRoadList.add(direction);
			}
		}
		
		int minFlow = Integer.MAX_VALUE;
		
		LinkedList<Direction> roadList = new LinkedList<>();

		for(Direction direction:passableRoadList){
			int flow = flowData.getFlowData(position, direction);
			if(flow==minFlow){
				roadList.add(direction);
			}
			else if(flow<=minFlow){
				roadList.clear();
				roadList.add(direction);
				minFlow = flow;
			}
		}
		
		Direction[] passableWays = new Direction[roadList.size()];
		roadList.toArray(passableWays);

		return passableWays;
	}
	
	/**
	 * REQUIRES: from and to is not null
	 * MODIFIES: none
	 * EFFECTS: return a Spot map record the path tracing information,
	 * the path is the shortest path and have the minimum flow which is dynamically got;
	 */
	private Spot[][] searchPath(Spot from, Spot to) throws NoPathException{
		/*the algorithm is BFS based*/

		Spot[][] trace = new Spot[MAX_X][MAX_Y];		//record the tracing information
		int[][] flow = new int[MAX_X][MAX_Y]; 		//record flow
		int[][] length = new int[MAX_X][MAX_Y];		//record the length of path to every visited spots, prevent revisit
		LinkedList<Spot> searchQueue = new LinkedList<>();

		trace[to.x][to.y] = to;
		flow[to.x][to.y] = 0;
		length[to.x][to.y] = 0;
		searchQueue.add(to);
		boolean pathFound = false;
		while(!searchQueue.isEmpty()){
			Spot spot = searchQueue.removeFirst();
			if(spot.equals(from)){
				pathFound = true;
				break;
			}
			
			int currentLength = length[spot.x][spot.y];
			int currentFlow = flow[spot.x][spot.y];
			for(Direction direction:Direction.values()){
				if(map.isPassable(spot, direction)){
					Spot adjecentSpot = spot.getAdjecentSpot(direction);
					int x = adjecentSpot.x;
					int y = adjecentSpot.y;
					if(trace[x][y]==null){
						/* if never visited*/
						trace[x][y] = spot;
						length[x][y] = currentLength+1;
						flow[x][y] = currentFlow+flowData.getFlowData(spot, direction);
						searchQueue.add(adjecentSpot);
					}
					else{
						/* if already visited */
						if(length[x][y]<=currentLength){
							continue;
						}
						else{
							if(flow[x][y]>(currentFlow+flowData.getFlowData(spot, direction))){
								/*this path have less flow*/
								flow[x][y] = currentFlow+flowData.getFlowData(spot, direction);
								trace[x][y] = spot;
							}
						}
					}
				}
			}
			if(pathFound){
				break;
			}
		}
		if(!pathFound){
			throw new NoPathException(""+from+to+"no path found");
		}
		return trace;
	}
	
	/**
	 * REQUIRES: from and to is not null
	 * MODIFIES: none
	 * EFFECTS: if there is no path between two spots, throws NoPathException;
	 * if there is a path, return the Spots(Vertexes)' sequence of that path given by searchPath, by tracing back the return value of searchPath;
	 */
	private Spot[] derivePathVertexes(Spot from,Spot to) throws NoPathException{
		
		Spot[][] trace = searchPath(from, to);
		
		LinkedList<Spot> pathList = new LinkedList<>();		
		pathList.add(from);
		
		Spot lastSpot = from;
		while(true){
			int lx = lastSpot.x;
			int ly = lastSpot.y;
			Spot nextSpot = trace[lx][ly];
			if(nextSpot == null){
				throw new NoPathException("no spot found");
			}
			pathList.add(nextSpot);
			if(nextSpot.equals(to)){
				break;
			}
			lastSpot = nextSpot;
		}
		
		Spot[] path = new Spot[pathList.size()];
		pathList.toArray(path);
		return path;
	}
	
	/**
	 * REQUIRES: vertexes' elements are not null;
	 * MODIFIES: none
	 * EFFECTS: if any two sequential adjacent vertexes' elements(Spots) are not adjacent in map, throws NoPathException;
	 * else return the Directions(Edges) sequence connect all vertexes;
	 */
	private Direction[] derivePathEdges(Spot[] vertexes) throws NoPathException{
		LinkedList<Direction> edgeList = new LinkedList<>();
		
		for(int i=1;i<vertexes.length;i++){
			Spot lastSpot = vertexes[i-1];
			Spot nextSpot = vertexes[i];
			Direction direction = lastSpot.getDirection(nextSpot);
			edgeList.add(direction);
		}
		
		Direction[] edges = new Direction[edgeList.size()];
		edgeList.toArray(edges);
		return edges;
	}
	
}
