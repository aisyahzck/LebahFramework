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
/*
 * Created on May 4, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lebah.log;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.db.Db;

import org.apache.velocity.Template;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LogCountryModule  extends lebah.portal.velocity.VTemplate{

	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		
		String template_name = "vtl/admin/log_country.vm";
		String submit = getParam("command");
		
		Vector v = getLog();
		context.put("list", v);
		
		Template template = engine.getTemplate(template_name);	
		return template; 
	}
	
	Vector getLog() throws Exception {
	    Db db = null;
	    try {
	        db = new Db();
	        String sql = "select country_name, count from log_country order by count desc";
	        Statement stmt = db.getStatement();
	        ResultSet rs = stmt.executeQuery(sql);
	        Hashtable h = null;
	        Vector v = new Vector();
	        while ( rs.next() ) {
	            h = new Hashtable();
	            h.put("country", rs.getString("country_name"));
	            h.put("count", new Integer(rs.getInt("count")));
	            v.addElement(h);
	        }
	        return v;
	    } finally {
	        if ( db != null ) db.close();
	    }
	}
}
