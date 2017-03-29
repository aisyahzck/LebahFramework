package lebah.app;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class EncryptModule extends LebahModule {
	
	private String path = "encrypt";

	@Override
	public String start() {
		return path + "/start.vm";
	}
	
	@Command("encrypt")
	public String encrypt() throws Exception {
		String originalValue = getParam("originalValue");
		String encryptValue = lebah.util.PasswordService.encrypt(originalValue);
		context.put("encryptValue", encryptValue);
		return path + "/encrypt.vm";
	}

}
