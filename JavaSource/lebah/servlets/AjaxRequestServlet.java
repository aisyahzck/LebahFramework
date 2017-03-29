package lebah.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.portal.db.AuthenticateUser;

public class AjaxRequestServlet  implements IServlet {
    
    public void doService(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        try {
            PrintWriter out = res.getWriter();
            System.out.println("in AJAX servlet!");
			String usrlogin = req.getParameter("username");
			String password = req.getParameter("password");
			
			AuthenticateUser auth = new AuthenticateUser(req);

			if ( auth.lookup(usrlogin, password) ) {
				
				out.print("login OK");
				
			}
			else {
				out.print("Access Denied....");
			}
			
        } catch ( Exception e ) {
        	e.printStackTrace();
        }
    }


}
