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
	
	public enum Preiskategorien{
		
		    LOGE(6,1,"Loge"),
	        PARKETT(6,2, "Parkett"),
	        ROLLSTUHL(6,3, "Rollstuhl"),
	        PREMIUM(10,4, "Premium");
	        
	        private int price;
	        private int kategorie;
	        private String name;
	        
	        private Preiskategorien(int price, int kategorie, String name) {
	            this.price = price;
	            this.kategorie = kategorie;
	            this.name = name;
	        }
	        
	        public int getPrice() {
	            return price;
	        }
	        
	        public int getKategorie() {
	        	return kategorie;
	        }
		
		
		
	}
	
    
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
        JPanel sitzPlatzPanel = new JPanel(new GridLayout(sitzPlatzReihenAnzahl,1));
        
        for(int i = 1; i <= sitzPlatzReihenAnzahl; i++) {
        	int seatCount = databaseAdapter.getSeatsInRow(kinoSaal, i);
        	JPanel sitzPlatzReihe = new JPanel(new GridLayout(1, seatCount));
        	
        	
        	for(int ii = 0; i <= seatCount; ii++) {
        		JCheckBox sitzPlatz = new JCheckBox();
        		
        		
        		sitzPlatzReihe.add(sitzPlatz);
        		
        	}
        	
        }
        
        JCheckBox box = new JCheckBox();
        box.setEnabled(false);
        
        
        panel.add(sitzPlatzPanel, BorderLayout.CENTER);
        this.setContentPane(panel);
    }
}