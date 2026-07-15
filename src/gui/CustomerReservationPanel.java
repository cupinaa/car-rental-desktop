package gui;
import java.awt.GridLayout;
import java.time.temporal.ChronoUnit;
import data.Settings;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import pricing.PriceList;
import users.Customer;
import data.PriceListRepository;
import data.ExtraServiceRepository;
import data.VehicleModelRepository;
import data.SubscriptionRepository;
import data.ReservationRepository;
import data.VehicleRepository;
import reservation.ExtraService;
import reservation.Reservation;
import vehicles.VehicleCategory;
import vehicles.VehicleModel;
import vehicles.Vehicle;

public class CustomerReservationPanel extends JPanel {

	private ReservationRepository rp;
	private VehicleModelRepository mp;
	private VehicleRepository vp;
	private ExtraServiceRepository dup;
	private PriceListRepository cp;
	private SubscriptionRepository pp;
	private Customer customer;

	private JTextField txtManufacturer;
	private JTextField txtNameModela;
	private JComboBox<String> cbCategory;
	private JTextField txtDateOd;
	private JTextField txtDateDo;

	private JTable tableModela;
	private DefaultTableModel tableModel;

	private JList<String> listServices;
	private JButton bookButton;

	public CustomerReservationPanel(ReservationRepository rp, VehicleModelRepository mp, VehicleRepository vp, ExtraServiceRepository dup, PriceListRepository cp, SubscriptionRepository pp, Customer customer) {
		this.rp = rp;
		this.mp = mp;
		this.vp = vp;
		this.dup = dup;
		this.cp = cp;
		this.pp = pp;
		this.customer = customer;

		setLayout(new BorderLayout(10, 10));

		if (customer.podZabranom()) {
			showZabranu();
			return;
		}

		createTopFilterPanel();
		createCenterTabeluPanel();
		createEastExtraServicesPanel();
	}

	private void showZabranu() {
		setLayout(new BorderLayout());
		JLabel blockedMessageLabel = new JLabel("Access denied. A recent cancellation blocks new reservations for 24 hours. Blocked until: " + customer.getBookingBlockedUntil().toString(), SwingConstants.CENTER);
		add(blockedMessageLabel, BorderLayout.CENTER);
	}

	private void createTopFilterPanel() {
		JPanel filterPanel = new JPanel(new GridLayout(3, 4, 5, 5));

		filterPanel.add(new JLabel("Manufacturer (Manufacturer):"));
		txtManufacturer = new JTextField();
		filterPanel.add(txtManufacturer);

		filterPanel.add(new JLabel("Model:"));
		txtNameModela = new JTextField();
		filterPanel.add(txtNameModela);

		filterPanel.add(new JLabel("Category:"));
		cbCategory = new JComboBox<>();
		cbCategory.addItem("SVE KATEGORIJE");
		for (VehicleCategory kat : VehicleCategory.values()) {
			cbCategory.addItem(kat.toString());
		}
		filterPanel.add(cbCategory);

		filterPanel.add(new JLabel("Date od (YYYY-MM-DD):"));
		txtDateOd = new JTextField(LocalDate.now().plusDays(1).toString());
		filterPanel.add(txtDateOd);

		filterPanel.add(new JLabel("Date do (YYYY-MM-DD):"));
		txtDateDo = new JTextField(LocalDate.now().plusDays(4).toString());
		filterPanel.add(txtDateDo);

		JButton btnPretrazi = new JButton("Search");
		filterPanel.add(new JLabel(""));
		filterPanel.add(btnPretrazi);

		btnPretrazi.addActionListener(e -> izvrsiPretragu());

		add(filterPanel, BorderLayout.NORTH);
	}

