/**
 * @author Shaiful
 * @since Apr 27, 2009
 */
package lebah.portal.announcement;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.velocity.Template;

import lebah.db.Db;
import lebah.portal.Attributable;
import lebah.portal.velocity.VTemplate;

public class AnnouncementModule extends VTemplate implements Attributable {

	//private String className = this.getClass().getName();
	private HttpSession session;
	private String moduleId;
	private String templateFile;
	private String userId;
	private String category;
	private String selectedWeek;
	private final String TEMPLATE_MAIN = "vtl/portal/announcement/announcement_list.vm";
	private final String TEMPLATE_CREATE = "vtl/portal/announcement/announcement_create.vm";
	private final String TEMPLATE_READ = "vtl/portal/announcement/announcement_read.vm";
	private final String TEMPLATE_EDIT = "vtl/portal/announcement/announcement_edit.vm";
	private final String TEMPLATE_ATTACMENT = "vtl/portal/announcement/announcement_attachment.vm";
	private String[] names = {"Moderators", "Attachment"};
	private Hashtable values = new Hashtable();
	List fileItemsList = null;
	ServletRequestContext reqContext;
	
	public String[] getNames() {
		return names;
	}
	
	public Hashtable getValues() {
		return values;
	}
	
	public void setValues(Hashtable hashtable) {
		values = hashtable;
	}	
	
	public Template doTemplate() throws Exception {

		moduleId = getId();
		context.put("moduleId", moduleId);
		category = moduleId;
		session = request.getSession();
		
		userId = (String) session.getAttribute("_portal_login");
		//System.out.println("["+className+"] userId = " + userId);
		
		if (isModerator()) {
			context.put("isModerator", Boolean.TRUE);
		} else {
			context.put("isModerator", Boolean.FALSE);
		}
		
		String attachmentStatus = (String) values.get(names[1]);
		//System.out.println("["+className+"] attachmentStatus = "+attachmentStatus);
		
		if ("true".equals(attachmentStatus)) {
			context.put("allowAnnouncementHaveAttachment", Boolean.TRUE);
		} else {
			context.put("allowAnnouncementHaveAttachment", Boolean.FALSE);
		}
		
		// ====================================================
		// value of SelectedWeek must be carried on every page.
		// ====================================================
		
		ServletFileUpload servletFileUpload = null;
		String command = "";
		
		reqContext = new ServletRequestContext(request);
		/*
		 * Since there is a function to upload files, must determine 
		 * if the incoming request comes from a Multipart form.
		 */
		if (ServletFileUpload.isMultipartContent(reqContext)){
			//System.out.println("["+className+"] Getting form fields from Multipart form.");
			servletFileUpload = new ServletFileUpload(new DiskFileItemFactory());
			fileItemsList = servletFileUpload.parseRequest(request);
			
			/*
			 * If request is from a Multipart form, pick out required form fields.
			 */
			Iterator it = fileItemsList.iterator();
			while (it.hasNext()) {
				FileItem fileItemTemp = (FileItem) it.next();
				if (fileItemTemp.isFormField()) {
					if (fileItemTemp.getFieldName().equals("command_"+moduleId)) {
				        command = fileItemTemp.getString();
					} else if (fileItemTemp.getFieldName().equals("selected_week_"+moduleId)) {
						selectedWeek = fileItemTemp.getString();
					}
				}
			}
		} else {
			//System.out.println("["+className+"] Getting form fields from normal form.");
			command = request.getParameter("command_" + moduleId);
			selectedWeek = request.getParameter("selected_week_"+moduleId);
		}
		if (command == null) {
			command = "";
		}
		//System.out.println("["+className+"] command = " + command);
		
		if(selectedWeek == null) {
			selectedWeek = "0";
		}
		//System.out.println("["+className+"] selectedWeek = "+selectedWeek);
		context.put("selectedWeek", selectedWeek);
		
		if (command.equals("")) {
			getListOfAnnouncements();
			
		} else if (command.equals("READ ANNOUNCEMENT")) {
			readAnnouncement();
			
		} else if (command.equals("CREATE ANNOUNCEMENT")) {
			templateFile = this.TEMPLATE_CREATE;
			
		} else if (command.equals("SUBMIT ANNOUNCEMENT")) {
			submitAnnouncement();
			
		} else if (command.equals("EDIT ANNOUNCEMENT")) {
			editAnnouncement();
			
		} else if (command.equals("UPDATE ANNOUNCEMENT")) {
			updateAnnouncement();
			
		} else if (command.equals("DELETE ANNOUNCEMENT")) {
			deleteAnnouncement();
			
		} else if (command.equals("MANAGE ATTACHMENT")) {
			manageAttachment();
			
		} else if (command.equals("UPLOAD ATTACHMENT")) {
			uploadAttachment();
			
		} else if (command.equals("REMOVE ATTACHMENT")) {
			removeAttachment();
			
		} else {
			getListOfAnnouncements();
			
		}
		
		Template template = engine.getTemplate(templateFile);
		return template;
	}
	
