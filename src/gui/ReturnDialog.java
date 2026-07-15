package gui;

import java.awt.GridLayout;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import pricing.PriceList;
import rental.Rental;
import data.PriceListRepository;
import data.RentalRepository;
import data.UserRepository;
import data.ReservationRepository;

public class ReturnDialog extends JDialog {

	private RentalRepository ip;
	private ReservationRepository rp;
	private UserRepository kp;
	private PriceListRepository cp;
	private Rental rental;
	private Runnable onSuccessfulReturn;

	private JTextField txtKilometraza;
	private JTextField txtDateVracanja;

	public ReturnDialog(RentalRepository ip, ReservationRepository rp, UserRepository kp, PriceListRepository cp, Rental rental, Runnable onSuccessfulReturn) {
		this.ip = ip;
		this.rp = rp;
		this.kp = kp;
		this.cp = cp;
		this.rental = rental;
		this.onSuccessfulReturn = onSuccessfulReturn;

		setTitle("Return Vozila");
		setSize(350, 150);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(3, 2, 5, 5));

		add(new JLabel("Endnja mileage:"));
		txtKilometraza = new JTextField();
		add(txtKilometraza);

		add(new JLabel("Return date (YYYY-MM-DD):"));
		txtDateVracanja = new JTextField(LocalDate.now().toString());
		add(txtDateVracanja);

		JButton btnZavrsi = new JButton("Evidentiraj Return");
		add(new JLabel(""));
		add(btnZavrsi);

		btnZavrsi.addActionListener(e -> recordReturn());
	}

	private void recordReturn() {
		try {
			double endnja = Double.parseDouble(txtKilometraza.getText());
			if (endnja < rental.getStartingMileage()) {
				JOptionPane.showMessageDialog(this, "Ending mileage cannot be lower than starting mileage (" + rental.getStartingMileage() + ")!", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			LocalDate returnDate = LocalDate.parse(txtDateVracanja.getText());


			PriceList currentPriceList = cp.findVazeciPriceList(returnDate);


			ip.returnVehicle(rental, endnja, returnDate, rp, currentPriceList, kp);


			double finalPrice = rental.getReservation().getTotalPrice();
			JOptionPane.showMessageDialog(this, "Vehicle returned successfully!\nThe final amount due is: " + finalPrice + " RSD");

			onSuccessfulReturn.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Enter valid mileage and date (YYYY-MM-DD)!", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}
