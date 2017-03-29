package lebah.portal;

import java.util.Vector;

import lebah.portal.db.User;
import lebah.portal.db.UserData;

import org.apache.velocity.Template;

public class SecondaryUserRoleModule extends lebah.portal.velocity.VTemplate {

	public Template doTemplate() throws Exception {
		
		String vm = "vtl/main/secondary_role_get_user.vm";
		
		
		String submit = getParam("command");
		
		context.put("roleUpdated", false);
		
		if ( "getUser".equals(submit) ) {
			String userId = getParam("user_id"); 
			User user = UserData.getUser(userId);
			context.put("user", user);
			vm = "vtl/main/secondary_role.vm";

		}
		else if ( "updateRoles".equals(submit)) {
			String userId = getParam("user_id");
			String[] roles = request.getParameterValues("roleList");
			UserData.updateSecondaryRoles(userId, roles);
			User user = UserData.getUser(userId);
			context.put("user", user);
			context.put("roleUpdated", true);
			vm = "vtl/main/secondary_role_get_user.vm";
		}
		Vector roleList = UserData.getRoleList();
		context.put("roleList", roleList);
		
		Template template = engine.getTemplate(vm);	
		return template;		
	}

}
