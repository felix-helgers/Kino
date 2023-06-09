package Default;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import Database.SQLiteAdapter;
import Database.IDatabaseAdapter;
import javax.swing.JLabel;

public class ScreeningsGUI extends JFrame {
	
	int saal;
	int vorstellungID;
	
	public ScreeningsGUI(JPanel plakatPanel, String film) {
		super("Vorstellungen");
		this.setSize(1500, 1000);
		this.setAlwaysOnTop(true);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        IDatabaseAdapter databaseAdapter = SQLiteAdapter.getInstance();
        JPanel panel = new JPanel(new BorderLayout());
        JPanel vorstellungenPanel = new JPanel(new GridBagLayout());
        JScrollPane scrollPane = new JScrollPane(vorstellungenPanel);
        panel.add(scrollPane, BorderLayout.EAST);
        plakatPanel.removeMouseListener(plakatPanel.getMouseListeners()[0]);
        panel.add(plakatPanel, BorderLayout.WEST);
        this.add(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10); 
        
        ResultSet vorstellungen =  databaseAdapter.getVorstellungen(film);
        
        try {
        	if (vorstellungen == null) {
        		return;
        	}
			while (vorstellungen.next()) {
				
				JPanel infoBoxPanel = new JPanel();
				infoBoxPanel.setLayout(new GridLayout(3, 1));
		        infoBoxPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		        JLabel saalLabel = new JLabel( "Saal: "+vorstellungen.getInt("Saal"));
		        JLabel startzeitLabel = new JLabel("Startzeit: " + vorstellungen.getString("Startzeit"));
		        JLabel datumLabel = new JLabel("Datum: " + vorstellungen.getString("Datum"));
		        saal = vorstellungen.getInt("Saal");
		        vorstellungID = vorstellungen.getInt("ID");
		        infoBoxPanel.add(saalLabel);
		        infoBoxPanel.add(startzeitLabel);
		        infoBoxPanel.add(datumLabel);
		        
		        infoBoxPanel.addMouseListener(new MouseAdapter() {
		            @Override
		            public void mouseClicked(MouseEvent e) {
						new LunaKinosaalGUI(saal, vorstellungID);
						dispose();
		            	System.out.println("Vorstellung wurde angeklickt.");
		            }
				});
		        vorstellungenPanel.add(infoBoxPanel, gbc);
		        gbc.gridx++;
		            
		        if (gbc.gridx % 2 == 0) {
		            gbc.gridx = 0;
		        	gbc.gridy++;
		        }
			}
			//vorstellungen.close();
			this.pack();
	        this.setLocationRelativeTo(null);
		    setVisible(true);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
