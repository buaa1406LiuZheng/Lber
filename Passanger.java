package lber;

import java.util.concurrent.atomic.AtomicInteger;

class Passenger {
	
	private static final AtomicInteger count = new AtomicInteger(0);
	
	public final Spot position;
	public final Spot destination;
	public final int id;
	private ServingCenter servingCenter;
	
	/**
	 * EFFECTS: constructs a(n) Passenger object
	 */
	public Passenger(int x,int y,int dx,int dy,ServingCenter servingCenter) {
		position = new Spot(x, y);
		destination = new Spot(dx, dy);
		this.servingCenter = servingCenter;
		id = count.getAndIncrement();
	}
	
	/**
	 * EFFECTS: send this call to the servingCenter
	 */
	public void sendCall(){
		//determine if the position and destination is leagel
		servingCenter.addCall(this);
	}
	
}
