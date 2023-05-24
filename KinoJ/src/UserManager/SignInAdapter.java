package UserManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import Default.Main;
import Hash.Hash;

public class SignInAdapter implements ActionListener {
	SignInGUI signInGUI;
	User user;
	UserManager usermanager;
	int trys = 1;
	int maxTrys = 3;
	
	public SignInAdapter(UserManager usermanager) {
		this.usermanager = usermanager;
	}
	
	public User signin() {
	    SignInGUI.SignInCallback callback = new SignInGUI.SignInCallback() {
	    	
	        public void onSignIn(String username, String password) {
	            System.out.println("Signed in with username: " + username + ", password: " + password);
	            signInGUI.dispose();
	            user = usermanager.getUser(username, password);
	            Main.setUserInfos(user);
	        }
	        
	        public void onCancel() { 
	        	System.out.println("Sign in cancelled.");
	            signInGUI.dispose();
	            user = null;
	        }
	    };
	    signinGUI(callback, this);
	    return user;
	}
	
	private void signinGUI(SignInGUI.SignInCallback callback, ActionListener listener) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	            signInGUI = new SignInGUI(usermanager, callback);
	            signInGUI.addActionListener(listener);
	        }
	    });
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == signInGUI.signInButton){
			if (trys >= maxTrys) {
				signInGUI.callback.onCancel();
    		}
    		
	        String username = signInGUI.usernameField.getText();
	        String password = Hash.hashPassword(new String(signInGUI.passwordField.getPassword()));
	        boolean authenticated = usermanager.authenticate(username, password);
	
	        if (!authenticated) {
	        	signInGUI.errorLabel.setText("  Passwort ist ungültig");
	        	trys++;
	        } else {
	        	signInGUI.errorLabel.setText("");
	        	JOptionPane.showMessageDialog(null, "LogIn successfully.");
	        	signInGUI.callback.onSignIn(username, password);
	        }
		} else if (e.getSource() == signInGUI.cancelButton) {
			signInGUI.callback.onCancel();
		}
	}
}
