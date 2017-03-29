package lebah.util;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.db.RegisterUser;
import lebah.repository.Thumbnail;
import lebah.upload.FileLocation;
import lebah.upload.UploadUtil;



public class UserPhotoManager {
	
	public static String saveUserPhoto(HttpSession session, HttpServletRequest request, HttpServletResponse response, String user_id) throws Exception {
		String dir = FileLocation.getPhotosDir() + "photos/" + user_id;
		
		File file = new File(dir);
		if ( !file.exists() ) file.mkdirs();
		
		String photoFileName = UploadUtil.uploadFile(session.getServletContext(), request, response, dir);
		//context.put("imageFile", dir + photoFileName);

		//create thumb
		file = new File(dir + "/thumb");
		if ( !file.exists() ) file.mkdirs();
		Thumbnail.create(dir + photoFileName, dir + "/thumb"+ photoFileName, 200, 160, 100);
		
		//create avatar
		file = new File(dir + "/avatar");
		if ( !file.exists() ) file.mkdirs();
		Thumbnail.create(dir + photoFileName, dir + "/avatar"+ photoFileName, 100, 60, 100);            
		RegisterUser.savePhoto(user_id, photoFileName);
		return photoFileName;
	}

}
