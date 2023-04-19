package UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RegisterGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	JTextField usernameField;
    JPasswordField passwordField;
    JTextField emailField;
    JTextField firstNameField;
    JTextField lastNameField;
    JButton registerButton;
    JButton cancelButton;
	JLabel errorLabel;
	RegisterCallback callback;
	
    public interface RegisterCallback {
        void onRegister(String username, String password, String email, String firstName, String lastName);
        void onCancel();
    }
    
    public RegisterGUI(UserManager usermanager, RegisterCallback callback) {
    	super("Registrieren");
    	this.callback = callback;
        this.setResizable(false);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (dim.width) / 2;
		int y = (dim.height) / 2;
		this.setLocation(x - 50, y - 200);
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

        JLabel emailLabel = new JLabel(" Email:");
        panel.add(emailLabel);

        emailField = new JTextField(20);
        panel.add(emailField);

        JLabel firstNameLabel = new JLabel(" Vorname:");
        panel.add(firstNameLabel);

        firstNameField = new JTextField(20);
        panel.add(firstNameField);

        JLabel lastNameLabel = new JLabel(" Nachname:");
        panel.add(lastNameLabel);

        lastNameField = new JTextField(20);
        panel.add(lastNameField);

	    errorLabel = new JLabel("");
	    errorLabel.setForeground(Color.RED);
	    panel.add(errorLabel);
	    
	    JPanel buttonPanel = new JPanel(new GridLayout(1,0));
	    
        registerButton = new JButton("Registrieren");
        buttonPanel.add(registerButton);
        
        cancelButton = new JButton("Abbrechen");
        buttonPanel.add(cancelButton);

        panel.add(buttonPanel);
        add(panel);
        pack();
        setVisible(true);
    }
    
    public void addActionListener(ActionListener listener) {
    	registerButton.addActionListener(listener);
    	cancelButton.addActionListener(listener);
    }
}
