/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.portal;

import javax.portlet.PortalContext;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MerakRequest implements RenderRequest {
	
	protected HttpServletRequest httpServletRequest;
	protected boolean windowStateAllowed;
	protected boolean portletModeAllowed;
	protected PortletMode portletMode;
	protected WindowState windowState;
	protected PortletPreferences preferences;
	protected PortletSession portletSession;
	protected String property;
	protected java.util.Enumeration properties; 
	protected java.util.Enumeration propertyNames;
	protected PortalContext portalContext;
	protected java.lang.String authType;
	protected String contextPath;
	protected java.lang.String remoteUser;
	protected java.security.Principal userPrincipal;
	protected boolean userInRole;
	protected Object attribute;
	protected java.util.Enumeration attributeNames;
	protected String parameter;
	protected java.util.Enumeration parameterNames;
	protected String[] parameterValues;
	protected java.util.Map parameterMap;
	protected boolean secure;
	protected String requestedSessionId;
	protected boolean requestedSessionIdValid;
	protected String responseContentType;
	protected java.util.Enumeration responseContentTypes;
	protected java.util.Locale locale;
	protected java.util.Enumeration locales;
	protected String scheme;
	protected String serverName;
	protected int serverPort;	
	
	public HttpServletRequest getHttpServletRequest() {
		return httpServletRequest;
	}
	
	public boolean isWindowStateAllowed(WindowState state) {
		return windowStateAllowed;	
	}
	
	public boolean isPortletModeAllowed(PortletMode mode) {
		return portletModeAllowed;	
	}
	
	public PortletMode getPortletMode () {
		return portletMode;	
	}
	
	public WindowState getWindowState () {
		return windowState;	
	}
	
	public PortletPreferences getPreferences () {
		return preferences;	
	}
	
	public PortletSession getPortletSession () {
		return portletSession;	
	}
	
	public PortletSession getPortletSession (boolean create) {
		return portletSession;	
	}
	
	public String getProperty(String name) {
		return property;	
	}
	
	public java.util.Enumeration getProperties(String name) {
		return properties;	
	} 
	
	public java.util.Enumeration getPropertyNames() {
		return propertyNames;	
	}
	
	public PortalContext getPortalContext() {
		return portalContext;	
	}
	
	public java.lang.String getAuthType() {
		return authType;
	}
	
	public String getContextPath() {
		return contextPath;
	}
	
	public java.lang.String getRemoteUser() {
		return remoteUser;
	}
	
	public java.security.Principal getUserPrincipal() {
		return userPrincipal;
	}
	
	public boolean isUserInRole(java.lang.String role) {
		return userInRole;
	}
	
	public Object getAttribute(String name) {
		return httpServletRequest.getAttribute(name);
	}
	
	public java.util.Enumeration getAttributeNames() {
		return httpServletRequest.getAttributeNames();
	}
	
	public String getParameter(String name) {
		return httpServletRequest.getParameter(name);
	}
	
	public java.util.Enumeration getParameterNames() {
		return httpServletRequest.getParameterNames();
	}
	
	public String[] getParameterValues(String name) {
		return httpServletRequest.getParameterValues(name);
	}
	
	public java.util.Map getParameterMap() {
		return httpServletRequest.getParameterMap();
	}
	
	public boolean isSecure() {
		return secure;
	}
	
	public void setAttribute(String name, Object o) {
		httpServletRequest.setAttribute(name, o);
	}
	
	public void removeAttribute(String name) {
		httpServletRequest.removeAttribute(name);
	}
	
	public String getRequestedSessionId() {
		return requestedSessionId;
	}
	
	public boolean isRequestedSessionIdValid() {
		return requestedSessionIdValid;
	}
	
	public String getResponseContentType() {
		return responseContentType;
	}
	
	public java.util.Enumeration getResponseContentTypes() {
		return responseContentTypes;
	}
	
	public java.util.Locale getLocale() {
		return locale;	
	}
	
	public java.util.Enumeration getLocales() {
		return locales;	
	}
	
	public String getScheme() {
		return scheme;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public int getServerPort() {
		return serverPort;
	}
}
