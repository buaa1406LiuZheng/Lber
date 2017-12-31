package lber;

import java.util.ArrayList;

public class TaxiInfo {
	
	private static int TOTAL_TAXI = 100;
	private static ArrayList<Taxi> allTaxies = new ArrayList<>(TOTAL_TAXI);
	
	/**
	 * REQUIRES: none 
	 * MODIFIES: allTaxies
	 * EFFECTS: add the given taxi to allTaxies
	 */
	public static void addTaxi(Taxi taxi) {
		allTaxies.add(taxi);
	}
	
	/**
	 * REQUIRES: 0<=i<100
	 * MODIFIES: none 
	 * EFFECTS: print the information of the NO.i taxi
	 */
	public static void printTaxiInfo(int i){
		if(i>=100){
			System.out.println("no taxi");
			return;
		}
		Taxi taxi = allTaxies.get(i);
		System.out.println(taxi+"currentSystemTime:"+System.currentTimeMillis());
	}
	
	/**
	 * EFFECTS: print taxi's information in allTaxies List
	 */
	public static void printAllTaxiInfo(){
		for(int i=0;i<TOTAL_TAXI;i++){
			printTaxiInfo(i);
		}
	}
	
}
