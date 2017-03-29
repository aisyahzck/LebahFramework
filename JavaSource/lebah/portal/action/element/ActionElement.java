package lebah.portal.action.element;

import java.util.*;

import org.apache.commons.lang.StringUtils;

import lebah.portal.AjaxBasedModule;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.00
 */

/*
 * History
 * -------
 * #	Date		Name				Remarks
 * ----	----------	------------------	---------------------------------------------------
 * 1.	2009-08-07	Taufek				Added cssClass var to add css-class support.
 * 2.	2009-08-12	Taufek				Added selected required component checking.
 * 3.	2009-08-20	Taufek				Added validation call for checking dates format.
 */

public class ActionElement {
	
	private String label;
	private String command;
	private String params = "";
	private String confirm = "";
	private String style = "";
	private String divName = "";
	boolean checkRequired = false;
	
	protected String uniqueId = "";
	protected String type = "button";
	
	protected AjaxBasedModule observer;
	private String currentMethodName = "";

	private String cssClass = "";
	
	private String requiredNames = "";
	
	public ActionElement command(String s) {
		if ( observer != null ) {
			observer.receiveMethod(s);
			currentMethodName = s;
		}
		command = s;
		params = "";
		confirm = "";
		style = "";
		divName = "";
		cssClass = "";
		requiredNames = "";
		return this;
	}

	public ActionElement param(String p){
		
		if ( observer != null ) {
			observer.receiveParams(p, currentMethodName);
		}
		
		params = p;
		return this;
	}
	
	public ActionElement p(String p) {
		return param(p);
	}
	
	public ActionElement confirm(String s) {
		if ( observer != null ) {
			observer.receiveConfirm(s, currentMethodName);
		}		
		confirm = s;
		return this;
	}
	
	public ActionElement text(String s) {
		label = s;
		return this;
	}
	
	public ActionElement style(String s) {
		style = s;
		return this;
	}	
	
	public ActionElement divName(String s) {
		divName = s;
		return this;
	}
	
	public ActionElement cssClass(String s) {
		cssClass = s;
		return this;
	}
	
	public ActionElement requiredNames(String s) {
		requiredNames = s;
		return this;
	}

	public String toString() {
		String str = "";
		String formName = "F" + uniqueId.replace('.', '_');
		String confirmPart = "";
		if ( !"".equals(confirm)) {
			confirmPart = "if ( !confirm('" + confirm + "') ) {return;}";
		}
		else {
			confirmPart = "";
		}
		
		String validate = null;
		if(StringUtils.isBlank(requiredNames)){
			validate = "if ( !checkRequiredFields() ) return; ";
			validate += "if(!checkDateFieldsFormat()){ return;} ;";
		}
		else{
			validate = "if ( !checkSelectedRequiredFields('"+requiredNames+"') ) return; ";
		}
		
		String ajaxCommand = confirmPart + "doAjaxCall" + formName + "('" + command + "', '" + params.replaceAll(", ", "&") + "')";
		String ajaxCommand2 = confirmPart + validate + "doAjaxCall" + formName + "('" + command + "', '" + params.replaceAll(", ", "&") + "')";
		
		String ajaxCommand3 = confirmPart + "doDivAjaxCall" + formName + "('" + divName + "', '" + command + "', '" + params.replaceAll(", ", "&") + "')";
		
		if ( "button".equals(type)) {
			str = "<input type=\"button\" value=\"" + label + "\" onclick=\"" + ajaxCommand + "\" style=\"" + style + "\" class=\"" + cssClass + "\">";
		}
		else if ( "button_validate".equals(type)) {
			str = "<input type=\"button\" value=\"" + label + "\" onclick=\"" + ajaxCommand2 + "\" style=\"" + style + "\" class=\"" + cssClass + "\">";
		}		
		else if ( "href".equals(type)) {
			str = "<a href=\"javascript:void()\" onclick=\"" + ajaxCommand + "\" style=\"" + style + "\" class=\"" + cssClass + "\">" + label + "</a>";
		}
		else if ( "alink".equals(type)) {
			str = "<a href=\"javascript:void()\" onclick=\"" + ajaxCommand + "\" style=\"" + style + "\" class=\"" + cssClass + "\">";
		}
		else if ( "button_div".equals(type)) {
			str = "<input type=\"button\" value=\"" + label + "\" onclick=\"" + ajaxCommand3 + "\" style=\"" + style + "\" class=\"" + cssClass + "\">";
		}
		return str;
	}

}
