/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.app;

import java.util.Hashtable;

import javax.servlet.http.HttpSession;

import org.apache.velocity.Template;

/**
 * 
 * @author Shamsul Bahrin Abd Mutalb
 *
 * @version 1.0
 */
public class EmptyModule extends lebah.portal.velocity.VTemplate implements lebah.portal.Attributable {

	private String[] names = {"Height"};
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
		String height = (String) values.get("Height");		
		context.put("height", height);
		
		Template template = engine.getTemplate("vtl/main/empty.vm");	
		return template;		
	}
	
}

