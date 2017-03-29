package lebah.portal.action;

public class TestModule3 extends TestModule2 {
	
	@Command("command3")
	public String command3() {
		System.out.println("command3");
		return path + "start.vm";
	}

}
