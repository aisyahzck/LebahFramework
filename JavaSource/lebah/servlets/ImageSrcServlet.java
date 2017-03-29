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
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.db.Db;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ImageSrcServlet extends HttpServlet{

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3257005458144441142L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String id = request.getParameter("id");
		try {
			ServletOutputStream out = response.getOutputStream(); //ServletOutputStream
			response.setContentType("image/jpeg");
			out.write(getBlob(id));
			out.flush();
			out.close();			
		} catch (Exception e ) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter(); //PrintWriter
			e.printStackTrace(out);
			out.flush();
			out.close();			
		}
	}
  
	public byte[] getBlob(String id) throws Exception {
		String sql = "SELECT BINARYDATA FROM BLOBS WHERE ID = ?;";
		Db db = null;
		try {
			Blob blob = null;
			db = new Db();
			Connection con = db.getConnection();
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1,id);  

			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				blob = rs.getBlob(1);
			}
			byte[] bytes = blob.getBytes( 1, (int)(blob.length()));  
			return bytes;
		} finally {
			if ( db != null ) db.close();
		}

	}
}

 
