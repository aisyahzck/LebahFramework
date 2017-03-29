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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class FCKEditorModule extends lebah.portal.velocity.VTemplate {
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        String template_name = prepareTemplate(session, submit);
        
        Template template = engine.getTemplate(template_name);  
        return template;        
    }
    
    
    protected String prepareTemplate(HttpSession session, String submit) throws Exception {
        String template_name = "vtl/editor/fck_editor.vm";
        if ( "save".equals(submit)) {
        	template_name = "vtl/editor/saved.vm";
            String content = getParam("message");
            context.put("content", content);
        	String dirName = session.getAttribute("dirName") != null ? (String) session.getAttribute("dirName") : "";
        	
        	String fileName = getParam("fileName");
        	//if fileName contains : then the full path is included
        	if ( fileName.indexOf(":") < 0 ) {
        		fileName = dirName + "/" + getParam("fileName");
        	}
        	writeFile(content, fileName);
            
        }
        else if ( "edit".equals(submit)) {
        	template_name = "vtl/editor/fck_editor.vm";
        	//String file = (String) session.getAttribute("fileName");
            String dirName = session.getAttribute("dirName") != null ? (String) session.getAttribute("dirName") : "";
            String fileName = getParam("fileName");  
        	context.put("fileName", fileName);
        	context.put("content", readFile(dirName + "/" + fileName));
        }
        else if ( "editContent".equals(submit)) {
        	template_name = "vtl/editor/fck_editor.vm";
            String file = (String) session.getAttribute("fileName");
            context.put("fileName", file);
            context.put("content", readFile(file));
        }      
        /*
        else if ( "addNewHtmlFile".equals(submit)) {
            String dirName = session.getAttribute("dirName") != null ? (String) session.getAttribute("dirName") : "";
            String fileName = getParam("fileName");
            if ( fileName.indexOf(".htm") < 0 ) {
                fileName = fileName + ".htm";
            }
            writeFile("", dirName + "/" + fileName);
        }
        */
        
        return template_name;
    }
    
    protected String readFile(String fileName) {
        try {
        	StringBuffer sb = new StringBuffer();
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            while ((str = in.readLine()) != null) {
                sb.append(str);
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
        	return "";
        }
    	
    }
    
    protected void writeFile(String content, String fileName) {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
            out.write(content);
            out.close();
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

}
