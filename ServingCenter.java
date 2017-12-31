package lber;

import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

class ServingCenter implements Runnable{
	
	private LinkedBlockingQueue<Passenger> callList;
	private LinkedList<Taxi> taxies;
	
	/**
	 * EFFECTS: constructs a(n) ServingCenter object
	 */
	public ServingCenter() {
		callList = new LinkedBlockingQueue<>(300);
		taxies = new LinkedList<>();
	}
	
	@Override
	public void run() {
		
		while(true){
			Passenger passenger;
			
			try {
				passenger = callList.take();
			} catch (InterruptedException e) {
				System.out.println(e);
				continue;
			}
			
			@SuppressWarnings("unchecked")
			LinkedList<Taxi> taxiesClone = (LinkedList<Taxi>) taxies.clone();
			
			new Thread(new CallResponsor(passenger,taxiesClone ),"responsor").start();
		}
		
	}
	
	/**
	 * REQUIRES: none
	 * MODIFIES: callList
	 * EFFECTS: if no Exception happen, add the given passenger to the callList
	 */
	public void addCall(Passenger passenger){
		try {
			callList.put(passenger);
		} catch (InterruptedException e) {
			System.out.println(e);
		}
	}

	/**
	 * REQUIRES: none
	 * MODIFIES: taxise
	 * EFFECTS: add the given taxi to the taxies List
	 */
	public void addTaxi(Taxi taxi){
		taxies.add(taxi);
	}
}
