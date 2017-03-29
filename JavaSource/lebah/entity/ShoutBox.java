package lebah.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

@Entity
@Table(name="shout_box")
public class ShoutBox {
	@Id
	@Column(name="uid", length=50)
	private String id;
	@Column(name="user_id", length=50)
	private String userId;
	@Column(name="category_id", length=100)
	private String categoryId;
	@Lob
	@Column(name="shout_text")
	private String shoutText;
	@Column(name="shout_date", length=100)
	private String shoutDate;
	@Column(name="user_name", length=100)
	private String userName;
	@Column(name="remote_addr", length=50)
	private String remoteAddr;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getShoutText() {
		return shoutText;
	}
	public void setShoutText(String shoutText) {
		this.shoutText = shoutText;
	}
	public String getShoutDate() {
		return shoutDate;
	}
	public void setShoutDate(String shoutDate) {
		this.shoutDate = shoutDate;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	
	

}
