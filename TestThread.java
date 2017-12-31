package lber;

public class TestThread extends Thread{
	
	@Override
	public void run() {
		
		/*sample*/
		CallSender.sendCall(30, 30, 79, 79);
//		int count = 0;
		while(true){
			//TaxiInfo.printTaxiInfo(0);
//			count++;
//			if(count == 100){
//				RoadModifier.modifyRoad(78,79,true,false);
//				RoadModifier.modifyRoad(79,78,false,false);
//				RoadModifier.modifyRoad(48,20,true,true);
//			}
//			if(count == 300){
//				RoadModifier.modifyRoad(48,79,true,true);
//				RoadModifier.modifyRoad(48,20,true,false);
//			}
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}
	
}
