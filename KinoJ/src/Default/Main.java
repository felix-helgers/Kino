package Default;

import UserManager.*;

public class Main {
	static UserManager usermanager = new UserManager();

	public static void main(String[] args) {
		Main main = new Main();
		
		main.showMainWindow();
	}
	
	public static void printUserInfos(User user) {
		System.out.println("Username: " + user.getUsername());
		System.out.println("FirstName: " + user.getFirstName());
		System.out.println("LastName: " + user.getLastName());
		System.out.println("Email: " + user.getEmail());
		System.out.println("Password: " + user.getPassword());
		System.out.println("PaymentMethod: " + user.getPaymentMethod());
	}
	
	public static User register() {
		RegisterAdapter rad = new RegisterAdapter(usermanager);
		return rad.register();
	}
	
	public static User SignIn() {
		SignInAdapter sad = new SignInAdapter(usermanager);
		return sad.signin();
	}
	
	private void showMainWindow() {
		MainAdapter mad = new MainAdapter();
		mad.showGUI();
	}
}
