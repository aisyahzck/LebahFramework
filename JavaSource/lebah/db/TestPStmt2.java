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

package lebah.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TestPStmt2 {
	
	public static void main(String[] args) throws Exception {
		testInsert();
	}
	
	public static void testUpdate() throws Exception {
		
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			
			r.update("id", "")
			.add("name", "")
			.add("icno", "")
			;
			
			r.getPStmtUpdate(db.getConnection(), "person");
			for ( int i=0; i < 10; i++ ) {
				r.setUpdate("id", "id - " + i)
				.set("name", "Name - " + i)
				.set("icno", "IC No - " + i)
				;
				r.getPStmt().executeUpdate();
			}
		} finally {
			if ( db != null ) db.close();
		}
		
	}	
	
	public static void testInsert() throws Exception {
		
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			
			r.add("id", "")
			.add("name", "")
			.add("icno", "")
			;
			
			PreparedStatement pstmt = r.getPStmtInsert(db.getConnection(), "person");
			ResultSet rs = null;
			for ( int i=0; i < 10; i++ ) {
				r.set("id", "id - " + i)
				.set("name", "Name - " + i)
				.set("icno", "IC No - " + i)
				;
				r.getPStmt().executeUpdate();
			}
		} finally {
			if ( db != null ) db.close();
		}
		
	}		
}
