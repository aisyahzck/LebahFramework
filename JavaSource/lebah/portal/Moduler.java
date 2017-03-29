/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.db.PrepareModule;
import lebah.portal.element.Module;
import lebah.portal.element.Module2;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class Moduler extends lebah.portal.velocity.VTemplate {

	private Vector v;
	private int counter;
	
	public Moduler(VelocityEngine engine, VelocityContext context, HttpServletRequest req, HttpServletResponse res) {
		super(engine, context, req, res);
		try {
			prepareEnvironment();
		} catch ( Exception ex ) {
			//--MODIFY
			System.out.println(ex.getMessage());
		}
	}
	
	protected void prepareEnvironment() throws Exception {
		HttpSession session = request.getSession();
		String usrlogin = (String) session.getAttribute("_portal_login");
		String action = (String) session.getAttribute("_portal_action");
		String myrole = (String) session.getAttribute("myrole");
		String sessionId = session.getId();
		
		v = PrepareModule.getPortalModules(usrlogin, action, myrole, sessionId);
		context.put("modules", v);			
	}
	
	public Template doTemplate() throws Exception {
		Template template = engine.getTemplate("vtl/main/moduler.vm");	
		return template;		
	}
	
	public Vector getModuleList() {
		return v;
	}
	
	public Module2 getFirstModule() {
		if ( v != null && (v.size() > 0) ) return (Module2) v.elementAt(0);
		else return null;
	}
	
	public Module2 getModuleById(String module_id) {
		Module2 m = null;
		//had to iterate thru the vector
		for ( int i=0; i < v.size(); i++ ) {
			if ( module_id.equals( ((Module2) v.elementAt(i)).getId() ) ) {
				m = (Module2) v.elementAt(i);
				break;	
			}	
		}
		return m;	
	}
	
	public Module2 getModuleAt(int i) {
		return (Module2) v.elementAt(i);	
	}
	
	public int getModuleCount() {
		return v.size();
	}
	
	public Module2 getNext() {
		if ( counter < v.size() )
			return (Module2) v.elementAt(counter++);
		else
			return null;
	}
	
	public boolean hasMoreModules() {
		if ( counter < v.size() )
			return true;
		else
			return false;		
	}
	
	public Vector getModulesInColumn(int num) {
		Vector vm = new Vector();
		for (int i=0; i<v.size(); i++ ) {
			Module2 m = (Module2) v.elementAt(i);
			if ( m.getColumn() == num )	vm.addElement(m);
		}
		return vm;
	}
	
	public void removeModule(Module module) {
		if ( v.contains(module)) {
			v.remove(module);
		}
	}
	
	
}
