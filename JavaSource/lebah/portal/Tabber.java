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

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.db.*;
import lebah.portal.element.Tab;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Tabber extends lebah.portal.velocity.VTemplate {

	Vector v; 
	
	public Tabber(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
		super(engine, context, req, res);
		try {
			prepareEnvironment();
		} catch ( Exception ex ) {
			//--MODIFY
			System.out.println(ex.getMessage());
		}		
	}
	
	public Template doTemplate() throws Exception {
		Template template = engine.getTemplate("vtl/main/tabber-main.vm");	
		return template;		
	}
	
	public Tab getFirstTab() {
		if ( v != null && (v.size() > 0) ) return (Tab) v.elementAt(0);
		else return null;		
	}
	
	private void prepareEnvironment() throws Exception {
		HttpSession session = request.getSession();
		String usrlogin = (String) session.getAttribute("_portal_login");
		String myrole = (String) session.getAttribute("myrole");
		v = TabDb.getRoleTabs(usrlogin, myrole);
		context.put("tabs", v);
	}
	
}
