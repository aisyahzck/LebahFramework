package lebah.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity @IdClass(UserModuleTemplateId.class)
@Table(name="user_module_template")
public class UserModuleTemplate {


	@Id
	@Column(name="tab_id", length=100)
	private String tabId;
	@Id
	@Column(name="user_login", length=100)
	private String userLogin;
	@Id
	@Column(name="module_id", length=100)
	private String moduleId;
	@Column(name="sequence")
	private int sequence;
	@Column(name="module_custom_title", length=100)
	private String moduleCustomTitle;
	@Column(name="column_number")
	private int columnNumber;
	
	public String getTabId() {
		return tabId;
	}
	public void setTabId(String tabId) {
		this.tabId = tabId;
	}
	public String getUserLogin() {
		return userLogin;
	}
	public void setUserLogin(String userLogin) {
		this.userLogin = userLogin;
	}
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public String getModuleCustomTitle() {
		return moduleCustomTitle;
	}
	public void setModuleCustomTitle(String moduleCustomTitle) {
		this.moduleCustomTitle = moduleCustomTitle;
	}
	public int getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}
	
	
	
	
}
