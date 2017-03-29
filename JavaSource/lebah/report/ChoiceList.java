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

package lebah.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ChoiceList {
	private List items;

	public ChoiceList() {
		items = new ArrayList();
	}

	public class Item {
		private String key;
		private String data;
		Item(String key, String data) {
			this.key = key;
			this.data = data;
		}
		public String getKey() { return key; }
		public String getData() { return data; }
	}
	public void add(String key, String data) {
		Item item = new Item(key, data);
		items.add(item);
	}
	public Iterator getItems() { return items.iterator(); }
}
