package lebah.app.site;

public class SiteContent {
	
	private String id;
	private String title;
	private SiteTab tab;
	
	public SiteContent() {
		
	}
	
	public SiteContent(String id, String title, SiteTab tab) {
		this.id = id;
		this.title = title;
		this.tab = tab;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public SiteTab getTab() {
		return tab;
	}
	public void setTab(SiteTab tab) {
		this.tab = tab;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	

}
