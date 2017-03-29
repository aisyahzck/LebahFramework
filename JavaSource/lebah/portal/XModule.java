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

import java.io.File;
import java.io.FileDescriptor;

import javax.servlet.http.HttpSession;

import lebah.portal.db.RegisterModule;
import lebah.portal.db.UserPage;
import lebah.util.FilesRepositoryModule.Resource;

import org.apache.velocity.Template;

public class XModule extends lebah.portal.velocity.VTemplate {
	
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		String userId = (String) session.getAttribute("_portal_login");
		//String isLogin = (String) session.getAttribute("_portal_islogin");
		String submit = getParam("command");
		String moduleId = getParam("moduleId");
		String tab_id = (String) session.getAttribute("_tab_id");	
		if ( "deleteModule".equals(submit)) {
			RegisterModule.deleteAnonHtmlModule(tab_id, moduleId, userId);
			//delete the physical file
            String dir = Resource.getPATH() + "anon/";
            File file = new File(dir + "/" + moduleId + ".htm");
            if ( file.exists() ) {
            	file.delete();
            }
		}
		else if ( "changeTheme".equals(submit)) {
			String theme = getParam("theme");
			if ( !"".equals(theme) ) {
				UserPage.saveTheme(userId, theme);
				//change the current theme
				session.setAttribute("_portal_css", theme);
			}
		}

		Template template = engine.getTemplate("vtl/url/x.vm");	
		//response.sendRedirect("../../c/" + tab_id);
		//response.sendRedirect("../../c/");
		return template;		
	}

}
