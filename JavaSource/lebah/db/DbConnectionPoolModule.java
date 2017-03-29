package lebah.db;

import javax.servlet.http.HttpSession;
import java.util.*;
import org.apache.velocity.Template;

public class DbConnectionPoolModule extends lebah.portal.velocity.VTemplate {
	
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		String template_name = "vtl/main/db_connection.vm";
		String submit = getParam("command");
		
		if ( "releaseAll".equals(submit)) {
			ConnectionPool.releaseAll();
		}
		
		Vector pools = ConnectionPool.getConnectionList();
		context.put("pools", pools);
		
		
		Template template = engine.getTemplate(template_name);	
		return template;
	}

}
