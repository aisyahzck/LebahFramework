package lebah.app;

import java.util.Hashtable;

import lebah.portal.Attributable;
import lebah.portal.ClassLoadManager;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

public class ApplicationContainerPortlet extends VTemplate  implements Attributable {
	
	protected String[] names = {"DefaultModule"};
	protected Hashtable values = new Hashtable();	

	/*
	@Override
	public String start() {
		return "vtl/main/application-container.vm";
	}
	*/
	
	public Template doTemplate() throws Exception {
		
		
		
		String module = request.getSession().getAttribute("_module_requested") != null ?
				(String) request.getSession().getAttribute("_module_requested") : "";
		
		if ( !"".equals(module)) {
			loadModule(module);
		}
		else {
			module = values.get(names[0]) != null ? (String) values.get(names[0]) : "";
			if ( !"".equals(module)) loadModule(module);
		}
		
        Template template = engine.getTemplate("vtl/main/application-container.vm");  
        return template;

	}
	
	private void loadModule(String module) throws Exception {
		VTemplate app = (VTemplate) ClassLoadManager.load(module); // load(CustomClass.getName(module), module, request.getRequestedSessionId());
		request.getSession().setAttribute("_module_requested", module);
		app.setEnvironment(engine, context, request, response);
		app.setServletContext(this.getServletContext());	
		app.setServletConfig(this.getServletConfig());				
		app.setId(module);	
		app.setDiv(false);
		String appContent = app.getBuffer().toString();
		context.put("app-content", appContent);
	}

	@Override
	public String[] getNames() {
		return names;
	}

	@Override
	public Hashtable getValues() {
		return values;
	}

	@Override
	public void setValues(Hashtable hashtable) {
		values = hashtable;
	}

}
