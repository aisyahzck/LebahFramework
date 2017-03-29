/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public abstract class MerakPortlet extends javax.servlet.http.HttpServlet {
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	
	public void setEnvironment(HttpServletRequest req, HttpServletResponse res) throws Exception {
		this.request = req;
		this.response = res;
	}
	
	public abstract void doView(HttpServletRequest req, HttpServletResponse res) throws Exception;
	
	/*
	public void doView() throws Exception {
		MerakResponse mres = new MerakResponse();
		mres.setWriter(response.getWriter());
		doView(mres);	
	}
	*/
	
}
