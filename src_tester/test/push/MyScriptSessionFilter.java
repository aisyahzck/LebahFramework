package test.push;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;

public class MyScriptSessionFilter implements ScriptSessionFilter {
	
	private String attributeName;
	
    public MyScriptSessionFilter(String attributeName) {
        this.attributeName = attributeName;
    }

   
    public boolean match(ScriptSession session) {
        Object check = session.getAttribute(attributeName);
        return (check != null && check.equals(Boolean.TRUE));
    }

    
}
