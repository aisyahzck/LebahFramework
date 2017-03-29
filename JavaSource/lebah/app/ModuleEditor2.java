package lebah.app;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import lebah.db.Db;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;
import lebah.portal.db.PrepareModule;
import lebah.portal.db.RegisterModule;
import lebah.portal.element.Module;
import lebah.portal.element.Role;

public class ModuleEditor2 extends LebahModule {

	private String path = "vtl/modules2";

	@Override
	public String start() {
		try {
			listGroup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return path + "/start.vm";
	}

	private void listGroup() throws Exception {
		String sql = "SELECT distinct(module_group) FROM module ORDER BY module_group";
		Db db = null;
		try {
			db = new Db();
			List<String> groups = new ArrayList<String>();
			context.put("groups", groups);
			ResultSet rs = db.getStatement().executeQuery(sql);
			while ( rs.next() ) {
				groups.add(rs.getString(1));
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
	}

	@Command("listModules")
	public String listModules() throws Exception {
		String module_group = getParam("module_group");
		context.put("module_group", module_group);
		String sql = "SELECT module_id, module_title, module_class, module_group FROM module where module_group = '" + module_group + "' ORDER by module_title";				
		Db db = null;
		try {
			db = new Db();
			ResultSet rs = db.getStatement().executeQuery(sql);
			List<Module> modules = new ArrayList<Module>();
			context.put("modules", modules);
			while ( rs.next() ) {
				String id = rs.getString("module_id");
				String title = rs.getString("module_title");
				String klazz = rs.getString("module_class");
				Module module = new Module(id, title, klazz);
				module.setGroupName(module_group); 
				modules.add(module);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
		return path + "/listModules.vm";
	}

	@Command("editModule")
	public String editModule() throws Exception {
		String module_id = getParam("editModuleId");
		Module module = PrepareModule.getModuleById(module_id);
		context.put("module", module);
		Vector moduleRoles = module.getRoles();
		context.put("moduleRoles", moduleRoles);
		RoleProcessor processor = new RoleProcessor();        
		Vector userRoles = processor.getRoles();
		context.put("userRoles", userRoles);
		int j = 0;
		for (Enumeration e = userRoles.elements(); e.hasMoreElements();)
		{
			Role obj = new Role();
			obj = (Role) e.nextElement();
			// TAKING OUT ROOT USER FROM LIST
			if (obj.getName().equals("root")) userRoles.removeElementAt(j);
			j++;
		}

		Hashtable map = new Hashtable();
		context.put("roleMap", map);
		for(Enumeration e = userRoles.elements(); e.hasMoreElements();)
		{
			Role obj = new Role();
			obj = (Role) e.nextElement();
			if ( moduleRoles.contains(obj.getName()) ) {
				map.put(obj.getName(), "true");
			} else {
				map.put(obj.getName(), "false");
			}
		}	
		listGroup();
		return path + "/editModule.vm";
	}

	@Command("deleteModule")
	public String deleteModule() throws Exception {
		String module_id = getParam("deleteModuleId");
		RegisterModule.delete(module_id);
		return listModules();
	}
	
	
	@Command("saveModule")
	public String saveModule() throws Exception {
		update();
		return listModules();
	}

	private void update() throws Exception {
		String module_id = getParam("saveModuleId");
		String module_title = getParam("module_title");
		String module_class = getParam("module_class");
		String module_group = getParam("group_name");
		String[] cbroles = request.getParameterValues("selectedRoles");   
		String[] checkRoles = null; 

		//root roles must exist in the arrays
		if ( cbroles != null ) {
			boolean found = false;
			for ( int i = 0; i < cbroles.length; i++ ) {
				if ( "root".equals(cbroles[i]) ) {
					found = true;   
					break;
				}
			}

			if ( !found ) {
				checkRoles = new String[cbroles.length + 1];
				int i = 0;
				for ( ; i < cbroles.length; i++ ) checkRoles[i] = cbroles[i];
				checkRoles[i] = "root";
			} else checkRoles = cbroles;
		} else checkRoles = new String[] { "root" };   

		RegisterModule.update(module_id, module_title, module_class, module_group, "", checkRoles);

	}

}
