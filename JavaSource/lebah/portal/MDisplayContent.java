/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.DbException;
import lebah.portal.db.CustomClass;
import lebah.portal.db.PrepareModule;
import lebah.portal.db.UserPage;
import lebah.portal.db.UserTrackerLog;
import lebah.portal.element.Module;
import lebah.portal.element.Module2;
import lebah.portal.mobile.MModuleTitle;
import lebah.portal.velocity.VTemplate;
//import mecca.lms.CollabPortlet;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MDisplayContent {

	public static void createPageById(VelocityEngine engine,
			VelocityContext context, ServletConfig svtCfg,
			HttpServletRequest req, HttpServletResponse res, String module,
			PrintWriter out, HttpSession session) throws Exception {

		
		// prepare String for module title
		String moduleTitle = "";
		// --JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//		
		Module currentModule = PrepareModule.getModuleById(module);
		Object content = null;
        if ( currentModule != null ) {
            moduleTitle = currentModule.getTitle();
            portletInfo.id = module;
            portletInfo.title = moduleTitle;            
            content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);    
        }
		// SHOW CONTENT
		
		if (!"".equals(moduleTitle)) {
			context.put("moduleTitle", moduleTitle);
			MModuleTitle cModuleTitle = new MModuleTitle(engine, context, req, res);
			try {
				cModuleTitle.print();
			} catch (Exception ex) {
				out.println(ex.getMessage());
			}
		}
		
		try {
			printContent(content, svtCfg, req, res, out, portletInfo);
		} catch (Exception ex) {
			out.println(ex.getMessage());
		} finally {
			content = null;
		}

		out.println("</td></tr>");
		out.println("</table>");

		out.println("</td></tr>");
		out.println("</table>");
		out.println("</td></tr>");

	}

	private static void printContent(Object content, ServletConfig svtCfg,
			HttpServletRequest req, HttpServletResponse res, PrintWriter out,
			PortletInfo portletInfo) throws Exception {
		if (content != null) {
			if (content instanceof VTemplate) {
				((VTemplate) content).setShowVM(false);
				((VTemplate) content).print();
			} else if (content instanceof MerakPortlet) {
				((MerakPortlet) content).doView(req, res);
			} else if (content instanceof GenericPortlet) {
				Hashtable portletState = getPortletState(svtCfg, req, res, out,
						portletInfo);
				RenderRequest renderRequest = (RenderRequest) portletState
						.get("renderRequest");
				RenderResponse renderResponse = (RenderResponse) portletState
						.get("renderResponse");
				PortletConfig config = (PortletConfig) portletState
						.get("config");
				GenericPortlet portlet = (GenericPortlet) content;
				portlet.init(config);
				portlet.render(renderRequest, renderResponse);
			}
		}
	}

	private static Hashtable getPortletState(ServletConfig svtCfg,
			HttpServletRequest req, HttpServletResponse res, PrintWriter out,
			PortletInfo portletInfo) throws Exception {
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
		// renderResponse.printWriter = res.getWriter();
		renderResponse.printWriter = out;
		// renderResponse.outputStream = res.getOutputStream(); //will cause
		// exception: getWriter() has already been called for this response
		// kalau dah panggil getWriter(), lepas tu tak boleh panggil
		// OutputStream() .... so nak wat cam na?
		renderRequest.httpServletRequest = req;
		renderResponse.httpServletResponse = res;

		h.put("renderRequest", renderRequest);
		h.put("renderResponse", renderResponse);
		h.put("config", config);
		return h;
	}

	private static Object renderContent(VelocityEngine engine,
			VelocityContext context, ServletConfig svtCfg,
			HttpServletRequest req, HttpServletResponse res, String module,
			PortletInfo portletInfo) {

		HttpSession session = req.getSession();
		Object content = null;

		try {
			// Class klazz = Class.forName(CustomClass.getName(module));
			// content = klazz.newInstance();
			if (!"".equals(module)) {
				if (session.getAttribute("_log_module") != null
						&& !module.equals((String) session
								.getAttribute("_log_module"))) {
					//UserTrackerLog.save(req, (String) session
					//		.getAttribute("_portal_login"), module);
					session.setAttribute("_log_module", module);
				} else
					session.setAttribute("_log_module", module);

			}

			content = ClassLoadManager.load(CustomClass.getName(module), module, req.getRequestedSessionId());
			// String moduleTitle = CustomClass.getCustomTitle(module);
			// System.out.println(moduleTitle);
			if (content instanceof VTemplate) {
				((VTemplate) content).setEnvironment(engine, context, req, res);
				((VTemplate) content).setServletContext(svtCfg
						.getServletContext());
				((VTemplate) content).setServletConfig(svtCfg);
				((VTemplate) content).setId(module);
			}
			/*
			 * else if ( content instanceof MerakPortlet ) { ((MerakPortlet)
			 * content).setEnvironment(req, res); }
			 */

			// content is HtmlContainer or RSSContainer
			if (content instanceof HtmlContainer) {
				// get the url for this content
				String url = UserPage.getUrlForHtmlContainer(module);
				if (url != null)
					((HtmlContainer) content).setUrl(url);
			}
			if (content instanceof XMLContainer) {
				// get the url for this content
				Hashtable h = UserPage.getUrlAndXslForXMLContainer(module);
				if (h != null) {
					((XMLContainer) content).setXml((String) h.get("xml"));
					((XMLContainer) content).setXsl((String) h.get("xsl"));
				}
			}
			if (content instanceof Attributable) {
				Hashtable h = UserPage.getValuesForAttributable(module);
				if (h != null) {
					((Attributable) content).setValues(h);
				}
			}
		} catch (DbException dbx) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("Database Error : "
					+ dbx.getMessage());
		} catch (ClassNotFoundException cnfex) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("ClassNotFoundException : "
					+ cnfex.getMessage());
		} catch (InstantiationException iex) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("InstantiationException : "
					+ iex.getMessage());
		} catch (IllegalAccessException illex) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content).setError("IllegalAccessException : "
					+ illex.getMessage());
		} catch (Exception ex) {
			content = new ErrorMsg(engine, context, req, res);
			((ErrorMsg) content)
					.setError("Other Exception during class initiation : "
							+ ex.getMessage());
		}
		return content;
	}
	
	public static void createPageByClassName(String module, ServletContext servletContext, ServletConfig servletConfig,  
            VelocityEngine engine, VelocityContext context,
            HttpSession session,
            HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		PrintWriter out = res.getWriter();
        if ( !"".equals(module) ) {
            try {
                
                Object content = null;
                try {
                    Class klazz = Class.forName(module);
                    content = klazz.newInstance();          
                    if ( content instanceof GenericPortlet ) {
                        /*
                        if ( content instanceof CollabPortlet ) {
                            Hashtable h = new Hashtable();
                            h.put("Standalone", "false");
                            ((CollabPortlet) content).setValues(h);
                        }                        
                        */
                        PortletInfo portletInfo = new PortletInfo();
                        portletInfo.id = "test_id";
                        portletInfo.title = "Test Title";                           
                        Hashtable portletState = getPortletState(servletConfig, req, res, out, portletInfo);
                        RenderRequest renderRequest = (RenderRequest) portletState.get("renderRequest");
                        RenderResponse renderResponse = (RenderResponse) portletState.get("renderResponse");
                        PortletConfig config = (PortletConfig) portletState.get("config");
                        GenericPortlet portlet = (GenericPortlet) content;
                        
                        portlet.init(config);
                        portlet.render(renderRequest, renderResponse);
                    } else {    

                        ((VTemplate) content).setEnvironment(engine, context, req, res);    
                        ((VTemplate) content).setServletContext(servletConfig.getServletContext());    
                        ((VTemplate) content).setServletConfig(servletConfig);
                        
                        try {
                            if ( content != null ) {
                                ((VTemplate) content).setShowVM(false);
                                ((VTemplate) content).print();
                            }
                        } catch ( Exception ex ) {
                            out.println( ex.getMessage() );
                        }                       
                    }
                    
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
                    ex.printStackTrace();                   
                }   
                
            } catch ( Exception ex ) {
                System.out.println( ex.getMessage() );  
            } finally {
                //long totalMem = Runtime.getRuntime().totalMemory();
                //System.out.println("total memory = " + totalMem); 
            }
        }
	}

}
