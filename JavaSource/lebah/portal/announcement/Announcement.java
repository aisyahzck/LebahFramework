/**
 * @author Shaiful
 * @since Apr 27, 2009
 */
package lebah.portal.announcement;

import java.util.Vector;

public class Announcement {

	private String id;
	private String headline;
	private String content;
	private String dateOfPost;
	private Vector<Attachment> attachments;
	private String author;

	public Announcement() {
		
	}
	
	public Announcement(String id) {
		setId(id);
	}
	
	public String getHeadline() {
		if (headline == null) {
			return "";
		} else {
			return headline;
		}
	}

	public void setHeadline(String headline) {
		if (headline == null) {
			this.headline = "";
		} else {
			this.headline = headline;
		}
	}

	public String getAuthor() {
		if (author == null) {
			return "";
		} else {
			return author;
		}
	}

	public void setAuthor(String author) {
		if (author == null) {
			this.author = "";
		} else {
			this.author = author;
		}
	}

	public boolean hasAttachment() {
		if (attachments == null) {
			return false;
		} else {
			if (attachments.isEmpty()) {
				return false;
			} else {
				return true;
			}
		}
	}

	public Vector<Attachment> getAttachments() {
		if (attachments == null) {
			attachments = new Vector<Attachment>();
		}
		return attachments;
	}

	public void setAttachments(Attachment attachment) {
		if (attachments == null) {
			attachments = new Vector<Attachment>();
		}
		if (attachment != null) {
			attachments.add(attachment);
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
