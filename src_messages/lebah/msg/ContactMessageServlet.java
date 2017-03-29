package lebah.msg;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.DbException;
import lebah.portal.db.AuthenticateUser;
import lebah.portal.db.UserPage;
import lebah.servlets.IServlet;

public class ContactMessageServlet implements IServlet {
	

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	response.setContentType("text/javascrip");
    	HttpSession session = request.getSession();
    	PrintWriter out = response.getWriter();
    	
    	String name = request.getParameter("name");
    	String email = request.getParameter("email");
    	String organization = request.getParameter("organization");
    	String phone = request.getParameter("phone");
    	String message = request.getParameter("message");
    	
    	System.out.println("name=" + name);
    	System.out.println("email=" + email);
    	System.out.println("organization=" + organization);
    	System.out.println("phone=" + phone);
    	System.out.println("message=" + message);
    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	JSON json = new JSON();
    	json.add("result", "ok");
    	out.println(json.getJSONElements());
    }


}
