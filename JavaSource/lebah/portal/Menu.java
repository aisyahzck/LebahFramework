/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import java.util.Vector;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Menu {
	
	protected static Menu instance = new Menu();
	protected Vector childs = new Vector();
	protected String title;
	protected String url;
	
	private Menu() {
		prepare();
	}
	
	private Menu(String title, String url) {
		this.title = title;
		this.url = url;
	}
	
	private Menu(String title) {
		this.title = title;
		this.url = "";
	}
	
	void setChilds(Vector v) {
		childs = v;
	}
	
	void add(Menu m) {
		childs.addElement(m);
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getUrl() {
		return url;
	}
	
	void prepare() {
		
		Menu menu = new Menu("Academic Structure");
		menu.add(new Menu("Faculties", "infusion.sis.struct.SetupFacultyModule"));
		menu.add(new Menu("Study Periods", "infusion.sis.struct.StudyPeriodModule"));
		add(menu);
		
	}
	
	
}
