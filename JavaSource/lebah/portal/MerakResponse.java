/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import javax.portlet.PortletURL;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MerakResponse implements RenderResponse {
	
	protected HttpServletResponse httpServletResponse;
	protected String contentType;
	protected String namespace;
	protected String title;
	protected java.util.Locale locale;
	protected int bufferSize;
	protected String characterEncoding;
	protected PortletURL renderPortletURL;
	protected PortletURL actionPortletURL;
	protected java.io.PrintWriter printWriter;
	protected java.io.OutputStream outputStream;
	protected boolean committed;
	
	public HttpServletResponse getHttpServletResponse() {
		return httpServletResponse;
	}
	
	public void addProperty(String key, String value) {
		
	}
	
	public void setProperty(String key, String value) {
		
	}
	public String encodeURL (String path) { 
		return "";
	}

	public String getContentType () {
		return contentType;	
	}
	
	public PortletURL createRenderURL () {
		return renderPortletURL;	
	}
	
	public PortletURL createActionURL () {
		return actionPortletURL;
	}
	
	public String getNamespace () {
		return namespace;	
	}
	
	public void setTitle(String title) {
		this.title = title;	
	};
	
	public void setContentType(String type) {
		contentType = type;
	}
	
	public String getCharacterEncoding() {
		return characterEncoding;	
	}
	
	public java.io.PrintWriter getWriter() throws java.io.IOException {
		return printWriter;	
	}
	
	public void setWriter(java.io.PrintWriter printWriter) {
		this.printWriter = printWriter;
	}
	
	public java.util.Locale getLocale() {
		return locale;
	}
	
	public void setBufferSize(int size) {
		bufferSize = size;
	}
	
	public int getBufferSize() {
		return bufferSize;
	}
	
	public void flushBuffer() throws java.io.IOException {
		
	}
	
	public void resetBuffer() {
		
	}
	
	public boolean isCommitted() {
		return committed;	
	}
	
	public void reset() {
		
	}
	
	public java.io.OutputStream getPortletOutputStream() throws java.io.IOException {
		return outputStream;	
	}

}
