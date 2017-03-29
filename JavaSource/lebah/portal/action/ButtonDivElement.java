package lebah.portal.action;

import lebah.portal.*;
import lebah.portal.action.element.ActionElement;

public class ButtonDivElement  extends ActionElement{
	
	public ButtonDivElement(String id, AjaxBasedModule obs) {
		uniqueId = id;
		type = "button_div";
		observer = obs;
	}	

}
