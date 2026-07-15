package gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import rental.Rental;
import data.RentalRepository;

public class RentalDialog extends JDialog {

	private JTextField txtPocetna;
	private JTextField txtEndnja;

	public RentalDialog(RentalRepository ip, Rental i, Runnable onSave) {
		setTitle("Edit Rental");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(3, 2, 10, 10));

		add(new JLabel("Pocetna Kilometraza:"));
		txtPocetna = new JTextField(String.valueOf(i.getStartingMileage()));
		add(txtPocetna);

		add(new JLabel("Endnja Kilometraza:"));
		txtEndnja = new JTextField(String.valueOf(i.getEndingMileage()));
		add(txtEndnja);

		JButton btnSave = new JButton("Save");
		JButton btnOdustani = new JButton("Odustani");

		add(btnSave);
		add(btnOdustani);

		btnOdustani.addActionListener(e -> dispose());

		btnSave.addActionListener(e -> {
			try {
				double poc = Double.parseDouble(txtPocetna.getText().trim());
				double end = Double.parseDouble(txtEndnja.getText().trim());

				i.setStartingMileage(poc);
				i.setEndingMileage(end);

				ip.write("rentals.csv");
				onSave.run();
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Invalid number.");
			}
		});
	}
}
