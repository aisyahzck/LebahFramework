/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.app;

import java.io.IOException;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.portal.MerakPortlet;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalb
 *
 * @version 1.0
 */
public class JspModule extends MerakPortlet implements lebah.portal.HtmlContainer {
	
	private String strUrl = "";
	private Vector replaceList = new Vector();
	
	//set the url
	public void setUrl(String strUrl) {
		this.strUrl = strUrl;
	}
	
	public void doView(HttpServletRequest req, HttpServletResponse res) throws javax.servlet.ServletException, IOException {

		if ( req.getParameter("page") != null && !"".equals(req.getParameter("page")) ) {
			strUrl = req.getParameter("page");	
		}
		
		System.out.println("url=" + strUrl);
				
		RequestDispatcher rd = req.getRequestDispatcher(strUrl);
		if ( rd != null )
			rd.include(req, res);
		else
			System.out.println("Request Dispatcher is NULL!");
	}
}

