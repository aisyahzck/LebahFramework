package lebah.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity @IdClass(UserTrackerId.class)
@Table(name="user_tracker")
public class UserTracker {

	@Id
	@Column(name="user_login", length=50)
	private String userLogin;
	@Id
	@Column(name="log_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date logDate;
	@Id
	@Column(name="remote_add", length=50)
	private String remoteAdd;


	@Column(name="log_year")
	private int logYear;
	@Column(name="log_month", length=50)
	private int logMonth;
	@Column(name="log_day", length=50)
	private int logDay;
	@Column(name="module_id", length=50)
	private String moduleId;
	@Column(name="module_class", length=100)
	private String moduleClass;
	@Column(name="module_name", length=100)
	private String moduleName;
	@Column(name="time12", length=50)
	private String time12;
	@Column(name="time24", length=50)
	private String time24;
	@Column(name="str_date", length=50)
	private String strDate;

	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
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
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getModuleClass() {
		return moduleClass;
	}
	public void setModuleClass(String moduleClass) {
		this.moduleClass = moduleClass;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public String getTime12() {
		return time12;
	}
	public void setTime12(String time12) {
		this.time12 = time12;
	}
	public String getTime24() {
		return time24;
	}
	public void setTime24(String time24) {
		this.time24 = time24;
	}
	public String getStrDate() {
		return strDate;
	}
	public void setStrDate(String strDate) {
		this.strDate = strDate;
	}
	public String getRemote_add() {
		return remoteAdd;
	}
	public void setRemote_add(String remote_add) {
		this.remoteAdd = remote_add;
	}
	public Date getLogDate() {
		return logDate;
	}
	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}
	
	

}
