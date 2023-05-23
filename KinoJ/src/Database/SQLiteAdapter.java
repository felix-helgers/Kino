package Database;

import java.sql.*;
import java.util.ArrayList;

import UserManager.User;

public class SQLiteAdapter implements IDatabaseAdapter {

	private final String DatabasePath = "D:\\Workspace-Eclipse\\Kino\\Database\\Kino.db";


	private Connection conn;
	private static SQLiteAdapter instance;
	
	public SQLiteAdapter() {
		establishDatabaseConnection();
	}
	
	public static SQLiteAdapter getInstance() {
	    if (instance == null) {
	        synchronized (SQLiteAdapter.class) {
	            if (instance == null) {
	                instance = new SQLiteAdapter();
	            }
	        }
	    }
	    return instance;
	}
	
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
	public User GetUser(String username, String password) {
		ensureConnection();
		
		try {
			ResultSet rs = executeQuery("Select * from User where username = '" + username + "' And passwort = '" + password + "'");
			return new User(username, password, rs.getString(4), rs.getString(2), rs.getString(3));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean SaveUser(User user) {
		ensureConnection();
		
		int UserGroupID = 0;
		if (user.getUserGroup() != null) {
			UserGroupID = user.getUserGroup().getID();
		}
		return executeNonQuery("Insert into User (username, vorname, nachname, email, passwort, BerechtigungsGruppenId) Values ('" + user.getUsername() + "','" + user.getFirstName() + "','" + user.getLastName() + "','" + user.getEmail() + "','" + user.getPassword() + "', '" + UserGroupID + "');", 1);
	}

	@Override
	public boolean DeleteUser(User user) {
		ensureConnection();
		return executeNonQuery("Delete user where username = " + user.getUsername() + ";", 1);
	}

	@Override
	public ResultSet getTable(String tableName) {
		if (this.conn == null) {
			if (this.establishDatabaseConnection() == false) {
				return null;			
			}
		}
		return executeQuery("Select * from " + tableName + ";");
	}

	@Override
	public boolean CreateBooking(User user, String screeningID, String... seatNr) {
		ensureConnection();
		if (!executeNonQuery("Insert into Buchung (Username) Values ('" + user.getUsername() + "');", 1)) {
			return false;
		}
		
		try {
			int bookingId = executeQuery("Select Max(Id) from Buchung;").getInt(0);
			for (int i = 0; i < seatNr.length; i++) {
				executeNonQuery("Insert into Reservierung (Buchung, Vorstellung, SitzPlatzId) Values ('" + bookingId + "', '" + screeningID + "','" + seatNr[i] + "');", 1);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public boolean CreateScreening(int film, int hall, String startTime) {
		ensureConnection();
		return executeNonQuery("Insert into Vorstellung (Saal, Film, Startzeit) Values (" + hall + ", " + film + ", '" + startTime + "');", 1);
	}

	public boolean executeNonQuery(String sqlStatement, int expectedRowsChanged) {
		ensureConnection();
		
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
		ensureConnection();
		
		try {
			this.conn.setAutoCommit(true);
	        Statement statement = conn.createStatement();
	        ResultSet resultSet = statement.executeQuery(sql);
			return resultSet;
		} catch (SQLException ex) {
			System.out.println("Execution failed: " + ex.getMessage());
		}
		return null;
	}

	@Override
	public boolean UserNameExists(String username) {
		ensureConnection();
		
		try {
			return executeQuery("Select * from User where username = '" + username + "';").first();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean ensureConnection() {
		if (this.conn == null) {
			if (this.establishDatabaseConnection() == false) {
				return false;			
			}
		}
		return true;
	}
	
	public ArrayList<String> getMoviesWithScreeningAndPoster() {
		
	    ArrayList<String> returnArray = new ArrayList<String>();

		ResultSet filme = executeQuery("select f.Name from Film f inner join Vorstellung v on f.id = v.Film where f.Poster = 1");

		String value;
		 try {
			while (filme.next()) {
			       value = filme.getString(1);
				   returnArray.add(value);
			    }
		} catch (SQLException e) {
			
			System.out.println("Filme konnten aus der Datenbank nicht geholt werden");
		}
		return returnArray;
	}
}
