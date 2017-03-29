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
@Table(name="guestbook")
public class Guestbook {

	@Id
	@Column(name="uid", length=50)
	private String id;
	@Column(name="posted_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date postedDate;
	@Column(name="posted_by", length=50)
	private String postedBy;
	@Lob
	@Column(name="message")
	private String message;
	@Column(name="module_id", length=100)
	private String moduleId;
	@Column(name="remote_address", length=50)
	private String remoteAddress;
	@Column(name="email", length=50)
	private String email;
	@Column(name="homepage", length=100)
	private String homepage;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Date getPostedDate() {
		return postedDate;
	}
	public void setPostedDate(Date postedDate) {
		this.postedDate = postedDate;
	}
	public String getPostedBy() {
		return postedBy;
	}
	public void setPostedBy(String postedBy) {
		this.postedBy = postedBy;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getHomepage() {
		return homepage;
	}
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}
	
	

}
