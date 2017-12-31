package lber;

class FlowRefresher implements Runnable{
	
	private final int REFRESH_INTERVAL = 50;
	
	private FlowData flowData;
	
	/**
	 * EFFECTS: constructs a(n) FlowRefresher object
	 */
	public FlowRefresher(FlowData flowData) {
		this.flowData = flowData;
	}
	
	@Override
	public void run() {
		
		while (true) {
			flowData.refresh();
			try {
				Thread.sleep(REFRESH_INTERVAL);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}

}