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
package lebah.portal.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;
import lebah.portal.element.PageTheme;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class UserPage {
	public static String getCSS(String usrlogin) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			sql = "SELECT css_name FROM user_css WHERE user_login = '" + usrlogin + "' ";
			//Log.print(sql);
			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) return rs.getString("css_name");
			else return "default.css";
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}	
	
	public static String getTheme(String usrlogin) throws DbException {
		return getCSS(usrlogin);
	}
	
	public static Vector getPageThemeList() throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			sql = "SELECT css_name, css_title FROM page_css";
			ResultSet rs = stmt.executeQuery(sql);
			Vector v = new Vector();
			while ( rs.next() ) {
				String css_name = rs.getString("css_name");
				String css_title = rs.getString("css_title");
				v.addElement(new PageTheme(css_name, css_title));
			}
			return v;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static void saveTheme(String usrlogin, String css) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			boolean found = false;
			{
				sql = "SELECT user_login FROM user_css WHERE user_login = '" + usrlogin + "'";	
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) found = true;
			}
			if ( found )
				sql = "UPDATE user_css SET css_name = '" + css + "' WHERE user_login = '" + usrlogin + "'";	
			else
				sql = "INSERT into user_css (user_login, css_name) VALUES ('" + usrlogin + "', '" + css + "')";
			stmt.executeUpdate(sql);
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}			
	}
	
	//HtmlContainer - to get the url
	public static String getUrlForHtmlContainer(String module) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			sql = "SELECT html_url FROM module_htmlcontainer WHERE module_id = '" + module + "' ";	
			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) return rs.getString("html_url");
			else return "";
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	//RSSContainer - to get the url
	public static String getUrlForRSSContainer(String module) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			sql = "SELECT rss_source FROM rss_module WHERE module_id = '" + module + "' ";	
			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) return rs.getString("rss_source");
			else return "";
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}	
	
	//XMLContainer
	public static Hashtable getUrlAndXslForXMLContainer(String module) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			Hashtable h = new Hashtable();
			sql = "SELECT xml, xsl FROM xml_module WHERE module_id = '" + module + "' ";	
			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) {
				h.put("xml", rs.getString("xml"));
				h.put("xsl", rs.getString("xsl"));
			}
			return h;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}	
	
	//Attributable
	public static Hashtable getValuesForAttributable(String module) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			Hashtable h = new Hashtable();
			sql = "SELECT attribute_name, attribute_value  FROM attr_module_data WHERE module_id = '" + module + "' ";	
			ResultSet rs = stmt.executeQuery(sql);
			while ( rs.next() ) {
				h.put(rs.getString("attribute_name"), rs.getString("attribute_value"));
			}
			return h;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static String getDisplayType(String usrlogin, String tabid) throws Exception {
		String display = getDisplayTypePersonal(usrlogin, tabid, "");
		return display;
	}	
	
	public static String getDisplayType(String usrlogin, String tabid, String myrole) throws Exception {
		if ( myrole == null ) myrole = "";
		String display = getDisplayTypePersonal(usrlogin, tabid, myrole);
		return display;
	}
	
	public static String getDisplayTypePersonal(String usrlogin, String tabid) throws DbException {
		return getDisplayTypePersonal(usrlogin, tabid, "");
	}
	
	public static String getDisplayTypePersonal(String usrlogin, String tabid, String myrole) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			
			//get user role
			String role = "";
			if ( "".equals(myrole)){
				sql = "SELECT user_role FROM users WHERE user_login = '" + usrlogin + "'";
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) role = rs.getString(1);
			}		
			else{
				role = myrole;
			}
			
			if ( ("anon".equals(role)) || "root".equals(role) ) 
			{
				String display_type = "";
				if ( tabid != null && !"".equals(tabid) )
					sql = "SELECT display_type FROM tab_user WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";	
				else 
					sql = "SELECT display_type FROM tab_user WHERE user_login = '" + usrlogin + "' ORDER BY sequence";	
	
				//System.out.println(sql);
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) display_type = rs.getString("display_type");
				if ( display_type == null ) display_type = "";
				return display_type;
			}
			else {
				{
					String display_type = "";
					sql = "SELECT display_type FROM tab_template WHERE user_login = '" + role + "' AND tab_id = '" + tabid + "' ";	
					ResultSet rs = stmt.executeQuery(sql);
					if ( rs.next() ) display_type = rs.getString("display_type");
					if ( display_type == null ) display_type = "";
					return display_type;
				}
				
			}
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}			
	}
	
	public static String getDisplayTypePersonal2(String usrlogin, String tabid) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			
			//is this tab locked?
			boolean isLocked = false;
			{
				sql = "SELECT locked FROM tab_user WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) {
					isLocked = rs.getInt(1) == 1 ? true : false;
				}
			}
			
			if ( !isLocked )
			{
				String display_type = "";
				if ( tabid != null && !"".equals(tabid) )
					sql = "SELECT display_type FROM tab_user WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";	
				else 
					sql = "SELECT display_type FROM tab_user WHERE user_login = '" + usrlogin + "' ORDER BY sequence";	
	
	
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) display_type = rs.getString("display_type");
				if ( display_type == null ) display_type = "";
				return display_type;
			}
			else {
				//get user role
				String role = "";
				{
					sql = "SELECT user_role FROM users WHERE user_login = '" + usrlogin + "'";
					ResultSet rs = stmt.executeQuery(sql);
					if ( rs.next() ) role = rs.getString(1);
				}
				{
					String display_type = "";
					//if ( tabid != null && !"".equals(tabid) )
						sql = "SELECT display_type FROM tab_template WHERE user_login = '" + role + "' AND tab_id = '" + tabid + "' ";	
					//else 
					//	sql = "SELECT display_type FROM tab_template WHERE user_login = '" + usrlogin + "' ORDER BY sequence";	
		
		
					ResultSet rs = stmt.executeQuery(sql);
					if ( rs.next() ) display_type = rs.getString("display_type");
					if ( display_type == null ) display_type = "";
					return display_type;
				}
				
			}
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}			
	}
	
	
	public static String getDisplayTypeTemplate(String usrlogin, String tabid) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			SQLRenderer r = new SQLRenderer();
			String role = "";
			{
				sql =
				r
				.reset()
				.add("user_role")
				.add("user_login", usrlogin)
				.getSQLSelect("users")
				;
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) {
					role = rs.getString(1);
				}
			}
			
			String display_type = "";
			if ( tabid != null && !"".equals(tabid) )
				sql = "SELECT display_type FROM tab_template WHERE user_login = '" + role + "' AND tab_id = '" + tabid + "' ";	
			else 
				sql = "SELECT display_type FROM tab_template WHERE user_login = '" + role + "' ORDER BY sequence";	

			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) display_type = rs.getString("display_type");
			if ( display_type == null ) display_type = "";
			return display_type;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}			
	}	
	
	public static String getTabTitle(String usrlogin, String tabid) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			String tab_title = "";
			if ( tabid != null && !"".equals(tabid) )
				sql = "SELECT tab_title FROM tabs WHERE user_login = '" + usrlogin + "' AND tab_id = '" + tabid + "' ";	
			else 
				sql = "SELECT tab_title FROM tabs WHERE user_login = '" + usrlogin + "' ORDER BY sequence";	

			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) tab_title = rs.getString("tab_title");
			if ( tab_title == null || "".equals("tab_title") ) tab_title = "left_navigation";
			return tab_title;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}			
	}	
	
	public static String getDisplayType(String usrlogin) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			String display_type = "";
			sql = "SELECT display_type FROM tabs WHERE user_login = '" + usrlogin + "' ORDER BY sequence";	
			ResultSet rs = stmt.executeQuery(sql);
			if ( rs.next() ) display_type = rs.getString("display_type");
			if ( display_type == null || "".equals("display_type") ) display_type = "left_navigation";
			return display_type;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}			
	}	
	
	public static String[] getDisplayTypes() {
		
		return new String[] {
				"single_column", 
				"two_columns", 
				"three_columns", 
				"narrow_wide", 
				"wide_narrow",
				//"narrow_wide_narrow",
				//"narrow_left_three_columns",
				"left_navigation", 
				"top_navigation", 
				"pulldown_menu"
				//"windows"
				};

		/*
		return new String[] {
				"left_navigation", 
				"top_navigation", 
				"top_navigation_with_title",
				"single_column", 
				"single_column_no_title",
				"two_columns", 
				"three_columns", 
				"narrow_wide", 
				"wide_narrow",
				"two_columns_with_top",
				"two_columns_with_bottom",
				"three_columns_with_top", 
				"three_columns_with_bottom",
				"pulldown_menu",
				"windows"
				};
				*/ 
	}
	
	public static List<Hashtable<String, String>> listDisplayTypes() {
		
		String[][] types = {
				{"single_column", "Single Column"}, 
				{"two_columns", "Two Columns"}, 
				{"three_columns", "Three Columns"}, 
				{"narrow_wide", "Narrow & Wide Columns"},
				//{"narrow_wide_narrow", "Narrow & Wide & Narrow Columns"},
				{"wide_narrow", "Wide & Narrow Columns"},
				//{"narrow_left_three_columns", "Narrow Left Three Columns"},
				{"left_navigation", "Left Menus"}, 
				{"top_navigation", "Top Menus"}, 
				{"pulldown_menu", "Pulldown Menus"}
				//{"windows", "Windows"}
				};

		
		List<Hashtable<String, String>> list = new ArrayList<Hashtable<String, String>>();
		Hashtable<String, String> h = null;
		
		for ( String[] s : types ) {
			h = new Hashtable<String, String>();
			h.put("id", s[0]);
			h.put("name", s[1]);
			list.add(h);
		}
		
		return list;
		
	}
	
	public static Vector getDisplayTypeVector() {
		String[] s = getDisplayTypes();
		Vector v = new Vector();
		for ( int i=0; i < s.length; i++ ) v.addElement(s[i]);
		return v;	
	}
}
