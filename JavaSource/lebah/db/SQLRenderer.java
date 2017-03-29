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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Date;
import java.util.Vector;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class SQLRenderer {
	
	public static final int STMT_INSERT = 0;
	public static final int STMT_UPDATE = 1;
	public static final int STMT_SELECT = 2;
	
	protected Vector v = new Vector();
	protected Vector vwhere = new Vector(); //for UPDATE statement
	 
	protected class Item {
		
		String column;
		Object data;		
		String op;
		String operator = "=";
		
		private Item(String s, String d) {
			column = s;
			data = d;
		}
		private Item(String s, Unquote d) {
			column = s;
			data = d;
		}		
		private Item(String s, int d) {
			column = s;
			data = new Integer(d);
		}
		//
		private Item(String s, long d) {
			column = s;
			data = new Long(d);
		}
		private Item(String s, float d) {
			column = s;
			data = new Float(d);
		}
        private Item(String s, double d) {
            column = s;
            data = new Double(d);
        }        
		private Item(String s, boolean d) {
			column = s;
			data = new Boolean(d);
		}
		private Item(String s) {
			column = s;
			data = null;
		}
		private Item(String s, String d, String op) {
			column = s;
			data = d;
			operator = op;
		}
		private Item(String s, int d, String op) {
			column = s;
			data = new Integer(d);
			operator = op;
		}	
		private Item(String s, Date d, String op) {
			column = s;
			data = d;
			operator = op;
		}		
		
		public boolean equals(Object o) {
			if ( o instanceof SQLRenderer.Item )
				if ( ((SQLRenderer.Item) o).column.equals(column)) return true;
			return false;
		}
		
	}
	
	public SQLRenderer add(String s, Unquote value) {
		v.add(new Item(s, value));
		return this;
	}	
	
	public SQLRenderer add(String s, String value) {
		v.add(new Item(s, value));
		return this;
	}
	
	public SQLRenderer add(String s, int value) {
		v.add(new Item(s, value));
		return this;
	}
	
	public SQLRenderer add(String s, long value) {
		v.add(new Item(s, value));
		return this;
	}
	
	public SQLRenderer add(String s, float value) {
		v.add(new Item(s, value));
		return this;
	}
    
    public SQLRenderer add(String s, double value) {
        v.add(new Item(s, value));
        return this;
    }    
	
	public SQLRenderer add(String s, boolean value) {
		v.add(new Item(s, value));
		return this;
	}
	
	public SQLRenderer add(String s, String value, String op) {
		v.add(new Item(s, value, op));	
		return this;
	}
	
	public SQLRenderer add(String s, int value, String op) {
		v.add(new Item(s, value, op));	
		return this;
	}
	
	public SQLRenderer add(String s, Date value, String op) {
		v.add(new Item(s, value, op));	
		return this;
	}	
    
    public SQLRenderer relate(String s1, String s2) {
        return add(s1, unquote(s2));
    }
    
    //---reset for prepared statement
    
	private void doItem(Item item, Vector v) {
		int i = v.indexOf(item);
		v.removeElementAt(i);
		v.insertElementAt(item, i);
	}    
    
	public SQLRenderer set(String s, String value) {
		Item item = new Item(s, value);
		doItem(item, v);
		return this;
	}
	
	public SQLRenderer set(String s, int value) {
		Item item = new Item(s, value);
		doItem(item, v);
		return this;
	}
	
	public SQLRenderer set(String s, long value) {
		Item item = new Item(s, value);
		doItem(item, v);
		return this;
	}

	public SQLRenderer set(String s, float value) {
		Item item = new Item(s, value);
		doItem(item, v);
		return this;
	}
	
	public SQLRenderer set(String s, boolean value) {
		Item item = new Item(s, value);
		doItem(item, v);
		return this;
	}
	
	public SQLRenderer set(String s, String value, String op) {
		Item item = new Item(s, value, op);
		doItem(item, v);		
		return this;
	}
	
	public SQLRenderer set(String s, int value, String op) {
		Item item = new Item(s, value, op);
		doItem(item, v);		
		return this;
	}
	
	public SQLRenderer setUpdate(String s, String value) {
		Item item = new Item(s, value);
		doItem(item, vwhere);
		return this;
	}
	
	public SQLRenderer setUpdate(String s, int value) {
		Item item = new Item(s, value);
		doItem(item, vwhere);
		return this;
	}
	
	public SQLRenderer setUpdate(String s, long value) {
		Item item = new Item(s, value);
		doItem(item, vwhere);
		return this;
	}

	public SQLRenderer setUpdate(String s, float value) {
		Item item = new Item(s, value);
		doItem(item, vwhere);
		return this;
	}
    
    public SQLRenderer setUpdate(String s, double value) {
        Item item = new Item(s, value);
        doItem(item, vwhere);
        return this;
    }    
	
	public SQLRenderer setUpdate(String s, boolean value) {
		Item item = new Item(s, value);
		doItem(item, vwhere);
		return this;
	}
	
	public SQLRenderer setUpdate(String s, String value, String op) {
		Item item = new Item(s, value, op);
		doItem(item, vwhere);		
		return this;
	}
	
	public SQLRenderer setUpdate(String s, int value, String op) {
		Item item = new Item(s, value, op);
		doItem(item, vwhere);		
		return this;
	}
    
    
    //--
	
	
	public void beginGroup() {
	    v.add(new Item("("));
	}
	
	public void endGroup() {
	    v.add(new Item(")"));
	}
	
	//for UPDATE statement
	public SQLRenderer update(String s, String value) {
		vwhere.add(new Item(s, value));
		return this;
	}
	
	public SQLRenderer update(String s, Unquote value) {
		vwhere.add(new Item(s, value));
		return this;
	}	
	
	public SQLRenderer update(String s, int value) {
		vwhere.add(new Item(s, value));
		return this;
	}
	
	public SQLRenderer update(String s, long value) {
		vwhere.add(new Item(s, value));
		return this;
	}
	
	public SQLRenderer update(String s, float value) {
		vwhere.add(new Item(s, value));
		return this;
	}
    
    public SQLRenderer update(String s, double value) {
        vwhere.add(new Item(s, value));
        return this;
    }    
	
	public SQLRenderer update(String s, boolean value) {
		vwhere.add(new Item(s, value));
		return this;
	}	
    
	public SQLRenderer add(String s) {
		v.add(new Item(s));
		return this;
	}
	
	//*** using generic Object, added 1 March, 2005, Shamsul Bahrin
	public void add(String s, Object o) {
		if ( o instanceof String ) add( s, (String) o );	
		if ( o instanceof Integer ) add( s, ((Integer) o).intValue() );
		if ( o instanceof Long ) add( s, ((Long) o).longValue() );
		if ( o instanceof Float ) add( s, ((Float) o).floatValue() );
        if ( o instanceof Double ) add( s, ((Double) o).doubleValue() );
		if ( o instanceof Boolean ) add( s, ((Boolean) o).booleanValue() );
		
	}
	
	public void update(String s, Object o) {
		if ( o instanceof String ) update( s, (String) o );	
		if ( o instanceof Integer ) update( s, ((Integer) o).intValue() );
		if ( o instanceof Long ) update( s, ((Long) o).longValue() );
		if ( o instanceof Float ) update( s, ((Float) o).floatValue() );
        if ( o instanceof Double ) update( s, ((Double) o).doubleValue() );
		if ( o instanceof Boolean ) update( s, ((Boolean) o).booleanValue() );
		
	}	
	
	//**

    public void clear() {
        v = new Vector();
        vwhere = new Vector();
    }
    
	public SQLRenderer reset() {
		v = new Vector();
		vwhere = new Vector();
        return this;
	}

	
	public String getSQL(String table, int type) {
		if ( type == STMT_INSERT ) return getSQLInsert(table);
		if ( type == STMT_SELECT ) return getSQLSelect(table);
		else return "";
	}	
	
	public String getSQLInsert(String table) {
		String sql = "INSERT INTO " + table + " (";
		for ( int i=0; i < v.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
			sql += item.column + ((i < v.size() - 1) ? ", ": ") ");
		}
		sql += " VALUES (";
		for ( int i=0; i < v.size(); i++ ) {
			SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
			if ( item.data instanceof Unquote ) sql += " " + (Unquote) item.data + " ";
			if ( item.data instanceof String ) sql += "'" + replace((String) item.data) + "'";
			if ( item.data instanceof Integer ) sql += ((Integer) item.data).intValue();
			if ( item.data instanceof Long ) sql += ((Long) item.data).longValue();
			if ( item.data instanceof Boolean ) sql += ((Boolean) item.data).booleanValue();
			if ( item.data instanceof Float ) sql += ((Float) item.data).floatValue();
            if ( item.data instanceof Double ) sql += ((Double) item.data).doubleValue();
			sql += (i < v.size() - 1) ? ", ": ") ";
		}
		//reset();
		return sql;
	}
	
	public String getSQLSelect(String table) {
		return getSQLSelect(table, "");
	}
	
	public String getSQLSelectDistinct(String table, String orderby) {
		return getSQLSelect(table, orderby, "distinct");
	}
	
	public String getSQLSelect(String table, String orderby) {
		return getSQLSelect(table, orderby, "");
	}
	
	public String getSQLSelect(String table, String orderby, String option) {
		boolean inGroup = false;
		Vector v1 = new Vector(), v2 = new Vector();
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
		
		String sql = "SELECT ";
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
					if ( item.data instanceof String ) sql += item.column + " " + item.operator + " '" + replace((String) item.data) + "' ";
					else if ( item.data instanceof Unquote ) sql += item.column + " " + item.operator + " " + (Unquote) item.data + " ";
					else if ( item.data instanceof Integer ) sql += item.column + " " + item.operator + " " + ((Integer) item.data).intValue() + " ";
					else if ( item.data instanceof Long ) sql += item.column + " " + item.operator + " " + ((Long) item.data).longValue() + " ";
					else if ( item.data instanceof Boolean ) sql += item.column + " " + item.operator + " " + ((Boolean) item.data).booleanValue() + " ";
					else if ( item.data instanceof Float ) sql += item.column  + " " + item.operator + " " + ((Float) item.data).floatValue() + " ";
                    else if ( item.data instanceof Double ) sql += item.column  + " " + item.operator + " " + ((Double) item.data).doubleValue() + " ";
                    else if ( item.data instanceof Date ) sql += item.column  + " " + item.operator + " '" + ((Date) item.data) + "' ";
					afterBracket = false;
				}
				
				
				
				
			}				
		}
		
		if ( !"".equals(orderby) )
			sql += " ORDER BY " + orderby;
		//reset();
		return sql;	
	}
		
	
	public String getSQLUpdate(String table) {
		String sql = " UPDATE " + table;
		
		if ( v.size() > 0 ) {
			sql += " SET ";
			for ( int i=0; i < v.size(); i++ ) {
				SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
				if ( item.data instanceof String ) sql += item.column + " = '" + replace((String) item.data) + "' ";
				else if ( item.data instanceof Unquote ) sql += item.column + " = " + (Unquote) item.data + " ";
				else if ( item.data instanceof Integer ) sql += item.column + " = " + ((Integer) item.data).intValue() + " ";
				else if ( item.data instanceof Long ) sql += item.column + " = " + ((Long) item.data).longValue() + " ";
				else if ( item.data instanceof Boolean ) sql += item.column + " = " + ((Boolean) item.data).booleanValue() + " ";
				else if ( item.data instanceof Float ) sql += item.column + " = " + ((Float) item.data).floatValue() + " ";
                else if ( item.data instanceof Double ) sql += item.column + " = " + ((Double) item.data).doubleValue() + " ";
				sql += (i < v.size() - 1) ? ", ": " ";
			}				
		}
		
		//vwhere IS A MUST!
		if ( vwhere.size() > 0 ) {
			sql += " WHERE ";
			for ( int i=0; i < vwhere.size(); i++ ) {
				SQLRenderer.Item item = (SQLRenderer.Item) vwhere.elementAt(i);
				if ( item.data instanceof String ) sql += item.column + " = '" + replace((String) item.data) + "' ";
				else if ( item.data instanceof Unquote ) sql += item.column + " = " + (Unquote) item.data + " ";
				else if ( item.data instanceof Integer ) sql += item.column + " = " + ((Integer) item.data).intValue() + " ";
				else if ( item.data instanceof Long ) sql += item.column + " = " + ((Long) item.data).longValue() + " ";
				else if ( item.data instanceof Boolean ) sql += item.column + " = " + ((Boolean) item.data).booleanValue() + " ";
				else if ( item.data instanceof Float ) sql += item.column + " = " + ((Float) item.data).floatValue() + " ";
                else if ( item.data instanceof Double ) sql += item.column + " = " + ((Double) item.data).doubleValue() + " ";
				sql += (i < vwhere.size() - 1) ? " AND ": " ";
			}				
		} else {
			sql = "STATEMENT FOR UPDATE WAS DENIED";
		}		
		//reset();
		return sql;	
	}	
	
	public String getSQLDelete(String table) {
		String sql = "DELETE FROM " + table;
		if ( v.size() > 0 ) {
			sql += " WHERE ";
			for ( int i=0; i < v.size(); i++ ) {
				SQLRenderer.Item item = (SQLRenderer.Item) v.elementAt(i);
				if ( item.data instanceof String ) sql += item.column + " = '" + replace((String) item.data) + "' ";
				else if ( item.data instanceof Unquote ) sql += item.column + " = " + (Unquote) item.data + " ";
				else if ( item.data instanceof Integer ) sql += item.column + " = " + ((Integer) item.data).intValue() + " ";
				else if ( item.data instanceof Long ) sql += item.column + " = " + ((Long) item.data).longValue() + " ";
				else if ( item.data instanceof Boolean ) sql += item.column + " = " + ((Boolean) item.data).booleanValue() + " ";
				else if ( item.data instanceof Float ) sql += item.column + " = " + ((Float) item.data).floatValue() + " ";
                else if ( item.data instanceof Double ) sql += item.column + " = " + ((Double) item.data).doubleValue() + " ";
				sql += (i < v.size() - 1) ? " AND ": " ";
			}	
			//reset();
			return sql;			
		} else {
			return "";	
		} 
	}	
	
	public class Unquote { 
		public String string = "";
		Unquote(String s) {
			string = s;
		}
		public String toString() {
			return string;
		}
	}
	
	public Unquote unquote(String s) {
		return new Unquote(s);
	}
	
    protected String dblch(String s, char c)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        if(s != null)
        {
            for(int i = 0; i < s.length(); i++)
            {
                char c1 = s.charAt(i);
                if(c1 == c)
                    stringbuffer.append(c).append(c);
                else
                    stringbuffer.append(String.valueOf(c1));
            }

        } else
        {
            stringbuffer.append("");
        }
        return stringbuffer.toString();
    }

    protected String replace(String s)
    {
        return dblch(s, '\'');
    }	
	
   



}
