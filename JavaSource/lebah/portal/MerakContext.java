/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import javax.portlet.PortletContext;
import javax.portlet.PortletRequestDispatcher;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MerakContext implements PortletContext {
	
	protected String serverInfo;
	protected PortletRequestDispatcher requestDispatcher;
	protected PortletRequestDispatcher namedDispatcher;
	protected java.io.InputStream resourceAsStream;
	protected int majorVersion;
	protected int minorVersion;
	protected String mimeType;
	protected String realPath;
	protected java.util.Set resourcePaths;
	protected java.net.URL resource;
	protected java.lang.Object attribute;
	protected java.util.Enumeration attributeNames;
	protected java.lang.String initParameter;
	protected java.util.Enumeration initParameterNames;
	protected String portletContextName;	
	protected HttpServletRequest httpServletRequest;

 
	public String getServerInfo (){
		return serverInfo;
	}
	
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	
	public PortletRequestDispatcher getRequestDispatcher(String path) {
		RequestDispatcher rd = httpServletRequest.getRequestDispatcher(path);
		MerakRequestDispatcher requestDispatcher = new MerakRequestDispatcher(rd);
		return requestDispatcher;
	}
	
	public PortletRequestDispatcher getNamedDispatcher(String name) {
		return namedDispatcher;
	}
	
	public java.io.InputStream getResourceAsStream (String path) {
		return resourceAsStream;
	}
	
	public int getMajorVersion () {
		return majorVersion;
	}
	
	public int getMinorVersion () {
		return minorVersion;
	}
	
	public String getMimeType(String file) {
		return mimeType;
	}
	
	public String getRealPath(String path) {
		return realPath;
	}
	
	public java.util.Set getResourcePaths(String path) {
		return resourcePaths;
	}
	
	public java.net.URL getResource(String path) throws java.net.MalformedURLException {
		return resource;
	}
	
	public java.lang.Object getAttribute(java.lang.String name) {
		return attribute;
	}
	
	public java.util.Enumeration getAttributeNames() {
		return attributeNames;
	}
	
	public java.lang.String getInitParameter(java.lang.String name) {
		return initParameter;
	}
	
	public java.util.Enumeration getInitParameterNames() {
		return initParameterNames;
	}
	
	public void log(java.lang.String msg) {
		
	}
	
	public void log(java.lang.String message, java.lang.Throwable throwable) {
		
	}
	
	
	public void removeAttribute(java.lang.String name) {
		
	}
		
	
	public void setAttribute(java.lang.String name, java.lang.Object object) {
		
	}
	
	
	public String getPortletContextName() {
		return portletContextName;
	}

}
