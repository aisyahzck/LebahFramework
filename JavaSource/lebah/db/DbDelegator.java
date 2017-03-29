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

package lebah.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 *
 *

 */
public class DbDelegator {
    
	public Vector select(String entityName, Vector data, Hashtable whereData) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			for ( int i = 0; i < data.size(); i++ ) r.add( (String) data.elementAt(i) );
			if ( whereData != null ) {
				String key = "";
				for ( Enumeration e = whereData.keys(); e.hasMoreElements(); ) {
					key = (String) e.nextElement();
					r.add( key, whereData.get(key) );
				}
			}
			PreparedStatement pstmt = r.getPStmtSelect(db.getConnection(), entityName);
			Vector results = new Vector();
			String fieldName = "";
			ResultSet rs = pstmt.executeQuery();
			while ( rs.next() ) {
				Hashtable hashTable = new Hashtable(); 
				for ( int i = 0; i < data.size(); i++ ) {
					fieldName = (String) data.elementAt(i);
					hashTable.put(fieldName, rs.getString(fieldName));
				}
				results.addElement(hashTable);
			}
			return results;
		} finally {
			if ( db != null ) db.close();
		}	
	}
	
	public Vector selectDistinct(String entityName, Vector data, Hashtable whereData, String orderBy) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			for ( int i = 0; i < data.size(); i++ ) r.add( (String) data.elementAt(i) );
			if ( whereData != null ) {
				String key = "";
				for ( Enumeration e = whereData.keys(); e.hasMoreElements(); ) {
					key = (String) e.nextElement();
					r.add( key, whereData.get(key) );
				}
			}
			PreparedStatement pstmt = r.getPStmtSelectDistinct(db.getConnection(), entityName, orderBy);
			Vector results = new Vector();
			String fieldName = "";
			ResultSet rs = pstmt.executeQuery();
			while ( rs.next() ) {
				Hashtable hashTable = new Hashtable(); 
				for ( int i = 0; i < data.size(); i++ ) {
					fieldName = (String) data.elementAt(i);
					hashTable.put(fieldName, rs.getString(fieldName));
				}
				results.addElement(hashTable);
			}
			return results;
		} finally {
			if ( db != null ) db.close();
		}	
	}		
	
	
	
	public void insert(String entityName, Hashtable data) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			String key = "";
			for ( Enumeration e = data.keys(); e.hasMoreElements(); ) {
			    key = (String) e.nextElement();
			    r.add(key, data.get(key));
			}
			PreparedStatement pstmt = r.getPStmtInsert(db.getConnection(), entityName);
			pstmt.executeUpdate();
		} finally {
			if ( db != null ) db.close();
		}	
		
	}
	
	

	public void update(String entityName, Hashtable updateData, Hashtable whereData) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			String key = "";
			for ( Enumeration e = updateData.keys(); e.hasMoreElements(); ) {
			    key = (String) e.nextElement();
			    r.add(key, updateData.get(key));
			}
			for ( Enumeration e = whereData.keys(); e.hasMoreElements(); ) {
			    key = (String) e.nextElement();
			    r.update(key, whereData.get(key));
			}
			PreparedStatement pstmt = r.getPStmtUpdate(db.getConnection(), entityName);
			pstmt.executeUpdate();			
			
		} finally {
			if ( db != null ) db.close();
		}	
		
	}	
	

	public void delete(String entityName, Hashtable data) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLPStmtRenderer r = new SQLPStmtRenderer();
			String key = "";
			for ( Enumeration e = data.keys(); e.hasMoreElements(); ) {
			    key = (String) e.nextElement();
			    r.add(key, data.get(key));
			}
			PreparedStatement pstmt = r.getPStmtDelete(db.getConnection(), entityName);
			pstmt.executeUpdate();
		} finally {
			if ( db != null ) db.close();
		}	
		
	}			

	
}	
