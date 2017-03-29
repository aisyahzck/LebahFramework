package sb.ajax;

import javax.servlet.http.HttpSession;

import lebah.portal.AjaxBasedModule;

import org.apache.velocity.Template;

public class AjaxTestModule extends AjaxBasedModule {
	
    public String doTemplate2() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        context.put("text", "");
        System.out.println(submit);
        if ( "ajaxTest".equals(submit)) {
        	String text = getParam("ajaxtext");
        	context.put("text", text);
        }
        
        return "vtl/ajax/ajax_test.vm";
    }

}
