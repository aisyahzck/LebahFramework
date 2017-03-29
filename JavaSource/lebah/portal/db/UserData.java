package lebah.portal.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import lebah.db.Db;
import lebah.db.DbException;
import lebah.db.SQLRenderer;

public class UserData {
	
	public static User getUser(String userId) throws Exception {
		
		//System.out.println("GET USER...");
		
		Db db = null;
		try {
			User user = new User();
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			boolean found = false;
			String sql = "";
			{
				sql = r
				.add("u.user_login")
				.add("u.user_login_alt")
				.add("u.user_name")
				.add("r.name")
				.add("r.description")
				.add("u.avatar")
				.add("u.user_login", userId)
				.relate("u.user_role", "r.name")
				.getSQLSelect("users u, role r")
				;  
				//System.out.println(sql);
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) {
					found = true;
					setResultSet(user, rs);
				}
			}
			if ( !found ) {
				sql = r
				.reset()
				.add("u.user_login")
				.add("u.user_login_alt")
				.add("u.user_name")
				.add("r.name")
				.add("r.description")
				.add("u.avatar")
				.add("user_login_alt", userId)
				.relate("u.user_role", "r.name")
				.getSQLSelect("users u, role r")				
				;
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) {
					found = true;
					setResultSet(user, rs);
				}
			}
			
			if ( !found ) {
				sql = r
				.reset()
				.add("u.user_login")
				.add("u.user_login_alt")
				.add("u.user_name")
				.add("u.avatar")
				.add("u.user_login", userId)
				.getSQLSelect("users u")
				;
				ResultSet rs = db.getStatement().executeQuery(sql);
				if ( rs.next() ) {
					found = true;
					user.setLogin(rs.getString("user_login"));
					user.setName(rs.getString("user_name"));
					user.setAlternateLogin(rs.getString("user_login_alt"));
					Role role = new Role();
					role.setName("root");
					role.setDescription("Root");
					user.setRole(role);
					user.setAvatar(rs.getString("avatar"));
				}				
			}
			
			
			if ( found ) {  //get secondary roles
				int count = 0;
				{
					sql = r
					.reset()
					.add("COUNT(role_id)AS cnt")
					.add("user_id", user.getLogin())
					.getSQLSelect("user_role")
					;
					ResultSet rs = db.getStatement().executeQuery(sql);
					if ( rs.next()){
						count = rs.getInt(1);
					}
				}
				
				Role[] roles = new Role[count];
				sql = r
				.reset()
				.add("r.name")
				.add("r.description")
				.add("user_id", user.getLogin())
				.relate("role_id", "name")
				.getSQLSelect("user_role u, role r")
				;
				ResultSet rs = db.getStatement().executeQuery(sql);
				int i = 0;
				while ( rs.next() ) {
					Role role = new Role();
					role.setName(rs.getString(1));
					role.setDescription(rs.getString(2));
					roles[i++] = role;
				}
				user.setSecondaryRoles(roles);
			}
			return user;
			
		} finally {
			if ( db != null ) db.close();
		}
		
	}

	private static void setResultSet(User user, ResultSet rs) throws SQLException {
		user.setLogin(rs.getString("user_login"));
		user.setName(rs.getString("user_name"));
		user.setAlternateLogin(rs.getString("user_login_alt"));
		Role role = new Role();
		role.setName(rs.getString("name"));
		role.setDescription(rs.getString("description"));
		user.setRole(role);
		user.setAvatar(rs.getString("avatar"));
	}
	
	public static void updateSecondaryRoles(String userId, String[] roles) throws Exception {
		Db db = null;
		try {
			User user = getUser(userId);
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql = "";
			{
				sql = "DELETE FROM user_role WHERE user_id = '" + user.getLogin() +"'";
				db.getStatement().executeUpdate(sql);
			}
			{
				for ( String role : roles ) {
					sql = r
					.reset()
					.add("role_id", role)
					.add("user_id", user.getLogin())
					.getSQLInsert("user_role")
					;
					db.getStatement().executeUpdate(sql);
				}
			}
	
		} finally { 
			if ( db != null ) db.close();
		}
	}
	
	public static Vector getRoleList() throws Exception {
		Vector<Role> list = new Vector<Role>();
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql =
			r
			.add("name")
			.add("description")
			.getSQLSelect("role")
			;
			ResultSet rs = db.getStatement().executeQuery(sql);
			while ( rs.next() ) {
				Role role = new Role();
				role.setName(rs.getString(1));
				role.setDescription(rs.getString(2));
				list.addElement(role);
			}
			return list;
		} finally {
			if ( db != null ) db.close();
		}
	}
	
	public static List<String> listUsers(String role) throws DbException, SQLException {
		Db db = null;
		try {
			db = new Db();
			SQLRenderer r = new SQLRenderer();
			String sql =
			r
			.add("user_login")
			.add("user_role", role)
			.getSQLSelect("users")
			;
			//SELECT user_login FROM users u where user_role = 'student';
			ResultSet rs = db.getStatement().executeQuery(sql);
			List<String> list = new ArrayList<String>();
			while ( rs.next() ) {
				list.add(rs.getString(1));
			}
			return list;
		} finally {
			if ( db != null ) db.close();
		}
	}

}
