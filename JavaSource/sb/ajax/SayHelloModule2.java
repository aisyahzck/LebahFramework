package sb.ajax;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class SayHelloModule2 extends LebahModule {
	
	private String path = "vtl/ajax/";

	@Override
	public String start() {
		// TODO Auto-generated method stub
		return path + "hello.vm";
	}
	
	@Command("sayHello2")
	public String sayHello() {
    	String name = getParam("hello-name");
    	context.put("name", name);
    	return path + "hello2.vm";
	}

}
