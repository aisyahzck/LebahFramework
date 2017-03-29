package lebah.msg;

import java.util.Date;

public class Message {
	
	private String to;
	private String from;
	private Date date;
	private String text;
	private String fromName;
	
	public Message(Date date, String from, String to, String txt) {
		this.date = date;
		this.from = from;
		this.to = to;
		this.text = txt;
	}
	
	public Message(Date date, String from, String name, String to, String txt) {
		this.date = date;
		this.from = from;
		this.fromName = name;
		this.to = to;
		this.text = txt;
	}
	
	
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getFrom() {
		return from;
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}


	public String getFromName() {
		return fromName;
	}


	public void setFromName(String fromName) {
		this.fromName = fromName;
	}
	
	
	

}
