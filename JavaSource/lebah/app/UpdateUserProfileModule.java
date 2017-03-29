/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import javax.servlet.http.HttpSession;

import lebah.db.DbException;
import lebah.portal.db.PrepareUser;
import lebah.portal.db.RegisterUser;
import lebah.portal.element.User;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class UpdateUserProfileModule extends lebah.portal.velocity.VTemplate {
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		doJob(session);
		Template template = engine.getTemplate("vtl/admin/updateuserprofile.vm");	
		return template;		
	}
	
	private void doJob(HttpSession session) throws Exception {
	    context.put("operation", "");
		String submit = getParam("command");
		if ( "update".equals(submit)) {
		    context.put("operation", "update");
			String user_login = (String) session.getAttribute("_portal_login");
			String user_name = getParam("user_name");
			String user_password = getParam("user_password");			
			//String user_role = getParam("user_role");
			String user_login_alt = getParam("user_login_alt");
			
			/*
			context.put("user_login", user_login);
			context.put("user_name", user_name);
			context.put("user_password", user_password);
			context.put("user_role", user_role);
			*/
			
			if ( "".equals(user_password) ) {
				if ( RegisterUser.update(user_name, user_login, user_login_alt) ) {
				    context.put("updateUserSuccess", new Boolean(true));
				} else {
				    context.put("updateUserSuccess", new Boolean(false));
				}			    
			}
			else {
				if ( RegisterUser.update(user_name, user_login, user_password, user_login_alt) ) {
				    context.put("updateUserSuccess", new Boolean(true));
				} else {
				    context.put("updateUserSuccess", new Boolean(false));
				}
			}
			
			//update session
			session.setAttribute("_portal_username", user_name);
			session.setAttribute("_portal_login", user_login);

		}
		
		String user_login = (String) session.getAttribute("_portal_login");
		User user = PrepareUser.getUserById(user_login);
		if ( user == null ) throw new DbException("User Is NULL!");

		String user_name = user.getName();
		String user_password = user.getPassword();
		String user_role = user.getRole();
		String login_alt = user.getAlternateLogin();
		
		context.put("user_login", user_login);
		context.put("user_name", user_name);
		context.put("user_password", user_password);
		context.put("user_role", user_role);	
		context.put("user_login_alt", login_alt);


	}
	
	
	

}
