package lebah.portal.action.element;

import lebah.portal.*;

public class HrefElement extends ActionElement {

	public HrefElement(String id){
		uniqueId = id;
		type = "href";
	}
	
	public HrefElement(String id, AjaxBasedModule obs) {
		uniqueId = id;
		type = "href";
		observer = obs;
	}	
}
