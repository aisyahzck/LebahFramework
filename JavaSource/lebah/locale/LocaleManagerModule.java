package lebah.locale;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import lebah.db.Db;
import lebah.portal.action.AjaxModule;

public class LocaleManagerModule extends AjaxModule {
	
	private String path = "vtl/locale/";
	private String vm = "";
	private List<String> categories = null;


	@Override
	public String doAction() throws Exception {
		vm = path + "locale.vm";
		String command = request.getParameter("command");
		System.out.println("command=" + command);
		if ( command == null || "".equals(command)) start();
		else if ( "add_category".equals(command)) addCategory();
		else if ( "delete_category".equals(command)) deleteCategory();
		else if ( "show_locales".equals(command)) showLocales();
		else if ( "add_locale".equals(command)) addLocale();
		else if ( "delete_locale".equals(command)) deleteLocale();
		return vm;
	}

	private void deleteLocale() {
		vm = path + "locales.vm";
		String category = request.getParameter("category_id");
		context.put("category", category);
		String localeId = request.getParameter("locale_id");
		Db db = null;
		try {
			db = new Db();
			db.getStatement().executeUpdate("delete from locale where id = '" + localeId + "'");
			
			List<Locale> locales = new ArrayList<Locale>();
			ResultSet rs = db.getStatement().executeQuery("select id, category, name, value from locale where category = '" + category + "'");
			while ( rs.next() ) {
				locales.add(new Locale(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			context.put("locales", locales);
		
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
		
	}

	private void addLocale() {
		vm = path + "locales.vm";

		System.out.println(request.getRequestURL().toString() + ", " + request.getCharacterEncoding() + ", " + request.getPathInfo());
		
		String name = request.getParameter("name");
		String value = request.getParameter("value");
		System.out.println("value = " + value.getBytes().toString());
		
		String category = request.getParameter("category_id");
		context.put("category", category);

		
		
		String id = lebah.db.UniqueID.getUID();
		
		Db db = null;
		try {
			db = new Db();
			db.getStatement().execute("insert into locale values ('" + id + "', '" + category + "', '" + name + "', '" + value + "')");
			
			List<Locale> locales = new ArrayList<Locale>();
			ResultSet rs = db.getStatement().executeQuery("select id, category, name, value from locale where category = '" + category + "'");
			while ( rs.next() ) {
				locales.add(new Locale(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4)));
			}
			context.put("locales", locales);
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
		
		
	}

	private void showLocales() {
		vm = path + "locales.vm";
		List<Locale> locales = new ArrayList<Locale>();
		String category = request.getParameter("category_id");
		System.out.println("category = " + category);
		context.put("category", category);
		Db db = null;
		try {
			db = new Db();
			ResultSet rs = db.getStatement().executeQuery("select id, category, name, value from locale where category = '" + category + "'");
			while ( rs.next() ) {
				Locale locale = new Locale(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				locales.add(locale);
			}
			context.put("locales", locales);
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			if ( db != null ) db.close();
		}
		
	}

	private void deleteCategory() {
		vm = path + "categories.vm";
		String categoryId = request.getParameter("category_id");
		categories = (List) request.getSession().getAttribute("categories");
		categories.remove(categoryId);
		request.getSession().setAttribute("categories", categories);
		context.put("categories", categories);
		
	}

	private void addCategory() {
		vm = path + "categories.vm";
		String category = request.getParameter("category");
		categories = (List) request.getSession().getAttribute("categories");
		categories.add(category);
		context.put("categories", categories);
		request.getSession().setAttribute("categories", categories);
		
	}

	private void start() {
		vm = path + "start.vm";
		categories = new ArrayList<String>();
		request.getSession().setAttribute("categories", categories);
		context.put("categories", categories);
		Db db = null;
		try {
			db = new Db();
			ResultSet rs = db.getStatement().executeQuery("select category from locale group by category");
			while ( rs.next() ) categories.add(rs.getString(1));
			
		} catch ( Exception e ) {
			
		} finally {
			if ( db != null ) db.close();
		}
		
		
	}

}
