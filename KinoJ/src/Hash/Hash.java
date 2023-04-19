package Hash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	private static final String ALGORITHM = "SHA-256";
	
	public static String hashPassword(String password) {
		try {
		    MessageDigest md = MessageDigest.getInstance(ALGORITHM);
		      
		    byte[] passwordBytes = password.getBytes();
		    byte[] hashBytes = md.digest(passwordBytes);
		      
		    StringBuilder sb = new StringBuilder();
		    for (byte b : hashBytes) {
		        sb.append(String.format("%02x", b));
		    }
		    return sb.toString();
		      
		} catch (NoSuchAlgorithmException e) {
		    throw new RuntimeException("Error: " + ALGORITHM + " algorithm not found.", e);
		}
	}
}
