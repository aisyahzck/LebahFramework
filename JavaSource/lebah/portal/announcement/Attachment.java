/**
 * @author Shaiful
 * @since Apr 27, 2009
 */
package lebah.portal.announcement;

public class Attachment {

	private String path;
	private String name;
	public String getPath() {
		if (path == null) {
			return "";
		} else {
			return path;
		}
	}
	public void setPath(String path) {
		if (path == null) {
			this.path = "";
		} else {
			this.path = path;
		}
	}
	public String getName() {
		if (name == null) {
			return "";
		} else {
			return name;
		}
	}
	public void setName(String name) {
		if (name == null) {
			this.name = "";
		} else {
			this.name = name;
		}
	}
	

}
