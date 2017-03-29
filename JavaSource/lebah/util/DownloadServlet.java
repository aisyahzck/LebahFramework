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
package lebah.util;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class DownloadServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			response.setContentType("application/x-msdownload");
			String file_name = request.getParameter("file");
			com.jspsmart.upload.SmartUpload myUpload = new com.jspsmart.upload.SmartUpload();
			myUpload.initialize(getServletConfig(), request, response);
			myUpload.downloadFile(file_name);	
		} catch ( Exception e ) {
			showErrorMessage(request, response, e.getMessage());	
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);			
	}	
	
	void showErrorMessage(HttpServletRequest request, HttpServletResponse response, String message) {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("<html>");
			out.println("<head>");
			out.println("<title>ERROR</title>");
			out.println("</head>");
			out.println("<body bgcolor=\"white\">");
			out.println("<span class=\"bold\">");
			out.println(message);
			out.println("</span>");
			out.println("</body>");
			out.println("</html>");		
		} catch ( Exception e ) {
			System.out.println(e.getMessage());	
		}		
	}
	
}
