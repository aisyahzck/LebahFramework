package lebah.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity @IdClass(AttrModuleDataId.class)
@Table(name="attr_module_data")
public class AttrModuleData {
	
	@Id
	@Column(name="module_id")
	private String moduleId;
	@Id
	@Column(name="attribute_name")
	private String attributeName;
	@Column(name="attribute_value")
	private String attributeValue;
//	@ManyToOne
//	@JoinColumn(name="module_id", insertable=false, updatable=false)
//	private Module module;
	
	public String getModuleId() {
		return moduleId;
	}
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public String getAttributeValue() {
		return attributeValue;
	}
	public void setAttributeValue(String attributeValue) {
		this.attributeValue = attributeValue;
	}
//	public Module getModule() {
//		return module;
//	}
//	public void setModule(Module module) {
//		this.module = module;
//	}
	
	
	

}
