package UserManager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Default.Main;
import Hash.Hash;

public class RegisterAdapter implements ActionListener {
	RegisterGUI registerGUI;
	UserManager usermanager;
	User user = null;
	
	public RegisterAdapter(UserManager usermanager) {
		this.usermanager = usermanager;
	}
	
	public User register() {
	    RegisterGUI.RegisterCallback callback = new RegisterGUI.RegisterCallback() {

	        public void onRegister(String username, String password, String email, String firstName, String lastName) {
	            System.out.println("Registered with username: " + username + ", password: " + password);
	            registerGUI.dispose();
	            user = new User(username, password, email, firstName, lastName);
//	            Main.printUserInfos(user);
	        }

	        public void onCancel() {
	            System.out.println("Registration cancelled.");
	            registerGUI.dispose();
	            user = null;
	        }
	    };
	    registerGUI(callback, this);
	    return user;
	}

	
	private void registerGUI(RegisterGUI.RegisterCallback callback, ActionListener listener) {
	    SwingUtilities.invokeLater(new Runnable() {
	        public void run() {
	        	registerGUI = new RegisterGUI(usermanager, callback);
	        	registerGUI.addActionListener(listener);
	        }
	    });
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == registerGUI.registerButton) {
	        String username = registerGUI.usernameField.getText();
	        String password = Hash.hashPassword(String.valueOf(registerGUI.passwordField.getPassword()));
	        String email = registerGUI.emailField.getText();
	        String firstName = registerGUI.firstNameField.getText();
	        String lastName = registerGUI.lastNameField.getText();
	            
	        if (username.equals("") || password.equals("") || email.equals("") || firstName.equals("") || lastName.equals("")) {
	        	registerGUI.errorLabel.setText(" Bitte alle Felder ausfüllen!");
	           	return;
	        }
	            
	        User user = new User(username, password, email, firstName, lastName);
	        boolean success = usermanager.addUser(user);
	        if (success) {
	            JOptionPane.showMessageDialog(null, "User registered successfully.");
	            registerGUI.callback.onRegister(username, password, email, firstName, lastName);
	            registerGUI.dispose();
	        } else {
	            JOptionPane.showMessageDialog(null, "Username already exists.", "Error", JOptionPane.ERROR_MESSAGE);
	        }
		} else if (e.getSource() == registerGUI.cancelButton) {
			registerGUI.callback.onCancel();
			registerGUI.dispose();
		}
	}
}
