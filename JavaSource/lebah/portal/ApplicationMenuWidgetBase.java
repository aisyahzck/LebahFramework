/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */

package lebah.portal;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpSession;

import lebah.portal.action.*;
import lebah.portal.action.element.ButtonElement;
import lebah.portal.action.element.HTMLElement;
import lebah.portal.action.element.HrefElement;

import org.apache.velocity.Template;

public abstract class ApplicationMenuWidgetBase extends lebah.portal.velocity.VTemplate {
	
	protected String formName = "";
	protected String moduleId = "";
	protected String className = "";
	
	private boolean addMethod = false;
	private Vector<String> methods = new Vector<String>();
	private Hashtable<String, Vector> hiddenField = new Hashtable<String, Vector>();
	private Vector<String> hiddenFieldList = new Vector<String>();
	private Hashtable<String, String> confirms = new Hashtable<String, String>();	
	
	public abstract String doTemplate2() throws Exception;
	
    public Template doTemplate() throws Exception {
    	HttpSession session = request.getSession();
    	//String requestMethod = request.getMethod();  //either GET or POST
    	
    	moduleId = this.getId();
    	
    	if ( moduleId == null || "".equals(moduleId)) {
    		moduleId = getParam("module_id");
    	}
    	
    	context.put("moduleId", moduleId);
    	context.put("uniqueId", moduleId.replace('.', '_'));
    	
        this.showVM = false;
        
        String vmMain = "";
        
        if ( !isDiv) vmMain = "vtl/ajax/ajax_main.vm";
        else vmMain = "vtl/ajax/div_main.vm";
        
        
        className = this.getClass().getName();
        
        context.put("className", className);
        String pid = moduleId;
        context.put("targetName", "D" + pid.replace('.', '_'));
        formName = "F" + pid.replace('.', '_');
        formName = formName.replace(" ", "");
        context.put("formName", formName);
        context.put("formname", formName);
        context.put("form", "F" + formName);
        
        String securityToken = (String) session.getAttribute("securityToken");
        context.put("securityToken", securityToken);
    	
        //get the inner vm template
        context.put("vmName", doTemplate2());  
        
        
        Template template = engine.getTemplate(vmMain);  
        return template;
        
    }
    
	public void receiveMethod(String methodName) {
		if ( !methods.contains(methodName)) {
			methods.addElement(methodName);
			confirms.put(methodName, "");
			addMethod = true;
		}
		else {
			addMethod = false;
		}
	}
	
	public void receiveParams(String params, String methodName) {
		if ( addMethod ) {
			StringTokenizer st = new StringTokenizer(params, ",");
			Vector<String> v = new Vector<String>();
			while ( st.hasMoreTokens() ) {
				String token = st.nextToken();
				String param = token.substring(0, token.indexOf("=")).trim();
				v.addElement(param);
				if ( !hiddenFieldList.contains(param) ) hiddenFieldList.addElement(param);
			}
			hiddenField.put(methodName, v);
		}
	}
	
	public void receiveConfirm(String confirm, String methodName) {
		if ( addMethod ) {
			confirms.put(methodName, confirm);
		}
	}    

}

