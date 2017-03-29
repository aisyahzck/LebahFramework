package lebah.portal.action.element;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import metadata.EntityLister;
import lebah.portal.*;

/*
 * History
 * -------
 * #	Date		Name				Remarks
 * ----	----------	------------------	---------------------------------------------------
 * 1.	2009-08-07	Taufek				Added cssClass var to add css-class support.
 * 2.	2009-08-11	Taufek				Added null-check for the List obj. in doDropDownWithOnChange();
 * 3.	2009-08-13	Taufek				Added THE_STYLE var to all <select> tag
 * 4.	2009-08-20	Taufek				Change value() method. If passed 'null' arg, just set to empty string.
 * 5. 	2009-08-20	Taufek				Added getDateFieldRequired().
 * 6.	2009-09-01	Taufek				Added null-check for the List obj. in doDropDown();
 * 7.	2009-09-02	Taufek				Added getDropDownWithOnChange().
 */

public class HTMLElement {

	private static Log logger = LogFactory.getLog(HTMLElement.class);

	private String uniqueId = "";
	private String relativeDir = "";
	private String retString = "";
	private String value = "";
	private String style = "";
	private String name = "";
	private String filesCount = "1";
	private String uploadDir = "";
	private Vector<String> requiredFields;
	private Vector<String> messages;
	private String elementType = "";
	private String message = "";
	private String cssClass = "";
	private Vector<String> dateFields;

	public HTMLElement(String id) {
		uniqueId = id;
		requiredFields = new Vector<String>();
		dateFields = new Vector<String>();
		messages = new Vector<String>();
	}

	public HTMLElement(String id, AjaxBasedModule obs, String dir) {
		uniqueId = id;
		relativeDir = dir;
		requiredFields = new Vector<String>();
		dateFields = new Vector<String>();
		messages = new Vector<String>();
	}

	public HTMLElement getUploadFilesField() {
		elementType = "upload_field";
		filesCount = "1";
		retString = "<iframe frameborder=\"1\" marginwidth=\"0\" marginheight=\"0\" noscroll scrolling=\"no\" id=\"iframe_upload\" "
				+ "name=\"iframe_upload\" src=\""
				+ relativeDir
				+ "y/lebah.upload.UploadModule?files=THE_COUNT&uploadDir=THE_UPLOAD_DIR\" "
				+ "style=\"width:100%;border:0px\""
				//+ "></iframe>";
		
				+ "onload=\"this.height=this.contentWindow.document.body.scrollHeight;\"></iframe>";
		return this;
	}

	public HTMLElement getDateField() {
		elementType = "date_field";
		name = "";
		value = "";
		style = "";
		message = "";
		String formName = "F" + uniqueId.replace('.', '_');
		retString = "<input name=\"THE_NAME\" id=\"THE_NAME\" type=\"text\" THE_VALUE THE_STYLE THE_CSS_CLASS>"
				+ "<a style=\"text-decoration:none\" href=\"javascript:void()\" onclick=\"calndr.select(document."
				+ formName
				+ ".THE_NAME,'CallCalendar_THE_NAME','dd-MM-yyyy'); return false;\" "
				+ "name=\"CallCalendar_THE_NAME\" id=\"CallCalendar_THE_NAME\">"
				+ "&nbsp;<img src='../img/calendar_icon.gif' border='0'></a>";
		return this;
	}
	
	public HTMLElement getDateFieldDisabled() {
		elementType = "date_field";
		name = "";
		value = "";
		style = "";
		message = "";
		String formName = "F" + uniqueId.replace('.', '_');
		retString = "<input readonly name=\"THE_NAME\" id=\"THE_NAME\" type=\"text\" THE_VALUE THE_STYLE THE_CSS_CLASS>"
				+ "<a style=\"text-decoration:none\" href=\"javascript:void()\" onclick=\"calndr.select(document."
				+ formName
				+ ".THE_NAME,'CallCalendar_THE_NAME','dd-MM-yyyy'); return false;\" "
				+ "name=\"CallCalendar_THE_NAME\" id=\"CallCalendar_THE_NAME\">"
				+ "&nbsp;<img src='../img/calendar_icon.gif' border='0'></a>";
		return this;
	}
	
	
	public HTMLElement getDateFieldRequired() {
		HTMLElement temp = getDateField();
		temp.elementType = "required_date_field";
		return this;
	}

