package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import users.User;
import data.*;

public class LoginWindow extends JFrame {

	private UserRepository kp;
	private VehicleModelRepository mp;
	private VehicleRepository vp;
	private ExtraServiceRepository dup;
	private PriceListRepository cp;
	private ReservationRepository rp;
	private RentalRepository ip;
	private SubscriptionRepository pp;

	public LoginWindow(UserRepository kp, VehicleModelRepository mp, VehicleRepository vp, ExtraServiceRepository dup,
			PriceListRepository cp, ReservationRepository rp, RentalRepository ip, SubscriptionRepository pp) {

		this.kp = kp;
		this.mp = mp;
		this.vp = vp;
		this.dup = dup;
		this.cp = cp;
		this.rp = rp;
		this.ip = ip;
		this.pp = pp;

		setTitle("Prijava na systemManager");
		setSize(350, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setLayout(null);

		JLabel usernameLabel = new JLabel("E-mail address:");
		usernameLabel.setBounds(30, 30, 100, 25);
		add(usernameLabel);

		JTextField usernameField = new JTextField();
		usernameField.setBounds(140, 30, 150, 25);
		add(usernameField);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(30, 70, 100, 25);
		add(lblPassword);

		JPasswordField pfPassword = new JPasswordField();
		pfPassword.setBounds(140, 70, 150, 25);
		add(pfPassword);

		JButton btnPrijava = new JButton("Prijavi se");
		btnPrijava.setBounds(140, 110, 150, 30);
		add(btnPrijava);

		btnPrijava.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username = usernameField.getText().trim();
				String password = new String(pfPassword.getPassword());

				if (username.isEmpty() || password.isEmpty()) {
					JOptionPane.showMessageDialog(LoginWindow.this, "All fields are requirow!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				User loggedIn = kp.login(username, password);
				if (loggedIn != null) {
					JOptionPane.showMessageDialog(LoginWindow.this, "Welcome, " + loggedIn.getFirstName() + "!",
							"Signed in", JOptionPane.INFORMATION_MESSAGE);

					MainWindow glavni = new MainWindow(loggedIn, kp, mp, vp, dup, cp, rp, ip, pp);
					glavni.setVisible(true);
					LoginWindow.this.dispose();

				} else {
					JOptionPane.showMessageDialog(LoginWindow.this, "Incorrect email/username or password!", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}
