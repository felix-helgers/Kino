package Default;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.ArrayList;
import javax.swing.border.LineBorder;
import javax.swing.*;
import Database.IDatabaseAdapter;
import Database.SQLiteAdapter;

  public class LunaKinosaalGUI extends JFrame {

        private IDatabaseAdapter databaseAdapter;
        private float kosteninEuro = 0;
        private JButton bestaetigen;
        private JLabel kosten;
        private String sitzPlatzNummer;
        private ArrayList<String> gebuchtePlaetze = new ArrayList<String>();
        private int kinoSaal;

        public LunaKinosaalGUI(int kinoSaal, int vorstellungsID) {
            super("Sitzplatz Reservierung");
            this.kinoSaal = kinoSaal;
            
            
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            databaseAdapter = SQLiteAdapter.getInstance();

            JPanel panel = new JPanel(new BorderLayout());
            JPanel bottomPanel = new JPanel(new BorderLayout());
            JPanel userButtonPanel = new JPanel();
            kosten = new JLabel("Kosten gesamt:  " + this.kosteninEuro + " €");
            bestaetigen = new JButton("Bestätigen");
            bestaetigen.addActionListener(e -> {
                if (Main.currentUser == null){
                    JOptionPane.showMessageDialog(null, "Bitte einloggen", "Kein Benutzer", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                databaseAdapter.makeBuchung(Main.currentUser.getUsername(), kosteninEuro);
                for (String platzNummer : gebuchtePlaetze) {
                    databaseAdapter.makeReservation(kosteninEuro, vorstellungsID, platzNummer);
                }
                JOptionPane.showMessageDialog(null, "Buchung erfolgreich", "Buchung", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            });

            userButtonPanel.add(kosten);
            userButtonPanel.add(bestaetigen);
            bottomPanel.add(userButtonPanel, BorderLayout.EAST);
            panel.add(bottomPanel, BorderLayout.SOUTH);
            this.add(panel);

            int sitzPlatzReihenAnzahl = this.databaseAdapter.getSitzplatzReihenAnzahl(this.kinoSaal);

            JPanel sitzPlatzPanel = new JPanel(new GridLayout(sitzPlatzReihenAnzahl, 1));
            sitzPlatzPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
           
            for (int i = 1; i <= sitzPlatzReihenAnzahl; i++) {
                int seatCount = databaseAdapter.getSeatsInRow(this.kinoSaal, i);
                JPanel sitzPlatzReihe = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

                for (int ii = 1; ii <= seatCount; ii++) {
                    JCheckBox sitzPlatz = new JCheckBox();
                    sitzPlatz.setMargin(new Insets(5, 5, 5, 5));
                    sitzPlatz.setBorder(new LineBorder(Color.BLACK));
                    sitzPlatz.setPreferredSize(new Dimension(50, 50));

                    sitzPlatzNummer = ((char) ('A' + i - 1)) + Integer.toString(ii);
                    sitzPlatz.setText(sitzPlatzNummer);
                    sitzPlatz.setToolTipText("Platz " + sitzPlatzNummer);

                    int[] platzUndReservierung = this.databaseAdapter.getReservierungAndPlatzkategorieForSeat(sitzPlatzNummer, vorstellungsID, kinoSaal);

                    if (platzUndReservierung[1] == 0) {
                        switch (platzUndReservierung[0]) {
                            case 1:
                                sitzPlatz.setBackground(Color.BLUE);
                                sitzPlatz.setToolTipText("Loge");
                                sitzPlatz.putClientProperty("preis", 6); 
                                break;
                            case 2:
                                sitzPlatz.setBackground(Color.RED);
                                sitzPlatz.setToolTipText("Parkett");
                                sitzPlatz.putClientProperty("preis", 6); 
                                break;
                            case 3:
                                sitzPlatz.setBackground(Color.YELLOW);
                                sitzPlatz.setToolTipText("Rollstuhl");
                                sitzPlatz.putClientProperty("preis", 6); 
                                break;
                            case 4:
                                sitzPlatz.setBackground(Color.PINK);
                                sitzPlatz.setToolTipText("Premium");
                                sitzPlatz.putClientProperty("preis", 10); 
                                break;
                        }

                        sitzPlatz.setOpaque(true);
                        sitzPlatz.addActionListener(e -> {
                        	 JCheckBox cb = (JCheckBox) e.getSource();
                        	    if (cb.isSelected()) {
                        	        this.kosteninEuro += (int) cb.getClientProperty("preis"); // Preis abrufen
                        	        this.gebuchtePlaetze.add(cb.getText());
                                    System.out.println("Nr. " + cb.getText() + " wurde hinzugefügt");
                        	    } else {
                        	        this.kosteninEuro -= (int) cb.getClientProperty("preis"); // Preis abrufen
                        	        this.gebuchtePlaetze.remove(cb.getText());
                                    System.out.println("Nr. " + cb.getText() + " wurde entfernt");
                        	    }
                        	    kosten.setText("Kosten gesamt:  " + this.kosteninEuro + " €");
                        });
                    } else {
                        sitzPlatz.setBackground(Color.GRAY);
                        sitzPlatz.setEnabled(false);
                    }
                    sitzPlatz.setForeground(sitzPlatz.getBackground());
                    sitzPlatzReihe.add(sitzPlatz);
                }
                sitzPlatzPanel.add(sitzPlatzReihe);
            }

            panel.add(sitzPlatzPanel, BorderLayout.CENTER);
            this.setContentPane(panel);
            this.pack();
            this.setVisible(true);
        }
    }