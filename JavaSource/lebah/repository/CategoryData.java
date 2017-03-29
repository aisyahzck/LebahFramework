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
package lebah.repository;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.db.UniqueID;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class CategoryData {

	public static Vector getList() throws Exception {
		Db db = null;
		String sql = "";
		Vector v = new Vector();
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.clear();
			r.add("category_id");
			r.add("category_name");
			r.add("category_type");
			sql = r.getSQLSelect("library_category");
			ResultSet rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				Category c = new Category();
				c.setId(rs.getString("category_id"));
				c.setName(rs.getString("category_name"));
				c.setType(rs.getString("category_type"));
				v.addElement(c);
			}
		} finally {
			if ( db != null ) db.close();
		}	
		return v;
	}
	
	public static Category getData(String id) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.clear();
			r.add("category_id", id);
			r.add("category_name");
			r.add("category_type");
			sql = r.getSQLSelect("library_category");
			ResultSet rs = stmt.executeQuery(sql);
			Category c = new Category();
			if ( rs.next() ) {
				
				c.setId(id);
				c.setName(rs.getString("category_name"));
				c.setType(rs.getString("category_type"));
			}
			return c;
		} finally {
			if ( db != null ) db.close();
		}	
	}	

	public static void add(Category c) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			String uid = Long.toString(UniqueID.get());
			r.add("category_id", uid);
			r.add("category_name", c.getName());
			r.add("category_type", c.getType());
			sql = r.getSQLInsert("library_category");
			stmt.executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}	
	}	
	
	public static void delete(String id) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			
			r.add("category_id", id);
			sql = r.getSQLDelete("library_group");
			stmt.executeUpdate(sql);
			
			
			r.clear();
			r.add("category_id", id);
			sql = r.getSQLDelete("library_category");
			stmt.executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}	
		
	}			


}
