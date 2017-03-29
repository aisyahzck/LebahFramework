package lebah.portal.action;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 */
public abstract class AjaxModule extends lebah.portal.AjaxBasedModule {

	protected String templateName = "vtl/action/ajax/main.vm";
	
	public String doTemplate2() throws Exception {
		context.put("currentAction", getParam("command"));
		context.put("view", doAction());
		return templateName;
	}

	public abstract String doAction() throws Exception;

}
