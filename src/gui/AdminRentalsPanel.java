package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import rental.Rental;
import data.RentalRepository;

public class AdminRentalsPanel extends JPanel {

	private RentalRepository ip;
	private JTable table;
	private DefaultTableModel tableModel;

	public AdminRentalsPanel(RentalRepository ip) {
		this.ip = ip;
		setLayout(new BorderLayout());

		String[] columns = {"ID", "Reservation", "Agent", "Pocetna KM", "Endnja KM"};
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);

		refreshTable();

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnEdit = new JButton("Edit");
		JButton btnDelete = new JButton("Delete");

		buttonPanel.add(btnEdit);
		buttonPanel.add(btnDelete);
		add(buttonPanel, BorderLayout.SOUTH);

		btnEdit.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Select a rental to edit.");
				return;
			}
			int idRentals = (int) tableModel.getValueAt(selectedRow, 0);
			Rental i = ip.getRentals().stream().filter(izd -> izd.getId() == idRentals).findFirst().orElse(null);
			if (i != null) {
				RentalDialog iff = new RentalDialog(ip, i, this::refreshTable);
				iff.setVisible(true);
			}
		});

		btnDelete.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "Select a rental to delete.");
				return;
			}
			int idRentals = (int) tableModel.getValueAt(selectedRow, 0);
			Rental i = ip.getRentals().stream().filter(izd -> izd.getId() == idRentals).findFirst().orElse(null);
			if (i != null) {
				int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirmation", JOptionPane.YES_NO_OPTION);
				if (confirmation == JOptionPane.YES_OPTION) {
					ip.getRentals().remove(i);
					try {
						ip.write("rentals.csv");
						refreshTable();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(this, "Error pri brisanju.");
					}
				}
			}
		});
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (Rental i : ip.getRentals()) {
			Object[] row = { i.getId(), i.getReservation().getId(), i.getAgent().getFirstName(), i.getStartingMileage(), i.getEndingMileage() };
			tableModel.addRow(row);
		}
	}
}
