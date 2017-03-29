package lebah.app;

public class UserData {
	
	private String login;
	private String altLogin;
	private String password;
	private String name;
	private String role;
	
	public UserData() {
		
	}
	
	public UserData(String login, String name, String role) {
		this.login = login;
		this.name = name;
		this.role = role;
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getAltLogin() {
		return altLogin;
	}
	public void setAltLogin(String altLogin) {
		this.altLogin = altLogin;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
	

}
