package gui;

import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import podaci.ModeliVozilaPodaci;
import vozila.KategorijaVozila;
import vozila.ModelVozila;

public class ModelForma extends JDialog {

	private JTextField txtMarka;
	private JTextField txtNaziv;
	private JComboBox<KategorijaVozila> cbKategorija;

	public ModelForma(ModeliVozilaPodaci mp, ModelVozila m, Runnable onSave) {
		setTitle(m == null ? "Novi Model" : "Izmeni Model");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(4, 2, 10, 10));

		add(new JLabel("Marka:"));
		txtMarka = new JTextField(m == null ? "" : m.getMarkaVozila());
		add(txtMarka);

		add(new JLabel("Naziv Modela:"));
		txtNaziv = new JTextField(m == null ? "" : m.getNazivModela());
		add(txtNaziv);

		add(new JLabel("Kategorija:"));
		cbKategorija = new JComboBox<>(KategorijaVozila.values());
		if (m != null) cbKategorija.setSelectedItem(m.getKategorija());
		add(cbKategorija);

		JButton btnSacuvaj = new JButton("Sacuvaj");
		JButton btnOdustani = new JButton("Odustani");
		add(btnSacuvaj);
		add(btnOdustani);

		btnOdustani.addActionListener(e -> dispose());

		btnSacuvaj.addActionListener(e -> {
			if (txtMarka.getText().isEmpty() || txtNaziv.getText().isEmpty()) {
				JOptionPane.showMessageDialog(this, "Sva polja su obavezna!");
				return;
			}
			
			if (m != null) {
				m.setMarkaVozila(txtMarka.getText());
				m.setNazivModela(txtNaziv.getText());
				m.setKategorija((KategorijaVozila) cbKategorija.getSelectedItem());
			} else {
				ModelVozila novi = new ModelVozila(mp.getModeli().stream().mapToInt(x -> x.getId()).max().orElse(0) + 1, txtMarka.getText(), txtNaziv.getText(), (KategorijaVozila) cbKategorija.getSelectedItem());
				mp.getModeli().add(novi);
			}

			try {
				mp.upisi("modeli.csv");
				onSave.run();
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Greska pri cuvanju!");
			}
		});
	}
}
