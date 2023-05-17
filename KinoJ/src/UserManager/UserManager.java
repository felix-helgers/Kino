package UserManager;

import Database.IDatabaseAdapter;
import Database.SQLiteAdapter;

public class UserManager {
	private IDatabaseAdapter DBAdapter;
	
	public UserManager() {
		DBAdapter = SQLiteAdapter.getInstance();
	}

	public boolean addUser(User user) {
		return DBAdapter.SaveUser(user);
	}

	public boolean authenticate(String username, String password) {
		User user = DBAdapter.GetUser(username, password);
		return user != null && user.getPassword().equals(password);
	}
	
	public User getUser(String username, String password) {
		return DBAdapter.GetUser(username, password);
	}

	public void deleteUser(User user) {
		DBAdapter.DeleteUser(user);
	}

	public boolean usernameExists(String username) {
		return DBAdapter.UserNameExists(username);
	}
}
