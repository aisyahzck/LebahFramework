package lebah.msg;

import lebah.portal.action.LebahModule;

public class ContactMessageModule extends LebahModule {
	
	String path = "contactUs";

	@Override
	public String start() {
		return path + "/start.vm";
	}

}
