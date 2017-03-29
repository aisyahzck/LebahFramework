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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.repository.Thumbnail;

public class ThumbnailServlet extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			response.setContentType("application/x-msdownload");
			String fullFileName = request.getParameter("file");
			ServletOutputStream outputStream = response.getOutputStream();
			createThumbnailInOutputStream(outputStream, stripFileName(fullFileName), getDirName(fullFileName));
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
	

	void createThumbnailInOutputStream(ServletOutputStream out, String fileName, String dir) {
		try {
			String imgName = dir + "/" + fileName;
			Thumbnail.createOutputStream(out, imgName, 120, 80, 75);
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	String stripFileName(String name) {
		return name.substring(name.lastIndexOf('/') + 1);
	}
	
	String getDirName(String name) {
		int fileLength = name.lastIndexOf('/');
		return name.substring(0, fileLength);
	}
	

}
