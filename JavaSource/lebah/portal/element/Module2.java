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

import java.io.Serializable;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Module2 extends Module implements Serializable {
	private boolean isMarked = false;
	private String customTitle = "";
	private int col = 1;
	
	public Module2(String module_id, String module_title, String module_class, boolean b) {
		super(module_id, module_title, module_class);
		isMarked = b;
	}
	
	public Module2(String module_id, String module_title, String module_class, boolean b, String s) {
		super(module_id, module_title, module_class);
		isMarked = b;
		customTitle = s;
	}	
	
	public Module2(String module_id, String module_title, String module_class, String s) {
		super(module_id, module_title, module_class);
		customTitle = s;
	}	
	
	public Module2(String module_id, String module_title, String module_class, String s, int i) {
		super(module_id, module_title, module_class);
		customTitle = s;
		col = i;
	}
	
	public Module2(String module_id, String module_title, String module_class, String s, int i, int seq) {
		super(module_id, module_title, module_class);
		setSequence(seq);
		customTitle = s;
		col = i;
		
	}	
	
	public boolean getMarked() {
		return isMarked;
	}
	
	public void setMarked(boolean b) {
		isMarked = b;
	}
	
	public String getCustomTitle() {
		return customTitle;
	}
	
	public String getModuleTitle() {
		return !"".equals(customTitle) ? customTitle : getTitle();
	}
	
	public void setCustomTitle(String s) {
		customTitle = s;
	}
	
	public void setColumn(int i) {
		col = i;
	}
	
	public int getColumn() {
		return col;
	}
}
