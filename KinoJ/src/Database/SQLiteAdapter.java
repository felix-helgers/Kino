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
		try {
			ResultSet rs = executeQuery("Select * from User where username = '" + username + "'");
			return new User(rs.getString(1), rs.getString(2), rs.getString(5), rs.getString(3), rs.getString(4));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean SaveUser(User user) {
		return executeNonQuery("Insert into User (username, vorname, nachname, email, passwort, BerechtigungsGruppenId) Values ('" + user.getUsername() + "','" + user.getFirstName() + "','" + user.getLastName() + "','" + user.getEmail() + "','" + user.getPassword() + "', '" + user.getUserGroup().getID() + "')", 1);
	}

	@Override
	public boolean DeleteUser(User user) {
		return executeNonQuery("Delete user where username = " + user.getUsername(), 1);
	}

	@Override
	public ResultSet getTable(String tableName) {
		return executeQuery("Select * from " + tableName);
	}

	@Override
	public boolean CreateBooking(User user, String screeningID, String seatNr) {
		if (!executeNonQuery("Insert into Buchung (Username) Values ('" + user.getUsername() + "')", 1)) {
			return false;
		}
		//TODO: Reservierung erstellen
		
		return true;
	}

	@Override
	public boolean CreateScreening(String name, String length) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean executeNonQuery(String sqlStatement, int expectedRowsChanged) {
        Statement statement = null;

        try {
        	this.conn.setAutoCommit(false);

            statement = this.conn.createStatement();
            int rowsChanged = statement.executeUpdate(sqlStatement);

            if (rowsChanged != expectedRowsChanged) {
            	this.conn.rollback();
                System.out.println("Execution failed. Unexpected number of rows changed. Expected: " + expectedRowsChanged + ", Actual: " + rowsChanged);
            } else {
            	this.conn.commit();
                System.out.println("Execution successful. Number of rows changed: " + rowsChanged);
            }
            return true;
        } catch (SQLException e) {
            if (this.conn != null) {
                try {
                	this.conn.rollback();
                } catch (SQLException ex) {
                    System.out.println("Rollback failed: " + ex.getMessage());
                }
            }
            System.out.println("Execution failed: " + e.getMessage());
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("Failed to close statement: " + ex.getMessage());
                }
            }
        }
    }
	
	private ResultSet executeQuery(String sql) {
		if (this.conn == null) {
			if (this.establishDatabaseConnection() == false) {
				return null;			
			}
		}
		
		try {
			this.conn.setAutoCommit(true);
			return conn.prepareStatement(sql).executeQuery();
		} catch (SQLException ex) {
			System.out.println("Execution failed: " + ex.getMessage());
		}
		return null;
	}
}
