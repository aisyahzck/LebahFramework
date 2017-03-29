/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.app;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

public class ProtectionModule extends lebah.portal.AjaxBasedModule {

		
	public String doTemplate2() throws Exception {
		HttpSession session = request.getSession();
		
		String submit = getParam("command");
		if ( "addModule".equals(submit)) {
			String moduleName = getParam("moduleName");
			String[] roles = request.getParameterValues("roles");
			if ( roles != null ) {
				
			}
		}
	
		//Template template = engine.getTemplate("vtl/main/module_protection.vm");	
		//return template;
		return "vtl/main/module_protection.vm";
	}

}
