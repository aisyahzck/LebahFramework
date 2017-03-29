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

package lebah.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;


public class ServletTemplate extends HttpServlet {
	
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException    {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		ServletContext context = this.getServletConfig().getServletContext();
        String pathInfo = req.getPathInfo();
        pathInfo = pathInfo != null ? pathInfo.substring(1) : ""; //get rid of the first '/'
		String module = pathInfo != null ? pathInfo : "";	    	
		if ( !"".equals(module) ) {
			Object object = null;
			try {
				Class klazz = Class.forName(module);
				object = klazz.newInstance();	
				
				if ( object instanceof IServlet )
					((IServlet) object).doService(req, res);
				else if ( object instanceof IServlet2 )
					((IServlet2) object).doService(req, res, context);
				
			} catch ( ClassNotFoundException cnfex ) {
				System.out.println(cnfex.getMessage());
			} catch ( InstantiationException iex ) {
				System.out.println(iex.getMessage());
			} catch ( IllegalAccessException illex ) {
				System.out.println(illex.getMessage());
			} catch ( Exception ex ) {
				System.out.println(ex.getMessage());
				ex.printStackTrace();
			}	
		}

	}
	
}
