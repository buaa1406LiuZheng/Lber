package lber;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

enum State{Stop,Serving,Idle,Picking};

class Taxi extends Car implements Runnable{
	
	private static final int MOVE_TIME = 100;
	private final int MaxDrivingTime = 20000;
	private final int RestTime = 1000;
	
	private AtomicInteger credit;
	private int drivingTime;
	private AtomicReference<State> state;
	private Passenger passenger;
	private final Object passengerLock;
	private FlowData flowData;
	
	/**
	 * EFFECTS: constructs a(n) Taxi object
	 */
	public Taxi(int licensePlate,int x,int y,Navigator navigator,FlowData flowData,Map map) {
		super(licensePlate,x,y,navigator,map);
		credit = new AtomicInteger(0);
		drivingTime = 0;
		state = new AtomicReference<State>(State.Idle);
		passenger = null;
		passengerLock = new Object();
		this.flowData = flowData;
	}
	
	@Override
	public void run() {
		
		while(true){
			
			if(passenger==null){
				if(drivingTime>=MaxDrivingTime){
					rest();
				}
				else{
					try{
						wander();
					}catch(NoPathException e){
						System.out.println(e.getMessage());
						break;
					}
				}
			}
			else{
				state.set(State.Picking);
				
				moveTo(passenger.position);
				rest();
				state.set(State.Serving);
				
				moveTo(passenger.destination);
				System.out.println("taxi "+licensePlate+" finishes serving passenger "+passenger.id+
						", drivingTime:"+drivingTime);
				rest();
				synchronized (passengerLock) {
					passenger = null;
				}
				state.set(State.Idle);
			}
		}
		
	}
	
	/**
	 * Override Car.move()
	 * REQUIRES: as Car;
	 * MODIFIES: position, drivingTime, flowData;
	 * EFFECTS: if no InterruptedException or other Exception happen,
	 * position change to the spot to the direction of current position,
	 * flowData add flow between prevPosition and currentPosition,
	 * Thread sleep MOVE_TIME and drivingTime plus MOVE_TIME;
	 */
	@Override
	protected void move(Direction direction){
		
		flowData.deFlow(preposition,position);
		flowData.addFlow(position,position.getAdjecentSpot(direction));
		super.move(direction);
		try {		
			Thread.sleep(MOVE_TIME);
			drivingTime += MOVE_TIME;	
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	/**
	 * REQUIRES: none
	 * MODIFIES: credit
	 * EFFECTS: credit's value plus 1
	 */
	public void receiveCall(){
		credit.getAndIncrement();
	}
	
	/**
	 * REQUIRES: none
	 * MODIFIES: credit, passenger
	 * EFFECTS: credit's value plus 3, passenger change to the given passenger;
	 * print taxi and passenger's information
	 */
	public void pickPassenger(Passenger passenger){
		synchronized (passengerLock) {
			this.passenger = passenger;
		}
		credit.getAndAdd(3);
		System.out.println("taxi "+licensePlate+" has got the passenger "+passenger.id+"'s call");
	}
	
	/**
	 * EFFECTS: return the credit's value;
	 */
	public int getCredit(){
		return credit.get();
	}
	
	/**
	 * EFFECTS: return the state's value;
	 */
	public State getState(){
		return state.get();
	}
	
	/**
	 * REQUIRES: none
	 * MODIFIES: none
	 * EFFECTS: if currently passenger is null and state's value is Idle, return true;
	 * else return false; 
	 */
	public boolean isAvaliable(){
		synchronized (passengerLock) {
			if(passenger == null && state.get()==State.Idle){
				return true;
			}
		}
		return false;
	}

	/**
	 * REQUIRES: exist a passable road around the position
	 * MODIFIES: position, drivingTime
	 * EFFECTS: randomly select a direction given by navigator,
	 * drivingTime plus MOVE_TIME, position change to the direction of current position
	 */
	private void wander() throws NoPathException{
		Direction[] passableWays = navigator.findPassableWays(position);
		
		if(passableWays.length==0){
			throw new NoPathException("spot "+position+" is isolated");
		}
		
		//randomly pick a way
		Random rand = new Random();
		int randNum = rand.nextInt(passableWays.length);
		
		Direction direction = passableWays[randNum];
		
		move(direction);
	}
	
	/**
	 * REQUIRES: no state change during rest
	 * MODIFIES: state drivingTime
	 * EFFECTS: if no Exception happen,
	 * state's value change to stop, Thread sleep RestTime, then state's value change back to previous value,
	 * drivingTime change to 0;
	 * if state change during rest, state will change back to the value before rest still;
	 */
	private void rest() {
		State prevState = state.get();
		state.set(State.Stop);
		try {
			Thread.sleep(RestTime);
		} catch (Exception e) {
			System.out.println(e);
		}
		state.set(prevState);
		drivingTime = 0;
	}
	
	/**
	 * Override toString
	 * REQUIRES: none
	 * MODIFIES: none
	 * EFFECTS: return formatted taxi's information in String
	 */
	@Override
	public String toString() {
		String info = "Taxi "+licensePlate + ": " +
				"Position:" + position + " "+
				"state:" + state.get() + " "+
				"credit:" + credit.get()+ " "+
				"drivingTime:" + drivingTime+ " ";
		return info;
	}
	
}
