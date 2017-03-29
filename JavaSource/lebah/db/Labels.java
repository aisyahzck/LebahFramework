/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin

This program is free software; you can redistribute it and/or




This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

* ************************************************************************ */

package lebah.db;


import java.util.*;


/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */

public class Labels {
	
	private static Labels instance = null;
	private static Hashtable<String, String> titles = new Hashtable<String, String>();
	private static String propertiesName = "";
	private static String defaultLanguage = "";
	private ResourceBundle rb;
	
	static {
		ResourceBundle r = ResourceBundle.getBundle("dbconnection");
		defaultLanguage = r.getString("language");
	}
	
	private Labels() {
		prepareLabels();
	}
	
	private Labels(String lang) {
		prepareLabels(lang);
	}
	
	public static Labels getInstance(String lang) {
		if ( lang.equals(propertiesName) ) {
			if( instance == null ) instance = new Labels();
		} else {
			propertiesName = lang;
			instance = new Labels(lang);
		}
		return instance;
	}
	
	public static Labels getInstance() {
		if( instance == null ) instance = new Labels();
		return instance;
	}
	
	public static Labels reload() {
		instance = new Labels();
		return instance;
	}
	
	private void prepareLabels() {
		try {
			String language= "en"; //default is en
//			try {
//				ResourceBundle r = ResourceBundle.getBundle("dbconnection");
//				language = r.getString("language");
//				defaultLanguage = language;
//			} catch ( Exception e ) {
//				language = "label";
//			}
			propertiesName = defaultLanguage != null && !"".equals(defaultLanguage) ? defaultLanguage : language;
			rb = ResourceBundle.getBundle(propertiesName);
			for ( Enumeration keys = rb.getKeys(); keys.hasMoreElements(); ) {
				String key = (String) keys.nextElement();
				String value = rb.getString(key);
				titles.put(key, value);
			}
		} catch ( MissingResourceException  e ) {
			System.out.println(propertiesName + " NOT FOUND!");
		} catch ( NullPointerException e ) {
			System.out.println(propertiesName + " NOT FOUND!");
		}
	}
	
	private void prepareLabels(String lang) {
		try {
			
			propertiesName = lang;
			rb = ResourceBundle.getBundle(propertiesName);
			for ( Enumeration keys = rb.getKeys(); keys.hasMoreElements(); ) {
				String key = (String) keys.nextElement();
				String value = rb.getString(key);
				titles.put(key, value);
			}
		} catch ( MissingResourceException  e ) {
			System.out.println(propertiesName + " NOT FOUND!");
		} catch ( NullPointerException e ) {
			System.out.println(propertiesName + " NOT FOUND!");
		}
	}

	public static String getTitle(String name) {
		Labels labels = Labels.getInstance("labels");
		return Labels.titles.get(name) != null ? Labels.titles.get(name) : "";
		
	}
	
	public Hashtable<String, String> getTitles() {
		return titles;
	}
	
	public static String getDefaultLanguage() {
		return defaultLanguage;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("getting labels..");

		Hashtable<String, String> h = lebah.db.Labels.getInstance("labels_mal").getTitles();
		System.out.println(h.get("Password"));
		System.out.println(h.get("Username"));
		
	}
	
}
