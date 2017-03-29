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
package lebah.portal.action;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */

public class RequestUtil {

	public static String getParam(HttpServletRequest request, String param) {
		return request.getParameter(param) != null ? request.getParameter(param) : "";
	}	
	public static int getParamAsInteger(HttpServletRequest request, String param) {
		return getParam(request, param) != "" ? Integer.parseInt(getParam(request, param)) : 0;	 
	}	
}
