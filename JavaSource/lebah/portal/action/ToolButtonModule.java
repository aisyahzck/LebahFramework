package lebah.portal.action;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpSession;

public abstract class ToolButtonModule extends AjaxModule {
	
	String path = "vtl/ajax/";
	protected String vm = "";
	HttpSession session;

	
	public String doAction() throws Exception {
		context.put("_formName", formName);
		session = request.getSession();
		context.put("dateFormat", new SimpleDateFormat("yyyy-MM-dd"));
		context.put("timeFormat", new SimpleDateFormat("hh:mm a"));	
		context.put("numFormat", new DecimalFormat("#,###,###.00"));	
		String command = request.getParameter("command");
		System.out.println(command);
		if ( command == null ) start();
		else if ( "_init_page".equals(command)) initPage();
		else if ( "_new".equals(command)) doNew();
		else if ( "_save".equals(command)) doSave();
		else if ( "_list".equals(command)) doList();
		else if ( "_edit".equals(command)) doEdit();
		else {
			System.out.println("this is other command.. which is " + command);
			doCommand(command);
		}
		waiting(1);
		return vm;
	}

	public abstract void doList();
	public abstract void initPage();
	public abstract void doSave();
	public abstract void doNew();
	public abstract void doEdit();
	public abstract void doCommand(String command);

	private void start() {
		vm = path + "ajax_toolbutton.vm";
	}
	
	

	static void waiting (int n){
		long t0, t1;
        t0 =  System.currentTimeMillis();
        do{
            t1 = System.currentTimeMillis();
        } 
        while ((t1 - t0) < (n * 1000));
    }
	
	

}
