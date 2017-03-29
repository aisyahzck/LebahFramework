package lebah.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AjaxInterceptorServlet  extends HttpServlet  {
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		out.println("Please Wait...");
		waiting(1);
		getServletContext().getRequestDispatcher("/div" + req.getPathInfo()).forward(req, res);
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
