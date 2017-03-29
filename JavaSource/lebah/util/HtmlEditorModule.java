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

package lebah.util;

import javax.servlet.http.HttpSession;

import lebah.util.FilesRepositoryModule.Resource;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class HtmlEditorModule extends FCKEditorModule {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        String template_name = "";
        String submit = getParam("command");
        
        if ( "editContent".equals(submit) ) {
            String root = Resource.getROOT();
            String dirName = Resource.getPATH();
            String fileName = "";
            String url = getParam("url");
            String http = request.getRequestURL().toString().substring(0, request.getRequestURL().toString().indexOf("://") + 3);
            if ( url.indexOf(http) == 0 ) {
                String server = (String) session.getAttribute("_portal_server");
                String urlStr = url.substring(7);
                String urlServer = urlStr.substring(0, urlStr.indexOf('/'));
                if ( server.equals(urlServer) || "localhost".equals(urlServer)) {
                    url = url.substring((http + urlServer + "/").length() - 1);
                    String urlRoot = url.substring(1).substring(0, url.substring(1).indexOf('/'));
                    if ( urlRoot.equals(root)) {
                        if ( !"".equals(root)) root = "/" + root + "/";
                        fileName = dirName + url.substring(root.length());
                        session.setAttribute("fileName", fileName);
                        template_name = prepareTemplate(session, submit);
                    }
                    else {
                        template_name = "vtl/editor/deny_edit.vm";
                    }
                }
                else {
                    template_name = "vtl/editor/deny_edit.vm";
                }
            }
            else {
                String urlRoot = url.substring(1).substring(0, url.substring(1).indexOf('/'));
                if ( urlRoot.equals(root)) {
                    if ( !"".equals(root)) root = "/" + root + "/";
                    fileName = dirName + url.substring(root.length());
                    session.setAttribute("fileName", fileName);
                    template_name = prepareTemplate(session, submit);
                }
                else {
                    template_name = "vtl/editor/deny_edit.vm";
                }
            }

        }
        else {
        	template_name = prepareTemplate(session, submit);
        }
        
        
        Template template = engine.getTemplate(template_name);
        return template;        
    }

}
