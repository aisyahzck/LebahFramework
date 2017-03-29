/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.guestbook;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;

public class GuestBookModule extends GuestBookModuleImpl implements lebah.portal.Attributable {

	//Attributable implementations
	protected String[] names = {"Moderators"};
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
	//-- end attributable implementations
	
    String prepareTemplate(String submit, HttpSession session) throws Exception {
		String moderators = values.get(names[0]) != null ? (String) values.get(names[0]) + "," : "";
		String _portal_role = (String) session.getAttribute("_portal_role");
		if ( moderators.indexOf(_portal_role + ",") == -1 ) {
			context.put("allowUpdate", new Boolean(false));
			context.put("allowDelete", new Boolean(false));
		} else {
			context.put("allowUpdate", new Boolean(true));
			context.put("allowDelete", new Boolean(true));
		}
        return super.prepareTemplate(submit, session);
    }
	
}
