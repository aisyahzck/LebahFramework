/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or




This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.portal;


import java.util.Enumeration;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.db.PrepareUser;
import lebah.portal.db.RegisterUser;
import lebah.portal.db.UserModuleDb;
import lebah.portal.db.UserPage;
import lebah.portal.db.UserTabDb;
import lebah.portal.element.Module2;
import lebah.portal.element.Tab;
import lebah.portal.element.User;
import lebah.util.Logger;

import org.apache.velocity.Template;

/**
 * @author Shaiful Nizam Tajul
 * @version 1.01
 */
public class UserModuleTemplate extends lebah.portal.velocity.VTemplate {
    private String className = "mecca.portal.UserModuleTemplate";
    private boolean logger = false;
    private Logger log;
    
    int cntListItem = 50;  //number of modules item in the list

    public Template doTemplate() throws Exception {
        // Create new logger
        if (logger) log = new Logger(className);
        
        HttpSession session = request.getSession();
        String usrlogin = getParam("role");
        if (usrlogin == null) usrlogin = "";
        String vtl = "vtl/user_module_template/customize.vm";
        
        String type = getParam("item");
        if (type == null) type = "";
        String submit = getParam("process");
        if (submit == null) submit = "";
        String tabid = getParam("tabid");
        if (tabid == null) tabid = "";

        if (logger) log.setMessage("role = "+usrlogin);
        if (logger) log.setMessage("item="+type+", process="+submit+", tabid="+tabid);
        //db.Log.print(type + ", " + submit);
        //put empy message in msg
        context.put("msg", "");
        
        Vector list = UserTabDb.getRoles();
        context.put("roleList",list);
        context.put("userRole",usrlogin);
        
        if ("".equals(usrlogin))
        {
            context.put("isRoleSelected",Boolean.valueOf(false));
        } else {
            context.put("isRoleSelected",Boolean.valueOf(true));
        
            if ( !"".equals(type) )
            {
                if ( "tab".equals(type) ) 
                {
                    if ( "up".equals(submit) || "down".equals(submit) ) {
                        UserTabDb.changeSequence(usrlogin, tabid, submit);
                        Vector tabs = UserTabDb.retrieve(usrlogin);
                        context.put("tabs", tabs);                  
                        vtl = "vtl/user_module_template/customize.vm";
                    } 
                    else if ("add".equals(submit) ) {
                        String newtab = getParam("newtab");
                        //generate tabid
                        tabid = newtab;
                        if ( !UserTabDb.addNewTab(usrlogin, tabid, newtab) ) {
                            context.put("msg", "Error adding new tab");
                        }
                        Vector tabs = UserTabDb.retrieve(usrlogin);
                        context.put("tabs", tabs);                  
                        vtl = "vtl/user_module_template/customize.vm";
                    } 
                    else if ("delete".equals(submit) ) {
                        UserTabDb.deleteTab(usrlogin, tabid);
                        Vector tabs = UserTabDb.retrieve(usrlogin);
                        context.put("tabs", tabs);                  
                        vtl = "vtl/user_module_template/customize.vm";
                    } 
                    else if ( "content".equals(submit) ) {
                        Vector modules = UserModuleDb.retrieve(usrlogin, tabid);
                        Tab tab = UserTabDb.getTab(usrlogin, tabid);
                        Vector  displaytypes = UserPage.getDisplayTypeVector();
                        context.put("modules", modules);
                        context.put("cTab", tab);
                        context.put("displaytypes", displaytypes);
                        vtl = "vtl/user_module_template/customize_module.vm";
                    } 
                    else if ("apply_template".equals(submit) ) {
                        // APPLY TEMPLATE TO ALL USER WITH THE GIVEN ROLE
                        String role = getParam("role");
                        if (logger) log.setMessage("role = "+role);
                        if (role != null)
                        {
                            Vector users = PrepareUser.retrieve(role);
                            if (logger) log.setMessage("no. of user = "+users.size());
                            for (Enumeration e = users.elements(); e.hasMoreElements();)
                            {
                                User user = new User();
                                user = (User) e.nextElement();
                                if (logger) log.setMessage("user = "+user.getLogin());
                                RegisterUser.setUserModule(user.getLogin(),role);
                            }
                        }
                        
                        Vector tabs = UserTabDb.retrieve(usrlogin);
                        context.put("tabs", tabs);
                        vtl = "vtl/user_module_template/customize.vm";
                    }                     
                }               
                else if ( "module".equals(type) ) {
                    
                    String moduleid = getParam("moduleid");
                    
                    if ( "up".equals(submit) || "down".equals(submit) ) {
                        UserModuleDb.changeSequence(usrlogin, tabid, moduleid, submit);
                        Vector modules = UserModuleDb.retrieve(usrlogin, tabid);
                        Tab tab = UserTabDb.getTab(usrlogin, tabid);
                        context.put("modules", modules);
                        context.put("cTab", tab);
                        vtl = "vtl/user_module_template/customize_module.vm";
                    } 
                    else if ("changetitle".equals(submit) ) {
                        
                        String tabtitle = getParam("tabtitle");
                        String displaytype = getParam("displaytype");
                        
                        UserTabDb.changeTitleAndDisplayType(usrlogin, tabid, tabtitle, displaytype);
                        Vector modules = UserModuleDb.retrieve(usrlogin, tabid);
                        Tab tab = UserTabDb.getTab(usrlogin, tabid);
                        context.put("modules", modules);
                        context.put("cTab", tab);
                        vtl = "vtl/user_module_template/customize_module.vm";      
                                    
                    }
                    else if ("addmodule".equals(submit) ) { //BUTTON MANAGE MODULE WAS CLICKED
                        //String role = (String) session.getAttribute("_portal_role");
                        String role = usrlogin;
                        
                        //put all modules in the vector and saved in session
                        //db.Log.print("UserModuleTemplate: PrepareTemplateModule.getListOfModules(" + role + ", " + usrlogin + ", " + tabid + ")");
                        Vector moduleList = UserModuleDb.getListOfModules(role, usrlogin, tabid);
                        //Vector moduleList = PrepareTemplateModule.getListOfModules(usrlogin);
                        session.setAttribute("_portal_moduleListAll", moduleList);
                        
                        //for list display, get only 5 items per page
                        prepareItemForDisplay(session, moduleList, cntListItem, "first");
                        //db.Log.print("Displaying list of modules...");
                        
                        vtl = "vtl/user_module_template/customize_listofmodules.vm";
                        
                    }
                    else if ("delete".equals(submit) ) {
                        if (logger) log.setMessage("removing modules [no code]");
                        vtl = "vtl/user_module_template/customize_module.vm";
                        
                    }
                    else {
                        Vector modules = UserModuleDb.retrieve(usrlogin, tabid);
                        session.setAttribute("_portal_moduleList", modules);
                        vtl = "vtl/user_module_template/customize_module.vm";                          
                    }
                } else if ( "listmodules".equals(type) ) {
                    if ( "next".equals(submit) || "previous".equals(submit) ) { //Button Next or Previous
                        
                        //db.Log.print(submit);
                    
                        Vector moduleList = (Vector) session.getAttribute("_portal_moduleListAll");
                        //for list display, get only 5 items per page
                        prepareItemForDisplay(session, moduleList, cntListItem, submit);                    
                        vtl = "vtl/user_module_template/customize_listofmodules.vm";
                    } 
                    else if ( "save".equals(submit) ) { //Button Save
                        Vector moduleList = (Vector) session.getAttribute("_portal_moduleListAll");
                        moduleList = setCheckedModules(session, moduleList);
                        UserModuleDb.saveModules(usrlogin, tabid, moduleList);
                        Vector modules = UserModuleDb.retrieve(usrlogin, tabid);
                        Tab tab = UserTabDb.getTab(usrlogin, tabid);
                        context.put("modules", modules);
                        context.put("cTab", tab);
                        vtl = "vtl/user_module_template/customize_module.vm";                  
                    }

                }
            } else {
                
                //db.Log.print("Entering UserModuleTemplate Module by: " + usrlogin);
                if ( "save".equals(submit) ) {
                    //check if submitted from form - get the custom titles
                    String[] custom_titles = request.getParameterValues("custom_titles");
                    //check for column numbers
                    String[] column_numbers = request.getParameterValues("column_numbers");             
                    if ( custom_titles != null ) {
                        if ( column_numbers != null )
                            UserModuleDb.saveCustomTitlesAndColumnNumbers(usrlogin, tabid, custom_titles, column_numbers);
                        else
                            UserModuleDb.saveCustomTitles(usrlogin, tabid, custom_titles);
                    }

                    
                    String tabtitle = getParam("tabtitle");
                    String displaytype = getParam("displaytype");
                    UserTabDb.changeTitleAndDisplayType(usrlogin, tabid, tabtitle, displaytype);
                }
                
                Vector tabs = UserTabDb.retrieve(usrlogin);
                context.put("tabs", tabs);                  
                vtl = "vtl/user_module_template/customize.vm";
                                
            }
            
        }
        Template template = engine.getTemplate(vtl);    
        return template;                
    }
    
