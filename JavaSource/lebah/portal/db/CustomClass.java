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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class CustomClass {
	public static String getName(String module) throws DbException {
		Db db = null;
		Connection conn = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			
			SQLRenderer r = new SQLRenderer();
			r.add("module_class");
			r.add("module_id", module);
			
			sql = r.getSQLSelect("module");
			ResultSet rs = stmt.executeQuery(sql);
			
			if ( rs.next() ) {
				String name = rs.getString("module_class");
				//System.out.println("getting content = " + name);
				return name;
			}
			return null;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	
	public static String getName(String module, String role) throws DbException {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			
			SQLRenderer r = new SQLRenderer();
			r.add("m.module_class");
			r.add("m.module_id", module);
			r.add("r.user_role", role);
			r.relate("m.module_id", "r.module_id");
			sql = r.getSQLSelect("module m, role_module r");
			ResultSet rs = stmt.executeQuery(sql);
			
			if ( rs.next() ) {
				String name = rs.getString("module_class");
				return name;
			}
			return null;
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}
	}	
	
    public static String getCustomTitle (String moduleId) throws DbException
    {
        Db db = null;
        Connection conn = null;
        String sql = "";
        try {
            db = new Db();
            Statement stmt = db.getStatement();
            
            SQLRenderer r = new SQLRenderer();
            r.add("module_title");
            r.add("module_id", moduleId);
            
            sql = r.getSQLSelect("module");
            ResultSet rs = stmt.executeQuery(sql);
            
            if ( rs.next() ) {
                String name = rs.getString("module_title");
                //System.out.println("getting content = " + name);
                return name;
            }
            else return null;
        } catch ( SQLException ex ) {
            throw new DbException(ex.getMessage() + ": " + sql);
        } finally {
            if ( db != null ) db.close();
        }
    }
    

	
}
