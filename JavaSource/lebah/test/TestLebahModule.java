package lebah.test;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class TestLebahModule extends LebahModule {
	
	private String path = "test/reflect/";

	public String start() {
		return path + "start.vm";
	}
	
	@Command("else")
	public String doSomethingElse() {
		return path + "else.vm";
	}
	
	@Command("here")
	public String here() {
		return path + "here.vm";
	}
}
