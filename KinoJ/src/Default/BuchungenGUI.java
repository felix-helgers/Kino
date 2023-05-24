package Default;

import java.awt.event.ActionListener;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class BuchungenGUI extends JFrame {

	private JTable table;
    private DefaultTableModel tableModel;

    public BuchungenGUI() {
        setTitle("Buchungen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);

        // Erstellen der Tabelle
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Filmname");
        tableModel.addColumn("Saal");
        tableModel.addColumn("Platznummer");
        tableModel.addColumn("Uhrzeit/Datum");
        table = new JTable(tableModel);

        // Hinzufügen des Buttons als Spalte zur Tabelle
        TableColumn buttonColumn = new TableColumn(tableModel.getColumnCount());
        buttonColumn.setHeaderValue("Aktion");
        table.addColumn(buttonColumn);

        // Erstellen des Button-Renderers und -Editors
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox());

        // Setzen des Button-Renderers und -Editors für die Spalte
        buttonColumn.setCellRenderer(buttonRenderer);
        buttonColumn.setCellEditor(buttonEditor);

        // Hinzufügen der Tabelle zur ScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // ButtonListener
        buttonEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                int column = table.getColumnCount() - 1; // Letzte Spalte mit Button
                if (row >= 0 && column >= 0) {
                    String filmname = (String) table.getValueAt(row, 0);
                    String saal = (String) table.getValueAt(row, 1);
                    String platznummer = (String) table.getValueAt(row, 2);
                    String uhrzeitDatum = (String) table.getValueAt(row, 3);

                    // Datenbank-Operation zum Löschen der Reservierung
                    deleteReservation(filmname, saal, platznummer, uhrzeitDatum);

                    // Entfernen der Zeile aus der Tabelle
                    tableModel.removeRow(row);
                }
            }
        });
    }

    private void deleteReservation(String filmname, String saal, String platznummer, String uhrzeitDatum) {
        // Code zum Löschen der Reservierung aus der SQLite-Datenbank hier einfügen
        // Verbindung zur Datenbank herstellen, SQL-DELETE-Statement ausführen, usw.
        // Beispielcode:
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:datenbank.db");
            Statement statement = connection.createStatement();
            String sql = "DELETE FROM reservierungen WHERE filmname = ? AND saal = ? AND platznummer = ? AND uhrzeit_datum = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, filmname);
            preparedStatement.setString(2, saal);
            preparedStatement.setString(3, platznummer);
            preparedStatement.setString(4, uhrzeitDatum);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        // Code zum Aktualisieren der Tabelle aus der SQLite-Datenbank hier einfügen
        // Verbindung zur Datenbank herstellen, SQL-SELECT-Statement ausführen, usw.
        // Beispielcode:
        tableModel.setRowCount(0); // Alle vorhandenen Zeilen entfernen
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:datenbank.db");
            Statement statement = connection.createStatement();
            String sql = "SELECT filmname, saal, platznummer, uhrzeit_datum FROM reservierungen";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String filmname = resultSet.getString("filmname");
                String saal = resultSet.getString("saal");
                String platznummer = resultSet.getString("platznummer");
                String uhrzeitDatum = resultSet.getString("uhrzeit_datum");
                Vector<String> row = new Vector<>();
                row.add(filmname);
                row.add(saal);
                row.add(platznummer);
                row.add(uhrzeitDatum);
                tableModel.addRow(row);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
}
