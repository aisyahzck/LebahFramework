package lebah.test;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

public class TestScriptaculousModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "vtl/scriptaculous/test2.vm";
        String submit = getParam("command");
        
        if ( "updateModules".equals(submit)) {
        	String[] moduleIds = request.getParameterValues("moduleIds");
        	if ( moduleIds != null ) {
        		for ( String moduleId : moduleIds ) {
        			System.out.println(moduleId);
        		}
        	}
        }
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    

}
