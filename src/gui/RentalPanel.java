package gui;
import pricing.PriceList;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javax.swing.JList;
import javax.swing.ListSelectionModel;
import data.Settings;
import reservation.ExtraService;
import vehicles.VehicleStatus;
import vehicles.Vehicle;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import users.Agent;
import data.PriceListRepository;
import data.ExtraServiceRepository;
import data.RentalRepository;
import data.ReservationRepository;
import data.VehicleRepository;
import reservation.Reservation;
import reservation.ReservationStatus;

public class RentalPanel extends JPanel {

	private ReservationRepository rp;
	private RentalRepository ip;
	private VehicleRepository vp;
	private ExtraServiceRepository dup;
	private PriceListRepository cp;
	private Agent loggedInAgent;
	private JTable table;
	private DefaultTableModel tableModel;

	public RentalPanel(ReservationRepository rp, RentalRepository ip, VehicleRepository vp, ExtraServiceRepository dup, PriceListRepository cp, Agent loggedInAgent) {
		this.rp = rp;
		this.ip = ip;
		this.vp = vp;
		this.dup = dup;
		this.cp = cp;
		this.loggedInAgent = loggedInAgent;

		setLayout(new BorderLayout());

		String[] columns = {"ID Rez.", "Customer", "Vehicle", "Date Od - Do", "Ukupna Price"};
		tableModel = new DefaultTableModel(columns, 0);
		table = new JTable(tableModel);

		refreshTable();

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		JButton btnIzdaj = new JButton("Izdaj Vehicle");

		buttonPanel.add(btnIzdaj);
		add(buttonPanel, BorderLayout.SOUTH);

		btnIzdaj.addActionListener(e -> izdajVehicle());
	}

	private void izdajVehicle() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "You must select an approved reservation!", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		int idReservations = (int) tableModel.getValueAt(selectedRow, 0);
		Reservation r = rp.findReservation(idReservations);

		if (r != null) {


			ArrayList<Vehicle> availableVehicles = new ArrayList<>();
			for (Vehicle v : vp.getVehicles()) {
				if (v.getVehicleModel().getId() == r.getVehicle().getVehicleModel().getId()) {
					if (rp.daLiJeVehicleAvailableno(v, r.getStartDate(), r.getEndDate()) || v.getId() == r.getVehicle().getId()) {
						availableVehicles.add(v);
					}
				}
			}

			if (availableVehicles.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No vehicles are available for rental!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String[] vehicleOptions = new String[availableVehicles.size()];
			for (int i=0; i<availableVehicles.size(); i++) {
				Vehicle v = availableVehicles.get(i);
				vehicleOptions[i] = v.getLicensePlate();
			}


			String selectedVehicleText = (String) JOptionPane.showInputDialog(
					this,
					"Select a specific vehicle:",
					"Vehicle selection",
					JOptionPane.QUESTION_MESSAGE,
					null,
					vehicleOptions,
					vehicleOptions[0]
			);

			if (selectedVehicleText == null) return;

			int selectedVehicleIndex = -1;
			for (int i=0; i<vehicleOptions.length; i++) {
				if (vehicleOptions[i].equals(selectedVehicleText)) {
					selectedVehicleIndex = i; break;
				}
			}

			Vehicle selectedVehicle = availableVehicles.get(selectedVehicleIndex);

			String unos = JOptionPane.showInputDialog(this, "Enter the vehicle starting mileage (" + selectedVehicle.getLicensePlate() + "):");
			if (unos != null && !unos.trim().isEmpty()) {
				try {
					double pocetnaK = Double.parseDouble(unos);
					if (pocetnaK < 0) {
						JOptionPane.showMessageDialog(this, "Mileage cannot be negative!", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}

					Object[] yesNoOptions = {"Da", "Ne"};
					int serviceResponse = JOptionPane.showOptionDialog(this, "Would the customer like to add an extra service?", "Extra services", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, yesNoOptions, yesNoOptions[0]);
					if (serviceResponse == 0) {
						ArrayList<String> options = new ArrayList<>();
						ArrayList<ExtraService> available = new ArrayList<>();
						PriceList dayssnjiPriceList = cp.findVazeciPriceList(LocalDate.now());

						for (ExtraService du : dup.getServices()) {
							boolean vecIma = false;
							for (ExtraService postojeca : r.getExtraServices()) {
								if (postojeca.getId() == du.getId()) vecIma = true;
							}
							if (!vecIma) {
								double currentPrice = 0.0;
								if (dayssnjiPriceList != null && dayssnjiPriceList.getExtraServicePrices() != null && dayssnjiPriceList.getExtraServicePrices().containsKey(du.getId())) {
									currentPrice = dayssnjiPriceList.getExtraServicePrices().get(du.getId());
								}
								options.add(du.getServiceName() + " (" + currentPrice + " RSD)");
								available.add(du);
							}
						}

						if (!options.isEmpty()) {
							JList<String> lista = new JList<>(options.toArray(new String[0]));
							lista.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
							JOptionPane.showMessageDialog(this, new JScrollPane(lista), "Select services", JOptionPane.PLAIN_MESSAGE);

							int[] sel = lista.getSelectedIndices();
							if (sel.length > 0) {
								for (int idx : sel) {
									ExtraService newService = available.get(idx);
									r.getExtraServices().add(newService);

									double currentPrice = 0.0;
									if (dayssnjiPriceList != null && dayssnjiPriceList.getExtraServicePrices() != null && dayssnjiPriceList.getExtraServicePrices().containsKey(newService.getId())) {
										currentPrice = dayssnjiPriceList.getExtraServicePrices().get(newService.getId());
									}

									String serviceName = newService.getServiceName().toLowerCase();
									if (serviceName.contains("extended") || serviceName.contains("extended")) {
										Settings p = new Settings();
										p.load();
										long podrazumevano = p.getDefaultRentalDuration();
										long numberOfDays = ChronoUnit.DAYS.between(r.getStartDate(), r.getEndDate());
										long dodatniDani = numberOfDays - podrazumevano;
										if (dodatniDani > 0) {
											r.setTotalPrice(r.getTotalPrice() + (currentPrice * dodatniDani));
										}
									} else {
										r.setTotalPrice(r.getTotalPrice() + currentPrice);
									}
								}
							}
						} else {
							JOptionPane.showMessageDialog(this, "Sve available services su already selectane.");
						}
					}


					if (r.getVehicle().getId() != selectedVehicle.getId()) {

						r.getVehicle().setVehicleStatus(VehicleStatus.AVAILABLE);

						r.setVehicle(selectedVehicle);
					}


					r.setReservationStatus(ReservationStatus.COMPLETED);
					rp.saveChanges();


					ip.izdajVehicle(r, loggedInAgent, pocetnaK);

					JOptionPane.showMessageDialog(this, "Vehicle successfully izdato customer!");
					refreshTable();

				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(this, "Enter a valid mileage value!", "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	private void refreshTable() {
		tableModel.setRowCount(0);
		for (Reservation r : rp.getReservations()) {

			if (r.getReservationStatus() == ReservationStatus.APPROVED) {
				String customerInfo = r.getCustomer().getFirstName() + " " + r.getCustomer().getLastName();
				String vehicleInfo = r.getVehicle().getVehicleModel().getManufacturer() + " " + r.getVehicle().getVehicleModel().getModelName();
				String period = r.getStartDate() + " do " + r.getEndDate();

				Object[] row = {
					r.getId(),
					customerInfo,
					vehicleInfo,
					period,
					r.getTotalPrice()
				};
				tableModel.addRow(row);
			}
		}
	}
}
