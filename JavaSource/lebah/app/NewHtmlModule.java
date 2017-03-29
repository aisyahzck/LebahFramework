/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.db.UniqueID;
import lebah.portal.db.RegisterModule;
import lebah.portal.db.UserPage;
import lebah.util.FilesRepositoryModule.Resource;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 * @version 0.1
 */
public class NewHtmlModule extends lebah.portal.velocity.VTemplate {
    
    public NewHtmlModule() {
        
    }       
    
    public NewHtmlModule(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
        super(engine, context, req, res);
    }   
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String tab_id = (String) session.getAttribute("_tab_id");
        
        //System.out.println("tab_id=" + tab_id);
        String vm = "vtl/main/new_html.vm";
        vm = doJob(session, tab_id);
        Template template = engine.getTemplate(vm); 
        return template;        
    }
    
    private String doJob(HttpSession session, String tab_id) throws Exception {

        String submit = getParam("command");
        if ( "addNewHtml".equals(submit) ) {
            String module_id = "Anon_" + UniqueID.getUID();
            String module_title = getParam("module_title");
            String module_class = getParam("module_class");
            int colNum = Integer.parseInt(getParam("colNum"));
            RegisterModule.add(module_id, module_title, module_class, "HTML_ANON", "", new String[] {"anon", "admin", "root"});
            String html_location = getParam("pageUrl");
            String url = "";
            if ( "".equals(html_location)) {
                String root = Resource.getROOT();
                url = "/" + root + "/anon/" + module_id + ".htm";       
                String dir = Resource.getPATH() + "anon/";
                
                File file = new File(dir);
                if ( !file.exists() ) {
                	file.mkdirs();
                }
                String content = getParam("editor1");
                System.out.println("content = " + content);
            	writeFile(content, dir + "/" + module_id + ".htm");                
            } else {
                url = html_location;
            }
            RegisterModule.updateHtmlLocation(module_id, url);  
            RegisterModule.addUserModule(tab_id, "anon", module_id, "", -1, colNum);
            
            
            
            //response.sendRedirect("../c/" + tab_id);
            //getServletContext().getRequestDispatcher("../c/" + request.getPathInfo()).forward(request, response);
            //getServletContext().getRequestDispatcher("../c/" + tab_id).forward(request, response);
            
            context.put("tabId", tab_id);
            return "vtl/main/new_html_added.vm";

        }
        else 
        	return "vtl/main/new_html.vm";
    }
    
    static void createEmptyFile(String fileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write("");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    protected void writeFile(String content, String fileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(content);
            out.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    

}
