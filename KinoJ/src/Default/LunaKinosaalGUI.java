package Default;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JPanel;

import Database.IDatabaseAdapter;
import Database.SQLiteAdapter;
import javax.swing.JLabel;
import javax.swing.*;

public class LunaKinosaalGUI extends JFrame {
	
	private IDatabaseAdapter databaseAdapter;
	private float kosteninEuro = 0;
	JButton bestaetigen;
	JLabel kosten;
	
    
    public LunaKinosaalGUI(int kinoSaal) {
    	super("Sitzplatz Reservierung");
    	this.setSize(1500, 1000);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        databaseAdapter = SQLiteAdapter.getInstance();
        JPanel panel = new JPanel(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel userButtonPanel = new JPanel();
        kosten = new JLabel("Kosten:  "+ this.kosteninEuro + " €");
        bestaetigen = new JButton("Bestätigen");
        userButtonPanel.add(kosten);
        userButtonPanel.add(bestaetigen);
        bottomPanel.add(userButtonPanel, BorderLayout.EAST);
        panel.add(bottomPanel, BorderLayout.NORTH);
        this.add(panel);
        int sitzPlatzReihenAnzahl = this.databaseAdapter.getSitzplatzReihenAnzahl(kinoSaal);
        JPanel sitzPlatzPanel = new JPanel(new GridLayout(1,sitzPlatzReihenAnzahl));
        
        for(int i = 1; i <= sitzPlatzReihenAnzahl; i++) {
        	int columnCount = databaseAdapter.getSeatsInRow(kinoSaal, i);
        }
        
        
        
        panel.add(sitzPlatzPanel, BorderLayout.CENTER);
        this.setContentPane(panel);
    }
}