/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin







but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.portal;

import java.io.File;
import java.io.IOException;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

//import mecca.lms.CollabPortlet;
import lebah.portal.db.CustomClass;
import lebah.portal.velocity.VServlet;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/*
 * History
 * -------
 * #	Date		Name				Remarks
 * ----	----------	------------------	---------------------------------------------------
 * 1.	2009-08-25	Taufek				Updated CalenDarDiv1 style z-index to 10.
 */

public class PCDeviceController {
    
    public static void doService(ServletContext servletContext, ServletConfig servletConfig,  
            VelocityEngine engine, VelocityContext context,
            HttpSession session,
            HttpServletRequest req, HttpServletResponse res) throws IOException {
        PrintWriter out = res.getWriter();
        
        res.setContentType("text/html");
        //System.setErr(new PrintStream(new FileOutputStream("c:/error_portal.txt")));
        boolean localAccess = false;
        if ( "127.0.0.1".equals(req.getRemoteAddr()) ) localAccess = true;
        
        String app_path = servletContext.getRealPath("/"); //getServletContext().getRealPath("/");
        app_path = app_path.replace('\\', '/');     
        session.setAttribute("_portal_app_path", app_path);
       
        String _portal_login = (String) session.getAttribute("_portal_login");
        if ( _portal_login == null || "".equals(_portal_login)) {
            session.setAttribute("_portal_login", "none");
        }
        
        //to handle browser's refresh that might trigger double submission
        //get previous token
        String prev_token = session.getAttribute("form_token") != null ? (String) session.getAttribute("form_token") : "";
        //get form token
        //if user refresh form_token shall always empty
        String form_token = req.getParameter("form_token") != null ? req.getParameter("form_token") : "empty";
        //pre_token equals form_token if not refresh
        
        if ( prev_token.equals(form_token) ) {
            session.setAttribute("doPost", "true");
            session.setAttribute("isPost", new Boolean(true));
        }
        else if ( "empty".equals(form_token) ) {
            session.setAttribute("doPost", "false");
            session.setAttribute("isPost", new Boolean(false));
        }
        else {
            session.setAttribute("doPost", "false");
            session.setAttribute("isPost", new Boolean(false));
        }
        //create a new form token              
        form_token = Long.toString(System.currentTimeMillis());
        session.setAttribute("form_token", form_token);
        //end handle browser's refresh      
        
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
        
        //check if there is a security token in PATH
        int slash = pathInfo.indexOf("/");
        
        //initialize the allowed variable
        boolean allowed = true;
        
        if (  slash == -1 && localAccess ) { //always allow access to localhost access
        	allowed = true;
        } else if ( slash == -1 && !localAccess ) { //set access denied if not localhost access
        	securityTokenDenied(engine, context, req, res);
        	allowed = false;
        } else if ( localAccess ) { //always allow access to localhost access
        	allowed = true;
        }
        
        {
        	boolean hasToken = true;
        	if ( slash > - 1 ) {
        		//read the security token in the path info
		        String securityTokenURI = pathInfo.substring(0, slash);
		        //read the security token in the SESSION Object
		        String securityToken =(String) session.getAttribute("securityToken");
		        if ( !localAccess && !securityTokenURI.equals(securityToken)) {
		        	//display message access denied
		        	securityTokenDenied(engine, context, req, res); 
		        	allowed = false;
		        	System.out.println("SECURITY TOKEN DENIED... access was done thru x controller");
		        }
		        context.put("securityToken", securityToken);
        	} else {
        		hasToken = false;
        	}
        	
        	String className = "";
        	
        	//DISABLE SECURITY
        	//allowed = true;
        	
	        if( allowed ) {
	        	
		        //pathInfo only contains action
		        pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
				String module = pathInfo != null ? pathInfo : "";      

		        //module
		        session.setAttribute("_portal_action", module);
		        session.setAttribute("_portal_module", module);
		        
		        context.put("session", session);        
		        
		        
		        String ddir = "../";
		        if ( hasToken ) ddir = "../../"; //token will add one more directory in the address URL
		        context.put("relativeDir", ddir);
		        
		        out.println("<!DOCTYPE html>");
		        out.println("<html>");
		        out.println("<title>LeBAH Controller</title>");
		        
		        out.println("<meta http-equiv=\"Content-type\" content=\"text/html; charset=utf-8\" />");
		        out.println("<link href=\"" + ddir + "styles.css\" rel=\"stylesheet\" type=\"text/css\">");
		        
		        /*
		        out.println("<link rel=\"stylesheet\" href=\"" + ddir + "bootstrap/bootstrap.min.css\">");
				out.println("<script src=\"" + ddir + "bootstrap/jquery.min.js\"></script>");
				out.println("<script src=\"" + ddir + "bootstrap/bootstrap.min.js\"></script>");
				out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/misc.js\" ></script>");
				out.println("<script type=\"text/javascript\" src=\"" + ddir + "ckeditor/ckeditor.js\"></script>");
		        out.println("<script type=\"text/javascript\" src=\"" + ddir + "dropdown.js\" ></script>");
		        out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/prototype.js\" ></script>");
		        out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/scriptaculous.js\" ></script>");
				out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/fixed.js\" ></script>");
				out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/dragdrop.js\" ></script>");
				out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/unittest.js\" ></script>");
				out.println("<script type=\"text/javascript\" src=\"" + ddir + "scriptaculous/ajax.js\" ></script>");
				*/

				//js_css
				JS_CSS js_css = new JS_CSS(engine, context, req, res);
				try {
					js_css.print();
				} catch ( Exception ex ) {
					ex.printStackTrace();
				} 
				
		        out.println("<body leftmargin=\"0\" rightmargin=\"0\" topmargin=\"0\">");
		        out.println("<div id=\"CalenDarDiv1\" style=\"position:absolute;visibility:hidden;background-color:white;z-index:10;\"></div>");
		        
		        
				ModulePopWindow popwin = new ModulePopWindow(engine, context, req, res);
				try {
					popwin.print();
				} catch ( Exception ex ) {
					out.println( ex.getMessage() );
				}
		        
		        if ( !"".equals(module) ) {
		        	
		        	//System.out.println("lebah module = " + module);
		        	
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
			                    ((ErrorMsg) content).setError("No privillege for " + module + " on role " + role); 
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
	        
	        
	        } else {
	        	System.out.println("UNAUTHORIZED ACCESS... for module " + className);
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
    

    
	private static void securityTokenDenied(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res)  {
		try {
			VTemplate content = new SecurityTokenDenied(engine, context, req, res);
			content.print();
		} catch ( Exception e) {
			System.out.println(e.getMessage());
		} finally {
			
		}
	}

}
