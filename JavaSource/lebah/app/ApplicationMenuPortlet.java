package lebah.app;

import java.util.Hashtable;

import lebah.portal.ApplicationMenuWidgetBase;
import lebah.portal.Attributable;
import lebah.portal.ClassLoadManager;
import lebah.portal.velocity.VTemplate;

public class ApplicationMenuPortlet extends ApplicationMenuWidgetBase implements Attributable {
	
	protected String[] names = {"VMName"};
	protected Hashtable values = new Hashtable();	
	
	@Override
	public String doTemplate2() throws Exception {
		String command = getParam("command");
		if ( "__loadModule__".equals(command)) {
			loadModule(getParam("loadModuleName"));
			return "vtl/main/application-content.vm";
		}
		String vm = values.get(names[0]) != null ? (String) values.get(names[0]) : "";
		context.put("menu_template", vm);
		return "vtl/main/application-menu-template.vm";
	}
	
	private void loadModule(String module) throws Exception {
		VTemplate app = (VTemplate) ClassLoadManager.load(module); // load(CustomClass.getName(module), module, request.getRequestedSessionId());
		request.getSession().setAttribute("_module_requested", module);
		app.setEnvironment(engine, context, request, response);
		app.setServletContext(this.getServletContext());	
		app.setServletConfig(this.getServletConfig());				
		app.setId(module);	
		app.setDiv(false);
		
		//System.out.println("Loading Module = " + app);
		String appContent = app.getBuffer().toString();
		//
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
