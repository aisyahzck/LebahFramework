package lebah.upload;

import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.app.FilesRepository;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.velocity.Template;
import java.io.*;
import java.util.*;

public class FileUploadModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/test/upload.vm";
        
        setShowVM(false);
        
    	int files = 1;
    	try {
    		files = Integer.parseInt(getParam("files"));
    	} catch ( Exception e ) {}
    	context.put("num_files", files);        
        
        String submit = getParam("command");
        if ( "uploadFile".equals(submit)) {
            uploadFiles(session);
			template_name = "vtl/test/files_uploaded.vm";
        }
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    private void uploadFiles(HttpSession session) throws Exception {
    	DiskFileItemFactory factory = new DiskFileItemFactory();
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	//upload.setSizeMax(yourMaxRequestSize);
    	List items = upload.parseRequest(request);  	
    	Iterator itr = items.iterator();
    	while(itr.hasNext()) {
    		FileItem item = (FileItem) itr.next();
    		// check if the current item is a form field or an uploaded file
    		if(!item.isFormField()) {
    			if ( item.getName() != null && !"".equals(item.getName())) {
	    			File fullFile  = new File(item.getName());
	    			String upload_dir = FilesRepository.getUploadDir() + getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
	    			java.io.File dir = new java.io.File(upload_dir);
	    			if ( !dir.exists() ) {
	    				dir.mkdirs();
	    			}    			
	    			String filename = upload_dir + fullFile.getName();
	    			File savedFile = new File(filename);
	    			item.write(savedFile);
    			}
    		}
    	}
    }
    
    

    

}
