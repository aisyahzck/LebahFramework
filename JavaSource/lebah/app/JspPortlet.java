/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.app;

import java.io.IOException;
import java.util.Vector;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class JspPortlet extends GenericPortlet implements lebah.portal.HtmlContainer {
	
	private String strUrl = "";
	private Vector replaceList = new Vector();
	
	//set the url
	public void setUrl(String strUrl) {
		this.strUrl = strUrl;
	}
	
	public void doView(RenderRequest req, RenderResponse res) throws PortletException, IOException {
		System.out.println("url=" + strUrl);
		/*
		if ( req.getParameter("page") != null && !"".equals(req.getParameter("page")) ) {
			strUrl = req.getParameter("page");	
		}
		*/
		PortletRequestDispatcher rd = getPortletConfig().getPortletContext().getRequestDispatcher(strUrl);
		rd.include(req, res);
	}
}
