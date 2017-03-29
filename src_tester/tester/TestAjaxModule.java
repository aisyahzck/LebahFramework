package tester;

import lebah.portal.action.AjaxModule;

public class TestAjaxModule extends AjaxModule {
	
	private String path = "tester/ajax/";
	private String vm = "";
	

	@Override
	public String doAction() throws Exception {
		String command = request.getParameter("command");
		
		if ( command == null || "".equals(command)) return start();
		else if ("test".equals(command)) return test();
		return "";
	}


	private String test() {
		String test = request.getParameter("ajaxTest");
		context.put("test", test);
		return path + "start.vm";
	}


	private String start() {
		return path + "start.vm";
	}

}
