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
package lebah.portal.element;

import java.util.Vector;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Module {
	private String module_id;
	private String module_title;
	private String module_class;
	private String module_group;
	private String module_description;
	private int module_sequence;
	
	private Vector roles = new Vector();
	
	public Module(String module_id, String module_title, String module_class) {
		this.module_id = module_id;
		this.module_title = module_title;
		this.module_class = module_class;
	}
	
	public void setGroupName(String s) {
		module_group = s;
	}
	
	public String getGroupName() {
		return module_group;
	}
	
	public void setDescription(String s) {
		module_description = s;
	}
	
	public String getDescription() {
		return module_description;
	}
	
	public String getId() {
		return module_id;
	}
	
	public String getTitle() {
		return module_title;
	}
	
	public String getClassName() {
		return module_class;
	}
	
	public void setRoles(Vector v) {
		this.roles = v;
	}
	
	public Vector getRoles() {
		return roles;
	}
	
	public void addRole(String role) {
		roles.addElement(role);
	}
	
	public boolean equals(Object o) {
		if ( o instanceof Module ) {
			Module m = (Module) o;
			if ( m.getId().equals(module_id)) return true;
		}
		return false;
	}
	
	public void setSequence(int sequence) {
		this.module_sequence = sequence;
	}
	
	public int getSequence() {
		return this.module_sequence;
	}

}
