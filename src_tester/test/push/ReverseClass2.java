package test.push;

import org.directwebremoting.Browser;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.ui.dwr.Util;

public class ReverseClass2 {
	
	
	public void callReverseAjax() {
		try {
			for (int i = 0; i < 10; i++) {
				update(i);
				try {
					Thread.sleep(3000);
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
		String page = ServerContextFactory.get().getContextPath() + "/push/index.html";
		Browser.withPage(page, new Runnable() {
			public void run() {
				System.out.println("count = " + i);
				Util.setValue("updates", "test " + i);
			}
		});
	}

}
