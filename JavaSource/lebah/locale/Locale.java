package lebah.locale;

public class Locale {
	
	private String id;
	private String category;
	private String name;
	private String value;
	
	public Locale() {}
	
	public Locale(String id, String category, String name, String value) {
		this.id = id;
		this.category = category;
		this.name = name;
		this.value = value;
		
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	

}
