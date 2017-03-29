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

import java.sql.Statement;

import lebah.db.Db;
import lebah.db.SQLRenderer;

public class PasswordReset {
	
	public static String pwd(String user, String pass) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.update("user_login", user);
			r.add("user_password", PasswordService.encrypt(pass != null && !"".equals(pass) ? pass : user));
			sql = r.getSQLUpdate("users");
			stmt.executeUpdate(sql);
			return sql;
		} finally {
			if ( db != null ) db.close();
		}
		
	}
	
	public static String pwd(String user) throws Exception	{
		return pwd(user, "");
	}
	

}
