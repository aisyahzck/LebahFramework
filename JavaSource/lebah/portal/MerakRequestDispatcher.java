/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MerakRequestDispatcher implements PortletRequestDispatcher {
	
	RequestDispatcher rd;
	
	public MerakRequestDispatcher(RequestDispatcher rd) {
		this.rd = rd;
	}

	public void include(RenderRequest req, RenderResponse res) throws PortletException, java.io.IOException {
		
		HttpServletRequest request = ((MerakRequest) req).getHttpServletRequest();
		HttpServletResponse response = ((MerakResponse) res).getHttpServletResponse();
		
		try {
			rd.include(request, response);
		} catch ( javax.servlet.ServletException ex ) {
			throw new PortletException( ex.getMessage() );
		}
	}


}
