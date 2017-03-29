/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or





but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.portal.mobile;

import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.db.PrepareModule;
import lebah.portal.db.PrepareUser;

import org.apache.velocity.Template;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SetupMobileModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(session, submit);
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    
    String prepareTemplate(HttpSession session, String submit) throws Exception {
        String template_name = "vtl/mobile/roles_module.vm";
        
        Vector roleList = MobileData.getRoleNames();
        context.put("roleList", roleList);
        
        String role = getParam("role");
        if ( "addModule".equals(submit)) {
        	if ( !"".equals(role)) {
        		String module = getParam("module");
        		String title = getParam("title");
        		MobileData.addModule(role, module, title);
        	}
        }
        else if ( "deleteModule".equals(submit)) {
        	
        	if ( !"".equals(role)) {
        		String module = getParam("module");
        		MobileData.deleteModule(role, module);
        	}
        }
    	if ( !"".equals(role)) {
        	context.put("roleSelect", role);
        	Vector modules = MobileData.getModules(role);
        	context.put("modules", modules);
    	}
    	else {
    		context.put("modules", new Vector());
    	}
   
        
        return template_name;
    }


}
