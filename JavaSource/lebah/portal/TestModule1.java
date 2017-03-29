package lebah.portal;

import lebah.portal.action.LebahModule;

public class TestModule1 extends LebahModule {
	
	private String path = "test/module1/";

	@Override
	public String start() {
		// TODO Auto-generated method stub
		return path + "start.vm";
	}

}
