package lebah.app.site;

import java.util.*;

public class SiteTab {
	
	private String id;
	private String title;
	private Vector<SiteContent> contents;
	
	public SiteTab() {
		
	}
	
	public SiteTab(String id, String title) {
		this.id = id;
		this.title = title;
	}
	
	public void addContent(SiteContent content) {
		if ( contents == null ) contents = new Vector<SiteContent>();
		contents.add(content);
	}
	
	public Vector<SiteContent> getContents() {
		return contents;
	}
	public void setContents(Vector<SiteContent> contents) {
		this.contents = contents;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	
	

}
