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

package lebah.portal;

import java.sql.ResultSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.db.Db;
import lebah.db.SQLRenderer;

import org.apache.velocity.Template;

public class RenameGroupModule extends lebah.portal.velocity.VTemplate {

    public Template doTemplate() throws Exception {
        
    
        Template template = engine.getTemplate("vtl/admin/rename_module.vm");
        String submit = getParam("command");
        if ( "saveName".equals(submit)) {
            String oldName = getParam("groupList");
            String newName = getParam("newName");
            saveName(oldName, newName);
        }
        Vector list = getGroupNameList();
        context.put("groupList", list);
        
        return template;        
    }
    
    public static Vector getGroupNameList() throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            sql = "select distinct module_group from module order by module_group";
            ResultSet rs = db.getStatement().executeQuery(sql);
            Vector list = new Vector();
            while ( rs.next()) {
                list.addElement(rs.getString(1));
            }
            return list;
        } finally {
            if ( db != null ) db.close();
        }
    }
    
    public static void saveName(String oldName, String newName) throws Exception {
        Db db = null;
        String sql = "";
        try {
            db = new Db();
            SQLRenderer r = new SQLRenderer();
            r.update("module_group", oldName);
            r.add("module_group", newName);
            sql = r.getSQLUpdate("module");
            db.getStatement().executeUpdate(sql);
        } finally {
            if ( db != null ) db.close();
        }        
    }

}
