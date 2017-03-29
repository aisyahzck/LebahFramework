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
package lebah.tree;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Category {
	private String id;
	private String order_no;
	private String name;

	public Category(String order_no, String name) {
		this.order_no = order_no;
		this.name = name;
	}

	public Category(String id, String order_no, String name) {
		this.id = id;
		this.order_no = order_no;
		this.name = name;
	}

	public void setId(String id) { this.id = id; }
	public String getId() { return id; }
	public String getOrder() { return order_no; }
	public String getName() { return name; }

}
