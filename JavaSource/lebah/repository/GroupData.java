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
public class GroupData {

	public static Vector getList(String category) throws Exception {
		Db db = null;
		String sql = "";
		Vector v = new Vector();
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.clear();
			r.add("group_id");
			r.add("group_name");
			r.add("category_id");
			r.add("category_id", category);
			sql = r.getSQLSelect("library_group");
			ResultSet rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				Group g = new Group();
				g.setId(rs.getString("group_id"));
				g.setName(rs.getString("group_name"));
				g.setCategoryId(rs.getString("category_id"));
				v.addElement(g);
			}
		} finally {
			if ( db != null ) db.close();
		}	
		return v;
	}
	
	public static Group getData(String group) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			Group g = null;
			SQLRenderer r = new SQLRenderer();
			if ( !"".equals(group)){
				r.clear();
				r.add("group_id");
				r.add("group_name");
				r.add("group_id", group);			
				sql = r.getSQLSelect("library_group g");
				
				ResultSet rs = stmt.executeQuery(sql);
				
				if ( rs.next() ) {
					g = new Group();
					g.setId(rs.getString("group_id"));
					g.setName(rs.getString("group_name"));
				}
			}
			if ( g == null ) {
				g = new Group();
				g.setId("no_category");
				g.setCategoryId("");
				g.setCategoryName("No Category");				
				sql =
				r
				.reset()
				.add("group_id", "no_category")
				.add("category_id", g.getCategoryId())
				.add("group_name", g.getCategoryName())
				.getSQLInsert("library_group")
				;
				stmt.executeUpdate(sql);

			}
			return g;
		} finally {
			if ( db != null ) db.close();
		}	
	}	

	public static void add(Group g) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			String uid = Long.toString(UniqueID.get());
			r.add("group_id", uid);
			r.add("group_name", g.getName());
			r.add("category_id", g.getCategoryId());
			sql = r.getSQLInsert("library_group");
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
			
			r.add("group_id", id);
			sql = r.getSQLDelete("library_item");
			stmt.executeUpdate(sql);
						
			r.clear();
			r.add("group_id", id);
			sql = r.getSQLDelete("library_group");
			stmt.executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}	
		
	}			


}
