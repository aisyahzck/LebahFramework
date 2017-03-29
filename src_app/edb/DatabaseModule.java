package edb;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class DatabaseModule extends LebahModule {
	
	private String path = "manageDatabase/db";
	private Db db;
	
	public void preProcess() {
		try {
			db = new Db();
		} catch ( Exception e ) {
			e.printStackTrace();
		}
	}
	
	public void postProcess() {
		if ( db != null ) db.close();
	}

	@Override
	public String start() {
		try {
			DatabaseMetaData md = db.getConnection().getMetaData();
			
			String   catalog          = null;
			String   schemaPattern    = null;
			String   tableNamePattern = null;
			String[] types            = null;

			ResultSet result = md.getTables(catalog, schemaPattern, tableNamePattern, types );

			List<Map<String, String>> tables = new ArrayList<Map<String, String>>();
			context.put("tables", tables);
			while(result.next()) {
			    String tableName = result.getString("TABLE_NAME");
			    String tableType = result.getString("TABLE_TYPE");
			    if ( "TABLE".equals(tableType)) {
				    Map<String, String> table = new HashMap<String, String>();
				    table.put("name", tableName);
				    table.put("type", tableType);
				    tables.add(table);
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return path + "/start.vm";
	}
	
	@Command("view")
	public String view() throws Exception {
		String tableName = getParam("tableName");
		DatabaseMetaData md = db.getConnection().getMetaData();
		
		String   catalog           = null;
		String   schemaPattern     = null;
		String   tableNamePattern  = tableName;
		String   columnNamePattern = null;
		
		context.put("tableName", tableName);

		ResultSet result = md.getColumns(catalog, schemaPattern,  tableNamePattern, columnNamePattern);
		List<Map<String, Object>> columns = new ArrayList<Map<String, Object>>();
		context.put("columns", columns);
		while(result.next()){
		    String columnName = result.getString(4);
		    int    columnType = result.getInt(5);
		    Map<String, Object> m = new HashMap<String, Object>();
		    m.put("name", columnName);
		    m.put("type", columnType);
		    columns.add(m);
		}
		
		
		ResultSet rs = db.getStatement().executeQuery("select * from " + tableName);
		List<List<String>> records = new ArrayList<List<String>>();
		context.put("records", records);
		while ( rs.next() ) {
			List<String> record = new ArrayList<String>();
			for ( Map<String, Object> column : columns ) {
				String columnName = (String) column.get("name");
				String s = rs.getString(columnName);
				record.add(s);
			}
			records.add(record);
		}
		
		return path + "/view.vm";
	}
	

}
