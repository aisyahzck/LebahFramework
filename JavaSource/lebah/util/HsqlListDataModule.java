package lebah.util;

import java.sql.ResultSet;

import lebah.db.Db;
import lebah.portal.action.LebahModule;

public class HsqlListDataModule extends LebahModule {

	private String path = "hsql/";

	@Override
	public String start() {
		Db db = null;
		
		try {
			db = new Db();
			ResultSet rs = db.getStatement().executeQuery("select module_id, module_title from module");
			while ( rs.next() ) {
				System.out.println(rs.getString("module_id") + ", " + rs.getString("module_title"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			
		}
		return path + "start.vm";
	}

}
