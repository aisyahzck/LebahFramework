/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

 * ************************************************************************ */


package lebah.portal;

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
public class DisplayContent {
	
	public static void showTwoColumnsResponsiveType(VelocityEngine engine, VelocityContext context, ServletConfig svtCfg, HttpServletRequest req, HttpServletResponse res, String module, PrintWriter out, HttpSession session) throws Exception {	
		context.put("showMaximizeIcon", true);
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);
		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		PortletInfo portletInfo = new PortletInfo();
		out.println("<tr><td>");
		
		//START
		out.println("<div class=\"row\" style=\"padding-left:2px;padding-right:2px;margin-left:1px;\">");
		
		out.println("<div class=\"col-md-6\" style=\"padding:1px\">");
		for ( int colnum=0; colnum < 2; colnum++ ) {
			if ( colnum > 0 ) out.println("</div><div class=\"col-md-6\" style=\"padding:1px\">");
			
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
			//
			Vector vmodules = cModuler.getModulesInColumn(colnum);
			if ( vmodules.size() == 0 ) {
				out.println("<div class=\"portlet sortable\">&nbsp;</div>");
			}
			//Iterate thru all the modules, and open them
			for ( int cn=0; cn < vmodules.size(); cn++ ) {

				Module2 currentModule = (Module2) vmodules.elementAt(cn);	
				if ( currentModule != null ) {
					module = currentModule.getId();
					context.put("_moduleId", module);
					moduleTitle = currentModule.getCustomTitle();
					moduleRealTitle = currentModule.getTitle();
					portletInfo.id = module;
					portletInfo.title = moduleTitle;	
					
					String moduleClassName = currentModule.getClassName();
					context.put("moduleClassName", moduleClassName);
					
				} else {
					//Log.print("Attempted was denied due to NullPointerException!");
					res.sendRedirect("");
				}

				Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	

				//SHOW CONTENT
				out.println("<tr><td>");
				out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
				out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
				//if ( !"".equals(moduleRealTitle)) {
				if ( !"".equals(moduleTitle)) {
					out.println("<tr><td>");
					out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
					context.put("moduleTitle", moduleTitle);
					ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
					try {
						cModuleTitle.print();
					} catch ( Exception ex ) {
						out.println(ex.getMessage());	
					}
					out.println("</div>");
					out.println("</td></tr>");
				}
				out.println("<tr><td><div class=\"module_content\">");	

				try {
					printContent(content, svtCfg, req, res, out, portletInfo);
				} catch ( Exception ex ) {
					out.println( ex.getMessage() );
				}

				out.println("</div></td></tr>");
				out.println("</table>");
				
				out.println("</div>");
				
				if ( cn == vmodules.size() - 1 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}
				out.println("</td></tr>");		

			}
			out.println("</table>");
			
		}
		out.println("</div>");
		//END
		
		
		out.println("</td></tr>");
	}
	
	public static void showThreeColumnsResponsiveType(VelocityEngine engine, VelocityContext context, ServletConfig svtCfg, HttpServletRequest req, HttpServletResponse res, String module, PrintWriter out, HttpSession session) throws Exception {	
		context.put("showMaximizeIcon", true);
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);
		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		PortletInfo portletInfo = new PortletInfo();
		out.println("<tr><td>");
		
		//START
		out.println("<div class=\"row\" style=\"padding-left:2px;padding-right:4px;margin-left:1px;\">");
		
		out.println("<div class=\"col-md-4\" style=\"padding:1px\">");
		for ( int colnum=0; colnum < 3; colnum++ ) {
			if ( colnum > 0 ) out.println("</div><div class=\"col-md-4\" style=\"padding:1px\">");
			
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
			//
			Vector vmodules = cModuler.getModulesInColumn(colnum);
			if ( vmodules.size() == 0 ) {
				out.println("<div class=\"portlet sortable\">&nbsp;</div>");
			}
			//Iterate thru all the modules, and open them
			for ( int cn=0; cn < vmodules.size(); cn++ ) {

				Module2 currentModule = (Module2) vmodules.elementAt(cn);	
				if ( currentModule != null ) {
					module = currentModule.getId();
					context.put("_moduleId", module);
					moduleTitle = currentModule.getCustomTitle();
					moduleRealTitle = currentModule.getTitle();
					portletInfo.id = module;
					portletInfo.title = moduleTitle;	
					
					String moduleClassName = currentModule.getClassName();
					context.put("moduleClassName", moduleClassName);
					
				} else {
					//Log.print("Attempted was denied due to NullPointerException!");
					res.sendRedirect("");
				}

				Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	

				//SHOW CONTENT
				out.println("<tr><td>");
				out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
				out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
				//if ( !"".equals(moduleRealTitle)) {
				if ( !"".equals(moduleTitle)) {
					out.println("<tr><td>");
					out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
					context.put("moduleTitle", moduleTitle);
					ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
					try {
						cModuleTitle.print();
					} catch ( Exception ex ) {
						out.println(ex.getMessage());	
					}
					out.println("</div>");
					out.println("</td></tr>");
				}
				out.println("<tr><td><div class=\"module_content\">");	

				try {
					printContent(content, svtCfg, req, res, out, portletInfo);
				} catch ( Exception ex ) {
					out.println( ex.getMessage() );
				}

				out.println("</div></td></tr>");
				out.println("</table>");
				
				out.println("</div>");
				
				if ( cn == vmodules.size() - 1 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}
				out.println("</td></tr>");		

			}
			out.println("</table>");
			
		}
		out.println("</div>");
		//END
		
		
		out.println("</td></tr>");
	}
	
	public static void showNarrowWideResponsiveType(VelocityEngine engine, VelocityContext context, ServletConfig svtCfg, HttpServletRequest req, HttpServletResponse res, String module, PrintWriter out, HttpSession session) throws Exception {	
		context.put("showMaximizeIcon", true);
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);
		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		PortletInfo portletInfo = new PortletInfo();
		out.println("<tr><td>");
		
		//START
		out.println("<div class=\"row\" style=\"padding-left:2px;padding-right:4px;margin-left:1px;\">");
		
		out.println("<div class=\"col-md-4\" style=\"padding:1px\">");
		for ( int colnum=0; colnum < 2; colnum++ ) {
			if ( colnum > 0 ) {
				out.println("</div><div class=\"col-md-8\" style=\"padding:1px\">");
			}
			
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
			//
			Vector vmodules = cModuler.getModulesInColumn(colnum);
			if ( vmodules.size() == 0 ) {
				out.println("<div class=\"portlet sortable\">&nbsp;</div>");
			}
			//Iterate thru all the modules, and open them
			for ( int cn=0; cn < vmodules.size(); cn++ ) {

				Module2 currentModule = (Module2) vmodules.elementAt(cn);	
				if ( currentModule != null ) {
					module = currentModule.getId();
					context.put("_moduleId", module);
					moduleTitle = currentModule.getCustomTitle();
					moduleRealTitle = currentModule.getTitle();
					portletInfo.id = module;
					portletInfo.title = moduleTitle;	
					
					String moduleClassName = currentModule.getClassName();
					context.put("moduleClassName", moduleClassName);
					
				} else {
					//Log.print("Attempted was denied due to NullPointerException!");
					res.sendRedirect("");
				}

				Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	

				//SHOW CONTENT
				out.println("<tr><td>");
				out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
				out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
				//if ( !"".equals(moduleRealTitle)) {
				if ( !"".equals(moduleTitle)) {
					out.println("<tr><td>");
					out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
					context.put("moduleTitle", moduleTitle);
					ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
					try {
						cModuleTitle.print();
					} catch ( Exception ex ) {
						out.println(ex.getMessage());	
					}
					out.println("</div>");
					out.println("</td></tr>");
				}
				out.println("<tr><td><div class=\"module_content\">");	

				try {
					printContent(content, svtCfg, req, res, out, portletInfo);
				} catch ( Exception ex ) {
					out.println( ex.getMessage() );
				}

				out.println("</div></td></tr>");
				out.println("</table>");
				
				out.println("</div>");
				
				if ( cn == vmodules.size() - 1 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}
				out.println("</td></tr>");		

			}
			out.println("</table>");
			
		}
		out.println("</div>");
		//END
		
		
		out.println("</td></tr>");
	}
	
	
	public static void showWideNarrowResponsiveType(VelocityEngine engine, VelocityContext context, ServletConfig svtCfg, HttpServletRequest req, HttpServletResponse res, String module, PrintWriter out, HttpSession session) throws Exception {	
		context.put("showMaximizeIcon", true);
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);
		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		PortletInfo portletInfo = new PortletInfo();
		out.println("<tr><td>");
		
		//START
		out.println("<div class=\"row\" style=\"padding-left:2px;padding-right:4px;margin-left:1px;\">");
		
		out.println("<div class=\"col-md-8\" style=\"padding:1px\">");
		for ( int colnum=0; colnum < 2; colnum++ ) {
			if ( colnum > 0 ) {
				out.println("</div><div class=\"col-md-4\" style=\"padding:1px\">");
			}
			
			out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
			//
			Vector vmodules = cModuler.getModulesInColumn(colnum);
			if ( vmodules.size() == 0 ) {
				out.println("<div class=\"portlet sortable\">&nbsp;</div>");
			}
			//Iterate thru all the modules, and open them
			for ( int cn=0; cn < vmodules.size(); cn++ ) {

				Module2 currentModule = (Module2) vmodules.elementAt(cn);	
				if ( currentModule != null ) {
					module = currentModule.getId();
					context.put("_moduleId", module);
					moduleTitle = currentModule.getCustomTitle();
					moduleRealTitle = currentModule.getTitle();
					portletInfo.id = module;
					portletInfo.title = moduleTitle;	
					
					String moduleClassName = currentModule.getClassName();
					context.put("moduleClassName", moduleClassName);
					
				} else {
					//Log.print("Attempted was denied due to NullPointerException!");
					res.sendRedirect("");
				}

				Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	

				//SHOW CONTENT
				out.println("<tr><td>");
				out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
				out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
				//if ( !"".equals(moduleRealTitle)) {
				if ( !"".equals(moduleTitle)) {
					out.println("<tr><td>");
					out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
					context.put("moduleTitle", moduleTitle);
					ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
					try {
						cModuleTitle.print();
					} catch ( Exception ex ) {
						out.println(ex.getMessage());	
					}
					out.println("</div>");
					out.println("</td></tr>");
				}
				out.println("<tr><td><div class=\"module_content\">");	

				try {
					printContent(content, svtCfg, req, res, out, portletInfo);
				} catch ( Exception ex ) {
					out.println( ex.getMessage() );
				}

				out.println("</div></td></tr>");
				out.println("</table>");
				
				out.println("</div>");
				
				if ( cn == vmodules.size() - 1 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}
				out.println("</td></tr>");		

			}
			out.println("</table>");
			
		}
		out.println("</div>");
		//END
		
		
		out.println("</td></tr>");
	}
	
	
	public static void showTwoColumnsType(VelocityEngine engine, VelocityContext context, ServletConfig svtCfg, HttpServletRequest req, HttpServletResponse res, String module, PrintWriter out, HttpSession session) throws Exception {	
		context.put("showMaximizeIcon", true);
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
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, svtCfg, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");
			out.println("<tr><td valign=\"top\" width=\"50%\">");
			for ( int colnum=0; colnum < 2; colnum++ ) {
				if ( colnum > 0 ) out.println("</td><td valign=\"top\" width=\"50%\">");
				out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
				//
				Vector vmodules = cModuler.getModulesInColumn(colnum);
				if ( vmodules.size() == 0 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}
				//Iterate thru all the modules, and open them
				for ( int cn=0; cn < vmodules.size(); cn++ ) {
	
					Module2 currentModule = (Module2) vmodules.elementAt(cn);	
					if ( currentModule != null ) {
						module = currentModule.getId();
						context.put("_moduleId", module);
						moduleTitle = currentModule.getCustomTitle();
						moduleRealTitle = currentModule.getTitle();
						portletInfo.id = module;
						portletInfo.title = moduleTitle;	
						
						String moduleClassName = currentModule.getClassName();
						context.put("moduleClassName", moduleClassName);
						
					} else {
						//Log.print("Attempted was denied due to NullPointerException!");
						res.sendRedirect("");
					}
	
					Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
	
					//SHOW CONTENT
					out.println("<tr><td>");
					out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
					out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
					//if ( !"".equals(moduleRealTitle)) {
					if ( !"".equals(moduleTitle)) {
						out.println("<tr><td>");
						out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
						context.put("moduleTitle", moduleTitle);
						ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
						try {
							cModuleTitle.print();
						} catch ( Exception ex ) {
							out.println(ex.getMessage());	
						}
						out.println("</div>");
						out.println("</td></tr>");
					}
					out.println("<tr><td><div class=\"module_content\">");	
	
					try {
						printContent(content, svtCfg, req, res, out, portletInfo);
					} catch ( Exception ex ) {
						out.println( ex.getMessage() );
					}
	
					out.println("</div></td></tr>");
					out.println("</table>");
					
					out.println("</div>");
					
					if ( cn == vmodules.size() - 1 ) {
						out.println("<div class=\"portlet sortable\">&nbsp;</div>");
					}

					
					out.println("</td></tr>");
					//to give a gap effect
					out.println("<tr><td height=\"3px\"> </td></tr>");			
	
				}
				out.println("</table>");
			}
		}
		out.println("</td></tr></table>");
		out.println("</td></tr>");
	}

	public static void showWindowsType(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {

		
		context.remove("showMaximizeIcon");
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//		



		out.println("<tr><td align=\"center\">");
		out.println("<table class=\"body\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
		out.println("<tr><td valign=\"top\">");

		context.put("modules", cModuler.getModuleList());

		int cnt = cModuler.getModuleCount();
		ModuleBarDragabble moduleBar = new ModuleBarDragabble(engine, context, req, res);

		try {
			moduleBar.print();
		} catch ( Exception ex ) {
			out.println(ex.getMessage());	
		}		

		out.println("</td></tr>");
		out.println("<tr><td align=\"center\">");

		Vector vmodules = cModuler.getModuleList();
		for ( int i=0; i < vmodules.size(); i++) {
			//for ( int colnum=0; colnum < 2; colnum++ ) {

			//if ( colnum > 0 ) out.println("</td><td valign=\"top\" width=\"55%\">");
			//out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");

			//

			//Vector vmodules = cModuler.getModulesInColumn(colnum);
			//Iterate thru all the modules, and open them
			//for ( int cn=0; cn < vmodules.size(); cn++ ) {

			//be carefull... might throw NullPointerException
			Module2 currentModule = (Module2) vmodules.elementAt(i);	
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
			String portalId = currentModule.getId();
			out.println("<div id=\"" + portalId + "\" name=\"" + portalId + "\" " +
					"onmouseover=\"this.style.zIndex=500\" " +
					"onmouseout=\"this.style.zIndex=0\" " +
					"style=\"visibility:hidden;position:absolute;background-color:white;border: 2px solid;border-color: #f7faff #99a #99a #f7faff;" +
			"width:500px\">");
			out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
			if ( !"".equals(moduleRealTitle)) {

				out.println("<script>new Draggable('" + portalId + "');</script>");
				out.println("<tr>");
				out.println("<td>");

				context.put("portletId", portalId);
				context.put("moduleTitle", moduleTitle);

				ModuleTitleDragabble cModuleTitle = new ModuleTitleDragabble(engine, context, req, res);

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
			out.println("</div>");
			//out.println("</td></tr>");
			//to give a gap effect
			//out.println("<tr><td height=\"1\"> </td></tr>");			

		}
		//out.println("</table>");
		//}

		//out.println("<tr><td>");



		out.println("</td></tr>");

		out.println("</td></tr></table>");
		out.println("</td></tr>");


	}



	public static void showTwoColumnsWithSingleBottom(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {


		context.put("showMaximizeIcon", true);
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//      

		Module2 module1 = cModuler.getFirstModule();
		

		out.println("<tr><td>");
		out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, svtCfg, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");
			cModuler.removeModule(module1);
		
			out.println("<tr><td valign=\"top\" width=\"50%\">");
			for ( int colnum=0; colnum < 2; colnum++ ) {
				if ( colnum == 1 ) out.println("</td><td valign=\"top\" width=\"60%\">");           
				if ( colnum == 2 ) out.println("</td><td valign=\"top\" width=\"20%\">");           
				out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
				Vector vmodules = cModuler.getModulesInColumn(colnum);
				//Iterate thru all the modules, and open them
				for ( int cn=0; cn < vmodules.size(); cn++ ) {
					//be carefull... might throw NullPointerException
					Module2 currentModule = (Module2) vmodules.elementAt(cn);
					context.put("_moduleId", currentModule.getId());
					doPrintModule(currentModule, portletInfo, engine, context, svtCfg, req, res, module, out, session);
				}
				out.println("</table>");
			}
			out.println("</td></tr></table>");
			out.println("</td></tr>");      

			// RENDERING BOTTOM ROW
			out.println("<tr><td>");
			out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
			//first columns shall span 3
			out.println("<tr><td valign=\"top\">");
			//Module2 currentModule = (Module2) vmodules.elementAt(0);
			context.put("_moduleId", module1.getId());
			doPrintModule(module1, portletInfo, engine, context, svtCfg, req, res, module, out, session);
			out.println("</td></tr>");
			
		}
			
		out.println("</table>");
		out.println("</td></tr>");

	}


	public static void showThreeColumnsWithSingleBottom(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {

		context.put("showMaximizeIcon", true);
		//Initiate VTemplate objects
		Moduler cModuler = new Moduler(engine, context, req, res);

		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//      

		Module2 module1 = cModuler.getFirstModule();
		

		out.println("<tr><td>");
		out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, svtCfg, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");
			cModuler.removeModule(module1);
			//-
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
					context.put("_moduleId", currentModule.getId());
					doPrintModule(currentModule, portletInfo, engine, context, svtCfg, req, res, module, out, session);
				}
				out.println("</table>");
			}
			out.println("</td></tr></table>");
			out.println("</td></tr>");      

			// RENDERING BOTTOM ROW
			out.println("<tr><td>");
			out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
			//first columns shall span 3
			out.println("<tr><td valign=\"top\">");
			//Module2 currentModule = (Module2) vmodules.elementAt(0);
			context.put("_moduleId", module1.getId());
			doPrintModule(module1, portletInfo, engine, context, svtCfg, req, res, module, out, session);

			//-
		}
		
		out.println("</td></tr></table>");
		out.println("</td></tr>");

	}

	public static void showThreeColumnsWithSingleTop(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {

		context.put("showMaximizeIcon", true);
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
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, svtCfg, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");
		
			out.println("<tr><td valign=\"top\">");
			//Module2 currentModule = (Module2) vmodules.elementAt(0);
			Module2 module1 = cModuler.getFirstModule();
			context.put("_moduleId", module1.getId());
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
					context.put("_moduleId", currentModule.getId());
					doPrintModule(currentModule, portletInfo, engine, context, svtCfg, req, res, module, out, session);
				}
				out.println("</table>");
			}
			
		}	
			
		out.println("</td></tr></table>");
		out.println("</td></tr>");    	



	}

	public static void showTwoColumnsWithSingleTop(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {

		context.put("showMaximizeIcon", true);
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
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, svtCfg, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");		
			//first columns shall span 3
			out.println("<tr><td valign=\"top\">");
			//Module2 currentModule = (Module2) vmodules.elementAt(0);
			Module2 module1 = cModuler.getFirstModule();
			context.put("_moduleId", module1.getId());
			doPrintModule(module1, portletInfo, engine, context, svtCfg, req, res, module, out, session);
			out.println("</td></tr></table>");
			out.println("</td></tr>");
	
			out.println("<tr><td>");
			out.println("<table border=\"0\" cellpadding=\"1\" cellspacing=\"1\" width=\"100%\">");
			out.println("<tr><td valign=\"top\" width=\"20%\">");
			cModuler.removeModule(module1);
			for ( int colnum=0; colnum < 2; colnum++ ) {
				if ( colnum == 1 ) out.println("</td><td valign=\"top\" width=\"60%\">");			
				if ( colnum == 2 ) out.println("</td><td valign=\"top\" width=\"20%\">");			
				out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
				Vector vmodules = cModuler.getModulesInColumn(colnum);
				//Iterate thru all the modules, and open them
				for ( int cn=0; cn < vmodules.size(); cn++ ) {
					//be carefull... might throw NullPointerException
					Module2 currentModule = (Module2) vmodules.elementAt(cn);	
					context.put("_moduleId", currentModule.getId());
					doPrintModule(currentModule, portletInfo, engine, context, svtCfg, req, res, module, out, session);
				}
				out.println("</table>");
			}
		}
		
		
		out.println("</td></tr></table>");
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

		context.remove("showMaximizeIcon");
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
				moduleRealTitle = moduleTitle;
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
		//out.println("<tr><td class=\"navigation_menu\" valign=\"top\">");
		out.println("<tr><td class=\"nav-container\" valign=\"top\">");
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

	public static void showPullDownMenuType(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {	

		context.remove("showMaximizeIcon");
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
		out.println("<table align=\"center\" width=\"95%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");

		out.println("<tr><td valign=\"top\">");
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

		context.remove("showMaximizeIcon");
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

		out.println("<td>");
		out.println("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
		out.println("<tr><td align=\"center\" valign=\"top\" nowrap>");
		//DISPLAY TOP MENU
		try {
			cModuler.print();
		} catch ( Exception ex ) {
			out.println( ex.getMessage() );
		}
		out.println("</td></tr><tr><td valign=\"top\">");
		//SHOW CONTENT

		out.println("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
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
		out.println("</td>");      

	}    

	public static void showTopNavigationTitleType(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {  

		context.remove("showMaximizeIcon");
		String tab_id = (String) session.getAttribute("_tab_id");
		String usrlogin = (String) session.getAttribute("_portal_login");
		String tabTitle = UserPage.getTabTitle(usrlogin, tab_id);

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

		out.println("<td>");
		out.println("<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
		out.println("<tr><td class=\"tab_title\">&nbsp;");
		out.println(tabTitle);
		out.println("</td></tr>");
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
		out.println("</td>");      

	}    
	
	
	public static void showModularType(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {
			
			showModularType(engine, context, svtCfg, req, res, module, out, session, true);
			
	}


	public static void showModularType(VelocityEngine engine, 
			VelocityContext context, 
			ServletConfig svtCfg,
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session, boolean hasTitle) throws Exception {
		//Initiate VTemplate objects
		
		context.remove("showMaximizeIcon");
		
		Moduler cModuler = new Moduler(engine, context, req, res);

		//prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
		//--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();
		//

		while ( cModuler.hasMoreModules() ) {

			//be carefull... might throw NullPointerException
			Module2 currentModule = cModuler.getNext();	
			if ( currentModule != null ) {
				module = currentModule.getId();
				moduleTitle = currentModule.getCustomTitle();
				moduleRealTitle = currentModule.getTitle();
				portletInfo.id = module;
				portletInfo.title = moduleTitle;
				
				String moduleClassName = currentModule.getClassName();
				context.put("moduleClassName", moduleClassName);
				
			} else {
				//Log.print("Attempted was denied due to NullPointerException!");
				res.sendRedirect("");
			}


			Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);			

			//SHOW CONTENT
			out.println("<tr><td>");
			out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");

			if ( hasTitle ) {
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
	}

	public static void showNarrowWideType(VelocityEngine engine, 
			VelocityContext context,
			ServletConfig svtCfg, 
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {
		
		context.put("showMaximizeIcon", true);
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
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, svtCfg, req, res, out, cModuler, portletInfo);
		} else {
			
			context.remove("isMaximized");
		
			//-- start of modules
			out.println("<tr><td valign=\"top\" width=\"20%\">");
			for ( int colnum=0; colnum < 2; colnum++ ) {
	
				if ( colnum == 1 ) {
					//out.println("</td><td width=\"1\"></td><td width=\"70%\" valign=\"top\">");
					out.println("</td><td valign=\"top\" width=\"80%\">");
				}
				out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
	
				Vector vmodules = cModuler.getModulesInColumn(colnum);
				
				if ( vmodules.size() == 0 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}

				
				//Iterate thru all the modules, and open them
				for ( int cn=0; cn < vmodules.size(); cn++ ) {
	
					//be carefull... might throw NullPointerException
					Module2 currentModule = (Module2) vmodules.elementAt(cn);	
					if ( currentModule != null ) {
						module = currentModule.getId();
						context.put("_moduleId", module);
						moduleTitle = currentModule.getCustomTitle();
						moduleRealTitle = currentModule.getTitle();
						portletInfo.id = module;
						portletInfo.title = moduleTitle;
						
						String moduleClassName = currentModule.getClassName();
						context.put("moduleClassName", moduleClassName);

					} else {
						//Log.print("Attempted was denied due to NullPointerException!");
						res.sendRedirect("");
					}
	
	
					Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
	
					//SHOW CONTENT
					out.println("<tr><td>");
					
					out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
					
					out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");

					
					//display the module title
					if ( !"".equals(moduleRealTitle)) {
						out.println("<tr><td>");
						out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
						context.put("moduleTitle", moduleTitle);
						ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
						try {
							cModuleTitle.print();
						} catch ( Exception ex ) {
							out.println(ex.getMessage());	
						}
	
						out.println("</div>");
						
						out.println("</td></tr>");
					}
					
					out.println("<tr><td>");				
	
					try {
						printContent(content, svtCfg, req, res, out, portletInfo);
					} catch ( Exception ex ) {
						out.println( ex.getMessage() );
					}
	
					out.println("</td></tr>");
					//-- end of modules
					
					
					out.println("</table>");
					
					out.println("</div>");
					
					if ( cn == vmodules.size() - 1 ) {
						out.println("<div class=\"portlet sortable\">&nbsp;</div>");
					}
					
					if ( colnum == 0 ) {
						out.println("</td>");
					} else {
						out.println("</td></tr>");
					}
					//to give a gap effect
					out.println("<tr><td height=\"3px\"></td></tr>");			
	
					//do next column
					
				}
				//
				out.println("</table>");
				
			}
		
		}
		out.println("</td></tr></table>");
		out.println("</td></tr>");
	}



	private static void doMaximized(VelocityEngine engine,
			VelocityContext context, ServletConfig svtCfg,
			HttpServletRequest req, HttpServletResponse res, PrintWriter out,
			Moduler cModuler, PortletInfo portletInfo) {
		String module;
		String moduleTitle;
		String moduleRealTitle;
		context.put("isMaximized", true);
		String moduleId = req.getParameter("moduleId");
		
		Module2 currentModule = cModuler.getModuleById(moduleId);
		module = currentModule.getId();
		context.put("_moduleId", module);
		moduleTitle = currentModule.getCustomTitle();
		moduleRealTitle = currentModule.getTitle();
		portletInfo.id = module;
		portletInfo.title = moduleTitle;
		Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);
		
		out.println("<tr><td>");
		
		if ( !"".equals(moduleTitle)) {
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



	public static void showWideNarrowType(VelocityEngine engine, 
			VelocityContext context,
			ServletConfig svtCfg, 
			HttpServletRequest req, 
			HttpServletResponse res,
			String module,
			PrintWriter out,
			HttpSession session) throws Exception {
		
		context.put("showMaximizeIcon", true);

		Moduler cModuler = new Moduler(engine, context, req, res);

//		prepare String for module title
		String moduleTitle = "";
		String moduleRealTitle = "";
//		--JSR 168 implementation
		PortletInfo portletInfo = new PortletInfo();


		out.println("<tr><td>");
		out.println("<table width=\"100%\" border=\"0\" cellpadding=\"1\" cellspacing=\"1\">");
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, svtCfg, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");

			out.println("<tr><td valign=\"top\" width=\"80%\" >");
			for ( int colnum=0; colnum < 2; colnum++ ) {
				if ( colnum == 1 ) {
					out.println("</td><td width=\"20%\" valign=\"top\">");
				}
				out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
	
				Vector vmodules = cModuler.getModulesInColumn(colnum);
				
				if ( vmodules.size() == 0 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}

				for ( int cn=0; cn < vmodules.size(); cn++ ) {
					Module2 currentModule = (Module2) vmodules.elementAt(cn);	
					if ( currentModule != null ) {
						module = currentModule.getId();
						context.put("_moduleId", module);
						moduleTitle = currentModule.getCustomTitle();
						moduleRealTitle = currentModule.getTitle();
						portletInfo.id = module;
						portletInfo.title = moduleTitle;
						
						String moduleClassName = currentModule.getClassName();
						context.put("moduleClassName", moduleClassName);
						
						
					} else {
						res.sendRedirect("");
					}
	
	
					Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
	
	//				SHOW CONTENT
					out.println("<tr><td>");
					
					out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
					
					out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
	
					if ( !"".equals(moduleRealTitle)) {
						out.println("<tr><td>");
						out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
						
						context.put("moduleTitle", moduleTitle);
						ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
						try {
							cModuleTitle.print();
						} catch ( Exception ex ) {
							out.println(ex.getMessage());	
						}
	
						out.println("</div>");
	
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
					
					out.println("</div>");
					
					if ( cn == vmodules.size() - 1 ) {
						out.println("<div class=\"portlet sortable\">&nbsp;</div>");
					}

					
					if ( colnum == 0 ) {
						out.println("</td>");
					} else {
						out.println("</td></tr>");
					}
	//				to give a gap effect
					out.println("<tr><td height=\"1\"> </td></tr>");			
	
				}
				out.println("</table>");
			}
			
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
		
		context.put("showMaximizeIcon", true);
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
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, svtCfg, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");
		
			out.println("<tr><td valign=\"top\" width=\"20%\">");
			for ( int colnum=0; colnum < 3; colnum++ ) {
	
				if ( colnum == 1 ) out.println("</td><td valign=\"top\" width=\"60%\">");			
				if ( colnum == 2 ) out.println("</td><td valign=\"top\" width=\"20%\">");			
	
				out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
	
				Vector vmodules = cModuler.getModulesInColumn(colnum);
				
				if ( vmodules.size() == 0 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}

				//Iterate thru all the modules, and open them
				for ( int cn=0; cn < vmodules.size(); cn++ ) {
	
					//be carefull... might throw NullPointerException
					Module2 currentModule = (Module2) vmodules.elementAt(cn);	
					if ( currentModule != null ) {
						module = currentModule.getId();
						context.put("_moduleId", module);
						moduleTitle = currentModule.getCustomTitle();
						moduleRealTitle = currentModule.getTitle();
						portletInfo.id = module;
						portletInfo.title = moduleTitle;
						
						String moduleClassName = currentModule.getClassName();
						context.put("moduleClassName", moduleClassName);
						
					} else {
						//Log.print("Attempted was denied due to NullPointerException!");
						res.sendRedirect("");
					}
	
	
					Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
	
					//SHOW CONTENT
					out.println("<tr><td>");

					out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
	
					out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
					if ( !"".equals(moduleRealTitle)) {
						out.println("<tr><td>");
	
						out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
						context.put("moduleTitle", moduleTitle);
						ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
						try {
							cModuleTitle.print();
						} catch ( Exception ex ) {
							out.println(ex.getMessage());	
						}
	
						out.println("</div>");
						
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
					
					out.println("</div>");
					
					if ( cn == vmodules.size() - 1 ) {
						out.println("<div class=\"portlet sortable\">&nbsp;</div>");
					}
					
					out.println("</td></tr>");
					//to give a gap effect
					out.println("<tr><td height=\"3px\"> </td></tr>");			
	
				}
	
				out.println("</table>");
			}
		
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

		out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
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


	private static void printContent(Object content,
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

				portlet.render(renderRequest, renderResponse);
			}			
		}		
	}

	private static Hashtable getPortletState(ServletConfig svtCfg,
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



	public static void showNarrowLeftThreeColumnsType(VelocityEngine engine,
			VelocityContext context, ServletConfig servletConfig,
			HttpServletRequest req, HttpServletResponse res, String module,
			PrintWriter out, HttpSession session) throws Exception {

		
		context.put("showMaximizeIcon", true);
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
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, servletConfig, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");
		
			out.println("<tr><td valign=\"top\" width=\"20%\">");
			for ( int colnum=0; colnum < 3; colnum++ ) {
	
				if ( colnum == 1 ) out.println("</td><td valign=\"top\" width=\"40%\">");			
				if ( colnum == 2 ) out.println("</td><td valign=\"top\" width=\"40%\">");			
	
				out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
	
				Vector vmodules = cModuler.getModulesInColumn(colnum);
				
				if ( vmodules.size() == 0 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}

				//Iterate thru all the modules, and open them
				for ( int cn=0; cn < vmodules.size(); cn++ ) {
	
					//be carefull... might throw NullPointerException
					Module2 currentModule = (Module2) vmodules.elementAt(cn);	
					if ( currentModule != null ) {
						module = currentModule.getId();
						context.put("_moduleId", module);
						moduleTitle = currentModule.getCustomTitle();
						moduleRealTitle = currentModule.getTitle();
						portletInfo.id = module;
						portletInfo.title = moduleTitle;
						
						String moduleClassName = currentModule.getClassName();
						context.put("moduleClassName", moduleClassName);
						
					} else {
						//Log.print("Attempted was denied due to NullPointerException!");
						res.sendRedirect("");
					}
	
	
					Object content = renderContent(engine, context, servletConfig, req, res, module, portletInfo);	
	
					//SHOW CONTENT
					out.println("<tr><td>");

					out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
	
					out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
					if ( !"".equals(moduleRealTitle)) {
						out.println("<tr><td>");
	
						out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
						context.put("moduleTitle", moduleTitle);
						ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
						try {
							cModuleTitle.print();
						} catch ( Exception ex ) {
							out.println(ex.getMessage());	
						}
	
						out.println("</div>");
						
						out.println("</td></tr>");
					}
					
					out.println("<tr><td>");	
	
					try {
						printContent(content, servletConfig, req, res, out, portletInfo);
					} catch ( Exception ex ) {
						out.println( ex.getMessage() );
					}
	
					out.println("</td></tr>");
					out.println("</table>");
					
					out.println("</div>");
					
					if ( cn == vmodules.size() - 1 ) {
						out.println("<div class=\"portlet sortable\">&nbsp;</div>");
					}
					
					out.println("</td></tr>");
					//to give a gap effect
					out.println("<tr><td height=\"3px\"> </td></tr>");			
	
				}
	
				out.println("</table>");
			}
		
		}
		
		
		out.println("</td></tr></table>");
		out.println("</td></tr>");

		
	}
	
	public static void showNarrowWideNarrow(VelocityEngine engine,
			VelocityContext context, ServletConfig servletConfig,
			HttpServletRequest req, HttpServletResponse res, String module,
			PrintWriter out, HttpSession session) throws Exception {

		
		context.put("showMaximizeIcon", true);
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
		
		String moduleState = req.getParameter("moduleState");
		if ("maximize".equals(moduleState)) {
			doMaximized(engine, context, servletConfig, req, res, out, cModuler, portletInfo);
		} else {
			context.remove("isMaximized");
		
			out.println("<tr><td valign=\"top\" width=\"20%\">");
			for ( int colnum=0; colnum < 3; colnum++ ) {
	
				if ( colnum == 1 ) out.println("</td><td valign=\"top\" width=\"60%\">");			
				if ( colnum == 2 ) out.println("</td><td valign=\"top\" width=\"20%\">");			
	
				out.println("<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">");
	
				Vector vmodules = cModuler.getModulesInColumn(colnum);
				
				if ( vmodules.size() == 0 ) {
					out.println("<div class=\"portlet sortable\">&nbsp;</div>");
				}

				//Iterate thru all the modules, and open them
				for ( int cn=0; cn < vmodules.size(); cn++ ) {
	
					//be carefull... might throw NullPointerException
					Module2 currentModule = (Module2) vmodules.elementAt(cn);	
					if ( currentModule != null ) {
						module = currentModule.getId();
						context.put("_moduleId", module);
						moduleTitle = currentModule.getCustomTitle();
						moduleRealTitle = currentModule.getTitle();
						portletInfo.id = module;
						portletInfo.title = moduleTitle;
						
						String moduleClassName = currentModule.getClassName();
						context.put("moduleClassName", moduleClassName);
						
					} else {
						//Log.print("Attempted was denied due to NullPointerException!");
						res.sendRedirect("");
					}
	
	
					Object content = renderContent(engine, context, servletConfig, req, res, module, portletInfo);	
	
					//SHOW CONTENT
					out.println("<tr><td>");

					out.println("<div id=\"" + module + "\" colNum=\"" + cn + "\" class=\"portlet sortable\">");
	
					out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">");
					if ( !"".equals(moduleRealTitle)) {
						out.println("<tr><td>");
	
						out.println("<div class=\"portlet-header\" style=\"cursor:pointer;\">");
						context.put("moduleTitle", moduleTitle);
						ModuleTitle cModuleTitle = new ModuleTitle(engine, context, req, res);
						try {
							cModuleTitle.print();
						} catch ( Exception ex ) {
							out.println(ex.getMessage());	
						}
	
						out.println("</div>");
						
						out.println("</td></tr>");
					}
					
					out.println("<tr><td>");	
	
					try {
						printContent(content, servletConfig, req, res, out, portletInfo);
					} catch ( Exception ex ) {
						out.println( ex.getMessage() );
					}
	
					out.println("</td></tr>");
					out.println("</table>");
					
					out.println("</div>");
					
					if ( cn == vmodules.size() - 1 ) {
						out.println("<div class=\"portlet sortable\">&nbsp;</div>");
					}
					
					out.println("</td></tr>");
					//to give a gap effect
					out.println("<tr><td height=\"3px\"> </td></tr>");			
	
				}
	
				out.println("</table>");
			}
		
		}
		
		
		out.println("</td></tr></table>");
		out.println("</td></tr>");

		
	}					

}
