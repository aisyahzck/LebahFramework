/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.db.PrepareUser;
import lebah.portal.db.RegisterUser;
import lebah.portal.element.User;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.0
 */
public class UpdateUserModule extends VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        doJob(session);
        Template template = engine.getTemplate("vtl/admin/registeruser_update.vm"); 
        return template;        
    }
    
    private void doJob(HttpSession session) throws Exception {
        String submit = getParam("command");;
        if ( !"".equals(submit)) {
            String user_login = getParam("user_login");
            String user_name = getParam("user_name");
            String user_password = getParam("user_password");           
            String user_role = getParam("user_role");
            String page_style = getParam("page_style");

            if ("update".equals(submit) ) {
                String current_role = getParam("current_role");
                
                if (RegisterUser.update(user_name, user_login, user_password, user_role, current_role, page_style))
                    context.put("registerUserStatus", "success");
                else
                    context.put("registerUserStatus", "failed");
            }
            else if ( "change_password".equals(submit) ) {
	            PrepareUser.updatePassword(user_login, user_password);
            }
            else if ( "update_biodata".equals(submit) ) {
	            Hashtable data = new Hashtable();
	            data.put("user_name", user_name);
	         	PrepareUser.updateBiodata(user_login, data);
            }
            else if ( "update_role".equals(submit) ) {
	            PrepareUser.updateRole(user_login, user_role);
	            //RegisterUser.setUserModule(user_login, user_role);
            }
            else if ( "update_theme".equals(submit) ) {
	            PrepareUser.updateTheme(user_login, page_style);
            }
            getUser(user_login);
            context.put("submit", submit);
        } 
        else {
            
            context.put("registerUserStatus", "none");
        }
        //get user roles list        
        RoleProcessor processor = new RoleProcessor();
        Vector userRoles = processor.getRoles();        
        context.put("userRoles", userRoles);
        
        Vector pageStyleList = RegisterUser.getPageStyles();
        context.put("pageStyleList", pageStyleList);
    }
    
    void getUser(String user_login) throws Exception {
        User user = PrepareUser.getUserById(user_login);
        Hashtable h = new Hashtable();
        h.put("user_login", user_login);
        if ( user != null ) {
            h.put("user_name", user.getName());
            h.put("user_password", user.getPassword());
            h.put("user_role", user.getRole());
            h.put("page_style", user.getStyle());
            context.put("registerUserStatus", "success");
        } else {
            h.put("user_name", "");
            h.put("user_password", "");
            h.put("user_role", "");
            h.put("page_style", "");
            context.put("registerUserStatus", "failed");
        }	    
        context.put("userData", h);
    }
}
