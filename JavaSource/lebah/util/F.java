/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.util;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class F {
	public static final String MAILSERVER = "202.190.118.120";
	//public static final String MAILSERVER = "172.31.1.24";
	public static String str(String s, int len) {
		String result = s;
		if ( s.length() > len ) {
			result = s.substring(0, len);
		}
		return result;
	}
	
}
