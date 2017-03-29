package lebah.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="role_module")
public class RoleModule {
	@Id
	@Column(name="module_id", length=100)
	private String moduleId;
	@Id
	@Column(name="user_role", length=100)
	private String userRole;
//	@ManyToOne
//	@JoinColumn(name="module_id", insertable=false, updatable=false)
//	private Module module;
//	@ManyToOne
//	@JoinColumn(name="user_role", insertable=false, updatable=false)
//	private Role role;
	
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getUserRole() {
		return userRole;
	}
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
//	public Module getModule() {
//		return module;
//	}
//	public void setModule(Module module) {
//		this.module = module;
//	}
//	public Role getRole() {
//		return role;
//	}
//	public void setRole(Role role) {
//		this.role = role;
//	}
	
	

}
