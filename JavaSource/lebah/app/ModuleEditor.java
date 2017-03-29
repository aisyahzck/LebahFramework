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
import lebah.portal.db.PrepareModule;
import lebah.portal.db.RegisterModule;
import lebah.portal.db.UserPage;
import lebah.portal.element.Module;
import lebah.portal.element.Role;
import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

public class ModuleEditor extends VTemplate
{
    //private Hashtable conProp = new Hashtable();

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String vtl = "";
        String role = (String) session.getAttribute("role");
        Vector moduleList = new Vector();
        
        String nav = request.getParameter("nav");
        if ( "next".equals(nav) || "previous".equals(nav) ) {
            //get list
            moduleList = session.getAttribute("moduleList") != null ? (Vector) session.getAttribute("moduleList") : PrepareModule.getListOfModules();
            session.setAttribute("moduleList", moduleList);             
            
            prepareItemForDisplay(session, moduleList, 50, nav);                    
            vtl = "vtl/modules/module_edit.vm";
        } 
        else if ( "delete".equals(nav) ) {
            
            String module_id = getParam("module_id");
            RegisterModule.delete(module_id);
            
            moduleList = PrepareModule.getListOfModules(); 
            session.setAttribute("moduleList", moduleList);
            
            prepareItemForDisplay(session, moduleList, 50, "back"); 
            vtl = "vtl/modules/module_edit.vm";
        } 
        else if ( "edit".equals(nav) ) {
            //get list
            moduleList = session.getAttribute("moduleList") != null ? (Vector) session.getAttribute("moduleList") : PrepareModule.getListOfModules();
            session.setAttribute("moduleList", moduleList);             
            
            String module_id = getParam("module_id");
            Module module = PrepareModule.getModuleById(module_id);
            Vector moduleRoles = module.getRoles();
            
            //get list of roles
            // Passing empty hashtable coz not using attributable class to store db connection properties
            RoleProcessor processor = new RoleProcessor();        
            Vector userRoles = processor.getRoles();
            int j = 0;
            for (Enumeration e = userRoles.elements(); e.hasMoreElements();)
            {
                Role obj = new Role();
                obj = (Role) e.nextElement();
                // TAKING OUT ROOT USER FROM LIST
                if (obj.getName().equals("root")) userRoles.removeElementAt(j);
                j++;
            }
            
            Hashtable map = new Hashtable();
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
            
            //String[] userRoles1 = UserRole.roles;
            //take out root from the arrays
            /*
            String[] userRoles = new String[userRoles1.length - 1];
            int cnt = 0;
            for ( int i = 0; i < userRoles1.length; i++ ) {
                if ( !"root".equals(userRoles1[i]) ) {
                    userRoles[cnt++] = userRoles1[i];
                }
            }           
            
            Hashtable roleDescription = UserRole.getTbRoles();
            Hashtable map = new Hashtable();
            for ( int i=0; i < userRoles.length; i++ ) {
                if ( moduleRoles.contains(userRoles[i]) ) {
                    map.put(userRoles[i], "true");  
                } else {
                    map.put(userRoles[i], "false");
                }
            }
            */
            //validate the class
            String object_type = "", err_msg = "";
            ClassValidator v = new ClassValidator();
            if ( !v.validateClass(module_id) ) context.put("err_msg", v.getErrorMessage());                 
            context.put("object_types", v.getTypes());
            
            if ( v.getTypes().contains("html_container") ) {
                String html_location = UserPage.getUrlForHtmlContainer(module_id);
                context.put("html_location", html_location);
            }               
            if ( v.getTypes().contains("rss_container") ) {
                String html_location = UserPage.getUrlForRSSContainer(module_id);
                context.put("html_location", html_location);
            }               
            if ( v.getTypes().contains("xml_container") ) {
                //get the url for this content
                Hashtable h = UserPage.getUrlAndXslForXMLContainer(module_id);
                if ( h != null ) {
                    context.put("html_location", (String) h.get("xml"));
                    context.put("xsl_name", (String) h.get("xsl"));
                } else {
                    context.put("html_location", "");
                    context.put("xsl_name", "");
                }                   
            }   
            if ( v.getTypes().contains("attributable") ) {
                String[] attributes = v.getAttributes();
                String[] values = new String[attributes.length];                
                Hashtable h = new Hashtable();
                h = UserPage.getValuesForAttributable(module_id);
                for ( int i = 0; i < attributes.length; i++ ) {
                    values[i] = (String) h.get(attributes[i]);                          
                }               
                context.put("attributes_name", attributes);
                context.put("attributes_value", values);                
                context.put("attributes_data", h);
            }                           
            context.put("module", module);
            context.put("userRoles", userRoles);
            //context.put("roleDescription", roleDescription);
            context.put("assignedRoles", map);
            
            vtl = "vtl/modules/module_edit_fields.vm";
        } 
        else if ( "update".equals(nav) ) {
            
            String module_id = getParam("module_id");
            String module_title = getParam("module_title");
            String module_class = getParam("module_class");
            String module_group = getParam("module_group");
            String module_description = getParam("module_description");
            
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

            RegisterModule.update(module_id, module_title, module_class, module_group, module_description, checkRoles);
            
            //validate the class
            String object_type = "", err_msg = "";
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
                    //System.out.println("value=" + values[i]);
                }
                context.put("attributes_data", h);
                context.put("attributes_name", attributes);
                context.put("attributes_value", values);
                RegisterModule.updateAttributeData(module_id, attributes, values);  
            }                                       
            
            
            moduleList = PrepareModule.getListOfModules(); 
            session.setAttribute("moduleList", moduleList);
            
            prepareItemForDisplay(session, moduleList, 50, "back"); 
            vtl = "vtl/modules/module_edit.vm";             
        }
        else if ( "cancel".equals(nav) ) {
            //get list
            moduleList = session.getAttribute("moduleList") != null ? (Vector) session.getAttribute("moduleList") : PrepareModule.getListOfModules();
            session.setAttribute("moduleList", moduleList);     
            
            prepareItemForDisplay(session, moduleList, 50, "back"); 
            vtl = "vtl/modules/module_edit.vm";             
        }
        else {
            //refresh module list in session
            moduleList = PrepareModule.getListOfModules();
            session.setAttribute("moduleList", moduleList);
            
            prepareItemForDisplay(session, moduleList, 50, "first");                                
            vtl = "vtl/modules/module_edit.vm";
        }
        
        Vector groupList = RenameGroupModule.getGroupNameList();
        context.put("groupList", groupList);         
                    
                
        Template template = engine.getTemplate(vtl);    
        return template;        
    }
    
    private void prepareItemForDisplay(HttpSession session, Vector objVector, int cntItemPage, String nav ) {
        
        //start number
        int startno = 0;
        if ( nav == null ) nav = "first";
        if ( session.getAttribute("_portal_startno") != null ) startno = ((Integer) session.getAttribute("_portal_startno")).intValue();
        if ( nav.equals("previous") ) {
            if ( startno == objVector.size() ) {
                int x = startno%cntItemPage;
                if ( x > 0 ) {
                    startno = startno - x;
                    startno = startno - cntItemPage;
                } else {
                    startno = startno - (cntItemPage*2);
                    if ( startno < 0 ) startno = 0;                         
                }
            } else {
                startno = startno - (cntItemPage*2);
                if ( startno < 0 ) startno = 0; 
            } 
        } else if ( nav.equals("first") ) {
            startno = 0;
        } else if ( nav.equals("last") ) {
            int x = cntItemPage;
        } else if ( nav.equals("back") ) {
            if ( startno == objVector.size() ) {
                int x = startno%cntItemPage;
                if ( x == 0 ) x = cntItemPage;
                startno = startno - x;
            } else {    
                startno = startno - cntItemPage;    
                if ( startno < 0 ) startno = 0; 
            }
        
        }
    
        Vector moduleVector = new Vector();     
        int i = 0, cnt = 0;
        if (objVector.size() > 0 ) {
            for ( cnt = 0, i = startno; cnt < cntItemPage && i < objVector.size(); i++, cnt++ ) {
                moduleVector.addElement(objVector.elementAt(i));
            }
        }
        
        session.setAttribute("_portal_moduleVector", moduleVector);
        context.put("startnoInt", new Integer(startno));
        context.put("totalInt", new Integer(objVector.size()));
        
        startno = i;
        //set the next start number
        session.setAttribute("_portal_startno", new Integer(startno));
    }   
    
}
