package lebah.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.db.AuthenticateUser;

public class XLoginServlet implements IServlet {
    
    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        boolean isLoginSuccess = false;
        String securityToken = "";
		try {
			AuthenticateUser auth = new AuthenticateUser(request);

			if ( auth.lookup(username, password) ) {
				session.setAttribute("_portal_role", auth.getRole());
				session.setAttribute("_portal_username", auth.getUserName());
				session.setAttribute("_portal_login", auth.getUserLogin());
				session.setAttribute("_portal_islogin", "true");
				securityToken = (String) session.getAttribute("securityToken");
				if ( securityToken == null || "".equals(securityToken) ) {
					securityToken = lebah.db.UniqueID.getUID();
					session.setAttribute("securityToken", securityToken);
				}
				isLoginSuccess = true;

			} else {
				session.setAttribute("_portal_role", "anon");
				session.setAttribute("_portal_username", "Anonymous");
				session.setAttribute("_portal_login", "anon");
				session.setAttribute("_portal_islogin", "false");
				
			}
		} catch ( Exception ex ) {

		}
		if ( isLoginSuccess ){
			System.out.println("login success!" + securityToken);
			response.sendRedirect("../x.html?" + securityToken);
		} else {
			System.out.println("login failed!");
			session.invalidate();
			response.sendRedirect("../x.html");
		}
    }


}
