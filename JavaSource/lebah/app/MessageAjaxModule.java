/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.app;

public class MessageAjaxModule extends ForumAjaxModule {
	
	public MessageAjaxModule() {
		idPrefix = "mes_";
		vtlDir = "vtl/message/ajax";
		LIST_ROWS = ROWS;
		names = new String[] {"Rows", "Moderators", "Attachment", "StartAsList", "ShowSubject"};
	}

}
