/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.app;

import org.apache.velocity.Template;

public class TemplateModule extends lebah.portal.velocity.VTemplate {

	public Template doTemplate() throws Exception {
		Template template = engine.getTemplate("vtl/template.vm");	
		return template;		
	}

}
