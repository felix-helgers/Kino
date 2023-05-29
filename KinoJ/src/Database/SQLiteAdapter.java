package Database;

import java.sql.*;
import java.util.ArrayList;
import UserManager.User;

public class SQLiteAdapter implements IDatabaseAdapter {

	private final String DatabasePath = System.getProperty("user.dir") + "\\..\\Database\\Kino.db";

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
			if (rs.getString(1) != null) {			
				return new User(username, password, rs.getString(4), rs.getString(2), rs.getString(3));
			}
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
	
	public void deleteReservation(int reservierungsID) {
		this.ensureConnection();
		this.executeNonQuery("delete from Reservierung where ID = "+ reservierungsID, 1);
		System.out.println("Reservierung mit der ID " + reservierungsID + " wurde gel�scht.");
	}
	
	public ResultSet getReservierungen(User user) {
		this.ensureConnection();
		return this.executeQuery("select ReservierungsID, Name, Saal, Seat, Uhrzeit, Datum from V_Res where username = '" + user.getUsername() + "'");
	}
	
	public ResultSet getVorstellungen (String film) {
		this.ensureConnection();
		return this.executeQuery("Select v.Saal, v.Startzeit, v.Datum from Vorstellung v inner join Film f on v.Film = f.ID where f.Name ='" + film + "';");
	}

	@Override
	public ResultSet getSeatsForCinema(int cinemaID) {
		this.ensureConnection();
		return this.executeQuery("Select * from Seats where SaalNr = " + cinemaID + ";");
	}

	@Override
	public int getSitzplatzReihenAnzahl(int cinemaID) {
		try {
			ResultSet highestSeatNr = this.executeQuery("Select * from Seats where SaalNr = " + cinemaID + " order by SeatNr desc LIMIT 1;");
		    while(highestSeatNr.next()) {
				String seatNr = highestSeatNr.getString("SeatNr");
				String buchstabe = seatNr.substring(0, 1);
				int zahl = buchstabe.charAt(0) - 'A' + 1;
				return zahl;
		    }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getSeatsInRow(int cinemaID, int RowID) {
		char rowLetter = (char)('A' + RowID - 1);
		ResultSet seatsInRow = this.executeQuery("Select Count(*) from Seats where SaalNr = " + cinemaID + " And SeatNr ILike '" + rowLetter + "%';");
		try {
			seatsInRow.last();
			return (int)seatsInRow.getRow();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public int[] getReservierungAndPlatzkategorieForSeat(String seatNr, int vorstellungID ) {
		
		int returnArray[] = new int[2];
		
		ResultSet resultSetPlatz = this.executeQuery()
				while(resultSetPlatz.next()) {
					returnArray[0] = resultSetPlatz.getInt("Platzkategorie");
					
				}
		resultSetPlatz.close();
		
		
		
		
	}
}
