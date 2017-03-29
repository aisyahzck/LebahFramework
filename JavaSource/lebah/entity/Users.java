package lebah.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name="users")
public class Users {

	@Id
	@Column(name="user_login", length=100)
	private String login;
	@Column(name="user_password", length=100)
	private String password;
	@Column(name="user_name", length=200)
	private String username;
	@Column(name="user_role", length=100)
	private String role;
	@Column(name="user_login_alt", length=50)
	private String loginAlternate;
	@Column(name="user_address", length=255)
	private String address;
	@Column(name="user_ip_address", length=50)
	private String ipAddress;
	@Column(name="date_registered")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateRegistered;
	@Column(name="avatar", length=255)
	private String avatar;
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getLoginAlternate() {
		return loginAlternate;
	}
	public void setLoginAlternate(String loginAlternate) {
		this.loginAlternate = loginAlternate;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public Date getDateRegistered() {
		return dateRegistered;
	}
	public void setDateRegistered(Date dateRegistered) {
		this.dateRegistered = dateRegistered;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	
	
	
}