	private boolean isModerator() {
		String userRole = (String) session.getAttribute("_portal_role");
		//System.out.println("["+className+"] userRole = " + userRole);
		
		String[] moderators = ((String) values.get(names[0])).split(",");
		boolean isModerator = false;
		if (moderators == null) {
			isModerator = false;
		} else {
			String moderator = null;
			for (int i = 0; i < moderators.length; i++) {
				moderator = moderators[i].trim();
				//System.out.println("["+className+"] moderator = " + moderator);
				if (moderator.equals(userRole)) {
					isModerator = true;
					break;
				}
			}
		}
		//System.out.println("["+className+"] isModerator = " + isModerator);
		return isModerator;
	}
	
	private void getListOfAnnouncements() throws Exception {
		Calendar cal = Calendar.getInstance();
		Date today = cal.getTime();
		Date dateFrom = null;
		Date dateUntil = null;
		
		int noOfDaysFromToday = Integer.parseInt(selectedWeek) * 7;
		
		if (noOfDaysFromToday == 0) {
			dateUntil = today;
		} else {
			cal.add(Calendar.DATE, -noOfDaysFromToday);
			dateUntil = cal.getTime();
		}
		cal.add(Calendar.DATE, -6);
		dateFrom = cal.getTime();
		
		//System.out.println("["+className+"] dateFrom = "+dateFrom.toString());
		//System.out.println("["+className+"] dateUntil = "+dateUntil.toString());
		
		Db db = null;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			db = new Db();
			String sql = "SELECT id, headline, author, date_of_post " +
					"FROM portal_announcement " +
					"WHERE category = ? AND date_of_post BETWEEN ? AND ? " +
					"ORDER BY date_of_post DESC";
			Connection conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, category);
			ps.setString(2, df.format(dateFrom));
			ps.setString(3, df.format(dateUntil));
			
			ResultSet rs = ps.executeQuery();
			Announcement ann = null;
			Vector<Announcement> list = new Vector<Announcement>();
			while (rs.next()) {
				ann = new Announcement();
				ann.setId(rs.getString("id"));
				ann.setHeadline(rs.getString("headline"));
				ann.setAuthor(rs.getString("author"));
				df = new SimpleDateFormat("dd-MM-yyyy");
				ann.setDateOfPost(df.format(rs.getDate("date_of_post")));
				
				list.add(ann);
			}
			
