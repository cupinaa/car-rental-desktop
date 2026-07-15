package gui;
import javax.swing.JOptionPane;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import data.ReservationRepository;
import reservation.Reservation;
import reservation.ReservationStatus;

public class ReservationsPanel extends JPanel {

	private ReservationRepository rp;
	private JTable table;
	private DefaultTableModel tableModel;
	private users.User loggedInUser;

	public ReservationsPanel(ReservationRepository rp, users.User loggedInUser) {
		this.rp = rp;
		this.loggedInUser = loggedInUser;
		setLayout(new BorderLayout());

		String[] columns = {"ID", "Customer", "Vehicle", "Od - Do", "Price", "Status"};
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);

		refreshTable();

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnApprove = new JButton("Approve");
		JButton btnOdbij = new JButton("Odbij");

		buttonPanel.add(btnApprove);
		buttonPanel.add(btnOdbij);
		add(buttonPanel, BorderLayout.SOUTH);

		btnApprove.addActionListener(e -> promeniStatus(ReservationStatus.APPROVED));
		btnOdbij.addActionListener(e -> promeniStatus(ReservationStatus.REJECTED));

		if (loggedInUser instanceof users.Administrator) {
			JButton btnEdit = new JButton("Edit");
			JButton btnDelete = new JButton("Delete");
			buttonPanel.add(btnEdit);
			buttonPanel.add(btnDelete);

			btnEdit.addActionListener(e -> {
				int row = table.getSelectedRow();
				if (row == -1) { JOptionPane.showMessageDialog(this, "Select a reservation."); return; }
				int id = (int) tableModel.getValueAt(row, 0);
				Reservation r = rp.findReservation(id);
				if (r != null) { new ReservationDialog(rp, r, this::refreshTable).setVisible(true); }
			});

			btnDelete.addActionListener(e -> {
				int row = table.getSelectedRow();
				if (row == -1) { JOptionPane.showMessageDialog(this, "Select a reservation."); return; }
				int id = (int) tableModel.getValueAt(row, 0);
				Reservation r = rp.findReservation(id);
				if (r != null && JOptionPane.showConfirmDialog(this, "Sigurno?", "Confirmation", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
					rp.getReservations().remove(r);
					try { rp.write("reservations.csv"); refreshTable(); } catch (Exception ex) {}
				}
			});
		}
	}

	private void promeniStatus(ReservationStatus newStatus) {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select a reservation!", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int idReservations = (int) tableModel.getValueAt(selectedRow, 0);
		Reservation r = rp.findReservation(idReservations);

		if (r != null) {
			if (r.getReservationStatus() != ReservationStatus.PENDING) {
				JOptionPane.showMessageDialog(this, "Only pending reservations can change status.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}


			if (newStatus == ReservationStatus.APPROVED) {
				if (!rp.daLiJeVehicleAvailableno(r.getVehicle(), r.getStartDate(), r.getEndDate(), r.getId())) {
					JOptionPane.showMessageDialog(this, "The vehicle is already booked for this period. The reservation must be rejected.", "Error", JOptionPane.ERROR_MESSAGE);
					rp.promeniReservationStatus(r, ReservationStatus.REJECTED);
					refreshTable();
					return;
				}
			}

			rp.promeniReservationStatus(r, newStatus);
			refreshTable();
			JOptionPane.showMessageDialog(this, "Status reservations successfully changed u " + newStatus + ".");
		}
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (Reservation r : rp.getReservations()) {
			String customerInfo = r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName();
			String vehicleInfo = r.getVehicle().getVehicleModel().getManufacturer() + " " + r.getVehicle().getVehicleModel().getModelName();
			String period = r.getStartDate() + " do " + r.getEndDate();

			Object[] row = {
				r.getId(),
				customerInfo,
				vehicleInfo,
				period,
				r.getTotalPrice(),
				r.getReservationStatus().toString()
			};
			tableModel.addRow(row);
		}
	}
}
