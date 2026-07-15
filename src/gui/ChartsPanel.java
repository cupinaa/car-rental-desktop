package gui;
import java.time.temporal.ChronoUnit;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.CategorySeries.CategorySeriesRenderStyle;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler.LegendPosition;

import rental.Rental;
import users.Agent;
import users.Customer;
import users.User;
import data.RentalRepository;
import data.UserRepository;
import data.SubscriptionRepository;
import data.ReservationRepository;
import reservation.Reservation;

public class ChartsPanel extends JPanel {

	private RentalRepository ip;
	private ReservationRepository rp;
	private SubscriptionRepository pp;
	private UserRepository kp;

	public ChartsPanel(RentalRepository ip, ReservationRepository rp, SubscriptionRepository pp, UserRepository kp) {
		this.ip = ip;
		this.rp = rp;
		this.pp = pp;
		this.kp = kp;

		setLayout(new BorderLayout());

		JPanel mainPanel = new JPanel(new BorderLayout());


		JPanel revenuePanel = createRevenueChart();
		mainPanel.add(revenuePanel, BorderLayout.NORTH);


		JPanel bottomPanel = new JPanel(new GridLayout(1, 2));
		bottomPanel.add(createStatusiChart());
		bottomPanel.add(createAgentiChart());

		mainPanel.add(bottomPanel, BorderLayout.CENTER);

		JScrollPane scroll = new JScrollPane(mainPanel);
		add(scroll, BorderLayout.CENTER);
	}

	private JPanel createRevenueChart() {
		LocalDate start = LocalDate.now().minusMonths(11).withDayOfMonth(1);

		List<String> xData = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
		for (int i = 0; i < 12; i++) {
			xData.add(start.plusMonths(i).format(formatter));
		}

		double[] studentData = new double[12];
		double[] firmaData = new double[12];
		double[] penzionerData = new double[12];
		double[] bezKategorijeData = new double[12];
		double[] totalData = new double[12];

		for (Rental i : ip.getRentals()) {
			LocalDate date = i.getReservation().getStartDate();
			if (!date.isBefore(start)) {
				int index = (int) ChronoUnit.MONTHS.between(start.withDayOfMonth(1), date.withDayOfMonth(1));
				if (index >= 0 && index < 12) {
					double price = i.getReservation().getTotalPrice();
					totalData[index] += price;

					Customer k = i.getReservation().getCustomer();
					String kat = k.getCustomerCategory() != null ? k.getCustomerCategory().toString() : "STANDARDNI";
					if (kat.equals("STUDENT")) studentData[index] += price;
					else if (kat.equals("COMPANY")) firmaData[index] += price;
					else if (kat.equals("RETIREE")) penzionerData[index] += price;
					else bezKategorijeData[index] += price;
				}
			}
		}

		CategoryChart chart = new CategoryChartBuilder().width(800).height(400).title("Revenue po kategoriji customera").build();
		chart.getStyler().setDefaultSeriesRenderStyle(CategorySeriesRenderStyle.Line);
		chart.getStyler().setLegendPosition(LegendPosition.OutsideS);



		boolean hasAnyData = false;
		for(double d : totalData) if(d > 0) hasAnyData = true;

		if (!hasAnyData) {
			for(int i=0; i<12; i++) totalData[i] = 0.001;
		}

		chart.addSeries("Student", xData, toList(studentData));
		chart.addSeries("Firma", xData, toList(firmaData));
		chart.addSeries("Penzioner", xData, toList(penzionerData));
		chart.addSeries("Bez kategorije", xData, toList(bezKategorijeData));
		chart.addSeries("Total", xData, toList(totalData));

		return new XChartPanel<>(chart);
	}

	private JPanel createAgentiChart() {
		LocalDate oneMonthAgo = LocalDate.now().minusDays(30);

		HashMap<String, Integer> rentalsByAgent = new HashMap<>();
		for (User k : kp.getUsers()) {
			if (k instanceof Agent) {
				rentalsByAgent.put(k.getFirstName() + " " + k.getLastName(), 0);
			}
		}

		for (Rental i : ip.getRentals()) {
			if (!i.getReservation().getStartDate().isBefore(oneMonthAgo)) {
				String agentName = i.getAgent().getFirstName() + " " + i.getAgent().getLastName();
				rentalsByAgent.put(agentName, rentalsByAgent.getOrDefault(agentName, 0) + 1);
			}
		}

		PieChart chart = new PieChartBuilder().width(400).height(350).title("Agent workload u prethodnih 30 days").build();
		chart.getStyler().setLegendPosition(LegendPosition.OutsideS);

		boolean imaPodataka = false;
		for (String a : rentalsByAgent.keySet()) {
			if (rentalsByAgent.get(a) > 0) {
				chart.addSeries(a, rentalsByAgent.get(a));
				imaPodataka = true;
			}
		}

		if (!imaPodataka) {
			chart.addSeries("No reservation", 1);
		}

		return new XChartPanel<>(chart);
	}

	private JPanel createStatusiChart() {
		LocalDate oneMonthAgo = LocalDate.now().minusDays(30);

		int confirmed = 0, rejectede = 0, cancelane = 0, naPending = 0;
		for (Reservation r : rp.getReservations()) {
			if (!r.getStartDate().isBefore(oneMonthAgo)) {
				String sName = r.getReservationStatus().name();
				if (sName.equals("APPROVED") || sName.equals("COMPLETED")) {
					confirmed++;
				} else if (sName.equals("REJECTED")) {
					rejectede++;
				} else if (sName.equals("CANCELLED")) {
					cancelane++;
				} else if (sName.contains("EKANJU") || sName.contains("CEKANJU")) {
					naPending++;
				}
			}
		}

		PieChart chart = new PieChartBuilder().width(400).height(350).title("Status reservation u prethodnih 30 days").build();
		chart.getStyler().setLegendPosition(LegendPosition.OutsideS);

		if (confirmed > 0) chart.addSeries("CONFIRMED", confirmed);
		if (rejectede > 0) chart.addSeries("REJECTED", rejectede);
		if (cancelane > 0) chart.addSeries("CANCELLED", cancelane);
		if (naPending > 0) chart.addSeries("PENDING", naPending);

		if (confirmed == 0 && rejectede == 0 && cancelane == 0 && naPending == 0) {
			chart.addSeries("No data", 1);
		}

		return new XChartPanel<>(chart);
	}

	private List<Double> toList(double[] arr) {
		List<Double> list = new ArrayList<>();
		for (double d : arr) list.add(d);
		return list;
	}
}
