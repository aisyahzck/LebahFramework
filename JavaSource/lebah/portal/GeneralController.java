package lebah.portal;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletMode;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import lebah.portal.ClassLoadManager;
import lebah.portal.ErrorMsg;
import lebah.portal.MerakConfig;
import lebah.portal.MerakContext;
import lebah.portal.MerakRequest;
import lebah.portal.MerakResponse;
import lebah.portal.ModulePopWindow;
import lebah.portal.PCDeviceController;
import lebah.portal.PortletInfo;
import lebah.portal.db.CustomClass;
import lebah.portal.velocity.VServlet;
import lebah.portal.velocity.VTemplate;
import lebah.util.Util;

public class GeneralController extends VServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException    {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {

        HttpSession session = req.getSession();

		synchronized(this) {
			context = (org.apache.velocity.VelocityContext) session.getAttribute("VELOCITY_CONTEXT");
			engine = (org.apache.velocity.app.VelocityEngine) session.getAttribute("VELOCITY_ENGINE");
			if ( context == null ) {
				initVelocity(getServletConfig());
				session.setAttribute("VELOCITY_CONTEXT", context);
				session.setAttribute("VELOCITY_ENGINE", engine);
			}
		}
		
		/*
		context = (org.apache.velocity.VelocityContext) getServletConfig().getServletContext().getAttribute("VELOCITY_CONTEXT");
		engine = (org.apache.velocity.app.VelocityEngine) getServletConfig().getServletContext().getAttribute("VELOCITY_ENGINE");
		*/

        
        
        context.put("util", new Util());
        
		//LABELS Properties
		//context.put("label", lebah.db.Labels.getInstance().getTitles());
		
		String userAgent = req.getHeader("User-Agent");
        context.put("userAgent", userAgent);
        
        if ( userAgent.indexOf("MSIE") > 0 ) {
            context.put("browser", "ie");
        }
        else if ( userAgent.indexOf("Firefox") > 0 ) {
            context.put("browser", "firefox");
        }
        else if ( userAgent.indexOf("Netscape") > 0 ) {
            context.put("browser", "netscape");
        }
        else if ( userAgent.indexOf("Safari") > 0 ) {
            context.put("browser", "safari");
        }
        else if ( userAgent.indexOf("MIDP") > 0 ) {
            context.put("browser", "midp");
        }

        doService(getServletContext(), getServletConfig(), engine, context, session, req, res);
	}
	
