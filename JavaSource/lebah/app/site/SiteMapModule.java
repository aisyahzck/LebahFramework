package lebah.app.site;

import java.util.*;
import java.sql.ResultSet;

import javax.servlet.http.HttpSession;

import lebah.db.Db;

import org.apache.velocity.Template;

public class SiteMapModule extends lebah.portal.velocity.VTemplate {
	
	public Template doTemplate() throws Exception {
		String vm = "vtl/site_map/sites.vm";
		HttpSession session = request.getSession();
		
		String sql = "SELECT tab_id, tab_title FROM tab_user t where user_login = 'anon' order by sequence";
		Db db = null;
		try {
			db = new Db();
			ResultSet rs = db.getStatement().executeQuery(sql);
			Vector<SiteTab> tabs = new Vector<SiteTab>();
			while ( rs.next() ) {
				String tabId = rs.getString("tab_id") != null ? rs.getString("tab_id") : "";
				String title = rs.getString("tab_title") != null ? rs.getString("tab_title") : "no title";
				SiteTab tab = new SiteTab(tabId, title);
				tabs.add(tab);
			}
			
			for ( SiteTab  tab : tabs ) {
				sql = "SELECT u.module_id, m.module_title FROM user_module u, module m " +
				"WHERE u.module_id = m.module_id " +
				"AND u.user_login = 'anon' and u.tab_id = '" + tab.getId() + "' order by u.sequence";
				
				ResultSet rs2 = db.getStatement().executeQuery(sql);
				while ( rs2.next() ) {
					String moduleId = rs2.getString("module_id") != null ? rs2.getString("module_id") : "";
					String title = rs2.getString("module_title") != null ? rs2.getString("module_title") : "no title";
					SiteContent content = new SiteContent(moduleId, title, tab);
					tab.addContent(content);
				}
			}
			
			context.put("site_tabs", tabs);
			
		} catch ( Exception e ) {
			throw e;
		}
		
		
		Template template = engine.getTemplate(vm);	
		return template;		
	}

}
