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
/*
 * Created on Apr 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lebah.db;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;

/**
 * @author Owner
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestPstmt {
	public static void main(String[] args) throws Exception {
		String[] ids = {"0607-0159", "0607-0160"};
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			r.add("name");
			r.add("icno");
			r.add("id", "");
			PreparedStatement pstmt = r.getPStmtSelect(db.getConnection(), "student");
			ResultSet rs = null;
			for ( int i=0; i < ids.length; i++ ) {
				r.set("id", ids[i]);		
				rs = r.getPStmt().executeQuery();
				if ( rs.next() ) {
				    String name = rs.getString("name");
				    String icno = rs.getString("icno");
				    System.out.println(name + ", " + icno);
				}
			}
		} finally {
			if ( db != null ) db.close();
		}
		
	}	
}