			context.put("listOfAnnouncements", list);
			
		} finally {
			if (db != null) {
				db.close();
			}
		}
		templateFile = this.TEMPLATE_MAIN;
	}
	
	private synchronized void submitAnnouncement() throws Exception {
		String headline = request.getParameter("headline_"+moduleId);
		String content = request.getParameter("fck_content_"+moduleId);

		//System.out.println("["+className+"] headline = "+headline);
		//System.out.println("["+className+"] content = "+content);
		
		Calendar cal = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Db db = null;
		try {
			String sql = "INSERT INTO portal_announcement (category, headline, " +
					"content, author, date_of_post) VALUES (?,?,?,?,?)";
			db = new Db();
			Connection conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, category);
			ps.setString(2, headline);
			ps.setString(3, content);
			ps.setString(4, userId);
			ps.setString(5, df.format(cal.getTime()));
			ps.executeUpdate();
			
		} finally {
			if (db != null) {
				db.close();
			}
		}
		getListOfAnnouncements();
	}
	
	private Vector<Attachment> getAttachments(String announcementId) throws Exception {
		Vector<Attachment> list = new Vector<Attachment>();
		File uploadFolder = getUploadFolder(announcementId);
		if (uploadFolder.isDirectory()) {
			File[] fileList = uploadFolder.listFiles();
			File file = null;
			Attachment attachment = null;
			for (int i = 0; i < fileList.length; i++) {
				file = fileList[i];
				attachment = new Attachment();
				// NOTE: Window OS use backslash "\" for file system, therefore have to 
				// change it to forward slash "/" when displaying on the web.
				attachment.setPath(getUploadFolderRelativePath(uploadFolder).replace('\\', '/'));
				attachment.setName(file.getName());
				list.add(attachment);
			}
		}
		return list;
	}
	
	private Announcement getAnnouncement(String announcementId) throws Exception {
		Announcement ann = null;
		Db db = null;
		try {
			String sql = "SELECT headline, content, author, date_of_post " +
					"FROM portal_announcement " +
					"WHERE id = ?";
			db = new Db();
			Connection conn = db.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, announcementId);
			
			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				ann = new Announcement(announcementId);
				ann.setHeadline(rs.getString("headline"));
				ann.setContent(rs.getString("content"));
				ann.setAuthor(rs.getString("author"));
				ann.setDateOfPost(df.format(rs.getDate("date_of_post")));
			}
			
		} finally {
			if (db != null) {
				db.close();
			}
		}
		return ann;
	}
	
	private void readAnnouncement() throws Exception {
		Announcement ann = null;
		Vector<Attachment> attachments = null;
		String announcementId = request.getParameter("announcement_id_"+moduleId);
		if (announcementId == null || announcementId.equals("")) {
			ann = new Announcement();
			ann.setHeadline("ERROR: Unable to retrieve announcement.");
			attachments = new Vector<Attachment>();
		} else {
			ann = getAnnouncement(announcementId);
			attachments = getAttachments(announcementId);
		}
		context.put("announcement", ann);
		context.put("attachments", attachments);
		
		templateFile = this.TEMPLATE_READ;
	}
	
	private void editAnnouncement() throws Exception {
		String announcementId = request.getParameter("announcement_id_"+moduleId);
		if (announcementId == null || announcementId.equals("")) {
			getListOfAnnouncements();
		} else {
			Announcement ann = getAnnouncement(announcementId);
			Vector<Attachment> attachments = getAttachments(announcementId);
			context.put("announcement", ann);
			context.put("attachments", attachments);
			templateFile = this.TEMPLATE_EDIT;
		}
	}
	
	private synchronized void updateAnnouncement() throws Exception {
		String announcementId = request.getParameter("announcement_id_"+moduleId);
		if (announcementId == null || announcementId.equals("")) {
			getListOfAnnouncements();
		} else {
			String headline = request.getParameter("headline_"+moduleId);
			String content = request.getParameter("fck_content_"+moduleId);
			
			Db db = null;
			try {
				String sql = "UPDATE portal_announcement SET headline = ?, content = ? " +
						"WHERE id = ?";
				db = new Db();
				Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, headline);
				ps.setString(2, content);
				ps.setString(3, announcementId);
				ps.executeUpdate();
			} finally {
				if (db != null) {
					db.close();
				}
			}
			readAnnouncement();
		}
	}
	
	private synchronized void deleteAnnouncement() throws Exception {
		String announcementId = request.getParameter("announcement_id_"+moduleId);
		if (announcementId != null && !announcementId.equals("")) {
			Db db = null;
			try {
				String sql = "DELETE FROM portal_announcement WHERE id = ?";
				db = new Db();
				Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ps.setString(1, announcementId);
				ps.executeUpdate();
				
				// Deleting folder and files.
				File uploadFolder = getUploadFolder(announcementId);
				deleteDirectory(uploadFolder);
			} finally {
				if (db != null) {
					db.close();
				}
			}
		}
		getListOfAnnouncements();
	}
	
	private void manageAttachment() throws Exception {
		String announcementId = "";
		if (ServletFileUpload.isMultipartContent(reqContext)){
			Iterator it = fileItemsList.iterator();
			while (it.hasNext()) {
				FileItem fileItemTemp = (FileItem) it.next();
				if (fileItemTemp.isFormField()) {
					if (fileItemTemp.getFieldName().equals("announcement_id_"+moduleId)) {
						announcementId = fileItemTemp.getString();
						break;
					}
				}
			}
			
		} else {
			announcementId = request.getParameter("announcement_id_"+moduleId);
		}
		//System.err.println("["+className+".manageAttachment] announcementId = " + announcementId);
		
		if (announcementId == null || announcementId.equals("")) {
			getListOfAnnouncements();
		} else {
			Announcement announcement = getAnnouncement(announcementId);
			Vector attachments = getAttachments(announcementId);
			
			context.put("announcement", announcement);
			context.put("attachments", attachments);
			
			templateFile = this.TEMPLATE_ATTACMENT;
		}
	}
	
	private synchronized void uploadAttachment() throws Exception {
		//System.err.println("["+className+".uploadAttachment] Start Uploading Files");
		String announcementId = "";
		Vector<FileItem> uploadList = new Vector<FileItem>();
		Iterator it = fileItemsList.iterator();
		while (it.hasNext()) {
			FileItem fileItemTemp = (FileItem) it.next();
			if (fileItemTemp.isFormField()) {
				if (fileItemTemp.getFieldName().equals("announcement_id_"+moduleId)) {
					announcementId = fileItemTemp.getString();
					//System.err.println("["+className+".uploadAttachment] announcementId = " + announcementId);
				}
			} else {
				uploadList.addElement(fileItemTemp);
			}
		}
		// Doing multiple upload.
		if (!uploadList.isEmpty()) {
			FileItem fileItem = null;
			String fileName = "";
			String filePath = "";
			File uploadFolder = getUploadFolder(announcementId);
			
			// If announcement upload folder does not exist, create it.
			if (!uploadFolder.exists()) {
				uploadFolder.mkdirs();
			}
			
			for (int i = 0; i < uploadList.size(); i++) {
				fileItem = uploadList.get(i);
				filePath = fileItem.getName(); // The location of the file on PC.
				//System.err.println("["+className+".uploadAttachment] filePath = " + filePath);
				fileName = FilenameUtils.getName(filePath); // The filename.
				//System.err.println("["+className+".uploadAttachment] fileName = " + fileName);
				if (fileItem.getSize() > 0) {
					File saveTo = new File(uploadFolder.getAbsolutePath() + 
							File.separator + fileName);
					//System.err.println("["+className+".uploadAttachment] saveTo = " + saveTo.getPath());
					try {
						//System.err.println("["+className+".uploadAttachment] Saving File.");
						fileItem.write(saveTo);
					} catch (Exception ex) {
						//System.err.println("["+className+".uploadAttachment] An error occured while saving the uploaded file.");
						//System.err.println(ex.getMessage());
					}
				}
			}
		}
		manageAttachment();
	}
	
	private synchronized void removeAttachment() throws Exception {
		String announcementId = request.getParameter("announcement_id_"+moduleId);
		String filename = request.getParameter("filename_"+moduleId);
		
		if (announcementId != null && filename != null && !announcementId.equals("") && !filename.equals("")) {
			File uploadFolder = getUploadFolder(announcementId);
			File attachment = new File(uploadFolder.getPath() + File.separator + filename);
			if (attachment.exists()) {
				attachment.delete();
			}
		}
		manageAttachment();
	}

	/*
	 * This method should get the path of the application's ROOT folder.
	 * Example: [tomcat home]/webapps/ROOT/[app html folder]/
	 */
	private String getAppRootPath() throws Exception {
		ResourceBundle rb = ResourceBundle.getBundle("files");
		String root = rb.getString("dir");
		//System.err.println("["+className+".getAppRootPath] root path = " + root);
		if (root == null) root = "";
		return root;
	}
	
	private synchronized File getUploadFolder (String announcementId) throws Exception {
		File uploadFolder = new File(getAppRootPath() + "resources" + 
				File.separator + moduleId + File.separator + announcementId);
		//System.err.println("["+className+".getUploadFolder] uploadFolder = " + uploadFolder.getPath());
		return uploadFolder;
	}
	
	/*
	 * This method will extract the relative path from the upload folder's
	 * full path. 
	 * i.e. from [tomcat home]/webapps/ROOT/[app html folder]/[upload folder]
	 * into [app html folder]/[upload folder]
	 * Method will return [app html folder]/[upload folder].
	 */
	private synchronized String getUploadFolderRelativePath(File uploadFolder) throws Exception {
		String path = uploadFolder.getPath();
		String relPath = "";
		if (path.indexOf("ROOT") > -1) {
			relPath = path.substring(path.indexOf("ROOT") + 5);
		} else {
			relPath = path;
		}
		//System.err.println("["+className+".getUploadFolderRelativePath] relative path = "+relPath);
		return relPath;
	}
	/*
	 * Recursively delete files and directory from a parent folder.
	 */
	private synchronized boolean deleteDirectory(File folder) {
		if (folder.isDirectory()) {
			String[] children = folder.list();
			for (int i=0; i<children.length; i++) {
				deleteDirectory(new java.io.File(folder, children[i]));
			}
		}
		return folder.delete();
	}
}
