package gui;
import pricing.PriceList;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import users.Customer;
import data.PriceListRepository;

import data.ReservationRepository;
import data.UserRepository;
import reservation.ExtraService;
import reservation.Reservation;
import reservation.ReservationStatus;

public class CustomerMyReservationsPanel extends JPanel {

	private ReservationRepository rp;
	private UserRepository kp;
	private PriceListRepository cp;
	private Customer loggedInCustomer;

	private JTable table;
	private DefaultTableModel tableModel;

	public CustomerMyReservationsPanel(ReservationRepository rp, UserRepository kp, PriceListRepository cp, Customer loggedInCustomer) {
		this.rp = rp;
		this.kp = kp;
		this.cp = cp;
		this.loggedInCustomer = loggedInCustomer;

		setLayout(new BorderLayout());

		String[] columns = {"ID", "Vehicle", "Period", "Rental cost", "Service cost", "Late fees", "Total amount due", "Status"};
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);

		refreshTable();

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnCanceli = new JButton("Cancel reservation");

		buttonPanel.add(btnCanceli);
		add(buttonPanel, BorderLayout.SOUTH);

		btnCanceli.addActionListener(e -> {
			int selectedRow = table.getSelectedRow();
			if (selectedRow == -1) {
				JOptionPane.showMessageDialog(this, "You must select a reservation from the table!", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idReservations = (int) tableModel.getValueAt(selectedRow, 0);
			Reservation r = rp.findReservation(idReservations);

			if (r.getReservationStatus() == ReservationStatus.PENDING || r.getReservationStatus() == ReservationStatus.APPROVED) {
				Object[] options = {"Da", "Ne"};
				int confirmation = JOptionPane.showOptionDialog(this,
						"Are you sure you want to cancel the reservation? (New reservations will be blocked for the next 24 hours)",
						"Confirmation cancelivanja",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[1]);
				if (confirmation == JOptionPane.YES_OPTION) {
					r.setReservationStatus(ReservationStatus.CANCELLED);

					loggedInCustomer.setBookingBlockedUntil(LocalDateTime.now().plusHours(24));
					kp.saveChanges();
					rp.saveChanges();

					JOptionPane.showMessageDialog(this, "Reservation cancelled. New reservations are disabled for 24 hours.");
					refreshTable();
				}
			} else {
				JOptionPane.showMessageDialog(this, "You can cancelati only reservations with status PENDING ili APPROVED.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (Reservation r : rp.getReservations()) {
			if (r.getCustomer().getId() == loggedInCustomer.getId()) {

				String vehicleInfo = r.getVehicle().getVehicleModel().getManufacturer() + " " + r.getVehicle().getVehicleModel().getModelName();
				String period = r.getStartDate() + " do " + r.getEndDate();

				double priceService = 0;
				PriceList cZaR = cp.findVazeciPriceList(r.getStartDate());
				for (ExtraService du : r.getExtraServices()) {
					double cUsl = 0;
					if (cZaR != null && cZaR.getExtraServicePrices() != null && cZaR.getExtraServicePrices().containsKey(du.getId())) {
						cUsl = cZaR.getExtraServicePrices().get(du.getId());
					}

					String name = du.getServiceName().toLowerCase();
					if (name.contains("extended") || name.contains("extended")) {
						long numberOfDays = ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate());
						if (numberOfDays <= 0) numberOfDays = 1;
						priceService += cUsl * numberOfDays;
					} else {
						priceService += cUsl;
					}
				}

				double originalPrice = 0;
				if (cZaR != null) {
					originalPrice = rp.calculateTotalPrice(r, cZaR);
				} else {
					originalPrice = r.getTotalPrice();
				}

				double lateFee = r.getTotalPrice() - originalPrice;
				if (lateFee < 0 || r.getReservationStatus() != ReservationStatus.COMPLETED) {
					lateFee = 0;
				}

				double rentalPrice = originalPrice - priceService;
				if (rentalPrice < 0) rentalPrice = 0;

				Object[] row = {
					r.getId(),
					vehicleInfo,
					period,
					String.format("%.2f RSD", rentalPrice),
					String.format("%.2f RSD", priceService),
					String.format("%.2f RSD", lateFee),
					String.format("%.2f RSD", r.getTotalPrice()),
					r.getReservationStatus()
				};
				tableModel.addRow(row);
			}
		}
	}
}
