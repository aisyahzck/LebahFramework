/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.portal.handler.DeviceHandler;
import lebahlic.Activation;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class InterceptorServlet extends HttpServlet {
	
	private Hashtable<String, DeviceHandler> devHandlers = new Hashtable<String, DeviceHandler>();
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		Enumeration params = getServletConfig().getInitParameterNames();
		Thread t = Thread.currentThread();
		for (; params.hasMoreElements(); ) {
            String name = (String) params.nextElement();
            String className = getServletConfig().getInitParameter(name);
			ClassLoader cl = t.getContextClassLoader();
			try {
				Class loadedClass = cl.loadClass(className);
				if ( "LOGIN_INTERCEPT".equals(name)) {
					lebah.portal.LoginIntercept interceptor = (lebah.portal.LoginIntercept) loadedClass.newInstance();
					Props.item.put("LOGIN_INTERCEPT", interceptor);
				}
				else {
					addDeviceHandler(name, (DeviceHandler) loadedClass.newInstance());
				}
			} catch ( Exception e ) {
				//no need to throw an exception
				System.out.println("ERROR CLASS NOT FOUND: " + e.getMessage());  //class not found?
			}
        }		
	}

	public void addDeviceHandler(String name, DeviceHandler handler) {
		devHandlers.put(name, handler);
	}
	
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		Props.item.put("HTTP_REQUEST", req);
		Props.item.put("HTTP_RESPONSE", res);
		req.getSession().setAttribute("__expired_", "");
		if ( !Activation.expire(req, res) ) {
			getDeviceHandler(req.getHeader("User-Agent")).process(getServletContext(), req, res);
		}
	}
	
	private DeviceHandler getDeviceHandler(String userAgent) {
		String name = "NORMAL";
		Enumeration keys = devHandlers.keys();
		for (; keys.hasMoreElements(); ) {
			String key = (String) keys.nextElement();
			if ( userAgent.indexOf(key) > -1 ) {
				name = key;
				break;
			}
		}
		DeviceHandler handler = (DeviceHandler) devHandlers.get(name);
		return handler;
	}

}
