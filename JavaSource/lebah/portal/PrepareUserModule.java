/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.portal;

import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.db.*;
import lebah.portal.element.*;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.0
 */

public class PrepareUserModule extends lebah.portal.velocity.VTemplate {
	
    public Template doTemplate() throws Exception {
    	HttpSession session = request.getSession();
    	String template_name = "vtl/user_module_template/roles.vm";
    	
    	String role = getParam("role");
    	context.put("role", role);

    	
    	String submit = getParam("command");
    	if ( "".equals(submit) || "changeRole".equals(submit)) {
        	Vector roleList = UserTabDb.getRoles();
        	context.put("roles", roleList);
    		template_name = "vtl/user_module_template/roles.vm";
    	}
    	else if ( "listTabs".equals(submit)) {
    		listTabs(session, role);
    		template_name = "vtl/user_module_template/tabs.vm";
    	}
    	else if ( "saveTabs".equals(submit)) {
    		saveTabs(session, role);
    		listTabs(session, role);
    		template_name = "vtl/user_module_template/tabs.vm";
    	}
    	else if ( "addTab".equals(submit)) {
    		saveTabs(session, role);
    		addTab(session, role);
    		listTabs(session, role);
    		template_name = "vtl/user_module_template/tabs.vm";
    	}
    	else if ( "deleteTab".equals(submit)) {
    		saveTabs(session, role);
    		deleteTab(session, role);
    		listTabs(session, role);
    		template_name = "vtl/user_module_template/tabs.vm";
    	}
    	else if ( "updateTab".equals(submit)) {
    		saveTabs(session, role);
    		renameTab(session, role);
    		listTabs(session, role);
    	}
    	else if ( "listModules".equals(submit)) {
    		saveTabs(session, role);
    		listModules(session, role);
    		template_name = "vtl/user_module_template/modules.vm";
    	}
    	else if ( "saveModules".equals(submit)) {
    		saveModules(session, role);
    		listModules(session, role);
    		template_name = "vtl/user_module_template/modules.vm";
    	}
    	else if ( "addRemoveModules".equals(submit)) {
    		addRemoveModules(session, role);
    		template_name = "vtl/user_module_template/allmodules.vm";
    	}
    	else if ( "updateTabModules".equals(submit)) {
    		updateTabModules(session, role);
    		listModules(session, role);
    		template_name = "vtl/user_module_template/modules.vm";
    		
    	}
    	
        Template template = engine.getTemplate(template_name);    
        return template; 
    	
    }

	private void updateTabModules(HttpSession session, String role) throws Exception {
		String tabId = getParam("tabId");
		Vector moduleList = UserModuleDb.getListOfModules(role, tabId);
		moduleList = setCheckedModules(session, moduleList);
		UserModuleDb.saveModules(role, tabId, moduleList);
		
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

	private void addRemoveModules(HttpSession session, String role) throws Exception {
		String tabId = getParam("tabId");
		Tab tab = UserTabDb.getTab(role, tabId);
		context.put("tab", tab);
		Vector allModules = UserModuleDb.getListOfModules(role, tabId);
		context.put("allModules", allModules);
		
	}

	private void saveModules(HttpSession session, String role) throws Exception {
		String[] moduleIds = request.getParameterValues("moduleIds"); 
		String[] moduleTitles = request.getParameterValues("moduleTitles");
		String[] columnNumbers = request.getParameterValues("columnNumbers");
		String tabId = getParam("tabId");
		UserModuleDb.saveModules(role, tabId, moduleIds, moduleTitles, columnNumbers);
		String displaytype = getParam("displaytype");
		UserTabDb.changeDisplayType(role, tabId, displaytype);
		
	}

	private void listModules(HttpSession session, String role) throws Exception {
		String tab_id = getParam("tabId");
		Tab tab = UserTabDb.getTab(role, tab_id);
		context.put("tab", tab);
		Vector moduleList = UserModuleDb.retrieve(role, tab_id);
		context.put("modules", moduleList);
        Vector  displaytypes = UserPage.getDisplayTypeVector();
        context.put("displaytypes", displaytypes);		
		
	}

	private void renameTab(HttpSession session, String role) throws Exception {
		String tab_title = getParam("tab_title");
		String tab_id = getParam("tabId");
		UserTabDb.changeTitle(role, tab_id, tab_title);
		
		
	}

	private void deleteTab(HttpSession session, String role) throws Exception {
		String tab_id = getParam("tabId");
		UserTabDb.deleteTab(role, tab_id);
		
	}

	private void addTab(HttpSession session, String role) throws Exception {
		String tab_title = getParam("tab_title");
		String tab_id = tab_title;
		UserTabDb.addNewTab(role, tab_id, tab_title);
		
		
	}

	private void saveTabs(HttpSession session, String role) throws Exception {
		String[] tabIds = request.getParameterValues("tabIds");
		String[] lockIds = request.getParameterValues("lockIds");
		UserTabDb.saveTabsOrder(tabIds, lockIds, role);
		
	}

	private void listTabs(HttpSession session, String role) throws Exception {
		Vector tabs = UserTabDb.retrieve(role);
		context.put("tabs", tabs);
		
	}

}
