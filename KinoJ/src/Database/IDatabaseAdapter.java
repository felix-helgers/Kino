package Database;

import java.sql.ResultSet;
import java.util.ArrayList;

import UserManager.User;

public interface IDatabaseAdapter {
	public User GetUser(String username, String password);
	public boolean UserNameExists(String username);
	public boolean SaveUser(User user);
	public boolean DeleteUser(User user);
	public ResultSet getTable(String tableName);
	public boolean CreateBooking(User user, String screeningID, String... seatNr);
	public boolean CreateScreening(int film, int hall, String startTime);
	public ArrayList<String> getMoviesWithScreeningAndPoster();
	public void deleteReservation(int reservierungsID);
	public ResultSet getSeatsForCinema(int cinemaID);
	public ResultSet getReservierungen(User user);
	public ResultSet getVorstellungen(String film);
	public int getSitzplatzReihenAnzahl(int cinemaID);
	public int getSeatsInRow(int cinemaID, int RowID);
	public int[] getReservierungAndPlatzkategorieForSeat(String seatNr, int vorstellungID, int kinoSaal );
	public void makeBuchung(String Username, float Preis);
	public void makeReservation(float Preis, int vorstellungID, String sitzPlatzNummer);
}
