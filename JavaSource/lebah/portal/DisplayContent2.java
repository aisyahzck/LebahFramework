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

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class DisplayContent2 {
	
    
    public static void showThreeColumnsWithSingleTop(VelocityEngine engine, 
               VelocityContext context, 
               ServletConfig svtCfg,
               HttpServletRequest req, 
               HttpServletResponse res,
               String module,
               PrintWriter out,
               HttpSession session) throws Exception {
        

		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//prepare String for module title
		//String moduleTitle = "";
		//String moduleRealTitle = "";
		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//		
		
		out.println("<tr><td>");
		out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
		//first columns shall span 3
		out.println("<tr><td valign=\"top\">");
		//Module2 currentModule = (Module2) vmodules.elementAt(0);
		Module2 module1 = cModuler.getFirstModule();
		doPrintModule(module1, portletInfo, engine, context, svtCfg, req, res, module, out, session);
		out.println("</td></tr></table>");
		out.println("</td></tr>");
		
		out.println("<tr><td>");
		out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
		out.println("<tr><td valign=\"top\" width=\"20%\">");
		cModuler.removeModule(module1);
		for ( int colnum=0; colnum < 3; colnum++ ) {
			if ( colnum == 1 ) out.println("</td><td valign=\"top\" width=\"60%\">");			
			if ( colnum == 2 ) out.println("</td><td valign=\"top\" width=\"20%\">");			
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
			Vector vmodules = cModuler.getModulesInColumn(colnum);
			//Iterate thru all the modules, and open them
			for ( int cn=0; cn < vmodules.size(); cn++ ) {
				//be carefull... might throw NullPointerException
				Module2 currentModule = (Module2) vmodules.elementAt(cn);	
				doPrintModule(currentModule, portletInfo, engine, context, svtCfg, req, res, module, out, session);
			}
			out.println("</table>");
		}
		out.println("</td></tr></table>");
		out.println("</td></tr>");    	
        
        
        
    }
    
    private static void doPrintModule(Module2 currentModule, PortletInfo portletInfo, VelocityEngine engine, 
            VelocityContext context, 
            ServletConfig svtCfg,
            HttpServletRequest req, 
            HttpServletResponse res,
            String module,
            PrintWriter out,
            HttpSession session) throws Exception {
    	
		String moduleTitle = "";
		String moduleRealTitle = "";    	
		if ( currentModule != null ) {
			module = currentModule.getId();
			moduleTitle = currentModule.getCustomTitle();
			moduleRealTitle = currentModule.getTitle();
			portletInfo.id = module;
			portletInfo.title = moduleTitle;					
		} else {
			//Log.print("Attempted was denied due to NullPointerException!");
			res.sendRedirect("");
		}


		Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
		
		//SHOW CONTENT
		out.println("<tr><td>");
		
		out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\">");
		if ( !"".equals(moduleRealTitle)) {
			out.println("<tr><td>");

			context.put("moduleTitle", moduleTitle);
			ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
			try {
				cModuleTitle.print();
			} catch ( Exception ex ) {
				out.println(ex.getMessage());	
			}
		
		
			out.println("</td></tr>");
		}
		out.println("<tr><td>");	
	
		try {
			printContent(content, svtCfg, req, res, out, portletInfo);
		} catch ( Exception ex ) {
			out.println( ex.getMessage() );
		}
	
		out.println("</td></tr>");
		out.println("</table>");
		out.println("</td></tr>");    	
    }
	
	
	public static void showNavigationType(VelocityEngine engine, 
						   					   VelocityContext context, 
						   					   ServletConfig svtCfg,
						   					   HttpServletRequest req, 
						   					   HttpServletResponse res,
						   					   String module,
						   					   PrintWriter out,
						   					   HttpSession session) throws Exception {	

		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//		
		Object content = null;
		
		//be carefull... might throw NullPointerException
		Module2 currentModule = cModuler.getModuleById(module);	
		if ( currentModule != null ) {
			moduleTitle = currentModule.getCustomTitle();
			moduleRealTitle = currentModule.getTitle();
			portletInfo.id = module;
			portletInfo.title = moduleTitle;			
			content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
		} else {
			Module2 firstModule = cModuler.getFirstModule();
			if ( firstModule!= null ) {
				module = firstModule.getId();
				moduleTitle = firstModule.getCustomTitle();
				portletInfo.id = module;
				portletInfo.title = moduleTitle;					
				content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
			}
			if ( firstModule == null ) {
				//res.sendRedirect("../expired.jsp");	
				moduleTitle = "Modules has not been setup!";
			}
			session.setAttribute("_portal_module", module);				
		}
		
		out.println("<tr><td>");
		out.println("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
		out.println("<tr><td class=\"navigation_menu\" align=\"center\" valign=\"top\" width=\"180\" nowrap>");
		//DISPLAY LEFT MENU
		try {
			cModuler.print();
		} catch ( Exception ex ) {
			out.println( ex.getMessage() );
		}
		out.println("</td><td valign=\"top\">");
		//SHOW CONTENT
	
		out.println("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
		if ( !"".equals(moduleRealTitle)) {
			out.println("<tr><td>");

			context.put("moduleTitle", moduleTitle);
			ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
			try {
				cModuleTitle.print();
			} catch ( Exception ex ) {
				out.println(ex.getMessage());	
			}
		
		
			out.println("</td></tr>");
		}
		out.println("<tr><td>");	
	
		try {
			printContent(content, svtCfg, req, res, out, portletInfo);  
		} catch ( Exception ex ) {
			out.println( ex.getMessage() );
		} finally {
			content = null;
		}
	
		out.println("</td></tr>");
		out.println("</table>");
		
		out.println("</td></tr>");
		out.println("</table>");
		out.println("</td></tr>");		
	
	}	
    
    public static void showTopNavigationType(VelocityEngine engine, 
               VelocityContext context, 
               ServletConfig svtCfg,
               HttpServletRequest req, 
               HttpServletResponse res,
               String module,
               PrintWriter out,
               HttpSession session) throws Exception {  

        //Initiate VTemplate objects
        TopModuler cModuler = new TopModuler(engine, context, req, res);
        
        //prepare String for module title
        String moduleTitle = "";
        String moduleRealTitle = "";
        //--JSR 168 implementation
        PortletInfo portletInfo = new PortletInfo();
        //      
        Object content = null;
        
        //be carefull... might throw NullPointerException
        Module2 currentModule = cModuler.getModuleById(module); 
        if ( currentModule != null ) {
            moduleTitle = currentModule.getCustomTitle();
            moduleRealTitle = currentModule.getTitle();
            portletInfo.id = module;
            portletInfo.title = moduleTitle;            
            content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);    
        } else {
            Module2 firstModule = cModuler.getFirstModule();
            if ( firstModule!= null ) {
                module = firstModule.getId();
                moduleTitle = firstModule.getCustomTitle();
                portletInfo.id = module;
                portletInfo.title = moduleTitle;                    
                content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);    
            }
            if ( firstModule == null ) {
                //res.sendRedirect("../expired.jsp");   
                moduleTitle = "Modules has not been setup!";
            }
            session.setAttribute("_portal_module", module);             
        }
        
        out.println("<tr><td>");
        out.println("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
        out.println("<tr><td class=\"navigation_menu\" align=\"center\" valign=\"top\" nowrap>");
        //DISPLAY TOP MENU
        try {
            cModuler.print();
        } catch ( Exception ex ) {
            out.println( ex.getMessage() );
        }
        out.println("</td></tr><tr><td valign=\"top\">");
        //SHOW CONTENT
        
        out.println("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
        if ( !"".equals(moduleRealTitle)) {
            out.println("<tr><td>");
        
            context.put("moduleTitle", moduleTitle);
            ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
            try {
                cModuleTitle.print();
            } catch ( Exception ex ) {
                out.println(ex.getMessage());   
            }
            
            
            out.println("</td></tr>");
        }
        out.println("<tr><td>");    
        
        try {
            printContent(content, svtCfg, req, res, out, portletInfo);  
        } catch ( Exception ex ) {
            out.println( ex.getMessage() );
        } finally {
            content = null;
        }
        
        out.println("</td></tr>");
        out.println("</table>");
        
        out.println("</td></tr>");
        out.println("</table>");
        out.println("</td></tr>");      
    
    }    
	
	public static void showModularType(VelocityEngine engine, 
						   					   VelocityContext context, 
						   					   ServletConfig svtCfg,
						   					   HttpServletRequest req, 
						   					   HttpServletResponse res,
						   					   String module,
						   					   PrintWriter out,
						   					   HttpSession session) throws Exception {
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//
		
		//Iterate thru all the modules, and open them
		context.put("CONTENT_MODULE", cModuler);
		while ( cModuler.hasMoreModules() ) {

			//be carefull... might throw NullPointerException
			Module2 currentModule = cModuler.getNext();	
			if ( currentModule != null ) {
				module = currentModule.getId();
				moduleTitle = currentModule.getCustomTitle();
				moduleRealTitle = currentModule.getTitle();
				portletInfo.id = module;
				portletInfo.title = moduleTitle;
			} else {
				//Log.print("Attempted was denied due to NullPointerException!");
				res.sendRedirect("");
			}


			Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);			
			
			//SHOW CONTENT
			//out.println("<tr><td>");
			//out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\">");
			context.put("PORTAL_MODULE_TITLE", moduleRealTitle);
			if ( !"".equals(moduleRealTitle)) {
				//out.println("<tr><td>");
	
				context.put("moduleTitle", moduleTitle);
				/*
				ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
				try {
					cModuleTitle.print();
				} catch ( Exception ex ) {
					out.println(ex.getMessage());	
				}
				*/
			
			
				//out.println("</td></tr>");
			}
			//out.println("<tr><td>");
		
			try {
				printContent(content, svtCfg, req, res, out, portletInfo);
			} catch ( Exception ex ) {
				out.println( ex.getMessage() );
			}
		
			out.println("</td></tr>");
			out.println("</table>");
			out.println("</td></tr>");
			//to give a gap effect
			out.println("<tr><td height=\"1\"> </td></tr>");			
		
		}
	}
	
	public static void showNarrowWideType(VelocityEngine engine, 
						   					   VelocityContext context,
						   					   ServletConfig svtCfg, 
						   					   HttpServletRequest req, 
						   					   HttpServletResponse res,
						   					   String module,
						   					   PrintWriter out,
						   					   HttpSession session) throws Exception {
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//		
		
		out.println("<tr><td>");
		out.println("<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\">");
		out.println("<tr><td valign=\"top\" width=\"30%\">");
		for ( int colnum=0; colnum < 2; colnum++ ) {
			
			if ( colnum == 1 ) {
				//out.println("</td><td width=\"1\"></td><td width=\"70%\" valign=\"top\">");
				out.println("</td><td width=\"70%\" valign=\"top\">");
			}
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");

			Vector vmodules = cModuler.getModulesInColumn(colnum);
			//Iterate thru all the modules, and open them
			for ( int cn=0; cn < vmodules.size(); cn++ ) {

				//be carefull... might throw NullPointerException
				Module2 currentModule = (Module2) vmodules.elementAt(cn);	
				if ( currentModule != null ) {
					module = currentModule.getId();
					moduleTitle = currentModule.getCustomTitle();
					moduleRealTitle = currentModule.getTitle();
					portletInfo.id = module;
					portletInfo.title = moduleTitle;					
				} else {
					//Log.print("Attempted was denied due to NullPointerException!");
					res.sendRedirect("");
				}


				Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
				
				//SHOW CONTENT
				out.println("<tr><td>");
				out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\">");
				
				if ( !"".equals(moduleRealTitle)) {
					out.println("<tr><td>");
		
					context.put("moduleTitle", moduleTitle);
					ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
					try {
						cModuleTitle.print();
					} catch ( Exception ex ) {
						out.println(ex.getMessage());	
					}
				
				
					out.println("</td></tr>");
				}
				out.println("<tr><td>");				
			
				try {
					printContent(content, svtCfg, req, res, out, portletInfo);
				} catch ( Exception ex ) {
					out.println( ex.getMessage() );
				}
			
				out.println("</td></tr>");
				out.println("</table>");
				if ( colnum == 0 ) {
					out.println("</td>");
				} else {
					out.println("</td></tr>");
				}
				//to give a gap effect
				out.println("<tr><td height=\"1\"> </td></tr>");			
			
			}
			out.println("</table>");
		}
		out.println("</td></tr></table>");
		out.println("</td></tr>");
	}
	
	public static void showTwoColumnsType(VelocityEngine engine, 
						   					   VelocityContext context, 
						   					   ServletConfig svtCfg,
						   					   HttpServletRequest req, 
						   					   HttpServletResponse res,
						   					   String module,
						   					   PrintWriter out,
						   					   HttpSession session) throws Exception {
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//		
		
		out.println("<tr><td>");
		out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
		out.println("<tr><td valign=\"top\" width=\"50%\">");
		for ( int colnum=0; colnum < 2; colnum++ ) {
			
			if ( colnum > 0 ) out.println("</td><td valign=\"top\" width=\"50%\">");
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");

			Vector vmodules = cModuler.getModulesInColumn(colnum);
			//Iterate thru all the modules, and open them
			for ( int cn=0; cn < vmodules.size(); cn++ ) {

				//be carefull... might throw NullPointerException
				Module2 currentModule = (Module2) vmodules.elementAt(cn);	
				if ( currentModule != null ) {
					module = currentModule.getId();
					moduleTitle = currentModule.getCustomTitle();
					moduleRealTitle = currentModule.getTitle();
					portletInfo.id = module;
					portletInfo.title = moduleTitle;					
				} else {
					//Log.print("Attempted was denied due to NullPointerException!");
					res.sendRedirect("");
				}


				Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
				
				//SHOW CONTENT
				out.println("<tr><td>");
				out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\">");
				if ( !"".equals(moduleRealTitle)) {
					out.println("<tr><td>");
		
					context.put("moduleTitle", moduleTitle);
					ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
					try {
						cModuleTitle.print();
					} catch ( Exception ex ) {
						out.println(ex.getMessage());	
					}
				
				
					out.println("</td></tr>");
				}
				out.println("<tr><td>");	
			
				try {
					printContent(content, svtCfg, req, res, out, portletInfo);
				} catch ( Exception ex ) {
					out.println( ex.getMessage() );
				}
			
				out.println("</td></tr>");
				out.println("</table>");
				out.println("</td></tr>");
				//to give a gap effect
				out.println("<tr><td height=\"1\"> </td></tr>");			
			
			}
			out.println("</table>");
		}
		out.println("</td></tr></table>");
		out.println("</td></tr>");
	}
	
	public static void showThreeColumnsType(VelocityEngine engine, 
						   					   VelocityContext context, 
						   					   ServletConfig svtCfg,
						   					   HttpServletRequest req, 
						   					   HttpServletResponse res,
						   					   String module,
						   					   PrintWriter out,
						   					   HttpSession session) throws Exception {
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//		
		
		out.println("<tr><td>");
		out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
		out.println("<tr><td valign=\"top\" width=\"20%\">");
		for ( int colnum=0; colnum < 3; colnum++ ) {

			if ( colnum == 1 ) out.println("</td><td valign=\"top\" width=\"60%\">");			
			if ( colnum == 2 ) out.println("</td><td valign=\"top\" width=\"20%\">");			
			
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");

			Vector vmodules = cModuler.getModulesInColumn(colnum);
			//Iterate thru all the modules, and open them
			for ( int cn=0; cn < vmodules.size(); cn++ ) {

				//be carefull... might throw NullPointerException
				Module2 currentModule = (Module2) vmodules.elementAt(cn);	
				if ( currentModule != null ) {
					module = currentModule.getId();
					moduleTitle = currentModule.getCustomTitle();
					moduleRealTitle = currentModule.getTitle();
					portletInfo.id = module;
					portletInfo.title = moduleTitle;					
				} else {
					//Log.print("Attempted was denied due to NullPointerException!");
					res.sendRedirect("");
				}


				Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
				
				//SHOW CONTENT
				out.println("<tr><td>");
				
				out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\">");
				if ( !"".equals(moduleRealTitle)) {
					out.println("<tr><td>");
		
					context.put("moduleTitle", moduleTitle);
					ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
					try {
						cModuleTitle.print();
					} catch ( Exception ex ) {
						out.println(ex.getMessage());	
					}
				
				
					out.println("</td></tr>");
				}
				out.println("<tr><td>");	
			
				try {
					printContent(content, svtCfg, req, res, out, portletInfo);
				} catch ( Exception ex ) {
					out.println( ex.getMessage() );
				}
			
				out.println("</td></tr>");
				out.println("</table>");
				out.println("</td></tr>");
				//to give a gap effect
				//out.println("<tr><td height=\"1\"> </td></tr>");			
			
			}
			
			out.println("</table>");
		}
		out.println("</td></tr></table>");
		out.println("</td></tr>");
	}	
	
	private static void printContent(Object content,
									ServletConfig svtCfg,
									HttpServletRequest req,
						   			HttpServletResponse res,
						   			PrintWriter out,
						   			PortletInfo portletInfo) throws Exception {
		if ( content != null ) {
			if ( content instanceof VTemplate ) {
				((VTemplate) content).print();
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
				portlet.render(renderRequest, renderResponse);
			}			
		}		
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
		//renderResponse.printWriter = res.getWriter(); 
		renderResponse.printWriter = out;
		//renderResponse.outputStream = res.getOutputStream(); //will cause exception: getWriter() has already been called for this response 
		//kalau dah panggil getWriter(), lepas tu tak boleh panggil OutputStream() .... so nak wat cam na?
		renderRequest.httpServletRequest = req;
		renderResponse.httpServletResponse = res;
		
		
		
		h.put("renderRequest", renderRequest);
		h.put("renderResponse", renderResponse);
		h.put("config", config);
		return h;	
	}
	
		
	
	private static Object renderContent(VelocityEngine engine, 
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
			
			content = ClassLoadManager.load(CustomClass.getName(module), module, req.getRequestedSessionId());
			
			if ( content instanceof VTemplate ) {		
				((VTemplate) content).setEnvironment(engine, context, req, res);	
				((VTemplate) content).setServletContext(svtCfg.getServletContext());	
				((VTemplate) content).setServletConfig(svtCfg);				
				((VTemplate) content).setId(module);	
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
