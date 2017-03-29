package lebah.app.shout;

import javax.servlet.http.HttpSession;

import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

public class ClassroomShoutModule extends VTemplate {

    public Template doTemplate() throws Exception {
        String template_name = "vtl/collab/shout/classroom_shout_box.vm";
        Template template = engine.getTemplate(template_name);    
        return template;        
    }

}
