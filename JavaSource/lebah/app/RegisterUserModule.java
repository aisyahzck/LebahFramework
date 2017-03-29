/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.db.RegisterUser;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;


public class RegisterUserModule extends VTemplate {
    private Hashtable conProp = new Hashtable();
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        doJob(session);
        Template template = engine.getTemplate("vtl/admin/registeruser.vm");    
        return template;        
    }
    
    private void doJob(HttpSession session) throws Exception {
        String submit = getParam("command");
        if ( !"".equals(submit)) {
            String user_login = getParam("user_login");
            String user_name = getParam("user_name");
            String user_password = getParam("user_password");           
            String user_role = getParam("user_role");
            String page_style = getParam("page_style");
            
            context.put("submit", submit);
            context.put("user_login", user_login);
            context.put("user_name", user_name);
            context.put("user_password", user_password);
            context.put("user_role", user_role);
            context.put("page_style", page_style);

            
            if ( "add".equals(submit) ) {
                if ( RegisterUser.add(user_name, user_login, user_password, user_role, page_style) )
                    context.put("registerUserStatus", "success");
                else
                    context.put("registerUserStatus", "failed");
            } else if ("update".equals(submit) ) {
                RegisterUser.update(user_name, user_login, user_password, user_role, user_role, page_style);
                context.put("registerUserStatus", "success");
            }           
        } else {
            context.put("registerUserStatus", "none");
        }
        // Passing empty hashtable coz not using attributable class to store db connection properties
        RoleProcessor processor = new RoleProcessor(conProp);
        Vector userRoles = processor.getRoles();        
        context.put("userRoles", userRoles);
        
        Vector pageStyleList = RegisterUser.getPageStyles();
        context.put("pageStyleList", pageStyleList);
    }
    
 
}
