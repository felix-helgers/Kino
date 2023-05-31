package Default;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
public class MainAdapter implements ActionListener {
	MainGUI mainGUI;
	
	public void showGUI() {
		mainGUI(this);
	}
	
	private void mainGUI(ActionListener listener) {
		SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            mainGUI = new MainGUI();
	            mainGUI.addActionListener(listener);
	        }
	    });
	}
	
	public void setUserButtonVisibility(boolean visibility) {
		mainGUI.setUserButtonVisibility(visibility);
	}
	
	public void setSighInButtonVisibility(boolean visibility) {
		mainGUI.setSighInButtonVisibility(visibility);
	}
	
	public void setBookingEnables(boolean enabled) {
		mainGUI.setBookingEnables(enabled);
	}
	
	public void setUserLabel(String username) {
		mainGUI.setUserLabel(username + "  ");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() == mainGUI.registerButton) {
			  Main.register();
			
		} else if (e.getSource() == mainGUI.signInButton) {
			  Main.SignIn();
			
		} else if (e.getSource() == mainGUI.signOutButton) {
			setUserLabel("Nicht angemeldet");
			System.out.println("User abgemeldet!");
			setUserButtonVisibility(false);
			setSighInButtonVisibility(true);
		} else if (e.getSource() == mainGUI.showBookingsButton) {
             new BuchungenGUI(Main.currentUser);
             Main.currentUser = null;
        }
	}
}