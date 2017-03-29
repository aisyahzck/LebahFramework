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

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class NoSuchItemException extends Exception {
	private String msg;
	public NoSuchItemException() {
		super("NoSuchItemException: Item not found in the database.");
	}
	public NoSuchItemException(String s) {
		this();
		msg = s;
	}
	public void setMessage(String s) { msg = s; }
	public String getMessage() { return msg; }
}
