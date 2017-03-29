package test.push;

import org.directwebremoting.Browser;
import org.directwebremoting.ScriptSession;
import org.directwebremoting.ScriptSessionFilter;
import org.directwebremoting.ScriptSessions;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.ui.dwr.Util;

public class ReverseClass3 {
	
	
	public void callReverseAjax() {
		
		// Add the attribute into the ScriptSession sometime before using the Filter.
		ScriptSession scriptSession = WebContextFactory.get().getScriptSession();
		String attributeName = "test3";
		scriptSession.setAttribute(attributeName, true);
		
		try {
			for (int i = 0; i < 10; i++) {
				update(i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			System.out.println("Error in callReverseDWR");
			e.printStackTrace();
		}

	}
	
	public void update(final int i) {
		String page = ServerContextFactory.get().getContextPath() + "/push/test3.html";
		ScriptSessionFilter filter = new MyScriptSessionFilter("test3");
		Browser.withPageFiltered(page, filter, new Runnable() {
			public void run() {
				System.out.println("count = " + i);
				Util.setValue("div1", "test3-" + i);
		        //ScriptSessions.addFunctionCall("yourJavaScriptFunctionName", arg1, arg2, etc.);
		        ScriptSessions.addFunctionCall("myFunc", i);

			}
		});
	}

}
