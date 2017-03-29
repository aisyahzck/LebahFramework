package lebah.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="rss_module")
public class RssModule {


	@Id
	@Column(name="module_id", length=100)
	private String moduleId;
	@Column(name="rss_source", length=200)
	private String rssSource;
	
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getRssSource() {
		return rssSource;
	}
	public void setRssSource(String rssSource) {
		this.rssSource = rssSource;
	}
	
	
	
}
