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
package lebah.app.content;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.db.UniqueID;



/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1
 */

public class ArticleData {
	public static Vector getEntries(String catId) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			r
			.add("id")
			.add("title")
			.add("create_date")
			.add("cat_id", catId);
			sql = r.getSQLSelect("article_entry");
			ResultSet rs = db.getStatement().executeQuery(sql);
			Vector list = new Vector();
			while ( rs.next() ) {
				Entry e = new Entry();
				e.setId(rs.getString("id"));
				e.setTitle(rs.getString("title"));
				e.setCategory(catId);
				e.setCreateDate(rs.getDate("create_date"));
				list.addElement(e);
			}
			return list;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void addEntry(Entry e) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			r
			.add("id", lebah.db.UniqueID.getUID())
			.add("title", e.getTitle())
			.add("cat_id", e.getCategory())
			.add("create_date", r.unquote("now()"))
			;
			sql = r.getSQLInsert("article_entry");
			db.getStatement().executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void updateEntry(Entry e) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			r
			.update("id", e.getId())
			.add("title", e.getTitle())
			.add("cat_id", e.getCategory())
			;
			sql = r.getSQLUpdate("article_entry");
			db.getStatement().executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void deleteEntry(String id) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			sql = "delete from article_entry where id = '" + id + "'";
			db.getStatement().executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}
	}	
	
	public static Article getArticle(String subjectId) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("subject_id", subjectId);
			r.add("content_text");
			sql = r.getSQLSelect("article_text", "page_no");
			ResultSet rs = stmt.executeQuery(sql);
			Article p = new Article();
			p.setSubjectId(subjectId);
			int i = 0;
			while ( rs.next() ) {
				String txt = rs.getString(1);
				p.setText(i++, txt);
			}
			return p;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static Hashtable getPageContent(String subjectId, int page) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("subject_id", subjectId);
			r.add("page_no", page);
			r.add("content_text");
			r.add("page_id");
			sql = r.getSQLSelect("article_text");
			ResultSet rs = stmt.executeQuery(sql);
			Hashtable h = new Hashtable();
			if ( rs.next() ) {
				h.put("text", rs.getString(1));
				h.put("id", rs.getString(2));
			}
			return h;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void addNewPage(String subjectId, String text) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			int pageno = 0;
			{
				r.add("MAX(page_no) AS no ");
				r.add("subject_id", subjectId);
	   			sql = r.getSQLSelect("article_text");
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) pageno = rs.getInt(1);
				pageno++;
			}
			{
				r.clear();
				r.add("subject_id", subjectId);
				r.add("content_text", text);
				r.add("page_no", pageno);
				r.add("page_id", Long.toString(UniqueID.get()));
				sql = r.getSQLInsert("article_text");
				stmt.executeUpdate(sql);
			}
			
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void movePage(String subjectId, int from, int to) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			{
				r.clear();
				r.update("subject_id", subjectId);
				r.update("page_no", from);
				r.add("page_no", -99);
				sql = r.getSQLUpdate("article_text");

				stmt.executeUpdate(sql);
			}
			if ( from > to ) {
				{
					sql = "update article_text " +
					"set page_no = page_no + 1 " +
					"where subject_id = '" + subjectId + "' " +
					"and page_no > " + Integer.toString(to - 1);

					stmt.executeUpdate(sql);
				}
	
				//reNumber(stmt, subjectId, to-1, to+1);
				{
					r.clear();
					r.update("subject_id", subjectId);
					r.update("page_no", -99);
					r.add("page_no", to);
					sql = r.getSQLUpdate("article_text");

					stmt.executeUpdate(sql);
				}	
			}
			else if ( from < to ) {
				{
					sql = "update article_text " +
					"set page_no = page_no - 1 " +
					"where subject_id = '" + subjectId + "' " +
					"and page_no > " + Integer.toString(from);

					stmt.executeUpdate(sql);
				}
				{
					r.clear();
					r.update("subject_id", subjectId);
					r.update("page_no", -99);
					r.add("page_no", to);
					sql = r.getSQLUpdate("article_text");

					stmt.executeUpdate(sql);
				}
				
				
			}
			
			reNumber(stmt, subjectId, 0, 1);
			
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void reNumber(Statement stmt, String subjectId, int start, int num) throws Exception {
		String sql = "";
		SQLRenderer r = new SQLRenderer();
		Vector v = new Vector();
		{
			r.clear();
			r.add("page_id");
			r.add("subject_id", subjectId);
			r.add("page_no", start, ">");
			sql = r.getSQLSelect("article_text", "page_no");
			ResultSet rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				v.addElement(rs.getString(1));
			}
		}
		for ( int i=0; i < v.size(); i++ ) {
			String id = (String) v.elementAt(i);
			sql = "update article_text set page_no = " + num +
			" where page_id = '" + id + "'";
			stmt.executeUpdate(sql);
			num++; 
		}
	}
	
	public static void deletePage(String subjectId, int page) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			sql = "delete from article_text where page_no = " + page + " " +
			"and subject_id = '" + subjectId + "'";

			stmt.executeUpdate(sql);
			reNumber(stmt, subjectId, 0, 1);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void updatePage(String page_id, String text) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.update("page_id", page_id);
			r.add("content_text", text);
			sql = r.getSQLUpdate("article_text");
			stmt.executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void reNumber(String subjectId, int start, int num) throws Exception {
		Db db = null;
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			reNumber(stmt, subjectId, start, num);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void main(String[] args) throws Exception {
		//movePage("lms_science_1123318693302",4, 3);
		reNumber("article_content", 0, 1);
	}	
	
}
