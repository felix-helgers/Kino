package Database;

import java.sql.*;
import java.util.ArrayList;
import UserManager.User;

public class SQLiteAdapter implements IDatabaseAdapter {

	//private final String DatabasePath = "E:\\eclipse-workspace\\Kino\\Kino\\Database\\Kino.db";
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
            statement.executeUpdate(sqlStatement);

            this.conn.commit();
            System.out.println("Execution successful.");

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
		if (!this.ensureConnection()){
			return null;
		}
		
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
		if (!this.ensureConnection()){
			return true;
		}
		
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
		if (!this.ensureConnection()){
			return null;
		}

	    ArrayList<String> returnArray = new ArrayList<String>();
		ResultSet filme = executeQuery("select f.Name from Film f inner join Vorstellung v on f.id = v.Film where f.Poster = 1");
		String value;

		try {
			while (filme.next()) {
			       value = filme.getString(1);
				   returnArray.add(value);
			    }
		} catch (Exception e) {
			
			System.out.println("Filme konnten aus der Datenbank nicht geholt werden");
		}
		return returnArray;
	}
	
	public void deleteReservation(int reservierungsID) {
		this.ensureConnection();
		if (this.executeNonQuery("delete from Reservierung where ID = "+ reservierungsID, 1)) {
			System.out.println("Reservierung mit der ID " + reservierungsID + " wurde ge�scht.");			
		} else {
			System.out.println("Reservierung mit der ID " + reservierungsID + " konnte nicht ge�scht werden.");				
		}
	}
	
	public ResultSet getReservierungen(User user) {
		this.ensureConnection();
		return this.executeQuery("select ReservierungsID, Name, Saal, Seat, Uhrzeit, Datum from V_Res where username = '" + user.getUsername() + "'");
	}
	
	public ResultSet getVorstellungen (String film) {
		this.ensureConnection();
		return this.executeQuery("Select v.Saal, v.Startzeit, v.Datum, v.ID from Vorstellung v inner join Film f on v.Film = f.ID where f.Name ='" + film + "';");
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
		try {
		char rowLetter = (char)('A' + RowID - 1);
		ResultSet seatsInRow = this.executeQuery("Select Count(*) from Seats where SaalNr = " + cinemaID + " And SeatNr Like '" + rowLetter + "%';");
		
			while(seatsInRow.next()) {
				return seatsInRow.getInt(1);
			}
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return 0;
	}
	
	public int[] getReservierungAndPlatzkategorieForSeat(String seatNr, int vorstellungID, int kinoSaal ) {
		int returnArray[] = new int[2];
		try {
		    ResultSet resultSetPlatz = this.executeQuery("select s.Platzkategorie from Seats s where s.SaalNr = "+ kinoSaal +" and s.SeatNr = '" + seatNr + "';");
				
			while(resultSetPlatz.next()) {
				returnArray[0] = resultSetPlatz.getInt("Platzkategorie");
			}
			resultSetPlatz.close();
						
			ResultSet resultSetReservierung = this.executeQuery("select count(*) from v_Res where VorstellungsID = " + vorstellungID + " and Seat = '" + seatNr + "';");
			while(resultSetReservierung.next()) {
				returnArray[1] = resultSetReservierung.getInt(1);
			}  resultSetReservierung.close();
					
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return returnArray;	
	}
	
	public void makeBuchung(String Username, float Preis) {
		this.ensureConnection();

		System.out.println("Buchung wird erstellt..");
		this.executeNonQuery("Insert into Buchung (Username, Preis) values ('" + Username + "', " + Preis + ");", 1);
	}

	public void makeReservation(float Preis, int vorstellungID, String sitzPlatzNummer) {
		this.ensureConnection();

		sitzPlatzNummer = getSeatID(sitzPlatzNummer, vorstellungID);
		try {
			ResultSet BuchungsID = this.executeQuery("Select Max(ID) AS ID from Buchung;");
			int BuchungsIDInt = BuchungsID.getInt("ID");
			System.out.println("Reservierung f�r den Film mit der ID " + vorstellungID + " und dem Platz " + sitzPlatzNummer + " wird erstellt...");
			this.executeNonQuery("insert into Reservierung (Buchung, Vorstellung, SitzPlatzID, Preis) values ('" + BuchungsIDInt + "', " + vorstellungID + ", '" + sitzPlatzNummer + "', " + (int)Preis + ");", 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private String getSeatID(String sitzPlatzNummer, int VorstellungsID){
		this.ensureConnection();
		try {
			ResultSet seatID = this.executeQuery("Select ID from Seats where SeatNr = '" + sitzPlatzNummer + "' and SaalNr = (Select Saal from Vorstellung where ID = " + VorstellungsID + ");");
			return seatID.getString("ID");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public boolean userHasBockings(String username) {
		if (!this.ensureConnection()){
			return false;
		}
		
		ResultSet rs = null;
		
		try {
			rs = this.executeQuery("Select * from v_Res where username = '" + username + "'");
			return rs.next();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}				
			}
		}
		return false;
	}
}
