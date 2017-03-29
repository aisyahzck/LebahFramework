package test.push;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.directwebremoting.ServerContext;
import org.directwebremoting.proxy.dwr.Util;

public class Service {
	
	 private int count = 0;

	public void update(ServerContext wctx) {
	   List<Data> messages = new ArrayList<Data>();
	   messages.add(new Data("testing" + count++));
	   System.out.println(count);
	   //Collection sessions = wctx.getAllScriptSessions();
	   Collection sessions = wctx.getScriptSessionsByPage("../push/index.html");
	   Util utilAll = new Util(sessions);
	   utilAll.addOptions("updates", messages, "value");
	 }

}
