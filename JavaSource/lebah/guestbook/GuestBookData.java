/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or





but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.guestbook;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import lebah.db.DataHelper;
import lebah.db.SQLRenderer;
import lebah.db.UniqueID;
import lebah.util.CDate;
import lebah.util.DateTool;

public class GuestBookData {
	
	public static void add(final GuestBook g) throws Exception {
		String uid = UniqueID.getUID();
		g.setUid(uid);
		new DataHelper() {
			public String doSQL() {
				SQLRenderer r = new SQLRenderer();
				r.add("uid", g.getUid());
				r.add("module_id", g.getCategoryId());
				r.add("posted_date", DateTool.getCurrentDate());
				r.add("posted_by", g.getPostedBy());
				r.add("message", g.getMessage());
				r.add("remote_address", g.getRemoteAddress());
				r.add("email", g.getEmail());
				r.add("homepage", g.getHomepage());
				String sql = r.getSQLInsert("guestbook");
				//-
				return sql;
			}
		}.execute();
	}
	
	public static void delete(final String uid) throws Exception {
		new DataHelper() {
			public String doSQL() {
				SQLRenderer r = new SQLRenderer();
				r.add("uid", uid);
				return r.getSQLDelete("guestbook");
			}
		}.execute();
	}
	
	public static Vector getList(final String categoryId) throws Exception {
		return new DataHelper() {
			public String doSQL() {
				SQLRenderer r = new SQLRenderer();
				r.add("module_id", categoryId);
				prepareRenderer(r);
				return r.getSQLSelect("guestbook", "posted_date desc");
			}

			public Object createObject(ResultSet rs) throws Exception {
				return readGuestBookData(rs);
			}
		}.getObjectList();
	}
	

	
	public static GuestBook getGuestBook(final String uid) throws Exception {
		return (GuestBook) new DataHelper() {
			public String doSQL() {
				SQLRenderer r = new SQLRenderer();
				r.add("uid", uid);
				prepareRenderer(r);
				return r.getSQLSelect("guestbook");
			}
			
			public Object createObject(ResultSet rs) throws Exception {
				return readGuestBookData(rs);
			}
		}.getObject();
	}
	
	private static Object readGuestBookData(ResultSet rs) throws SQLException {
		GuestBook g = new GuestBook();
		g.setUid(rs.getString("uid"));
		g.setPostedBy(rs.getString("posted_by"));
		g.setPostedDate(rs.getDate("posted_date"));
		g.setPostedTime(rs.getTime("posted_date"));
		g.setMessage(rs.getString("message"));
		g.setRemoteAddress(rs.getString("remote_address"));
		g.setEmail(rs.getString("email"));
		g.setHomepage(rs.getString("homepage"));
		return g;
	}
	
	private static void prepareRenderer(SQLRenderer r) {
		r.add("uid");
		r.add("posted_date");
		r.add("posted_by");
		r.add("message");
		r.add("remote_address");
		r.add("email");
		r.add("homepage");
	}

}
