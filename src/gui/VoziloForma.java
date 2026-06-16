package gui;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import podaci.ModeliVozilaPodaci;
import podaci.VozilaPodaci;
import vozila.ModelVozila;
import vozila.StatusVozila;
import vozila.Vozilo;

public class VoziloForma extends JDialog {

	private VozilaPodaci vp;
	private Runnable naUspesnoDodavanje;
	private Vozilo vZaIzmenu;

	private JComboBox<ModelVozila> cbModel;
	private JTextField txtRegistracija;
	private JComboBox<StatusVozila> cbStatus;

	public VoziloForma(VozilaPodaci vp, ModeliVozilaPodaci mp, Vozilo vZaIzmenu, Runnable naUspesnoDodavanje) {
		this.vp = vp;
		this.naUspesnoDodavanje = naUspesnoDodavanje;
		this.vZaIzmenu = vZaIzmenu;

		setTitle(vZaIzmenu == null ? "Novo vozilo" : "Izmena vozila");
		setSize(400, 250);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(4, 2, 5, 5));

		add(new JLabel("Model vozila:"));
		cbModel = new JComboBox<>();
		for (ModelVozila m : mp.getModeli()) {
			cbModel.addItem(m); 
		}
		add(cbModel);

		add(new JLabel("Registarske tablice:"));
		txtRegistracija = new JTextField();
		add(txtRegistracija);

		add(new JLabel("Status vozila:"));
		cbStatus = new JComboBox<>(StatusVozila.values());
		add(cbStatus);

		JButton btnSacuvaj = new JButton("Sačuvaj");
		add(new JLabel("")); 
		add(btnSacuvaj);

		if (vZaIzmenu != null) {
			popuniPolja();
		}

		btnSacuvaj.addActionListener(e -> sacuvajVozilo());
	}

	private void popuniPolja() {
		txtRegistracija.setText(vZaIzmenu.getRegistarskeTablice());
		cbStatus.setSelectedItem(vZaIzmenu.getStatusVozila());



		cbModel.setSelectedItem(vZaIzmenu.getModelVozila());
	}

	private void sacuvajVozilo() {
		try {
			ModelVozila model = (ModelVozila) cbModel.getSelectedItem();
			String registracija = txtRegistracija.getText();
			StatusVozila status = (StatusVozila) cbStatus.getSelectedItem();

			if (model == null || registracija.trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Morate odabrati model i uneti registraciju!", "Greška", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (vZaIzmenu == null) {
				Vozilo v = new Vozilo(0, model, registracija, status);
				vp.dodajVozilo(v);
				JOptionPane.showMessageDialog(this, "Uspešno dodato!");
			} else {
				vZaIzmenu.setModelVozila(model);
				vZaIzmenu.setRegistarskeTablice(registracija);
				vZaIzmenu.setStatusVozila(status);
				vp.sacuvajIzmene();
				JOptionPane.showMessageDialog(this, "Uspešno izmenjeno!");
			}

			naUspesnoDodavanje.run();
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Greška prilikom čuvanja podataka.", "Greška", JOptionPane.ERROR_MESSAGE);
		}
	}
}
