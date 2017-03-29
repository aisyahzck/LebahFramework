/**
 * 
 */
package lebah.listeners;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 */
public class LoginRequest {
	
	private String sessionId;
	private String remoteAddr;
	private Date dateTime;
	private boolean success;
	private String username;
	
	public LoginRequest(HttpServletRequest request) {
		sessionId = request.getSession().getId();
		remoteAddr = request.getRemoteAddr();
		dateTime = new Date();
	}
	
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public String getRemoteAddr() {
		return remoteAddr;
	}
	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDateTimeStamp() {
		return new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(dateTime);
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

}
