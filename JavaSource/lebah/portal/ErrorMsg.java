/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ErrorMsg extends lebah.portal.velocity.VTemplate {
	
	private String errorStr = "";

	public ErrorMsg(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
		super(engine, context, req, res);
	}
	
	public void setError(String s) {
		errorStr = s;
	}
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		context.put("errormessage", errorStr);
			
		Template template = engine.getTemplate("vtl/admin/errormessage.vm");	
		return template;		
	}
}
