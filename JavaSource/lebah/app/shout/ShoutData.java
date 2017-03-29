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

package lebah.app.shout;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.db.UniqueID;
import lebah.util.DateTool;

public class ShoutData {
	
	public static void addMessage(String category_id, String user_id, String user_name, String remoteAddr, String shout_text) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			String uid = UniqueID.getUID();
			SQLRenderer r = new SQLRenderer();
			r.add("uid", uid);
			r.add("category_id", category_id);
			r.add("user_id", user_id);
            r.add("user_name", user_name);
			r.add("shout_text", shout_text);
			r.add("shout_date", DateTool.getCurrentDate());
            r.add("remote_addr", remoteAddr);
			sql = r.getSQLInsert("shout_box");

			stmt.executeUpdate(sql);
			
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static Vector getMessageList(String category_id, String limit) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("category_id", category_id);
			r.add("uid");
			r.add("user_id");
            r.add("user_name");
			r.add("shout_text");
			r.add("shout_date");
            r.add("remote_addr");
			sql = r.getSQLSelect("shout_box", "shout_date desc");
            sql += " limit " + limit;
			ResultSet rs = stmt.executeQuery(sql);
			Vector list = new Vector();
			java.text.DateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm a");
			while ( rs.next() ) {
				String uid = rs.getString(1);
				String user_id = rs.getString(2);
                String user_name = rs.getString(3);
				String shout_text = rs.getString(4);
				//Date shoutDate = db.getDate(rs, "shout_date");
                Date shoutDate = rs.getTimestamp("shout_date");
                String remote_addr = rs.getString("remote_addr");
				Hashtable h = new Hashtable();
				h.put("uid", uid);
				h.put("user_id", user_id);
                h.put("user_name", user_name);
				h.put("shout_text", shout_text);
				//h.put("shout_date", DateTool.getDateTimeFormatted(shoutDate));
				h.put("shout_date", df.format(shoutDate));
                h.put("remote_addr", remote_addr);
				list.addElement(h);
			}
			return list;
		} finally {
			if ( db != null ) db.close();
		}		
	}
	
	public static void deleteShout(String uid) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			sql = "delete from shout_box where uid = '" + uid + "'";
            
			db.getStatement().executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}
	}

}