    public void doService(ServletContext servletContext, ServletConfig servletConfig,  
            VelocityEngine engine, VelocityContext context,
            HttpSession session,
            HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        
        res.setContentType("text/html");

        String app_path = servletContext.getRealPath("/"); //getServletContext().getRealPath("/");
        app_path = app_path.replace('\\', '/');     
        session.setAttribute("_portal_app_path", app_path);
        
       
        String _portal_login = (String) session.getAttribute("_portal_login");
        if ( _portal_login == null || "".equals(_portal_login)) {
            session.setAttribute("_portal_login", "none");
        }

        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        session.setAttribute("_portal_server", serverPort != 80 ? serverName + ":" + serverPort : serverName );
        context.put("server", serverPort != 80 ? serverName + ":" + serverPort : serverName);       
        
        String uri = req.getRequestURI();
        String s1 = uri.substring(1);
        context.put("appname", s1.substring(0, s1.indexOf("/")));       
        session.setAttribute("_portal_appname", s1.substring(0, s1.indexOf("/")));      
        
		//get pathinfo
        String pathInfo = req.getPathInfo();

		String queryString = req.getQueryString();
		context.put("queryString", queryString);
		
        pathInfo = pathInfo.substring(1); //get rid of the first '/'
        String className = "";
        //pathInfo only contains action
        pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
		String module = pathInfo != null ? pathInfo : "";      

        //module
        session.setAttribute("_portal_action", module);
        session.setAttribute("_portal_module", module);
        
        context.put("session", session);        

        String ddir = "../";
        context.put("relativeDir", ddir);
        
//        out.println("<html>");
//        out.println("<title>eduCATE</title>");
        
        //**HTML
		out.println("<html>");
		out.println("<title>");
		//**HTML
        
		//TITLE
		Title cTitle = new Title(engine, context, req, res);
		try {
			cTitle.print();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}  

        //**HTML
		out.println("</title>");
        out.println("<link href=\"../styles.css\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("<script type=\"text/javascript\" src=\"../scriptaculous/misc.js\" ></script>");
        out.println("<script type=\"text/javascript\" src=\"../dropdown.js\" ></script>");
        out.println("<script type=\"text/javascript\" src=\"../fck/fckeditor.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"../fck/createFCKEditor.js\"></script>");
        out.println("<script type=\"text/javascript\" src=\"../scriptaculous/prototype.js\" ></script>");
        out.println("<script type=\"text/javascript\" src=\"../scriptaculous/scriptaculous.js\" ></script>");
		out.println("<script type=\"text/javascript\" src=\"../scriptaculous/fixed.js\" ></script>");
		out.println("<script type=\"text/javascript\" src=\"../scriptaculous/dragdrop.js\" ></script>");
		out.println("<script type=\"text/javascript\" src=\"../scriptaculous/unittest.js\" ></script>");
		out.println("<script type=\"text/javascript\" src=\"../scriptaculous/ajax.js\" ></script>");
		
		out.println("<script type=\"text/javascript\" src=\"../scriptaculous/effects.js\" ></script>");
		out.println("<script type=\"text/javascript\" src=\"../scriptaculous/EventDispatcher.js\" ></script>");
		out.println("<script type=\"text/javascript\" src=\"../scriptaculous/FishEye.js\" ></script>");
		
		out.println("<script type=\"text/javascript\" src=\"../CalendarPopup.js\" ></script>");
		out.println("<script type=\"text/javascript\">document.write(getCalendarStyles());</script>");
		out.println("<script type=\"text/javascript\">var calndr = new CalendarPopup(\"CalenDarDiv1\");</script>");
		
		//js_css
		JS_CSS js_css = new JS_CSS(engine, context, req, res);
		try {
			js_css.print();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		} 
		
		
        out.println("<body leftmargin=\"0\" rightmargin=\"0\" topmargin=\"0\">");
        out.println("<div id=\"CalenDarDiv1\" style=\"position:absolute;visibility:hidden;background-color:white;z-index:10;\"></div>");
        
        boolean localAccess = false;
        if ( "127.0.0.1".equals(req.getRemoteAddr()) ) localAccess = true;
        
        if ( !"".equals(module) ) {
            try {
                Object content = null;
                try {
                	String role = (String) session.getAttribute("_portal_role");
                	if ( !localAccess ) {
                		className = CustomClass.getName(module, role);
                		content = ClassLoadManager.load(className, module, req.getRequestedSessionId());
                	} else {
                		className = module;
                		content = ClassLoadManager.load(className, module, "");
                	}
					if ( content == null ) {
	                    content = new ErrorMsg(engine, context, req, res);
	                    ((ErrorMsg) content).setError("No privillege for " + module + " on role " + role + ", or this module is not registered."); 
	                    ((VTemplate) content).print(session);
					}
					else if ( content instanceof GenericPortlet ) {
                        PortletInfo portletInfo = new PortletInfo();
                        portletInfo.id = "test_id";
                        portletInfo.title = "Test Title";                           
                        Hashtable portletState = getPortletState(servletConfig, req, res, out, portletInfo);
                        RenderRequest renderRequest = (RenderRequest) portletState.get("renderRequest");
                        RenderResponse renderResponse = (RenderResponse) portletState.get("renderResponse");
                        PortletConfig config = (PortletConfig) portletState.get("config");
                        GenericPortlet portlet = (GenericPortlet) content;
                        portlet.init(config);
        				String reqMethod = req.getMethod();
                        portlet.render(renderRequest, renderResponse);
                    } else { 
	                    ((VTemplate) content).setEnvironment(engine, context, req, res);    
	                    ((VTemplate) content).setServletContext(servletConfig.getServletContext());    
	                    ((VTemplate) content).setServletConfig(servletConfig);
	                    ((VTemplate) content).setId(module);
	                    ((VTemplate) content).setDiv(false);
	                    
	                    try {
	                        if ( content != null ) {
	                            ((VTemplate) content).setShowVM(true);
								((VTemplate) content).print(session);
	                        }
	                    } catch ( Exception ex ) {
	                        out.println( ex.getMessage() );
	                    } 
                    }
 
                } catch ( ClassNotFoundException cnfex ) {
                    content = new ErrorMsg(engine, context, req, res);
                    ((ErrorMsg) content).setError("ClassNotFoundException : " + cnfex.getMessage());
                    ((VTemplate) content).print(session);
                } catch ( InstantiationException iex ) {
                    content = new ErrorMsg(engine, context, req, res);
                    ((ErrorMsg) content).setError("InstantiationException : " + iex.getMessage());   
                    ((VTemplate) content).print(session);
                } catch ( IllegalAccessException illex ) {
                    content = new ErrorMsg(engine, context, req, res);
                    ((ErrorMsg) content).setError("IllegalAccessException : " + illex.getMessage());  
                    ((VTemplate) content).print(session);
                } catch ( Exception ex ) {
                    content = new ErrorMsg(engine, context, req, res);
                    ((ErrorMsg) content).setError("Other Exception during class initiation : " + ex.getMessage());
                    ((VTemplate) content).print(session);
                    ex.printStackTrace();                   
                }   
                
            } catch ( Exception ex ) {
                System.out.println( ex.getMessage() );  
                
            } finally {
                //long totalMem = Runtime.getRuntime().totalMemory();
                //System.out.println("total memory = " + totalMem); 
            }
        }
        
        out.println("</body>");
        out.println("</html>");
        
        
    }
    
    private static Hashtable getPortletState(ServletConfig svtCfg,
            HttpServletRequest req,
                HttpServletResponse res,
                PrintWriter out,
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
        renderResponse.printWriter = out;
        renderRequest.httpServletRequest = req;
        renderResponse.httpServletResponse = res;
        h.put("renderRequest", renderRequest);
        h.put("renderResponse", renderResponse);
        h.put("config", config);
        return h;   
    }
    

}
