/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.listeners;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

public class SessionListenerModule extends lebah.portal.velocity.VTemplate {
            
    public Template doTemplate() throws Exception {
        
    	long sessionCount = SessionListener.getNumberOfSession();
    	context.put("sessionCount", sessionCount);
        
        Template template = engine.getTemplate("vtl/admin/session_listener.vm"); 
        return template;        
    }
    
}

