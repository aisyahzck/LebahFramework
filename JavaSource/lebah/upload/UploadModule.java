package lebah.upload;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.app.FilesRepository;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.velocity.Template;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalb
 * @version 1.0
 */
public class UploadModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/upload/upload_form.vm";
        
		String server = (String) session.getAttribute("_portal_server");
		String app = (String) session.getAttribute("_portal_appname");
		String http = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf("://") + 3);
        String serverUrl = http + server;
		context.put("portal_url", http + server + "/" + app);
        
        setShowVM(false);
        
    	int files = 1;
    	try {
    		files = Integer.parseInt(getParam("files"));
    	} catch ( Exception e ) {}
    	context.put("num_files", files);        
        
        String submit = getParam("command");
        if ( "uploadFile".equals(submit)) {
			String upload_dir = getParam("uploadDir");
			if ( !"".equals(upload_dir)) {
				upload_dir = upload_dir + "/";
			} else {
				upload_dir = FilesRepository.getUploadDir() + getId() + "/" + (String) session.getAttribute("_portal_login") + "/";
			}
			uploadFiles(session, upload_dir);
			template_name = "vtl/upload/files_uploaded.vm";
        }
        else {
        	session.setAttribute("uploaded_files", new ArrayList());
        	UploadProgress progress = (UploadProgress) session.getAttribute("upload_progress_" + session.getId());
        	if ( progress == null ) {
        		progress = new UploadProgress(request, response);
        		session.setAttribute("upload_progress_" + session.getId(), progress);
        	} 
        }
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    private void uploadFiles(final HttpSession session, String upload_dir) throws Exception {
    	final String sessionId = session.getId();
		java.io.File dir = new java.io.File(upload_dir);
		if ( !dir.exists() ) {
			dir.mkdirs();
		}    	
    	DiskFileItemFactory factory = new DiskFileItemFactory();
    	ServletFileUpload upload = new ServletFileUpload(factory);
    	
    	//upload.setSizeMax(yourMaxRequestSize);

    	ProgressListener progressListener = new ProgressListener(){
    		   private long megaBytes = -1;
    		   int itemNo = 0;

    		   public void update(long pBytesRead, long pContentLength, int pItems) {
    			    //
    			   if ( pItems != itemNo ) {
    				   itemNo = pItems;
    			   }
    			   UploadProgress progress = (UploadProgress) session.getAttribute("upload_progress_" + sessionId);
    			   if ( progress == null ) {
    		    		progress = new UploadProgress(request, response);
    		    		session.setAttribute("upload_progress_" + sessionId, progress);
    			   }
    		       long mBytes = pBytesRead / 1000000;
    		       if (megaBytes == mBytes) {
    		           return;
    		       }
    		       megaBytes = mBytes;
    		       progress.setTotalBytes(pContentLength);
    		       progress.setCurrentBytes(pBytesRead);
    		   }
    		};  
    	
    	upload.setProgressListener(progressListener);
    	
    	UploadProgress progress = (UploadProgress) session.getAttribute("upload_progress_" + sessionId);
    	List items = upload.parseRequest(request);
    	
    	Iterator itr = items.iterator();
    	List<String> files = new ArrayList<String>();
    	session.setAttribute("uploaded_files", files);
    	progress.setStatus("finished");
    	while(itr.hasNext()) {
    		FileItem item = (FileItem) itr.next();
    		
    		//check if the current item is a form field or an uploaded file
    		if(!item.isFormField()) {
    			if ( item.getName() != null && !"".equals(item.getName())) {
    				//InputStream is = item.getInputStream();
	    			File fullFile  = new File(item.getName());
	    			String filename = upload_dir + fullFile.getName();
	    			files.add(filename);
	    			File savedFile = new File(filename);
	    			item.write(savedFile);
    			}
    		}
    	}
    	
    	System.out.println("Finished uploading..");
   		progress.setStatus("done");
    }	
}
