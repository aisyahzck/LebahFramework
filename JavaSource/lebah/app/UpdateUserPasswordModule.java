package lebah.app;

import javax.servlet.http.HttpSession;

import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.portal.db.PrepareUser;
import lebah.portal.db.RegisterUser;
import lebah.portal.element.User;

public class UpdateUserPasswordModule extends LebahModule {
	
	private String path = "vtl/admin";
	private HttpSession session;
	
	public void preProcess() {
		session = request.getSession();
	}

	@Override
	public String start() {
		context.remove("operation");
		String user_login = (String) session.getAttribute("_portal_login");
		User user = null;
		try {
			user = PrepareUser.getUserById(user_login);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ( user == null ) {
			return path + "/null.vm";
		}

		String user_name = user.getName();
		String user_password = user.getPassword();
		String user_role = user.getRole();
		String login_alt = user.getAlternateLogin();
		
		context.put("user_login", user_login);
		context.put("user_name", user_name);
		context.put("user_password", user_password);
		context.put("user_role", user_role);	
		context.put("user_login_alt", login_alt);

		return path + "/userPassword.vm";
	}
	
	@Command("update")
	public String updateUser() throws Exception {
	    context.put("operation", "update");
		String user_login = (String) session.getAttribute("_portal_login");
		String user_name = getParam("user_name");
		String user_password = getParam("user_password");			
		String user_login_alt = getParam("user_login_alt");
		
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

		User user = PrepareUser.getUserById(user_login);
		
		context.put("user_login", user_login);
		context.put("user_name", user_name);
		context.put("user_password", user_password);
		context.put("user_role", user.getRole());	
		context.put("user_login_alt", user.getAlternateLogin());

		return path + "/userPassword.vm";
	}

}