	public HTMLElement getInputRequired() {
		elementType = "required_field";
		name = "";
		value = "";
		style = "";
		message = "";
		retString = "<input name=\"THE_NAME\" id=\"THE_NAME\" type=\"text\" THE_VALUE THE_STYLE THE_CSS_CLASS onblur=\"onRequired(this)\">";
		return this;
	}

	public HTMLElement getInput() {
		name = "";
		value = "";
		style = "";
		message = "";
		retString = "<input name=\"THE_NAME\" id=\"THE_NAME\" type=\"text\" THE_VALUE THE_STYLE THE_CSS_CLASS>";
		return this;
	}

	public HTMLElement getNumberInputRequired() {
		elementType = "required_field";
		name = "";
		value = "";
		style = "text-align:right";
		message = "";
		retString = "<input name=\"THE_NAME\" id=\"THE_NAME\" type=\"text\" THE_VALUE THE_STYLE THE_CSS_CLASS onkeypress=\"return numeralsOnly(event)\">";
		return this;
	}

	public HTMLElement getNumberInput() {
		elementType = "";
		name = "";
		value = "";
		style = "text-align:right";
		message = "";
		retString = "<input name=\"THE_NAME\" id=\"THE_NAME\" type=\"text\" THE_VALUE THE_STYLE THE_CSS_CLASS onkeypress=\"return numeralsOnly(event)\">";
		return this;
	}

	public HTMLElement name(String s) {
		name = s;
		return this;
	}

	public HTMLElement value(String s) {
		
		if(s==null){
			value = "";
		}
		else{
			value = s;
		}
		return this;
	}

	public HTMLElement style(String s) {
		style = s;
		return this;
	}

	public HTMLElement cssClass(String s) {
		cssClass = s;
		return this;
	}

	public HTMLElement filesCount(int i) {
		filesCount = Integer.toString(i);
		return this;
	}

	public HTMLElement count(int i) {
		return filesCount(i);
	}

	public HTMLElement setUploadDir(String dir) {
		uploadDir = dir;
		return this;
	}

	public HTMLElement message(String msg) {
		message = msg;
		return this;
	}

	public HTMLElement getDropDown(Collection<EntityLister> list, String val) {
		elementType = "select_field";
		return doDropDown(list, val);
	}
	
	public HTMLElement getDropDownWithOnChange(Collection<EntityLister> list, String val, String command) {
		elementType = "select_field";
		return doDropDownWithOnChange(list, val, command);
	}

	public HTMLElement getDropDownRequired(Collection<EntityLister> list,
			String val) {
		elementType = "required_select_field";
		return doDropDown(list, val);
	}

	public HTMLElement getDropDownRequiredWithOnChange(
			Collection<EntityLister> list, String val, String command) {
		elementType = "required_select_field";
		return doDropDownWithOnChange(list, val, command);
	}

	private HTMLElement doDropDown(Collection<EntityLister> list, String val) {
		name = "";
		value = "";
		style = "";
		message = "";
		cssClass = "";
		StringBuffer buff = new StringBuffer();
		String selected = "";
		buff
				.append("<select name=\"THE_NAME\"  id=\"THE_NAME\" THE_CSS_CLASS THE_STYLE>");
		buff.append("<option value=''>Please Select</option>");
		

		if(list != null){
			Iterator<EntityLister> it = list.iterator();
			while (it.hasNext()) {
				EntityLister id = it.next();
				if (val != null && val.equals(id.getId()))
					selected = "selected";
				else
					selected = "";
				buff.append("\n\t<option value=\"").append(id.getId())
						.append("\" ").append(selected).append(">").append(
								id.getValue()).append(" </option>");
			}
		}
		buff.append("</select>");
		retString = buff.toString();
		return this;
	}

