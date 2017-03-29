package lebah.app;

import java.util.Hashtable;

import lebah.portal.AjaxBasedModule;
import lebah.portal.Attributable;
import lebah.portal.XMLContainer;
import lebah.portal.XMLTransformer;

public class XMLAjaxModule extends AjaxBasedModule implements XMLContainer, Attributable {
	
	protected String[] names = {"Height"};
	protected Hashtable values = new Hashtable();
	
	private String strUrl = "";
	private String strXsl = "";
	private String path = "vtl/rss/";
	
	public String[] getNames() {
		return names;
	}
	
	public Hashtable getValues() {
		return values;
	}
	
	public void setValues(java.util.Hashtable hashtable) {
		values = hashtable;
	}
	
	//set the url
	public void setXml(String strUrl) {
		this.strUrl = strUrl;
	}	
	//set the style sheet
	public void setXsl(String strXsl) {
		this.strXsl = strXsl;
	}

	@Override
	public String doTemplate2() throws Exception {
		String command = request.getParameter("command");
		String height = values.get(names[0]) != null ? (String) values.get(names[0]) : "";
		context.put("height", !"".equals(height) ? height + "px" : "300px");
		if ( "getRSS".equals(command)) return getRSS();
		return path + "start.vm";
	}
	
	private String getRSS() {
        String s;
		try {
			s = XMLTransformer.transform(strXsl, strUrl);
			context.put("xmlcontent", s);
		} catch (Exception e) {
			context.remove("xmlcontent");
			e.printStackTrace();
		}
        
		return path + "xml.vm";
	}


}
