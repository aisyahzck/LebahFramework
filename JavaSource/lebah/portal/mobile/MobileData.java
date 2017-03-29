/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or




This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
package lebah.portal.mobile;

import java.sql.ResultSet;
import java.util.Hashtable;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.util.UIDGenerator;

public class MobileData {
	
	public static Vector getRoleNames() throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql = r
			.add("name")
			.getSQLSelect("role")
			;
			ResultSet rs = db.getStatement().executeQuery(sql);
			Vector list = new Vector();
			while ( rs.next() ) {
				list.addElement(rs.getString(1));
			}
			return list;	
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static Vector getModules(String role) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql = r
			.add("a.module_id")
			.add("a.module_title as title")
			.add("m.module_title")
			.add("m.module_class")
			.add("a.role_id", role)
			.relate("a.module_id", "m.module_id")
			.getSQLSelect("mobile_module a, module m")
			;
			
			System.out.println(sql);
			
			ResultSet rs = db.getStatement().executeQuery(sql);
			Vector list = new Vector();
			while ( rs.next() ) {
				Hashtable h = new Hashtable();
				String id = rs.getString("module_id");
				String title = rs.getString("title");
				if ( "".equals(title) ) title = rs.getString("module_title");
				if ( "".equals(title)) title = "(" + id + ")";
				h.put("id", id); 
				h.put("title", title);
				h.put("className", rs.getString("module_class"));
				list.addElement(h);
			}
			System.out.println("number of modules = " + list.size());
			return list;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void addModule(String role, String module, String title) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql = "";
			boolean found = false;
			{
				sql = r
				.reset()
				.add("role_id")
				.add("role_id", role)
				.add("module_id", module)
				.getSQLSelect("mobile_module")
				;
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) found = true;
			}
			if ( !found ){
				sql = r
				.reset()
				.add("role_id", role)
				.add("module_id", module)
				.add("module_title", title)
				.getSQLInsert("mobile_module")
				;
				db.getStatement().executeUpdate(sql);
			}
			else {
				sql = r
				.reset()
				.update("role_id", role)
				.update("module_id", module)
				.add("module_title", title)
				.getSQLUpdate("mobile_module")
				;
				db.getStatement().executeUpdate(sql);
			}
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void deleteModule(String role, String module) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql = "";
			sql = r
			.add("role_id", role)
			.add("module_id", module)
			.getSQLDelete("mobile_module")
			;
			db.getStatement().executeUpdate(sql);

		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static String createUserSession(String user) throws Exception {
		Db db = null;
		String sql = "";
		String sessionId = UIDGenerator.getUID();
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			boolean found = false;
			{
				sql = r
				.add("user_id")
				.add("user_id", user)
				.getSQLSelect("mobile_sessions")
				;
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) found = true;
			}
			if ( !found ){
				sql = r
				.reset()
				.add("user_id", user)
				.add("session_id", sessionId)
				.getSQLInsert("mobile_sessions")
				;
				db.getStatement().executeUpdate(sql);
			}
			else {
				sql = r
				.reset()
				.update("user_id", user)
				.add("session_id", sessionId)
				.getSQLUpdate("mobile_sessions")
				;
				db.getStatement().executeUpdate(sql);
			}
			return sessionId;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static UserInfo getUserInfo(String sessionId) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			sql = r
			.add("user_login")
			.add("user_password")
			.add("user_name")
			.add("user_role")
			.add("m.session_id", sessionId)
			.relate("u.user_login", "m.user_id")
			.getSQLSelect("users u, mobile_sessions m")
			;
			ResultSet rs = db.getStatement().executeQuery(sql);
			UserInfo info = new UserInfo();
			if ( rs.next() ) {
				info.setUserId(rs.getString("user_login"));
				info.setPassword(rs.getString("user_password"));
				info.setName(rs.getString("user_name"));
				info.setRole(rs.getString("user_role"));
			}
			return info;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static UserInfo getUserAction(String sessionId, String module) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			UserInfo info = new UserInfo();
			{
				sql = r
				.add("user_login")
				.add("user_password")
				.add("user_name")
				.add("user_role")
				.add("m.session_id", sessionId)
				.relate("u.user_login", "m.user_id")
				.getSQLSelect("users u, mobile_sessions m")
				;
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) {
					info.setUserId(rs.getString("user_login"));
					info.setPassword(rs.getString("user_password"));
					info.setName(rs.getString("user_name"));
					info.setRole(rs.getString("user_role"));
				}
			}
			{
				sql = r
				.reset()
				.update("session_id", sessionId)
				.add("current_module", module)
				.getSQLUpdate("mobile_sessions")
				;
				db.getStatement().executeUpdate(sql);
			}
			return info;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static String getCurrentModule(String sessionId) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql = r
			.add("current_module")
			.add("session_id", sessionId)
			.getSQLSelect("mobile_sessions")
			;
			ResultSet rs = db.getStatement().executeQuery(sql);
			if ( rs.next() ) return rs.getString(1);
			else return "";
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static Vector getModuleList() throws Exception {
		return getModuleList("");
	}
	public static Vector getModuleList(String group) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			r
			.add("module_id")
			.add("module_title")
			.add("module_group")
			.add("module_class")
			;
			if ( !"".equals(group)) {
				r.add("module_group", group);
			}
			
			sql = r.getSQLSelect("module", "module_group, module_id");
			ResultSet rs = db.getStatement().executeQuery(sql);
			Vector list = new Vector();
			while ( rs.next() ) {
				Hashtable h = new Hashtable();
				h.put("id", rs.getString("module_id"));
				h.put("title", rs.getString("module_title"));
				h.put("group", rs.getString("module_group"));
				h.put("className", rs.getString("module_class"));
				list.addElement(h);
			}
			return list;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static Vector getModuleGroupList() throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			r
			.add("module_group")
			;
			sql = r.getSQLSelectDistinct("module", "module_group");
			ResultSet rs = db.getStatement().executeQuery(sql);
			Vector list = new Vector();
			while ( rs.next() ) {
				list.addElement(rs.getString(1));
			}
			return list;
		} finally {
			if ( db != null ) db.close();
		}
	}

}
