package gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import rental.Rental;
import data.PriceListRepository;
import data.RentalRepository;
import data.UserRepository;
import data.ReservationRepository;

public class ReturnsPanel extends JPanel {

	private RentalRepository ip;
	private ReservationRepository rp;
	private UserRepository kp;
	private PriceListRepository cp;

	private JTable table;
	private DefaultTableModel tableModel;

	public ReturnsPanel(RentalRepository ip, ReservationRepository rp, UserRepository kp, PriceListRepository cp) {
		this.ip = ip;
		this.rp = rp;
		this.kp = kp;
		this.cp = cp;

		setLayout(new BorderLayout());

		String[] columns = {"ID Rentals", "Customer", "Vehicle", "Expected end", "Starting mileage"};
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);

		refreshTable();

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnReturn = new JButton("Evidentiraj Return");

		buttonPanel.add(btnReturn);
		add(buttonPanel, BorderLayout.SOUTH);

		btnReturn.addActionListener(e -> otvoriReturn());
	}

	private void otvoriReturn() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select an active rental!", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int idRentals = (int) tableModel.getValueAt(selectedRow, 0);
		Rental i = null;
		for (Rental iz : ip.getRentals()) {
			if (iz.getId() == idRentals) {
				i = iz;
				break;
			}
		}

		if (i != null) {
			ReturnDialog vf = new ReturnDialog(ip, rp, kp, cp, i, this::refreshTable);
			vf.setVisible(true);
		}
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (Rental i : ip.getRentals()) {

			if (i.getEndingMileage() == 0.0) {
				String customerInfo = i.getReservation().getCustomer().getFirstName() + " " + i.getReservation().getCustomer().getLastName();
				String vehicleInfo = i.getReservation().getVehicle().getVehicleModel().getManufacturer() + " " + i.getReservation().getVehicle().getVehicleModel().getModelName();

				Object[] row = {
					i.getId(),
					customerInfo,
					vehicleInfo,
					i.getReservation().getEndDate().toString(),
					i.getStartingMileage()
				};
				tableModel.addRow(row);
			}
		}
	}
}
