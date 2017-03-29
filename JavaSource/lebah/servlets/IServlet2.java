package lebah.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

public interface IServlet2 {
	
	void doService(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws IOException, ServletException;

}
