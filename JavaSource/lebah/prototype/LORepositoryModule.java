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

package lebah.prototype;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

public class LORepositoryModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(submit);
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    
    String prepareTemplate(String submit) throws Exception {
        String template_name = "vtl/prototype/LORepository.vm";
        
        return template_name;
    }

}
