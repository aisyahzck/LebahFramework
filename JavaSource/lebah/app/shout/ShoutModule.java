/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.app.shout;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

public class ShoutModule extends lebah.portal.velocity.VTemplate implements lebah.portal.Attributable {
    
    protected String[] names = {"ShoutId", "Moderators", "width", "height"};
    protected Hashtable values = new Hashtable();
    
    public String[] getNames() {
        return names;
    }
    
    public Hashtable getValues() {
        return values;
    }
    
    public void setValues(java.util.Hashtable hashtable) {
        values = hashtable;
    }
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String user_role = (String) session.getAttribute("_portal_role");
        
        String shoutId = (String) values.get(names[0]);
        
        context.put("allowCreateClassroom", "false");
        context.put("isModerator", "false");
        context.put("moduleId", shoutId);
        
        String moderators = values.get(names[1]) != null ? (String) values.get(names[1]) + "," : "";
        if ( moderators.indexOf(user_role + ",") > -1 ) {
            context.put("isModerator", "true");
            context.put("allowCreateClassroom", "true");
        }
        
        String width = values.get(names[2]) != null ? (String) values.get(names[2]) : "600";
        String height = values.get(names[3]) != null ? (String) values.get(names[3]) : "250";
        context.put("width", width);
        context.put("height", height);
        
        String template_name = "vtl/collab/shout/shout_box2.vm";
        
        Template template = engine.getTemplate(template_name);  
        return template;    
    }
    
    

}
