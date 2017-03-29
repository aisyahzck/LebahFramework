package lebah.portal;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class UserAttributes {
	
	private static UserAttributes instance = null;
	private HttpServletRequest request = null;
	
	
	String islogin;
	String role;
	String username;
	String login;

	
	private UserAttributes() {

	}
	
	public static UserAttributes getInstance() {
		if ( instance == null ) instance = new UserAttributes();
		return instance;
	}
	

	public boolean isLogin() {
		request = (HttpServletRequest) Props.item.get("HTTP_REQUEST");
		HttpSession session = request.getSession();
		islogin = session.getAttribute("_portal_islogin") != null ? (String) session.getAttribute("_portal_islogin") : "";
		return islogin.equals("true");
	}
	
	public String getRole() {
		request = (HttpServletRequest) Props.item.get("HTTP_REQUEST");
		HttpSession session = request.getSession();
		role = session.getAttribute("_portal_role") != null ? (String) session.getAttribute("_portal_role") : "";
		if ( role == null ) role = session.getAttribute("myrole") != null ? (String) session.getAttribute("myrole") : "";
		return role;
	}
	
	public String getPortalRole() {
		return getRole();
	}
	
	public String getCurrentRole() {
		request = (HttpServletRequest) Props.item.get("HTTP_REQUEST");
		HttpSession session = request.getSession();
		role = session.getAttribute("myrole") != null ? (String) session.getAttribute("myrole") : "";
		if ( role == null || "".equals(role) ) role = (String) session.getAttribute("_portal_role");
		return role;
	}	
	
	public String getUserName() {
		request = (HttpServletRequest) Props.item.get("HTTP_REQUEST");
		HttpSession session = request.getSession();
		username = session.getAttribute("_portal_username") != null ? (String) session.getAttribute("_portal_username") : "";
		return username;
	}
	
	public String getLogin() {
		request = (HttpServletRequest) Props.item.get("HTTP_REQUEST");
		HttpSession session = request.getSession();
		login = session.getAttribute("_portal_login") != null ? (String) session.getAttribute("_portal_login") : "";
		return login;
	}
	
	public void setRole(String role) {
		request = (HttpServletRequest) Props.item.get("HTTP_REQUEST");
		HttpSession session = request.getSession();		
		session.setAttribute("_portal_role", role);
		session.setAttribute("myrole", role);
	}

}
