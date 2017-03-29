package lebah.msg;

public class User {
	
	private String uid;
	private String userId;
	private String userName;
	private String userAvatarName;
	
	public User(String uid, String userId, String userName, String userAvatarName) {
		this.uid = uid;
		this.userId = userId;
		this.userName = userName;
		this.userAvatarName = userAvatarName;
	}
	
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUserAvatarName() {
		return userAvatarName;
	}
	public void setUserAvatarName(String userAvatarName) {
		this.userAvatarName = userAvatarName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	

}
