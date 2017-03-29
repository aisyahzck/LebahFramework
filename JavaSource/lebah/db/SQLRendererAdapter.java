/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class SQLRendererAdapter {
    
    SQLRenderer r;
    String sql = "";
    
    public SQLRendererAdapter(SQLRenderer r) {
       this.r = r; 
    }
    
    public ResultSet doSelect(Db db, String table) throws Exception {
        return doSelect(db, table, null, null);
    }
    
    public ResultSet doSelect(Db db, String table, String orderby) throws Exception {
        return doSelect(db, table, orderby, null);
    }
    
    public ResultSet doSelect(Db db, String table, String orderby, String option) throws Exception {
        if ( r instanceof SQLRenderer ) {
            
            if ( orderby == null && option == null ) sql = r.getSQLSelect(table);
            else if ( orderby != null && option == null ) sql = r.getSQLSelect(table, orderby);
            else if ( orderby != null && option != null ) sql = r.getSQLSelect(table, orderby, option);
            
            return db.getStatement().executeQuery(sql);
        }
        else if ( r instanceof SQLPStmtRenderer ){
            PreparedStatement pstmt = null;
            
            if ( orderby == null && option == null ) 
                pstmt = ((SQLPStmtRenderer) r).getPStmtSelect(db.getConnection(), table);
            else if ( orderby != null && option == null ) 
                pstmt = ((SQLPStmtRenderer) r).getPStmtSelect(db.getConnection(), table, orderby);
            else if ( orderby != null && option != null ) 
                pstmt = ((SQLPStmtRenderer) r).getPStmtSelect(db.getConnection(), table, orderby, option);            
            
            return pstmt.executeQuery();
        }
        else {
            return null;
        }
    }
    
    public void doInsert(Db db, String table) throws Exception {
        if ( r instanceof SQLRenderer ) {
            sql = r.getSQLInsert(table);
            db.getStatement().executeUpdate(sql);
        }
        else if ( r instanceof SQLPStmtRenderer) {
            PreparedStatement pstmt = ((SQLPStmtRenderer) r).getPStmtInsert(db.getConnection(), table);
            pstmt.executeUpdate();
        }
    }
    
    public void doUpdate(Db db, String table) throws Exception {
        if ( r instanceof SQLRenderer ) {
            sql = r.getSQLUpdate(table);
            db.getStatement().executeUpdate(sql);
        }
        else if ( r instanceof SQLPStmtRenderer) {
            PreparedStatement pstmt = ((SQLPStmtRenderer) r).getPStmtUpdate(db.getConnection(), table);
            pstmt.executeUpdate();
        }
    }

}
