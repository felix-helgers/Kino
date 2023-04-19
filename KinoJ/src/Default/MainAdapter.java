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
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == mainGUI.registerButton) {
			Main.register();
		} else if (e.getSource() == mainGUI.signInButton) {
			Main.SignIn();
		}
	}
}
