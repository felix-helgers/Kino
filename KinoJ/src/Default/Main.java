package Default;

import java.sql.ResultSet;

import Database.IDatabaseAdapter;
import Database.SQLiteAdapter;
import UserManager.*;

public class Main {
	static UserManager usermanager = new UserManager();
	public static User currentUser;
	private static MainAdapter mad = null;
	private static IDatabaseAdapter adapter = SQLiteAdapter.getInstance();
	
	public static void main(String[] args) {
		Main main = new Main();
		main.showMainWindow();
	}

	public static void setUserInfos(User user) {
		mad.setUserLabel(user.getUsername());
		mad.setSighInButtonVisibility(false);
		mad.setUserButtonVisibility(true);
		currentUser = user;
		setBuchungButtonEnabled();
	}
	
	private static void setBuchungButtonEnabled() {		
		mad.setBookingEnables(adapter.userHasBockings(currentUser.getUsername()));
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
		mad = new MainAdapter();
		mad.showGUI();
	}
}
