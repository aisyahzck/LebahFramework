/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import java.util.Vector;

import lebah.portal.db.CustomClass;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class ClassValidator {
	
	private String objectType = "";
	private String errMessage = "";
	private String[] attributes;
	private Object content;
	private Vector types = new Vector();
	
	//check whether the class exist
	//NOTE: the class must be the type of VTemplate
	//type is the special type
	public boolean validateClass(String module) {
		try {
			//load the class
			Class klazz = Class.forName(CustomClass.getName(module));
			//instantiate
			content = klazz.newInstance();			
			
			//content is HtmlContainer and RSSContainer
			if ( content instanceof HtmlContainer ) {
				objectType = "html_container";  //will be deprecated
				types.addElement("html_container");
			} 
			if ( content instanceof XMLContainer ) {
				objectType = "xml_container";	//will be deprecated
				types.addElement("xml_container");
			} 
			if ( content instanceof Attributable ) {
				objectType = "attributable"; // will be deprecated
				types.addElement("attributable");
				attributes = ((Attributable) content).getNames();
			} 
			//db.Log.print("validate class: type = " + objectType);
			return true;
		} catch ( ClassNotFoundException cnfex ) {
			errMessage = "ClassNotFoundException: " + cnfex.getMessage();
		} catch ( InstantiationException iex ) {
			errMessage = "InstantiationException: " + iex.getMessage();
		} catch ( IllegalAccessException illex ) {
			errMessage = "IllegalAccessException: " + illex.getMessage();
		} catch ( Exception ex ) {
			errMessage = "Exception: " + ex.getMessage();
		}	
		return false;		
	}		 
	
	//should be deprecated
	public String getObjectType() {
		return objectType;
	}

	
	public String getErrorMessage() {
		return errMessage;
	}
	
	public String[] getAttributes() {
		return attributes;
	}
	
	public Object getContent() {
		return content;
	}
	
	public Vector getTypes() {
		return types;
	}
}
