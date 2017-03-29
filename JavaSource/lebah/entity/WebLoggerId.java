package lebah.entity;

import java.util.Date;

public class WebLoggerId {
	
	private String remoteAdd;
	private int userName;
	private Date logDate;
	
	public String getRemoteAdd() {
		return remoteAdd;
	}
	public void setRemoteAdd(String remoteAdd) {
		this.remoteAdd = remoteAdd;
	}
	public int getUserName() {
		return userName;
	}
	public void setUserName(int userName) {
		this.userName = userName;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	

}
