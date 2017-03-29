package lebah.portal.action.element;

import lebah.portal.*;

public class ButtonElement extends ActionElement {
	
	public ButtonElement(String id) {
		uniqueId = id;
		type = "button";
	}
	
	public ButtonElement(String id,  AjaxBasedModule obs) {
		uniqueId = id;
		type = "button";
		observer = obs;
	}	
	
	public ButtonElement(String id, AjaxBasedModule obs, boolean checkRequired) {
		uniqueId = id;
		type = "button_validate";
		observer = obs;
		this.checkRequired = checkRequired;
	}	


}
