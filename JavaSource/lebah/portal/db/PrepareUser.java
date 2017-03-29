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
package lebah.portal.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;
import lebah.portal.element.User;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class PrepareUser {
	public static Vector retrieve(String role) throws DbException {
		Db db = null;
		String sql = "";
		try { 
			db = new Db();
			Statement stmt = db.getStatement();

			sql = "SELECT user_login, user_name, user_password FROM users " +
			"WHERE user_role = '" + role + "'";

			ResultSet rs = stmt.executeQuery(sql);
			
			Vector v = new Vector();
			while ( rs.next() ) {
				String usrlogin = rs.getString("user_login");
				String name = rs.getString("user_name");
				String password = rs.getString("user_password");
				User user = new User(usrlogin, name, password, role);
				v.addElement(user);
			}
			return v;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static User getUserById(String usrlogin) throws Exception {
		User user = null;
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			String name = "", password = "", role = "", login_alt = "";
			{
				sql = "SELECT user_name, user_password, user_role, user_login_alt " +
				"FROM users WHERE user_login = '" + usrlogin + "'";	
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) {
					name = rs.getString("user_name");
					password = rs.getString("user_password");
					role = rs.getString("user_role");
					login_alt = rs.getString("user_login_alt");
						
				}
			}
			String theme = getTheme(usrlogin);
			user = new User(usrlogin, name, password, role, theme);	
			user.setAlternateLogin(login_alt);
		} catch ( SQLException ex ) {
			throw new Exception(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}			
		return user;
	}
	
	public static void updateBiodata(String usrlogin, Hashtable data) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			r.update("user_login", usrlogin);
			r.add("user_name", (String) data.get("user_name"));
			sql = r.getSQLUpdate("users");
			db.getStatement().executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}	
	}
	
	public static void updatePassword(String usrlogin, String password) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			r.update("user_login", usrlogin);
			r.add("user_password", lebah.util.PasswordService.encrypt(password));
			sql = r.getSQLUpdate("users");
			db.getStatement().executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}		
	}
	
	public static String getRole(String usrlogin) {
		String role = "";
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			{
				sql = "SELECT user_role " +
				"FROM users WHERE user_login = '" + usrlogin + "'";	
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) {
					role = rs.getString("user_role");
				}
				else
					role = "";
			}
		} catch ( Exception ex ) {
			//throw ex;
			System.out.println( ex.getMessage() );
		} finally {
			if ( db != null ) db.close();
		}	
		return role;		
	}
	
	public static void updateRole(String usrlogin, String role) throws Exception {
		String theme = "";
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.update("user_login", usrlogin);
			r.add("user_role", role);
			sql = r.getSQLUpdate("users");
			stmt.executeUpdate(sql);
		} catch ( Exception ex ) {
			throw ex;
		} finally {
			if ( db != null ) db.close();
		}		
	}		
	
	public static String getTheme(String usrlogin) throws Exception {
		String theme = "";
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			{
				sql = "SELECT u.css_name " +
				"FROM user_css u, page_css p " +
				"WHERE u.css_name = p.css_name AND user_login = '" + usrlogin + "'";	
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) {
					theme = rs.getString("css_name");
				}
				else
					theme = "";
			}
		} finally {
			if ( db != null ) db.close();
		}	
		return theme;		
	}	
	
	public static void updateTheme(String usrlogin, String css_name) throws Exception {
		String theme = "";
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			boolean found = false;
			{
				r.add("css_name");
				r.add("user_login", usrlogin);
				sql = r.getSQLSelect("user_css");
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) found = true;
			}
			
			if ( found ) {
				r.clear();
				r.update("user_login", usrlogin);
				r.add("css_name", css_name);
				sql = r.getSQLUpdate("user_css");
				stmt.executeUpdate(sql);
			} else {
				r.clear();
				r.add("user_login", usrlogin);
				r.add("css_name", css_name);
				sql = r.getSQLInsert("user_css");
				//-
				stmt.executeUpdate(sql);				
			}
			
		} finally {
			if ( db != null ) db.close();
		}		
	}	
	

	
}
