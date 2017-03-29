package lebah.msg;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Room {
	
	private Hashtable<String, Vector<Message>> messages = new Hashtable<String, Vector<Message>>();
	private List<User> users = new ArrayList<User>();
	private int slideNo = 1;
	
	public Hashtable<String, Vector<Message>> getMessages() {
		return messages;
	}
	public void setMessages(Hashtable<String, Vector<Message>> messages) {
		this.messages = messages;
	}
	public int getSlideNo() {
		return slideNo;
	}
	public void setSlideNo(int slideNo) {
		this.slideNo = slideNo;
	}
	public List<User> getUsers() {
		return users;
	}
	public void setUsers(List<User> users) {
		this.users = users;
	}

	
	

}
