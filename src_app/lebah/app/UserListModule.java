package lebah.app;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class UserListModule extends LebahModule {
	
	private String path = "users";
	private Db db = null;

	@Override
	public String start() {
		List<UserData> users = getUserDataList();
		context.put("users", users);
		return path + "/start.vm";
	}
	
	@Command("listUsers")
	public String listUsers() throws Exception {
		List<UserData> users = getUserDataList();
		context.put("users", users);
		return path + "/list_users.vm";
	}
	
	@Command("deleteUsers")
	public String deleteUsers() throws Exception {
		
		String[] logins = request.getParameterValues("logins");
		for ( String login : logins ) {
			lebah.portal.db.RegisterUser.delete(login);
		}
		List<UserData> users = getUserDataList();
		context.put("users", users);
		return path + "/list_users.vm";
	}

	private List<UserData> getUserDataList() {
		List<UserData> users = new ArrayList<UserData>();
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			r.add("user_login");
			r.add("user_name");
			r.add("user_role");
			String sql = r.getSQLSelect("users", "user_name");
			ResultSet rs = db.getStatement().executeQuery(sql);
			while ( rs.next() ) {
				UserData u = new UserData(rs.getString(1), rs.getString(2), rs.getString(3));
				users.add(u);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return users;
	}

}
