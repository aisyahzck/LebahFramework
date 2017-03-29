/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.portal.mobile;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

public class LogoutModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(session, submit);
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    
    String prepareTemplate(HttpSession session, String submit) throws Exception {
        String template_name = "vtl/mobile/logout.vm";
		session.setAttribute("_portal_role", "anon");
		session.setAttribute("_portal_username", "Anonymous");
		session.setAttribute("_portal_login", "anon");
		session.setAttribute("_portal_islogin", "false");        
        context.put("userSessionId", "anon");
		context.put("isLogin", false);
		context.put("user", "anon");
		context.put("userName", "Guest");	        
        return template_name;
    }

}
