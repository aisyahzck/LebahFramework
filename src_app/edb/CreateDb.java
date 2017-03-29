package edb;

import lebah.db.Db;

public class CreateDb {
	
	public static void run() throws Exception {
		
		Db db = null;
		try {
			db = new Db();
			db.getStatement().execute(SQL.createAttrModuleData);
			db.getStatement().execute(SQL.createForum);
			db.getStatement().execute(SQL.createForumAttachment);
			db.getStatement().execute(SQL.createMemberSubject);
			db.getStatement().execute(SQL.createModule);
			db.getStatement().execute(SQL.createModuleHtmlContainer);
			db.getStatement().execute(SQL.createPageCss);
			db.getStatement().execute(SQL.createRole);
			db.getStatement().execute(SQL.createRoleModule);
			db.getStatement().execute(SQL.createTabs);
			db.getStatement().execute(SQL.createTabTemplate);
			db.getStatement().execute(SQL.createUser);
			db.getStatement().execute(SQL.createUserActivityLog);
			db.getStatement().execute(SQL.createTabUser);
			db.getStatement().execute(SQL.createUserCss);
			db.getStatement().execute(SQL.createUserModule);
			db.getStatement().execute(SQL.createUserModuleTemplate);
			db.getStatement().execute(SQL.createUserRole);
			db.getStatement().execute(SQL.createUserTracker);
			db.getStatement().execute(SQL.createWebLogger);
			db.getStatement().execute(SQL.createXmlModule);
			
			db.getStatement().execute(SQL.insertUsers);
			db.getStatement().execute(SQL.insertModule);
			db.getStatement().execute(SQL.insertRoleModule);
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
		
	}

}
