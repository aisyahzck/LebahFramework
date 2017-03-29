/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.portal;

import java.io.IOException;
import java.util.Hashtable;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.velocity.*;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;


public abstract class ModuleWrapperPortlet extends GenericPortlet {
	
	VelocityEngine engine;
	VelocityContext context;
	HttpServletRequest request;
	HttpServletResponse response;
	protected VTemplate module;
	
	public abstract void runModule() throws Exception;
	
	public void doView(RenderRequest req, RenderResponse res) throws PortletException, IOException {
		try {
			HttpServletRequest request = ((MerakRequest) req).getHttpServletRequest();
			HttpServletResponse response = ((MerakResponse) res).getHttpServletResponse();
			HttpSession session = request.getSession();
			context = (VelocityContext) session.getAttribute("_VELOCITY_CONTEXT");
			engine = (VelocityEngine) session.getAttribute("_VELOCITY_ENGINE");
			runModule();
			module.setEnvironment(engine, context, request, response);
			module.print();

		} catch ( Exception e) {
			System.out.println(e.getMessage());
		}

	}
	
}
