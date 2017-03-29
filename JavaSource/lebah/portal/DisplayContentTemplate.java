package lebah.portal;

import java.io.PrintWriter;
import java.util.Hashtable;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.DbException;
import lebah.portal.db.CustomClass;
import lebah.portal.db.UserPage;
import lebah.portal.db.UserTrackerLog;
import lebah.portal.element.Module2;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public abstract class DisplayContentTemplate {
	
	public abstract void display(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception;
	
	public void doMaximized(VelocityEngine engine,
			VelocityContext context, ServletConfig svtCfg,
			HttpServletRequest req, HttpServletResponse res, PrintWriter out,
			Moduler cModuler, PortletInfo portletInfo) {
		String module;
		String moduleTitle;
		String moduleRealTitle;
		context.put("isMaximized", true);
		String moduleId = req.getParameter("moduleId");
		System.out.println("module = " + moduleId);
		
		Module2 currentModule = cModuler.getModuleById(moduleId);
		module = currentModule.getId();
		context.put("_moduleId", module);
		moduleTitle = currentModule.getCustomTitle();
		moduleRealTitle = currentModule.getTitle();
		portletInfo.id = module;
		portletInfo.title = moduleTitle;
		Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);
		
		out.println("<tr><td>");
		
		if ( !"".equals(moduleRealTitle)) {
			context.put("moduleTitle", moduleTitle);
			ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
			try {
				cModuleTitle.print();
			} catch ( Exception ex ) {
				out.println(ex.getMessage());	
			}
		}
		
		out.println("</td></tr>");
		out.println("<tr><td>");
		try {
			printContent(content, svtCfg, req, res, out, portletInfo);
		} catch ( Exception ex ) {
			out.println( ex.getMessage() );
		}
		
		out.println("</td></tr>");
	}

	public void printContent(Object content,
			ServletConfig svtCfg,
			HttpServletRequest req,
			HttpServletResponse res,
			PrintWriter out,
			PortletInfo portletInfo) throws Exception {
		if ( content != null ) {
			if ( content instanceof VTemplate ) {
				//Tue Feb 6, 2007
				//modify by shamsul to enable activity log
				HttpSession session = req.getSession();
				((VTemplate) content).print(session);
			}
			else if ( content instanceof MerakPortlet ) {
				((MerakPortlet) content).doView(req, res);	
			}
			else if ( content instanceof GenericPortlet ) {
				Hashtable portletState = getPortletState(svtCfg, req, res, out, portletInfo);
				RenderRequest renderRequest = (RenderRequest) portletState.get("renderRequest");
				RenderResponse renderResponse = (RenderResponse) portletState.get("renderResponse");
				PortletConfig config = (PortletConfig) portletState.get("config");
				GenericPortlet portlet = (GenericPortlet) content;
				portlet.init(config);
				String reqMethod = req.getMethod();
				//System.out.println("#####Portlet request method is = " + reqMethod);
				portlet.render(renderRequest, renderResponse);
			}			
		}		
	}

	public Hashtable getPortletState(ServletConfig svtCfg,
			HttpServletRequest req,
			HttpServletResponse res,
			PrintWriter out,
			PortletInfo portletInfo) throws Exception {

		HttpSession session = req.getSession();
		Hashtable h = new Hashtable();

		MerakContext context = new MerakContext();
		context.httpServletRequest = req;

		MerakConfig config = new MerakConfig();
		config.portletInfo = portletInfo;
		config.portletContext = context;

		MerakResponse renderResponse = new MerakResponse();
		MerakRequest renderRequest = new MerakRequest();	
		renderRequest.windowState = WindowState.NORMAL;
		renderRequest.portletMode = PortletMode.VIEW;

		//System.out.println("window state = " + renderRequest.getWindowState());

		renderResponse.printWriter = out;
		//renderResponse.outputStream = res.getOutputStream(); //will cause exception: getWriter() has already been called for this response 
		//kalau dah panggil getWriter(), lepas tu tak boleh panggil OutputStream() .... so nak wat cam na?
		renderRequest.httpServletRequest = req;
		//renderRequest.portletSession = session;

		renderResponse.httpServletResponse = res;




		h.put("renderRequest", renderRequest);
		h.put("renderResponse", renderResponse);
		h.put("config", config);
		return h;	
	}



	public Object renderContent(VelocityEngine engine, 
			VelocityContext context,
			ServletConfig svtCfg,
			HttpServletRequest req,
			HttpServletResponse res,
			String module,
			PortletInfo portletInfo) {



		HttpSession session = req.getSession();
		Object content = null;

		try {
			//Class klazz = Class.forName(CustomClass.getName(module));
			//content = klazz.newInstance();	
			if ( !"".equals(module) ) {
				if ( session.getAttribute("_log_module") != null &&
						!module.equals((String) session.getAttribute("_log_module"))) {
					UserTrackerLog.save(req, (String) session.getAttribute("_portal_login"), module); 
					session.setAttribute("_log_module", module);
				}
				else
					session.setAttribute("_log_module", module);

			}

//			System.out.println("In DisplayContent.renderContent()");
//			System.out.println("module=" + module);

			content = ClassLoadManager.load(CustomClass.getName(module), module, req.getRequestedSessionId());
			//String moduleTitle = CustomClass.getCustomTitle(module);
			//System.out.println(moduleTitle);
			if ( content instanceof VTemplate ) {		
				((VTemplate) content).setEnvironment(engine, context, req, res);	
				((VTemplate) content).setServletContext(svtCfg.getServletContext());	
				((VTemplate) content).setServletConfig(svtCfg);				
				((VTemplate) content).setId(module);	
				((VTemplate) content).setDiv(false);
			}
			/*
			else if ( content instanceof MerakPortlet ) {
				((MerakPortlet) content).setEnvironment(req, res);
			}
			 */

			//content is HtmlContainer or RSSContainer
			if ( content instanceof HtmlContainer ) {
				//get the url for this content
				String url = UserPage.getUrlForHtmlContainer(module);
				if ( url != null ) ((HtmlContainer) content).setUrl(url);
			} 
			if ( content instanceof XMLContainer ) {
				//get the url for this content
				Hashtable h = UserPage.getUrlAndXslForXMLContainer(module);
				if ( h != null ) {
					((XMLContainer) content).setXml((String) h.get("xml"));
					((XMLContainer) content).setXsl((String) h.get("xsl"));
				}
			}
			if ( content instanceof Attributable ) {
				Hashtable h = UserPage.getValuesForAttributable(module);
				if ( h != null ) {
					((Attributable) content).setValues(h);
				}	
			}			
		} catch ( DbException dbx ) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("Database Error : " + dbx.getMessage());						
		} catch ( ClassNotFoundException cnfex ) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("ClassNotFoundException : " + cnfex.getMessage());				
		} catch ( InstantiationException iex ) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("InstantiationException : " + iex.getMessage());			
		} catch ( IllegalAccessException illex ) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("IllegalAccessException : " + illex.getMessage());			
		} catch ( Exception ex ) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("Other Exception during class initiation : " + ex.getMessage());						
		}
		return content;	
	}					
	

}
