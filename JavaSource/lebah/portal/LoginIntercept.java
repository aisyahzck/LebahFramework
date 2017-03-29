package lebah.portal;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;

public abstract class LoginIntercept {
	
	public abstract void process(HttpServletRequest req, HttpServletResponse res, VelocityContext context) throws ServletException, IOException ;

}
