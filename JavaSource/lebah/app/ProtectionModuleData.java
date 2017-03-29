/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.app;

import lebah.db.*;

import java.sql.*;

public class ProtectionModuleData {
	
	public static void updateModule(String module, String[] roles) throws Exception {
		if ( roles == null ) return;
		
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql = "";
			{
				sql = "DELETE FROM module_access WHERE module_class = '" + module + "'";
				db.getStatement().executeUpdate(sql);
			}
			for ( int i = 0; i < roles.length; i++ ) {
				sql =
				r
				.reset()
				.add("module_class", module)
				.add("role", roles[i])
				.getSQLInsert("module_access");
				db.getStatement().executeUpdate(sql);
			}
		} finally {
			if ( db != null ) db.close();
		}
		
	}
	
	public static void removeModule(String module) throws Exception {

		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql = "";
			{
				sql = "DELETE FROM module_access WHERE module_class = '" + module + "'";
				db.getStatement().executeUpdate(sql);
			}

		} finally {
			if ( db != null ) db.close();
		}
		
	}	
	
	public static boolean hasAccess(String module, String role) throws Exception {
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			boolean found = false;
			String sql = "";
			{
				sql =
				r
				.add("module", module)
				.getSQLSelect("module_access");
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) {
					found = true;
				}
			}
			if (found){
				sql =
				r
				.reset()
				.add("module_class", module)
				.add("role", role)
				.getSQLSelect("module_access");
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) return true;
				return false;
			}
			else{
				return true;
			}
		} finally {
			if ( db != null ) db.close();
		}
	}

}
