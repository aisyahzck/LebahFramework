package lebah.portal.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestWrapper {
	
	HttpServletRequest request;

	public RequestWrapper(HttpServletRequest req) {
		request = req;
	}

	public Object getParameter(String name) {
		return request.getParameter(name);
	}
	
	public String[] getParameterValues(String name) {
		return request.getParameterValues(name);
	}		
	
	public Object getAttribute(String name) {
		return request.getAttribute(name);
	}
	
	public void setAttribute(String name, Object value) {
		request.setAttribute(name, value);
	}
	
	public HttpSession getSession() {
		return request.getSession();
	}
	
	public Object getUploadedFiles() {
		return request.getSession().getAttribute("uploaded_files");
	}

}
