package lebah.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @IdClass(ForumAttachmentId.class)
@Table(name="forum_attachment")
public class ForumAttachment {
	
	@Id
	@Column(name="forum_id", length=50)
	private String forumId;
	@Id
	@Column(name="file_name", length=255)
	private String fileName;
	@Column(name="directory", length=255)
	private String directory;
//	@ManyToOne @JoinColumn(name="forum_id", insertable=false, updatable=false)
//	private Forum forum;
	
	public String getForumId() {
		return forumId;
	}
	public void setForumId(String forumId) {
		this.forumId = forumId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getDirectory() {
		return directory;
	}
	public void setDirectory(String directory) {
		this.directory = directory;
	}
//	public Forum getForum() {
//		return forum;
//	}
//	public void setForum(Forum forum) {
//		this.forum = forum;
//	}
	
	
	

}
