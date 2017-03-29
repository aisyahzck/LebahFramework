package lebah.app;

import java.util.Vector;

public class Message {

	private String id;
	private String title;
	private String content;
	private String dateOfPost;
	private Vector<String> attachedFiles;
	private String attachmentFolder;
	private String classroomId;
	
	public boolean hasAttachment() {
		if (attachedFiles.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public Vector<String> getAttachment() {
		if (attachedFiles == null) {
			attachedFiles = new Vector<String>();
		}
		return attachedFiles;
	}
	public void setAttachment(String fileName) {
		if (attachedFiles == null) {
			attachedFiles = new Vector<String>();
		}
		if (fileName != null) {
			attachedFiles.add(fileName);
		}
	}
	public String getAttachmentFolder() {
		if (attachmentFolder == null) {
			return "";
		} else {
			return attachmentFolder;
		}
	}
	public void setAttachmentFolder(String attachmentFolder) {
		if (attachmentFolder == null) {
			this.attachmentFolder = "";
		} else {
			this.attachmentFolder = attachmentFolder;
		}
	}
	public String getContent() {
		if (content == null) {
			return "";
		} else {
			return content;
		}
	}
	public void setContent(String content) {
		if (content == null) {
			this.content = "";
		} else {
			this.content = content;
		}
	}
	public String getId() {
		if (id == null) {
			return "";
		} else {
			return id;
		}
	}
	public void setId(String id) {
		if (id == null) {
			this.id = "";
		} else {
			this.id = id;
		}
	}
	public String getTitle() {
		if (title == null) {
			return "";
		} else {
			return title;
		}
	}
	public void setTitle(String title) {
		if (title == null) {
			this.title = "";
		} else {
			this.title = title;
		}
	}
	public String getDateOfPost() {
		if (dateOfPost == null) {
			return "";
		} else {
			return dateOfPost;
		}
	}
	public void setDateOfPost(String dateOfPost) {
		if (dateOfPost == null) {
			this.dateOfPost = "";
		} else {
			this.dateOfPost = dateOfPost;
		}
	}
	
}
