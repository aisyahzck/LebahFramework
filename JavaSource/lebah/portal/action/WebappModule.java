package lebah.portal.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public abstract class WebappModule extends LebahModule {
	
	public void doUploadFile(String uploadDir) throws Exception {
		String divUploadStatusName = getParam("divUploadStatusName");
		context.put("divUploadStatusName", divUploadStatusName);
		String documentId = getParam("documentId");
		String savedFileName = doUploadFile(documentId, uploadDir);
		context.put("savedFileName", savedFileName);
	}
	
	public String doUploadFile(String documentId, String uploadDir) throws Exception {
		File dir = new File(uploadDir);
		if ( !dir.exists() ) dir.mkdir();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem)itr.next();
			if ((!(item.isFormField())) && (item.getName() != null) && (!("".equals(item.getName())))) {
				if ( documentId.equals(item.getFieldName())) {
					files.add(item);
				}
			}
		}
		String savedName = "";
		for ( FileItem item : files ) {
			String fileName = item.getName();
			savedName = uploadDir + fileName;
			item.write(new File(savedName));
		}
		return savedName;
	}	
	
	public String doUploadMultipleFiles(String[] documentIds, String uploadDir) throws Exception {
		String divUploadStatusName = getParam("divUploadStatusName");
		context.put("divUploadStatusName", divUploadStatusName);

		File dir = new File(uploadDir);
		if ( !dir.exists() ) dir.mkdir();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem)itr.next();
			if ((!(item.isFormField())) && (item.getName() != null) && (!("".equals(item.getName())))) {
				for ( String documentId : documentIds ) {
					if ( documentId.equals(item.getFieldName())) {
						files.add(item);
					}
				}
			}
		}
		String savedName = "";
		for ( FileItem item : files ) {
			String fileName = item.getName();
			savedName = uploadDir + fileName;
			item.write(new File(savedName));
		}
		return savedName;
	}	

}
