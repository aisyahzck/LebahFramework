/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib 
 * @version 1.01
 */
public class VelocityModule extends lebah.portal.velocity.VTemplate implements lebah.portal.Attributable {

	private String[] names = {"VM File"};
	private Hashtable values = new Hashtable();
	
	public String[] getNames() {
		return names;
	}
	
	public Hashtable getValues() {
		return values;
	}
	
	public void setValues(java.util.Hashtable hashtable) {
		values = hashtable;
	}
			
	public Template doTemplate() throws Exception {
		HttpSession session = request.getSession();
		String file = (String) values.get(names[0]);		
		
		Template template = engine.getTemplate(file);	
		return template;		
	}
	
}
