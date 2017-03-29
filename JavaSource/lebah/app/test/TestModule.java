package lebah.app.test;

import lebah.portal.action.LebahModule;

public class TestModule extends LebahModule {
	
	private String path = "test/test/";

	@Override
	public String start() {
		// TODO Auto-generated method stub
		return path + "start.vm";
	}

}
