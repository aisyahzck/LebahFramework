package lebah.entity;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="forum")
public class Forum {

	@Id
	@Column(name="id", length=50)
	private String id;
	@Column(name="parent_id", length=50)
	private String parentId;
	@Column(name="category_id", length=50)
	private String categoryId;
	@Column(name="member_id", length=50)
	private String memberId;
	@Column(name="posted_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date postedDate;
	@Column(name="title", length=255)
	private String title;
	@Column(name="description", length=255)
	private String description;
	@Lob
	@Column(name="content")
	private String content;
	@Column(name="is_parent")
	private int isParent;
	@Column(name="is_delete")
	private int isDelete;
	@Lob
	@Column(name="message_text")
	private String messageText;
	@Column(name="subject_id", length=50)
	private String subjectId;
	@Column(name="classroom_id", length=50)
	private String classroomId;
	@Column(name="rate")
	
	private int rate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getMemberId() {
		return memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getIsParent() {
		return isParent;
	}
	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public String getMessageText() {
		return messageText;
	}
	public void setMessageText(String messageText) {
		this.messageText = messageText;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getClassroomId() {
		return classroomId;
	}
	public void setClassroomId(String classroomId) {
		this.classroomId = classroomId;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	
	

}
