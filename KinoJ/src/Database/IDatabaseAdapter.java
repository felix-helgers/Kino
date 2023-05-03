package Database;

import java.sql.ResultSet;

import UserManager.User;

public interface IDatabaseAdapter {
	public User GetUser(String username);
	public boolean SaveUser(User user);
	public void DeleteUser(User user);
	public ResultSet getTable(String tableName);
	public void CreateBooking(User user, String screeningID, String seatNr);
	public void CreateScreening(String name, String length);
}
