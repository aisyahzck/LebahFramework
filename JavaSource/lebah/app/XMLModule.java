/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */



package lebah.app;

import lebah.portal.XMLTransformer;

import org.apache.velocity.Template;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class XMLModule extends lebah.portal.velocity.VTemplate implements lebah.portal.XMLContainer {
	
	private String strUrl = "";
	private String strXsl = "";
	
	//set the url
	public void setXml(String strUrl) {
		this.strUrl = strUrl;
	}	
	//set the style sheet
	public void setXsl(String strXsl) {
		this.strXsl = strXsl;
	}

	
	public Template doTemplate() throws Exception {
        javax.servlet.http.HttpSession session = request.getSession();
        String s = XMLTransformer.transform(strXsl, strUrl);
        context.put("xmlcontent", s);
        Template template = engine.getTemplate("vtl/custom/xml.vm");
        return template;		
	}
}
