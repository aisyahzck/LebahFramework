package lebah.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="module_htmlcontainer")
public class ModuleHtmlContainer {
	
	@Id
	@Column(name="module_id", length=100)
	private String id;
	@Column(name="html_url", length=100)
	private String url;
//	@ManyToOne 
//	@JoinColumn(name="module_id", insertable=false, updatable=false)
//	private Module module;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
//	public Module getModule() {
//		return module;
//	}
//	public void setModule(Module module) {
//		this.module = module;
//	}
	
	

}
