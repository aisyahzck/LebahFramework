/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.app;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.ClassValidator;
import lebah.portal.RenameGroupModule;
import lebah.portal.db.RegisterModule;
import lebah.portal.element.Role;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

public class RegisterHTMLModule extends VTemplate
{    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String vm = "vtl/admin/register_html_module.vm";
        
        String submit = getParam("command");
        
        if ( "add_new".equals(submit) ) {
        	String moduleId = getParam("moduleId");
        	String moduleTitle = getParam("moduleTitle");
        	String moduleClass = getParam("moduleClass");
        	String moduleGroup = getParam("moduleGroup");
        	String moduleDescription = getParam("moduleDescription");
        	String[] roles = {"anon"};
        	RegisterModule.add(moduleId, moduleTitle, moduleClass, moduleGroup, moduleDescription, roles);
            ClassValidator v = new ClassValidator();
            if ( !v.validateClass(moduleId) ) context.put("err_msg", v.getErrorMessage()); 
             
        }

        Template template = engine.getTemplate(vm);  
        return template;        
    }

}
