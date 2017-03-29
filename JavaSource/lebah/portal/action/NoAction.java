package lebah.portal.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import javax.naming.*;
public class NoAction implements lebah.portal.action.ActionTemplate {

	public boolean doAction(HttpServletRequest request, HttpServletResponse response, VelocityContext context, Context ctx) throws Exception {
		return true;
	}
	public String getView() {
		return "";
	}
	
}
