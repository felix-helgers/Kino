package Database;

import java.sql.ResultSet;

import UserManager.User;

public interface IDatabaseAdapter {
	public User GetUser(String username);
	public boolean SaveUser(User user);
	public boolean DeleteUser(User user);
	public ResultSet getTable(String tableName);
	public boolean CreateBooking(User user, String screeningID, String seatNr);
	public boolean CreateScreening(String name, String length);
}
