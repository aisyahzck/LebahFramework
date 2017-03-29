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
package lebah.db;

import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class TestEnt extends lebah.portal.velocity.VTemplate {

	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		Template template = engine.getTemplate("vtl/testEnt.vm");	
		Vector data = new Vector();
		Hashtable whereData = new Hashtable();
		data.addElement("user_login");
		data.addElement("user_password");
		whereData.put("user_login", "admin");
		DbDelegator delegator = new DbDelegator();
		Vector results = delegator.select("users", data, null);
		context.put("results", results);
		/*
		for ( int i=0; i < results.size(); i++) {
			Hashtable h = (Hashtable) results.elementAt(i);
			String user_login = (String) h.get("user_login");
			String user_password = (String) h.get("user_password");
			System.out.println(user_login + " = " + user_password);
		}
		*/
		return template;		
	}		

}
