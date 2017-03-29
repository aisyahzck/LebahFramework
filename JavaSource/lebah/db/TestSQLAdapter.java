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

import java.sql.ResultSet;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class TestSQLAdapter {
    public static void main(String[] args) throws Exception {
        testSQLPStmtRenderer();
        testSQLRenderer();
    }   
    
    public static void testSQLRenderer() throws Exception {
        Db db = new Db();
        SQLRenderer r = new SQLRenderer();
        r.add("app_name");
        SQLRendererAdapter sql = new SQLRendererAdapter(r);
        ResultSet rs = sql.doSelect(db, "inceif_applicant");
        while ( rs.next() ) {
            String name = rs.getString("app_name");
            System.out.println(name);
        }
    }
    
    public static void testSQLPStmtRenderer() throws Exception {
        Db db = new Db();
        SQLPStmtRenderer r = new SQLPStmtRenderer();
        r.add("app_name");
        SQLRendererAdapter sql = new SQLRendererAdapter(r);
        ResultSet rs = sql.doSelect(db, "inceif_applicant");
        while ( rs.next() ) {
            String name = rs.getString("app_name");
            System.out.println(name);
        }
    }
}
