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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.db.UniqueID;

import java.util.*;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ItemData {
	
	class SearchCriteria {
		String[] facultyList;
		String[] fileTypeList;
		String uploadDate;
		String days;
		String date_upload_from;
		String date_upload_to;
	}
	
	public static Vector searchItemByCriteria(
			String group,
			String[] facultyList,
			String[] fileTypeList,
			String uploadDate,
			String days,
			String date_upload_from,
			String date_upload_to
			) throws Exception {
		
		SearchCriteria c = new ItemData().new SearchCriteria();
		c.facultyList = facultyList;
		c.fileTypeList = fileTypeList;
		c.uploadDate = uploadDate;
		c.days = days;
		c.date_upload_from = date_upload_from;
		c.date_upload_to = date_upload_to;
		
		return getList(group, c);

	}
	
	public static Vector getList(String group, SearchCriteria c) throws Exception {
		
		
		Db db = null;
		String sql = "";
		Vector<Item> v = new Vector<Item>();
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.clear();
			r.add("i.item_id");
			r.add("i.item_title");
			r.add("i.item_name");
			r.add("i.file_name");
			r.add("i.thumb_filename");
			r.add("i.item_description");
			r.add("i.user_id");
			r.add("i.date_upload");
			r.add("u.user_name");
			r.add("i.faculty_code");
			r.add("i.group_id");
			r.add("i.url_path");
			r.add("i.group_id", group);
			r.relate("i.user_id", "u.user_login");
			
			sql = r.getSQLSelect("library_item i, users u", "date_upload");
			//-
			if ( c.facultyList != null ) {
				sql += " AND (";
				int i=0;
				for ( String faculty : c.facultyList ) {
					if ( ++i > 1 ) sql += " OR ";
					sql += " i.faculty_code = '" + faculty + "' ";
				}
				sql += ") ";
			}
			if ( c.fileTypeList != null ) {
				sql += " AND (";
				int i=0;
				for ( String type : c.fileTypeList ) {
					if ( ++i > 1 ) sql += " OR ";
					sql += " i.file_type = '" + type + "' ";
				}
				sql += ") ";
			}
			
			//-
			
			ResultSet rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				Item item = prepareItem(group, rs);
				v.addElement(item);
			}
			
		} finally {
			if ( db != null ) db.close();
		}	
		return v;
	}	
	
	
	public static String[] getFileTypes() throws Exception {
		return new String[] {"Image", "Audio", "Interactive", "Text Document"};
	}
	
	public static Hashtable facultyCodeToName() throws Exception {
		Db db = null;
		Hashtable<String, String> h = new Hashtable<String, String>();
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql =
			r
			.add("faculty_code")
			.add("faculty_name")
			.getSQLSelect("faculty")
			;
			ResultSet rs = db.getStatement().executeQuery(sql);
			while ( rs.next() ) {
				h.put(rs.getString(1), rs.getString(2));
			}
			return h;
		} finally {
			if ( db != null ) db.close();
		}
	}

	public static Vector getList(String group) throws Exception {
		Db db = null;
		String sql = "";
		Vector<Item> v = new Vector<Item>();
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.clear();
			r.add("i.item_id");
			r.add("i.item_title");
			r.add("i.item_name");
			r.add("i.file_name");
			r.add("i.thumb_filename");
			r.add("i.item_description");
			r.add("i.user_id");
			r.add("i.date_upload");
			r.add("u.user_name");
			r.add("i.faculty_code");
			r.add("i.group_id");
			r.add("i.url_path");
			r.add("i.group_id", group);
			r.relate("i.user_id", "u.user_login");
			sql = r.getSQLSelect("library_item i, users u", "date_upload");
			ResultSet rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				Item item = prepareItem(group, rs);
				v.addElement(item);
			}
			
		} finally {
			if ( db != null ) db.close();
		}	
		return v;
	}

	private static Item prepareItem(String group, ResultSet rs) throws SQLException, Exception {
		Item item = new Item();
		item.setId(rs.getString("item_id"));
		item.setTitle(rs.getString("item_title"));
		item.setName(rs.getString("item_name"));
		String filename = rs.getString("file_name");
		item.setFileName(filename);
		String thumb_filename = rs.getString("thumb_filename");
		item.setThumbFilename(thumb_filename);
		String txt = putLineBreak(Db.getString(rs, "item_description"));
		item.setDescription(txt);
		item.setGroupId(Db.getString(rs, "group_id"));
		item.setUserId(Db.getString(rs, "user_id"));
		item.setUploadDate(Db.getDate(rs, "date_upload"));
		item.setUserName(Db.getString(rs, "user_name"));
		item.setFacultyCode(rs.getString("faculty_code"));
		item.setPath(Db.getString(rs, "url_path"));
		//determine the item type
		String ext = item.getName().indexOf(".") > -1 ?
				item.getName().substring(item.getName().indexOf(".")) : "";
		item.setType(evaluateBasedOnExt(ext));
		return item;
	}
	
	private static Item prepareItem(ResultSet rs) throws SQLException, Exception {
		return prepareItem("", rs); 
	}
	


	public static Item getItem(String uid) throws Exception {
		Db db = null;
		String sql = "";
		Vector v = new Vector();
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.clear();
			r.add("i.item_id", uid);
			r.add("i.item_id");
			r.add("i.item_title");
			r.add("i.item_name");
			r.add("i.file_name");
			r.add("i.thumb_filename");
			r.add("i.item_description");
			r.add("i.user_id");
			r.add("i.date_upload");
			r.add("i.faculty_code");
			r.add("u.user_name");
			r.add("i.group_id");
			r.add("i.url_path");
			r.relate("i.user_id", "u.user_login");
			sql = r.getSQLSelect("library_item i, users u");
			ResultSet rs = stmt.executeQuery(sql);
			Item item = new Item();
			if ( rs.next() ) {
				item = prepareItem(rs);
				
			}
			return item;
		} finally {
			if ( db != null ) db.close();
		}	
	}	
	
	public static String add(Item item) throws Exception {
		Db db = null;
		String sql = "";
		//determine the item type
		String ext = item.getName().indexOf(".") > -1 ?
				item.getName().substring(item.getName().indexOf(".")) : "";
		item.setType(evaluateBasedOnExt(ext));	
		
		try {
			String uid = Long.toString(UniqueID.get());
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.add("item_id", uid);
			r.add("item_title", item.getTitle());
			r.add("item_name", item.getName());
			r.add("item_description", item.getDescription());
			r.add("file_name", item.getFileName());
			r.add("file_type", item.getTypeName());
			r.add("thumb_filename", item.getThumbFilename());
			r.add("group_id", item.getGroupId());
			r.add("user_id", item.getUserId());
			r.add("faculty_code", item.getFacultyCode());
			r.add("date_upload", r.unquote("now()"));
			r.add("url_path", item.getPath());
			sql = r.getSQLInsert("library_item");
			stmt.executeUpdate(sql);
			return uid;
		} finally {
			if ( db != null ) db.close();
		}	
		
	}
	
	public static String update(String uid, String description) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.update("item_id", uid);
			r.add("item_description", description);
			sql = r.getSQLUpdate("library_item");
			stmt.executeUpdate(sql);
			return uid;
		} finally {
			if ( db != null ) db.close();
		}	
		
	}
	
	public static String update(String uid, String title, String description) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			r.update("item_id", uid);
			r.add("item_title", title);
			r.add("item_description", description);
			sql = r.getSQLUpdate("library_item");
			stmt.executeUpdate(sql);
			return uid;
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
			r.add("item_id", id);
			sql = r.getSQLDelete("library_item");
			stmt.executeUpdate(sql);
		} finally {
			if ( db != null ) db.close();
		}	
		
	}	
	
	public static void delete(String[] ids) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			for ( int i=0; i < ids.length; i++ ) {
				r.clear();
				r.add("item_id", ids[i]);
				sql = r.getSQLDelete("library_item");
				stmt.executeUpdate(sql);
			}
		} finally {
			if ( db != null ) db.close();
		}	
		
	}
	


    static String putLineBreak(String str) {
    	StringBuffer txt = new StringBuffer(str);
    	char c = '\n';
    	while (txt.toString().indexOf(c) > -1) {
    		int pos = txt.toString().indexOf(c);
    		txt.replace(pos, pos + 1, "<br>");
    	}
    	return txt.toString();
    } 
    
	public static ItemType evaluateBasedOnExt(String ext) {
		
		if ( 	
				ext.equalsIgnoreCase(".png")
			|| 	ext.equalsIgnoreCase(".gif")
			|| 	ext.equalsIgnoreCase(".jpg")
			|| 	ext.equalsIgnoreCase(".jpeg")
			||	ext.equalsIgnoreCase(".wav")
			) {
			return ItemType.Image;
		}		
		else if ( 
				ext.equalsIgnoreCase(".mov")
			||	ext.equalsIgnoreCase(".avi")
			||	ext.equalsIgnoreCase(".mpg")
			||	ext.equalsIgnoreCase(".mpeg")
			) {
			return ItemType.Interactive;
		}
		else if ( 
				ext.equalsIgnoreCase(".swf")
			) {
			return ItemType.Flash;
		}
		else if ( 
				ext.equalsIgnoreCase(".doc")
			||	ext.equalsIgnoreCase(".xls")
			||	ext.equalsIgnoreCase(".ppt")
			||	ext.equalsIgnoreCase(".txt")
			||	ext.equalsIgnoreCase(".pdf")
			) {
			return ItemType.TextDocument;
		}
		else if ( 
				ext.equalsIgnoreCase(".wav")
			) {
			return ItemType.Audio;
		}
		return ItemType.Image;
	}    

}
