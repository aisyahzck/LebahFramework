/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import java.util.List;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.db.PrepareModule;
import lebah.portal.db.PrepareTab;
import lebah.portal.db.UserPage;
import lebah.portal.element.Module2;
import lebah.portal.element.Tab;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Customize extends lebah.portal.velocity.VTemplate {
	
	int cntListItem = 10000;  //number of modules item in the list - LET'S DISPLAY ALL ITEMS

	public Customize(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
		super(engine, context, req, res);
	}
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		String usrlogin = (String) session.getAttribute("_portal_login");
		
		String vtl = "vtl/admin/customize.vm";
		
		String type = getParam("type");
		String submit = getParam("do");
		String tabid = getParam("tabid");
		
		//put empy message in msg
		context.put("msg", "");
		

		
		if ( !"".equals(type) ) {
			if ( "tab".equals(type) ) {
				if ( "saveTabs".equals(submit)) {
					saveTabs(session, usrlogin);
					listTabs(session, usrlogin);
					vtl = "vtl/admin/customize.vm";
				}
		    	else if ( "listTabs".equals(submit)) {
		    		listTabs(session, usrlogin);
		    		vtl = "vtl/admin/customize.vm";
		    	}				
		    	else if ( "addTab".equals(submit)) {
		    		//saveTabs(session, usrlogin);
		    		addTab(session, usrlogin);
		    		listTabs(session, usrlogin);
		    		vtl = "vtl/admin/customize.vm";
		    	}
		    	else if ( "deleteTab".equals(submit)) {
		    		//saveTabs(session, usrlogin);
		    		deleteTab(session, usrlogin);
		    		listTabs(session, usrlogin);
		    		vtl = "vtl/admin/customize.vm";
		    	}
		    	else if ( "updateTab".equals(submit)) {
		    		//saveTabs(session, usrlogin);
		    		renameTab(session, usrlogin);
		    		listTabs(session, usrlogin);
		    		vtl = "vtl/admin/customize.vm";
		    	}	
		    	else if ( "listModules".equals(submit)) {
		    		//saveTabs(session, usrlogin);
		    		listModules(session, usrlogin);
		    		vtl = "vtl/admin/customize_modules.vm";
		    	}
			
			} 
			else if ( "module".equals(type) ) {
				
		    	if ( "saveModules".equals(submit)) {
		    		saveModules(session, usrlogin);
		    		listModules(session, usrlogin);
		    		vtl = "vtl/admin/customize_modules.vm";
		    	}
		    	else if ( "addRemoveModules".equals(submit)) {
		    		addRemoveModules(session, usrlogin);
		    		vtl = "vtl/admin/allmodules.vm";
		    	}
		    	else if ( "updateTabModules".equals(submit)) {
		    		updateTabModules(session, usrlogin);
		    		listModules(session, usrlogin);
		    		vtl = "vtl/admin/customize_modules.vm";
		    		
		    	}
		    	else if ( "listModules".equals(submit)) {
		    		saveTabs(session, usrlogin);
		    		listModules(session, usrlogin);
		    		vtl = "vtl/admin/customize_modules.vm";
		    	}
		    	else if ( "listTabs".equals(submit)) {
		    		saveModules(session, usrlogin);
		    		listTabs(session, usrlogin);
		    		vtl = "vtl/admin/customize.vm";
		    	}	
		    	else if ( "cancelSave".equals(submit)) {
		    		listTabs(session, usrlogin);
		    		vtl = "vtl/admin/customize.vm";
		    	}		    	

			} 
		} else {
			

			if ( "save".equals(submit) ) {

				//check if submitted from form - get the custom titles
				String[] custom_titles = request.getParameterValues("custom_titles");
				//check for column numbers
				String[] column_numbers = request.getParameterValues("column_numbers");		
				//modules ids
				String[] module_ids = request.getParameterValues("module_ids");
				if ( custom_titles != null ) {
					if ( column_numbers != null )
						PrepareModule.saveCustomTitlesAndColumnNumbers(usrlogin, tabid, custom_titles, column_numbers, module_ids);
					else
						PrepareModule.saveCustomTitles(usrlogin, tabid, custom_titles);
				}

				
				String tabtitle = getParam("tabtitle");
				String displaytype = getParam("displaytype");
				PrepareTab.changeTitleAndDisplayType(usrlogin, tabid, tabtitle, displaytype);
				//Vector modules = PrepareModule.retrieve(usrlogin, tabid);
				//Tab tab = PrepareTab.getTab(usrlogin, tabid);
				//context.put("modules", modules);
				//context.put("cTab", tab);				
			}
			
			
			listTabs(session, usrlogin);
			//Vector tabs = PrepareTab.retrieve(usrlogin);
			//context.put("tabs", tabs);					
			//vtl = "vtl/admin/customize.vm";			
		}
		
		
		Template template = engine.getTemplate(vtl);	
		return template;		
	}
	
	private boolean checkMarkedModules(HttpSession session ) {
		String[] values = request.getParameterValues("cbmodules");
		//check for NULL value
		if ( values == null ) return false;
        //System.out.println("checked modules = " + values.length);
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
	

	private void saveTabs(HttpSession session, String user) throws Exception {
		String[] tabIds = request.getParameterValues("tabIds");
		//System.out.println("no of tabs=" + tabIds.length);
		//PrepareTab.saveTabs(tabIds, user);
		TabDb.saveTabs(tabIds, user);

	}
	
	private void listTabs(HttpSession session, String usr) throws Exception {
		//Vector tabs = PrepareTab.retrieve(usr);

		Vector tabs = TabDb.getRoleTabs(usr);
		context.put("tabs", tabs);

	}	
	
	private void addTab(HttpSession session, String usr) throws Exception {
		String tab_title = getParam("tab_title");
		TabDb.addNewTab(usr, tab_title);

	}	
	
	private void deleteTab(HttpSession session, String usr) throws Exception {
		String tab_id = getParam("tabId");
		TabDb.deleteTab(usr, tab_id);
		
	}
	
	private void renameTab(HttpSession session, String usr) throws Exception {
		String tab_title = getParam("tab_title");
		String tab_id = getParam("tabId");
		TabDb.changeTitle(usr, tab_id, tab_title);
		
		
	}
	
	private void listModules(HttpSession session, String usr) throws Exception {
		String tabId = getParam("tabId");
		Tab tab = TabDb.getTab(usr, tabId);
		context.put("tab", tab);
		Vector moduleList = PrepareModule.retrieve(usr, tabId);
		context.put("modules", moduleList);
        //Vector  displaytypes = UserPage.getDisplayTypeVector();
        //context.put("displaytypes", displaytypes);	
		List displayTypes = UserPage.listDisplayTypes();
		context.put("displayTypes", displayTypes);
		
	}
	
	private void saveModules(HttpSession session, String usr) throws Exception {
		String[] moduleIds = request.getParameterValues("moduleIds"); 
		String[] moduleTitles = request.getParameterValues("moduleTitles");
		String[] columnNumbers = request.getParameterValues("columnNumbers");
		String tabId = getParam("tabId");
		if ( moduleIds != null ) {
			PrepareModule.saveModules(usr, tabId, moduleIds, moduleTitles, columnNumbers);
		}
		String displaytype = getParam("displaytype");
		TabDb.changeDisplayType(usr, tabId, displaytype);
		
	}
	
	private void addRemoveModules(HttpSession session, String usr) throws Exception {
		String tabId = getParam("tabId");
		Tab tab = TabDb.getTab(usr, tabId);
		context.put("tab", tab);
		Vector allModules = PrepareModule.getListOfModules(usr, tabId);
		context.put("allModules", allModules);
		
	}
	
	private void updateTabModules(HttpSession session, String usr) throws Exception {
		String tabId = getParam("tabId");
		Vector moduleList = PrepareModule.getListOfModules(usr, tabId);
		moduleList = setCheckedModules(session, moduleList);
		PrepareModule.saveModules(usr, tabId, moduleList);
		
	}
	
    private Vector setCheckedModules(HttpSession session, Vector allModules) {
        Vector modules = new Vector();
        String[] values = request.getParameterValues("cbmodules");
        //check for NULL value
        if ( values != null )
        {
            if ( allModules != null ) {
                Vector v = allModules;
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
            if ( allModules != null ) {
                Vector v = allModules;
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
	
	

}
