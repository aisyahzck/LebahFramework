/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.portal;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;

public class HtmlModuleData {
	
	public static void update(String user_id, String module_id, String html_location, String title) throws Exception {
		Db db = null;
		String sql = "";
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			
			boolean change = false;
			{
				sql = "SELECT html_url FROM module_htmlcontainer WHERE module_id = '" + module_id + "' ";
				ResultSet rs = stmt.executeQuery(sql);
				if ( rs.next() ) {
					if ( !html_location.equals(rs.getString(1)));
					change = true;
				}
			}
			if ( change ) {
				sql = "UPDATE module_htmlcontainer SET html_url = '" + html_location + "' WHERE module_id = '" + module_id + "' ";
				stmt.executeUpdate(sql);
			}
			
			HtmlModuleData.updateTitle(user_id, module_id, title, stmt);
			
		} catch ( SQLException ex ) {
			throw new DbException(ex.getMessage() + ": " + sql);
		} finally {
			if ( db != null ) db.close();
		}	
	}
	
	public static void updateTitle(String user_id, String module_id, String title, Statement stmt) throws Exception {
		SQLRenderer r = new SQLRenderer();
		r.update("user_login", user_id);
		r.update("module_id", module_id);
		r.add("module_custom_title", title);
		String sql = r.getSQLUpdate("user_module");
		stmt.executeUpdate(sql);
	}		

}
