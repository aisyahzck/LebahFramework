package lebah.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class ModulePopWindow extends lebah.portal.velocity.VTemplate {
	
	public ModulePopWindow(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
		super(engine, context, req, res);
	}
	
	public Template doTemplate() throws Exception {
		Template template = engine.getTemplate("vtl/main/popwin.vm");	
		return template;		
	}

}
