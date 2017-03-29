package lebah.portal.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;

public interface ActionModuleTemplate {
	
	boolean doAction(HttpServletRequest request, HttpServletResponse response, VelocityContext context) throws Exception;

}
