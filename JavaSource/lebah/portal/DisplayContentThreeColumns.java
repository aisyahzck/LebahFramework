package lebah.portal;

import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.element.Module2;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class DisplayContentThreeColumns extends DisplayContentTemplate {
	
	
	public void display(VelocityEngine engine, 
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
					} else {
						//Log.print("Attempted was denied due to NullPointerException!");
						res.sendRedirect("");
					}
	
	
					Object content = renderContent(engine, context, svtCfg, req, res, module, portletInfo);	
	
					//SHOW CONTENT
					out.println("<tr><td>");

					out.println("<div class=\"portlet sortable\">");
	
					out.println("<table class=\"module_frame\" width=\"100%\" cellpadding=\"0\" cellspacing=\"1\" border=\"0\">");
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
						
						if ( cn == vmodules.size() - 1 ) {
							out.println("<div class=\"portlet sortable\">&nbsp;</div>");
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
					
					out.println("</td></tr>");
					//to give a gap effect
					//out.println("<tr><td height=\"1\"> </td></tr>");			
	
				}
	
				out.println("</table>");
			}
		
		}
		
		
		out.println("</td></tr></table>");
		out.println("</td></tr>");
	}


}