	private void createCenterTabeluPanel() {
		String[] columns = {"ID Modela", "Manufacturer", "Model", "Category"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableModela = new JTable(tableModel);

		tableModela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(tableModela);
		scrollPane.setBorder(BorderFactory.createTitledBorder("Dostupni modeli za selectani period"));

		add(scrollPane, BorderLayout.CENTER);
	}

	private void createEastExtraServicesPanel() {
		JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
		rightPanel.setPreferredSize(new Dimension(250, 0));

		String[] serviceNames = new String[dup.getServices().size()];
		PriceList currentPriceList = cp.findVazeciPriceList(LocalDate.now());
		for (int i = 0; i < dup.getServices().size(); i++) {
			ExtraService du = dup.getServices().get(i);
			double servicePrice = 0.0;
			if (currentPriceList != null && currentPriceList.getExtraServicePrices() != null && currentPriceList.getExtraServicePrices().containsKey(du.getId())) {
				servicePrice = currentPriceList.getExtraServicePrices().get(du.getId());
			}
			serviceNames[i] = du.getId() + " - " + du.getServiceName() + " (" + servicePrice + " RSD)";
		}
		listServices = new JList<>(serviceNames);
		listServices.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		JScrollPane scrollList = new JScrollPane(listServices);
		scrollList.setBorder(BorderFactory.createTitledBorder("Extra services (hold Ctrl to select multiple)"));
		rightPanel.add(scrollList, BorderLayout.CENTER);

		bookButton = new JButton("Book odabrano");
		bookButton.addActionListener(e -> confirmReservation());

		rightPanel.add(bookButton, BorderLayout.SOUTH);

		add(rightPanel, BorderLayout.EAST);
	}

	private void izvrsiPretragu() {
		tableModel.setRowCount(0);

		try {
			LocalDate start = LocalDate.parse(txtDateOd.getText());
			LocalDate end = LocalDate.parse(txtDateDo.getText());

			if (start.isBefore(LocalDate.now())) {
				JOptionPane.showMessageDialog(this, "The start date cannot be in the past!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (end.isBefore(start)) {
				JOptionPane.showMessageDialog(this, "The end date must be after the start date!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String filterManufacturer = txtManufacturer.getText().trim().toLowerCase();
			String filterModel = txtNameModela.getText().trim().toLowerCase();
			String filterKat = (String) cbCategory.getSelectedItem();

			for (VehicleModel m : mp.getModels()) {

				if (!filterManufacturer.isEmpty() && !m.getManufacturer().toLowerCase().contains(filterManufacturer)) continue;
				if (!filterModel.isEmpty() && !m.getModelName().toLowerCase().contains(filterModel)) continue;
				if (!filterKat.equals("SVE KATEGORIJE") && !m.getCategory().toString().equals(filterKat)) continue;


				Vehicle availableVehicle = rp.findAvailablenoVehicleZaModel(m, vp, start, end);

				if (availableVehicle != null) {

					Object[] row = {m.getId(), m.getManufacturer(), m.getModelName(), m.getCategory()};
					tableModel.addRow(row);
				}
			}

			if (tableModel.getRowCount() == 0) {
				JOptionPane.showMessageDialog(this, "No dostupnih vehicles za zadate kriterijume pretrage i period.", "Information", JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (DateTimeParseException ex) {
			JOptionPane.showMessageDialog(this, "Incorrect format datea! Koristite YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void confirmReservation() {
		int selectedRow = tableModela.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please vas izaberite model from the table sa leve strane!", "Warning", JOptionPane.WARNING_MESSAGE);
			return;
		}

		try {
			LocalDate start = LocalDate.parse(txtDateOd.getText());
			LocalDate end = LocalDate.parse(txtDateDo.getText());

			long numberOfDays = ChronoUnit.DAYS.between(start, end);
			Settings pod = new Settings();
			pod.load();
			long maximumDays = pod.getDefaultRentalDuration();

			ArrayList<ExtraService> selectedServices = new ArrayList<>();
			int[] selektovaniIndeksi = listServices.getSelectedIndices();
			boolean hasExtension = false;
			for (int i : selektovaniIndeksi) {
				ExtraService du = dup.getServices().get(i);
				selectedServices.add(du);
				if (du.getServiceName().toLowerCase().contains("extend") || du.getServiceName().toLowerCase().contains("extend")) {
					hasExtension = true;
				}
			}

			if (!hasExtension && numberOfDays > maximumDays) {
				JOptionPane.showMessageDialog(this, "The standard rental duration is limited to " + maximumDays + " days.\nSelect the extended-use service to keep the vehicle longer.", "Warning", JOptionPane.WARNING_MESSAGE);
				return;
			}

			int idModela = (int) tableModel.getValueAt(selectedRow, 0);
			VehicleModel model = mp.findModel(idModela);


			Vehicle availableVehicle = rp.findAvailablenoVehicleZaModel(model, vp, start, end);
			if (availableVehicle == null) {
				JOptionPane.showMessageDialog(this, "The vehicle is no longer available; refresh the search.", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Reservation privremena = new Reservation(customer, availableVehicle, start, end, selectedServices);
			PriceList currentPriceList = cp.findVazeciPriceList(start);
			double totalPrice = rp.calculateTotalPrice(privremena, currentPriceList);

			if (totalPrice == 0.0) {
				JOptionPane.showMessageDialog(this, "PriceList za selected date nije definisan!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			String message = "The total rental price after discounts is: " + totalPrice + " RSD\nWould you like to confirm the reservation?";
			Object[] options = {"Da", "Ne"};
			int odziv = JOptionPane.showOptionDialog(this, message, "Confirmation reservations", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

			if (odziv == JOptionPane.YES_OPTION) {
				Reservation newReservation = rp.createReservation(customer, availableVehicle, start, end, selectedServices, currentPriceList, pp);
				if (newReservation != null) {
					JOptionPane.showMessageDialog(this, "Reservation successfully created! Your request je with status: PENDING.");
					tableModel.setRowCount(0);
				} else {
					JOptionPane.showMessageDialog(this, "Your request was rejected. Check that your subscription is active and your driving license is at least two years old.", "Reservation rejected", JOptionPane.ERROR_MESSAGE);
				}
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error while kreiranja reservations.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
