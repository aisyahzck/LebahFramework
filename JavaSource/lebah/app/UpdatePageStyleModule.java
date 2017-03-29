/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.db.UserPage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class UpdatePageStyleModule extends lebah.portal.velocity.VTemplate {
	
	public UpdatePageStyleModule() {
		
	}		
	
	public UpdatePageStyleModule(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
		super(engine, context, req, res);
	}	
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		doJob(session);
		Template template = engine.getTemplate("vtl/theme/updatepagestyle.vm");	
		return template;		
	}
	
	private void doJob(HttpSession session) throws Exception {
		//check if form submitted by getting values
		String theme = getParam("theme");
		String usrlogin = (String) session.getAttribute("_portal_login");
		if ( !"".equals(theme) ) {
			UserPage.saveTheme(usrlogin, theme);
			//change the current theme
			session.setAttribute("_portal_css", theme);
		}
		
		//get list of page themes
		Vector themes = UserPage.getPageThemeList();
		//put into context
		context.put("themes", themes);
	}
	
	
	

}
