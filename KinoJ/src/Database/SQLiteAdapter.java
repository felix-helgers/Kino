package Database;

import java.sql.*;
import UserManager.User;

public class SQLiteAdapter implements IDatabaseAdapter {
	private final String DatabasePath = ".\\Database\\Kino.db";
	private Connection conn;
	
	private boolean establishDatabaseConnection() {
		Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + DatabasePath);
            System.out.println("Verbindung zur SQLite-Datenbank hergestellt.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        this.conn = conn;
        return true;
	}
	
	@Override
	public User GetUser(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean SaveUser(User user) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void DeleteUser(User user) {
		// TODO Auto-generated method stub
	}

	@Override
	public ResultSet getTable(String tableName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void CreateBooking(User user, String screeningID, String seatNr) {
		// TODO Auto-generated method stub
	}

	@Override
	public void CreateScreening(String name, String length) {
		// TODO Auto-generated method stub
	}
	
	private ResultSet executeQuery(String sql) {
		if (this.conn == null) {
			if (this.establishDatabaseConnection() == false) {
				return null;			
			}
		}
		
		try {
			return conn.prepareStatement(sql).executeQuery();
		} catch (SQLException ex) {
			
		}
		return null;
	}
}
