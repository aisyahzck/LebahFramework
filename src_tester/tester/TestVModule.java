package tester;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

import lebah.portal.velocity.VTemplate;

public class TestVModule extends VTemplate {

    public Template doTemplate() throws Exception {
        String targetPage = "tester/testv.vm";  // DEFAULT PAGE
        HttpSession session = request.getSession();
        Template template = engine.getTemplate(targetPage);
        return template;
    }
}
