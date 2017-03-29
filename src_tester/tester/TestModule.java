package tester;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class TestModule extends LebahModule {
	
	private String path = "tester/";

	@Override
	public String start() {
		// TODO Auto-generated method stub
		return path + "start.vm";
	}
	
	@Command("tester")
	public String doTester() throws Exception {
		String test = request.getParameter("tester");
		context.put("test", test);
		return path + "tester.vm";
	}

}
