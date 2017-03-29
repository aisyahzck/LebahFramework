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

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class RegisterNewModule extends VTemplate
{    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        doJob(session);
        Template template = engine.getTemplate("vtl/admin/registermodule.vm");  
        return template;        
    }
    
    private void doJob(HttpSession session) throws Exception {
        String submit = getParam("command");
        String role = (String) session.getAttribute("_portal_role");

        RoleProcessor processor = new RoleProcessor();
        Vector userRoles = processor.getRoles();
        int j = 0;
        for (Enumeration e = userRoles.elements(); e.hasMoreElements();) {
            Role obj = new Role();
            obj = (Role) e.nextElement();
            // TAKING OUT ROOT USER FROM LIST
            if (obj.getName().equals("root")) userRoles.removeElementAt(j);
            j++;
        }
                
        context.put("userRoles", userRoles);
        //context.put("roleDescription", roleDescription);    
        
        //these are for class validation
        context.put("object_type", "");
        context.put("err_msg", ""); 
        context.put("object_types", new Vector());
        
        if ( !"".equals(submit)) {
            String module_id = getParam("module_id");
            String module_title = getParam("module_title");
            String module_class = getParam("module_class"); 
            String module_group = getParam("module_group");
            String module_description = getParam("module_description");
            
            module_group = module_group.toUpperCase();
            
            //get roles selected from radio buttons
            String[] cbroles = request.getParameterValues("cbroles");   
            String[] checkRoles = null; 
            
            //root roles must exist in the arrays
            if ( cbroles != null ) {
                boolean found = false;
                for ( int i = 0; i < cbroles.length; i++ ) {
                    if ( "root".equals(cbroles[i]) ) {
                        found = true;   
                        break;
                    }
                }
                
                if ( !found ) {
                    checkRoles = new String[cbroles.length + 1];
                    int i = 0;
                    for ( ; i < cbroles.length; i++ ) {
                        checkRoles[i] = cbroles[i];
                    }
                    checkRoles[i] = "root";
                } else {
                    checkRoles = cbroles;
                }
            } else {
                checkRoles = new String[] { "root" };   
            }

            
            context.put("submit", submit);
            context.put("module_id", module_id);
            context.put("module_title", module_title);
            context.put("module_class", module_class);
            context.put("module_group", module_group);
            context.put("module_description", module_description);
            
            System.out.println(submit);
            
            if ( "add".equals(submit) ) {
    
                if ( RegisterModule.add(module_id, module_title, module_class, module_group, module_description, checkRoles) ) {
                    
                    //validate the class
                    String object_type = "", err_msg = "";
                    ClassValidator v = new ClassValidator();
                    if ( !v.validateClass(module_id) ) context.put("err_msg", v.getErrorMessage()); 
                                    
                    context.put("object_types", v.getTypes());
                    context.put("registerModuleStatus", "success");
                    
                    if ( v.getTypes().contains("attributable") ) {
                        String[] attributes = v.getAttributes();
                        String[] values = new String[attributes.length];
                        Hashtable h = new Hashtable();
                        for ( int i=0; i < attributes.length; i++ ) {
                            h.put(attributes[i], ""); //put with empty string
                        }
                        context.put("attributes_name", attributes);
                        context.put("attributes_value", values);
                        context.put("attributes_data", h);
                    }   
                    
                    //need to update display of checked roles
                    Vector moduleRoles = new Vector();
                    if ( cbroles != null ) {
                        for ( int i=0; i < cbroles.length; i++ ) moduleRoles.addElement(cbroles[i]);
                    }
                    Hashtable map = new Hashtable();
                    for(Enumeration e = userRoles.elements(); e.hasMoreElements();) {
                        Role obj = new Role();
                        obj = (Role) e.nextElement();
                        if ( moduleRoles.contains(obj.getName()) ) {
                            map.put(obj.getName(), "true");
                        } else {
                            map.put(obj.getName(), "false");
                        }
                    }
                    context.put("assignedRoles", map);                  
                }
                else {
                    context.put("registerModuleStatus", "failed");
                }
            } else if ("update".equals(submit) ) {
                
                if ( RegisterModule.update(module_id, module_title, module_class, module_group, module_description, checkRoles) ) {
                    
                    //validate the class
                    //String object_type = "", err_msg = "";
                    ClassValidator v = new ClassValidator();
                    if ( !v.validateClass(module_id) ) context.put("err_msg", v.getErrorMessage());                 
                    context.put("object_types", v.getTypes());
                    
                    if ( v.getTypes().contains("html_container") ) {
                        String html_location = getParam("html_location");
                        context.put("html_location", html_location);
                        RegisterModule.updateHtmlLocation(module_id, html_location);    
                    }
                    if ( v.getTypes().contains("rss_container") ) {
                        String html_location = getParam("html_location");
                        context.put("html_location", html_location);
                        RegisterModule.updateRSSLocation(module_id, html_location); 
                    }
                    if ( v.getTypes().contains("xml_container") ) {
                        String html_location = getParam("html_location");
                        String xsl_name = getParam("xsl_name");
                        context.put("html_location", html_location);
                        context.put("xsl_name", xsl_name);
                        RegisterModule.updateXMLData(module_id, html_location, xsl_name);   
                    }   
                    if ( v.getTypes().contains("attributable") ) {
                        String[] attributes = v.getAttributes();
                        String[] values = new String[attributes.length];
                        Hashtable h = new Hashtable();
                        for ( int i=0; i < attributes.length; i++ ) {
                            values[i] = getParam(attributes[i]);
                            h.put(attributes[i], values[i]);
                        }
                        context.put("attributes_name", attributes);
                        context.put("attributes_value", values);
                        context.put("attributes_data", h);
                        RegisterModule.updateAttributeData(module_id, attributes, values);  
                    }                           
                    
                    context.put("registerModuleStatus", "success");
                    //need to update display of checked roles
                    Vector moduleRoles = new Vector();
                    if ( cbroles != null ) {
                        for ( int i=0; i < cbroles.length; i++ ) moduleRoles.addElement(cbroles[i]);
                    }
                    Hashtable map = new Hashtable();
                    /*
                    for ( int i=0; i < userRoles.length; i++ ) {
                        if ( moduleRoles.contains(userRoles[i]) ) {
                            map.put(userRoles[i], "true");  
                        } else {
                            map.put(userRoles[i], "false");
                        }
                    }
                    */
                    for(Enumeration e = userRoles.elements(); e.hasMoreElements();)
                    {
                        Role obj = new Role();
                        obj = (Role) e.nextElement();
                        if ( moduleRoles.contains(obj.getName()) ) {
                            map.put(obj.getName(), "true");
                        } else {
                            map.put(obj.getName(), "false");
                        }
                    }                    
                    context.put("assignedRoles", map);      
                }               
                else {
                    context.put("registerModuleStatus", "failed");          
                }
            }           
        } else {
            context.put("registerModuleStatus", "none");
        }
        
        Vector groupList = RenameGroupModule.getGroupNameList();
        context.put("groupList", groupList);          
    }

}
