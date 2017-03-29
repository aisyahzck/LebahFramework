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

public class LoginServlet implements IServlet {
	
	private static Hashtable<String, List<Message>> chatMessages = new Hashtable<String, List<Message>>();
	private boolean isLoginSuccess;
	//private List<Message> messages = new ArrayList<Message>();
	private boolean isAccessDenied;
	

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	response.setContentType("text/javascrip");
    	HttpSession session = request.getSession();
    	PrintWriter out = response.getWriter();
    	
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	String result = "login FAILED";
    	JSON json = new JSON();
		try {
			AuthenticateUser auth = new AuthenticateUser(request);

			if ( auth.lookup(username, password) ) {
				//for chat module
				session.setAttribute("nickname", username);
				
				session.setAttribute("_portal_role", auth.getRole());
				session.setAttribute("_portal_username", auth.getUserName());
				session.setAttribute("_portal_login", auth.getUserLogin());
				session.setAttribute("_portal_islogin", "true");

				//CSS
				try {
					String css = UserPage.getCSS((String) session.getAttribute("_portal_login") );
					session.setAttribute("_portal_css", css);
				} catch ( DbException cssex ) {
					System.out.println("[DesktopController] Can't get CSS");
				}
				isLoginSuccess = true;
				
				json.add("result", "true");

			} else {
				session.setAttribute("_portal_role", "anon");
				session.setAttribute("_portal_username", "Anonymous");
				session.setAttribute("_portal_login", "anon");
				session.setAttribute("_portal_islogin", "false");
				isAccessDenied = true;
				json.add("result", "false");
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
    	
    	out.println(json.getJSONElements());
    	 
    }

}
