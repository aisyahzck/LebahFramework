/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or





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

import javax.servlet.http.HttpServletRequest;

import lebah.db.Db;
import lebah.db.SQLRenderer;
import lebah.listeners.LoginLogger;
import lebah.listeners.LoginRequest;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class AuthenticateUser {
	private String role;
	private String userLogin;
	private String userName;
	private boolean allowed;
	HttpServletRequest req;	 
	
	public AuthenticateUser() {
		
	}
	
	public AuthenticateUser(HttpServletRequest req) {
		this.req = req;
	}	
	
	public boolean lookup(String username, String password) throws  Exception {
		Db db = null;
		Connection conn = null;
		String sql = ""; 
		try {
			db = new Db();
			Statement stmt = db.getStatement();
			
			//POSSIBLE SQL INJECTION CHECK
			/*
			username = ' or user_login like '%
			password =' or user_password like '%
			*/
			
			//LOG THIS LOGIN ACTIVITY
			//UserLogger.save(req, username);
			
			if (  ( username.toUpperCase().indexOf(" OR ") > 0 ) || ( password.toUpperCase().indexOf(" OR ") > 0 ) ) {
				System.out.println("[ALERT]: POSSIBLE SQL INJECTION ");
				System.out.println("username = " + username);
				System.out.println("password = " + password);
				UserLogger.save(req, "ALERT!!" + username + ", " + password);
				
				LoginLogger.log(req, username, false);
				
				return false;
			}			
			
			SQLRenderer r = new SQLRenderer();
			{
				
				r.add("user_role");
				r.add("user_name");
				r.add("user_login", username);
				//encrypt the given password before add into argument
				r.add("user_password", lebah.util.PasswordService.encrypt(password));
				sql = r.getSQLSelect("users");
				//System.out.println("1: " + sql);
				ResultSet rs = stmt.executeQuery(sql);
				
				/*
				PreparedStatement ps = null;
				sql = "SELECT user_role, user_name  FROM users WHERE user_login = ?  AND user_password = ?";
				ps = db.getConnection().prepareStatement(sql);
				ps.setString(1, username);
				ps.setString(2, lebah.util.PasswordService.encrypt(password));
				ResultSet rs = ps.executeQuery(sql);
				*/
				if ( rs.next() ) {
					role = rs.getString("user_role");
					userName = rs.getString("user_name");
					userLogin = username;
					allowed = true;
				}
				
	            
						
			}
			
			if ( !allowed ){
				
			    r.clear();
				r.add("user_role");
				r.add("user_name");
				r.add("user_login");
				r.add("user_login_alt", username);
				//encrypt the given password before add into argument
				r.add("user_password", lebah.util.PasswordService.encrypt(password));
				sql = r.getSQLSelect("users");
				//System.out.println("2: " + sql);
				ResultSet rs = stmt.executeQuery(sql);
				
				/*
				PreparedStatement ps = null;
				sql = "SELECT user_role, user_name, user_login  FROM users WHERE user_login_alt = ?  AND user_password = ?";
				ps = db.getConnection().prepareStatement(sql);
				ps.setString(1, username);
				ps.setString(2, lebah.util.PasswordService.encrypt(password));
				ResultSet rs = ps.executeQuery(sql);
				*/
				if ( rs.next() ) {
					role = rs.getString("user_role");
					userName = rs.getString("user_name");
					userLogin = rs.getString("user_login");
					allowed = true;
				}
			}
			
			LoginLogger.log(req, username, allowed);
				
		} catch ( SQLException ex ) {
		    System.out.println(ex.getMessage() + ": " + sql);
		    allowed = false;
		    ex.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
		return allowed;
	}
	
	public String getRole() {
		return role;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getUserLogin() {
		return userLogin;
	}	
	
	public boolean getAllowed() {
		return allowed;
	}
	
	public static void main(String[] args) throws Exception {
		AuthenticateUser a = new AuthenticateUser();
		System.out.println(a.lookup("admin", "admin"));
	}
}
