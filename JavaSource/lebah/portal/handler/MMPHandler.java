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

package lebah.portal.handler;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class MMPHandler implements DeviceHandler {
	
	public void process(ServletContext cx, HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		cx.getRequestDispatcher("/mobile" + req.getPathInfo()).forward(req, res);
	}

}
