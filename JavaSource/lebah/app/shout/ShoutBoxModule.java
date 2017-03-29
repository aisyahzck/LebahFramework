/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin







but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.app.shout;

import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.velocity.VTemplate;

import org.apache.velocity.Template;

public class ShoutBoxModule extends VTemplate {

    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        setShowVM(false);
        String template_name = "vtl/collab/shout/shout.vm";
        String remoteAddr = request.getRemoteAddr();
        String submit = getParam("command");
        
        String categoryId = getParam("shout_category_id");
        if ( "shout".equals(submit)) {
	        String shoutText = getParam("shoutText");
	        String userid = (String) session.getAttribute("_portal_login");
            String username = (String) session.getAttribute("_portal_username");
            if ( username == null || "".equals(username) ) username = userid;
	        ShoutData.addMessage(categoryId, userid, username, remoteAddr, shoutText);
        }
        else if ( "delete".equals(submit)) {
            String shout_id = getParam("shout_id");
            ShoutData.deleteShout(shout_id);
        }
        else if ( "refresh".equals(submit)) {
        	
        }
        
        String isModerator = getParam("isModerator");
        boolean moderator = false;
        if ( isModerator != null && !"".equals(isModerator) ) {
            moderator = "true".equals(isModerator) ? true : false;
        }
        context.put("moderator", new Boolean(moderator));
        
        String shoutListLimit = getParam("shoutListLimit");
        if ( "".equals(shoutListLimit) ) shoutListLimit = "20";
        Vector shoutList = ShoutData.getMessageList(categoryId, shoutListLimit);
        context.put("shoutList", shoutList);
        
        Template template = engine.getTemplate(template_name);    
        return template;        
    }        

}