    private boolean checkMarkedModules(HttpSession session ) {
        String[] values = request.getParameterValues("cbmodules");
        //check for NULL value
        if ( values == null ) return false;
        if ( session.getAttribute("_portal_moduleVector") != null ) {
            Vector v = (Vector) session.getAttribute("_portal_moduleVector");
            for ( int i=0; i < v.size(); i++ ) {
                Module2 m = (Module2) v.elementAt(i);
                m.setMarked(false);
                if ( values != null) {
                    for ( int k=0; k < values.length; k++ ) {
                        if ( m.getId().equals(values[k]) ) {
                            m.setMarked(true);
                            break;
                        }
                    }
                }
            }
        }
        return true;            
    }
    
    private Vector setCheckedModules(HttpSession session, Vector allModules) {
        Vector modules = new Vector();
        String[] values = request.getParameterValues("cbmodules");
        //check for NULL value
        if ( values != null )
        {
            if ( session.getAttribute("_portal_moduleVector") != null ) {
                Vector v = (Vector) session.getAttribute("_portal_moduleVector");
                for ( int i=0; i < v.size(); i++ ) {
                    Module2 m = (Module2) v.elementAt(i);
                    m.setMarked(false);
                    if ( values != null) {
                        for ( int k=0; k < values.length; k++ ) {
                            if ( m.getId().equals(values[k]) ) {
                                m.setMarked(true);
                                break;
                            }
                        }
                    }
                    //System.out.println("["+i+"] add into checkedModule vector: "+m.getId());
                    modules.add(m);
                }
            }
        } else {
            if ( session.getAttribute("_portal_moduleVector") != null ) {
                Vector v = (Vector) session.getAttribute("_portal_moduleVector");
                for ( int i=0; i < v.size(); i++ ) {
                    Module2 m = (Module2) v.elementAt(i);
                    m.setMarked(false);
                    modules.add(m);
                }
            }
            
        }
        //set check mark for overall module list
        for (int i=0; i < modules.size(); i++)
        {
            Module2 m1 = (Module2) modules.elementAt(i);
            for ( int k=0; k < allModules.size(); k++ )
            {
                Module2 m2 = (Module2) allModules.elementAt(k);
                if ( m1.getId().equals(m2.getId()) ) {
                    m2.setMarked(m1.getMarked());
                    break;
                }
                allModules.setElementAt(m2,k);
            }
        }
        return allModules;
    }
/*
    private Vector setCheckedModules(HttpSession session ) {
        Vector modules = new Vector();
        String[] values = request.getParameterValues("cbmodules");
        //check for NULL value
        if ( values != null )
        {
            if ( session.getAttribute("_portal_moduleVector") != null ) {
                Vector v = (Vector) session.getAttribute("_portal_moduleVector");
                for ( int i=0; i < v.size(); i++ ) {
                    Module2 m = (Module2) v.elementAt(i);
                    m.setMarked(false);
                    if ( values != null) {
                        for ( int k=0; k < values.length; k++ ) {
                            if ( m.getId().equals(values[k]) ) {
                                m.setMarked(true);
                                break;
                            }
                        }
                    }
                    modules.add(m);
                }
            }
        }
        return modules;
    }
*/    
    private void prepareItemForDisplay(HttpSession session, Vector objVector, int cntItemPage, String nav ) {
        
        //check request first
        //update according to checkbox value
        checkMarkedModules(session);
        
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
