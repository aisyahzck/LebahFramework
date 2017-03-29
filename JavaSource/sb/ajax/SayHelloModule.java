package sb.ajax;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

public class SayHelloModule extends lebah.portal.AjaxBasedModule {
	
    public String doTemplate2() throws Exception {
        context.put("name", "");
        String submit = getParam("command");
        if ( "sayHello".equals(submit)) {
        	String name = getParam("hello-name");
        	context.put("name", name);
        	return "vtl/ajax/hello.vm";
        } else if ( "sayHello2".equals(submit)) {
        	String name = getParam("hello-name");
        	context.put("name", name);
        	return "vtl/ajax/hello2.vm";
        }
        return "vtl/ajax/hello.vm";
    }
}
