package UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignInGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	JTextField usernameField;
	JPasswordField passwordField;
	JButton signInButton;
	JButton cancelButton;
	JLabel errorLabel;
	SignInCallback callback;
	
	public interface SignInCallback {
		void onSignIn(String username, String password);
		void onCancel();
	}
	
	public SignInGUI(UserManager usermanager, SignInCallback callback) {
		super("Anmelden");
		this.callback = callback;
		this.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width) / 2;
		int y = (dim.height) / 2;
		this.setLocation(x - 50, y - 150);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
	    JPanel panel = new JPanel(new GridLayout(0, 1));
	
	    JLabel usernameLabel = new JLabel(" Benutzername:");
	    panel.add(usernameLabel);
	
	    usernameField = new JTextField(20);
	    panel.add(usernameField);
	
	    JLabel passwordLabel = new JLabel(" Passwort:");
	    panel.add(passwordLabel);
	
	    passwordField = new JPasswordField(20);
	    panel.add(passwordField);
	
	    errorLabel = new JLabel("");
	    errorLabel.setForeground(Color.RED);
	    panel.add(errorLabel);
	
	    JPanel buttonPanel = new JPanel(new GridLayout(1,0));
	    
	    signInButton = new JButton("Anmelden");
	    buttonPanel.add(signInButton);

	    cancelButton = new JButton("Abbrechen");
	    buttonPanel.add(cancelButton);
	    
	    panel.add(buttonPanel);
	    add(panel);
	    pack();
	    setVisible(true);
	}
	
    public void addActionListener(ActionListener listener) {
    	signInButton.addActionListener(listener);
    	cancelButton.addActionListener(listener);
    }
}
