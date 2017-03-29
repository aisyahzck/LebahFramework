package lebah.app;

import lebah.portal.action.LebahModule;

public class Main extends LebahModule {
	
	private String path = "app";

	@Override
	public String start() {
		// TODO Auto-generated method stub
		return path + "/start.vm";
	}

}
