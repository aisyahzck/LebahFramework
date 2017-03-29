package lebah.module;

import lebah.portal.action.AjaxModule;

public class TestAjaxModule extends AjaxModule {

	String vm = "";
	
	
	public String doAction() throws Exception {
		vm = "vtl/test.vm";
		
		String command = request.getParameter("command");
		if ( command == null ) command = "";
		
		if ( "do1".equals(command)) do1();
		else if ( "do2".equals(command)) do2();
		
		return vm;
	}

	private void do2() {
		vm = "vtl/testpopup2.vm";
	}

	private void do1() {
		vm = "vtl/testpopup.vm";
		
	}

}
