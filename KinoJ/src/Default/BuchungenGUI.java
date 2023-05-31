package Default;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import Database.IDatabaseAdapter;
import Database.SQLiteAdapter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;
import UserManager.User;

public class BuchungenGUI extends JFrame {

	private JTable table;
    private DefaultTableModel tableModel;
    private User user;
    IDatabaseAdapter adapter;

    public BuchungenGUI(User user) {
    	this.user = user;
    	adapter = SQLiteAdapter.getInstance();
        setTitle("Reservierungen");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 400);
        this.setLocationRelativeTo(null);

        // Erstellen der Tabelle
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Filmname");
        tableModel.addColumn("Saal");
        tableModel.addColumn("Platznummer");
        tableModel.addColumn("Uhrzeit");
        tableModel.addColumn("Datum");
        table = new JTable(tableModel);

        // HinzufÃ¼gen des Buttons als Spalte zur Tabelle
        TableColumn buttonColumn = new TableColumn();
        buttonColumn.setHeaderValue("Aktion");
        table.addColumn(buttonColumn);
        
        // Erstellen des Button-Renderers und -Editors
        ButtonRenderer buttonRenderer = new ButtonRenderer();
        ButtonEditor buttonEditor = new ButtonEditor(new JCheckBox());

        // Setzen des Button-Renderers und -Editors fï¿½r die Spalte
        buttonColumn.setCellRenderer(buttonRenderer);
        buttonColumn.setCellEditor(buttonEditor);

        // Hinzufï¿½gen der Tabelle zur ScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // ButtonListener
        buttonEditor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                int column = table.getColumnCount() - 1; // Letzte Spalte mit Button
                if (row >= 0 && column >= 0) {
                    adapter.deleteReservation(Integer.parseInt(table.getValueAt(row, 0).toString()));
                    tableModel.removeRow(row);
                    tableModel.fireTableDataChanged();
                }
            }
        });
        this.refreshTable();
        this.setVisible(true);
    }

    public void refreshTable() {
        tableModel.setRowCount(0); // Alle vorhandenen Zeilen entfernen
        ResultSet resultSet = adapter.getReservierungen(user);       
        try {
		    while (resultSet.next()) {
			    int id = resultSet.getInt("ReservierungsID");
				String filmname = resultSet.getString("Name");
			    int saal = resultSet.getInt("Saal");
			    String platznummer = resultSet.getString("Seat");
			    String uhrzeit = resultSet.getString("Uhrzeit");
			    String datum = resultSet.getString("Datum");

			    Vector<String> row = new Vector<>();
			    row.add(Integer.toString(id));
			    row.add(filmname);
			    row.add(Integer.toString(saal));
			    row.add(platznummer);
			    row.add(uhrzeit);
			    row.add(datum);
			    tableModel.addRow(row);
			}
			resultSet.close();
            
        } catch (SQLException e) {
			e.printStackTrace();
		}
    }
	
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setForeground(table.getSelectionForeground());
                setBackground(table.getSelectionBackground());
            } else {
                setForeground(table.getForeground());
                setBackground(UIManager.getColor("Button.background"));
            }
            setText("Löschen");
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
    	protected JButton button;
    	private String label;
    	private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if (isSelected) {
                button.setForeground(table.getSelectionForeground());
                button.setBackground(table.getSelectionBackground());
            } else {
                button.setForeground(table.getForeground());
                button.setBackground(table.getBackground());
            }
            label = (value == null) ? "" : value.toString();
            button.setText("Löschen");
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
            isPushed = true;
            return button;
        }

        public Object getCellEditorValue() {
            if (isPushed) {
                // Aktion ausführen, wenn Button geklickt wurde
            }
            isPushed = false;
            return label;
        }

        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        protected void fireEditingStopped() {
            //super.fireEditingStopped();
        }

        public void addActionListener(ActionListener listener) {
            button.addActionListener(listener);
        }
    }
}
