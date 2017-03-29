package lebah.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import lebah.db.Database;
import lebah.entity.LebahPersistence;
import lebah.entity.Module;
import lebah.entity.ModuleHtmlContainer;
import lebah.entity.PageCss;
import lebah.entity.Role;
import lebah.entity.RoleModule;
import lebah.entity.TabTemplate;
import lebah.entity.TabUser;
import lebah.entity.UserCss;
import lebah.entity.UserModule;
import lebah.entity.UserModuleTemplate;
import lebah.entity.UserRole;
import lebah.entity.Users;
import lebah.portal.action.LebahModule;

public class CopyHsqlModule extends LebahModule {
	
	private String path = "hsql/";

	@Override
	public String start() {
		try {
			copyToHsql();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path + "start.vm";
	}
	
	public void copyToHsql() throws Exception {
		
		Database d = new Database("com.mysql.jdbc.Driver", "jdbc:mysql://localhost:3306/educateportal", "root", "");
		LebahPersistence p = new LebahPersistence();
		
//		copyRole(d, p);
//		copyRoleModule(d, p);
//		copyTabTemplate(d, p);
//		copyTabUser(d, p);
//		copyUserCss(d, p);
//		copyUserModule(d, p);
		copyUserModuleTemplate(d, p);
//		copyModule(d, p);
//		copyModuleHtmlContainer(d, p);
//		copyPageCss(d, p);
//		copyUserRole(d, p);
//		copyUsers(d, p);
		
		
	}	
	
	private void copyUsers(Database d, LebahPersistence p) throws Exception {

		p.begin();
		p.executeUpdate("delete from Users x");
		p.commit();

		String s = "select user_login, date_registered, user_name, user_login_alt, user_address, " +
				"user_role, avatar, user_password, user_ip_address from users order by user_login";
		ResultSet rs = d.getStatement().executeQuery(s);
		String temp = "";
		String id = "";
		while ( rs.next() ) {
			id = rs.getString("user_login");
			if ( !id.equals(temp)) {
				temp = id;
				p.begin();
				Users x = new Users();
				x.setLogin(id);
				x.setDateRegistered(rs.getDate("date_registered"));
				x.setUsername(rs.getString("user_name"));
				x.setLoginAlternate(rs.getString("user_login_alt"));
				x.setAddress(rs.getString("user_address"));
				x.setRole(rs.getString("user_role"));
				x.setAvatar(rs.getString("avatar"));
				x.setPassword(rs.getString("user_password"));
				x.setIpAddress(rs.getString("user_ip_address"));
				p.persist(x);
				p.commit();
				
			}

		}
	}

	
	
	
	private  void copyUserRole(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from UserRole x");
		p.commit();

		String s = "select user_id, role_id from user_role";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			UserRole x = new UserRole();
			x.setRoleId(rs.getString("role_id"));
			x.setUserId(rs.getString("user_id"));
			p.persist(x);
			p.commit();
		}
	}


	private void copyPageCss(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from PageCss p");
		p.commit();
		ResultSet rs = d.getStatement().executeQuery("select css_name, css_title from page_css");
		while ( rs.next() ) {
			String cssName = rs.getString("css_name");
			String cssTitle = rs.getString("css_title");
			//System.out.println(cssName + ", " + cssTitle);
			p.begin();
			PageCss x = new PageCss();
			x.setName(cssName);
			x.setTitle(cssTitle);
			p.persist(x);
			p.commit();
		}
	}
	
