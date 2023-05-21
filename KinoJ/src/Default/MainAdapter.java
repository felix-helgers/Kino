package Default;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

import UserManager.User;


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
	
	public void setUserLabel(String username) {
		mainGUI.setUserLabel(username);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mainGUI.registerButton) {
			setUserLabel(Main.register().getUsername());
		} else if (e.getSource() == mainGUI.signInButton) {
			User user = Main.SignIn();
			if (user.getUsername() != null) {
				setUserLabel(user.getUsername());
			}
		}
	}
}