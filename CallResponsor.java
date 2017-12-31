package lber;

import java.util.Iterator;
import java.util.LinkedList;

class CallResponsor implements Runnable{
	/*response to a single passenger's call*/
	
	private final int SearchInterval = 100;
	private final int SearchWindowTime = 3000;
	private static Navigator navigator;
	
	private Passenger passenger;
	private LinkedList<Taxi> allTaxies;
	private LinkedList<Taxi> taxiesInArea;
	
	/**
	 * EFFECTS: constructs a(n) CallResponsor object
	 */
	public CallResponsor(Passenger passenger,LinkedList<Taxi> taxies) {
		this.passenger = passenger;
		allTaxies = taxies;
		taxiesInArea = new LinkedList<>();
	}
	
	/**
	 * EFFECTS: set the navigator to the given navigator object
	 */
	public static void setNavigator(Navigator navigator) {
		CallResponsor.navigator = navigator;
	}
	
	/**
	 * EFFECTS: find a taxies for this call, and add the call to the selected taxi;
	 * if no taxi found during SearchWindowTime, print no taxi information
	 */
	@Override
	public void run() {
		
		int searchTime = 0;
		while(searchTime<SearchWindowTime){
			serchTaxiInArea();
			try {
				Thread.sleep(SearchInterval);
				searchTime += SearchInterval;
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		if(taxiesInArea.size() == 0){
			System.out.println("passenger "+passenger.id+": no taxi response");
		}else{
			boolean takeTaxi = false;
			while(taxiesInArea.size()!=0){
				Taxi taxi = selectTaxi();
				if(taxi == null){
					break;
				}
				synchronized (taxi) {
					//two passengers may select the same taxi
					if(taxi.isAvaliable()){
						taxi.pickPassenger(passenger);
						takeTaxi = true;
						break;
					}
				}
			}
			if(!takeTaxi){
				System.out.println("passenger "+passenger.id+": no taxi response");
			}
		}
	}
	
	/**
	 * REQUIRES: none
	 * MODIFIES: allTaxies, taxiesInArea
	 * EFFECTS: remove all available taxies in area in allTaxies List and add it to the taxiesInArea List 
	 */
	private void serchTaxiInArea(){
		
		Iterator<Taxi> taxiIterator = allTaxies.iterator();
		while(taxiIterator.hasNext()){
			Taxi taxi = taxiIterator.next();
			if(isInArea(taxi) && taxi.isAvaliable()){
				taxi.receiveCall();
				taxiesInArea.add(taxi);
				taxiIterator.remove();
			}
		}
		return;
	}
	
	/**
	 * REQUIRES: taxi is not null
	 * MODIFIES: none
	 * EFFECTS: if taxi or position is null, throws NullPointerException;
	 * if taxi's position is in the 4*4 area around this.position, return true;
	 * otherwise return false;
	 */
	private boolean isInArea(Taxi taxi){
		Spot position = taxi.getPosition();
		int x = position.x;
		int y = position.y;
		int px = passenger.position.x;
		int py = passenger.position.y;
		
		if((x>=px-2 && x<=px+2) &&
		   (y>=py-2 && y<=py+2)){
			return true;
		}
		return false;
	}
	
	/**
	 * REQUIRES: none
	 * MODIFIES: taxiesInArea
	 * EFFECTS: return the best fitted taxi and remove it in the taxiesInArea List
	 * remove all not currently not available taxies in the meantime
	 */
	private Taxi selectTaxi() {
		
		Taxi selectedTaxi = null;
		//choose the first taxi available
		Iterator<Taxi> taxiIterator = taxiesInArea.iterator();
		while(taxiIterator.hasNext()){
			Taxi taxi = taxiIterator.next();
			if(taxi.isAvaliable()){
				selectedTaxi = taxi;
			}
			else{
				taxiIterator.remove();
			}
		}
		
		taxiIterator = taxiesInArea.iterator();
		while(taxiIterator.hasNext()){
			Taxi taxi = taxiIterator.next();
			if(taxi.isAvaliable()){
				if(taxi.getCredit()>selectedTaxi.getCredit()){	
					selectedTaxi = taxi;
				}
				else if(taxi.getCredit()==selectedTaxi.getCredit()){
					Spot selectedTaxiPosition = selectedTaxi.getPosition();
					Spot taxiPosition = taxi.getPosition();
					int selectedTaxiDistance = navigator.getDistance(selectedTaxiPosition,passenger.position);
					int taxiDistance = navigator.getDistance(taxiPosition,passenger.position);
					if(taxiDistance<selectedTaxiDistance){
						selectedTaxi = taxi;
					}		
				}
			}else{
				taxiIterator.remove();
			}
		}
		
		taxiesInArea.remove(selectedTaxi);
		return selectedTaxi;
	}
	
}
