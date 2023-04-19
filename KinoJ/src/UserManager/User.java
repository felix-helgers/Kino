package UserManager;

public class User {
	private String username;
	private String password;
	private String email;
	private String firstName;
	private String lastName;
	private String paymentMethod;
	private Group UserGroup;

	public User(String username, String password, String email, String firstName, String lastName, Group userGroup) {
	    setUsername(username);
	    setPassword(password);
	    setEmail(email);
	    setFirstName(firstName);
	    setLastName(lastName);
	}
	
	public User(String username, String password, String email, String firstName, String lastName) {
	    setUsername(username);
	    setPassword(password);
	    setEmail(email);
	    setFirstName(firstName);
	    setLastName(lastName);
	}
	
	public User(String username, String password) {
		setUsername(username);
		setPassword(password);
	}

	public String getUsername() {
	    return username;
	}

	public void setUsername(String username) {
	    if (username == null || username.isEmpty()) {
	      throw new IllegalArgumentException("Username cannot be empty");
	    }
	    this.username = username;
	}

	public String getPassword() {
	    return password;
	}

	public void setPassword(String password) {
	    if (password == null || password.isEmpty()) {
	      throw new IllegalArgumentException("Password cannot be empty");
	    }
	    this.password = password;
	}

	public String getEmail() {
	    return email;
	}

	public void setEmail(String email) {
	    this.email = email;
	}

	public Group getUserGroup() {
		return UserGroup;
	}
	 
	public void setUserGroup(Group userGroup) {
		this.UserGroup = userGroup;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	
	@Override
	public String toString() {
	    return "User{" +
	            "username='" + username + '\'' +
	            ", email='" + email + '\'' +
	            ", firstName='" + firstName + '\'' +
	            ", lastName='" + lastName + '\'' +
	            '}';
	}
}
