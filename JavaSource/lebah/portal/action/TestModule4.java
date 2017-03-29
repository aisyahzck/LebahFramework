package lebah.portal.action;

public class TestModule4 extends TestModule3 {
	
	@Command("command4")
	public String command4() {
		System.out.println("command4");
		return path + "start.vm";
	}

}
