/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class PortalLoginModule extends lebah.portal.velocity.VTemplate {
			
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();	
		
		session.setAttribute("inCollabModule", "false");
		
		Template template = engine.getTemplate("vtl/login.vm");	
		return template;		
	}
	
}