	private void copyModule(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from Module x");
		p.commit();
		String s = "select module_id, module_title, module_description, module_group, module_class " +
				"from module";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			Module x = new Module();
			x.setId(rs.getString("module_id"));
			x.setTitle(rs.getString("module_title"));
			x.setDescription(rs.getString("module_description"));
			x.setGroup(rs.getString("module_group"));
			x.setMclass(rs.getString("module_class"));
			p.persist(x);
			p.commit();
		}
	}
	
	private void copyModuleHtmlContainer(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from ModuleHtmlContainer x");
		p.commit();
		String s = "select module_id, html_url from module_htmlcontainer";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			ModuleHtmlContainer x = new ModuleHtmlContainer();
			x.setId(rs.getString("module_id"));
			x.setUrl(rs.getString("html_url"));
			p.persist(x);
			p.commit();
		}

	}
	
	private  void copyRole(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from Role x");
		p.commit();
		String s = "select name, description from role";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			Role x = new Role();
			x.setName(rs.getString("name"));
			x.setDescription(rs.getString("description"));
			p.persist(x);
			p.commit();
		}
	}
	
	private  void copyRoleModule(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from RoleModule x");
		p.commit();
		String s = "select module_id, user_role from role_module";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			RoleModule x = new RoleModule();
			x.setModuleId(rs.getString("module_id"));
			x.setUserRole(rs.getString("user_role"));
			p.persist(x);
			p.commit();
		}
	}
	
	private  void copyTabTemplate(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from TabTemplate x");
		p.commit();

		String s = "select tab_id, tab_title, sequence, display_type, locked, user_login from tab_template";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			TabTemplate x = new TabTemplate();
			x.setTabId(rs.getString("tab_id"));
			x.setTabTitle(rs.getString("tab_title"));
			x.setSequence(rs.getInt("sequence"));
			x.setDisplayType(rs.getString("display_type"));
			x.setLocked(rs.getInt("locked"));
			x.setUserLogin(rs.getString("user_login"));

			p.persist(x);
			p.commit();
		}
	}
	
	private  void copyTabUser(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from TabUser x");
		p.commit();
		String s = "select tab_id, tab_title, hide, sequence, display_type, locked, user_login from tab_user";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			TabUser x = new TabUser();
			x.setTabId(rs.getString("tab_id"));
			x.setTabTitle(rs.getString("tab_title"));
			x.setSequence(rs.getInt("sequence"));
			x.setDisplayType(rs.getString("display_type"));
			x.setLocked(rs.getInt("locked"));
			x.setUserLogin(rs.getString("user_login"));

			p.persist(x);
			p.commit();
		}
	}
	
	private  void copyUserCss(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from UserCss x");
		p.commit();
		String s = "select user_login, css_name from user_css";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			UserCss x = new UserCss();
			x.setUserLogin(rs.getString("user_login"));
			x.setCssName(rs.getString("css_name"));
			
			p.persist(x);
			p.commit();
		}
	}
	
	private  void copyUserModule(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from UserModule x");
		p.commit();
		String s = "select module_id, tab_id, user_login, sequence, module_custom_title, column_number from user_module";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			UserModule x = new UserModule();
			x.setModuleId(rs.getString("module_id"));
			x.setTabId(rs.getString("tab_id"));
			x.setUserLogin(rs.getString("user_login"));
			x.setSequence(rs.getInt("sequence"));
			x.setModuleCustomTitle(rs.getString("module_custom_title"));
			x.setColumnNumber(rs.getInt("column_number"));
			
			p.persist(x);
			p.commit();
		}
	}
	
	private  void copyUserModuleTemplate(Database d, LebahPersistence p) throws Exception {
		p.begin();
		p.executeUpdate("delete from UserModuleTemplate x");
		p.commit();
		String s = "select module_id, tab_id, user_login, sequence, module_custom_title, column_number from user_module_template";
		ResultSet rs = d.getStatement().executeQuery(s);
		while ( rs.next() ) {
			p.begin();
			UserModuleTemplate x = new UserModuleTemplate();
			x.setModuleId(rs.getString("module_id"));
			x.setTabId(rs.getString("tab_id"));
			x.setUserLogin(rs.getString("user_login"));
			x.setSequence(rs.getInt("sequence"));
			x.setModuleCustomTitle(rs.getString("module_custom_title"));
			x.setColumnNumber(rs.getInt("column_number"));
			
			p.persist(x);
			p.commit();
		}
	}



}
