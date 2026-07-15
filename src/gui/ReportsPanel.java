package gui;
import pricing.PriceList;
import java.time.temporal.ChronoUnit;
import javax.swing.JOptionPane;
import reservation.ReservationStatus;

import java.awt.BorderLayout;
import java.awt.Font;
import java.time.LocalDate;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import rental.Rental;
import users.Agent;
import users.User;
import users.Subscription;
import users.SubscriptionStatus;
import users.Employee;
import data.PriceListRepository;
import data.RentalRepository;
import data.UserRepository;
import data.VehicleModelRepository;
import data.SubscriptionRepository;
import data.ReservationRepository;
import reservation.Reservation;
import vehicles.VehicleModel;

public class ReportsPanel extends JPanel {

	private RentalRepository ip;
	private ReservationRepository rp;
	private VehicleModelRepository mp;
	private UserRepository kp;
	private SubscriptionRepository pp;
	private PriceListRepository cp;

	private JTextField txtDateOd;
	private JTextField txtDateDo;

	private JTextArea txtRentals;
	private JTextArea txtReservations;
	private JTextArea txtModeli;
	private JTextArea txtRevenue;

	public ReportsPanel(RentalRepository ip, ReservationRepository rp, VehicleModelRepository mp, UserRepository kp, SubscriptionRepository pp, PriceListRepository cp) {
		this.ip = ip;
		this.rp = rp;
		this.mp = mp;
		this.kp = kp;
		this.pp = pp;
		this.cp = cp;

		setLayout(new BorderLayout());

		JPanel topPanel = new JPanel();
		topPanel.add(new JLabel("Od datea (YYYY-MM-DD):"));
		txtDateOd = new JTextField(LocalDate.now().minusMonths(1).toString(), 10);
		topPanel.add(txtDateOd);
		topPanel.add(new JLabel("Do datea:"));
		txtDateDo = new JTextField(LocalDate.now().toString(), 10);
		topPanel.add(txtDateDo);
		JButton btnShow = new JButton("Generate reports");
		topPanel.add(btnShow);
		add(topPanel, BorderLayout.NORTH);

		JTabbedPane tabbedPane = new JTabbedPane();

		txtRentals = createTextArea();
		tabbedPane.addTab("Rentals po Agentu", new JScrollPane(txtRentals));

		txtReservations = createTextArea();
		tabbedPane.addTab("Reservations", new JScrollPane(txtReservations));

		txtModeli = createTextArea();
		tabbedPane.addTab("Modeli Vozila", new JScrollPane(txtModeli));

		txtRevenue = createTextArea();
		tabbedPane.addTab("Revenue i Expenses", new JScrollPane(txtRevenue));

		add(tabbedPane, BorderLayout.CENTER);

		btnShow.addActionListener(e -> generateSve());
	}

	private JTextArea createTextArea() {
		JTextArea ta = new JTextArea();
		ta.setEditable(false);
		ta.setFont(new Font("Monospaced", Font.PLAIN, 14));
		return ta;
	}

