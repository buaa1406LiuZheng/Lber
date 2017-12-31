package lber;

import java.util.Random;

public class Main {

	public static void main(String[] args) {
		
		MapGenerator mapGenerator = new MapGenerator();
		Map map = mapGenerator.generateMap();
		if(map == null){
			System.out.println("Map generate fail");
			return;
		}
		
		/*initiate*/
		FlowData flowData = new FlowData();
		Navigator navigator = new Navigator(map,flowData);
		ServingCenter servingCenter = new ServingCenter();
		
		CallResponsor.setNavigator(navigator);
		CallSender.setServingCenter(servingCenter);
		RoadModifier.setMap(map);
		
		/*start taxies*/
		for(int i=0;i<100;i++){
			Random rand = new Random();
			int x = rand.nextInt(80);
			int y = rand.nextInt(80);
			Taxi taxi = new Taxi(i, x, y, navigator,flowData,map);
			servingCenter.addTaxi(taxi);
			TaxiInfo.addTaxi(taxi);
			
			new Thread(taxi,"taxi"+i).start();
		}
		
//		FlowRefresher flowRefresher = new FlowRefresher(flowData);
//		new Thread(flowRefresher,"flowRefresher").start();
		new Thread(servingCenter,"servingCenter").start();
		System.out.println("system start");
		
		new TestThread().start();
	}

}
