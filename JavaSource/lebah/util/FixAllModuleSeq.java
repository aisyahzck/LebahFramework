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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class FixAllModuleSeq {
	public static void main(String[] args) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			//get users
			Vector users = new Vector();
			{
				sql = "SELECT DISTINCT user_login, tab_id FROM user_module GROUP BY user_login, tab_id";
				ResultSet rs = stmt.executeQuery(sql);
				while ( rs.next() ) {
					Hashtable tb = new Hashtable();
					tb.put("user_login", rs.getString("user_login"));
					tb.put("tab_id", rs.getString("tab_id"));
					users.addElement(tb);	
				}
			}
			
			{
				for ( int i=0; i < users.size(); i++ ) {
					Hashtable tb = (Hashtable) users.elementAt(i);
					fixModuleSequence(stmt, (String) tb.get("user_login"), (String) tb.get("tab_id"));
				}	
			}
		} catch ( SQLException ex ) {
			
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void fixModuleSequence(Statement stmt, String usrlogin, String tab) throws SQLException {
		Vector v = new Vector();
		String sql = "SELECT module_id FROM user_module WHERE tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
		ResultSet rs = stmt.executeQuery(sql);
		while ( rs.next() ) {
			String module_id = rs.getString("module_id");
			v.addElement(module_id);	
		}
		for ( int i=0; i < v.size(); i++ ) {
			String module_id = (String) v.elementAt(i);
			sql = "UPDATE user_module SET sequence = " + Integer.toString(i + 1) + " WHERE module_id = '" + module_id + "' AND tab_id = '" + tab + "' AND user_login = '" + usrlogin + "'";
			//-
			stmt.executeUpdate(sql);
		}
	}
			
}
