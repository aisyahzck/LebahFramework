package lebah.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyAjaxServlet implements IServlet {
    
    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            PrintWriter out = response.getWriter();
            System.out.println("in AJAX servlet!");
            out.println("Hello World!");
			
        } catch ( Exception e ) {
        	e.printStackTrace();
        }
    }

}
