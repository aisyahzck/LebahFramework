package lebah.portal.action;

public class TestModule extends LebahModule {
	
	protected String path = "vtl/action/test/";

	@Override
	public String start() {
		System.out.println("start.");
		return path + "start.vm";
	}
	
	@Command("command1")
	public String command1() {
		System.out.println("command1");
		return path + "start.vm";
	}

}
