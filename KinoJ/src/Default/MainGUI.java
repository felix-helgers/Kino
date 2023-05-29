package Default;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.*;
import Database.IDatabaseAdapter;
import Database.SQLiteAdapter;
import java.util.ArrayList;
import java.awt.GridBagConstraints;
import java.awt.*;
import java.awt.event.*;

public class MainGUI extends JFrame {
	private static final long serialVersionUID = 1L;
	JButton registerButton;
	JButton signInButton;
	JButton signOutButton;
	JButton showBookingsButton;
	private JLabel userLabel;
	private JPanel filmplakatePanel;
	private JScrollPane scrollPane;
	private IDatabaseAdapter databaseAdapter;
	private String  ordnerpfad = System.getProperty("user.dir") + "\\src\\Bilder\\";
	String film;
	
	public MainGUI() {
		super("Kino");
		this.setSize(1500, 1000);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        databaseAdapter = SQLiteAdapter.getInstance();
        
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel(new BorderLayout());
        JPanel userButtonPanel = new JPanel();
        
        userButtonPanel.add(new JLabel());
        
        userLabel = new JLabel("Nicht angemeldet  ");
        userButtonPanel.add(userLabel);
        
        registerButton = new JButton("Registrieren");
        userButtonPanel.add(registerButton);
        
        signInButton = new JButton("Anmelden");
        userButtonPanel.add(signInButton);
        
        signOutButton = new JButton("Abmelden");
        signOutButton.setVisible(false);
        userButtonPanel.add(signOutButton);
        
        showBookingsButton = new JButton("Buchungen");
        showBookingsButton.setVisible(false);
        userButtonPanel.add(showBookingsButton);
        
        topPanel.add(userButtonPanel, BorderLayout.EAST);
        panel.add(topPanel, BorderLayout.NORTH);
        this.add(panel);
        
        filmplakatePanel = new JPanel(new GridBagLayout());
        scrollPane = new JScrollPane(filmplakatePanel);
        panel.add(scrollPane, BorderLayout.CENTER);
        this.setContentPane(panel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); // Abstand zwischen den Filmplakaten
        
        ArrayList<String> filme = this.databaseAdapter.getMoviesWithScreeningAndPoster();
        
        filme.forEach(film -> {
        
        ImageIcon bildIcon = new ImageIcon(ordnerpfad + film +".jpg");
        
        int desiredWidth = 200;
        int desiredHeight = 300;
        Image scaledImage = bildIcon.getImage().getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel bildLabel = new JLabel(scaledIcon);

        
        JLabel titelLabel = new JLabel(film);
        titelLabel.setPreferredSize(new Dimension(150, titelLabel.getPreferredSize().height)); // Maximale Breite des Labels festlegen
        titelLabel.setToolTipText(film); // Hinzufügen eines Tooltips, um den vollständigen Titel anzuzeigen

        if (film.length() > 20) {
            // Titel abschneiden und mit ... enden
            String abgeschnittenerTitel = film.substring(0, 20) + "...";
            titelLabel.setText(abgeschnittenerTitel);
        }

        JPanel plakatPanel = new JPanel(new BorderLayout());
        plakatPanel.add(bildLabel, BorderLayout.CENTER);
        plakatPanel.add(titelLabel, BorderLayout.SOUTH);
        		
        plakatPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            	System.out.println("Plakat " + film + " wurde geklickt.");
            	new ScreeningsGUI(plakatPanel, film);
            	refresh();
            	
                
            }
        });
        
//        JPanel plakatPanel2 = new JPanel(new BorderLayout());
//        for (int i = 0; i < plakatPanel.getComponentCount(); i++) {
//        	plakatPanel2.add(plakatPanel.getComponent(i));        	
//        }
        
        filmplakatePanel.add(plakatPanel, gbc);
        gbc.gridx++;
        
        if (gbc.gridx % 3 == 0) {
            gbc.gridx = 0;
            gbc.gridy++;
        }});
        
        this.pack();
        this.setLocationRelativeTo(null);
	    setVisible(true);
	    
	}
	
	public void setUserLabel(String username) {
		userLabel.setVisible(true);
		userLabel.setText(username);
	}
	
	public void setSighInButtonVisibility(boolean visibility) {
		registerButton.setVisible(visibility);
		signInButton.setVisible(visibility);
	}
	
	public void setUserButtonVisibility(boolean visibility) {
		signOutButton.setVisible(visibility);
		showBookingsButton.setVisible(visibility);
	}
	
    public void addActionListener(ActionListener listener) {
    	signInButton.addActionListener(listener);
    	registerButton.addActionListener(listener);
    	signOutButton.addActionListener(listener);
    	showBookingsButton.addActionListener(listener);
    }
    
    public void refresh() {
    	
    	this.filmplakatePanel.removeAll();
    	this.filmplakatePanel.setLayout(new GridBagLayout());
    	
    	 GridBagConstraints gbc = new GridBagConstraints();
         gbc.gridx = 0;
         gbc.gridy = 0;
         gbc.insets = new Insets(10, 10, 10, 10); // Abstand zwischen den Filmplakaten
    	
    	 ArrayList<String> filme = this.databaseAdapter.getMoviesWithScreeningAndPoster();
         
         filme.forEach(film -> {
         
         ImageIcon bildIcon = new ImageIcon(ordnerpfad + film +".jpg");
         
         int desiredWidth = 200;
         int desiredHeight = 300;
         Image scaledImage = bildIcon.getImage().getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
         ImageIcon scaledIcon = new ImageIcon(scaledImage);

         JLabel bildLabel = new JLabel(scaledIcon);

         
         JLabel titelLabel = new JLabel(film);
         titelLabel.setPreferredSize(new Dimension(150, titelLabel.getPreferredSize().height)); // Maximale Breite des Labels festlegen
         titelLabel.setToolTipText(film); // Hinzufügen eines Tooltips, um den vollständigen Titel anzuzeigen

         if (film.length() > 20) {
             // Titel abschneiden und mit ... enden
             String abgeschnittenerTitel = film.substring(0, 20) + "...";
             titelLabel.setText(abgeschnittenerTitel);
         }

         JPanel plakatPanel = new JPanel(new BorderLayout());
         plakatPanel.add(bildLabel, BorderLayout.CENTER);
         plakatPanel.add(titelLabel, BorderLayout.SOUTH);
         		
         plakatPanel.addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
             	System.out.println("Plakat " + film + " wurde geklickt.");
             	new ScreeningsGUI(plakatPanel, film);
             	refresh();
             	
                 
             } });
    	
         filmplakatePanel.add(plakatPanel, gbc);
         gbc.gridx++;
         
         if (gbc.gridx % 3 == 0) {
             gbc.gridx = 0;
             gbc.gridy++;
         
         
         this.pack();
         this.setLocationRelativeTo(null);
 	      setVisible(true);
    	
    
    
    
         
    };
});
}
}
