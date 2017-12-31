package lber;

public class CallSender{
	
	private static final int MAX_X = 80;
	private static final int MAX_Y = 80;
	
	private static ServingCenter servingCenter;
	
	/**
	 * REQUIRES: none
	 * MODIFIES: servingCenter
	 * EFFECTS: set the servingCenter to the given servingCenter;
	 */
	public static void setServingCenter(ServingCenter servingCenter) {
		CallSender.servingCenter = servingCenter;
	}
	
	/**
	 * REQUIRES: (x,y) and (dx,dy) is in the map
	 * MODIFIES: none
	 * EFFECTS: send a passenger call whose position is (x,y) and destination is (dx,dy) to the servingCenter;
	 */
	public static void sendCall(int x,int y,int dx,int dy){
		if( (x<0 || y<0 || x>=MAX_X || y>=MAX_Y) ||
			(dx<0 || dy<0 || dx>=MAX_X || dy>=MAX_Y))	{
			System.out.println("invalid call");
			return;
		}
		Passenger passenger = new Passenger(x, y, dx, dy, servingCenter);
		passenger.sendCall();
	}
	
}
