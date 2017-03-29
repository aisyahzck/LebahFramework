/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class RoleEditorModule extends VTemplate
{
    private String targetPage;
    
    public Template doTemplate() throws Exception
    {
        
        HttpSession session = request.getSession();
        
        RoleProcessor processor = new RoleProcessor();
                                
        String action = request.getParameter("form_action");
        if ((action == null) || (action.equals(""))) action = "none";
        
        if (action.equals("none"))
        {
            // RENDER DEFAULT PAGE
            Vector list = processor.getRoles();
            context.put("roleList",list);
            context.put("isRoleSelected",Boolean.valueOf(false));
            
            targetPage = "vtl/admin/role_editor.vm";

        } else if (action.equals("get_modules"))
        {
            // GET LIST OF MODULES
            String role = request.getParameter("role");
            Vector moduleList = processor.getModules(role);
            context.put("moduleList",moduleList);
            
            Vector roleList = processor.getRoles();
            context.put("roleList",roleList);
            context.put("isRoleSelected",Boolean.valueOf(true));
            context.put("userRole",role);
            
            targetPage = "vtl/admin/role_editor.vm";
            
        } else if (action.equals("update_role"))
        {
            // UPDATE ROLE MODULE
            String role = request.getParameter("role");
            String[] modules = request.getParameterValues("module");
            processor.updateRoleModule(role, modules);
            
            // GET LIST OF MODULES
            Vector moduleList = processor.getModules(role);
            context.put("moduleList",moduleList);
            
            Vector roleList = processor.getRoles();
            context.put("roleList",roleList);
            context.put("isRoleSelected",Boolean.valueOf(true));
            context.put("userRole",role);
            
            targetPage = "vtl/admin/role_editor.vm";
        }
        
        Template template = engine.getTemplate(targetPage); 
        return template;        
    }    
}
