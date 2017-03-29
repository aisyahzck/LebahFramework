package lebah.portal.action;

public class AjaxTest extends AjaxModule {

	
	public String doAction() throws Exception {
		String vm = "vtl/ajax/test.vm";
		String command = request.getParameter("command");
		System.out.println(command);
		waiting(3);
		if ( "input1".equals(command)) {
			vm = "vtl/ajax/test1.vm";
			String name = request.getParameter("text1");
			System.out.println(name);
			context.put("name", name);
		} else if ( "input2".equals(command)) {
			vm = "vtl/ajax/test2.vm";		
				context.put("name", request.getParameter("input2"));
		} else if ( "all".equals(command)) {
			vm = "vtl/ajax/test.vm";
		}
		return vm;
	}
	
	public static void waiting (int n){
		long t0, t1;
        t0 =  System.currentTimeMillis();
        do{
            t1 = System.currentTimeMillis();
        } 
        while ((t1 - t0) < (n * 1000));
    }

}
