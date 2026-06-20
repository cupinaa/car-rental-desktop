package gui;

import java.awt.GridLayout;
import java.time.LocalDate;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import podaci.RezervacijePodaci;
import rezervacija.Rezervacija;
import rezervacija.StatusRezervacije;

public class RezervacijaForma extends JDialog {

	private JTextField txtDatumOd;
	private JTextField txtDatumDo;
	private JComboBox<StatusRezervacije> cbStatus;

	public RezervacijaForma(RezervacijePodaci rp, Rezervacija r, Runnable onSave) {
		setTitle("Izmeni Rezervaciju");
		setSize(300, 200);
		setLocationRelativeTo(null);
		setModal(true);
		setLayout(new GridLayout(4, 2, 10, 10));

		add(new JLabel("Datum Od (YYYY-MM-DD):"));
		txtDatumOd = new JTextField(r.getDatumPocetka().toString());
		add(txtDatumOd);

		add(new JLabel("Datum Do (YYYY-MM-DD):"));
		txtDatumDo = new JTextField(r.getDatumKraja().toString());
		add(txtDatumDo);

		add(new JLabel("Status:"));
		cbStatus = new JComboBox<>(StatusRezervacije.values());
		cbStatus.setSelectedItem(r.getStatusRezervacije());
		add(cbStatus);

		JButton btnSacuvaj = new JButton("Sacuvaj");
		JButton btnOdustani = new JButton("Odustani");

		add(btnSacuvaj);
		add(btnOdustani);

		btnOdustani.addActionListener(e -> dispose());

		btnSacuvaj.addActionListener(e -> {
			try {
				LocalDate datumOd = LocalDate.parse(txtDatumOd.getText().trim());
				LocalDate datumDo = LocalDate.parse(txtDatumDo.getText().trim());
				StatusRezervacije status = (StatusRezervacije) cbStatus.getSelectedItem();
				
				r.setDatumPocetka(datumOd);
				r.setDatumKraja(datumDo);
				r.setStatusRezervacije(status);
				
				rp.upisi("rezervacije.csv");
				onSave.run();
				dispose();
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "Neispravan datum ili greska.");
			}
		});
	}
}
