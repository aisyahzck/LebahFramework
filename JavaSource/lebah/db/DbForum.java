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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import lebah.app.Forum;
import lebah.app.ForumData;
import lebah.tree.Attachment;
import lebah.util.CalendarList;
import lebah.util.DateTool;
import lebah.util.SimpleDate;



/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class DbForum extends CollabDb {
	private Forum parent;
	private String forum_parent_id = "0";

	private static final String sql_main = "select f.id, f.category_id, f.member_id,  " +
	"f.posted_date, p.user_name as name, p.avatar, f.title, f.description, " +
	"f.message_text, f.parent_id, f.rate " +
	"from forum f, users p where f.member_id = p.user_login ";

	public DbForum() throws DbException {
		super();
		parent = new Forum("Root");
	}
	
	/**
	@Deprecated - don't use
	*/
	public void initializeMember(String memberid, String name) throws SQLException {
		boolean isMember = false;
		{
			sql = "select member_id from member where member_id = '" + memberid + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) {
				isMember = true;
			}
		}
		if ( !isMember ) {
			sql = "insert into member (member_id, login, password, name) values ('" + memberid + "', '" +
			memberid + "', '" + memberid + "', '" + rep(name) + "')";	
			stmt.executeUpdate(sql);
		}
		
	}

	private Forum readData(ResultSet rs) throws SQLException {
		String id = rs.getString("id");
		String category_id = rs.getString("category_id");
		String member_id = rs.getString("member_id");
		java.sql.Date postdate = rs.getDate("posted_date");
		String member_name = rs.getString("name");
		if (member_name == null) member_name = "";
		String title = rs.getString("title");
		if (title == null) title = "";
		String description = rs.getString("description");
		forum_parent_id = rs.getString("parent_id");
		if ( forum_parent_id == null ) forum_parent_id = "0";

		String message_text = rs.getString("message_text");
		if ( message_text == null ) message_text = "";
		
		String avatar = rs.getString("avatar");
		if ( avatar == null ) avatar = "";
		
		int rate = rs.getInt("rate");
		
		Forum forum = new Forum(title);
		forum.setId(id);
		forum.setUserId(member_id);
		forum.setDatePosted(new SimpleDate(postdate));
		forum.setPostedBy(member_name);
		forum.setCategoryId(category_id);
		forum.setDescription(description);
		//forum.setNotes(notes.toString());
		forum.setNotes(message_text);
		forum.setAvatar(avatar);
		forum.setRate(rate);
		
		forum.setPostedDate(postdate);
		
		return forum;
	}

	public Forum getItem(String id) throws DbException, NoSuchItemException {
		Forum forum = null;
		try {
			sql = sql_main + " and f.id = '" + id + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) {
				forum = readData(rs);
				if ( !forum_parent_id.equals("0") ) {
					Forum parentF = getItem(forum_parent_id);
					forum.setParent(parentF);
				}
			}
			else
				throw new NoSuchItemException();
		}catch(SQLException sqle) {
			throw new DbException("DbForum.get() :" + sqle.getMessage() + "; " + sql);
		}
		try {
			prepare_attachment(forum);
		}catch(SQLException sqle) {
			throw new DbException("DbForum.get() :" + sqle.getMessage() + "; " + sql);
		}
		return forum;
	}


	public List getRootList(String category_id) throws DbException {
		List list = new ArrayList();
		try {
			sql = sql_main + " and f.is_parent = 1 and f.category_id = '" + category_id +
			"' and is_delete = 0 order by f.posted_date desc";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Forum forum = readData(rs);
				list.add(forum);
			}
		} catch(SQLException sqle) {
			throw new DbException("DbForum.getRootList(): " + sqle.getMessage() + "; " + sql);
		}
		return list;
	}
	public Iterator getRootIterator(String category_id) throws DbException {
		return getRootList(category_id).iterator();
	}
	
	public Vector getRootVector(String category_id) throws DbException {
		Vector list = new Vector();
		try {
			sql = sql_main + " and f.is_parent = 1 and f.category_id = '" + category_id +
			"' and is_delete = 0 order by f.posted_date desc";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Forum forum = readData(rs);
				list.addElement(forum);
			}
			//for each post get number of replies
			try {
				Hashtable h = ForumData.getReplyCount(list);
				for ( int i=0; i < list.size(); i++ ) {
					Forum forum = (Forum) list.elementAt(i);
					forum.setReplyCount(((Integer)h.get(forum.getId())).intValue());
				}
			} catch ( Exception e ) {
				throw new DbException(e.getMessage());
			}
			//for each forum get attachments
			try {
				for ( int i=0; i < list.size(); i++ ) {
					Forum forum = (Forum) list.elementAt(i);
					prepare_attachment(forum);
				}
			} catch ( Exception e ) {
				throw new DbException(e.getMessage());
			}			
		} catch(SQLException sqle) {
			throw new DbException("DbForum.getRootVector(): " + sqle.getMessage() + "; " + sql);
		}
		return list;
	}	


	public void add(Forum child) throws DbException {
		String id = "0";
		add(id, child);
	}

	public void add(Forum parent, Forum child) throws DbException {
		String parentid = parent.getId();
		add(parentid, child);
	}

	public void add(String parentid, Forum child) throws DbException {
		try {
			String title = child.getTitle();
			if (title.length() > 255 ) title = title.substring(0, 255);
			String member_id = child.getUserId();
			String category_id = child.getCategoryId();
			String classroom_id = child.getClassroomId();
			
			//check if need activation
			if ( parentid.equals("0") ) activation(member_id, category_id);
			
			String description = child.getDescription();
			String message_text = child.getNotes();


			String forum_id = uid();
			String today = DateTool.getDateStr(new Date());
			
			/*
			sql = "insert into forum (id, posted_date, title, parent_id, is_parent, member_id, " +
			"category_id, is_delete, description, " +
			"message_text) values (";
			sql += "'" + forum_id + "', ";
			sql += "'" + today + "', ";
			sql += "'" + rep(title) + "', ";
			if ( parentid.equals("0") ) {
				sql += "0, 1" ;
			} else sql += "'" + parentid + "', 0";
			sql += ", '" + member_id + "', '" +
			category_id + "', 0, '" + rep(description) + "', '" +
			rep(message_text) + "')";
			*/
			
			SQLRenderer r = new SQLRenderer();
			r
			.add("id", forum_id)
			.add("posted_date", today)
			.add("title", title)
			;
			if ( parentid.equals("0")){
				r.add("parent_id", 0);
				r.add("is_parent", 1);
			}
			else {
				r.add("parent_id", parentid);
				r.add("is_parent", 0);				
			}
			r
			.add("member_id", member_id)
			.add("category_id", category_id)
			.add("classroom_id", classroom_id)
			.add("is_delete", 0)
			.add("description", description)
			.add("message_text", message_text)
			;
			sql = r.getSQLInsert("forum");
            
            //-

			stmt.executeUpdate(sql);

			
			//check for attachment
			if ( child.isAttached() ) {

				

				Attachment attach = child.getAttachment();
				String sub_directory = child.getAttachmentFolder();
				List fileList = attach.get();
				if ( !fileList.isEmpty() ) {
					Iterator files = fileList.iterator();
					while ( files.hasNext() ) {
						String filename = (String) files.next();
						save_attachment(forum_id, filename, sub_directory);
					}
				}
			}

		}catch(SQLException sqle) {
			throw new DbException("DbForum.add(): " + sqle.getMessage() + sql);
		}
	}
	
	private void activation(String member_id, String category_id) throws SQLException {
		boolean result = false;
		sql = "select id from forum where is_parent = 2 and category_id = '" + category_id + "'";
		ResultSet rs = stmt.executeQuery(sql);
		String today = DateTool.getDateStr(new Date());
		if ( !rs.next() ) {
			sql = "insert into forum (id, posted_date, title, parent_id, is_parent, " +
			"member_id, category_id, is_delete, description, message_text) " +
			"values (0, '" + today + "', 'ACTIVATION', 0, 2, '" + 
			member_id + "', '" + category_id + "', 0, 'ACTIVATION','ACTIVATION') ";
			stmt.executeUpdate(sql);
			//System.out.println("FORUM ACTIVATION");
		}
	}

	private String getForumId(String title, String parent_id, String member_id, String category_id) throws SQLException {
		String id = "";
		sql = "select id from forum where title = '" + title + "' " +
		" and parent_id = '" + parent_id +
		"' and member_id = '" + member_id + "' " +
		" and category_id = '" + category_id + "'";
		ResultSet rs = stmt.executeQuery(sql);
		if ( rs.next() )
			id = rs.getString("id");
		return id;

	}
	
	private void save_attachment(String forumid, String filename, String subdirectory) throws SQLException {
		sql = "insert into forum_attachment (forum_id, file_name, directory) values ('" +
		forumid + "', '" + filename + "', '" + subdirectory + "') ";
		stmt.executeUpdate(sql);
	}
	
	private void delete_attachment(String forumid, String filename) throws SQLException {
		sql = "delete from forum_attachment where forum_id = '" + forumid + "' and file_name = '" + filename + "'";
		stmt.executeUpdate(sql);
	}	
	
	private void delete_all_attachment(String forumid) throws SQLException {
		sql = "delete from forum_attachment where forum_id = '" + forumid + "'";
		stmt.executeUpdate(sql);
	}	

	private Iterator manage_notes(String txt) {
		List notefields = new ArrayList();
		int len = txt.length();
		int start = 0;
		int end = 255;
		while (true) {
			if (end < len) {
				String notes = txt.substring(start, end);
				notefields.add(notes);
				start = end;
				end = start + 255;
			} else {
				end = len;
				String notes = txt.substring(start, end);
				notefields.add(notes);
				break;
			}
		}
		return notefields.iterator();
	}



	public Forum get(String pid) throws DbException, NoSuchItemException {
		parent = getItem(pid);
		List list = new ArrayList();
		try {
			sql = sql_main +
			" and f.parent_id = '" + pid + "' and is_delete = 0 order by f.posted_date desc";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				Forum forum = readData(rs);
				list.add(forum);
			}
			methodA(parent, list);
		} catch(SQLException sqle) {
			throw new DbException("DbForum.get(): " + sqle.getMessage() + "; " + sql);
		}
		return parent;
	}

	public Forum get(Forum p) throws DbException, NoSuchItemException {
		String id = p.getId();
		return get(id);
	}

	public Forum get() throws DbException, NoSuchItemException {
		return get("0");
	}

	private void methodA(Forum fo, List list) throws DbException, SQLException {
		Iterator itr = list.iterator();
		while (itr.hasNext()) {
			Forum forum = (Forum) itr.next();
			//***
			prepare_attachment(forum);
			//
			fo.add(forum);
			methodB(forum);
		}
	}

	private void methodB(Forum forum) throws DbException, SQLException {
		List list = new ArrayList();
		String s = sql_main +
		"and f.parent_id = '" + forum.getId() + "' and is_delete = 0 order by f.posted_date";
		ResultSet rs = stmt.executeQuery(s);
		while (rs.next()) {
			Forum f = readData(rs);
			list.add(f);
		}
		methodA(forum, list);
	}

	public void delete(Forum forum) throws DbException {
			String id = forum.getId();
			delete(id);
	}

	public void delete(String id) throws DbException {
		try {
			sql = "update forum set is_delete = 1 where parent_id = '" + id + "'";
			stmt.executeUpdate(sql);
			sql = "update forum set is_delete = 1 where id = '" + id + "'";
			stmt.executeUpdate(sql);
		}catch(SQLException sqle) {
			throw new DbException("delete() : " + sqle.getMessage());
		}
	}

	private void prepare_attachment(Forum forum) throws SQLException {
		Attachment attachment = new Attachment();
		sql = "select file_name, directory from forum_attachment where forum_id = '" + forum.getId() + "'";
		ResultSet rs = stmt.executeQuery(sql);
		String directory = "";
		boolean got = false;
		while ( rs.next() ) {
			got = true;
			String file_name = rs.getString("file_name");
			directory = rs.getString("directory");
			attachment.add(file_name);
		}
		if ( got ) {
			forum.setAttachment(attachment);
			forum.setAttachmentFolder(directory);

		}
	}
	
	public void update(Forum forum) throws DbException {
		try {
			String forum_id = forum.getId();
			String title = forum.getTitle();
			if (title.length() > 255 ) title = title.substring(0, 255);
			String member_id = forum.getUserId();
			String category_id = forum.getCategoryId();
			
			//check if need activation
			//if ( parentid.equals("0") ) activation(member_id, category_id);
			
			String description = forum.getDescription();
			String message_text = forum.getNotes();
			
			SQLRenderer r = new SQLRenderer();
			r.update("id", forum_id);
			r.add("title", title);
			r.add("message_text", message_text);
			sql = r.getSQLUpdate("forum");

			stmt.executeUpdate(sql);

			
			//check for attachment
			if ( forum.isAttached() ) {

				//String forum_id = getForumId(title, parentid, member_id, category_id);

				Attachment attach = forum.getAttachment();
				String sub_directory = forum.getAttachmentFolder();
				List fileList = attach.get();
				if ( !fileList.isEmpty() ) {
					Iterator files = fileList.iterator();
					while ( files.hasNext() ) {
						String filename = (String) files.next();
						save_attachment(forum_id, filename, sub_directory);
					}
				}
			}

		}catch(SQLException sqle) {
			throw new DbException("DbForum.add(): " + sqle.getMessage() + sql);
		}
	}
	
	public void update(Forum forum, String[] removeAttachFiles) throws DbException {
		try {
			String forum_id = forum.getId();
			String title = forum.getTitle();
			if (title.length() > 255 ) title = title.substring(0, 255);
			String member_id = forum.getUserId();
			String category_id = forum.getCategoryId();
			String classroom_id = forum.getClassroomId();
			
			String description = forum.getDescription();
			String message_text = forum.getNotes();

			
			SQLRenderer r = new SQLRenderer();
			r.update("id", forum_id);
			r.add("title", title);
			if (!"".equals(classroom_id)) r.add("classroom_id", classroom_id);
			r.add("message_text", message_text);
			sql = r.getSQLUpdate("forum");

			stmt.executeUpdate(sql);
			
			//remove attach files
			/*
			if ( removeAttachFiles != null ) {
				for ( int i=0; i < removeAttachFiles.length; i++ ) {
					//System.out.println("removed = " + removeAttachFiles[i]);	
					delete_attachment(forum_id, removeAttachFiles[i]);
				}	
			}
			*/
			delete_all_attachment(forum_id);			

			//check for attachment
			if ( forum.isAttached() ) {
				Attachment attach = forum.getAttachment();
				String sub_directory = forum.getAttachmentFolder();
				List fileList = attach.get();
				if ( !fileList.isEmpty() ) {
					Iterator files = fileList.iterator();
					while ( files.hasNext() ) {
						String filename = (String) files.next();
						save_attachment(forum_id, filename, sub_directory);
					}
				}
			}
			

		} catch(SQLException sqle) {
			throw new DbException("DbForum.add(): " + sqle.getMessage() + sql);
		}
	}
	
	public void updateRate(String forum_id, int rate) throws DbException {
		try {
			SQLRenderer r = new SQLRenderer();
			r.update("id", forum_id);
			r.add("rate", rate);
			sql = r.getSQLUpdate("forum");
			stmt.executeUpdate(sql);
		}catch(SQLException sqle) {
			throw new DbException("DbForum.add(): " + sqle.getMessage() + sql);
		}
	}	

}
