package lebah.entity;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity @IdClass(WebLoggerId.class)
@Table(name="web_logger")
public class WebLogger {

	@Id
	@Column(name="remote_add",  length=50)
	private String remoteAdd;
	@Id
	@Column(name="user_name",  length=50)
	private int userName;
	@Id
	@Column(name="log_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date logDate;
	
	@Column(name="log_year")
	private int logYear;
	@Column(name="log_month")
	private int logMonth;
	@Column(name="log_day")
	private int logDay;
	@Column(name="log_string",  length=255)
	private String logString;
	
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
	public int getLogYear() {
		return logYear;
	}
	public void setLogYear(int logYear) {
		this.logYear = logYear;
	}
	public int getLogMonth() {
		return logMonth;
	}
	public void setLogMonth(int logMonth) {
		this.logMonth = logMonth;
	}
	public int getLogDay() {
		return logDay;
	}
	public void setLogDay(int logDay) {
		this.logDay = logDay;
	}
	public String getLogString() {
		return logString;
	}
	public void setLogString(String logString) {
		this.logString = logString;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	

}
