package gui;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import data.Settings;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import users.Administrator;
import users.Agent;
import users.Customer;
import users.User;
import data.*;

public class MainWindow extends JFrame {

	private User loggedInUser;

	private UserRepository kp;
	private VehicleModelRepository mp;
	private VehicleRepository vp;
	private ExtraServiceRepository dup;
	private PriceListRepository cp;
	private ReservationRepository rp;
	private RentalRepository ip;
	private SubscriptionRepository pp;

	private JPanel mainContent;

	public MainWindow(User loggedInUser, UserRepository kp, VehicleModelRepository mp, VehicleRepository vp,
			ExtraServiceRepository dup, PriceListRepository cp, ReservationRepository rp, RentalRepository ip, SubscriptionRepository pp) {

		this.loggedInUser = loggedInUser;
		this.kp = kp;
		this.mp = mp;
		this.vp = vp;
		this.dup = dup;
		this.cp = cp;
		this.rp = rp;
		this.ip = ip;
		this.pp = pp;

		setTitle("Car Rental Desktop — Signed in: " + loggedInUser.getFirstName() + " "
				+ loggedInUser.getLastName());
		setSize(900, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		mainContent = new JPanel(new BorderLayout());
		add(mainContent, BorderLayout.CENTER);

		JLabel welcomeLabel = new JLabel("Welcome u Rent-a-Car, " + loggedInUser.getFirstName(),
				SwingConstants.CENTER);
		welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
		mainContent.add(welcomeLabel, BorderLayout.CENTER);

		createMenu();
	}

	private void showPanel(JPanel newPanel) {
		mainContent.removeAll();
		mainContent.add(newPanel, BorderLayout.CENTER);
		mainContent.revalidate();
		mainContent.repaint();
	}

	private void createMenu() {
		JMenuBar menuBar = new JMenuBar();

		if (loggedInUser instanceof Administrator) {
			JMenu adminMenu = new JMenu("Administracija");

			JMenuItem usersItem = new JMenuItem("Employee");
			usersItem.addActionListener(e -> {
				UsersPanel kpPanel = new UsersPanel(kp, loggedInUser, false);
				showPanel(kpPanel);
			});
			adminMenu.add(usersItem);

			JMenuItem customeriItem = new JMenuItem("Customeri");
			customeriItem.addActionListener(e -> {
				UsersPanel kPanel = new UsersPanel(kp, loggedInUser, true);
				showPanel(kPanel);
			});
			adminMenu.add(customeriItem);

			JMenuItem subscriptionsItem = new JMenuItem("Subscriptions");
			subscriptionsItem.addActionListener(e -> {
				SubscriptionsPanel ppPanel = new SubscriptionsPanel(pp, loggedInUser);
				showPanel(ppPanel);
			});
			adminMenu.add(subscriptionsItem);

			JMenuItem vehiclesItem = new JMenuItem("Vozila");
			vehiclesItem.addActionListener(e -> {
				VehiclesPanel vpPanel = new VehiclesPanel(vp, mp, loggedInUser, rp);
				showPanel(vpPanel);
			});
			adminMenu.add(vehiclesItem);

			JMenuItem modeliItem = new JMenuItem("Modeli vehicles");
			modeliItem.addActionListener(e -> {
				VehicleModelsPanel mpPanel = new VehicleModelsPanel(mp);
				showPanel(mpPanel);
			});
			adminMenu.add(modeliItem);

			JMenuItem servicesItem = new JMenuItem("Extra services");
			servicesItem.addActionListener(e -> {
				ExtraServicesPanel upPanel = new ExtraServicesPanel(dup);
				showPanel(upPanel);
			});
			adminMenu.add(servicesItem);

			JMenuItem reservationsItem = new JMenuItem("Reservations");
			reservationsItem.addActionListener(e -> {
				ReservationsPanel rpPanel = new ReservationsPanel(rp, loggedInUser);
				showPanel(rpPanel);
			});
			adminMenu.add(reservationsItem);

			JMenuItem rentalsItem = new JMenuItem("Rentals");
			rentalsItem.addActionListener(e -> {
				AdminRentalsPanel ipPanel = new AdminRentalsPanel(ip);
				showPanel(ipPanel);
			});
			adminMenu.add(rentalsItem);

			menuBar.add(adminMenu);
			JMenuItem cenovniciItem = new JMenuItem("Cenovnici");
			cenovniciItem.addActionListener(e -> {
				PriceListPanel cpPanel = new PriceListPanel(cp, dup);
				showPanel(cpPanel);
			});
			adminMenu.add(cenovniciItem);

			JMenuItem reportsItem = new JMenuItem("Reports");
			reportsItem.addActionListener(e -> {
				ReportsPanel izp = new ReportsPanel(ip, rp, mp, kp, pp, cp);
				showPanel(izp);
			});
			adminMenu.add(reportsItem);

			JMenuItem chartsItem = new JMenuItem("Charts");
			chartsItem.addActionListener(e -> {
				ChartsPanel gp = new ChartsPanel(ip, rp, pp, kp);
				showPanel(gp);
			});
			adminMenu.add(chartsItem);

			JMenuItem settingsItem = new JMenuItem("Settings");
			settingsItem.addActionListener(e -> {
				Settings pod = new Settings();
				pod.load();
				SettingsPanel podPanel = new SettingsPanel(pod);
				showPanel(podPanel);
			});
			adminMenu.add(settingsItem);

		} else if (loggedInUser instanceof Agent) {
			JMenu agentMenu = new JMenu("Radna tabla (Agent)");

			JMenuItem customeriItem = new JMenuItem("Customeri");
			customeriItem.addActionListener(e -> {
				UsersPanel kpPanel = new UsersPanel(kp, loggedInUser, true);
				showPanel(kpPanel);
			});
			agentMenu.add(customeriItem);

			JMenuItem subscriptionsItem = new JMenuItem("Subscriptions");
			subscriptionsItem.addActionListener(e -> {
				SubscriptionsPanel ppPanel = new SubscriptionsPanel(pp, loggedInUser);
				showPanel(ppPanel);
			});
			agentMenu.add(subscriptionsItem);

			JMenuItem vehiclesItem = new JMenuItem("Vozila");
			vehiclesItem.addActionListener(e -> {
				VehiclesPanel vpPanel = new VehiclesPanel(vp, mp, loggedInUser, rp);
				showPanel(vpPanel);
			});
			agentMenu.add(vehiclesItem);

			menuBar.add(agentMenu);

			JMenuItem reservationsItem = new JMenuItem("Reservations");
			reservationsItem.addActionListener(e -> {
				ReservationsPanel rpPanel = new ReservationsPanel(rp, loggedInUser);
				showPanel(rpPanel);
			});
			agentMenu.add(reservationsItem);

			JMenuItem rentalItem = new JMenuItem("Rental Vozila");
			rentalItem.addActionListener(e -> {
				RentalPanel ipan = new RentalPanel(rp, ip, vp, dup, cp, (Agent) loggedInUser);
				showPanel(ipan);
			});
			agentMenu.add(rentalItem);

			JMenuItem returnItem = new JMenuItem("Return Vozila");
			returnItem.addActionListener(e -> {
				ReturnsPanel vpPanel = new ReturnsPanel(ip, rp, kp, cp);
				showPanel(vpPanel);
			});
			agentMenu.add(returnItem);



		} else if (loggedInUser instanceof Customer) {
			JMenu customerMenu = new JMenu("Customerski Portal");

			JMenuItem newReservationItem = new JMenuItem("New Reservation");
			newReservationItem.addActionListener(e -> {
				CustomerReservationPanel krp = new CustomerReservationPanel(rp, mp, vp, dup, cp, pp,
						(Customer) loggedInUser);
				showPanel(krp);
			});
			customerMenu.add(newReservationItem);

			JMenuItem myRezItem = new JMenuItem("My Reservations");
			myRezItem.addActionListener(e -> {
				CustomerMyReservationsPanel kmrp = new CustomerMyReservationsPanel(rp, kp, cp,
						(Customer) loggedInUser);
				showPanel(kmrp);
			});
			customerMenu.add(myRezItem);

			JMenuItem mojaSubscriptionItem = new JMenuItem("Moja Subscription");
			mojaSubscriptionItem.addActionListener(e -> {
				CustomerSubscriptionPanel kpp = new CustomerSubscriptionPanel(pp, (Customer) loggedInUser);
				showPanel(kpp);
			});
			customerMenu.add(mojaSubscriptionItem);

			menuBar.add(customerMenu);
		}

		JMenu optionsMenu = new JMenu("Opcije");
		JMenuItem odjavaItem = new JMenuItem("Odjavi se");
		odjavaItem.addActionListener(e -> {
			this.dispose();
			LoginWindow lp = new LoginWindow(kp, mp, vp, dup, cp, rp, ip, pp);
			lp.setVisible(true);
		});
		optionsMenu.add(odjavaItem);
		menuBar.add(optionsMenu);

		setJMenuBar(menuBar);
	}
}
