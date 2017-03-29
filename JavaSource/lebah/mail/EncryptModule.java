package lebah.mail;

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
		DESEncryption myEncryptor= new DESEncryption();
		
		String originalValue = getParam("originalValue");
		String encryptValue = myEncryptor.encrypt(originalValue);
		context.put("encryptValue", encryptValue);
		return path + "/encrypt.vm";
	}

}
