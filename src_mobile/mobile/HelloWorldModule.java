package mobile;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

public class HelloWorldModule  extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(session, submit);
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    
    String prepareTemplate(HttpSession session, String submit) throws Exception {
        String template_name = "vtl/mobile/hello_world.vm";
        
        return template_name;
    }


}
