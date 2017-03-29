/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin








MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */
/*
 * Created on Apr 20, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package lebah.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SQLPStmtRenderer extends SQLRenderer {
	
	String sql = "";
	String type = "";
	Vector v2 = null;
	PreparedStatement pstmt;
    
	public PreparedStatement getPStmtSelect(Connection conn, String table) throws Exception {
		return getPStmtSelect(conn, table, "");
	}
	
	public PreparedStatement getPStmtSelectDistinct(Connection conn, String table, String orderby)  throws Exception{
		return getPStmtSelect(conn, table, orderby, "distinct");
	}
	
	public PreparedStatement getPStmtSelect(Connection conn, String table, String orderby)  throws Exception {
		return getPStmtSelect(conn, table, orderby, "");
	}    

	public PreparedStatement getPStmtSelect(Connection conn, String table, String orderby, String option) throws Exception {
		type = "SELECT";
		//boolean inGroup = false;
		Vector v1 = new Vector(); //, 
		v2 = new Vector();
		for ( int i=0; i < v.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
			if ( item.column.equals("(") || item.column.equals(")")) {
			    v2.add(item);
			} else if ( item.data == null ){
			    v1.add(item);
			} else {
			    v2.add(item);
			}
		}
		
		sql = "SELECT ";
		if ( "distinct".equals(option) ) sql += " DISTINCT ";
		for ( int i=0; i < v1.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v1.elementAt(i);
			sql += item.column + ((i < v1.size() - 1) ? ", ": " ");
		}
		sql += " FROM " + table;
		boolean afterBracket = false;
		
		if ( v2.size() > 0 ) {
			sql += " WHERE ";
			for ( int i=0; i < v2.size(); i++ ) {
				SQLRenderer.Item item = (SQLRenderer.Item) v2.elementAt(i);
				if ( i > 0) {
					if ( item.column.equals(")")) {
					    sql += ") AND ";
					    afterBracket = true;
					} else if ( !afterBracket ) {
					    sql += " AND ";
					}
				}
				if ( item.column.equals("(")) {
				    sql += " (";
				    afterBracket = true;
				} else if (!item.column.equals(")")) {
					if ( item.data instanceof String ) sql += item.column + " " + item.operator + " ? ";
					else if ( item.data instanceof Integer ) sql += item.column + " " + item.operator + " ? ";
					else if ( item.data instanceof Boolean ) sql += item.column + " " + item.operator + " ? ";
					else if ( item.data instanceof Float ) sql += item.column  + " " + item.operator + " ? ";
					else if ( item.data instanceof Unquote ) sql += item.column + " " + item.operator + " " + (Unquote) item.data + " ";
					afterBracket = false;
				}
			}
		}
			
			
		if ( !"".equals(orderby) ) {
			sql += " ORDER BY " + orderby;
		}
		
		pstmt = conn.prepareStatement(sql);

		return getPStmtSelect();
	
	}

	public PreparedStatement getPStmtSelect() throws SQLException {
		v2 = new Vector();
		for ( int i=0; i < v.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
			if ( item.column.equals("(") || item.column.equals(")")) {
			    v2.add(item);
			} else if ( item.data != null ){
			    v2.add(item);
			}
		}
		if ( v2.size() > 0 ) {
			for ( int i=0; i < v2.size(); i++ ) {
				SQLRenderer.Item item = (SQLRenderer.Item) v2.elementAt(i);
				if (!item.column.equals(")") ||  !item.column.equals("(")) {
					if ( item.data instanceof String ) 
					    pstmt.setString(i+1, (String) item.data);
					else if ( item.data instanceof Integer ) 
					    pstmt.setInt(i+1, ((Integer) item.data).intValue());
					else if ( item.data instanceof Boolean )
					    pstmt.setBoolean(i+1, ((Boolean) item.data).booleanValue());
					else if ( item.data instanceof Float ) 
					    pstmt.setFloat(i+1, ((Float) item.data).floatValue());
				}
			}
		}
		
		return pstmt;
	}
	
	public PreparedStatement getPStmtUpdate(Connection conn, String table) throws Exception {
		type = "UPDATE";
		sql = " UPDATE " + table;
		if ( v.size() > 0 ) {
			sql += " SET ";
			for ( int i=0; i < v.size(); i++ ) {
				SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
				if ( item.data instanceof Unquote ) sql += item.column + " = " + (Unquote) item.data + " ";
				else sql += item.column + " = ? ";
				sql += (i < v.size() - 1) ? ", ": " ";
			}				
		}
		//vwhere IS A MUST!
		if ( vwhere.size() > 0 ) {
			sql += " WHERE ";
			for ( int i=0; i < vwhere.size(); i++ ) {
				SQLRenderer.Item item = (SQLRenderer.Item) vwhere.elementAt(i);
				if ( item.data instanceof Unquote ) sql += item.column + " = " + (Unquote) item.data + " ";
				else sql += item.column + " = ? ";
				sql += (i < vwhere.size() - 1) ? " AND ": " ";
			}				
		} else {
			//sql = "STATEMENT FOR UPDATE WAS DENIED";
			throw new Exception("UPDATE must have WHERE!");
		}
		
		pstmt = conn.prepareStatement(sql);
		
		return getPStmtUpdate();
	}

	public PreparedStatement getPStmtUpdate() throws SQLException {
		int cnt = 0;
		for ( int i=0; i < v.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
			if (!item.column.equals(")") ||  !item.column.equals("(")) {
				if ( item.data instanceof String ) {
				    pstmt.setString(++cnt, (String) item.data);
				}
				else if ( item.data instanceof Integer ) {
					pstmt.setInt(++cnt, ((Integer) item.data).intValue());
				}
				else if ( item.data instanceof Boolean ) {
				    pstmt.setBoolean(++cnt, ((Boolean) item.data).booleanValue());
				}
				else if ( item.data instanceof Float ) { 
				    pstmt.setFloat(++cnt, ((Float) item.data).floatValue());
				}
			}
		}
		for ( int i=0; i < vwhere.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) vwhere.elementAt(i);
			if (!item.column.equals(")") ||  !item.column.equals("(")) {
				if ( item.data instanceof String ) 
				    pstmt.setString(++cnt, (String) item.data);
				else if ( item.data instanceof Integer ) 
				    pstmt.setInt(++cnt, ((Integer) item.data).intValue());
				else if ( item.data instanceof Boolean )
				    pstmt.setBoolean(++cnt, ((Boolean) item.data).booleanValue());
				else if ( item.data instanceof Float ) 
				    pstmt.setFloat(++cnt, ((Float) item.data).floatValue());
			}
		}			
		return pstmt;
	}
	
	public PreparedStatement getPStmtInsert(Connection conn, String table) throws Exception {
		type = "INSERT";
		sql = "INSERT INTO " + table + " (";
		for ( int i=0; i < v.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
			sql += item.column + ((i < v.size() - 1) ? ", ": ") ");
		}
		sql += " VALUES (";
		for ( int i=0; i < v.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
			if ( item.data instanceof Unquote ) sql += " " + (Unquote) item.data + " ";
			else sql += " ? ";
			sql += (i < v.size() - 1) ? ", ": ") ";
		}
		pstmt = conn.prepareStatement(sql);
		return getPStmtInsert();
	}

	public PreparedStatement getPStmtInsert() throws SQLException {
		int cnt = 0;
		for ( int i=0; i < v.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
			if ( item.data instanceof String ) 
			    pstmt.setString(++cnt, (String) item.data);
			else if ( item.data instanceof Integer ) 
			    pstmt.setInt(++cnt, ((Integer) item.data).intValue());
			else if ( item.data instanceof Boolean )
			    pstmt.setBoolean(++cnt, ((Boolean) item.data).booleanValue());
			else if ( item.data instanceof Float ) 
			    pstmt.setFloat(++cnt, ((Float) item.data).floatValue());
		}
		return pstmt;
	}
	
	public PreparedStatement getPStmtDelete(Connection conn, String table) throws Exception {
		type = "INSERT";
		sql = "DELETE FROM " + table + " WHERE ";
		if ( v.size() > 0 ) {
			for ( int i=0; i < v.size(); i++ ) {
				SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
				if ( item.data instanceof Unquote ) sql += item.column + " = " + (Unquote) item.data + " ";
				else sql += item.column + " = ? ";
				sql += (i < v.size() - 1) ? " AND ": " ";
			}	
			pstmt = conn.prepareStatement(sql);
			return getPStmtInsert();			
		} else {
			throw new Exception("DELETE must have WHERE!");	
		} 
	}
	
	public PreparedStatement getPStmt() throws SQLException {
		if ("SELECT".equals(type)) {
			return getPStmtSelect();
		}
		else if ( "UPDATE".equals(type)) {
			return getPStmtUpdate();
		}
		else if ( "INSERT".equals(type)) {
			return getPStmtInsert();
		}
		else if ( "DELETE".equals(type)) {
			return getPStmtInsert();
		}
		return getPStmtInsert();
	}
	
	public String getSQL() {
		return sql;
	}
	
}
