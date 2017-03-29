package lebah.util.data;

import javax.servlet.http.HttpSession;

import lebah.db.*;
import java.sql.*;
import java.io.*;

import org.apache.velocity.Template;

public class SQLModule extends lebah.portal.velocity.VTemplate {
	
	static String sqlfile = "d:/eclipse2/workspace/lebah/sql/create_tables_hsql.sql";
    
    public Template doTemplate() throws Exception {
        HttpSession session = request.getSession();
        
        String submit = getParam("command");
        context.put("error", "");
        if ("runSQL".equals(submit)) {
	        try {
	        	runSQL();
	        } catch ( Exception e ) {
	        	context.put("error", e.getMessage());
	        }
        }
        else if ( "runSQLFromFile".equals(submit)) {
	        try {
	        	runSQLFromFile();
	        } catch ( Exception e ) {
	        	context.put("error", e.getMessage());
	        }
        }
        else {
	        try {
	        	getDbInfo();
	        } catch ( Exception e ) {
	        	context.put("error", e.getMessage());
	        }
        }
        
        Template template = engine.getTemplate("vtl/main/sql.vm");  
        return template;        
    }
    
    void getDbInfo() throws Exception {
    	Db db = null;
    	try {
    		db = new Db();
    		context.put("dbURL", db.getConnectionURL());
    	} finally {
    		if ( db != null ) db.close();
    	}
    }
    
    void runSQL() throws Exception {
    	Db db = null;
    	try {
    		db = new Db();
    		context.put("dbURL", db.getConnectionURL());
    		String sql = getParam("sql");
    		if ( "".equals(sql) ) return;
    		
    		db.getStatement().execute(sql);
    		
    	} finally {
    		if ( db != null ) db.close();
    	}
    }
    
    void runSQLFromFile() throws Exception {
    	String sqlfilename = getParam("sqlfilename");
    	context.put("sqlfilename", sqlfilename);
    	BufferedReader in = new BufferedReader(new FileReader(sqlfilename));
    	String str = "";
    	Db db = null;
    	try {
    		db = new Db();

	    	while ((str = in.readLine()) != null ) {
	    		System.out.println(str);
	    		db.getStatement().execute(str);
	    	}
	    	
    	} finally {
    		if ( db != null ) db.close();
    	}
    }

}
