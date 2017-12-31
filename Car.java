package lber;

class Car {
	
	protected volatile Spot position;
	protected volatile Spot preposition;
	public final int licensePlate;
	protected Navigator navigator;
	protected Map map;
	
	/**
	 * EFFECTS: constructs a(n) Car object
	 */
	public Car(int licensePlate,int x,int y,
			Navigator navigator,Map map) {
		position = new Spot(x, y);
		preposition = new Spot(x, y);
		this.licensePlate = licensePlate;
		this.navigator = navigator;
		this.map = map;
	}

	
	/**
	 * REQUIRES: position is not null, and Spot to the direction of position is in the map
	 * MODIFIES: position
	 * EFFECTS: if position is null, throws NullPointerException, position dosen't change;
	 * if direction is not null, position change to the spot to the direction of current position, 
	 * don't guarantee new position is in the map;
	 * otherwise nothing change;
	 */
	protected void move(Direction direction){
		/*car move according to the direction*/
		
		preposition = position;
		int x = position.x;
		int y = position.y;
		switch (direction) {
		case North:	
			position = new Spot(x, y+1);
			break;
		case East:	
			position = new Spot(x+1, y);
			break;
		case South:	
			position = new Spot(x, y-1);
			break;
		case West:	
			position = new Spot(x-1, y);
			break;
		default:
			return;
		}
	}
	
	/**
	 * REQUIRES: navigator is not null; exist a path from position to destination
	 * MODIFIES: position
	 * EFFECTS: position change to the destination, according to the path given by navigator
	 */
	protected void moveTo(Spot destination) {
		/*car move to destination*/
		while(!position.equals(destination)){
			Direction[] path;
			try{
				path = navigator.findPath(position,destination);
			}catch(NoPathException e){
				System.out.println(e.getMessage());
				return;
			}
			
			if(path !=null){
				boolean flag = false;
				
				for(Direction direction:path){
					if(map.isPassable(position, direction)){
						move(direction);
						flag = true;
						break;
					}
					else{
						flag = true;
						break;
					}
				}
//				for(Direction direction:path){
//					if(map.isPassable(position, direction)){
//						move(direction);
//					}
//					else{
//						flag = true;
//						break;
//					}
//				}
				
				if(flag){
					continue;
				}		
			}
			else{
				break;
			}
		}
	}
	
	/**
	 * EFFECTS: return the position
	 */
	public Spot getPosition() {
		return position;
	}
	
}
