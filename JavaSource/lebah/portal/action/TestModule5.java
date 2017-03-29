package lebah.portal.action;

public class TestModule5 extends TestModule4 {
	
	@Command("command5")
	public String command5() {
		System.out.println("command5");
		return path + "start.vm";
	}

}
