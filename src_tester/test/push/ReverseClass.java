package test.push;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletContext;

import org.directwebremoting.ServerContext;
import org.directwebremoting.ServerContextFactory;
import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.proxy.dwr.Util;

public class ReverseClass {
	   private int count = 0;
	   WebContext webContext = WebContextFactory.get();
	   ServletContext servletContext = webContext.getServletContext();
	   ServerContext serverContext = ServerContextFactory.get(servletContext);

	   /**
	   * This method continually calls the update method utill the
	   * for loop completes
	   */
	   public void callReverseDWR() {
	      System.out.println(" Ur in callReverseDWR ");
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

	   /**
	   * This method updates page with <ul id="updates">
	   * using dwr reverse ajax
	   */
	   public void update(int i) {
		   System.out.println("update.." + i);
		   try {
			   List<Data> messages = new ArrayList<Data>();
			   messages.add(new Data("testing" + count++));
			   Collection sessions =  serverContext.getScriptSessionsByPage("/lebah4/push/index.html");
			   Util util = new Util(sessions);
			   util.addOptions("updates", messages, "value");

		   } catch (Exception e) {
			   System.out.println("Error in Update");
			   e.printStackTrace();
		   }
	   }
}