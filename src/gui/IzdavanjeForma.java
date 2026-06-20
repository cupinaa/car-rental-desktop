package gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import izdavanje.Izdavanje;
import podaci.IzdavanjePodaci;

public class IzdavanjeForma extends JDialog {

	private JTextField txtPocetna;
	private JTextField txtKrajnja;

	public IzdavanjeForma(IzdavanjePodaci ip, Izdavanje i, Runnable onSave) {
		setTitle("Izmeni Izdavanje");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(3, 2, 10, 10));

		add(new JLabel("Pocetna Kilometraza:"));
		txtPocetna = new JTextField(String.valueOf(i.getPocetnaKilometraza()));
		add(txtPocetna);

		add(new JLabel("Krajnja Kilometraza:"));
		txtKrajnja = new JTextField(String.valueOf(i.getKrajnjaKilometraza()));
		add(txtKrajnja);

		JButton btnSacuvaj = new JButton("Sacuvaj");
		JButton btnOdustani = new JButton("Odustani");

		add(btnSacuvaj);
		add(btnOdustani);

		btnOdustani.addActionListener(e -> dispose());

		btnSacuvaj.addActionListener(e -> {
			try {
				double poc = Double.parseDouble(txtPocetna.getText().trim());
				double kraj = Double.parseDouble(txtKrajnja.getText().trim());
				
				i.setPocetnaKilometraza(poc);
				i.setKrajnjaKilometraza(kraj);
				
				ip.upisi("izdavanja.csv");
				onSave.run();
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Neispravan broj ili greska.");
			}
		});
	}
}
