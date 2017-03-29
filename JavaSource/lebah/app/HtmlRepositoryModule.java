/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */
package lebah.app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.util.FilesRepositoryModule;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

public class HtmlRepositoryModule  extends FilesRepositoryModule {
	
    public HtmlRepositoryModule() {
        
    }       
    
    public HtmlRepositoryModule(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
        super(engine, context, req, res);
    }   
	
	public Template doTemplate() throws Exception {
			HttpSession session = request.getSession();
			String template_name = "vtl/html_repository/browse.vm";
			return doTask(session, template_name);		
	}
	
    protected void listFiles(HttpSession session, String dir) throws Exception {
    	String root = Resource.getROOT();
    	context.put("root", root);
        String current_dir = Resource.getPATH() + dir;
        String dirName = Resource.getPATH() + (dir != null && !"".equals(dir) ? dir.substring(1) : "");
        session.setAttribute("dirName", dirName);
        context.put("dirName", dirName);
        listFilesInDirectory(session, dir, current_dir);
    }


}
