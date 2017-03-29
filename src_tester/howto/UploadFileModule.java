package howto;

import lebah.portal.action.Command;
import lebah.portal.action.WebappModule;

public class UploadFileModule extends WebappModule {
	
	private String path = "howto/upload";

	@Override
	public String start() {
		return path + "/start.vm";
	}
	
	@Command("uploadFile")
	public String uploadFile() throws Exception {
		String uploadDir = "c:/UploadFile/";
		doUploadFile(uploadDir);
		return path + "/uploaded.vm";
	}



}
