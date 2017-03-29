package lebah.portal.action;

public class TestModule2 extends TestModule {
	
	@Command("command2")
	public String command2() {
		System.out.println("command2");
		return path + "start.vm";
	}

}
