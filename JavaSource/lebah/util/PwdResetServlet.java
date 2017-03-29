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
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lebah.db.Db;


/**
 * @author Shamsul Bahrin
 */
public class PwdResetServlet extends HttpServlet {
	public void service(HttpServletRequest request,	HttpServletResponse response) throws IOException, javax.servlet.ServletException {
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		//get remote ip
		String ip = request.getRemoteAddr();
		if ( !ip.equals("127.0.0.1") ) {
			System.out.println("Operation disallowed!<br>");			
			return;
		}
		String all = request.getParameter("all");
		if ( all == null ) {
			String user_login = request.getParameter("login");
			try {
				out.println("Resetting password for : " + user_login);
				String sql = PasswordReset.pwd(user_login);
				out.println(sql + "<br>");
			} catch ( Exception e ) {
				out.println( e.getMessage() );
			}
		}
		else if ( all.equals("true") ) {
			try {
				String pwd = request.getParameter("password") != null ? request.getParameter("password") : "";
				updateAllPassword(out, pwd);
			} catch ( Exception ex ) {
				ex.printStackTrace(out);
			}
		}
		try {
			String pwd = "change";
			out.println("Password is " + pwd);
			updateAllPassword(out, pwd);		
		} catch ( Exception ex ) {
			ex.printStackTrace(out);
		}		
		
	}
	
	void updateAllPassword(PrintWriter out, String pwd) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			sql = "SELECT user_login FROM users";
			Vector users = new Vector();
			ResultSet rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				String user = rs.getString("user_login");
				if ( user != null ) users.addElement(user);
			}
			
			for ( int i=0; i < users.size(); i++ ) {
				String sql2 = PasswordReset.pwd((String) users.elementAt(i), pwd);
				out.println(sql2 + "<br>");
			}
		} finally {
			if ( db != null ) db.close();
		}
		
	}
}
