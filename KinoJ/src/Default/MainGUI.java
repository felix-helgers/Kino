package Default;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MainGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton registerButton;
	JButton signInButton;
	JLabel userLabel;
	
	public MainGUI() {
		super("Main GUI");
		this.setSize(1500, 1000);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel userButtonPanel = new JPanel();
        
        userButtonPanel.add(new JLabel());
        
        userLabel = new JLabel("Username ");
        userButtonPanel.add(userLabel);
        
        registerButton = new JButton("Registrieren");
        userButtonPanel.add(registerButton);
        
        signInButton = new JButton("Anmelden");
        userButtonPanel.add(signInButton);
        
        topPanel.add(userButtonPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);
        
        add(panel);
	    setVisible(true);
	}
	
    public void addActionListener(ActionListener listener) {
    	signInButton.addActionListener(listener);
    	registerButton.addActionListener(listener);
    }
}
