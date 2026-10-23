package devpro.example.CommerceAdmin.dto;

public class UserDto {
	
	private String username;
	private String password;
	private String passwordRetype;
	private String email;
	private String mobile;
	
	public UserDto() {
		super();
	}

	public UserDto(String username, String password, String passwordRetype, String email, String mobile) {
		super();
		this.username = username;
		this.password = password;
		this.passwordRetype = passwordRetype;
		this.email = email;
		this.mobile = mobile;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordRetype() {
		return passwordRetype;
	}

	public void setPasswordRetype(String passwordRetype) {
		this.passwordRetype = passwordRetype;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	
	
}
