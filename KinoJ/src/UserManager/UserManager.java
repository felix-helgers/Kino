package UserManager;

import java.util.HashMap;
import java.util.Map;
//import DataAdapter.*;

public class UserManager {
	private Map<String, User> users;
	
	public UserManager() {
		this.users = new HashMap<>();
		this.addUser(new User("Felix","f6b6aad7a7690810466cb5288aea79c4eccd72d05bae7ab16be65bf9b3b6538e"));
	}

	public boolean addUser(User user) {
		if (users.containsKey(user.getUsername())) {
			return false;
		}
		users.put(user.getUsername(), user);
		return true;
	}

	public boolean authenticate(String username, String password) {
		User user = users.get(username);
		return user != null && user.getPassword().equals(password);
	}

	public User getUser(String username) {
		return users.get(username);
	}

	public void updateUser(User user) {
		users.put(user.getUsername(), user);
	}

	public void deleteUser(String username) {
		users.remove(username);
	}

	public int getUserCount() {
		return users.size();
	}

	public boolean usernameExists(String username) {
		return users.containsKey(username);
	}

	public Map<String, User> getUsers() {
		return new HashMap<>(users);
	}

	public void clearUsers() {
		users.clear();
	}
	
	public boolean isInUserGroup(String username, Group userGroup) {
		User user = users.get(username);
		return user != null && user.getUserGroup().equals(userGroup);
	}
}