	private void generateSve() {
		try {
			LocalDate odDatea = LocalDate.parse(txtDateOd.getText());
			LocalDate doDatea = LocalDate.parse(txtDateDo.getText());

			generateRentals(odDatea, doDatea);
			generateReservations(odDatea, doDatea);
			generateModele(odDatea, doDatea);
			generatePrihodeRashode(odDatea, doDatea);

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Invalid format datea. Koristite YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void generateRentals(LocalDate odDatea, LocalDate doDatea) {
		HashMap<Integer, Integer> rentalsByAgent = new HashMap<>();
		for (User k : kp.getUsers()) {
			if (k instanceof Agent) rentalsByAgent.put(k.getId(), 0);
		}

		for (Rental i : ip.getRentals()) {
			LocalDate d = i.getReservation().getStartDate();
			if (!d.isBefore(odDatea) && !d.isAfter(doDatea)) {
				int agentId = i.getAgent().getId();
				rentalsByAgent.put(agentId, rentalsByAgent.getOrDefault(agentId, 0) + 1);
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Number izdatih vehicles po agentu u periodu ").append(odDatea).append(" do ").append(doDatea).append(":\n\n");
		for (Integer agentId : rentalsByAgent.keySet()) {
			Agent a = kp.findAgent(agentId);
			if (a != null) {
				sb.append(a.getFirstName()).append(" ").append(a.getLastName()).append(" (ID: ").append(agentId).append(") -> ")
				  .append(rentalsByAgent.get(agentId)).append(" izdata vehicles\n");
			}
		}
		txtRentals.setText(sb.toString());
	}

	private void generateReservations(LocalDate odDatea, LocalDate doDatea) {
		int confirmed = 0, rejectede = 0, cancelane = 0, naPending = 0;
		for (Reservation r : rp.getReservations()) {
			if (!r.getStartDate().isBefore(odDatea) && !r.getStartDate().isAfter(doDatea)) {
				switch (r.getReservationStatus()) {
				case APPROVED:
				case COMPLETED:
					confirmed++; break;
				case REJECTED:
					rejectede++; break;
				case CANCELLED:
					cancelane++; break;
				case PENDING:
					naPending++; break;
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		sb.append("Statusi reservation u periodu ").append(odDatea).append(" do ").append(doDatea).append(":\n\n");
		sb.append("Confirmed/completed: ").append(confirmed).append("\n");
		sb.append("Odbijene: ").append(rejectede).append("\n");
		sb.append("Cancelane: ").append(cancelane).append("\n");
		sb.append("Pending: ").append(naPending).append("\n");
		txtReservations.setText(sb.toString());
	}

	private void generateModele(LocalDate odDatea, LocalDate doDatea) {
		StringBuilder sb = new StringBuilder();
		sb.append("Statistika po modelima u periodu ").append(odDatea).append(" do ").append(doDatea).append(":\n\n");

		for (VehicleModel m : mp.getModels()) {
			int numberReservation = 0;
			int numberRentals = 0;

			for (Reservation r : rp.getReservations()) {
				if (r.getVehicle().getVehicleModel().getId() == m.getId()) {
					if (!r.getStartDate().isBefore(odDatea) && !r.getStartDate().isAfter(doDatea)) {
						numberReservation++;
						if (r.getReservationStatus() == ReservationStatus.COMPLETED) {
							numberRentals++;
						}
					}
				}
			}

			sb.append(m.getManufacturer()).append(" ").append(m.getModelName())
			  .append(" [").append(m.getCategory()).append("]\n")
			  .append("   Number reservation: ").append(numberReservation).append("\n")
			  .append("   Number rentals: ").append(numberRentals).append("\n\n");
		}
		txtModeli.setText(sb.toString());
	}

	private void generatePrihodeRashode(LocalDate odDatea, LocalDate doDatea) {
		double revenueNajmovi = 0;
		for (Rental i : ip.getRentals()) {
			LocalDate d = i.getReservation().getStartDate();
			if (!d.isBefore(odDatea) && !d.isAfter(doDatea)) {
				revenueNajmovi += i.getReservation().getTotalPrice();
			}
		}

		double revenueSubscriptions = 0;
		for (Subscription p : pp.getSubscriptions()) {
			if (p.getStatus() == SubscriptionStatus.ACTIVE) {
				LocalDate paymentDate = p.getExpiryDate().minusYears(1);
				if (!paymentDate.isBefore(odDatea) && !paymentDate.isAfter(doDatea)) {
					PriceList vazeci = cp.findVazeciPriceList(paymentDate);
					if (vazeci != null) {
						revenueSubscriptions += vazeci.getSubscriptionPrice();
					}
				}
			}
		}


		double expensesPlate = 0;
		long numberOfDays = ChronoUnit.DAYS.between(odDatea, doDatea);
		if (numberOfDays <= 0) numberOfDays = 1;

		for (User k : kp.getUsers()) {
			if (k instanceof Employee) {
				double dnevnica = ((Employee)k).getSalary() / 30.0;
				expensesPlate += (dnevnica * numberOfDays);
			}
		}

		double totalRevenue = revenueNajmovi + revenueSubscriptions;

		StringBuilder sb = new StringBuilder();
		sb.append("Finansijski bilans u periodu ").append(odDatea).append(" do ").append(doDatea).append(":\n\n");
		sb.append("PRIHODI OD NAJMOVA (Najmovi, services, kazne): ").append(String.format("%.2f RSD", revenueNajmovi)).append("\n");
		sb.append("SUBSCRIPTION REVENUE: ").append(String.format("%.2f RSD", revenueSubscriptions)).append("\n");
		sb.append("UKUPNI PRIHODI: ").append(String.format("%.2f RSD", totalRevenue)).append("\n");
		sb.append("UKUPNI RASHODI (Plate employeesh - za ").append(numberOfDays).append(" days): ").append(String.format("%.2f RSD", expensesPlate)).append("\n");
		sb.append("----------------------------------------------------\n");
		sb.append("PROFIT: ").append(String.format("%.2f RSD", totalRevenue - expensesPlate)).append("\n");

		txtRevenue.setText(sb.toString());
	}
}