	private HTMLElement doDropDownWithOnChange(Collection<EntityLister> list,
			String val, String command) {
		name = "";
		value = "";
		style = "";
		message = "";
		cssClass = "";
		String formName = "F" + uniqueId.replace('.', '_');
		StringBuffer buff = new StringBuffer();
		String selected = "";
		buff
				.append("<select name=\"THE_NAME\"  id=\"THE_NAME\" THE_CSS_CLASS THE_STYLE onchange=\"doAjaxCall"
						+ formName + "('" + command + "', '')\">");
		buff.append("<option value=''>Please Select</option>");
		
		
		if(list != null){
			Iterator<EntityLister> it = list.iterator();
			while (it.hasNext()) {
				EntityLister id = it.next();
				if (val != null && val.equals(id.getId()))
					selected = "selected";
				else
					selected = "";
				buff.append("\n\t<option value=\"").append(id.getId())
				.append("\" ").append(selected).append(">").append(
						id.getValue()).append(" </option>");
			}
		}
		buff.append("</select>");
		retString = buff.toString();
		return this;
	}

	public String getDefaultStyle() {
		return "width:300px; BORDER-BOTTOM:1px solid SILVER;BORDER-LEFT:none; BORDER-RIGHT:none; BORDER-TOP:none;";
	}

	public Collection getRequiredFields() {
		return requiredFields;
	}

	public Collection getMessages() {
		return messages;
	}


	public Collection<String> getDateFields() {
		return dateFields;
	}

	public HTMLElement getSelectField(Collection<Hashtable> list, String val) {
		elementType = "select_field";
		return doSelectField(list, val);
	}

	public HTMLElement getSelectFieldRequired(Collection<Hashtable> list,
			String val) {
		elementType = "required_select_field";
		return doSelectField(list, val);
	}

	private HTMLElement doSelectField(Collection<Hashtable> list, String val) {
		name = "";
		value = "";
		style = "";
		message = "";
		cssClass = "";
		StringBuffer buff = new StringBuffer();
		String selected = "";
		buff
				.append("<select name=\"THE_NAME\" id=\"THE_NAME\" THE_CSS_CLASS THE_STYLE >");
		buff.append("<option value=''>Please Select</option>");
		Iterator<Hashtable> it = list.iterator();
		while (it.hasNext()) {
			Hashtable h = it.next();
			String text = (String) h.get("text");
			String value = (String) h.get("value");
			if (val != null && val.equals(value))
				selected = "selected";
			else
				selected = "";
			buff.append("\n\t<option value=\"").append(value).append("\" ")
					.append(selected).append(">").append(text).append(
							" </option>");
		}
		buff.append("</select>");
		retString = buff.toString();
		return this;
	}

	public String toString() {

		if (logger.isDebugEnabled()) {
			logger.debug("name: " + name);
			logger.debug("value: " + value);
		}

		if ("required_field".equals(elementType)
				|| "required_select_field".equals(elementType)
				|| "required_date_field".equals(elementType)) {
			requiredFields.addElement(name);
			messages.addElement(message);
		}
		
		if ("required_date_field".equals(elementType) || "date_field".equals(elementType)) {
			dateFields.addElement(name);
		}
		
		retString = retString.replaceAll("THE_NAME", name);
		retString = retString.replace("THE_VALUE", " value=\"" + value + "\" ");
		retString = retString.replace("THE_STYLE", " style=\"" + style + "\" ");
		retString = retString.replace("THE_CSS_CLASS", " class=\"" + cssClass + "\" ");
		retString = retString.replace("THE_COUNT", filesCount);
		retString = retString.replace("THE_UPLOAD_DIR", uploadDir);
		return retString;
	}

}
