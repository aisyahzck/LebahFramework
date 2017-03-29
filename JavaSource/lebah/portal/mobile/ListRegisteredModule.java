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

import org.apache.velocity.Template;

public class ListRegisteredModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(session, submit);
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    
    String prepareTemplate(HttpSession session, String submit) throws Exception {
        String template_name = "vtl/mobile/list_modules.vm";
        
        Vector groupList = MobileData.getModuleGroupList();
        context.put("groupList", groupList);
        
        String group = getParam("group");
        context.put("groupSelect", group);
        if ( !"".equals(group)) {
        	Vector moduleList = MobileData.getModuleList(group);	
        	context.put("moduleList", moduleList);
        }
        else {
        	context.put("moduleList", new Vector());
        }
        
        
        
        
       
        
        
        return template_name;
    }
}
